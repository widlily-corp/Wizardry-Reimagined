package com.binaris.wizardry.api.content.spell;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class SpellAction {
    public final ResourceLocation location;

    public static final SpellAction POINT = new SpellAction("point") {
        @Override
        public void renderArms(LivingEntity entity, HumanoidModel<?> model, InteractionHand hand) {
            float pitch = (float) Math.toRadians(entity.getXRot());
            float yaw = (float) Math.toRadians(entity.yHeadRot - entity.yBodyRot);
            float x = -((float) Math.PI / 2) + pitch + 0.2f;
            float y = (hand == InteractionHand.MAIN_HAND ? -0.25f : 0.25f) + yaw;
            if (hand == InteractionHand.MAIN_HAND) model.rightArm.setRotation(x, y, 0);
            else model.leftArm.setRotation(x, y, 0);
        }
    };

    public static final SpellAction POINT_UP = new SpellAction("point_up") {
        @Override
        public void renderArms(LivingEntity entity, HumanoidModel<?> model, InteractionHand hand) {
            float x = -((float) Math.PI) + 0.5f;
            float y = (hand == InteractionHand.MAIN_HAND ? -0.25f : 0.25f);
            if (hand == InteractionHand.MAIN_HAND) model.rightArm.setRotation(x, y, 0);
            else model.leftArm.setRotation(x, y, 0);
        }
    };

    public static final SpellAction POINT_DOWN = new SpellAction("point_down") {
        @Override
        public void renderArms(LivingEntity entity, HumanoidModel<?> model, InteractionHand hand) {
            float x = 0.2f;
            float y = (hand == InteractionHand.MAIN_HAND ? -0.25f : 0.25f);
            if (hand == InteractionHand.MAIN_HAND) model.rightArm.setRotation(x, y, 0);
            else model.leftArm.setRotation(x, y, 0);
        }
    };

    public static final SpellAction SUMMON = new SpellAction("summon") {
        @Override
        public void renderArms(LivingEntity entity, HumanoidModel<?> model, InteractionHand hand) {
            float x = -((float) Math.PI / 2) + 0.2f;
            model.rightArm.setRotation(x, -0.4f, 0);
            model.leftArm.setRotation(x, 0.4f, 0);
        }
    };

    public static final SpellAction THRUST = new SpellAction("thrust");
    public static final SpellAction IMBUE = new SpellAction("imbue");
    public static final SpellAction GRAPPLE = new SpellAction("grapple");
    public static final SpellAction NONE = new SpellAction("none");

    public SpellAction(String name) {
        this(ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, name));
    }

    public SpellAction(ResourceLocation location) {
        this.location = location;
    }

    public void renderArms(LivingEntity entity, HumanoidModel<?> model, InteractionHand hand) {}
}
