package nl.rubend.lobby;

import org.bukkit.entity.Player;

public class Command implements Action {
	private String command;
	public Command(String command) {
		this.command=command;
	}
	public void call(Player player) {
		player.performCommand(command);
	}
}
