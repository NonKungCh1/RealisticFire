
package com.nonkungch.realisticfire;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class FireExtinguishListener implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        // ตรวจสอบว่าสภาพอากาศกำลังจะเปลี่ยนเป็นฝนตกหรือไม่
        if (!event.toWeatherState()) {
            return; // ถ้าฝนไม่ได้ตก ก็ไม่ต้องทำอะไร
        }

        World world = event.getWorld();

        // ค้นหา Chunks ที่โหลดอยู่ทั้งหมดใน World นั้น
        for (org.bukkit.Chunk chunk : world.getLoadedChunks()) {
            // วนลูปหา TileEntities ทั้งหมดใน Chunk (เตาหลอม, กองไฟ เป็น TileEntity)
            for (org.bukkit.block.BlockState state : chunk.getTileEntities()) {
                Block block = state.getBlock();

                // ตรวจสอบว่าบล็อกนั้นๆ สามารถโดนฝนได้หรือไม่
                // (getHighestBlockAt จะได้บล็อกที่อยู่สูงสุดที่ฝนจะตกถึง)
                if (world.getHighestBlockAt(block.getLocation()).getY() > block.getY()) {
                    continue; // ถ้าบล็อกมีหลังคาอยู่ ก็ข้ามไป
                }
                
                // ตรวจสอบชนิดของบล็อกและทำการดับไฟ
                Material type = state.getType();
                if (type == Material.CAMPFIRE || type == Material.SOUL_CAMPFIRE) {
                    Campfire campfire = (Campfire) state.getBlockData();
                    if (campfire.isLit()) {
                        campfire.setLit(false); // ดับไฟกองไฟ
                        state.setBlockData(campfire);
                        state.update();
                    }
                } else if (type == Material.FURNACE || type == Material.BLAST_FURNACE || type == Material.SMOKER) {
                    Furnace furnace = (Furnace) state.getBlockData();
                    if (furnace.isLit()) {
                        furnace.setLit(false); // ดับไฟเตาหลอม
                        state.setBlockData(furnace);
                        state.update();
                    }
                }
            }
        }
    }
}
