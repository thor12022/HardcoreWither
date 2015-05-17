package thor12022.hardcorewither.entity;

import thor12022.hardcorewither.ModInformation;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.world.World;

public class EntityBlazeMinion extends EntityBlaze
{
   public static final String LOCALIZED_NAME = ModInformation.ID + ".BlazeMinion";
   public static final String UNLOCALIZED_NAME = "BlazeMinion";

   public EntityBlazeMinion(World p_i1731_1_)
   {
      super(p_i1731_1_);
   }
   
   @Override
   public EnumCreatureAttribute getCreatureAttribute()
   {
       return EnumCreatureAttribute.UNDEAD;
   }
}
