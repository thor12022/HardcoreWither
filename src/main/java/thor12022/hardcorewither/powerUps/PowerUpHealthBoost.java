package thor12022.hardcorewither.powerUps;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityWither;

class PowerUpHealthBoost extends AbstractPowerUp
{
   protected PowerUpHealthBoost()
   {
      super();
   }
   
   private PowerUpHealthBoost(EntityWither theOwnerWither)
   {
      super(theOwnerWither);
      increasePower();
   }

   @Override
   public IPowerUp createPowerUp(EntityWither theOwnerWither)
   {
      PowerUpHealthBoost powerUp = new PowerUpHealthBoost(theOwnerWither);
      return powerUp;
   }

   @Override
   public void updateWither()
   {}

   @Override
   public void witherDied()
   {}

	@Override
	public void increasePower() 
	{
      double health = ownerWither.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue();
      ownerWither.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(health * 1.1D);
	}
};
