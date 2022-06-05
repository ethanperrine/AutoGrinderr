// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.schedulers.tasks;

import com.autogrinder.gold.Driver;
import com.autogrinder.gold.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.Item;
import net.minecraft.inventory.Slot;
import com.autogrinder.gold.AutoGrinder;
import com.autogrinder.gold.utils.helpers.ClickHelper;
import net.minecraft.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import com.autogrinder.gold.utils.Logger;

public class PrestigeTask extends Task
{


    private final int phase;
    
    public PrestigeTask() {
        this(10, 0);
    }
    
    public PrestigeTask(final int n, final int phase) {

        super(n);
        this.phase = phase;
    }
    
    @Override
    public void run() {
        if (!this.isPrestigeWindowOpen()) {
            Logger.error("Prestige window was not open?");
            this.finishTaskChain();
        }
        else if (this.phase == 0) {
            final Slot func_75139_a = ((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots.getSlot(11);
            final ItemStack func_75211_c = func_75139_a.getStack();
            if (func_75211_c != null && func_75211_c.getItem() == Items.diamond) {
                ClickHelper.clickOnSlot(func_75139_a.slotNumber);
                AutoGrinder.getInstance().getScheduler().addTask(new PrestigeTask(110, 1));
            }
            else {
                Logger.error("Prestige diamond could not be found?");
                this.finishTaskChain();
            }
        }
        else if (this.phase == 1) {
            final Slot slot = ((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots.inventorySlots.get(11);
            final ItemStack func_75211_c2 = slot.getStack();
            if (func_75211_c2 != null && func_75211_c2.getItem() == Item.getItemById(159) && func_75211_c2.getItem() instanceof ItemCloth) {
                switch (func_75211_c2.getItemDamage()) {
                    case 4: {
                        AutoGrinder.getInstance().getScheduler().addTask(new PrestigeTask(5, 1));
                        return;
                    }
                    case 13: {
                        ClickHelper.clickOnSlot(slot.slotNumber);
                        AutoGrinder.getInstance().getDriver().onPrestige();
                        break;
                    }
                }
                AutoGrinder.getInstance().getScheduler().addTask(new PrestigeTask(5, 2));
            }
            else {
                Logger.error("Could not find prestige confirmation?");
                this.finishTaskChain();
            }
        }
        else {
            this.finishTaskChain();
        }
    }
    
    private boolean isPrestigeWindowOpen() {
        return Utils.guiNameContains("Prestige");
    }
    
    private void finishTaskChain() {
        if (Minecraft.getMinecraft().currentScreen != null) {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
        AutoGrinder.getInstance().getDriver().finishedTask();
    }
}
