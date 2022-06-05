// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils;

public enum Setting
{
    hasTheWay, 
    playPit, 
    showTarget,
    
    shouldJump, 
    autoReconnect, 
    targetDiamonds, 
    mysticPathfinding, 
    autoPrestige, 
    useExpKs,

    
    useInsurance, 
    useProfiles, 
    autoPerk, 
    hasDirty, 
    minPlayersInLobby;

    private static final Setting[] $VALUES;
    private boolean enabled;

    static {
        $VALUES = new Setting[] { Setting.playPit, Setting.autoReconnect, Setting.autoPrestige, Setting.autoPerk, Setting.hasDirty, Setting.hasTheWay, Setting.useExpKs, Setting.minPlayersInLobby, Setting.mysticPathfinding, Setting.useProfiles, Setting.targetDiamonds, Setting.shouldJump, Setting.showTarget, Setting.useInsurance };
    }
    
    private Setting() {
        this.enabled = false;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
