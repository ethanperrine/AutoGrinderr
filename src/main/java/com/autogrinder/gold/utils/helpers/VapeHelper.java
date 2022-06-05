// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils.helpers;

import com.autogrinder.gold.commands.ToggleCommand;
import com.autogrinder.gold.AutoGrinder;
import com.autogrinder.gold.utils.Logger;

public class VapeHelper
{
    public static void turnOnKA() {
        Logger.info("Turning on KA.");
        AutoGrinder.getInstance().getKeyBuffer().addKey(ToggleCommand.kaOn);
    }
    
    public static void toggleKA() {
        Logger.info("Toggling KA.");
        AutoGrinder.getInstance().getKeyBuffer().addKey(ToggleCommand.kaToggle);
    }
    
    public static void turnOffKA() {
        Logger.info("Turning off KA.");
        AutoGrinder.getInstance().getKeyBuffer().addKey(ToggleCommand.kaOff);
    }
}
