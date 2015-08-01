package thor12022.hardcorewither.powerUps;

import java.util.Random;

import thor12022.hardcorewither.config.IConfigClass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.common.config.Configuration;

class PowerUpLightning extends AbstractPowerUp implements IConfigClass
{
   private static final Random random = new Random();
   private static float lightningFrequencyMultiplier  = 1.1f;
   private static float lightningRandomness           = 0.5f;
   private static int   lightningFequencyBase         = 100;
   private static float lightningInaccuracy           = 0.5f;
   
   private long   lightningNextTick;
   
   protected PowerUpLightning()
   {
      super();
   }
   
   private PowerUpLightning(EntityWither theOwnerWither)
   {
      super(theOwnerWither);
      setNextRandomTick();
   }

   private void setNextRandomTick()
   {
      int strengthBasedTick = (int) (lightningFequencyBase / (super.powerStrength * lightningFrequencyMultiplier));
      int modifier = (int) ((random.nextGaussian() * lightningRandomness) * strengthBasedTick);
      lightningNextTick =  ownerWither.worldObj.getTotalWorldTime() + strengthBasedTick + modifier;
   }
   
   @Override
   public IPowerUp createPowerUp(EntityWither theOwnerWither)
   {
      PowerUpLightning powerUp = new PowerUpLightning(theOwnerWither);
      return powerUp;
   }

   @Override
   public void updateWither()
   {
      if( ownerWither.worldObj.getTotalWorldTime() > lightningNextTick )
      {
         int targetId = ownerWither.getWatchedTargetId(0);
         if( targetId != -1)
         {
            Entity target = ownerWither.worldObj.getEntityByID(targetId);
            if(target != null)
            {
               double lightningXPos = target.lastTickPosX + (8 * random.nextGaussian() * lightningInaccuracy);
               double lightningYPos = target.lastTickPosY + (8 * random.nextGaussian() * lightningInaccuracy);
               double lightningZPos = target.lastTickPosZ + (8 * random.nextGaussian() * lightningInaccuracy);
               ownerWither.worldObj.addWeatherEffect(new EntityLightningBolt(ownerWither.worldObj, lightningXPos, lightningYPos, lightningZPos));
               setNextRandomTick();
            }
         }
      }
   }

   @Override
   public void witherDied()
   {}

   @Override
   public boolean increasePower() 
   {
      if(super.powerStrength < 20 && super.increasePower())
      {
         setNextRandomTick();
         return true;
      }
      else
      {
         return false;
      }
   }
   
   @Override
   public int minPower()
   {
      return 1;
   }

   @Override
   public String getSectionName()
   {
      return "PowerUpLightning";
   }

   @Override
   public void syncConfig(Configuration config)
   {
      lightningFrequencyMultiplier = config.getFloat("LightningFrequencyMultiplier", this.getSectionName(), lightningFrequencyMultiplier, 0.0f, 10.0f, "");
      lightningRandomness = config.getFloat("LightningRandomness", this.getSectionName(), lightningRandomness, 0.0f, 10.0f, "0 is not random, 1 is more random");
      lightningFequencyBase = config.getInt("LightningFequencyBase", this.getSectionName(), lightningFequencyBase, 1, Integer.MAX_VALUE, "Avg number of ticks between lightning");
      lightningInaccuracy = config.getFloat("LightningInnacuracy", this.getSectionName(), lightningInaccuracy, 0.0f, 10.0f, "0 is prefect");
   }
};
