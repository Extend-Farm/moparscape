/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class WBWOBAFW
extends XHHRODPC {
    private byte m = (byte)7;
    private int n;
    int[] o;
    int p;
    int q;
    private int r;
    private int s;
    private int t;
    private int u;
    private LKGEGIEW v;
    private int w;
    public static client x;
    private int y;
    private int z;
    private int A;
    private int B;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final ZKARKDQW a(int n) {
        int n2 = client.Jj;
        try {
            YZDBYLRM yZDBYLRM;
            int n3;
            block12: {
                block11: {
                    n3 = -1;
                    if (n != 4016) {
                        this.B = -272;
                    }
                    if (this.v != null) {
                        int n4;
                        boolean bl = true;
                        do {
                            if (bl && !(bl = false)) {
                                n4 = client.kh - this.w;
                                if (n4 <= 100 || this.v.i <= 0) continue;
                                n4 = 100;
                                if (n2 == 0) continue;
                            }
                            n4 -= this.v.a(this.n, (byte)-39);
                            ++this.n;
                            if (this.n < this.v.e) continue;
                            this.n -= this.v.i;
                            if (this.n >= 0 && this.n < this.v.e) continue;
                            this.v = null;
                            if (n2 == 0) break;
                        } while (n4 > this.v.a(this.n, (byte)-39));
                        this.w = client.kh - n4;
                        if (this.v != null) {
                            n3 = this.v.f[this.n];
                        }
                    }
                    if (this.o == null) break block11;
                    yZDBYLRM = this.a(true);
                    if (n2 == 0) break block12;
                }
                yZDBYLRM = YZDBYLRM.a(this.y);
            }
            if (yZDBYLRM != null) return yZDBYLRM.a(this.z, this.A, this.r, this.s, this.t, this.u, n3);
            return null;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("90577, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final YZDBYLRM a(boolean bl) {
        int n = client.Jj;
        try {
            int n2;
            block11: {
                block10: {
                    n2 = -1;
                    if (!bl) {
                        int n3 = 1;
                        boolean bl2 = true;
                        do {
                            if (bl2 && !(bl2 = false) && n == 0) continue;
                            ++n3;
                        } while (n3 > 0);
                    }
                    if (this.p == -1) break block10;
                    SXYSOXTR sXYSOXTR = SXYSOXTR.c[this.p];
                    int n4 = sXYSOXTR.e;
                    int n5 = sXYSOXTR.f;
                    int n6 = sXYSOXTR.g;
                    int n7 = client.Di[n6 - n5];
                    n2 = WBWOBAFW.x.Bd[n4] >> n5 & n7;
                    if (n == 0) break block11;
                }
                if (this.q != -1) {
                    n2 = WBWOBAFW.x.Bd[this.q];
                }
            }
            if (n2 >= 0 && n2 < this.o.length && this.o[n2] != -1) {
                return YZDBYLRM.a(this.o[n2]);
            }
            return null;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("17301, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public WBWOBAFW(int n, int n2, int n3, int n4, byte by, int n5, int n6, int n7, int n8, boolean bl) {
        try {
            if (by != this.m) {
                int n9 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && client.Jj == 0) continue;
                    ++n9;
                } while (n9 > 0);
            }
            this.y = n;
            this.z = n3;
            this.A = n2;
            this.r = n6;
            this.s = n4;
            this.t = n5;
            this.u = n7;
            if (n8 != -1) {
                this.v = LKGEGIEW.d[n8];
                this.n = 0;
                this.w = client.kh;
                if (bl && this.v.i != -1) {
                    this.n = (int)(Math.random() * (double)this.v.e);
                    this.w -= (int)(Math.random() * (double)this.v.a(this.n, (byte)-39));
                }
            }
            YZDBYLRM yZDBYLRM = YZDBYLRM.a(this.y);
            this.p = yZDBYLRM.M;
            this.q = yZDBYLRM.n;
            this.o = yZDBYLRM.x;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("84816, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + by + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

