package thor12022.hardcorewither.powerUps;

import thor12022.hardcorewither.interfaces.INBTStorageClass;
import net.minecraft.entity.boss.EntityWither;

public interface IPowerUp extends INBTStorageClass
{
   void updateWither();
   
   void witherDied();
   
   IPowerUp createPowerUp(EntityWither theOwnerWither);
   
   void increasePower();
}
