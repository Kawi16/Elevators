package com.lkeehl.elevators;

import com.lkeehl.elevators.helpers.ResourceHelper;
import com.lkeehl.elevators.services.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import world.bentobox.bentobox.api.hooks.Hook;

import java.io.File;
import java.util.logging.Logger;

public class Elevators extends JavaPlugin {

    @Override()
    public void onEnable() {
        DataContainerService.init(this);
        ElevatorVersionService.init();
        ElevatorEffectService.init();
        ElevatorRecipeService.init();
        ElevatorTypeService.init();
        ElevatorActionService.init();
        ObstructionService.init();
        HookService.init();
        ListenerService.init();

        this.reloadElevators();
    }

    @Override()
    public void onDisable() {

    }

    private void reloadElevators() {

        File configFile = new File(this.getDataFolder(), "config.yml");
        ResourceHelper.exportResource(this, "config.yml", configFile,false);

        ConfigService.loadConfig(configFile);
    }

    public static Elevators getInstance() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Elevators");
        if(plugin instanceof Elevators)
            return (Elevators) plugin;
        return null;
    }

    public static File getConfigDirectory() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Elevators");
        File configDirectory;
        if(plugin == null) {
            plugin = Bukkit.getPluginManager().getPlugins()[0];
            if(plugin == null)
                return null;
            configDirectory = new File(plugin.getDataFolder().getParent(),"Elevators");
        }else
            configDirectory = plugin.getDataFolder();
        try {
            //noinspection ResultOfMethodCallIgnored
            configDirectory.mkdirs();
        } catch (Exception ignored) {
        }

        return configDirectory;
    }

    public static Logger getElevatorsLogger() {

        Plugin plugin = Bukkit.getPluginManager().getPlugin("Elevators");
        if(plugin != null)
            return plugin.getLogger();

        return Bukkit.getLogger();

    }

}
