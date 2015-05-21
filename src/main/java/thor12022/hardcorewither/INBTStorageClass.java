package thor12022.hardcorewither;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTStorageClass
{
   public void writeToNBT(NBTTagCompound nbt);
   
   public void readFromNBT(NBTTagCompound nbt);
}
