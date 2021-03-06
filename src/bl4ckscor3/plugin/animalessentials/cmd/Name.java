package bl4ckscor3.plugin.animalessentials.cmd;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import com.darkblade12.particleeffect.ParticleEffect;

import bl4ckscor3.plugin.animalessentials.core.AECommands;
import bl4ckscor3.plugin.animalessentials.core.AnimalEssentials;
import bl4ckscor3.plugin.animalessentials.util.Utilities;

public class Name implements IAECommand,Listener
{
	public static HashMap<Player, String> currentlyNaming = new HashMap<Player, String>();
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, final Player p, Command cmd, String[] args) throws IOException
	{
		if(currentlyNaming.containsKey(p))
		{
			Utilities.sendChatMessage(p, "You can't name multiple animals at a time. Please name an animal or wait, then issue the command again.");
			return;
		}

		plugin = pl;
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to name.");
		currentlyNaming.put(p, putNameTogether(args));
		AECommands.setIssuingCmd(p, true);
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(currentlyNaming.containsKey(p))
				{
					currentlyNaming.remove(p);
					AECommands.setIssuingCmd(p, false);
					Utilities.sendChatMessage(p, "You ran out of time to select an animal to name. Use /()/ae name()/ to start again.");
				}
			}
		}, 10L * 20); //10 seconds * 20 (server ticks/second)
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(currentlyNaming.containsKey(event.getPlayer()))
		{
			Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't name this mob, it's " + Utilities.aN(entity.getType().name(), false) + " /()" + (entity.getType().name() == null ? "Player" : entity.getType().name()) + "()/ and not an animal.");
				event.setCancelled(true);
				return;
			}

			if(!Utilities.isOwnedBy(event.getPlayer(), entity, true))
			{
				Utilities.sendChatMessage(event.getPlayer(), "This is not your animal, you can't name it.");
				event.setCancelled(true);
				return;
			}

			if(((LivingEntity)entity).getCustomName() != null && ((LivingEntity)entity).getCustomName().equals(currentlyNaming.get(event.getPlayer()))) //we need equals so players can change the casing of the animal
			{
				Utilities.sendChatMessage(event.getPlayer(), "The animal is already named /()" + ((LivingEntity)entity).getCustomName() + "()/.");
				event.setCancelled(true);
				return;
			}
			
			if(plugin.getConfig().getBoolean("shouldNamingUseNametag"))
			{
				if(event.getPlayer().getGameMode() != GameMode.CREATIVE) //checking if the player is in creative, if so, we won't use a name tag
				{
					if(!event.getPlayer().getInventory().contains(Material.NAME_TAG))
					{
						Utilities.sendChatMessage(event.getPlayer(), "You need a name tag in your inventory to be able to name this animal.");
						return;
					}
					else
					{
						PlayerInventory inv = event.getPlayer().getInventory();
						ItemStack stack = inv.getItem(inv.first(Material.NAME_TAG));

						stack.setAmount(stack.getAmount() - 1);
						inv.setItem(inv.first(Material.NAME_TAG), stack);
					}
				}
			}
			
			//x offset, y offset, z offset from the center, speed, amount, center, radius
			ParticleEffect.CLOUD.display(0.5F, (float)((LivingEntity)entity).getEyeHeight() + 0.5F, 0.5F, 0.0F, 20, entity.getLocation(), 255);
			//Play the sound at the location
			entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);

			((LivingEntity)entity).setCustomName(currentlyNaming.get(event.getPlayer()));
			currentlyNaming.remove(event.getPlayer());
			AECommands.setIssuingCmd(event.getPlayer(), false);
			Utilities.sendChatMessage(event.getPlayer(), "Animal named.");
			event.setCancelled(true);
		}
	}

	private String putNameTogether(String[] args)
	{
		String s = "";
		
		for(int i = 1; i < args.length; i++)
		{
			s += args[i] + " ";
		}
		
		return s.trim();
	}
	
	@Override
	public String getAlias()
	{
		return "name";
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
				"Names the right-clicked animal."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.name";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{2}); // /ae name <name>
	}
	
	@Override
	public String getSyntax()
	{
		return "<newName>";
	}
}
