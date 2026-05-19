/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class MUDLUUBC {
    private int a = 9;
    private static boolean b = true;
    public static int c;
    public static MUDLUUBC[] d;
    public int e;
    public int f;
    public int g = -1;
    public LKGEGIEW h;
    public int[] i = new int[6];
    public int[] j = new int[6];
    public int k = 128;
    public int l = 128;
    public int m;
    public int n;
    public int o;
    public static GCPOSBWX p;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, XTGLDHGX xTGLDHGX) {
        try {
            MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("spotanim.dat", null), 891);
            if (n != 0) {
                b = !b;
            }
            c = mBMGIXGO.e();
            if (d == null) {
                d = new MUDLUUBC[c];
            }
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && LKGEGIEW.t == 0) continue;
                if (d[n2] == null) {
                    MUDLUUBC.d[n2] = new MUDLUUBC();
                }
                MUDLUUBC.d[n2].e = n2;
                d[n2].a(true, mBMGIXGO);
                ++n2;
            } while (n2 < c);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("33647, " + n + ", " + xTGLDHGX + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(boolean bl, MBMGIXGO mBMGIXGO) {
        int n = LKGEGIEW.t;
        try {
            if (!bl) {
                throw new NullPointerException();
            }
            while (true) {
                int n2;
                if ((n2 = mBMGIXGO.c()) == 0) {
                    return;
                }
                if (n2 == 1) {
                    this.f = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 2) {
                    this.g = mBMGIXGO.e();
                    if (LKGEGIEW.d == null) continue;
                    this.h = LKGEGIEW.d[this.g];
                    if (n == 0) continue;
                }
                if (n2 == 4) {
                    this.k = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 5) {
                    this.l = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 6) {
                    this.m = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 == 7) {
                    this.n = mBMGIXGO.c();
                    if (n == 0) continue;
                }
                if (n2 == 8) {
                    this.o = mBMGIXGO.c();
                    if (n == 0) continue;
                }
                if (n2 >= 40 && n2 < 50) {
                    this.i[n2 - 40] = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                if (n2 >= 50 && n2 < 60) {
                    this.j[n2 - 50] = mBMGIXGO.e();
                    if (n == 0) continue;
                }
                System.out.println("Error unrecognised spotanim config code: " + n2);
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("51394, " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public ZKARKDQW a() {
        ZKARKDQW zKARKDQW = (ZKARKDQW)p.a(this.e);
        if (zKARKDQW != null) {
            return zKARKDQW;
        }
        zKARKDQW = ZKARKDQW.b(this.a, this.f);
        if (zKARKDQW == null) {
            return null;
        }
        int n = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && LKGEGIEW.t == 0) continue;
            if (this.i[0] != 0) {
                zKARKDQW.e(this.i[n], this.j[n]);
            }
            ++n;
        } while (n < 6);
        p.a(zKARKDQW, this.e, (byte)2);
        return zKARKDQW;
    }

    static {
        p = new GCPOSBWX(false, 30);
    }
}

