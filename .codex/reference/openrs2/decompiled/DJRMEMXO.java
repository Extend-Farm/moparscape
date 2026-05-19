/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

final class DJRMEMXO {
    private byte a;
    public int b;
    private int[] c;
    public int d = -1;
    static GCPOSBWX e = new GCPOSBWX(false, 100);
    public static GCPOSBWX f = new GCPOSBWX(false, 50);
    private int[] g;
    public boolean h;
    public int i;
    public int j;
    private int k;
    private int l;
    public int m;
    private int n;
    public String[] o;
    private int p;
    public String q;
    private int r = 9;
    private static DJRMEMXO[] s;
    public int t;
    private int u;
    private int v;
    public boolean w;
    private int x = 9;
    public byte[] y;
    public int z;
    private static int A;
    public int B;
    public static boolean C;
    private static MBMGIXGO D;
    private int E;
    public int F;
    private boolean G = false;
    private static boolean H;
    private int I;
    public String[] J;
    public int K;
    private int L;
    private int M;
    public int[] N;
    private int O;
    private static int[] P;
    private int Q;
    public int R;
    public int S;
    public int T;
    private int U;
    public int[] V;
    public int W;
    public static int X;
    private int Y;
    private byte Z;
    private boolean ab = false;

    public static final void a(int n) {
        try {
            f = null;
            e = null;
            P = null;
            if (n >= 0) {
                H = !H;
            }
            s = null;
            D = null;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("67199, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final boolean a(int n, int n2) {
        try {
            if (n != -2836) {
                this.G = !this.G;
            }
            int n3 = this.v;
            int n4 = this.m;
            if (n2 == 1) {
                n3 = this.R;
                n4 = this.t;
            }
            if (n3 == -1) {
                return true;
            }
            boolean bl = true;
            if (!ZKARKDQW.c(n3)) {
                bl = false;
            }
            if (n4 != -1 && !ZKARKDQW.c(n4)) {
                bl = false;
            }
            return bl;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("65881, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public static final void a(XTGLDHGX xTGLDHGX) {
        int n = client.Jj;
        D = new MBMGIXGO(xTGLDHGX.a("obj.dat", null), 891);
        MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("obj.idx", null), 891);
        X = mBMGIXGO.e();
        P = new int[X];
        int n2 = 2;
        int n3 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && n == 0) continue;
            DJRMEMXO.P[n3] = n2;
            n2 += mBMGIXGO.e();
            ++n3;
        } while (n3 < X);
        s = new DJRMEMXO[10];
        int n4 = 0;
        boolean bl2 = true;
        do {
            if (bl2 && !(bl2 = false) && n == 0) continue;
            DJRMEMXO.s[n4] = new DJRMEMXO();
            ++n4;
        } while (n4 < 10);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final ZKARKDQW b(int n, int n2) {
        int n3 = client.Jj;
        try {
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n3 == 0) continue;
                boolean bl2 = this.G = !this.G;
            } while (n >= 0);
            int n4 = this.v;
            int n5 = this.m;
            if (n2 == 1) {
                n4 = this.R;
                n5 = this.t;
            }
            if (n4 == -1) {
                return null;
            }
            ZKARKDQW zKARKDQW = ZKARKDQW.b(this.r, n4);
            if (n5 != -1) {
                ZKARKDQW zKARKDQW2 = ZKARKDQW.b(this.r, n5);
                ZKARKDQW[] zKARKDQWArray = new ZKARKDQW[]{zKARKDQW, zKARKDQW2};
                zKARKDQW = new ZKARKDQW(2, zKARKDQWArray, -38);
            }
            if (this.c != null) {
                int n6 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && n3 == 0) continue;
                    zKARKDQW.e(this.c[n6], this.g[n6]);
                    ++n6;
                } while (n6 < this.c.length);
            }
            return zKARKDQW;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("88378, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final boolean c(int n, int n2) {
        try {
            if (n != 40903) {
                this.ab = !this.ab;
            }
            int n3 = this.l;
            int n4 = this.I;
            int n5 = this.F;
            if (n2 == 1) {
                n3 = this.U;
                n4 = this.k;
                n5 = this.i;
            }
            if (n3 == -1) {
                return true;
            }
            boolean bl = true;
            if (!ZKARKDQW.c(n3)) {
                bl = false;
            }
            if (n4 != -1 && !ZKARKDQW.c(n4)) {
                bl = false;
            }
            if (n5 != -1 && !ZKARKDQW.c(n5)) {
                bl = false;
            }
            return bl;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("51557, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final ZKARKDQW a(boolean bl, int n) {
        int n2 = client.Jj;
        try {
            ZKARKDQW zKARKDQW;
            block13: {
                ZKARKDQW[] zKARKDQWArray;
                ZKARKDQW zKARKDQW2;
                int n3;
                block14: {
                    if (bl) {
                        throw new NullPointerException();
                    }
                    int n4 = this.l;
                    n3 = this.I;
                    int n5 = this.F;
                    if (n == 1) {
                        n4 = this.U;
                        n3 = this.k;
                        n5 = this.i;
                    }
                    if (n4 == -1) {
                        return null;
                    }
                    zKARKDQW = ZKARKDQW.b(this.r, n4);
                    if (n3 == -1) break block13;
                    if (n5 == -1) break block14;
                    zKARKDQW2 = ZKARKDQW.b(this.r, n3);
                    zKARKDQWArray = ZKARKDQW.b(this.r, n5);
                    ZKARKDQW[] zKARKDQWArray2 = new ZKARKDQW[]{zKARKDQW, zKARKDQW2, zKARKDQWArray};
                    zKARKDQW = new ZKARKDQW(3, zKARKDQWArray2, -38);
                    if (n2 == 0) break block13;
                }
                zKARKDQW2 = ZKARKDQW.b(this.r, n3);
                zKARKDQWArray = new ZKARKDQW[]{zKARKDQW, zKARKDQW2};
                zKARKDQW = new ZKARKDQW(2, zKARKDQWArray, -38);
            }
            if (n == 0 && this.Z != 0) {
                zKARKDQW.a(0, this.Z, 16384, 0);
            }
            if (n == 1 && this.a != 0) {
                zKARKDQW.a(0, this.a, 16384, 0);
            }
            if (this.c != null) {
                int n6 = 0;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n2 == 0) continue;
                    zKARKDQW.e(this.c[n6], this.g[n6]);
                    ++n6;
                } while (n6 < this.c.length);
            }
            return zKARKDQW;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("8710, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a() {
        this.u = 0;
        this.q = null;
        this.y = null;
        this.c = null;
        this.g = null;
        this.B = 2000;
        this.K = 0;
        this.S = 0;
        this.Y = 0;
        this.p = 0;
        this.O = 0;
        this.T = -1;
        this.w = false;
        this.b = 1;
        this.h = false;
        this.o = null;
        this.J = null;
        this.l = -1;
        this.I = -1;
        this.Z = 0;
        this.U = -1;
        this.k = -1;
        this.a = 0;
        this.F = -1;
        this.i = -1;
        this.v = -1;
        this.m = -1;
        this.R = -1;
        this.t = -1;
        this.N = null;
        this.V = null;
        this.z = -1;
        this.j = -1;
        this.n = 128;
        this.M = 128;
        this.L = 128;
        this.Q = 0;
        this.E = 0;
        this.W = 0;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public static final DJRMEMXO b(int n) {
        int n2 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && client.Jj == 0) continue;
            if (DJRMEMXO.s[n2].d == n) {
                return s[n2];
            }
            ++n2;
        } while (n2 < 10);
        A = (A + 1) % 10;
        DJRMEMXO dJRMEMXO = s[A];
        DJRMEMXO.D.z = P[n];
        dJRMEMXO.d = n;
        dJRMEMXO.a();
        dJRMEMXO.a(true, D);
        if (dJRMEMXO.j != -1) {
            dJRMEMXO.a((byte)61);
        }
        if (!C && dJRMEMXO.h) {
            dJRMEMXO.q = "Members Object";
            dJRMEMXO.y = "Login to a members' server to use this object.".getBytes();
            dJRMEMXO.o = null;
            dJRMEMXO.J = null;
            dJRMEMXO.W = 0;
        }
        return dJRMEMXO;
    }

    public void a(byte by) {
        try {
            DJRMEMXO dJRMEMXO = DJRMEMXO.b(this.j);
            this.u = dJRMEMXO.u;
            this.B = dJRMEMXO.B;
            this.K = dJRMEMXO.K;
            this.S = dJRMEMXO.S;
            this.Y = dJRMEMXO.Y;
            this.p = dJRMEMXO.p;
            this.O = dJRMEMXO.O;
            if (by != 61) {
                this.G = !this.G;
            }
            this.c = dJRMEMXO.c;
            this.g = dJRMEMXO.g;
            DJRMEMXO dJRMEMXO2 = DJRMEMXO.b(this.z);
            this.q = dJRMEMXO2.q;
            this.h = dJRMEMXO2.h;
            this.b = dJRMEMXO2.b;
            String string = "a";
            char c = dJRMEMXO2.q.charAt(0);
            if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
                string = "an";
            }
            this.y = ("Swap this note at any bank for " + string + " " + dJRMEMXO2.q + ".").getBytes();
            this.w = true;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("56771, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final CXGZMTJK a(int n, int n2, int n3, int n4) {
        int n5 = client.Jj;
        try {
            CXGZMTJK cXGZMTJK;
            block53: {
                block52: {
                    int n6;
                    int n7;
                    int n8;
                    int n9;
                    int n10;
                    int n11;
                    int n12;
                    int[] nArray;
                    int[] nArray2;
                    int n13;
                    int n14;
                    CXGZMTJK cXGZMTJK2;
                    DJRMEMXO dJRMEMXO;
                    block51: {
                        int n15;
                        block46: {
                            ZKARKDQW zKARKDQW;
                            if (n3 == 0) {
                                cXGZMTJK = (CXGZMTJK)e.a(n);
                                if (cXGZMTJK != null && cXGZMTJK.O != n2 && cXGZMTJK.O != -1) {
                                    cXGZMTJK.a();
                                    cXGZMTJK = null;
                                }
                                if (cXGZMTJK != null) {
                                    return cXGZMTJK;
                                }
                            }
                            dJRMEMXO = DJRMEMXO.b(n);
                            if (dJRMEMXO.N == null) {
                                n2 = -1;
                            }
                            if (n2 > 1) {
                                int n16 = -1;
                                int n17 = 0;
                                boolean bl = true;
                                do {
                                    if (bl && !(bl = false) && n5 == 0) continue;
                                    if (n2 >= dJRMEMXO.V[n17] && dJRMEMXO.V[n17] != 0) {
                                        n16 = dJRMEMXO.N[n17];
                                    }
                                    ++n17;
                                } while (n17 < 10);
                                if (n16 != -1) {
                                    dJRMEMXO = DJRMEMXO.b(n16);
                                }
                            }
                            if ((zKARKDQW = dJRMEMXO.c(1)) == null) {
                                return null;
                            }
                            cXGZMTJK2 = null;
                            if (dJRMEMXO.j != -1 && (cXGZMTJK2 = DJRMEMXO.a(dJRMEMXO.z, 10, -1, 9)) == null) {
                                return null;
                            }
                            cXGZMTJK = new CXGZMTJK(32, 32);
                            n14 = OPPOFIOL.F;
                            n13 = OPPOFIOL.G;
                            nArray2 = OPPOFIOL.L;
                            nArray = AFCKELYG.m;
                            n12 = AFCKELYG.n;
                            n11 = AFCKELYG.o;
                            n10 = AFCKELYG.r;
                            n9 = AFCKELYG.s;
                            n8 = AFCKELYG.p;
                            n7 = AFCKELYG.q;
                            OPPOFIOL.D = false;
                            AFCKELYG.a(32, 32, -293, cXGZMTJK.I);
                            AFCKELYG.a(32, 0, 0, 0, 32, 0);
                            OPPOFIOL.a((byte)6);
                            int n18 = dJRMEMXO.B;
                            if (n3 == -1) {
                                n18 = (int)((double)n18 * 1.5);
                            }
                            if (n3 > 0) {
                                n18 = (int)((double)n18 * 1.04);
                            }
                            int n19 = OPPOFIOL.J[dJRMEMXO.K] * n18 >> 16;
                            n15 = OPPOFIOL.K[dJRMEMXO.K] * n18 >> 16;
                            zKARKDQW.a(0, dJRMEMXO.S, dJRMEMXO.Y, dJRMEMXO.K, dJRMEMXO.p, n19 + zKARKDQW.k / 2 + dJRMEMXO.O, n15 + dJRMEMXO.O);
                            int n20 = 31;
                            boolean bl = true;
                            do {
                                if (bl && !(bl = false) && n5 == 0) continue;
                                n15 = 31;
                                boolean bl2 = true;
                                do {
                                    block42: {
                                        block45: {
                                            block44: {
                                                block43: {
                                                    if (bl2 && !(bl2 = false) && n5 == 0) continue;
                                                    if (cXGZMTJK.I[n20 + n15 * 32] != 0) break block42;
                                                    if (n20 <= 0 || cXGZMTJK.I[n20 - 1 + n15 * 32] <= 1) break block43;
                                                    cXGZMTJK.I[n20 + n15 * 32] = 1;
                                                    if (n5 == 0) break block42;
                                                }
                                                if (n15 <= 0 || cXGZMTJK.I[n20 + (n15 - 1) * 32] <= 1) break block44;
                                                cXGZMTJK.I[n20 + n15 * 32] = 1;
                                                if (n5 == 0) break block42;
                                            }
                                            if (n20 >= 31 || cXGZMTJK.I[n20 + 1 + n15 * 32] <= 1) break block45;
                                            cXGZMTJK.I[n20 + n15 * 32] = 1;
                                            if (n5 == 0) break block42;
                                        }
                                        if (n15 < 31 && cXGZMTJK.I[n20 + (n15 + 1) * 32] > 1) {
                                            cXGZMTJK.I[n20 + n15 * 32] = 1;
                                        }
                                    }
                                    --n15;
                                } while (n15 >= 0);
                                --n20;
                            } while (n20 >= 0);
                            if (n3 <= 0) break block46;
                            n6 = 31;
                            boolean bl3 = true;
                            do {
                                if (bl3 && !(bl3 = false) && n5 == 0) continue;
                                n15 = 31;
                                boolean bl4 = true;
                                do {
                                    block47: {
                                        block50: {
                                            block49: {
                                                block48: {
                                                    if (bl4 && !(bl4 = false) && n5 == 0) continue;
                                                    if (cXGZMTJK.I[n6 + n15 * 32] != 0) break block47;
                                                    if (n6 <= 0 || cXGZMTJK.I[n6 - 1 + n15 * 32] != 1) break block48;
                                                    cXGZMTJK.I[n6 + n15 * 32] = n3;
                                                    if (n5 == 0) break block47;
                                                }
                                                if (n15 <= 0 || cXGZMTJK.I[n6 + (n15 - 1) * 32] != 1) break block49;
                                                cXGZMTJK.I[n6 + n15 * 32] = n3;
                                                if (n5 == 0) break block47;
                                            }
                                            if (n6 >= 31 || cXGZMTJK.I[n6 + 1 + n15 * 32] != 1) break block50;
                                            cXGZMTJK.I[n6 + n15 * 32] = n3;
                                            if (n5 == 0) break block47;
                                        }
                                        if (n15 < 31 && cXGZMTJK.I[n6 + (n15 + 1) * 32] == 1) {
                                            cXGZMTJK.I[n6 + n15 * 32] = n3;
                                        }
                                    }
                                    --n15;
                                } while (n15 >= 0);
                                --n6;
                            } while (n6 >= 0);
                            if (n5 == 0) break block51;
                        }
                        if (n3 == 0) {
                            n6 = 31;
                            boolean bl = true;
                            do {
                                if (bl && !(bl = false) && n5 == 0) continue;
                                n15 = 31;
                                boolean bl5 = true;
                                do {
                                    if (bl5 && !(bl5 = false) && n5 == 0) continue;
                                    if (cXGZMTJK.I[n6 + n15 * 32] == 0 && n6 > 0 && n15 > 0 && cXGZMTJK.I[n6 - 1 + (n15 - 1) * 32] > 0) {
                                        cXGZMTJK.I[n6 + n15 * 32] = 0x302020;
                                    }
                                    --n15;
                                } while (n15 >= 0);
                                --n6;
                            } while (n6 >= 0);
                        }
                    }
                    if (dJRMEMXO.j != -1) {
                        n6 = cXGZMTJK2.N;
                        int n21 = cXGZMTJK2.O;
                        cXGZMTJK2.N = 32;
                        cXGZMTJK2.O = 32;
                        cXGZMTJK2.b(0, 16083, 0);
                        cXGZMTJK2.N = n6;
                        cXGZMTJK2.O = n21;
                    }
                    if (n3 == 0) {
                        e.a(cXGZMTJK, n, (byte)2);
                    }
                    AFCKELYG.a(n11, n12, -293, nArray);
                    AFCKELYG.a(n7, n10, false, n9, n8);
                    OPPOFIOL.F = n14;
                    OPPOFIOL.G = n13;
                    OPPOFIOL.L = nArray2;
                    OPPOFIOL.D = true;
                    if (n4 < 9 || n4 > 9) {
                        n6 = 1;
                        boolean bl = true;
                        do {
                            if (bl && !(bl = false) && n5 == 0) continue;
                            ++n6;
                        } while (n6 > 0);
                    }
                    if (!dJRMEMXO.w) break block52;
                    cXGZMTJK.N = 33;
                    if (n5 == 0) break block53;
                }
                cXGZMTJK.N = 32;
            }
            cXGZMTJK.O = n2;
            return cXGZMTJK;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("60477, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public final ZKARKDQW c(int n) {
        ZKARKDQW zKARKDQW;
        int n2;
        int n3 = client.Jj;
        if (this.N != null && n > 1) {
            int n4 = -1;
            n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n3 == 0) continue;
                if (n >= this.V[n2] && this.V[n2] != 0) {
                    n4 = this.N[n2];
                }
                ++n2;
            } while (n2 < 10);
            if (n4 != -1) {
                return DJRMEMXO.b(n4).c(1);
            }
        }
        if ((zKARKDQW = (ZKARKDQW)f.a(this.d)) != null) {
            return zKARKDQW;
        }
        zKARKDQW = ZKARKDQW.b(this.r, this.u);
        if (zKARKDQW == null) {
            return null;
        }
        if (this.n != 128 || this.M != 128 || this.L != 128) {
            zKARKDQW.b(this.n, this.L, this.x, this.M);
        }
        if (this.c != null) {
            n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n3 == 0) continue;
                zKARKDQW.e(this.c[n2], this.g[n2]);
                ++n2;
            } while (n2 < this.c.length);
        }
        zKARKDQW.a(64 + this.Q, 768 + this.E, -50, -10, -50, true);
        zKARKDQW.fb = true;
        f.a(zKARKDQW, this.d, (byte)2);
        return zKARKDQW;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final ZKARKDQW a(int n, boolean bl) {
        int n2 = client.Jj;
        try {
            int n3;
            if (this.N != null && n > 1) {
                int n4 = -1;
                n3 = 0;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n2 == 0) continue;
                    if (n >= this.V[n3] && this.V[n3] != 0) {
                        n4 = this.N[n3];
                    }
                    ++n3;
                } while (n3 < 10);
                if (n4 != -1) {
                    return DJRMEMXO.b(n4).a(1, true);
                }
            }
            ZKARKDQW zKARKDQW = ZKARKDQW.b(this.r, this.u);
            if (!bl) {
                throw new NullPointerException();
            }
            if (zKARKDQW == null) {
                return null;
            }
            if (this.c != null) {
                n3 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && n2 == 0) continue;
                    zKARKDQW.e(this.c[n3], this.g[n3]);
                    ++n3;
                } while (n3 < this.c.length);
            }
            return zKARKDQW;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("80813, " + n + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(boolean bl, MBMGIXGO mBMGIXGO) {
        int n = client.Jj;
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
                    this.u = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 2) {
                    this.q = mBMGIXGO.i();
                    if (n == 0) continue;
                }
                if (n2 == 3) {
                    this.y = mBMGIXGO.a((byte)30);
                    if (n == 0) continue;
                }
                if (n2 == 4) {
                    this.B = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 5) {
                    this.K = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 6) {
                    this.S = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 7) {
                    this.p = mBMGIXGO.e();
                    if (this.p <= Short.MAX_VALUE) continue;
                    this.p -= 65536;
                    if (n == 0) continue;
                }
                if (n2 == 8) {
                    this.O = mBMGIXGO.e();
                    if (this.O <= Short.MAX_VALUE) continue;
                    this.O -= 65536;
                    if (n == 0) continue;
                }
                if (n2 == 10) {
                    this.T = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 11) {
                    this.w = true;
                    if (n == 0) continue;
                }
                if (n2 == 12) {
                    this.b = mBMGIXGO.h();
                    if (n == 0) continue;
                }
                if (n2 == 16) {
                    this.h = true;
                    if (n == 0) continue;
                }
                if (n2 == 23) {
                    this.l = mBMGIXGO.e();
                    this.Z = mBMGIXGO.d();
                    if (n == 0) continue;
                }
                if (n2 == 24) {
                    this.I = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 25) {
                    this.U = mBMGIXGO.e();
                    this.a = mBMGIXGO.d();
                    if (n == 0) continue;
                }
                if (n2 == 26) {
                    this.k = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 >= 30 && n2 < 35) {
                    if (this.o == null) {
                        this.o = new String[5];
                    }
                    this.o[n2 - 30] = mBMGIXGO.i();
                    if (!this.o[n2 - 30].equalsIgnoreCase("hidden")) continue;
                    this.o[n2 - 30] = null;
                    if (n == 0) continue;
                }
                if (n2 >= 35 && n2 < 40) {
                    if (this.J == null) {
                        this.J = new String[5];
                    }
                    this.J[n2 - 35] = mBMGIXGO.i();
                    if (n == 0) continue;
                }
                if (n2 == 40) {
                    int n3 = mBMGIXGO.c();
                    this.c = new int[n3];
                    this.g = new int[n3];
                    int n4 = 0;
                    boolean bl2 = true;
                    do {
                        if (bl2 && !(bl2 = false) && n == 0) continue;
                        this.c[n4] = mBMGIXGO.e();
                        this.g[n4] = mBMGIXGO.e();
                        ++n4;
                    } while (n4 < n3);
                    if (n == 0) continue;
                }
                if (n2 == 78) {
                    this.F = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 79) {
                    this.i = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 90) {
                    this.v = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 91) {
                    this.R = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 92) {
                    this.m = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 93) {
                    this.t = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 95) {
                    this.Y = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 97) {
                    this.z = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 98) {
                    this.j = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 >= 100 && n2 < 110) {
                    if (this.N == null) {
                        this.N = new int[10];
                        this.V = new int[10];
                    }
                    this.N[n2 - 100] = mBMGIXGO.e();
                    this.V[n2 - 100] = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 110) {
                    this.n = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 111) {
                    this.M = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 112) {
                    this.L = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 113) {
                    this.Q = mBMGIXGO.d();
                    if (n == 0) continue;
                }
                if (n2 == 114) {
                    this.E = mBMGIXGO.d() * 5;
                    if (n == 0) continue;
                }
                if (n2 != 115) continue;
                this.W = mBMGIXGO.c();
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("11503, " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    DJRMEMXO() {
    }

    static {
        C = true;
    }
}

