package org.open2.openplugin2.net;

import java.io.IOException;
import java.io.InputStream;

public class LoadText {
    public String loadtext(String s) {
        String lines = "";
        ClassLoader classLoader = LoadText.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(String.valueOf(s) + ".txt");
        if (inputStream != null) {
            try {
                Exception exception2, exception1 = null;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return "エラー　文章";
        }
        return lines;
    }
}
