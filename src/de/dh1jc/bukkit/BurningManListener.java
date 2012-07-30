package de.dh1jc.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BurningManListener implements Listener{

	private final BurningMan plugin;

	public BurningManListener(BurningMan plugin) {
		// Register the listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent evt) {
		// Is player burning?
		if (this.plugin.BurningPlayers.containsKey(evt.getPlayer().getName())) {
			Location loc = evt.getPlayer().getLocation();
			// is this distance >= 1 then set last location on fire
			if (loc.distance(this.plugin.BurningPlayers.get(evt.getPlayer().getName()))>1) {

				// decrease burn duration if player has not ignoreduration set.
				if (! evt.getPlayer().hasPermission("burningman.ignoreduration")) {
					this.plugin.BurnDuration.put(evt.getPlayer().getName(), this.plugin.BurnDuration.get(evt.getPlayer().getName())-1);
				}
				
				// if duration is 0 stop burning
				if (this.plugin.BurnDuration.get(evt.getPlayer().getName()) == 0) {
					evt.getPlayer().sendMessage(ChatColor.YELLOW + "You stopped burning.");					
					this.plugin.BurningPlayers.remove(evt.getPlayer().getName());
					this.plugin.BurnDuration.remove(evt.getPlayer().getName());
				} else {
					// set last position on fire and update current location
					this.plugin.BurningPlayers.get(evt.getPlayer().getName()).getBlock().getRelative(BlockFace.SELF).setType(Material.FIRE);
					this.plugin.BurningPlayers.put(evt.getPlayer().getName(), loc);

					// play flames effect around player
					evt.getPlayer().getWorld().playEffect(evt.getPlayer().getLocation(), Effect.MOBSPAWNER_FLAMES , 0);
				}
			}
		}		
	}	
	
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent evt) {
        if(this.plugin.BurningPlayers.containsKey(evt.getPlayer().getName()))
        {
        	// On quit remove player from burning
        	this.plugin.getLogger().info("Player "+evt.getPlayer().getName()+" Quits");
			this.plugin.BurningPlayers.remove(evt.getPlayer().getName());
			this.plugin.BurnDuration.remove(evt.getPlayer().getName());
        }
    }

}