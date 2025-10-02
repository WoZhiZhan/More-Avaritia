package net.wzz.more_avaritia.event;

import committee.nova.mods.avaritia.init.registry.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wzz.more_avaritia.init.MoreAvaritiaModItems;
import net.wzz.more_avaritia.util.InfinityUtils;
import net.wzz.more_avaritia.util.ListUtils;

@Mod.EventBusSubscriber
public class InfinityHandle {

    @SubscribeEvent
    public static void onLeftBlock(PlayerInteractEvent.LeftClickBlock e) {
        Level world = e.getLevel();
        if (e.getEntity().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_GOD_SWORD.get() || e.getEntity().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_PICKAXE.get() && !e.getEntity().getMainHandItem().getOrCreateTag().getBoolean("hammer")) {
            BlockState blockState = e.getLevel().getBlockState(e.getPos());
            Block block = blockState.getBlock();
            if (block == Blocks.BEDROCK || block == Blocks.END_PORTAL_FRAME || block == Blocks.NETHER_PORTAL || block == Blocks.END_PORTAL
            || block.properties.destroyTime == -1) {
                e.getLevel().addFreshEntity(new ItemEntity(e.getLevel(),
                        e.getPos().getX(),
                        e.getPos().getY(),
                        e.getPos().getZ(),
                        new ItemStack(block)));
                e.getLevel().destroyBlock(e.getPos(), false);
            }
        }
        if (e.getEntity().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_HOE.get()) {
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
        if (e.getPlayer().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_AXE.get()) {
            BlockState blockState = e.getState();
            Block block = blockState.getBlock();
            ItemStack dropStack = new ItemStack(block);
            if (!dropStack.isEmpty()) {
                for (ItemStack itemStack : Block.getDrops(blockState, (ServerLevel) e.getLevel(), e.getPos(), null)) {
                    ItemStack tripledDrop = itemStack.copy();
                    tripledDrop.setCount(tripledDrop.getCount() * 3);
                    e.getLevel().addFreshEntity(new ItemEntity((Level) e.getLevel(), e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), tripledDrop));
                }
            }
        }
        if (e.getPlayer().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_PICKAXE.get()) {
            BlockState blockState = e.getState();
            Block block = blockState.getBlock();
            if (blockState.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                ItemStack resultStack = getSmeltingResult(block, e.getPlayer().level());
                if (!resultStack.isEmpty()) {
                    e.getLevel().addFreshEntity(new ItemEntity((Level) e.getLevel(),
                            e.getPos().getX(),
                            e.getPos().getY(),
                            e.getPos().getZ(),
                            resultStack));
                    e.setCanceled(true);
                    e.getLevel().setBlock(e.getPos(), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
        if (e.getPlayer().getMainHandItem().getItem() == MoreAvaritiaModItems.INFINITY_HOE.get()) {
            e.setCanceled(true);
        }
    }

    private static ItemStack getSmeltingResult(Block block, Level level) {
        ItemStack itemStack = new ItemStack(block);
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemStack), level).map(recipe -> recipe.getResultItem(
                level.registryAccess()).copy()).orElse(ItemStack.EMPTY).copy();
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent e) {
        if (ListUtils.isDieEntity(e.getEntity()))
            InfinityUtils.easyAttack(e.getEntity(), null);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingAttack(LivingAttackEvent e) {
        if (e.getEntity() instanceof Player player)
            if (InfinityUtils.hasInfinityArmor(player))
                e.setCanceled(true);
        if (e.getSource() != null && e.getEntity() instanceof Player player && e.getSource().getEntity() instanceof LivingEntity living && living != player) {
            if (InfinityUtils.hasInfinityArmor(player)) {
                living.hurt(player.damageSources().source(ModDamageTypes.INFINITY, player, e.getEntity()), Float.POSITIVE_INFINITY);
            }
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
}
