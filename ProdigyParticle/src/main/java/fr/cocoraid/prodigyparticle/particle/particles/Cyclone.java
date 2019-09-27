package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class Cyclone extends ProdigyParticle {

    private LinkedList<Vector> vecs = new LinkedList<>();
    {
        double radius = 2;
        double particleamount = 1;
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


    public Cyclone() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
    }


    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);
        Location l = player.getLocation();

        vecs.forEach(vec -> {
            Vector v = vec.clone();
            UtilMath.rotateAroundAxisY(v,time * Math.PI / 32);
            l.add(v);
            displayParticle(l, Particle.CLOUD,new Vector(0,1,0),0.1F);
            displayParticle(l, Particle.BLOCK_CRACK, UtilMath.randomRange(0,1) == 0 ? Material.STONE : Material.WATER, 1);
            l.subtract(v);
            UtilMath.rotateAroundAxisY(v,( time + 10) * Math.PI / 32);
            l.add(v);
            displayParticle(l, Particle.BLOCK_CRACK, UtilMath.randomRange(0,1) == 0 ? Material.DIRT : Material.LAVA, 1);
            displayParticle(l, Particle.CLOUD,new Vector(0,1,0),0.1F);
            l.subtract(v);
        });
    }


}
