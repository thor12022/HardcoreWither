package thor12022.hardcorewither.handlers.wither;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import thor12022.hardcorewither.ConfigHandler;
import thor12022.hardcorewither.HardcoreWither;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class WitherHandler
{
   private Map<UUID, WitherMinionSpawner> witherSpawnerMap ;

   public WitherHandler()
   {
      witherSpawnerMap = new HashMap<UUID, WitherMinionSpawner>();
      MinecraftForge.EVENT_BUS.register(this);
   }
   
   private void AttachSpawner(EntityWither wither)
   {
      HardcoreWither.logger.debug("Attaching Minion Spawner to Wither");
      WitherMinionSpawner minionSpawner = new WitherMinionSpawner(wither);
      witherSpawnerMap.put(wither.getUniqueID(), minionSpawner);
   }
   
   @SubscribeEvent
   public void onLivingUpdate(LivingUpdateEvent event)
   {
      if (event.entityLiving != null && event.entityLiving.getClass() == EntityWither.class)
      {
         try
         {
            witherSpawnerMap.get(event.entityLiving.getUniqueID()).updateSpawner();
         }
         catch( NullPointerException e)
         {
            AttachSpawner((EntityWither)event.entityLiving);
         }
      }
   }

   @SubscribeEvent
   public void onEntitySpawn(LivingSpawnEvent event)
   {
      if (event.entityLiving != null && event.entityLiving.getClass() == EntityWither.class)
      {
         AttachSpawner((EntityWither)event.entityLiving);
      }
   }
}
