// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.gui;

import com.autogrinder.gold.utils.Setting;
import java.io.IOException;
import com.autogrinder.gold.AutoGrinder;
import com.autogrinder.gold.gui.widget.FeatureButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class PerkGui extends GuiScreen
{
    public void drawScreen(final int n, final int n2, final float n3) {
        this.drawDefaultBackground();
        super.drawScreen(n, n2, n3);
    }
    
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
    
    public void initGui() {
        this.buttonList.add(new FeatureButton(this.width / 2 - 100, 10, "Auto Perk", Setting.autoPerk));
        this.buttonList.add(new FeatureButton(this.width / 2 - 100, 40, "Use Dirty", Setting.hasDirty));
        this.buttonList.add(new FeatureButton(this.width / 2 - 100, 70, "Has The Way", Setting.hasTheWay));
        this.buttonList.add(new FeatureButton(this.width / 2 - 100, 100, "Use Explicious", Setting.useExpKs));
        this.buttonList.add(new GuiButton(0, 10, this.height - 30, 20, 20, "Back"));
    }
}
