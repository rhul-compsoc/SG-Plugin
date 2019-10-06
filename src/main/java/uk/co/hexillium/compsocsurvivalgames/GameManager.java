package uk.co.hexillium.compsocsurvivalgames;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import uk.co.hexillium.compsocsurvivalgames.entities.Arena;
import uk.co.hexillium.compsocsurvivalgames.entities.ChestPoint;
import uk.co.hexillium.compsocsurvivalgames.entities.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {

    private SurvivalGames sg;
    private List<Game> games;
    private static Random random = new Random();

    public GameManager(SurvivalGames sg){
        games = new ArrayList<>();
        this.sg = sg;
    }

    public Game initGame(Arena arena){
        if (arenaIsInUse(arena)) throw new IllegalStateException("Already a game running in that arena.");
        Game game = new Game(arena);
        game.setState(Game.GameState.LOBBY);
        genLoot(arena);
        //games.add(game);
        return game;
    }

    private void genLoot(Arena arena){
        for (ChestPoint cp : arena.getChestPoints()){
            if (cp.getChance() > random.nextDouble()){
                //do spawn it
                cp.getLocation().getBlock().setType(sg.getConfigManager().getMaterialFromTier(cp.getTier()));
                ShulkerBox box = ((ShulkerBox) cp.getLocation().getBlock().getState());

                ItemStack[] inv = new ItemStack[27];
                for (int i = 0; i < 27; i++){
                    if (random.nextDouble() > 0.7) {
                        inv[i] = arena.getLootTable().generateLoot(cp.getTier());
                        //System.out.println("Generated " + inv[i].getType() + "x" + inv[i].getAmount());
                    } else {
                        inv[i] = new ItemStack(Material.AIR);
                    }
                }
                box.setCustomName("Tier " + cp.getTier() + " Chest.");
                box.update();
                box.getInventory().setContents(inv);

            } else {
                cp.getLocation().getBlock().setType(Material.AIR);
            }
        }
    }

    public boolean arenaIsInUse(Arena arena){
        return games.stream().anyMatch(game -> game.getArena().equals(arena));
    }

}
