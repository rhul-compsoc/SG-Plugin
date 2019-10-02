package uk.co.hexillium.compsocsurvivalgames.entities;

import org.bukkit.entity.Player;

import java.util.List;

public class Game {

    private Arena arena;
    private GameState state;

    private List<Player> players;
    private List<Spectator> spectators;

    private long timeStarted;

    public Game(Arena arena){

    }

    public enum GameState{
        DISABLED,
        RESTARTING,
        LOBBY,
        COUNTDOWN,
        INGAME,
        ENDING
    }

}
