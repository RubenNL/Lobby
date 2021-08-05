package nl.rubend.lobby;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;

public class ConnectTo implements Action {
	private String server;
	public ConnectTo(String server) {
		this.server=server;
	}
	public void call(Player player) {
		Lobby.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(Lobby.getPlugin(), "BungeeCord");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendPluginMessage(Lobby.getPlugin(), "BungeeCord", out.toByteArray());
	}
}
