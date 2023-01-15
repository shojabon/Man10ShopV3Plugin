package com.shojabon.man10shopv3;

import com.shojabon.man10shopv3.dataClass.Man10Shop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Man10ShopV3API {

    Man10ShopV3 plugin;

    HashMap<String, Man10Shop> shopObjectCache = new HashMap<>();

    public Man10ShopV3API(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    // global methods
    public static JSONObject httpRequest(String endpoint, String method, JSONObject jsonInput) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // optional default is GET
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("Accept", "application/json; charset=UTF-8");
            con.setRequestProperty("x-api-key", Man10ShopV3.config.getString("api.key"));
            con.setDoOutput(true);

            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInput.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = con.getResponseCode();
            BufferedReader in = null;
            if(responseCode == 200){
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            }else{
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void warnMessage(Player p, String message){
        p.sendMessage(Man10ShopV3.prefix + "§c§l" + message);
    }

    public static void successMessage(Player p, String message){
        p.sendMessage(Man10ShopV3.prefix + "§a§l" + message);
    }

    public static JSONObject getPlayerJSON(Player p){
        Map<String, Object> result = new HashMap<>();
        result.put("name", p.getName());
        result.put("uuid", p.getUniqueId().toString());
        result.put("server", Man10ShopV3.config.getString("serverName"));
        if(p.getAddress() != null) result.put("ipAddress", p.getAddress().getAddress().getHostAddress());
        return new JSONObject(result);
    }

    public JSONObject getPlayerShops(Player p){
        Map<String, Object> payload = new HashMap<>();
        payload.put("player", getPlayerJSON(p));
        JSONObject result = httpRequest(this.plugin.getConfig().getString("api.endpoint") + "/shop/list", "POST", new JSONObject(payload));
        return result;
    }
    public JSONObject getAdminShops(Player p){
        Map<String, Object> payload = new HashMap<>();
        payload.put("player", getPlayerJSON(p));
        payload.put("admin", true);
        JSONObject result = httpRequest(this.plugin.getConfig().getString("api.endpoint") + "/shop/list", "POST", new JSONObject(payload));
        return result;
    }

    public Man10Shop getShopFromSign(Player p, Location loc){
        JSONObject data = new JSONObject();
        data.put("server", Man10ShopV3.config.getString("serverName"));
        data.put("world", loc.getWorld().getName());
        data.put("x", loc.getBlockX());
        data.put("y", loc.getBlockY());
        data.put("z", loc.getBlockZ());

        JSONObject payload = new JSONObject();
        if(p != null){
            payload.put("player", getPlayerJSON(p));
        }
        payload.put("sign", data);
        JSONObject result = httpRequest(this.plugin.getConfig().getString("api.endpoint") + "/shop/info", "POST", payload);
        if(result == null || !result.getString("status").equals("success")) return null;
        return new Man10Shop(result.getJSONObject("data"));
    }

    public JSONObject getShopInformation(String shopId, Player requestingPlayer){
        Map<String, Object> payload = new HashMap<>();
        payload.put("shopId", shopId);
        if(requestingPlayer != null){
            payload.put("player", getPlayerJSON(requestingPlayer));
        }
        JSONObject result = httpRequest(this.plugin.getConfig().getString("api.endpoint") + "/shop/info", "POST", new JSONObject(payload));
        if(result == null || !result.getString("status").equals("success")) return null;
        return result;
    }
    public Man10Shop getShop(String shopId, Player requestingPlayer){
//        if(shopObjectCache.containsKey(shopId)){
//            Man10Shop shop =  shopObjectCache.get(shopId);
//            shop.updateData();
//            return shop;
//        }
        JSONObject shopInformationRequest = getShopInformation(shopId, requestingPlayer);
        if(shopInformationRequest == null) return null;
        shopObjectCache.put(shopId, new Man10Shop(shopInformationRequest.getJSONObject("data")));
        return shopObjectCache.get(shopId);
    }

}