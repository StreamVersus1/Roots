package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.Rituals;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.ritual.CraftingRitual;
import net.minecraft.resources.ResourceKey;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModRituals {
  public static final RegistryEntry<Ritual> TRANSMUTATION = ritual(Rituals.TRANSMUTATION, () -> new Ritual() {
    @Override
    public void ritualTick(PyreBlockEntity blockEntity) {

    }

    @Override
    public void animateTick(PyreBlockEntity blockEntity) {

    }

    @Override
    public void initialize() {

    }
  });
  public static final RegistryEntry<RitualProperty<Integer>> TRANSMUTATION_COUNT = REGISTRATE.simple("transmutation/count", RitualProperty.class, () -> new RitualProperty<>(Rituals.TRANSMUTATION, 10, Property.INTEGER_SERIALIZER, "Number of times the ritual will operate per interval"));

  public static final RegistryEntry<CraftingRitual> CRAFTING = ritual(Rituals.CRAFTING, () -> new CraftingRitual());
  public static final RegistryEntry<RitualProperty<Integer>> CRAFTING_DURATION = REGISTRATE.simple("crafting/duration", RitualProperty.class, () -> new RitualProperty<>(Rituals.CRAFTING, 160, Property.INTEGER_SERIALIZER, "The duration of the ritual"));
  public static final RegistryEntry<RitualProperty<Integer>> CRAFTING_INTERVAL = REGISTRATE.simple("crafting/interval", RitualProperty.class, () -> new RitualProperty<>(Rituals.CRAFTING, 120, Property.INTEGER_SERIALIZER, "How long after the ritual starts before execution begins"));

  private static <T extends Ritual> RegistryEntry<T> ritual (ResourceKey<Ritual> key, NonNullSupplier<T> builder) {
    return REGISTRATE.simple(key.location().getPath(), Ritual.class, builder);
  }

  public static void load() {
  }
}