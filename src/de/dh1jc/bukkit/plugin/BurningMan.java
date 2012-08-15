package de.dh1jc.bukkit.plugin;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BurningMan extends JavaPlugin {
	
	HashMap<String, Location> BurningPlayers= new HashMap<String, Location>();
	HashMap<String, Integer> BurnDuration= new HashMap<String, Integer>();
	
	public void onEnable(){
		// Create the Listener
        new BurningManListener(this);    
        
	}
	
	public void onDisable(){        
        getLogger().info("Burningman disabled.");
	}		
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if((sender instanceof Player) && cmd.getName().equalsIgnoreCase("burn")) {
			String player=sender.getName();
			if (sender.hasPermission("burningman.burn")) {				
	
				if (args.length == 1) {
					player = args[0];
					if (sender.hasPermission("burningman.other")) {
						if (this.getServer().getPlayer(player) != null) {
							if (this.BurningPlayers.containsKey(player)) {
								sender.sendMessage(ChatColor.YELLOW + "You removed the burn curse from "+player);
							} else {
								sender.sendMessage(ChatColor.YELLOW + "You put the burn curse onto "+player);	
							}
						} else {
							sender.sendMessage(ChatColor.RED + "Players is not online.");
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.RED + "You have no power to do that.");
						return false;
					}	
				}
				
				// unburn player if already burning
				if (this.BurningPlayers.containsKey(player)) {
					this.BurningPlayers.remove(player);
					this.BurnDuration.remove(player);
					this.getServer().getPlayer(player).sendMessage(ChatColor.YELLOW + "You stopped burning.");
				} else {
					// register player to BURN!
					this.BurningPlayers.put(player, this.getServer().getPlayer(player).getLocation());
					this.BurnDuration.put(player, this.getConfig().getInt("burningman.duration"));
					this.getServer().getPlayer(player).sendMessage(ChatColor.YELLOW + "Burn...");
				}				
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "You can't burn.");
				return false;
			}
		} 
		return false; 
	}

}