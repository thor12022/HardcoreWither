package thor12022.hardcorewither.config;

/*
 * Creation and usage of the config file.
 */

import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigManager
{

   public static Configuration config;
   
   // Sections to add to the config
   public static final String generalSection = "General";
   public static final String tinkersConstructSection = "Tinkers' Construct";

   // Options in the config
   public static boolean doDebug = false;
   public static boolean enableStarryApple = true;
   public static int antiWitherPotionId = 30;
   public static boolean enableGreenHeartCanister = true;
   public static boolean enableGreenHeartWitherDrop = true;
   public static int greenHeartDropRarity = 2;
   
   public static void init(File file)
   {
      config = new Configuration(file);
      syncConfig();
   }

   public static void syncConfig()
   {
      doDebug = config.getBoolean("Debug Information",generalSection, doDebug, "");
      enableStarryApple = config.getBoolean("Enable Starry Apple",generalSection, enableStarryApple, "");
      antiWitherPotionId = config.getInt("Anti-Wither Potion ID", generalSection, antiWitherPotionId, 24, Potion.potionTypes.length-1, "Set to -1 to attempt an auto-assignment (experimental)");
      
      enableGreenHeartCanister = config.getBoolean("Enable Green Heart Canister Crafting",tinkersConstructSection, enableGreenHeartCanister, "Requires Tinkers' Construct");
      enableGreenHeartWitherDrop = config.getBoolean("Enable Withers Dropping Green Hearts",tinkersConstructSection, enableGreenHeartWitherDrop, "Requires Tinkers' Construct");
      greenHeartDropRarity = config.getInt("Anti-Wither Potion ID", generalSection, greenHeartDropRarity, 0, Integer.MAX_VALUE, "How rare the Green Heart drop is, 0 is guarnenteed 1 per level of fortune");
      
      
      
      config.save();
   }
}
