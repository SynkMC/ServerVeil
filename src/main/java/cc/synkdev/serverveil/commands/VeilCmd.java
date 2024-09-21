package cc.synkdev.serverveil.commands;

import cc.synkdev.serverveil.ServerVeil;
import cc.synkdev.serverveil.managers.Lang;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@CommandAlias("serverveil|sv")
public class VeilCmd extends BaseCommand {
    private final ServerVeil core = ServerVeil.getInstance();
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("reload")
    @CommandPermission("serverveil.command.reload")
    @Description("Reload the plugin configuration")
    public void onReload(CommandSender sender) {
        core.setConfig(YamlConfiguration.loadConfiguration(new File(core.getDataFolder(), "config.yml")));
        Lang.read();
        sender.sendMessage(core.prefix()+ ChatColor.GREEN+"The plugin has been reloaded! Some changes might require a server restart to take effect.");
    }
}
