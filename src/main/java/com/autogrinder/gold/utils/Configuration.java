// 
// Decompiled by Procyon v0.6.0
// 

package com.autogrinder.gold.utils;

import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Arrays;
import com.autogrinder.gold.utils.data.PerkData;
import com.autogrinder.gold.commands.ToggleCommand;
import org.apache.commons.io.FileUtils;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.common.Loader;
import com.google.gson.JsonArray;
import com.autogrinder.gold.utils.data.enums.Perks;
import com.autogrinder.gold.utils.data.UpgradePair;
import com.google.gson.JsonObject;
import java.io.File;

public class Configuration
{
    private final File file;
    private JsonObject config;
    
    public UpgradePair[] initPerks(final String s, final UpgradePair[] array) {
        if (!this.config.has(s)) {
            this.setValue(s, array);
            return array;
        }
        final JsonArray asJsonArray = this.config.get(s).getAsJsonArray();
        final UpgradePair[] array2 = new UpgradePair[asJsonArray.size()];
        int i = 0;
        while (i < asJsonArray.size()) {
            final JsonObject asJsonObject = asJsonArray.get(i).getAsJsonObject();
            array2[i] = new UpgradePair(Perks.valueOf(asJsonObject.get("name").getAsString()), asJsonObject.get("level").getAsInt());
            final boolean b = false;
            ++i;
            if (b) {
                return null;
            }
        }
        return array2;
    }
    
    public Configuration() {
        this.file = new File(Loader.instance().getConfigDir(), "autogrinder.json");
        try {
            if (this.file.createNewFile()) {
                this.config = new JsonObject();
            }
            else {
                this.config = new JsonParser().parse(FileUtils.readFileToString(this.file)).getAsJsonObject();
            }
        }
        catch (final Exception ex) {
            this.config = new JsonObject();
        }
        Setting.playPit.setEnabled(this.initBoolean("playPit", true));
        Setting.autoReconnect.setEnabled(this.initBoolean("autoReconnect", false));
        Setting.autoPrestige.setEnabled(this.initBoolean("autoPrestige", true));
        Setting.autoPerk.setEnabled(this.initBoolean("autoPerk", true));
        Setting.hasDirty.setEnabled(this.initBoolean("hasDirty", false));
        Setting.hasTheWay.setEnabled(this.initBoolean("hasTheWay", false));
        Setting.useExpKs.setEnabled(this.initBoolean("useExpKs", true));
        Setting.useInsurance.setEnabled(this.initBoolean("useInsurance", false));
        Setting.minPlayersInLobby.setEnabled(this.initBoolean("minPlayersInLobby", false));
        Setting.mysticPathfinding.setEnabled(this.initBoolean("mysticPathfinding", true));
        Setting.useProfiles.setEnabled(this.initBoolean("useProfiles", true));
        ToggleCommand.kaToggle = this.initChar("kaToggle", 'N');
        ToggleCommand.kaOn = this.initChar("kaOn", 'N');
        ToggleCommand.kaOff = this.initChar("kaOff", 'M');
        ToggleCommand.minKills = this.initInt("minKills", 7);
        Setting.targetDiamonds.setEnabled(this.initBoolean("targetDiamonds", false));
        Setting.shouldJump.setEnabled(this.initBoolean("shouldJump", true));
        Setting.showTarget.setEnabled(this.initBoolean("showTarget", false));
        PerkData.UPGRADE_PATH.addAll(Arrays.asList(this.initPerks("upgradePath", new UpgradePair[] { new UpgradePair(Perks.Perk.STR_CHAINING), new UpgradePair(Perks.Passive.EL_GATO, 1), new UpgradePair(Perks.Passive.XP_BOOST, 1), new UpgradePair(Perks.Passive.EL_GATO, 2), new UpgradePair(Perks.Passive.XP_BOOST, 2), new UpgradePair(Perks.Perk.VAMPIRE), new UpgradePair(Perks.Passive.GOLD_BOOST, 1), new UpgradePair(Perks.Passive.EL_GATO, 4), new UpgradePair(Perks.Passive.GOLD_BOOST, 2), new UpgradePair(Perks.Passive.XP_BOOST, 3), new UpgradePair(Perks.Perk.GLADIATOR), new UpgradePair(Perks.KillStreak.EXPLICIOUS), new UpgradePair(Perks.Perk.DIRTY), new UpgradePair(Perks.Perk.STREAKER), new UpgradePair(Perks.KillStreak.FIGHT_OR_FLIGHT), new UpgradePair(Perks.KillStreak.COUNTER_STRIKE), new UpgradePair(Perks.Passive.XP_BOOST, 4), new UpgradePair(Perks.Passive.GOLD_BOOST, 3), new UpgradePair(Perks.Passive.XP_BOOST, 5), new UpgradePair(Perks.Passive.GOLD_BOOST, 4), new UpgradePair(Perks.Passive.GOLD_BOOST, 5), new UpgradePair(Perks.Passive.MELEE_DAMAGE, 1), new UpgradePair(Perks.Passive.DAMAGE_REDUCTION, 1), new UpgradePair(Perks.Passive.MELEE_DAMAGE, 2), new UpgradePair(Perks.Passive.DAMAGE_REDUCTION, 2), new UpgradePair(Perks.Passive.MELEE_DAMAGE, 3), new UpgradePair(Perks.Passive.DAMAGE_REDUCTION, 3), new UpgradePair(Perks.Passive.MELEE_DAMAGE, 4), new UpgradePair(Perks.Passive.DAMAGE_REDUCTION, 4), new UpgradePair(Perks.Passive.MELEE_DAMAGE, 5), new UpgradePair(Perks.Passive.DAMAGE_REDUCTION, 5) })));
        this.saveConfig();
    }
    
    public void setValue(final String s, final char c) {
        this.config.addProperty(s, c);
        this.saveConfig();
    }
    
    public boolean initBoolean(final String s, final boolean b) {
        if (!this.config.has(s)) {
            this.config.addProperty(s, b);
            return b;
        }
        return this.config.get(s).getAsBoolean();
    }
    
    public void setValue(final String s, final String s2) {
        this.config.addProperty(s, s2);
        this.saveConfig();
    }
    
    public void setValue(final String s, final int n) {
        this.config.addProperty(s, (Number)n);
        this.saveConfig();
    }
    
    public void setValue(final String s, final String[] array) {
        final JsonArray jsonArray = new JsonArray();
        final int length = array.length;
        int i = 0;
        while (i < length) {
            jsonArray.add((JsonElement)new JsonPrimitive(array[i]));
            final int n = 2;
            final int n2 = 2;
            ++i;
            if (n != n2) {
                return;
            }
        }
        this.config.add(s, (JsonElement)jsonArray);
        this.saveConfig();
    }
    
    public String initStr(final String s, final String s2) {
        if (!this.config.has(s)) {
            this.config.addProperty(s, s2);
            return s2;
        }
        return this.config.get(s).getAsString();
    }
    
    public void saveConfig() {
        try {
            FileUtils.writeStringToFile(this.file, this.config.toString(), "utf-8");
        }
        catch (final IOException ex) {
            Logger.error("Failed to save configuration.");
        }
    }
    
    public int initInt(final String s, final int n) {
        if (!this.config.has(s)) {
            this.config.addProperty(s, (Number)n);
            return n;
        }
        return this.config.get(s).getAsInt();
    }
    
    public void setValue(final String s, final UpgradePair[] array) {
        final JsonArray jsonArray = new JsonArray();
        final int length = array.length;
        int i = 0;
        while (i < length) {
            final UpgradePair upgradePair = array[i];
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", upgradePair.getItem().toString());
            jsonObject.addProperty("level", (Number)upgradePair.getLevel());
            jsonArray.add((JsonElement)jsonObject);
            final Object o = null;
            ++i;
            if (o != null) {
                return;
            }
        }
        this.config.add(s, (JsonElement)jsonArray);
        this.saveConfig();
    }
    
    public char initChar(final String s, final char c) {
        if (!this.config.has(s)) {
            this.config.addProperty(s, Character.valueOf(c));
            return c;
        }
        return this.config.get(s).getAsCharacter();
    }
    
    public void setValue(final String s, final boolean b) {
        this.config.addProperty(s, Boolean.valueOf(b));
        this.saveConfig();
    }
    
    public String[] initArr(final String s, final String[] array) {
        if (!this.config.has(s)) {
            this.setValue(s, array);
            return array;
        }
        final JsonArray asJsonArray = this.config.get(s).getAsJsonArray();
        final String[] array2 = new String[asJsonArray.size()];
        int i = 0;
        while (i < asJsonArray.size()) {
            array2[i] = asJsonArray.get(i).getAsString();
            final boolean b = false;
            ++i;
            if (b) {
                return null;
            }
        }
        return array2;
    }
}
