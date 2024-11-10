//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.event;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class SkinUtils {
    SkinUtils() {
    }

    public static BufferedImage getPlayerSkin(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)(new URL(url)).openConnection();
        connection.setRequestMethod("GET");

        try {
            Throwable var2 = null;
            Object var3 = null;

            try {
                InputStream inputStream = connection.getInputStream();

                BufferedImage var10000;
                try {
                    var10000 = ImageIO.read(inputStream);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                }

                return var10000;
            } catch (Throwable var12) {
                if (var2 == null) {
                    var2 = var12;
                } else if (var2 != var12) {
                    var2.addSuppressed(var12);
                }

                throw var2;
            }
        } catch (Throwable var13) {
            return null;
        }
    }
}
