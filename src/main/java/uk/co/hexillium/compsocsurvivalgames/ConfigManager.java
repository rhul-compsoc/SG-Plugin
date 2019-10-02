package uk.co.hexillium.compsocsurvivalgames;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import uk.co.hexillium.compsocsurvivalgames.entities.Arena;
import uk.co.hexillium.compsocsurvivalgames.entities.LootTable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigManager {




    static public List<Arena> loadArenas(File folder){
        List<Arena> arenas = new ArrayList<>();
        for (File f : Objects.requireNonNull(folder.listFiles())){
            if (!f.getName().endsWith(".yml")) continue; //ignore files that do not end with .yml
            loadArenaFromFile(f);
        }
        return arenas;
    }

    private static Arena loadArenaFromFile(File file){
        try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            return (Arena) config.get("arena");
        } catch (NullPointerException e){
            return null;
        }
    }

    public static List<LootTable> loadLootTables(File folder){
        List<LootTable> tables = new ArrayList<>();
        for (File f : Objects.requireNonNull(folder.listFiles())){
            if (!f.getName().endsWith(".yml")) continue; //ignore files that do not end with .yml
            tables.add(loadLootTableFromFile(f));
        }
        return tables;
    }


    private static LootTable loadLootTableFromFile(File file){
        try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            return (LootTable) config.get("loot");
        } catch (NullPointerException e){
            return null;
        }
    }

    static public void saveArenas(File folder, List<Arena> arenas){
        for (Arena arena : arenas){
            saveArenaToFile(new File(folder.getPath() + File.separator + arena.getName() + ".yml"), arena);
        }
    }

    private static void saveArenaToFile(File file, Arena arena){
        try {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            fc.set("arena", arena);
            fc.save(file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    static public void saveLootTables(File folder, List<LootTable> lootTables){
        for (LootTable table : lootTables){
            saveLootTableToFile(new File(folder.getPath() + File.separator + table.getName() + ".yml"), table);
        }
    }

    private static void saveLootTableToFile(File file, LootTable table){
        try {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            fc.set("loot", table);
            fc.save(file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }


}
