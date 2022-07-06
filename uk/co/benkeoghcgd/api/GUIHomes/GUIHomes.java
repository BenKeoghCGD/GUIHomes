package uk.co.benkeoghcgd.api.GUIHomes;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.VersionFormat;
import uk.co.benkeoghcgd.api.AxiusCore.API.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;
import uk.co.benkeoghcgd.api.GUIHomes.Commands.homeCommand;
import uk.co.benkeoghcgd.api.GUIHomes.Commands.setHomeCommand;
import uk.co.benkeoghcgd.api.GUIHomes.Data.ConfigYML;
import uk.co.benkeoghcgd.api.GUIHomes.Data.HomesYML;
import uk.co.benkeoghcgd.api.GUIHomes.Listeners.JoinLeaveListeners;

public class GUIHomes extends AxiusPlugin {

    public HomesYML hyml;
    public ConfigYML cyml;

    @Override
    protected void Preregister() {
        Logging.Log(this, "Running plugin pre-registry tasks");
        EnableUpdater(102909, VersionFormat.MajorMinorPatch, ".");
        Metrics m = new Metrics(this, 15677);

        Logging.Log(this, "Registering Data Files");
        hyml = new HomesYML(this);
        cyml = new ConfigYML(this);

        Logging.Log(this, "Collecting Commands");
        commands.add(new homeCommand(this, hyml));
        commands.add(new setHomeCommand(this));

        Logging.Log(this, "Registering Listeners");
        getServer().getPluginManager().registerEvents(new JoinLeaveListeners(this, hyml), this);
    }

    @Override
    protected void Postregister() {
        setIcon(GUI.createGuiItem(Material.CYAN_BED, "§3§lGUIHomes"));
        setFormattedName("&x&0&0&f&b&a&8&lG&x&0&0&f&6&b&4&lU&x&0&0&f&1&c&0&lI&x&0&0&e&c&c&c&lH&x&0&0&e&6&d&9&lo&x&0&0&e&1&e&5&lm&x&0&0&d&c&f&1&le&x&0&0&d&7&f&d&ls");

        Logging.Log(this, "Registering Commands");
        registerCommands();
    }

    @Override
    protected void Stop() {

    }

    @Override
    protected void FullStop() {

    }
}
