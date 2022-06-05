// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils.data;

public class PlayerData
{
    private boolean isFighting;
    private int streak;
    private int level;
    private double gold;
    private int prestige;
    private String currentServer;
    
    public int getLevel() {
        return this.level;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setStreak(final int streak) {
        this.streak = streak;
    }
    
    public int getPrestige() {
        return this.prestige;
    }
    
    public void setPrestige(final int prestige) {
        this.prestige = prestige;
    }
    
    public PlayerData(final int n) {
        this(n, -1.0);
    }
    
    public PlayerData(final int level, final double gold) {
        this.prestige = -1;
        this.isFighting = false;
        this.currentServer = null;
        this.level = level;
        this.gold = gold;
    }
    
    public int getStreak() {
        return this.streak;
    }
    
    public void setGold(final double gold) {
        this.gold = gold;
    }
    
    public void clearData() {
        this.level = -1;
        this.gold = -1.0;
        this.prestige = -1;
        this.isFighting = false;
        this.currentServer = null;
    }
    
    public String getCurrentServer() {
        return this.currentServer;
    }
    
    public double getGold() {
        return this.gold;
    }
    
    public PlayerData(final double n) {
        this(-1, n);
    }
    
    public boolean isFighting() {
        return this.isFighting;
    }
    
    public PlayerData() {
        this(-1, -1.0);
    }
    
    public void setFighting(final boolean isFighting) {
        this.isFighting = isFighting;
    }
    
    public void setCurrentServer(final String currentServer) {
        this.currentServer = currentServer;
    }
}
