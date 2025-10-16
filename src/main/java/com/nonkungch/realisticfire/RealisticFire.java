// /src/main/java/com/nonkungch/realisticfire/RealisticFire.java

package com.nonkungch.realisticfire;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class RealisticFire extends JavaPlugin {

    // Map สำหรับเก็บข้อมูลกองไฟแต่ละอันที่ผู้เล่นเคยเปิด
    private final Map<Location, CampfireManager> campfireManagers = new HashMap<>();

    @Override
    public void onEnable() {
        // ลงทะเบียน Listener ทั้งหมด
        getServer().getPluginManager().registerEvents(new FireExtinguishListener(this), this);
        getServer().getPluginManager().registerEvents(new CampfireInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new CampfireGuiListener(this), this);
        getServer().getPluginManager().registerEvents(new CampfireBreakListener(this), this);

        // เริ่ม Task ที่จะทำงานทุก 1 วินาที เพื่ออัปเดตการเผาอาหาร
        startCookingTask();

        getLogger().info("RealisticFire Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RealisticFire Plugin has been disabled!");
    }

    // Method ให้คลาสอื่นเรียกใช้ Map นี้ได้
    public Map<Location, CampfireManager> getCampfireManagers() {
        return campfireManagers;
    }

    private void startCookingTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // วนลูป CampfireManager ทั้งหมดที่ทำงานอยู่ แล้วสั่งให้มันอัปเดตสถานะ
                for (CampfireManager manager : campfireManagers.values()) {
                    manager.tick();
                }
            }
        }.runTaskTimer(this, 20L, 20L); // ทำงานทุก 20 ticks (1 วินาที)
    }
}
