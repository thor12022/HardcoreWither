package thor12022.hardcorewither.powerUps;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.nbt.NBTTagCompound;
import thor12022.hardcorewither.ConfigHandler;
import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.entity.EntityGhastMinion;

class PowerUpGhastMinionSpawner extends AbstractPowerUp
{
   
   private final WitherMinionSpawner spawner;

   protected PowerUpGhastMinionSpawner()
   {
      super();
      spawner = null;
   }
   
   private PowerUpGhastMinionSpawner(EntityWither theOwnerWither)
   {
      super(theOwnerWither);
      spawner = new WitherMinionSpawner(ownerWither);
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setString("EntityId", EntityGhastMinion.LOCALIZED_NAME);
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
      if( ownerWither.func_82212_n() <= 0 && !ownerWither.isArmored() )
      {
         spawner.updateSpawner();
      }
   }

   @Override
   public IPowerUp createPowerUp(EntityWither theOwnerWither)
   {
      PowerUpGhastMinionSpawner powerUpGhastMinionSpawner = new PowerUpGhastMinionSpawner(theOwnerWither);
      return powerUpGhastMinionSpawner;
   }
   
   @Override
   public void increasePower()
   {
      
   }
};
