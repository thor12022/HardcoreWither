package thor12022.hardcorewither;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import thor12022.hardcorewither.HardcoreWither;
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

public class DataStore
{
   private NBTTagCompound data;
   private File saveFile = null;
   
   public DataStore()
   {
      MinecraftForge.EVENT_BUS.register(this);
   }
   
   private void openFile()
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
            if( !saveFile.exists() || !saveFile.createNewFile() )
            {
               HardcoreWither.logger.error("Failed to create " + saveFile.getAbsolutePath() + " data will not save" );
            }
            else
            {
               saveFile = null;
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
         FileOutputStream fileOutputStream = null;
         try
         {
            fileOutputStream = new FileOutputStream( saveFile );
            CompressedStreamTools.writeCompressed(data, fileOutputStream );
         }
         catch( Throwable e )
         {
            data = new NBTTagCompound();
            HardcoreWither.logger.error("Cannot save data" );
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
      openFile();
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
            data = CompressedStreamTools.readCompressed( fileInputStream );
         }
         catch( Throwable e )
         {
            data = new NBTTagCompound();
            HardcoreWither.logger.error("Cannot load data" );
         }
      }
   }
   
   public NBTTagCompound getWitherData()
   {
      return data.getCompoundTag("WitherData");
   }
   
   public NBTTagCompound getWitherData(UUID id)
   {
      NBTTagCompound playerData = data.getCompoundTag("WitherData");
      return playerData.getCompoundTag(id.toString());
   } 
   
   public NBTTagCompound getPlayerData()
   {
      return data.getCompoundTag("PlayerData");
   }
   
   public NBTTagCompound getPlayerData(UUID id)
   {
      NBTTagCompound playerData = data.getCompoundTag("PlayerData");
      return playerData.getCompoundTag(id.toString());
   }
   
}
