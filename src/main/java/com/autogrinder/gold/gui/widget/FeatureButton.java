// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.gui.widget;

import com.autogrinder.gold.AutoGrinder;
import com.autogrinder.gold.utils.Setting;
import net.minecraft.client.gui.GuiButton;

public class FeatureButton extends GuiButton
{
    private final Setting setting;
    private boolean toggled;
    private String baseText;
    
    public void onClick() {
        this.toggled = !this.toggled;
        this.baseText = getButtonText(this.baseText, this.toggled);
        this.setting.setEnabled(this.toggled);
        AutoGrinder.getInstance().getConfiguration().setValue(this.setting.toString(), this.toggled);
    }
    
    private static String getButtonText(final String s, final boolean b) {
        return String.valueOf(new StringBuilder().append(s).append(": ").append(b ? "On" : "Off"));
    }
    
    public FeatureButton(final int n, final int n2, final String baseText, final Setting setting) {
        super(-1, n, n2, getButtonText(baseText, setting.isEnabled()));
        this.baseText = baseText;
        this.setting = setting;
        this.toggled = setting.isEnabled();
    }
}
