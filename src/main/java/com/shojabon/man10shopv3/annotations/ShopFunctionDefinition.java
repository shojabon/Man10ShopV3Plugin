package com.shojabon.man10shopv3.annotations;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ShopFunctionDefinition {
    String name() default "無名";
    Material iconMaterial() default Material.DIAMOND;
    boolean isAdminSetting() default true;

    boolean isAgentSetting() default false;
    String[] explanation() default {};
    String category() default "一般設定";
    String[] enabledShopType() default {};
    String allowedPermission() default "MODERATOR";
    String internalFunctionName() default "";
    boolean enabled() default true;
}
