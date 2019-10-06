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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.hexillium.compsocsurvivalgames.entities.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class SurvivalGames extends JavaPlugin {

    public static File plFolder;
    public static SurvivalGames instance;

    private List<Arena> arenas;
    private List<LootTable> lootTables;
    private HashSet<UUID> adminsInEditMode;

    CommandManager commandManager = new CommandManager(this);

    private GameLogic gl;
    private GameManager gm;
    private ArenaModifications am;
    private ConfigManager config;

    @Override
    public void onLoad(){
        ConfigurationSerialization.registerClass(LootNode.class);
        ConfigurationSerialization.registerClass(LootTable.class);
        ConfigurationSerialization.registerClass(Arena.class);
        ConfigurationSerialization.registerClass(ChestPoint.class);
    }

    @Override
    public void onEnable() {
        instance = this;
        plFolder = this.getDataFolder();
        saveDefaultConfig();
        //this.getConfig().options().copyDefaults(true);
        saveConfig();

        this.adminsInEditMode = new HashSet<>();

        File lootTableFolder = new File(plFolder.getPath() + File.separator + "loottables" + File.separator);
        lootTableFolder.mkdirs();
        this.lootTables = ConfigManager.loadLootTables(lootTableFolder);

        File arenaFolder = new File(plFolder.getPath() + File.separator + "arenas" + File.separator);
        arenaFolder.mkdirs();
        this.arenas = ConfigManager.loadArenas(arenaFolder);

        this.config = new ConfigManager(this);

        gm = new GameManager(this);
        gl = new GameLogic(this, gm);
        am = new ArenaModifications(this);



        Bukkit.getPluginManager().registerEvents(gl, this);
        Bukkit.getPluginManager().registerEvents(am, this);


    }

    @Override
    public void onDisable() {
        File lootTableFolder = new File(plFolder.getPath() + File.separator + "loottables" + File.separator);
        ConfigManager.saveLootTables(lootTableFolder, lootTables);
        File arenaFolder = new File(plFolder.getPath() + File.separator + "arenas" + File.separator);
        ConfigManager.saveArenas(arenaFolder, arenas);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()){
            case "survivalgames":
                if (args.length == 0 || !commandManager.fire(sender, args)){
                    sender.sendMessage("[join, leave, arena, toggleedit, game]");
                    //send help
                }
                return true;
            default:
                sender.sendMessage(ChatColor.RED + "Failed to process command.");
                return true;
        }
    }

    public boolean isUserInEditMode(Player player){
        return isUserInEditMode(player.getUniqueId());
    }

    public boolean isUserInEditMode(UUID uuid){
        return adminsInEditMode.contains(uuid);
    }

    /**
     * Adds a user into the edit mode
     * @param player the player to add
     * @return true if not already present, false if already present.
     */
    public boolean addUserToEditMode(Player player){
        return addUserToEditMode(player.getUniqueId());
    }

    /**
     * Adds a user by their id to the edit mode
     * @param uuid the player's UUID to add
     * @return true if not already present, false if already present
     */
    public boolean addUserToEditMode(UUID uuid){
        return adminsInEditMode.add(uuid);
    }

    public void removeUserFromEditMode(Player player){
        removeUserFromEditMode(player.getUniqueId());
    }

    public void removeUserFromEditMode(UUID uuid){
        adminsInEditMode.remove(uuid);
    }

    public static File getPlFolder() {
        return plFolder;
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public List<LootTable> getLootTables() {
        return lootTables;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public HashSet<UUID> getAdminsInEditMode() {
        return adminsInEditMode;
    }

    public Arena getArenaByName(String name){
        return arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public LootTable getLootTableByName(String name){
        return lootTables.stream().filter(table -> table.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public GameLogic getGameLogic() {
        return gl;
    }

    public GameManager getGameManager() {
        return gm;
    }

    public ArenaModifications getArenaModificationManager() {
        return am;
    }

    public ConfigManager getConfigManager() {
        return config;
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