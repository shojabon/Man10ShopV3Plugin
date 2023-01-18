package com.shojabon.man10shopv3.menus.settings.lootBoxSettings;

import ToolMenu.LargeSInventoryMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.man10shopv3.dataClass.lootBox.LootBox;
import com.shojabon.man10shopv3.dataClass.lootBox.LootBoxGroupData;
import com.shojabon.man10shopv3.menus.settings.SettingsMainMenu;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;

public class LootBoxBigWinSelectorMenu extends LargeSInventoryMenu {

    Man10ShopV3 plugin;
    Player player;
    Man10Shop shop;
    LootBox lootBox;

    public LootBoxBigWinSelectorMenu(Player p, Man10Shop shop, LootBox lootBox, Man10ShopV3 plugin){
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
            icon.addLore("");
            icon.addLore("§d確率" + data.getPercentage() + "%");
            icon.addLore("§d" + data.percentageWeight + "/10000");
            icon.addLore("");
            icon.addLore("§b§lアイテム数:" + data.itemStacks.size() + "種類");
            //warning
            if(lootBox.getLackingWeight() < 0){
                icon.addLore("§cウェイト超過: " + Math.abs(lootBox.getLackingWeight()));
            }else if(lootBox.getLackingWeight() > 0){
                icon.addLore("§cウェイト不足: " + Math.abs(lootBox.getLackingWeight()));
            }
            icon.addLore("");
            icon.addLore("§c§l左クリックで切り替え");

            //icon.addLore("§c§l右クリックで削除");
            SInventoryItem item = new SInventoryItem(icon.build());
            item.clickable(false);

            item.setAsyncEvent(e -> {
                if(e.getClick() != ClickType.LEFT) return;
                data.bigWin = !data.bigWin;
                if(!shop.lootBoxGroupFunction.saveLootBox(player, lootBox)){
                    return;
                }
                new LootBoxBigWinSelectorMenu(player, shop, lootBox, plugin).open(player);
            });


            items.add(item);
        }
        setItems(items);
        setOnCloseEvent(ee -> new SettingsMainMenu(player, shop, shop.lootBoxBigWinFunction.getDefinition().category(), plugin).open(player));
    }

    public void afterRenderMenu() {
        renderInventory(0);
        renderInventory();
    }
}
