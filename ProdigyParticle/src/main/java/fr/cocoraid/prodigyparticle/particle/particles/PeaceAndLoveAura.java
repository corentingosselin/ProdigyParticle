package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeaceAndLoveAura extends ProdigyParticle {

    private Color[] rainbow = new Color[] {Color.FUCHSIA, Color.BLUE,Color.AQUA,Color.LIME,Color.YELLOW,Color.ORANGE,Color.RED};

    private Map<Integer,List<Vector>> vecs = new HashMap<>();
    {
        int id = 0;
        for(double radius = 0.2; radius < 1.8; radius+=0.1) {
            double particleamount = 26;
            double amount = radius * particleamount;
            double inc = (2 * Math.PI) / amount;
            vecs.putIfAbsent(id,new ArrayList<>());
            for (int i = 0; i < amount; i++) {
                double angle = i * inc;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Vector v = new Vector(x, 0.1, z);
                vecs.get(id).add(v);
            }
            id++;
        }
    }

    public PeaceAndLoveAura() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
    }

    private int current = 0;
    private int inc = 0;
    private boolean loop = false;

    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);
        Location l = player.getLocation();

        if(time % (20 * 2) == 0) {

            for (int k = 0; k < 5; k++) {
                displayParticle(l.clone().add(UtilMath.randomRange(-1, 1), 1 + UtilMath.randomRange(-1, 1), UtilMath.randomRange(-0.5, 0.5)), Particle.HEART);
            }
        }
        vecs.get(inc).forEach(v -> {
            l.add(v);
            displayParticle(l, Particle.REDSTONE,rainbow[current]);
            l.subtract(v);
        });


    }

    @Override
    public void globalUpdate() {
        super.globalUpdate();
        if(time % 2 == 0) {
            current++;
            if(current >= rainbow.length) {
                current = 0;
            }
            inc+= loop ? -1 : 1;
            if(inc >= vecs.size()) {
                loop = true;
                inc = vecs.size() - 1;
            } else if(inc <= 0) {
                loop = false;
                inc = 0;
            }
        }
    }
}
