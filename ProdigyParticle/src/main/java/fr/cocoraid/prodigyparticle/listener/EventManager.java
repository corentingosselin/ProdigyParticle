package fr.cocoraid.prodigyparticle.listener;

import fr.cocoraid.prodigyparticle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.database.MysqlManager;
import fr.cocoraid.prodigyparticle.particle.ParticlesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EventManager implements Listener {


    private ParticlesManager manager;
    private MysqlManager mysqlManager;
    public EventManager(ProdigyParticle instance) {
        this.manager = instance.getManager();
        this.mysqlManager = instance.getMysqlManager();
    }

    @EventHandler
    public void asyncJoin(AsyncPlayerPreLoginEvent e) {
        mysqlManager.saveInAsync(e.getUniqueId());

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        mysqlManager.setParticleBack(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        mysqlManager.saveOutAsync(uuid);
        if(manager.getEnabledParticles().containsKey(uuid)) {
            manager.getEnabledParticles().get(uuid).removeParticle(e.getPlayer());
        }

    }
}
