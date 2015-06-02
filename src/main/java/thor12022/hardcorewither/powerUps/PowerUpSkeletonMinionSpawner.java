package thor12022.hardcorewither.powerUps;

import net.minecraft.entity.boss.EntityWither;
import thor12022.hardcorewither.entity.EntitySkeletonMinion;

class PowerUpSkeletonMinionSpawner extends AbstractPowerUpMinionSpawner
{
   protected PowerUpSkeletonMinionSpawner()
   {
      super();
   }
   
   private PowerUpSkeletonMinionSpawner(EntityWither theOwnerWither)
   {
      super(theOwnerWither, EntitySkeletonMinion.LOCALIZED_NAME);
   }

   @Override
   public IPowerUp createPowerUp(EntityWither theOwnerWither)
   {
      PowerUpSkeletonMinionSpawner powerUpSkeletonMinionSpawner = new PowerUpSkeletonMinionSpawner(theOwnerWither);
      return powerUpSkeletonMinionSpawner;
   }

   @Override
   public void witherDied()
   {}
};
