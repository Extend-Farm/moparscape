/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class ZKARKDQW
extends XHHRODPC {
    private int m;
    private boolean n;
    private int o;
    private int p;
    private boolean q;
    private static int r = -192;
    public static int s;
    public static ZKARKDQW t;
    private static int[] u;
    private static int[] v;
    private static int[] w;
    private static int[] x;
    public int y;
    public int[] z;
    public int[] A;
    public int[] B;
    public int C;
    public int[] D;
    public int[] E;
    public int[] F;
    public int[] G;
    public int[] H;
    public int[] I;
    public int[] J;
    public int[] K;
    public int[] L;
    public int[] M;
    public int N;
    public int O;
    public int[] P;
    public int[] Q;
    public int[] R;
    public int S;
    public int T;
    public int U;
    public int V;
    public int W;
    public int X;
    public int Y;
    public int Z;
    public int ab;
    public int[] bb;
    public int[] cb;
    public int[][] db;
    public int[][] eb;
    public boolean fb;
    RJXWGZGD[] gb;
    static LLORVYLP[] hb;
    static VJKFYAWG ib;
    static boolean[] jb;
    static boolean[] kb;
    static int[] lb;
    static int[] mb;
    static int[] nb;
    static int[] ob;
    static int[] pb;
    static int[] qb;
    static int[] rb;
    static int[][] sb;
    static int[] tb;
    static int[][] ub;
    static int[] vb;
    static int[] wb;
    static int[] xb;
    static int[] yb;
    static int[] zb;
    static int[] Ab;
    static int Bb;
    static int Cb;
    static int Db;
    public static boolean Eb;
    public static int Fb;
    public static int Gb;
    public static int Hb;
    public static int[] Ib;
    public static int[] Jb;
    public static int[] Kb;
    static int[] Lb;
    static int[] Mb;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void b(int n) {
        try {
            hb = null;
            jb = null;
            kb = null;
            lb = null;
            if (XHHRODPC.l || n >= 0) {
                return;
            }
            mb = null;
            nb = null;
            ob = null;
            pb = null;
            qb = null;
            rb = null;
            sb = null;
            tb = null;
            ub = null;
            vb = null;
            wb = null;
            xb = null;
            Jb = null;
            Kb = null;
            Lb = null;
            Mb = null;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("30970, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static void a(int n, VJKFYAWG vJKFYAWG) {
        hb = new LLORVYLP[n];
        ib = vJKFYAWG;
    }

    public static void a(byte[] byArray, int n, int n2) {
        boolean bl = XHHRODPC.l;
        try {
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            LLORVYLP lLORVYLP;
            block28: {
                block27: {
                    int n8;
                    block26: {
                        block25: {
                            int n9;
                            block24: {
                                block23: {
                                    int n10;
                                    block22: {
                                        block21: {
                                            int n11;
                                            block20: {
                                                int n12;
                                                block19: {
                                                    if (byArray == null) {
                                                        LLORVYLP lLORVYLP2 = ZKARKDQW.hb[n2] = new LLORVYLP();
                                                        lLORVYLP2.b = 0;
                                                        lLORVYLP2.c = 0;
                                                        lLORVYLP2.d = 0;
                                                        return;
                                                    }
                                                    MBMGIXGO mBMGIXGO = new MBMGIXGO(byArray, 891);
                                                    mBMGIXGO.z = byArray.length - 18;
                                                    lLORVYLP = ZKARKDQW.hb[n2] = new LLORVYLP();
                                                    lLORVYLP.a = byArray;
                                                    lLORVYLP.b = mBMGIXGO.e();
                                                    lLORVYLP.c = mBMGIXGO.e();
                                                    lLORVYLP.d = mBMGIXGO.c();
                                                    n10 = mBMGIXGO.c();
                                                    n12 = mBMGIXGO.c();
                                                    if (n != -4036) {
                                                        return;
                                                    }
                                                    n8 = mBMGIXGO.c();
                                                    n11 = mBMGIXGO.c();
                                                    n9 = mBMGIXGO.c();
                                                    n7 = mBMGIXGO.e();
                                                    n6 = mBMGIXGO.e();
                                                    n5 = mBMGIXGO.e();
                                                    n4 = mBMGIXGO.e();
                                                    lLORVYLP.e = n3 = 0;
                                                    lLORVYLP.k = n3 += lLORVYLP.b;
                                                    lLORVYLP.n = n3 += lLORVYLP.c;
                                                    if (n12 != 255) break block19;
                                                    n3 += lLORVYLP.c;
                                                    if (!bl) break block20;
                                                }
                                                lLORVYLP.n = -n12 - 1;
                                            }
                                            lLORVYLP.p = n3;
                                            if (n11 != 1) break block21;
                                            n3 += lLORVYLP.c;
                                            if (!bl) break block22;
                                        }
                                        lLORVYLP.p = -1;
                                    }
                                    lLORVYLP.m = n3;
                                    if (n10 != 1) break block23;
                                    n3 += lLORVYLP.c;
                                    if (!bl) break block24;
                                }
                                lLORVYLP.m = -1;
                            }
                            lLORVYLP.i = n3;
                            if (n9 != 1) break block25;
                            n3 += lLORVYLP.b;
                            if (!bl) break block26;
                        }
                        lLORVYLP.i = -1;
                    }
                    lLORVYLP.o = n3;
                    if (n8 != 1) break block27;
                    n3 += lLORVYLP.c;
                    if (!bl) break block28;
                }
                lLORVYLP.o = -1;
            }
            lLORVYLP.j = n3;
            lLORVYLP.l = n3 += n4;
            lLORVYLP.q = n3 += lLORVYLP.c * 2;
            lLORVYLP.f = n3 += lLORVYLP.d * 6;
            lLORVYLP.g = n3 += n7;
            lLORVYLP.h = n3 += n6;
            n3 += n5;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("43492, " + byArray + ", " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static void a(int n, int n2) {
        try {
            ZKARKDQW.hb[n2] = null;
            if (n <= 0) {
                r = -219;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("72035, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ZKARKDQW b(int n, int n2) {
        try {
            if (hb == null) {
                return null;
            }
            LLORVYLP lLORVYLP = hb[n2];
            if (n != 9) {
                int n3 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && !XHHRODPC.l) continue;
                    ++n3;
                } while (n3 > 0);
            }
            if (lLORVYLP == null) {
                ib.a(n2);
                return null;
            }
            return new ZKARKDQW(n2, -870);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("30916, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static boolean c(int n) {
        if (hb == null) {
            return false;
        }
        LLORVYLP lLORVYLP = hb[n];
        if (lLORVYLP == null) {
            ib.a(n);
            return false;
        }
        return true;
    }

    private ZKARKDQW(boolean bl) {
        this.m = 9;
        this.n = false;
        this.o = 360;
        this.p = 1;
        this.q = true;
        this.fb = false;
        try {
            if (!bl) {
                this.q = !this.q;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("59290, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private ZKARKDQW(int n, int n2) {
        boolean bl = XHHRODPC.l;
        this.m = 9;
        this.n = false;
        this.o = 360;
        this.p = 1;
        this.q = true;
        this.fb = false;
        try {
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            LLORVYLP lLORVYLP;
            block27: {
                block26: {
                    ++s;
                    lLORVYLP = hb[n];
                    this.y = lLORVYLP.b;
                    this.C = lLORVYLP.c;
                    this.O = lLORVYLP.d;
                    this.z = new int[this.y];
                    this.A = new int[this.y];
                    this.B = new int[this.y];
                    this.D = new int[this.C];
                    this.E = new int[this.C];
                    boolean bl2 = true;
                    do {
                        if (bl2 && !(bl2 = false) && !bl) continue;
                        boolean bl3 = this.q = !this.q;
                    } while (n2 >= 0);
                    this.F = new int[this.C];
                    this.P = new int[this.O];
                    this.Q = new int[this.O];
                    this.R = new int[this.O];
                    if (lLORVYLP.i >= 0) {
                        this.bb = new int[this.y];
                    }
                    if (lLORVYLP.m >= 0) {
                        this.J = new int[this.C];
                    }
                    if (lLORVYLP.n < 0) break block26;
                    this.K = new int[this.C];
                    if (!bl) break block27;
                }
                this.N = -lLORVYLP.n - 1;
            }
            if (lLORVYLP.o >= 0) {
                this.L = new int[this.C];
            }
            if (lLORVYLP.p >= 0) {
                this.cb = new int[this.C];
            }
            this.M = new int[this.C];
            MBMGIXGO mBMGIXGO = new MBMGIXGO(lLORVYLP.a, 891);
            mBMGIXGO.z = lLORVYLP.e;
            MBMGIXGO mBMGIXGO2 = new MBMGIXGO(lLORVYLP.a, 891);
            mBMGIXGO2.z = lLORVYLP.f;
            MBMGIXGO mBMGIXGO3 = new MBMGIXGO(lLORVYLP.a, 891);
            mBMGIXGO3.z = lLORVYLP.g;
            MBMGIXGO mBMGIXGO4 = new MBMGIXGO(lLORVYLP.a, 891);
            mBMGIXGO4.z = lLORVYLP.h;
            MBMGIXGO mBMGIXGO5 = new MBMGIXGO(lLORVYLP.a, 891);
            mBMGIXGO5.z = lLORVYLP.i;
            int n8 = 0;
            int n9 = 0;
            int n10 = 0;
            int n11 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl) continue;
                n7 = mBMGIXGO.c();
                n6 = 0;
                if ((n7 & 1) != 0) {
                    n6 = mBMGIXGO2.j();
                }
                n5 = 0;
                if ((n7 & 2) != 0) {
                    n5 = mBMGIXGO3.j();
                }
                n4 = 0;
                if ((n7 & 4) != 0) {
                    n4 = mBMGIXGO4.j();
                }
                this.z[n11] = n8 + n6;
                this.A[n11] = n9 + n5;
                this.B[n11] = n10 + n4;
                n8 = this.z[n11];
                n9 = this.A[n11];
                n10 = this.B[n11];
                if (this.bb != null) {
                    this.bb[n11] = mBMGIXGO5.c();
                }
                ++n11;
            } while (n11 < this.y);
            mBMGIXGO.z = lLORVYLP.l;
            mBMGIXGO2.z = lLORVYLP.m;
            mBMGIXGO3.z = lLORVYLP.n;
            mBMGIXGO4.z = lLORVYLP.o;
            mBMGIXGO5.z = lLORVYLP.p;
            n7 = 0;
            boolean bl5 = true;
            do {
                if (bl5 && !(bl5 = false) && !bl) continue;
                this.M[n7] = mBMGIXGO.e();
                if (this.J != null) {
                    this.J[n7] = mBMGIXGO2.c();
                }
                if (this.K != null) {
                    this.K[n7] = mBMGIXGO3.c();
                }
                if (this.L != null) {
                    this.L[n7] = mBMGIXGO4.c();
                }
                if (this.cb != null) {
                    this.cb[n7] = mBMGIXGO5.c();
                }
                ++n7;
            } while (n7 < this.C);
            mBMGIXGO.z = lLORVYLP.j;
            mBMGIXGO2.z = lLORVYLP.k;
            n6 = 0;
            n5 = 0;
            n4 = 0;
            int n12 = 0;
            int n13 = 0;
            boolean bl6 = true;
            do {
                if (bl6 && !(bl6 = false) && !bl) continue;
                n3 = mBMGIXGO2.c();
                if (n3 == 1) {
                    n12 = n6 = mBMGIXGO.j() + n12;
                    n12 = n5 = mBMGIXGO.j() + n12;
                    n12 = n4 = mBMGIXGO.j() + n12;
                    this.D[n13] = n6;
                    this.E[n13] = n5;
                    this.F[n13] = n4;
                }
                if (n3 == 2) {
                    n5 = n4;
                    n12 = n4 = mBMGIXGO.j() + n12;
                    this.D[n13] = n6;
                    this.E[n13] = n5;
                    this.F[n13] = n4;
                }
                if (n3 == 3) {
                    n6 = n4;
                    n12 = n4 = mBMGIXGO.j() + n12;
                    this.D[n13] = n6;
                    this.E[n13] = n5;
                    this.F[n13] = n4;
                }
                if (n3 == 4) {
                    int n14 = n6;
                    n6 = n5;
                    n5 = n14;
                    n12 = n4 = mBMGIXGO.j() + n12;
                    this.D[n13] = n6;
                    this.E[n13] = n5;
                    this.F[n13] = n4;
                }
                ++n13;
            } while (n13 < this.C);
            mBMGIXGO.z = lLORVYLP.q;
            n3 = 0;
            boolean bl7 = true;
            do {
                if (bl7 && !(bl7 = false) && !bl) continue;
                this.P[n3] = mBMGIXGO.e();
                this.Q[n3] = mBMGIXGO.e();
                this.R[n3] = mBMGIXGO.e();
                ++n3;
            } while (n3 < this.O);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("51145, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ZKARKDQW(int n, ZKARKDQW[] zKARKDQWArray, int n2) {
        boolean bl = XHHRODPC.l;
        this.m = 9;
        this.n = false;
        this.o = 360;
        this.p = 1;
        this.q = true;
        this.fb = false;
        try {
            ++s;
            boolean bl2 = false;
            boolean bl3 = false;
            boolean bl4 = false;
            boolean bl5 = false;
            this.y = 0;
            this.C = 0;
            this.O = 0;
            this.N = -1;
            int n3 = 0;
            boolean bl6 = true;
            do {
                block27: {
                    ZKARKDQW zKARKDQW;
                    block29: {
                        block28: {
                            if (bl6 && !(bl6 = false) && !bl) continue;
                            zKARKDQW = zKARKDQWArray[n3];
                            if (zKARKDQW == null) break block27;
                            this.y += zKARKDQW.y;
                            this.C += zKARKDQW.C;
                            this.O += zKARKDQW.O;
                            bl2 |= zKARKDQW.J != null;
                            if (zKARKDQW.K == null) break block28;
                            bl3 = true;
                            if (!bl) break block29;
                        }
                        if (this.N == -1) {
                            this.N = zKARKDQW.N;
                        }
                        if (this.N != zKARKDQW.N) {
                            bl3 = true;
                        }
                    }
                    bl4 |= zKARKDQW.L != null;
                    bl5 |= zKARKDQW.cb != null;
                }
                ++n3;
            } while (n3 < n);
            this.z = new int[this.y];
            this.A = new int[this.y];
            this.B = new int[this.y];
            this.bb = new int[this.y];
            this.D = new int[this.C];
            this.E = new int[this.C];
            this.F = new int[this.C];
            this.P = new int[this.O];
            this.Q = new int[this.O];
            this.R = new int[this.O];
            if (bl2) {
                this.J = new int[this.C];
            }
            if (bl3) {
                this.K = new int[this.C];
            }
            if (bl4) {
                this.L = new int[this.C];
            }
            if (bl5) {
                this.cb = new int[this.C];
            }
            this.M = new int[this.C];
            this.y = 0;
            this.C = 0;
            this.O = 0;
            if (n2 >= 0) {
                r = 23;
            }
            int n4 = 0;
            int n5 = 0;
            boolean bl7 = true;
            do {
                block30: {
                    int n6;
                    if (bl7 && !(bl7 = false) && !bl) continue;
                    ZKARKDQW zKARKDQW = zKARKDQWArray[n5];
                    if (zKARKDQW == null) break block30;
                    int n7 = 0;
                    boolean bl8 = true;
                    do {
                        block35: {
                            block36: {
                                block33: {
                                    block34: {
                                        block31: {
                                            block32: {
                                                if (bl8 && !(bl8 = false) && !bl) continue;
                                                if (!bl2) break block31;
                                                if (zKARKDQW.J != null) break block32;
                                                this.J[this.C] = 0;
                                                if (!bl) break block31;
                                            }
                                            if (((n6 = zKARKDQW.J[n7]) & 2) == 2) {
                                                n6 += n4 << 2;
                                            }
                                            this.J[this.C] = n6;
                                        }
                                        if (!bl3) break block33;
                                        if (zKARKDQW.K != null) break block34;
                                        this.K[this.C] = zKARKDQW.N;
                                        if (!bl) break block33;
                                    }
                                    this.K[this.C] = zKARKDQW.K[n7];
                                }
                                if (!bl4) break block35;
                                if (zKARKDQW.L != null) break block36;
                                this.L[this.C] = 0;
                                if (!bl) break block35;
                            }
                            this.L[this.C] = zKARKDQW.L[n7];
                        }
                        if (bl5 && zKARKDQW.cb != null) {
                            this.cb[this.C] = zKARKDQW.cb[n7];
                        }
                        this.M[this.C] = zKARKDQW.M[n7];
                        this.D[this.C] = this.a(zKARKDQW, zKARKDQW.D[n7]);
                        this.E[this.C] = this.a(zKARKDQW, zKARKDQW.E[n7]);
                        this.F[this.C] = this.a(zKARKDQW, zKARKDQW.F[n7]);
                        ++this.C;
                        ++n7;
                    } while (n7 < zKARKDQW.C);
                    n6 = 0;
                    boolean bl9 = true;
                    do {
                        if (bl9 && !(bl9 = false) && !bl) continue;
                        this.P[this.O] = this.a(zKARKDQW, zKARKDQW.P[n6]);
                        this.Q[this.O] = this.a(zKARKDQW, zKARKDQW.Q[n6]);
                        this.R[this.O] = this.a(zKARKDQW, zKARKDQW.R[n6]);
                        ++this.O;
                        ++n6;
                    } while (n6 < zKARKDQW.O);
                    n4 += zKARKDQW.O;
                }
                ++n5;
            } while (n5 < n);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("84140, " + n + ", " + zKARKDQWArray + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ZKARKDQW(int n, int n2, boolean bl, ZKARKDQW[] zKARKDQWArray) {
        boolean bl2 = XHHRODPC.l;
        this.m = 9;
        this.n = false;
        this.o = 360;
        this.p = 1;
        this.q = true;
        this.fb = false;
        try {
            ++s;
            boolean bl3 = false;
            boolean bl4 = false;
            boolean bl5 = false;
            boolean bl6 = false;
            this.y = 0;
            this.C = 0;
            this.O = 0;
            this.N = -1;
            int n3 = 0;
            boolean bl7 = true;
            do {
                block33: {
                    ZKARKDQW zKARKDQW;
                    block35: {
                        block34: {
                            if (bl7 && !(bl7 = false) && !bl2) continue;
                            zKARKDQW = zKARKDQWArray[n3];
                            if (zKARKDQW == null) break block33;
                            this.y += zKARKDQW.y;
                            this.C += zKARKDQW.C;
                            this.O += zKARKDQW.O;
                            bl3 |= zKARKDQW.J != null;
                            if (zKARKDQW.K == null) break block34;
                            bl4 = true;
                            if (!bl2) break block35;
                        }
                        if (this.N == -1) {
                            this.N = zKARKDQW.N;
                        }
                        if (this.N != zKARKDQW.N) {
                            bl4 = true;
                        }
                    }
                    bl5 |= zKARKDQW.L != null;
                    bl6 |= zKARKDQW.M != null;
                }
                ++n3;
            } while (n3 < n);
            this.z = new int[this.y];
            this.A = new int[this.y];
            this.B = new int[this.y];
            this.D = new int[this.C];
            this.E = new int[this.C];
            this.F = new int[this.C];
            this.G = new int[this.C];
            this.H = new int[this.C];
            this.I = new int[this.C];
            this.P = new int[this.O];
            this.Q = new int[this.O];
            this.R = new int[this.O];
            if (n2 >= 0) {
                int n4 = 1;
                boolean bl8 = true;
                do {
                    if (bl8 && !(bl8 = false) && !bl2) continue;
                    ++n4;
                } while (n4 > 0);
            }
            if (bl3) {
                this.J = new int[this.C];
            }
            if (bl4) {
                this.K = new int[this.C];
            }
            if (bl5) {
                this.L = new int[this.C];
            }
            if (bl6) {
                this.M = new int[this.C];
            }
            this.y = 0;
            this.C = 0;
            this.O = 0;
            int n5 = 0;
            int n6 = 0;
            boolean bl9 = true;
            do {
                block36: {
                    int n7;
                    if (bl9 && !(bl9 = false) && !bl2) continue;
                    ZKARKDQW zKARKDQW = zKARKDQWArray[n6];
                    if (zKARKDQW == null) break block36;
                    int n8 = this.y;
                    int n9 = 0;
                    boolean bl10 = true;
                    do {
                        if (bl10 && !(bl10 = false) && !bl2) continue;
                        this.z[this.y] = zKARKDQW.z[n9];
                        this.A[this.y] = zKARKDQW.A[n9];
                        this.B[this.y] = zKARKDQW.B[n9];
                        ++this.y;
                        ++n9;
                    } while (n9 < zKARKDQW.y);
                    int n10 = 0;
                    boolean bl11 = true;
                    do {
                        block41: {
                            block42: {
                                block39: {
                                    block40: {
                                        block37: {
                                            block38: {
                                                if (bl11 && !(bl11 = false) && !bl2) continue;
                                                this.D[this.C] = zKARKDQW.D[n10] + n8;
                                                this.E[this.C] = zKARKDQW.E[n10] + n8;
                                                this.F[this.C] = zKARKDQW.F[n10] + n8;
                                                this.G[this.C] = zKARKDQW.G[n10];
                                                this.H[this.C] = zKARKDQW.H[n10];
                                                this.I[this.C] = zKARKDQW.I[n10];
                                                if (!bl3) break block37;
                                                if (zKARKDQW.J != null) break block38;
                                                this.J[this.C] = 0;
                                                if (!bl2) break block37;
                                            }
                                            if (((n7 = zKARKDQW.J[n10]) & 2) == 2) {
                                                n7 += n5 << 2;
                                            }
                                            this.J[this.C] = n7;
                                        }
                                        if (!bl4) break block39;
                                        if (zKARKDQW.K != null) break block40;
                                        this.K[this.C] = zKARKDQW.N;
                                        if (!bl2) break block39;
                                    }
                                    this.K[this.C] = zKARKDQW.K[n10];
                                }
                                if (!bl5) break block41;
                                if (zKARKDQW.L != null) break block42;
                                this.L[this.C] = 0;
                                if (!bl2) break block41;
                            }
                            this.L[this.C] = zKARKDQW.L[n10];
                        }
                        if (bl6 && zKARKDQW.M != null) {
                            this.M[this.C] = zKARKDQW.M[n10];
                        }
                        ++this.C;
                        ++n10;
                    } while (n10 < zKARKDQW.C);
                    n7 = 0;
                    boolean bl12 = true;
                    do {
                        if (bl12 && !(bl12 = false) && !bl2) continue;
                        this.P[this.O] = zKARKDQW.P[n7] + n8;
                        this.Q[this.O] = zKARKDQW.Q[n7] + n8;
                        this.R[this.O] = zKARKDQW.R[n7] + n8;
                        ++this.O;
                        ++n7;
                    } while (n7 < zKARKDQW.O);
                    n5 += zKARKDQW.O;
                }
                ++n6;
            } while (n6 < n);
            this.a(false);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("65900, " + n + ", " + n2 + ", " + bl + ", " + zKARKDQWArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public ZKARKDQW(int var1_1, boolean var2_2, boolean var3_3, boolean var4_4, ZKARKDQW var5_5) {
        var7_6 = XHHRODPC.l;
        super();
        this.m = 9;
        this.n = false;
        this.o = 360;
        this.p = 1;
        this.q = true;
        this.fb = false;
        try {
            block14: {
                block12: {
                    block13: {
                        block11: {
                            block10: {
                                block9: {
                                    block8: {
                                        block7: {
                                            ++ZKARKDQW.s;
                                            this.y = var5_5.y;
                                            this.C = var5_5.C;
                                            this.O = var5_5.O;
                                            if (!var4_4) break block7;
                                            this.z = var5_5.z;
                                            this.A = var5_5.A;
                                            this.B = var5_5.B;
                                            if (!var7_6) break block8;
                                        }
                                        this.z = new int[this.y];
                                        this.A = new int[this.y];
                                        this.B = new int[this.y];
                                        var6_7 = 0;
                                        if (!var7_6) ** GOTO lbl30
                                        do {
                                            this.z[var6_7] = var5_5.z[var6_7];
                                            this.A[var6_7] = var5_5.A[var6_7];
                                            this.B[var6_7] = var5_5.B[var6_7];
                                            ++var6_7;
lbl30:
                                            // 2 sources

                                        } while (var6_7 < this.y);
                                    }
                                    if (!var2_2) break block9;
                                    this.M = var5_5.M;
                                    if (!var7_6) break block10;
                                }
                                this.M = new int[this.C];
                                var6_7 = 0;
                                if (!var7_6) ** GOTO lbl42
                                do {
                                    this.M[var6_7] = var5_5.M[var6_7];
                                    ++var6_7;
lbl42:
                                    // 2 sources

                                } while (var6_7 < this.C);
                            }
                            if (!var3_3) break block11;
                            this.L = var5_5.L;
                            if (!var7_6) break block12;
                        }
                        this.L = new int[this.C];
                        if (var5_5.L != null) break block13;
                        var6_7 = 0;
                        if (!var7_6) ** GOTO lbl55
                        do {
                            this.L[var6_7] = 0;
                            ++var6_7;
lbl55:
                            // 2 sources

                        } while (var6_7 < this.C);
                        break block12;
                    }
                    var6_7 = 0;
                    if (!var7_6) ** GOTO lbl63
                    do {
                        this.L[var6_7] = var5_5.L[var6_7];
                        ++var6_7;
lbl63:
                        // 2 sources

                    } while (var6_7 < this.C);
                }
                this.bb = var5_5.bb;
                this.cb = var5_5.cb;
                this.J = var5_5.J;
                this.D = var5_5.D;
                this.E = var5_5.E;
                this.F = var5_5.F;
                this.K = var5_5.K;
                this.N = var5_5.N;
                this.P = var5_5.P;
                if (var1_1 >= 9 && var1_1 <= 9) break block14;
                var6_7 = 1;
                if (!var7_6) ** GOTO lbl79
                do {
                    ++var6_7;
lbl79:
                    // 2 sources

                } while (var6_7 > 0);
            }
            this.Q = var5_5.Q;
            this.R = var5_5.R;
            return;
        }
        catch (RuntimeException var6_8) {
            signlink.reporterror("76077, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_4 + ", " + var5_5 + ", " + var6_8.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public ZKARKDQW(boolean var1_1, int var2_2, boolean var3_3, ZKARKDQW var4_4) {
        var9_5 = XHHRODPC.l;
        super();
        this.m = 9;
        this.n = false;
        this.o = 360;
        this.p = 1;
        this.q = true;
        this.fb = false;
        try {
            block17: {
                block14: {
                    block16: {
                        block15: {
                            block13: {
                                block12: {
                                    ++ZKARKDQW.s;
                                    this.y = var4_4.y;
                                    this.C = var4_4.C;
                                    this.O = var4_4.O;
                                    if (!var1_1) break block12;
                                    this.A = new int[this.y];
                                    var5_6 = 0;
                                    if (!var9_5) ** GOTO lbl21
                                    do {
                                        this.A[var5_6] = var4_4.A[var5_6];
                                        ++var5_6;
lbl21:
                                        // 2 sources

                                    } while (var5_6 < this.y);
                                    break block13;
                                }
                                this.A = var4_4.A;
                            }
                            if (!var3_3) break block14;
                            this.G = new int[this.C];
                            this.H = new int[this.C];
                            this.I = new int[this.C];
                            var5_6 = 0;
                            if (!var9_5) ** GOTO lbl37
                            do {
                                this.G[var5_6] = var4_4.G[var5_6];
                                this.H[var5_6] = var4_4.H[var5_6];
                                this.I[var5_6] = var4_4.I[var5_6];
                                ++var5_6;
lbl37:
                                // 2 sources

                            } while (var5_6 < this.C);
                            this.J = new int[this.C];
                            if (var4_4.J != null) break block15;
                            var6_8 = 0;
                            if (!var9_5) ** GOTO lbl45
                            do {
                                this.J[var6_8] = 0;
                                ++var6_8;
lbl45:
                                // 2 sources

                            } while (var6_8 < this.C);
                            break block16;
                        }
                        var6_8 = 0;
                        if (!var9_5) ** GOTO lbl53
                        do {
                            this.J[var6_8] = var4_4.J[var6_8];
                            ++var6_8;
lbl53:
                            // 2 sources

                        } while (var6_8 < this.C);
                    }
                    this.j = new RJXWGZGD[this.y];
                    var6_8 = 0;
                    if (!var9_5) ** GOTO lbl66
                    do {
                        var7_9 = this.j[var6_8] = new RJXWGZGD();
                        var8_10 = var4_4.j[var6_8];
                        var7_9.a = var8_10.a;
                        var7_9.b = var8_10.b;
                        var7_9.c = var8_10.c;
                        var7_9.d = var8_10.d;
                        ++var6_8;
lbl66:
                        // 2 sources

                    } while (var6_8 < this.y);
                    this.gb = var4_4.gb;
                    if (!var9_5) break block17;
                }
                this.G = var4_4.G;
                this.H = var4_4.H;
                this.I = var4_4.I;
                this.J = var4_4.J;
            }
            this.z = var4_4.z;
            this.B = var4_4.B;
            this.M = var4_4.M;
            this.L = var4_4.L;
            this.K = var4_4.K;
            this.N = var4_4.N;
            this.D = var4_4.D;
            this.E = var4_4.E;
            this.F = var4_4.F;
            this.P = var4_4.P;
            this.Q = var4_4.Q;
            this.R = var4_4.R;
            this.k = var4_4.k;
            this.X = var4_4.X;
            if (var9_5) {
                throw new NullPointerException();
            }
            if (var2_2 >= 0) ** continue;
            this.W = var4_4.W;
            this.Z = var4_4.Z;
            this.Y = var4_4.Y;
            this.S = var4_4.S;
            this.U = var4_4.U;
            this.V = var4_4.V;
            this.T = var4_4.T;
            return;
        }
        catch (RuntimeException var5_7) {
            signlink.reporterror("16425, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_4 + ", " + var5_7.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public void a(int var1_1, ZKARKDQW var2_2, boolean var3_3) {
        var6_4 = XHHRODPC.l;
        try {
            block10: {
                block11: {
                    block9: {
                        block8: {
                            this.y = var2_2.y;
                            if (var1_1 == 7) break block8;
                            var4_5 = 1;
                            if (!var6_4) ** GOTO lbl9
                            do {
                                ++var4_5;
lbl9:
                                // 2 sources

                            } while (var4_5 > 0);
                        }
                        this.C = var2_2.C;
                        this.O = var2_2.O;
                        if (ZKARKDQW.u.length < this.y) {
                            ZKARKDQW.u = new int[this.y + 100];
                            ZKARKDQW.v = new int[this.y + 100];
                            ZKARKDQW.w = new int[this.y + 100];
                        }
                        this.z = ZKARKDQW.u;
                        this.A = ZKARKDQW.v;
                        this.B = ZKARKDQW.w;
                        var4_5 = 0;
                        if (!var6_4) ** GOTO lbl27
                        do {
                            this.z[var4_5] = var2_2.z[var4_5];
                            this.A[var4_5] = var2_2.A[var4_5];
                            this.B[var4_5] = var2_2.B[var4_5];
                            ++var4_5;
lbl27:
                            // 2 sources

                        } while (var4_5 < this.y);
                        if (!var3_3) break block9;
                        this.L = var2_2.L;
                        if (!var6_4) break block10;
                    }
                    if (ZKARKDQW.x.length < this.C) {
                        ZKARKDQW.x = new int[this.C + 100];
                    }
                    this.L = ZKARKDQW.x;
                    if (var2_2.L != null) break block11;
                    var5_7 = 0;
                    if (!var6_4) ** GOTO lbl41
                    do {
                        this.L[var5_7] = 0;
                        ++var5_7;
lbl41:
                        // 2 sources

                    } while (var5_7 < this.C);
                    break block10;
                }
                var5_8 = 0;
                if (!var6_4) ** GOTO lbl49
                do {
                    this.L[var5_8] = var2_2.L[var5_8];
                    ++var5_8;
lbl49:
                    // 2 sources

                } while (var5_8 < this.C);
            }
            this.J = var2_2.J;
            this.M = var2_2.M;
            this.K = var2_2.K;
            this.N = var2_2.N;
            this.eb = var2_2.eb;
            this.db = var2_2.db;
            this.D = var2_2.D;
            this.E = var2_2.E;
            this.F = var2_2.F;
            this.G = var2_2.G;
            this.H = var2_2.H;
            this.I = var2_2.I;
            this.P = var2_2.P;
            this.Q = var2_2.Q;
            this.R = var2_2.R;
            return;
        }
        catch (RuntimeException var4_6) {
            signlink.reporterror("18331, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_6.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private final int a(ZKARKDQW zKARKDQW, int n) {
        boolean bl = XHHRODPC.l;
        int n2 = -1;
        int n3 = zKARKDQW.z[n];
        int n4 = zKARKDQW.A[n];
        int n5 = zKARKDQW.B[n];
        int n6 = 0;
        boolean bl2 = true;
        do {
            if (bl2 && !(bl2 = false) && !bl) continue;
            if (n3 == this.z[n6] && n4 == this.A[n6] && n5 == this.B[n6]) {
                n2 = n6;
                if (!bl) break;
            }
            ++n6;
        } while (n6 < this.y);
        if (n2 != -1) return n2;
        this.z[this.y] = n3;
        this.A[this.y] = n4;
        this.B[this.y] = n5;
        if (zKARKDQW.bb == null) return this.y++;
        this.bb[this.y] = zKARKDQW.bb[n];
        return this.y++;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(boolean bl) {
        try {
            this.k = 0;
            this.W = 0;
            this.X = 0;
            int n = 0;
            boolean bl2 = true;
            do {
                int n2;
                if (bl2 && !(bl2 = false) && !XHHRODPC.l) continue;
                int n3 = this.z[n];
                int n4 = this.A[n];
                int n5 = this.B[n];
                if (-n4 > this.k) {
                    this.k = -n4;
                }
                if (n4 > this.X) {
                    this.X = n4;
                }
                if ((n2 = n3 * n3 + n5 * n5) > this.W) {
                    this.W = n2;
                }
                ++n;
            } while (n < this.y);
            if (bl) {
                r = 455;
            }
            this.W = (int)(Math.sqrt(this.W) + 0.99);
            this.Z = (int)(Math.sqrt(this.W * this.W + this.k * this.k) + 0.99);
            this.Y = this.Z + (int)(Math.sqrt(this.W * this.W + this.X * this.X) + 0.99);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("41353, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void b(boolean bl) {
        try {
            this.k = 0;
            this.X = 0;
            if (bl) {
                this.n = !this.n;
            }
            int n = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !XHHRODPC.l) continue;
                int n2 = this.A[n];
                if (-n2 > this.k) {
                    this.k = -n2;
                }
                if (n2 > this.X) {
                    this.X = n2;
                }
                ++n;
            } while (n < this.y);
            this.Z = (int)(Math.sqrt(this.W * this.W + this.k * this.k) + 0.99);
            this.Y = this.Z + (int)(Math.sqrt(this.W * this.W + this.X * this.X) + 0.99);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("87212, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void d(int n) {
        try {
            this.k = 0;
            this.W = 0;
            this.X = 0;
            this.S = 999999;
            this.T = -999999;
            this.U = -99999;
            this.V = 99999;
            int n2 = 0;
            boolean bl = true;
            do {
                int n3;
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                int n4 = this.z[n2];
                int n5 = this.A[n2];
                int n6 = this.B[n2];
                if (n4 < this.S) {
                    this.S = n4;
                }
                if (n4 > this.T) {
                    this.T = n4;
                }
                if (n6 < this.V) {
                    this.V = n6;
                }
                if (n6 > this.U) {
                    this.U = n6;
                }
                if (-n5 > this.k) {
                    this.k = -n5;
                }
                if (n5 > this.X) {
                    this.X = n5;
                }
                if ((n3 = n4 * n4 + n6 * n6) > this.W) {
                    this.W = n3;
                }
                ++n2;
            } while (n2 < this.y);
            this.W = (int)Math.sqrt(this.W);
            this.Z = (int)Math.sqrt(this.W * this.W + this.k * this.k);
            if (n != 21073) {
                return;
            }
            this.Y = this.Z + (int)Math.sqrt(this.W * this.W + this.X * this.X);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("2042, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(byte by) {
        boolean bl = XHHRODPC.l;
        try {
            int n;
            int n2;
            int n3;
            int n4;
            int n5;
            if (by != -71) {
                int n6 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && !bl) continue;
                    ++n6;
                } while (n6 > 0);
            }
            if (this.bb != null) {
                int[] nArray = new int[256];
                n5 = 0;
                n4 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    int n7 = n3 = this.bb[n4];
                    nArray[n7] = nArray[n7] + 1;
                    if (n3 > n5) {
                        n5 = n3;
                    }
                    ++n4;
                } while (n4 < this.y);
                this.db = new int[n5 + 1][];
                n3 = 0;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && !bl) continue;
                    this.db[n3] = new int[nArray[n3]];
                    nArray[n3] = 0;
                    ++n3;
                } while (n3 <= n5);
                n2 = 0;
                boolean bl5 = true;
                do {
                    if (bl5 && !(bl5 = false) && !bl) continue;
                    int n8 = n = this.bb[n2];
                    int n9 = nArray[n8];
                    nArray[n8] = n9 + 1;
                    this.db[n][n9] = n2++;
                } while (n2 < this.y);
                this.bb = null;
            }
            if (this.cb == null) return;
            int[] nArray = new int[256];
            n5 = 0;
            n4 = 0;
            boolean bl6 = true;
            do {
                if (bl6 && !(bl6 = false) && !bl) continue;
                int n10 = n3 = this.cb[n4];
                nArray[n10] = nArray[n10] + 1;
                if (n3 > n5) {
                    n5 = n3;
                }
                ++n4;
            } while (n4 < this.C);
            this.eb = new int[n5 + 1][];
            n3 = 0;
            boolean bl7 = true;
            do {
                if (bl7 && !(bl7 = false) && !bl) continue;
                this.eb[n3] = new int[nArray[n3]];
                nArray[n3] = 0;
                ++n3;
            } while (n3 <= n5);
            n2 = 0;
            boolean bl8 = true;
            do {
                if (bl8 && !(bl8 = false) && !bl) continue;
                int n11 = n = this.cb[n2];
                int n12 = nArray[n11];
                nArray[n11] = n12 + 1;
                this.eb[n][n12] = n2++;
            } while (n2 < this.C);
            this.cb = null;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("96395, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void c(int n, int n2) {
        try {
            if (this.db == null) {
                return;
            }
            if (n == -1) {
                return;
            }
            SQHJOGRT sQHJOGRT = SQHJOGRT.a(this.m, n);
            if (sQHJOGRT == null) {
                return;
            }
            KVCQPLIW kVCQPLIW = sQHJOGRT.d;
            if (n2 != 40542) {
                return;
            }
            Bb = 0;
            Cb = 0;
            Db = 0;
            int n3 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                int n4 = sQHJOGRT.f[n3];
                this.a(kVCQPLIW.c[n4], kVCQPLIW.d[n4], sQHJOGRT.g[n3], sQHJOGRT.h[n3], sQHJOGRT.i[n3]);
                ++n3;
            } while (n3 < sQHJOGRT.e);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("77052, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int[] nArray, int n2, int n3) {
        boolean bl = XHHRODPC.l;
        try {
            int n4;
            if (n3 == -1) {
                return;
            }
            if (nArray == null || n2 == -1) {
                this.c(n3, 40542);
                return;
            }
            SQHJOGRT sQHJOGRT = SQHJOGRT.a(this.m, n3);
            if (sQHJOGRT == null) {
                return;
            }
            SQHJOGRT sQHJOGRT2 = SQHJOGRT.a(this.m, n2);
            if (n != -20491) {
                return;
            }
            if (sQHJOGRT2 == null) {
                this.c(n3, 40542);
                return;
            }
            KVCQPLIW kVCQPLIW = sQHJOGRT.d;
            Bb = 0;
            Cb = 0;
            Db = 0;
            int n5 = 0;
            int n6 = nArray[n5++];
            int n7 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                n4 = sQHJOGRT.f[n7];
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    n6 = nArray[n5++];
                } while (n4 > n6);
                if (n4 != n6 || kVCQPLIW.c[n4] == 0) {
                    this.a(kVCQPLIW.c[n4], kVCQPLIW.d[n4], sQHJOGRT.g[n7], sQHJOGRT.h[n7], sQHJOGRT.i[n7]);
                }
                ++n7;
            } while (n7 < sQHJOGRT.e);
            Bb = 0;
            Cb = 0;
            Db = 0;
            n5 = 0;
            n6 = nArray[n5++];
            n4 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl) continue;
                int n8 = sQHJOGRT2.f[n4];
                boolean bl5 = true;
                do {
                    if (bl5 && !(bl5 = false) && !bl) continue;
                    n6 = nArray[n5++];
                } while (n8 > n6);
                if (n8 == n6 || kVCQPLIW.c[n8] == 0) {
                    this.a(kVCQPLIW.c[n8], kVCQPLIW.d[n8], sQHJOGRT2.g[n4], sQHJOGRT2.h[n4], sQHJOGRT2.i[n4]);
                }
                ++n4;
            } while (n4 < sQHJOGRT2.e);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("63853, " + n + ", " + nArray + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private void a(int var1_1, int[] var2_2, int var3_3, int var4_4, int var5_5) {
        block24: {
            block25: {
                block22: {
                    block20: {
                        block18: {
                            block16: {
                                var18_6 = XHHRODPC.l;
                                var6_7 = var2_2.length;
                                if (var1_1 != 0) break block16;
                                var7_8 = 0;
                                ZKARKDQW.Bb = 0;
                                ZKARKDQW.Cb = 0;
                                ZKARKDQW.Db = 0;
                                var8_13 = 0;
                                if (!var18_6) ** GOTO lbl25
                                do {
                                    block17: {
                                        if ((var9_18 = var2_2[var8_13]) >= this.db.length) break block17;
                                        var10_23 = this.db[var9_18];
                                        var11_28 = 0;
                                        if (!var18_6) ** GOTO lbl22
                                        do {
                                            var12_33 = var10_23[var11_28];
                                            ZKARKDQW.Bb += this.z[var12_33];
                                            ZKARKDQW.Cb += this.A[var12_33];
                                            ZKARKDQW.Db += this.B[var12_33];
                                            ++var7_8;
                                            ++var11_28;
lbl22:
                                            // 2 sources

                                        } while (var11_28 < var10_23.length);
                                    }
                                    ++var8_13;
lbl25:
                                    // 2 sources

                                } while (var8_13 < var6_7);
                                if (var7_8 > 0) {
                                    ZKARKDQW.Bb = ZKARKDQW.Bb / var7_8 + var3_3;
                                    ZKARKDQW.Cb = ZKARKDQW.Cb / var7_8 + var4_4;
                                    ZKARKDQW.Db = ZKARKDQW.Db / var7_8 + var5_5;
                                    return;
                                }
                                ZKARKDQW.Bb = var3_3;
                                ZKARKDQW.Cb = var4_4;
                                ZKARKDQW.Db = var5_5;
                                return;
                            }
                            if (var1_1 != 1) break block18;
                            var7_9 = 0;
                            if (!var18_6) ** GOTO lbl55
                            do {
                                block19: {
                                    if ((var8_14 = var2_2[var7_9]) >= this.db.length) break block19;
                                    var9_19 = this.db[var8_14];
                                    var10_24 = 0;
                                    if (!var18_6) ** GOTO lbl52
                                    do {
                                        v0 = var11_29 = var9_19[var10_24];
                                        this.z[v0] = this.z[v0] + var3_3;
                                        v1 = var11_29;
                                        this.A[v1] = this.A[v1] + var4_4;
                                        v2 = var11_29;
                                        this.B[v2] = this.B[v2] + var5_5;
                                        ++var10_24;
lbl52:
                                        // 2 sources

                                    } while (var10_24 < var9_19.length);
                                }
                                ++var7_9;
lbl55:
                                // 2 sources

                            } while (var7_9 < var6_7);
                            return;
                        }
                        if (var1_1 != 2) break block20;
                        var7_10 = 0;
                        if (!var18_6) ** GOTO lbl104
                        do {
                            block21: {
                                if ((var8_15 = var2_2[var7_10]) >= this.db.length) break block21;
                                var9_20 = this.db[var8_15];
                                var10_25 = 0;
                                if (!var18_6) ** GOTO lbl101
                                do {
                                    v3 = var11_30 = var9_20[var10_25];
                                    this.z[v3] = this.z[v3] - ZKARKDQW.Bb;
                                    v4 = var11_30;
                                    this.A[v4] = this.A[v4] - ZKARKDQW.Cb;
                                    v5 = var11_30;
                                    this.B[v5] = this.B[v5] - ZKARKDQW.Db;
                                    var12_34 = (var3_3 & 255) * 8;
                                    var13_35 = (var4_4 & 255) * 8;
                                    var14_36 = (var5_5 & 255) * 8;
                                    if (var14_36 != 0) {
                                        var15_37 = ZKARKDQW.Jb[var14_36];
                                        var16_38 = ZKARKDQW.Kb[var14_36];
                                        var17_39 = this.A[var11_30] * var15_37 + this.z[var11_30] * var16_38 >> 16;
                                        this.A[var11_30] = this.A[var11_30] * var16_38 - this.z[var11_30] * var15_37 >> 16;
                                        this.z[var11_30] = var17_39;
                                    }
                                    if (var12_34 != 0) {
                                        var15_37 = ZKARKDQW.Jb[var12_34];
                                        var16_38 = ZKARKDQW.Kb[var12_34];
                                        var17_39 = this.A[var11_30] * var16_38 - this.B[var11_30] * var15_37 >> 16;
                                        this.B[var11_30] = this.A[var11_30] * var15_37 + this.B[var11_30] * var16_38 >> 16;
                                        this.A[var11_30] = var17_39;
                                    }
                                    if (var13_35 != 0) {
                                        var15_37 = ZKARKDQW.Jb[var13_35];
                                        var16_38 = ZKARKDQW.Kb[var13_35];
                                        var17_39 = this.B[var11_30] * var15_37 + this.z[var11_30] * var16_38 >> 16;
                                        this.B[var11_30] = this.B[var11_30] * var16_38 - this.z[var11_30] * var15_37 >> 16;
                                        this.z[var11_30] = var17_39;
                                    }
                                    v6 = var11_30;
                                    this.z[v6] = this.z[v6] + ZKARKDQW.Bb;
                                    v7 = var11_30;
                                    this.A[v7] = this.A[v7] + ZKARKDQW.Cb;
                                    v8 = var11_30;
                                    this.B[v8] = this.B[v8] + ZKARKDQW.Db;
                                    ++var10_25;
lbl101:
                                    // 2 sources

                                } while (var10_25 < var9_20.length);
                            }
                            ++var7_10;
lbl104:
                            // 2 sources

                        } while (var7_10 < var6_7);
                        return;
                    }
                    if (var1_1 != 3) break block22;
                    var7_11 = 0;
                    if (!var18_6) ** GOTO lbl135
                    do {
                        block23: {
                            if ((var8_16 = var2_2[var7_11]) >= this.db.length) break block23;
                            var9_21 = this.db[var8_16];
                            var10_26 = 0;
                            if (!var18_6) ** GOTO lbl132
                            do {
                                v9 = var11_31 = var9_21[var10_26];
                                this.z[v9] = this.z[v9] - ZKARKDQW.Bb;
                                v10 = var11_31;
                                this.A[v10] = this.A[v10] - ZKARKDQW.Cb;
                                v11 = var11_31;
                                this.B[v11] = this.B[v11] - ZKARKDQW.Db;
                                this.z[var11_31] = this.z[var11_31] * var3_3 / 128;
                                this.A[var11_31] = this.A[var11_31] * var4_4 / 128;
                                this.B[var11_31] = this.B[var11_31] * var5_5 / 128;
                                v12 = var11_31;
                                this.z[v12] = this.z[v12] + ZKARKDQW.Bb;
                                v13 = var11_31;
                                this.A[v13] = this.A[v13] + ZKARKDQW.Cb;
                                v14 = var11_31;
                                this.B[v14] = this.B[v14] + ZKARKDQW.Db;
                                ++var10_26;
lbl132:
                                // 2 sources

                            } while (var10_26 < var9_21.length);
                        }
                        ++var7_11;
lbl135:
                        // 2 sources

                    } while (var7_11 < var6_7);
                    return;
                }
                if (var1_1 != 5) break block24;
                if (this.eb == null || this.L == null) break block25;
                var7_12 = 0;
                if (!var18_6) ** GOTO lbl158
                do {
                    block26: {
                        if ((var8_17 = var2_2[var7_12]) >= this.eb.length) break block26;
                        var9_22 = this.eb[var8_17];
                        var10_27 = 0;
                        if (!var18_6) ** GOTO lbl155
                        do {
                            v15 = var11_32 = var9_22[var10_27];
                            this.L[v15] = this.L[v15] + var3_3 * 8;
                            if (this.L[var11_32] < 0) {
                                this.L[var11_32] = 0;
                            }
                            if (this.L[var11_32] > 255) {
                                this.L[var11_32] = 255;
                            }
                            ++var10_27;
lbl155:
                            // 2 sources

                        } while (var10_27 < var9_22.length);
                    }
                    ++var7_12;
lbl158:
                    // 2 sources

                } while (var7_12 < var6_7);
            }
            return;
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void e(int n) {
        try {
            if (n <= 0) {
                return;
            }
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                int n3 = this.z[n2];
                this.z[n2] = this.B[n2];
                this.B[n2] = -n3;
                ++n2;
            } while (n2 < this.y);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("59385, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void d(int n, int n2) {
        try {
            int n3 = Jb[n];
            int n4 = Kb[n];
            int n5 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                int n6 = this.A[n5] * n4 - this.B[n5] * n3 >> 16;
                this.B[n5] = this.A[n5] * n3 + this.B[n5] * n4 >> 16;
                this.A[n5] = n6;
                ++n5;
            } while (n5 < this.y);
            if (n2 < this.p || n2 > this.p) {
                this.p = 324;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("13317, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2, int n3, int n4) {
        try {
            if (n3 != 16384) {
                this.m = -132;
            }
            int n5 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                int n6 = n5;
                this.z[n6] = this.z[n6] + n;
                int n7 = n5;
                this.A[n7] = this.A[n7] + n2;
                int n8 = n5++;
                this.B[n8] = this.B[n8] + n4;
            } while (n5 < this.y);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("1706, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public void e(int n, int n2) {
        int n3 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && !XHHRODPC.l) continue;
            if (this.M[n3] == n) {
                this.M[n3] = n2;
            }
            ++n3;
        } while (n3 < this.C);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void f(int n) {
        boolean bl = XHHRODPC.l;
        try {
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                this.B[n2] = -this.B[n2];
                ++n2;
            } while (n2 < this.y);
            int n3 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                int n4 = this.D[n3];
                this.D[n3] = this.F[n3];
                this.F[n3] = n4;
                ++n3;
            } while (n3 < this.C);
            if (n == 0) return;
            r = 107;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("2772, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void b(int n, int n2, int n3, int n4) {
        try {
            if (n3 != 9) {
                this.p = -383;
            }
            int n5 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                this.z[n5] = this.z[n5] * n / 128;
                this.A[n5] = this.A[n5] * n4 / 128;
                this.B[n5] = this.B[n5] * n2 / 128;
                ++n5;
            } while (n5 < this.y);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("64795, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public final void a(int n, int n2, int n3, int n4, int n5, boolean bl) {
        block17: {
            int n6;
            boolean bl2;
            block16: {
                int n7;
                bl2 = XHHRODPC.l;
                int n8 = (int)Math.sqrt(n3 * n3 + n4 * n4 + n5 * n5);
                int n9 = n2 * n8 >> 8;
                if (this.G == null) {
                    this.G = new int[this.C];
                    this.H = new int[this.C];
                    this.I = new int[this.C];
                }
                if (this.j == null) {
                    this.j = new RJXWGZGD[this.y];
                    n7 = 0;
                    boolean bl3 = true;
                    do {
                        if (bl3 && !(bl3 = false) && !bl2) continue;
                        this.j[n7] = new RJXWGZGD();
                        ++n7;
                    } while (n7 < this.y);
                }
                n7 = 0;
                boolean bl4 = true;
                do {
                    block15: {
                        int n10;
                        int n11;
                        int n12;
                        block14: {
                            if (bl4 && !(bl4 = false) && !bl2) continue;
                            n6 = this.D[n7];
                            int n13 = this.E[n7];
                            int n14 = this.F[n7];
                            int n15 = this.z[n13] - this.z[n6];
                            int n16 = this.A[n13] - this.A[n6];
                            int n17 = this.B[n13] - this.B[n6];
                            int n18 = this.z[n14] - this.z[n6];
                            int n19 = this.A[n14] - this.A[n6];
                            int n20 = this.B[n14] - this.B[n6];
                            n12 = n16 * n20 - n19 * n17;
                            n11 = n17 * n18 - n20 * n15;
                            n10 = n15 * n19 - n18 * n16;
                            boolean bl5 = true;
                            do {
                                if (bl5 && !(bl5 = false) && !bl2) continue;
                                n12 >>= 1;
                                n11 >>= 1;
                                n10 >>= 1;
                            } while (n12 > 8192 || n11 > 8192 || n10 > 8192 || n12 < -8192 || n11 < -8192 || n10 < -8192);
                            int n21 = (int)Math.sqrt(n12 * n12 + n11 * n11 + n10 * n10);
                            if (n21 <= 0) {
                                n21 = 1;
                            }
                            n12 = n12 * 256 / n21;
                            n11 = n11 * 256 / n21;
                            n10 = n10 * 256 / n21;
                            if (this.J != null && (this.J[n7] & 1) != 0) break block14;
                            RJXWGZGD rJXWGZGD = this.j[n6];
                            rJXWGZGD.a += n12;
                            rJXWGZGD.b += n11;
                            rJXWGZGD.c += n10;
                            ++rJXWGZGD.d;
                            rJXWGZGD = this.j[n13];
                            rJXWGZGD.a += n12;
                            rJXWGZGD.b += n11;
                            rJXWGZGD.c += n10;
                            ++rJXWGZGD.d;
                            rJXWGZGD = this.j[n14];
                            rJXWGZGD.a += n12;
                            rJXWGZGD.b += n11;
                            rJXWGZGD.c += n10;
                            ++rJXWGZGD.d;
                            if (!bl2) break block15;
                        }
                        int n22 = n + (n3 * n12 + n4 * n11 + n5 * n10) / (n9 + n9 / 2);
                        this.G[n7] = ZKARKDQW.a(this.M[n7], n22, this.J[n7]);
                    }
                    ++n7;
                } while (n7 < this.C);
                if (!bl) break block16;
                this.a(n, n9, n3, n4, n5);
                if (!bl2) break block17;
            }
            this.gb = new RJXWGZGD[this.y];
            n6 = 0;
            boolean bl6 = true;
            do {
                if (bl6 && !(bl6 = false) && !bl2) continue;
                RJXWGZGD rJXWGZGD = this.j[n6];
                RJXWGZGD rJXWGZGD2 = this.gb[n6] = new RJXWGZGD();
                rJXWGZGD2.a = rJXWGZGD.a;
                rJXWGZGD2.b = rJXWGZGD.b;
                rJXWGZGD2.c = rJXWGZGD.c;
                rJXWGZGD2.d = rJXWGZGD.d;
                ++n6;
            } while (n6 < this.y);
        }
        if (bl) {
            this.a(false);
            return;
        }
        this.d(21073);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public final void a(int n, int n2, int n3, int n4, int n5) {
        int n6;
        boolean bl = XHHRODPC.l;
        int n7 = 0;
        boolean bl2 = true;
        do {
            block10: {
                int n8;
                RJXWGZGD rJXWGZGD;
                int n9;
                int n10;
                int n11;
                block9: {
                    if (bl2 && !(bl2 = false) && !bl) continue;
                    n6 = this.D[n7];
                    n11 = this.E[n7];
                    n10 = this.F[n7];
                    if (this.J != null) break block9;
                    n9 = this.M[n7];
                    rJXWGZGD = this.j[n6];
                    n8 = n + (n3 * rJXWGZGD.a + n4 * rJXWGZGD.b + n5 * rJXWGZGD.c) / (n2 * rJXWGZGD.d);
                    this.G[n7] = ZKARKDQW.a(n9, n8, 0);
                    rJXWGZGD = this.j[n11];
                    n8 = n + (n3 * rJXWGZGD.a + n4 * rJXWGZGD.b + n5 * rJXWGZGD.c) / (n2 * rJXWGZGD.d);
                    this.H[n7] = ZKARKDQW.a(n9, n8, 0);
                    rJXWGZGD = this.j[n10];
                    n8 = n + (n3 * rJXWGZGD.a + n4 * rJXWGZGD.b + n5 * rJXWGZGD.c) / (n2 * rJXWGZGD.d);
                    this.I[n7] = ZKARKDQW.a(n9, n8, 0);
                    if (!bl) break block10;
                }
                if ((this.J[n7] & 1) == 0) {
                    n9 = this.M[n7];
                    int n12 = this.J[n7];
                    rJXWGZGD = this.j[n6];
                    n8 = n + (n3 * rJXWGZGD.a + n4 * rJXWGZGD.b + n5 * rJXWGZGD.c) / (n2 * rJXWGZGD.d);
                    this.G[n7] = ZKARKDQW.a(n9, n8, n12);
                    rJXWGZGD = this.j[n11];
                    n8 = n + (n3 * rJXWGZGD.a + n4 * rJXWGZGD.b + n5 * rJXWGZGD.c) / (n2 * rJXWGZGD.d);
                    this.H[n7] = ZKARKDQW.a(n9, n8, n12);
                    rJXWGZGD = this.j[n10];
                    n8 = n + (n3 * rJXWGZGD.a + n4 * rJXWGZGD.b + n5 * rJXWGZGD.c) / (n2 * rJXWGZGD.d);
                    this.I[n7] = ZKARKDQW.a(n9, n8, n12);
                }
            }
            ++n7;
        } while (n7 < this.C);
        this.j = null;
        this.gb = null;
        this.bb = null;
        this.cb = null;
        if (this.J != null) {
            n6 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                if ((this.J[n6] & 2) == 2) {
                    return;
                }
                ++n6;
            } while (n6 < this.C);
        }
        this.M = null;
    }

    public static final int a(int n, int n2, int n3) {
        block12: {
            block11: {
                boolean bl;
                block8: {
                    block10: {
                        block9: {
                            bl = XHHRODPC.l;
                            if ((n3 & 2) != 2) break block8;
                            if (n2 >= 0) break block9;
                            n2 = 0;
                            if (!bl) break block10;
                        }
                        if (n2 > 127) {
                            n2 = 127;
                        }
                    }
                    n2 = 127 - n2;
                    return n2;
                }
                if ((n2 = n2 * (n & 0x7F) >> 7) >= 2) break block11;
                n2 = 2;
                if (!bl) break block12;
            }
            if (n2 > 126) {
                n2 = 126;
            }
        }
        return (n & 0xFF80) + n2;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8 = OPPOFIOL.F;
        int n9 = OPPOFIOL.G;
        int n10 = Jb[n];
        int n11 = Kb[n];
        int n12 = Jb[n2];
        int n13 = Kb[n2];
        int n14 = Jb[n3];
        int n15 = Kb[n3];
        int n16 = Jb[n4];
        int n17 = Kb[n4];
        int n18 = n6 * n16 + n7 * n17 >> 16;
        int n19 = 0;
        boolean bl = true;
        do {
            int n20;
            if (bl && !(bl = false) && !XHHRODPC.l) continue;
            int n21 = this.z[n19];
            int n22 = this.A[n19];
            int n23 = this.B[n19];
            if (n3 != 0) {
                n20 = n22 * n14 + n21 * n15 >> 16;
                n22 = n22 * n15 - n21 * n14 >> 16;
                n21 = n20;
            }
            if (n != 0) {
                n20 = n22 * n11 - n23 * n10 >> 16;
                n23 = n22 * n10 + n23 * n11 >> 16;
                n22 = n20;
            }
            if (n2 != 0) {
                n20 = n23 * n12 + n21 * n13 >> 16;
                n23 = n23 * n13 - n21 * n12 >> 16;
                n21 = n20;
            }
            n20 = (n22 += n6) * n17 - (n23 += n7) * n16 >> 16;
            n23 = n22 * n16 + n23 * n17 >> 16;
            n22 = n20;
            ZKARKDQW.nb[n19] = n23 - n18;
            ZKARKDQW.lb[n19] = n8 + ((n21 += n5) << 9) / n23;
            ZKARKDQW.mb[n19] = n9 + (n22 << 9) / n23;
            if (this.O > 0) {
                ZKARKDQW.ob[n19] = n21;
                ZKARKDQW.pb[n19] = n22;
                ZKARKDQW.qb[n19] = n23;
            }
            ++n19;
        } while (n19 < this.y);
        try {
            this.a(false, false, 0);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        int n10;
        int n11;
        int n12;
        boolean bl;
        boolean bl2;
        int n13;
        boolean bl3;
        block25: {
            block30: {
                int n14;
                int n15;
                int n16;
                int n17;
                block29: {
                    int n18;
                    block28: {
                        int n19;
                        block27: {
                            block26: {
                                bl3 = XHHRODPC.l;
                                int n20 = n8 * n5 - n6 * n4 >> 16;
                                n13 = n7 * n2 + n20 * n3 >> 16;
                                int n21 = this.W * n3 >> 16;
                                n18 = n13 + n21;
                                if (n18 <= 50 || n13 >= 3500) {
                                    return;
                                }
                                int n22 = n8 * n4 + n6 * n5 >> 16;
                                n17 = n22 - this.W << 9;
                                if (n17 / n18 >= AFCKELYG.u) {
                                    return;
                                }
                                n16 = n22 + this.W << 9;
                                if (n16 / n18 <= -AFCKELYG.u) {
                                    return;
                                }
                                n19 = n7 * n3 - n20 * n2 >> 16;
                                int n23 = this.W * n2 >> 16;
                                n15 = n19 + n23 << 9;
                                if (n15 / n18 <= -AFCKELYG.v) {
                                    return;
                                }
                                int n24 = n23 + (this.k * n3 >> 16);
                                n14 = n19 - n24 << 9;
                                if (n14 / n18 >= AFCKELYG.v) {
                                    return;
                                }
                                int n25 = n21 + (this.k * n2 >> 16);
                                bl2 = false;
                                if (n13 - n25 <= 50) {
                                    bl2 = true;
                                }
                                bl = false;
                                if (n9 <= 0 || !Eb) break block25;
                                n12 = n13 - n21;
                                if (n12 <= 50) {
                                    n12 = 50;
                                }
                                if (n22 <= 0) break block26;
                                n17 /= n18;
                                n16 /= n12;
                                if (!bl3) break block27;
                            }
                            n16 /= n18;
                            n17 /= n12;
                        }
                        if (n19 <= 0) break block28;
                        n14 /= n18;
                        n15 /= n12;
                        if (!bl3) break block29;
                    }
                    n15 /= n18;
                    n14 /= n12;
                }
                n11 = Fb - OPPOFIOL.F;
                n10 = Gb - OPPOFIOL.G;
                if (n11 <= n17 || n11 >= n16 || n10 <= n14 || n10 >= n15) break block25;
                if (!this.fb) break block30;
                ZKARKDQW.Ib[ZKARKDQW.Hb++] = n9;
                if (!bl3) break block25;
            }
            bl = true;
        }
        n12 = OPPOFIOL.F;
        n11 = OPPOFIOL.G;
        n10 = 0;
        int n26 = 0;
        if (n != 0) {
            n10 = Jb[n];
            n26 = Kb[n];
        }
        int n27 = 0;
        boolean bl4 = true;
        do {
            int n28;
            int n29;
            int n30;
            block32: {
                block31: {
                    int n31;
                    if (bl4 && !(bl4 = false) && !bl3) continue;
                    n30 = this.z[n27];
                    n29 = this.A[n27];
                    n28 = this.B[n27];
                    if (n != 0) {
                        n31 = n28 * n10 + n30 * n26 >> 16;
                        n28 = n28 * n26 - n30 * n10 >> 16;
                        n30 = n31;
                    }
                    n31 = (n28 += n8) * n4 + (n30 += n6) * n5 >> 16;
                    n28 = n28 * n5 - n30 * n4 >> 16;
                    n30 = n31;
                    n31 = (n29 += n7) * n3 - n28 * n2 >> 16;
                    n28 = n29 * n2 + n28 * n3 >> 16;
                    n29 = n31;
                    ZKARKDQW.nb[n27] = n28 - n13;
                    if (n28 < 50) break block31;
                    ZKARKDQW.lb[n27] = n12 + (n30 << 9) / n28;
                    ZKARKDQW.mb[n27] = n11 + (n29 << 9) / n28;
                    if (!bl3) break block32;
                }
                ZKARKDQW.lb[n27] = -5000;
                bl2 = true;
            }
            if (bl2 || this.O > 0) {
                ZKARKDQW.ob[n27] = n30;
                ZKARKDQW.pb[n27] = n29;
                ZKARKDQW.qb[n27] = n28;
            }
            ++n27;
        } while (n27 < this.y);
        try {
            this.a(bl2, bl, n9);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    /*
     * Unable to fully structure code
     */
    private final void a(boolean var1_1, boolean var2_2, int var3_3) {
        block50: {
            block49: {
                block43: {
                    var20_4 = XHHRODPC.l;
                    var4_5 = 0;
                    if (!var20_4) ** GOTO lbl7
                    do {
                        ZKARKDQW.rb[var4_5] = 0;
                        ++var4_5;
lbl7:
                        // 2 sources

                    } while (var4_5 < this.Y);
                    var5_6 = 0;
                    if (!var20_4) ** GOTO lbl43
                    do {
                        block39: {
                            block42: {
                                block41: {
                                    block40: {
                                        if (this.J != null && this.J[var5_6] == -1) break block39;
                                        var6_7 = this.D[var5_6];
                                        var7_8 = this.E[var5_6];
                                        var8_9 = this.F[var5_6];
                                        var9_11 = ZKARKDQW.lb[var6_7];
                                        var10_13 = ZKARKDQW.lb[var7_8];
                                        var11_14 = ZKARKDQW.lb[var8_9];
                                        if (!var1_1 || var9_11 != -5000 && var10_13 != -5000 && var11_14 != -5000) break block40;
                                        ZKARKDQW.kb[var5_6] = true;
                                        v0 = var12_15 = (ZKARKDQW.nb[var6_7] + ZKARKDQW.nb[var7_8] + ZKARKDQW.nb[var8_9]) / 3 + this.Z;
                                        v1 = ZKARKDQW.rb[v0];
                                        ZKARKDQW.rb[v0] = v1 + 1;
                                        ZKARKDQW.sb[var12_15][v1] = var5_6;
                                        if (!var20_4) break block39;
                                    }
                                    if (var2_2 && this.a(ZKARKDQW.Fb, ZKARKDQW.Gb, ZKARKDQW.mb[var6_7], ZKARKDQW.mb[var7_8], ZKARKDQW.mb[var8_9], var9_11, var10_13, var11_14)) {
                                        ZKARKDQW.Ib[ZKARKDQW.Hb++] = var3_3;
                                        var2_2 = false;
                                    }
                                    if ((var9_11 - var10_13) * (ZKARKDQW.mb[var8_9] - ZKARKDQW.mb[var7_8]) - (ZKARKDQW.mb[var6_7] - ZKARKDQW.mb[var7_8]) * (var11_14 - var10_13) <= 0) break block39;
                                    ZKARKDQW.kb[var5_6] = false;
                                    if (var9_11 >= 0 && var10_13 >= 0 && var11_14 >= 0 && var9_11 <= AFCKELYG.t && var10_13 <= AFCKELYG.t && var11_14 <= AFCKELYG.t) break block41;
                                    ZKARKDQW.jb[var5_6] = true;
                                    if (!var20_4) break block42;
                                }
                                ZKARKDQW.jb[var5_6] = false;
                            }
                            v2 = var12_15 = (ZKARKDQW.nb[var6_7] + ZKARKDQW.nb[var7_8] + ZKARKDQW.nb[var8_9]) / 3 + this.Z;
                            v3 = ZKARKDQW.rb[v2];
                            ZKARKDQW.rb[v2] = v3 + 1;
                            ZKARKDQW.sb[var12_15][v3] = var5_6;
                        }
                        ++var5_6;
lbl43:
                        // 2 sources

                    } while (var5_6 < this.C);
                    if (this.K != null) break block43;
                    var6_7 = this.Y - 1;
                    if (!var20_4) ** GOTO lbl58
                    do {
                        block44: {
                            if ((var7_8 = ZKARKDQW.rb[var6_7]) <= 0) break block44;
                            var8_10 = ZKARKDQW.sb[var6_7];
                            var9_11 = 0;
                            if (!var20_4) ** GOTO lbl55
                            do {
                                this.g(var8_10[var9_11]);
                                ++var9_11;
lbl55:
                                // 2 sources

                            } while (var9_11 < var7_8);
                        }
                        --var6_7;
lbl58:
                        // 2 sources

                    } while (var6_7 >= 0);
                    return;
                }
                var6_7 = 0;
                if (!var20_4) ** GOTO lbl67
                do {
                    ZKARKDQW.tb[var6_7] = 0;
                    ZKARKDQW.xb[var6_7] = 0;
                    ++var6_7;
lbl67:
                    // 2 sources

                } while (var6_7 < 12);
                var7_8 = this.Y - 1;
                if (!var20_4) ** GOTO lbl95
                do {
                    block45: {
                        if ((var8_9 = ZKARKDQW.rb[var7_8]) <= 0) break block45;
                        var9_12 = ZKARKDQW.sb[var7_8];
                        var10_13 = 0;
                        if (!var20_4) ** GOTO lbl92
                        do {
                            block47: {
                                block48: {
                                    block46: {
                                        var11_14 = var9_12[var10_13];
                                        v4 = var12_15 = this.K[var11_14];
                                        ZKARKDQW.tb[v4] = ZKARKDQW.tb[v4] + 1;
                                        ZKARKDQW.ub[var12_15][var13_16] = var11_14;
                                        if (var12_15 >= 10) break block46;
                                        v5 = var12_15;
                                        ZKARKDQW.xb[v5] = ZKARKDQW.xb[v5] + var7_8;
                                        if (!var20_4) break block47;
                                    }
                                    if (var12_15 != 10) break block48;
                                    ZKARKDQW.vb[var13_16] = var7_8;
                                    if (!var20_4) break block47;
                                }
                                ZKARKDQW.wb[var13_16] = var7_8;
                            }
                            ++var10_13;
lbl92:
                            // 2 sources

                        } while (var10_13 < var8_9);
                    }
                    --var7_8;
lbl95:
                    // 2 sources

                } while (var7_8 >= 0);
                var8_9 = 0;
                if (ZKARKDQW.tb[1] > 0 || ZKARKDQW.tb[2] > 0) {
                    var8_9 = (ZKARKDQW.xb[1] + ZKARKDQW.xb[2]) / (ZKARKDQW.tb[1] + ZKARKDQW.tb[2]);
                }
                var9_11 = 0;
                if (ZKARKDQW.tb[3] > 0 || ZKARKDQW.tb[4] > 0) {
                    var9_11 = (ZKARKDQW.xb[3] + ZKARKDQW.xb[4]) / (ZKARKDQW.tb[3] + ZKARKDQW.tb[4]);
                }
                var10_13 = 0;
                if (ZKARKDQW.tb[6] > 0 || ZKARKDQW.tb[8] > 0) {
                    var10_13 = (ZKARKDQW.xb[6] + ZKARKDQW.xb[8]) / (ZKARKDQW.tb[6] + ZKARKDQW.tb[8]);
                }
                var12_15 = 0;
                var13_16 = ZKARKDQW.tb[10];
                var14_17 = ZKARKDQW.ub[10];
                var15_18 = ZKARKDQW.vb;
                if (var12_15 == var13_16) {
                    var12_15 = 0;
                    var13_16 = ZKARKDQW.tb[11];
                    var14_17 = ZKARKDQW.ub[11];
                    var15_18 = ZKARKDQW.wb;
                }
                if (var12_15 >= var13_16) break block49;
                var11_14 = var15_18[var12_15];
                if (!var20_4) break block50;
            }
            var11_14 = -1000;
        }
        var16_19 = 0;
        if (!var20_4) ** GOTO lbl176
        block7: while (true) {
            block52: {
                block51: {
                    this.g(var14_17[var12_15++]);
                    if (var12_15 == var13_16 && var14_17 != ZKARKDQW.ub[11]) {
                        var12_15 = 0;
                        var13_16 = ZKARKDQW.tb[11];
                        var14_17 = ZKARKDQW.ub[11];
                        var15_18 = ZKARKDQW.wb;
                    }
                    if (var12_15 >= var13_16) break block51;
                    var11_14 = var15_18[var12_15];
                    if (!var20_4) break block52;
                }
                var11_14 = -1000;
            }
            do {
                if (var16_19 != 0) ** GOTO lbl151
                if (var11_14 > var8_9) continue block7;
                if (!var20_4) ** GOTO lbl151
                do {
                    block54: {
                        block53: {
                            this.g(var14_17[var12_15++]);
                            if (var12_15 == var13_16 && var14_17 != ZKARKDQW.ub[11]) {
                                var12_15 = 0;
                                var13_16 = ZKARKDQW.tb[11];
                                var14_17 = ZKARKDQW.ub[11];
                                var15_18 = ZKARKDQW.wb;
                            }
                            if (var12_15 >= var13_16) break block53;
                            var11_14 = var15_18[var12_15];
                            if (!var20_4) break block54;
                        }
                        var11_14 = -1000;
                    }
                    if (var16_19 != 3) ** GOTO lbl166
                } while (var11_14 > var9_11);
                if (!var20_4) ** GOTO lbl166
                do {
                    this.g(var14_17[var12_15++]);
                    if (var12_15 == var13_16 && var14_17 != ZKARKDQW.ub[11]) {
                        var12_15 = 0;
                        var13_16 = ZKARKDQW.tb[11];
                        var14_17 = ZKARKDQW.ub[11];
                        var15_18 = ZKARKDQW.wb;
                    }
                    if (var12_15 < var13_16) {
                        var11_14 = var15_18[var12_15];
                        if (!var20_4) continue;
                    }
                    var11_14 = -1000;
lbl166:
                    // 4 sources

                } while (var16_19 == 5 && var11_14 > var10_13);
                var17_20 = ZKARKDQW.tb[var16_19];
                var18_21 = ZKARKDQW.ub[var16_19];
                var19_22 = 0;
                if (!var20_4) ** GOTO lbl174
                do {
                    this.g(var18_21[var19_22]);
                    ++var19_22;
lbl174:
                    // 2 sources

                } while (var19_22 < var17_20);
                ++var16_19;
lbl176:
                // 2 sources

            } while (var16_19 < 10);
            break;
        }
        if (!var20_4) ** GOTO lbl189
        do {
            this.g(var14_17[var12_15++]);
            if (var12_15 == var13_16 && var14_17 != ZKARKDQW.ub[11]) {
                var12_15 = 0;
                var14_17 = ZKARKDQW.ub[11];
                var13_16 = ZKARKDQW.tb[11];
                var15_18 = ZKARKDQW.wb;
            }
            if (var12_15 < var13_16) {
                var11_14 = var15_18[var12_15];
                if (!var20_4) continue;
            }
            var11_14 = -1000;
lbl189:
            // 3 sources

        } while (var11_14 != -1000);
    }

    private final void g(int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        block14: {
            block13: {
                boolean bl;
                block12: {
                    block11: {
                        bl = XHHRODPC.l;
                        if (kb[n]) {
                            this.h(n);
                            return;
                        }
                        n5 = this.D[n];
                        n4 = this.E[n];
                        n3 = this.F[n];
                        OPPOFIOL.B = jb[n];
                        if (this.L != null) break block11;
                        OPPOFIOL.E = 0;
                        if (!bl) break block12;
                    }
                    OPPOFIOL.E = this.L[n];
                }
                if (this.J != null) break block13;
                n2 = 0;
                if (!bl) break block14;
            }
            n2 = this.J[n] & 3;
        }
        if (n2 == 0) {
            OPPOFIOL.a(mb[n5], mb[n4], mb[n3], lb[n5], lb[n4], lb[n3], this.G[n], this.H[n], this.I[n]);
            return;
        }
        if (n2 == 1) {
            OPPOFIOL.c(mb[n5], mb[n4], mb[n3], lb[n5], lb[n4], lb[n3], Lb[this.G[n]]);
            return;
        }
        if (n2 == 2) {
            int n6 = this.J[n] >> 2;
            int n7 = this.P[n6];
            int n8 = this.Q[n6];
            int n9 = this.R[n6];
            OPPOFIOL.a(mb[n5], mb[n4], mb[n3], lb[n5], lb[n4], lb[n3], this.G[n], this.H[n], this.I[n], ob[n7], ob[n8], ob[n9], pb[n7], pb[n8], pb[n9], qb[n7], qb[n8], qb[n9], this.M[n]);
            return;
        }
        if (n2 == 3) {
            int n10 = this.J[n] >> 2;
            int n11 = this.P[n10];
            int n12 = this.Q[n10];
            int n13 = this.R[n10];
            OPPOFIOL.a(mb[n5], mb[n4], mb[n3], lb[n5], lb[n4], lb[n3], this.G[n], this.G[n], this.G[n], ob[n11], ob[n12], ob[n13], pb[n11], pb[n12], pb[n13], qb[n11], qb[n12], qb[n13], this.M[n]);
        }
    }

    private final void h(int n) {
        block37: {
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            int n10;
            int n11;
            int n12;
            block45: {
                block44: {
                    int n13;
                    boolean bl;
                    block38: {
                        block43: {
                            block42: {
                                block41: {
                                    block40: {
                                        block39: {
                                            block36: {
                                                int n14;
                                                int n15;
                                                int n16;
                                                int n17;
                                                int n18;
                                                int n19;
                                                int n20;
                                                int n21;
                                                block35: {
                                                    block34: {
                                                        block33: {
                                                            block32: {
                                                                block31: {
                                                                    bl = XHHRODPC.l;
                                                                    n21 = OPPOFIOL.F;
                                                                    n20 = OPPOFIOL.G;
                                                                    n13 = 0;
                                                                    n19 = this.D[n];
                                                                    n18 = this.E[n];
                                                                    n17 = this.F[n];
                                                                    n16 = qb[n19];
                                                                    n15 = qb[n18];
                                                                    n14 = qb[n17];
                                                                    if (n16 < 50) break block31;
                                                                    ZKARKDQW.yb[n13] = lb[n19];
                                                                    ZKARKDQW.zb[n13] = mb[n19];
                                                                    ZKARKDQW.Ab[n13++] = this.G[n];
                                                                    if (!bl) break block32;
                                                                }
                                                                n12 = ob[n19];
                                                                n11 = pb[n19];
                                                                n10 = this.G[n];
                                                                if (n14 >= 50) {
                                                                    n9 = (50 - n16) * Mb[n14 - n16];
                                                                    ZKARKDQW.yb[n13] = n21 + (n12 + ((ob[n17] - n12) * n9 >> 16) << 9) / 50;
                                                                    ZKARKDQW.zb[n13] = n20 + (n11 + ((pb[n17] - n11) * n9 >> 16) << 9) / 50;
                                                                    ZKARKDQW.Ab[n13++] = n10 + ((this.I[n] - n10) * n9 >> 16);
                                                                }
                                                                if (n15 >= 50) {
                                                                    n9 = (50 - n16) * Mb[n15 - n16];
                                                                    ZKARKDQW.yb[n13] = n21 + (n12 + ((ob[n18] - n12) * n9 >> 16) << 9) / 50;
                                                                    ZKARKDQW.zb[n13] = n20 + (n11 + ((pb[n18] - n11) * n9 >> 16) << 9) / 50;
                                                                    ZKARKDQW.Ab[n13++] = n10 + ((this.H[n] - n10) * n9 >> 16);
                                                                }
                                                            }
                                                            if (n15 < 50) break block33;
                                                            ZKARKDQW.yb[n13] = lb[n18];
                                                            ZKARKDQW.zb[n13] = mb[n18];
                                                            ZKARKDQW.Ab[n13++] = this.H[n];
                                                            if (!bl) break block34;
                                                        }
                                                        n12 = ob[n18];
                                                        n11 = pb[n18];
                                                        n10 = this.H[n];
                                                        if (n16 >= 50) {
                                                            n9 = (50 - n15) * Mb[n16 - n15];
                                                            ZKARKDQW.yb[n13] = n21 + (n12 + ((ob[n19] - n12) * n9 >> 16) << 9) / 50;
                                                            ZKARKDQW.zb[n13] = n20 + (n11 + ((pb[n19] - n11) * n9 >> 16) << 9) / 50;
                                                            ZKARKDQW.Ab[n13++] = n10 + ((this.G[n] - n10) * n9 >> 16);
                                                        }
                                                        if (n14 >= 50) {
                                                            n9 = (50 - n15) * Mb[n14 - n15];
                                                            ZKARKDQW.yb[n13] = n21 + (n12 + ((ob[n17] - n12) * n9 >> 16) << 9) / 50;
                                                            ZKARKDQW.zb[n13] = n20 + (n11 + ((pb[n17] - n11) * n9 >> 16) << 9) / 50;
                                                            ZKARKDQW.Ab[n13++] = n10 + ((this.I[n] - n10) * n9 >> 16);
                                                        }
                                                    }
                                                    if (n14 < 50) break block35;
                                                    ZKARKDQW.yb[n13] = lb[n17];
                                                    ZKARKDQW.zb[n13] = mb[n17];
                                                    ZKARKDQW.Ab[n13++] = this.I[n];
                                                    if (!bl) break block36;
                                                }
                                                n12 = ob[n17];
                                                n11 = pb[n17];
                                                n10 = this.I[n];
                                                if (n15 >= 50) {
                                                    n9 = (50 - n14) * Mb[n15 - n14];
                                                    ZKARKDQW.yb[n13] = n21 + (n12 + ((ob[n18] - n12) * n9 >> 16) << 9) / 50;
                                                    ZKARKDQW.zb[n13] = n20 + (n11 + ((pb[n18] - n11) * n9 >> 16) << 9) / 50;
                                                    ZKARKDQW.Ab[n13++] = n10 + ((this.H[n] - n10) * n9 >> 16);
                                                }
                                                if (n16 >= 50) {
                                                    n9 = (50 - n14) * Mb[n16 - n14];
                                                    ZKARKDQW.yb[n13] = n21 + (n12 + ((ob[n19] - n12) * n9 >> 16) << 9) / 50;
                                                    ZKARKDQW.zb[n13] = n20 + (n11 + ((pb[n19] - n11) * n9 >> 16) << 9) / 50;
                                                    ZKARKDQW.Ab[n13++] = n10 + ((this.G[n] - n10) * n9 >> 16);
                                                }
                                            }
                                            if (((n12 = yb[0]) - (n11 = yb[1])) * ((n8 = zb[2]) - (n7 = zb[1])) - ((n9 = zb[0]) - n7) * ((n10 = yb[2]) - n11) <= 0) break block37;
                                            OPPOFIOL.B = false;
                                            if (n13 != 3) break block38;
                                            if (n12 < 0 || n11 < 0 || n10 < 0 || n12 > AFCKELYG.t || n11 > AFCKELYG.t || n10 > AFCKELYG.t) {
                                                OPPOFIOL.B = true;
                                            }
                                            if (this.J != null) break block39;
                                            n6 = 0;
                                            if (!bl) break block40;
                                        }
                                        n6 = this.J[n] & 3;
                                    }
                                    if (n6 != 0) break block41;
                                    OPPOFIOL.a(n9, n7, n8, n12, n11, n10, Ab[0], Ab[1], Ab[2]);
                                    if (!bl) break block38;
                                }
                                if (n6 != 1) break block42;
                                OPPOFIOL.c(n9, n7, n8, n12, n11, n10, Lb[this.G[n]]);
                                if (!bl) break block38;
                            }
                            if (n6 != 2) break block43;
                            n5 = this.J[n] >> 2;
                            n4 = this.P[n5];
                            n3 = this.Q[n5];
                            n2 = this.R[n5];
                            OPPOFIOL.a(n9, n7, n8, n12, n11, n10, Ab[0], Ab[1], Ab[2], ob[n4], ob[n3], ob[n2], pb[n4], pb[n3], pb[n2], qb[n4], qb[n3], qb[n2], this.M[n]);
                            if (!bl) break block38;
                        }
                        if (n6 == 3) {
                            n5 = this.J[n] >> 2;
                            n4 = this.P[n5];
                            n3 = this.Q[n5];
                            n2 = this.R[n5];
                            OPPOFIOL.a(n9, n7, n8, n12, n11, n10, this.G[n], this.G[n], this.G[n], ob[n4], ob[n3], ob[n2], pb[n4], pb[n3], pb[n2], qb[n4], qb[n3], qb[n2], this.M[n]);
                        }
                    }
                    if (n13 != 4) break block37;
                    if (n12 < 0 || n11 < 0 || n10 < 0 || n12 > AFCKELYG.t || n11 > AFCKELYG.t || n10 > AFCKELYG.t || yb[3] < 0 || yb[3] > AFCKELYG.t) {
                        OPPOFIOL.B = true;
                    }
                    if (this.J != null) break block44;
                    n6 = 0;
                    if (!bl) break block45;
                }
                n6 = this.J[n] & 3;
            }
            if (n6 == 0) {
                OPPOFIOL.a(n9, n7, n8, n12, n11, n10, Ab[0], Ab[1], Ab[2]);
                OPPOFIOL.a(n9, n8, zb[3], n12, n10, yb[3], Ab[0], Ab[2], Ab[3]);
                return;
            }
            if (n6 == 1) {
                n5 = Lb[this.G[n]];
                OPPOFIOL.c(n9, n7, n8, n12, n11, n10, n5);
                OPPOFIOL.c(n9, n8, zb[3], n12, n10, yb[3], n5);
                return;
            }
            if (n6 == 2) {
                n5 = this.J[n] >> 2;
                n4 = this.P[n5];
                n3 = this.Q[n5];
                n2 = this.R[n5];
                OPPOFIOL.a(n9, n7, n8, n12, n11, n10, Ab[0], Ab[1], Ab[2], ob[n4], ob[n3], ob[n2], pb[n4], pb[n3], pb[n2], qb[n4], qb[n3], qb[n2], this.M[n]);
                OPPOFIOL.a(n9, n8, zb[3], n12, n10, yb[3], Ab[0], Ab[2], Ab[3], ob[n4], ob[n3], ob[n2], pb[n4], pb[n3], pb[n2], qb[n4], qb[n3], qb[n2], this.M[n]);
                return;
            }
            if (n6 == 3) {
                n5 = this.J[n] >> 2;
                n4 = this.P[n5];
                n3 = this.Q[n5];
                n2 = this.R[n5];
                OPPOFIOL.a(n9, n7, n8, n12, n11, n10, this.G[n], this.G[n], this.G[n], ob[n4], ob[n3], ob[n2], pb[n4], pb[n3], pb[n2], qb[n4], qb[n3], qb[n2], this.M[n]);
                OPPOFIOL.a(n9, n8, zb[3], n12, n10, yb[3], this.G[n], this.G[n], this.G[n], ob[n4], ob[n3], ob[n2], pb[n4], pb[n3], pb[n2], qb[n4], qb[n3], qb[n2], this.M[n]);
            }
        }
    }

    private final boolean a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (n2 < n3 && n2 < n4 && n2 < n5) {
            return false;
        }
        if (n2 > n3 && n2 > n4 && n2 > n5) {
            return false;
        }
        if (n < n6 && n < n7 && n < n8) {
            return false;
        }
        return n <= n6 || n <= n7 || n <= n8;
    }

    static {
        t = new ZKARKDQW(true);
        u = new int[2000];
        v = new int[2000];
        w = new int[2000];
        x = new int[2000];
        jb = new boolean[4096];
        kb = new boolean[4096];
        lb = new int[4096];
        mb = new int[4096];
        nb = new int[4096];
        ob = new int[4096];
        pb = new int[4096];
        qb = new int[4096];
        rb = new int[1500];
        sb = new int[1500][512];
        tb = new int[12];
        ub = new int[12][2000];
        vb = new int[2000];
        wb = new int[2000];
        xb = new int[12];
        yb = new int[10];
        zb = new int[10];
        Ab = new int[10];
        Ib = new int[1000];
        Jb = OPPOFIOL.J;
        Kb = OPPOFIOL.K;
        Lb = OPPOFIOL.V;
        Mb = OPPOFIOL.I;
    }
}

