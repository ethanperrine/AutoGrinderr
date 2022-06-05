// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils.data;

import com.autogrinder.gold.utils.Logger;
import java.util.Iterator;
import java.util.function.BiFunction;
import com.autogrinder.gold.utils.Setting;
import java.util.HashMap;
import java.util.ArrayList;
import com.autogrinder.gold.utils.data.enums.Perks;

public class PerkData
{
    private Perks.KillStreak killStreak2;
    private Perks.KillStreak killStreak1;
    public static final ArrayList<UpgradePair> UPGRADE_PATH;
    private Perks.Perk perk1;
    private Perks.Perk perk2;
    private final HashMap<Perks.Passive, Integer> passiveLevels;
    private Perks.Perk perk4;
    boolean fourthPerk;
    private Perks.Perk perk3;
    private int upgradeIndex;
    
    public boolean canGetNextUpgrade(final PlayerData playerData) {
        final UpgradePair nextUpgrade = this.getNextUpgrade();
        return nextUpgrade != null && (playerData.getLevel() >= nextUpgrade.getItem().getMinLevel(nextUpgrade.getLevel()) || (Setting.hasTheWay.isEnabled() && nextUpgrade.getItem() instanceof Perks.Perk)) && playerData.getGold() >= nextUpgrade.getItem().getPrice(nextUpgrade.getLevel());
    }
    
    public void setPerk1(final Perks.Perk perk1) {
        this.perk1 = perk1;
    }
    
    static {
        UPGRADE_PATH = new ArrayList<UpgradePair>();
    }
    
    public PerkData() {
        this.perk1 = Perks.Perk.UNKNOWN;
        this.perk2 = Perks.Perk.UNKNOWN;
        this.perk3 = Perks.Perk.UNKNOWN;
        this.perk4 = Perks.Perk.UNKNOWN;
        this.killStreak1 = Perks.KillStreak.UNKNOWN;
        this.killStreak2 = Perks.KillStreak.UNKNOWN;
        this.passiveLevels = new HashMap<Perks.Passive, Integer>();
        this.fourthPerk = false;
        this.upgradeIndex = 0;
        this.passiveLevels.put(Perks.Passive.XP_BOOST, -1);
        this.passiveLevels.put(Perks.Passive.GOLD_BOOST, -1);
        this.passiveLevels.put(Perks.Passive.MELEE_DAMAGE, -1);
        this.passiveLevels.put(Perks.Passive.DAMAGE_REDUCTION, -1);
        this.passiveLevels.put(Perks.Passive.EL_GATO, -1);
    }
    
    public Perks.Perk getPerk1() {
        return this.perk1;
    }
    
    public void setPerk2(final Perks.Perk perk2) {
        this.perk2 = perk2;
    }
    
    public Perks.KillStreak getKillStreak2() {
        return this.killStreak2;
    }
    
    public Perks.Perk getPerk2() {
        return this.perk2;
    }
    
    public void onPrestige() {
        this.perk1 = Perks.Perk.NONE;
        this.perk2 = Perks.Perk.NONE;
        this.perk3 = Perks.Perk.NONE;
        this.perk4 = Perks.Perk.NONE;
        this.killStreak1 = Perks.KillStreak.NONE;
        this.killStreak2 = Perks.KillStreak.NONE;
        this.passiveLevels.replaceAll(PerkData::lambda$onPrestige$0);
        this.upgradeIndex = 0;
    }
    
    public void setKillStreak2(final Perks.KillStreak killStreak2) {
        this.killStreak2 = killStreak2;
    }
    
    public boolean hasItem(final UpgradePair upgradePair) {
        return upgradePair != null && this.hasItem(upgradePair.getItem(), upgradePair.getLevel());
    }
    
    public boolean hasUnknown(final PlayerData playerData) {
        boolean b = false;
        final Iterator<Perks.Passive> iterator = this.passiveLevels.keySet().iterator();
        while (iterator.hasNext()) {
            if (this.passiveLevels.get(iterator.next()) == -1) {
                b = true;
                break;
            }
        }
        return b || this.perk1 == Perks.Perk.UNKNOWN || this.perk2 == Perks.Perk.UNKNOWN || this.perk3 == Perks.Perk.UNKNOWN || (this.perk4 == Perks.Perk.UNKNOWN && this.fourthPerk) || ((playerData.getPrestige() >= 1) ? (this.killStreak1 == Perks.KillStreak.UNKNOWN || this.killStreak2 == Perks.KillStreak.UNKNOWN) : (playerData.getLevel() >= 60 && this.killStreak1 == Perks.KillStreak.UNKNOWN));
    }
    
    public int getPassiveLevel(final Perks.Passive passive) {
        return this.passiveLevels.get(passive);
    }
    
    public Perks.KillStreak getKillStreak1() {
        return this.killStreak1;
    }
    
    public void setPassiveLevel(final Perks.Passive passive, final int n) {
        this.passiveLevels.put(passive, n);
    }
    
    public boolean hasFourthPerk() {
        return this.fourthPerk;
    }
    
    public void setPerk4(final Perks.Perk perk4) {
        this.perk4 = perk4;
    }
    
    private static Integer lambda$onPrestige$0(final Perks.Passive passive, final Integer n) {
        return 0;
    }
    
    public boolean hasItem(final Perks.ShopItem shopItem, final int n) {
        return this.perk1 == shopItem || this.perk2 == shopItem || this.perk3 == shopItem || this.perk4 == shopItem || this.killStreak1 == shopItem || this.killStreak2 == shopItem || (shopItem instanceof Perks.Passive && this.passiveLevels.get(shopItem) >= n);
    }
    
    public void setFourthPerk(final boolean fourthPerk) {
        this.fourthPerk = fourthPerk;
    }
    
    public boolean hasFreeSpace(final Perks.ShopItem shopItem, final PlayerData playerData) {
        if (shopItem instanceof Perks.Perk) {
            return this.perk1 == Perks.Perk.NONE || this.perk1 == Perks.Perk.OTHER || this.perk2 == Perks.Perk.NONE || this.perk2 == Perks.Perk.OTHER || this.perk3 == Perks.Perk.NONE || this.perk3 == Perks.Perk.OTHER || (this.fourthPerk && (this.perk4 == Perks.Perk.NONE || this.perk4 == Perks.Perk.OTHER));
        }
        if (shopItem instanceof Perks.KillStreak) {
            return this.killStreak1 == Perks.KillStreak.NONE || this.killStreak1 == Perks.KillStreak.OTHER || (playerData.getPrestige() > 0 && (this.killStreak2 == Perks.KillStreak.NONE || this.killStreak2 == Perks.KillStreak.OTHER));
        }
        return shopItem instanceof Perks.Passive;
    }
    
    public void setKillStreak1(final Perks.KillStreak killStreak1) {
        this.killStreak1 = killStreak1;
    }
    
    public Perks.Perk getPerk4() {
        return this.perk4;
    }
    
    public void updatePerk(final Perks.ShopItem shopItem, final int n, final int n2) {
        if (shopItem instanceof Perks.Perk) {
            switch (n2) {
                case 1: {
                    this.perk1 = (Perks.Perk)shopItem;
                    break;
                }
                case 2: {
                    this.perk2 = (Perks.Perk)shopItem;
                    break;
                }
                case 3: {
                    this.perk3 = (Perks.Perk)shopItem;
                    break;
                }
                case 4: {
                    this.perk4 = (Perks.Perk)shopItem;
                    break;
                }
                default: {
                    Logger.error(String.valueOf(new StringBuilder().append("Failed to update perk ").append(shopItem).append(" at slot ").append(n2).append(".")));
                    break;
                }
            }
        }
        else if (shopItem instanceof Perks.KillStreak) {
            switch (n2) {
                case 1: {
                    this.killStreak1 = (Perks.KillStreak)shopItem;
                    break;
                }
                case 2: {
                    this.killStreak2 = (Perks.KillStreak)shopItem;
                    break;
                }
                default: {
                    Logger.error(String.valueOf(new StringBuilder().append("Failed to update killstreak ").append(shopItem).append(" at slot ").append(n2).append(".")));
                    break;
                }
            }
        }
        else if (shopItem instanceof Perks.Passive) {
            this.passiveLevels.put((Perks.Passive)shopItem, n);
        }
        else {
            Logger.error(String.valueOf(new StringBuilder().append("Failed to update shop data of ").append(shopItem)));
        }
    }
    
    public void incrementPath() {
        ++this.upgradeIndex;
    }
    
    public Perks.Perk getPerk3() {
        return this.perk3;
    }
    
    public UpgradePair getNextUpgrade() {
        return (this.upgradeIndex < PerkData.UPGRADE_PATH.size()) ? PerkData.UPGRADE_PATH.get(this.upgradeIndex) : null;
    }
    
    private static Integer lambda$makePerksUnknown$1(final Perks.Passive passive, final Integer n) {
        return -1;
    }
    
    public boolean shouldSkipPerk(final PlayerData playerData) {
        final UpgradePair nextUpgrade = this.getNextUpgrade();
        return nextUpgrade != null && (this.hasItem(nextUpgrade) || !this.hasFreeSpace(nextUpgrade.getItem(), playerData) || (nextUpgrade.getItem() == Perks.Perk.STREAKER && this.hasItem(Perks.Perk.DIRTY, 1)) || (nextUpgrade.getItem() == Perks.Perk.DIRTY && this.hasItem(Perks.Perk.STREAKER, 1)) || (nextUpgrade.getItem() == Perks.Perk.DIRTY && !Setting.hasDirty.isEnabled()) || (nextUpgrade.getItem() == Perks.KillStreak.COUNTER_STRIKE && playerData.getPrestige() < 1) || (nextUpgrade.getItem() == Perks.Perk.DIRTY && !this.fourthPerk) || (nextUpgrade.getItem() == Perks.Perk.STREAKER && !this.fourthPerk) || (nextUpgrade.getItem() == Perks.KillStreak.FIGHT_OR_FLIGHT && this.hasItem(Perks.KillStreak.EXPLICIOUS, 1)) || (nextUpgrade.getItem() == Perks.KillStreak.EXPLICIOUS && this.hasItem(Perks.KillStreak.FIGHT_OR_FLIGHT, 1)) || (nextUpgrade.getItem() == Perks.KillStreak.FIGHT_OR_FLIGHT && Setting.useExpKs.isEnabled()) || (nextUpgrade.getItem() == Perks.KillStreak.EXPLICIOUS && !Setting.useExpKs.isEnabled()) || (nextUpgrade.getItem() == Perks.Perk.GLADIATOR && Setting.useInsurance.isEnabled()) || (nextUpgrade.getItem() == Perks.Perk.INSURANCE && !Setting.useInsurance.isEnabled()));
    }
    
    public void makePerksUnknown() {
        this.perk1 = Perks.Perk.UNKNOWN;
        this.perk2 = Perks.Perk.UNKNOWN;
        this.perk3 = Perks.Perk.UNKNOWN;
        this.perk4 = Perks.Perk.UNKNOWN;
        this.killStreak1 = Perks.KillStreak.UNKNOWN;
        this.killStreak2 = Perks.KillStreak.UNKNOWN;
        this.passiveLevels.replaceAll(PerkData::lambda$makePerksUnknown$1);
        this.upgradeIndex = 0;
    }
    
    public void setPerk3(final Perks.Perk perk3) {
        this.perk3 = perk3;
    }
}
