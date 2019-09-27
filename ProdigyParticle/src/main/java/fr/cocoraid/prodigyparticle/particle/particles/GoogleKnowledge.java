package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class GoogleKnowledge extends ProdigyParticle {


    private LinkedList<Vector> vecs = new LinkedList<>();
    {
        double radius = 0.5;
        double particleamount = 25;
        double amount = radius * particleamount;
        double inc = (2 * Math.PI) / amount;

        for (int i = 0; i < amount; i++) {
            double angle = i * inc;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Vector v = new Vector(x, 2, z);
            vecs.add(v);
        }
    }

    public GoogleKnowledge() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
    }


    private Color getColor(int id) {
        if(id == 3)
            return Color.BLUE;
        else if(id == 1)
            return Color.LIME;
        else if(id == 2)
            return Color.YELLOW;
        else
            return Color.RED;
    }

    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);


        Location l = player.getLocation();
        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(0, 3, 0), 1, 0f, 0f, 0f, 3);
        int i = 0;
        int colorID = 0;
        int div = vecs.size() / 4;
        for (Vector vec : vecs) {
            Vector v = vec.clone();
            l.add(v);
            displayParticle(l, Particle.REDSTONE,getColor(colorID));
            l.subtract(v);
            if(i % div == 0)
                colorID++;
            i++;
        }

    }


}
