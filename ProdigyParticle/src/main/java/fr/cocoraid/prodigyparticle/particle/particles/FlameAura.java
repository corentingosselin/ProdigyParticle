package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class FlameAura extends ProdigyParticle {

    private LinkedList<Vector> vecs = new LinkedList<>();
    {
        double radius = 1;
        double particleamount = 10;
        double amount = radius * particleamount;
        double inc = (2 * Math.PI) / amount;

        for (int i = 0; i < amount; i++) {
            double angle = i * inc;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Vector v = new Vector(x, 0.05, z);
            vecs.add(v);
        }
    }

    public FlameAura() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
    }

    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);
        Location l = player.getLocation();
        int[] i = {0};
        vecs.forEach(vec -> {
            Vector v = vec.clone();
            l.add(v);
            displayParticle(l, i[0]++ % 2 == 0 ? Particle.END_ROD : Particle.FLAME,v,0.08);
            l.subtract(v);
        });
    }

    @Override
    public void globalUpdate() {
        super.globalUpdate();
        vecs.forEach(v -> {
            UtilMath.rotateAroundAxisY(v, Math.PI / 64);
        });
    }
}
