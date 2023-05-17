package uk.co.benkeoghcgd.api.GUIHomes.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusCommand;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;
import uk.co.benkeoghcgd.api.GUIHomes.Data.HomesYML;
import uk.co.benkeoghcgd.api.GUIHomes.GUIs.HomesGUI;

import java.util.ArrayList;
import java.util.List;

public class homeCommand extends AxiusCommand {

    private HomesYML homesYML;

    public homeCommand(AxiusPlugin instance, HomesYML homeData) {
        super(instance, false,
                "home",
                "Command to view and teleport to homes",
                instance.getNameFormatted() + "§7 Incorrect Syntax: /home",
                "h", "homes");

        homesYML = homeData;
        setPermission("guihomes.home");
    }

    @Override
    public boolean onCommand(CommandSender sndr, Command command, String label, String[] args) {
        List<String> homes = new ArrayList<>();
        // /homes
        if(args.length == 0) {
            homes = homesYML.getPlayerHomes((Player)sndr);

            if(homes.isEmpty()) {
                sndr.sendMessage(plugin.getNameFormatted() + "§7 You don't have any homes set.");
                return true;
            }

            HomesGUI gui = new HomesGUI(plugin, homesYML, (Player)sndr, (Player)sndr, GUIAssets.getInventoryRows(homes.size()));
            gui.show((Player) sndr);

            return true;
        }

        // /homes [player | homeName]
        if(args.length == 1) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
            if(p.hasPlayedBefore()) {
                if(!sndr.hasPermission("guihomes.other")) return false;

                homes = homesYML.getPlayerHomes(p);
                if(homes.isEmpty()) {
                    sndr.sendMessage(plugin.getNameFormatted() + "§7 That player does not exist, or has no homes set.");
                    return true;
                }

                HomesGUI gui = new HomesGUI(plugin, homesYML, (Player)sndr, p, GUIAssets.getInventoryRows(homes.size()));
                gui.show((Player) sndr);
                sndr.sendMessage(plugin.getNameFormatted() + "§7 Showing homes for player:§3 " + args[0] + ".");
                return true;
            }

            homes = homesYML.getPlayerHomes((Player)sndr);

            if(homes.isEmpty()) {
                sndr.sendMessage(plugin.getNameFormatted() + "§7 You don't have any homes set.");
                return true;
            }

            for(String s : homes) {
                String[] parts = s.split(";");
                if(parts[0].equalsIgnoreCase(args[0])) {
                    Location loc = HomesYML.stringToLocation(parts[1]);
                    ((Player)sndr).teleport(loc);
                    sndr.sendMessage(plugin.getNameFormatted() + "§7 Teleported to home: §3" + args[0] + "§7.");
                    return true;
                }
            }

            sndr.sendMessage(plugin.getNameFormatted() + "§7 You don't have a home set by that name.");
            return true;
        }

        if(args.length == 2) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);

            if(!p.hasPlayedBefore())
                sndr.sendMessage(plugin.getNameFormatted() + "§7 That player does not exist, or has no homes set.");

            homes = homesYML.getPlayerHomes(p);

            for(String s : homes) {
                String[] parts = s.split(";");
                if(parts[1].equalsIgnoreCase(args[1])) {
                    Location loc = HomesYML.stringToLocation(parts[1]);
                    ((Player)sndr).teleport(loc);
                    sndr.sendMessage(plugin.getNameFormatted() + "§7 Teleported to home: §3" + args[1] + "§7.");
                    return true;
                }
            }
        }

        return false;
    }
}
