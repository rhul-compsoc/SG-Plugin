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
        this.arena = arena;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Arena getArena() {
        return arena;
    }

    public GameState getState() {
        return state;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Spectator> getSpectators() {
        return spectators;
    }

    public long getTimeStarted() {
        return timeStarted;
    }

    public static enum GameState{
        DISABLED,
        RESTARTING,
        LOBBY,
        COUNTDOWN,
        INGAME,
        ENDING
    }


}
