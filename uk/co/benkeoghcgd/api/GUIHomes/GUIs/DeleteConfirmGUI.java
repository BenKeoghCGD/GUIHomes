package uk.co.benkeoghcgd.api.GUIHomes.GUIs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;
import uk.co.benkeoghcgd.api.GUIHomes.Data.HomesYML;

public class DeleteConfirmGUI extends GUI {

    HomesYML data;
    Player sender;
    OfflinePlayer target;
    AxiusPlugin plug;
    String home;
    Location loc;

    public DeleteConfirmGUI(AxiusPlugin instance, HomesYML hyml, Player sender, OfflinePlayer target, String home, Location loc) {
        super(instance, 1, instance.getNameFormatted() + "§7 Confirm Deletion");
        data = hyml;
        this.sender = sender;
        this.target = target;
        plug = instance;
        this.home = home;
        this.loc = loc;

        Populate();
    }

    @Override
    protected void Populate() {
        container.setItem(6, createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "§a§lConfirm Deletion"));
        container.setItem(2, createGuiItem(Material.RED_STAINED_GLASS_PANE, "§c§lCancel"));
    }

    @Override
    protected void onInvClick(InventoryClickEvent inventoryClickEvent) {
        if(inventoryClickEvent.getSlot() == 6) {
            inventoryClickEvent.getWhoClicked().sendMessage(plug.getNameFormatted() + "§7 Deleted home: §3" + home.toUpperCase() + "§7.");
            data.deleteHome(target, home);
        }
        else if (inventoryClickEvent.getSlot() == 2) {}
        else return;

        inventoryClickEvent.getWhoClicked().closeInventory();
        HomesGUI gui = new HomesGUI(plug, data, sender, target, GUIAssets.getInventoryRows(data.getPlayerHomes(target).size()));
        gui.show(sender);
    }
}
