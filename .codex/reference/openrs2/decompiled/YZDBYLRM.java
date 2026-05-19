/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

final class YZDBYLRM {
    public boolean a;
    private byte b;
    private int c;
    public String d;
    private int e;
    private static ZKARKDQW[] f = new ZKARKDQW[4];
    private byte g;
    private int h = 9;
    public int i;
    private int j;
    public int k;
    private int[] l;
    private int m;
    public int n;
    private int o;
    private boolean p;
    public static boolean q;
    private static MBMGIXGO r;
    public int s = -1;
    private static int[] t;
    private static int u;
    public boolean v;
    public int w;
    public int[] x;
    public int y;
    public int z;
    public boolean A;
    private boolean B = true;
    public boolean C;
    public static client D;
    public boolean E;
    public boolean F;
    public int G;
    private boolean H;
    private int I = 9;
    private static int J;
    private int K;
    private int[] L;
    public int M;
    public int N;
    private int[] O;
    public byte[] P;
    public boolean Q;
    public boolean R;
    public static GCPOSBWX S;
    public int T;
    private static YZDBYLRM[] U;
    private int V;
    private int[] W;
    public static GCPOSBWX X;
    public String[] Y;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public static final YZDBYLRM a(int n) {
        int n2 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && client.Jj == 0) continue;
            if (YZDBYLRM.U[n2].s == n) {
                return U[n2];
            }
            ++n2;
        } while (n2 < 20);
        J = (J + 1) % 20;
        YZDBYLRM yZDBYLRM = U[J];
        YZDBYLRM.r.z = t[n];
        yZDBYLRM.s = n;
        yZDBYLRM.a();
        yZDBYLRM.a(true, r);
        return yZDBYLRM;
    }

    private final void a() {
        this.L = null;
        this.O = null;
        this.d = null;
        this.P = null;
        this.W = null;
        this.l = null;
        this.i = 1;
        this.z = 1;
        this.F = true;
        this.v = true;
        this.Q = false;
        this.A = false;
        this.H = false;
        this.C = false;
        this.T = -1;
        this.N = 16;
        this.b = 0;
        this.g = 0;
        this.Y = null;
        this.k = -1;
        this.w = -1;
        this.p = false;
        this.R = true;
        this.m = 128;
        this.K = 128;
        this.e = 128;
        this.G = 0;
        this.c = 0;
        this.j = 0;
        this.V = 0;
        this.a = false;
        this.E = false;
        this.y = -1;
        this.M = -1;
        this.n = -1;
        this.x = null;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(GHOWLKWN gHOWLKWN, int n) {
        int n2 = client.Jj;
        try {
            if (this.L == null) {
                return;
            }
            int n3 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                gHOWLKWN.a(this.L[n3] & 0xFFFF, 0, false);
                ++n3;
            } while (n3 < this.L.length);
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                boolean bl3 = this.B = !this.B;
            } while (n >= 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("81306, " + gHOWLKWN + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void b(int n) {
        try {
            X = null;
            S = null;
            t = null;
            U = null;
            r = null;
            if (client.Jj != 0 || n >= 0) {
                return;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("56607, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public static final void a(XTGLDHGX xTGLDHGX) {
        int n = client.Jj;
        r = new MBMGIXGO(xTGLDHGX.a("loc.dat", null), 891);
        MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("loc.idx", null), 891);
        u = mBMGIXGO.e();
        t = new int[u];
        int n2 = 2;
        int n3 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && n == 0) continue;
            YZDBYLRM.t[n3] = n2;
            n2 += mBMGIXGO.e();
            ++n3;
        } while (n3 < u);
        U = new YZDBYLRM[20];
        int n4 = 0;
        boolean bl2 = true;
        do {
            if (bl2 && !(bl2 = false) && n == 0) continue;
            YZDBYLRM.U[n4] = new YZDBYLRM();
            ++n4;
        } while (n4 < 20);
    }

    /*
     * Unable to fully structure code
     */
    public final boolean a(int var1_1, boolean var2_2) {
        var5_3 = client.Jj;
        try {
            block8: {
                if (!var2_2) {
                    throw new NullPointerException();
                }
                if (this.O != null) break block8;
                if (this.L == null) {
                    return true;
                }
                if (var1_1 != 10) {
                    return true;
                }
                var3_4 = true;
                var4_7 = 0;
                if (var5_3 == 0) ** GOTO lbl16
                do {
                    var3_4 &= ZKARKDQW.c(this.L[var4_7] & 65535);
                    ++var4_7;
lbl16:
                    // 2 sources

                } while (var4_7 < this.L.length);
                return var3_4;
            }
            var3_5 = 0;
            if (var5_3 == 0) ** GOTO lbl25
            do {
                if (this.O[var3_5] == var1_1) {
                    return ZKARKDQW.c(this.L[var3_5] & 65535);
                }
                ++var3_5;
lbl25:
                // 2 sources

            } while (var3_5 < this.O.length);
            return true;
        }
        catch (RuntimeException var3_6) {
            signlink.reporterror("89605, " + var1_1 + ", " + var2_2 + ", " + var3_6.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public final ZKARKDQW a(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        ZKARKDQW zKARKDQW = this.a(0, n, n7, n2);
        if (zKARKDQW == null) {
            return null;
        }
        if (this.A || this.H) {
            zKARKDQW = new ZKARKDQW(this.A, -819, this.H, zKARKDQW);
        }
        if (this.A) {
            int n8 = (n3 + n4 + n5 + n6) / 4;
            int n9 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && client.Jj == 0) continue;
                int n10 = zKARKDQW.z[n9];
                int n11 = zKARKDQW.B[n9];
                int n12 = n3 + (n4 - n3) * (n10 + 64) / 128;
                int n13 = n6 + (n5 - n6) * (n10 + 64) / 128;
                int n14 = n12 + (n13 - n12) * (n11 + 64) / 128;
                int n15 = n9++;
                zKARKDQW.A[n15] = zKARKDQW.A[n15] + (n14 - n8);
            } while (n9 < zKARKDQW.y);
            zKARKDQW.b(false);
        }
        return zKARKDQW;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean a(boolean bl) {
        try {
            if (this.L == null) {
                return true;
            }
            boolean bl2 = true;
            int n = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && client.Jj == 0) continue;
                bl2 &= ZKARKDQW.c(this.L[n] & 0xFFFF);
                ++n;
            } while (n < this.L.length);
            if (!bl) {
                throw new NullPointerException();
            }
            return bl2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("29403, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final YZDBYLRM b(boolean bl) {
        try {
            int n;
            block9: {
                block8: {
                    if (!bl) {
                        throw new NullPointerException();
                    }
                    n = -1;
                    if (this.M == -1) break block8;
                    SXYSOXTR sXYSOXTR = SXYSOXTR.c[this.M];
                    int n2 = sXYSOXTR.e;
                    int n3 = sXYSOXTR.f;
                    int n4 = sXYSOXTR.g;
                    int n5 = client.Di[n4 - n3];
                    n = YZDBYLRM.D.Bd[n2] >> n3 & n5;
                    if (client.Jj == 0) break block9;
                }
                if (this.n != -1) {
                    n = YZDBYLRM.D.Bd[this.n];
                }
            }
            if (n < 0 || n >= this.x.length || this.x[n] == -1) {
                return null;
            }
            return YZDBYLRM.a(this.x[n]);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("60219, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private final ZKARKDQW a(int var1_1, int var2_2, int var3_3, int var4_4) {
        var13_5 = client.Jj;
        try {
            block40: {
                block39: {
                    block38: {
                        block37: {
                            block36: {
                                block35: {
                                    block34: {
                                        var7_6 = null;
                                        if (this.O != null) break block34;
                                        if (var2_2 != 10) {
                                            return null;
                                        }
                                        var5_7 = (long)((this.s << 6) + var4_4) + ((long)(var3_3 + 1) << 32);
                                        var8_9 = (ZKARKDQW)YZDBYLRM.S.a(var5_7);
                                        if (var8_9 != null) {
                                            return var8_9;
                                        }
                                        if (this.L == null) {
                                            return null;
                                        }
                                        var9_12 = this.p ^ (var4_4 <= 3 ? 0 : 1);
                                        var10_13 = this.L.length;
                                        var11_16 = 0;
                                        while (var11_16 < var10_13) {
                                            var12_17 = this.L[var11_16];
                                            if (var9_12 != 0) {
                                                var12_17 += 65536;
                                            }
                                            if ((var7_6 = (ZKARKDQW)YZDBYLRM.X.a(var12_17)) == null) {
                                                var7_6 = ZKARKDQW.b(this.I, var12_17 & 65535);
                                                if (var7_6 == null) {
                                                    return null;
                                                }
                                                if (var9_12 != 0) {
                                                    var7_6.f(0);
                                                }
                                                YZDBYLRM.X.a(var7_6, var12_17, (byte)2);
                                            }
                                            if (var10_13 > 1) {
                                                YZDBYLRM.f[var11_16] = var7_6;
                                            }
                                            ++var11_16;
                                        }
                                        if (var10_13 > 1) {
                                            var7_6 = new ZKARKDQW(var10_13, YZDBYLRM.f, -38);
                                        }
                                        break block35;
                                    }
                                    var8_10 = -1;
                                    var9_12 = 0;
                                    if (var13_5 == 0) ** GOTO lbl43
                                    do {
                                        if (this.O[var9_12] == var2_2) {
                                            var8_10 = var9_12;
                                            if (var13_5 == 0) break;
                                        }
                                        ++var9_12;
lbl43:
                                        // 2 sources

                                    } while (var9_12 < this.O.length);
                                    if (var8_10 == -1) {
                                        return null;
                                    }
                                    var5_7 = (long)((this.s << 6) + (var8_10 << 3) + var4_4) + ((long)(var3_3 + 1) << 32);
                                    var10_14 = (ZKARKDQW)YZDBYLRM.S.a(var5_7);
                                    if (var10_14 != null) {
                                        return var10_14;
                                    }
                                    var11_16 = this.L[var8_10];
                                    var12_17 = this.p ^ (var4_4 <= 3 ? 0 : 1);
                                    if (var12_17 != 0) {
                                        var11_16 += 65536;
                                    }
                                    if ((var7_6 = (ZKARKDQW)YZDBYLRM.X.a(var11_16)) == null) {
                                        var7_6 = ZKARKDQW.b(this.I, var11_16 & 65535);
                                        if (var7_6 == null) {
                                            return null;
                                        }
                                        if (var12_17 != 0) {
                                            var7_6.f(0);
                                        }
                                        YZDBYLRM.X.a(var7_6, var11_16, (byte)2);
                                    }
                                }
                                if (this.m == 128 && this.K == 128 && this.e == 128) break block36;
                                var8_11 = true;
                                if (var13_5 == 0) break block37;
                            }
                            var8_11 = false;
                        }
                        if (this.c == 0 && this.j == 0 && this.V == 0) break block38;
                        var9_12 = 1;
                        if (var13_5 == 0) break block39;
                    }
                    var9_12 = 0;
                }
                var10_15 = new ZKARKDQW(9, this.W == null, SQHJOGRT.a(var3_3, false), var4_4 == 0 && var3_3 == -1 && var8_11 == false && var9_12 == 0, var7_6);
                if (var3_3 == -1) ** GOTO lbl83
                var10_15.a((byte)-71);
                var10_15.c(var3_3, 40542);
                var10_15.eb = null;
                var10_15.db = null;
                if (var13_5 == 0) ** GOTO lbl83
                do {
                    var10_15.e(360);
lbl83:
                    // 3 sources

                } while (var4_4-- > 0);
                if (this.W == null) break block40;
                var11_16 = 0;
                if (var13_5 == 0) ** GOTO lbl90
                do {
                    var10_15.e(this.W[var11_16], this.l[var11_16]);
                    ++var11_16;
lbl90:
                    // 2 sources

                } while (var11_16 < this.W.length);
            }
            if (var8_11) {
                var10_15.b(this.m, this.e, this.h, this.K);
            }
            if (var9_12 != 0) {
                var10_15.a(this.c, this.j, 16384, this.V);
            }
            var10_15.a(64 + this.b, 768 + this.g * 5, -50, -10, -50, this.H == false);
            if (this.y == 1) {
                var10_15.ab = var10_15.k;
            }
            YZDBYLRM.S.a(var10_15, var5_7, (byte)2);
            if (var1_1 != 0) {
                this.h = 422;
            }
            return var10_15;
        }
        catch (RuntimeException var5_8) {
            signlink.reporterror("87963, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_4 + ", " + var5_8.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void a(boolean bl, MBMGIXGO mBMGIXGO) {
        int n = client.Jj;
        try {
            int n2;
            if (!bl) {
                this.o = 217;
            }
            int n3 = -1;
            while ((n2 = mBMGIXGO.c()) != 0) {
                int n4;
                int n5;
                if (n2 == 1) {
                    n5 = mBMGIXGO.c();
                    if (n5 <= 0) continue;
                    if (this.L == null || q) {
                        this.O = new int[n5];
                        this.L = new int[n5];
                        n4 = 0;
                        boolean bl2 = true;
                        do {
                            if (bl2 && !(bl2 = false) && n == 0) continue;
                            this.L[n4] = mBMGIXGO.e();
                            this.O[n4] = mBMGIXGO.c();
                            ++n4;
                        } while (n4 < n5);
                        if (n == 0) continue;
                    }
                    mBMGIXGO.z += n5 * 3;
                    if (n == 0) continue;
                }
                if (n2 == 2) {
                    this.d = mBMGIXGO.i();
                    if (n == 0) continue;
                }
                if (n2 == 3) {
                    this.P = mBMGIXGO.a((byte)30);
                    if (n == 0) continue;
                }
                if (n2 == 5) {
                    n5 = mBMGIXGO.c();
                    if (n5 <= 0) continue;
                    if (this.L == null || q) {
                        this.O = null;
                        this.L = new int[n5];
                        n4 = 0;
                        boolean bl3 = true;
                        do {
                            if (bl3 && !(bl3 = false) && n == 0) continue;
                            this.L[n4] = mBMGIXGO.e();
                            ++n4;
                        } while (n4 < n5);
                        if (n == 0) continue;
                    }
                    mBMGIXGO.z += n5 * 2;
                    if (n == 0) continue;
                }
                if (n2 == 14) {
                    this.i = mBMGIXGO.c();
                    if (n == 0) continue;
                }
                if (n2 == 15) {
                    this.z = mBMGIXGO.c();
                    if (n == 0) continue;
                }
                if (n2 == 17) {
                    this.F = false;
                    if (n == 0) continue;
                }
                if (n2 == 18) {
                    this.v = false;
                    if (n == 0) continue;
                }
                if (n2 == 19) {
                    n3 = mBMGIXGO.c();
                    if (n3 != 1) continue;
                    this.Q = true;
                    if (n == 0) continue;
                }
                if (n2 == 21) {
                    this.A = true;
                    if (n == 0) continue;
                }
                if (n2 == 22) {
                    this.H = true;
                    if (n == 0) continue;
                }
                if (n2 == 23) {
                    this.C = true;
                    if (n == 0) continue;
                }
                if (n2 == 24) {
                    this.T = mBMGIXGO.e();
                    if (this.T != 65535) continue;
                    this.T = -1;
                    if (n == 0) continue;
                }
                if (n2 == 28) {
                    this.N = mBMGIXGO.c();
                    if (n == 0) continue;
                }
                if (n2 == 29) {
                    this.b = mBMGIXGO.d();
                    if (n == 0) continue;
                }
                if (n2 == 39) {
                    this.g = mBMGIXGO.d();
                    if (n == 0) continue;
                }
                if (n2 >= 30 && n2 < 39) {
                    if (this.Y == null) {
                        this.Y = new String[5];
                    }
                    this.Y[n2 - 30] = mBMGIXGO.i();
                    if (!this.Y[n2 - 30].equalsIgnoreCase("hidden")) continue;
                    this.Y[n2 - 30] = null;
                    if (n == 0) continue;
                }
                if (n2 == 40) {
                    n5 = mBMGIXGO.c();
                    this.W = new int[n5];
                    this.l = new int[n5];
                    n4 = 0;
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && n == 0) continue;
                        this.W[n4] = mBMGIXGO.e();
                        this.l[n4] = mBMGIXGO.e();
                        ++n4;
                    } while (n4 < n5);
                    if (n == 0) continue;
                }
                if (n2 == 60) {
                    this.k = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 62) {
                    this.p = true;
                    if (n == 0) continue;
                }
                if (n2 == 64) {
                    this.R = false;
                    if (n == 0) continue;
                }
                if (n2 == 65) {
                    this.m = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 66) {
                    this.K = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 67) {
                    this.e = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 68) {
                    this.w = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 69) {
                    this.G = mBMGIXGO.c();
                    if (n == 0) continue;
                }
                if (n2 == 70) {
                    this.c = mBMGIXGO.f();
                    if (n == 0) continue;
                }
                if (n2 == 71) {
                    this.j = mBMGIXGO.f();
                    if (n == 0) continue;
                }
                if (n2 == 72) {
                    this.V = mBMGIXGO.f();
                    if (n == 0) continue;
                }
                if (n2 == 73) {
                    this.a = true;
                    if (n == 0) continue;
                }
                if (n2 == 74) {
                    this.E = true;
                    if (n == 0) continue;
                }
                if (n2 == 75) {
                    this.y = mBMGIXGO.c();
                    if (n == 0) continue;
                }
                if (n2 != 77) continue;
                this.M = mBMGIXGO.e();
                if (this.M == 65535) {
                    this.M = -1;
                }
                this.n = mBMGIXGO.e();
                if (this.n == 65535) {
                    this.n = -1;
                }
                n5 = mBMGIXGO.c();
                this.x = new int[n5 + 1];
                n4 = 0;
                boolean bl5 = true;
                do {
                    if (bl5 && !(bl5 = false) && n == 0) continue;
                    this.x[n4] = mBMGIXGO.e();
                    if (this.x[n4] == 65535) {
                        this.x[n4] = -1;
                    }
                    ++n4;
                } while (n4 <= n5);
                if (n == 0) continue;
            }
            if (n3 == -1) {
                this.Q = false;
                if (this.L != null && (this.O == null || this.O[0] == 10)) {
                    this.Q = true;
                }
                if (this.Y != null) {
                    this.Q = true;
                }
            }
            if (this.E) {
                this.F = false;
                this.v = false;
            }
            if (this.y != -1) return;
            this.y = this.F ? 1 : 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("46459, " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    YZDBYLRM() {
    }

    static {
        S = new GCPOSBWX(false, 30);
        X = new GCPOSBWX(false, 500);
    }
}

