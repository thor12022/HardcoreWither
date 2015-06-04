package thor12022.hardcorewither;

/*
 * Class for most of your events to be registered in.
 * Remember that there are two different registries for Events. This one will not work for everything.
 */

import java.util.List;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import thor12022.hardcorewither.ConfigManager;
import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.ModInformation;
import thor12022.hardcorewither.handlers.PlayerHandler;
import thor12022.hardcorewither.powerUps.PowerUpManager;
import thor12022.hardcorewither.util.TextHelper;

public class EventHandler
{
   private PlayerHandler playerHandler;
   private PowerUpManager powerUpManager;
   
   public EventHandler(PlayerHandler playerHandler, PowerUpManager powerUpManager)
   {
      this.playerHandler = playerHandler;
      this.powerUpManager = powerUpManager;
      MinecraftForge.EVENT_BUS.register(this);
   }

   @SubscribeEvent
   public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
   {
      if(eventArgs.modID.equals(ModInformation.ID))
      {
         ConfigManager.syncConfig();
         HardcoreWither.logger.info(TextHelper.localize("info." + ModInformation.ID + ".console.config.refresh"));
      }
   }
   
   @SubscribeEvent
   public void onSpawnMob(EntityJoinWorldEvent event)
   {
      if (event.entity != null && event.entity.getClass() == EntityWither.class)
      {
         EntityWither theWither = (EntityWither)event.entity;
         // only when the Wither is charging up (to prevent some triggering with chunk reload)
         // @todo get a better way to see it we've had it before
         if( theWither.func_82212_n() > 0 )
         {
            List nearbyPlayers = theWither.worldObj.getEntitiesWithinAABB(EntityPlayer.class, theWither.boundingBox.expand(64.0D, 64.0D, 64.0D));
            double powerUpSize = 0.0;
            for (int index = 0; index < nearbyPlayers.size(); ++index)
            {
               EntityPlayer player = (EntityPlayer)nearbyPlayers.get(index);
               powerUpSize += playerHandler.wasAtWitherSpawn(player);
            }
            powerUpManager.powerUpWither( theWither, (int)Math.round(powerUpSize));
         }
      }
   }
   
   @SubscribeEvent
   public void onLivingUpdate(LivingUpdateEvent event)
   {
      if (event.entityLiving != null && event.entityLiving.getClass() == EntityWither.class)
      {
         powerUpManager.update((EntityWither) event.entityLiving);
      }
   }
   
   @SubscribeEvent
   public void onEntityDieing(LivingDeathEvent event)
   {
      if (event.entityLiving != null && event.entityLiving.getClass() == EntityWither.class)
      {
         powerUpManager.witherDied((EntityWither) event.entityLiving);
      }
   }
}
