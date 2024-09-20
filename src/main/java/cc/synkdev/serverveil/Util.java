package cc.synkdev.serverveil;

import org.bukkit.ChatColor;

public class Util {
    public static String convertColors(String s) {
        if (s == null) return null;
        s = s.replaceAll("<black>", "&0");
        s = s.replaceAll("<dark_blue>", "&1");
        s = s.replaceAll("<dark_green>", "&2");
        s = s.replaceAll("<dark_aqua>", "&3");
        s = s.replaceAll("<dark_red>", "&4");
        s = s.replaceAll("<dark_purple>", "&5");
        s = s.replaceAll("<gold>", "&6");
        s = s.replaceAll("<gray>", "&7");
        s = s.replaceAll("<dark_gray>", "&8");
        s = s.replaceAll("<blue>", "&9");
        s = s.replaceAll("<green>", "&a");
        s = s.replaceAll("<aqua>", "&b");
        s = s.replaceAll("<red>", "&c");
        s = s.replaceAll("<light_purple>", "&d");
        s = s.replaceAll("<yellow>", "&e");
        s = s.replaceAll("<white>", "&f");
        s = s.replaceAll("<magic>", "&k");
        s = s.replaceAll("<bold>", "&l");
        s = s.replaceAll("<strikethrough>", "&m");
        s = s.replaceAll("<undernline>", "&n");
        s = s.replaceAll("<italic>", "&o");
        s = s.replaceAll("<reset>", "&r");
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static String convertColorsSymbol(String s) {
        if (s == null) return null;
        s = s.replaceAll("<black>", "§0");
        s = s.replaceAll("<dark_blue>", "§1");
        s = s.replaceAll("<dark_green>", "§2");
        s = s.replaceAll("<dark_aqua>", "§3");
        s = s.replaceAll("<dark_red>", "§4");
        s = s.replaceAll("<dark_purple>", "§5");
        s = s.replaceAll("<gold>", "§6");
        s = s.replaceAll("<gray>", "§7");
        s = s.replaceAll("<dark_gray>", "§8");
        s = s.replaceAll("<blue>", "§9");
        s = s.replaceAll("<green>", "§a");
        s = s.replaceAll("<aqua>", "§b");
        s = s.replaceAll("<red>", "§c");
        s = s.replaceAll("<light_purple>", "§d");
        s = s.replaceAll("<yellow>", "§e");
        s = s.replaceAll("<white>", "§f");
        s = s.replaceAll("<magic>", "§k");
        s = s.replaceAll("<bold>", "§l");
        s = s.replaceAll("<strikethrough>", "§m");
        s = s.replaceAll("<undernline>", "§n");
        s = s.replaceAll("<italic>", "§o");
        s = s.replaceAll("<reset>", "§r");
        return s;
    }
}
