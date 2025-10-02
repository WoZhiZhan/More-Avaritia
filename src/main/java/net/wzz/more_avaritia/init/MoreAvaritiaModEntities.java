
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wzz.more_avaritia.init;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wzz.more_avaritia.MoreAvaritiaMod;
import net.wzz.more_avaritia.entity.StarLightingEntity;
import net.wzz.more_avaritia.entity.VoidEntity;
import net.wzz.more_avaritia.entity.VoidSpeedEntity;
import net.wzz.more_avaritia.entity.VoidThrowEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MoreAvaritiaModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MoreAvaritiaMod.MODID);
	public static final RegistryObject<EntityType<StarLightingEntity>> STAR_LIGHTING = register("star_lighting",
			EntityType.Builder.<StarLightingEntity>of(StarLightingEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(StarLightingEntity::new)

					.sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<VoidEntity>> VOID = register("void",
			EntityType.Builder.<VoidEntity>of(VoidEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(VoidEntity::new)

					.sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<VoidThrowEntity>> VOID_THROW = register("void_throw",
			EntityType.Builder.<VoidThrowEntity>of(VoidThrowEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(VoidThrowEntity::new)

					.sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<VoidSpeedEntity>> VOID_SPEED = register("void_speed",
			EntityType.Builder.<VoidSpeedEntity>of(VoidSpeedEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(VoidSpeedEntity::new)

					.sized(0.6f, 1.8f));
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
	}
}
