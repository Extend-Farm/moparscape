/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class CLRWXPOI {
    private boolean a = true;
    private OZKFTHAD b;
    private OZKFTHAD c;
    private OZKFTHAD d;
    private OZKFTHAD e;
    private OZKFTHAD f;
    private OZKFTHAD g;
    private OZKFTHAD h;
    private OZKFTHAD i;
    private int[] j = new int[5];
    private int[] k = new int[5];
    private int[] l = new int[5];
    private int m;
    private int n = 100;
    private VADHJTLJ o;
    private OZKFTHAD p;
    int q = 500;
    int r;
    private static int[] s;
    private static int[] t;
    private static int[] u;
    private static int[] v;
    private static int[] w;
    private static int[] x;
    private static int[] y;
    private static int[] z;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public static final void a() {
        int n = OZKFTHAD.p;
        t = new int[32768];
        int n2 = 0;
        boolean bl = true;
        do {
            block5: {
                block4: {
                    if (bl && !(bl = false) && n == 0) continue;
                    if (!(Math.random() > 0.5)) break block4;
                    CLRWXPOI.t[n2] = 1;
                    if (n == 0) break block5;
                }
                CLRWXPOI.t[n2] = -1;
            }
            ++n2;
        } while (n2 < 32768);
        u = new int[32768];
        int n3 = 0;
        boolean bl2 = true;
        do {
            if (bl2 && !(bl2 = false) && n == 0) continue;
            CLRWXPOI.u[n3] = (int)(Math.sin((double)n3 / 5215.1903) * 16384.0);
            ++n3;
        } while (n3 < 32768);
        s = new int[220500];
    }

    /*
     * Unable to fully structure code
     */
    public final int[] a(int var1_1, int var2_2) {
        block35: {
            block34: {
                block31: {
                    var23_3 = OZKFTHAD.p;
                    var3_4 = 0;
                    if (var23_3 == 0) ** GOTO lbl7
                    do {
                        CLRWXPOI.s[var3_4] = 0;
                        ++var3_4;
lbl7:
                        // 2 sources

                    } while (var3_4 < var1_1);
                    if (var2_2 < 10) {
                        return CLRWXPOI.s;
                    }
                    var4_5 = (double)var1_1 / ((double)var2_2 + 0.0);
                    this.b.a((byte)8);
                    this.c.a((byte)8);
                    var6_6 = 0;
                    var7_7 = 0;
                    var8_8 = 0;
                    if (this.d != null) {
                        this.d.a((byte)8);
                        this.e.a((byte)8);
                        var6_6 = (int)((double)(this.d.i - this.d.h) * 32.768 / var4_5);
                        var7_7 = (int)((double)this.d.h * 32.768 / var4_5);
                    }
                    var9_9 = 0;
                    var10_10 = 0;
                    var11_11 = 0;
                    if (this.f != null) {
                        this.f.a((byte)8);
                        this.g.a((byte)8);
                        var9_9 = (int)((double)(this.f.i - this.f.h) * 32.768 / var4_5);
                        var10_10 = (int)((double)this.f.h * 32.768 / var4_5);
                    }
                    var12_12 = 0;
                    if (var23_3 == 0) ** GOTO lbl39
                    do {
                        if (this.j[var12_12] != 0) {
                            CLRWXPOI.v[var12_12] = 0;
                            CLRWXPOI.w[var12_12] = (int)((double)this.l[var12_12] * var4_5);
                            CLRWXPOI.x[var12_12] = (this.j[var12_12] << 14) / 100;
                            CLRWXPOI.y[var12_12] = (int)((double)(this.b.i - this.b.h) * 32.768 * Math.pow(1.0057929410678534, this.k[var12_12]) / var4_5);
                            CLRWXPOI.z[var12_12] = (int)((double)this.b.h * 32.768 / var4_5);
                        }
                        ++var12_12;
lbl39:
                        // 2 sources

                    } while (var12_12 < 5);
                    var13_13 = 0;
                    if (var23_3 == 0) ** GOTO lbl66
                    do {
                        var14_14 = this.b.a(true, var1_1);
                        var15_15 = this.c.a(true, var1_1);
                        if (this.d != null) {
                            var16_16 = this.d.a(true, var1_1);
                            var17_17 = this.e.a(true, var1_1);
                            var14_14 += this.a(var17_17, 0, var8_8, this.d.j) >> 1;
                            var8_8 += (var16_16 * var6_6 >> 16) + var7_7;
                        }
                        if (this.f != null) {
                            var16_16 = this.f.a(true, var1_1);
                            var17_17 = this.g.a(true, var1_1);
                            var15_15 = var15_15 * ((this.a(var17_17, 0, var11_11, this.f.j) >> 1) + 32768) >> 15;
                            var11_11 += (var16_16 * var9_9 >> 16) + var10_10;
                        }
                        var16_16 = 0;
                        if (var23_3 == 0) ** GOTO lbl64
                        do {
                            if (this.j[var16_16] != 0 && (var17_17 = var13_13 + CLRWXPOI.w[var16_16]) < var1_1) {
                                v0 = var17_17;
                                CLRWXPOI.s[v0] = CLRWXPOI.s[v0] + this.a(var15_15 * CLRWXPOI.x[var16_16] >> 15, 0, CLRWXPOI.v[var16_16], this.b.j);
                                v1 = var16_16;
                                CLRWXPOI.v[v1] = CLRWXPOI.v[v1] + ((var14_14 * CLRWXPOI.y[var16_16] >> 16) + CLRWXPOI.z[var16_16]);
                            }
                            ++var16_16;
lbl64:
                            // 2 sources

                        } while (var16_16 < 5);
                        ++var13_13;
lbl66:
                        // 2 sources

                    } while (var13_13 < var1_1);
                    if (this.h == null) break block31;
                    this.h.a((byte)8);
                    this.i.a((byte)8);
                    var14_14 = 0;
                    var15_15 = 0;
                    var16_16 = 1;
                    var17_17 = 0;
                    if (var23_3 == 0) ** GOTO lbl90
                    do {
                        block33: {
                            block32: {
                                var18_18 = this.h.a(true, var1_1);
                                var19_19 = this.i.a(true, var1_1);
                                if (var16_16 == 0) break block32;
                                var15_15 = this.h.h + ((this.h.i - this.h.h) * var18_18 >> 8);
                                if (var23_3 == 0) break block33;
                            }
                            var15_15 = this.h.h + ((this.h.i - this.h.h) * var19_19 >> 8);
                        }
                        if ((var14_14 += 256) >= var15_15) {
                            var14_14 = 0;
                            v2 = var16_16 = var16_16 != 0 ? 0 : 1;
                        }
                        if (var16_16 != 0) {
                            CLRWXPOI.s[var17_17] = 0;
                        }
                        ++var17_17;
lbl90:
                        // 2 sources

                    } while (var17_17 < var1_1);
                }
                if (this.m <= 0 || this.n <= 0) break block34;
                var15_15 = var14_14 = (int)((double)this.m * var4_5);
                if (var23_3 == 0) ** GOTO lbl99
                do {
                    v3 = var15_15;
                    CLRWXPOI.s[v3] = CLRWXPOI.s[v3] + CLRWXPOI.s[var15_15 - var14_14] * this.n / 100;
                    ++var15_15;
lbl99:
                    // 2 sources

                } while (var15_15 < var1_1);
            }
            if (this.o.c[0] <= 0 && this.o.c[1] <= 0) break block35;
            this.p.a((byte)8);
            var14_14 = this.p.a(true, var1_1 + 1);
            var15_15 = this.o.a(0, (float)var14_14 / 65536.0f, 201);
            var16_16 = this.o.a(1, (float)var14_14 / 65536.0f, 201);
            if (var1_1 < var15_15 + var16_16) break block35;
            var17_17 = 0;
            var18_18 = var16_16;
            if (var18_18 <= var1_1 - var15_15) ** GOTO lbl129
            var18_18 = var1_1 - var15_15;
            if (var23_3 == 0) ** GOTO lbl129
            do {
                var19_19 = (int)((long)CLRWXPOI.s[var17_17 + var15_15] * (long)VADHJTLJ.j >> 16);
                var20_20 = 0;
                if (var23_3 == 0) ** GOTO lbl119
                do {
                    var19_19 += (int)((long)CLRWXPOI.s[var17_17 + var15_15 - 1 - var20_20] * (long)VADHJTLJ.h[0][var20_20] >> 16);
                    ++var20_20;
lbl119:
                    // 2 sources

                } while (var20_20 < var15_15);
                var21_21 = 0;
                if (var23_3 == 0) ** GOTO lbl125
                do {
                    var19_19 -= (int)((long)CLRWXPOI.s[var17_17 - 1 - var21_21] * (long)VADHJTLJ.h[1][var21_21] >> 16);
                    ++var21_21;
lbl125:
                    // 2 sources

                } while (var21_21 < var17_17);
                CLRWXPOI.s[var17_17] = var19_19;
                var14_14 = this.p.a(true, var1_1 + 1);
                ++var17_17;
lbl129:
                // 3 sources

            } while (var17_17 < var18_18);
            var18_18 = var19_19 = 128;
            do {
                if (var18_18 <= var1_1 - var15_15) ** GOTO lbl152
                var18_18 = var1_1 - var15_15;
                if (var23_3 == 0) ** GOTO lbl152
                do {
                    var20_20 = (int)((long)CLRWXPOI.s[var17_17 + var15_15] * (long)VADHJTLJ.j >> 16);
                    var21_21 = 0;
                    if (var23_3 == 0) ** GOTO lbl142
                    do {
                        var20_20 += (int)((long)CLRWXPOI.s[var17_17 + var15_15 - 1 - var21_21] * (long)VADHJTLJ.h[0][var21_21] >> 16);
                        ++var21_21;
lbl142:
                        // 2 sources

                    } while (var21_21 < var15_15);
                    var22_22 = 0;
                    if (var23_3 == 0) ** GOTO lbl148
                    do {
                        var20_20 -= (int)((long)CLRWXPOI.s[var17_17 - 1 - var22_22] * (long)VADHJTLJ.h[1][var22_22] >> 16);
                        ++var22_22;
lbl148:
                        // 2 sources

                    } while (var22_22 < var16_16);
                    CLRWXPOI.s[var17_17] = var20_20;
                    var14_14 = this.p.a(true, var1_1 + 1);
                    ++var17_17;
lbl152:
                    // 3 sources

                } while (var17_17 < var18_18);
                if (var17_17 >= var1_1 - var15_15) break;
                var15_15 = this.o.a(0, (float)var14_14 / 65536.0f, 201);
                var16_16 = this.o.a(1, (float)var14_14 / 65536.0f, 201);
                var18_18 += var19_19;
            } while (var23_3 == 0);
            if (var23_3 == 0) ** GOTO lbl176
            do {
                var20_20 = 0;
                var21_21 = var17_17 + var15_15 - var1_1;
                if (var23_3 == 0) ** GOTO lbl166
                do {
                    var20_20 += (int)((long)CLRWXPOI.s[var17_17 + var15_15 - 1 - var21_21] * (long)VADHJTLJ.h[0][var21_21] >> 16);
                    ++var21_21;
lbl166:
                    // 2 sources

                } while (var21_21 < var15_15);
                var22_22 = 0;
                if (var23_3 == 0) ** GOTO lbl172
                do {
                    var20_20 -= (int)((long)CLRWXPOI.s[var17_17 - 1 - var22_22] * (long)VADHJTLJ.h[1][var22_22] >> 16);
                    ++var22_22;
lbl172:
                    // 2 sources

                } while (var22_22 < var16_16);
                CLRWXPOI.s[var17_17] = var20_20;
                var14_14 = this.p.a(true, var1_1 + 1);
                ++var17_17;
lbl176:
                // 2 sources

            } while (var17_17 < var1_1);
        }
        var14_14 = 0;
        if (var23_3 == 0) ** GOTO lbl186
        do {
            if (CLRWXPOI.s[var14_14] < -32768) {
                CLRWXPOI.s[var14_14] = -32768;
            }
            if (CLRWXPOI.s[var14_14] > 32767) {
                CLRWXPOI.s[var14_14] = 32767;
            }
            ++var14_14;
lbl186:
            // 2 sources

        } while (var14_14 < var1_1);
        return CLRWXPOI.s;
    }

    private final int a(int n, int n2, int n3, int n4) {
        try {
            if (n2 != 0) {
                boolean bl = this.a = !this.a;
            }
            if (n4 == 1) {
                if ((n3 & Short.MAX_VALUE) < 16384) {
                    return n;
                }
                return -n;
            }
            if (n4 == 2) {
                return u[n3 & Short.MAX_VALUE] * n >> 14;
            }
            if (n4 == 3) {
                return ((n3 & Short.MAX_VALUE) * n >> 14) - n;
            }
            if (n4 == 4) {
                return t[n3 / 2607 & Short.MAX_VALUE] * n;
            }
            return 0;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("46169, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void a(boolean var1_1, MBMGIXGO var2_2) {
        var6_3 = OZKFTHAD.p;
        try {
            this.b = new OZKFTHAD();
            this.b.a(true, var2_2);
            this.c = new OZKFTHAD();
            if (!var1_1) {
                throw new NullPointerException();
            }
            this.c.a(true, var2_2);
            var3_4 = var2_2.c();
            if (var3_4 != 0) {
                --var2_2.z;
                this.d = new OZKFTHAD();
                this.d.a(true, var2_2);
                this.e = new OZKFTHAD();
                this.e.a(true, var2_2);
            }
            if ((var3_4 = var2_2.c()) != 0) {
                --var2_2.z;
                this.f = new OZKFTHAD();
                this.f.a(true, var2_2);
                this.g = new OZKFTHAD();
                this.g.a(true, var2_2);
            }
            if ((var3_4 = var2_2.c()) != 0) {
                --var2_2.z;
                this.h = new OZKFTHAD();
                this.h.a(true, var2_2);
                this.i = new OZKFTHAD();
                this.i.a(true, var2_2);
            }
            var4_6 = 0;
            if (var6_3 == 0) ** GOTO lbl36
            v0 = PKVMXVTO.e = PKVMXVTO.e == false;
            while ((var5_7 = var2_2.k()) != 0) {
                this.j[var4_6] = var5_7;
                this.k[var4_6] = var2_2.j();
                this.l[var4_6] = var2_2.k();
                ++var4_6;
lbl36:
                // 2 sources

                if (var4_6 < 10) continue;
            }
            this.m = var2_2.k();
            this.n = var2_2.k();
            this.q = var2_2.e();
            this.r = var2_2.e();
            this.o = new VADHJTLJ();
            this.p = new OZKFTHAD();
            this.o.a(var2_2, false, this.p);
            return;
        }
        catch (RuntimeException var3_5) {
            signlink.reporterror("9595, " + var1_1 + ", " + var2_2 + ", " + var3_5.toString());
            throw new RuntimeException();
        }
    }

    static {
        v = new int[5];
        w = new int[5];
        x = new int[5];
        y = new int[5];
        z = new int[5];
    }
}

