package fr.cocoraid.prodigyparticle.listener;

import fr.cocoraid.prodigyparticle.database.MysqlManager;
import fr.cocoraid.prodigyparticle.particle.ParticlesManager;
import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskManager extends BukkitRunnable {


    private ParticlesManager manager;
    private MysqlManager mysqlManager;
    public TaskManager(fr.cocoraid.prodigyparticle.ProdigyParticle instance) {
        this.manager = instance.getManager();
        this.mysqlManager = instance.getMysqlManager();
    }


    private int time = 0;
    @Override
    public void run() {

        if(!manager.getEnabledParticles().isEmpty()) {
            manager.getEnabledParticles().keySet().forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                if(!p.isOnline()) return;
                if(time % 5 == 0)
                    manager.checkMove(p);

                if(time % 20 == 0)
                    manager.getOldLocation().put(uuid,p.getLocation());

                ProdigyParticle particle = manager.getEnabledParticles().get(uuid);
                if(!particle.isFollowOnMove() && manager.isPlayerMoving(p)) return;

                particle.asyncUpdatePlayer(Bukkit.getPlayer(uuid));
            });
            manager.getParticles().forEach(pp -> pp.globalUpdate());
        }

        if(mysqlManager.mysqlDisconnected) {
            if(time >= (20* 60)) {
                Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(cur -> cur.isOp())
                        .forEach(cur -> {
                            cur.sendMessage("§4ProdigyParticle: Mysql has been disconnected while attempting to send a request " +
                                    "\n§cPlease check your mysql, the plugin will try to send another request in 1 minute");
                        });
                mysqlManager.startConnection();
            }
        }

        time++;
        time%=(20*60);
    }


}
