package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class AngelAura extends ProdigyParticle {


    private LinkedList<Vector> vecs = new LinkedList<>();
    {
        double radius = 0.5;
        double particleamount = 6;
        double amount = radius * particleamount;
        double inc = (2 * Math.PI) / amount;

        for (int i = 0; i < amount; i++) {
            double angle = i * inc;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Vector v = new Vector(x, 0.2, z);
            vecs.add(v);

        }
    }



    public AngelAura() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
    }


    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);

        Location l = player.getLocation();
        displayParticle(l.clone().add(0,2.3,0), Particle.CLOUD,0.01F,0.2D);
        displayParticle(l, Particle.CLOUD,0.01F,0.2D);
        for (Vector v : vecs) {
            l.add(v);
            Vector dir = l.toVector().subtract(player.getLocation().toVector());
            displayParticle(l.clone().add(0,2.3,0),Particle.FIREWORKS_SPARK,dir.setY(-0.5),0.15);
            displayParticle(l,Particle.FIREWORKS_SPARK,dir.setY(0.5),0.15);
            l.subtract(v);
        }

    }


    @Override
    public void globalUpdate() {
        super.globalUpdate();
        for (Vector vec : vecs) {
            UtilMath.rotateAroundAxisY(vec, Math.PI / 100);


        }
    }
}
