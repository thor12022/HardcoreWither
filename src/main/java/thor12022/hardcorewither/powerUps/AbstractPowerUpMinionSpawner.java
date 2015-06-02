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

   private final WitherMinionSpawner spawner;
   
   public AbstractPowerUpMinionSpawner()
   {
      super();
      spawner = null;
   }
   
   protected AbstractPowerUpMinionSpawner(EntityWither theOwnerWither, String entityLocalizedName)
   {
      super(theOwnerWither);
      spawner = new WitherMinionSpawner(ownerWither);
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setString("EntityId", entityLocalizedName);
      nbt.setShort("Delay", (short)10);
      nbt.setShort("RequiredPlayerRange", (short)64);
      nbt.setShort("MaxNearbyEntities", (short)128);
      nbt.setShort("MinSpawnDelay", (short)10);
      nbt.setShort("MaxSpawnDelay", (short)30);
      nbt.setShort("SpawnCount", (short)4);
      nbt.setShort("SpawnRange", (short)16);
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
