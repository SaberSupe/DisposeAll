package saber.disposeall;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import saber.disposeall.commands.DisposeAllCommand;

import java.util.logging.Level;

public final class DisposeAll extends JavaPlugin {

    private static Economy econ = null;
    @Override
    public void onEnable() {
        // Plugin startup logic

        //Load Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Register command
        getCommand("disposeall").setExecutor(new DisposeAllCommand(this));

        // Setup economy vault link
        if (!setupEconomy()){
            this.getLogger().log(Level.INFO, "DisposeAll failed to load due to Valut not found");
            return;
        }

        //Log successful launch
        this.getLogger().log(Level.INFO, "DisposeAll loaded Successfully");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
