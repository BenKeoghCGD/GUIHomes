package uk.co.benkeoghcgd.api.GUIHomes.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusCommand;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;
import uk.co.benkeoghcgd.api.GUIHomes.Data.HomesYML;
import uk.co.benkeoghcgd.api.GUIHomes.Data.MessagesYML;
import uk.co.benkeoghcgd.api.GUIHomes.GUIHomes;
import uk.co.benkeoghcgd.api.GUIHomes.GUIs.HomesGUI;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class homeCommand extends AxiusCommand {

    private final HomesYML homesYML;
    private final HashMap<String, Object> messageData;
    GUIHomes instance;

    public homeCommand(GUIHomes instance, HomesYML homeData) {
        super(instance, false,
                "home",
                "Command to view and teleport to homes",
                instance.getNameFormatted() + " " + instance.myml.data.get("Commands.Home.Syntax"),
                "h", "homes");

        this.instance = instance;
        messageData = instance.myml.data;
        homesYML = homeData;
        setPermission("guihomes.home");
    }

    @Override
    public boolean onCommand(CommandSender sndr, Command command, String label, String[] args) {
        List<String> homes;
        // /homes
        if(args.length == 0) {
            homes = homesYML.getPlayerHomes((Player)sndr);

            if(homes.isEmpty()) {
                sndr.sendMessage(plugin.getNameFormatted() + " " + MessagesYML.translateColor(messageData.get("Commands.Home.NoHomes")));
                return true;
            }

            HomesGUI gui = new HomesGUI(plugin, homesYML, (Player)sndr, (Player)sndr, GUIAssets.getInventoryRows(homes.size()), instance.myml);
            gui.show((Player) sndr);

            return true;
        }

        // /homes [player | homeName]
        if(args.length == 1) {
            OfflinePlayer p = Bukkit.getPlayer(args[0]);
            if(Objects.requireNonNull(p).hasPlayedBefore()) {
                if(!sndr.hasPermission("guihomes.other")) return false;

                homes = homesYML.getPlayerHomes(p);
                if(homes.isEmpty()) {
                    sndr.sendMessage(plugin.getNameFormatted() + " " + MessagesYML.translateColor(messageData.get("Commands.Home.NoPlayer")));
                    return true;
                }

                HomesGUI gui = new HomesGUI(plugin, homesYML, (Player)sndr, p, GUIAssets.getInventoryRows(homes.size()), instance.myml);
                gui.show((Player) sndr);
                sndr.sendMessage(plugin.getNameFormatted() + " " +
                        MessagesYML.translateColor(messageData.get("Commands.Home.Other")).replaceAll("%PLAYER%", args[0]));
                return true;
            }

            homes = homesYML.getPlayerHomes((Player)sndr);

            if(homes.isEmpty()) {
                sndr.sendMessage(plugin.getNameFormatted() + " " + MessagesYML.translateColor(messageData.get("Commands.Home.NoHomes")));
                return true;
            }

            for(String s : homes) {
                String[] parts = s.split(";");
                if(parts[0].equalsIgnoreCase(args[0])) {
                    Location loc = HomesYML.stringToLocation(parts[1]);
                    ((Player)sndr).teleport(Objects.requireNonNull(loc));
                    sndr.sendMessage(plugin.getNameFormatted() + " " + MessagesYML.translateColor(messageData.get("Teleport")).replaceAll("%HOME%", args[0]));
                    return true;
                }
            }

            sndr.sendMessage(plugin.getNameFormatted() + " " + MessagesYML.translateColor(messageData.get("Commands.Home.NoHomeSpecified")));
            return true;
        }

        if(args.length == 2) {
            if(!sndr.hasPermission("guihomes.other")) return false;
            OfflinePlayer p = Bukkit.getPlayer(args[0]);

            if(!Objects.requireNonNull(p).hasPlayedBefore())
                sndr.sendMessage(plugin.getNameFormatted() + " " + MessagesYML.translateColor(messageData.get("Commands.Home.NoPlayer")));

            homes = homesYML.getPlayerHomes(p);

            for(String s : homes) {
                String[] parts = s.split(";");
                if(parts[1].equalsIgnoreCase(args[1])) {
                    Location loc = HomesYML.stringToLocation(parts[1]);
                    assert loc != null;
                    ((Player)sndr).teleport(loc);
                    sndr.sendMessage(plugin.getNameFormatted() + " " + MessagesYML.translateColor(messageData.get("Teleport")).replaceAll("%HOME%", args[1]));
                    return true;
                }
            }
        }

        return false;
    }
}
