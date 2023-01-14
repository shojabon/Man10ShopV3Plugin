package com.shojabon.man10shopv3.dataClass;


import java.util.UUID;

public class Man10ShopModerator {

    public UUID uuid;
    public String name;
    public String  permission;
    public boolean notificationEnabled = false;

    public Man10ShopModerator(String name, UUID uuid, String permission, boolean notificationEnabled){
        this.name = name;
        this.uuid = uuid;
        this.permission = permission;
        this.notificationEnabled = notificationEnabled;
    }

}
