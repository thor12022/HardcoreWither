package thor12022.hardcorewither.handlers.wither;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import thor12022.hardcorewither.ConfigHandler;
import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.entity.EntityBlazeMinion;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

enum WitherState 
{
   Init,
   Charging,
   Airborne,
   Melee
};

class WitherMinionSpawner extends MobSpawnerBaseLogic
   {
      
      private EntityWither ownerWither;
      private WitherState witherState;   //!< Should never set this directly, use setState(MinionSpawnerState)
      
      WitherMinionSpawner(EntityWither theOwnerWither)
      {
         ownerWither = theOwnerWither;
         witherState = WitherState.Init;
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
         return (int) Math.round(ownerWither.lastTickPosY);
      }

      public int getSpawnerZ()
      {
         return (int) Math.round(ownerWither.lastTickPosZ);
      }
      
      public void updateSpawner()
      {
         super.updateSpawner();
         switch(witherState)
         {
            case Init:
               if(ownerWither.func_82212_n() > 0)
               {
                  setState( WitherState.Charging );
               }
               else if( !ownerWither.isArmored() )
               {
                  setState( WitherState.Airborne );
               }
               else
               {
                  setState( WitherState.Melee );
               }
               break;
            case Charging:
               if( ownerWither.func_82212_n() <= 0 )
               {
                  setState( WitherState.Airborne );
               }
               break;
            case Airborne:
               if( ownerWither.isArmored() )
               {
                  setState( WitherState.Melee );
               }
               break;
            case Melee:
               break;
         }
         
      }
      
      public void setState( WitherState state )
      {
         switch(state)
         {
            case Init:
               break;
            case Charging:
            {
               NBTTagCompound nbt = new NBTTagCompound();
               nbt.setString("EntityId", EntityBlazeMinion.LOCALIZED_NAME);
               nbt.setShort("Delay", (short)10);
               nbt.setShort("RequiredPlayerRange", (short)64);
               nbt.setShort("MaxNearbyEntities", (short)128);
               nbt.setShort("MinSpawnDelay", (short)10);
               nbt.setShort("MaxSpawnDelay", (short)30);
               nbt.setShort("SpawnCount", (short)4);
               nbt.setShort("SpawnRange", (short)16);
               readFromNBT(nbt);
               break;
            }
            case Airborne:
            {
               NBTTagCompound nbt = new NBTTagCompound();
               nbt.setString("EntityId", "Ghast");
               nbt.setShort("Delay", (short)10);
               nbt.setShort("RequiredPlayerRange", (short)64);
               nbt.setShort("MaxNearbyEntities", (short)128);
               nbt.setShort("MinSpawnDelay", (short)10);
               nbt.setShort("MaxSpawnDelay", (short)30);
               nbt.setShort("SpawnCount", (short)4);
               nbt.setShort("SpawnRange", (short)16);
               readFromNBT(nbt);
               break;
            }
            case Melee:
            {
               NBTTagCompound nbt = new NBTTagCompound();
               nbt.setString("EntityId", "Skeleton");
               nbt.setShort("Delay", (short)10);
               nbt.setShort("RequiredPlayerRange", (short)64);
               nbt.setShort("MaxNearbyEntities", (short)128);
               nbt.setShort("MinSpawnDelay", (short)10);
               nbt.setShort("MaxSpawnDelay", (short)30);
               nbt.setShort("SpawnCount", (short)4);
               nbt.setShort("SpawnRange", (short)16);
               readFromNBT(nbt);
               break;
            }
         }
         witherState = state;
      }
   };
