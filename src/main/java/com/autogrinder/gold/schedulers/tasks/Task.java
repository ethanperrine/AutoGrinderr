// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.schedulers.tasks;

import com.autogrinder.gold.Driver;

public abstract class Task implements Runnable
{
    int ticksLeft;
    
    public Task(Driver driver, final int ticksLeft) {
        this.ticksLeft = ticksLeft;
    }

    public Task(final int ticksLeft) {
        this.ticksLeft = ticksLeft;
    }

    public int getTicksLeft() {
        return this.ticksLeft;
    }
    
    public void decrementTicks() {
        --this.ticksLeft;
    }
}
