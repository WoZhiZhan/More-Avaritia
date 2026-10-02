package net.wzz.more_avaritia.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.wzz.more_avaritia.MoreAvaritiaMod;

@Mod.EventBusSubscriber(modid = MoreAvaritiaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue KILL_ANIMALS = BUILDER
            .comment("Allow killEntity to attack animals")
            .define("killAnimals", true);

    public static final ForgeConfigSpec.BooleanValue KILL_PLAYERS = BUILDER
            .comment("Allow killEntity to attack players")
            .define("killPlayers", false);

    public static final ForgeConfigSpec.BooleanValue KILL_OTHER_ENTITIES = BUILDER
            .comment("Allow killEntity to attack other entities (boats, minecarts, etc.)")
            .define("killOtherEntities", false);

    public static final ForgeConfigSpec.IntValue GOD_SWORD_RANGE = BUILDER
            .comment("Infinity God Sword right-click range (blocks)")
            .defineInRange("godSwordRange", 128, 1, 512);

    public static final ForgeConfigSpec.IntValue GOD_SWORD_ANGLE = BUILDER
            .comment("Infinity God Sword right-click cone angle (degrees, total)")
            .defineInRange("godSwordAngle", 90, 10, 360);

    public static final ForgeConfigSpec CONFIG_SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }
}
