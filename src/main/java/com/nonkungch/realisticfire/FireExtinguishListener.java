// /src/main/java/com/nonkungch/realisticfire/FireExtinguishListener.java

package com.nonkungch.realisticfire;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Lightable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class FireExtinguishListener implements Listener {

    private final RealisticFire plugin;
    private BukkitTask rainTask = null; // ตัวแปรสำหรับเก็บ Task ของเรา

    // Constructor เพื่อรับ instance ของปลั๊กอิน
    public FireExtinguishListener(RealisticFire plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();

        // 1. ถ้าสภาพอากาศกำลังจะเปลี่ยนเป็นฝนตก
        if (event.toWeatherState()) {
            // สร้าง Task ให้ทำงานซ้ำๆ ทุก 2 วินาที (40 ticks)
            rainTask = new BukkitRunnable() {
                @Override
                public void run() {
                    // ถ้าฝนหยุดตกกลางคัน ให้ยกเลิก Task นี้
                    if (!world.hasStorm()) {
                        this.cancel();
                        return;
                    }
                    extinguishFiresInWorld(world);
                }
            }.runTaskTimer(plugin, 0L, 40L); // เริ่มทันที และทำซ้ำทุก 2 วินาที

        // 2. ถ้าฝนกำลังจะหยุดตก
        } else {
            // ยกเลิก Task ที่กำลังทำงานอยู่
            if (rainTask != null) {
                rainTask.cancel();
                rainTask = null;
            }
        }
    }

    // Method ที่จะค้นหาและดับไฟ
    private void extinguishFiresInWorld(World world) {
        for (org.bukkit.Chunk chunk : world.getLoadedChunks()) {
            for (BlockState state : chunk.getTileEntities()) {
                Block block = state.getBlock();

                // ตรวจสอบว่าบล็อกมีหลังคาบังหรือไม่
                if (world.getHighestBlockAt(block.getLocation()).getY() > block.getY()) {
                    continue; // ถ้ามีหลังคา ให้ข้ามไป
                }

                // ตรวจสอบว่าบล็อกนั้นเป็นสิ่งที่ "จุดไฟได้" หรือไม่ (ครอบคลุมทั้งเตาเผาและกองไฟ)
                if (state.getBlockData() instanceof Lightable) {
                    Lightable lightable = (Lightable) state.getBlockData();

                    // ถ้ามันกำลังติดไฟอยู่ ให้ดับไฟ
                    if (lightable.isLit()) {
                        lightable.setLit(false);
                        state.setBlockData(lightable);
                        state.update(true, false); // สั่งให้อัปเดตบล็อกในเกม
                    }
                }
            }
        }
    }
}
