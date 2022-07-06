package uk.co.benkeoghcgd.api.GUIHomes.Commands;

import org.bukkit.Bukkit;
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
    public boolean onCommand(CommandSender sndr, Command command, String s, String[] args) {

        if(args.length == 1) {
            if(!sndr.hasPermission("guihomes.other")) return false;

            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);

            List<String> homes = homesYML.getPlayerHomes(p);

            if(homes.isEmpty()) {
                sndr.sendMessage(plugin.getNameFormatted() + "§7 That player does not exist, or has no homes set.");
                return true;
            }

            HomesGUI gui = new HomesGUI(plugin, homesYML, (Player)sndr, p, GUIAssets.getInventoryRows(homes.size()));
            gui.show((Player) sndr);

            return true;
        }

        List<String> homes = homesYML.getPlayerHomes((Player)sndr);

        if(homes.isEmpty()) {
            sndr.sendMessage(plugin.getNameFormatted() + "§7 You don't have any homes set.");
            return true;
        }

        HomesGUI gui = new HomesGUI(plugin, homesYML, (Player)sndr, (Player)sndr, GUIAssets.getInventoryRows(homes.size()));
        gui.show((Player) sndr);

        return true;
    }
}
