package com.shojabon.man10shopv3.shopFunctions;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.annotations.ShopFunctionDefinition;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.ShopFunction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.json.JSONObject;

import java.util.ArrayList;


public class SignFunction extends ShopFunction {
    public SignFunction(Man10Shop shop, Man10ShopV3 plugin) {
        super(shop, plugin);
    }


    public void updateSigns(){
        JSONObject signs = this.shop.shopData.getJSONObject("sign").getJSONObject("signs");
        for(String locationId: signs.keySet()){
            JSONObject signData = signs.getJSONObject(locationId);
            if(!signData.getString("server").equals(plugin.getConfig().getString("serverName"))) continue;
            World world = Bukkit.getWorld(signData.getString("world"));
            if(world == null) continue;
            Location location = new Location(world, signData.getDouble("x"), signData.getDouble("y"), signData.getDouble("z"));
            Block b = world.getBlockAt(location);
            if(!(b.getState() instanceof Sign)) continue;
            Sign sign = ((Sign) b.getState());
            ArrayList<String> lineData = shop.getSignInfo();
            for(int i = 0; i < lineData.size(); i++){
                if(lineData.get(i) == null || lineData.get(i).equalsIgnoreCase("")) continue;
                sign.setLine(i, lineData.get(i));
            }
            sign.update();
            break;
        }
    }

}
