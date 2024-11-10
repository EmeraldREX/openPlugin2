//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.net.japanize;

import com.google.common.io.CharStreams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class IMEConverter {
    private static final String SOCIAL_IME_URL = "https://www.social-ime.com/api/?string=";
    private static final String GOOGLE_IME_URL = "https://www.google.com/transliterate?langpair=ja-Hira|ja&text=";

    public IMEConverter() {
    }

    public static String convByGoogleIME(String org) {
        return conv(org, true);
    }

    /** @deprecated */
    @Deprecated
    public static String convBySocialIME(String org) {
        return conv(org, false);
    }

    private static String conv(String org, boolean isGoogleIME) {
        if (org.length() == 0) {
            return "";
        } else {
            HttpURLConnection urlconn = null;
            BufferedReader reader = null;

            try {
                String baseurl;
                String encode;
                if (isGoogleIME) {
                    baseurl = "https://www.google.com/transliterate?langpair=ja-Hira|ja&text=" + URLEncoder.encode(org, "UTF-8");
                    encode = "UTF-8";
                } else {
                    baseurl = "https://www.social-ime.com/api/?string=" + URLEncoder.encode(org, "UTF-8");
                    encode = "EUC_JP";
                }

                URL url = new URL(baseurl);
                urlconn = (HttpURLConnection)url.openConnection();
                urlconn.setRequestMethod("GET");
                urlconn.setInstanceFollowRedirects(false);
                urlconn.connect();
                reader = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), encode));
                String json = CharStreams.toString(reader);
                String parsed = GoogleIME.parseJson(json);
                String var10 = parsed;
                return var10;
            } catch (MalformedURLException var22) {
                var22.printStackTrace();
            } catch (ProtocolException var23) {
                var23.printStackTrace();
            } catch (IOException var24) {
                var24.printStackTrace();
            } finally {
                if (urlconn != null) {
                    urlconn.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var21) {
                    }
                }

            }

            return "";
        }
    }

    public static void main(String[] args) {
        String testee = "sonnnakotohanak(ry)";
        System.out.println("original : " + testee);
        System.out.println("kana : " + YukiKanaConverter.conv(testee));
        System.out.println("GoogleIME : " + convByGoogleIME(YukiKanaConverter.conv(testee)));
        System.out.println("SocialIME : " + convBySocialIME(YukiKanaConverter.conv(testee)));
    }
}
