package cc.synkdev.serverveil.commands;

import cc.synkdev.serverveil.ServerVeil;
import cc.synkdev.serverveil.managers.Lang;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

@CommandAlias("ab|actionbar")
public class ActionBarCmd extends BaseCommand {
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

    @Subcommand("toggle")
    @CommandPermission("serverveil.command.actionbar.toggle")
    @Description("Toggle the actionbar on/off")
    public void onToggle(Player p) {
        core.getPlayerManager().toggle(p);
    }

    @Subcommand("send")
    @CommandPermission("serverveil.command.actionbar.toggle")
    @Description("Send a custom message to a user for a certain duration")
    @Syntax("[player] [time in ms] [message]")
    public void onSend(CommandSender sender, String[] args) {
        if (args.length<3) {
            sender.sendMessage(core.prefix()+ChatColor.RED+"Usage: /ab send [player] [time] [message]");
            return;
        }

        Player p = Bukkit.getPlayerExact(args[0]);
        if (p == null || !p.isOnline()) {
            sender.sendMessage(core.prefix()+ChatColor.RED+"This player isn't online!");
            return;
        }

        long time;
        try {
            time = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(core.prefix()+ChatColor.RED+"Invalid number format!");
            return;
        }

        String message;
        if (args.length == 3) {
            message = args[2];
        } else {
            StringBuilder sb = new StringBuilder();
            int index = 2;
            for (int i = 2; i < args.length-1; i++) {
                sb.append(args[i]).append(" ");
                index = i;
            }
            sb.append(args[index+1]);
            message = sb.toString();
        }

        core.getPlayerManager().add(p, message, time);
        sender.sendMessage(core.prefix()+ChatColor.GREEN+Lang.translate("sent-ab", message, p.getName(), String.valueOf(time)));
    }
}
