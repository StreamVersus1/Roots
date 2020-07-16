package epicsquid.roots.modifiers;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// TODO:
// - Don't worry about clearing entity data
// - Finalise and prevent modifiers from functioning if configured so in CraftTweaker
// - CraftTweaker ability to adjust modifier costs and amplifications
// - CraftTweaker remove the ability to change the costs of spells
// - setFireModifier, setPeacefulModifier, etc
// - Standard helper functions fire(), slow(), paralyse()
// - Standard function to storeEntityData modifier per-modifier/per-spell

public class ModifierRegistry {
  private static Map<ResourceLocation, Modifier> map = new HashMap<>();
  private static boolean initialized = false;

  @Nullable
  public static Modifier get(ResourceLocation name) {
    return map.get(name);
  }

  public static Modifier register(Modifier modifier) {
    ResourceLocation registryName = modifier.getRegistryName();
    if (registryName == null) {
      throw new IllegalStateException("Modifier being registered has a null registry name.");
    }
    if (map.containsKey(registryName)) {
      throw new IllegalStateException("Modifier with registry name " + registryName + " already exists!");
    }
    map.put(registryName, modifier);
    return modifier;
  }

  public static Collection<Modifier> getModifiers() {
    return map.values();
  }

  public static void initialize() {
    initialized = true;
  }

  public static boolean initialized() {
    return initialized;
  }
}
