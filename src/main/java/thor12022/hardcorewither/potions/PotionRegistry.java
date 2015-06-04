package thor12022.hardcorewither.potions;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import thor12022.hardcorewither.config.ConfigManager;
import thor12022.hardcorewither.HardcoreWither;
import net.minecraft.potion.Potion;

public class PotionRegistry
{
   public static Potion potionAntiWither;
   
   public static void registerPotions()
   {
      if(ConfigManager.antiWitherPotionId == -1)
      {
         ConfigManager.antiWitherPotionId = NextPotionId();
      }
      
      HardcoreWither.logger.debug("Assigning Potion ID " + ConfigManager.antiWitherPotionId + " to Anti-Wither");
      potionAntiWither = new PotionAntiWither(ConfigManager.antiWitherPotionId).setPotionName("potion.antiWither");
   }

   private static int NextPotionId()
   {
      for( int potionId = 34; potionId < Potion.potionTypes.length; potionId++ )
      {
         if( Potion.potionTypes[potionId] == null )
         {
            return potionId;
         }
      }
      return -1;
   }
}
