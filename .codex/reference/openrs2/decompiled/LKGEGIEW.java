/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class LKGEGIEW {
    private int a = 9;
    private boolean b = false;
    public static int c;
    public static LKGEGIEW[] d;
    public int e;
    public int[] f;
    public int[] g;
    private int[] h;
    public int i = -1;
    public int[] j;
    public boolean k = false;
    public int l = 5;
    public int m = -1;
    public int n = -1;
    public int o = 99;
    public int p = -1;
    public int q = -1;
    public int r = 2;
    public int s;
    public static int t;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, XTGLDHGX xTGLDHGX) {
        int n2 = t;
        try {
            MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("seq.dat", null), 891);
            c = mBMGIXGO.e();
            if (d == null) {
                d = new LKGEGIEW[c];
            }
            int n3 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                if (d[n3] == null) {
                    LKGEGIEW.d[n3] = new LKGEGIEW();
                }
                d[n3].a(true, mBMGIXGO);
                ++n3;
            } while (n3 < c);
            if (n == 0) return;
            int n4 = 1;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                ++n4;
            } while (n4 > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("95447, " + n + ", " + xTGLDHGX + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int a(int n, byte by) {
        try {
            SQHJOGRT sQHJOGRT;
            int n2 = this.h[n];
            if (by != -39) {
                int n3 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && t == 0) continue;
                    ++n3;
                } while (n3 > 0);
            }
            if (n2 == 0 && (sQHJOGRT = SQHJOGRT.a(this.a, this.f[n])) != null) {
                n2 = this.h[n] = sQHJOGRT.c;
            }
            if (n2 != 0) return n2;
            return 1;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("1006, " + n + ", " + by + ", " + runtimeException.toString());
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
        int n = t;
        try {
            block25: {
                block26: {
                    int n2;
                    if (!bl) {
                        boolean bl2 = this.b = !this.b;
                    }
                    while ((n2 = mBMGIXGO.c()) != 0) {
                        int n3;
                        if (n2 == 1) {
                            this.e = mBMGIXGO.c();
                            this.f = new int[this.e];
                            this.g = new int[this.e];
                            this.h = new int[this.e];
                            n3 = 0;
                            boolean bl3 = true;
                            do {
                                if (bl3 && !(bl3 = false) && n == 0) continue;
                                this.f[n3] = mBMGIXGO.e();
                                this.g[n3] = mBMGIXGO.e();
                                if (this.g[n3] == 65535) {
                                    this.g[n3] = -1;
                                }
                                this.h[n3] = mBMGIXGO.e();
                                ++n3;
                            } while (n3 < this.e);
                            if (n == 0) continue;
                        }
                        if (n2 == 2) {
                            this.i = mBMGIXGO.e();
                            if (n == 0) continue;
                        }
                        if (n2 == 3) {
                            n3 = mBMGIXGO.c();
                            this.j = new int[n3 + 1];
                            int n4 = 0;
                            boolean bl4 = true;
                            do {
                                if (bl4 && !(bl4 = false) && n == 0) continue;
                                this.j[n4] = mBMGIXGO.c();
                                ++n4;
                            } while (n4 < n3);
                            this.j[n3] = 9999999;
                            if (n == 0) continue;
                        }
                        if (n2 == 4) {
                            this.k = true;
                            if (n == 0) continue;
                        }
                        if (n2 == 5) {
                            this.l = mBMGIXGO.c();
                            if (n == 0) continue;
                        }
                        if (n2 == 6) {
                            this.m = mBMGIXGO.e();
                            if (n == 0) continue;
                        }
                        if (n2 == 7) {
                            this.n = mBMGIXGO.e();
                            if (n == 0) continue;
                        }
                        if (n2 == 8) {
                            this.o = mBMGIXGO.c();
                            if (n == 0) continue;
                        }
                        if (n2 == 9) {
                            this.p = mBMGIXGO.c();
                            if (n == 0) continue;
                        }
                        if (n2 == 10) {
                            this.q = mBMGIXGO.c();
                            if (n == 0) continue;
                        }
                        if (n2 == 11) {
                            this.r = mBMGIXGO.c();
                            if (n == 0) continue;
                        }
                        if (n2 == 12) {
                            this.s = mBMGIXGO.h();
                            if (n == 0) continue;
                        }
                        System.out.println("Error unrecognised seq config code: " + n2);
                        if (n == 0) continue;
                    }
                    if (this.e == 0) {
                        this.e = 1;
                        this.f = new int[1];
                        this.f[0] = -1;
                        this.g = new int[1];
                        this.g[0] = -1;
                        this.h = new int[1];
                        this.h[0] = -1;
                    }
                    if (this.p != -1) break block25;
                    if (this.j == null) break block26;
                    this.p = 2;
                    if (n == 0) break block25;
                }
                this.p = 0;
            }
            if (this.q != -1) return;
            if (this.j != null) {
                this.q = 2;
                return;
            }
            this.q = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("49098, " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

