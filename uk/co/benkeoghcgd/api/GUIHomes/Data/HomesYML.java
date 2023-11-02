package uk.co.benkeoghcgd.api.GUIHomes.Data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Utilities.DataHandler;
import uk.co.benkeoghcgd.api.GUIHomes.GUIHomes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomesYML extends DataHandler {

    YamlConfiguration cfg;
    static GUIHomes plug;

    public HomesYML(AxiusPlugin instance) {
        super(instance, "Homes");
        plug = GUIHomes.getInstance();
        this.cfg = YamlConfiguration.loadConfiguration(file);
        refresh();
    }

    public void refresh() {
        this.cfg = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    protected void saveDefaults() {
        List<String> exampleHomes = new ArrayList<>();
        exampleHomes.add("name;world:x:y:z:pitch:yaw:blockID");
        exampleHomes.add("example;world:0:0:0:-90:33:" + Material.GRASS_BLOCK.name());

        setData("homes.exampleUUID-someothernumbers-nstuff", exampleHomes, false);
    }

    public static String locationToString(Location location) {
        return Objects.requireNonNull(location.getWorld()).getName() + ":" + location.getX() + ":" + location.getY() + ":"
                + location.getZ() + ":" + location.getPitch() + ":" + location.getYaw();
    }

    public static Location stringToLocation(String s) {
        String[] parts = s.split(":");
        if(parts.length < 6) plug.errors.add(new Exception("Input Error: stringToLocation"));
        else {
            return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Float.parseFloat(parts[5]), Float.parseFloat(parts[4]));
        }

        return null;
    }

    public void initPlayer(Player player) {
        setData("homes." + player.getUniqueId(), new ArrayList<>(), false);
    }

    public List<String> getPlayerHomes(Player player) {
        return cfg.getStringList("homes." + player.getUniqueId());
    }

    public List<String> getPlayerHomes(OfflinePlayer player) {
        return cfg.getStringList("homes." + player.getUniqueId());
    }

    public void addHome(Player player, Location loc, String name) {
        List<String> homes = getPlayerHomes(player);
        boolean canAdd = true;
        if(!homes.isEmpty()) {
            for(String s : homes) {
                if(s.startsWith(name)) {
                    canAdd = false;
                    break;
                }
            }
        }

        if(canAdd) {
            homes.add(name + ";" + locationToString(loc) + ":default");
            setData("homes." + player.getUniqueId(), homes);
        }
    }

    public void overrideHome(Player player, Location loc, String name, Material m) {
        List<String> homes = getPlayerHomes(player);
        String oldHome = null;
        if(!homes.isEmpty()) {
            for(String s : homes) {
                if(s.startsWith(name)) oldHome = s;
            }
        }

        if(oldHome != null) {
            homes.remove(oldHome);
            homes.add(name + ";" + locationToString(loc) + ":" + m.toString());
            setData("homes." + player.getUniqueId(), homes, true);
        }

    }

    public void deleteHome(OfflinePlayer player, String name) {
        List<String> homes = getPlayerHomes(player);
        if(!homes.isEmpty()) {
            for(String s : homes) {
                if(s.startsWith(name)) {
                    homes.remove(s);
                    setData("homes." + player.getUniqueId(), homes);
                    break;
                }
            }
        }

    }

    public String getHomeRaw(OfflinePlayer p, String homeName) {
        for(String s : getPlayerHomes(p)) {
            if(s.split(";")[0].equalsIgnoreCase(homeName)) return s;
        }
        return null;
    }
}
