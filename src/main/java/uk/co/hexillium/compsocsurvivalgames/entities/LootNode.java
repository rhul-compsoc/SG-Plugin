package uk.co.hexillium.compsocsurvivalgames.entities;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("SGLootNode")
public class LootNode implements ConfigurationSerializable {

    Material material;
    int maxCount, minCount;
    double weight;

    private LootNode(Map<String, Object> map){
        this.material = Material.matchMaterial((String) map.get("material"));
        this.maxCount = (int) map.get("max");
        this.minCount = (int) map.get("min");
        this.weight = (double) map.get("weight");

    }

    public LootNode(Material material, int minCount, int maxCount, double weight) {
        this.material = material;
        this.maxCount = Math.max(maxCount, minCount);
        this.minCount = Math.min(maxCount, minCount);
        this.weight = weight;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getMinCount() {
        return minCount;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("material", material.toString());
        map.put("max", maxCount);
        map.put("min", minCount);
        map.put("weight", weight);
        return map;
    }

    public static LootNode deserialize(Map<String, Object> map){
        return new LootNode(map);
    }
}
