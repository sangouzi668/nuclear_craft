package com.song.nuclear_craft.effects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class Radioactive extends MobEffect {
    
    // 在 mod 主类中注册自定义伤害类型
    // public static final DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create(Registries.DAMAGE_TYPE, "nuclear_craft");
    // public static final RegistryObject<DamageType> RADIOACTIVE_DAMAGE = DAMAGE_TYPES.register("radioactive", 
    //     () -> new DamageType("radioactive", 0.1F));
    
    public Radioactive() {
        super(MobEffectCategory.HARMFUL, 0x00FF00);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        // 方法1：使用魔法伤害（推荐，简单）
        DamageSource magicDamage = entityLivingBaseIn.damageSources().magic();
        entityLivingBaseIn.hurt(magicDamage, 1.0F * (amplifier + 1));
        
        // 方法2：如果要使用自定义伤害类型，需要先注册
        // DamageSource radioactiveDamage = new DamageSource(entityLivingBaseIn.level().registryAccess()
        //     .registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(RADIOACTIVE_DAMAGE.getKey()));
        // entityLivingBaseIn.hurt(radioactiveDamage, 1.0F * (amplifier + 1));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int j = 25 >> amplifier;
        if (j > 0) {
            return duration % j == 0;
        } else {
            return true;
        }
    }
}