package fr.cocoraid.prodigyparticle.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.cocoraid.prodigyparticle.particle.ParticlesManager;
import fr.cocoraid.prodigyparticle.particle.ProdigyParticle;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("prodigyparticle|pp")
public class ParticleSelectorCMD extends BaseCommand {

    private ParticlesManager manager;
    public ParticleSelectorCMD(ParticlesManager manager) {
        this.manager = manager;
    }


    @CommandCompletion("@particleType")
    @Subcommand("select")
    public void onParticleEnable(Player player, String name) {
        Optional<ProdigyParticle> entry = manager.getParticles().stream().filter(p -> p.getClass().getSimpleName().equalsIgnoreCase(name)).findFirst();
        if (entry.isPresent()) {
            if(player.hasPermission("pp.use." + name.toLowerCase()))
                entry.get().toggleParticle(player);
            else
                player.sendMessage("§cyou do not have the permission to do that");
        } else {
            player.sendMessage("particle not found");
        }


    }


    @Subcommand("remove")
    public void onParticleDisable(Player player) {
        if(manager.getEnabledParticles().containsKey(player.getUniqueId())) {
            manager.getEnabledParticles().get(player.getUniqueId()).removeParticle(player);
        }
    }

}