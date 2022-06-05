// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils.data.enums;

import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Perks
{
    public static ShopItem valueOf(final String s) {
        try {
            return Perk.valueOf(s);
        }
        catch (final IllegalArgumentException e) {
            try {
                return KillStreak.valueOf(s);
            }
            catch (final IllegalArgumentException ex) {
                return Passive.valueOf(s);
            }
        }

    }
    
    public enum Perk implements ShopItemId
    {
        
        OTHER(-1.0, -1, -1), 
        DIRTY(8000.0, 100, 3), 
        STREAKER(8000.0, 100, 296), 
        INSURANCE(2000.0, 50, 329),
        STR_CHAINING(2000.0, 20, 331),
        GLADIATOR(4000.0, 70, 352),
        UNKNOWN(-1.0, -1, -1),
        NONE(-1.0, -1, -1),
        VAMPIRE(4000.0, 60, 376);


        private static final Perk[] $VALUES;
        private final int minLevel;
        private final int id;
        private final double price;

        
        @Override
        public double getPrice(final int n) {
            return this.price;
        }
        
        public static Perk getItemFromItemstack(final ItemStack itemStack) {
            if (itemStack == null) {
                return Perk.UNKNOWN;
            }
            if (itemStack.getItem() == Items.redstone) {
                return Perk.STR_CHAINING;
            }
            if (itemStack.getItem() == Items.fermented_spider_eye) {
                return Perk.VAMPIRE;
            }
            if (itemStack.getItem() == Items.bone) {
                return Perk.GLADIATOR;
            }
            if (itemStack.getItem() == Item.getItemById(3)) {
                return Perk.DIRTY;
            }
            if (itemStack.getItem() == Items.wheat) {
                return Perk.STREAKER;
            }
            if (itemStack.getItem() == Item.getItemById(57) || itemStack.getItem() == Item.getItemById(7)) {
                return Perk.NONE;
            }
            return Perk.OTHER;
        }
        
        @Override
        public int getItemId() {
            return this.id;
        }
        
        static {
            $VALUES = new Perk[] { Perk.VAMPIRE, Perk.GLADIATOR, Perk.STR_CHAINING, Perk.DIRTY, Perk.STREAKER, Perk.INSURANCE, Perk.OTHER, Perk.NONE, Perk.UNKNOWN };
        }
        
        public double getPrice() {
            return this.price;
        }
        
        private Perk(final double price, final int minLevel, final int id) {
            this.price = price;
            this.minLevel = minLevel;
            this.id = id;
        }
        
        @Override
        public int getMinLevel(final int n) {
            return this.minLevel;
        }
        
        public int getMinLevel() {
            return this.minLevel;
        }
    }
    
    public interface ShopItemId extends ShopItem
    {
        int getItemId();
    }
    
    public interface ShopItem
    {
        double getPrice(final int p0);
        
        int getMinLevel(final int p0);
    }
    
    public enum KillStreak implements ShopItemId
    {
        
        EXPLICIOUS(3000.0, 30, 351), 
        UNKNOWN(-1.0, -1, -1),
        COUNTER_STRIKE(5000.0, 75, 417), 
        FIGHT_OR_FLIGHT(5000.0, 75, 385), 
        OTHER(-1.0, -1, -1), 
        NONE(-1.0, -1, -1);
        
        private static final KillStreak[] $VALUES;
        private final double price;
        private final int id;
        private final int minLevel;
        
        @Override
        public int getItemId() {
            return this.id;
        }
        
        static {
            $VALUES = new KillStreak[] { KillStreak.EXPLICIOUS, KillStreak.COUNTER_STRIKE, KillStreak.FIGHT_OR_FLIGHT, KillStreak.OTHER, KillStreak.NONE, KillStreak.UNKNOWN };
        }
        
        public static KillStreak getItemFromItemstack(final ItemStack itemStack) {
            if (itemStack == null) {
                return KillStreak.UNKNOWN;
            }
            if (itemStack.getItem() == Items.dye) {
                return KillStreak.EXPLICIOUS;
            }
            if (itemStack.getItem() == Items.iron_horse_armor) {
                return KillStreak.COUNTER_STRIKE;
            }
            if (itemStack.getItem() == Items.fire_charge) {
                return KillStreak.FIGHT_OR_FLIGHT;
            }
            if (itemStack.getItem() == Item.getItemById(41) || itemStack.getItem() == Item.getItemById(7)) {
                return KillStreak.NONE;
            }
            return KillStreak.OTHER;
        }
        
        public int getMinLevel() {
            return this.minLevel;
        }
        
        @Override
        public int getMinLevel(final int n) {
            return this.minLevel;
        }
        
        public double getPrice() {
            return this.price;
        }
        
        private KillStreak(final double price, final int minLevel, final int id) {
            this.price = price;
            this.minLevel = minLevel;
            this.id = id;
        }
        
        @Override
        public double getPrice(final int n) {
            return this.price;
        }
    }
    
    public enum Passive implements ShopItemLocation
    {
        DAMAGE_REDUCTION(new double[] { 450.0, 1050.0, 1500.0, 2250.0, 3000.0 }, new int[] { 30, 50, 60, 100, 100 }, 32), 
        MELEE_DAMAGE(new double[] { 450.0, 1050.0, 1500.0, 2250.0, 3000.0 }, new int[] { 30, 50, 100, 100, 100 }, 30), 
        EL_GATO(new double[] { 1000.0, 2000.0, 3000.0, 4000.0, 5000.0 }, new int[] { 50, 60, 70, 80, 90 }, 34),
        GOLD_BOOST(new double[] { 1000.0, 2500.0, 10000.0, 25000.0, 40000.0 }, new int[] { 10, 30, 100, 100, 100 }, 29),
        XP_BOOST(new double[] { 500.0, 2500.0, 5000.0, 10000.0, 25000.0 }, new int[] { 10, 30, 100, 100, 100 }, 28);
        
        private final int[] upgradeLevel;
        private final double[] upgradePrice;
        private static final Passive[] $VALUES;
        private final int location;

        
        @Override
        public int getLocation() {
            return this.location;
        }
        
        private Passive(final double[] upgradePrice, final int[] upgradeLevel, final int location) {
            this.upgradePrice = upgradePrice;
            this.upgradeLevel = upgradeLevel;
            this.location = location;
        }
        
        public static ShopItem getItemFromItemstack(final ItemStack itemStack) {
            final String replaceAll = itemStack.getDisplayName().replaceAll("�.", "");
            switch (replaceAll) {
                case "XP Boost": {
                    return Passive.XP_BOOST;
                }
                case "Melee Damage": {
                    return Passive.MELEE_DAMAGE;
                }
                case "Damage Reduction": {
                    return Passive.DAMAGE_REDUCTION;
                }
                case "El Gato": {
                    return Passive.EL_GATO;
                }
                case "Gold Boost": {
                    return Passive.GOLD_BOOST;
                }
                default: {
                    return null;
                }
            }
        }
        
        @Override
        public int getMinLevel(final int n) {
            if (0 < n && n <= this.upgradeLevel.length) {
                return this.upgradeLevel[n - 1];
            }
            return -1;
        }
        
        @Override
        public double getPrice(final int n) {
            if (0 < n && n <= this.upgradePrice.length) {
                return this.upgradePrice[n - 1];
            }
            return -1.0;
        }
        
        static {
            $VALUES = new Passive[] { Passive.XP_BOOST, Passive.GOLD_BOOST, Passive.MELEE_DAMAGE, Passive.DAMAGE_REDUCTION, Passive.EL_GATO };
        }
    }
    
    public interface ShopItemLocation extends ShopItem
    {
        int getLocation();
    }
}
