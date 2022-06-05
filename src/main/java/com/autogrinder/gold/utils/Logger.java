// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils;

public class Logger
{
    public static void info(final String s) {
        System.out.println(String.valueOf(new StringBuilder().append("[AutoGrinder/INFO]: ").append(s)));
    }
    
    public static void error(final String s) {
        System.out.println(String.valueOf(new StringBuilder().append("[AutoGrinder/ERROR]: ").append(s)));
    }
}
