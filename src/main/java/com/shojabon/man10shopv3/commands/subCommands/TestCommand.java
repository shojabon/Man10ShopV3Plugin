package com.shojabon.man10shopv3.commands.subCommands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.dataClass.Man10Shop;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    Man10ShopV3 plugin;

    public TestCommand(Man10ShopV3 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Man10Shop shop = Man10ShopV3.api.getShop("eb2cab60-93b5-11ed-9a81-803253476232", player);
//        Block block = player.getTargetBlock(10);
//        block.getState().setMetadata("test", new FixedMetadataValue(plugin, "b"));

        Block block2 = player.getTargetBlock(10);
        Bukkit.broadcastMessage(block2.getMetadata("test").get(0).asString());

//        BlockDataMeta meta = (BlockDataMeta) block;
//        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "shopId"), PersistentDataType.STRING, "test");
//        block.getState()
        return true;
    }
}
