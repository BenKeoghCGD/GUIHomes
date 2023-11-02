package uk.co.benkeoghcgd.api.GUIHomes.GUIs;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.GUI;
import uk.co.benkeoghcgd.api.AxiusCore.Utils.GUIAssets;
import uk.co.benkeoghcgd.api.GUIHomes.Data.ConfigYML;
import uk.co.benkeoghcgd.api.GUIHomes.Data.HomesYML;
import uk.co.benkeoghcgd.api.GUIHomes.Data.MessagesYML;
import uk.co.benkeoghcgd.api.GUIHomes.GUIHomes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditHomeGUI extends GUI {

    HomesYML data;
    Player sender;
    OfflinePlayer target;
    String home;
    Material newMat = null;
    HashMap<Integer, Material> mats = new HashMap<>();

    List<Material> options;
    List<Material> optionsPermissionLocked = new ArrayList<>();


    public EditHomeGUI(GUIHomes instance, HomesYML data, Player sndr, OfflinePlayer p, String homeName) {
        super(instance, GUIAssets.getInventoryRows(ConfigYML.getOptions().size()) + 1, instance.getNameFormatted() + " " + MessagesYML.translateColor(instance.myml.data.get("GUI.Edit.Title").toString().replaceAll("%HOME%", homeName)));
        this.data = data;

        sender = sndr;
        target = p;
        home = homeName;
        options = ConfigYML.getOptions();

        ConfigYML cfgyml = ConfigYML.getInstance();
        for(String group : cfgyml.getGroupNames()) {
            boolean hasPermission = sndr.hasPermission("guihomes.icongroup." + group);
            for(String icon : cfgyml.getGroupPermissions(group)) {
                if(!sndr.hasPermission("guihomes.icon." + icon) && !hasPermission) optionsPermissionLocked.add(Material.matchMaterial(icon));
            }
        }
        Populate();
    }

    @Override
    protected void Populate() {
        int i = 0;

        for(Material m : options) {
            container.setItem(i, createGuiItem(m, "§" + (optionsPermissionLocked.contains(m) ? "c" : "a") + "§l" + m.name().replaceAll("_", " ").toUpperCase(), "§7Click to change block"));
            mats.put(i, m);
            i++;
        }

        refreshPreview();

        while(container.firstEmpty() != -1) {
            container.setItem(container.firstEmpty(), createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ""));
        }
    }

    private void refreshPreview() {
        container.setItem(((GUIAssets.getInventoryRows(ConfigYML.getOptions().size()) + 1) * 9) - 5, null);
        String[] parts = data.getHomeRaw(target, home).split(";");
        Location loc = HomesYML.stringToLocation(parts[1]);

        Material mat = Material.BARRIER; // DEFAULT MATERIAL SHOULD ONLY SHOW ON ERROR

        assert loc != null;
        mat = HomesGUI.getMaterial(parts, loc, mat);

        String[] loreLinesRaw = HomesGUI.LocationToString(loc).split(", ");
        ItemStack itm = createGuiItem(newMat != null ? newMat : mat, "§3§l" + parts[0].toUpperCase(), "§7" + loreLinesRaw[0], "§7" + loreLinesRaw[1], "§7" + loreLinesRaw[2], "", "§aClick to close");
        container.setItem( ((GUIAssets.getInventoryRows(ConfigYML.getOptions().size()) + 1) * 9) - 5, itm);
    }

    @Override
    protected void onInvClick(InventoryClickEvent inventoryClickEvent) {
        if(inventoryClickEvent.getCurrentItem() == null) return;

        if(inventoryClickEvent.getSlot() == ((GUIAssets.getInventoryRows(ConfigYML.getOptions().size()) + 1) * 9) - 5) {
            inventoryClickEvent.getWhoClicked().closeInventory();
        }
        else {
            if(optionsPermissionLocked.contains(inventoryClickEvent.getCurrentItem().getType())) return;
            if(mats.containsKey(inventoryClickEvent.getSlot())) {
                String[] parts = data.getHomeRaw(target, home).split(";");
                Location loc = HomesYML.stringToLocation(parts[1]);
                data.overrideHome(target.getPlayer(), loc, home, mats.get(inventoryClickEvent.getSlot()));
                newMat = mats.get(inventoryClickEvent.getSlot());
                refreshPreview();
            }
        }
    }
}
