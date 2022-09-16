package me.javisb98.privateitems.listeners;

import me.javisb98.privateitems.PrivateItems;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class PickItem implements Listener {

    private NamespacedKey clave = new NamespacedKey(PrivateItems.getPlugin(), "privateitemsowner");
    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void onPick(EntityPickupItemEvent e) {

        ItemStack item = e.getItem().getItemStack();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        // Si el item es privado
        if (data.has(clave, PersistentDataType.STRING)) {
            // Si quien recoge el item es un jugador
            if (e.getEntity() instanceof Player p) {
                if(p.hasPermission("privateitems.ignore")) return;
                String nombre = p.getName();
                // Si el item no tenía ningún jugador asociado
                if (data.get(clave, PersistentDataType.STRING).equals(" ")) {
                    data.set(clave, PersistentDataType.STRING, nombre);
                    item.setItemMeta(meta);
                    if (PrivateItems.getPlugin().getConfig().getBoolean("send_attach_item_message")) p.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("attach_item_message"));
                    // Si el item tiene algún jugador asociado
                } else {
                    // Si el jugador es dueño de ese item
                    if (data.get(clave, PersistentDataType.STRING).equals(nombre)) {
                        if (PrivateItems.getPlugin().getConfig().getBoolean("send_player_attached_item_pick_message")) p.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("player_attached_item_pick_message"));
                        return;
                    }
                    // Si no es dueño
                    else {
                        if(PrivateItems.getPlugin().getConfig().getBoolean("send_player_non_attached_item_pick_message")) {
                            if (!cooldown.containsKey(p.getUniqueId())) {
                                p.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("player_non_attached_item_pick_message").replace("%player%", data.get(clave, PersistentDataType.STRING)));
                                cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                            } else {
                                long segundos = (PrivateItems.getPlugin().getConfig().getLong("player_non_attached_item_pick_message_cooldown")) * 1000;
                                if (System.currentTimeMillis() - cooldown.get(p.getUniqueId()) > segundos) {
                                    p.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("player_non_attached_item_pick_message").replace("%player%", data.get(clave, PersistentDataType.STRING)));
                                    cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                                }
                            }
                        }
                        e.setCancelled(true);
                    }
                }
            }
            // Si no es un jugador no le dejamos coger el item directamente
            else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDisp(BlockDispenseArmorEvent e) {
        ItemStack item = e.getItem();
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;
        PersistentDataContainer data = meta.getPersistentDataContainer();

        // Si el item es privado
        if (data.has(new NamespacedKey(PrivateItems.getPlugin(), "privateitemsowner"), PersistentDataType.STRING)) {

            if(e.getTargetEntity() instanceof Player jugador) {
                String nombre = jugador.getName();
                if(jugador.hasPermission("privateitems.ignore")) return;
                // Si el item no tenía ningún jugador asociado
                if (data.get(clave, PersistentDataType.STRING).equals(" ")) {
                    data.set(clave, PersistentDataType.STRING, nombre);
                    item.setItemMeta(meta);
                    if (PrivateItems.getPlugin().getConfig().getBoolean("send_attach_item_message")) jugador.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("attach_item_message"));
                    // Si el item tiene algún jugador asociado
                } else {
                    // Si el jugador es dueño de ese item
                    if (data.get(clave, PersistentDataType.STRING).equals(nombre)) {
                        if (PrivateItems.getPlugin().getConfig().getBoolean("send_player_attached_item_pick_message")) jugador.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("player_attached_item_pick_message"));
                        return;
                    }
                    // Si no es dueño
                    else {
                        if (PrivateItems.getPlugin().getConfig().getBoolean("send_player_non_attached_item_pick_message")) jugador.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("player_non_attached_item_pick_message").replace("%player%", data.get(clave, PersistentDataType.STRING)));
                        e.setCancelled(true);
                    }
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInv(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;
        PersistentDataContainer data = meta.getPersistentDataContainer();

        // Si el item es privado
        if (data.has(new NamespacedKey(PrivateItems.getPlugin(), "privateitemsowner"), PersistentDataType.STRING)) {
            if(e.getWhoClicked() instanceof Player jugador) {
                if(jugador.hasPermission("privateitems.ignore")) return;
                String nombre = jugador.getName();
                // Si el item no tenía ningún jugador asociado
                if (data.get(clave, PersistentDataType.STRING).equals(" ")) {
                    data.set(clave, PersistentDataType.STRING, nombre);
                    item.setItemMeta(meta);
                    if (PrivateItems.getPlugin().getConfig().getBoolean("send_attach_item_message")) jugador.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("attach_item_message"));
                    // Si el item tiene algún jugador asociado
                } else {
                    // Si el jugador es dueño de ese item
                    if (data.get(clave, PersistentDataType.STRING).equals(nombre)) {
                        if (PrivateItems.getPlugin().getConfig().getBoolean("send_player_attached_item_pick_message")) jugador.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("player_attached_item_pick_message"));
                        return;
                    }
                    // Si no es dueño
                    else {
                        if (PrivateItems.getPlugin().getConfig().getBoolean("send_player_non_attached_item_pick_message")) jugador.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("player_non_attached_item_pick_message").replace("%player%", data.get(clave, PersistentDataType.STRING)));
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
