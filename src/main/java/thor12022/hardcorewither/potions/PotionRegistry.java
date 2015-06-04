package thor12022.hardcorewither.potions;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import thor12022.hardcorewither.config.ConfigManager;
import thor12022.hardcorewither.config.IConfigClass;
import thor12022.hardcorewither.HardcoreWither;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;

public class PotionRegistry implements IConfigClass
{
   
   public static Potion potionAntiWither;

   private static int antiWitherPotionId = 30;

   public static void registerPotions()
   {
      // the only reason to have a non-static members/methods is for the Config, 
      //    so we'll just give 'em a kinda-proxy-like instance
      ConfigManager.getInstance().addConfigClass(new PotionRegistry());
      
      if(antiWitherPotionId == -1)
      {
         antiWitherPotionId = NextPotionId();
      }
      
      HardcoreWither.logger.debug("Assigning Potion ID " + antiWitherPotionId + " to Anti-Wither");
      potionAntiWither = new PotionAntiWither(antiWitherPotionId).setPotionName("potion.antiWither");
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

   @Override
   public String getSectionName()
   {
      return "Potions";
   }

   @Override
   public void syncConfig(Configuration config)
   {
      antiWitherPotionId = config.getInt("Anti-Wither Potion ID", getSectionName(), antiWitherPotionId, 24, Potion.potionTypes.length-1, "Set to -1 to attempt an auto-assignment (experimental)");
   }
}
