package cc.synkdev.serverveil.managers;

import cc.synkdev.serverveil.ServerVeil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class BarPlayerManager implements Listener {
    private int delay;
    private Boolean papi;
    private final ServerVeil core = ServerVeil.getInstance();
    public Map<UUID, Long> timeMap = new HashMap<>();
    public Map<UUID, String> messageMap = new HashMap<>();
    List<UUID> list = new ArrayList<>();

    public BarPlayerManager(Boolean papi) {
        delay = core.getConfig().getInt("actionbar-delay");
        this.papi = papi;
        runnable.runTaskTimer(core, 0L, 10L);
    }

    public void add(Player p, String message, long duration) {
        timeMap.put(p.getUniqueId(), System.currentTimeMillis() + duration);
        messageMap.put(p.getUniqueId(), message);
        sendActionBarMessage(p, message);
    }

    public void add(Player p) {
        List<String> strings = new ArrayList<>(core.getConfig().getStringList("actionbar-messages"));
        if (strings.isEmpty()) return;
        Collections.shuffle(strings);
        String message = strings.get(0);
        add(p, message, delay);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        list.add(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        list.remove(event.getPlayer().getUniqueId());
        timeMap.remove(event.getPlayer().getUniqueId());
        messageMap.remove(event.getPlayer().getUniqueId());
    }

    public void toggle(Player p) {
        String s;
        if (list.contains(p.getUniqueId())) {
            s = ChatColor.RED + Lang.translate("off");
            list.remove(p.getUniqueId());
            messageMap.remove(p.getUniqueId());
            timeMap.remove(p.getUniqueId());
        } else {
            list.add(p.getUniqueId());
            s = ChatColor.GREEN + Lang.translate("on");
        }
        p.sendMessage(core.prefix() + ChatColor.GOLD + Lang.translate("toggle-bar", s));
    }

    private void sendActionBarMessage(Player player, String message) {
        if (papi) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    BukkitRunnable runnable = new BukkitRunnable() {
        @Override
        public void run() {
            for (UUID uuid : new ArrayList<>(list)) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null || !player.isOnline()) {
                    timeMap.remove(uuid);
                    messageMap.remove(uuid);
                    continue;
                }
                long currentTime = System.currentTimeMillis();
                long nextMessageTime = timeMap.getOrDefault(uuid, 0L);
                if (currentTime >= nextMessageTime) {
                    add(player);
                } else {
                    sendActionBarMessage(player, messageMap.get(player.getUniqueId()));
                }
            }
        }
    };
}
