package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class AlienShield extends ProdigyParticle {



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
            Vector v = new Vector(x, 0.1, z);
            vecs.add(v);

        }
    }

    public AlienShield() {
        super("§5Endless§fSpiral", "Un soleil de feu autours de vous ");
    }


    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);
        Location l = player.getLocation();
        int i = 0;

        for (Vector v : vecs) {
            l.add(v);
            Vector dir = l.toVector().subtract(player.getLocation().toVector());
            if(i % 2 == 0)
                displayParticle(l, Particle.DRAGON_BREATH,dir,0.1);
            else
                displayParticle(l, Particle.END_ROD,dir.setY(0.1),0.1);
            l.subtract(v);
            i++;
        }

        for(int k = 0 ; k < 10;k++)
            displayParticle(l.clone().add(UtilMath.getRandomVector()).add(0,1,0), Particle.REDSTONE,UtilMath.randomRange(0,1) == 0 ? Color.FUCHSIA: Color.LIME);



    }
}
