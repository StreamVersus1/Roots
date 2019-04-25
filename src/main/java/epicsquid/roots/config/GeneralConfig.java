package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;

@Config.LangKey("config.roots.category.general")
@Config(modid= Roots.MODID, name = "roots/general")
@SuppressWarnings("unused")
public class GeneralConfig {

  @Config.Comment("Disable the item override that allows vanilla bows to pull arrows from the Living Quiver while in Bauble slots. Disable this if you are having issues with bows.")
  @Config.Name("Disable Bauble Bow")
  public static boolean disableBaubleBow = false;

}


