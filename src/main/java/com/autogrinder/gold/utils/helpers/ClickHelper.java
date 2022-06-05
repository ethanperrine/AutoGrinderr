// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils.helpers;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.util.BlockPos;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.Minecraft;

public class ClickHelper
{
    public static void leftClick() {
        final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        ((EntityPlayer)thePlayer).swingItem();
        final MovingObjectPosition objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            Minecraft.getMinecraft().playerController.attackEntity((EntityPlayer)thePlayer, objectMouseOver.entityHit);
        }
        else if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final BlockPos getBlockPos = objectMouseOver.getBlockPos();
            if (Minecraft.getMinecraft().theWorld.getBlockState(getBlockPos).getBlock().getMaterial() != Material.air) {
                Minecraft.getMinecraft().playerController.clickBlock(getBlockPos, objectMouseOver.sideHit);
            }
        }
    }
    
    public static void clickOnSlot(final int n) {
        Minecraft.getMinecraft().playerController.windowClick(((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots.windowId, n, 2, 3, (EntityPlayer)Minecraft.getMinecraft().thePlayer);
    }
}
