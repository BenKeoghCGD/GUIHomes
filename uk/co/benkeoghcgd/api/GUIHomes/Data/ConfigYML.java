package uk.co.benkeoghcgd.api.GUIHomes.Data;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.DataHandler;
import java.util.*;

public class ConfigYML extends DataHandler {

    static ConfigYML instance;

    public static ConfigYML getInstance() {
        return instance;
    }

    private final static List<Material> optionsDefault = Arrays.asList(Material.GRASS_BLOCK, Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.LAVA_BUCKET, Material.WATER_BUCKET,
            Material.OAK_PLANKS, Material.ANVIL, Material.BEACON, Material.BEDROCK, Material.FARMLAND, Material.END_PORTAL_FRAME, Material.OBSIDIAN, Material.REDSTONE_BLOCK,
            Material.BEEHIVE, Material.BELL, Material.BONE_BLOCK, Material.BOOKSHELF, Material.ENCHANTING_TABLE, Material.ENDER_CHEST, Material.BLACKSTONE, Material.CRAFTING_TABLE,
            Material.FURNACE, Material.RAIL, Material.CLAY, Material.LECTERN, Material.SPAWNER);

    static List<Material> options = new ArrayList<>();

    public ConfigYML(AxiusPlugin instance) {
        super(instance, "Config");
    }

    @Override
    protected void saveDefaults() {
        ConfigYML.instance = this;
        setData("defaults.maxHomes", 2);
        setData("Icons.Materials", getDefaultOptionsRaw());
        List<String> gOwn = new ArrayList<>();
        gOwn.add("spawner");
        gOwn.add("rail");
        gOwn.add("diamond_block");
        setData("Icons.Groups.Owner", gOwn);
        List<String> gVip = new ArrayList<>();
        gOwn.add("iron_block");
        gOwn.add("furnace");
        gOwn.add("gold_block");
        setData("Icons.Groups.VIP", gVip);
    }

    public static List<String> getDefaultOptionsRaw() {
        List<String> defaultOptions = new ArrayList<>();
        for (Material opt : optionsDefault) defaultOptions.add(opt.name());
        return defaultOptions;
    }

    public static List<Material> getOptions() {
        getInstance().loadOptions();
        return options;
    }

    private void loadOptions() {
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(getInstance().file);
        options = new ArrayList<>();
        fileConfig.getStringList("Icons.Materials").forEach(i -> options.add(Material.matchMaterial(i)));
        groups = new HashMap<>();
        Objects.requireNonNull(fileConfig.getConfigurationSection("Icons.Groups")).getKeys(true).forEach(i -> groups.put(i, fileConfig.getStringList("Icons.Groups." + i)));
    }

    public HashMap<String, List<String>> groups = new HashMap<>();

    public List<String> getGroupNames() {
        return groups.keySet().stream().toList();
    }

    public List<String> getGroupPermissions(String group) {
        return groups.get(group);
    }
}
