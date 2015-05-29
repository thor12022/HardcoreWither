package thor12022.hardcorewither.interfaces;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTStorageClass
{
   public void writeToNBT(NBTTagCompound nbt);
   
   public void readFromNBT(NBTTagCompound nbt);
}
