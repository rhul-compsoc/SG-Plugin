package uk.co.hexillium.compsocsurvivalgames.entities;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("SGChestPoint")
public class ChestPoint implements ConfigurationSerializable {

    private Location location;
    private double chance;
    private int tier;

    private ChestPoint(Map<String, Object> map){
        this.location = (Location) map.get("location");
        this.chance = (double) map.get("chance");
        this.tier = (int) map.get("tier");
    }

    public ChestPoint(Location location, double chance, int tier) {
        this.location = location;
        this.chance = chance;
        this.tier = tier;
    }

    public Location getLocation() {
        return location;
    }

    public double getChance() {
        return chance;
    }

    public int getTier() {
        return tier;
    }

    /**
     * reconstruct the class from the attributes of the config
     * @param map map from the config
     * @return a new object with the attributes from it
     */
    public static ChestPoint deserialize(Map<String, Object> map){
        return new ChestPoint(map);
    }

    /**
     * Required for the interface ConfigurationSerializable
     * @return list of config attributes
     */
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("location", location);
        map.put("chance", chance);
        map.put("tier", tier);
        return map;
    }
}
