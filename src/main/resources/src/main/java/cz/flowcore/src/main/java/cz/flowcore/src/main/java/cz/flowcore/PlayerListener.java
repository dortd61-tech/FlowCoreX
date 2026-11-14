package cz.flowcore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class PlayerListener implements Listener {

    private final FlowCoreX plugin;

    public PlayerListener(FlowCoreX plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!plugin.isFrozen(event.getPlayer().getUniqueId())) {
            return;
        }

        if (event.getTo() == null) {
            return;
        }

        boolean changedBlock =
                event.getFrom().getBlockX() != event.getTo().getBlockX()
                || event.getFrom().getBlockY() != event.getTo().getBlockY()
                || event.getFrom().getBlockZ() != event.getTo().getBlockZ();

        if (changedBlock) {
            event.setTo(event.getFrom());
        }
    }
}
