package cc.synkdev.serverveil;

import cc.synkdev.serverveil.commands.VeilCmd;
import cc.synkdev.serverveil.managers.CommandBlockerListener;
import cc.synkdev.serverveil.managers.Lang;
import cc.synkdev.serverveil.objects.MOTDSettings;
import cc.synkdev.synkLibs.bukkit.SynkLibs;
import cc.synkdev.synkLibs.bukkit.Utils;
import cc.synkdev.synkLibs.components.SynkPlugin;
import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public final class ServerVeil extends JavaPlugin implements SynkPlugin {
    @Getter private static ServerVeil instance;
    public List<String> help = new ArrayList<>();
    public List<String> blocked = new ArrayList<>();
    public List<String> blockedNoSlash = new ArrayList<>();
    public HashMap<String, String> langMap = new HashMap<>();
    //public String brand;
    @Getter @Setter private FileConfiguration config;
    private PaperCommandManager commandManager;
    public MOTDSettings set;

    @Override
    public void onEnable() {
        instance = this;
        SynkLibs.setSpl(this);

        this.loadConfig();

        Lang.init();

        new Metrics(this, 23389);

        Utils.checkUpdate(this, this);

        Bukkit.getPluginManager().registerEvents(new CommandBlockerListener(), this);

        ProtocolManager pM = ProtocolLibrary.getProtocolManager();
        pM.addPacketListener(new PacketAdapter(this, PacketType.Status.Server.SERVER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrappedServerPing ping = event.getPacket().getServerPings().read(0);

                if (set.getDescription().length != 0) {
                    ping.setMotD(Util.convertColors(set.getDescription()[0]+"\n"+set.getDescription()[1]));
                }

                if (set.getSpoofMax()) {
                    ping.setPlayersMaximum(set.getSpoofedMax());
                }

                if (set.getSpoofCount()) {
                    ping.setPlayersOnline(set.getSpoofedCount());
                }

                if (!set.getHover().isEmpty()) {
                    List<WrappedGameProfile> list = new ArrayList<>();
                    for (String s : set.getHover()) {
                        list.add(new WrappedGameProfile(UUID.randomUUID(),Util.convertColorsSymbol(s)));
                    }
                    ping.setPlayers(list);
                }

                File icon = new File(getDataFolder(),set.getIcon()+".png");
                if (icon.exists()) {
                    try {
                        BufferedImage image = ImageIO.read(icon);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", baos);
                        baos.flush();

                        byte[] bytes = baos.toByteArray();
                        String base64 = Base64.getEncoder().encodeToString(bytes);
                        ping.setFavicon(WrappedServerPing.CompressedImage.fromBase64Png(base64));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                event.getPacket().getServerPings().write(0, ping);
            }
        });
        /*
        Will be added in a future update

        pM.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.CUSTOM_PAYLOAD) {
            @Override
            public void onPacketSending(PacketEvent event) {
                event.getPacket().getStrings().write(1, brand);
            }
        });*/

        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new VeilCmd());
    }

    private void loadConfig() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        try {
            if (!configFile.getParentFile().exists()) configFile.getParentFile().mkdirs();

            try (InputStream in = getResource("config.yml")) {
                if (configFile.exists()) {
                    YamlConfiguration curr = YamlConfiguration.loadConfiguration(configFile);
                    YamlConfiguration resrc = YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8));
                    for (String key: resrc.getKeys(true)) {
                        if (!curr.contains(key)) {
                            curr.set(key, resrc.get(key));
                        }
                    }

                    curr.save(configFile);
                    Utils.log("New config lines have been added!");
                } else {
                    if (in != null) {
                        Files.copy(in, configFile.toPath());
                        Utils.log("Default configuration has been loaded!");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setConfig(YamlConfiguration.loadConfiguration(configFile));

        set = new MOTDSettings(new String[]{config.getString("motd-line-1"), config.getString("motd-line-2")}, config.getBoolean("spoof-playercount"), config.getInt("spoofed-count"), config.getBoolean("spoof-maxcount"), config.getInt("spoofed-maxcount"), config.getStringList("playercount-hover"), config.getString("icon-file"));
        //brand = getConfig().getString("brand-name");
        blocked.clear();
        blocked.addAll(getConfig().getStringList("blocked-commands"));
        help.clear();
        help.addAll(getConfig().getStringList("help-content"));

        blockedNoSlash.clear();
        for (String s : blocked) {
            if (s.charAt(0) == '/') {
                StringBuilder sb = new StringBuilder(s);
                sb.deleteCharAt(0);
                blockedNoSlash.add(sb.toString());
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public String name() {
        return "ServerVeil";
    }

    @Override
    public String ver() {
        return "1.0";
    }

    @Override
    public String dlLink() {
        return "https://modrinth.com/plugin/serverveil";
    }

    @Override
    public String prefix() {
        return ChatColor.translateAlternateColorCodes('&', "&r&8[&6ServerVeil&8] » &r");
    }
}
