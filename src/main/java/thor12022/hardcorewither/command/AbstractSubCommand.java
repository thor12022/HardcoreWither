package thor12022.hardcorewither.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public abstract class AbstractSubCommand extends CommandBase implements ISubCommand
{
   @Override
   public String getCommandUsage(ICommandSender sender)
   {
      return "commands." + getCommandName() + ".usage";
   }
   
   @Override
   public void processCommand(ICommandSender sender, String[] args) 
   {
      //Clumsy, yes, but making the method private isn't a possibility in Java
      // I'll get used to this Java thing eventually
      assert(false);
   };
}
