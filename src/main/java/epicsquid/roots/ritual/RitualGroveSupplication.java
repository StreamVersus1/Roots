package epicsquid.roots.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.advancements.Advancements;
import epicsquid.roots.block.groves.BlockGroveStone;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualDivineProtection;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageGroveCompleteFX;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualGroveSupplication extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(1200);
  public static Property<Boolean> PROP_RAIN = new Property<>("rain", true).setDescription("whether or not rain should be disabled");
  public static Property<Boolean> PROP_TIME = new Property<>("time", true).setDescription("whether or not time should be accelerated or slowed");
  public static Property<Integer> PROP_NIGHT_REDUCTION = new Property<>("night_reduction", 1).setDescription("the number of additional ticks (per tick) added to night");
  public static Property<Integer> PROP_DAY_EXTENSION = new Property<>("day_extension", 3).setDescription("the chance (1 in X) that ticks will be subtracted, lengthening the day");
  public static Property<Float> PROP_CONSECRATION_DAMAGE = new Property<>("consecration_damage", 4.0f).setDescription("damage done to undead creatures if Consecration is installed");
  public static Property<Float> PROP_FIRE_DAMAGE = new Property<>("fire_damage", 4.0f).setDescription("amount of fire damage done to undead creatures");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 2).setDescription("duration in SECONDS undead creatures will be set on fire for");
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15);
  public static Property<Integer> PROP_DAY_LENGTH = new Property<>("day_length", 24000).setDescription("the length of the day for use in calculating addition/subtraction (modify if you have mods that adjust day/night length");
  public static Property<Integer> PROP_NIGHT_THRESHOLD = new Property<>("night_threshold", 12000).setDescription("the point at which day transitions into night (modify if you have mods that adjust day/night length");

  public boolean rain, time;
  public float radius_x, radius_y, radius_z, consecration_damage, fire_damage;
  public int night_reduction, day_extension, fire_duration, day_length, night_threshold;

  public RitualGroveSupplication(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RAIN, PROP_TIME, PROP_NIGHT_REDUCTION, PROP_DAY_EXTENSION, PROP_CONSECRATION_DAMAGE, PROP_FIRE_DAMAGE, PROP_FIRE_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_DAY_LENGTH, PROP_NIGHT_THRESHOLD);
  }

  @Override
  public void init() {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.pereskia),
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(ModItems.bark_birch),
        new ItemStack(ModItems.bark_oak),
        new ItemStack(Items.GLOWSTONE_DUST)
    ));
    setIcon(ModItems.ritual_divine_protection);
    setColor(TextFormatting.YELLOW);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.getProperty(PROP_DURATION);
    rain = properties.getProperty(PROP_RAIN);
    time = properties.getProperty(PROP_TIME);
    night_reduction = properties.getProperty(PROP_NIGHT_REDUCTION);
    day_extension = properties.getProperty(PROP_DAY_EXTENSION);
    consecration_damage = properties.getProperty(PROP_CONSECRATION_DAMAGE);
    fire_damage = properties.getProperty(PROP_FIRE_DAMAGE);
    fire_duration = properties.getProperty(PROP_FIRE_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5f;
    radius_y = radius[1] + 0.5f;
    radius_z = radius[2] + 0.5f;
    day_length = properties.getProperty(PROP_DAY_LENGTH);
    night_threshold = properties.getProperty(PROP_NIGHT_THRESHOLD);
  }

/*     List<BlockPos> positions = Util.getBlocksWithinRadius(player.world, player.getPosition(), 15, 10, 15, ModBlocks.grove_stone);
    if (positions.isEmpty()) return false;

    boolean didStuff = false;

    List<BlockPos> changed = new ArrayList<>();

    for (BlockPos pos : positions) {
      IBlockState state = player.world.getBlockState(pos);
      if (state.getBlock() != ModBlocks.grove_stone) {
        continue;
      }

      if (state.getValue(BlockGroveStone.VALID)) {
        continue;
      }

      didStuff = true;
      changed.add(pos);

      if (!player.world.isRemote) {
        player.world.setBlockState(pos, state.withProperty(BlockGroveStone.VALID, true));
        Advancements.GROVE_TRIGGER.trigger((EntityPlayerMP) player, null);
      }
    }

    if (didStuff && !changed.isEmpty() && !player.world.isRemote) {
      MessageGroveCompleteFX message = new MessageGroveCompleteFX(changed);
      PacketHandler.sendToAllTracking(message, player);
    }

    return didStuff;*/

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualDivineProtection.class);
  }
}