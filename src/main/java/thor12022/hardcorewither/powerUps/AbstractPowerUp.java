package thor12022.hardcorewither.powerUps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import thor12022.hardcorewither.HardcoreWither;
import net.minecraft.entity.boss.EntityWither;

abstract class AbstractPowerUp implements IPowerUp
{
   final protected EntityWither ownerWither;
   
   static private Set<Class> constructedPrototypeClasses = new HashSet<Class>();
   
   AbstractPowerUp()
   {
      ownerWither = null;
      if(constructedPrototypeClasses.contains(getClass()))
      {
         HardcoreWither.logger.debug("Duplicate Prototype constructed for " + getClass().toString());
      }
   }
   
   protected AbstractPowerUp(EntityWither theOwnerWither)
   {
      
      ownerWither = theOwnerWither;
   }
}
