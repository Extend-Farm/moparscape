/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class JHDAGNBV {
    private boolean a = true;
    private static boolean b;
    private static boolean c;
    private int d;
    private static JHDAGNBV[] e;
    public static int[] f;
    private static byte[] g;
    private static MBMGIXGO h;
    private CLRWXPOI[] i = new CLRWXPOI[10];
    private int j;
    private int k;

    private JHDAGNBV(int n) {
        try {
            if (n < 8 || n > 8) {
                this.d = 477;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("92850, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static final void a(int n, MBMGIXGO mBMGIXGO) {
        try {
            g = new byte[441000];
            h = new MBMGIXGO(g, 891);
            if (n != 0) {
                b = !b;
            }
            CLRWXPOI.a();
            while (true) {
                int n2;
                if ((n2 = mBMGIXGO.e()) == 65535) {
                    return;
                }
                JHDAGNBV.e[n2] = new JHDAGNBV(8);
                e[n2].a(true, mBMGIXGO);
                JHDAGNBV.f[n2] = e[n2].a(0);
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("17085, " + n + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static final MBMGIXGO a(int n, int n2, boolean bl) {
        try {
            if (bl) {
                boolean bl2 = c = !c;
            }
            if (e[n2] != null) {
                JHDAGNBV jHDAGNBV = e[n2];
                return jHDAGNBV.a(n, 6);
            }
            return null;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("50738, " + n + ", " + n2 + ", " + bl + ", " + runtimeException.toString());
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
        try {
            int n = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && OZKFTHAD.p == 0) continue;
                int n2 = mBMGIXGO.c();
                if (n2 != 0) {
                    --mBMGIXGO.z;
                    this.i[n] = new CLRWXPOI();
                    this.i[n].a(true, mBMGIXGO);
                }
                ++n;
            } while (n < 10);
            if (!bl) {
                this.d = 58;
            }
            this.j = mBMGIXGO.e();
            this.k = mBMGIXGO.e();
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("1629, " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final int a(int n) {
        int n2 = OZKFTHAD.p;
        try {
            int n3 = 9999999;
            if (n != 0) {
                this.d = -52;
            }
            int n4 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                if (this.i[n4] != null && this.i[n4].r / 20 < n3) {
                    n3 = this.i[n4].r / 20;
                }
                ++n4;
            } while (n4 < 10);
            if (this.j < this.k && this.j / 20 < n3) {
                n3 = this.j / 20;
            }
            if (n3 == 9999999 || n3 == 0) {
                return 0;
            }
            int n5 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                if (this.i[n5] != null) {
                    this.i[n5].r -= n3 * 20;
                }
                ++n5;
            } while (n5 < 10);
            if (this.j < this.k) {
                this.j -= n3 * 20;
                this.k -= n3 * 20;
            }
            return n3;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("43186, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final MBMGIXGO a(int n, int n2) {
        try {
            int n3 = this.b(n);
            JHDAGNBV.h.z = 0;
            h.d(1380533830);
            h.b(0, 36 + n3);
            h.d(1463899717);
            h.d(1718449184);
            h.b(0, 16);
            h.a(true, 1);
            if (n2 < 6 || n2 > 6) {
                int n4 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && OZKFTHAD.p == 0) continue;
                    ++n4;
                } while (n4 > 0);
            }
            h.a(true, 1);
            h.b(0, 22050);
            h.b(0, 22050);
            h.a(true, 1);
            h.a(true, 8);
            h.d(1684108385);
            h.b(0, n3);
            JHDAGNBV.h.z += n3;
            return h;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("47851, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private final int b(int n) {
        int n2;
        int n3;
        int n4;
        int n5 = OZKFTHAD.p;
        int n6 = 0;
        int n7 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && n5 == 0) continue;
            if (this.i[n7] != null && this.i[n7].q + this.i[n7].r > n6) {
                n6 = this.i[n7].q + this.i[n7].r;
            }
            ++n7;
        } while (n7 < 10);
        if (n6 == 0) {
            return 0;
        }
        int n8 = 22050 * n6 / 1000;
        int n9 = 22050 * this.j / 1000;
        int n10 = 22050 * this.k / 1000;
        if (n9 < 0 || n9 > n8 || n10 < 0 || n10 > n8 || n9 >= n10) {
            n = 0;
        }
        int n11 = n8 + (n10 - n9) * (n - 1);
        int n12 = 44;
        boolean bl2 = true;
        do {
            if (bl2 && !(bl2 = false) && n5 == 0) continue;
            JHDAGNBV.g[n12] = -128;
            ++n12;
        } while (n12 < n11 + 44);
        int n13 = 0;
        boolean bl3 = true;
        do {
            if (bl3 && !(bl3 = false) && n5 == 0) continue;
            if (this.i[n13] != null) {
                n4 = this.i[n13].q * 22050 / 1000;
                n3 = this.i[n13].r * 22050 / 1000;
                int[] nArray = this.i[n13].a(n4, this.i[n13].q);
                n2 = 0;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && n5 == 0) continue;
                    int n14 = n2 + n3 + 44;
                    g[n14] = (byte)(g[n14] + (byte)(nArray[n2] >> 8));
                    ++n2;
                } while (n2 < n4);
            }
            ++n13;
        } while (n13 < 10);
        if (n > 1) {
            n9 += 44;
            n10 += 44;
            n4 = (n11 += 44) - (n8 += 44);
            n3 = n8 - 1;
            boolean bl5 = true;
            do {
                if (bl5 && !(bl5 = false) && n5 == 0) continue;
                JHDAGNBV.g[n3 + n4] = g[n3];
                --n3;
            } while (n3 >= n10);
            int n15 = 1;
            boolean bl6 = true;
            do {
                if (bl6 && !(bl6 = false) && n5 == 0) continue;
                n4 = (n10 - n9) * n15;
                n2 = n9;
                boolean bl7 = true;
                do {
                    if (bl7 && !(bl7 = false) && n5 == 0) continue;
                    JHDAGNBV.g[n2 + n4] = g[n2];
                    ++n2;
                } while (n2 < n10);
                ++n15;
            } while (n15 < n);
            n11 -= 44;
        }
        return n11;
    }

    static {
        c = true;
        e = new JHDAGNBV[5000];
        f = new int[5000];
    }
}

