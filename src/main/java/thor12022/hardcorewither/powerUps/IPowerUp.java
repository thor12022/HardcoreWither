package thor12022.hardcorewither.powerUps;

import net.minecraft.entity.boss.EntityWither;

public interface IPowerUp
{
   void updateWither();
   
   void witherDied();
   
   IPowerUp createPowerUp(EntityWither theOwnerWither);
   
   void increasePower();
}
