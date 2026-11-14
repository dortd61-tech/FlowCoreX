
package cz.flowcore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class FlowCoreX extends JavaPlugin {

    private final Set<UUID> frozenPlayers = new HashSet<>();
    private boolean staffChatEnabled = false;

    @Override
    public void onEnable() {
        getLogger().info("FlowCoreX byl zapnut!");
        getServer().getPluginManager().registerEvents(
                new PlayerListener(this), this
        );
    }

    @Override
    public void onDisable() {
        getLogger().info("FlowCoreX byl vypnut!");
    }

    public boolean isFrozen(UUID uuid) {
        return frozenPlayers.contains(uuid);
    }

    public void freeze(UUID uuid) {
        frozenPlayers.add(uuid);
    }

    public void unfreeze(UUID uuid) {
        frozenPlayers.remove(uuid);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {

        String name = command.getName().toLowerCase();

        switch (name) {
            case "flowcore":
                if (!sender.hasPermission("flowcore.admin")) {
                    sender.sendMessage(ChatColor.RED + "Nemáš oprávnění.");
                    return true;
                }

                sender.sendMessage(ChatColor.DARK_PURPLE + "FlowCoreX");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Verze: 1.0.0");
                sender.sendMessage(ChatColor.GRAY + "Plugin je aktivní.");
                return true;

            case "staffchat":
                if (!sender.hasPermission("flowcore.staffchat")) {
                    sender.sendMessage(ChatColor.RED + "Nemáš oprávnění.");
                    return true;
                }

                if (!(sender instanceof Player player)) {
                    sender.sendMessage("Tento příkaz použij ve hře.");
                    return true;
                }

                staffChatEnabled = !staffChatEnabled;
                player.sendMessage(ChatColor.DARK_PURPLE + "Staff chat: "
                        + (staffChatEnabled ? ChatColor.GREEN + "ZAPNUT" :
                        ChatColor.RED + "VYPNUT"));
                return true;

            case "freeze":
                if (!sender.hasPermission("flowcore.freeze")) {
                    sender.sendMessage(ChatColor.RED + "Nemáš oprávnění.");
                    return true;
                }

                if (args.length != 1) {
                    sender.sendMessage(ChatColor.RED + "Použití: /freeze <hráč>");
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Hráč není online.");
                    return true;
                }

                freeze(target.getUniqueId());
                target.sendMessage(ChatColor.RED + "Jsi zmrazen staffem.");
                sender.sendMessage(ChatColor.GREEN + "Hráč " + target.getName()
                        + " byl zmrazen.");
                return true;

            case "unfreeze":
                if (!sender.hasPermission("flowcore.freeze")) {
                    sender.sendMessage(ChatColor.RED + "Nemáš oprávnění.");
                    return true;
                }

                if (args.length != 1) {
                    sender.sendMessage(ChatColor.RED + "Použití: /unfreeze <hráč>");
                    return true;
                }

                Player frozen = Bukkit.getPlayerExact(args[0]);
                if (frozen == null) {
                    sender.sendMessage(ChatColor.RED + "Hráč není online.");
                    return true;
                }

                unfreeze(frozen.getUniqueId());
                frozen.sendMessage(ChatColor.GREEN + "Už nejsi zmrazen.");
                sender.sendMessage(ChatColor.GREEN + "Hráč byl rozmrazen.");
                return true;
        }

        return false;
    }
}
