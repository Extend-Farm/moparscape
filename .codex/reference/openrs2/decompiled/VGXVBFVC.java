/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class VGXVBFVC {
    private static boolean a = true;
    private int b = -32357;
    public static int c;
    public static VGXVBFVC[] d;
    public static int e;
    public static int[] f;
    public String g;
    public int h;
    public int i;
    public boolean j = false;
    public boolean k = true;
    public int l;
    public boolean m = false;
    public int n;
    public int o;
    public boolean p = false;
    public int q = -1;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, XTGLDHGX xTGLDHGX) {
        try {
            MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("varp.dat", null), 891);
            e = 0;
            c = mBMGIXGO.e();
            if (d == null) {
                d = new VGXVBFVC[c];
            }
            if (f == null) {
                f = new int[c];
            }
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && LKGEGIEW.t == 0) continue;
                if (d[n2] == null) {
                    VGXVBFVC.d[n2] = new VGXVBFVC();
                }
                d[n2].a(mBMGIXGO, false, n2);
                ++n2;
            } while (n2 < c);
            if (n != 0) {
                a = !a;
            }
            if (mBMGIXGO.z == mBMGIXGO.y.length) return;
            System.out.println("varptype load mismatch");
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("14989, " + n + ", " + xTGLDHGX + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(MBMGIXGO mBMGIXGO, boolean bl, int n) {
        int n2 = LKGEGIEW.t;
        try {
            if (bl) {
                this.b = -91;
            }
            while (true) {
                int n3;
                if ((n3 = mBMGIXGO.c()) == 0) {
                    return;
                }
                if (n3 == 1) {
                    this.h = mBMGIXGO.c();
                    if (n2 == 0) continue;
                }
                if (n3 == 2) {
                    this.i = mBMGIXGO.c();
                    if (n2 == 0) continue;
                }
                if (n3 == 3) {
                    this.j = true;
                    VGXVBFVC.f[VGXVBFVC.e++] = n;
                    if (n2 == 0) continue;
                }
                if (n3 == 4) {
                    this.k = false;
                    if (n2 == 0) continue;
                }
                if (n3 == 5) {
                    this.l = mBMGIXGO.e();
                    if (n2 == 0) continue;
                }
                if (n3 == 6) {
                    this.m = true;
                    if (n2 == 0) continue;
                }
                if (n3 == 7) {
                    this.n = mBMGIXGO.h();
                    if (n2 == 0) continue;
                }
                if (n3 == 8) {
                    this.o = 1;
                    this.p = true;
                    if (n2 == 0) continue;
                }
                if (n3 == 10) {
                    this.g = mBMGIXGO.i();
                    if (n2 == 0) continue;
                }
                if (n3 == 11) {
                    this.p = true;
                    if (n2 == 0) continue;
                }
                if (n3 == 12) {
                    this.q = mBMGIXGO.h();
                    if (n2 == 0) continue;
                }
                if (n3 == 13) {
                    this.o = 2;
                    if (n2 == 0) continue;
                }
                System.out.println("Error unrecognised config code: " + n3);
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("43224, " + mBMGIXGO + ", " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

