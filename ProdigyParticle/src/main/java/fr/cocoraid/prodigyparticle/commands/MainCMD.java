package fr.cocoraid.prodigyparticle.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("prodigyparticle|pp")
public class MainCMD extends BaseCommand {

    @Default
    @Description("Show Help menu")
    public static void help(Player player, String[] args) {
        if (args.length == 0) {
          player.sendMessage("ceci est une aide");
        }
    }
}
