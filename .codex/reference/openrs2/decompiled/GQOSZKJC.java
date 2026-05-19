/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

class GQOSZKJC
extends XHHRODPC {
    int[] m = new int[10];
    int[] n = new int[10];
    int o = -1;
    int p;
    int q = 32;
    int r = -1;
    String s;
    int t = 200;
    private boolean u = false;
    private int v = -35698;
    int w;
    int x = -1;
    int y = -1;
    int z;
    int[] A = new int[4];
    int[] B = new int[4];
    int[] C = new int[4];
    int D = -1;
    int E;
    int F;
    int G = -1;
    int H;
    int I;
    int J;
    int K;
    int L;
    int M = -1;
    int N;
    int O;
    int P;
    int Q;
    int R;
    int S = -1000;
    int T;
    int U;
    int V = 100;
    private int W = -895;
    int X;
    int Y;
    int Z;
    int ab = 1;
    boolean bb = false;
    int cb;
    int db;
    int eb;
    int fb;
    int gb;
    int hb;
    int ib;
    int jb;
    int kb;
    int lb;
    int mb;
    boolean[] nb = new boolean[10];
    int ob = -1;
    int pb = -1;
    int qb = -1;
    int rb = -1;

    /*
     * Unable to fully structure code
     */
    public final void a(int var1_1, int var2_2, boolean var3_3, boolean var4_4) {
        try {
            block6: {
                if (this.M != -1 && LKGEGIEW.d[this.M].q == 1) {
                    this.M = -1;
                }
                if (var3_3) break block6;
                var5_5 = var1_1 - this.m[0];
                var6_7 = var2_2 - this.n[0];
                if (var5_5 < -8 || var5_5 > 8 || var6_7 < -8 || var6_7 > 8) break block6;
                if (this.L < 9) {
                    ++this.L;
                }
                var7_8 = this.L;
                if (client.Jj == 0) ** GOTO lbl17
                do {
                    this.m[var7_8] = this.m[var7_8 - 1];
                    this.n[var7_8] = this.n[var7_8 - 1];
                    this.nb[var7_8] = this.nb[var7_8 - 1];
                    --var7_8;
lbl17:
                    // 2 sources

                } while (var7_8 > 0);
                this.m[0] = var1_1;
                this.n[0] = var2_2;
                this.nb[0] = false;
                return;
            }
            this.L = 0;
            this.cb = 0;
            this.p = 0;
            this.m[0] = var1_1;
            this.n[0] = var2_2;
            this.kb = this.m[0] * 128 + this.ab * 64;
            if (var4_4) {
                this.W = 42;
            }
            this.lb = this.n[0] * 128 + this.ab * 64;
            return;
        }
        catch (RuntimeException var5_6) {
            signlink.reporterror("59622, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_4 + ", " + var5_6.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(boolean bl) {
        try {
            if (!bl) {
                int n = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && client.Jj == 0) continue;
                    ++n;
                } while (n > 0);
            }
            this.L = 0;
            this.cb = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("51960, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, int n2, int n3, int n4) {
        try {
            int n5 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && client.Jj == 0) continue;
                if (this.C[n5] <= n4) {
                    this.A[n5] = n3;
                    this.B[n5] = n2;
                    this.C[n5] = n4 + 70;
                    return;
                }
                ++n5;
            } while (n5 < 4);
            if (n == this.v) return;
            this.u = !this.u;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("20579, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(boolean bl, byte by, int n) {
        try {
            int n2 = this.m[0];
            int n3 = this.n[0];
            if (n == 0) {
                --n2;
                ++n3;
            }
            if (n == 1) {
                ++n3;
            }
            if (n == 2) {
                ++n2;
                ++n3;
            }
            if (n == 3) {
                --n2;
            }
            if (n == 4) {
                ++n2;
            }
            if (n == 5) {
                --n2;
                --n3;
            }
            if (n == 6) {
                --n3;
            }
            if (n == 7) {
                ++n2;
                --n3;
            }
            if (this.M != -1 && LKGEGIEW.d[this.M].q == 1) {
                this.M = -1;
            }
            if (this.L < 9) {
                ++this.L;
            }
            int n4 = this.L;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && client.Jj == 0) continue;
                this.m[n4] = this.m[n4 - 1];
                this.n[n4] = this.n[n4 - 1];
                this.nb[n4] = this.nb[n4 - 1];
                --n4;
            } while (n4 > 0);
            if (by != 20) {
                return;
            }
            this.m[0] = n2;
            this.n[0] = n3;
            this.nb[0] = bl;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("15386, " + bl + ", " + by + ", " + n + ", " + runtimeException.toString());
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
            if (!bl) {
                int n = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && client.Jj == 0) continue;
                    ++n;
                } while (n > 0);
            }
            return false;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("52737, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    GQOSZKJC() {
    }
}

