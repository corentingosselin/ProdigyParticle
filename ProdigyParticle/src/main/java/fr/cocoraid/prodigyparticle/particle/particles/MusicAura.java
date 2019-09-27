package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MusicAura extends ProdigyParticle {




    private Color getColor(int currentForce) {
        if(currentForce >= 0 && currentForce <= 2) {
            return Color.LIME;
        } else if(currentForce >= 3 && currentForce <= 4) {
            return Color.YELLOW;
        } else if(currentForce >= 5 && currentForce <= 6) {
            return Color.ORANGE;
        } else
            return Color.RED;

    }

    private Map<Integer, Integer> bars = new HashMap<>();
    private LinkedList<Vector> vecs = new LinkedList<>();{
        double radius = 1;
        double particleamount = 20;
        double amount = radius * particleamount;
        double inc = (2 * Math.PI) / amount;

        int id = 0;
        for (int i = 0; i < amount; i++) {
            double angle = i * inc;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Vector v = new Vector(x, 0, z);
            vecs.add(v);
            bars.putIfAbsent(id,0);
            id++;
        }
    }




    public MusicAura() {
        super("§5Music§bAura", "La musique est en vous");
    }


    @Override
    public void globalUpdate() {
        super.globalUpdate();

        for(int i = 0; i < vecs.size(); i++) {
            int forceToAdd = UtilMath.randomRange(-2, 2);
            //make it fall down sometimes
            if (UtilMath.randomRange(0, 100) >= 80) forceToAdd = -4;
            int force = bars.get(i);
            if (forceToAdd + force < 0)
                force = 0;
            else if (forceToAdd + force > 7) {
                force = 7;
            } else force += forceToAdd;
            bars.put(i, force);
        }
    }

    @Override
    public void removeParticle(Player player) {
        super.removeParticle(player);
        if(manager.getEnabledParticles().isEmpty())
            bars.clear();
    }

    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);

        Location l = player.getLocation();

        if(time % (20 * 2) == 0) {
            for (int k = 0; k < 5; k++) {
                displayNoteParticle(l.clone().add(UtilMath.randomRange(-1, 1), 1 + UtilMath.randomRange(-1, 1), UtilMath.randomRange(-0.5, 0.5)));
            }
        }

        int i = 0;
        for (Vector vec : vecs) {
            Vector v = vec.clone();
            l.add(v);
            int force = bars.get(i);
            for(int k = 0; k < 7 ; k++) {
                if(k < force) {
                    double height = k * 0.15; //transform to small height
                    displayParticle(l.clone().add(0,height,0), Particle.REDSTONE,getColor(k));
                }
            }
            l.subtract(v);
            i++;
        }


    }
}

