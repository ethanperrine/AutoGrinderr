// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils.data.enums;

public enum PitEvent
{
    ROBBERY(true), 
    TEAM_DEATHMATCH(true), 
    SQUADS(false), 
    RAGE_PIT(true), 
    BLOCKHEAD(true), 
    BEAST(true),
    RAFFLE(true),
    SPIRE(false),
    UNKNOWN(true),
    PIZZA(false);
    
    private final boolean drop;

    
    private static final PitEvent[] $VALUES;

    
    public static PitEvent getEventFromName(final String s) {
        if ("rage pit".equalsIgnoreCase(s)) {
            return PitEvent.RAGE_PIT;
        }
        if ("team deathmatch".equalsIgnoreCase(s)) {
            return PitEvent.TEAM_DEATHMATCH;
        }
        if ("tdm".equalsIgnoreCase(s)) {
            return PitEvent.TEAM_DEATHMATCH;
        }
        if ("raffle".equalsIgnoreCase(s)) {
            return PitEvent.RAFFLE;
        }
        if ("beast".equalsIgnoreCase(s)) {
            return PitEvent.BEAST;
        }
        if ("squads".equalsIgnoreCase(s)) {
            return PitEvent.SQUADS;
        }
        if ("blockhead".equalsIgnoreCase(s)) {
            return PitEvent.BLOCKHEAD;
        }
        if ("robbery".equalsIgnoreCase(s)) {
            return PitEvent.ROBBERY;
        }
        if ("spire".equalsIgnoreCase(s)) {
            return PitEvent.SPIRE;
        }
        if ("pizza".equalsIgnoreCase(s)) {
            return PitEvent.PIZZA;
        }
        return null;
    }
    
    public boolean stayInSpawn() {
        return !this.drop;
    }
    
    private PitEvent(final boolean drop) {
        this.drop = drop;
    }
    
    static {
        $VALUES = new PitEvent[] { PitEvent.RAGE_PIT, PitEvent.TEAM_DEATHMATCH, PitEvent.RAFFLE, PitEvent.BEAST, PitEvent.SQUADS, PitEvent.BLOCKHEAD, PitEvent.ROBBERY, PitEvent.SPIRE, PitEvent.PIZZA, PitEvent.UNKNOWN };
    }
}
