package net.wzz.more_avaritia.init;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModDamageTypes {
    public static ResourceKey<DamageType> INFINITY;
    public static final RegistrySetBuilder DAMAGE_BUILDER;

    public ModDamageTypes() {
    }

    public static HolderLookup.Provider append(HolderLookup.Provider original) {
        return DAMAGE_BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(INFINITY, new DamageType("infinity", DamageScaling.ALWAYS, 0.1F));
    }

    public static DamageSource causeRandomDamage(Entity attacker) {
        return new DamageSourceRandomMessages(attacker.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(INFINITY), attacker);
    }

    static {
        INFINITY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("more_avaritia", "infinity"));
        DAMAGE_BUILDER = (new RegistrySetBuilder()).add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);
    }

    public static class DamageSourceRandomMessages extends DamageSource {
        public DamageSourceRandomMessages(Holder<DamageType> damageTypeHolder, @Nullable Entity entity) {
            super(damageTypeHolder, entity);
        }

        public @NotNull Component getLocalizedDeathMessage(LivingEntity attacked) {
            int type = attacked.getRandom().nextInt(3);
            LivingEntity livingentity = attacked.getKillCredit();
            String var10000 = this.getMsgId();
            String s = "death.attack." + var10000 + "." + type;
            var10000 = this.getMsgId();
            String s1 = "death.attack." + var10000 + ".player." + type;
            return livingentity != null ? Component.translatable(s1, new Object[]{attacked.getDisplayName(), livingentity.getDisplayName()}) : Component.translatable(s, new Object[]{attacked.getDisplayName()});
        }
    }
}
