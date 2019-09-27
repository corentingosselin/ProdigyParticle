package fr.cocoraid.prodigyparticle;

import co.aikar.commands.PaperCommandManager;
import fr.cocoraid.prodigyparticle.commands.MainCMD;
import fr.cocoraid.prodigyparticle.commands.ParticleSelectorCMD;
import fr.cocoraid.prodigyparticle.database.MysqlManager;
import fr.cocoraid.prodigyparticle.listener.EventManager;
import fr.cocoraid.prodigyparticle.listener.TaskManager;
import fr.cocoraid.prodigyparticle.particle.ParticlesManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ProdigyParticle extends JavaPlugin {

    private static ProdigyParticle instance;

    private PaperCommandManager cmdManager;
    private ParticlesManager manager;
    private MysqlManager mysqlManager;

    @Override
    public void onEnable() {

        instance = this;
        manager = new ParticlesManager();
        manager.registerParticles();

        mysqlManager = new MysqlManager(manager).setup();


        new TaskManager(this).runTaskTimerAsynchronously(this,0,1);

        Bukkit.getPluginManager().registerEvents(new EventManager(this),this);

        registerCommands();
    }

    @Override
    public void onDisable() {

    }

    private void registerCommands() {
        cmdManager = new PaperCommandManager(this);

        List<String> list = new ArrayList<>();
        manager.getParticles().forEach(p -> {
            list.add(p.getClass().getSimpleName());
        });
        cmdManager.getCommandCompletions().registerAsyncCompletion("particleType", c -> {
            return list;
        });

        cmdManager.registerCommand(new MainCMD());
        cmdManager.registerCommand(new ParticleSelectorCMD(manager));

    }

    public static ProdigyParticle getInstance() {
        return instance;
    }

    public ParticlesManager getManager() {
        return manager;
    }

    public MysqlManager getMysqlManager() {
        return mysqlManager;
    }
}
