package uk.co.benkeoghcgd.api.GUIHomes.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusCommand;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.GUIHomes.GUIHomes;

import java.util.List;

public class setHomeCommand extends AxiusCommand {

    private GUIHomes plug;

    public setHomeCommand(AxiusPlugin instance) {
        super(instance, false,
                "sethome",
                "Command used to set new homes.",
                instance.getNameFormatted() + "§7Incorrect Syntax: /sethome <name>",
                "sh", "seth", "sethomes");
        plug = (GUIHomes) instance;
        setPermission("guihomes.sethome");
    }

    @Override
    public boolean onCommand(CommandSender sndr, Command command, String s, String[] args) {
        List<String> homes = plug.hyml.getPlayerHomes((Player)sndr);
        if(args.length != 1) {
            sndr.sendMessage(getPermissionMessage());
            return true;
        }

        int maxHomes = (Integer) plug.cyml.data.get("defaults.maxHomes");

        if(sndr.hasPermission("guihomes.homes.unlimited")) maxHomes = 54;
        else {
            for(PermissionAttachmentInfo pai : sndr.getEffectivePermissions()) {
                if(pai.getPermission().startsWith("guihomes.homes.")) {
                    int possible = Integer.parseInt(pai.getPermission().substring("guihomes.homes.".length()));
                    if(possible > maxHomes) maxHomes = possible;
                }
            }
        }

        if(homes.size() >= maxHomes) {
            sndr.sendMessage(plug.getNameFormatted() + "§7 You have reached maximum homes!");
            return true;
        }

        plug.hyml.addHome((Player) sndr, ((Player) sndr).getLocation(), args[0].toUpperCase());

        sndr.sendMessage(plug.getNameFormatted() + "§7 Home has been set.");
        plug.hyml.refresh();
        return true;
    }
}
