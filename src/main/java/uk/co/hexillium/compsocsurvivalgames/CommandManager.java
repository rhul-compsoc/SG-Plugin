package uk.co.hexillium.compsocsurvivalgames;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import uk.co.hexillium.compsocsurvivalgames.entities.Arena;

public class CommandManager {

    SurvivalGames sg;

    public CommandManager(SurvivalGames sg){
        this.sg = sg;
    }

    /**
     * I'm lazy.
     * @param sender who to send the no perm message
     */
    private void sendNoPermMessage(CommandSender sender){
        sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
    }

    /**
     * Handle the Arena sub commands
     * @param sender the sender of the command
     * @param args the arguments of the command
     * @return true if success, false to print help for the subcommand
     */
    private boolean handleArena(CommandSender sender, String[] args){
        switch (args[1]){
            case "create":
                //check for arena name arg
                if (args.length == 3){
                    String name = args[2];
                    if (sg.arenaExistsWithName(name)){  //stop them making an arena with the same name
                        sender.sendMessage(ChatColor.RED + "An arena with the name " + ChatColor.AQUA + " already exists.");
                        return true;
                    }
                    sg.addArena(new Arena(name));
                } else {
                    sender.sendMessage(ChatColor.AQUA + "arena create" + ChatColor.RED + " requires exactly one argument of a " + ChatColor.AQUA + " name " + ChatColor.RED + "and will create an arena with that name.");
                }
                break;
            case "modify":
                //check for arena name arg and a modification
                //check to see if the arena is in configure mode
                //[addspawn, bindloottable, definemax, definemin, recenter, chesthelp, clearchests, highlightchests]
                break;
            case "info":
                //if no args, print some basic info like player slots,
                //how many chests of each tier there are etc
            case "delete":
                //check for arena name arg
                //check to see if the arena is in disabled mode
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Fire the command handler for the sg supercommand
     * @param sender sender of the command
     * @param args arguments of the command
     * @return true if success, false to print help.
     */
    public boolean fire(CommandSender sender, String[] args){
        switch (args[0]){
            case "join":
                //check arenaName arg
                break;
            case "leave":
                //check if they're in a game
                break;
            case "arena":
                if (!sender.hasPermission("survivalgames.admin")){
                    sendNoPermMessage(sender);
                    return false;
                }
                if (args.length == 1 || !handleArena(sender, args)){
                    sender.sendMessage(ChatColor.RED + "Arena subcommand requires more arguments. Try one of: [create, modify, delete]");
                }
                break;
            case "game":
                break;
            default:
                return false;
        }
        return true;
    }
}
