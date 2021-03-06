package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import bl4ckscor3.plugin.animalessentials.core.AECommands;
import bl4ckscor3.plugin.animalessentials.core.AnimalEssentials;
import bl4ckscor3.plugin.animalessentials.save.Killing;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

import org.bukkit.ChatColor;

import com.darkblade12.particleeffect.ParticleEffect;

public class Kill implements IAECommand,Listener
{
	private static HashMap<Player,Killing> currentlyKilling = new HashMap<Player,Killing>();
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, final Player p, Command cmd, String[] args) throws IOException
	{
		if(currentlyKilling.containsKey(p))
		{
			Utilities.sendChatMessage(p, "You can't kill multiple animals by issuing the command multiple times. Please use /()/ae kill <amount>()/ after rightclicking an animal or waiting.");
			return;
		}

		plugin = pl;
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to kill. " + ChatColor.RED + " THIS IS IRREVERSIBLE!!");
		Utilities.sendChatMessage(p, "Kills available: " + (args.length == 1 ? 1 : Integer.parseInt(args[1])));
		currentlyKilling.put(p, new Killing(args.length == 1 ? 1 : Integer.parseInt(args[1])));
		AECommands.setIssuingCmd(p, true);
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(currentlyKilling.containsKey(p))
				{
					currentlyKilling.remove(p);
					AECommands.setIssuingCmd(p, false);
					Utilities.sendChatMessage(p, "You ran out of time to select an animal to kill. Use /()/ae kill()/ to start again.");
				}
			}
		}, 10L * 20); //10 seconds * 20 (server ticks/second)
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(currentlyKilling.containsKey(event.getPlayer()))
		{
			Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't kill this mob, it's " + Utilities.aN(entity.getType().name(), false) + " /()" + (entity.getType().name() == null? "Player" : entity.getType().name()) + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}

			if(!Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't kill it.");
				event.setCancelled(true);
				return;
			}
			//x offset, y offset, z offset from the center, speed, amount, center, radius
			ParticleEffect.SMOKE_NORMAL.display(0.5F, 0.0F, 0.0F, 0.0F, 100, entity.getLocation(), 255);
			//Play the sound at the location
			entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.FIZZ, 1.0F, 1.0F);

			entity.remove();
			Utilities.sendChatMessage(event.getPlayer(), "Animal killed.");
			
			if(currentlyKilling.get(event.getPlayer()).getAmount() == 1)
			{
				Utilities.sendChatMessage(event.getPlayer(), "Kills left: 0");
				currentlyKilling.remove(event.getPlayer());
				AECommands.setIssuingCmd(event.getPlayer(), false);
			}
			else
			{
				currentlyKilling.get(event.getPlayer()).decreaseAmount();
				Utilities.sendChatMessage(event.getPlayer(), "Kills left: " + currentlyKilling.get(event.getPlayer()).getAmount());
			}

			event.setCancelled(true);
		}
	}

	@Override
	public String getAlias()
	{
		return "kill";
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
				"Kills the right-clicked animal.",
				"By specifying a number at the end of the command, you can kill multiple animals."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.kill";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{1,2}); // /ae kill [number]
	}

	@Override
	public String getSyntax()
	{
		return "[number]";
	}
}
