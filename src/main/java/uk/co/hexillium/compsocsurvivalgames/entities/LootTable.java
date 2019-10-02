package uk.co.hexillium.compsocsurvivalgames.entities;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@SerializableAs("SGLootTable")
public class LootTable implements ConfigurationSerializable {

    static volatile Random random = new Random();


    String name;

    List<LootNode> tierZero;
    List<LootNode> tierOne;
    List<LootNode> tierTwo;

    transient double tierZeroWeight;
    transient double tierOneWeight;
    transient double tierTwoWeight;

    transient double totalWeight;

    private LootTable(Map<String, Object> map){
        this.name = (String) map.get("name");
        this.tierZero = (List<LootNode>) map.get("tierZeroLoot");
        this.tierOne = (List<LootNode>) map.get("tierOneLoot");
        this.tierTwo = (List<LootNode>) map.get("tierTwoLoot");
    }

    public LootTable(String name){
        this(name, new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>());
    }

    public LootTable(String name, List<LootNode> tierZero,
                     List<LootNode> tierOne, List<LootNode> tierTwo){
        this.name = name;
        this.tierZero = tierZero;
        this.tierOne = tierOne;
        this.tierTwo = tierTwo;
    }

    public void addAll(List<LootNode> nodes, int tier){
        switch (tier){
            case 0:
                tierZero.addAll(nodes);
                break;
            case 1:
                tierOne.addAll(nodes);
                break;
            case 2:
                tierTwo.addAll(nodes);
                break;
            default:
                throw new IllegalArgumentException("Tier must be 0, 1 or 2.");
        }
    }

    public void addNode(LootNode node, int tier){
        switch (tier){
            case 0:
                tierZero.add(node);
                break;
            case 1:
                tierOne.add(node);
                break;
            case 2:
                tierTwo.add(node);
                break;
            default:
                throw new IllegalArgumentException("Tier must be 0, 1 or 2.");
        }
    }

    public void recalculateTotalWeights(){
        tierZeroWeight = 0;
        tierOneWeight = 0;
        tierTwoWeight = 0;
        tierZero.forEach(ln -> tierZeroWeight += ln.getWeight());
        tierOne.forEach(ln -> tierOneWeight += ln.getWeight());
        tierTwo.forEach(ln -> tierTwoWeight += ln.getWeight());
    }

    public ItemStack generateLoot(int tier){
        switch (tier){
            case 0:
                return genLoot(tierZero, tierZeroWeight);
            case 1:
                return genLoot(tierOne, tierOneWeight);
            case 2:
                return genLoot(tierTwo, tierTwoWeight);
            default:
                throw new IllegalArgumentException("Tier can only be 0, 1 or 2");
        }
    }

    private ItemStack genLoot(List<LootNode> lootTable, double totalWeight){
        final double threshold = random.nextDouble() * Math.abs(totalWeight);
        double rollingCount = 0;
        for (LootNode node : lootTable){
            if (rollingCount > threshold){
                return new ItemStack(node.material,
                        getRandIncl(node.getMinCount(), node.getMaxCount()));
            }
            rollingCount += node.getWeight();
        }
        if (totalWeight < 0){
            throw new IllegalStateException("Cannot generate loot");
        }
        recalculateTotalWeights();
        return genLoot(lootTable, -totalWeight);
    }

    private int getRandIncl(int min, int max){
        return (random.nextInt((max - min) + 1)) - min;
    }

    public static LootTable deserialize(Map<String, Object> map){
        return new LootTable(map);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        map.put("tierZeroLoot", tierZero);
        map.put("tierOneLoot", tierOne);
        map.put("tierTwoLoot", tierTwo);
        return map;
    }

    public String getName() {
        return name;
    }
}
