package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.BaseRitualEntity;
import epicsquid.roots.entity.ritual.WardingProtectionRitualEntity;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import epicsquid.roots.util.types.Property;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualWardingProtection extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(1200);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(20);
  public static Property<Integer> PROP_INVULN_DURATION = new Property<>("invuln_duration", 22);

  public double radius_x, radius_y, radius_z;
  public int interval, invuln_duration;

  public RitualWardingProtection(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL, PROP_INVULN_DURATION);
  }

  @Override
  public void init() {
    addCondition(new ConditionItems(
        new ItemStack(Items.GLISTERING_MELON_SLICE),
        new ItemStack(ModItems.stalicripe),
        new ItemStack(ModItems.wildroot),
        /*        new OreIngredient("rootsBark"),*/
        null,
        new ItemStack(Items.IRON_CHESTPLATE)
    ));
    addCondition(new ConditionStandingStones(3, 3));
    addCondition(new ConditionStandingStones(4, 3));
    setIcon(ModItems.ritual_warding_protection);
    setColor(TextFormatting.DARK_BLUE);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.getProperty(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5;
    radius_y = radius[1] + 0.5;
    radius_z = radius[2] + 0.5;
    interval = properties.getProperty(PROP_INTERVAL);
    invuln_duration = properties.getProperty(PROP_INVULN_DURATION);
  }

  @Override
  public BaseRitualEntity doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, WardingProtectionRitualEntity.class);
  }
}