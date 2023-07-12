package mysticmods.roots.api.recipe;

import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.output.ConditionalOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class RootsRecipeBase implements IRootsRecipeBase {
  protected final ResourceLocation recipeId;
  protected final List<ConditionalOutput> conditionalOutputs = new ArrayList<>();
  protected final List<Grant> grants = new ArrayList<>();

  protected final List<LevelCondition> levelConditions = new ArrayList<>();
  protected final List<PlayerCondition> playerConditions = new ArrayList<>();
  protected ItemStack result;

  public RootsRecipeBase(ResourceLocation recipeId) {
    this.recipeId = recipeId;
  }

  @Override
  public void setLevelConditions(List<LevelCondition> levelConditions) {
    this.levelConditions.clear();
    this.levelConditions.addAll(levelConditions);
  }

  @Override
  public void setPlayerConditions(List<PlayerCondition> playerConditions) {
    this.playerConditions.clear();
    this.playerConditions.addAll(playerConditions);
  }

  @Override
  public List<LevelCondition> getLevelConditions() {
    return levelConditions;
  }

  @Override
  public List<PlayerCondition> getPlayerConditions() {
    return playerConditions;
  }

  @Override
  public void setResultItem(ItemStack result) {
    this.result = result;
  }

  @Override
  public void addConditionalOutput(ConditionalOutput output) {
    conditionalOutputs.add(output);
  }

  @Override
  public void addConditionalOutputs(Collection<ConditionalOutput> outputs) {
    conditionalOutputs.addAll(outputs);
  }

  @Override
  public void addGrant(Grant grant) {
    grants.add(grant);
  }

  @Override
  public void addGrants(Collection<Grant> grants) {
    this.grants.addAll(grants);
  }

  @Override
  public List<ConditionalOutput> getConditionalOutputs() {
    return conditionalOutputs;
  }

  @Override
  public List<Grant> getGrants() {
    return grants;
  }
}
