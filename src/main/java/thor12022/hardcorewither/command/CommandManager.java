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
   
   static public CommandManager getInstance()
   {
      return instance;
   }
   
   /**
    * Registers the commands with the Server
    */
   public static void serverStarting(FMLServerStartingEvent event)
   {
      event.registerServerCommand(instance);
   }
   
   private Map<String, AbstractSubCommand> commandMap = new HashMap<String, AbstractSubCommand>();
   
   private CommandManager()
   {}
   
   @Override
   public String getCommandName()
   {
      return ModInformation.CHANNEL;
   }

   @Override
   public String getCommandUsage(ICommandSender sender)
   {
      String text = "commands." + getCommandName() + ".usage";
      Iterator iter = commandMap.keySet().iterator();
      while(iter.hasNext())
      {
         text += iter.next() + "\n";
      }
      return text;
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
         commandMap.get(args[0]).processCommand(sender, args, 1);
      }
   }

   public void registerSubCommand(AbstractSubCommand subCommand)
   {
      commandMap.put(subCommand.getCommandName(), subCommand);
   }
   
}
