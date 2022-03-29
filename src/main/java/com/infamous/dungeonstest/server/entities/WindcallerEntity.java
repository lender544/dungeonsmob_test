package com.infamous.dungeonstest.server.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class WindcallerEntity extends SpellcastingIllagerEntity implements IAnimatable {

    public static final DataParameter<Integer> CAST_TICKS = EntityDataManager.defineId(WindcallerEntity.class, DataSerializers.INT);

    public WindcallerEntity(EntityType entity, World world) {
        super(entity, world);
    }

    AnimationFactory factory = new AnimationFactory(this);

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 10, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.getCurrentSpell() == SpellType.FANGS) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.windcaller.blast_attack", false));
            return PlayState.CONTINUE;
        } else if (this.getCurrentSpell() == SpellType.SUMMON_VEX) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.windcaller.casting", false));
            return PlayState.CONTINUE;
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.windcaller.move", true));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.windcaller.idle", true));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new CastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(5, new AttackSpellGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute windcaller() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.5D).add(Attributes.FOLLOW_RANGE, 12.0D).add(Attributes.MAX_HEALTH, 24.0D);
    }

    public void baseTick() {
        super.baseTick();

        if (this.getCastTicks() > 0) {
            this.setCastTicks(this.getCastTicks() - 1);
        }
    }

    public int getCastTicks() {
        return this.entityData.get(CAST_TICKS);
    }

    public void setCastTicks(int p_189794_1_) {
        this.entityData.set(CAST_TICKS, p_189794_1_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CAST_TICKS, 0);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return EvokerEntity.createAttributes();
    }


    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    public void applyRaidBuffs(int p_213660_1_, boolean p_213660_2_) {

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.EVOKER_HURT;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (WindcallerEntity.this.getTarget() != null) {
                WindcallerEntity.this.getLookControl().setLookAt(WindcallerEntity.this.getTarget(), (float) WindcallerEntity.this.getMaxHeadYRot(), (float) WindcallerEntity.this.getMaxHeadXRot());
            }

        }
    }


    class AttackSpellGoal extends UseSpellGoal {
        private AttackSpellGoal() {
        }

        protected int getCastingTime() {
            return 30;
        }

        protected int getCastingInterval() {
            return 100;
        }

        protected void performSpellCasting() {
            LivingEntity livingentity = WindcallerEntity.this.getTarget();
            if (livingentity != null) {
                Vector3d vector3d = WindcallerEntity.this.getViewVector(1.0F);
                double d1 = livingentity.getX() - (WindcallerEntity.this.getX() + vector3d.x * 4.0D);
                double d2 = livingentity.getY(0.5D) - WindcallerEntity.this.getY(0.5D);
                double d3 = livingentity.getZ() - (WindcallerEntity.this.getZ() + vector3d.z * 4.0D);
                TornadoProjetileEntity smallfireballentity = new TornadoProjetileEntity(WindcallerEntity.this.level, WindcallerEntity.this, d1, d2, d3);
                smallfireballentity.setPos(smallfireballentity.getX() + vector3d.x * 4.0D, WindcallerEntity.this.getY(0.5D) + 0.5D, smallfireballentity.getZ() + vector3d.z * 4.0D);
                WindcallerEntity.this.level.addFreshEntity(smallfireballentity);
            }

        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected SpellType getSpell() {
            return SpellType.FANGS;
        }
    }

}
