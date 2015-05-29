package thor12022.hardcorewither.powerUps;

import net.minecraft.entity.boss.EntityWither;

public abstract class AbstractPowerUp implements IPowerUp
{
   final protected EntityWither ownerWither;
   
   AbstractPowerUp()
   {
      ownerWither = null;
   }
   
   protected AbstractPowerUp(EntityWither theOwnerWither)
   {
      
      ownerWither = theOwnerWither;
   }
}
