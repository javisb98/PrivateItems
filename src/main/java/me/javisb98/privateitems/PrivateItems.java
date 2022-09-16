package me.javisb98.privateitems;

import me.javisb98.privateitems.commands.MakePrivate;
import me.javisb98.privateitems.commands.Reload;
import me.javisb98.privateitems.commands.SeeOwner;
import me.javisb98.privateitems.commands.UnmakePrivate;
import me.javisb98.privateitems.listeners.PickItem;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrivateItems extends JavaPlugin {

    public static PrivateItems getPlugin() {
        return plugin;
    }

    private static PrivateItems plugin;

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        System.out.println("################################################");
        System.out.println("############# STARTING PRIVATEITEMS ############");
        System.out.println("################################################");

        plugin = this;
        getCommand("makeprivate").setExecutor(new MakePrivate());
        getCommand("unmakeprivate").setExecutor(new UnmakePrivate());
        getCommand("seeowner").setExecutor(new SeeOwner());
        getCommand("reload").setExecutor(new Reload());
        getServer().getPluginManager().registerEvents(new PickItem(), this);
    }

}
