package thor12022.hardcorewither.items;

/*
 * Class to register your blocks in.
 * The order that you list them here is the order they are registered.
 * Keep that in mind if you like nicely organized creative tabs.
 */

import cpw.mods.fml.common.registry.GameRegistry;
import thor12022.hardcorewither.config.ConfigManager;
import thor12022.hardcorewither.config.IConfigClass;
import thor12022.hardcorewither.ModInformation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

public class ItemRegistry implements IConfigClass
{
   // items
   // public static Item exampleItem;
   public static Item starryApple;

   private static boolean enableStarryApple = true;

   public static void registerItems()
   {
      // the only reason to have a non-static members/methods is for the Config, 
      //    so we'll just give 'em a kinda-proxy-like instance
      ConfigManager.getInstance().addConfigClass(new ItemRegistry());

      starryApple = new ItemStarryApple();
      if(enableStarryApple)
      {
         GameRegistry.registerItem(starryApple, "StarryApple");
      }
   }


   @Override
   public String getSectionName()
   {
      return "ItemRegistry";
   }

   @Override
   public void syncConfig(Configuration config)
   {
      enableStarryApple = config.getBoolean("Enable Starry Apple",getSectionName(), enableStarryApple, "");
   }
}
