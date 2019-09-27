package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PoseidonShield extends ProdigyParticle {


    private List<Impact> impacts = new ArrayList<>();
    private class Impact {
        private Player target;
        private Location location;
        private int maxDistance = 100;
        private boolean toRemove = false;

        public Impact(Location start, Player target) {
            this.target = target;
            this.location = start;
            impacts.add(this);
        }

        public void goToTarget() {
            if(target == null) {
                toRemove = true;
                return;
            }
            Vector dir = target.getLocation().add(0,1,0).toVector().subtract(location.toVector()).multiply(0.01);
            dir.add(UtilMath.getRandomCircleVector().multiply(0.05));
            location.add(dir);

            displayParticle(location,Particle.FLAME,dir.clone().multiply(-1),0.05F);
            displayParticle(location.add(dir.clone().multiply(1.5)),Particle.SMOKE_LARGE);

            if(location.distanceSquared(target.getLocation()) <= 8) {
                displayParticle(location,Particle.FLAME,0.1F,0.5,10);
                displayParticle(location,Particle.LAVA,0.1F,0.1D,3);
                displayParticle(location,Particle.DRIP_WATER,0.1F,0.3D,10);


                Location l = target.getLocation();
                for (int i = 0; i < 200; i++) {
                    Vector vector = UtilMath.getRandomVector().multiply(2.5);
                    vector.setY(Math.abs(vector.getY()));
                    l.add(vector);
                    displayParticle(l,Particle.REDSTONE,i % 2 == 0 ? Color.BLUE : Color.AQUA);
                    l.subtract(vector);
                }

                toRemove = true;
            }
            maxDistance--;
            if(maxDistance <= 0) toRemove = true;
        }
    }


    public PoseidonShield() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
    }

    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);
        Location l = player.getLocation();

        if(time % (20 * 3)   == 0) {
            //pickup random location
            Vector vector = UtilMath.getRandomVector().multiply(6);
            vector.setY(Math.abs(vector.getY()));

            Location randomLoc = l.clone().add(vector).add(0, UtilMath.randomRange(0, 2D), 0);
            new Impact(randomLoc, player);
        }

    }

    @Override
    public void globalUpdate() {
        super.globalUpdate();
        impacts.removeIf(i -> {
            if(i.toRemove) return true;
            else i.goToTarget();
            return false;
        });
    }
}
