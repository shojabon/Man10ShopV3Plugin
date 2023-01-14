package com.shojabon.man10shopv3.commands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.commands.subCommands.internals.ItemGiveCommand;
import com.shojabon.man10shopv3.commands.subCommands.internals.ItemTakeCommand;
import com.shojabon.man10shopv3.commands.subCommands.ShopsCommand;
import com.shojabon.man10shopv3.commands.subCommands.TestCommand;
import com.shojabon.man10shopv3.commands.subCommands.internals.MoneyGiveCommand;
import com.shojabon.man10shopv3.commands.subCommands.internals.MoneyTakeCommand;
import com.shojabon.mcutils.Utils.SCommandRouter.SCommandArgument;
import com.shojabon.mcutils.Utils.SCommandRouter.SCommandObject;
import com.shojabon.mcutils.Utils.SCommandRouter.SCommandRouter;


public class Man10ShopV3Command extends SCommandRouter {

    Man10ShopV3 plugin;

    public Man10ShopV3Command(Man10ShopV3 plugin){
        this.plugin = plugin;
        registerCommands();
        registerEvents();
        pluginPrefix = Man10ShopV3.prefix;
    }

    public void registerEvents(){
        setNoPermissionEvent(e -> e.sender.sendMessage(Man10ShopV3.prefix + "§c§lあなたは権限がありません"));
        setOnNoCommandFoundEvent(e -> e.sender.sendMessage(Man10ShopV3.prefix + "§c§lコマンドが存在しません"));
    }

    public void registerCommands(){
        //shops command
        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("test")).

                        addRequiredPermission("man10shopv3.test").addExplanation("テスト").
                        setExecutor(new TestCommand(plugin))
        );

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("shops")).

                        addRequiredPermission("man10shopv3.shops").addExplanation("編集可能なショップを開く").
                        setExecutor(new ShopsCommand(plugin))
        );

        // internals
        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("itemGive"))
                        .addArgument(new SCommandArgument().addAlias("uuid"))
                        .addArgument(new SCommandArgument().addAlias("itemBase64"))
                        .addArgument(new SCommandArgument().addAlias("amount"))
                        .addRequiredPermission("man10shopv3.item.give").addExplanation("アイテムを付与する(内部用)").
                        setExecutor(new ItemGiveCommand(plugin))
        );

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("itemTake"))
                        .addArgument(new SCommandArgument().addAlias("uuid"))
                        .addArgument(new SCommandArgument().addAlias("itemBase64"))
                        .addArgument(new SCommandArgument().addAlias("amount"))
                        .addRequiredPermission("man10shopv3.item.take").addExplanation("アイテムを取る(内部用)").
                        setExecutor(new ItemTakeCommand(plugin))
        );

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("moneyTake"))
                        .addArgument(new SCommandArgument().addAlias("uuid"))
                        .addArgument(new SCommandArgument().addAlias("amount"))
                        .addRequiredPermission("man10shopv3.money.take").addExplanation("お金を取る(内部用)").
                        setExecutor(new MoneyTakeCommand(plugin))
        );

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("moneyGive"))
                        .addArgument(new SCommandArgument().addAlias("uuid"))
                        .addArgument(new SCommandArgument().addAlias("amount"))
                        .addRequiredPermission("man10shopv3.money.give").addExplanation("お金をあげる(内部用)").
                        setExecutor(new MoneyGiveCommand(plugin))
        );
    }

}
