package com.shojabon.man10shopv3.menus.settings.innerSettings;

import ToolMenu.LargeSInventoryMenu;
import ToolMenu.NumericInputMenu;
import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import com.shojabon.mcutils.Utils.SInventory.SInventoryItem;
import com.shojabon.mcutils.Utils.SItemStack;
import com.shojabon.mcutils.Utils.SStringBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RandomPricePriceSelector extends LargeSInventoryMenu {

    Man10ShopV3 plugin;
    Player player;
    Man10Shop shop;

    public RandomPricePriceSelector(Player p, Man10Shop shop, Man10ShopV3 plugin){
        super(new SStringBuilder().aqua().bold().text("値段群一覧").build(), plugin);
        this.player = p;
        this.plugin = plugin;
        this.shop = shop;

        shop.updateData();
    }

    public void renderMenu(){
        ArrayList<SInventoryItem> items = new ArrayList<>();

        for(int price: shop.randomPriceFunction.getPrices()){

            SItemStack icon = new SItemStack(Material.EMERALD);
            icon.setDisplayName(new SStringBuilder().green().bold().text(price).build());
            icon.addLore("§c§l右クリックで削除");

            SInventoryItem item = new SInventoryItem(icon.build());
            item.clickable(false);
            item.setAsyncEvent(e -> {
                if(e.getClick() != ClickType.RIGHT) return;
                List<Integer> newPrices = shop.randomPriceFunction.getPrices();
                newPrices.remove((Integer) price);
                JSONObject request = shop.setVariable(player, "randomPrice.prices", newPrices);
                if(!request.getString("status").equals("success")){
                    shop.randomPriceFunction.warn(player, request.getString("message"));
                    return;
                }
                shop.randomPriceFunction.success(player, "価格を削除しました");

                new RandomPricePriceSelector(player, shop, plugin).open(player);
            });

            items.add(item);
        }
        setItems(items);
        setOnCloseEvent(ee -> shop.randomPriceFunction.getInnerSettingMenu(player, plugin).open(player));
    }

    public void afterRenderMenu() {
        renderInventory(0);

        SInventoryItem addPrice = new SInventoryItem(new SItemStack(Material.DISPENSER).setDisplayName("§a§l値段を追加する").build()).clickable(false);
        addPrice.setEvent(e -> {
            NumericInputMenu priceInput = new NumericInputMenu("§b§l値段を入力してください", plugin);
            priceInput.setOnCloseEvent(ee -> new RandomPricePriceSelector(player, shop, plugin).open(player));
            priceInput.setOnConfirm(number -> {
                List<Integer> newPrices = shop.randomPriceFunction.getPrices();
                newPrices.add(number);
                JSONObject request = shop.setVariable(player, "randomPrice.prices", newPrices);
                if(!request.getString("status").equals("success")){
                    shop.randomPriceFunction.warn(player, request.getString("message"));
                    return;
                }
                shop.randomPriceFunction.success(player, "価格を追加しました");

                new RandomPricePriceSelector(player, shop, plugin).open(player);
            });
            priceInput.open(player);
        });

        setItem(51, addPrice);
        renderInventory();
    }
}
