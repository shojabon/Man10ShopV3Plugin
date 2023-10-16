package com.shojabon.man10shopv3.dataClass;

import org.bukkit.entity.Player;

import java.util.UUID;

public class QueueRequestObject {

    public QueueRequestObject(Player player, String shopId, String key, Object data){
        this.player = player;
        this.shopId = shopId;
        this.key = key;
        this.data = data;
    }

    public Player player;
    public String shopId;
    public String key;
    public Object data;

}
