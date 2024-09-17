package cc.synkdev.serverveil.managers;

import cc.synkdev.serverveil.ServerVeil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class CommandBlockerListener implements Listener {
    private final ServerVeil core = ServerVeil.getInstance();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preCmd(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        if (event.getMessage().startsWith("/help")) {
            if (core.help.isEmpty()) return;
            event.setCancelled(true);
            for (String s : core.help) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6"+s));
            }
        }
        if (p.hasPermission("serverveil.commandblocker.bypass")) return;
        for (String s : core.blocked) {
            if (event.getMessage().startsWith(s)) {
                event.setCancelled(true);
                p.sendMessage(core.prefix()+ ChatColor.RED+Lang.translate("blocked-command"));
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void sendCmd(PlayerCommandSendEvent event) {
        Player p = event.getPlayer();
        if (p.hasPermission("serverveil.commandblocker.bypass")) return;
        event.getCommands().removeAll(core.blockedNoSlash);
    }
}
