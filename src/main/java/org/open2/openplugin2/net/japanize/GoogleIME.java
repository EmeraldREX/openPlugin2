//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.net.japanize;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Iterator;

public class GoogleIME {
    protected GoogleIME() {
    }

    public static String parseJson(String json) {
        StringBuilder result = new StringBuilder();
        Iterator var3 = ((JsonArray)(new Gson()).fromJson(json, JsonArray.class)).iterator();

        while(var3.hasNext()) {
            JsonElement response = (JsonElement)var3.next();
            result.append(response.getAsJsonArray().get(1).getAsJsonArray().get(0).getAsString());
        }

        return result.toString();
    }
}
