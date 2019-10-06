package uk.co.hexillium.compsocsurvivalgames.entities;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public abstract class Participant{

    protected transient Player player;
    protected transient Game game;

    protected ItemStack[] oldItems;
    protected ItemStack[] oldArmour;
    protected Location oldLocation;
    protected Collection<PotionEffect> oldEffects;
    protected GameMode oldGameMode;

    Participant(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.oldGameMode = player.getGameMode();
        this.oldEffects = player.getActivePotionEffects();
        this.oldLocation = player.getLocation();
        oldItems = player.getInventory().getContents();
        oldArmour = player.getInventory().getArmorContents();
    }

    abstract void updateGameMode();

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    /**
     * Teleport the player back, restore their inventory, restore their game mode and restore their potion effects
     */
    public void destroyParticipant(){
        rollbackInventory();
        rollbackLocation();
        rollbackPotionEffects();
        rollbackPotionEffects();
    }

    public void rollbackInventory(){
        player.getInventory().setContents(oldItems);
        player.getInventory().setArmorContents(oldArmour);
    }

    public void rollbackPotionEffects(){
        for (PotionEffect pe : player.getActivePotionEffects()){
            player.removePotionEffect(pe.getType());
        }
        player.addPotionEffects(oldEffects);
    }

    public void rollbackLocation(){
        player.teleport(oldLocation);
    }

    public void rollbackGameMode(){
        player.setGameMode(oldGameMode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }
}
