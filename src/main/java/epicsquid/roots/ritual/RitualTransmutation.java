package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualOvergrowth;
import epicsquid.roots.entity.ritual.EntityRitualTransmutation;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualTransmutation extends RitualBase {

  public RitualTransmutation(String name, int duration) {
    super(name, duration);
    addCondition(
        new ConditionItems(
            new ItemStack(Blocks.FURNACE),
            new ItemStack(Blocks.MOSSY_COBBLESTONE),
            new ItemStack(ModItems.cloud_berry),
            new ItemStack(ModItems.bark_birch),
            new ItemStack(ModBlocks.chiseled_runestone)));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualTransmutation.class);
  }

}