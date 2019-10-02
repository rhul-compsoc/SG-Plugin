package uk.co.hexillium.compsocsurvivalgames.entities;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SerializableAs("SGArena")
public class Arena implements ConfigurationSerializable {


    private String name;
    private Location centre;

    private Location min;
    private Location max;

    private List<ChestPoint> chests;
    private transient LootTable lootTable;

    private transient ArenaState state;


    public Arena(String name){
        this.name = name;
        this.chests = new ArrayList<>();
        this.state = ArenaState.CONFIGURING;
    }

    public boolean isValid(){
        return (!name.isEmpty()) &&
                centre != null &&
                min != null &&
                max != null &&
                !chests.isEmpty() &&
                lootTable != null;
    }

    public List<ChestPoint> getChests() {
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

    public Location getMin() {
        return min;
    }

    public void setMin(Location min) {
        if (state != ArenaState.CONFIGURING)
            throw new IllegalStateException("Arena must be in configure mode.");
        this.min = min;
    }

    public Location getMax() {
        return max;
    }

    public void setMax(Location max) {
        if (state != ArenaState.CONFIGURING)
            throw new IllegalStateException("Arena must be in configure mode.");
        this.max = max;
    }

    public LootTable getLootTable() {
        return lootTable;
    }

    public void setLootTable(LootTable lootTable) {
        if (state != ArenaState.CONFIGURING)
            throw new IllegalStateException("Arena must be in configure mode.");
        this.lootTable = lootTable;
    }

    public ArenaState getState() {
        return state;
    }

    public void setState(ArenaState newState) {
        if (newState == ArenaState.ENABLED && !isValid()){
            this.state = ArenaState.ERRORED;
        }
    }

    private void regenArena(){
        //call the other plugin here
        //this can be done synchronously, as the lag isn't too bad
    }

    private Arena(Map<String, Object> map){
        this.centre = (Location) map.get("centre");
        this.name = (String) map.get("name");
        this.chests = (List<ChestPoint>) map.get("chest_spots");
        this.min = (Location) map.get("corner_min");
        this.max = (Location) map.get("corner_max");
        this.state = ArenaState.ENABLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new TreeMap<>();
        map.put("name", this.name);
        map.put("centre", this.centre);
        map.put("corner_min", this.min);
        map.put("corner_max", this.max);
        map.put("chest_spots", this.chests);
        return map;
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


