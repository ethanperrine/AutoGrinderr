// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils.data.enums;

import com.autogrinder.gold.utils.Utils;
import net.minecraft.util.Vec3;

public enum MapData
{
    ELEMENTS(82, 114, new Vec3(13.7, 114.0, -2.7), new Vec3(0.5, 115.0, -11.5), new Vec3(-1.5, 114.0, 12.5)),

    
    CORAL(82, 114, new Vec3(13.7, 114.0, -2.7), new Vec3(0.5, 115.0, -11.5), new Vec3(-1.5, 114.0, 12.5)),
    
    UNKNOWN(0, 0, (Vec3)null, (Vec3)null, (Vec3)null), 
    KINGS(71, 95, new Vec3(14.7, 95.0, -1.7), new Vec3(0.5, 96.0, -12.5), new Vec3(-1.5, 95.0, 12.5)), 
    GENESIS(43, 86, new Vec3(-3.7, 86.0, 18.7), new Vec3(0.5, 87.0, -17.5), new Vec3(-1.5, 86.0, 16.5)),
    SEASONS(82, 114, new Vec3(16.7, 114.0, -1.7), new Vec3(0.5, 115.0, -14.5), new Vec3(-1.5, 114.0, 12.5));

    private final int groundLevel;
    private final int spawnLevel;
    private final Vec3 perkLocation;
    private final Vec3 prestigeLocation;
    private final Vec3 afkPoint;
    private static final MapData[] $VALUES;

    public Vec3 getPrestigeLocation() {
        return this.prestigeLocation;
    }
    
    public Vec3 getPerkLocation() {
        return this.perkLocation;
    }
    
    public static MapData getMapData() {
        if (Utils.getIdFromBlock(-7, 113, 8) == 12) {
            return MapData.CORAL;
        }
        if (Utils.getIdFromBlock(-9, 113, 8) == 2) {
            return MapData.ELEMENTS;
        }
        if (Utils.getIdFromBlock(-7, 85, 6) == 80) {
            return MapData.GENESIS;
        }
        if (Utils.getIdFromBlock(-4, 122, 6) == 43) {
            return MapData.KINGS;
        }
        if (Utils.getIdFromBlock(-13, 114, 8) == 24) {
            return MapData.SEASONS;
        }
        return null;
    }
    
    public int getSpawnLevel() {
        return this.spawnLevel;
    }
    
    static {
        $VALUES = new MapData[] { MapData.CORAL, MapData.ELEMENTS, MapData.GENESIS, MapData.KINGS, MapData.SEASONS, MapData.UNKNOWN };
    }
    
    public int getGroundLevel() {
        return this.groundLevel;
    }
    
    private MapData(final int groundLevel, final int spawnLevel, final Vec3 afkPoint, final Vec3 prestigeLocation, final Vec3 perkLocation) {
        this.groundLevel = groundLevel;
        this.spawnLevel = spawnLevel;
        this.afkPoint = afkPoint;
        this.prestigeLocation = prestigeLocation;
        this.perkLocation = perkLocation;
    }
    
    public Vec3 getAfkPoint() {
        return this.afkPoint;
    }
}
