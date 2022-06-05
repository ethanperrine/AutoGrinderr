// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.schedulers.tasks;

import com.autogrinder.gold.AutoGrinder;
import com.autogrinder.gold.utils.Logger;
import com.autogrinder.gold.utils.data.PerkData;
import com.autogrinder.gold.utils.data.UpgradePair;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.inventory.Slot;
import com.autogrinder.gold.utils.helpers.ClickHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import com.autogrinder.gold.utils.Utils;
import com.autogrinder.gold.utils.data.enums.Perks;

public class BuyTask extends Task
{
    private final int phase;
    private final UpgradePair pair;
    private final PerkData data;
    private int num;


    @Override
    public void run() {
        if (this.pair.getItem() != null) {
            if (this.phase == 0) {
                if (Utils.isGuiName("Permanent upgrades")) {
                    final Container field_147002_h = ((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots;
                    Slot slot = null;
                    if (this.canPerkChange(this.data.getPerk1())) {
                        this.num = 1;
                        slot = field_147002_h.getSlot(12);
                    }
                    else if (this.canPerkChange(this.data.getPerk2())) {
                        this.num = 2;
                        slot = field_147002_h.getSlot(13);
                    }
                    else if (this.canPerkChange(this.data.getPerk3())) {
                        this.num = 3;
                        slot = field_147002_h.getSlot(14);
                    }
                    else if (this.data.hasFourthPerk() && this.canPerkChange(this.data.getPerk4())) {
                        this.num = 4;
                        slot = field_147002_h.getSlot(15);
                    }
                    if (slot != null) {
                        ClickHelper.clickOnSlot(slot.slotNumber);
                        this.addNextTask();
                    }
                    else {
                        Logger.error(String.valueOf(new StringBuilder().append("Could not find a free perk slot? (Current Perks: ").append(this.data.getPerk1()).append(", ").append(this.data.getPerk2()).append(", ").append(this.data.getPerk3()).append(this.data.hasFourthPerk() ? String.valueOf(new StringBuilder().append(", ").append(this.data.getPerk4())) : "").append(")")));
                        this.finishTaskChain();
                    }
                }
                else {
                    Logger.error(String.valueOf(new StringBuilder().append("Gui name (").append(Utils.getGuiName()).append(") did not match \"Permanent Upgrades\"")));
                    this.finishTaskChain();
                }
            }
            else if (this.phase == 1) {
                if (Utils.isGuiName("Choose a perk")) {
                    final Container field_147002_h2 = ((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots;
                    boolean b = false;
                    for (final Slot slot2 : field_147002_h2.inventorySlots) {
                        final ItemStack func_75211_c = slot2.getStack();
                        if (func_75211_c != null && func_75211_c.getItem() == Item.getItemById(((Perks.Perk)this.pair.getItem()).getItemId())) {
                            b = true;
                            ClickHelper.clickOnSlot(slot2.slotNumber);
                            this.addNextTask();
                            break;
                        }
                    }
                    if (!b) {
                        Logger.error("Could not find perk in purchase menu?");
                        this.finishTaskChain();
                    }
                }
                else {
                    Logger.error(String.valueOf(new StringBuilder().append("Gui name (").append(Utils.getGuiName()).append(") did not match \"Choose a perk\"")));
                    this.finishTaskChain();
                }
            }
            else if (this.phase == 2) {
                if (Utils.isGuiName("Permanent upgrades")) {
                    this.successfulAssignment();
                }
                else if (Utils.isGuiName("Are you sure?")) {
                    ClickHelper.clickOnSlot(((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots.getSlot(11).slotNumber);
                    this.addNextTask();
                }
                else {
                    Logger.error(String.valueOf(new StringBuilder().append("Gui name (").append(Utils.getGuiName()).append(") did not match \"Permanent Upgrade\" or \"Are you sure?\"")));
                    this.finishTaskChain();
                }
            }
            else {
                this.successfulAssignment();
            }
        }
        else if (this.pair.getItem() instanceof Perks.KillStreak) {
            if (this.phase == 0) {
                if (Utils.isGuiName("Permanent upgrades")) {
                    ClickHelper.clickOnSlot(((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots.getSlot(this.data.hasFourthPerk() ? 11 : 15).slotNumber);
                    this.addNextTask();
                }
                else {
                    Logger.error(String.valueOf(new StringBuilder().append("Gui name (").append(Utils.getGuiName()).append(") did not match \"Permanent Upgrades\"")));
                    this.finishTaskChain();
                }
            }
            else if (this.phase == 1) {
                if (Utils.isGuiName("Killstreaks")) {
                    final Container field_147002_h3 = ((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots;
                    Slot slot3 = null;
                    if (this.canKillstreakChange(this.data.getKillStreak1())) {
                        this.num = 1;
                        slot3 = field_147002_h3.getSlot(11);
                    }
                    else if (this.canKillstreakChange(this.data.getKillStreak2())) {
                        this.num = 2;
                        slot3 = field_147002_h3.getSlot(13);
                    }
                    if (slot3 != null) {
                        ClickHelper.clickOnSlot(slot3.slotNumber);
                        this.addNextTask();
                    }
                    else {
                        Logger.error(String.valueOf(new StringBuilder().append("Could not find a free killstreak slot? (Current Killstreaks: ").append(this.data.getKillStreak1()).append(", ").append(this.data.getKillStreak2()).append(")")));
                        this.finishTaskChain();
                    }
                }
                else {
                    Logger.error(String.valueOf(new StringBuilder().append("Gui name (").append(Utils.getGuiName()).append(") did not match \"Killstreaks\"")));
                    this.finishTaskChain();
                }
            }
            else if (this.phase == 2) {
                if (Utils.isGuiName("Choose a killstreak")) {
                    final Container field_147002_h4 = ((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots;
                    boolean b2 = false;
                    for (final Slot slot4 : field_147002_h4.inventorySlots) {
                        final ItemStack func_75211_c2 = slot4.getStack();
                        if (func_75211_c2 != null && func_75211_c2.getItem() == Item.getItemById(((Perks.KillStreak)this.pair.getItem()).getItemId())) {
                            b2 = true;
                            ClickHelper.clickOnSlot(slot4.slotNumber);
                            this.addNextTask();
                            break;
                        }
                    }
                    if (!b2) {
                        Logger.error("Could not find perk in purchase menu?");
                        this.finishTaskChain();
                    }
                }
                else {
                    Logger.error(String.valueOf(new StringBuilder().append("Gui name (").append(Utils.getGuiName()).append(") did not match \"Choose a killstreak\"")));
                    this.finishTaskChain();
                }
            }
            else if (this.phase == 3) {
                if (Utils.isGuiName("Killstreaks")) {
                    this.successfulAssignment();
                }
                else if (Utils.isGuiName("Are you sure?")) {
                    ClickHelper.clickOnSlot(((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots.getSlot(11).slotNumber);
                    this.addNextTask();
                }
                else {
                    Logger.error(String.valueOf(new StringBuilder().append("Gui name (").append(Utils.getGuiName()).append(") did not match \"Killstreaks\" or \"Are you sure?\"")));
                    this.finishTaskChain();
                }
            }
            else {
                this.successfulAssignment();
            }
        }
        else if (this.pair.getItem() instanceof Perks.Passive) {
            if (this.phase == 0) {
                if (Utils.isGuiName("Permanent upgrades")) {
                    ClickHelper.clickOnSlot(((Perks.Passive)this.pair.getItem()).getLocation());
                    this.addNextTask();
                }
                else {
                    Logger.error(String.valueOf(new StringBuilder().append("Gui name (").append(Utils.getGuiName()).append(") did not match \"Permanent Upgrades\"")));
                    this.finishTaskChain();
                }
            }
            else if (this.phase == 1) {
                if (Utils.isGuiName("Permanent upgrades")) {
                    this.successfulAssignment();
                }
                else if (Utils.isGuiName("Are you sure?")) {
                    ClickHelper.clickOnSlot(((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots.getSlot(11).slotNumber);
                    this.addNextTask();
                }
                else {
                    Logger.error(String.valueOf(new StringBuilder().append("Gui name (").append(Utils.getGuiName()).append(") did not match \"Permanent Upgrade\" or \"Are you sure?\"")));
                    this.finishTaskChain();
                }
            }
            else {
                this.successfulAssignment();
            }
        }
        else {
            Logger.error("Item was not an instance of any upgrade?");
            this.finishTaskChain();
        }
    }
    
    public BuyTask(final int n, final int phase, final UpgradePair pair, final PerkData data, final int num) {
        super(n);
        this.phase = phase;
        this.pair = pair;
        this.data = data;
        this.num = num;
    }
    
    private void finishTaskChain() {
        if (Minecraft.getMinecraft().currentScreen != null) {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
        AutoGrinder.getInstance().getDriver().finishedTask();
    }
    
    private boolean canKillstreakChange(final Perks.KillStreak killStreak) {
        return killStreak == Perks.KillStreak.NONE || killStreak == Perks.KillStreak.OTHER;
    }
    
    private int getRandomDelay() {
        return (int)(Math.floor(Math.random() * 10.0) + 10.0);
    }
    
    private void addTask(final int n) {
        AutoGrinder.getInstance().getScheduler().addTask(new BuyTask(this.getRandomDelay(), n, this.pair, this.data, this.num));
    }
    
    private void addNextTask() {
        this.addTask(this.phase + 1);
    }
    
    private void successfulAssignment() {
        Logger.info(String.valueOf(new StringBuilder().append("Successfully bought ").append(this.pair.getItem()).append((this.pair.getItem() instanceof Perks.Passive) ? String.valueOf(new StringBuilder().append(" ").append(this.pair.getLevel())) : "")));
        AutoGrinder.getInstance().getDriver().onSuccessfulPerk(this.pair.getItem(), this.pair.getLevel(), this.num);
        this.finishTaskChain();
    }
    
    public BuyTask(final UpgradePair upgradePair, final PerkData perkData) {
        this(10, 0, upgradePair, perkData, -1);
    }
    
    private boolean canPerkChange(final Perks.Perk perk) {
        return perk == Perks.Perk.NONE || perk == Perks.Perk.OTHER;
    }
}
