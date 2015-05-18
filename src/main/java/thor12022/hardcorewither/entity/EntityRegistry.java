package thor12022.hardcorewither.entity;

import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.ModInformation;

public class EntityRegistry
{
   public static void register()
   {
int id = 0;
      cpw.mods.fml.common.registry.EntityRegistry.registerModEntity(
            EntityBlazeMinion.class,
            EntityBlazeMinion.UNLOCALIZED_NAME,
            id++, 
            HardcoreWither.instance, 
            64, 
            10, 
            true);
      cpw.mods.fml.common.registry.EntityRegistry.registerModEntity(
            EntityGhastMinion.class,
            EntityGhastMinion.UNLOCALIZED_NAME,
            id++, 
            HardcoreWither.instance, 
            64, 
            10, 
            true);
      cpw.mods.fml.common.registry.EntityRegistry.registerModEntity(
            EntitySkeletonMinion.class,
            EntitySkeletonMinion.UNLOCALIZED_NAME,
            id++, 
            HardcoreWither.instance, 
            64, 
            10, 
            true);
   }
}
