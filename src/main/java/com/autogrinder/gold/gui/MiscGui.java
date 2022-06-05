// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.gui;

import com.autogrinder.gold.utils.Setting;
import net.minecraft.client.gui.GuiLabel;
import com.autogrinder.gold.utils.Logger;
import com.autogrinder.gold.commands.ToggleCommand;
import java.io.IOException;
import com.autogrinder.gold.AutoGrinder;
import com.autogrinder.gold.gui.widget.FeatureButton;
import net.minecraft.client.gui.GuiButton;
import com.autogrinder.gold.gui.widget.KeybindButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class MiscGui extends GuiScreen
{
    private GuiTextField minKills;
    private KeybindButton vapeToggle;
    private KeybindButton vapeOn;
    private KeybindButton vapeOff;
    
    protected void actionPerformed(final GuiButton guiButton) throws IOException {
        if (guiButton instanceof FeatureButton) {
            ((FeatureButton)guiButton).onClick();
        }
        else {
            if (guiButton.id == 0) {
                AutoGrinder.getInstance().setGuiToOpen(new ToggleGui());
            }
        }
    }
    
    public void onGuiClosed() {
        ToggleCommand.kaToggle = this.vapeToggle.getCurrentChar();
        ToggleCommand.kaOff = this.vapeOff.getCurrentChar();
        ToggleCommand.kaOn = this.vapeOn.getCurrentChar();
        try {
            ToggleCommand.minKills = Integer.parseInt(this.minKills.getText());
        }
        catch (final NumberFormatException ex) {
            Logger.error("Could not save min kills");
        }
        AutoGrinder.getInstance().getConfiguration().setValue("kaToggle", ToggleCommand.kaToggle);
        AutoGrinder.getInstance().getConfiguration().setValue("kaOff", ToggleCommand.kaOff);
        AutoGrinder.getInstance().getConfiguration().setValue("minKills", ToggleCommand.minKills);
        AutoGrinder.getInstance().getConfiguration().setValue("kaOn", ToggleCommand.kaOn);
        super.onGuiClosed();
    }
    
    public void initGui() {
        this.labelList.clear();
        this.labelList.add(new GuiLabel(this.fontRendererObj, -1, this.width / 2 - 100, 12, 200, 10, -1));
        this.labelList.add(new GuiLabel(this.fontRendererObj, -1, this.width / 2 - 100, 42, 200, 10, -1));
        this.labelList.add(new GuiLabel(this.fontRendererObj, -1, this.width / 2 - 100, 72, 200, 10, -1));
        this.labelList.get(0).func_175202_a("Min Kills: ");
        this.labelList.get(1).func_175202_a(Setting.useProfiles.isEnabled() ? "Vape On: " : "Vape Toggle: ");
        this.labelList.get(2).func_175202_a("Vape Off: ");
        final int func_78256_a = this.fontRendererObj.getStringWidth("Vape Toggle: ");
        final int func_78256_a2 = this.fontRendererObj.getStringWidth("Min Kills: ");
        this.vapeToggle = new KeybindButton(-1, this.width / 2 - 90 + func_78256_a, 10, 200 - func_78256_a, 20, ToggleCommand.kaToggle);
        this.vapeOff = new KeybindButton(-1, this.width / 2 - 90 + func_78256_a, 40, 200 - func_78256_a, 20, ToggleCommand.kaOff);
        this.vapeOn = new KeybindButton(-1, this.width / 2 - 90 + func_78256_a, 70, 200 - func_78256_a, 20, ToggleCommand.kaOn);
        this.vapeOn.enabled = Setting.useProfiles.isEnabled();
        this.vapeToggle.enabled = !Setting.useProfiles.isEnabled();
        this.buttonList.add(Setting.useProfiles.isEnabled() ? this.vapeOn : this.vapeToggle);
        this.buttonList.add(this.vapeOff);
        (this.minKills = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 90 + func_78256_a2, 10, 200 - func_78256_a2, 20)).setText(String.valueOf(ToggleCommand.minKills));
        this.buttonList.add(new GuiButton(0, 10, this.height - 30, 20, 20, "Back"));
    }
    
    protected void mouseClicked(final int n, final int n2, final int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        this.vapeOff.mouseClicked(n, n2);
        this.vapeToggle.mouseClicked(n, n2);
        this.vapeOn.mouseClicked(n, n2);
        this.minKills.setFocused(this.minKills.xPosition < n && n < this.minKills.xPosition + this.minKills.getWidth() && this.minKills.yPosition < n2 && n2 < this.minKills.yPosition + this.minKills.height);
    }
    
    protected void keyTyped(final char c, final int n) throws IOException {
        super.keyTyped(c, n);
        this.vapeOff.keyTyped(c);
        this.vapeToggle.keyTyped(c);
        this.vapeOn.keyTyped(c);
        if (this.minKills.isFocused() && !Character.isAlphabetic(c)) {
            this.minKills.textboxKeyTyped(c, n);
        }
    }
    
    public void drawScreen(final int n, final int n2, final float n3) {
        this.drawDefaultBackground();
        this.minKills.drawTextBox();
        super.drawScreen(n, n2, n3);
    }
}
