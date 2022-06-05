// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.commands;

import com.autogrinder.gold.AutoGrinder;
import com.autogrinder.gold.gui.ToggleGui;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class ToggleCommand extends CommandBase
{
    public static char kaToggle;
    public static int minKills;
    public static char kaOn;
    public static char kaOff;

    @Override
    public String getCommandName() {
        return "grinderConfig";
    }

    @Override
    public String getCommandUsage(final ICommandSender commandSender) {
        return String.valueOf(new StringBuilder().append("/").append(this.getCommandName()));
    }

    @Override
    public void processCommand(final ICommandSender commandSender, final String[] array) throws CommandException {
        AutoGrinder.getInstance().setGuiToOpen(new ToggleGui());
    }
}
