/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

final class CKDEJADD {
    public int a = -1;
    private static int b;
    public int c = -1;
    public int d = -1;
    public int e = -1;
    private static MBMGIXGO f;
    public int g = -1;
    public static int h;
    private int i = 9;
    private int j = 1834;
    public String k;
    public String[] l;
    public int m = -1;
    public byte n = 1;
    private int o = 9;
    private int[] p;
    public int q = -1;
    private static int[] r;
    private int[] s;
    private static int t;
    public int u = -1;
    private int[] v;
    public int w = -1;
    public long x = -1L;
    public int y = 32;
    private static CKDEJADD[] z;
    private boolean A = false;
    public static client B;
    public int C = -1;
    public boolean D = true;
    private int E;
    private int F = 128;
    public boolean G = true;
    public int[] H;
    public byte[] I;
    public int J = -1;
    private int K = 128;
    private int L;
    public boolean M = false;
    private int[] N;
    public static GCPOSBWX O;
    public int P = -1;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public static final CKDEJADD a(int n) {
        int n2 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && client.Jj == 0) continue;
            if (CKDEJADD.z[n2].x == (long)n) {
                return z[n2];
            }
            ++n2;
        } while (n2 < 20);
        b = (b + 1) % 20;
        CKDEJADD cKDEJADD = CKDEJADD.z[CKDEJADD.b] = new CKDEJADD();
        CKDEJADD.f.z = r[n];
        cKDEJADD.x = n;
        cKDEJADD.a(true, f);
        return cKDEJADD;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final ZKARKDQW a(boolean bl) {
        int n = client.Jj;
        try {
            ZKARKDQW zKARKDQW;
            block17: {
                ZKARKDQW[] zKARKDQWArray;
                block16: {
                    if (this.H != null) {
                        CKDEJADD cKDEJADD = this.b(this.j);
                        if (cKDEJADD == null) {
                            return null;
                        }
                        return cKDEJADD.a(true);
                    }
                    if (this.s == null) {
                        return null;
                    }
                    boolean bl2 = false;
                    if (!bl) {
                        this.j = 303;
                    }
                    int n2 = 0;
                    boolean bl3 = true;
                    do {
                        if (bl3 && !(bl3 = false) && n == 0) continue;
                        if (!ZKARKDQW.c(this.s[n2])) {
                            bl2 = true;
                        }
                        ++n2;
                    } while (n2 < this.s.length);
                    if (bl2) {
                        return null;
                    }
                    zKARKDQWArray = new ZKARKDQW[this.s.length];
                    int n3 = 0;
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && n == 0) continue;
                        zKARKDQWArray[n3] = ZKARKDQW.b(this.o, this.s[n3]);
                        ++n3;
                    } while (n3 < this.s.length);
                    if (zKARKDQWArray.length != 1) break block16;
                    zKARKDQW = zKARKDQWArray[0];
                    if (n == 0) break block17;
                }
                zKARKDQW = new ZKARKDQW(zKARKDQWArray.length, zKARKDQWArray, -38);
            }
            if (this.v != null) {
                int n4 = 0;
                boolean bl5 = true;
                do {
                    if (bl5 && !(bl5 = false) && n == 0) continue;
                    zKARKDQW.e(this.v[n4], this.p[n4]);
                    ++n4;
                } while (n4 < this.v.length);
            }
            return zKARKDQW;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("61524, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final CKDEJADD b(int n) {
        try {
            int n2;
            block9: {
                block8: {
                    n2 = -1;
                    if (n != 1834) {
                        boolean bl = this.A = !this.A;
                    }
                    if (this.c == -1) break block8;
                    SXYSOXTR sXYSOXTR = SXYSOXTR.c[this.c];
                    int n3 = sXYSOXTR.e;
                    int n4 = sXYSOXTR.f;
                    int n5 = sXYSOXTR.g;
                    int n6 = client.Di[n5 - n4];
                    n2 = CKDEJADD.B.Bd[n3] >> n4 & n6;
                    if (client.Jj == 0) break block9;
                }
                if (this.e != -1) {
                    n2 = CKDEJADD.B.Bd[this.e];
                }
            }
            if (n2 < 0 || n2 >= this.H.length || this.H[n2] == -1) {
                return null;
            }
            return CKDEJADD.a(this.H[n2]);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("19218, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public static final void a(XTGLDHGX xTGLDHGX) {
        int n = client.Jj;
        f = new MBMGIXGO(xTGLDHGX.a("npc.dat", null), 891);
        MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("npc.idx", null), 891);
        h = mBMGIXGO.e();
        r = new int[h];
        int n2 = 2;
        int n3 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && n == 0) continue;
            CKDEJADD.r[n3] = n2;
            n2 += mBMGIXGO.e();
            ++n3;
        } while (n3 < h);
        z = new CKDEJADD[20];
        int n4 = 0;
        boolean bl2 = true;
        do {
            if (bl2 && !(bl2 = false) && n == 0) continue;
            CKDEJADD.z[n4] = new CKDEJADD();
            ++n4;
        } while (n4 < 20);
    }

    public static final void c(int n) {
        try {
            O = null;
            r = null;
            if (n >= 0) {
                t = 60;
            }
            z = null;
            f = null;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("86254, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final ZKARKDQW a(int n, int n2, int n3, int[] nArray) {
        int n4 = client.Jj;
        try {
            ZKARKDQW zKARKDQW;
            block27: {
                block26: {
                    ZKARKDQW zKARKDQW2;
                    block23: {
                        block25: {
                            ZKARKDQW[] zKARKDQWArray;
                            block24: {
                                int n5;
                                if (this.H != null) {
                                    CKDEJADD cKDEJADD = this.b(this.j);
                                    if (cKDEJADD == null) {
                                        return null;
                                    }
                                    return cKDEJADD.a(0, n2, n3, nArray);
                                }
                                zKARKDQW2 = (ZKARKDQW)O.a(this.x);
                                if (n != 0) {
                                    n5 = 1;
                                    boolean bl = true;
                                    do {
                                        if (bl && !(bl = false) && n4 == 0) continue;
                                        ++n5;
                                    } while (n5 > 0);
                                }
                                if (zKARKDQW2 != null) break block23;
                                n5 = 0;
                                int n6 = 0;
                                boolean bl = true;
                                do {
                                    if (bl && !(bl = false) && n4 == 0) continue;
                                    if (!ZKARKDQW.c(this.N[n6])) {
                                        n5 = 1;
                                    }
                                    ++n6;
                                } while (n6 < this.N.length);
                                if (n5 != 0) {
                                    return null;
                                }
                                zKARKDQWArray = new ZKARKDQW[this.N.length];
                                int n7 = 0;
                                boolean bl2 = true;
                                do {
                                    if (bl2 && !(bl2 = false) && n4 == 0) continue;
                                    zKARKDQWArray[n7] = ZKARKDQW.b(this.o, this.N[n7]);
                                    ++n7;
                                } while (n7 < this.N.length);
                                if (zKARKDQWArray.length != 1) break block24;
                                zKARKDQW2 = zKARKDQWArray[0];
                                if (n4 == 0) break block25;
                            }
                            zKARKDQW2 = new ZKARKDQW(zKARKDQWArray.length, zKARKDQWArray, -38);
                        }
                        if (this.v != null) {
                            int n8 = 0;
                            boolean bl = true;
                            do {
                                if (bl && !(bl = false) && n4 == 0) continue;
                                zKARKDQW2.e(this.v[n8], this.p[n8]);
                                ++n8;
                            } while (n8 < this.v.length);
                        }
                        zKARKDQW2.a((byte)-71);
                        zKARKDQW2.a(64 + this.E, 850 + this.L, -30, -50, -30, true);
                        O.a(zKARKDQW2, this.x, (byte)2);
                    }
                    zKARKDQW = ZKARKDQW.t;
                    zKARKDQW.a(7, zKARKDQW2, SQHJOGRT.a(n3, false) & SQHJOGRT.a(n2, false));
                    if (n3 == -1 || n2 == -1) break block26;
                    zKARKDQW.a(-20491, nArray, n2, n3);
                    if (n4 == 0) break block27;
                }
                if (n3 != -1) {
                    zKARKDQW.c(n3, 40542);
                }
            }
            if (this.K != 128 || this.F != 128) {
                zKARKDQW.b(this.K, this.K, this.i, this.F);
            }
            zKARKDQW.a(false);
            zKARKDQW.eb = null;
            zKARKDQW.db = null;
            if (this.n == 1) {
                zKARKDQW.fb = true;
            }
            return zKARKDQW;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("81939, " + n + ", " + n2 + ", " + n3 + ", " + nArray + ", " + runtimeException.toString());
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
            if (!bl) {
                throw new NullPointerException();
            }
            while (true) {
                int n2;
                int n3;
                int n4;
                if ((n4 = mBMGIXGO.c()) == 0) {
                    return;
                }
                if (n4 == 1) {
                    n3 = mBMGIXGO.c();
                    this.N = new int[n3];
                    n2 = 0;
                    boolean bl2 = true;
                    do {
                        if (bl2 && !(bl2 = false) && n == 0) continue;
                        this.N[n2] = mBMGIXGO.e();
                        ++n2;
                    } while (n2 < n3);
                    if (n == 0) continue;
                }
                if (n4 == 2) {
                    this.k = mBMGIXGO.i();
                    if (n == 0) continue;
                }
                if (n4 == 3) {
                    this.I = mBMGIXGO.a((byte)30);
                    if (n == 0) continue;
                }
                if (n4 == 12) {
                    this.n = mBMGIXGO.d();
                    if (n == 0) continue;
                }
                if (n4 == 13) {
                    this.w = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 == 14) {
                    this.m = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 == 17) {
                    this.m = mBMGIXGO.e();
                    this.d = mBMGIXGO.e();
                    this.C = mBMGIXGO.e();
                    this.a = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 >= 30 && n4 < 40) {
                    if (this.l == null) {
                        this.l = new String[5];
                    }
                    this.l[n4 - 30] = mBMGIXGO.i();
                    if (!this.l[n4 - 30].equalsIgnoreCase("hidden")) continue;
                    this.l[n4 - 30] = null;
                    if (n == 0) continue;
                }
                if (n4 == 40) {
                    n3 = mBMGIXGO.c();
                    this.v = new int[n3];
                    this.p = new int[n3];
                    n2 = 0;
                    boolean bl3 = true;
                    do {
                        if (bl3 && !(bl3 = false) && n == 0) continue;
                        this.v[n2] = mBMGIXGO.e();
                        this.p[n2] = mBMGIXGO.e();
                        ++n2;
                    } while (n2 < n3);
                    if (n == 0) continue;
                }
                if (n4 == 60) {
                    n3 = mBMGIXGO.c();
                    this.s = new int[n3];
                    n2 = 0;
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && n == 0) continue;
                        this.s[n2] = mBMGIXGO.e();
                        ++n2;
                    } while (n2 < n3);
                    if (n == 0) continue;
                }
                if (n4 == 90) {
                    this.P = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 == 91) {
                    this.q = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 == 92) {
                    this.J = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 == 93) {
                    this.G = false;
                    if (n == 0) continue;
                }
                if (n4 == 95) {
                    this.g = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 == 97) {
                    this.K = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 == 98) {
                    this.F = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 == 99) {
                    this.M = true;
                    if (n == 0) continue;
                }
                if (n4 == 100) {
                    this.E = mBMGIXGO.d();
                    if (n == 0) continue;
                }
                if (n4 == 101) {
                    this.L = mBMGIXGO.d() * 5;
                    if (n == 0) continue;
                }
                if (n4 == 102) {
                    this.u = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 == 103) {
                    this.y = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n4 == 106) {
                    this.c = mBMGIXGO.e();
                    if (this.c == 65535) {
                        this.c = -1;
                    }
                    this.e = mBMGIXGO.e();
                    if (this.e == 65535) {
                        this.e = -1;
                    }
                    n3 = mBMGIXGO.c();
                    this.H = new int[n3 + 1];
                    n2 = 0;
                    boolean bl5 = true;
                    do {
                        if (bl5 && !(bl5 = false) && n == 0) continue;
                        this.H[n2] = mBMGIXGO.e();
                        if (this.H[n2] == 65535) {
                            this.H[n2] = -1;
                        }
                        ++n2;
                    } while (n2 <= n3);
                    if (n == 0) continue;
                }
                if (n4 != 107) continue;
                this.D = false;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("54892, " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    CKDEJADD() {
    }

    static {
        t = 748;
        O = new GCPOSBWX(false, 30);
    }
}

