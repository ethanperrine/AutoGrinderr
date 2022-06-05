// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.schedulers;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.autogrinder.gold.schedulers.tasks.Task;
import java.util.ArrayList;

public class Scheduler
{
    private final ArrayList<Task> toRemove;
    private final ArrayList<Task> tasks;
    private final ArrayList<Task> toAdd;
    
    public void addTask(final Task task) {
        this.toAdd.add(task);
    }
    
    public void removeTask(final Task task) {
        this.toRemove.add(task);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTick(final TickEvent.ClientTickEvent clientTickEvent) {
        if (Minecraft.getMinecraft().thePlayer == null) {
            return;
        }
        if (clientTickEvent.phase != TickEvent.Phase.START) {
            return;
        }
        for (final Task task : this.tasks) {
            task.decrementTicks();
            if (task.getTicksLeft() <= 0) {
                task.run();
                this.toRemove.add(task);
            }
        }
        this.tasks.removeAll(this.toRemove);
        this.tasks.addAll(this.toAdd);
        this.toRemove.clear();
        this.toAdd.clear();
    }
    
    public Scheduler() {
        this.tasks = new ArrayList<Task>();
        this.toRemove = new ArrayList<Task>();
        this.toAdd = new ArrayList<Task>();
    }
}
