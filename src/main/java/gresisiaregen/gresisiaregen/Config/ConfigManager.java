package gresisiaregen.gresisiaregen.Config;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.skills.Skills;
import gresisiaregen.gresisiaregen.GresisiaRegen;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class ConfigManager {

    private static FileConfiguration config;

    public static void setupConfig(GresisiaRegen gRegen) {
        gRegen.saveDefaultConfig();
        ConfigManager.config = gRegen.getConfig();
    }

    public static Set<String> GetGenerators() {
        return Objects.requireNonNull(config.getConfigurationSection("Generators")).getKeys(false);
    }

    public static String GetGenerator(String key) {
        return config.getString("Generators." + key);
    }

    public static Material GetBlock(String key) {
        String name = config.getString("Generators." + key + ".block");
        return Material.getMaterial(name.toUpperCase());
    }

    public static int GetAge(String key) {
        return config.getInt("Generators." + key + ".age");
    }

    public static int GetDelay(String key) {
        return config.getInt("Generators." + key + ".delay-ticks");
    }

    public static Material GetDrop(String key) {
        String name = config.getString("Generators." + key + ".drops.item");
        return Material.getMaterial(name.toUpperCase());
    }

    public static int GetAmountDrop(String key) {
        return config.getInt("Generators." + key + ".drops.amount");
    }

    public static String GetPermission(String key) {
        return config.getString("Generators." + key + ".permission");
    }

    public static Material GetReplacement(String key) {
        String name = config.getString("Generators." + key + ".replacement");
        return Material.getMaterial(name.toUpperCase());
    }

    public static boolean GetDoubleDrops(String key) {
        return config.getBoolean("Generators." + key + ".Aurelium.double-drops");
    }

    public static Skills GetSkillType(String key) {
        String skillString = config.getString("Generators." + key + ".Aurelium.type");

        return Skills.valueOf(skillString.toUpperCase());
    }

    public static boolean GetDoubleDrops(Player player, String key) {
        Random random = new Random();
        int skillLevel = AureliumAPI.getSkillLevel(player, GetSkillType(key));

        if(random.nextInt(101) < skillLevel) {
            return true;
        }

        return false;
    }

    public static int GetExp(String key) {
        return config.getInt("Generators." + key + ".Aurelium.exp");
    }
}
