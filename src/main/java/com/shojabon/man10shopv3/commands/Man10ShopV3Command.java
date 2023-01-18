package com.shojabon.man10shopv3.commands;

import com.shojabon.man10shopv3.Man10ShopV3;
import com.shojabon.man10shopv3.commands.subCommands.*;
import com.shojabon.man10shopv3.commands.subCommands.internals.*;
import com.shojabon.mcutils.Utils.SCommandRouter.SCommandArgument;
import com.shojabon.mcutils.Utils.SCommandRouter.SCommandArgumentType;
import com.shojabon.mcutils.Utils.SCommandRouter.SCommandObject;
import com.shojabon.mcutils.Utils.SCommandRouter.SCommandRouter;

import java.security.Signature;


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

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("adminShops")).

                        addRequiredPermission("man10shopv3.admin.shops").addExplanation("アドミンショップをショップを開く").
                        setExecutor(new AdminShopsCommand(plugin))
        );

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("toggleWorld")).

                        addRequiredPermission("man10shopv3.toggleWorld")
                        .addExplanation("看板が機能するワールド一覧を表示する")
                        .setExecutor(new ToggleWorldCommand(plugin))
        );

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("toggleWorld")).
                        addArgument(new SCommandArgument().addAllowedType(SCommandArgumentType.WORLD).addAlias("ワールド名")).

                        addRequiredPermission("man10shopv3.toggleWorld")
                        .addExplanation("看板が機能するワールドの有効/無効を設定")
                        .setExecutor(new ToggleWorldCommand(plugin))
        );


        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("create")).
                        addArgument(new SCommandArgument().addAlias("ショップ名")).

                        addRequiredPermission("man10shopv3.shop.create")
                        .addExplanation("ショップを作成")
                        .setExecutor(new CreateShopCommand(plugin))
        );

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("createAdmin")).
                        addArgument(new SCommandArgument().addAlias("ショップ名")).

                        addRequiredPermission("man10shopv3.admin.shop.create")
                        .addExplanation("管理者ショップを作成")
                        .setExecutor(new CreateAdminShopCommand(plugin))
        );

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("reload")).

                        addRequiredPermission("man10shopv3.reload")
                        .addExplanation("プラグインをリロードする")
                        .addExplanation("")
                        .addExplanation("設定を変更したときに使用する")
                        .addExplanation("コマンドを使用するとサーバー起動時状態に戻る")
                        .setExecutor(new ReloadConfigCommand(plugin))
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
                        .addArgument(new SCommandArgument().addAllowedString("itemCheck"))
                        .addArgument(new SCommandArgument().addAlias("uuid"))
                        .addArgument(new SCommandArgument().addAlias("itemBase64"))
                        .addArgument(new SCommandArgument().addAlias("amount"))
                        .addRequiredPermission("man10shopv3.item.check").addExplanation("アイテムを取る(内部用)").
                        setExecutor(new ItemCheckCommand(plugin))
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

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("signUpdate"))
                        .addArgument(new SCommandArgument().addAlias("shopId"))
                        .addRequiredPermission("man10shopv3.sign.update").addExplanation("看板アップデート(内部用)").
                        setExecutor(new SignUpdateCommand(plugin))
        );

        addCommand(
                new SCommandObject()
                        .addArgument(new SCommandArgument().addAllowedString("lootBoxPlay"))
                        .addArgument(new SCommandArgument().addAlias("shopId"))
                        .addArgument(new SCommandArgument().addAlias("uuid"))
                        .addArgument(new SCommandArgument().addAlias("logId"))
                        .addRequiredPermission("man10shopv3.lootbox.play").addExplanation("ガチャをプレーする(内部用)").
                        setExecutor(new LootBoxPlayCommand(plugin))
        );
    }

}
