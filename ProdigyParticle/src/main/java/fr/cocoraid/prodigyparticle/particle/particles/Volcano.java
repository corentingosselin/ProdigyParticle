package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class Volcano extends ProdigyParticle {




    private LinkedList<Vector> vecs = new LinkedList<>();{
        double radius = 0.5;
        for (double i = 0; i < (2 * Math.PI); i+= (Math.PI / 2)) {
            double x = radius * Math.cos(i);
            double z = radius * Math.sin(i);
            Vector v = new Vector(x, 0.4, z);
            vecs.add(v);
        }
    }

    public Volcano() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
    }


    @Override
    public void globalUpdate() {
        super.globalUpdate();
        for (Vector vec : vecs)
            UtilMath.rotateAroundAxisY(vec,Math.PI / 32);
    }

    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);
        Location l = player.getLocation();
        Location loc = l.clone().add(0,2.5,0);
        for (Vector v : vecs) {
            l.add(v);
            Vector dir = l.toVector().subtract(player.getLocation().toVector());
            l.subtract(v);
            displayParticle(l.clone().add(0,2.5,0), Particle.FLAME,dir.setY(-0.3),0.1);
        }

        for(int k = 0; k < 10; k++)
                displayParticle(l.clone().add(UtilMath.getRandomVector()
                            .setY(0)
                            .multiply(0.6))
                            .clone()
                        .add(0,2,0)
                    ,Particle.REDSTONE, Color.TEAL);

        if(time % (5) == 0) {
            displayParticle(loc, Particle.BLOCK_CRACK, Material.LAVA,5);

        }

        if(time % (20 * 4) == 0) {
            displayParticle(loc, Particle.LAVA);
            loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL,loc,10,0.1,0,0.1, 0.1F);
            displayParticle(loc, Particle.BLOCK_CRACK, Material.STONE,10);
            displayParticle(loc, Particle.BLOCK_CRACK, Material.DIRT,10);
            if(UtilMath.randomRange(0,10) >= 6)
                loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH ,0.01F,0F);
        }


    }


}
