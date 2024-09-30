package cc.synkdev.serverveil;

import cc.synkdev.synkLibs.bukkit.Utils;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static String convertColors(String s) {
        if (s == null) return null;

        Pattern pattern = Pattern.compile("<(#[A-Fa-f0-9]{6}|black|dark_blue|dark_green|dark_aqua|dark_red|dark_purple|gold|gray|dark_gray|blue|green|aqua|red|light_purple|yellow|white|magic|bold|strikethrough|underline|italic|reset)>");
        Matcher matcher = pattern.matcher(s);

        Map<String, String> colorMap = getStringStringMap();

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String match = matcher.group(1);

            Utils.log("Match: "+match);
            if (match.startsWith("#")) {
                matcher.appendReplacement(sb, "&"+match);
            } else if (colorMap.containsKey(match)) {
                matcher.appendReplacement(sb, colorMap.get(match));
            }
        }
        matcher.appendTail(sb);

        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }

    private static @NotNull Map<String, String> getStringStringMap() {
        Map<String, String> colorMap = new HashMap<>();
        colorMap.put("black", "&0");
        colorMap.put("dark_blue", "&1");
        colorMap.put("dark_green", "&2");
        colorMap.put("dark_aqua", "&3");
        colorMap.put("dark_red", "&4");
        colorMap.put("dark_purple", "&5");
        colorMap.put("gold", "&6");
        colorMap.put("gray", "&7");
        colorMap.put("dark_gray", "&8");
        colorMap.put("blue", "&9");
        colorMap.put("green", "&a");
        colorMap.put("aqua", "&b");
        colorMap.put("red", "&c");
        colorMap.put("light_purple", "&d");
        colorMap.put("yellow", "&e");
        colorMap.put("white", "&f");
        colorMap.put("magic", "&k");
        colorMap.put("bold", "&l");
        colorMap.put("strikethrough", "&m");
        colorMap.put("underline", "&n");
        colorMap.put("italic", "&o");
        colorMap.put("reset", "&r");
        return colorMap;
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
