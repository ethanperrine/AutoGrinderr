// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class HWID
{
    public static String byteArrayToHexString(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        final int length = array.length;
        int i = 0;
        while (i < length) {
            sb.append(Integer.toString((array[i] & 0xFF) + 256, 16).substring(1));
            final int n = 3;
            final int n2 = 3;
            ++i;
            if (n != n2) {
                return null;
            }
        }
        return String.valueOf(sb);
    }
    
    public static String getHWID() {
        final StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("os.arch"));
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("os.arch"));
        sb.append(System.getProperty("os.arch"));
        sb.append(System.getProperty("os.version"));
        sb.append(System.getProperty("os.name"));
        sb.append(System.getProperty("os.arch"));
        sb.append(System.getProperty("os.version"));
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("user.home"));
        sb.append(System.getProperty("user.name"));
        sb.append(System.getProperty("os.arch"));
        sb.append(System.getProperty("os.version"));
        sb.append(System.getProperty("user.home"));
        try {
            return byteArrayToHexString(MessageDigest.getInstance("SHA-1").digest(String.valueOf(sb).getBytes()));
        }
        catch (final NoSuchAlgorithmException ex) {
            return "";
        }
    }
}
