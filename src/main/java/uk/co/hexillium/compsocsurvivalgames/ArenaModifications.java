package uk.co.hexillium.compsocsurvivalgames;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import uk.co.hexillium.compsocsurvivalgames.entities.Arena;
import uk.co.hexillium.compsocsurvivalgames.entities.ChestPoint;

public class ArenaModifications implements Listener {

    SurvivalGames sg;

    public ArenaModifications(SurvivalGames sg){
        this.sg = sg;
    }

    /**
     * Checks to make sure that the Player in question is inside an arena
     * @param event the
     * @return the arena if they're in one, else null
     */
    private Arena ensureInArena(Location l, Player p, Cancellable event){
        Arena arena = findArena(l);
        if (arena == null){
            if (sg.isUserInEditMode(p)) p.sendMessage(ChatColor.RED + "[SG] You're in edit mode, but we can't find an arena here.");
            return null;
        } else {
            return arena;
        }
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Arena arena = ensureInArena(event.getBlock().getLocation(), event.getPlayer(), event);
        if (arena == null) return;
        if (arena.getState().equals(Arena.ArenaState.ENABLED)) return; //we can handle this in the GameLogic class; this class is only for the out-of-game edits.
        if (!sg.isUserInEditMode(event.getPlayer())){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You are trying to modify an arena that is in the " + ChatColor.AQUA + arena.getState() + ChatColor.RED + " state, but you are not in edit mode.");
            return;
        }
        if (!arena.getState().equals(Arena.ArenaState.CONFIGURING)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "This arena is currently in the " + ChatColor.AQUA + arena.getState() + ChatColor.RED + " state, and needs to be in CONFIGURE mode to be edited.");
            return;
        }
        arena.setHasBeenModified(true);
        ChestPoint cp = arena.getChestPoint(event.getBlock().getLocation());
        if (cp != null){
            arena.removeChestPoint(cp);
            event.getPlayer().sendMessage(ChatColor.GOLD + "You have just broken a tier " + cp.getTier() + " chest spawn point");
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Arena arena = ensureInArena(event.getBlock().getLocation(), event.getPlayer(), event);
        if (arena == null) return;
        if (arena.getState().equals(Arena.ArenaState.ENABLED)) return; //we can handle this in the GameLogic class; this class is only for the out-of-game edits.
        if (!sg.isUserInEditMode(event.getPlayer())){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You are trying to modify an arena that is in the " + ChatColor.AQUA + arena.getState() + ChatColor.RED + " state, but you are not in edit mode.");
            return;
        }
        if (!arena.getState().equals(Arena.ArenaState.CONFIGURING)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "This arena is currently in the " + ChatColor.AQUA + arena.getState() + ChatColor.RED + " state, and needs to be in CONFIGURE mode to be edited.");
            return;
        }
        arena.setHasBeenModified(true);
        int tierNum = sg.getConfigManager().getTierFromMaterial(event.getBlockPlaced().getType());
        if (tierNum >= 0){
            arena.addChestPoint(new ChestPoint(event.getBlock().getLocation(), tierNum == 0 ? 1 : 0.6, tierNum));
            event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "You just added a tier " + tierNum + " chest to the arena.");
        }
    }

    private Arena findArena(Location location){
        for (Arena arena : sg.getArenas()){
            if (arena.isInBounds(location)) return arena;
        }
        return null;
    }

}
