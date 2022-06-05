package com.autogrinder.gold.utils.helpers;

import com.autogrinder.gold.utils.Logger;
import com.autogrinder.gold.utils.data.PlayerData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ApiHelper {
    public static List<String> getNewCommands() {
        final ArrayList<String> list = new ArrayList<String>();
        try {
            final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("REMOTE COMMANDS").openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("uuid", Minecraft.getMinecraft().getSession().getUsername());
            httpURLConnection.getOutputStream().write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            if (httpURLConnection.getResponseCode() == 200) {
                final Iterator<JsonElement> iterator = new JsonParser().parse(IOUtils.toString(httpURLConnection.getInputStream())).getAsJsonObject().getAsJsonArray("commands").iterator();
                while (iterator.hasNext()) {
                    list.add((iterator.next()).getAsString());
                }
            }
            else {
                Logger.error("Could not fetch new commands");
            }
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static void keepAlive(final PlayerData playerData) {
        try {
            final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("KEEPALIVE").openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("api_key", "IloveMonkeys");
            jsonObject.addProperty("uuid", Minecraft.getMinecraft().getSession().getPlayerID());
            jsonObject.addProperty("level", (Number)playerData.getLevel());
            jsonObject.addProperty("prestige", (Number)playerData.getPrestige());
            httpURLConnection.getOutputStream().write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            if (httpURLConnection.getResponseCode() != 200) {
                Logger.error("API did not return true");
            }
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
