package com.binaris.wizardry.api.content.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public final class AllyDesignation {
    private AllyDesignation() {}

    public static boolean isAllied(@Nullable LivingEntity allyOf, @Nullable Entity possibleAlly) {
        if (allyOf == null || possibleAlly == null) return false;
        if (allyOf == possibleAlly) return true;

        if (possibleAlly instanceof LivingEntity living && allyOf.isAlliedTo(living)) {
            return true;
        }

        if (possibleAlly instanceof OwnableEntity pet) {
            Entity owner = pet.getOwner();
            if (owner == allyOf) return true;
            if (owner instanceof LivingEntity livingOwner && isAllied(allyOf, livingOwner)) {
                return true;
            }
        }

        if (allyOf instanceof OwnableEntity ownablePet) {
            Entity owner = ownablePet.getOwner();
            if (owner == possibleAlly) return true;
            if (owner instanceof LivingEntity livingOwner && isAllied(livingOwner, possibleAlly)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isValidTarget(@Nullable Entity attacker, @Nullable Entity target) {
        if (target == null || target == attacker) return false;
        if (attacker == null) return true;

        if (attacker instanceof OwnableEntity ownable && ownable.getOwner() != null) {
            return isValidTarget(ownable.getOwner(), target);
        }

        if (attacker instanceof LivingEntity livingAttacker && isAllied(livingAttacker, target)) {
            return false;
        }

        if (target instanceof OwnableEntity ownableTarget) {
            Entity targetOwner = ownableTarget.getOwner();
            if (targetOwner == attacker) return false;
            if (targetOwner instanceof LivingEntity livingOwner && attacker instanceof LivingEntity livingAttacker) {
                if (isAllied(livingAttacker, livingOwner)) return false;
            }
        }

        return true;
    }
}
