package thor12022.hardcorewither.powerUps;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.MinecraftForge;
import thor12022.hardcorewither.command.AbstractSubCommand;
import thor12022.hardcorewither.command.CommandManager;
import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.interfaces.INBTStorageClass;

public class PowerUpManager implements INBTStorageClass
{
   private Map<String, IPowerUp>              powerUpPrototypes;
   private Map<UUID, Map<String, IPowerUp>>  usedPowerUps;
   private Map<UUID, NBTTagCompound>         savedWitherData;
   private int largestPowerUp;
   private Random random;
   AbstractSubCommand spawnCommand = new AbstractSubCommand()
   {
      @Override
      public final String getCommandUsage(ICommandSender sender)
      {
         String text = super.getCommandUsage(sender);
         Iterator iter = powerUpPrototypes.keySet().iterator();
         while(iter.hasNext())
         {
            text += iter.next() + "\n";
         }
         return text;
      }

      @Override
      public final String getCommandName()
      {
         return "spawn";
      }

      @Override
      public void processCommand(ICommandSender sender, String[] args, int startingIndex)
      {
         if( args.length < startingIndex + 2 )
         {
            throw new WrongUsageException(getCommandUsage(sender));
         }
         else
         {
            NBTTagCompound nbt = new NBTTagCompound();
            EntityWither spawnedWither = new EntityWither(sender.getEntityWorld());
            try
            {
               for(int argIndex = startingIndex; argIndex < args.length - 1; argIndex += 2)
               {
                  String powerUpName = args[argIndex];
                  int powerUpStrength = parseIntWithMin(sender, args[argIndex + 1], 1);
                  IPowerUp powerUpPrototype = powerUpPrototypes.get(powerUpName);
                  IPowerUp powerUp = powerUpPrototype.createPowerUp(spawnedWither);
                  // @todo should be able to set or create with correct strength
                  for(; powerUpStrength > 1; --powerUpStrength)
                  {
                     powerUp.increasePower();
                  }
                  NBTTagCompound powerUpNbt = new NBTTagCompound();
                  powerUp.writeToNBT(powerUpNbt);
                  nbt.setTag(powerUpName, powerUpNbt);
               }
            }
            catch(Exception excp)
            {
               // ok, so this is kinda the Lazy Man's way of making sure nothing really goes wrong
               HardcoreWither.logger.debug("PowerUp Command Formatting Error (probably) not accounted for " + excp);
               throw new WrongUsageException(getCommandUsage(sender));
            }
            spawnedWither.func_82206_m();
            ChunkCoordinates chunkCoords = sender.getPlayerCoordinates();
            spawnedWither.setPosition(chunkCoords.posX, chunkCoords.posY, chunkCoords.posZ);
            savedWitherData.put(spawnedWither.getUniqueID(), nbt);
            sender.getEntityWorld().spawnEntityInWorld(spawnedWither);
         }
      }
   };
   
   /**
    * Default constructor
    */
   public PowerUpManager()
   {
      powerUpPrototypes = new HashMap<String, IPowerUp>();
      usedPowerUps = new HashMap<UUID, Map<String, IPowerUp>>();
      savedWitherData = new HashMap<UUID, NBTTagCompound>();
      largestPowerUp = 0;
      random = new Random();
      CommandManager.getInstance().registerSubCommand(spawnCommand);
      MinecraftForge.EVENT_BUS.register(this);
   }
   
   public void init()
   {
      registerPowerUp(new PowerUpBlazeMinionSpawner());
      registerPowerUp(new PowerUpDeathKnell());
      registerPowerUp(new PowerUpGhastMinionSpawner());
      registerPowerUp(new PowerUpHealthBoost());
      registerPowerUp(new PowerUpSkeletonMinionSpawner());
      registerPowerUp(new PowerUpLightning());
   }
   
   /**
    * Registers a Power Up with the Power Up Manager
    * @param powerUp a default-constructed Power Up to use as a prototype
    *    for constructing further Power Ups of its type
    * @pre powerUp is not of a class that has already been registered
    */
   public void registerPowerUp( IPowerUp powerUp)
   {
      if(!powerUpPrototypes.containsKey(powerUp.getName()))
      {
         powerUpPrototypes.put(powerUp.getName(), powerUp);
         HardcoreWither.logger.info("Registering Prototype for " + powerUp.getName().toString());
         
      }
      else
      {
         HardcoreWither.logger.debug("Duplicate Prototype registered for " + powerUp.getName().toString());
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
      if(savedWitherData.containsKey(wither.getUniqueID()))
      {
         loadWitherFromNBT(wither, savedWitherData.get(wither.getUniqueID()));
         // now that it's loaded, we don't need to worry about its NBT data anymore
         savedWitherData.remove(wither.getUniqueID());
      }
      else if(!usedPowerUps.containsKey(wither.getUniqueID()))
      {
         usedPowerUps.put(wither.getUniqueID(), new HashMap<String, IPowerUp>());
         int powerUpSize = sizeOfPowerUp != 0 ? sizeOfPowerUp : largestPowerUp + 1;
         Collection<IPowerUp> validPowerUpPrototypes = powerUpPrototypes.values();
         int usedStrength = 0;
         while(usedStrength < sizeOfPowerUp && validPowerUpPrototypes.size() > 0)
         {
            IPowerUp powerUpPrototpe = (IPowerUp) validPowerUpPrototypes.toArray()[random.nextInt(validPowerUpPrototypes.size())];
            Map<String, IPowerUp> powerUpsUsed = usedPowerUps.get(wither.getUniqueID());
            if(powerUpPrototpe.minPower() > sizeOfPowerUp)
            {
               validPowerUpPrototypes.remove(powerUpPrototpe);
            }
            else if(powerUpsUsed.keySet().contains(powerUpPrototpe.getClass()))
            {
               IPowerUp powerUp = powerUpsUsed.get(powerUpPrototpe.getClass());
               if( powerUp.increasePower() )
               {
                  ++usedStrength;
                  HardcoreWither.logger.debug("Increasing power of " + powerUpPrototpe.getClass());
               }
               else
               {
                  validPowerUpPrototypes.remove(powerUpPrototpe);
               }
            }
            // If this is a new powerup for this Wither
            else
            {
               powerUpsUsed.put(powerUpPrototpe.getName(), powerUpPrototpe.createPowerUp(wither));
               usedStrength += powerUpPrototpe.minPower() > 0 ? powerUpPrototpe.minPower() : 1;
               HardcoreWither.logger.debug("Adding " + powerUpPrototpe.getName());
            }
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
      {}
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
      {}
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

   public boolean isWitherPoweredUp(EntityWither wither)
   {
      return usedPowerUps.containsKey(wither.getUniqueID());
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
            String powerUpName = (String) powerUpIter.next();
            NBTTagCompound powerUpNbt = new NBTTagCompound();
            usedPowerUps.get(witherUuid).get(powerUpName).writeToNBT(powerUpNbt);
            witherNbt.setTag(powerUpName, powerUpNbt);
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
         // we have no way to look up Withers by UUID until the chunk they are in gets loaded
         savedWitherData.put(witherUuid, witherNbt);
      }
   }  
   
   /**
    * Loads the Power Up data for a Wither from NBT
    * @param wither
    * @param nbt
    * @pre   the wither is not already in usedPowerUps
    * @post  the Power Up map for this Wither in usedPowerUps contains the IPowerUps from NBT
    */
   private void loadWitherFromNBT(EntityWither wither, NBTTagCompound nbt)
   {
      usedPowerUps.put(wither.getUniqueID(), new HashMap<String, IPowerUp>());
      Set powerUpTags = nbt.func_150296_c();
      Iterator powerUpIter = powerUpTags.iterator();
      while (powerUpIter.hasNext()) 
      {
         String powerUpName = (String)powerUpIter.next();
         try 
         {
            if(powerUpPrototypes.containsKey(powerUpName))
            {
               NBTTagCompound powerUpNbt = (NBTTagCompound) nbt.getTag(powerUpName);
               IPowerUp powerUp = powerUpPrototypes.get(powerUpName).createPowerUp(wither);
               powerUp.readFromNBT(powerUpNbt);
               usedPowerUps.get(wither.getUniqueID()).put(powerUpName, powerUp);
            }
         }
         catch (Exception ex)
         {
            HardcoreWither.logger.warn("Attempting to powerup from save with unknown powerup: " + powerUpName + "\n\t" + ex);
         }
      
      }
   }
}
