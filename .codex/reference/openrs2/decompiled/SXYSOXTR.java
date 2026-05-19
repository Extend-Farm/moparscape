/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class SXYSOXTR {
    private static int a;
    public static int b;
    public static SXYSOXTR[] c;
    public String d;
    public int e;
    public int f;
    public int g;
    public boolean h = false;
    public int i = -1;
    public int j;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, XTGLDHGX xTGLDHGX) {
        try {
            if (n != 0) {
                a = 91;
            }
            MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("varbit.dat", null), 891);
            b = mBMGIXGO.e();
            if (c == null) {
                c = new SXYSOXTR[b];
            }
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && LKGEGIEW.t == 0) continue;
                if (c[n2] == null) {
                    SXYSOXTR.c[n2] = new SXYSOXTR();
                }
                c[n2].a(mBMGIXGO, false, n2);
                if (SXYSOXTR.c[n2].h) {
                    VGXVBFVC.d[SXYSOXTR.c[n2].e].p = true;
                }
                ++n2;
            } while (n2 < b);
            if (mBMGIXGO.z == mBMGIXGO.y.length) return;
            System.out.println("varbit load mismatch");
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("95423, " + n + ", " + xTGLDHGX + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(MBMGIXGO mBMGIXGO, boolean bl, int n) {
        int n2 = LKGEGIEW.t;
        try {
            if (bl) {
                return;
            }
            while (true) {
                int n3;
                if ((n3 = mBMGIXGO.c()) == 0) {
                    return;
                }
                if (n3 == 1) {
                    this.e = mBMGIXGO.e();
                    this.f = mBMGIXGO.c();
                    this.g = mBMGIXGO.c();
                    if (n2 == 0) continue;
                }
                if (n3 == 10) {
                    this.d = mBMGIXGO.i();
                    if (n2 == 0) continue;
                }
                if (n3 == 2) {
                    this.h = true;
                    if (n2 == 0) continue;
                }
                if (n3 == 3) {
                    this.i = mBMGIXGO.h();
                    if (n2 == 0) continue;
                }
                if (n3 == 4) {
                    this.j = mBMGIXGO.h();
                    if (n2 == 0) continue;
                }
                System.out.println("Error unrecognised config code: " + n3);
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("71039, " + mBMGIXGO + ", " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

