// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

public class CameraHelper
{
    private Runnable callback;
    private boolean yawRunning;
    private double targetYaw;
    private boolean pitchRunning;
    private double targetPitch;
    
    public void lookAt(final Vec3 vec3, final Runnable runnable) {
        this.lookAt(vec3, false, runnable);
    }
    
    public void look(final double targetYaw, final double targetPitch, final boolean b, final Runnable callback) {
        if (this.pitchRunning || this.yawRunning) {
            this.finish();
        }
        this.targetYaw = targetYaw;
        this.targetPitch = targetPitch;
        this.callback = callback;
        this.yawRunning = true;
        this.pitchRunning = true;
        if (b) {
            final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            ((EntityPlayer)thePlayer).rotationYaw = (float)targetYaw;
            ((EntityPlayer)thePlayer).rotationPitch = (float)targetPitch;
            this.finish();
        }
    }
    
    private double getMaxSpeed(double abs) {
        abs = Math.abs(abs);
        if (abs > 40.0) {
            return 20.0;
        }
        return 3.0 * abs / 10.0 + 5.0;
    }
    
    public void look(final double n, final double n2, final Runnable runnable) {
        this.look(n, n2, false, runnable);
    }
    
    public CameraHelper() {
        this.yawRunning = false;
        this.pitchRunning = false;
        this.callback = null;
    }
    
    public void look(final double n, final double n2) {
        this.look(n, n2, false);
    }
    
    private double deltaPitch(final double n, final double n2) {
        return n - n2;
    }
    
    private double deltaYaw(final double n, final double n2) {
        double n3 = (n - n2) % 360.0;
        if (n3 < -180.0) {
            n3 += 360.0;
        }
        if (n3 > 180.0) {
            n3 -= 360.0;
        }
        return n3;
    }
    
    public void tick() {
        final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        boolean b = false;
        if (this.yawRunning) {
            final double deltaYaw = this.deltaYaw(this.targetYaw, ((EntityPlayer)thePlayer).rotationYaw);
            final double maxSpeed = this.getMaxSpeed(deltaYaw);
            double n;
            for (n = ((EntityPlayer)thePlayer).rotationYaw + this.clamp(deltaYaw, -maxSpeed, maxSpeed); n < -180.0; n += 360.0) {}
            while (n > 180.0) {
                n -= 360.0;
            }
            ((EntityPlayer)thePlayer).rotationYaw = (float)n;
            if (Math.abs(this.deltaYaw(((EntityPlayer)thePlayer).rotationYaw, this.targetYaw)) < 1.0) {
                ((EntityPlayer)thePlayer).rotationYaw = (float)this.targetYaw;
                this.yawRunning = false;
                b = true;
            }
        }
        if (this.pitchRunning) {
            final double deltaPitch = this.deltaPitch(this.targetPitch, ((EntityPlayer)thePlayer).rotationPitch);
            final double maxSpeed2 = this.getMaxSpeed(deltaPitch);
            final double clamp = this.clamp(deltaPitch, -maxSpeed2, maxSpeed2);
            final EntityPlayerSP entityPlayerSP = thePlayer;
            ((EntityPlayer)entityPlayerSP).rotationPitch += (float)clamp;
            if (Math.abs(this.deltaYaw(((EntityPlayer)thePlayer).rotationPitch, this.targetPitch)) < 1.0) {
                ((EntityPlayer)thePlayer).rotationPitch = (float)this.targetPitch;
                this.pitchRunning = false;
                b = true;
            }
        }
        if (b && !this.isRunning()) {
            this.finish();
        }
    }
    
    public void cancel() {
        this.yawRunning = false;
        this.pitchRunning = false;
    }
    
    public void lookAt(final Vec3 vec3) {
        this.lookAt(vec3, false);
    }
    
    public void lookAt(final Vec3 vec3, final boolean b, final Runnable runnable) {
        final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        final Vec3 subtract = vec3.subtract(((EntityPlayer)thePlayer).posX, ((EntityPlayer)thePlayer).posY + ((EntityPlayer)thePlayer).eyeHeight, ((EntityPlayer)thePlayer).posZ);
        this.look(Math.toDegrees(MathHelper.atan2(-subtract.xCoord, subtract.zCoord)), Math.toDegrees(Math.atan2(-subtract.yCoord, MathHelper.sqrt_double(subtract.xCoord * subtract.xCoord + subtract.zCoord * subtract.zCoord))), b, runnable);
    }
    
    public void lookAtEntityNatural(final Entity entity) {
        final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        final Vec3 subtract = new Vec3(entity.posX, entity.posY + 1.0, entity.posZ).subtract(((EntityPlayer)thePlayer).posX, ((EntityPlayer)thePlayer).posY + ((EntityPlayer)thePlayer).eyeHeight, ((EntityPlayer)thePlayer).posZ);
        final double atan2 = MathHelper.atan2(-subtract.xCoord, subtract.zCoord);
        final double n = MathHelper.sqrt_double(subtract.xCoord * subtract.xCoord + subtract.zCoord * subtract.zCoord);
        final double degrees = Math.toDegrees(Math.atan2(-(subtract.yCoord + 0.45), n));
        final double degrees2 = Math.toDegrees(Math.atan2(-(subtract.yCoord + 1.5), n));
        final double n2 = ((EntityPlayer)thePlayer).rotationPitch;
        this.look(Math.toDegrees(atan2), this.clamp(this.clamp(n2 + ((n2 > degrees || n2 < degrees2) ? (((Math.random() < 0.5) ? -1 : 1) * (Math.random() * 8.0)) : 0.0), degrees2, degrees), -5.0, 50.0), false, null);
    }
    
    public void look(final double n, final double n2, final boolean b) {
        this.look(n, n2, b, null);
    }
    
    public void lookAtXZ(final double n, final double n2) {
        this.lookAtXZ(n, n2, null);
    }
    
    private double clamp(final double n, final double n2, final double n3) {
        return Math.max(Math.min(n, n3), n2);
    }
    
    public void lookAt(final Vec3 vec3, final boolean b) {
        this.lookAt(vec3, b, null);
    }
    
    public void lookAtXZ(final double n, final double n2, final Runnable runnable) {
        final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        this.look(Math.toDegrees(MathHelper.atan2(((EntityPlayer)thePlayer).posX - n, n2 - ((EntityPlayer)thePlayer).posZ)), ((EntityPlayer)thePlayer).rotationPitch, runnable);
    }
    
    public void lookAtXZ(final Vec3 vec3, final Runnable runnable) {
        this.lookAtXZ(vec3.xCoord, vec3.zCoord, runnable);
    }
    
    public void finish() {
        if (this.callback != null) {
            this.callback.run();
        }
        this.yawRunning = false;
        this.pitchRunning = false;
    }
    
    public boolean isRunning() {
        return this.yawRunning || this.pitchRunning;
    }
}
