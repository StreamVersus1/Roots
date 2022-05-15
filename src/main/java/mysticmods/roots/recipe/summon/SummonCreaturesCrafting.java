package mysticmods.roots.recipe.summon;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.entity.PyreBlockEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class SummonCreaturesCrafting extends RootsTileCrafting<SummonCreaturesInventory, PyreBlockEntity> {
  public SummonCreaturesCrafting(SummonCreaturesInventory handler, PyreBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}