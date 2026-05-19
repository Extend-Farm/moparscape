/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class MNHKFPQO {
    private boolean a = true;
    private static int b;
    public static int c;
    public static MNHKFPQO[] d;
    public String e;
    public int f;
    public int g = -1;
    public boolean h = false;
    public boolean i = true;
    public int j;
    public int k;
    public int l;
    public int m;
    public int n;
    public int o;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, XTGLDHGX xTGLDHGX) {
        try {
            if (n != 0) {
                b = 115;
            }
            MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a("flo.dat", null), 891);
            c = mBMGIXGO.e();
            if (d == null) {
                d = new MNHKFPQO[c];
            }
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && LKGEGIEW.t == 0) continue;
                if (d[n2] == null) {
                    MNHKFPQO.d[n2] = new MNHKFPQO();
                }
                d[n2].a(true, mBMGIXGO);
                ++n2;
            } while (n2 < c);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("44875, " + n + ", " + xTGLDHGX + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(boolean bl, MBMGIXGO mBMGIXGO) {
        int n = LKGEGIEW.t;
        try {
            if (!bl) {
                boolean bl2 = this.a = !this.a;
            }
            while (true) {
                int n2;
                if ((n2 = mBMGIXGO.c()) == 0) {
                    return;
                }
                if (n2 == 1) {
                    this.f = mBMGIXGO.g();
                    this.a(this.f, 9);
                    if (n == 0) continue;
                }
                if (n2 == 2) {
                    this.g = mBMGIXGO.c();
                    if (n == 0) continue;
                }
                if (n2 == 3) {
                    this.h = true;
                    if (n == 0) continue;
                }
                if (n2 == 5) {
                    this.i = false;
                    if (n == 0) continue;
                }
                if (n2 == 6) {
                    this.e = mBMGIXGO.i();
                    if (n == 0) continue;
                }
                if (n2 == 7) {
                    int n3 = this.j;
                    int n4 = this.k;
                    int n5 = this.l;
                    int n6 = this.m;
                    int n7 = mBMGIXGO.g();
                    this.a(n7, 9);
                    this.j = n3;
                    this.k = n4;
                    this.l = n5;
                    this.m = n6;
                    this.n = n6;
                    if (n == 0) continue;
                }
                System.out.println("Error unrecognised config code: " + n2);
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("56322, " + bl + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private void a(int n, int n2) {
        int n3 = LKGEGIEW.t;
        try {
            int n4;
            int n5;
            int n6;
            block51: {
                block50: {
                    block49: {
                        block48: {
                            block47: {
                                block46: {
                                    double d;
                                    block45: {
                                        double d2;
                                        double d3;
                                        block44: {
                                            block43: {
                                                block42: {
                                                    block41: {
                                                        block40: {
                                                            block37: {
                                                                double d4;
                                                                double d5;
                                                                double d6;
                                                                double d7;
                                                                double d8;
                                                                block39: {
                                                                    block38: {
                                                                        d8 = (double)(n >> 16 & 0xFF) / 256.0;
                                                                        d7 = (double)(n >> 8 & 0xFF) / 256.0;
                                                                        d6 = (double)(n & 0xFF) / 256.0;
                                                                        d5 = d8;
                                                                        if (d7 < d5) {
                                                                            d5 = d7;
                                                                        }
                                                                        if (d6 < d5) {
                                                                            d5 = d6;
                                                                        }
                                                                        if (d7 > (d4 = d8)) {
                                                                            d4 = d7;
                                                                        }
                                                                        if (d6 > d4) {
                                                                            d4 = d6;
                                                                        }
                                                                        d = 0.0;
                                                                        d3 = 0.0;
                                                                        d2 = (d5 + d4) / 2.0;
                                                                        if (d5 == d4) break block37;
                                                                        if (d2 < 0.5) {
                                                                            d3 = (d4 - d5) / (d4 + d5);
                                                                        }
                                                                        if (d2 >= 0.5) {
                                                                            d3 = (d4 - d5) / (2.0 - d4 - d5);
                                                                        }
                                                                        if (d8 != d4) break block38;
                                                                        d = (d7 - d6) / (d4 - d5);
                                                                        if (n3 == 0) break block37;
                                                                    }
                                                                    if (d7 != d4) break block39;
                                                                    d = 2.0 + (d6 - d8) / (d4 - d5);
                                                                    if (n3 == 0) break block37;
                                                                }
                                                                if (d6 == d4) {
                                                                    d = 4.0 + (d8 - d7) / (d4 - d5);
                                                                }
                                                            }
                                                            this.j = (int)((d /= 6.0) * 256.0);
                                                            this.k = (int)(d3 * 256.0);
                                                            this.l = (int)(d2 * 256.0);
                                                            if (this.k >= 0) break block40;
                                                            this.k = 0;
                                                            if (n3 == 0) break block41;
                                                        }
                                                        if (this.k > 255) {
                                                            this.k = 255;
                                                        }
                                                    }
                                                    if (this.l >= 0) break block42;
                                                    this.l = 0;
                                                    if (n3 == 0) break block43;
                                                }
                                                if (this.l > 255) {
                                                    this.l = 255;
                                                }
                                            }
                                            if (!(d2 > 0.5)) break block44;
                                            this.n = (int)((1.0 - d2) * d3 * 512.0);
                                            if (n3 == 0) break block45;
                                        }
                                        this.n = (int)(d2 * d3 * 512.0);
                                    }
                                    if (this.n < 1) {
                                        this.n = 1;
                                    }
                                    this.m = (int)(d * (double)this.n);
                                    n6 = this.j + (int)(Math.random() * 16.0) - 8;
                                    if (n6 >= 0) break block46;
                                    n6 = 0;
                                    if (n3 == 0) break block47;
                                }
                                if (n6 > 255) {
                                    n6 = 255;
                                }
                            }
                            n5 = this.k + (int)(Math.random() * 48.0) - 24;
                            if (n2 < 9 || n2 > 9) {
                                return;
                            }
                            if (n5 >= 0) break block48;
                            n5 = 0;
                            if (n3 == 0) break block49;
                        }
                        if (n5 > 255) {
                            n5 = 255;
                        }
                    }
                    if ((n4 = this.l + (int)(Math.random() * 48.0) - 24) >= 0) break block50;
                    n4 = 0;
                    if (n3 == 0) break block51;
                }
                if (n4 > 255) {
                    n4 = 255;
                }
            }
            this.o = this.a(n6, n5, n4);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("21687, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private final int a(int n, int n2, int n3) {
        if (n3 > 179) {
            n2 /= 2;
        }
        if (n3 > 192) {
            n2 /= 2;
        }
        if (n3 > 217) {
            n2 /= 2;
        }
        if (n3 > 243) {
            n2 /= 2;
        }
        int n4 = (n / 4 << 10) + (n2 / 32 << 7) + n3 / 2;
        return n4;
    }
}

