package thor12022.hardcorewither.powerUps;

import java.util.Map;

import thor12022.hardcorewither.entity.EntityBlazeMinion;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.world.World;

public abstract class AbstractPowerUpMinionSpawner extends AbstractPowerUp
{
   protected class WitherMinionSpawner extends MobSpawnerBaseLogic
   {
      
      final private EntityWither ownerWither;
      
      public WitherMinionSpawner(EntityWither theOwnerWither)
      {
         ownerWither = theOwnerWither;
      }
      
      public void func_98267_a(int p_98267_1_)
      {
         // This would usually register a block update event with the world
      }

      public World getSpawnerWorld()
      {
         return ownerWither.worldObj;
      }

      public int getSpawnerX()
      {
         return (int) Math.round(ownerWither.lastTickPosX);
      }

      public int getSpawnerY()
      {
         return (int) Math.round(ownerWither.lastTickPosY) + 1;
      }

      public int getSpawnerZ()
      {
         return (int) Math.round(ownerWither.lastTickPosZ);
      }
   }

   protected class SpawnerData
   {
      public String  entityLocalizedName = "Pig";  //!< Mod Localized name of Entity
      public int     delay = 20;                   //!< 0-255
      public int     playerRange = 16;             //!< 0-255
      public int     maxEntities = 6;              //!< 0-255
      public int     minDelay = 200;               //!< 0-255
      public int     MaxDelay = 255;               //!< 0-255
      public int     spawnCount = 4;               //!< 0-255
      public int     spawnRange = 4;               //!< 0-255
   }

   private final WitherMinionSpawner spawner;
   protected SpawnerData spawnerData;
   
   public AbstractPowerUpMinionSpawner()
   {
      super();
      spawner = null;
   }
   
   protected AbstractPowerUpMinionSpawner(EntityWither theOwnerWither, String entityLocalizedName)
   {
      super(theOwnerWither);
      spawner = new WitherMinionSpawner(ownerWither);
      spawnerData = new SpawnerData();
      spawnerData.entityLocalizedName = entityLocalizedName;
      ResetSpawnerToData();
   }

   protected void ResetSpawnerToData()
   {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setString("EntityId", spawnerData.entityLocalizedName);
      nbt.setShort("Delay", (short)spawnerData.delay);
      nbt.setShort("RequiredPlayerRange", (short)spawnerData.playerRange);
      nbt.setShort("MaxNearbyEntities", (short)spawnerData.maxEntities);
      nbt.setShort("MinSpawnDelay", (short)spawnerData.minDelay);
      nbt.setShort("MaxSpawnDelay", (short)spawnerData.MaxDelay);
      nbt.setShort("SpawnCount", (short)spawnerData.spawnCount);
      nbt.setShort("SpawnRange", (short)spawnerData.spawnRange);
      spawner.readFromNBT(nbt);
   }
   
   @Override
   public void updateWither()
   {
      if(ownerWither.func_82212_n() > 0)
      {
         spawner.updateSpawner();
      }
   }

   @Override
   public void increasePower()
   {
      // TODO Auto-generated method stub
      
   };
}
