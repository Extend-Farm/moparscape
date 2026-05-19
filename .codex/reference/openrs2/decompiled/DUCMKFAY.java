/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class DUCMKFAY {
    public CXGZMTJK a;
    public int b;
    public CXGZMTJK[] c;
    public static DUCMKFAY[] d;
    public int e;
    public int[] f;
    private int g = 9;
    public int h;
    public int[] i;
    public int j;
    public int k;
    public String l;
    public int m;
    public int n;
    public String o;
    public String p;
    public boolean q;
    public int r;
    public String[] s;
    public int[][] t;
    public boolean u;
    public String v;
    private int w = 891;
    public int x;
    public int y;
    public int z;
    public int A;
    public int B;
    public boolean C;
    public int D;
    public int E;
    private static GCPOSBWX F;
    public int G;
    public int[] H;
    public int[] I;
    public boolean J;
    public YXVQXWYR K;
    public int L;
    public int[] M;
    public int N;
    public int[] O;
    public String P;
    public boolean Q;
    public int R;
    public boolean S;
    public int[] T;
    public int[] U;
    public byte V;
    public int W;
    public int X;
    public int Y;
    public int Z;
    public boolean ab;
    public CXGZMTJK bb;
    public int cb;
    public int db;
    public int eb;
    static GCPOSBWX fb;
    public int gb;
    public boolean hb;
    public int ib;
    public boolean jb;
    public int kb;
    public int lb;
    public int mb;
    public int[] nb;

    public void a(int n, byte by, int n2) {
        try {
            int n3;
            block5: {
                block4: {
                    n3 = this.U[n];
                    this.U[n] = this.U[n2];
                    if (by != 9) break block4;
                    by = 0;
                    if (client.Jj == 0) break block5;
                }
                this.w = -76;
            }
            this.U[n2] = n3;
            n3 = this.T[n];
            this.T[n] = this.T[n2];
            this.T[n2] = n3;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("58455, " + n + ", " + by + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(XTGLDHGX xTGLDHGX, YXVQXWYR[] yXVQXWYRArray, byte by, XTGLDHGX xTGLDHGX2) {
        int n = client.Jj;
        try {
            F = new GCPOSBWX(false, 50000);
            MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("data", null), 891);
            int n2 = -1;
            int n3 = mBMGIXGO.e();
            d = new DUCMKFAY[n3];
            boolean bl = true;
            do {
                int n4;
                DUCMKFAY dUCMKFAY;
                block51: {
                    block55: {
                        block54: {
                            int n5;
                            block53: {
                                block52: {
                                    int n6;
                                    int n7;
                                    int n8;
                                    block50: {
                                        block49: {
                                            if (bl && !(bl = false) && n == 0) continue;
                                            n5 = mBMGIXGO.e();
                                            if (n5 == 65535) {
                                                n2 = mBMGIXGO.e();
                                                n5 = mBMGIXGO.e();
                                            }
                                            dUCMKFAY = DUCMKFAY.d[n5] = new DUCMKFAY();
                                            dUCMKFAY.R = n5;
                                            dUCMKFAY.D = n2;
                                            dUCMKFAY.db = mBMGIXGO.c();
                                            dUCMKFAY.k = mBMGIXGO.c();
                                            dUCMKFAY.h = mBMGIXGO.e();
                                            dUCMKFAY.n = mBMGIXGO.e();
                                            dUCMKFAY.ib = mBMGIXGO.e();
                                            dUCMKFAY.V = (byte)mBMGIXGO.c();
                                            dUCMKFAY.x = mBMGIXGO.c();
                                            if (dUCMKFAY.x == 0) break block49;
                                            dUCMKFAY.x = (dUCMKFAY.x - 1 << 8) + mBMGIXGO.c();
                                            if (n == 0) break block50;
                                        }
                                        dUCMKFAY.x = -1;
                                    }
                                    if ((n8 = mBMGIXGO.c()) > 0) {
                                        dUCMKFAY.M = new int[n8];
                                        dUCMKFAY.f = new int[n8];
                                        n7 = 0;
                                        boolean bl2 = true;
                                        do {
                                            if (bl2 && !(bl2 = false) && n == 0) continue;
                                            dUCMKFAY.M[n7] = mBMGIXGO.c();
                                            dUCMKFAY.f[n7] = mBMGIXGO.e();
                                            ++n7;
                                        } while (n7 < n8);
                                    }
                                    if ((n7 = mBMGIXGO.c()) > 0) {
                                        dUCMKFAY.t = new int[n7][];
                                        n6 = 0;
                                        boolean bl3 = true;
                                        do {
                                            if (bl3 && !(bl3 = false) && n == 0) continue;
                                            n4 = mBMGIXGO.e();
                                            dUCMKFAY.t[n6] = new int[n4];
                                            int n9 = 0;
                                            boolean bl4 = true;
                                            do {
                                                if (bl4 && !(bl4 = false) && n == 0) continue;
                                                dUCMKFAY.t[n6][n9] = mBMGIXGO.e();
                                                ++n9;
                                            } while (n9 < n4);
                                            ++n6;
                                        } while (n6 < n7);
                                    }
                                    if (dUCMKFAY.db == 0) {
                                        dUCMKFAY.cb = mBMGIXGO.e();
                                        dUCMKFAY.hb = mBMGIXGO.c() == 1;
                                        n6 = mBMGIXGO.e();
                                        dUCMKFAY.H = new int[n6];
                                        dUCMKFAY.I = new int[n6];
                                        dUCMKFAY.nb = new int[n6];
                                        n4 = 0;
                                        boolean bl5 = true;
                                        do {
                                            if (bl5 && !(bl5 = false) && n == 0) continue;
                                            dUCMKFAY.H[n4] = mBMGIXGO.e();
                                            dUCMKFAY.I[n4] = mBMGIXGO.f();
                                            dUCMKFAY.nb[n4] = mBMGIXGO.f();
                                            ++n4;
                                        } while (n4 < n6);
                                    }
                                    if (dUCMKFAY.db == 1) {
                                        dUCMKFAY.e = mBMGIXGO.e();
                                        boolean bl6 = dUCMKFAY.S = mBMGIXGO.c() == 1;
                                    }
                                    if (dUCMKFAY.db == 2) {
                                        dUCMKFAY.U = new int[dUCMKFAY.n * dUCMKFAY.ib];
                                        dUCMKFAY.T = new int[dUCMKFAY.n * dUCMKFAY.ib];
                                        dUCMKFAY.ab = mBMGIXGO.c() == 1;
                                        dUCMKFAY.Q = mBMGIXGO.c() == 1;
                                        dUCMKFAY.J = mBMGIXGO.c() == 1;
                                        dUCMKFAY.C = mBMGIXGO.c() == 1;
                                        dUCMKFAY.y = mBMGIXGO.c();
                                        dUCMKFAY.L = mBMGIXGO.c();
                                        dUCMKFAY.i = new int[20];
                                        dUCMKFAY.O = new int[20];
                                        dUCMKFAY.c = new CXGZMTJK[20];
                                        n6 = 0;
                                        boolean bl7 = true;
                                        do {
                                            if (bl7 && !(bl7 = false) && n == 0) continue;
                                            n4 = mBMGIXGO.c();
                                            if (n4 == 1) {
                                                dUCMKFAY.i[n6] = mBMGIXGO.f();
                                                dUCMKFAY.O[n6] = mBMGIXGO.f();
                                                String string = mBMGIXGO.i();
                                                if (xTGLDHGX2 != null && string.length() > 0) {
                                                    int n10 = string.lastIndexOf(",");
                                                    dUCMKFAY.c[n6] = DUCMKFAY.a(Integer.parseInt(string.substring(n10 + 1)), false, xTGLDHGX2, string.substring(0, n10));
                                                }
                                            }
                                            ++n6;
                                        } while (n6 < 20);
                                        dUCMKFAY.s = new String[5];
                                        n4 = 0;
                                        boolean bl8 = true;
                                        do {
                                            if (bl8 && !(bl8 = false) && n == 0) continue;
                                            dUCMKFAY.s[n4] = mBMGIXGO.i();
                                            if (dUCMKFAY.s[n4].length() == 0) {
                                                dUCMKFAY.s[n4] = null;
                                            }
                                            ++n4;
                                        } while (n4 < 5);
                                    }
                                    if (dUCMKFAY.db == 3) {
                                        boolean bl9 = dUCMKFAY.u = mBMGIXGO.c() == 1;
                                    }
                                    if (dUCMKFAY.db == 4 || dUCMKFAY.db == 1) {
                                        dUCMKFAY.q = mBMGIXGO.c() == 1;
                                        n6 = mBMGIXGO.c();
                                        if (yXVQXWYRArray != null) {
                                            dUCMKFAY.K = yXVQXWYRArray[n6];
                                        }
                                        boolean bl10 = dUCMKFAY.jb = mBMGIXGO.c() == 1;
                                    }
                                    if (dUCMKFAY.db == 4) {
                                        dUCMKFAY.P = mBMGIXGO.i();
                                        dUCMKFAY.v = mBMGIXGO.i();
                                    }
                                    if (dUCMKFAY.db == 1 || dUCMKFAY.db == 3 || dUCMKFAY.db == 4) {
                                        dUCMKFAY.z = mBMGIXGO.h();
                                    }
                                    if (dUCMKFAY.db == 3 || dUCMKFAY.db == 4) {
                                        dUCMKFAY.m = mBMGIXGO.h();
                                        dUCMKFAY.j = mBMGIXGO.h();
                                        dUCMKFAY.G = mBMGIXGO.h();
                                    }
                                    if (dUCMKFAY.db == 5) {
                                        String string = mBMGIXGO.i();
                                        if (xTGLDHGX2 != null && string.length() > 0) {
                                            n4 = string.lastIndexOf(",");
                                            dUCMKFAY.a = DUCMKFAY.a(Integer.parseInt(string.substring(n4 + 1)), false, xTGLDHGX2, string.substring(0, n4));
                                        }
                                        string = mBMGIXGO.i();
                                        if (xTGLDHGX2 != null && string.length() > 0) {
                                            n4 = string.lastIndexOf(",");
                                            dUCMKFAY.bb = DUCMKFAY.a(Integer.parseInt(string.substring(n4 + 1)), false, xTGLDHGX2, string.substring(0, n4));
                                        }
                                    }
                                    if (dUCMKFAY.db != 6) break block51;
                                    n5 = mBMGIXGO.c();
                                    if (n5 != 0) {
                                        dUCMKFAY.A = 1;
                                        dUCMKFAY.B = (n5 - 1 << 8) + mBMGIXGO.c();
                                    }
                                    if ((n5 = mBMGIXGO.c()) != 0) {
                                        dUCMKFAY.W = 1;
                                        dUCMKFAY.X = (n5 - 1 << 8) + mBMGIXGO.c();
                                    }
                                    if ((n5 = mBMGIXGO.c()) == 0) break block52;
                                    dUCMKFAY.Y = (n5 - 1 << 8) + mBMGIXGO.c();
                                    if (n == 0) break block53;
                                }
                                dUCMKFAY.Y = -1;
                            }
                            if ((n5 = mBMGIXGO.c()) == 0) break block54;
                            dUCMKFAY.Z = (n5 - 1 << 8) + mBMGIXGO.c();
                            if (n == 0) break block55;
                        }
                        dUCMKFAY.Z = -1;
                    }
                    dUCMKFAY.kb = mBMGIXGO.e();
                    dUCMKFAY.lb = mBMGIXGO.e();
                    dUCMKFAY.mb = mBMGIXGO.e();
                }
                if (dUCMKFAY.db == 7) {
                    dUCMKFAY.U = new int[dUCMKFAY.n * dUCMKFAY.ib];
                    dUCMKFAY.T = new int[dUCMKFAY.n * dUCMKFAY.ib];
                    dUCMKFAY.q = mBMGIXGO.c() == 1;
                    int n11 = mBMGIXGO.c();
                    if (yXVQXWYRArray != null) {
                        dUCMKFAY.K = yXVQXWYRArray[n11];
                    }
                    dUCMKFAY.jb = mBMGIXGO.c() == 1;
                    dUCMKFAY.z = mBMGIXGO.h();
                    dUCMKFAY.y = mBMGIXGO.f();
                    dUCMKFAY.L = mBMGIXGO.f();
                    dUCMKFAY.Q = mBMGIXGO.c() == 1;
                    dUCMKFAY.s = new String[5];
                    n4 = 0;
                    boolean bl11 = true;
                    do {
                        if (bl11 && !(bl11 = false) && n == 0) continue;
                        dUCMKFAY.s[n4] = mBMGIXGO.i();
                        if (dUCMKFAY.s[n4].length() == 0) {
                            dUCMKFAY.s[n4] = null;
                        }
                        ++n4;
                    } while (n4 < 5);
                }
                if (dUCMKFAY.k == 2 || dUCMKFAY.db == 2) {
                    dUCMKFAY.p = mBMGIXGO.i();
                    dUCMKFAY.l = mBMGIXGO.i();
                    dUCMKFAY.E = mBMGIXGO.e();
                }
                if (dUCMKFAY.k != 1 && dUCMKFAY.k != 4 && dUCMKFAY.k != 5 && dUCMKFAY.k != 6) continue;
                dUCMKFAY.o = mBMGIXGO.i();
                if (dUCMKFAY.o.length() != 0) continue;
                if (dUCMKFAY.k == 1) {
                    dUCMKFAY.o = "Ok";
                }
                if (dUCMKFAY.k == 4) {
                    dUCMKFAY.o = "Select";
                }
                if (dUCMKFAY.k == 5) {
                    dUCMKFAY.o = "Select";
                }
                if (dUCMKFAY.k != 6) continue;
                dUCMKFAY.o = "Continue";
            } while (mBMGIXGO.z < mBMGIXGO.y.length);
            F = null;
            if (by == -84) return;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("33326, " + xTGLDHGX + ", " + yXVQXWYRArray + ", " + by + ", " + xTGLDHGX2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private ZKARKDQW a(int n, int n2) {
        ZKARKDQW zKARKDQW = (ZKARKDQW)fb.a((n << 16) + n2);
        if (zKARKDQW != null) {
            return zKARKDQW;
        }
        if (n == 1) {
            zKARKDQW = ZKARKDQW.b(this.g, n2);
        }
        if (n == 2) {
            zKARKDQW = CKDEJADD.a(n2).a(true);
        }
        if (n == 3) {
            zKARKDQW = client.Bg.a((byte)-41);
        }
        if (n == 4) {
            zKARKDQW = DJRMEMXO.b(n2).a(50, true);
        }
        if (n == 5) {
            zKARKDQW = null;
        }
        if (zKARKDQW != null) {
            fb.a(zKARKDQW, (n << 16) + n2, (byte)2);
        }
        return zKARKDQW;
    }

    private static CXGZMTJK a(int n, boolean bl, XTGLDHGX xTGLDHGX, String string) {
        try {
            long l = (ZTQFNQRH.a((byte)1, string) << 8) + (long)n;
            if (bl) {
                throw new NullPointerException();
            }
            CXGZMTJK cXGZMTJK = (CXGZMTJK)F.a(l);
            if (cXGZMTJK != null) {
                return cXGZMTJK;
            }
            try {
                cXGZMTJK = new CXGZMTJK(xTGLDHGX, string, n);
                F.a(cXGZMTJK, l, (byte)2);
            }
            catch (Exception exception) {
                return null;
            }
            return cXGZMTJK;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("92556, " + n + ", " + bl + ", " + xTGLDHGX + ", " + string + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static void a(int n, boolean bl, int n2, ZKARKDQW zKARKDQW) {
        try {
            if (bl) {
                return;
            }
            fb.a();
            if (zKARKDQW != null && n2 != 4) {
                fb.a(zKARKDQW, (n2 << 16) + n, (byte)2);
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("62376, " + n + ", " + bl + ", " + n2 + ", " + zKARKDQW + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public ZKARKDQW a(int n, int n2, int n3, boolean bl) {
        try {
            ZKARKDQW zKARKDQW;
            block12: {
                block11: {
                    if (!bl) break block11;
                    zKARKDQW = this.a(this.W, this.X);
                    if (client.Jj == 0) break block12;
                }
                zKARKDQW = this.a(this.A, this.B);
            }
            if (zKARKDQW == null) {
                return null;
            }
            if (n3 == -1 && n2 == -1 && zKARKDQW.M == null) {
                return zKARKDQW;
            }
            ZKARKDQW zKARKDQW2 = new ZKARKDQW(9, true, SQHJOGRT.a(n3, false) & SQHJOGRT.a(n2, false), false, zKARKDQW);
            if (n3 != -1 || n2 != -1) {
                zKARKDQW2.a((byte)-71);
            }
            if (n3 != -1) {
                zKARKDQW2.c(n3, 40542);
            }
            if (n2 != -1) {
                zKARKDQW2.c(n2, 40542);
            }
            zKARKDQW2.a(64, 768, -50, -10, -50, true);
            if (n != 0) {
                throw new NullPointerException();
            }
            return zKARKDQW2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("33725, " + n + ", " + n2 + ", " + n3 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    static {
        fb = new GCPOSBWX(false, 30);
    }
}

