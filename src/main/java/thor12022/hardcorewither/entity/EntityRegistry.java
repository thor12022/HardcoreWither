package thor12022.hardcorewither.entity;

import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.ModInformation;

public class EntityRegistry
{
   public static void register()
   {
      cpw.mods.fml.common.registry.EntityRegistry.registerModEntity(
            EntityBlazeMinion.class,
            EntityBlazeMinion.UNLOCALIZED_NAME,
            0, 
            HardcoreWither.instance, 
            64, 
            10, 
            true);
   }
}
