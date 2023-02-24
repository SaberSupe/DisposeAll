package saber.disposeall.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import saber.disposeall.DisposeAll;

import java.util.List;

public class DisposeAllCommand implements CommandExecutor {

    private final DisposeAll plugin;

    public DisposeAllCommand(DisposeAll p1){plugin = p1;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Make sure right command was entered
        if (!command.getName().equalsIgnoreCase("disposeall")) return true;

        // Check perms
        if (!sender.hasPermission("disposeall.use")){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("msgNoPerms")));
            return true;
        }

        // Check for instance of player
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("msgNotPlayer")));
            return true;
        }

        // Get the disposeable items
        List<String> disposeables = plugin.getConfig().getStringList("disposeables");

        Player play = (Player) sender;
        int total = 0;

        // Loop through the players inventory checking items
        Inventory inv = play.getInventory();
        for (int i = 0; i < inv.getSize(); i++){
            ItemStack x = inv.getItem(i);

            // Check if it is a disposable material
            if (x != null && disposeables.contains(x.getType().toString())){

                // This step is to exclude and special items, for example compressed seeds
                ItemStack y = new ItemStack(x.getType());
                y.setAmount(x.getAmount());
                if (x.toString().equalsIgnoreCase(y.toString())){
                    total += x.getAmount();
                    inv.clear(i);
                }
            }
        }

        total = total * plugin.getConfig().getInt("sellPrice");
        DisposeAll.getEconomy().depositPlayer((OfflinePlayer)  play, total);
        String success = plugin.getConfig().getString("msgSuccess").replace("{0}",String.valueOf(total));
        play.sendMessage(ChatColor.translateAlternateColorCodes('&',success));

        return true;
    }
}
