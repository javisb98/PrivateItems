package me.javisb98.privateitems.commands;

import me.javisb98.privateitems.PrivateItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class UnmakePrivate implements CommandExecutor {

    NamespacedKey clave = new NamespacedKey(PrivateItems.getPlugin(), "privateitemsowner");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player p) {

            if((!p.isOp()) && (!p.hasPermission("privateitems.unmakeprivate"))) {
                p.sendMessage(ChatColor.RED + "You don't have permission to perform this command");
                return true;
            }

            ItemStack item = p.getInventory().getItemInMainHand();
            if(item.getType() == Material.AIR) {
                p.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("no_item_in_hand"));
                return true;
            }
            ItemMeta meta = item.getItemMeta();
            if(meta == null) {
                p.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("not_private_message"));
                return true;
            }
            PersistentDataContainer data = meta.getPersistentDataContainer();

            if(data.has(clave, PersistentDataType.STRING)) {
                p.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("removed_private_message"));
                data.remove(clave);
                item.setItemMeta(meta);
            } else {
                p.sendMessage(ChatColor.AQUA + PrivateItems.getPlugin().getConfig().getString("not_private_message"));
            }
        }
        return true;
    }
}
