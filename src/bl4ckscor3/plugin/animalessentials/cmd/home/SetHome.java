package bl4ckscor3.plugin.animalessentials.cmd.home;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.cmd.IAECommand;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class SetHome implements IAECommand
{
	@Override
	public void exe(Plugin pl, Player p, Command cmd, String[] args) throws IOException
	{
		File folder = new File(pl.getDataFolder(), "playerStorage");
		File f = new File(pl.getDataFolder(), "playerStorage/" + p.getUniqueId() +".yml");
		
		if(!folder.exists())
			folder.mkdirs();

		if(!f.exists())
			f.createNewFile();
		
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(f);
		List<String> homes = yaml.getStringList("homes");

		if(homes.contains(args[1]))
		{
			Utilities.sendChatMessage(p, "/()" + args[1] + "()/ already exists. Use /()/ae edithome " + args[1] + "()/ to change the home.");
			return;        
		}

		homes.add(args[1]);
		yaml.set("homes" ,homes);
		yaml.set(args[1] + ".world", p.getWorld().getName());
		yaml.set(args[1] + ".x", p.getLocation().getX());
		yaml.set(args[1] + ".y", p.getLocation().getY());
		yaml.set(args[1] + ".z", p.getLocation().getZ());
		yaml.save(f); //saving the file after editing it
		Utilities.sendChatMessage(p, "Home /()" + args[1] + "()/ has been set in world /()" + yaml.getString(args[1] + ".world") + "()/ at these coordinates: " + Utilities.getFormattedCoordinates(yaml.getInt(args[1] + ".x"), yaml.getInt(args[1] + ".y"), yaml.getInt(args[1] + ".z")));
	}

	@Override
	public String getAlias()
	{
		return "sethome";
	}

	@Override
	public boolean isConsoleCommand()
	{
		return false;
	}

	@Override
	public String[] getHelp()
	{
		return new String[]{
				"Sets a home with the given name, so you can teleport your animals to it."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.home.set";
	}
	
	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{2}); // /ae sethome test 
	}
	
	@Override
	public String getSyntax()
	{
		return "<homeName>";
	}
}
