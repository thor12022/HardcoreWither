package thor12022.hardcorewither.powerUps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.interfaces.INBTStorageClass;

public class PowerUpManager implements INBTStorageClass
{
   private Map<Class, IPowerUp> powerUpPrototypes;
   private Map<UUID, Map<Class, IPowerUp>> usedPowerUps;
   private int largestPowerUp;
   private Random random;
   
   public PowerUpManager()
   {
      powerUpPrototypes = new HashMap<Class, IPowerUp>();
      usedPowerUps = new HashMap<UUID, Map<Class, IPowerUp>>();
      largestPowerUp = 0;
      random = new Random();
      MinecraftForge.EVENT_BUS.register(this);
   }
   
   public void init()
   {
      registerPowerUp(new PowerUpBlazeMinionSpawner());
      registerPowerUp(new PowerUpDeathKnell());
      registerPowerUp(new PowerUpGhastMinionSpawner());
      registerPowerUp(new PowerUpHealthBoost());
      registerPowerUp(new PowerUpSkeletonMinionSpawner());
   }
   
   /**
    * Registers a Power Up with the Power Up Manager
    * @param powerUp a default-constructed Power Up to use as a prototype
    *    for constructing further Power Ups of its type
    * @pre powerUp is not of a class that has already been registered
    */
   public void registerPowerUp( IPowerUp powerUp)
   {
      if(!powerUpPrototypes.containsKey(powerUp.getClass()))
      {
         powerUpPrototypes.put(powerUp.getClass(), powerUp);
         HardcoreWither.logger.info("Registering Prototype for " + powerUp.getClass().toString());
         
      }
      else
      {
         HardcoreWither.logger.debug("Duplicate Prototype registered for " + powerUp.getClass().toString());
      }
   }
   
   private void addRandomPowerUp(EntityWither ownerWither)
   {
      //! @todo not very efficient here, are we?
      IPowerUp powerUpPrototpe = (IPowerUp) powerUpPrototypes.values().toArray()[random.nextInt(powerUpPrototypes.size())];
      // If we haven't seen this Wither yet
      if(!usedPowerUps.containsKey(ownerWither.getUniqueID()))
      {
         usedPowerUps.put(ownerWither.getUniqueID(), new HashMap<Class, IPowerUp>());
      }
      Map<Class, IPowerUp> powerUpsUsed = usedPowerUps.get(ownerWither.getUniqueID());
      // If we have used this Power up for this Wither
      if(powerUpsUsed.keySet().contains(powerUpPrototpe.getClass()))
      {
         IPowerUp powerUp = powerUpsUsed.get(powerUpPrototpe.getClass());
         powerUp.increasePower();
         HardcoreWither.logger.debug("Increasing power of " + powerUpPrototpe.getClass());
      }
      // If this is a new powerup for this Wither
      else
      {
         powerUpsUsed.put(powerUpPrototpe.getClass(), powerUpPrototpe.createPowerUp(ownerWither));
         HardcoreWither.logger.debug("Adding " + powerUpPrototpe.getClass());
      }
   }
   /**
    * Apply an amount of Power Ups to a certain Wither
    * @param wither apply power ups to this
    * @param sizeOfPowerUp this many levels of Power Ups
    * @note wither should not have had Power Ups applied to it already
    */
   public void powerUpWither(EntityWither wither, int sizeOfPowerUp)
   {
      if(!usedPowerUps.containsKey(wither.getUniqueID()))
      {
         int powerUpSize = sizeOfPowerUp != 0 ? sizeOfPowerUp : largestPowerUp + 1;
         for(int count = 0; count < sizeOfPowerUp; ++count)
         {
            addRandomPowerUp(wither);
         }
         if(powerUpSize > largestPowerUp)
         {
            largestPowerUp = powerUpSize;
         }
      }
      else
      {
         HardcoreWither.logger.debug("Attempting to re-powerup Wither");
      }
   }
   
   public void update(EntityWither wither)
   {
      if(!usedPowerUps.containsKey(wither.getUniqueID()))
      {

      }
      else
      {
         // Iterate through the powerups for this Wither
         Iterator iter = usedPowerUps.get(wither.getUniqueID()).values().iterator();
         while(iter.hasNext())
         {
            IPowerUp powerUp = (IPowerUp)iter.next();
            {
               powerUp.updateWither();
            }
         }
      }
   }  
   
   public void witherDied(EntityWither wither)
   {
      if(!usedPowerUps.containsKey(wither.getUniqueID()))
      {

      }
      else
      {
         // Iterate through the powerups for this Wither
         Iterator iter = usedPowerUps.get(wither.getUniqueID()).values().iterator();
         while(iter.hasNext())
         {
            IPowerUp powerUp = (IPowerUp)iter.next();
            {
               powerUp.witherDied();
            }
         }
         usedPowerUps.remove(wither.getUniqueID());
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound nbt)
   {
      //! @todo I feel like NBTTagList might be of use here
      
      Iterator witherIter = usedPowerUps.keySet().iterator();
      while (witherIter.hasNext()) 
      {
         UUID witherUuid = (UUID) witherIter.next();
         Iterator powerUpIter = usedPowerUps.get(witherUuid).keySet().iterator();
         NBTTagCompound witherNbt = new NBTTagCompound();
         while (powerUpIter.hasNext()) 
         {
            Class powerUpClass = (Class) powerUpIter.next();
            NBTTagCompound powerUpNbt = new NBTTagCompound();
            usedPowerUps.get(witherUuid).get(powerUpClass).writeToNBT(powerUpNbt);
            witherNbt.setTag(powerUpClass.toString(), powerUpNbt);
         }
         nbt.setTag(witherUuid.toString(), witherNbt);
      }
   }

   @Override
   public void readFromNBT(NBTTagCompound nbt)
   {
      //! @todo I feel like NBTTagList might be of use here
      
      Set witherTags = nbt.func_150296_c();
      Iterator witherIter = witherTags.iterator();
      while (witherIter.hasNext()) 
      {
         String witherUuidString = (String)witherIter.next();
         UUID witherUuid = UUID.fromString(witherUuidString);
         NBTTagCompound witherNbt = (NBTTagCompound) nbt.getTag(witherUuidString);
         
         Set powerUpTags = nbt.func_150296_c();
         Iterator powerUpIter = powerUpTags.iterator();
         while (powerUpIter.hasNext()) 
         {
            String powerUpClassString = (String)powerUpIter.next();
            try 
            {
               Class powerUpClass = Class.forName(powerUpClassString);
               if(powerUpPrototypes.containsKey(powerUpClass))
               {
                  NBTTagCompound powerUpNbt = (NBTTagCompound) witherNbt.getTag(powerUpClassString);
                  //! @todo the problem here is that there is no way to lookup an enitiy by it's uuid
                  //!   this means we need to delay the reading of the powerup NBT until a Wither is loaded
                  //!   with the correct UUID. This will likely take some adjustments to our current structure
               }
            }
            catch (Exception ex)
            {
               HardcoreWither.logger.warn("Attempting to powerup from save with unknown powerup: " + powerUpClassString);
            }
         
         }
      }
   }  
}
