/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public final class RKAYAFDQ {
    private static int a = 9;
    private static boolean b;
    private static int c;
    private static int d;
    private static boolean e;
    private static int f;
    private static byte g;
    private static int h;
    private static boolean i;
    private static int j;
    private static int k;
    private static byte l;
    private static int m;
    private static boolean n;
    private static int[] o;
    private static char[][] p;
    private static byte[][][] q;
    private static char[][] r;
    private static char[][] s;
    private static int[] t;
    private static final String[] u;
    public static boolean v;

    public static final void a(XTGLDHGX xTGLDHGX) {
        boolean bl = v;
        MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("fragmentsenc.txt", null), 891);
        MBMGIXGO mBMGIXGO2 = new MBMGIXGO(xTGLDHGX.a("badenc.txt", null), 891);
        MBMGIXGO mBMGIXGO3 = new MBMGIXGO(xTGLDHGX.a("domainenc.txt", null), 891);
        MBMGIXGO mBMGIXGO4 = new MBMGIXGO(xTGLDHGX.a("tldlist.txt", null), 891);
        RKAYAFDQ.a(mBMGIXGO, mBMGIXGO2, mBMGIXGO3, mBMGIXGO4);
        if (PKVMXVTO.e) {
            v = !bl;
        }
    }

    private static final void a(MBMGIXGO mBMGIXGO, MBMGIXGO mBMGIXGO2, MBMGIXGO mBMGIXGO3, MBMGIXGO mBMGIXGO4) {
        RKAYAFDQ.a(9121, mBMGIXGO2);
        RKAYAFDQ.a(mBMGIXGO3, (byte)-28);
        RKAYAFDQ.a(mBMGIXGO, true);
        RKAYAFDQ.a((byte)2, mBMGIXGO4);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(byte by, MBMGIXGO mBMGIXGO) {
        boolean bl = v;
        try {
            int n = mBMGIXGO.h();
            s = new char[n][];
            t = new int[n];
            if (by != 2) {
                return;
            }
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                RKAYAFDQ.t[n2] = mBMGIXGO.c();
                char[] cArray = new char[mBMGIXGO.c()];
                int n3 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    cArray[n3] = (char)mBMGIXGO.c();
                    ++n3;
                } while (n3 < cArray.length);
                RKAYAFDQ.s[n2] = cArray;
                ++n2;
            } while (n2 < n);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("65552, " + by + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final void a(int n, MBMGIXGO mBMGIXGO) {
        try {
            if (n != 9121) {
                RKAYAFDQ.n = !RKAYAFDQ.n;
            }
            int n2 = mBMGIXGO.h();
            p = new char[n2][];
            q = new byte[n2][][];
            RKAYAFDQ.a(mBMGIXGO, p, true, q);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("10223, " + n + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final void a(MBMGIXGO mBMGIXGO, byte by) {
        try {
            int n = mBMGIXGO.h();
            r = new char[n][];
            if (by != -28) {
                return;
            }
            RKAYAFDQ.a(r, mBMGIXGO, -490);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("41706, " + mBMGIXGO + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(MBMGIXGO mBMGIXGO, boolean bl) {
        try {
            o = new int[mBMGIXGO.h()];
            int n = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !v) continue;
                RKAYAFDQ.o[n] = mBMGIXGO.e();
                ++n;
            } while (n < o.length);
            if (bl) return;
            d = 167;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("99878, " + mBMGIXGO + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(MBMGIXGO mBMGIXGO, char[][] cArray, boolean bl, byte[][][] byArray) {
        boolean bl2 = v;
        try {
            int n;
            if (!bl) {
                n = 1;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl2) continue;
                    ++n;
                } while (n > 0);
            }
            n = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl2) continue;
                char[] cArray2 = new char[mBMGIXGO.c()];
                int n2 = 0;
                boolean bl5 = true;
                do {
                    if (bl5 && !(bl5 = false) && !bl2) continue;
                    cArray2[n2] = (char)mBMGIXGO.c();
                    ++n2;
                } while (n2 < cArray2.length);
                cArray[n] = cArray2;
                byte[][] byArray2 = new byte[mBMGIXGO.c()][2];
                int n3 = 0;
                boolean bl6 = true;
                do {
                    if (bl6 && !(bl6 = false) && !bl2) continue;
                    byArray2[n3][0] = (byte)mBMGIXGO.c();
                    byArray2[n3][1] = (byte)mBMGIXGO.c();
                    ++n3;
                } while (n3 < byArray2.length);
                if (byArray2.length > 0) {
                    byArray[n] = byArray2;
                }
                ++n;
            } while (n < cArray.length);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("31244, " + mBMGIXGO + ", " + cArray + ", " + bl + ", " + byArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(char[][] cArray, MBMGIXGO mBMGIXGO, int n) {
        boolean bl = v;
        try {
            if (n >= 0) {
                return;
            }
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                char[] cArray2 = new char[mBMGIXGO.c()];
                int n3 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    cArray2[n3] = (char)mBMGIXGO.c();
                    ++n3;
                } while (n3 < cArray2.length);
                cArray[n2] = cArray2;
                ++n2;
            } while (n2 < cArray.length);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("79837, " + cArray + ", " + mBMGIXGO + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(boolean bl, char[] cArray) {
        boolean bl2 = v;
        try {
            int n = 0;
            int n2 = 0;
            boolean bl3 = true;
            do {
                block10: {
                    block9: {
                        if (bl3 && !(bl3 = false) && !bl2) continue;
                        if (!RKAYAFDQ.a(cArray[n2], f)) break block9;
                        cArray[n] = cArray[n2];
                        if (!bl2) break block10;
                    }
                    cArray[n] = 32;
                }
                if (n == 0 || cArray[n] != ' ' || cArray[n - 1] != ' ') {
                    ++n;
                }
                ++n2;
            } while (n2 < cArray.length);
            if (bl) {
                return;
            }
            int n3 = n;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl2) continue;
                cArray[n3] = 32;
                ++n3;
            } while (n3 < cArray.length);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("14393, " + bl + ", " + cArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final boolean a(char c, int n) {
        try {
            if (n != 0) {
                throw new NullPointerException();
            }
            return c >= ' ' && c <= '\u007f' || c == ' ' || c == '\n' || c == '\t' || c == '\u00a3' || c == '\u20ac';
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("65557, " + c + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final String a(String string, int n) {
        boolean bl = v;
        try {
            long l = System.currentTimeMillis();
            char[] cArray = string.toCharArray();
            RKAYAFDQ.a(false, cArray);
            String string2 = new String(cArray).trim();
            cArray = string2.toLowerCase().toCharArray();
            String string3 = string2.toLowerCase();
            RKAYAFDQ.b(false, cArray);
            RKAYAFDQ.a(cArray, true);
            if (n != 0) {
                throw new NullPointerException();
            }
            RKAYAFDQ.a((byte)0, cArray);
            RKAYAFDQ.a(cArray, -511);
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false)) {
                    if (!bl) continue;
                    PKVMXVTO.e = !PKVMXVTO.e;
                }
                int n3 = -1;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    char[] cArray2 = u[n2].toCharArray();
                    int n4 = 0;
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && !bl) continue;
                        cArray[n4 + n3] = cArray2[n4];
                        ++n4;
                    } while (n4 < cArray2.length);
                } while ((n3 = string3.indexOf(u[n2], n3 + 1)) != -1);
                ++n2;
            } while (n2 < u.length);
            RKAYAFDQ.a(string2.toCharArray(), 2, cArray);
            RKAYAFDQ.a(0, cArray);
            long l2 = System.currentTimeMillis();
            return new String(cArray).trim();
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("44439, " + string + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(char[] cArray, int n, char[] cArray2) {
        try {
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !v) continue;
                if (cArray2[n2] != '*' && RKAYAFDQ.b(true, cArray[n2])) {
                    cArray2[n2] = cArray[n2];
                }
                ++n2;
            } while (n2 < cArray.length);
            if (n == 2) return;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("66493, " + cArray + ", " + n + ", " + cArray2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(int n, char[] cArray) {
        boolean bl = v;
        try {
            boolean bl2 = true;
            int n2 = 0;
            boolean bl3 = true;
            do {
                block7: {
                    block5: {
                        char c;
                        block6: {
                            if (bl3 && !(bl3 = false) && !bl) continue;
                            c = cArray[n2];
                            if (!RKAYAFDQ.c(c, -46837)) break block5;
                            if (!bl2) break block6;
                            if (!RKAYAFDQ.e(c, 1)) break block7;
                            bl2 = false;
                            if (!bl) break block7;
                        }
                        if (!RKAYAFDQ.b(true, c)) break block7;
                        cArray[n2] = (char)(c + 97 - 65);
                        if (!bl) break block7;
                    }
                    bl2 = true;
                }
                ++n2;
            } while (n2 < cArray.length);
            if (n == 0) return;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("29891, " + n + ", " + cArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(char[] cArray, boolean bl) {
        boolean bl2 = v;
        try {
            if (!bl) {
                return;
            }
            int n = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl2) continue;
                int n2 = p.length - 1;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && !bl2) continue;
                    RKAYAFDQ.a(q[n2], cArray, h, p[n2]);
                    --n2;
                } while (n2 >= 0);
                ++n;
            } while (n < 2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("1109, " + cArray + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(byte by, char[] cArray) {
        try {
            char[] cArray2 = (char[])cArray.clone();
            char[] cArray3 = new char[]{'(', 'a', ')'};
            RKAYAFDQ.a(null, cArray2, h, cArray3);
            char[] cArray4 = (char[])cArray.clone();
            char[] cArray5 = new char[]{'d', 'o', 't'};
            RKAYAFDQ.a(null, cArray4, h, cArray5);
            int n = r.length - 1;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !v) continue;
                RKAYAFDQ.a(29200, cArray, r[n], cArray4, cArray2);
                --n;
            } while (n >= 0);
            if (by == 0) return;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("73832, " + by + ", " + cArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(int n, char[] cArray, char[] cArray2, char[] cArray3, char[] cArray4) {
        boolean bl = v;
        try {
            if (n != 29200) {
                return;
            }
            if (cArray2.length > cArray.length) {
                return;
            }
            int n2 = 1;
            int n3 = 0;
            boolean bl2 = true;
            do {
                char c;
                char c2;
                int n4;
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n5 = n3;
                int n6 = 0;
                n2 = 1;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    n4 = 0;
                    c2 = cArray[n5];
                    c = '\u0000';
                    if (n5 + 1 < cArray.length) {
                        c = cArray[n5 + 1];
                    }
                    if (n6 < cArray2.length && (n4 = RKAYAFDQ.a(43, c2, cArray2[n6], c)) > 0) {
                        n5 += n4;
                        ++n6;
                        if (!bl) continue;
                    }
                    if (n6 == 0) break;
                    n4 = RKAYAFDQ.a(43, c2, cArray2[n6 - 1], c);
                    if (n4 > 0) {
                        n5 += n4;
                        if (n6 != 1) continue;
                        ++n2;
                        if (!bl) continue;
                    }
                    if (n6 >= cArray2.length || !RKAYAFDQ.a(-12789, c2)) break;
                    ++n5;
                } while (n5 < cArray.length);
                if (n6 >= cArray2.length) {
                    n4 = 0;
                    c2 = RKAYAFDQ.a(cArray, 4, cArray4, n3);
                    c = RKAYAFDQ.a(g, cArray3, n5 - 1, cArray);
                    if (c2 > '\u0002' || c > '\u0002') {
                        n4 = 1;
                    }
                    if (n4 != 0) {
                        int n7 = n3;
                        boolean bl4 = true;
                        do {
                            if (bl4 && !(bl4 = false) && !bl) continue;
                            cArray[n7] = 42;
                            ++n7;
                        } while (n7 < n5);
                    }
                }
                n3 += n2;
            } while (n3 <= cArray.length - cArray2.length);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("48695, " + n + ", " + cArray + ", " + cArray2 + ", " + cArray3 + ", " + cArray4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private static final int a(char[] var0, int var1_1, char[] var2_2, int var3_3) {
        var7_4 = RKAYAFDQ.v;
        try {
            if (var1_1 < 4 || var1_1 > 4) {
                return 2;
            }
            if (var3_3 == 0) {
                return 2;
            }
            var4_5 = var3_3 - 1;
            if (!var7_4) ** GOTO lbl13
            while (RKAYAFDQ.a(-12789, var0[var4_5])) {
                if (var0[var4_5] == '@') {
                    return 3;
                }
                --var4_5;
lbl13:
                // 2 sources

                if (var4_5 >= 0) continue;
            }
            var5_7 = 0;
            var6_8 = var3_3 - 1;
            if (!var7_4) ** GOTO lbl21
            while (RKAYAFDQ.a(-12789, var2_2[var6_8])) {
                if (var2_2[var6_8] == '*') {
                    ++var5_7;
                }
                --var6_8;
lbl21:
                // 2 sources

                if (var6_8 >= 0) continue;
            }
            if (var5_7 >= 3) {
                return 4;
            }
            if (RKAYAFDQ.a(-12789, var0[var3_3 - 1])) {
                return 1;
            }
            return 0;
        }
        catch (RuntimeException var4_6) {
            signlink.reporterror("87152, " + var0 + ", " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_6.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private static final int a(byte var0, char[] var1_1, int var2_2, char[] var3_3) {
        var7_4 = RKAYAFDQ.v;
        try {
            if (var2_2 + 1 == var3_3.length) {
                return 2;
            }
            var4_5 = var2_2 + 1;
            if (!var7_4) ** GOTO lbl11
            while (RKAYAFDQ.a(-12789, var3_3[var4_5])) {
                if (var3_3[var4_5] == '.' || var3_3[var4_5] == ',') {
                    return 3;
                }
                ++var4_5;
lbl11:
                // 2 sources

                if (var4_5 < var3_3.length) continue;
            }
            if (var0 != -117) {
                return RKAYAFDQ.h;
            }
            var5_7 = 0;
            var6_8 = var2_2 + 1;
            if (!var7_4) ** GOTO lbl21
            while (RKAYAFDQ.a(-12789, var1_1[var6_8])) {
                if (var1_1[var6_8] == '*') {
                    ++var5_7;
                }
                ++var6_8;
lbl21:
                // 2 sources

                if (var6_8 < var3_3.length) continue;
            }
            if (var5_7 >= 3) {
                return 4;
            }
            if (RKAYAFDQ.a(-12789, var3_3[var2_2 + 1])) {
                return 1;
            }
            return 0;
        }
        catch (RuntimeException var4_6) {
            signlink.reporterror("50081, " + var0 + ", " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_6.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void b(boolean bl, char[] cArray) {
        try {
            char[] cArray2 = (char[])cArray.clone();
            char[] cArray3 = new char[]{'d', 'o', 't'};
            if (bl) {
                return;
            }
            RKAYAFDQ.a(null, cArray2, h, cArray3);
            char[] cArray4 = (char[])cArray.clone();
            char[] cArray5 = new char[]{'s', 'l', 'a', 's', 'h'};
            RKAYAFDQ.a(null, cArray4, h, cArray5);
            int n = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !v) continue;
                RKAYAFDQ.a(cArray4, s[n], t[n], (byte)51, cArray2, cArray);
                ++n;
            } while (n < s.length);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("41909, " + bl + ", " + cArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(char[] cArray, char[] cArray2, int n, byte by, char[] cArray3, char[] cArray4) {
        boolean bl = v;
        try {
            if (cArray2.length > cArray4.length) {
                return;
            }
            int n2 = 1;
            int n3 = 0;
            boolean bl2 = true;
            do {
                block32: {
                    int n4;
                    int n5;
                    int n6;
                    block39: {
                        int n7;
                        block40: {
                            char c;
                            block33: {
                                block34: {
                                    char c2;
                                    int n8;
                                    if (bl2 && !(bl2 = false) && !bl) continue;
                                    int n9 = n3;
                                    int n10 = 0;
                                    n2 = 1;
                                    boolean bl3 = true;
                                    do {
                                        if (bl3 && !(bl3 = false) && !bl) continue;
                                        n8 = 0;
                                        c2 = cArray4[n9];
                                        c = '\u0000';
                                        if (n9 + 1 < cArray4.length) {
                                            c = cArray4[n9 + 1];
                                        }
                                        if (n10 < cArray2.length && (n8 = RKAYAFDQ.a(43, c2, cArray2[n10], c)) > 0) {
                                            n9 += n8;
                                            ++n10;
                                            if (!bl) continue;
                                        }
                                        if (n10 == 0) break;
                                        n8 = RKAYAFDQ.a(43, c2, cArray2[n10 - 1], c);
                                        if (n8 > 0) {
                                            n9 += n8;
                                            if (n10 != 1) continue;
                                            ++n2;
                                            if (!bl) continue;
                                        }
                                        if (n10 >= cArray2.length || !RKAYAFDQ.a(-12789, c2)) break;
                                        ++n9;
                                    } while (n9 < cArray4.length);
                                    if (n10 < cArray2.length) break block32;
                                    n8 = 0;
                                    c2 = RKAYAFDQ.a(36209, cArray4, n3, cArray3);
                                    c = RKAYAFDQ.a(false, cArray4, cArray, n9 - 1);
                                    if (n == 1 && c2 > '\u0000' && c > '\u0000') {
                                        n8 = 1;
                                    }
                                    if (n == 2 && (c2 > '\u0002' && c > '\u0000' || c2 > '\u0000' && c > '\u0002')) {
                                        n8 = 1;
                                    }
                                    if (n == 3 && c2 > '\u0000' && c > '\u0002') {
                                        n8 = 1;
                                    }
                                    boolean bl4 = n == 3 && c2 > '\u0002' && c > '\u0000';
                                    if (n8 == 0) break block32;
                                    n6 = n3;
                                    n5 = n9 - 1;
                                    if (c2 <= '\u0002') break block33;
                                    if (c2 != '\u0004') break block34;
                                    n4 = 0;
                                    n7 = n6 - 1;
                                    boolean bl5 = true;
                                    do {
                                        block36: {
                                            block35: {
                                                if (bl5 && !(bl5 = false) && !bl) continue;
                                                if (n4 == 0) break block35;
                                                if (cArray3[n7] != '*') break;
                                                n6 = n7;
                                                if (!bl) break block36;
                                            }
                                            if (cArray3[n7] == '*') {
                                                n6 = n7;
                                                n4 = 1;
                                            }
                                        }
                                        --n7;
                                    } while (n7 >= 0);
                                }
                                n4 = 0;
                                n7 = n6 - 1;
                                boolean bl6 = true;
                                do {
                                    block38: {
                                        block37: {
                                            if (bl6 && !(bl6 = false) && !bl) continue;
                                            if (n4 == 0) break block37;
                                            if (RKAYAFDQ.a(-12789, cArray4[n7])) break;
                                            n6 = n7;
                                            if (!bl) break block38;
                                        }
                                        if (!RKAYAFDQ.a(-12789, cArray4[n7])) {
                                            n4 = 1;
                                            n6 = n7;
                                        }
                                    }
                                    --n7;
                                } while (n7 >= 0);
                            }
                            if (c <= '\u0002') break block39;
                            if (c != '\u0004') break block40;
                            n4 = 0;
                            n7 = n5 + 1;
                            boolean bl7 = true;
                            do {
                                block42: {
                                    block41: {
                                        if (bl7 && !(bl7 = false) && !bl) continue;
                                        if (n4 == 0) break block41;
                                        if (cArray[n7] != '*') break;
                                        n5 = n7;
                                        if (!bl) break block42;
                                    }
                                    if (cArray[n7] == '*') {
                                        n5 = n7;
                                        n4 = 1;
                                    }
                                }
                                ++n7;
                            } while (n7 < cArray4.length);
                        }
                        n4 = 0;
                        n7 = n5 + 1;
                        boolean bl8 = true;
                        do {
                            block44: {
                                block43: {
                                    if (bl8 && !(bl8 = false) && !bl) continue;
                                    if (n4 == 0) break block43;
                                    if (RKAYAFDQ.a(-12789, cArray4[n7])) break;
                                    n5 = n7;
                                    if (!bl) break block44;
                                }
                                if (!RKAYAFDQ.a(-12789, cArray4[n7])) {
                                    n4 = 1;
                                    n5 = n7;
                                }
                            }
                            ++n7;
                        } while (n7 < cArray4.length);
                    }
                    n4 = n6;
                    boolean bl9 = true;
                    do {
                        if (bl9 && !(bl9 = false) && !bl) continue;
                        cArray4[n4] = 42;
                        ++n4;
                    } while (n4 <= n5);
                }
                n3 += n2;
            } while (n3 <= cArray4.length - cArray2.length);
            if (by == 51) return;
            RKAYAFDQ.n = !RKAYAFDQ.n;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("17314, " + cArray + ", " + cArray2 + ", " + n + ", " + by + ", " + cArray3 + ", " + cArray4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private static final int a(int var0, char[] var1_1, int var2_2, char[] var3_3) {
        var7_4 = RKAYAFDQ.v;
        try {
            if (var2_2 == 0) {
                return 2;
            }
            var4_5 = var2_2 - 1;
            if (!var7_4) ** GOTO lbl11
            while (RKAYAFDQ.a(-12789, var1_1[var4_5])) {
                if (var1_1[var4_5] == ',' || var1_1[var4_5] == '.') {
                    return 3;
                }
                --var4_5;
lbl11:
                // 2 sources

                if (var4_5 >= 0) continue;
            }
            var5_7 = 0;
            var6_8 = var2_2 - 1;
            if (!var7_4) ** GOTO lbl19
            while (RKAYAFDQ.a(-12789, var3_3[var6_8])) {
                if (var3_3[var6_8] == '*') {
                    ++var5_7;
                }
                --var6_8;
lbl19:
                // 2 sources

                if (var6_8 >= 0) continue;
            }
            if (var0 != 36209) {
                v0 = RKAYAFDQ.n = RKAYAFDQ.n == false;
            }
            if (var5_7 >= 3) {
                return 4;
            }
            if (RKAYAFDQ.a(-12789, var1_1[var2_2 - 1])) {
                return 1;
            }
            return 0;
        }
        catch (RuntimeException var4_6) {
            signlink.reporterror("50325, " + var0 + ", " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_6.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private static final int a(boolean var0, char[] var1_1, char[] var2_2, int var3_3) {
        var7_4 = RKAYAFDQ.v;
        try {
            if (var0) {
                RKAYAFDQ.c = 391;
            }
            if (var3_3 + 1 == var1_1.length) {
                return 2;
            }
            var4_5 = var3_3 + 1;
            if (!var7_4) ** GOTO lbl13
            while (RKAYAFDQ.a(-12789, var1_1[var4_5])) {
                if (var1_1[var4_5] == '\\' || var1_1[var4_5] == '/') {
                    return 3;
                }
                ++var4_5;
lbl13:
                // 2 sources

                if (var4_5 < var1_1.length) continue;
            }
            var5_7 = 0;
            var6_8 = var3_3 + 1;
            if (!var7_4) ** GOTO lbl21
            while (RKAYAFDQ.a(-12789, var2_2[var6_8])) {
                if (var2_2[var6_8] == '*') {
                    ++var5_7;
                }
                ++var6_8;
lbl21:
                // 2 sources

                if (var6_8 < var1_1.length) continue;
            }
            if (var5_7 >= 5) {
                return 4;
            }
            if (RKAYAFDQ.a(-12789, var1_1[var3_3 + 1])) {
                return 1;
            }
            return 0;
        }
        catch (RuntimeException var4_6) {
            signlink.reporterror("27208, " + var0 + ", " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_6.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public static final void a(byte[][] var0, char[] var1_1, int var2_2, char[] var3_3) {
        var20_4 = RKAYAFDQ.v;
        try {
            if (var20_4) {
                return;
            }
            if (var2_2 >= 0) ** continue;
            if (var3_3.length > var1_1.length) {
                return;
            }
            var4_5 = 1;
            var5_7 = 0;
            if (!var20_4) ** GOTO lbl129
            do {
                block34: {
                    block40: {
                        block36: {
                            block35: {
                                var6_8 = var5_7;
                                var7_9 = 0;
                                var8_10 = 0;
                                var4_5 = 1;
                                var9_11 = false;
                                var10_12 = false;
                                var11_13 = false;
                                if (!var20_4) ** GOTO lbl48
                                do {
                                    var12_14 = 0;
                                    var13_15 = var1_1[var6_8];
                                    var14_16 = 0;
                                    if (var6_8 + 1 < var1_1.length) {
                                        var14_16 = var1_1[var6_8 + 1];
                                    }
                                    if (var7_9 < var3_3.length && (var12_14 = RKAYAFDQ.a((char)var14_16, (char)var13_15, RKAYAFDQ.i, var3_3[var7_9])) > 0) {
                                        if (var12_14 == 1 && RKAYAFDQ.d((char)var13_15, -976)) {
                                            var10_12 = true;
                                        }
                                        if (var12_14 == 2 && (RKAYAFDQ.d((char)var13_15, -976) || RKAYAFDQ.d((char)var14_16, -976))) {
                                            var10_12 = true;
                                        }
                                        var6_8 += var12_14;
                                        ++var7_9;
                                        if (!var20_4) continue;
                                    }
                                    if (var7_9 == 0) break;
                                    var12_14 = RKAYAFDQ.a((char)var14_16, (char)var13_15, RKAYAFDQ.i, var3_3[var7_9 - 1]);
                                    if (var12_14 > 0) {
                                        var6_8 += var12_14;
                                        if (var7_9 != 1) continue;
                                        ++var4_5;
                                        if (!var20_4) continue;
                                    }
                                    if (var7_9 >= var3_3.length || !RKAYAFDQ.a(false, (char)var13_15)) break;
                                    if (RKAYAFDQ.a(-12789, (char)var13_15) && var13_15 != 39) {
                                        var9_11 = true;
                                    }
                                    if (RKAYAFDQ.d((char)var13_15, -976)) {
                                        var11_13 = true;
                                    }
                                    if (++var8_10 * 100 / (++var6_8 - var5_7) > 90 && !var20_4) break;
lbl48:
                                    // 5 sources

                                } while (var6_8 < var1_1.length && (!var10_12 || !var11_13));
                                if (var7_9 < var3_3.length || var10_12 && var11_13) break block34;
                                var12_14 = 1;
                                if (var9_11) break block35;
                                var13_15 = 32;
                                if (var5_7 - 1 >= 0) {
                                    var13_15 = var1_1[var5_7 - 1];
                                }
                                var14_16 = 32;
                                if (var6_8 < var1_1.length) {
                                    var14_16 = var1_1[var6_8];
                                }
                                var15_17 = RKAYAFDQ.b((char)var13_15, RKAYAFDQ.j);
                                var16_18 = RKAYAFDQ.b((char)var14_16, RKAYAFDQ.j);
                                if (var0 == null || !RKAYAFDQ.a((byte)var15_17, (byte)8, var0, (byte)var16_18)) break block36;
                                var12_14 = 0;
                                if (!var20_4) break block36;
                            }
                            var13_15 = 0;
                            var14_16 = 0;
                            if (var5_7 - 1 < 0 || RKAYAFDQ.a(-12789, var1_1[var5_7 - 1]) && var1_1[var5_7 - 1] != '\'') {
                                var13_15 = 1;
                            }
                            if (var6_8 >= var1_1.length || RKAYAFDQ.a(-12789, var1_1[var6_8]) && var1_1[var6_8] != '\'') {
                                var14_16 = 1;
                            }
                            if (var13_15 != 0 && var14_16 != 0) break block36;
                            var15_17 = 0;
                            var16_18 = var5_7 - 2;
                            if (var13_15 == 0) ** GOTO lbl94
                            var16_18 = var5_7;
                            if (!var20_4) ** GOTO lbl94
                            do {
                                block37: {
                                    if (var16_18 < 0 || RKAYAFDQ.a(-12789, var1_1[var16_18]) && var1_1[var16_18] != '\'') break block37;
                                    var17_19 = new char[3];
                                    var18_21 = 0;
                                    if (!var20_4) ** GOTO lbl84
                                    while (!(var16_18 + var18_21 >= var1_1.length || RKAYAFDQ.a(-12789, var1_1[var16_18 + var18_21]) && var1_1[var16_18 + var18_21] != '\'')) {
                                        var17_19[var18_21] = var1_1[var16_18 + var18_21];
                                        ++var18_21;
lbl84:
                                        // 2 sources

                                        if (var18_21 < 3) continue;
                                    }
                                    var19_22 = true;
                                    if (var18_21 == 0) {
                                        var19_22 = false;
                                    }
                                    if (!(var18_21 >= 3 || var16_18 - 1 < 0 || RKAYAFDQ.a(-12789, var1_1[var16_18 - 1]) && var1_1[var16_18 - 1] != '\'')) {
                                        var19_22 = false;
                                    }
                                    if (var19_22 && !RKAYAFDQ.a(var17_19, (byte)4)) {
                                        var15_17 = 1;
                                    }
                                }
                                ++var16_18;
lbl94:
                                // 3 sources

                            } while (var15_17 == 0 && var16_18 < var6_8);
                            if (var15_17 == 0) {
                                var12_14 = 0;
                            }
                        }
                        if (var12_14 == 0) break block34;
                        var13_15 = 0;
                        var14_16 = 0;
                        var15_17 = -1;
                        var16_18 = var5_7;
                        if (!var20_4) ** GOTO lbl114
                        do {
                            block39: {
                                block38: {
                                    if (!RKAYAFDQ.d(var1_1[var16_18], -976)) break block38;
                                    ++var13_15;
                                    if (!var20_4) break block39;
                                }
                                if (RKAYAFDQ.c(var1_1[var16_18], -46837)) {
                                    ++var14_16;
                                    var15_17 = var16_18;
                                }
                            }
                            ++var16_18;
lbl114:
                            // 2 sources

                        } while (var16_18 < var6_8);
                        if (var15_17 > -1) {
                            var13_15 -= var6_8 - 1 - var15_17;
                        }
                        if (var13_15 > var14_16) break block40;
                        var17_20 = var5_7;
                        if (!var20_4) ** GOTO lbl123
                        do {
                            var1_1[var17_20] = 42;
                            ++var17_20;
lbl123:
                            // 2 sources

                        } while (var17_20 < var6_8);
                        if (!var20_4) break block34;
                    }
                    var4_5 = 1;
                }
                var5_7 += var4_5;
lbl129:
                // 2 sources

            } while (var5_7 <= var1_1.length - var3_3.length);
            return;
        }
        catch (RuntimeException var4_6) {
            signlink.reporterror("25459, " + var0 + ", " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_6.toString());
            throw new RuntimeException();
        }
    }

    private static final boolean a(byte by, byte by2, byte[][] byArray, byte by3) {
        try {
            int n = 0;
            if (by2 != 8) {
                h = 308;
            }
            if (byArray[n][0] == by && byArray[n][1] == by3) {
                return true;
            }
            int n2 = byArray.length - 1;
            if (byArray[n2][0] == by && byArray[n2][1] == by3) {
                return true;
            }
            do {
                int n3;
                if (byArray[n3 = (n + n2) / 2][0] == by && byArray[n3][1] == by3) {
                    return true;
                }
                if (by < byArray[n3][0] || by == byArray[n3][0] && by3 < byArray[n3][1]) {
                    n2 = n3;
                    if (!v) continue;
                }
                n = n3;
            } while (n != n2 && n + 1 != n2);
            return false;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("67276, " + by + ", " + by2 + ", " + byArray + ", " + by3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final int a(int n, char c, char c2, char c3) {
        try {
            if (n <= 0) {
                return RKAYAFDQ.c;
            }
            if (c2 == c) {
                return 1;
            }
            if (c2 == 'o' && c == '0') {
                return 1;
            }
            if (c2 == 'o' && c == '(' && c3 == ')') {
                return 2;
            }
            if (c2 == 'c' && (c == '(' || c == '<' || c == '[')) {
                return 1;
            }
            if (c2 == 'e' && c == '\u20ac') {
                return 1;
            }
            if (c2 == 's' && c == '$') {
                return 1;
            }
            if (c2 == 'l' && c == 'i') {
                return 1;
            }
            return 0;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("60577, " + n + ", " + c + ", " + c2 + ", " + c3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final int a(char c, char c2, boolean bl, char c3) {
        try {
            if (!bl) {
                h = -260;
            }
            if (c3 == c2) {
                return 1;
            }
            if (c3 >= 'a' && c3 <= 'm') {
                if (c3 == 'a') {
                    if (c2 == '4' || c2 == '@' || c2 == '^') {
                        return 1;
                    }
                    if (c2 == '/' && c == '\\') {
                        return 2;
                    }
                    return 0;
                }
                if (c3 == 'b') {
                    if (c2 == '6' || c2 == '8') {
                        return 1;
                    }
                    if (c2 == '1' && c == '3' || c2 == 'i' && c == '3') {
                        return 2;
                    }
                    return 0;
                }
                if (c3 == 'c') {
                    if (c2 == '(' || c2 == '<' || c2 == '{' || c2 == '[') {
                        return 1;
                    }
                    return 0;
                }
                if (c3 == 'd') {
                    if (c2 == '[' && c == ')' || c2 == 'i' && c == ')') {
                        return 2;
                    }
                    return 0;
                }
                if (c3 == 'e') {
                    if (c2 == '3' || c2 == '\u20ac') {
                        return 1;
                    }
                    return 0;
                }
                if (c3 == 'f') {
                    if (c2 == 'p' && c == 'h') {
                        return 2;
                    }
                    if (c2 == '\u00a3') {
                        return 1;
                    }
                    return 0;
                }
                if (c3 == 'g') {
                    if (c2 == '9' || c2 == '6' || c2 == 'q') {
                        return 1;
                    }
                    return 0;
                }
                if (c3 == 'h') {
                    if (c2 == '#') {
                        return 1;
                    }
                    return 0;
                }
                if (c3 == 'i') {
                    if (c2 == 'y' || c2 == 'l' || c2 == 'j' || c2 == '1' || c2 == '!' || c2 == ':' || c2 == ';' || c2 == '|') {
                        return 1;
                    }
                    return 0;
                }
                if (c3 == 'j') {
                    return 0;
                }
                if (c3 == 'k') {
                    return 0;
                }
                if (c3 == 'l') {
                    if (c2 == '1' || c2 == '|' || c2 == 'i') {
                        return 1;
                    }
                    return 0;
                }
                if (c3 == 'm') {
                    return 0;
                }
            }
            if (c3 >= 'n' && c3 <= 'z') {
                if (c3 == 'n') {
                    return 0;
                }
                if (c3 == 'o') {
                    if (c2 == '0' || c2 == '*') {
                        return 1;
                    }
                    if (c2 == '(' && c == ')' || c2 == '[' && c == ']' || c2 == '{' && c == '}' || c2 == '<' && c == '>') {
                        return 2;
                    }
                    return 0;
                }
                if (c3 == 'p') {
                    return 0;
                }
                if (c3 == 'q') {
                    return 0;
                }
                if (c3 == 'r') {
                    return 0;
                }
                if (c3 == 's') {
                    if (c2 == '5' || c2 == 'z' || c2 == '$' || c2 == '2') {
                        return 1;
                    }
                    return 0;
                }
                if (c3 == 't') {
                    if (c2 == '7' || c2 == '+') {
                        return 1;
                    }
                    return 0;
                }
                if (c3 == 'u') {
                    if (c2 == 'v') {
                        return 1;
                    }
                    if (c2 == '\\' && c == '/' || c2 == '\\' && c == '|' || c2 == '|' && c == '/') {
                        return 2;
                    }
                    return 0;
                }
                if (c3 == 'v') {
                    if (c2 == '\\' && c == '/' || c2 == '\\' && c == '|' || c2 == '|' && c == '/') {
                        return 2;
                    }
                    return 0;
                }
                if (c3 == 'w') {
                    if (c2 == 'v' && c == 'v') {
                        return 2;
                    }
                    return 0;
                }
                if (c3 == 'x') {
                    if (c2 == ')' && c == '(' || c2 == '}' && c == '{' || c2 == ']' && c == '[' || c2 == '>' && c == '<') {
                        return 2;
                    }
                    return 0;
                }
                if (c3 == 'y') {
                    return 0;
                }
                if (c3 == 'z') {
                    return 0;
                }
            }
            if (c3 >= '0' && c3 <= '9') {
                if (c3 == '0') {
                    if (c2 == 'o' || c2 == 'O') {
                        return 1;
                    }
                    if (c2 == '(' && c == ')' || c2 == '{' && c == '}' || c2 == '[' && c == ']') {
                        return 2;
                    }
                    return 0;
                }
                if (c3 == '1') {
                    if (c2 == 'l') {
                        return 1;
                    }
                    return 0;
                }
                return 0;
            }
            if (c3 == ',') {
                if (c2 == '.') {
                    return 1;
                }
                return 0;
            }
            if (c3 == '.') {
                if (c2 == ',') {
                    return 1;
                }
                return 0;
            }
            if (c3 == '!') {
                if (c2 == 'i') {
                    return 1;
                }
                return 0;
            }
            return 0;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("9538, " + c + ", " + c2 + ", " + bl + ", " + c3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final byte b(char c, int n) {
        try {
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !v) continue;
                a = -93;
            } while (n >= 0);
            if (c >= 'a' && c <= 'z') {
                return (byte)(c - 97 + 1);
            }
            if (c == '\'') {
                return 28;
            }
            if (c >= '0' && c <= '9') {
                return (byte)(c - 48 + 29);
            }
            return 27;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("52349, " + c + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void a(char[] cArray, int n) {
        boolean bl = v;
        try {
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            int n5 = 0;
            boolean bl2 = true;
            do {
                block15: {
                    block14: {
                        if (bl2 && !(bl2 = false)) {
                            if (n < 0) continue;
                            boolean bl3 = b = !b;
                            if (!bl) continue;
                        }
                        boolean bl4 = false;
                        int n6 = n3;
                        boolean bl5 = true;
                        do {
                            if (bl5 && !(bl5 = false) && !bl) continue;
                            if (!RKAYAFDQ.a(-12789, cArray[n6]) && !RKAYAFDQ.a(false, cArray[n6])) {
                                bl4 = true;
                            }
                            ++n6;
                        } while (n6 >= 0 && n6 < n2 && !bl4);
                        if (bl4) {
                            n4 = 0;
                        }
                        if (n4 == 0) {
                            n5 = n2;
                        }
                        n3 = RKAYAFDQ.b(cArray, 0, n2);
                        int n7 = 0;
                        int n8 = n2;
                        boolean bl6 = true;
                        do {
                            if (bl6 && !(bl6 = false) && !bl) continue;
                            n7 = n7 * 10 + cArray[n8] - 48;
                            ++n8;
                        } while (n8 < n3);
                        if (n7 <= 255 && n3 - n2 <= 8) break block14;
                        n4 = 0;
                        if (!bl) break block15;
                    }
                    ++n4;
                }
                if (n4 != 4) continue;
                int n9 = n5;
                boolean bl7 = true;
                do {
                    if (bl7 && !(bl7 = false) && !bl) continue;
                    cArray[n9] = 42;
                    ++n9;
                } while (n9 < n3);
                n4 = 0;
            } while ((n2 = RKAYAFDQ.a(cArray, n3, 319)) != -1);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("38921, " + cArray + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final int a(char[] cArray, int n, int n2) {
        try {
            n2 = 23 / n2;
            int n3 = n;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !v) continue;
                if (cArray[n3] >= '0' && cArray[n3] <= '9') {
                    return n3;
                }
                ++n3;
            } while (n3 < cArray.length && n3 >= 0);
            return -1;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("27983, " + cArray + ", " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final int b(char[] cArray, int n, int n2) {
        try {
            int n3 = n2;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !v) continue;
                if (cArray[n3] < '0' || cArray[n3] > '9') {
                    return n3;
                }
                ++n3;
            } while (n3 < cArray.length && n3 >= 0);
            if (n != 0) {
                return 3;
            }
            return cArray.length;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("1466, " + cArray + ", " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final boolean a(int n, char c) {
        try {
            if (n != -12789) {
                throw new NullPointerException();
            }
            return !RKAYAFDQ.c(c, -46837) && !RKAYAFDQ.d(c, -976);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("18641, " + n + ", " + c + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final boolean a(boolean bl, char c) {
        try {
            if (bl) {
                j = -233;
            }
            if (c < 'a' || c > 'z') {
                return true;
            }
            return c == 'v' || c == 'x' || c == 'j' || c == 'q' || c == 'z';
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("32846, " + bl + ", " + c + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final boolean c(char c, int n) {
        try {
            if (n != -46837) {
                int n2 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && !v) continue;
                    ++n2;
                } while (n2 > 0);
            }
            return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("61160, " + c + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final boolean d(char c, int n) {
        try {
            if (n >= 0) {
                j = 254;
            }
            return c >= '0' && c <= '9';
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("30488, " + c + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final boolean e(char c, int n) {
        try {
            if (n != 1) {
                int n2 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && !v) continue;
                    ++n2;
                } while (n2 > 0);
            }
            return c >= 'a' && c <= 'z';
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("25533, " + c + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final boolean b(boolean bl, char c) {
        try {
            if (!bl) {
                throw new NullPointerException();
            }
            return c >= 'A' && c <= 'Z';
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("1272, " + bl + ", " + c + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final boolean a(char[] cArray, byte by) {
        boolean bl = v;
        try {
            block13: {
                block12: {
                    if (by != l) break block12;
                    by = 0;
                    if (!bl) break block13;
                }
                throw new NullPointerException();
            }
            boolean bl2 = true;
            int n = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                if (!RKAYAFDQ.d(cArray[n], -976) && cArray[n] != '\u0000') {
                    bl2 = false;
                }
                ++n;
            } while (n < cArray.length);
            if (bl2) {
                return true;
            }
            int n2 = RKAYAFDQ.b(cArray, 8801);
            int n3 = 0;
            int n4 = o.length - 1;
            if (n2 == o[n3] || n2 == o[n4]) {
                return true;
            }
            do {
                int n5;
                if (n2 == o[n5 = (n3 + n4) / 2]) {
                    return true;
                }
                if (n2 < o[n5]) {
                    n4 = n5;
                    if (!bl) continue;
                }
                n3 = n5;
            } while (n3 != n4 && n3 + 1 != n4);
            return false;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("62482, " + cArray + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final int b(char[] cArray, int n) {
        boolean bl = v;
        try {
            int n2;
            if (n != m) {
                n2 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && !bl) continue;
                    ++n2;
                } while (n2 > 0);
            }
            if (cArray.length > 6) {
                return 0;
            }
            n2 = 0;
            int n3 = 0;
            boolean bl3 = true;
            do {
                block12: {
                    char c;
                    block14: {
                        block13: {
                            block11: {
                                if (bl3 && !(bl3 = false) && !bl) continue;
                                c = cArray[cArray.length - n3 - 1];
                                if (c < 'a' || c > 'z') break block11;
                                n2 = n2 * 38 + (c - 97 + 1);
                                if (!bl) break block12;
                            }
                            if (c != '\'') break block13;
                            n2 = n2 * 38 + 27;
                            if (!bl) break block12;
                        }
                        if (c < '0' || c > '9') break block14;
                        n2 = n2 * 38 + (c - 48 + 28);
                        if (!bl) break block12;
                    }
                    if (c != '\u0000') {
                        return 0;
                    }
                }
                ++n3;
            } while (n3 < cArray.length);
            return n2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("67682, " + cArray + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    static {
        c = 748;
        d = 201;
        e = true;
        g = (byte)-117;
        h = -575;
        i = true;
        j = -720;
        k = -511;
        l = (byte)4;
        m = 8801;
        n = true;
        u = new String[]{"cook", "cook's", "cooks", "seeks", "sheet", "woop", "woops", "faq", "noob", "noobs"};
    }
}

