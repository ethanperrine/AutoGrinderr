// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils.data;

import com.autogrinder.gold.utils.data.enums.Perks;
import com.autogrinder.gold.utils.data.enums.Perks;

public class UpgradePair
{
    private final int level;
    private final Perks.ShopItem item;


    public int getLevel() {
        return this.level;
    }
    
    public UpgradePair(final Perks.ShopItem shopItem) {
        this(shopItem, 1);
    }
    
    public UpgradePair(final Perks.ShopItem item, final int level) {
        this.item = item;
        this.level = level;
    }
    
    public Perks.ShopItem getItem() {
        return this.item;
    }
}
