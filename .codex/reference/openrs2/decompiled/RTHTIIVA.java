/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public final class RTHTIIVA {
    private static int a;
    private static boolean b;
    private static boolean c;
    public static char[] d;
    private static MBMGIXGO e;
    private static char[] f;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String a(int n, boolean bl, MBMGIXGO mBMGIXGO) {
        boolean bl2 = MBMGIXGO.L;
        try {
            int n2;
            int n3;
            int n4 = 0;
            int n5 = -1;
            int n6 = 0;
            boolean bl3 = true;
            do {
                block18: {
                    block16: {
                        block17: {
                            block15: {
                                block13: {
                                    block14: {
                                        if (bl3 && !(bl3 = false) && !bl2) continue;
                                        n3 = mBMGIXGO.c();
                                        n2 = n3 >> 4 & 0xF;
                                        if (n5 != -1) break block13;
                                        if (n2 >= 13) break block14;
                                        RTHTIIVA.d[n4++] = f[n2];
                                        if (!bl2) break block15;
                                    }
                                    n5 = n2;
                                    if (!bl2) break block15;
                                }
                                RTHTIIVA.d[n4++] = f[(n5 << 4) + n2 - 195];
                                n5 = -1;
                            }
                            n2 = n3 & 0xF;
                            if (n5 != -1) break block16;
                            if (n2 >= 13) break block17;
                            RTHTIIVA.d[n4++] = f[n2];
                            if (!bl2) break block18;
                        }
                        n5 = n2;
                        if (!bl2) break block18;
                    }
                    RTHTIIVA.d[n4++] = f[(n5 << 4) + n2 - 195];
                    n5 = -1;
                }
                ++n6;
            } while (n6 < n);
            n3 = 1;
            n2 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl2) continue;
                char c = d[n2];
                if (n3 != 0 && c >= 'a' && c <= 'z') {
                    int n7 = n2;
                    d[n7] = (char)(d[n7] + -32);
                    n3 = 0;
                }
                if (c == '.' || c == '!' || c == '?') {
                    n3 = 1;
                }
                ++n2;
            } while (n2 < n4);
            if (!bl) {
                a = 466;
            }
            return new String(d, 0, n4);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("62664, " + n + ", " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(String string, boolean bl, MBMGIXGO mBMGIXGO) {
        boolean bl2 = MBMGIXGO.L;
        try {
            if (string.length() > 80) {
                string = string.substring(0, 80);
            }
            string = string.toLowerCase();
            int n = -1;
            int n2 = 0;
            boolean bl3 = true;
            do {
                block13: {
                    int n3;
                    block14: {
                        block11: {
                            block12: {
                                if (bl3 && !(bl3 = false) && !bl2) continue;
                                char c = string.charAt(n2);
                                n3 = 0;
                                int n4 = 0;
                                boolean bl4 = true;
                                do {
                                    if (bl4 && !(bl4 = false) && !bl2) continue;
                                    if (c == f[n4]) {
                                        n3 = n4;
                                        if (!bl2) break;
                                    }
                                    ++n4;
                                } while (n4 < f.length);
                                if (n3 > 12) {
                                    n3 += 195;
                                }
                                if (n != -1) break block11;
                                if (n3 >= 13) break block12;
                                n = n3;
                                if (!bl2) break block13;
                            }
                            mBMGIXGO.a(n3);
                            if (!bl2) break block13;
                        }
                        if (n3 >= 13) break block14;
                        mBMGIXGO.a((n << 4) + n3);
                        n = -1;
                        if (!bl2) break block13;
                    }
                    mBMGIXGO.a((n << 4) + (n3 >> 4));
                    n = n3 & 0xF;
                }
                ++n2;
            } while (n2 < string.length());
            if (!bl) {
                a = -452;
            }
            if (n == -1) return;
            mBMGIXGO.a(n << 4);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("94520, " + string + ", " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static String a(String string, int n) {
        try {
            RTHTIIVA.e.z = 0;
            RTHTIIVA.a(string, c, e);
            int n2 = RTHTIIVA.e.z;
            RTHTIIVA.e.z = 0;
            if (n != 0) {
                b = !b;
            }
            String string2 = RTHTIIVA.a(n2, true, e);
            return string2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("98483, " + string + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    static {
        c = true;
        d = new char[100];
        e = new MBMGIXGO(new byte[100], 891);
        f = new char[]{' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\u00a3', '$', '%', '\"', '[', ']'};
    }
}

