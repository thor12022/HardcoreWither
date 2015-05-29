package thor12022.hardcorewither.handlers.wither;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
import thor12022.hardcorewither.powerUps.IPowerUp;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class WitherHandler
{
   private static Map<UUID, List<IPowerUp>> usedPowerUps = new HashMap<UUID, List<IPowerUp>>();
   private static List<IPowerUp> powerUps = new ArrayList<IPowerUp>();
   
   public void registerPowerUp( IPowerUp powerUp)
   {
      powerUps.add(powerUp);
   }

   private IPowerUp getPowerUp( EntityWither ownerWither)
   {
      IPowerUp powerUp = powerUps.get((int)Math.round(Math.random()* (powerUps.size() - 1))).createPowerUp(ownerWither);
      if(!usedPowerUps.containsKey(ownerWither.getUniqueID()))
      {
         usedPowerUps.put( ownerWither.getUniqueID(), new LinkedList<IPowerUp>());
      }
      usedPowerUps.get(ownerWither.getUniqueID()).add(powerUp);
      return powerUp;
   }

   public WitherHandler()
   {
      MinecraftForge.EVENT_BUS.register(this);
   }
   

   @SubscribeEvent
   public void onLivingUpdate(LivingUpdateEvent event)
   {
      if (event.entityLiving != null && event.entityLiving.getClass() == EntityWither.class)
      {
         if(usedPowerUps.containsKey(event.entityLiving.getUniqueID()))
         {
            Iterator iter = usedPowerUps.get(event.entityLiving.getUniqueID()).iterator();
            if(iter.hasNext())
            {
               for(IPowerUp powerUp = (IPowerUp)iter.next(); iter.hasNext(); powerUp = (IPowerUp)iter.next() )
               {
                  powerUp.updateWither();
               }
            }
         }
         else
         {
            getPowerUp( (EntityWither)event.entityLiving);
         }
      }
   }

   @SubscribeEvent
   public void onEntitySpawn(LivingSpawnEvent event)
   {
      if (event.entityLiving != null && event.entityLiving.getClass() == EntityWither.class)
      {
         getPowerUp( (EntityWither)event.entityLiving);
      }
   }
}
