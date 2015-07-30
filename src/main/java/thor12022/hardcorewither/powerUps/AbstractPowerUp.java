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
   final protected String className = getClass().getSimpleName();
   protected int powerStrength;
   
   static private Set<String> constructedPrototypeClasses = new HashSet<String>();
   
   AbstractPowerUp()
   {
      ownerWither = null;
      powerStrength = 0;
      if(constructedPrototypeClasses.contains(className))
      {
         HardcoreWither.logger.debug("Duplicate Prototype constructed for " + className);
      }
      else
      {
         constructedPrototypeClasses.add(className);
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
   
   @Override
   public String getName()
   {
      return className;
   }
}
