package thor12022.hardcorewither.powerUps;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityWither;

class PowerUpHealthBoost extends AbstractPowerUp
{
   private static final float healthBoostMultiplier = 1.1f;
   
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
   {
      // the Wither's Charging time does not take a different health amount into account
      if(ownerWither.func_82212_n() > 0)
      {
         if (ownerWither.ticksExisted % 10 == 0)
         {
            ownerWither.heal( (10.0f * healthBoostMultiplier) - 10 );
         }
      }
   }

   @Override
   public void witherDied()
   {}

	@Override
	public void increasePower() 
	{
      double health = ownerWither.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue();
      ownerWither.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(health * healthBoostMultiplier);
	}
};
