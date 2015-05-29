package thor12022.hardcorewither.powerUps;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import thor12022.hardcorewither.ConfigHandler;
import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.entity.EntityBlazeMinion;
import thor12022.hardcorewither.entity.EntityGhastMinion;
import thor12022.hardcorewither.entity.EntitySkeletonMinion;
import thor12022.hardcorewither.handlers.wither.WitherMinionSpawner;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

class PowerUpBlazeMinionSpawner extends AbstractPowerUp
{
   
   private final WitherMinionSpawner spawner;
   
   private PowerUpBlazeMinionSpawner(EntityWither theOwnerWither)
   {
      super(theOwnerWither);
      spawner = new WitherMinionSpawner(ownerWither);
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setString("EntityId", EntityBlazeMinion.LOCALIZED_NAME);
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
   public IPowerUp createPowerUp(EntityWither theOwnerWither)
   {
      PowerUpBlazeMinionSpawner powerUpBlazeMinionSpawner = new PowerUpBlazeMinionSpawner(theOwnerWither);
      return powerUpBlazeMinionSpawner;
   }
   
   @Override
   public void increasePower()
   {
      
   }
};
