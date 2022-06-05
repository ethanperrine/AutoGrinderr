// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.gui;

import com.autogrinder.gold.AutoGrinder;
import com.autogrinder.gold.gui.widget.FeatureButton;
import com.autogrinder.gold.utils.Setting;
import net.minecraft.client.gui.GuiButton;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class ToggleGui extends GuiScreen
{
    private final int size;
    private final ArrayList<GuiButton> allButtons;
    private int page;
    
    public void initGui() {
        this.buttonList.clear();
        final int max = Math.max((this.height - 40) / 30, 1);
        final int max2 = Math.max((this.width - 10) / 210, 1);
        final int n = (this.width - max2 * 210 + 10) / 2;
        final boolean b = this.page > 0;
        final boolean b2 = max * max2 * (this.page + 1) < this.size;
        if (b) {
            this.buttonList.add(new GuiButton(0, 80, this.height - 30, 80, 20, "<<<"));
        }
        if (b2) {
            this.buttonList.add(new GuiButton(1, this.width - 160, this.height - 30, 80, 20, ">>>"));
        }
        int i = 0;
        while (i < max) {
            int j = 0;
            while (j < max2) {
                final int n2 = max * max2 * this.page + i * max2 + j;
                if (n2 >= this.size) {
                    break;
                }
                final GuiButton guiButton = this.allButtons.get(n2);
                guiButton.xPosition = n + 210 * j;
                guiButton.yPosition = 10 + 30 * i;
                this.buttonList.add(guiButton);
                final int n3 = 2;
                final int n4 = 1;
                ++j;
                if (n3 <= n4) {
                    return;
                }
            }
            final Object o = null;
            ++i;
            if (o != null) {
                return;
            }
        }
    }
    
    public ToggleGui() {
        this.allButtons = new ArrayList<GuiButton>();
        this.page = 0;
        this.allButtons.add(new GuiButton(2, 10, 192, "Auto Perk: Open GUI"));
        this.allButtons.add(new GuiButton(3, 220, 160, "Misc Options: Open GUI"));
        this.allButtons.add(new FeatureButton(0, 0, "Auto Play Pit", Setting.playPit));
        this.allButtons.add(new FeatureButton(0, 0, "Auto Reconnect", Setting.autoReconnect));
        this.allButtons.add(new FeatureButton(0, 0, "Auto Prestige", Setting.autoPrestige));
        this.allButtons.add(new FeatureButton(0, 0, "Target Diamonds", Setting.targetDiamonds));
        this.allButtons.add(new FeatureButton(0, 0, "Use Profiles", Setting.useProfiles));
        this.allButtons.add(new FeatureButton(0, 0, "Min Players Check", Setting.minPlayersInLobby));
        this.allButtons.add(new FeatureButton(0, 0, "Mystic Pathfinding", Setting.mysticPathfinding));
        this.allButtons.add(new FeatureButton(0, 0, "Jump", Setting.shouldJump));
        this.allButtons.add(new FeatureButton(0, 0, "Show Target (Beta)", Setting.showTarget));
        this.size = this.allButtons.size();
    }
    
    public void drawScreen(final int n, final int n2, final float n3) {
        this.drawDefaultBackground();
        super.drawScreen(n, n2, n3);
    }
    
    protected void actionPerformed(final GuiButton guiButton) {
        if (guiButton instanceof FeatureButton) {
            ((FeatureButton)guiButton).onClick();
        }
        else {
            switch (guiButton.id) {
                case 0: {
                    --this.page;
                    this.initGui();
                    break;
                }
                case 1: {
                    ++this.page;
                    this.initGui();
                    break;
                }
                case 2: {
                    AutoGrinder.getInstance().setGuiToOpen(new PerkGui());
                    break;
                }
                case 3: {
                    AutoGrinder.getInstance().setGuiToOpen(new MiscGui());
                    break;
                }
            }
        }
    }
}
