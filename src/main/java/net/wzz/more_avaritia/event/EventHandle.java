package net.wzz.more_avaritia.event;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import morph.avaritia.init.AvaritiaModContent;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;
import net.wzz.more_avaritia.item.InfinitySwordGodItem;
import net.wzz.more_avaritia.item.InfinitySwordItem;
import net.wzz.more_avaritia.item.SuperInfinityArmorItem;
import net.wzz.more_avaritia.util.ListHelper;
import net.wzz.more_avaritia.util.RainbowText;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static morph.avaritia.handler.ArmorHandler.isInfinite;

@Mod.EventBusSubscriber
public class EventHandle {
    private static final Set<String> entitiesWithFlight = new HashSet<>();
    private static final Set<String> entitiesWithHelmets = new HashSet<>();

    private static final Set<String> entitiesWithChestplates = new HashSet<>();

    private static final Set<String> entitiesWithLeggings = new HashSet<>();

    private static final Set<String> entitiesWithBoots = new HashSet<>();

    @SubscribeEvent
    public static void updateAbilities(LivingEvent.LivingUpdateEvent event) {
        LivingEntity e = event.getEntityLiving();
        String key = e.getEncodeId() + "|" + e.getEncodeId();
        if (isPlayerWearing(event.getEntityLiving(), EquipmentSlot.HEAD, item -> item instanceof SuperInfinityArmorItem)) {
            entitiesWithHelmets.add(key);
            handleHelmetStateChange(e, true);
        } else {
            entitiesWithHelmets.remove(key);
            handleHelmetStateChange(e, false);
        }
        if (isPlayerWearing(event.getEntityLiving(), EquipmentSlot.CHEST, item -> item instanceof SuperInfinityArmorItem)) {
            entitiesWithChestplates.add(key);
            handleChestplateStateChange(e, true);
        } else {
            entitiesWithChestplates.remove(key);
            handleChestplateStateChange(e, false);
        }
        if (isPlayerWearing(event.getEntityLiving(), EquipmentSlot.LEGS, item -> item instanceof SuperInfinityArmorItem)) {
            entitiesWithLeggings.add(key);
            handleLeggingsStateChange(e, true);
        } else {
            entitiesWithLeggings.remove(key);
            handleLeggingsStateChange(e, false);
        }
        if (isPlayerWearing(event.getEntityLiving(), EquipmentSlot.FEET, item -> item instanceof SuperInfinityArmorItem)) {
            handleBootsStateChange(e);
            entitiesWithBoots.add(key);
        } else {
            handleBootsStateChange(e);
            entitiesWithBoots.remove(key);
        }
        if (entitiesWithLeggings.contains(key))
            tickLeggingsAbilities(e);
        if (entitiesWithBoots.contains(key) && e instanceof Player player)
            tickBootsAbilities(player);
    }

    static void stripAbilities(LivingEntity entity) {
        String key = entity.getEncodeId() + "|" + entity.getEncodeId();
        if (entitiesWithHelmets.remove(key))
            handleHelmetStateChange(entity, false);
        if (entitiesWithChestplates.remove(key))
            handleChestplateStateChange(entity, false);
        if (entitiesWithLeggings.remove(key))
            handleLeggingsStateChange(entity, false);
        if (entitiesWithBoots.remove(key))
            handleBootsStateChange(entity);
    }

    @SubscribeEvent
    public static void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent e) {
        stripAbilities(e.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e) {
        stripAbilities(e.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
        stripAbilities(e.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
        stripAbilities(e.getPlayer());
    }

    static void handleHelmetStateChange(LivingEntity entity, boolean isNew) {
        String key = entity.getEncodeId() + "|" + entity.getEncodeId();
        if (isNew) {
            entity.setAirSupply(300);
            entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 301, 0, false, false));
            if (entity instanceof Player) {
                Player player = (Player)entity;
                player.getFoodData().eat(20, 20.0F);
            }
            entitiesWithHelmets.add(key);
        } else {
            entitiesWithHelmets.remove(key);
        }
    }

    static void tickLeggingsAbilities(LivingEntity e) {
        if (e.isOnFire()) {
            e.clearFire();
            e.fireImmune();
        }
    }

    static void tickBootsAbilities(Player e) {
        if (e.getInventory().getArmor(2).getItem() == MoreAvaritiaModItems.SUPER_INFINITY_LEG.get()) {
            boolean fly = e.getAbilities().flying;
            if (e.isOnGround() || fly || e.isSwimming()) {
                float speed = 0.15F * (fly ? 1.1F : 1.0F) * (e.isShiftKeyDown() ? 0.2F : 1.0F);
                if (e.zza > 0.0F) {
                    e.moveRelative(speed, new Vec3(0.0D, 0.0D, 1.0D));
                } else if (e.zza < 0.0F) {
                    e.moveRelative(-speed * 0.5F, new Vec3(0.0D, 0.0D, 1.0D));
                }
                if (e.xxa != 0.0F)
                    e.moveRelative(speed * 0.7F * Math.signum(e.xxa), new Vec3(1.0D, 0.0D, 0.0D));
            }
        }
    }

    static void handleChestplateStateChange(LivingEntity e, boolean isNew) {
        String key = e.getEncodeId() + "|" + e.getEncodeId();
        if (isNew) {
            for (MobEffectInstance potion : Collections2.filter(Lists.newArrayList(e.getActiveEffects()), potion -> !potion.getEffect().isBeneficial()))
                e.removeEffect(potion.getEffect());
            if (e instanceof Player) {
                Player player = (Player)e;
                (player.getAbilities()).mayfly = true;
                entitiesWithFlight.add(key);
            }
        } else if (e instanceof Player) {
            Player player = (Player)e;
            if (!player.isCreative() && !player.isSpectator() && entitiesWithFlight.contains(key)) {
                (player.getAbilities()).mayfly = false;
                (player.getAbilities()).flying = false;
            }
            entitiesWithFlight.remove(key);
        }
    }

    static void handleLeggingsStateChange(LivingEntity e, boolean isNew) {
        String key = e.getEncodeId() + "|" + e.getEncodeId();
        if (isNew) {
            entitiesWithLeggings.add(key);
        } else {
            entitiesWithLeggings.remove(key);
        }
    }

    static void handleBootsStateChange(LivingEntity e) {
        String key = e.getEncodeId() + "|" + e.getEncodeId();
        if (isPlayerWearing(e, EquipmentSlot.FEET, item -> item instanceof SuperInfinityArmorItem)) {
            e.maxUpStep = 1.0625F;
            if (!entitiesWithBoots.contains(key))
                entitiesWithBoots.add(key);
        } else if (entitiesWithBoots.contains(key)) {
            e.maxUpStep = 0.6F;
            entitiesWithBoots.remove(key);
        }
    }

    static boolean isPlayerWearing(LivingEntity entity, EquipmentSlot slot, Predicate<Item> predicate) {
        ItemStack stack = entity.getItemBySlot(slot);
        return (!stack.isEmpty() && predicate.test(stack.getItem()));
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (hasInfinityArmor(player)) {
                e.setCanceled(true);
                if (e.getSource() != null && e.getSource().getEntity() instanceof LivingEntity living)
                    InfinitySwordGodItem.killEntity(living, player);
            }
            if (player.isShiftKeyDown() && hasItem(player, MoreAvaritiaModItems.INFINITY_SWORD_GOD.get()))
                e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockBreaking(BlockEvent.BreakEvent e) {
        if (e.getPlayer().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_AXE.get()) {
            BlockState blockState = e.getState();
            Block block = blockState.getBlock();
            ItemStack dropStack = new ItemStack(block);
            if (!dropStack.isEmpty()) {
                for (ItemStack itemStack : Block.getDrops(blockState, (ServerLevel) e.getWorld(), e.getPos(), null)) {
                    ItemStack tripledDrop = itemStack.copy();
                    tripledDrop.setCount(tripledDrop.getCount() * 3);
                    e.getWorld().addFreshEntity(new ItemEntity((Level) e.getWorld(), e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), tripledDrop));
                }
            }
        }
        if (e.getPlayer().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_PICKAXE.get()) {
            BlockState blockState = e.getState();
            Block block = blockState.getBlock();
            if (block instanceof OreBlock) {
                ItemStack resultStack = getSmeltingResult(block, e.getPlayer().level);
                if (!resultStack.isEmpty()) {
                    e.getWorld().addFreshEntity(new ItemEntity((Level) e.getWorld(),
                            e.getPos().getX(),
                            e.getPos().getY(),
                            e.getPos().getZ(),
                            resultStack));
                    e.setCanceled(true);
                    e.getWorld().setBlock(e.getPos(), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
        if (e.getPlayer().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_HOE.get()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerLeftBLock(PlayerInteractEvent.LeftClickBlock e) {
        Level world = e.getWorld();
        if (e.getPlayer().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_PICKAXE.get() && !e.getPlayer().getMainHandItem().getOrCreateTag().getBoolean("hammer")) {
            BlockState blockState = e.getWorld().getBlockState(e.getPos());
            Block block = blockState.getBlock();
            if (block == Blocks.BEDROCK || block == Blocks.END_PORTAL_FRAME || block == Blocks.NETHER_PORTAL || block == Blocks.END_PORTAL) {
                e.getWorld().addFreshEntity(new ItemEntity(e.getWorld(),
                        e.getPos().getX(),
                        e.getPos().getY(),
                        e.getPos().getZ(),
                        new ItemStack(block)));
                e.getWorld().destroyBlock(e.getPos(), false);
            }
        }
        if (e.getPlayer().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_HOE.get()) {
            if (world.getBlockState(e.getPos()).getBlock() instanceof BonemealableBlock) {
                BlockPos _bp = e.getPos();
                if (BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), world, _bp) || BoneMealItem.growWaterPlant(new ItemStack(Items.BONE_MEAL), world, _bp, null)) {
                    if (!world.isClientSide())
                        world.levelEvent(2005, _bp, 0);
                }
            }
            e.setCanceled(true);
        }
    }

    private static ItemStack getSmeltingResult(Block block, Level level) {
        ItemStack itemStack = new ItemStack(block);
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemStack), level).map(recipe -> recipe.getResultItem().copy()).orElse(ItemStack.EMPTY).copy();
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (isInfinite(player)) {
                if (e.getAmount() >= player.getHealth() && player.getHealth() >= 10f)
                    e.setAmount(player.getHealth() - 2);
            }
            if (hasInfinityArmor(player))
                e.setCanceled(true);
            if (player.isShiftKeyDown() && hasItem(player, MoreAvaritiaModItems.INFINITY_SWORD_GOD.get()))
                e.setCanceled(true);
            if (e.getAmount() >= player.getHealth() && player.getHealth() >= 10f && hasItem(player, MoreAvaritiaModItems.INFINITY_SWORD_GOD.get()))
                e.setAmount(player.getHealth() - 2);
        }
        if (e.getEntity() instanceof Player player && e.getAmount() >= player.getHealth()) {
            if (player.getInventory().contains(new ItemStack(MoreAvaritiaModItems.INFINITYTOTEM.get()))) {
                e.setCanceled(true);
                Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(MoreAvaritiaModItems.INFINITYTOTEM.get()));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 9));
                player.getInventory().clearOrCountMatchingItems(p -> MoreAvaritiaModItems.INFINITYTOTEM.get() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
                if (player instanceof ServerPlayer _player) {
                    Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("more_avaritia:get_live"));
                    AdvancementProgress _ap = null;
                    if (_adv != null) {
                        _ap = _player.getAdvancements().getOrStartProgress(_adv);
                    }
                    if (_ap != null && !_ap.isDone()) {
                        for (String s : _ap.getRemainingCriteria())
                            _player.getAdvancements().award(_adv, s);
                    }
                }
                player.level.playSound(player,player.getX(),player.getY(),player.getZ(), SoundEvents.TOTEM_USE, SoundSource.AMBIENT, 1, 1);
                player.playSound(SoundEvents.TOTEM_USE, 1, 1);
                for (int i =0; i <= 10; i ++)
                    player.level.addParticle(ParticleTypes.CRIT, player.getX(),player.getY() + 1,player.getZ(), 0.5,0.5,0.5);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        if (ListHelper.entityContainsInList(e.getEntityLiving())) {
            if (e.getEntityLiving() != null && e.getEntityLiving().getHealth() > 0) {
                if (!(e.getEntityLiving() instanceof Player))
                    e.getEntityLiving().die(DamageSource.OUT_OF_WORLD);
                e.getEntityLiving().setHealth(0);
                ListHelper.removeEntityToList(e.getEntityLiving());
            }
        }
        if (ListHelper.entityContainsInRemoveList(e.getEntityLiving())) {
            if (e.getEntityLiving() != null) {
                e.getEntityLiving().discard();
                e.getEntityLiving().setInvisible(true);
                e.getEntityLiving().removeVehicle();
                e.getEntityLiving().remove(Entity.RemovalReason.DISCARDED);
                e.getEntityLiving().onRemovedFromWorld();
                e.getEntityLiving().setRemoved(Entity.RemovalReason.DISCARDED);
                e.getEntityLiving().setHealth(0);
                ListHelper.removeEntityToRemoveList(e.getEntityLiving());
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltipEvent(ItemTooltipEvent tooltipEvent) {
        if (tooltipEvent.getItemStack().getItem() == AvaritiaModContent.INFINITY_SWORD.get() ||
        tooltipEvent.getItemStack().getItem() instanceof InfinitySwordItem || tooltipEvent.getItemStack().getItem() instanceof InfinitySwordGodItem) {
            List<Component> tooltip = tooltipEvent.getToolTip();
            int size = tooltip.size();
            Component attackDamage = new TranslatableComponent("attribute.name.generic.attack_damage");
            String var10000 = RainbowText.makeColour("Infinity ");
            Component replacementAttackDamage = new TextComponent(" " + var10000 + ChatFormatting.DARK_GREEN + attackDamage.getString());
            for(int i = 0; i < size; ++i) {
                Component line = tooltip.get(i);
                if (line.getString().contains(attackDamage.getString())) {
                    tooltip.set(i, replacementAttackDamage);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemColor(ColorHandlerEvent.Item event) {
        long time = System.currentTimeMillis();
        int r = (int) (Math.sin(time * 0.001) * 0.5 + 0.5);
        int g = (int) (Math.sin(time * 0.001 + 2.0 * Math.PI / 3.0) * 0.5 + 0.5);
        int b = (int) (Math.sin(time * 0.001 + 4.0 * Math.PI / 3.0) * 0.5 + 0.5);
        event.getItemColors().register((ItemStack stack, int tintIndex) -> r, MoreAvaritiaModItems.ENDLESS_ENERGY.get());
        event.getItemColors().register((ItemStack stack, int tintIndex) -> g, MoreAvaritiaModItems.INFINITY_ARMOR_ARMOR_HELMET.get());
        event.getItemColors().register((ItemStack stack, int tintIndex) -> b, MoreAvaritiaModItems.INFINITY_ARMOR_ARMOR_BOOTS.get());
        event.getItemColors().register((ItemStack stack, int tintIndex) -> r, MoreAvaritiaModItems.INFINITY_ARMOR_ARMOR_CHESTPLATE.get());
        event.getItemColors().register((ItemStack stack, int tintIndex) -> g, MoreAvaritiaModItems.INFINITY_ARMOR_ARMOR_LEGGINGS.get());
        event.getItemColors().register((ItemStack stack, int tintIndex) -> g, MoreAvaritiaModItems.INFINITY_SWORD_GOD.get());
    }

    public static ItemStack findItem(Player player, ItemStack i) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack == i) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean hasItem(Player player, Item i) {
        if (player == null)
            return false;
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() == i) {
                return true;
            }
        }
        return player.getInventory().contains(new ItemStack(i));
    }

    public static boolean hasInfinityArmor(Player player) {
        if (player == null)
            return false;
        return player.getInventory().getArmor(3).getItem() == MoreAvaritiaModItems.SUPER_INFINITY_HEAD.get() &&
                player.getInventory().getArmor(2).getItem() == MoreAvaritiaModItems.SUPER_INFINITY_CHEST.get() &&
                player.getInventory().getArmor(1).getItem() == MoreAvaritiaModItems.SUPER_INFINITY_LEG.get() &&
                player.getInventory().getArmor(0).getItem() == MoreAvaritiaModItems.SUPER_INFINITY_BOOTS.get();
    }

    @SubscribeEvent
    public static void onTextureStitchPre(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            event.addSprite(new ResourceLocation("more_avaritia", "models/infinity_armor_wing"));
            event.addSprite(new ResourceLocation("more_avaritia", "models/infinity_armor_wingglow"));
        }
    }

    @SubscribeEvent
    public static void onTextureStitchPost(TextureStitchEvent.Post event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            MoreClientInit.WING = event.getAtlas().getSprite(new ResourceLocation("more_avaritia", "models/infinity_armor_wing"));
            MoreClientInit.WING_GLOW = event.getAtlas().getSprite(new ResourceLocation("more_avaritia", "models/infinity_armor_wingglow"));
        }
    }

    @SubscribeEvent
    public static void jumpBoost(LivingEvent.LivingJumpEvent e) {
        LivingEntity ent = e.getEntityLiving();
        if (entitiesWithBoots.contains(ent.getEncodeId() + "|" + ent.getEncodeId()))
            ent.setDeltaMovement(ent.getDeltaMovement().add(0.0D, 0.4000000059604645D, 0.0D));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawScreenPre(ScreenEvent.DrawScreenEvent.Pre e) {
        MoreClientInit.inventoryRender = true;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawScreenPost(ScreenEvent.DrawScreenEvent.Post e) {
        MoreClientInit.inventoryRender = false;
    }
}
