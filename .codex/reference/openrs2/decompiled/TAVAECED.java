/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class TAVAECED {
    private int a;
    public static int b;
    public static TAVAECED[] c;
    public int d;
    public int[] e;
    public int[] f;
    public int[] g;
    public int[] h;
    public boolean i;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, XTGLDHGX xTGLDHGX) {
        try {
            MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("idk.dat", null), 891);
            b = mBMGIXGO.e();
            if (c == null) {
                c = new TAVAECED[b];
            }
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && LKGEGIEW.t == 0) continue;
                if (c[n2] == null) {
                    TAVAECED.c[n2] = new TAVAECED();
                }
                c[n2].a(true, mBMGIXGO);
                ++n2;
            } while (n2 < b);
            if (n == 0) return;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("19348, " + n + ", " + xTGLDHGX + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(boolean bl, MBMGIXGO mBMGIXGO) {
        int n = LKGEGIEW.t;
        try {
            if (!bl) {
                throw new NullPointerException();
            }
            while (true) {
                int n2;
                if ((n2 = mBMGIXGO.c()) == 0) {
                    return;
                }
                if (n2 == 1) {
                    this.d = mBMGIXGO.c();
                    if (n == 0) continue;
                }
                if (n2 == 2) {
                    int n3 = mBMGIXGO.c();
                    this.e = new int[n3];
                    int n4 = 0;
                    boolean bl2 = true;
                    do {
                        if (bl2 && !(bl2 = false) && n == 0) continue;
                        this.e[n4] = mBMGIXGO.e();
                        ++n4;
                    } while (n4 < n3);
                    if (n == 0) continue;
                }
                if (n2 == 3) {
                    this.i = true;
                    if (n == 0) continue;
                }
                if (n2 >= 40 && n2 < 50) {
                    this.f[n2 - 40] = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 >= 50 && n2 < 60) {
                    this.g[n2 - 50] = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 >= 60 && n2 < 70) {
                    this.h[n2 - 60] = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                System.out.println("Error unrecognised config code: " + n2);
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("91892, " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean a(byte by) {
        int n = LKGEGIEW.t;
        try {
            int n2;
            boolean bl;
            block8: {
                block7: {
                    if (this.e == null) {
                        return true;
                    }
                    bl = true;
                    if (by != 2) break block7;
                    by = 0;
                    if (n == 0) break block8;
                }
                n2 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n == 0) continue;
                    ++n2;
                } while (n2 > 0);
            }
            n2 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n == 0) continue;
                if (!ZKARKDQW.c(this.e[n2])) {
                    bl = false;
                }
                ++n2;
            } while (n2 < this.e.length);
            return bl;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("9202, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public ZKARKDQW a(boolean var1_1) {
        var6_2 = LKGEGIEW.t;
        try {
            block10: {
                block9: {
                    if (var1_1) {
                        throw new NullPointerException();
                    }
                    if (this.e == null) {
                        return null;
                    }
                    var2_3 = new ZKARKDQW[this.e.length];
                    var3_5 = 0;
                    if (var6_2 == 0) ** GOTO lbl13
                    do {
                        var2_3[var3_5] = ZKARKDQW.b(this.a, this.e[var3_5]);
                        ++var3_5;
lbl13:
                        // 2 sources

                    } while (var3_5 < this.e.length);
                    if (var2_3.length != 1) break block9;
                    var4_6 = var2_3[0];
                    if (var6_2 == 0) break block10;
                }
                var4_6 = new ZKARKDQW(var2_3.length, var2_3, -38);
            }
            var5_7 = 0;
            if (var6_2 == 0) ** GOTO lbl25
            while (this.f[var5_7] != 0) {
                var4_6.e(this.f[var5_7], this.g[var5_7]);
                ++var5_7;
lbl25:
                // 2 sources

                if (var5_7 < 6) continue;
            }
            return var4_6;
        }
        catch (RuntimeException var2_4) {
            signlink.reporterror("82138, " + var1_1 + ", " + var2_4.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean b(boolean bl) {
        try {
            if (bl) {
                throw new NullPointerException();
            }
            boolean bl2 = true;
            int n = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && LKGEGIEW.t == 0) continue;
                if (this.h[n] != -1 && !ZKARKDQW.c(this.h[n])) {
                    bl2 = false;
                }
                ++n;
            } while (n < 5);
            return bl2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("71412, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public ZKARKDQW a(int var1_1) {
        var7_2 = LKGEGIEW.t;
        try {
            if (var1_1 != 0) {
                throw new NullPointerException();
            }
            var2_3 = new ZKARKDQW[5];
            var3_5 = 0;
            var4_6 = 0;
            if (var7_2 == 0) ** GOTO lbl13
            do {
                if (this.h[var4_6] != -1) {
                    var2_3[var3_5++] = ZKARKDQW.b(this.a, this.h[var4_6]);
                }
                ++var4_6;
lbl13:
                // 2 sources

            } while (var4_6 < 5);
            var5_7 = new ZKARKDQW(var3_5, var2_3, -38);
            var6_8 = 0;
            if (var7_2 == 0) ** GOTO lbl20
            while (this.f[var6_8] != 0) {
                var5_7.e(this.f[var6_8], this.g[var6_8]);
                ++var6_8;
lbl20:
                // 2 sources

                if (var6_8 < 6) continue;
            }
            return var5_7;
        }
        catch (RuntimeException var2_4) {
            signlink.reporterror("82628, " + var1_1 + ", " + var2_4.toString());
            throw new RuntimeException();
        }
    }

    public TAVAECED() {
        int n = LKGEGIEW.t;
        this.a = 9;
        this.d = -1;
        this.f = new int[6];
        this.g = new int[6];
        this.h = new int[]{-1, -1, -1, -1, -1};
        this.i = false;
        if (PKVMXVTO.e) {
            LKGEGIEW.t = ++n;
        }
    }
}

