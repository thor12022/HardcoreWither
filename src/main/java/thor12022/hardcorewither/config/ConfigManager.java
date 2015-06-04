package thor12022.hardcorewither.config;

/*
 * Creation and usage of the config file.
 */

import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.ModInformation;

public class ConfigManager
{

   private static ConfigManager instance = new ConfigManager();
   
   /**
    * @todo This was supposed to map IConfigClasses to Categories in a single Configuration
    *    however, this proved impractical as the Configuration class and ConfigurationCategory
    *    class do not provide high level methods for ConfigurationCategory. It doesn't have 
    *    getString getInt getDouble, etc . It should function similarly to the DataStoreManager,
    *    passing to each IConfigClass an object containing only its own data
    */
   private Map<IConfigClass, Configuration> configClasses;
   private File configDirectory;
   
   public void init(File configDirectory)
   {
      this.configDirectory = configDirectory;
      syncConfig();
   }
   
   public static ConfigManager getInstance()
   {
      return instance;
   }
   
   private ConfigManager()
   {
      configClasses = new HashMap<IConfigClass, Configuration>();
   }
   
   public void addConfigClass( IConfigClass theClass )
   {
      Configuration theConfig = new Configuration(new File( configDirectory + File.separator + ModInformation.CHANNEL + File.separator + theClass.getSectionName() + ".cfg"));
      configClasses.put(theClass, theConfig);
      theClass.syncConfig(theConfig);
      theConfig.save();
   }

   public void syncConfig()
   {
      try
      {
         Iterator iter = configClasses.keySet().iterator();
         while (iter.hasNext()) 
         {
            IConfigClass theClass = (IConfigClass)iter.next();
            Configuration theConfig = configClasses.get(theClass);
            theClass.syncConfig(theConfig);
            theConfig.save();
         }
         HardcoreWither.logger.debug("Synced config data");
      }
      catch( Throwable e )
      {
         HardcoreWither.logger.error("Error syncing config data: " + e.getLocalizedMessage());
      }
   }
}
