package uk.co.hexillium.compsocsurvivalgames;

import org.bukkit.event.Listener;

public class GameLogic implements Listener {

    SurvivalGames sg;
    GameManager gm;

    public GameLogic(SurvivalGames sg, GameManager gm){
        this.sg = sg;
        this.gm = gm;
    }

}
