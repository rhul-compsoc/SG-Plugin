package uk.co.hexillium.compsocsurvivalgames;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import uk.co.hexillium.compsocsurvivalgames.entities.Arena;
import uk.co.hexillium.compsocsurvivalgames.entities.LootTable;

import javax.swing.*;

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
                if (args.length == 9){
                    String name = args[2];
                    if (sg.arenaExistsWithName(name)){  //stop them making an arena with the same name
                        sender.sendMessage(ChatColor.RED + "An arena with the name " + ChatColor.AQUA + " already exists.");
                        break;
                    }
                    int[] coords = processCoordinateArgs(args, 3, 6);
                    if (coords == null){
                        sender.sendMessage(ChatColor.RED + "Must have a name then two pairs of coordinates");
                        break;
                    }
                    if (!(sender instanceof Player)){
                        sender.sendMessage("Needs to be sent from a player.");
                        break;
                    }
                    Player player = (Player) sender;
                    if (!checkCoords(coords)){
                        sender.sendMessage(ChatColor.RED + "There may have been a mistake in the coordinates.");
                        break;
                    }
                    Location[] corners = extractLocation(coords, player.getWorld());
                    Arena arena = new Arena(name, corners[0], corners[1]);
                    sg.addArena(arena);
                    sender.sendMessage(ChatColor.GREEN + "Success, arena " + name + " created.");
                    break;
                } else {
                    sender.sendMessage(ChatColor.AQUA + "Arena create" + ChatColor.RED + " requires exactly seven arguments of a " + ChatColor.AQUA + " name " + ChatColor.RED + "and two pairs of co-ordinates.");
                    break;
                }
            case "modify":
                //check for arena name arg and a modification
                //check to see if the arena is in configure mode
                //[addspawn, bindloottable, definemax, definemin, recenter, chesthelp, highlightchests]
                if (args.length < 4){
                    sender.sendMessage(ChatColor.RED + "Must specify an arena and sub-command from " + ChatColor.DARK_RED + "[addspawn, bindloottable, definemax, definemin, recentre, chesthelp, highlightchests]");
                    sender.sendMessage(ChatColor.RED + "/sg arena modify <arenaname> <subcommand> [args...]");
                    break;
                }
                String arenaName = args[2].toLowerCase();
                Arena arena = sg.getArenaByName(arenaName);
                if (arena == null){
                    sender.sendMessage(ChatColor.RED + "Could not find arena by name " + arenaName);
                    return true;
                }
                boolean hasLocation = sender instanceof Entity || sender instanceof BlockCommandSender;
                Location from = null;
                if (sender instanceof BlockCommandSender) {
                    from = ((BlockCommandSender) sender).getBlock().getLocation();
                }
                if (sender instanceof Entity){
                    from = ((Entity) sender).getLocation();
                }
                String arg = args[3];
                switch (arg){
                    case "addspawn":
                        if (!hasLocation){
                            sender.sendMessage(ChatColor.RED + "This command requires a location, please run from a player.");
                            break;
                        }
                        if (arena.isInBounds(from)){
                            arena.addSpawnPoint(from, true);
                            sender.sendMessage(ChatColor.GREEN + "Success. Added spawnpoint #" + arena.getSpawnPoints().size() + " to arena " + arena.getName());
                        } else {
                            sender.sendMessage(ChatColor.RED + "The new spawn point needs to be inside the specified arena.");
                        }
                        break;
                    case "bindloottable":
                        if (args.length < 5){
                            sender.sendMessage("Need to specify a loot table to bind to this arena.  Remember that one loot table can be assigned to any number of arenas, but each arena may only have one loot table.");
                            break;
                        }
                        String tableName = args[4].toLowerCase();
                        LootTable lootTable = sg.getLootTableByName(tableName);
                        if (lootTable == null){
                            sender.sendMessage(ChatColor.RED + "Could not find the specified loot table name.");
                            break;
                        }
                        arena.setLootTable(lootTable);
                        sender.sendMessage(ChatColor.GREEN + "Successfully bound " + lootTable.getName() + " to " + arena.getName());

                        break;
                    case "definemax":
                        //todo
                        break;
                    case "definemin":
                        //todo
                        break;
                    case "recenter":
                    case "recentre":
                        //todo
                        break;
                    case "chesthelp":
                        //todo
                        break;
                    case "highlightchests":
                        //todo
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "Must specify an arena and sub-command from " + ChatColor.DARK_RED + "[addspawn, bindloottable, definemax, definemin, recentre, chesthelp, highlightchests]");
                        sender.sendMessage(ChatColor.RED + "/sg arena modify <arenaname> <subcommand> [args...]");
                        break;
                }
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


    private boolean checkCoords(int[] coords){
        if (coords[1] < 0 || coords[4] < 0 || coords[1] > 256 || coords[4] > 256){
            return false;
        }
        return true;
    }

    private Location[] extractLocation(int[] coords, World world){
        Location[] locs = new Location[2];
        locs[0] = new Location(world, coords[0], coords[1], coords[2]);
        locs[1] = new Location(world, coords[3], coords[4], coords[5]);
        return locs;
    }

    private int[] processCoordinateArgs(String[] args, int from, int len){
        int[] res = new int[len];
        for (int i = 0; i < len; i++){
            try {
                res[i] = Integer.parseInt(args[from + i]);
            } catch (NumberFormatException ex){
                return null;
            }
        }
        return res;
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
                if (args.length < 3){
                    sender.sendMessage(ChatColor.RED + "Need to specify a subcommand from [start, forcestart, test, stop, info], followed by an arena name.");
                    return true;
                }
                String subcommand = args[1];
                String arenaName = args[2];
                if (!sg.arenaExistsWithName(arenaName)){
                    sender.sendMessage(ChatColor.RED + "Could not find that arena.");
                    return true;
                }
                Arena arena = sg.getArenaByName(arenaName);
                switch (subcommand){
                    case "test":
                        sg.getGameManager().initGame(arena);
                        sender.sendMessage(ChatColor.DARK_BLUE + "Initialised.");
                }
                break;
            case "toggleedit":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This may only be used by players.");
                    return true;
                }
                Player player = (Player) sender;
                if (!player.hasPermission("survivalgames.admin")){
                    player.sendMessage(ChatColor.RED + "You do not have permission for this.");
                    return true;
                }
                if (!sg.addUserToEditMode(player)){
                    sg.removeUserFromEditMode(player);
                }
                player.sendMessage("Toggled edit mode to " + (sg.isUserInEditMode(player) ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
            default:
                return false;
        }
        return true;
    }
}
