package uk.co.benkeoghcgd.api.GUIHomes;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.Exceptions.CoreSelfUpdateException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public final class CoreSelfInstaller {

    public static void InitSelfInstaller(JavaPlugin plugin) throws CoreSelfUpdateException {
        if(Bukkit.getServer().getPluginManager().getPlugin("AxiusCore") == null) {
            if(!SelfInstall(plugin, 102852)) throw new CoreSelfUpdateException("Could not update plugin.", null);

            ClassLoader cl = plugin.getClass().getClassLoader();
            if(cl instanceof URLClassLoader) {
                try {
                    ((URLClassLoader) cl).close();
                } catch (IOException e) {
                    throw new CoreSelfUpdateException("Could not unload plugin jar.", e);
                }
            }

            System.gc();
        }
    }

    public static boolean SelfInstall(JavaPlugin plugin, int PluginID) {
        try {
            FileOutputStream fos = new FileOutputStream(plugin.getDataFolder().getParentFile() + "AxiusCore.jar");
            ReadableByteChannel rbc = Channels.newChannel(new URL("https://cdn.spiget.org/file/spiget-resources/" + PluginID + ".jar").openStream());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return true;
        } catch (Exception e) {
            String reason = "Unknown Exception";
            if(e instanceof FileNotFoundException) reason = "Unknown File Save Error";
            if(e instanceof MalformedURLException) reason = "Invalid URL";
            if(e instanceof IOException) reason = "Error during file transfer";
            return false;
        }
    }
}
