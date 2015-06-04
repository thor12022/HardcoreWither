package thor12022.hardcorewither.config;

import net.minecraftforge.common.config.Configuration;

public interface IConfigClass
{
   public String getSectionName();
   
   public void syncConfig(Configuration config);
}
