package me.snazzy.battleroyale;

import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Loot implements Listener, CommandExecutor {
    ArrayList<ItemStack> chestItems = new ArrayList<ItemStack>();
    ArrayList<ItemStack> barrelItems = new ArrayList<ItemStack>();
    int barrelLootChance = 50;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("refill") && sender.hasPermission("refill.use")) {
            refill();
        }
        return true;
    }

    public void refill() {
        int chestCount = 0;
        int barrelCount = 0;
        setLootTables();

        int x = 25;
        int y = 25;

        while (x >= -25) {
            while (y >= -25) {
                Chunk chunk = Bukkit.getServer().getWorld("world").getChunkAt(x, y);
                chunk.load();
                BlockState[] containers = chunk.getTileEntities();
                for (BlockState container : containers) {
                    if (container.getType().equals(Material.CHEST)) {
                        Chest chest = (Chest) container;
                        Inventory chestInv = chest.getInventory();
                        chestInv.clear();
                        addChestItem(chestInv);
                        chestCount++;
                    } else if (container.getType().equals(Material.BARREL)) {
                        Barrel barrel = (Barrel) container;
                        Inventory barrelInv = barrel.getInventory();
                        barrelInv.clear();
                        if ((ThreadLocalRandom.current().nextInt(100) + 1) < barrelLootChance) {
                            addBarrelItem(barrelInv);
                            barrelCount++;
                        }
                    }
                }
                y--;
            }
            x--;
            y = 25;
        }
        System.out.println("Loot refilled.");
    }

    public void setLootTables() {
        //Potions
        //Potion 1
        ItemStack potion1 = new ItemStack(Material.POTION);
        PotionMeta meta1 = (PotionMeta) potion1.getItemMeta();
        meta1.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2), true);
        meta1.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 200, 2), true);
        meta1.setDisplayName("Agility Potion");
        meta1.setColor(Color.AQUA);
        potion1.setItemMeta(meta1);
        //Potion 2
        ItemStack potion2 = new ItemStack(Material.POTION);
        PotionMeta meta2 = (PotionMeta) potion2.getItemMeta();
        meta2.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 600, 0), true);
        meta2.setDisplayName("Speed Potion");
        meta2.setColor(Color.SILVER);
        potion2.setItemMeta(meta2);
        //Potion 3
        ItemStack potion3 = new ItemStack(Material.POTION);
        PotionMeta meta3 = (PotionMeta) potion3.getItemMeta();
        meta3.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 0), true);
        meta3.setDisplayName("Strength Potion");
        meta3.setColor(Color.MAROON);
        potion3.setItemMeta(meta3);
        //Potion 4
        ItemStack potion4 = new ItemStack(Material.POTION);
        PotionMeta meta4 = (PotionMeta) potion4.getItemMeta();
        meta4.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 0, 1), true);
        meta4.setDisplayName("Healing II Potion");
        meta4.setColor(Color.RED);
        potion4.setItemMeta(meta4);
        //Potion 5
        ItemStack potion5 = new ItemStack(Material.POTION);
        PotionMeta meta5 = (PotionMeta) potion5.getItemMeta();
        meta5.addCustomEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 600, 4), true);
        meta5.setDisplayName("Health Boost");
        meta5.setColor(Color.YELLOW);
        potion5.setItemMeta(meta5);
        //Potion 6
        ItemStack potion6 = new ItemStack(Material.POTION);
        PotionMeta meta6 = (PotionMeta) potion6.getItemMeta();
        meta6.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 300, 1), true);
        meta6.setDisplayName("Speed II Potion");
        meta6.setColor(Color.SILVER);
        potion6.setItemMeta(meta6);
        //Potion 7
        ItemStack potion7 = new ItemStack(Material.POTION);
        PotionMeta meta7 = (PotionMeta) potion7.getItemMeta();
        meta7.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 1), true);
        meta7.setDisplayName("Strength II Potion");
        meta7.setColor(Color.MAROON);
        potion7.setItemMeta(meta7);
        //Potion 8
        ItemStack potion8 = new ItemStack(Material.POTION);
        PotionMeta meta8 = (PotionMeta) potion8.getItemMeta();
        meta8.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 0, 0), true);
        meta8.setDisplayName("Healing Potion");
        meta8.setColor(Color.RED);
        potion8.setItemMeta(meta8);
        //Potion 9
        ItemStack potion9 = new ItemStack(Material.POTION);
        PotionMeta meta9 = (PotionMeta) potion9.getItemMeta();
        meta9.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300, 0), true);
        meta9.setDisplayName("Armor Potion");
        meta9.setColor(Color.GRAY);
        potion9.setItemMeta(meta9);
        //Potion 10
        ItemStack potion10 = new ItemStack(Material.POTION);
        PotionMeta meta10 = (PotionMeta) potion10.getItemMeta();
        meta10.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 160, 1), true);
        meta10.setDisplayName("Armor II Potion");
        meta10.setColor(Color.GRAY);
        potion10.setItemMeta(meta10);

        //Enchanted Books
        //Book 1
        ItemStack book1 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta1 = (EnchantmentStorageMeta) book1.getItemMeta();
        bMeta1.setDisplayName("Fire Aspect I");
        bMeta1.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
        book1.setItemMeta(bMeta1);
        //Book 2
        ItemStack book2 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta2 = (EnchantmentStorageMeta) book2.getItemMeta();
        bMeta2.setDisplayName("Sharpness I");
        bMeta2.addStoredEnchant(Enchantment.DAMAGE_ALL, 1, true);
        book2.setItemMeta(bMeta2);
        //Book 3
        ItemStack book3 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta3 = (EnchantmentStorageMeta) book3.getItemMeta();
        bMeta3.setDisplayName("Sharpness II");
        bMeta3.addStoredEnchant(Enchantment.DAMAGE_ALL, 2, true);
        book3.setItemMeta(bMeta3);
        //Book 4
        ItemStack book4 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta4 = (EnchantmentStorageMeta) book4.getItemMeta();
        bMeta4.setDisplayName("Sharpness III");
        bMeta4.addStoredEnchant(Enchantment.DAMAGE_ALL, 3, true);
        book4.setItemMeta(bMeta4);
        //Book 5
        ItemStack book5 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta5 = (EnchantmentStorageMeta) book5.getItemMeta();
        bMeta5.setDisplayName("Knockback I");
        bMeta5.addStoredEnchant(Enchantment.KNOCKBACK, 1, true);
        book5.setItemMeta(bMeta5);
        //Book 6
        ItemStack book6 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta6 = (EnchantmentStorageMeta) book6.getItemMeta();
        bMeta6.setDisplayName("Power I");
        bMeta6.addStoredEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        book6.setItemMeta(bMeta6);
        //Book 7
        ItemStack book7 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta7 = (EnchantmentStorageMeta) book7.getItemMeta();
        bMeta7.setDisplayName("Power II");
        bMeta7.addStoredEnchant(Enchantment.ARROW_DAMAGE, 2, true);
        book7.setItemMeta(bMeta7);
        //Book 8
        ItemStack book8 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta8 = (EnchantmentStorageMeta) book8.getItemMeta();
        bMeta8.setDisplayName("Power III");
        bMeta8.addStoredEnchant(Enchantment.ARROW_DAMAGE, 3, true);
        book8.setItemMeta(bMeta8);
        //Book 9
        ItemStack book9 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta9 = (EnchantmentStorageMeta) book9.getItemMeta();
        bMeta9.setDisplayName("Punch I");
        bMeta9.addStoredEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
        book9.setItemMeta(bMeta9);
        //Book 10
        ItemStack book10 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta10 = (EnchantmentStorageMeta) book10.getItemMeta();
        bMeta10.setDisplayName("Flame I");
        bMeta10.addStoredEnchant(Enchantment.ARROW_FIRE, 1, true);
        book10.setItemMeta(bMeta10);
        //Book 11
        ItemStack book11 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta11 = (EnchantmentStorageMeta) book11.getItemMeta();
        bMeta11.setDisplayName("Quick Charge I");
        bMeta11.addStoredEnchant(Enchantment.QUICK_CHARGE, 1, true);
        book11.setItemMeta(bMeta11);
        //Book 12
        ItemStack book12 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta12 = (EnchantmentStorageMeta) book12.getItemMeta();
        bMeta12.setDisplayName("Quick Charge II");
        bMeta12.addStoredEnchant(Enchantment.QUICK_CHARGE, 2, true);
        book12.setItemMeta(bMeta12);
        //Book 13
        ItemStack book13 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta13 = (EnchantmentStorageMeta) book13.getItemMeta();
        bMeta13.setDisplayName("Quick Charge III");
        bMeta13.addStoredEnchant(Enchantment.QUICK_CHARGE, 3, true);
        book13.setItemMeta(bMeta13);
        //Book 14
        ItemStack book14 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta14 = (EnchantmentStorageMeta) book14.getItemMeta();
        bMeta14.setDisplayName("Infinity I");
        bMeta14.addStoredEnchant(Enchantment.ARROW_INFINITE, 1, true);
        book14.setItemMeta(bMeta14);
        //Book 15
        ItemStack book15 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta15 = (EnchantmentStorageMeta) book15.getItemMeta();
        bMeta15.setDisplayName("Thorns I");
        bMeta15.addStoredEnchant(Enchantment.THORNS, 1, true);
        book15.setItemMeta(bMeta15);
        //Book 16
        ItemStack book16 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta16 = (EnchantmentStorageMeta) book16.getItemMeta();
        bMeta16.setDisplayName("Protection I");
        bMeta16.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        book16.setItemMeta(bMeta16);
        //Book 17
        ItemStack book17 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta17 = (EnchantmentStorageMeta) book17.getItemMeta();
        bMeta17.setDisplayName("Protection II");
        bMeta17.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
        book17.setItemMeta(bMeta17);
        //Book 18
        ItemStack book18 = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bMeta18 = (EnchantmentStorageMeta) book18.getItemMeta();
        bMeta18.setDisplayName("Protection III");
        bMeta18.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
        book18.setItemMeta(bMeta18);

        //Sniper
        ItemStack sniper = new ItemStack(Material.CROSSBOW, 1, (short) 464);
        ItemMeta sniperMeta = sniper.getItemMeta();
        sniperMeta.setLocalizedName("Sniper");
        sniperMeta.setDisplayName("Sniper");
        sniper.setItemMeta(sniperMeta);

        //Chest loot table
        chestItems.add(new ItemStack(Material.IRON_SWORD, 1));
        chestItems.add(new ItemStack(Material.DIAMOND_SWORD, 1));
        chestItems.add(new ItemStack(Material.ENDER_PEARL, 1));
        chestItems.add(new ItemStack(Material.ENDER_PEARL, 1));
        chestItems.add(new ItemStack(Material.IRON_HELMET, 1));
        chestItems.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
        chestItems.add(new ItemStack(Material.IRON_LEGGINGS, 1));
        chestItems.add(new ItemStack(Material.IRON_BOOTS, 1));
        chestItems.add(new ItemStack(Material.IRON_AXE, 1));
        chestItems.add(new ItemStack(Material.STONE_AXE, 1));
        chestItems.add(new ItemStack(Material.DIAMOND_HELMET, 1));
        chestItems.add(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
        chestItems.add(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
        chestItems.add(new ItemStack(Material.DIAMOND_BOOTS, 1));
        chestItems.add(new ItemStack(Material.APPLE, 3));
        chestItems.add(new ItemStack(Material.APPLE, 3));
        chestItems.add(new ItemStack(Material.APPLE, 4));
        chestItems.add(new ItemStack(Material.GOLDEN_APPLE, 1));
        chestItems.add(new ItemStack(Material.COOKED_BEEF, 3));
        chestItems.add(new ItemStack(Material.COOKED_BEEF, 4));
        chestItems.add(new ItemStack(Material.COOKED_BEEF, 4));
        chestItems.add(new ItemStack(Material.COOKED_BEEF, 5));
        chestItems.add(new ItemStack(Material.GOLDEN_CARROT, 3));
        chestItems.add(new ItemStack(Material.GOLDEN_CARROT, 4));
        chestItems.add(new ItemStack(Material.GOLDEN_CARROT, 5));
        chestItems.add(new ItemStack(Material.GOLDEN_CARROT, 5));
        chestItems.add(new ItemStack(Material.BOW, 1));
        chestItems.add(new ItemStack(Material.ARROW, 4));
        chestItems.add(new ItemStack(Material.ARROW, 8));
        chestItems.add(new ItemStack(Material.ARROW, 10));
        chestItems.add(new ItemStack(Material.ARROW, 12));
        chestItems.add(new ItemStack(Material.ARROW, 16));
        chestItems.add(new ItemStack(Material.CHAINMAIL_HELMET, 1));
        chestItems.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
        chestItems.add(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        chestItems.add(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        chestItems.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 4));
        chestItems.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 7));
        chestItems.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 10));
        chestItems.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 12));
        chestItems.add(new ItemStack(Material.CROSSBOW, 1));
        chestItems.add(new ItemStack(Material.TRIDENT, 1));
        chestItems.add(new ItemStack(Material.OAK_BOAT, 1));
        chestItems.add(new ItemStack(Material.SNOWBALL, 1));
        chestItems.add(new ItemStack(Material.SNOWBALL, 1));
        chestItems.add(new ItemStack(Material.SNOWBALL, 1));
        chestItems.add(new ItemStack(Material.SNOWBALL, 1));
        chestItems.add(sniper);
        //Non enchant/potion items are listed twice to manipulate chances
        chestItems.add(new ItemStack(Material.IRON_SWORD, 1));
        chestItems.add(new ItemStack(Material.DIAMOND_SWORD, 1));
        chestItems.add(new ItemStack(Material.ENDER_PEARL, 1));
        chestItems.add(new ItemStack(Material.ENDER_PEARL, 1));
        chestItems.add(new ItemStack(Material.IRON_HELMET, 1));
        chestItems.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
        chestItems.add(new ItemStack(Material.IRON_LEGGINGS, 1));
        chestItems.add(new ItemStack(Material.IRON_BOOTS, 1));
        chestItems.add(new ItemStack(Material.IRON_AXE, 1));
        chestItems.add(new ItemStack(Material.STONE_AXE, 1));
        chestItems.add(new ItemStack(Material.DIAMOND_HELMET, 1));
        chestItems.add(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
        chestItems.add(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
        chestItems.add(new ItemStack(Material.DIAMOND_BOOTS, 1));
        chestItems.add(new ItemStack(Material.APPLE, 3));
        chestItems.add(new ItemStack(Material.APPLE, 3));
        chestItems.add(new ItemStack(Material.APPLE, 4));
        chestItems.add(new ItemStack(Material.GOLDEN_APPLE, 1));
        chestItems.add(new ItemStack(Material.COOKED_BEEF, 3));
        chestItems.add(new ItemStack(Material.COOKED_BEEF, 4));
        chestItems.add(new ItemStack(Material.COOKED_BEEF, 4));
        chestItems.add(new ItemStack(Material.COOKED_BEEF, 5));
        chestItems.add(new ItemStack(Material.GOLDEN_CARROT, 3));
        chestItems.add(new ItemStack(Material.GOLDEN_CARROT, 4));
        chestItems.add(new ItemStack(Material.GOLDEN_CARROT, 5));
        chestItems.add(new ItemStack(Material.GOLDEN_CARROT, 5));
        chestItems.add(new ItemStack(Material.BOW, 1));
        chestItems.add(new ItemStack(Material.ARROW, 4));
        chestItems.add(new ItemStack(Material.ARROW, 8));
        chestItems.add(new ItemStack(Material.ARROW, 10));
        chestItems.add(new ItemStack(Material.ARROW, 12));
        chestItems.add(new ItemStack(Material.ARROW, 16));
        chestItems.add(new ItemStack(Material.CHAINMAIL_HELMET, 1));
        chestItems.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
        chestItems.add(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        chestItems.add(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        chestItems.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 4));
        chestItems.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 7));
        chestItems.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 10));
        chestItems.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 12));
        chestItems.add(new ItemStack(Material.CROSSBOW, 1));
        chestItems.add(new ItemStack(Material.TRIDENT, 1));
        chestItems.add(new ItemStack(Material.OAK_BOAT, 1));
        chestItems.add(new ItemStack(Material.SNOWBALL, 1));
        chestItems.add(new ItemStack(Material.SNOWBALL, 1));
        chestItems.add(new ItemStack(Material.SNOWBALL, 1));
        chestItems.add(new ItemStack(Material.SNOWBALL, 1));
        chestItems.add(sniper);
        chestItems.add(potion1);
        chestItems.add(potion2);
        chestItems.add(potion3);
        chestItems.add(potion4);
        chestItems.add(potion5);
        chestItems.add(potion6);
        chestItems.add(potion7);
        chestItems.add(potion8);
        chestItems.add(potion9);
        chestItems.add(potion10);
        chestItems.add(book1);
        chestItems.add(book2);
        chestItems.add(book3);
        chestItems.add(book4);
        chestItems.add(book5);
        chestItems.add(book6);
        chestItems.add(book7);
        chestItems.add(book8);
        chestItems.add(book9);
        chestItems.add(book10);
        chestItems.add(book11);
        chestItems.add(book12);
        chestItems.add(book13);
        chestItems.add(book14);
        chestItems.add(book15);
        chestItems.add(book16);
        chestItems.add(book17);
        chestItems.add(book18);

        //Barrel loot table
        barrelItems.add(new ItemStack(Material.WOODEN_AXE, 1));
        barrelItems.add(new ItemStack(Material.STONE_SWORD, 1));
        barrelItems.add(new ItemStack(Material.CARROT, 2));
        barrelItems.add(new ItemStack(Material.CARROT, 3));
        barrelItems.add(new ItemStack(Material.APPLE, 1));
        barrelItems.add(new ItemStack(Material.LEATHER_HELMET, 1));
        barrelItems.add(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
        barrelItems.add(new ItemStack(Material.LEATHER_LEGGINGS, 1));
        barrelItems.add(new ItemStack(Material.LEATHER_BOOTS, 1));
        barrelItems.add(new ItemStack(Material.WOODEN_SWORD, 1));
        barrelItems.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 4));
        barrelItems.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 7));
        barrelItems.add(new ItemStack(Material.CHAINMAIL_HELMET, 1));
        barrelItems.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
        barrelItems.add(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        barrelItems.add(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        barrelItems.add(new ItemStack(Material.OAK_BOAT, 1));
        barrelItems.add(new ItemStack(Material.OAK_BOAT, 1));
    }

    public void addChestItem(Inventory inventory) {
        int numberOfItems = ThreadLocalRandom.current().nextInt(4) + 3;
        while (numberOfItems > 0) {
            ItemStack item = chestItems.get(ThreadLocalRandom.current().nextInt(chestItems.size()));
            if (item.getItemMeta().getDisplayName().equals("Health Boost")) {
                if (ThreadLocalRandom.current().nextInt(2) == 0) {
                    inventory.addItem(item);
                }
            } else {
                inventory.addItem(item);
            }
            numberOfItems--;
        }
    }

    public void addBarrelItem(Inventory inventory) {
        int numberOfItems = ThreadLocalRandom.current().nextInt(3) + 2;
        while (numberOfItems > 0) {
            ItemStack item = barrelItems.get(ThreadLocalRandom.current().nextInt(barrelItems.size()));
            inventory.addItem(item);
            numberOfItems--;
        }
    }

    //Sniper
    @EventHandler
    public void onBowShoot(ProjectileLaunchEvent event) {
        Player shooter = (Player) event.getEntity().getShooter();
        if (shooter.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("Sniper")) {
            event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(3));
        }
    }

    //Impulse Grenade
    @EventHandler
    public void onSnoballHit(ProjectileHitEvent event) {
        if (event.getEntity().getType().equals(EntityType.SNOWBALL)) {
            Location hitloc = event.getEntity().getLocation();
            List<Entity> nearbyEntities = (List<Entity>) event.getEntity().getNearbyEntities(7, 7, 7);
            hitloc.getWorld().playSound(hitloc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            hitloc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, hitloc, 1);

            for (Entity ent : nearbyEntities) {
                Vector launchVector = ent.getLocation().toVector().subtract(event.getEntity().getLocation().toVector()).normalize();
                ent.setVelocity(launchVector.multiply(2));
            }

        }
    }
}
