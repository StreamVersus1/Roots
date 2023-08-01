package mysticmods.roots.api.recipe.crafting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Set;
import java.util.function.Predicate;

public interface IEntityCrafting extends IRootsCraftingBase {
  LivingEntity getEntity ();

  @Override
  default int getContainerSize() {
    return 0;
  }

  @Override
  default boolean isEmpty() {
    return false;
  }

  @Override
  default ItemStack getItem(int pSlot) {
    return ItemStack.EMPTY;
  }


  @Override
  default ItemStack removeItem(int pSlot, int pAmount) {
    return ItemStack.EMPTY;
  }

  @Override
  default ItemStack removeItemNoUpdate(int pSlot) {
    return ItemStack.EMPTY;
  }

  @Override
  default void setItem(int pSlot, ItemStack pStack) {
    return;
  }

  @Override
  default int getMaxStackSize() {
    return 0;
  }

  @Override
  default boolean canPlaceItem(int pIndex, ItemStack pStack) {
    return false;
  }

  @Override
  default int countItem(Item pItem) {
    return 0;
  }

  @Override
  default boolean hasAnyOf(Set<Item> pSet) {
    return false;
  }

  @Override
  default boolean hasAnyMatching(Predicate<ItemStack> p_216875_) {
    return false;
  }
}