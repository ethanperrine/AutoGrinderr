// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold;

import com.autogrinder.gold.schedulers.tasks.BuyTask;
import com.autogrinder.gold.schedulers.tasks.CheckTask;
import com.autogrinder.gold.utils.data.enums.Perks;
import com.autogrinder.gold.utils.helpers.*;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.GuiOpenEvent;
import java.awt.Color;
import com.autogrinder.gold.schedulers.tasks.PrestigeTask;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import com.autogrinder.gold.commands.ToggleCommand;
import net.minecraft.util.MathHelper;
import com.autogrinder.gold.schedulers.Scheduler;
import java.util.List;
import java.util.regex.Matcher;
import com.autogrinder.gold.schedulers.tasks.Task;
import java.util.regex.Pattern;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiMainMenu;
import com.autogrinder.gold.utils.Logger;
import com.autogrinder.gold.utils.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.autogrinder.gold.utils.Setting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import com.autogrinder.gold.utils.data.PlayerData;
import com.autogrinder.gold.utils.data.enums.MapData;
import com.autogrinder.gold.utils.data.PerkData;
import net.minecraft.entity.Entity;
import com.autogrinder.gold.utils.data.enums.PitEvent;

public class Driver
{
    private boolean darkQuestInProgress;
    private final CameraHelper cameraHelper;
    private PitEvent currentEvent;
    private int numKills;
    private long lastRan;
    private String targetServer;
    private boolean playPit;
    private int prestigeCheckTicksLeft;
    private final MovementHelper movementHelper;
    private int reconnectTicks;
    private int keepAliveTicksLeft;
    private Entity target;
    private int playPitTicksLeft;
    private int numKillsCheckTicksLeft;
    private boolean canPrestige;
    private int queueStateChangeTicksLeft;
    private int targetSwitchTicksLeft;
    private State currentState;
    private int lobbyCheckTicks;
    private boolean guiOpened;
    private int oofTicksLeft;
    private int numRetriesLeft;
    private boolean shouldOof;
    private final PerkData perkData;
    private MapData currentMap;
    private int queuePitTicksLeft;
    private final PlayerData playerData;
    final Driver driver = this;
    
    private void lambda$stateSetter$3() {
        this.cameraHelper.lookAt(this.currentMap.getPerkLocation().addVector(0.0, 1.62, 0.0), this::lambda$null$2);
    }
    
    public void setTargetServer(final String targetServer) {
        this.targetServer = targetServer;
    }
    
    private boolean inSpawn() {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        return player != null && this.currentMap != null && player.posY > this.currentMap.getSpawnLevel() - 1;
    }
    
    static CameraHelper access$000(final Driver driver) {
        return driver.cameraHelper;
    }
    
    @SubscribeEvent
    public void autoRejoin(final TickEvent.ClientTickEvent clientTickEvent) {
        if (!this.isRunning()) {
            return;
        }
        if (Setting.autoReconnect.isEnabled()) {
            return;
        }
        if (clientTickEvent.phase != TickEvent.Phase.START) {
            return;
        }
        if (--this.reconnectTicks <= 0) {
            this.reconnectTicks = 200;
            if (Minecraft.getMinecraft().thePlayer == null) {
                new Thread(Driver::lambda$autoRejoin$5).start();
            }
        }
    }

    private void grinderOff() {
        this.currentState = State.IDLE;
        this.movementHelper.cancel();
        this.cameraHelper.cancel();
        this.movementHelper.clearControlStates();
        VapeHelper.turnOffKA();
        this.playPit = false;
        this.shouldOof = false;
        this.target = null;
        this.targetSwitchTicksLeft = 0;
        this.guiOpened = false;
        this.resetKillChecker();
        this.perkData.makePerksUnknown();
        this.canPrestige = true;
        this.lobbyCheckTicks = 100;
        this.keepAliveTicksLeft = 0;
    }

    private void stateTickers() {
        if (!this.canSwitchStates()) {
            return;
        }
        this.playerData.clearData();
        Utils.updatePlayerData(this.playerData);
        if (this.playerData.getLevel() == 120 && Setting.autoPrestige.isEnabled() && this.canSwitchStates() && this.canPrestige) {
            this.queueStateChange(State.WAITING_PRESTIGE);
        }
        if (this.canSwitchStates() && this.playerData.getLevel() >= 10 && Setting.autoPerk.isEnabled()) {
            if (!this.perkData.hasUnknown(this.playerData)) {
                while (this.perkData.shouldSkipPerk(this.playerData)) {
                    Logger.info(String.valueOf(new StringBuilder().append("Skipping upgrade ").append(this.perkData.getNextUpgrade().getItem())));
                    this.perkData.incrementPath();
                }
            }
            if (this.perkData.canGetNextUpgrade(this.playerData) || this.perkData.hasUnknown(this.playerData)) {
                this.queueStateChange(State.WAITING_PERK);
            }
        }
    }

    private static void lambda$autoRejoin$5() {
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()));
        final long n = 1000L;
        try {
            Thread.sleep(n);
        }
        catch (final InterruptedException ex) {
            return;
        }
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiConnecting((GuiScreen)new GuiMainMenu(), Minecraft.getMinecraft(), "mc.hypixel.net", 25565));
    }

    private void resetKillChecker() {
        this.numKillsCheckTicksLeft = 1200;
        this.numKills = 0;
    }

    private Entity getTarget() {
        if (this.isEntityValid(this.target) && (this.target instanceof EntityItem || Utils.getDist2D(this.target, (Entity)Minecraft.getMinecraft().thePlayer) < 3.5)) {
            return this.target;
        }
        Object o = null;
        final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        double n = 100.0;
        boolean b = true;
        int n2 = 5;
        for (final EntityPlayer entityPlayer : Minecraft.getMinecraft().theWorld.playerEntities) {
            if (entityPlayer.getUniqueID().equals(((EntityPlayer)thePlayer).getUniqueID())) {
                continue;
            }
            if (!this.isEntityValid((Entity)entityPlayer)) {
                continue;
            }
            final ItemStack itemStack = entityPlayer.inventory.armorInventory[0];
            final ItemStack itemStack2 = entityPlayer.inventory.armorInventory[2];
            final boolean b2 = itemStack != null && itemStack.getItem() == Items.diamond_boots && itemStack2 != null && itemStack2.getItem() == Items.diamond_chestplate;
            if (!Setting.useProfiles.isEnabled() && !b && b2) {
                continue;
            }
            final double dist2D = Utils.getDist2D((Entity)thePlayer, (Entity)entityPlayer);
            final double dist2D2 = Utils.getDist2D((Entity)entityPlayer, 0.5, 0.5);
            final double n3 = entityPlayer.getHealth();
            final int calculateEntityLevel = Utils.calculateEntityLevel(dist2D, dist2D2);
            if (calculateEntityLevel >= n2 && (calculateEntityLevel != n2 || n3 >= n) && (b2 || !b)) {
                continue;
            }
            o = entityPlayer;
            n = n3;
            b = b2;
            n2 = calculateEntityLevel;
        }
        return (Entity)o;
    }

    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load load) {
        this.lastRan = Minecraft.getSystemTime();
    }

    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent clientChatReceivedEvent) {
        final String replaceAll = clientChatReceivedEvent.toString().replaceAll("\\u00A7.", "");
        final Matcher matcher = Pattern.compile("MAJOR EVENT! ([\\w\\s]+) starting now").matcher(replaceAll);
        if (matcher.find()) {
            this.currentEvent = PitEvent.getEventFromName(matcher.group(1));
            this.onEventStart();
            Logger.info(String.valueOf(new StringBuilder().append("Current event: ").append(this.currentEvent)));
        }
        else if (replaceAll.contains("MAJOR EVENT SPIRE in 1 min")) {
            this.currentEvent = PitEvent.SPIRE;
            this.onEventStart();
            Logger.info(String.valueOf(new StringBuilder().append("Current event: ").append(this.currentEvent)));
        }
        else if (replaceAll.contains("PIT EVENT ENDED")) {
            this.currentEvent = null;
            this.onEventEnd();
        }
        else if (replaceAll.contains("KILL! ")) {
            ++this.numKills;
            this.darkQuestInProgress = replaceAll.matches(".*KILL! on \\[\\d+] \\w{3,16} \\+.*XP \\+.*g \\(.*\\)");
        }
        else if (replaceAll.contains("OOPS! Cannot join The Pit from here!") && this.isRunning()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/l");
        }
        /*
        else if (replaceAll.contains("MYSTIC ITEM! dropped from")) {
            AutoGrinder.getInstance().getScheduler().addTask(new Task(this, 1) {
                final Driver this$0;

                @Override
                public void run() {
                    Driver.access$202(this.this$0, (Entity)Utils.getMysticDrop());
                    Logger.info(String.valueOf(new StringBuilder().append("Mystic drop detected: ").append(Driver.access$200(this.this$0))));
                }
            });
        }

         */
        else if (replaceAll.contains("A player has been removed from your game for hacking or abuse.")) {
            if (this.isRunning()) {
                this.queuePlayPit();
            }
        }
        else if (replaceAll.contains("OOPS! Cannot join The Pit from here!")) {
            new Thread(Driver::lambda$onChat$4).start();
        }
    }

    public PerkData getPerkData() {
        return this.perkData;
    }

    public void finishedTask() {
        this.grinderOn();
    }

    @SubscribeEvent
    public void checkWorld(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (this.currentMap == MapData.UNKNOWN && Minecraft.getMinecraft().theWorld.getChunkFromChunkCoords(-1, 0).isLoaded()) {
            this.currentMap = MapData.getMapData();
            Logger.info(String.valueOf(new StringBuilder().append("Current map: ").append(this.currentMap)));
        }
        if (this.currentEvent == PitEvent.UNKNOWN) {
            final List<String> scoreboardLines = Utils.getScoreboardLines();
            if (scoreboardLines.size() > 2) {
                final Matcher matcher = Pattern.compile("Event: ([\\w\\s]+)").matcher(scoreboardLines.get(2));
                this.currentEvent = PitEvent.getEventFromName(matcher.find() ? matcher.group(1) : "");
                if (this.currentEvent != null) {
                    this.onEventStart();
                    Logger.info(String.valueOf(new StringBuilder().append("Current event: ").append(this.currentEvent)));
                }
            }
        }
    }

    @SubscribeEvent
    public void loop(final TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase != TickEvent.Phase.START) {
            return;
        }
        if (!this.isRunning()) {
            return;
        }
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        final Scheduler scheduler = AutoGrinder.getInstance().getScheduler();
        if (this.guiOpened) {
            this.guiOpened = false;
            this.movementHelper.reapplyKeybindings();
        }
        this.cameraHelper.tick();
        this.movementHelper.tick();
        --this.playPitTicksLeft;
        --this.oofTicksLeft;
        --this.targetSwitchTicksLeft;
        if (Setting.playPit.isEnabled() && this.playPit && this.inSpawn() && ((!this.playerData.isFighting() && !this.darkQuestInProgress) || this.currentMap == null)) {
            if (this.playPitTicksLeft <= 0) {
                VapeHelper.turnOffKA();
                thePlayer.sendChatMessage("/play pit");
                this.currentState = State.RESPAWNING;
                this.playPit = false;
                this.playPitTicksLeft = 210;
                this.queuePitTicksLeft = 40;
                this.movementHelper.clearControlStates();
            }
        }
        else if (this.currentMap != MapData.UNKNOWN && this.currentMap != null) {
            if (this.currentState == State.RESPAWNING) {
                this.currentState = State.DROPPING;
                this.movementHelper.clearControlStates();
                thePlayer.inventory.currentItem = Utils.getBestWeapon((EntityPlayer)thePlayer);
                scheduler.addTask(new Task(this, 1) {
                    Driver this$0;

                    private void lambda$run$0() {
                        if (!Driver.access$100(this.this$0).isRunning()) {
                            Driver.access$100(this.this$0).setForwards(true);
                        }
                    }

                    @Override
                    public void run() {
                        Driver.access$000(this.this$0).lookAtXZ(0.0, 0.0, this::lambda$run$0);
                    }
                });
            }
            else if (this.currentState == State.DROPPING) {
                this.movementHelper.setJump(Math.random() < 0.05);
            }
            else if (this.currentState == State.LEVELING || this.currentState == State.WAITING_PRESTIGE || this.currentState == State.WAITING_PERK || this.currentState == State.WAITING_EVENT) {
                if (this.shouldOof) {
                    this.shouldOof = false;
                    if (this.playerData.getStreak() < 10 && this.oofTicksLeft <= 0) {
                        thePlayer.sendChatMessage("/oof");
                        this.oofTicksLeft = 210;
                    }
                }
                if (this.targetSwitchTicksLeft <= 0) {
                    this.targetSwitchTicksLeft = (int)(Math.floor(Math.random() * 50.0) + 50.0);
                    this.target = this.getTarget();
                }
                if (this.isEntityValid(this.target)) {
                    this.cameraHelper.lookAtEntityNatural(this.target);
                }
                else {
                    this.targetSwitchTicksLeft = 0;
                    this.target = null;
                    this.simpleWander();
                }
                this.movementHelper.setJump(this.target instanceof EntityPlayer);
            }
        }
    }

    private void onEventEnd() {
        if (this.currentState == State.HEADING_TO_AFK) {
            this.movementHelper.cancel();
            this.currentState = State.RESPAWNING;
        }
        else if (this.currentState == State.WAITING_EVENT) {
            if (this.onGround()) {
                this.currentState = State.LEVELING;
            }
            else if (this.inSpawn()) {
                this.currentState = State.RESPAWNING;
            }
            else {
                this.currentState = State.DROPPING;
            }
        }
    }

    @SubscribeEvent
    public void onLoadWorld(final WorldEvent.Load load) {
        if (load.world.isRemote) {
            this.currentMap = MapData.UNKNOWN;
            this.currentEvent = PitEvent.UNKNOWN;
            this.movementHelper.clearControlStates();
            if (this.isRunning()) {
                this.currentState = State.RESPAWNING;
            }
            this.cameraHelper.cancel();
            this.movementHelper.cancel();
            this.resetKillChecker();
            this.lobbyCheckTicks = 100;
            this.numRetriesLeft = 5;
        }
    }

    private void simpleWander() {
        final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (Utils.getDist2D((Entity)thePlayer, 0.5, 0.5) > 5.0 && !this.cameraHelper.isRunning()) {
            final double degrees = Math.toDegrees(MathHelper.atan2(((EntityPlayer)thePlayer).posX - 0.5, 0.5 - ((EntityPlayer)thePlayer).posZ));
            double n = (degrees - ((EntityPlayer)thePlayer).rotationYaw) % 360.0;
            if (n < -180.0) {
                n += 360.0;
            }
            if (n > 180.0) {
                n -= 360.0;
            }
            if (Math.abs(n) > 90.0) {
                this.cameraHelper.look(degrees + ((Math.random() < 0.5) ? -1 : 1) * (Math.random() * 60.0), ((EntityPlayer)thePlayer).cameraPitch);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void timerTicker(final TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase != TickEvent.Phase.START) {
            return;
        }
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (this.currentState != State.LEVELING && this.currentState != State.DROPPING) {
            return;
        }
        if (this.currentEvent != null) {
            return;
        }
        --this.numKillsCheckTicksLeft;
        if (this.numKillsCheckTicksLeft <= 0) {
            if (this.numKills < ToggleCommand.minKills) {
                this.queuePlayPit();
            }
            this.resetKillChecker();
        }
    }

    private static void lambda$onChat$4() {
        final long n = 500L;
        try {
            Thread.sleep(n);
        }
        catch (final InterruptedException ex) {}
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/l");
    }

    static Entity access$202(final Driver driver, final Entity target) {
        return driver.target = target;
    }

    public boolean isRunning() {
        return this.currentState != State.IDLE;
    }

    public void toggle() {
        if (this.isRunning()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText("Bot off"));
            this.grinderOff();
        }
        else {
            Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText("Bot on"));
            this.grinderOn();
        }
    }

    private void queuePlayPit() {
        if (this.queuePitTicksLeft <= 0) {
            this.playPit = true;
        }
    }

    private boolean canSwitchStates() {
        return this.currentState == State.LEVELING || this.currentState == State.DROPPING || this.currentState == State.RESPAWNING;
    }

    private static void lambda$null$0() {
        ClickHelper.leftClick();
        AutoGrinder.getInstance().getScheduler().addTask(new PrestigeTask());
    }

    public MovementHelper getMovementHelper() {
        return this.movementHelper;
    }

    @SubscribeEvent
    public void onRenderTick(final TickEvent.RenderTickEvent renderTickEvent) {
        if (renderTickEvent.phase != TickEvent.Phase.START) {
            return;
        }
        if (!this.isRunning()) {
            return;
        }
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (this.target == null) {
            return;
        }
        if (!Setting.showTarget.isEnabled()) {
            return;
        }
        Utils.highlightEntity(this.target, new Color(255, 0, 0));
    }

    private void grinderOn() {
        if (this.inSpawn()) {
            this.currentState = State.RESPAWNING;
        }
        else {
            this.currentState = State.DROPPING;
            if (this.currentMap != null) {
                this.movementHelper.setForwards(true);
            }
        }
        this.resetKillChecker();
        if (this.currentEvent != null) {
            this.onEventStart();
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent guiOpenEvent) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (this.isRunning()) {
            final GuiScreen gui = guiOpenEvent.gui;
            if (Minecraft.getMinecraft().thePlayer != null && gui != null && !(gui instanceof GuiChat) && !(gui instanceof GuiChest)) {
                guiOpenEvent.setCanceled(true);
            }
            else {
                this.guiOpened = true;
            }
        }
    }

    @SubscribeEvent
    public void onKeyDown(final InputEvent.KeyInputEvent keyInputEvent) {
        if (this.isRunning()) {
            final GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
            for (final KeyBinding keyBinding : new KeyBinding[] { gameSettings.keyBindForward, gameSettings.keyBindBack, gameSettings.keyBindRight, gameSettings.keyBindLeft, gameSettings.keyBindJump }) {
                if (keyBinding.getKeyCode() >= 0 && Keyboard.isKeyDown(keyBinding.getKeyCode())) {
                    this.toggle();
                    break;
                }
            }
        }
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public CameraHelper getCameraHelper() {
        return this.cameraHelper;
    }

    public Driver() {
        this.currentState = State.IDLE;
        this.queueStateChangeTicksLeft = 0;
        this.currentMap = MapData.UNKNOWN;
        this.currentEvent = PitEvent.UNKNOWN;
        this.playerData = new PlayerData();
        this.perkData = new PerkData();
        this.playPit = false;
        this.playPitTicksLeft = 0;
        this.queuePitTicksLeft = 0;
        this.shouldOof = false;
        this.oofTicksLeft = 0;
        this.target = null;
        this.targetSwitchTicksLeft = 0;
        this.numKills = 0;
        this.numKillsCheckTicksLeft = 1200;
        this.guiOpened = false;
        this.cameraHelper = new CameraHelper();
        this.movementHelper = new MovementHelper();
        this.canPrestige = true;
        this.prestigeCheckTicksLeft = 0;
        this.lobbyCheckTicks = 100;
        this.keepAliveTicksLeft = 0;
        this.numRetriesLeft = 5;
        this.darkQuestInProgress = false;
        this.targetServer = null;
        this.lastRan = 0L;
        this.reconnectTicks = 0;
    }

    public void onSuccessfulPerk(final Perks.ShopItem shopItem, final int n, final int n2) {
        this.perkData.updatePerk(shopItem, n, n2);
        this.perkData.incrementPath();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void stateSetter(final TickEvent.ClientTickEvent clientTickEvent) {
        if (!this.isRunning()) {
            return;
        }
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        --this.queuePitTicksLeft;
        --this.queueStateChangeTicksLeft;
        if (this.currentMap == null) {
            this.queuePlayPit();
        }
        final int lobbyCheckTicks = this.lobbyCheckTicks - 1;
        this.lobbyCheckTicks = lobbyCheckTicks;
        if (lobbyCheckTicks <= 0 && this.currentMap != null && this.currentMap != MapData.UNKNOWN && Minecraft.getMinecraft().theWorld.playerEntities.size() < 60 && Setting.minPlayersInLobby.isEnabled()) {
            this.lobbyCheckTicks = 100;
            this.queuePlayPit();
        }
        if (this.targetServer != null && !this.targetServer.equals(this.playerData.getCurrentServer())) {
            this.queuePlayPit();
        }
        if (this.currentMap == null || this.currentMap == MapData.UNKNOWN) {
            return;
        }
        final boolean onGround = this.onGround();
        final boolean inSpawn = this.inSpawn();
        if (this.currentState == State.DROPPING && onGround) {
            this.currentState = State.LEVELING;
            if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
                Minecraft.getMinecraft().thePlayer.closeScreen();
            }
            if (Setting.useProfiles.isEnabled()) {
                VapeHelper.turnOnKA();
            }
            else {
                VapeHelper.toggleKA();
            }
        }
        if (this.currentState == State.LEVELING && inSpawn) {
            this.currentState = State.RESPAWNING;
            if (Setting.useProfiles.isEnabled()) {
                VapeHelper.turnOffKA();
            }
        }
        if (!inSpawn && !Utils.entityInMid((Entity)Minecraft.getMinecraft().thePlayer, this.currentMap) && this.currentMap != null && this.currentMap != MapData.UNKNOWN) {
            Logger.info("Player is not in mid");
            this.shouldOof = true;
        }
        this.stateTickers();
        if (this.currentState == State.WAITING_PRESTIGE && inSpawn) {
            this.currentState = State.HEADING_TO_LOC;
            VapeHelper.turnOffKA();
            this.movementHelper.goToXZ(this.currentMap.getPrestigeLocation(), this::lambda$stateSetter$1);
        }
        if (this.currentState == State.WAITING_PERK && inSpawn) {
            this.currentState = State.HEADING_TO_LOC;
            VapeHelper.turnOffKA();
            this.movementHelper.goToXZ(this.currentMap.getPerkLocation(), this::lambda$stateSetter$3);
        }
        if (this.currentState == State.HEADING_TO_LOC && !inSpawn) {
            this.currentState = State.DROPPING;
            this.movementHelper.cancel();
            this.cameraHelper.cancel();
            if (Setting.useProfiles.isEnabled()) {
                VapeHelper.turnOnKA();
            }
            else {
                VapeHelper.toggleKA();
            }
        }
        if (this.currentState == State.WAITING_EVENT && inSpawn) {
            this.currentState = State.HEADING_TO_AFK;
            VapeHelper.turnOffKA();
            this.movementHelper.clearControlStates();
            AutoGrinder.getInstance().getScheduler().addTask(new Task(this, 5) {
                Driver this$0;

                @Override
                public void run() {
                    Driver.access$100(this.this$0).clearControlStates();
                    VapeHelper.turnOffKA();
                }
            });
        }
        if (this.currentState == State.HEADING_TO_AFK && !inSpawn) {
            this.currentState = State.WAITING_EVENT;
            this.movementHelper.cancel();
            this.cameraHelper.cancel();
            if (Setting.useProfiles.isEnabled()) {
                VapeHelper.turnOnKA();
            }
            else {
                VapeHelper.toggleKA();
            }
        }
        if (--this.keepAliveTicksLeft <= 0) {
            this.keepAliveTicksLeft = 3600;
            ApiHelper.keepAlive(this.playerData);
        }
    }

    static Entity access$200(final Driver driver) {
        return driver.target;
    }

    public void onPrestige() {
        this.perkData.onPrestige();
    }

    private void lambda$null$2() {
        ClickHelper.leftClick();
        if (this.perkData.hasUnknown(this.playerData)) {
            AutoGrinder.getInstance().getScheduler().addTask(new CheckTask(this.perkData));
        }
        else if (this.perkData.canGetNextUpgrade(this.playerData)) {
            AutoGrinder.getInstance().getScheduler().addTask(new BuyTask(this.perkData.getNextUpgrade(), this.perkData));
        }
    }

    private boolean onGround() {
        final EntityPlayerSP field_71439_g = Minecraft.getMinecraft().thePlayer;
        return field_71439_g != null && this.currentMap != null && ((EntityPlayer)field_71439_g).posY < this.currentMap.getGroundLevel() + 3;
    }

    @SubscribeEvent
    public void onTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (Minecraft.getMinecraft().thePlayer == null) {
            return;
        }
        if (Minecraft.getSystemTime() - this.lastRan < 20000L) {
            return;
        }
        if (!this.isRunning()) {
            return;
        }
        final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer.getWorldScoreboard() == null || thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1) == null) {
            thePlayer.sendChatMessage("/l");
            return;
        }
        final StringBuilder sb = new StringBuilder(thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());
        int n = 0;
        for (final char c : String.valueOf(sb).toCharArray()) {
            if (Character.UnicodeBlock.of(c) != Character.UnicodeBlock.BASIC_LATIN || c == 'e' || c == '6' || c == 'l' || c == 'f') {
                sb.deleteCharAt(n);
                --n;
            }
            ++n;
        }
        this.lastRan = Minecraft.getSystemTime();
        if (Setting.playPit.isEnabled() && String.valueOf(sb).contains("HYPIXEL") && !String.valueOf(sb).contains("PIT")) {
            thePlayer.sendChatMessage("/play pit");
        }
        if (String.valueOf(sb).contains("PIT")) {
            return;
        }
    }

    private boolean isEntityValid(final Entity entity) {
        return entity != null && Math.abs(entity.posY - ((EntityPlayer)Minecraft.getMinecraft().thePlayer).posY) < 3.0 && Utils.entityInMid(entity, this.currentMap) && entity.isEntityAlive();
    }
    
    static MovementHelper access$100(final Driver driver) {
        return driver.movementHelper;
    }
    
    private void queueStateChange(final State currentState) {
        if (this.queueStateChangeTicksLeft <= 0) {
            this.currentState = currentState;
            this.queueStateChangeTicksLeft = 300;
        }
    }
    
    private void lambda$stateSetter$1() {
        this.cameraHelper.lookAt(this.currentMap.getPrestigeLocation().addVector(0.0, 1.62, 0.0), Driver::lambda$null$0);
    }
    
    private void onEventStart() {
        if (this.currentEvent != null && this.currentEvent.stayInSpawn() && this.isRunning()) {
            this.currentState = State.WAITING_EVENT;
            if (!this.inSpawn()) {
                this.shouldOof = true;
            }
        }
    }
    
    public enum State
    {
        LEVELING, 
        WAITING_PERK, 
        WAITING_PRESTIGE, 
        WAITING_EVENT, 
        DROPPING, 
        RESPAWNING, 
        HEADING_TO_LOC,

        
        HEADING_TO_AFK, 
        IDLE;

        private static final State[] $VALUES;

        static {
            $VALUES = new State[] { State.IDLE, State.RESPAWNING, State.DROPPING, State.LEVELING, State.WAITING_PRESTIGE, State.WAITING_PERK, State.HEADING_TO_LOC, State.WAITING_EVENT, State.HEADING_TO_AFK };
        }
    }
}
