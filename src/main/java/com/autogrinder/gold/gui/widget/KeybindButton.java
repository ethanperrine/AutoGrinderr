// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.gui.widget;

import net.minecraft.client.gui.GuiButton;

public class KeybindButton extends GuiButton
{
    private boolean shouldChange;
    private char currentChar;
    
    public void keyTyped(final char c) {
        if (this.shouldChange && Character.isAlphabetic(c)) {
            this.currentChar = Character.toUpperCase(c);
            this.shouldChange = false;
            this.displayString = String.valueOf(this.currentChar);
        }
    }
    
    public char getCurrentChar() {
        return this.currentChar;
    }
    
    public void mouseClicked(final int n, final int n2) {
        if (this.enabled && this.xPosition < n && n < this.xPosition + this.width && this.yPosition < n2 && n2 < this.yPosition + this.height) {
            this.shouldChange = !this.shouldChange;
            if (this.shouldChange) {
                this.displayString = ">  <";
            }
            else {
                this.displayString = String.valueOf(this.currentChar);
            }
        }
        else if (this.shouldChange) {
            this.shouldChange = false;
            this.displayString = String.valueOf(this.currentChar);
        }
    }
    
    public KeybindButton(final int n, final int n2, final int n3, final int n4, final int n5, final char currentChar) {
        super(n, n2, n3, n4, n5, String.valueOf(currentChar));
        this.shouldChange = false;
        this.currentChar = currentChar;
    }
}
