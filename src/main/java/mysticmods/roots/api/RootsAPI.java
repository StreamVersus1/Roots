package mysticmods.roots.api;

import mysticmods.roots.api.access.IRecipeManagerAccessor;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RootsAPI {
  public static RootsAPI INSTANCE;

  public static RootsAPI getInstance() {
    return INSTANCE;
  }

  // Identifiers & Logs
  public static final String MODID = "roots";
  public static final String MOD_IDENTIFIER = "Roots";
  public static Logger LOG = LogManager.getLogger();

  // Registry keys
  public static ResourceKey<Registry<Herb>> HERB_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "herbs"));
  public static ResourceKey<Registry<Ritual>> RITUAL_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "rituals"));
  public static ResourceKey<Registry<Spell>> SPELL_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "spells"));
  public static ResourceKey<Registry<Modifier>> MODIFIER_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "modifiers"));
  public static ResourceKey<Registry<RitualProperty<?>>> RITUAL_PROPERTY_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "ritual_properties"));
  public static ResourceKey<Registry<SpellProperty<?>>> SPELL_PROPERTY_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "spell_properties"));
  public static ResourceKey<Registry<LevelCondition>> LEVEL_CONDITION_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "level_conditions"));
  public static ResourceKey<Registry<PlayerCondition>> PLAYER_CONDITION_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "player_conditions"));

  public static ResourceKey<Registry<SnapshotSerializer<?>>>
    SNAPSHOT_SERIALIZER_REGISTRY = key(new ResourceLocation(RootsAPI.MODID, "snapshot_serializers"));

  private static <T> ResourceKey<Registry<T>> key(ResourceLocation name) {
    return ResourceKey.createRegistryKey(name);
  }

  // Capability IDs
  public static final ResourceLocation HERB_CAPABILITY_ID = new ResourceLocation(MODID, "herb_capability");
  public static final ResourceLocation GRANT_CAPABILITY_ID = new ResourceLocation(MODID, "grant_capability");
  public static final ResourceLocation SNAPSHOT_CAPABILITY_ID = new ResourceLocation(MODID, "snapshot_capability");

  // Actual API methods
  public abstract IRecipeManagerAccessor getRecipeAccessor();

  public abstract void grant(ServerPlayer player, Grant grant);

  public abstract Player getPlayer();

  public abstract boolean isShiftKeyDown ();

  public RecipeManager getRecipeManager() {
    return getRecipeAccessor().getManager();
  }

  // Tags
  public static class Tags {
    public static class Blocks {
      // Crops & Adjacent
      // Forge compat tag (filled by ModTags)
      public static final TagKey<Block> FORGE_CROPS = compatTag("crops");

      // Minecraft compat tag (filled by ModTags)
      public static final TagKey<Block> MINECRAFT_LOGS_THAT_BURN = compatTag("minecraft", "logs_that_burn");
      public static final TagKey<Block> MINECRAFT_LOGS = compatTag("minecraft", "logs");

      // General crops (filled by ModTags)
      public static final TagKey<Block> CROPS = modTag("crops");
      public static final TagKey<Block> ELEMENTAL_CROPS = modTag("crops/elemental");

      // Specific crop types (filled in by ModTags)
      public static final TagKey<Block> WATER_CROPS = modTag("crops/elemental/water");
      public static final TagKey<Block> EARTH_CROPS = modTag("crops/elemental/earth");
      public static final TagKey<Block> AIR_CROPS = modTag("crops/elemental/air");
      public static final TagKey<Block> FIRE_CROPS = modTag("crops/elemental/fire");

      // Specific crops (filled in by ModBlocks)
      public static final TagKey<Block> CLOUD_BERRY_CROP = modTag("crops/cloud_berry");
      public static final TagKey<Block> DEWGONIA_CROP = modTag("crops/dewgonia");
      public static final TagKey<Block> SPIRITLEAF_CROP = modTag("crops/spiritleaf");
      public static final TagKey<Block> STALICRIPE_CROP = modTag("crops/stalicripe");
      public static final TagKey<Block> WILDEWHEET_CROP = modTag("crops/wildewheet");
      public static final TagKey<Block> WILDROOT_CROP = modTag("crops/wildroot");
      public static final TagKey<Block> INFERNO_BULB_CROP = modTag("crops/inferno_bulb");
      public static final TagKey<Block> MOONGLOW_CROP = modTag("crops/moonglow");
      public static final TagKey<Block> PERESKIA_CROP = modTag("crops/pereskia");

      // General soils (filled in by ModTags)
      public static final TagKey<Block> SOILS = modTag("soils");

      // Specific soils
      public static final TagKey<Block> WATER_SOIL = modTag("soils/water");
      public static final TagKey<Block> AIR_SOIL = modTag("soils/air");
      public static final TagKey<Block> EARTH_SOIL = modTag("soils/earth");
      public static final TagKey<Block> FIRE_SOIL = modTag("soils/fire");
      public static final TagKey<Block> ELEMENTAL_SOIL = modTag("soils/elemental");

      public static final TagKey<Block> RUNED_OBSIDIAN = modTag("runed_obsidian");
      public static final TagKey<Block> RUNESTONE = modTag("runestone");
      public static final TagKey<Block> WILDWOOD_LOGS = modTag("logs/wildwood");

      // Specific types of Runed Logs
      public static final TagKey<Block> RUNED_LOGS = modTag("logs/runed");
      public static final TagKey<Block> RUNED_ACACIA_LOG = modTag("logs/runed/acacia");
      public static final TagKey<Block> RUNED_DARK_OAK_LOG = modTag("logs/runed/dark_oak");
      public static final TagKey<Block> RUNED_OAK_LOG = modTag("logs/runed/oak");
      public static final TagKey<Block> RUNED_BIRCH_LOG = modTag("logs/runed/birch");
      public static final TagKey<Block> RUNED_JUNGLE_LOG = modTag("logs/runed/jungle");
      public static final TagKey<Block> RUNED_SPRUCE_LOG = modTag("logs/runed/spruce");
      public static final TagKey<Block> RUNED_WILDWOOD_LOG = modTag("logs/runed/wildwood");
      public static final TagKey<Block> RUNED_CRIMSON_STEM = modTag("logs/runed/crimson");
      public static final TagKey<Block> RUNED_WARPED_STEM = modTag("logs/runed/warped");

      // Grove Stones
      public static final TagKey<Block> GROVE_STONES = modTag("grove_stones");

      public static final TagKey<Block> GROVE_STONE_PRIMAL = modTag("grove_stones/primal");

      // Catalyst plates, offering plates and incense plates
      public static final TagKey<Block> PEDESTALS = modTag("pedestals");
      public static final TagKey<Block> RITUAL_PEDESTALS = modTag("pedestals/ritual");
      public static final TagKey<Block> GROVE_PEDESTALS = modTag("pedestals/grove");

      // Pyres (does not include decorative)
      public static final TagKey<Block> PYRES = modTag("pyres");

      // Fey and runic crafters
      public static final TagKey<Block> CRAFTERS = modTag("crafters");

      // Mortars
      public static final TagKey<Block> MORTARS = modTag("mortars");

      // Valid capstones for runed/runestone pillars
      public static final TagKey<Block> RUNE_CAPSTONES = modTag("capstones/rune");
      public static final TagKey<Block> RUNE_PILLARS = modTag("pillars/rune");

      public static final TagKey<Block> ACACIA_CAPSTONES = modTag("capstones/log/acacia");
      public static final TagKey<Block> DARK_OAK_CAPSTONES = modTag("capstones/log/dark_oak");
      public static final TagKey<Block> OAK_CAPSTONES = modTag("capstones/log/oak");
      public static final TagKey<Block> BIRCH_CAPSTONES = modTag("capstones/log/birch");
      public static final TagKey<Block> JUNGLE_CAPSTONES = modTag("capstones/log/jungle");
      public static final TagKey<Block> SPRUCE_CAPSTONES = modTag("capstones/log/spruce");
      public static final TagKey<Block> WILDWOOD_CAPSTONES = modTag("capstones/log/wildwood");
      public static final TagKey<Block> CRIMSON_CAPSTONES = modTag("capstones/log/crimson");
      public static final TagKey<Block> WARPED_CAPSTONES = modTag("capstones/log/warped");

      public static final TagKey<Block> ACACIA_PILLARS = modTag("pillars/log/acacia");
      public static final TagKey<Block> DARK_OAK_PILLARS = modTag("pillars/log/dark_oak");
      public static final TagKey<Block> OAK_PILLARS = modTag("pillars/log/oak");
      public static final TagKey<Block> BIRCH_PILLARS = modTag("pillars/log/birch");
      public static final TagKey<Block> JUNGLE_PILLARS = modTag("pillars/log/jungle");
      public static final TagKey<Block> SPRUCE_PILLARS = modTag("pillars/log/spruce");
      public static final TagKey<Block> WILDWOOD_PILLARS = modTag("pillars/log/wildwood");
      public static final TagKey<Block> CRIMSON_PILLARS = modTag("pillars/log/crimson");
      public static final TagKey<Block> WARPED_PILLARS = modTag("pillars/log/warped");

      // Crops that should not be affected by the growth spells
      public static final TagKey<Block> GROWTH_BLACKLIST = modTag("growth/blacklist");
      // Crops that should still receive growth ticks even if they are considered "grown"
      public static final TagKey<Block> GROWTH_FORCE = modTag("growth/force");
      // Crops that should receive a reduced number of growth ticks
      public static final TagKey<Block> GROWTH_REDUCE = modTag("growth/reduce");

      public static final TagKey<Block> NYI = modTag("nyi");

      private static TagKey<Block> modTag(String name) {
        return BlockTags.create(new ResourceLocation(MODID, name));
      }

      private static TagKey<Block> compatTag(String name) {
        return BlockTags.create(new ResourceLocation("forge", name));
      }

      private static TagKey<Block> compatTag(String prefix, String name) {
        return BlockTags.create(new ResourceLocation(prefix, name));
      }
    }

    public static class Items {
      public static final TagKey<Item> SEEDS = modTag("seeds");
      public static final TagKey<Item> CLOUD_BERRY_SEEDS = modTag("seeds/cloud_berry");
      public static final TagKey<Item> DEWGONIA_SEEDS = modTag("seeds/dewgonia");
      public static final TagKey<Item> INFERNO_BULB_SEEDS = modTag("seeds/inferno_bulb");
      public static final TagKey<Item> MOONGLOW_SEEDS = modTag("seeds/moonglow");
      public static final TagKey<Item> PERESKIA_SEEDS = modTag("seeds/pereskia");
      public static final TagKey<Item> SPIRITLEAF_SEEDS = modTag("seeds/spiritleaf");
      public static final TagKey<Item> STALICRIPE_SEEDS = modTag("seeds/stalicripe");
      public static final TagKey<Item> WILDEWHEET_SEEDS = modTag("seeds/wildewheet");
      public static final TagKey<Item> WILDROOT_SEEDS = modTag("seeds/wildroot");

      public static final TagKey<Item> CROPS = modTag("crops");
       // TODO: Unused
      public static final TagKey<Item> ELEMENTAL_CROPS = modTag("crops/elemental");
      public static final TagKey<Item> WATER_CROPS = modTag("crops/elemental/water");
      public static final TagKey<Item> EARTH_CROPS = modTag("crops/elemental/earth");
      public static final TagKey<Item> AIR_CROPS = modTag("crops/elemental/air");
      public static final TagKey<Item> FIRE_CROPS = modTag("crops/elemental/fire");
      public static final TagKey<Item> CLOUD_BERRY_CROP = modTag("crops/cloud_berry");
      public static final TagKey<Item> DEWGONIA_CROP = modTag("crops/dewgonia");
      public static final TagKey<Item> SPIRITLEAF_CROP = modTag("crops/spiritleaf");
      public static final TagKey<Item> STALICRIPE_CROP = modTag("crops/stalicripe");
      public static final TagKey<Item> WILDEWHEET_CROP = modTag("crops/wildewheet");
      public static final TagKey<Item> WILDROOT_CROP = modTag("crops/wildroot");
      public static final TagKey<Item> GROVE_MOSS_CROP = modTag("crops/grove_moss");
      public static final TagKey<Item> INFERNO_BULB_CROP = modTag("crops/inferno_bulb");
      public static final TagKey<Item> MOONGLOW_CROP = modTag("crops/moonglow");
      public static final TagKey<Item> PERESKIA_CROP = modTag("crops/pereskia");

      public static final TagKey<Item> BAFFLECAP_CROP = modTag("crops/bafflecap");

      public static final TagKey<Item> BARKS = modTag("barks");
      public static final TagKey<Item> ACACIA_BARK = modTag("barks/acacia");
      public static final TagKey<Item> BIRCH_BARK = modTag("barks/birch");
      public static final TagKey<Item> DARK_OAK_BARK = modTag("barks/dark_oak");
      public static final TagKey<Item> JUNGLE_BARK = modTag("barks/jungle");
      public static final TagKey<Item> OAK_BARK = modTag("barks/oak");
      public static final TagKey<Item> SPRUCE_BARK = modTag("barks/spruce");
      public static final TagKey<Item> WILDWOOD_BARK = modTag("barks/wildwood");
      public static final TagKey<Item> CRIMSON_BARK = modTag("barks/crimson");
      public static final TagKey<Item> WARPED_BARK = modTag("barks/warped");
      public static final TagKey<Item> MIXED_BARK = modTag("barks/mixed");

      // TODO: Unused
      public static final TagKey<Item> POUCHES = modTag("pouches");

      public static final TagKey<Item> GROVE_CRAFTER_ACTIVATION = modTag("grove_crafter_activation");
      public static final TagKey<Item> MORTAR_ACTIVATION = modTag("mortar_activation");
      public static final TagKey<Item> PYRE_ACTIVATION = modTag("pyre_activation");

      public static final TagKey<Item> CASTING_TOOLS = modTag("casting_tools");

      public static final TagKey<Item> NYI = modTag("nyi");

      // These are all filled in by ModTags
      public static class Blocks {
        public static final TagKey<Item> SOILS = modTag("soils");
        public static final TagKey<Item> WATER_SOIL = modTag("soils/water");
        public static final TagKey<Item> AIR_SOIL = modTag("soils/air");
        public static final TagKey<Item> EARTH_SOIL = modTag("soils/earth");
        public static final TagKey<Item> FIRE_SOIL = modTag("soils/fire");
        public static final TagKey<Item> ELEMENTAL_SOIL = modTag("soils/elemental");

        public static final TagKey<Item> RUNED_OBSIDIAN = modTag("runed_obsidian");
        public static final TagKey<Item> RUNESTONE = modTag("runestone");
        public static final TagKey<Item> WILDWOOD_LOGS = modTag("logs/wildwood");

        public static final TagKey<Item> RUNED_LOGS = modTag("logs/runed");
        public static final TagKey<Item> RUNED_ACACIA_LOG = modTag("logs/runed/acacia");
        public static final TagKey<Item> RUNED_DARK_OAK_LOG = modTag("logs/runed/dark_oak");
        public static final TagKey<Item> RUNED_OAK_LOG = modTag("logs/runed/oak");
        public static final TagKey<Item> RUNED_BIRCH_LOG = modTag("logs/runed/birch");
        public static final TagKey<Item> RUNED_JUNGLE_LOG = modTag("logs/runed/jungle");
        public static final TagKey<Item> RUNED_SPRUCE_LOG = modTag("logs/runed/spruce");
        public static final TagKey<Item> RUNED_WILDWOOD_LOG = modTag("logs/runed/wildwood");
        public static final TagKey<Item> RUNED_CRIMSON_STEM = modTag("logs/runed/crimson");
        public static final TagKey<Item> RUNED_WARPED_STEM = modTag("logs/runed/warped");

        public static final TagKey<Item> GROVE_STONES = modTag("grove_stones");
        public static final TagKey<Item> GROVE_STONE_PRIMAL = modTag("grove_stones/primal");

        public static final TagKey<Item> PEDESTALS = modTag("pedestals");
        public static final TagKey<Item> RITUAL_PEDESTALS = modTag("pedestals/ritual");
        public static final TagKey<Item> GROVE_PEDESTALS = modTag("pedestals/grove");
        public static final TagKey<Item> PYRES = modTag("pyres");
        public static final TagKey<Item> CRAFTERS = modTag("crafters");
        public static final TagKey<Item> MORTARS = modTag("mortars");
      }

      protected static TagKey<Item> modTag(String name) {
        return ItemTags.create(new ResourceLocation(MODID, name));
      }

      protected static TagKey<Item> compatTag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
      }

      public static class Herbs extends Tags {
        public static final TagKey<Item> GROVE_MOSS = modTag("herbs/grove_moss");
        public static final TagKey<Item> INFERNO_BULB = modTag("herbs/inferno_bulb");
        public static final TagKey<Item> MOONGLOW = modTag("herbs/moonglow");
        public static final TagKey<Item> PERESKIA = modTag("herbs/pereskia");
        public static final TagKey<Item> SPIRITLEAF = modTag("herbs/spiritleaf");
        public static final TagKey<Item> STALICRIPE = modTag("herbs/stalicripe");
        public static final TagKey<Item> WILDEWHEET = modTag("herbs/wildewheet");
        public static final TagKey<Item> WILDROOT = modTag("herbs/wildroot");
        public static final TagKey<Item> CLOUD_BERRY = modTag("herbs/cloud_berry");
        public static final TagKey<Item> DEWGONIA = modTag("herbs/dewgonia");

        public static final TagKey<Item> BAFFLECAP = modTag("herbs/bafflecap");
        public static final TagKey<Item> HERBS = modTag("herbs");
      }
    }

    public static class Potions extends Tags {
      public static final TagKey<Potion> RANDOM_BLACKLIST = compatTag("random_potion_blacklist");

      static TagKey<Potion> modTag(String name) {
        return TagKey.create(Registry.POTION_REGISTRY, new ResourceLocation(MODID, name));
      }

      static TagKey<Potion> compatTag(String name) {
        return TagKey.create(Registry.POTION_REGISTRY, new ResourceLocation("forge", name));
      }
    }

    public static class Entities extends Tags {
      public static final TagKey<EntityType<?>> ANIMAL_HARVEST = modTag("animal_harvest_entities");
      public static final TagKey<EntityType<?>> PACIFIST = modTag("pacifist");

      public static final TagKey<EntityType<?>> BOATS = modTag("boats");

      static TagKey<EntityType<?>> modTag(String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MODID, name));
      }

      static TagKey<EntityType<?>> compatTag(String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("forge", name));
      }
    }

    public static class Rituals extends Tags {
      public static final TagKey<Ritual> NYI = modTag("nyi");

      static TagKey<Ritual> modTag(String name) {
        return TagKey.create(RootsAPI.RITUAL_REGISTRY, new ResourceLocation(MODID, name));
      }

      static TagKey<Ritual> compatTag(String name) {
        return TagKey.create(RootsAPI.RITUAL_REGISTRY, new ResourceLocation("forge", name));
      }
    }

    public static class Spells extends Tags {
      public static final TagKey<Spell> NYI = modTag("nyi");

      static TagKey<Spell> modTag(String name) {
        return TagKey.create(RootsAPI.SPELL_REGISTRY, new ResourceLocation(MODID, name));
      }

      static TagKey<Spell> compatTag(String name) {
        return TagKey.create(RootsAPI.SPELL_REGISTRY, new ResourceLocation("forge", name));
      }
    }

    public static class Modifiers extends Tags {
      public static final TagKey<Modifier> NYI = modTag("nyi");

      static TagKey<Modifier> modTag(String name) {
        return TagKey.create(RootsAPI.MODIFIER_REGISTRY, new ResourceLocation(MODID, name));
      }

      static TagKey<Modifier> compatTag(String name) {
        return TagKey.create(RootsAPI.MODIFIER_REGISTRY, new ResourceLocation("forge", name));
      }
    }

    public static class Herbs extends Tags {
      public static final TagKey<Herb> ELEMENTAL = modTag("elemental");
      public static final TagKey<Herb> FIRE = modTag("elemental/fire");
      public static final TagKey<Herb> WATER = modTag("elemental/water");
      public static final TagKey<Herb> EARTH = modTag("elemental/earth");
      public static final TagKey<Herb> AIR = modTag("elemental/air");

      static TagKey<Herb> modTag(String name) {
        return TagKey.create(RootsAPI.HERB_REGISTRY, new ResourceLocation(MODID, name));
      }

      static TagKey<Herb> compatTag(String name) {
        return TagKey.create(RootsAPI.HERB_REGISTRY, new ResourceLocation("forge", name));
      }
    }
  }
}
