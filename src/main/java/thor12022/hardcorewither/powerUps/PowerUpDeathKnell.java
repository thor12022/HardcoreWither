package thor12022.hardcorewither.powerUps;

import thor12022.hardcorewither.config.ConfigManager;
import thor12022.hardcorewither.config.IConfigClass;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityWither;
import net.minecraftforge.common.config.Configuration;

class PowerUpDeathKnell extends AbstractPowerUp implements IConfigClass
{
   private static float knellStrengthMultiplier = 0.6666667f;
   
   protected PowerUpDeathKnell()
   {
      super();
      ConfigManager.getInstance().addConfigClass(this);
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
	public boolean increasePower() 
	{
	   if(super.powerStrength > 20)
	   {
	      return false;
	   }
	   else
	   {
	      knellStrengthMultiplier *= 1.5f;
	      return super.increasePower();
	   }
	}

   @Override
   public String getSectionName()
   {
      return "PowerUpDeathKnell";
   }

   @Override
   public void syncConfig(Configuration config)
   {
      knellStrengthMultiplier = config.getFloat("KnellStrengthMultiplier", this.getSectionName(), knellStrengthMultiplier, 0.0f, 10.0f, "");
      
   }

   @Override
   public int minPower()
   {
      return 1;
   }
};
