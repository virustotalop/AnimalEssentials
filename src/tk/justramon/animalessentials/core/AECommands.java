package tk.justramon.animalessentials.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import tk.justramon.animalessentials.cmd.Help;
import tk.justramon.animalessentials.cmd.IAECommand;
import tk.justramon.animalessentials.cmd.Reload;
import tk.justramon.animalessentials.util.Utilities;

public class AECommands implements CommandExecutor
{
	private final List<IAECommand> cmds = new ArrayList<IAECommand>();
	private final AnimalEssentials pl = AnimalEssentials.instance;

	public AECommands()
	{
		cmds.add(new Reload()); //adding this command to the list so we can access it below and in help
		cmds.add(new Help(cmds));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player p = null;

		if(sender instanceof Player)
			p  = (Player)sender;

		for(IAECommand c : cmds) //for each command in the commands list...
		{
			if(c.getAlias().equals(args[0])) //...we check if it's the command which got issued...
			{
				if(sender instanceof ConsoleCommandSender && c.isConsoleCommand()) //...we check if the sender is the console and if it's a console command, and if both is true...
				{
					c.exe(pl, p, cmd, args); //...we execute it...
					return true; //...and return true to stop the method since the command has been executed...
				}
				else if(p != null)//...if not, it has to come from a player but we check anyways so we can display a warning message to the console later on...
				{
					if(p.hasPermission(c.getPermission())) //...so we check if they have the permission needed to execute the command...
					{
						c.exe(pl, p, cmd, args); //...and if so, we execute it...
					}
					else
						Utilities.sendChatMessage(p, "You do not have the required permission to execute this command"); //...and if not we send this message to the player...

					return true; //...and finally return true since we know that everything went as expected...
				}
				else //...HOWEVER if the command gets issued from a command block or any other unhandled command sender (=anything else than console or player)...
				{
					Utilities.sendConsoleMessage(ChatColor.DARK_RED + "[SEVERE] " + ChatColor.RESET + "Command executed by unhandeled command sender: " + ChatColor.RED + sender.getName()); //...we send this message to the console including the command sender's name (for a command block it would be something like "commandblock")...
					return true; //...and return true to stop the method since we know that the command was sent by something
				}
			}
		}
		return false;
	}
}
