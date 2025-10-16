
package com.nonkungch.realisticfire;

import org.bukkit.plugin.java.JavaPlugin;

public class RealisticFire extends JavaPlugin {

    @Override
    public void onEnable() {
        // ลงทะเบียน Listener ของเรา
        getServer().getPluginManager().registerEvents(new FireExtinguishListener(), this);
        getLogger().info("RealisticFire Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RealisticFire Plugin has been disabled!");
    }
}
