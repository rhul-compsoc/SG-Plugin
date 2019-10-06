package uk.co.hexillium.compsocsurvivalgames.entities;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

/**
 * A player that is a member of a game, and is not spectating; ie, a fighter of the game
 */
public class Fighter extends Participant{

    private int kills;

    public Fighter(Player player, Game game){
        super(player, game);
    }

    @Override
    void updateGameMode() {
        player.setGameMode(GameMode.SURVIVAL);
    }



}
