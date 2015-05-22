package thor12022.hardcorewither.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import thor12022.hardcorewither.HardcoreWither;
import thor12022.hardcorewither.INBTStorageClass;
import thor12022.hardcorewither.ModInformation;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;

public class DataStoreHandler
{
   private File saveFile = null;
   private Map<INBTStorageClass, String> storageClasses;
   
   public DataStoreHandler()
   {
      MinecraftForge.EVENT_BUS.register(this);
      storageClasses = new HashMap<INBTStorageClass, String>();
   }
   
   public void addStorageClass( INBTStorageClass theClass, String tagName )
   {
      storageClasses.put(theClass, tagName);
   }
   
   private void getDataFile()
   {
      File worldConfig = DimensionManager.getCurrentSaveRootDirectory();
      File hardcoreWitherFolder = new File( worldConfig.getPath(), ModInformation.CHANNEL );
      if( !hardcoreWitherFolder.isDirectory() && !hardcoreWitherFolder.mkdir() )
      {
         HardcoreWither.logger.error("Failed to create " + hardcoreWitherFolder.getAbsolutePath() + " data will not save" );
      }
      else
      {
         saveFile = new File(hardcoreWitherFolder, ModInformation.CHANNEL + ".dat");
         try
         {
            if( !saveFile.exists() && !saveFile.createNewFile() )
            {
               HardcoreWither.logger.error("Failed to create " + saveFile.getAbsolutePath() + " data will not save" );
               saveFile = null;
            }
            else
            {
               HardcoreWither.logger.debug("Data file: " + saveFile.getAbsolutePath() + " found/created" );
            }
         } catch (IOException e)
         {
            HardcoreWither.logger.error("Failed to create " + saveFile.getAbsolutePath() + " data will not save" );
            saveFile = null;
         }
      }
   }
      
   @SubscribeEvent
   public void onWorldSave(  Save event )
   {
      // we only need to do this once per Save, not per level
      if( event.world.provider.dimensionId != 0 )
      {
         return;
      }
      if( saveFile == null )
      {
         HardcoreWither.logger.error("Cannot save data" );
      }
      else
      {
         try
         {
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            NBTTagCompound globalNbt = new NBTTagCompound();
            Iterator iter = storageClasses.keySet().iterator();
            while (iter.hasNext()) 
            {
               NBTTagCompound classNbt = new NBTTagCompound();
               INBTStorageClass theClass = (INBTStorageClass)iter.next();
               theClass.writeToNBT(classNbt);
               globalNbt.setTag(storageClasses.get(theClass), classNbt);
            }
            CompressedStreamTools.writeCompressed(globalNbt, fileOutputStream );
            HardcoreWither.logger.debug("Saved data" );
         }
         catch( Throwable e )
         {
            HardcoreWither.logger.error("Error saving data: " + e.getLocalizedMessage());
         }
      }
   }
   
   @SubscribeEvent
   public void onWorldUnload(Unload event)
   {
      // we only need to do this once per Save, not per level
      if( event.world.provider.dimensionId != 0 )
      {
         return;
      }
      if( saveFile != null )
      {
         saveFile = null;
      }
   }
   
   @SubscribeEvent
   public void onWorldLoad(Load event)
   {
      // we only need to do this once per Save, not per level
      if( event.world.provider.dimensionId != 0 )
      {
         return;
      }
      getDataFile();
      if( saveFile == null )
      {
         HardcoreWither.logger.error("Cannot load data" );
      }
      else
      {
         FileInputStream fileInputStream = null;
         try
         {
            fileInputStream = new FileInputStream( saveFile );
            NBTTagCompound globalNbt = CompressedStreamTools.readCompressed( fileInputStream );
            Iterator iter = storageClasses.keySet().iterator();
            while (iter.hasNext()) 
            {
               INBTStorageClass theClass = (INBTStorageClass)iter.next();
               theClass.readFromNBT(globalNbt.getCompoundTag(storageClasses.get(theClass)));
            }
            HardcoreWither.logger.debug("Data loaded" );
         }
         catch( Throwable e )
         {
            HardcoreWither.logger.error("Error loading data" + e.getLocalizedMessage());
         }
      }
   }
}
