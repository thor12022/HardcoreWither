package thor12022.hardcorewither.command;

import net.minecraft.command.ICommandSender;

public interface ISubCommand
{
   public void processCommand(ICommandSender sender, String[] args, int startingIndex);
}
