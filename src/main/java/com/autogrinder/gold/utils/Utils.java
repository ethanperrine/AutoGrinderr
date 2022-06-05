// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.block.Block;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import java.util.Collections;
import net.minecraft.scoreboard.Score;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.regex.Pattern;
import com.autogrinder.gold.utils.data.enums.MapData;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import com.autogrinder.gold.utils.data.PlayerData;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.Minecraft;

public class Utils
{
    public static String getGuiName() {
        final GuiScreen field_71462_r = Minecraft.getMinecraft().currentScreen;
        return (field_71462_r instanceof GuiChest) ? ((ContainerChest)((GuiChest)field_71462_r).inventorySlots).getLowerChestInventory().getName() : null;
    }
    
    public static double getPlayerHealth(final EntityPlayer entityPlayer) {
        return entityPlayer.getHealth();
    }
    
    public static void renderEdgeBoundingBox(final AxisAlignedBB axisAlignedBB, final Color color) {
        GlStateManager.disableDepth();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0.0f, 240.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        RenderGlobal.drawOutlinedBoundingBox(axisAlignedBB, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();

    }
    
    public static PlayerData getPlayerData() {
        final PlayerData playerData = new PlayerData();
        updatePlayerData(playerData);
        return playerData;
    }
    
    public static double getDist2D(final Entity entity, final double n, final double n2) {
        return getDist2D(entity.posX, entity.posZ, n, n2);
    }
    
    public static int numeralToInt(final String s) {
        int n = 0;
        int i = 0;
        while (i < s.length()) {
            final int value = value(s.charAt(i));
            if (i + 1 < s.length()) {
                final int value2 = value(s.charAt(i + 1));
                if (value >= value2) {
                    n += value;
                }
                else {
                    n = n + value2 - value;
                    ++i;
                }
            }
            else {
                n += value;
            }
            final int n2 = 4;
            final int n3 = 1;
            ++i;
            if (n2 <= n3) {
                return 0;
            }
        }
        return n;
    }
    
    public static int getIdFromBlock(final int n, final int n2, final int n3) {
        return getIdFromBlock(new BlockPos(n, n2, n3));
    }
    
    public static double getDist2D(final Entity entity, final Entity entity2) {
        return getDist2D(entity.posX, entity.posZ, entity2.posX, entity2.posZ);
    }
    
    public static boolean entityInMid(final Entity entity, final MapData mapData) {
        final boolean hasRing = hasRing(mapData);
        final double field_70165_t = entity.posX;
        final double field_70161_v = entity.posZ;
        final double dist2D = getDist2D(field_70165_t, field_70161_v, 0.5, 0.5);
        if (mapData == MapData.KINGS) {
            return hasRing ? (dist2D <= 9.0) : (Math.abs(field_70165_t) < 10.5 && Math.abs(field_70161_v) < 10.5);
        }
        return dist2D <= (hasRing ? 9 : 15);
    }
    
    public static void updatePlayerData(final PlayerData playerData) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> iterator = getScoreboardLines().iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next()).append("\n");
        }
        final String value = String.valueOf(sb);
        final Matcher matcher = Pattern.compile("Level: \\[(\\d+)]").matcher(value);
        final Matcher matcher2 = Pattern.compile("Gold: (\\d+(?:\\.\\d+)?)g").matcher(value.replaceAll(",", ""));
        final Matcher matcher3 = Pattern.compile("Prestige: ([IVXLCDM]+)").matcher(value);
        final Matcher matcher4 = Pattern.compile("Streak: (\\d+)").matcher(value);
        final Matcher matcher5 = Pattern.compile("\\d\\d/\\d\\d/\\d\\d ([Mm]\\w\\w)").matcher(value);
        if (matcher.find()) {
            playerData.setLevel(Integer.parseInt(matcher.group(1)));
        }
        if (matcher2.find()) {
            playerData.setGold(Double.parseDouble(matcher2.group(1)));
        }
        playerData.setPrestige(matcher3.find() ? numeralToInt(matcher3.group(1)) : 0);
        playerData.setFighting(value.contains("Fighting"));
        playerData.setStreak(matcher4.find() ? Integer.parseInt(matcher4.group(1)) : 0);
        if (matcher5.find()) {
            playerData.setCurrentServer(matcher5.group(1));
        }
    }
    
    public static List<String> getScoreboardLines() {
        final ArrayList list = new ArrayList();
        final Scoreboard func_96441_U = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (func_96441_U == null) {
            return list;
        }
        final ScoreObjective func_96539_a = func_96441_U.getObjectiveInDisplaySlot(1);
        if (func_96539_a == null) {
            return list;
        }
        final ArrayList list2 = new ArrayList();
        for (final Score score : func_96441_U.getSortedScores(func_96539_a)) {
            if (score != null && score.getPlayerName() != null && !score.getPlayerName().startsWith("#")) {
                list2.add(score);
            }
        }
        final Iterator iterator2 = list2.iterator();
        while (iterator2.hasNext()) {
            final ScorePlayerTeam func_96509_i = func_96441_U.getPlayersTeam(((Score)iterator2.next()).getPlayerName());
            if (func_96509_i == null) {
                continue;
            }
            final String value = String.valueOf(new StringBuilder().append(func_96509_i.getColorPrefix()).append(func_96509_i.getColorSuffix()));
            if (value.matches("\\s*")) {
                continue;
            }
            list.add(value.replaceAll("�.", ""));
        }
        Collections.reverse(list);
        return list;
    }
    
    public static int getIdFromBlock(final BlockPos blockPos) {
        return Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock());
    }
    
    public static boolean guiNameContains(final String s) {
        final String guiName = getGuiName();
        return guiName != null && guiName.contains(s);
    }
    
    public static int calculateEntityLevel(final double n, final double n2) {
        if (n < 3.5) {
            return 1;
        }
        if (n < 7.0 && n2 < 5.0) {
            return 2;
        }
        if (n2 < 5.0) {
            return 3;
        }
        if (n < 7.0 && n2 < 9.0) {
            return 4;
        }
        return 5;
    }
    
    public static int getBestWeapon(final EntityPlayer entityPlayer) {
        final ItemStack[] field_70462_a = entityPlayer.inventory.mainInventory;
        int n = 0;
        float func_150931_i = 0.0f;
        int i = 0;
        while (i < 9) {
            if (field_70462_a[i] != null && field_70462_a[i].getItem() instanceof ItemSword) {
                final ItemSword itemSword = (ItemSword)field_70462_a[i].getItem();
                if (itemSword.getDamageVsEntity() + ((itemSword.getDamageVsEntity() == 0.0f) ? 2.5 : 0.0) > func_150931_i) {
                    n = i;
                    func_150931_i = itemSword.getDamageVsEntity();
                }
            }
            final int n2 = 4;
            ++i;
            if (n2 == 0) {
                return 0;
            }
        }
        return n;
    }
    
    public static int value(final char c) {
        if (c == 'I') {
            return 1;
        }
        if (c == 'V') {
            return 5;
        }
        if (c == 'X') {
            return 10;
        }
        if (c == 'L') {
            return 50;
        }
        if (c == 'C') {
            return 100;
        }
        if (c == 'D') {
            return 500;
        }
        if (c == 'M') {
            return 1000;
        }
        return -1;
    }
    
    public static EntityItem getMysticDrop() {
        final EntityPlayerSP field_71439_g = Minecraft.getMinecraft().thePlayer;
        for (final Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity instanceof EntityItem && getDist2D((Entity)field_71439_g, entity) < 5.0) {
                return (EntityItem)entity;
            }
        }
        return null;
    }
    
    public static boolean isGuiName(final String s) {
        return s.equals(getGuiName());
    }
    
    public static double getDist2D(final Entity entity, final Vec3 vec3) {
        return getDist2D(entity, vec3.xCoord, vec3.zCoord);
    }
    
    public static boolean hasRing(final MapData mapData) {
        if (Minecraft.getMinecraft().theWorld == null) {
            return false;
        }
        if (mapData == null) {
            return false;
        }
        final BlockPos[] array = { new BlockPos(0, mapData.getGroundLevel() + 1, 10), new BlockPos(10, mapData.getGroundLevel() + 1, 0), new BlockPos(0, mapData.getGroundLevel() + 1, -9), new BlockPos(-9, mapData.getGroundLevel() + 1, 0) };
        int n = 0;
        int i = 0;
        while (i < 4) {
            if (getIdFromBlock(array[i]) == 49) {
                ++n;
            }
            final boolean b = true;
            final boolean b2 = true;
            ++i;
            if (b != b2) {
                return false;
            }
        }
        return n >= 3;
    }
    
    public static double getDist2D(final double n, final double n2, final double n3, final double n4) {
        return MathHelper.sqrt_double((n - n3) * (n - n3) + (n2 - n4) * (n2 - n4));
    }
    
    public static void highlightEntity(final Entity entity, final Color color) {
        return;
        /*
        final RenderManager getRenderManager = Minecraft.getMinecraft().getRenderManager();
        final double n = (entity.ticksExisted == 0) ? entity.posX : entity.lastTickPosX;
        final double n2 = (entity.ticksExisted == 0) ? entity.posY : entity.lastTickPosY;
        final double n3 = (entity.ticksExisted == 0) ? entity.posZ : entity.lastTickPosZ;
        final float renderPartialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;
        final double n4 = n + (entity.posX - n) * renderPartialTicks;
        final double n5 = n2 + (entity.posY - n2) * renderPartialTicks;
        final double n6 = n3 + (entity.posZ - n3) * renderPartialTicks;
        final double n7 = n4 - getRenderManager.field_78725_b;
        final double n8 = n5 - getRenderManager.field_78726_c;
        final double n9 = n6 - getRenderManager.field_78723_d;
        final AxisAlignedBB func_72317_d = new AxisAlignedBB(-0.3, 0.0, -0.3, 0.3, 1.8, 0.3).func_72317_d(entity.posX, entity.field_70163_u, entity.field_70161_v);
        renderEdgeBoundingBox(new AxisAlignedBB(func_72317_d.field_72340_a - entity.posX + n7, func_72317_d.field_72338_b - entity.field_70163_u + n8, func_72317_d.field_72339_c - entity.field_70161_v + n9, func_72317_d.field_72336_d - entity.posX + n7, func_72317_d.field_72337_e - entity.field_70163_u + n8, func_72317_d.field_72334_f - entity.field_70161_v + n9), color);


         */
    }
    
    public static boolean entityInBlock(final Vec3 vec3) {
        for (final Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (getDist2D(vec3.xCoord, vec3.zCoord, entity.posX, entity.posZ) < 0.7) {
                return true;
            }
        }
        return false;
    }
}
