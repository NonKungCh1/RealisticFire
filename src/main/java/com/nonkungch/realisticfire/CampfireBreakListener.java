// /src/main/java/com/nonkungch/realisticfire/CampfireBreakListener.java

package com.nonkungch.realisticfire;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class CampfireBreakListener implements Listener {

    private final RealisticFire plugin;

    public CampfireBreakListener(RealisticFire plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material type = block.getType();

        if (type == Material.CAMPFIRE || type == Material.SOUL_CAMPFIRE) {
            Location loc = block.getLocation();
            // ถ้ากองไฟนี้มีข้อมูลอยู่ในระบบของเรา
            if (plugin.getCampfireManagers().containsKey(loc)) {
                CampfireManager manager = plugin.getCampfireManagers().get(loc);
                
                // วนลูปเอาไอเทมทั้งหมดใน GUI ออกมาดรอป
                for (ItemStack item : manager.getInventory().getContents()) {
                    if (item != null && item.getType() != Material.BLACK_STAINED_GLASS_PANE) {
                        block.getWorld().dropItemNaturally(loc, item);
                    }
                }
                
                // ลบข้อมูลของกองไฟนี้ออกจากระบบ
                plugin.getCampfireManagers().remove(loc);
            }
        }
    }
}
