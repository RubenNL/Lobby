package nl.rubend.lobby;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Lobby extends JavaPlugin implements Listener {
    private static Lobby plugin;
    public static Lobby getPlugin() {
        return plugin;
    }
    private int taskId=-1;
    private ArrayList<ClickableEntity> entities=new ArrayList<>();
    private World world;
    private ClickableEntity newEntity(EntityType type, Location location, String name, Action action) {
        ClickableEntity entity=new ClickableEntity(type,location,name,action);
        entities.add(entity);
        return entity;
    }
    @Override
    public void onEnable() {
        world= Bukkit.getWorld("lobby");
        plugin=this;
        Bukkit.getPluginManager().registerEvents(this, Lobby.getPlugin());
        placeVillagers();
    }
    @Override
    public void onDisable() {
        for(ClickableEntity entity:entities) entity.stop();
    }
    @EventHandler
    private void onEntityHurt(EntityDamageEvent event) {
        if(event.getEntity().getWorld()==world) event.setCancelled(true);
    }
    @EventHandler private void onJoin(PlayerJoinEvent event) {
        event.getPlayer().performCommand("lobby");
        configurePlayer(event.getPlayer());
    }
    @EventHandler private void onWorldJoin(PlayerChangedWorldEvent event) {
        configurePlayer(event.getPlayer());
    }
    private void configurePlayer(Player player) {
        if(player.getWorld()!=world) return;
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.getInventory().clear();
    }
    @EventHandler private void onChunkLoad(ChunkLoadEvent event) {
        if(event.getWorld()!=world) return;
        if(taskId>-1) Bukkit.getScheduler().cancelTask(taskId);
        taskId=Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::placeVillagers, 10L);
    }
    private void placeVillagers() {
        for(Entity entity:world.getEntities()) {
            if(entity.getType()!=EntityType.PLAYER) entity.remove();
        }
        getLogger().info("re-created villagers.");
        newEntity(EntityType.VILLAGER,new Location(world,16.5F,64,14.5F,90,0),"BedWars",new Command("bedwars"));
        newEntity(EntityType.VILLAGER,new Location(world,16.5F,64,10.5F,90,0),"FishSlap",new Command("fs"));
        newEntity(EntityType.VILLAGER,new Location(world,16.5F,64,6.5F,90,0),"RocketRiders",new ConnectTo("rr"));
        newEntity(EntityType.VILLAGER,new Location(world,10.5F,65,4.5F,0,0),"Creative",new Command("cr"));
        newEntity(EntityType.VILLAGER,new Location(world,10.5F,65,16.5F,180,0),"Survival",new ConnectTo("su"));
    }
}
