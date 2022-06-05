// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.schedulers.tasks;

import java.util.Iterator;

import com.autogrinder.gold.utils.Logger;
import net.minecraft.entity.player.EntityPlayer;
import java.util.regex.Matcher;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Container;
import com.autogrinder.gold.AutoGrinder;
import com.autogrinder.gold.utils.helpers.ClickHelper;
import java.util.regex.Pattern;

import com.autogrinder.gold.utils.data.enums.Perks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import com.autogrinder.gold.utils.Utils;
import com.autogrinder.gold.utils.data.PerkData;

public class CheckTask extends Task
{
    private final int phase;
    private final PerkData data;
    
    public CheckTask(final int n, final int phase, final PerkData data) {
        super(n);
        this.phase = phase;
        this.data = data;
    }
    
    @Override
    public void run() {
        if (this.phase == 0) {
            if (Utils.isGuiName("Permanent upgrades")) {
                final Container field_147002_h = ((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots;
                final ItemStack func_75211_c = field_147002_h.getSlot(12).getStack();
                final ItemStack func_75211_c2 = field_147002_h.getSlot(13).getStack();
                final ItemStack func_75211_c3 = field_147002_h.getSlot(14).getStack();
                this.data.setPerk1(Perks.Perk.getItemFromItemstack(func_75211_c));
                this.data.setPerk2(Perks.Perk.getItemFromItemstack(func_75211_c2));
                this.data.setPerk3(Perks.Perk.getItemFromItemstack(func_75211_c3));
                this.data.setFourthPerk(field_147002_h.getSlot(11).getStack() != null);
                Logger.info(String.valueOf(new StringBuilder().append("Has fourth perk: ").append(this.data.hasFourthPerk())));
                if (this.data.hasFourthPerk()) {
                    this.data.setPerk4(Perks.Perk.getItemFromItemstack(field_147002_h.getSlot(15).getStack()));
                }
                final ItemStack func_75211_c4 = field_147002_h.getSlot(28).getStack();
                final ItemStack func_75211_c5 = field_147002_h.getSlot(29).getStack();
                final ItemStack func_75211_c6 = field_147002_h.getSlot(30).getStack();
                final ItemStack func_75211_c7 = field_147002_h.getSlot(32).getStack();
                final ItemStack func_75211_c8 = field_147002_h.getSlot(34).getStack();
                final Pattern compile = Pattern.compile("Tier: ([IVXLCDM]+)");
                if (func_75211_c4 != null) {
                    final Matcher matcher = compile.matcher(this.getToolTip(func_75211_c4));
                    this.data.setPassiveLevel(Perks.Passive.XP_BOOST, matcher.find() ? Utils.numeralToInt(matcher.group(1)) : 0);
                }
                if (func_75211_c5 != null) {
                    final Matcher matcher2 = compile.matcher(this.getToolTip(func_75211_c5));
                    this.data.setPassiveLevel(Perks.Passive.GOLD_BOOST, matcher2.find() ? Utils.numeralToInt(matcher2.group(1)) : 0);
                }
                if (func_75211_c6 != null) {
                    final Matcher matcher3 = compile.matcher(this.getToolTip(func_75211_c6));
                    this.data.setPassiveLevel(Perks.Passive.MELEE_DAMAGE, matcher3.find() ? Utils.numeralToInt(matcher3.group(1)) : 0);
                }
                if (func_75211_c7 != null) {
                    final Matcher matcher4 = compile.matcher(this.getToolTip(func_75211_c7));
                    this.data.setPassiveLevel(Perks.Passive.DAMAGE_REDUCTION, matcher4.find() ? Utils.numeralToInt(matcher4.group(1)) : 0);
                }
                if (func_75211_c8 != null) {
                    final Matcher matcher5 = compile.matcher(this.getToolTip(func_75211_c8));
                    this.data.setPassiveLevel(Perks.Passive.EL_GATO, matcher5.find() ? Utils.numeralToInt(matcher5.group(1)) : 0);
                }
                ClickHelper.clickOnSlot(field_147002_h.getSlot(this.data.hasFourthPerk() ? 11 : 15).slotNumber);
                AutoGrinder.getInstance().getScheduler().addTask(new CheckTask(15, 1, this.data));
            }
            else {
                this.finishTaskChain();
            }
        }
        else if (this.phase == 1) {
            if (Utils.isGuiName("Killstreaks")) {
                final Container field_147002_h2 = ((GuiChest)Minecraft.getMinecraft().currentScreen).inventorySlots;
                final ItemStack func_75211_c9 = field_147002_h2.getSlot(11).getStack();
                final ItemStack func_75211_c10 = field_147002_h2.getSlot(13).getStack();
                this.data.setKillStreak1(Perks.KillStreak.getItemFromItemstack(func_75211_c9));
                this.data.setKillStreak2(Perks.KillStreak.getItemFromItemstack(func_75211_c10));
            }
            AutoGrinder.getInstance().getScheduler().addTask(new CheckTask(5, 2, this.data));
        }
        else {
            this.finishTaskChain();
        }
    }
    
    private String getToolTip(final ItemStack itemStack) {
        if (itemStack == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final Iterator iterator = itemStack.getTooltip((EntityPlayer)Minecraft.getMinecraft().thePlayer, false).iterator();
        while (iterator.hasNext()) {
            sb.append((String)iterator.next()).append("\n");
        }
        return String.valueOf(sb).replaceAll("�.", "");
    }
    
    public CheckTask(final PerkData perkData) {
        this(10, 0, perkData);
    }
    
    private void finishTaskChain() {
        if (Minecraft.getMinecraft().currentScreen != null) {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
        AutoGrinder.getInstance().getDriver().finishedTask();
        Logger.info(String.valueOf(new StringBuilder().append("Perks: ").append(this.data.getPerk1()).append(", ").append(this.data.getPerk2()).append(", ").append(this.data.getPerk3()).append(", ").append(this.data.getPerk4())));
        Logger.info(String.valueOf(new StringBuilder().append("Killstreaks: ").append(this.data.getKillStreak1()).append(", ").append(this.data.getKillStreak2())));
    }
}
