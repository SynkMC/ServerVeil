package cc.synkdev.serverveil;

import cc.synkdev.serverveil.commands.VeilCmd;
import cc.synkdev.serverveil.managers.CommandBlockerListener;
import cc.synkdev.serverveil.managers.Lang;
import cc.synkdev.synkLibs.bukkit.SynkLibs;
import cc.synkdev.synkLibs.bukkit.Utils;
import cc.synkdev.synkLibs.components.SynkPlugin;
import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ServerVeil extends JavaPlugin implements SynkPlugin {
    @Getter private static ServerVeil instance;
    public List<String> help = new ArrayList<>();
    public List<String> blocked = new ArrayList<>();
    public List<String> blockedNoSlash = new ArrayList<>();
    public HashMap<String, String> langMap = new HashMap<>();
    //public String brand;
    @Getter @Setter private FileConfiguration config;
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfig();

        Lang.init();

        new Metrics(this, 23389);

        SynkLibs.setSpl(this);
        Utils.checkUpdate(this, this);

        Bukkit.getPluginManager().registerEvents(new CommandBlockerListener(), this);

        /*
        Will be added in a future update
        ProtocolManager pM = ProtocolLibrary.getProtocolManager();

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
