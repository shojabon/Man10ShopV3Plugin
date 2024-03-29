package com.shojabon.man10shopv3.menus.settings.lootBoxSettings;

import ToolMenu.ConfirmationMenu;
import ToolMenu.LargeSInventoryMenu;
import ToolMenu.NumericInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.lootBox.LootBox;
import com.shojabon.man10shopv3.dataClass.lootBox.LootBoxGroupData;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class LootBoxGroupSelectorMenu extends LargeSInventoryMenu {

    Man10ShopV3 plugin;
    Player player;
    Man10Shop shop;
    LootBox lootBox;

    Material[] defaultItemGroups = {
            Material.BEACON,
            Material.NETHERITE_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.GOLD_BLOCK,
            Material.IRON_BLOCK,
            Material.COPPER_BLOCK
    };

    public LootBoxGroupSelectorMenu(Player p, Man10Shop shop, LootBox lootBox, Man10ShopV3 plugin){
        super("§6§lアイテムグループ一覧", plugin);
        this.player = p;
        this.plugin = plugin;
        this.shop = shop;
        this.lootBox = lootBox;
    }

    public void renderMenu(){
        ArrayList<SInventoryItem> items = new ArrayList<>();

        
        for(int i = 0; i < lootBox.groupData.size(); i++){

            LootBoxGroupData data = lootBox.groupData.get(i);

            SItemStack icon = new SItemStack(data.icon);
            icon.setDisplayName("§a§lグループ:" + i);

            if(data.bigWin){
                icon.addLore("§6通知・花火: §a有効");
            }else{
                icon.addLore("§6通知・花火: §c無効");
            }
            icon.addLore("§d確率" + data.getPercentage() + "%");
            icon.addLore("§d" + data.percentageWeight + "/100000000");
            icon.addLore("");
            icon.addLore("§b§lアイテム数:" + data.itemStacks.size() + "種類");
            //warning
            if(lootBox.getLackingWeight() < 0){
                icon.addLore("§cウェイト超過: " + Math.abs(lootBox.getLackingWeight()));
            }else if(lootBox.getLackingWeight() > 0){
                icon.addLore("§cウェイト不足: " + Math.abs(lootBox.getLackingWeight()));
            }
            icon.addLore("");
            icon.addLore("§c§l右クリックで削除");
            icon.addLore("§b§lホイールクリックで確率調整");
            icon.addLore("§a§l左クリックでアイテム調整");

            //icon.addLore("§c§l右クリックで削除");
            SInventoryItem item = new SInventoryItem(icon.build());
            item.clickable(false);

            int finalI = i;
            item.setAsyncEvent(e -> {
                if(e.getClick() != ClickType.RIGHT) return;
                ArrayList<LootBoxGroupData> newData = lootBox.groupData;
                ConfirmationMenu menu = new ConfirmationMenu("グループを消去しますか？", plugin);
                menu.setOnConfirm(ee -> {
                    newData.remove(finalI);

                    if(!shop.lootBoxGroupFunction.saveLootBox(player, lootBox)){
                        return;
                    }
                    new LootBoxGroupSelectorMenu(player, shop, lootBox, plugin).open(player);
                });
                menu.setOnCancel(ee -> {new LootBoxGroupSelectorMenu(player, shop, lootBox, plugin).open(player);});
                menu.setOnCloseEvent(ee -> {new LootBoxGroupSelectorMenu(player, shop, lootBox, plugin).open(player);});
                menu.open(player);
            });

            item.setAsyncEvent(e -> {
                if(e.getClick() != ClickType.LEFT) return;

                new LootBoxGroupItemEditorMenu(player, shop, lootBox, finalI, plugin).open(player);
            });

            item.setAsyncEvent(e -> {
                if(e.getClick() != ClickType.MIDDLE) return;
                ArrayList<LootBoxGroupData> newData = lootBox.groupData;

                NumericInputMenu numberMenu = new NumericInputMenu("N x 0.000001%で入力してください", plugin);
                numberMenu.setMaxValue(100000000);
                numberMenu.setAllowZero(true);
                numberMenu.setDefaultValue(data.percentageWeight);

                numberMenu.setOnConfirm(ee -> {
                    newData.get(finalI).percentageWeight = ee;
                    if(!shop.lootBoxGroupFunction.saveLootBox(player, lootBox)){
                        return;
                    }
                     new LootBoxGroupSelectorMenu(player, shop, lootBox, plugin).open(player);
                });
                numberMenu.setOnCancel(ee -> new LootBoxGroupSelectorMenu(player, shop, lootBox, plugin).open(player));
                numberMenu.setOnCloseEvent(ee -> new LootBoxGroupSelectorMenu(player, shop, lootBox, plugin).open(player));

                numberMenu.open(player);
            });

            items.add(item);
        }
        setItems(items);
        setOnCloseEvent(ee -> new SettingsMainMenu(player, shop, shop.lootBoxGroupFunction.getDefinition().category(), plugin).open(player));
    }

    public Material getRandomMaterial(){
        for(int i = 0; i < 1024; i++){
            boolean alreadyExists = false;
            Material checking = Material.values()[new Random().nextInt(Material.values().length-1)];
            if(!checking.isItem()) continue;
            for(LootBoxGroupData data: lootBox.groupData){
                if(data.icon == checking) {
                    alreadyExists = true;
                    break;
                }
            }
            if(alreadyExists) continue;
            return checking;
        }
        return null;
    }

    public void afterRenderMenu() {
        renderInventory(0);

        SInventoryItem addPrice = new SInventoryItem(new SItemStack(Material.DISPENSER).setDisplayName("§a§lアイテムグループを追加").build()).clickable(false);
        addPrice.setAsyncEvent(e -> {
            ArrayList<LootBoxGroupData> newItemGroups = lootBox.groupData;
            if(newItemGroups.size() >= 45){
                player.sendMessage(Man10ShopV3.prefix + "§c§l45グループ以上は追加できません");
                return;
            }
            Material addingMaterial;
            if(newItemGroups.size() >= defaultItemGroups.length) {
                addingMaterial = getRandomMaterial();
            }else {
                addingMaterial = defaultItemGroups[newItemGroups.size()];
            }
            if(addingMaterial == null) return;
            LootBoxGroupData newData = new LootBoxGroupData();
            newData.icon = addingMaterial;
            newItemGroups.add(newData);

            if(!shop.lootBoxGroupFunction.saveLootBox(player, lootBox)){
                return;
            }

             new LootBoxGroupSelectorMenu(player, shop, lootBox, plugin).open(player);
        });


        setItem(51, addPrice);
        renderInventory();
    }
}
