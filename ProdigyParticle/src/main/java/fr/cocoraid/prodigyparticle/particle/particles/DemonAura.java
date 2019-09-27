package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class DemonAura extends ProdigyParticle {


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

    public DemonAura() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
    }


    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);
        Location l = player.getLocation();

        displayParticle(l, Particle.SMOKE_LARGE,0.01F,0.2D);
        for (Vector v : vecs) {
            Vector dir = l.toVector().subtract(player.getLocation().toVector());
            l.add(v);
            displayParticle(l, Particle.FLAME,dir.setY(0.5),0.15);
            l.subtract(v);
        }


    }


    @Override
    public void globalUpdate() {
        super.globalUpdate();
        vecs.forEach(v -> {
            UtilMath.rotateAroundAxisY(v, -Math.PI / 32);
        });
    }
}
