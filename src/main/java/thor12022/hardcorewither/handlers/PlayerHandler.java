package thor12022.hardcorewither.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PlayerHandler
{
   public PlayerHandler()
   {
      MinecraftForge.EVENT_BUS.register(this);
   }
   
   //! @todo this event seems like it's a lot of unnecessary overhead
   @SubscribeEvent
   public void onSpawnMob(EntityJoinWorldEvent event)
   {
      // Yes I know, this is the Player Handler, work with me here
      if (event.entity != null && event.entity.getClass() == EntityWither.class)
      {
         EntityWither theWither = (EntityWither)event.entity;
         List nearbyPlayers = theWither.worldObj.getEntitiesWithinAABB(EntityPlayer.class, theWither.boundingBox.expand(20.0D, 8.0D, 20.0D));
         for (int index = 0; index < nearbyPlayers.size(); ++index)
         {
            EntityPlayer player = (EntityPlayer)nearbyPlayers.get(index);
            addWitherExperience(player, 1.0);
         }
      }
   }
   
   
   private void addWitherExperience( EntityPlayer player, double wxp )
   {
      //! @todo save it in the Data Store
   }
   
   private double getWitherExperience( EntityPlayer player )
   {
      //! @todo get it from the Data Store
      return 0.0;
   }
}
