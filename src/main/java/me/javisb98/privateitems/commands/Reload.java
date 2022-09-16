package me.javisb98.privateitems.commands;

import me.javisb98.privateitems.PrivateItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player p) {
            if(!p.isOp()) {
                p.sendMessage(ChatColor.RED + "You don't have permission to perform this command");
            } else {
                PrivateItems.getPlugin().reloadConfig();
                p.sendMessage(ChatColor.AQUA + "PrivateItems config reloaded");
            }
        } else {
            PrivateItems.getPlugin().reloadConfig();
            System.out.println(ChatColor.AQUA + "PrivateItems config reloaded");
        }
        return true;
    }
}
