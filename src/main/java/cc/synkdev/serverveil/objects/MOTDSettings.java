package cc.synkdev.serverveil.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MOTDSettings {
    private String[] description;
    private Boolean spoofCount;
    private int spoofedCount;
    private Boolean spoofMax;
    private int spoofedMax;
    private List<String> hover;
    private String icon;
    public MOTDSettings(String[] description, Boolean spoofCount, int spoofedCount, Boolean spoofMax, int spoofedMax, List<String> hover, String icon) {
        this.description = description;
        this.spoofCount = spoofCount;
        this.spoofedCount = spoofedCount;
        this.spoofMax = spoofMax;
        this.spoofedMax = spoofedMax;
        this.hover = hover;
        this.icon = icon;
    }
}
