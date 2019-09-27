package fr.cocoraid.prodigyparticle.particle;

import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ProdigyParticle {

    protected static ParticlesManager manager = fr.cocoraid.prodigyparticle.ProdigyParticle.getInstance().getManager();


    private String name,description;
    protected int time = 0;
    protected boolean followOnMove = false;

    public ProdigyParticle(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public void toggleParticle(Player player) {
        if(manager.getEnabledParticles().containsKey(player.getUniqueId())) {
            ProdigyParticle current = manager.getEnabledParticles().get(player.getUniqueId());
            removeParticle(player);
            if(current.equals(this))
                return;
        }
        startParticle(player);
    }

    public void removeParticle(Player player) {
        manager.resetPlayer(player);
    }

    public void startParticle(Player player) {
        manager.getOldLocation().put(player.getUniqueId(),player.getLocation());
        manager.getEnabledParticles().put(player.getUniqueId(),this);
    }

    public void asyncUpdatePlayer(Player player) {
    }

    public void globalUpdate() {
        time++;
        time%=20*60;
    }

    protected void displayParticle(Location point, Particle particle, Vector direction, double speed) {
        point.getWorld().spawnParticle(particle,point,0,direction.getX(),direction.getY(),direction.getZ(), speed);
    }

    protected void displayParticle(Location point, Particle particle) {
        point.getWorld().spawnParticle(particle,point,1,0,0,0, 0);
    }

    protected void displayParticle(Location point, Particle particle, float speed, double radius) {
        point.getWorld().spawnParticle(particle,point,1,radius,radius,radius, speed);
    }

    protected void displayParticle(Location point, Particle particle, float speed, double radius, int amount) {
        point.getWorld().spawnParticle(particle,point,amount,radius,radius,radius, speed);
    }

    protected void displayParticle(Location point, Particle particle, float speed, double offsetX,double offsetY,double offsetZ) {
        point.getWorld().spawnParticle(particle,point,1,offsetX,offsetY,offsetZ, speed);
    }



    protected void displayNoteParticle(Location point) {
        point.getWorld().spawnParticle(Particle.NOTE, point, 0, UtilMath.randomRange(0,24) / 24.0F, 0, 0);

    }

    protected void displayParticle(Location point, Particle particle, Color color) {
        point.getWorld().spawnParticle(particle,
                point.getX(),
                point.getY(),
                point.getZ(),
                0,
                color.getRed() <= 0 ? 0.000001 : color.getRed() / 255,
                color.getGreen() <= 0 ? 0.000001 : color.getGreen() / 255,
                color.getBlue() <= 0 ? 0.000001 : color.getBlue() / 255,
                1);
    }

    protected void displayParticle(Location point, Particle particle, Material material, int amount){
        point.getWorld().spawnParticle(
                particle,
                point,
                amount,
                0, 0, 0,
                new MaterialData(material)
        );
    }

    protected void displayParticle(Location point, Particle particle, ItemStack material){
        point.getWorld().spawnParticle(
                particle,
                point,
                0,
                0, 0, 0,
                material
        );
    }

    public String getName() {
        return name;
    }


    public boolean isFollowOnMove() {
        return followOnMove;
    }
}
