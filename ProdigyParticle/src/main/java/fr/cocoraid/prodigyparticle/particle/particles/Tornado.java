package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class Tornado extends ProdigyParticle {


    private LinkedList<Vector> vecs = new LinkedList<>();{
        double radius = 0.5;
        for (double i = 0; i < (2 * Math.PI); i+= (Math.PI / 2)) {
            double x = radius * Math.cos(i);
            double z = radius * Math.sin(i);
            Vector v = new Vector(x, 0, z);
            vecs.add(v);
        }
    }


    public Tornado() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
        followOnMove = true;
    }


    private class TornadoObject {
        private double noMoveTime = 0, movementSpeed = 0.2d;
        private Vector vector = new Vector(0, 0, 0);
        private Location currentLocation, targetLocation;
        private Player player;

        public TornadoObject(Player p) {
            this.player = p;
            targetLocation = getNewTarget();
            Vector vector = UtilMath.getRandomCircleVector().multiply(3);
            vector.setY(0);
            currentLocation = p.getLocation().add(vector);
            tornado.put(p.getUniqueId(),this);
        }

        public void update() {

            if (!currentLocation.getWorld().equals(player.getWorld())) currentLocation = player.getLocation();
            if (!currentLocation.getWorld().equals(targetLocation.getWorld())) return;
            double distanceBtw = player.getEyeLocation().distance(currentLocation);
            double distTarget = currentLocation.distance(targetLocation);

            if (distTarget < 1d || distanceBtw > 3)
                targetLocation = getNewTarget();

            distTarget = currentLocation.distance(targetLocation);

            if (UtilMath.random.nextDouble() > 0.95)
                noMoveTime = System.currentTimeMillis() + UtilMath.randomRange(0D, 2000D);

            if (player.getLocation().distance(currentLocation) < 5)
                movementSpeed = noMoveTime > System.currentTimeMillis() ? Math.max(0, movementSpeed - 0.0075) : Math.min(0.1, movementSpeed + 0.0075);
            else {
                noMoveTime = 0;
                movementSpeed = Math.min(0.15 + distanceBtw * 0.05, movementSpeed + 0.02);
            }

            vector.add(targetLocation.toVector().subtract(currentLocation.toVector()).multiply(0.2));

            if (vector.length() < 1)
                movementSpeed = vector.length() * movementSpeed;

            vector = vector.normalize();

            if (distTarget > 0.1)
                currentLocation.add(vector.clone().multiply(movementSpeed));


            Location l = currentLocation.clone();
            for (Vector vec : vecs) {
                Vector v = vec.clone();
                l.add(v);
                l.subtract(v);
                displayParticle(l,Particle.CLOUD);
                displayParticle(l, Particle.DRAGON_BREATH,v.clone().setY(1),0.1);
            }

        }


        private Location getNewTarget() {
            return player.getLocation().add(Math.random() * 6 - 3, 0 , Math.random() * 6 - 3);
        }


    }

    @Override
    public void globalUpdate() {
        super.globalUpdate();
        vecs.forEach(v -> {
            UtilMath.rotateAroundAxisY(v,Math.PI / 16);
        });

    }

    @Override
    public void startParticle(Player player) {
        super.startParticle(player);
        new TornadoObject(player);
    }

    @Override
    public void removeParticle(Player player) {
        super.removeParticle(player);
        tornado.remove(player.getUniqueId());
    }

    private Map<UUID,TornadoObject> tornado = new HashMap<>();

    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);
        tornado.get(player.getUniqueId()).update();
    }


}
