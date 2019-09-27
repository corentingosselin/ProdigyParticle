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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FreedomFirework extends ProdigyParticle {

    private Color[] freedom = new Color[] {Color.RED,Color.WHITE,Color.BLUE};
    private Vector[] vecs = new Vector[] {new Vector(0,1,0), new Vector(1,0,0),new Vector(0,0,1), new Vector(-1,0,0), new Vector(0,0,-1)};
    private Map<Firework, Integer> explode = new HashMap<>();
    private List<Firework> fireworks = new ArrayList<>();

    private class Firework {

        private Color color = freedom[UtilMath.randomRange(0,2)];
        private int counter = 0;
        private double down = UtilMath.randomRange(0,0.05);
        private Location start;
        private boolean toRemove = false;

        public Firework(Location start) {
            this.start = start;
            fireworks.add(this);
        }

        public void up() {
            if(toRemove) return;
            displayParticle(start.add(0,0.1,0), Particle.FIREWORKS_SPARK);
            if(counter >= 5) {
                explode.putIfAbsent(this,0);
                toRemove = true;
            }
            counter++;
        }

    }

    public FreedomFirework() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
    }


    @Override
    public void globalUpdate() {
        super.globalUpdate();

        if(time % 2 == 0) {

            fireworks.removeIf(f -> {
                if(f.toRemove) return true;
                else f.up();
                return false;
            });

            explode.keySet().forEach(f -> {
                Location point = f.start;
                int i = explode.get(f);
                for (Vector vec : vecs) {
                    displayParticle(point.clone().add(vec.clone().multiply(i* 0.1)).subtract(0,i*f.down,0),Particle.REDSTONE, f.color);
                }
                i++;
                explode.put(f,i);

            });
            explode.keySet().removeIf(f -> {
                int i = explode.get(f);
                if( i >= 10) return true;
                else return false;
            });
        }

    }

    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);
        Location l = player.getLocation();

        if(time % 20  == 0) {
            //pickup random location
            Location randomLoc = l.clone().add(UtilMath.getRandom2DVector().multiply(3)).add(0,UtilMath.randomRange(0,2D),0);
            new Firework(randomLoc);
            if(UtilMath.randomRange(0,10) == 0)
                randomLoc.getWorld().playSound(randomLoc, UtilMath.randomRange(0,1) == 0 ? Sound.ENTITY_FIREWORK_TWINKLE : Sound.ENTITY_FIREWORK_LAUNCH,0.1F,1.5F);
        }

    }


}
