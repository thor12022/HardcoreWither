package thor12022.hardcorewither.handlers.wither;

import java.util.Map;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.world.World;

public class WitherMinionSpawner extends MobSpawnerBaseLogic
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
   };
