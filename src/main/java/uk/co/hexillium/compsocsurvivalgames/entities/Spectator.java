package uk.co.hexillium.compsocsurvivalgames.entities;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.io.Serializable;

public class Spectator extends Participant {

    public Spectator(Player player, Game game){
        super(player, game);
    }

    @Override
    public void updateGameMode(){
        player.setGameMode(GameMode.SPECTATOR);
    }


}
