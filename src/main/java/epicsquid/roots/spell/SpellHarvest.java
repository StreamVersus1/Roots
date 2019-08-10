package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageHarvestCompleteFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.HarvestUtil;
import epicsquid.roots.util.ItemUtil;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.*;

public class SpellHarvest extends SpellBase {
  public static String spellName = "spell_harvest";
  public static SpellHarvest instance = new SpellHarvest(spellName);

  public SpellHarvest(String name) {
    super(name, TextFormatting.GREEN, 57f / 255f, 253f / 255f, 28f / 255f, 197f / 255f, 233f / 255f, 28f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 25;

    addCost(HerbRegistry.getHerbByName("wildewheet"), 0.55f);
    addIngredients(
        new ItemStack(Items.GOLDEN_HOE),
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(ModItems.wildewheet),
        new ItemStack(ModItems.terra_moss),
        new ItemStack(Items.WHEAT_SEEDS)
    );
  }

  private static List<Block> skipBlocks = Arrays.asList(Blocks.BEDROCK, Blocks.GRASS, Blocks.DIRT, Blocks.STONE, Blocks.TALLGRASS, Blocks.WATER, Blocks.LAVA, Blocks.DOUBLE_PLANT, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
  private static Map<IProperty<Integer>, Integer> stateMax = new Object2IntOpenHashMap<>();

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    HarvestUtil.prepare();
    List<BlockPos> affectedPositions = new ArrayList<>();
    List<BlockPos> pumpkinsAndMelons = new ArrayList<>();
    List<BlockPos> reedsAndCactus = new ArrayList<>();
    List<BlockPos> crops = Util.getBlocksWithinRadius(player.world, player.getPosition(),
        6, 5, 6, (pos) -> {
          if (player.world.isAirBlock(pos)) return false;
          IBlockState state = player.world.getBlockState(pos);
          Block block = state.getBlock();

          if (skipBlocks.contains(block)) return false;

          if (block.getRegistryName() != null && block.getRegistryName().getNamespace().equals("rustic")) return false;

          if (state.getBlock() == Blocks.PUMPKIN || state.getBlock() == Blocks.MELON_BLOCK) {
            pumpkinsAndMelons.add(pos);
            return false;
          }
          if (state.getBlock() == Blocks.REEDS || state.getBlock() == Blocks.CACTUS) {
            reedsAndCactus.add(pos);
            return false;
          }
          for (IProperty<?> prop : state.getPropertyKeys()) {
            if ((prop.getName().equals("age") || prop.getName().equals("growth")) && prop.getValueClass() == Integer.class) {
              int max = stateMax.getOrDefault(prop, -1);
              if (max == -1) {
                max = Collections.max((Collection<Integer>) prop.getAllowedValues());
                stateMax.put((IProperty<Integer>) prop, max);
              }
              if (state.getValue((IProperty<Integer>) prop) == max) {
                return true;
              }
            }
          }
          return false;
        });

    int count = 0;

    for (BlockPos pos : crops) {
      IBlockState state = player.world.getBlockState(pos);
      ItemStack seed = HarvestUtil.getSeed(state);
      // Do do do the harvest!
      IProperty<Integer> prop = null;
      for (IProperty<Integer> entry : stateMax.keySet()) {
        if (state.getPropertyKeys().contains(entry)) {
          prop = entry;
        }
      }

      assert prop != null;

      if (!player.world.isRemote) {
        IBlockState newState = state.withProperty(prop, 0);
        NonNullList<ItemStack> drops = NonNullList.create();
        HarvestUtil.add(seed, player.dimension, pos, state);
        state.getBlock().getDrops(drops, player.world, pos, state, 0);
        ForgeEventFactory.fireBlockHarvesting(drops, player.world, pos, state, 0, 1.0f, false, player);
        player.world.setBlockState(pos, newState);
        for (ItemStack stack : drops) {
          if (stack.isEmpty()) continue;
          ItemUtil.spawnItem(player.world, pos, stack);
        }
        affectedPositions.add(pos);
      }
      count++;
    }
    for (BlockPos pos : pumpkinsAndMelons) {
      count++;
      if (!player.world.isRemote) {
        IBlockState state = player.world.getBlockState(pos);
        player.world.setBlockState(pos, Blocks.AIR.getDefaultState());
        state.getBlock().harvestBlock(player.world, player, pos, state, null, player.getHeldItemMainhand());
        affectedPositions.add(pos);
      }
    }
    Set<BlockPos> done = new HashSet<>();
    List<BlockPos> lowest = new ArrayList<>();
    for (BlockPos pos : reedsAndCactus) {
      if (done.contains(pos)) continue;

      BlockPos down = pos.down();
      IBlockState downState = player.world.getBlockState(down);
      while (downState.getBlock() == Blocks.CACTUS || downState.getBlock() == Blocks.REEDS) {
        done.add(down);
        down = down.down();
        downState = player.world.getBlockState(down);
      }
      lowest.add(down.up());
      done.add(pos);
    }
    for (BlockPos pos : lowest) {
      IBlockState state = player.world.getBlockState(pos.up());
      if (state.getBlock() == Blocks.CACTUS || state.getBlock() == Blocks.REEDS) {
        count++;
        if (!player.world.isRemote) {
          state.getBlock().harvestBlock(player.world, player, pos.up(), state, null, player.getHeldItemMainhand());
          player.world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
          affectedPositions.add(pos);
        }
      }
    }

    if (!affectedPositions.isEmpty() && !player.world.isRemote) {
      MessageHarvestCompleteFX message = new MessageHarvestCompleteFX(affectedPositions);
      PacketHandler.sendToAllTracking(message, player);
    }

    return count != 0;
  }
}
