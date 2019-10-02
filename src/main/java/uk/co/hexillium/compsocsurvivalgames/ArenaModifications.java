package uk.co.hexillium.compsocsurvivalgames;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import uk.co.hexillium.compsocsurvivalgames.entities.Arena;

public class ArenaModifications implements Listener {

    SurvivalGames sg;

    public ArenaModifications(SurvivalGames sg){
        this.sg = sg;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        //need to check to see if the player is in edit mode

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        //need to check to se if the player is in edit mode
        Arena arena = findArena(event.getBlock().getLocation());
        if (arena == null) return;
        if (arena.getState() == Arena.ArenaState.DISABLED){
            event.setCancelled(true);
        }

    }

    private Arena findArena(Location location){
        for (Arena arena : sg.getArenas()){
            if (isInBounds(location, arena)) return arena;
        }
        return null;
    }

    private boolean isInBounds(Location location, Arena arena){
        double[] lowest = {Math.min(arena.getMin().getX(), arena.getMax().getX()),
                            Math.min(arena.getMin().getY(), arena.getMax().getY()),
                            Math.min(arena.getMin().getZ(), arena.getMax().getZ())};
        double[] highest = {Math.max(arena.getMin().getX(), arena.getMax().getX()),
                            Math.max(arena.getMin().getY(), arena.getMax().getY()),
                            Math.max(arena.getMin().getZ(), arena.getMax().getZ())};
        return lowest[0] < location.getX() && location.getX() < highest[0] &&
                lowest[1] < location.getY() && location.getY() < highest[1] &&
                lowest[2] < location.getZ() && location.getZ() < highest[2];
    }

}
