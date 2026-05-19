/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

final class OJEALINP
extends XHHRODPC {
    public int m;
    public int n;
    public int o;
    public int p;
    public int q;
    private boolean r = true;
    private int s = 9;
    public boolean t = false;
    private MUDLUUBC u;
    private int v;
    private int w;

    public OJEALINP(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        try {
            this.u = MUDLUUBC.d[n5];
            this.m = n;
            this.n = n8;
            this.o = n7;
            this.p = n6;
            this.q = n2 + n4;
            if (n3 != 6) {
                throw new NullPointerException();
            }
            this.t = false;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("97569, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final ZKARKDQW a(int n) {
        try {
            if (n != 4016) {
                throw new NullPointerException();
            }
            ZKARKDQW zKARKDQW = this.u.a();
            if (zKARKDQW == null) {
                return null;
            }
            int n2 = this.u.h.f[this.v];
            ZKARKDQW zKARKDQW2 = new ZKARKDQW(9, true, SQHJOGRT.a(n2, false), false, zKARKDQW);
            if (!this.t) {
                zKARKDQW2.a((byte)-71);
                zKARKDQW2.c(n2, 40542);
                zKARKDQW2.eb = null;
                zKARKDQW2.db = null;
            }
            if (this.u.k != 128 || this.u.l != 128) {
                zKARKDQW2.b(this.u.k, this.u.k, this.s, this.u.l);
            }
            if (this.u.m != 0) {
                if (this.u.m == 90) {
                    zKARKDQW2.e(360);
                }
                if (this.u.m == 180) {
                    zKARKDQW2.e(360);
                    zKARKDQW2.e(360);
                }
                if (this.u.m == 270) {
                    zKARKDQW2.e(360);
                    zKARKDQW2.e(360);
                    zKARKDQW2.e(360);
                }
            }
            zKARKDQW2.a(64 + this.u.n, 850 + this.u.o, -30, -50, -30, true);
            return zKARKDQW2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("45504, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, boolean bl) {
        int n2 = client.Jj;
        try {
            if (!bl) {
                int n3 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n2 == 0) continue;
                    ++n3;
                } while (n3 > 0);
            }
            this.w += n;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n2 == 0) continue;
                this.w -= this.u.h.a(this.v, (byte)-39) + 1;
                ++this.v;
                if (this.v < this.u.h.e || this.v >= 0 && this.v < this.u.h.e) continue;
                this.v = 0;
                this.t = true;
            } while (this.w > this.u.h.a(this.v, (byte)-39));
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("99687, " + n + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

