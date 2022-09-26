package gresisiaregen.gresisiaregen;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.api.AureliumAPI;
import gresisiaregen.gresisiaregen.Commands.GeneratorCommand;
import gresisiaregen.gresisiaregen.Config.ConfigManager;
import gresisiaregen.gresisiaregen.Generators.GeneratorManager;
import gresisiaregen.gresisiaregen.Listener.BlockBreakListener;
import gresisiaregen.gresisiaregen.Listener.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class GresisiaRegen extends JavaPlugin {

    public static GresisiaRegen main;
    public static AureliumSkills skills;

    @Override
    public void onEnable() {
        main = this;
        skills = AureliumAPI.getPlugin();

        ConfigManager.setupConfig(this);
        GeneratorManager.setupFile(this);

        getCommand("oreregen").setExecutor(new GeneratorCommand());
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
