package uk.co.benkeoghcgd.api.GUIHomes;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.UpdaterMethod;
import uk.co.benkeoghcgd.api.AxiusCore.API.PluginData.PluginInfoData;
import uk.co.benkeoghcgd.api.AxiusCore.API.PluginData.PluginInfoDataButton;
import uk.co.benkeoghcgd.api.AxiusCore.API.PluginData.PublicPluginData;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.Logging;
import uk.co.benkeoghcgd.api.GUIHomes.Commands.homeCommand;
import uk.co.benkeoghcgd.api.GUIHomes.Commands.setHomeCommand;
import uk.co.benkeoghcgd.api.GUIHomes.Data.ConfigYML;
import uk.co.benkeoghcgd.api.GUIHomes.Data.HomesYML;
import uk.co.benkeoghcgd.api.GUIHomes.Data.MessagesYML;
import uk.co.benkeoghcgd.api.GUIHomes.Listeners.JoinLeaveListeners;

public class GUIHomes extends AxiusPlugin {

    public HomesYML hyml;
    public ConfigYML cyml;
    public MessagesYML myml;

    @Override
    protected void Preregister() {
        instance = this;
        Logging.Log(this, "Running plugin pre-registry tasks");

        PublicPluginData ppd = new PublicPluginData();
        ppd.setPublicStatus(true);
        ppd.setRegisterStatus(true);
        ppd.setSpigotID(102909);
        ppd.setVersionSeparator(".");
        ppd.setUpdaterMethod(UpdaterMethod.SPIGOT);
        SetPublicPluginData(ppd);

        PluginInfoData pid = new PluginInfoData();
        pid.addButton(GUI.createGuiItem(Material.COMMAND_BLOCK, "§3§lRELOAD CONFIG", "§7Reload the Config.yml"), new PluginInfoDataButton() {
            @Override
            public void execute() {}

            @Override
            public void execute(CommandSender sender) {
                sender.sendMessage(getNameFormatted() + " §7Reloaded config.yml!");
                cyml.loadData();
            }
        });

        pid.addButton(GUI.createGuiItem(Material.WRITABLE_BOOK, "§b§lRELOAD MESSAGES", "§7Reload the Language.yml"), new PluginInfoDataButton() {
            @Override
            public void execute() {}

            @Override
            public void execute(CommandSender sender) {
                sender.sendMessage(getNameFormatted() + " §7Reloaded language.yml!");
                myml.loadData();
            }
        });

        pid.addButton(GUI.createGuiItem(Material.ENCHANTED_BOOK, "§a§lRELOAD ALL", "§7Reload the Language.yml and Config.yml files"), new PluginInfoDataButton() {
            @Override
            public void execute() {}

            @Override
            public void execute(CommandSender sender) {
                sender.sendMessage(getNameFormatted() + " §7Reloaded language.yml and config.yml!");
                myml.loadData();
                cyml.loadData();
            }
        });

        SetPluginInfoData(pid);

        Metrics m = new Metrics(this, 15677);

        Logging.Log(this, "Registering Data Files");
        hyml = new HomesYML(this);
        cyml = new ConfigYML(this);
        myml = new MessagesYML(this);

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

    static GUIHomes instance;
    public static GUIHomes getInstance() {
      return instance;
    }
}
