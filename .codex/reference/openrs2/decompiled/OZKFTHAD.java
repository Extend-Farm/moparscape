/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class OZKFTHAD {
    private boolean a = false;
    private byte b = (byte)-112;
    private boolean c = false;
    private boolean d = true;
    private int e;
    private int[] f;
    private int[] g;
    int h;
    int i;
    int j;
    private int k;
    private int l;
    private int m;
    private int n;
    private int o;
    public static int p;

    public final void a(boolean bl, MBMGIXGO mBMGIXGO) {
        try {
            this.j = mBMGIXGO.c();
            if (!bl) {
                throw new NullPointerException();
            }
            this.h = mBMGIXGO.h();
            this.i = mBMGIXGO.h();
            this.a((byte)-112, mBMGIXGO);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("70259, " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(byte by, MBMGIXGO mBMGIXGO) {
        try {
            if (by != this.b) {
                this.c = !this.c;
            }
            this.e = mBMGIXGO.c();
            this.f = new int[this.e];
            this.g = new int[this.e];
            int n = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && p == 0) continue;
                this.f[n] = mBMGIXGO.e();
                this.g[n] = mBMGIXGO.e();
                ++n;
            } while (n < this.e);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("22533, " + by + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    final void a(byte by) {
        try {
            block5: {
                block4: {
                    this.k = 0;
                    if (by != 8) break block4;
                    by = 0;
                    if (p == 0) break block5;
                }
                this.d = !this.d;
            }
            this.l = 0;
            this.m = 0;
            this.n = 0;
            this.o = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("98303, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    final int a(boolean bl, int n) {
        try {
            if (!bl) {
                boolean bl2 = this.a = !this.a;
            }
            if (this.o >= this.k) {
                this.n = this.g[this.l++] << 15;
                if (this.l >= this.e) {
                    this.l = this.e - 1;
                }
                this.k = (int)((double)this.f[this.l] / 65536.0 * (double)n);
                if (this.k > this.o) {
                    this.m = ((this.g[this.l] << 15) - this.n) / (this.k - this.o);
                }
            }
            this.n += this.m;
            ++this.o;
            return this.n - this.m >> 15;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("64313, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

