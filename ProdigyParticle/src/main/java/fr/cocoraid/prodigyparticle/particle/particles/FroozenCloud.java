package fr.cocoraid.prodigyparticle.particle.particles;

import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import fr.cocoraid.prodigyparticle.utils.UtilMath;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FroozenCloud extends ProdigyParticle {




    public FroozenCloud() {
        super("§4Flame§cAura", "Un soleil de feu autours de vous ");
    }



    @Override
    public void asyncUpdatePlayer(Player player) {
        super.asyncUpdatePlayer(player);

        Location l = player.getLocation().add(0,3,0);

        displayParticle(l.clone().add(UtilMath.getRandomCircleVector()
                .setY(0)
                .multiply(1)), Particle.END_ROD,new Vector(0,-3,0),0.1F);

        displayParticle(l.clone().add(UtilMath.getRandomCircleVector()
                .setY(0)
                .multiply(0.7)), Particle.REDSTONE, Color.AQUA);


        //display cloud
        for(int k = 0; k < 15; k++)
            displayParticle(l.add(UtilMath.getRandomCircleVector()
                    .setY(0)
                    .multiply(0.2))
                    .clone()
                    .add(0,UtilMath.randomRange(0F,0.4F),0)
                    ,Particle.CLOUD);
    }
}
