package mysticmods.roots.api.property;

import mysticmods.roots.api.registry.IDescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class SpellProperty<V> extends Property<V> implements IDescribedRegistryEntry {
  private String descriptionId;
  protected Supplier<Spell> spell;

  public SpellProperty(Supplier<Spell> spell, V defaultValue, Serializer<V> serializer, String comment) {
    super(defaultValue, serializer, comment);
    this.spell = spell;
  }

  public Spell getSpell() {
    return spell.get();
  }


/*
  @Override
  public Class<SpellProperty<?>> getRegistryType() {
    return c(SpellProperty.class);
  }
*/

  @Override
  public ResourceLocation getKey() {
    return Registries.SPELL_PROPERTY_REGISTRY.get().getKey(this);
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("spell_property", getKey());
    }

    return this.descriptionId;
  }
}
