package thor12022.hardcorewither.powerUps;

import net.minecraft.entity.boss.EntityWither;
import thor12022.hardcorewither.entity.EntityGhastMinion;

class PowerUpGhastMinionSpawner extends AbstractPowerUpMinionSpawner
{
   protected PowerUpGhastMinionSpawner()
   {
      super();
   }
   
   private PowerUpGhastMinionSpawner(EntityWither theOwnerWither)
   {
      super(theOwnerWither, EntityGhastMinion.LOCALIZED_NAME);
   }

   @Override
   public IPowerUp createPowerUp(EntityWither theOwnerWither)
   {
      PowerUpGhastMinionSpawner powerUpGhastMinionSpawner = new PowerUpGhastMinionSpawner(theOwnerWither);
      return powerUpGhastMinionSpawner;
   }

   @Override
   public void updateWither()
   {
      if(ownerWither.func_82212_n() <= 0 && !ownerWither.isArmored())
      {
         super.updateWither();
      }
   }

   @Override
   public void witherDied()
   {}

   @Override
   public String getSectionName()
   {
      return "PowerUpGhastMinionSpawner";
   }
   
   @Override
   public int minPower()
   {
      return 2;
   }
};
