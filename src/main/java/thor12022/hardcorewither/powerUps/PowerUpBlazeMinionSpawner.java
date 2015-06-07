package thor12022.hardcorewither.powerUps;

import net.minecraft.entity.boss.EntityWither;
import thor12022.hardcorewither.entity.EntityBlazeMinion;

class PowerUpBlazeMinionSpawner extends AbstractPowerUpMinionSpawner
{
   protected PowerUpBlazeMinionSpawner()
   {
      super();
   }
   
   private PowerUpBlazeMinionSpawner(EntityWither theOwnerWither)
   {
      super(theOwnerWither, EntityBlazeMinion.LOCALIZED_NAME);
   }

   @Override
   public IPowerUp createPowerUp(EntityWither theOwnerWither)
   {
      PowerUpBlazeMinionSpawner powerUpBlazeMinionSpawner = new PowerUpBlazeMinionSpawner(theOwnerWither);
      return powerUpBlazeMinionSpawner;
   }
   
   @Override
   public void updateWither()
   {
      if(ownerWither.func_82212_n() > 0)
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
      return "PowerUpBlazeMinionSpawner";
   }
};
