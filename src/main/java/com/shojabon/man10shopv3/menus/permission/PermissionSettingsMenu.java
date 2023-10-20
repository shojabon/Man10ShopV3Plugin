package com.shojabon.man10shopv3.menus.permission;

import ToolMenu.BooleanInputMenu;
import ToolMenu.ConfirmationMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.Man10ShopModerator;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.json.JSONObject;

import java.util.function.Consumer;

public class PermissionSettingsMenu extends SInventory{

    Man10Shop shop;
    Man10ShopV3 plugin;
    Player player;
    Man10ShopModerator target;

    String[] permissions = new String[]{
            "OWNER",
            "MODERATOR",
            "ACCOUNTANT",
            "STORAGE_ACCESS",
            "ALLOWED_TO_USE",
            "BANNED"
    };
    int[] slots = new int[]{19, 20, 21, 22, 23, 24};

    int deleteUserSlot = 35;

    public PermissionSettingsMenu(Player p, Man10Shop shop, Man10ShopModerator target, Man10ShopV3 plugin){
        super(new SStringBuilder().red().text(target.name).text("の権限設定").build(), 4, plugin);
        this.player = p;

        shop.updateData();
        this.shop = shop;
        this.target = shop.permissionFunction.getModerator(target.uuid);
        this.plugin = plugin;

    }

    public Consumer<InventoryClickEvent> generateClickChangePermissionEvent(){
        return e -> {
            for(int i = 0; i < slots.length; i++){
                if(e.getRawSlot() != slots[i]) continue;
                if(permissions[i].equals(target.permission)) continue;

                //permission change confirmation
                ConfirmationMenu menu = new ConfirmationMenu("確認", plugin);
                int finalI = i;
                menu.setOnConfirm(ee -> {
                    if(target.permission.equals("OWNER") && shop.permissionFunction.totalOwnerCount() == 1){
                        player.sendMessage(Man10ShopV3.prefix + "§c§lオーナーは最低一人必要です");
                        return;
                    }
                    target.permission = permissions[finalI];
                    if(!shop.permissionFunction.addModerator(target)){
                        return;
                    }

                    player.sendMessage(Man10ShopV3.prefix + "§a§l権限を設定しました");
                    new PermissionSettingsMenu(player, shop, target, plugin).open(player);
                });

                menu.setOnCancel(ee -> new PermissionSettingsMenu(player, shop, target, plugin).open(player));
                menu.setOnClose(ee -> new PermissionSettingsMenu(player, shop, target, plugin).open(player));

                //open confirmation
                menu.open(player);
                return;
            }
        };
    }

    public void renderSelector(){
        for(int i = 0; i < slots.length; i++){
            if(shop.permissionFunction.hasPermission(player.getUniqueId(), permissions[i])){
                if(shop.permissionFunction.getPermission(target.uuid).equals(permissions[i])){
                    //has permission and is current permission
                    SInventoryItem current = new SInventoryItem(new SItemStack(Material.LIME_STAINED_GLASS_PANE).setDisplayName(new SStringBuilder().green().bold().text("現在の権限").build()).build());
                    current.clickable(false);
                    setItem(slots[i], current);
                }else{
                    //has permission but not this permission
                    SInventoryItem background = new SInventoryItem(new SItemStack(Material.RED_STAINED_GLASS_PANE).setDisplayName(new SStringBuilder().red().bold().text("この権限に設定する").build()).build());
                    background.clickable(false);
                    background.setEvent(generateClickChangePermissionEvent());
                    setItem(slots[i], background);
                }
            }else{
                // no permission
                SInventoryItem notAllowed = new SInventoryItem(new SItemStack(Material.BARRIER).setDisplayName(new SStringBuilder().gray().bold().text("この権限に設定することはできません").build()).build());
                notAllowed.clickable(false);
                setItem(slots[i], notAllowed);
            }
        }

    }

    public void renderIcons(){

        SInventoryItem owner = new SInventoryItem(new SItemStack(Material.DIAMOND_BLOCK).setDisplayName(new SStringBuilder().aqua().bold().text("オーナー権限").build()).build());
        owner.clickable(false);
        setItem(slots[0]-9, owner);

        SInventoryItem moderator = new SInventoryItem(new SItemStack(Material.GOLD_BLOCK).setDisplayName(new SStringBuilder().gold().bold().text("管理者権限").build()).build());
        moderator.clickable(false);
        setItem(slots[1]-9, moderator);

        SInventoryItem accountant = new SInventoryItem(new SItemStack(Material.EMERALD_BLOCK).setDisplayName(new SStringBuilder().green().bold().text("会計権限").build()).build());
        accountant.clickable(false);
        setItem(slots[2]-9, accountant);

        SInventoryItem storageAccess = new SInventoryItem(new SItemStack(Material.CHEST).setDisplayName(new SStringBuilder().yellow().bold().text("倉庫編集権限").build()).build());
        storageAccess.clickable(false);
        setItem(slots[3]-9, storageAccess);

        SInventoryItem allowedToUse = new SInventoryItem(new SItemStack(Material.IRON_BLOCK).setDisplayName(new SStringBuilder().gray().bold().text("利用権限").build()).build());
        allowedToUse.clickable(false);
        setItem(slots[4]-9, allowedToUse);

        SInventoryItem banned = new SInventoryItem(new SItemStack(Material.BARRIER).setDisplayName(new SStringBuilder().red().bold().text("ショップ使用禁止").build()).build());
        banned.clickable(false);
        setItem(slots[5]-9, banned);


        //delete item
        SInventoryItem deleteUser = new SInventoryItem(new SItemStack(Material.LAVA_BUCKET).setDisplayName(new SStringBuilder().yellow().obfuscated().text("OO")
                .darkRed().bold().text("ユーザーを削除")
                .yellow().obfuscated().text("OO")
                .build()).build());
        deleteUser.setAsyncEvent(e -> {
            if(!shop.permissionFunction.hasPermission(player.getUniqueId(), "MODERATOR")){
                player.sendMessage(Man10ShopV3.prefix + "§c§lあなたはこのユーザーを消去する権限を持っていません");
                return;
            }
            if(target.permission.equals("OWNER") && shop.permissionFunction.totalOwnerCount() == 1){
                player.sendMessage(Man10ShopV3.prefix + "§c§lオーナーは最低一人必要です");
                return;
            }


            //confirmation
            ConfirmationMenu menu = new ConfirmationMenu("確認", plugin);
            menu.setOnConfirm(ee -> {
                Man10ShopV3.threadPool.submit(() -> {
                    if(!shop.permissionFunction.removeModerator(target)){
                        return;
                    }
                    new PermissionSettingsMainMenu(player, shop, plugin).open(player);
                    player.sendMessage(Man10ShopV3.prefix + "§c§a" + target.name + "を消去しました");
                });
            });

            menu.setOnCancel(ee -> new PermissionSettingsMenu(player, shop, target, plugin).open(player));
            menu.setOnClose(ee -> new PermissionSettingsMenu(player, shop, target, plugin).open(player));
            menu.open(player);


        });

        deleteUser.clickable(false);
        setItem(deleteUserSlot, deleteUser);


    }

//    public void renderNotification(){
//        SInventoryItem notification = new SInventoryItem(new SItemStack(Material.BELL).setDisplayName(new SStringBuilder().gold().bold().text("通知設定").build()).build());
//        notification.clickable(false);
//        setItem(10, notification);
//
//        SInventoryItem enabled = new SInventoryItem(new SItemStack(Material.LIME_STAINED_GLASS_PANE).setDisplayName(new SStringBuilder().green().bold().text("有効").build()).build());
//        enabled.clickable(false);
//        enabled.setAsyncEvent(renderNotificationEvent());
//
//        SInventoryItem disabled = new SInventoryItem(new SItemStack(Material.RED_STAINED_GLASS_PANE).setDisplayName(new SStringBuilder().red().bold().text("無効").build()).build());
//        disabled.clickable(false);
//        disabled.setAsyncEvent(renderNotificationEvent());
//
//        if(target.notificationEnabled){
//            setItem(19, enabled);
//        }else{
//            setItem(19, disabled);
//        }
//
//    }

//    public Consumer<InventoryClickEvent> renderNotificationEvent(){
//        return e -> {
//            if(!target.uuid.equals(player.getUniqueId())){
//                player.sendMessage(Man10ShopV3.prefix + "§c§lこの設定は本人のみ編集可能です");
//                return;
//            }
//
//
//            BooleanInputMenu boolMenu = new BooleanInputMenu(target.notificationEnabled, "設定を変更しますか？", plugin);
//            boolMenu.setOnConfirm(bool -> {
//                Man10ShopV3.threadPool.submit(() -> {
//                    Man10ShopModerator moderator = shop.permissionFunction.getModerator(target.uuid);
//                    if(moderator == null){
//                        player.sendMessage(Man10ShopV3.prefix + "§c§l内部エラーが発生しました");
//                        return;
//                    }
//                    moderator.notificationEnabled = bool;
//                    if(!shop.permissionFunction.addModerator(moderator)){
//                        return;
//                    }
//                    player.sendMessage(Man10ShopV3.prefix + "§a§l通知設定を設定しました");
//                    new PermissionSettingsMenu(player, shop, target, plugin).open(player);
//                });
//            });
//            boolMenu.setOnCancel(ee -> new PermissionSettingsMenu(player, shop, target, plugin).open(player));
//            boolMenu.setOnClose(ee -> new PermissionSettingsMenu(player, shop, target, plugin).open(player));
//
//            boolMenu.open(player);
//
//        };
//    }

    public void renderMenu(){
        SInventoryItem background = new SInventoryItem(new SItemStack(Material.BLUE_STAINED_GLASS_PANE).setDisplayName(" ").build());
        background.clickable(false);
        fillItem(background);

        renderSelector();
        renderIcons();
//        renderNotification();

        setOnCloseEvent(e -> new PermissionSettingsMainMenu(player, shop, plugin).open(player));
    }

}
