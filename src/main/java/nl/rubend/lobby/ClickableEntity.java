package nl.rubend.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ClickableEntity implements Listener {
	private LivingEntity entity;
	private String command;
	ClickableEntity(EntityType type,Location location,String name,String command) {
		this.command=command;
		this.entity=(LivingEntity) location.getWorld().spawnEntity(location, type);
		entity.setCustomName(name);
		entity.setAI(false);
		entity.setInvulnerable(true);
		Bukkit.getPluginManager().registerEvents(this, Lobby.getPlugin());
	}
	void stop() {
		entity.remove();
		HandlerList.unregisterAll(this);
	}
	@EventHandler
	private void onClick(PlayerInteractEntityEvent event) {
		if(event.getRightClicked()!=entity) return;
		event.getPlayer().performCommand(command);
	}
}
