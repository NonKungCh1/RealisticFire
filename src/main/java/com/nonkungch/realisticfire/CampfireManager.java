// /src/main/java/com/nonkungch/realisticfire/CampfireManager.java

package com.nonkungch.realisticfire;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmokingRecipe;

import java.util.Iterator;

public class CampfireManager {

    private final Inventory inventory;
    private final int[] cookingProgress = new int[6]; // เก็บความคืบหน้าการเผาของแต่ละช่อง
    private final int[] cookingTime = new int[6];     // เก็บเวลาที่ต้องใช้ในการเผาของแต่ละช่อง
    private static final int COOK_TIME_SECONDS = 10;  // อาหารจะสุกใน 10 วินาที

    public CampfireManager() {
        this.inventory = Bukkit.createInventory(null, 27, "§c§lกองไฟย่างอาหาร");
        setupInventory();
    }

    // ตั้งค่าหน้าตาของ GUI
    private void setupInventory() {
        ItemStack placeholder = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 6; i < 27; i++) {
            if (i % 9 < 1 || i % 9 > 7 || i < 10 || i > 16) {
                inventory.setItem(i, placeholder);
            }
        }
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }
    
    public Inventory getInventory() {
        return inventory;
    }

    // Method นี้จะถูกเรียกทุกวินาทีโดย Task หลัก
    public void tick() {
        for (int i = 0; i < 6; i++) {
            ItemStack item = inventory.getItem(i);

            // ถ้าไม่มีไอเทม หรือเป็นอาหารที่สุกแล้ว ก็ข้ามไป
            if (item == null || item.getType() == Material.AIR) {
                cookingProgress[i] = 0;
                continue;
            }

            SmokingRecipe recipe = getSmokingRecipe(item);
            if (recipe == null) {
                cookingProgress[i] = 0;
                continue; // ถ้าเผาไม่ได้ ก็ไม่ต้องทำอะไร
            }

            // ถ้าเป็นไอเทมใหม่ที่เพิ่งใส่เข้ามา
            if (cookingTime[i] == 0) {
                 cookingTime[i] = recipe.getCookingTime() / 2; // ย่างในกองไฟจะเร็วกว่าเตาปกติ
            }

            cookingProgress[i]++; // เพิ่มความคืบหน้า

            // ถ้าเผาเสร็จแล้ว
            if (cookingProgress[i] >= (cookingTime[i] / 20) ) { // หาร 20 เพราะเรา tick ทุก 20 tick
                ItemStack result = recipe.getResult();
                item.setAmount(item.getAmount() -1);
                inventory.setItem(i, item); // ลดจำนวนของดิบ
                
                // หาช่องว่างเพื่อใส่ของสุก
                 inventory.addItem(result);


                cookingProgress[i] = 0;
                cookingTime[i] = 0;
            }
        }
    }

    // หา Recipe การเผาของไอเทม
    private SmokingRecipe getSmokingRecipe(ItemStack item) {
        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (recipe instanceof SmokingRecipe) {
                SmokingRecipe smokingRecipe = (SmokingRecipe) recipe;
                if (smokingRecipe.getInput().isSimilar(item)) {
                    return smokingRecipe;
                }
            }
        }
        return null;
    }
}
