package net.wzz.more_avaritia.event;

import net.wzz.more_avaritia.init.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wzz.more_avaritia.init.ModItems;
import net.wzz.more_avaritia.util.InfinityUtils;
import net.wzz.more_avaritia.util.ListUtils;

import java.util.List;

@Mod.EventBusSubscriber
public class InfinityHandle {

    @SubscribeEvent
    public static void onLeftBlock(PlayerInteractEvent.LeftClickBlock e) {
        Level world = e.getLevel();
        if (e.getEntity().getMainHandItem().getItem() == ModItems.INFINITY_GOD_SWORD.get() || e.getEntity().getMainHandItem().getItem() == ModItems.INFINITY_PICKAXE.get() && !e.getEntity().getMainHandItem().getOrCreateTag().getBoolean("hammer")) {
            BlockState blockState = e.getLevel().getBlockState(e.getPos());
            Block block = blockState.getBlock();
            if (!world.isClientSide() && (block == Blocks.BEDROCK || block == Blocks.END_PORTAL_FRAME || block == Blocks.NETHER_PORTAL || block == Blocks.END_PORTAL
            || block.properties.destroyTime == -1)) {
                e.getLevel().addFreshEntity(new ItemEntity(e.getLevel(),
                        e.getPos().getX(),
                        e.getPos().getY(),
                        e.getPos().getZ(),
                        new ItemStack(block)));
                e.getLevel().destroyBlock(e.getPos(), false);
            }
        }
        if (e.getEntity().getMainHandItem().getItem() == ModItems.INFINITY_HOE.get()) {
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

    @SubscribeEvent
    public static void onBlockBreaking(BlockEvent.BreakEvent e) {
        if (e.getPlayer().getMainHandItem().getItem() == ModItems.INFINITY_AXE.get()) {
            BlockState blockState = e.getState();
            Block block = blockState.getBlock();
            ItemStack dropStack = new ItemStack(block);
            if (!dropStack.isEmpty()) {
                for (ItemStack itemStack : Block.getDrops(blockState, (ServerLevel) e.getLevel(), e.getPos(), null)) {
                    ItemStack tripledDrop = itemStack.copy();
                    tripledDrop.setCount(tripledDrop.getCount() * 2);
                    e.getLevel().addFreshEntity(new ItemEntity((Level) e.getLevel(), e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), tripledDrop));
                }
            }
        }
        if (e.getPlayer().getMainHandItem().getItem() == ModItems.INFINITY_PICKAXE.get()) {
            ItemStack tool = e.getPlayer().getMainHandItem();
            BlockState blockState = e.getState();
            if (blockState.is(BlockTags.MINEABLE_WITH_PICKAXE) && !isSilkTouch(tool)) {
                ItemStack meltingResult = getMeltingResult((ServerLevel) e.getLevel(), e.getPos(), e.getPlayer(), tool);
                if (!meltingResult.isEmpty()) {
                    e.getLevel().addFreshEntity(new ItemEntity((Level) e.getLevel(),
                            e.getPos().getX() + 0.5D,
                            e.getPos().getY() + 0.5D,
                            e.getPos().getZ() + 0.5D,
                            meltingResult));
                    e.getLevel().setBlock(e.getPos(), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
        if (e.getPlayer().getMainHandItem().getItem() == ModItems.INFINITY_HOE.get()) {
            e.setCanceled(true);
        }
    }

    private static boolean isSilkTouch(ItemStack tool) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;
    }

    private static ItemStack getMeltingResult(ServerLevel level, BlockPos pos, Player player, ItemStack tool) {
        BlockState state = level.getBlockState(pos);
        ItemStack result = getSmeltingResult(new ItemStack(state.getBlock()), level);
        if (result.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int count = 0;
        List<ItemStack> drops = Block.getDrops(state, level, pos, level.getBlockEntity(pos), player, tool);
        for (ItemStack drop : drops) {
            ItemStack dropResult = getSmeltingResult(drop, level);
            if (!dropResult.isEmpty() && ItemStack.isSameItem(dropResult, result)) {
                count += drop.getCount();
            }
        }
        result.setCount(result.getCount() * Math.max(count, 1));
        return result;
    }

    private static ItemStack getSmeltingResult(ItemStack stack, Level level) {
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), level)
                .map(recipe -> recipe.getResultItem(level.registryAccess()).copy())
                .orElse(ItemStack.EMPTY);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent e) {
        if (ListUtils.isDieEntity(e.getEntity())) {
            InfinityUtils.easyAttack(e.getEntity(), null);
            ListUtils.removeDieEntity(e.getEntity());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingAttack(LivingAttackEvent e) {
        if (e.getSource() != null && e.getEntity() instanceof Player player
                && e.getSource().getEntity() instanceof LivingEntity living && living != player) {
            if (InfinityUtils.hasInfinityArmor(player) && !e.getSource().is(ModDamageTypes.INFINITY)) {
                e.setCanceled(true);
                living.hurt(player.damageSources().source(ModDamageTypes.INFINITY, player, e.getEntity()), Float.POSITIVE_INFINITY);
            }
        }
    }

    @SubscribeEvent
    public static void onGodSwordParry(LivingAttackEvent e) {
        if (!(e.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }
        if (!player.isUsingItem() || player.getUseItem().getItem() != ModItems.INFINITY_GOD_SWORD.get()) {
            return;
        }
        DamageSource source = e.getSource();
        Entity direct = source.getDirectEntity();
        if (direct instanceof Projectile projectile) {
            e.setCanceled(true);
            Vec3 motion = projectile.getDeltaMovement();
            Vec3 back = motion.lengthSqr() > 1.0E-6D ? motion.normalize().scale(0.8D) : Vec3.ZERO;
            projectile.setPos(projectile.getX() - back.x, projectile.getY() - back.y, projectile.getZ() - back.z);
            projectile.setDeltaMovement(motion.scale(-1.0D));
            projectile.hasImpulse = true;
            if (projectile instanceof AbstractArrow arrow) {
                arrow.setOwner(player);
            }
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (source.getEntity() instanceof LivingEntity attacker && attacker != player) {
            e.setCanceled(true);
            attacker.hurt(player.damageSources().source(ModDamageTypes.INFINITY, player, player), Math.max(e.getAmount(), 10.0F));
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent e) {
        if (e.getEntity() instanceof Player player)
            if (InfinityUtils.hasInfinityArmor(player))
                e.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.player.getPersistentData().getBoolean("isInfinityArmorFly")) {
            if (InfinityUtils.hasInfinityArmor(e.player)) {
                if (!e.player.getAbilities().mayfly) {
                    e.player.getAbilities().mayfly = true;
                    e.player.onUpdateAbilities();
                }
            } else {
                e.player.getPersistentData().putBoolean("isInfinityArmorFly", false);
                if (e.player.getAbilities().mayfly && !e.player.isCreative()) {
                    e.player.getAbilities().mayfly = false;
                    e.player.onUpdateAbilities();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingKnockBack(LivingKnockBackEvent e) {
        if (e.getEntity() instanceof Player player && InfinityUtils.hasInfinityArmor(player)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
        InfinityUtils.clearGodSwordGlow(e.getEntity());
    }
}
