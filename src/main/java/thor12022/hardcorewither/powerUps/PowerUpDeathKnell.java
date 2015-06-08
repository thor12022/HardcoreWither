package thor12022.hardcorewither.powerUps;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityWither;

class PowerUpDeathKnell extends AbstractPowerUp
{
   private float knellStrengthMultiplier = 1.0f;
   
   protected PowerUpDeathKnell()
   {
      super();
   }
   
   private PowerUpDeathKnell(EntityWither theOwnerWither)
   {
      super(theOwnerWither);
      increasePower();
   }

   @Override
   public IPowerUp createPowerUp(EntityWither theOwnerWither)
   {
      PowerUpDeathKnell powerUp = new PowerUpDeathKnell(theOwnerWither);
      return powerUp;
   }

   @Override
   public void updateWither()
   {}

   @Override
   public void witherDied()
   {
      ownerWither.worldObj.newExplosion(ownerWither, ownerWither.posX, ownerWither.posY + (double)ownerWither.getEyeHeight(), ownerWither.posZ, 7.0F * knellStrengthMultiplier, false, ownerWither.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
   }

	@Override
	public void increasePower() 
	{
	   knellStrengthMultiplier *= 1.5f;
	}
};
