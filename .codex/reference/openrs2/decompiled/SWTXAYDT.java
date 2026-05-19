/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

final class SWTXAYDT
extends XHHRODPC {
    public int m;
    public int n;
    private int o = 9;
    private double p;
    private double q;
    private double r;
    private double s;
    private double t;
    public boolean u = false;
    public int v;
    public int w;
    public int x;
    public int y;
    private int z;
    public double A;
    public double B;
    public double C;
    public int D;
    public int E;
    public int F;
    private boolean G = true;
    private MUDLUUBC H;
    private int I;
    private int J;
    public int K;
    public int L;
    public int M;

    public final void a(int n, int n2, int n3, int n4, byte by) {
        try {
            double d;
            if (!this.u) {
                d = n4 - this.v;
                double d2 = n2 - this.w;
                double d3 = Math.sqrt(d * d + d2 * d2);
                this.A = (double)this.v + d * (double)this.E / d3;
                this.B = (double)this.w + d2 * (double)this.E / d3;
                this.C = this.x;
            }
            d = this.n + 1 - n;
            this.p = ((double)n4 - this.A) / d;
            if (by != -83) {
                return;
            }
            this.q = ((double)n2 - this.B) / d;
            this.r = Math.sqrt(this.p * this.p + this.q * this.q);
            if (!this.u) {
                this.s = -this.r * Math.tan((double)this.D * 0.02454369);
            }
            this.t = 2.0 * ((double)n3 - this.C - this.s * d) / (d * d);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("48918, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final ZKARKDQW a(int n) {
        try {
            ZKARKDQW zKARKDQW = this.H.a();
            if (zKARKDQW == null) {
                return null;
            }
            int n2 = -1;
            if (this.H.h != null) {
                n2 = this.H.h.f[this.I];
            }
            ZKARKDQW zKARKDQW2 = new ZKARKDQW(9, true, SQHJOGRT.a(n2, false), false, zKARKDQW);
            if (n2 != -1) {
                zKARKDQW2.a((byte)-71);
                zKARKDQW2.c(n2, 40542);
                zKARKDQW2.eb = null;
                zKARKDQW2.db = null;
            }
            if (this.H.k != 128 || this.H.l != 128) {
                zKARKDQW2.b(this.H.k, this.H.k, this.o, this.H.l);
            }
            zKARKDQW2.d(this.L, 1);
            zKARKDQW2.a(64 + this.H.n, 850 + this.H.o, -30, -50, -30, true);
            if (n != 4016) {
                throw new NullPointerException();
            }
            return zKARKDQW2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("73693, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public SWTXAYDT(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12) {
        try {
            this.H = MUDLUUBC.d[n12];
            if (n3 != 46883) {
                this.G = !this.G;
            }
            this.M = n7;
            this.v = n10;
            this.w = n9;
            this.x = n8;
            this.m = n4;
            this.n = n5;
            this.D = n;
            this.E = n6;
            this.F = n11;
            this.y = n2;
            this.u = false;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("59291, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + n9 + ", " + n10 + ", " + n11 + ", " + n12 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, int n2) {
        try {
            this.u = true;
            this.A += this.p * (double)n;
            this.B += this.q * (double)n;
            this.C += this.s * (double)n + 0.5 * this.t * (double)n * (double)n;
            this.s += this.t * (double)n;
            if (n2 != 0) {
                this.z = 16;
            }
            this.K = (int)(Math.atan2(this.p, this.q) * 325.949) + 1024 & 0x7FF;
            this.L = (int)(Math.atan2(this.s, this.r) * 325.949) & 0x7FF;
            if (this.H.h == null) return;
            this.J += n;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && client.Jj == 0) continue;
                this.J -= this.H.h.a(this.I, (byte)-39) + 1;
                ++this.I;
                if (this.I < this.H.h.e) continue;
                this.I = 0;
            } while (this.J > this.H.h.a(this.I, (byte)-39));
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("76529, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

