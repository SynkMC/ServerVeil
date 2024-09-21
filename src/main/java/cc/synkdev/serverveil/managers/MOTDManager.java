package cc.synkdev.serverveil.managers;

import cc.synkdev.serverveil.ServerVeil;
import cc.synkdev.serverveil.Util;
import cc.synkdev.serverveil.objects.MOTDSettings;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class MOTDManager {
    public static void handle(PacketEvent event, MOTDSettings set) {
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

        File icon = new File(ServerVeil.getInstance().getDataFolder(),set.getIcon()+".png");
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
}
