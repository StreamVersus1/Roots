package mysticmods.roots.blockentity;

import mysticmods.roots.RootsTags;
import mysticmods.roots.api.ClientTickBlockEntity;
import mysticmods.roots.api.InventoryBlockEntity;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.ServerTickBlockEntity;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ModRegistries;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.pyre.PyreCrafting;
import mysticmods.roots.recipe.pyre.PyreInventory;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;
import noobanidus.libs.noobutil.util.ItemUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// This controls the LIT state
public class PyreBlockEntity extends UseDelegatedBlockEntity implements ClientTickBlockEntity, ServerTickBlockEntity, InventoryBlockEntity {
  private final PyreInventory inventory = new PyreInventory() {
    @Override
    protected void onContentsChanged(int slot) {
      if (PyreBlockEntity.this.hasLevel() && !PyreBlockEntity.this.getLevel().isClientSide()) {
        PyreBlockEntity.this.revalidateRecipe();
        PyreBlockEntity.this.updateViaState();
      }
    }
  };
  private final PyreCrafting playerlessCrafting = new PyreCrafting(inventory, this, null);
  private final List<ItemStack> previousRecipeItems = new ArrayList<>();
  private PyreRecipe lastRecipe = null;
  private PyreRecipe cachedRecipe = null;
  private Ritual currentRitual = null;
  private ItemStack storedItem = null;
  private int lifetime = -1;

  public PyreBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
    RootsAPI.LOG.info(cachedRecipe);
    ItemStack inHand = player.getItemInHand(hand);
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }
    if (inHand.isEmpty()) {
      // extract
      ItemStack popped = inventory.pop();
      if (!popped.isEmpty()) {
        ItemUtil.Spawn.spawnItem(level, getBlockPos(), popped);
      }
    } else if (inHand.is(RootsTags.Items.MORTAR_ACTIVATION)) {
      if (cachedRecipe == null) {
        // should this revalidate?
        revalidateRecipe();
      }
      if (cachedRecipe != null && cachedRecipe.matches(playerlessCrafting, level)) {
        // TODO: CRAFTING RITUAL
        // CRAFTING HAPPENS HERE, OR IT BECOMES A RITUAL!!!
        PyreCrafting playerCrafting = new PyreCrafting(inventory, this, player);
        // ritual things have to happen
        lastRecipe = cachedRecipe;
        previousRecipeItems.clear();
        previousRecipeItems.addAll(inventory.getItemsCopy());
        ItemStack result = cachedRecipe.assemble(playerCrafting);
        // process
        NonNullList<ItemStack> processed = cachedRecipe.process(inventory.getItemsAndClear());
        /*        ItemUtil.Spawn.spawnItem(level, player.blockPosition(), result);*/
        for (ItemStack stack : processed) {
          ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
        }
        cachedRecipe = null;
        setChanged();
      }
    } else {
      // insert
      player.setItemInHand(hand, inventory.insert(inHand));
    }

    return InteractionResult.SUCCESS;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    super.onDataPacket(net, pkt);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      load(tag);
    } else {
      lastRecipe = null;
      cachedRecipe = null;
      currentRitual = null;
      storedItem = ItemStack.EMPTY;
    }
  }

  protected void revalidateRecipe() {
    boolean matched = false;
    if (cachedRecipe == null) {
      if (lastRecipe != null && lastRecipe.matches(playerlessCrafting, getLevel())) {
        cachedRecipe = lastRecipe;
        matched = true;
      } else {
        cachedRecipe = ResolvedRecipes.PYRE.findRecipe(playerlessCrafting, getLevel());
      }
    }

    if (cachedRecipe != null) {
      // only test once
      if (!matched && !cachedRecipe.matches(playerlessCrafting, getLevel())) {
        cachedRecipe = null;
      }
    }
  }

  @Override
  protected void saveAdditional(CompoundTag pTag) {
    super.saveAdditional(pTag);
    ListTag previousItems = new ListTag();
    for (ItemStack stack : previousRecipeItems) {
      if (!stack.isEmpty()) {
        previousItems.add(stack.save(new CompoundTag()));
      }
    }

    if (!previousItems.isEmpty()) {
      pTag.put("previous_items", previousItems);
    }

    if (cachedRecipe != null) {
      pTag.putString("cached_recipe", cachedRecipe.getId().toString());
    }
    if (lastRecipe != null) {
      pTag.putString("last_recipe", lastRecipe.getId().toString());
    }
    if (currentRitual != null) {
      pTag.putString("current_ritual", ModRegistries.RITUAL_REGISTRY.get().getKey(currentRitual).toString());
    }
    if (!storedItem.isEmpty()) {
      pTag.put("stored_item", storedItem.serializeNBT());
    }
    pTag.putInt("lifetime", lifetime);
    pTag.put("inventory", inventory.serializeNBT());
  }

  @Override
  public void load(CompoundTag pTag) {
    super.load(pTag);
    if (pTag.contains("previous_items", Tag.TAG_LIST)) {
      previousRecipeItems.clear();
      ListTag previousItems = pTag.getList("previous_items", Tag.TAG_COMPOUND);
      for (int i = 0; i < previousItems.size(); i++) {
        previousRecipeItems.add(ItemStack.of(previousItems.getCompound(i)));
      }
    }
    if (pTag.contains("cached_recipe", Tag.TAG_STRING)) {
      ResourceLocation cachedId = new ResourceLocation(pTag.getString("cached_recipe"));
      cachedRecipe = ResolvedRecipes.PYRE.getRecipe(cachedId);
    }
    if (pTag.contains("last_recipe", Tag.TAG_STRING)) {
      ResourceLocation lastId = new ResourceLocation(pTag.getString("last_recipe"));
      lastRecipe = ResolvedRecipes.PYRE.getRecipe(lastId);
    }
    if (pTag.contains("inventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(pTag.getCompound("inventory"));
    }
    if (pTag.contains("current_ritual", Tag.TAG_STRING)) {
      ResourceLocation ritualId = new ResourceLocation(pTag.getString("current_ritual"));
      currentRitual = ModRegistries.RITUAL_REGISTRY.get().getValue(ritualId);
    }
    if (pTag.contains("lifetime", Tag.TAG_INT)) {
      lifetime = pTag.getInt("lifetime");
    }
    if (pTag.contains("stored_item", Tag.TAG_COMPOUND)) {
      storedItem = ItemStack.of(pTag.getCompound("stored_item"));
    }
  }

  @Override
  public ItemStackHandler getInventory() {
    return inventory;
  }

  public ItemStack getStoredItem() {
    return storedItem;
  }

  public int getLifetime () {
    return lifetime;
  }

  @Nullable
  public Ritual getCurrentRitual() {
    return currentRitual;
  }

  @Override
  // TODO: handle client ticking
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (currentRitual != null && lifetime > 0) {
      currentRitual.animateTick(this);
    }
  }

  @Override
  public void serverTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (currentRitual != null && lifetime > 0) {
      lifetime--;
      setChanged();
      if (lifetime <= 0) {
        currentRitual = null;
        if (pState.is(RootsTags.Blocks.PYRES) && pState.hasProperty(PyreBlock.LIT)) {
          pLevel.setBlock(pPos, pState.setValue(PyreBlock.LIT, false), 3);
        } else {
          updateViaState();
        }
      } else {
        currentRitual.ritualTick(this);
        if (pState.is(RootsTags.Blocks.PYRES) && pState.hasProperty(PyreBlock.LIT) && !pState.getValue(PyreBlock.LIT)) {
          pLevel.setBlock(pPos, pState.setValue(PyreBlock.LIT, true), 3);
        }
      }
    }
  }
}