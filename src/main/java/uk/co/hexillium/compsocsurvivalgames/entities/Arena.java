package uk.co.hexillium.compsocsurvivalgames.entities;

import net.shadowxcraft.rollbackcore.Copy;
import net.shadowxcraft.rollbackcore.Paste;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import uk.co.hexillium.compsocsurvivalgames.SurvivalGames;

import java.util.*;

@SerializableAs("SGArena")
public class Arena implements ConfigurationSerializable {


    private String name;
    private Location centre;

    private Location corner1;
    private Location corner2;

    private List<ChestPoint> chests;
    private List<Location> spawnPoints;

    private transient LootTable lootTable;
    private transient boolean hasBeenModified;

    private transient double[] highest;
    private transient double[] lowest;

    private ArenaState state;


    public Arena(String name, Location l1, Location l2){
        this.name = name;
        this.chests = new ArrayList<>();
        this.spawnPoints = new ArrayList<>();
        this.state = ArenaState.CONFIGURING;
        this.corner1 = l1;
        this.corner2 = l2;
        this.centre = new Location(corner1.getWorld(),
                mid(corner1.getX(), corner2.getX()),
                mid(corner1.getY(), corner2.getY()),
                mid(corner1.getZ(), corner2.getZ())
        );
        updateBounds();
        saveArenaDatFile();
    }

    private double mid(double one, double two){
        return (one + two) / 2;
    }

    private Arena(Map<String, Object> map){
        this.centre = (Location) map.get("centre");
        this.state = ArenaState.valueOf((String) map.get("state"));
        this.name = (String) map.get("name");
        this.chests = (List<ChestPoint>) map.get("chest_spots");
        this.spawnPoints = (List<Location>) map.get("spawn_points");
        this.corner1 = (Location) map.get("corner_min");
        this.corner2 = (Location) map.get("corner_max");
        this.lootTable = SurvivalGames.instance.getLootTableByName((String) map.get("loottable"));
        updateBounds();
    }

    public boolean isValid(){
        return (!name.isEmpty()) &&
                centre != null &&
                corner1 != null &&
                corner2 != null &&
                !chests.isEmpty() &&
                lootTable != null;
    }

    public List<ChestPoint> getChestPoints() {
        return chests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not yet supported.");
//        if (state != ArenaState.CONFIGURING)
//            throw new IllegalStateException("Arena must be in configure mode.");
//        this.name = name;
    }

    public Location getCentre() {
        return centre;
    }

    public void setCentre(Location centre) {
        if (state != ArenaState.CONFIGURING)
            throw new IllegalStateException("Arena must be in configure mode.");
        this.centre = centre;
    }

    public Location getCorner1() {
        return corner1;
    }

    public void setCorner1(Location corner1) {
        if (state != ArenaState.CONFIGURING)
            throw new IllegalStateException("Arena must be in configure mode.");
        this.corner1 = corner1;
        updateBounds();
    }

    public Location getCorner2() {
        return corner2;
    }

    public void setCorner2(Location corner2) {
        if (state != ArenaState.CONFIGURING)
            throw new IllegalStateException("Arena must be in configure mode.");
        this.corner2 = corner2;
        updateBounds();
    }

    public LootTable getLootTable() {
        return lootTable;
    }

    public void saveArenaDatFile(){
        saveArenaDatFile(null);
    }

    public void saveArenaDatFile(CommandSender sender){
        Location min = new Location(this.corner1.getWorld(), this.lowest[0], this.lowest[1], this.lowest[2]);
        Location max = new Location(this.corner2.getWorld(), this.highest[0], this.highest[1], this.highest[2]);
        new Copy(min, max, SurvivalGames.plFolder + "/arenaregions/" + this.getName().toLowerCase(),
                sender, "SurvivalGames >> ").run();
    }

    public void setLootTable(LootTable lootTable) {
        if (state != ArenaState.CONFIGURING)
            throw new IllegalStateException("Arena must be in configure mode.");
        this.lootTable = lootTable;
    }

    public boolean hasBeenModified() {
        return hasBeenModified;
    }

    public void setHasBeenModified(boolean hasBeenModified) {
        if (state != ArenaState.CONFIGURING){
            throw new IllegalStateException("Arena must be in configure mode to be edited.");
        }
        this.hasBeenModified = hasBeenModified;
    }

    public ArenaState getState() {
        return state;
    }

    public void setState(ArenaState newState) {
        if (newState == ArenaState.ENABLED && !isValid()){
            this.state = ArenaState.ERRORED;
        }
        if (newState == ArenaState.ENABLED && hasBeenModified){
            saveArenaDatFile();
            //handle the saving using the dependency here.
        }
        this.state = newState;
    }

    private void regenArena(){
        //call the other plugin here
        //this can be done synchronously, as the lag isn't too bad
    }

    public List<Location> getSpawnPoints() {
        return spawnPoints;
    }

    public void addSpawnPoint(Location location, boolean forceLookAtCentre){
        if (!isInBounds(location)) throw new IllegalStateException("New spawn must be inside the arena.");
        this.spawnPoints.add(location);
    }

    public Location getClosestSpawn(Location to){
        //todo - loop over comparing distance
        throw new UnsupportedOperationException("This feature has not yet been added.");
    }

    public ChestPoint getChestPoint(Location location){
        return chests.stream().filter(chestPoint -> chestPoint.getLocation().equals(location)).findAny().orElse(null);
    }

    public void addChestPoint(ChestPoint cp){
        if (!chests.contains(cp))
            chests.add(cp);
    }

    public void removeChestPoint(ChestPoint cp){
        chests.remove(cp);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new TreeMap<>();
        map.put("name", this.name);
        map.put("state", this.state.toString());
        map.put("centre", this.centre);
        map.put("corner_min", this.corner1);
        map.put("corner_max", this.corner2);
        map.put("chest_spots", this.chests);
        map.put("spawn_points", this.spawnPoints);
        map.put("loottable", this.lootTable.getName());
        return map;
    }

    public boolean isInBounds(Location location){
        return lowest[0] <= location.getX() && location.getX() <= highest[0] &&
                lowest[1] <= location.getY() && location.getY() <= highest[1] &&
                lowest[2] <= location.getZ() && location.getZ() <= highest[2];
    }

    private void updateBounds(){
        this.lowest = new double[]{Math.min(this.getCorner1().getX(), this.getCorner2().getX()),
                Math.min(this.getCorner1().getY(), this.getCorner2().getY()),
                Math.min(this.getCorner1().getZ(), this.getCorner2().getZ())};
        this.highest= new double[]{Math.max(this.getCorner1().getX(), this.getCorner2().getX()),
                Math.max(this.getCorner1().getY(), this.getCorner2().getY()),
                Math.max(this.getCorner1().getZ(), this.getCorner2().getZ())};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return name.equals(arena.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static Arena deserialize(Map<String, Object> map){
        return new Arena(map);
    }


    public static enum ArenaState{
        CONFIGURING,
        DISABLED,
        ERRORED,
        ENABLED;
    }
}


