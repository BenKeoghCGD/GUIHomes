package uk.co.benkeoghcgd.api.GUIHomes.GUIs;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.GUI;
import uk.co.benkeoghcgd.api.GUIHomes.Data.HomesYML;
import uk.co.benkeoghcgd.api.GUIHomes.GUIHomes;

import java.util.HashMap;

public class HomesGUI extends GUI {

    Player p, sndr;
    HomesYML data;
    HashMap<ItemStack, Location> homes = new HashMap<>();
    GUIHomes main;
    AxiusPlugin instance;

    public HomesGUI(AxiusPlugin plugin, HomesYML homesYML, Player sndr, Player target, int i) {
        super(plugin, i, plugin.getNameFormatted() + (sndr == target ? "§7 Your homes." : "§7 Homes of: " + target.getName()));

        instance = plugin;
        p = target;
        this.sndr = sndr;
        main = (GUIHomes) plugin;
        data = homesYML;

        Populate();
    }

    public HomesGUI(AxiusPlugin plugin, HomesYML homesYML, Player sndr, OfflinePlayer target, int i) {
        super(plugin, i, plugin.getNameFormatted() + (sndr == target ? "§7 Your homes." : "§7 Homes of: " + target.getName()));

        instance = plugin;
        p = target.getPlayer();
        this.sndr = sndr;
        main = (GUIHomes) plugin;
        data = homesYML;

        Populate();
    }

    @Override
    protected void Populate() {
        data.refresh();
        for(String s : data.getPlayerHomes(p)) {
            String[] parts = s.split(";");
            Location loc = HomesYML.stringToLocation(parts[1]);

            Material mat = Material.BARRIER; // DEFAULT MATERIAL SHOULD ONLY SHOW ON ERROR

            if(loc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) mat = Material.GRASS_BLOCK;
            else if(loc.getWorld().getEnvironment().equals(World.Environment.THE_END)) mat = Material.END_STONE;
            else if(loc.getWorld().getEnvironment().equals(World.Environment.NETHER)) mat = Material.NETHERRACK;

            if(parts[0].equalsIgnoreCase("home")) mat = Material.OAK_PLANKS;
            else if(parts[0].equalsIgnoreCase("bed")) mat = Material.RED_BED;

            if(parts[1].split(":").length == 7) {
                Material tempMat = Material.matchMaterial(parts[1].split(":")[6]);
                if(tempMat != null && !parts[1].split(":")[6].equalsIgnoreCase("default")) mat = tempMat;
            }

            String[] loreLinesRaw = LocationToString(loc).split(", ");
            ItemStack itm = createGuiItem(mat, "§3§l" + parts[0].toUpperCase(), "§7" + loreLinesRaw[0], "§7" + loreLinesRaw[1], "§7" + loreLinesRaw[2], "", "§aLeft-Click to Teleport", "§6SHIFT-Left-CLick to Edit Block", "§cRight-Click to Delete");
            homes.put(itm, loc);
            container.addItem(itm);
        }
    }

    @Override
    protected void onInvClick(InventoryClickEvent inventoryClickEvent) {
       for(ItemStack i : homes.keySet()) {
            if(i.equals(inventoryClickEvent.getCurrentItem())) {
                String homeName = ChatColor.stripColor(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName());
                if(inventoryClickEvent.getClick() == ClickType.LEFT) {
                    inventoryClickEvent.getWhoClicked().teleport(homes.get(i));
                    inventoryClickEvent.getWhoClicked().sendMessage(main.getNameFormatted() + "§7 Teleported to home: §3" + homeName + "§7.");
                    inventoryClickEvent.getWhoClicked().closeInventory();
                }
                else if (inventoryClickEvent.getClick() == ClickType.RIGHT) {
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    DeleteConfirmGUI dcGUI = new DeleteConfirmGUI(instance, data, sndr, p, homeName, homes.get(i));
                    dcGUI.show(sndr);
                }
                else if (inventoryClickEvent.getClick() == ClickType.SHIFT_LEFT) {
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    EditHomeGUI dcGUI = new EditHomeGUI(instance, data, sndr, p, homeName);
                    dcGUI.show(sndr);
                }
                return;
            }
        }
    }

    public static String LocationToString(Location location) {
        return "X=" + location.getX() + ", Y=" + location.getY() + ", Z=" + location.getZ();
    }
}
