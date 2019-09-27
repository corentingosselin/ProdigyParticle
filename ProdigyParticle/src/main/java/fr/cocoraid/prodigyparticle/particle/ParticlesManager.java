package fr.cocoraid.prodigyparticle.particle;

import fr.cocoraid.prodigyparticle.particle.particles.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class ParticlesManager {

    private Map<UUID,ProdigyParticle> enabledParticles = new HashMap<>();
    private Map<UUID,Location> oldLocation = new HashMap<>();
    private List<UUID> moving = new ArrayList<>();

    private List<ProdigyParticle> particles;

    public ParticlesManager() {
        this.particles = new ArrayList<>();
    }


    public void registerParticles() {
        particles.add(new FlameAura());
        particles.add(new MusicAura());
        particles.add(new BlackHole());
        particles.add(new Tornado());
        particles.add(new AngelAura());
        particles.add(new EndlessSpiral());
        particles.add(new FroozenCloud());
        particles.add(new FreedomFirework());
        particles.add(new AlienShield());
        particles.add(new GoogleKnowledge());
        particles.add(new PeaceAndLoveAura());
        particles.add(new PoseidonShield());
        particles.add(new Volcano());
        particles.add(new DemonAura());
        particles.add(new Cyclone());
        particles.add(new FreedomFirework());
    }

    public boolean isPlayerMoving(Player p) {
        return moving.contains(p.getUniqueId());
    }


    public void checkMove(Player p) {
        UUID uuid = p.getUniqueId();
        Location current = p.getLocation();
        Location last = oldLocation.get(uuid);
        if(last == null) {
            last = current;
        }
        oldLocation.put(uuid,current);
        if ((last.getX() != current.getX()) || (last.getZ() != current.getZ())) {
            if (!moving.contains(uuid))
                moving.add(p.getUniqueId());

        }
        else if (moving.contains(uuid))
            moving.remove(uuid);

    }

    public void resetPlayer(Player p) {
        UUID uuid = p.getUniqueId();
        enabledParticles.remove(uuid);
        oldLocation.remove(uuid);
        moving.remove(uuid);
    }


    public List<ProdigyParticle> getParticles() {
        return particles;
    }

    public Map<UUID, ProdigyParticle> getEnabledParticles() {
        return enabledParticles;
    }

    public Map<UUID, Location> getOldLocation() {
        return oldLocation;
    }

    public List<UUID> getMoving() {
        return moving;
    }
}
