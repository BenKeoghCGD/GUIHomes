package uk.co.benkeoghcgd.api.GUIHomes.Data;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlayer;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.DataHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class MessagesYML extends DataHandler {

    public MessagesYML(AxiusPlugin instance) {
        super(instance, "Language");
    }

    @Override
    protected void saveDefaults() {
        setData("Commands.Home.Syntax", "&7Incorrect Syntax: /home");
        setData("Commands.Home.NoHomes", "&7You don't have any homes set.");
        setData("Commands.Home.NoHomeSpecified", "&7You don't have a home set by that name.");
        setData("Commands.Home.NoPlayer", "&7That player does not exist, or has no homes set.");
        setData("Commands.Home.Other", "&7Showing homes for player:&3 %PLAYER%.");
        setData("Teleport", "&7Teleporting to home: &3%HOME%.");
        setData("GUI.Homes.Title.Own", "&7Your homes.");
        setData("GUI.Homes.Title.Other", "&7Homes for: &3%PLAYER%");
        setData("GUI.Edit.Title", "&7Editing home: &3%HOME%");
        setData("GUI.Delete.Title", "&c&lAre you sure?");
        setData("GUI.Homes.Icons.Name", "&3&l%HOME%");
        List<String> a = new ArrayList<>();
        a.add("&7X: %HOME_X%");
        a.add("&7Y: %HOME_Y%");
        a.add("&7Z: %HOME_Z%");
        a.add("");
        a.add("&aLeft-Click to Teleport");
        a.add("&6SHIFT-Left-Click to Edit Block");
        a.add("&cLeft-Click to Delete");
        setData("GUI.Homes.Icons.Lore", a);
    }

    public static String translateColor(String key) {
        return ChatColor.translateAlternateColorCodes('&', key);
    }

    public static String translateColor(Object key) {
        return translateColor((String)key);
    }

    public static List<String> translateColors(List<String> key) {
        List<String> newStrings = new ArrayList<>();
        for (String val : key) {
            newStrings.add(translateColor(val));
        }
        return newStrings;
    }

    public static List<String> Syntax(List<String> key, Location loc, AxiusPlayer player) {
        IntStream.range(0, key.size()).forEach(i -> key.set(i,
                key.get(i).replaceAll("%HOME_X%", player.isStreamer() ? "**.**" : String.valueOf(loc.getBlockX()))
                          .replaceAll("%HOME_Y%", player.isStreamer() ? "**.**" : String.valueOf(loc.getBlockY()))
                          .replaceAll("%HOME_Z%", player.isStreamer() ? "**.**" : String.valueOf(loc.getBlockZ()))));
        return key;
    }
}
