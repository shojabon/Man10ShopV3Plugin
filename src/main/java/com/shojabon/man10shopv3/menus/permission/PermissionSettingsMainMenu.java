package com.shojabon.man10shopv3.menus.permission;

import ToolMenu.ConfirmationMenu;
import ToolMenu.LargeSInventoryMenu;
import ToolMenu.OnlinePlayerSelectorMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.Man10ShopModerator;
import com.shojabon.man10shopv3.menus.ShopMainMenu;
import com.shojabon.mcutils.Utils.BannerDictionary;
import com.shojabon.mcutils.Utils.SInventory.SInventory;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.ArrayList;

public class PermissionSettingsMainMenu extends LargeSInventoryMenu{
    Man10Shop shop;
    Man10ShopV3 plugin;
    Player player;

    public PermissionSettingsMainMenu(Player p, Man10Shop shop, Man10ShopV3 plugin){
        super("§4§l権限設定", plugin);
        this.player = p;
        this.shop = shop;
        this.plugin = plugin;
        addItems();

        shop.updateData();
    }

    public void addItems(){
        ArrayList<SInventoryItem> items = new ArrayList<>();

        for(Man10ShopModerator mod: shop.permissionFunction.getModerators()){

            SItemStack icon = new SItemStack(Material.PLAYER_HEAD).setHeadOwner(mod.uuid);
            icon.setDisplayName(new SStringBuilder().gold().bold().text(mod.name).build());
            icon.addLore(new SStringBuilder().lightPurple().text("権限: ").yellow().text(shop.permissionFunction.getPermissionString(mod.permission)).build());

            SInventoryItem item = new SInventoryItem(icon.build());
            item.clickable(false);
            item.setEvent(e -> {
                new PermissionSettingsMenu(player, shop, mod, plugin).open(player);
            });
            items.add(item);

        }

        items.add(creteAddModeratorItem());

        setItems(items);

        setOnCloseEvent(e -> new ShopMainMenu(player, shop, plugin).open(player));
    }

    public SInventoryItem creteAddModeratorItem(){
        BannerDictionary dictionary = new BannerDictionary();
        SInventoryItem item = new SInventoryItem(new SItemStack(dictionary.getSymbol("plus")).setDisplayName("§a§l管理者を追加する").build());
        item.clickable(false);
        item.setEvent(e -> {

            OnlinePlayerSelectorMenu playerSelectorMenu = new OnlinePlayerSelectorMenu(player, plugin);
            for(Man10ShopModerator mod: shop.permissionFunction.getModerators()){
                playerSelectorMenu.addException(mod.uuid);
            }
            playerSelectorMenu.setOnClick(targetPlayer -> {

                if(shop.permissionFunction.isModerator(targetPlayer.getUniqueId())){
                    player.sendMessage(Man10ShopV3.prefix + "§c§lこのプレイヤーはすでに管理者です");
                    return;
                }

                ConfirmationMenu menu = new ConfirmationMenu("§a" + targetPlayer.getName() + "を管理者にしますか？", plugin);
                menu.setOnConfirm(ee -> {
                    JSONObject request = shop.permissionFunction.addModerator(new Man10ShopModerator(targetPlayer.getName(), targetPlayer.getUniqueId(), "STORAGE_ACCESS", true));
                    if(!request.get("status").equals("success")){
                        player.sendMessage(Man10ShopV3.prefix + "§c§l" + request.get("message"));
                        return;
                    }
                    player.sendMessage(Man10ShopV3.prefix + "§a§l管理者を追加しました");
                    new ShopMainMenu(player, shop, plugin).open(player);
                });

                menu.open(player);


            });
            playerSelectorMenu.setOnCloseEvent(ee -> new PermissionSettingsMainMenu(player, shop, plugin).open(player));

            playerSelectorMenu.open(player);
        });
        return item;
    }

}
