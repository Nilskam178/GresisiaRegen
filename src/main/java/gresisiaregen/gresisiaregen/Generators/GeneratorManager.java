package gresisiaregen.gresisiaregen.Generators;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.skills.Skill;
import gresisiaregen.gresisiaregen.GresisiaRegen;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GeneratorManager {

    private static File generatorFile;
    private static YamlConfiguration modifyGeneratorFile;

    private static GresisiaRegen main;

    public static void setupFile(GresisiaRegen gRegen) {
        main = gRegen;

        try {
            initiateFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration getModifyGeneratorFile() { return modifyGeneratorFile; }
    public static File getGeneratorFile() { return generatorFile; }

    private static void initiateFiles() throws IOException {
        generatorFile = new File(Bukkit.getServer().getPluginManager().getPlugin(main.getDescription().getName()).getDataFolder(), "generators.yml");

        if(!generatorFile.exists()) {
            generatorFile.createNewFile();
        }

        modifyGeneratorFile = YamlConfiguration.loadConfiguration(generatorFile);

        if(!getModifyGeneratorFile().contains("Generators")) {
            getModifyGeneratorFile().createSection("Generators");
        }

        try {
            getModifyGeneratorFile().save(getGeneratorFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void AddGenerators(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if(!getModifyGeneratorFile().contains("Generators." + list.get(i))) {
                getModifyGeneratorFile().set("Generators", list.get(i));
            }
        }

        saveFile();
    }

    public static List<String> GetBlocks(String key) {
        return getModifyGeneratorFile().getStringList("Generators." + key);
    }

    public static List<Location> GetLocationsOfGenerator(String key) {
        List<Location> locations = new ArrayList<>();
        List<String> list = GetBlocks(key);

        for (int i = 0; i < list.size(); i++) {
            String[] result = list.get(i).split(";");

            locations.add(new Location(Bukkit.getServer().getWorld(result[0]), Double.parseDouble(result[1]), Double.parseDouble(result[2]), Double.parseDouble(result[3])));
        }

        return locations;
    }

    public static List<Location> GetAllLocations() {
        List<String> list = new ArrayList<>();
        list.addAll(Objects.requireNonNull(getModifyGeneratorFile().getConfigurationSection("Generators.")).getKeys(true));

        List<Location> locations = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            List<String> locationsStrings = getModifyGeneratorFile().getStringList("Generators." + list.get(i));

            for (int y = 0; y < locationsStrings.size(); y++) {
                String[] result = locationsStrings.get(y).split(";");

                locations.add(new Location(Bukkit.getServer().getWorld(result[0]), Double.parseDouble(result[1]), Double.parseDouble(result[2]), Double.parseDouble(result[3])));
            }
        }
        return locations;
    }

    public static List<Location> GetAllLocationsFromGenerator(String key) {
        List<String> list = getModifyGeneratorFile().getStringList("Generators." + key);
        List<Location> locations = new ArrayList<>();

        for (String locationsString : list) {
            String[] result = locationsString.split(";");

            locations.add(new Location(Bukkit.getServer().getWorld(result[0]), Double.parseDouble(result[1]), Double.parseDouble(result[2]), Double.parseDouble(result[3])));
        }

        return locations;
    }

    public static String GetGeneratorName(Location location) {
        List<String> list = new ArrayList<>();
        list.addAll(Objects.requireNonNull(getModifyGeneratorFile().getConfigurationSection("Generators.")).getKeys(true));

        for (int i = 0; i < list.size(); i++) {
            List<String> locationsStrings = getModifyGeneratorFile().getStringList("Generators." + list.get(i));

            for (int y = 0; y < locationsStrings.size(); y++) {
                String[] result = locationsStrings.get(y).split(";");
                Location loc = new Location(Bukkit.getServer().getWorld(result[0]), Double.parseDouble(result[1]), Double.parseDouble(result[2]), Double.parseDouble(result[3]));

                if(location.equals(loc)) {
                    return result[4];
                }
            }
        }

        return null;
    }

    public static boolean GeneratorAtLocation(Location location) {
        List<Location> list = GetAllLocations();

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(location)) {
                return true;
            }
        }

        return false;
    }

    public static void AddBlock(Location location, String key) {
        List<String> list = GetBlocks(key);
        list.add(location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + key + ";");

        getModifyGeneratorFile().set("Generators." + key, list);
        saveFile();
    }

    public static void RemoveBlock(Location location) {
        String key = GetGeneratorName(location);
        List<Location> locations = GetAllLocationsFromGenerator(key);
        locations.remove(location);

        getModifyGeneratorFile().set("Generators." + key, null);
        saveFile();

        for(Location value : locations) {
            AddBlock(value, key);
        }
    }

    private static void saveFile() {
        try {
            getModifyGeneratorFile().save(getGeneratorFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
