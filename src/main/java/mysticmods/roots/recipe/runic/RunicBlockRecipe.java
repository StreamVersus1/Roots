package mysticmods.roots.recipe.runic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.WorldRecipe;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RunicBlockRecipe extends WorldRecipe<SimpleWorldCrafting> {
  protected final List<String> skipProperties = new ArrayList<>();
  protected int durabilityCost = 1;

  public RunicBlockRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public ItemStack getBaseResultItem() {
    return getResultItem();
  }

  @Override
  public void setIngredients(NonNullList<Ingredient> ingredients) {
  }

  public int getDurabilityCost() {
    return durabilityCost;
  }

  public void setDurabilityCost(int cost) {
    this.durabilityCost = cost;
  }

  public List<String> getSkipProperties() {
    return skipProperties;
  }

  public void setSkipProperties(List<String> skipProperty) {
    this.skipProperties.clear();
    this.skipProperties.addAll(skipProperty);
  }

  @Override
  public BlockState modifyState(SimpleWorldCrafting pContainer, BlockState state) {
    BlockState newState = outputState;
    for (Property<?> prop : newState.getProperties()) {
      if (!state.hasProperty(prop)) {
        continue;
      }

      if (skipProperties.contains(prop.getName())) {
        continue;
      }

      newState = copyProperty(state, newState, prop);
    }

    return newState;
  }

  private static <T extends Comparable<T>> BlockState copyProperty(BlockState pSourceState, BlockState pTargetState, Property<T> pProperty) {
    return pTargetState.setValue(pProperty, pSourceState.getValue(pProperty));
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.RUNIC_BLOCK.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.RUNIC_BLOCK.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.RUNIC_BLOCK_RECIPE_GROUP;
  }

  public static class Serializer extends WorldRecipe.Serializer<SimpleWorldCrafting, RunicBlockRecipe> {
    public Serializer() {
      super(RunicBlockRecipe::new);
    }

    @Override
    protected void fromJsonAdditional(RunicBlockRecipe recipe, ResourceLocation pRecipeId, JsonObject pJson) {
      super.fromJsonAdditional(recipe, pRecipeId, pJson);
      if (pJson.has("skip_properties")) {
        JsonArray array = pJson.getAsJsonArray("skip_properties");
        List<String> skipProps = new ArrayList<>();
        array.forEach(o -> skipProps.add(o.getAsString()));
        recipe.setSkipProperties(skipProps);
      }
      if (pJson.has("durability_cost")) {
        recipe.setDurabilityCost(GsonHelper.getAsInt(pJson, "durability_cost"));
      }
    }

    @Override
    protected void fromNetworkAdditional(RunicBlockRecipe recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      super.fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      int size = pBuffer.readVarInt();
      for (int i = 0; i < size; i++) {
        recipe.skipProperties.add(pBuffer.readUtf());
      }
      recipe.setDurabilityCost(pBuffer.readVarInt());
    }

    @Override
    protected void toNetworkAdditional(RunicBlockRecipe recipe, FriendlyByteBuf pBuffer) {
      super.toNetworkAdditional(recipe, pBuffer);
      pBuffer.writeVarInt(recipe.skipProperties.size());
      for (String property : recipe.skipProperties) {
        pBuffer.writeUtf(property);
      }
      pBuffer.writeVarInt(recipe.durabilityCost);
    }
  }

  public static class Builder extends WorldRecipe.Builder {
    protected List<String> skipProperties = new ArrayList<>();
    protected int durability_cost;

    public Builder() {
    }

    public Builder(ItemStack result) {
      super(result);
    }

    public Builder skipProperty (String property) {
      this.skipProperties.add(property);
      return this;
    }

    public Builder skipProperty (Property<?> property) {
      this.skipProperties.add(property.getName());
      return this;
    }

    public Builder durabilityCost (int cost) {
      this.durability_cost = cost;
      return this;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModSerializers.RUNIC_BLOCK.get();
    }

    @Override
    protected boolean requireIngredients() {
      return false;
    }

    @Override
    public void doSave(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new Result(recipeName, result, outputState, condition, chanceOutputs, grants, levelConditions, playerConditions, getSerializer(), advancement, getAdvancementId(recipeName), skipProperties, durability_cost));
    }

    public static class Result extends WorldRecipe.Builder.Result {
      private final List<String> skipProperties;
      private final int durability_cost;

      public Result(ResourceLocation id, ItemStack result, BlockState outputState, Condition condition, List<ChanceOutput> chanceOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, Advancement.Builder advancementBuilder, ResourceLocation advancementId, List<String> skipProperties, int durabilityCost) {
        super(id, result, outputState, condition, chanceOutputs, grants, levelConditions, playerConditions, serializer, advancementBuilder, advancementId);
        this.skipProperties = skipProperties;
        this.durability_cost = durabilityCost;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        if (!skipProperties.isEmpty()) {
          JsonArray skipPropertiesArray = new JsonArray();
          for (String property : skipProperties) {
            skipPropertiesArray.add(property);
          }
          json.add("skip_properties", skipPropertiesArray);
        }
        json.addProperty("durability_cost", durability_cost);
      }
    }
  }

  public static Builder builder(ItemStack stack) {
    return new Builder(stack);
  }

  public static Builder builder(ItemLike item, int count) {
    return new Builder(new ItemStack(item, count));
  }

  public static Builder builder(ItemLike item) {
    return builder(item, 1);
  }
}
