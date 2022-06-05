// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils.helpers;

import net.minecraft.entity.player.EntityPlayer;
import com.autogrinder.gold.AutoGrinder;
import net.minecraft.client.entity.EntityPlayerSP;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import com.autogrinder.gold.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.Minecraft;
import com.autogrinder.gold.utils.Setting;
import net.minecraft.util.Vec3;
import java.util.ArrayList;

public class MovementHelper
{
    private int stuckTicksLeft;
    private Runnable onError;
    private int pathIndex;
    private Runnable callback;
    private boolean running;
    private boolean forwards;
    private final ArrayList<Vec3> path;
    private boolean jump;
    
    public void goToXZ(final double n, final double n2) {
        this.goToXZ(n, n2, null, null);
    }
    
    public void setJump(final boolean b) {
        this.jump = (b && Setting.shouldJump.isEnabled());
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), this.jump);
    }
    
    public MovementHelper() {
        this.forwards = false;
        this.jump = false;
        this.running = false;
        this.path = new ArrayList<Vec3>();
        this.pathIndex = 0;
        this.stuckTicksLeft = 0;
    }
    
    public void reapplyKeybindings() {
        this.setForwards(this.forwards);
        this.setJump(this.jump);
    }
    
    private void lambda$tick$0() {
        this.setForwards(true);
    }
    
    public void cancel() {
        if (this.running) {
            this.running = false;
            this.clearControlStates();
            if (this.onError != null) {
                this.onError.run();
            }
        }
    }
    
    public void finish() {
        if (this.running) {
            this.running = false;
            this.clearControlStates();
            if (this.callback != null) {
                this.callback.run();
            }
        }
    }
    
    public void calculatePath(final double n, final double n2) {
        this.path.clear();
        this.pathIndex = 0;
        final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        this.path.add(new Vec3(((EntityPlayer)thePlayer).posX, -1.0, ((EntityPlayer)thePlayer).posZ));
        if (Utils.getDist2D((Entity)Minecraft.getMinecraft().thePlayer, n, n2) < 1.3) {
            this.path.add(new Vec3(n, -1.0, n2));
            return;
        }
        final ArrayList arrayList = Lists.newArrayList((Object[])new Vec3[] { new Vec3(9.0, -1.0, 0.0), new Vec3(7.0, -1.0, -7.0), new Vec3(0.0, -1.0, -9.0), new Vec3(-7.0, -1.0, -7.0), new Vec3(-9.0, -1.0, 0.0), new Vec3(-7.0, -1.0, 7.0), new Vec3(0.0, -1.0, 9.0), new Vec3(7.0, -1.0, 7.0) });
        double n3 = 100.0;
        int n4 = -1;
        double n5 = 100.0;
        int n6 = -1;
        int i = 0;
        while (i < arrayList.size()) {
            final Vec3 vec3 = (Vec3)arrayList.get(i);
            final double dist2D = Utils.getDist2D((Entity)thePlayer, vec3);
            if (dist2D < n3) {
                n3 = dist2D;
                n4 = i;
            }
            if (i % 2 == 0) {
                final double dist2D2 = Utils.getDist2D(n, n2, vec3.xCoord, vec3.zCoord);
                if (dist2D2 < n5) {
                    n5 = dist2D2;
                    n6 = i;
                }
            }
            final int n7 = 0;
            final int n8 = -1;
            ++i;
            if (n7 < n8) {
                return;
            }
        }
        final boolean b = (n6 + 8 - n4) % 8 < 4;
        final int n9 = b ? 1 : -1;
        if (b && n6 < n4) {
            n6 += 8;
        }
        if (!b && n4 < n6) {
            n4 += 8;
        }
        for (int j = n4; j != n6; j += n9) {
            this.path.add((Vec3)arrayList.get(j % 8));
        }
        this.path.add((Vec3)arrayList.get(n6 % 8));
        this.path.add(new Vec3(n, -1.0, n2));
    }
    
    public void recalculatePath() {
        if (this.path.size() == 0) {
            return;
        }
        final Vec3 vec3 = this.path.get(this.path.size() - 1);
        this.calculatePath(vec3.xCoord, vec3.zCoord);
    }
    
    public void goToXZ(final Vec3 vec3) {
        this.goToXZ(vec3.xCoord, vec3.zCoord, null, null);
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public void goToXZ(final double n, final double n2, final Runnable callback, final Runnable onError) {
        if (this.running) {
            this.cancel();
        }
        this.clearControlStates();
        this.running = true;
        this.stuckTicksLeft = 400;
        this.callback = callback;
        this.onError = onError;
        this.calculatePath(n, n2);
    }
    
    public void setForwards(final boolean forwards) {
        this.forwards = forwards;
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), forwards);
    }
    
    public void tick() {
        if (this.running) {
            --this.stuckTicksLeft;
            boolean b = false;
            final double dist2D = Utils.getDist2D((Entity)Minecraft.getMinecraft().thePlayer, this.path.get(this.pathIndex));
            final double n = (this.pathIndex + 1 >= this.path.size()) ? 1.0 : 0.5;
            if (this.pathIndex < this.path.size() && dist2D < n) {
                ++this.pathIndex;
                this.stuckTicksLeft = 400;
                b = true;
            }
            if (this.pathIndex < this.path.size()) {
                final CameraHelper cameraHelper = AutoGrinder.getInstance().getDriver().getCameraHelper();
                if (b) {
                    this.clearControlStates();
                    final Vec3 vec3 = this.path.get(this.pathIndex);
                    cameraHelper.lookAtXZ(vec3.xCoord, vec3.zCoord, this::lambda$tick$0);
                }
                else if (!cameraHelper.isRunning()) {
                    final Vec3 vec4 = this.path.get(this.pathIndex);
                    cameraHelper.lookAtXZ(vec4.xCoord, vec4.zCoord);
                }
            }
            else {
                this.finish();
            }
            if (this.stuckTicksLeft <= 0) {
                this.cancel();
            }
        }
    }
    
    public void goToXZ(final Vec3 vec3, final Runnable runnable) {
        this.goToXZ(vec3.xCoord, vec3.zCoord, runnable, null);
    }
    
    public void goToXZ(final double n, final double n2, final Runnable runnable) {
        this.goToXZ(n, n2, runnable, null);
    }
    
    public void clearControlStates() {
        this.setForwards(false);
        this.setJump(false);
    }
    
    public void goToXZ(final Vec3 vec3, final Runnable runnable, final Runnable runnable2) {
        this.goToXZ(vec3.xCoord, vec3.zCoord, runnable, runnable2);
    }
}
