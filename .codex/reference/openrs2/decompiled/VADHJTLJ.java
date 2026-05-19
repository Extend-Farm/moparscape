/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class VADHJTLJ {
    private int a;
    private int b;
    int[] c;
    int[][][] d;
    int[][][] e;
    int[] f;
    static float[][] g = new float[2][8];
    static int[][] h = new int[2][8];
    static float i;
    static int j;

    private float a(int n, int n2, float f, boolean bl) {
        try {
            float f2 = (float)this.e[n][0][n2] + f * (float)(this.e[n][1][n2] - this.e[n][0][n2]);
            if (bl) {
                throw new NullPointerException();
            }
            return 1.0f - (float)Math.pow(10.0, -(f2 *= 0.0015258789f) / 20.0f);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("38400, " + n + ", " + n2 + ", " + f + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private float a(byte by, float f) {
        try {
            float f2;
            block5: {
                block4: {
                    f2 = 32.703197f * (float)Math.pow(2.0, f);
                    if (by != 8) break block4;
                    by = 0;
                    if (OZKFTHAD.p == 0) break block5;
                }
                throw new NullPointerException();
            }
            return f2 * (float)Math.PI / 11025.0f;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("88058, " + by + ", " + f + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private float a(byte by, float f, int n, int n2) {
        try {
            block5: {
                block4: {
                    if (by != 9) break block4;
                    by = 0;
                    if (OZKFTHAD.p == 0) break block5;
                }
                this.a = -457;
            }
            float f2 = (float)this.d[n2][0][n] + f * (float)(this.d[n2][1][n] - this.d[n2][0][n]);
            return this.a((byte)8, f2 *= 1.2207031E-4f);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("4279, " + by + ", " + f + ", " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int a(int n, float f, int n2) {
        int n3 = OZKFTHAD.p;
        try {
            float f2;
            n2 = 31 / n2;
            if (n == 0) {
                f2 = (float)this.f[0] + (float)(this.f[1] - this.f[0]) * f;
                i = (float)Math.pow(0.1, (f2 *= 0.0030517578f) / 20.0f);
                j = (int)(i * 65536.0f);
            }
            if (this.c[n] == 0) {
                return 0;
            }
            f2 = this.a(n, 0, f, false);
            VADHJTLJ.g[n][0] = -2.0f * f2 * (float)Math.cos(this.a((byte)9, f, 0, n));
            VADHJTLJ.g[n][1] = f2 * f2;
            int n4 = 1;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n3 == 0) continue;
                f2 = this.a(n, n4, f, false);
                float f3 = -2.0f * f2 * (float)Math.cos(this.a((byte)9, f, n4, n));
                float f4 = f2 * f2;
                VADHJTLJ.g[n][n4 * 2 + 1] = g[n][n4 * 2 - 1] * f4;
                VADHJTLJ.g[n][n4 * 2] = g[n][n4 * 2 - 1] * f3 + g[n][n4 * 2 - 2] * f4;
                int n5 = n4 * 2 - 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n3 == 0) continue;
                    float[] fArray = g[n];
                    int n6 = n5;
                    fArray[n6] = fArray[n6] + (g[n][n5 - 1] * f3 + g[n][n5 - 2] * f4);
                    --n5;
                } while (n5 >= 2);
                float[] fArray = g[n];
                fArray[1] = fArray[1] + (g[n][0] * f3 + f4);
                float[] fArray2 = g[n];
                fArray2[0] = fArray2[0] + f3;
                ++n4;
            } while (n4 < this.c[n]);
            if (n == 0) {
                int n7 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && n3 == 0) continue;
                    float[] fArray = g[0];
                    int n8 = n7++;
                    fArray[n8] = fArray[n8] * i;
                } while (n7 < this.c[0] * 2);
            }
            int n9 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && n3 == 0) continue;
                VADHJTLJ.h[n][n9] = (int)(g[n][n9] * 65536.0f);
                ++n9;
            } while (n9 < this.c[n] * 2);
            return this.c[n] * 2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("12770, " + n + ", " + f + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(MBMGIXGO mBMGIXGO, boolean bl, OZKFTHAD oZKFTHAD) {
        int n = OZKFTHAD.p;
        try {
            int n2;
            int n3 = mBMGIXGO.c();
            this.c[0] = n3 >> 4;
            if (bl) {
                return;
            }
            this.c[1] = n3 & 0xF;
            if (n3 == 0) {
                this.f[1] = 0;
                this.f[0] = 0;
                return;
            }
            this.f[0] = mBMGIXGO.e();
            this.f[1] = mBMGIXGO.e();
            int n4 = mBMGIXGO.c();
            int n5 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n == 0) continue;
                n2 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && n == 0) continue;
                    this.d[n5][0][n2] = mBMGIXGO.e();
                    this.e[n5][0][n2] = mBMGIXGO.e();
                    ++n2;
                } while (n2 < this.c[n5]);
                ++n5;
            } while (n5 < 2);
            n2 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && n == 0) continue;
                int n6 = 0;
                boolean bl5 = true;
                do {
                    block13: {
                        block12: {
                            if (bl5 && !(bl5 = false) && n == 0) continue;
                            if ((n4 & 1 << n2 * 4 << n6) == 0) break block12;
                            this.d[n2][1][n6] = mBMGIXGO.e();
                            this.e[n2][1][n6] = mBMGIXGO.e();
                            if (n == 0) break block13;
                        }
                        this.d[n2][1][n6] = this.d[n2][0][n6];
                        this.e[n2][1][n6] = this.e[n2][0][n6];
                    }
                    ++n6;
                } while (n6 < this.c[n2]);
                ++n2;
            } while (n2 < 2);
            if (n4 != 0 || this.f[1] != this.f[0]) {
                oZKFTHAD.a((byte)-112, mBMGIXGO);
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("95601, " + mBMGIXGO + ", " + bl + ", " + oZKFTHAD + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public VADHJTLJ() {
        int n = OZKFTHAD.p;
        this.a = 748;
        this.b = 201;
        this.c = new int[2];
        this.d = new int[2][2][4];
        this.e = new int[2][2][4];
        this.f = new int[2];
        if (PKVMXVTO.e) {
            OZKFTHAD.p = ++n;
        }
    }
}

