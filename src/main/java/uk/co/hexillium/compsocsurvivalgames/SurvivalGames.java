package uk.co.hexillium.compsocsurvivalgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.hexillium.compsocsurvivalgames.entities.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class SurvivalGames extends JavaPlugin {

    public static File plFolder;

    private List<Arena> arenas;
    private List<Game> games;
    private List<LootTable> lootTables;
    private List<UUID> adminsInEditMode;

    CommandManager commandManager = new CommandManager(this);

    private GameLogic gl;
    private GameManager gm;
    private ArenaModifications am;

    @Override
    public void onLoad(){
        ConfigurationSerialization.registerClass(LootNode.class);
        ConfigurationSerialization.registerClass(LootTable.class);
        ConfigurationSerialization.registerClass(Arena.class);
        ConfigurationSerialization.registerClass(ChestPoint.class);
    }

    @Override
    public void onEnable() {
        plFolder = this.getDataFolder();
        saveDefaultConfig();
        //this.getConfig().options().copyDefaults(true);
        saveConfig();

        games = new ArrayList<>();

        File lootTableFolder = new File(plFolder.getPath() + File.separator + "loottables" + File.separator);
        lootTableFolder.mkdirs();
        this.lootTables = ConfigManager.loadLootTables(lootTableFolder);

        File arenaFolder = new File(plFolder.getPath() + File.separator + "arenas" + File.separator);
        arenaFolder.mkdirs();
        this.arenas = ConfigManager.loadArenas(arenaFolder);


        gm = new GameManager(this);
        gl = new GameLogic(this, gm);
        am = new ArenaModifications(this);



        Bukkit.getPluginManager().registerEvents(gl, this);
        Bukkit.getPluginManager().registerEvents(am, this);


    }

    @Override
    public void onDisable() {
        File lootTableFolder = new File(plFolder.getPath() + File.separator + "arenas" + File.separator);
        ConfigManager.saveLootTables(lootTableFolder, lootTables);
        File arenaFolder = new File(plFolder.getPath() + File.separator + "arenas" + File.separator);
        ConfigManager.saveArenas(arenaFolder, arenas);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()){
            case "survivalgames":
                if (args.length == 0 || !commandManager.fire(sender, args)){
                    //send help
                }
                return true;
            default:
                sender.sendMessage(ChatColor.RED + "Failed to process command.");
                return true;
        }
    }

    public static File getPlFolder() {
        return plFolder;
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public List<Game> getGames() {
        return games;
    }

    public List<LootTable> getLootTables() {
        return lootTables;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public List<UUID> getAdminsInEditMode() {
        return adminsInEditMode;
    }

    public GameLogic getGl() {
        return gl;
    }

    public GameManager getGm() {
        return gm;
    }

    public ArenaModifications getAm() {
        return am;
    }

    public void addArena(Arena arena){
        this.arenas.add(arena);
    }

    /**
     * Checks to see if an arena with a given name exists
     * @param name Name to check against
     * @return true if an arena with the given name already exists, else false.
     */
    public boolean arenaExistsWithName(String name){
        return arenas.stream().anyMatch(arena -> arena.getName().equalsIgnoreCase(name));
    }
}