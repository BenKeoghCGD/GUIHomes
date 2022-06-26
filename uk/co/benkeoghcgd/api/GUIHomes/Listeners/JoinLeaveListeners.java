package uk.co.benkeoghcgd.api.GUIHomes.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.co.benkeoghcgd.api.GUIHomes.Data.HomesYML;
import uk.co.benkeoghcgd.api.GUIHomes.GUIHomes;

public class JoinLeaveListeners implements Listener {

    GUIHomes plugin;
    HomesYML homesYML;

    public JoinLeaveListeners(GUIHomes guiHomes, HomesYML hyml) {
        plugin = guiHomes;
        homesYML = hyml;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        homesYML.initPlayer(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
    }
}
