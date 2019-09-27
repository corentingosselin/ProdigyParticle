package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class BlackHole extends ProdigyParticle {


    private LinkedList<Vector> vecs = new LinkedList<>();
    {
        double radius = 0.5;
        double particleamount = 20;
        double amount = radius * particleamount;
        double inc = (2 * Math.PI) / amount;

        for (int i = 0; i < amount; i++) {
            double angle = i * inc;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Vector v = new Vector(x, 0, z);
            vecs.add(v);
        }
    }

    public BlackHole() {
        super("§8Black§5Hole", "Un soleil de feu autours de vous ");
    }



    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);

        Location l = player.getLocation();

        int i = 0;
        for (Vector v : vecs) {
            l.add(v);
            displayParticle(l, Particle.REDSTONE, Color.BLACK);
            if(i % 2 == 0 )
                displayParticle(l, Particle.DRAGON_BREATH,v.setY(0),0.01);

            l.subtract(v);
            i++;
        }
    }

    @Override
    public void globalUpdate() {
        super.globalUpdate();
        vecs.forEach(v -> {
            UtilMath.rotateAroundAxisY(v,Math.PI / 64);
        });
    }
}
