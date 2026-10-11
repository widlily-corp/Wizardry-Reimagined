package com.binaris.wizardry.compat.curios;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CuriosCompat {
    private static final boolean CURIOS_LOADED = ModList.get() != null && ModList.get().isLoaded("curios");
    private static Method getCuriosInventoryMethod = null;
    private static Method findFirstCurioMethod = null;
    private static boolean reflectionInitialized = false;

    private static void initReflection() {
        if (reflectionInitialized) return;
        reflectionInitialized = true;
        if (!CURIOS_LOADED) return;

        try {
            Class<?> curiosApiClass = Class.forName("top.theillusivec4.curios.api.CuriosApi");
            getCuriosInventoryMethod = curiosApiClass.getMethod("getCuriosInventory", net.minecraft.world.entity.LivingEntity.class);
        } catch (Throwable ignored) {
            // Curios not available or API changed
        }
    }

    public static boolean isCuriosLoaded() {
        return CURIOS_LOADED;
    }

    public static boolean isEquipped(Player player, Item item) {
        if (!CURIOS_LOADED) return false;
        initReflection();
        if (getCuriosInventoryMethod == null) return false;

        try {
            Object optHandler = getCuriosInventoryMethod.invoke(null, player);
            if (optHandler instanceof Optional<?> optional && optional.isPresent()) {
                Object curioItemHandler = optional.get();
                if (findFirstCurioMethod == null) {
                    findFirstCurioMethod = curioItemHandler.getClass().getMethod("findFirstCurio", Item.class);
                }
                Object slotResultOpt = findFirstCurioMethod.invoke(curioItemHandler, item);
                if (slotResultOpt instanceof Optional<?> resOpt) {
                    return resOpt.isPresent();
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    public static List<ItemStack> getEquippedCurios(Player player) {
        List<ItemStack> list = new ArrayList<>();
        if (!CURIOS_LOADED) return list;
        initReflection();
        // Fallback or safely return empty if Curios reflection fails
        return list;
    }

    private CuriosCompat() {}
}
