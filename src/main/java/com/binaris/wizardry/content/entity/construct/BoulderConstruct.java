package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BoulderConstruct extends ScaledConstructEntity {
    public BoulderConstruct(EntityType<?> type, Level world) {
        super(type, world);
        this.noPhysics = false;
        this.setNoGravity(false);
        int dur = EBSpells.BOULDER != null && EBSpells.BOULDER.hasProperty(DefaultProperties.DURATION)
                ? EBSpells.BOULDER.property(DefaultProperties.DURATION) : 200;
        this.setLifetime(dur);
        this.setBaseSize(2.375f, 2.375f);
    }

    public BoulderConstruct(Level world) {
        this(EBEntities.BOULDER.get(), world);
    }

    public void setHorizontalVelocity(double velX, double velZ) {
        this.setDeltaMovement(velX, this.getDeltaMovement().y, velZ);
    }

    @Override
    public void tick() {
        super.tick();

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.98, 0.98, 0.98));

        if (this.level().isClientSide) {
            if (this.getDeltaMovement().lengthSqr() > 0.01) {
                for (int i = 0; i < 3; i++) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),
                            this.getX() + (random.nextDouble() - 0.5) * getBbWidth(),
                            this.getY() + 0.1,
                            this.getZ() + (random.nextDouble() - 0.5) * getBbWidth(),
                            (random.nextDouble() - 0.5) * 0.2, 0.15, (random.nextDouble() - 0.5) * 0.2);
                }
            }
            return;
        }

        if (this.tickCount % 12 == 1 && this.getDeltaMovement().lengthSqr() > 0.02 && EBSounds.ENTITY_BOULDER_ROLL != null && EBSounds.ENTITY_BOULDER_ROLL.get() != null) {
            this.level().playSound(null, getX(), getY(), getZ(), EBSounds.ENTITY_BOULDER_ROLL.get(), net.minecraft.sounds.SoundSource.PLAYERS, 1.2F, 0.8F);
        }

        if (this.getDeltaMovement().lengthSqr() > 0.05) {
            List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(getBbWidth() / 1.5, getX(), getY(), getZ(), level());
            for (LivingEntity target : targets) {
                if (isValidTarget(target)) {
                    float dmg = EBSpells.BOULDER != null && EBSpells.BOULDER.hasProperty(DefaultProperties.DAMAGE)
                            ? EBSpells.BOULDER.property(DefaultProperties.DAMAGE) : 10.0f;
                    MagicDamageSource.causeMagicDamage(this, target, dmg * damageMultiplier, EBDamageSources.EARTH);

                    // Knockback like bowling pins
                    Vec3 motion = this.getDeltaMovement().normalize().scale(1.2);
                    target.setDeltaMovement(motion.x, 0.35, motion.z);
                    target.hurtMarked = true;

                    if (EBSounds.ENTITY_BOULDER_HIT != null && EBSounds.ENTITY_BOULDER_HIT.get() != null) {
                        this.level().playSound(null, target.getX(), target.getY(), target.getZ(), EBSounds.ENTITY_BOULDER_HIT.get(), net.minecraft.sounds.SoundSource.PLAYERS, 1.2F, 1.0F);
                    }
                }
            }
        }
    }
}
