// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold;

import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import com.autogrinder.gold.commands.ToggleCommand;
import com.autogrinder.gold.commands.StartCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import java.util.concurrent.ExecutorService;
import net.minecraft.client.gui.GuiScreen;
import com.autogrinder.gold.schedulers.Scheduler;
//import com.autogrinder.gold.remote.CommandExecutor;
import com.autogrinder.gold.utils.Configuration;
import com.autogrinder.gold.schedulers.KeyBuffer;
import net.minecraftforge.fml.common.Mod;



@Mod(modid = "AutoGrinder", acceptedMinecraftVersions = "[1.8.9]")
public class AutoGrinder
{
    private KeyBuffer keyBuffer;
    private String title;
    private static AutoGrinder instance;
    private Driver driver;
    private Configuration configuration;
    //private CommandExecutor commandExecutor;
    private Scheduler scheduler;
    private boolean enabled;
    private GuiScreen guiToOpen;
    public static final ExecutorService THREAD_POOL;
    
    /*public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }

     */
    
    public static AutoGrinder getInstance() {
        return AutoGrinder.instance;
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent fmlInitializationEvent) {
        AutoGrinder.instance = this;
        this.driver = new Driver();
        this.configuration = new Configuration();
        this.scheduler = new Scheduler();
        this.keyBuffer = new KeyBuffer();
        //this.commandExecutor = new CommandExecutor();

        MinecraftForge.EVENT_BUS.register((Object)this);
        //MinecraftForge.EVENT_BUS.register((Object)this.commandExecutor);
        MinecraftForge.EVENT_BUS.register((Object)this.scheduler);
        this.enabled = true;
        if (this.enabled) {
            MinecraftForge.EVENT_BUS.register(this.driver);
            MinecraftForge.EVENT_BUS.register(this.keyBuffer);


            ClientCommandHandler.instance.registerCommand((new StartCommand()));
            ClientCommandHandler.instance.registerCommand((new ToggleCommand()));
        }
    }

    @SubscribeEvent
    public void onGuiScreenLoad(final GuiOpenEvent guiOpenEvent) {
        if (this.enabled) {
            this.title = String.valueOf(new StringBuilder().append("Minecraft - ").append(Minecraft.getMinecraft().getSession().getUsername()));
        }
        Display.setTitle(this.title);
    }
    
    public Driver getDriver() {
        return this.driver;
    }
    
    public void setGuiToOpen(final GuiScreen guiToOpen) {
        this.guiToOpen = guiToOpen;
    }
    
    static {
        THREAD_POOL = Executors.newCachedThreadPool(new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            
            @Override
            public Thread newThread(final Runnable runnable) {
                return new Thread(runnable, String.valueOf(new StringBuilder().append("oofspitaddons").append(this.threadNumber.getAndIncrement())));
            }
        });
    }
    
    public KeyBuffer getKeyBuffer() {
        return this.keyBuffer;
    }
    
    @SubscribeEvent
    public void displayGui(final TickEvent.RenderTickEvent renderTickEvent) {
        if (this.guiToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(this.guiToOpen);
            this.guiToOpen = null;
        }
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public Scheduler getScheduler() {
        return this.scheduler;
    }
    
    public AutoGrinder() {
        this.guiToOpen = null;
        this.enabled = true;
    }
}
