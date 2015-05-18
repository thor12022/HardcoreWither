package thor12022.hardcorewither.entity;

import thor12022.hardcorewither.ModInformation;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySkeletonMinion extends EntitySkeleton
{
   public static final String UNLOCALIZED_NAME = "SkeletonMinion";
   public static final String LOCALIZED_NAME = ModInformation.ID + "." + UNLOCALIZED_NAME;

   public EntitySkeletonMinion(World p_i1731_1_)
   {
      super(p_i1731_1_);
      setSkeletonType(1);
   }
   
   @Override
   public EnumCreatureAttribute getCreatureAttribute()
   {
       return EnumCreatureAttribute.UNDEAD;
   }
   
   @Override
   public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
   {
      if (p_70097_1_.getEntity() != null && p_70097_1_.getEntity().getClass() == EntityWither.class)
      {
         return false;
      }
      return super.attackEntityFrom(p_70097_1_, p_70097_2_);
   }
   
   @Override
   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
   {}
}
