package thor12022.hardcorewither.powerUps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.interfaces.INBTStorageClass;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.nbt.NBTTagCompound;

abstract class AbstractPowerUp implements IPowerUp
{
   final protected EntityWither ownerWither;
   protected int powerStrength;
   
   static private Set<Class> constructedPrototypeClasses = new HashSet<Class>();
   
   AbstractPowerUp()
   {
      ownerWither = null;
      powerStrength = 0;
      if(constructedPrototypeClasses.contains(getClass()))
      {
         HardcoreWither.logger.debug("Duplicate Prototype constructed for " + getClass().toString());
      }
   }
   
   protected AbstractPowerUp(EntityWither theOwnerWither)
   {
      powerStrength = 1;
      ownerWither = theOwnerWither;
   }
   
   @Override
   public void writeToNBT(NBTTagCompound nbt)
   {
      nbt.setInteger("powerStrength", powerStrength);
   }

   @Override
   public void readFromNBT(NBTTagCompound nbt)
   {
      powerStrength = nbt.getInteger("powerStrength");
      powerStrength = powerStrength <= 0 ? 1 : powerStrength;
   }
   
   @Override
   public boolean increasePower()
   {
      ++powerStrength;
      return true;
   }
}
