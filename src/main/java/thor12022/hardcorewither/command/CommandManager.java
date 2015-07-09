package thor12022.hardcorewither.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import thor12022.hardcorewither.ModInformation;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandManager extends CommandBase
{
   static private CommandManager instance = new CommandManager(); 
   
   static void registerSubCommand(CommandBase command)
   {
      instance.commandMap.put(command.getCommandName(), command);
   }
   
   public static void serverStarting(FMLServerStartingEvent event)
   {
      //registerSubCommand(new CommandName());
      
      Iterator commandIter = instance.commandMap.keySet().iterator();
      while( commandIter.hasNext() )
      {
         event.registerServerCommand((ICommand) commandIter.next());
      }
   }

   private Map<String, CommandBase> commandMap = new HashMap<String, CommandBase>();
   
   @Override
   public String getCommandName()
   {
      return ModInformation.CHANNEL;
   }

   @Override
   public String getCommandUsage(ICommandSender sender)
   {
      return "commands." + getCommandName() + ".usage";
   }

   /**
    * Return the required permission level for this command.
    */
   @Override
   public int getRequiredPermissionLevel()
   {
       return 2;
   }
   
   @Override
   public void processCommand(ICommandSender sender, String[] args)
   {
      if (args.length == 0)
      {
          throw new WrongUsageException(getCommandUsage(sender));
      }
      else if (args[0].equals("help"))
      {
          throw new WrongUsageException(getCommandUsage(sender));
      }
      else if (commandMap.containsKey(args[0]))
      {
         //! @todo This is stupid, fix it
         ArrayList argsArray = new ArrayList(args.length);
         argsArray.remove(0);
         commandMap.get(args[0]).processCommand(sender, (String[])argsArray.toArray());
      }
   }

}
