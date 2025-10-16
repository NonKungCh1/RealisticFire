// /src/main/java/com/nonkungch/realisticfire/CampfireInteractListener.java

package com.nonkungch.realisticfire;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CampfireInteractListener implements Listener {

    private final RealisticFire plugin;

    public CampfireInteractListener(RealisticFire plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        Material type = clickedBlock.getType();
        if (type == Material.CAMPFIRE || type == Material.SOUL_CAMPFIRE) {
            // เช็คว่ากองไฟติดไฟอยู่หรือไม่
            Lightable lightable = (Lightable) clickedBlock.getBlockData();
            if (!lightable.isLit()) return;

            event.setCancelled(true); // ป้องกันไม่ให้เมนูเดิมของเกมทำงาน

            Location loc = clickedBlock.getLocation();
            // หา Manager ของกองไฟนี้ ถ้าไม่มีก็สร้างใหม่
            CampfireManager manager = plugin.getCampfireManagers().computeIfAbsent(loc, k -> new CampfireManager());
            manager.openInventory(event.getPlayer());
        }
    }
}
