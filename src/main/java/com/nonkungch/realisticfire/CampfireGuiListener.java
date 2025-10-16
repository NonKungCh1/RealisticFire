// /src/main/java/com/nonkungch/realisticfire/CampfireGuiListener.java

package com.nonkungch.realisticfire;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CampfireGuiListener implements Listener {

    private final RealisticFire plugin;

    public CampfireGuiListener(RealisticFire plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§c§lกองไฟย่างอาหาร")) {
            return;
        }

        // กันไม่ให้คลิกที่ช่องกระจกดำ
        if (event.getCurrentItem() != null && event.getCurrentItem().getType().toString().contains("STAINED_GLASS_PANE")) {
            event.setCancelled(true);
        }
    }
}
