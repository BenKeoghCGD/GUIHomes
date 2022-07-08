package uk.co.benkeoghcgd.api.GUIHomes.GUIs;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.GUI;
import uk.co.benkeoghcgd.api.GUIHomes.Data.HomesYML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class EditHomeGUI extends GUI {

    HomesYML data;
    Player sender;
    OfflinePlayer target;
    String home;
    Material newMat = null;

    List<Material> options = Arrays.asList(Material.GRASS_BLOCK, Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.LAVA_CAULDRON, Material.WATER_CAULDRON,
            Material.OAK_PLANKS, Material.ANVIL, Material.BEACON, Material.BEDROCK, Material.FARMLAND, Material.END_PORTAL_FRAME, Material.OBSIDIAN, Material.REDSTONE_BLOCK,
            Material.BEEHIVE, Material.BELL, Material.BONE_BLOCK, Material.BOOKSHELF, Material.ENCHANTING_TABLE, Material.ENDER_CHEST, Material.BLACKSTONE, Material.CRAFTING_TABLE,
            Material.FURNACE, Material.RAIL, Material.CLAY, Material.LECTERN, Material.SPAWNER);

    HashMap<Integer, Material> mats = new HashMap<>();


    public EditHomeGUI(AxiusPlugin instance, HomesYML data, Player sndr, OfflinePlayer p, String homeName) {
        super(instance, 4, instance.getNameFormatted() + "§7 Change home block");
        this.data = data;
        sender = sndr;
        target = p;
        home = homeName;
        Populate();
    }

    @Override
    protected void Populate() {
        int i = 0;
        for(Material m : options) {
            container.setItem(i, createGuiItem(m, "§a" + m.name(), "§7Click to change block"));
            mats.put(i, m);
            i++;
        }

        refreshPreview();

        while(container.firstEmpty() != -1) {
            container.setItem(container.firstEmpty(), createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ""));
        }
    }

    private void refreshPreview() {
        container.setItem(31, null);
        String[] parts = data.getHomeRaw(target, home).split(";");
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

        String[] loreLinesRaw = HomesGUI.LocationToString(loc).split(", ");
        ItemStack itm = createGuiItem(newMat != null ? newMat : mat, "§3§l" + parts[0].toUpperCase(), "§7" + loreLinesRaw[0], "§7" + loreLinesRaw[1], "§7" + loreLinesRaw[2], "", "§aClick to close");
        container.setItem(31, itm);
    }

    @Override
    protected void onInvClick(InventoryClickEvent inventoryClickEvent) {
        if(inventoryClickEvent.getSlot() == 31) {
            inventoryClickEvent.getWhoClicked().closeInventory();
        }
        else {
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
