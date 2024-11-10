//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.open2.openplugin2.net.japanize;

public enum JapanizeType {
    NONE("none"),
    KANA("kana"),
    GOOGLE_IME("googleime");

    private String id;

    private JapanizeType(String id) {
        this.id = id;
    }

    public String toString() {
        return this.id;
    }

    public static JapanizeType fromID(String id, JapanizeType def) {
        if (id == null) {
            return def;
        } else {
            JapanizeType[] var5;
            int var4 = (var5 = values()).length;

            for(int var3 = 0; var3 < var4; ++var3) {
                JapanizeType type = var5[var3];
                if (type.id.equalsIgnoreCase(id)) {
                    return type;
                }
            }

            return def;
        }
    }
}
