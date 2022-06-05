// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.schedulers;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.sun.jna.platform.win32.WinDef;
import org.lwjgl.opengl.Display;
import com.sun.jna.platform.win32.User32;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.ArrayList;

public class KeyBuffer
{
    private final ArrayList<KeyPair> keyBuffer;
    
    public KeyBuffer() {
        this.keyBuffer = new ArrayList<KeyPair>();
    }
    
    public void keyDown(final char c) {
        this.keyBuffer.add(new KeyPair(c, true));
    }
    
    public void addKey(final char c) {
        this.keyDown(c);
        this.keyUp(c);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTick(final TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase != TickEvent.Phase.START) {
            return;
        }
        if (Minecraft.getMinecraft().currentScreen != null) {
            return;
        }
        if (this.keyBuffer.size() == 0) {
            return;
        }
        final KeyPair keyPair = this.keyBuffer.remove(0);
        User32.INSTANCE.PostMessage(User32.INSTANCE.FindWindow((String)null, Display.getTitle()), keyPair.isKeyDown() ? 256 : 257, new WinDef.WPARAM((long)keyPair.getKey()), new WinDef.LPARAM(keyPair.isKeyDown() ? 0L : -1073741823L));
    }
    
    public void keyUp(final char c) {
        this.keyBuffer.add(new KeyPair(c, false));
    }
    
    static class KeyPair
    {
        private final boolean keyDown;
        private final char key;
        
        public boolean isKeyDown() {
            return this.keyDown;
        }
        
        public KeyPair(final char key, final boolean keyDown) {
            this.key = key;
            this.keyDown = keyDown;
        }
        
        public char getKey() {
            return this.key;
        }
    }
}
