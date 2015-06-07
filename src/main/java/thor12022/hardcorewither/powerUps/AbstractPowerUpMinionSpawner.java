package thor12022.hardcorewither.powerUps;

import java.util.Map;

import thor12022.hardcorewither.config.ConfigManager;
import thor12022.hardcorewither.config.IConfigClass;
import thor12022.hardcorewither.entity.EntityBlazeMinion;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public abstract class AbstractPowerUpMinionSpawner extends AbstractPowerUp implements IConfigClass
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
      public int     delay = 20;                   //!< 0-65535
      public int     playerRange = 16;             //!< 0-65535
      public int     maxEntities = 6;              //!< 0-65535
      public int     minDelay = 200;               //!< 0-65535
      public int     maxDelay = 800;               //!< 0-65535
      public int     spawnCount = 4;               //!< 0-65535
      public int     spawnRange = 4;               //!< 0-65535
      
      SpawnerData()
      {}
      
      SpawnerData(SpawnerData other)
      {
         this.entityLocalizedName = other.entityLocalizedName;
         this.delay = other.delay;
         this.playerRange = other.playerRange;
         this.maxEntities = other.maxEntities;
         this.minDelay = other.minDelay;
         this.maxDelay = other.maxDelay;
         this.spawnCount = other.spawnCount;
         this.spawnRange = other.spawnRange;
      }
   }

   private final WitherMinionSpawner spawner;
   protected SpawnerData spawnerData;
   
   static protected SpawnerData  defaultSpawnerData;
   static protected float        spawnCountModifier   =  1.1f;
   static protected float        spawnDelayModifier   =  0.8f;
   static protected float        maxEntitiesModifier  =  1.1f;
   
   public AbstractPowerUpMinionSpawner()
   {
      super();
      spawner = null;
      ConfigManager.getInstance().addConfigClass(this);
   }
   
   protected AbstractPowerUpMinionSpawner(EntityWither theOwnerWither, String entityLocalizedName)
   {
      super(theOwnerWither);
      spawner = new WitherMinionSpawner(ownerWither);
      if(defaultSpawnerData == null)
      {
         spawnerData = new SpawnerData();
      }
      else
      {
         spawnerData = new SpawnerData(defaultSpawnerData);
      }
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
      nbt.setShort("MaxSpawnDelay", (short)spawnerData.maxDelay);
      nbt.setShort("SpawnCount", (short)spawnerData.spawnCount);
      nbt.setShort("SpawnRange", (short)spawnerData.spawnRange);
      spawner.readFromNBT(nbt);
   }
   
   @Override
   public void updateWither()
   {
      spawner.updateSpawner();
   }

   @Override
   public void increasePower()
   {
      spawnerData.spawnCount *= spawnCountModifier;
      spawnerData.delay *= spawnDelayModifier;
      spawnerData.minDelay *=  spawnDelayModifier;
      spawnerData.maxDelay *=  spawnDelayModifier;
      spawnerData.maxEntities *=  maxEntitiesModifier;
      ResetSpawnerToData();
   };
   
   public void syncConfig(Configuration config)
   {
      defaultSpawnerData = new SpawnerData();
      defaultSpawnerData.delay = config.getInt("defaultDelay", this.getSectionName(), defaultSpawnerData.delay, 0, 65535, "");
      defaultSpawnerData.playerRange = config.getInt("defaultPlayerRange", this.getSectionName(), defaultSpawnerData.playerRange, 0, 65535, "");
      defaultSpawnerData.minDelay = config.getInt("defaultMinimumDelay", this.getSectionName(), defaultSpawnerData.minDelay, 0, 65535, "");
      defaultSpawnerData.maxDelay = config.getInt("defaultMaximumDelay", this.getSectionName(), defaultSpawnerData.maxDelay, 0, 65535, "");
      defaultSpawnerData.spawnCount = config.getInt("defaultSpawnCount", this.getSectionName(), defaultSpawnerData.spawnCount, 0, 65535, "");
      defaultSpawnerData.spawnRange = config.getInt("defaultSpawnRange", this.getSectionName(), defaultSpawnerData.spawnRange, 0, 65535, "");
      
      spawnCountModifier = config.getFloat("spawnCountModifier", this.getSectionName(), spawnCountModifier, 1f, 10f, "Amount to increase Spawn Count by. 1.0 to never increase");
      spawnDelayModifier = config.getFloat("spawnDelayModifier", this.getSectionName(), spawnDelayModifier, 0f, 1f, "The smaller it is, the faster the delay decrease. 1.0 to never decrease");
      maxEntitiesModifier = config.getFloat("maxEntitiesModifier", this.getSectionName(), spawnCountModifier, 1f, 10f, "Amount to increase Max Entities by. 1.0 to never increase");
   }
}
