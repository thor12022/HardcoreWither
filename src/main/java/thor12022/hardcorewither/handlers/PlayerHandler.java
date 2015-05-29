package thor12022.hardcorewither.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.interfaces.INBTStorageClass;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PlayerHandler implements INBTStorageClass
{
   private Map<UUID, Double> playerXp;
   
   public PlayerHandler()
   {
      MinecraftForge.EVENT_BUS.register(this);
      playerXp = new HashMap<UUID, Double>();
   }
   
   @SubscribeEvent
   public void onSpawnMob(EntityJoinWorldEvent event)
   {
      // Yes I know, this is the Player Handler, work with me here
      if (event.entity != null && event.entity.getClass() == EntityWither.class)
      {
         EntityWither theWither = (EntityWither)event.entity;
         // only when the Wither is charging up (to prevent some triggering with chunk reload)
         if( theWither.func_82212_n() > 0 )
         {
            List nearbyPlayers = theWither.worldObj.getEntitiesWithinAABB(EntityPlayer.class, theWither.boundingBox.expand(64.0D, 64.0D, 64.0D));
            for (int index = 0; index < nearbyPlayers.size(); ++index)
            {
               EntityPlayer player = (EntityPlayer)nearbyPlayers.get(index);
               addWitherExperience(player, 1.0);
            }
         }
      }
   }

   private void addWitherExperience( EntityPlayer player, double wxp )
   {
      double prevXp = 0.0;
      if( playerXp.containsKey(player.getUniqueID()) )
      {
         prevXp = playerXp.get(player.getUniqueID());
      }
      playerXp.put(player.getUniqueID(), prevXp + wxp);
   }
   
   public void writeToNBT(NBTTagCompound nbt)
   {
      Iterator playerIter = playerXp.keySet().iterator();
      while( playerIter.hasNext())
      {
         UUID uuid = (UUID) playerIter.next();
         nbt.setDouble(uuid.toString(), playerXp.get(uuid));
      }
   }
   
   public void readFromNBT(NBTTagCompound nbt)
   {
      Set tags = nbt.func_150296_c();
      Iterator iter = tags.iterator();
      while (iter.hasNext()) 
      {
         String tag = (String)iter.next();
         playerXp.put( UUID.fromString(tag), nbt.getDouble(tag));
      }
   }
}
