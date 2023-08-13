package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class TimeStopSpell extends Spell {
  public TimeStopSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0x404040, 0xc020ff);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.TIME_STOP_COOLDOWN.get();
  }

  @Override
  public void initialize() {

  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {

  }
}
