/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public final class ZTQFNQRH {
    private static int a = 923;
    private static byte b = (byte)-99;
    private static int c = -388;
    private static boolean d;
    private static final char[] e;

    /*
     * Unable to fully structure code
     */
    public static long a(String var0) {
        var5_1 = MBMGIXGO.L;
        var1_2 = 0L;
        var3_3 = 0;
        if (!var5_1) ** GOTO lbl20
        do {
            block7: {
                block8: {
                    block6: {
                        var4_4 = var0.charAt(var3_3);
                        var1_2 *= 37L;
                        if (var4_4 < 'A' || var4_4 > 'Z') break block6;
                        var1_2 += (long)('\u0001' + var4_4 - 65);
                        if (!var5_1) break block7;
                    }
                    if (var4_4 < 'a' || var4_4 > 'z') break block8;
                    var1_2 += (long)('\u0001' + var4_4 - 97);
                    if (!var5_1) break block7;
                }
                if (var4_4 >= '0' && var4_4 <= '9') {
                    var1_2 += (long)(27 + var4_4 - 48);
                }
            }
            ++var3_3;
lbl20:
            // 2 sources

            if (var3_3 >= var0.length()) ** GOTO lbl25
        } while (var3_3 < 12);
        if (!var5_1) ** GOTO lbl25
        do {
            var1_2 /= 37L;
lbl25:
            // 3 sources

        } while (var1_2 % 37L == 0L && var1_2 != 0L);
        return var1_2;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String a(long l, byte by) {
        try {
            if (by != b) {
                throw new NullPointerException();
            }
            if (l <= 0L || l >= 6582952005840035281L) {
                return "invalid_name";
            }
            if (l % 37L == 0L) {
                return "invalid_name";
            }
            int n = 0;
            char[] cArray = new char[12];
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !MBMGIXGO.L) continue;
                long l2 = l;
                cArray[11 - n++] = e[(int)(l2 - (l /= 37L) * 37L)];
            } while (l != 0L);
            return new String(cArray, 12 - n, n);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("81570, " + l + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static long a(byte by, String string) {
        try {
            string = string.toUpperCase();
            long l = 0L;
            int n = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !MBMGIXGO.L) continue;
                l = l * 61L + (long)string.charAt(n) - 32L;
                l = l + (l >> 56) & 0xFFFFFFFFFFFFFFL;
                ++n;
            } while (n < string.length());
            if (by != 1) {
                d = !d;
            }
            return l;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("27119, " + by + ", " + string + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static String a(int n, boolean bl) {
        try {
            if (!bl) {
                throw new NullPointerException();
            }
            return String.valueOf(n >> 24 & 0xFF) + "." + (n >> 16 & 0xFF) + "." + (n >> 8 & 0xFF) + "." + (n & 0xFF);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("3088, " + n + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String a(int n, String string) {
        try {
            if (n != -45804) {
                a = -410;
            }
            if (string.length() <= 0) {
                return string;
            }
            char[] cArray = string.toCharArray();
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !MBMGIXGO.L) continue;
                if (cArray[n2] == '_') {
                    cArray[n2] = 32;
                    if (n2 + 1 < cArray.length && cArray[n2 + 1] >= 'a' && cArray[n2 + 1] <= 'z') {
                        cArray[n2 + 1] = (char)(cArray[n2 + 1] + 65 - 97);
                    }
                }
                ++n2;
            } while (n2 < cArray.length);
            if (cArray[0] >= 'a' && cArray[0] <= 'z') {
                cArray[0] = (char)(cArray[0] + 65 - 97);
            }
            return new String(cArray);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("64121, " + n + ", " + string + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String a(String string, int n) {
        try {
            if (n != 0) {
                throw new NullPointerException();
            }
            StringBuffer stringBuffer = new StringBuffer();
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !MBMGIXGO.L) continue;
                stringBuffer.append("*");
                ++n2;
            } while (n2 < string.length());
            return stringBuffer.toString();
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("97205, " + string + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    static {
        e = new char[]{'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    }
}

