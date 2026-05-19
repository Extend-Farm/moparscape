/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class OPPOFIOL
extends AFCKELYG {
    private static int x = -436;
    private static int y = -477;
    private static boolean z = true;
    public static boolean A = true;
    static boolean B;
    static boolean C;
    public static boolean D;
    public static int E;
    public static int F;
    public static int G;
    public static int[] H;
    public static int[] I;
    public static int[] J;
    public static int[] K;
    public static int[] L;
    static int M;
    public static DSMJIEPN[] N;
    static boolean[] O;
    static int[] P;
    static int Q;
    static int[][] R;
    static int[][] S;
    public static int[] T;
    public static int U;
    public static int[] V;
    static int[][] W;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void b(int n) {
        try {
            H = null;
            H = null;
            J = null;
            K = null;
            L = null;
            N = null;
            O = null;
            P = null;
            R = null;
            S = null;
            T = null;
            V = null;
            W = null;
            if (n < 0) return;
            int n2 = 1;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                ++n2;
            } while (n2 > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("80327, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void a(byte by) {
        boolean bl = XHHRODPC.l;
        try {
            int n;
            block6: {
                block5: {
                    L = new int[AFCKELYG.o];
                    if (by != 6) break block5;
                    by = 0;
                    if (!bl) break block6;
                }
                n = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && !bl) continue;
                    ++n;
                } while (n > 0);
            }
            n = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                OPPOFIOL.L[n] = AFCKELYG.n * n;
                ++n;
            } while (n < AFCKELYG.o);
            F = AFCKELYG.n / 2;
            G = AFCKELYG.o / 2;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("79230, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void a(int n, int n2, int n3) {
        boolean bl = XHHRODPC.l;
        try {
            L = new int[n3];
            if (bl || n >= 0) {
                return;
            }
            int n4 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                OPPOFIOL.L[n4] = n2 * n4;
                ++n4;
            } while (n4 < n3);
            F = n2 / 2;
            G = n3 / 2;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("8612, " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void c(int n) {
        try {
            if (n < 0 || n > 0) {
                return;
            }
            R = null;
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                OPPOFIOL.S[n2] = null;
                ++n2;
            } while (n2 < 50);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("84003, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void a(int n, boolean bl) {
        boolean bl2 = XHHRODPC.l;
        try {
            int n2;
            block9: {
                block8: {
                    if (!bl) {
                        n2 = 1;
                        boolean bl3 = true;
                        do {
                            if (bl3 && !(bl3 = false) && !bl2) continue;
                            ++n2;
                        } while (n2 > 0);
                    }
                    if (R != null) return;
                    Q = n;
                    if (!A) break block8;
                    R = new int[Q][16384];
                    if (!bl2) break block9;
                }
                R = new int[Q][65536];
            }
            n2 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl2) continue;
                OPPOFIOL.S[n2] = null;
                ++n2;
            } while (n2 < 50);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("54075, " + n + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void a(XTGLDHGX xTGLDHGX, int n) {
        boolean bl = XHHRODPC.l;
        try {
            M = 0;
            if (n != 0) {
                z = !z;
            }
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                try {
                    block10: {
                        block9: {
                            OPPOFIOL.N[n2] = new DSMJIEPN(xTGLDHGX, String.valueOf(n2), 0);
                            if (!A || OPPOFIOL.N[n2].H != 128) break block9;
                            N[n2].b(false);
                            if (!bl) break block10;
                        }
                        N[n2].c(false);
                    }
                    ++M;
                }
                catch (Exception exception) {}
                ++n2;
            } while (n2 < 50);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("98799, " + xTGLDHGX + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final int a(int n, int n2) {
        try {
            if (n2 != 12660) {
                return 2;
            }
            if (P[n] != 0) {
                return P[n];
            }
            int n3 = 0;
            int n4 = 0;
            int n5 = 0;
            int n6 = W[n].length;
            int n7 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                n3 += W[n][n7] >> 16 & 0xFF;
                n4 += W[n][n7] >> 8 & 0xFF;
                n5 += W[n][n7] & 0xFF;
                ++n7;
            } while (n7 < n6);
            int n8 = (n3 / n6 << 16) + (n4 / n6 << 8) + n5 / n6;
            if ((n8 = OPPOFIOL.a(n8, 1.4)) == 0) {
                n8 = 1;
            }
            OPPOFIOL.P[n] = n8;
            return n8;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("10237, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void b(int n, int n2) {
        try {
            if (S[n] == null) {
                return;
            }
            OPPOFIOL.R[OPPOFIOL.Q++] = S[n];
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                x = 7;
            } while (n2 >= 0);
            OPPOFIOL.S[n] = null;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("64331, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public static final int[] d(int n) {
        int[] nArray;
        block15: {
            int n2;
            int n3;
            boolean bl;
            block17: {
                int[] nArray2;
                DSMJIEPN dSMJIEPN;
                block16: {
                    block14: {
                        block13: {
                            block12: {
                                bl = XHHRODPC.l;
                                ++U;
                                if (S[n] != null) {
                                    return S[n];
                                }
                                if (Q <= 0) break block12;
                                nArray = R[--Q];
                                OPPOFIOL.R[OPPOFIOL.Q] = null;
                                if (!bl) break block13;
                            }
                            int n4 = 0;
                            int n5 = -1;
                            n3 = 0;
                            boolean bl2 = true;
                            do {
                                if (bl2 && !(bl2 = false) && !bl) continue;
                                if (S[n3] != null && (T[n3] < n4 || n5 == -1)) {
                                    n4 = T[n3];
                                    n5 = n3;
                                }
                                ++n3;
                            } while (n3 < M);
                            nArray = S[n5];
                            OPPOFIOL.S[n5] = null;
                        }
                        OPPOFIOL.S[n] = nArray;
                        dSMJIEPN = N[n];
                        nArray2 = W[n];
                        if (!A) break block14;
                        OPPOFIOL.O[n] = false;
                        n3 = 0;
                        boolean bl3 = true;
                        do {
                            if (bl3 && !(bl3 = false) && !bl) continue;
                            nArray[n3] = nArray2[dSMJIEPN.B[n3]] & 0xF8F8FF;
                            n2 = nArray[n3];
                            if (n2 == 0) {
                                OPPOFIOL.O[n] = true;
                            }
                            nArray[4096 + n3] = n2 - (n2 >>> 3) & 0xF8F8FF;
                            nArray[8192 + n3] = n2 - (n2 >>> 2) & 0xF8F8FF;
                            nArray[12288 + n3] = n2 - (n2 >>> 2) - (n2 >>> 3) & 0xF8F8FF;
                            ++n3;
                        } while (n3 < 4096);
                        if (!bl) break block15;
                    }
                    if (dSMJIEPN.D != 64) break block16;
                    n3 = 0;
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && !bl) continue;
                        n2 = 0;
                        boolean bl5 = true;
                        do {
                            if (bl5 && !(bl5 = false) && !bl) continue;
                            nArray[n2 + (n3 << 7)] = nArray2[dSMJIEPN.B[(n2 >> 1) + (n3 >> 1 << 6)]];
                            ++n2;
                        } while (n2 < 128);
                        ++n3;
                    } while (n3 < 128);
                    if (!bl) break block17;
                }
                n3 = 0;
                boolean bl6 = true;
                do {
                    if (bl6 && !(bl6 = false) && !bl) continue;
                    nArray[n3] = nArray2[dSMJIEPN.B[n3]];
                    ++n3;
                } while (n3 < 16384);
            }
            OPPOFIOL.O[n] = false;
            n3 = 0;
            boolean bl7 = true;
            do {
                if (bl7 && !(bl7 = false) && !bl) continue;
                int n6 = n3;
                nArray[n6] = nArray[n6] & 0xF8F8FF;
                n2 = nArray[n3];
                if (n2 == 0) {
                    OPPOFIOL.O[n] = true;
                }
                nArray[16384 + n3] = n2 - (n2 >>> 3) & 0xF8F8FF;
                nArray[32768 + n3] = n2 - (n2 >>> 2) & 0xF8F8FF;
                nArray[49152 + n3] = n2 - (n2 >>> 2) - (n2 >>> 3) & 0xF8F8FF;
                ++n3;
            } while (n3 < 16384);
        }
        return nArray;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void a(double d, byte by) {
        boolean bl = XHHRODPC.l;
        try {
            int n;
            if (by != 9) {
                n = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && !bl) continue;
                    ++n;
                } while (n > 0);
            }
            d += Math.random() * 0.03 - 0.015;
            n = 0;
            int n2 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                double d2 = (double)(n2 / 8) / 64.0 + 0.0078125;
                double d3 = (double)(n2 & 7) / 8.0 + 0.0625;
                int n3 = 0;
                boolean bl4 = true;
                do {
                    double d4;
                    double d5;
                    double d6;
                    block30: {
                        double d7;
                        block43: {
                            double d8;
                            double d9;
                            block42: {
                                block41: {
                                    block38: {
                                        block40: {
                                            double d10;
                                            block39: {
                                                block37: {
                                                    block34: {
                                                        block36: {
                                                            double d11;
                                                            block35: {
                                                                block33: {
                                                                    double d12;
                                                                    block32: {
                                                                        block31: {
                                                                            if (bl4 && !(bl4 = false) && !bl) continue;
                                                                            d6 = d12 = (double)n3 / 128.0;
                                                                            d5 = d12;
                                                                            d4 = d12;
                                                                            if (d3 == 0.0) break block30;
                                                                            if (!(d12 < 0.5)) break block31;
                                                                            d9 = d12 * (1.0 + d3);
                                                                            if (!bl) break block32;
                                                                        }
                                                                        d9 = d12 + d3 - d12 * d3;
                                                                    }
                                                                    d7 = 2.0 * d12 - d9;
                                                                    d11 = d2 + 0.3333333333333333;
                                                                    if (d11 > 1.0) {
                                                                        d11 -= 1.0;
                                                                    }
                                                                    d10 = d2;
                                                                    d8 = d2 - 0.3333333333333333;
                                                                    if (d8 < 0.0) {
                                                                        d8 += 1.0;
                                                                    }
                                                                    if (!(6.0 * d11 < 1.0)) break block33;
                                                                    d6 = d7 + (d9 - d7) * 6.0 * d11;
                                                                    if (!bl) break block34;
                                                                }
                                                                if (!(2.0 * d11 < 1.0)) break block35;
                                                                d6 = d9;
                                                                if (!bl) break block34;
                                                            }
                                                            if (!(3.0 * d11 < 2.0)) break block36;
                                                            d6 = d7 + (d9 - d7) * (0.6666666666666666 - d11) * 6.0;
                                                            if (!bl) break block34;
                                                        }
                                                        d6 = d7;
                                                    }
                                                    if (!(6.0 * d10 < 1.0)) break block37;
                                                    d5 = d7 + (d9 - d7) * 6.0 * d10;
                                                    if (!bl) break block38;
                                                }
                                                if (!(2.0 * d10 < 1.0)) break block39;
                                                d5 = d9;
                                                if (!bl) break block38;
                                            }
                                            if (!(3.0 * d10 < 2.0)) break block40;
                                            d5 = d7 + (d9 - d7) * (0.6666666666666666 - d10) * 6.0;
                                            if (!bl) break block38;
                                        }
                                        d5 = d7;
                                    }
                                    if (!(6.0 * d8 < 1.0)) break block41;
                                    d4 = d7 + (d9 - d7) * 6.0 * d8;
                                    if (!bl) break block30;
                                }
                                if (!(2.0 * d8 < 1.0)) break block42;
                                d4 = d9;
                                if (!bl) break block30;
                            }
                            if (!(3.0 * d8 < 2.0)) break block43;
                            d4 = d7 + (d9 - d7) * (0.6666666666666666 - d8) * 6.0;
                            if (!bl) break block30;
                        }
                        d4 = d7;
                    }
                    int n4 = (int)(d6 * 256.0);
                    int n5 = (int)(d5 * 256.0);
                    int n6 = (int)(d4 * 256.0);
                    int n7 = (n4 << 16) + (n5 << 8) + n6;
                    if ((n7 = OPPOFIOL.a(n7, d)) == 0) {
                        n7 = 1;
                    }
                    OPPOFIOL.V[n++] = n7;
                    ++n3;
                } while (n3 < 128);
                ++n2;
            } while (n2 < 512);
            int n8 = 0;
            boolean bl5 = true;
            do {
                if (bl5 && !(bl5 = false) && !bl) continue;
                if (N[n8] != null) {
                    int[] nArray = OPPOFIOL.N[n8].C;
                    OPPOFIOL.W[n8] = new int[nArray.length];
                    int n9 = 0;
                    boolean bl6 = true;
                    do {
                        if (bl6 && !(bl6 = false) && !bl) continue;
                        OPPOFIOL.W[n8][n9] = OPPOFIOL.a(nArray[n9], d);
                        if ((W[n8][n9] & 0xF8F8FF) == 0 && n9 != 0) {
                            OPPOFIOL.W[n8][n9] = 1;
                        }
                        ++n9;
                    } while (n9 < nArray.length);
                }
                ++n8;
            } while (n8 < 50);
            int n10 = 0;
            boolean bl7 = true;
            do {
                if (bl7 && !(bl7 = false) && !bl) continue;
                OPPOFIOL.b(n10, -477);
                ++n10;
            } while (n10 < 50);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("71578, " + d + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static int a(int n, double d) {
        double d2 = (double)(n >> 16) / 256.0;
        double d3 = (double)(n >> 8 & 0xFF) / 256.0;
        double d4 = (double)(n & 0xFF) / 256.0;
        d2 = Math.pow(d2, d);
        d3 = Math.pow(d3, d);
        d4 = Math.pow(d4, d);
        int n2 = (int)(d2 * 256.0);
        int n3 = (int)(d3 * 256.0);
        int n4 = (int)(d4 * 256.0);
        return (n2 << 16) + (n3 << 8) + n4;
    }

    /*
     * Unable to fully structure code
     */
    public static final void a(int var0, int var1_1, int var2_2, int var3_3, int var4_4, int var5_5, int var6_6, int var7_7, int var8_8) {
        block58: {
            block56: {
                block57: {
                    block52: {
                        block55: {
                            block53: {
                                block54: {
                                    block48: {
                                        block51: {
                                            block49: {
                                                block50: {
                                                    var15_9 = XHHRODPC.l;
                                                    var9_10 = 0;
                                                    var10_11 = 0;
                                                    if (var1_1 != var0) {
                                                        var9_10 = (var4_4 - var3_3 << 16) / (var1_1 - var0);
                                                        var10_11 = (var7_7 - var6_6 << 15) / (var1_1 - var0);
                                                    }
                                                    var11_12 = 0;
                                                    var12_13 = 0;
                                                    if (var2_2 != var1_1) {
                                                        var11_12 = (var5_5 - var4_4 << 16) / (var2_2 - var1_1);
                                                        var12_13 = (var8_8 - var7_7 << 15) / (var2_2 - var1_1);
                                                    }
                                                    var13_14 = 0;
                                                    var14_15 = 0;
                                                    if (var2_2 != var0) {
                                                        var13_14 = (var3_3 - var5_5 << 16) / (var0 - var2_2);
                                                        var14_15 = (var6_6 - var8_8 << 15) / (var0 - var2_2);
                                                    }
                                                    if (var0 > var1_1 || var0 > var2_2) break block48;
                                                    if (var0 >= AFCKELYG.q) {
                                                        return;
                                                    }
                                                    if (var1_1 > AFCKELYG.q) {
                                                        var1_1 = AFCKELYG.q;
                                                    }
                                                    if (var2_2 > AFCKELYG.q) {
                                                        var2_2 = AFCKELYG.q;
                                                    }
                                                    if (var1_1 >= var2_2) break block49;
                                                    var5_5 = var3_3 <<= 16;
                                                    var8_8 = var6_6 <<= 15;
                                                    if (var0 < 0) {
                                                        var5_5 -= var13_14 * var0;
                                                        var3_3 -= var9_10 * var0;
                                                        var8_8 -= var14_15 * var0;
                                                        var6_6 -= var10_11 * var0;
                                                        var0 = 0;
                                                    }
                                                    var4_4 <<= 16;
                                                    var7_7 <<= 15;
                                                    if (var1_1 < 0) {
                                                        var4_4 -= var11_12 * var1_1;
                                                        var7_7 -= var12_13 * var1_1;
                                                        var1_1 = 0;
                                                    }
                                                    if ((var0 == var1_1 || var13_14 >= var9_10) && (var0 != var1_1 || var13_14 <= var11_12)) break block50;
                                                    var2_2 -= var1_1;
                                                    var1_1 -= var0;
                                                    var0 = OPPOFIOL.L[var0];
                                                    if (!var15_9) ** GOTO lbl51
                                                    do {
                                                        OPPOFIOL.a(AFCKELYG.m, var0, 0, 0, var5_5 >> 16, var3_3 >> 16, var8_8 >> 7, var6_6 >> 7);
                                                        var5_5 += var13_14;
                                                        var3_3 += var9_10;
                                                        var8_8 += var14_15;
                                                        var6_6 += var10_11;
                                                        var0 += AFCKELYG.n;
lbl51:
                                                        // 2 sources

                                                    } while (--var1_1 >= 0);
                                                    if (!var15_9) ** GOTO lbl60
                                                    do {
                                                        OPPOFIOL.a(AFCKELYG.m, var0, 0, 0, var5_5 >> 16, var4_4 >> 16, var8_8 >> 7, var7_7 >> 7);
                                                        var5_5 += var13_14;
                                                        var4_4 += var11_12;
                                                        var8_8 += var14_15;
                                                        var7_7 += var12_13;
                                                        var0 += AFCKELYG.n;
lbl60:
                                                        // 2 sources

                                                    } while (--var2_2 >= 0);
                                                    return;
                                                }
                                                var2_2 -= var1_1;
                                                var1_1 -= var0;
                                                var0 = OPPOFIOL.L[var0];
                                                if (!var15_9) ** GOTO lbl74
                                                do {
                                                    OPPOFIOL.a(AFCKELYG.m, var0, 0, 0, var3_3 >> 16, var5_5 >> 16, var6_6 >> 7, var8_8 >> 7);
                                                    var5_5 += var13_14;
                                                    var3_3 += var9_10;
                                                    var8_8 += var14_15;
                                                    var6_6 += var10_11;
                                                    var0 += AFCKELYG.n;
lbl74:
                                                    // 2 sources

                                                } while (--var1_1 >= 0);
                                                if (!var15_9) ** GOTO lbl83
                                                do {
                                                    OPPOFIOL.a(AFCKELYG.m, var0, 0, 0, var4_4 >> 16, var5_5 >> 16, var7_7 >> 7, var8_8 >> 7);
                                                    var5_5 += var13_14;
                                                    var4_4 += var11_12;
                                                    var8_8 += var14_15;
                                                    var7_7 += var12_13;
                                                    var0 += AFCKELYG.n;
lbl83:
                                                    // 2 sources

                                                } while (--var2_2 >= 0);
                                                return;
                                            }
                                            var4_4 = var3_3 <<= 16;
                                            var7_7 = var6_6 <<= 15;
                                            if (var0 < 0) {
                                                var4_4 -= var13_14 * var0;
                                                var3_3 -= var9_10 * var0;
                                                var7_7 -= var14_15 * var0;
                                                var6_6 -= var10_11 * var0;
                                                var0 = 0;
                                            }
                                            var5_5 <<= 16;
                                            var8_8 <<= 15;
                                            if (var2_2 < 0) {
                                                var5_5 -= var11_12 * var2_2;
                                                var8_8 -= var12_13 * var2_2;
                                                var2_2 = 0;
                                            }
                                            if ((var0 == var2_2 || var13_14 >= var9_10) && (var0 != var2_2 || var11_12 <= var9_10)) break block51;
                                            var1_1 -= var2_2;
                                            var2_2 -= var0;
                                            var0 = OPPOFIOL.L[var0];
                                            if (!var15_9) ** GOTO lbl112
                                            do {
                                                OPPOFIOL.a(AFCKELYG.m, var0, 0, 0, var4_4 >> 16, var3_3 >> 16, var7_7 >> 7, var6_6 >> 7);
                                                var4_4 += var13_14;
                                                var3_3 += var9_10;
                                                var7_7 += var14_15;
                                                var6_6 += var10_11;
                                                var0 += AFCKELYG.n;
lbl112:
                                                // 2 sources

                                            } while (--var2_2 >= 0);
                                            if (!var15_9) ** GOTO lbl121
                                            do {
                                                OPPOFIOL.a(AFCKELYG.m, var0, 0, 0, var5_5 >> 16, var3_3 >> 16, var8_8 >> 7, var6_6 >> 7);
                                                var5_5 += var11_12;
                                                var3_3 += var9_10;
                                                var8_8 += var12_13;
                                                var6_6 += var10_11;
                                                var0 += AFCKELYG.n;
lbl121:
                                                // 2 sources

                                            } while (--var1_1 >= 0);
                                            return;
                                        }
                                        var1_1 -= var2_2;
                                        var2_2 -= var0;
                                        var0 = OPPOFIOL.L[var0];
                                        if (!var15_9) ** GOTO lbl135
                                        do {
                                            OPPOFIOL.a(AFCKELYG.m, var0, 0, 0, var3_3 >> 16, var4_4 >> 16, var6_6 >> 7, var7_7 >> 7);
                                            var4_4 += var13_14;
                                            var3_3 += var9_10;
                                            var7_7 += var14_15;
                                            var6_6 += var10_11;
                                            var0 += AFCKELYG.n;
lbl135:
                                            // 2 sources

                                        } while (--var2_2 >= 0);
                                        if (!var15_9) ** GOTO lbl144
                                        do {
                                            OPPOFIOL.a(AFCKELYG.m, var0, 0, 0, var3_3 >> 16, var5_5 >> 16, var6_6 >> 7, var8_8 >> 7);
                                            var5_5 += var11_12;
                                            var3_3 += var9_10;
                                            var8_8 += var12_13;
                                            var6_6 += var10_11;
                                            var0 += AFCKELYG.n;
lbl144:
                                            // 2 sources

                                        } while (--var1_1 >= 0);
                                        return;
                                    }
                                    if (var1_1 > var2_2) break block52;
                                    if (var1_1 >= AFCKELYG.q) {
                                        return;
                                    }
                                    if (var2_2 > AFCKELYG.q) {
                                        var2_2 = AFCKELYG.q;
                                    }
                                    if (var0 > AFCKELYG.q) {
                                        var0 = AFCKELYG.q;
                                    }
                                    if (var2_2 >= var0) break block53;
                                    var3_3 = var4_4 <<= 16;
                                    var6_6 = var7_7 <<= 15;
                                    if (var1_1 < 0) {
                                        var3_3 -= var9_10 * var1_1;
                                        var4_4 -= var11_12 * var1_1;
                                        var6_6 -= var10_11 * var1_1;
                                        var7_7 -= var12_13 * var1_1;
                                        var1_1 = 0;
                                    }
                                    var5_5 <<= 16;
                                    var8_8 <<= 15;
                                    if (var2_2 < 0) {
                                        var5_5 -= var13_14 * var2_2;
                                        var8_8 -= var14_15 * var2_2;
                                        var2_2 = 0;
                                    }
                                    if ((var1_1 == var2_2 || var9_10 >= var11_12) && (var1_1 != var2_2 || var9_10 <= var13_14)) break block54;
                                    var0 -= var2_2;
                                    var2_2 -= var1_1;
                                    var1_1 = OPPOFIOL.L[var1_1];
                                    if (!var15_9) ** GOTO lbl181
                                    do {
                                        OPPOFIOL.a(AFCKELYG.m, var1_1, 0, 0, var3_3 >> 16, var4_4 >> 16, var6_6 >> 7, var7_7 >> 7);
                                        var3_3 += var9_10;
                                        var4_4 += var11_12;
                                        var6_6 += var10_11;
                                        var7_7 += var12_13;
                                        var1_1 += AFCKELYG.n;
lbl181:
                                        // 2 sources

                                    } while (--var2_2 >= 0);
                                    if (!var15_9) ** GOTO lbl190
                                    do {
                                        OPPOFIOL.a(AFCKELYG.m, var1_1, 0, 0, var3_3 >> 16, var5_5 >> 16, var6_6 >> 7, var8_8 >> 7);
                                        var3_3 += var9_10;
                                        var5_5 += var13_14;
                                        var6_6 += var10_11;
                                        var8_8 += var14_15;
                                        var1_1 += AFCKELYG.n;
lbl190:
                                        // 2 sources

                                    } while (--var0 >= 0);
                                    return;
                                }
                                var0 -= var2_2;
                                var2_2 -= var1_1;
                                var1_1 = OPPOFIOL.L[var1_1];
                                if (!var15_9) ** GOTO lbl204
                                do {
                                    OPPOFIOL.a(AFCKELYG.m, var1_1, 0, 0, var4_4 >> 16, var3_3 >> 16, var7_7 >> 7, var6_6 >> 7);
                                    var3_3 += var9_10;
                                    var4_4 += var11_12;
                                    var6_6 += var10_11;
                                    var7_7 += var12_13;
                                    var1_1 += AFCKELYG.n;
lbl204:
                                    // 2 sources

                                } while (--var2_2 >= 0);
                                if (!var15_9) ** GOTO lbl213
                                do {
                                    OPPOFIOL.a(AFCKELYG.m, var1_1, 0, 0, var5_5 >> 16, var3_3 >> 16, var8_8 >> 7, var6_6 >> 7);
                                    var3_3 += var9_10;
                                    var5_5 += var13_14;
                                    var6_6 += var10_11;
                                    var8_8 += var14_15;
                                    var1_1 += AFCKELYG.n;
lbl213:
                                    // 2 sources

                                } while (--var0 >= 0);
                                return;
                            }
                            var5_5 = var4_4 <<= 16;
                            var8_8 = var7_7 <<= 15;
                            if (var1_1 < 0) {
                                var5_5 -= var9_10 * var1_1;
                                var4_4 -= var11_12 * var1_1;
                                var8_8 -= var10_11 * var1_1;
                                var7_7 -= var12_13 * var1_1;
                                var1_1 = 0;
                            }
                            var3_3 <<= 16;
                            var6_6 <<= 15;
                            if (var0 < 0) {
                                var3_3 -= var13_14 * var0;
                                var6_6 -= var14_15 * var0;
                                var0 = 0;
                            }
                            if (var9_10 >= var11_12) break block55;
                            var2_2 -= var0;
                            var0 -= var1_1;
                            var1_1 = OPPOFIOL.L[var1_1];
                            if (!var15_9) ** GOTO lbl242
                            do {
                                OPPOFIOL.a(AFCKELYG.m, var1_1, 0, 0, var5_5 >> 16, var4_4 >> 16, var8_8 >> 7, var7_7 >> 7);
                                var5_5 += var9_10;
                                var4_4 += var11_12;
                                var8_8 += var10_11;
                                var7_7 += var12_13;
                                var1_1 += AFCKELYG.n;
lbl242:
                                // 2 sources

                            } while (--var0 >= 0);
                            if (!var15_9) ** GOTO lbl251
                            do {
                                OPPOFIOL.a(AFCKELYG.m, var1_1, 0, 0, var3_3 >> 16, var4_4 >> 16, var6_6 >> 7, var7_7 >> 7);
                                var3_3 += var13_14;
                                var4_4 += var11_12;
                                var6_6 += var14_15;
                                var7_7 += var12_13;
                                var1_1 += AFCKELYG.n;
lbl251:
                                // 2 sources

                            } while (--var2_2 >= 0);
                            return;
                        }
                        var2_2 -= var0;
                        var0 -= var1_1;
                        var1_1 = OPPOFIOL.L[var1_1];
                        if (!var15_9) ** GOTO lbl265
                        do {
                            OPPOFIOL.a(AFCKELYG.m, var1_1, 0, 0, var4_4 >> 16, var5_5 >> 16, var7_7 >> 7, var8_8 >> 7);
                            var5_5 += var9_10;
                            var4_4 += var11_12;
                            var8_8 += var10_11;
                            var7_7 += var12_13;
                            var1_1 += AFCKELYG.n;
lbl265:
                            // 2 sources

                        } while (--var0 >= 0);
                        if (!var15_9) ** GOTO lbl274
                        do {
                            OPPOFIOL.a(AFCKELYG.m, var1_1, 0, 0, var4_4 >> 16, var3_3 >> 16, var7_7 >> 7, var6_6 >> 7);
                            var3_3 += var13_14;
                            var4_4 += var11_12;
                            var6_6 += var14_15;
                            var7_7 += var12_13;
                            var1_1 += AFCKELYG.n;
lbl274:
                            // 2 sources

                        } while (--var2_2 >= 0);
                        return;
                    }
                    if (var2_2 >= AFCKELYG.q) {
                        return;
                    }
                    if (var0 > AFCKELYG.q) {
                        var0 = AFCKELYG.q;
                    }
                    if (var1_1 > AFCKELYG.q) {
                        var1_1 = AFCKELYG.q;
                    }
                    if (var0 >= var1_1) break block56;
                    var4_4 = var5_5 <<= 16;
                    var7_7 = var8_8 <<= 15;
                    if (var2_2 < 0) {
                        var4_4 -= var11_12 * var2_2;
                        var5_5 -= var13_14 * var2_2;
                        var7_7 -= var12_13 * var2_2;
                        var8_8 -= var14_15 * var2_2;
                        var2_2 = 0;
                    }
                    var3_3 <<= 16;
                    var6_6 <<= 15;
                    if (var0 < 0) {
                        var3_3 -= var9_10 * var0;
                        var6_6 -= var10_11 * var0;
                        var0 = 0;
                    }
                    if (var11_12 >= var13_14) break block57;
                    var1_1 -= var0;
                    var0 -= var2_2;
                    var2_2 = OPPOFIOL.L[var2_2];
                    if (!var15_9) ** GOTO lbl310
                    do {
                        OPPOFIOL.a(AFCKELYG.m, var2_2, 0, 0, var4_4 >> 16, var5_5 >> 16, var7_7 >> 7, var8_8 >> 7);
                        var4_4 += var11_12;
                        var5_5 += var13_14;
                        var7_7 += var12_13;
                        var8_8 += var14_15;
                        var2_2 += AFCKELYG.n;
lbl310:
                        // 2 sources

                    } while (--var0 >= 0);
                    if (!var15_9) ** GOTO lbl319
                    do {
                        OPPOFIOL.a(AFCKELYG.m, var2_2, 0, 0, var4_4 >> 16, var3_3 >> 16, var7_7 >> 7, var6_6 >> 7);
                        var4_4 += var11_12;
                        var3_3 += var9_10;
                        var7_7 += var12_13;
                        var6_6 += var10_11;
                        var2_2 += AFCKELYG.n;
lbl319:
                        // 2 sources

                    } while (--var1_1 >= 0);
                    return;
                }
                var1_1 -= var0;
                var0 -= var2_2;
                var2_2 = OPPOFIOL.L[var2_2];
                if (!var15_9) ** GOTO lbl333
                do {
                    OPPOFIOL.a(AFCKELYG.m, var2_2, 0, 0, var5_5 >> 16, var4_4 >> 16, var8_8 >> 7, var7_7 >> 7);
                    var4_4 += var11_12;
                    var5_5 += var13_14;
                    var7_7 += var12_13;
                    var8_8 += var14_15;
                    var2_2 += AFCKELYG.n;
lbl333:
                    // 2 sources

                } while (--var0 >= 0);
                if (!var15_9) ** GOTO lbl342
                do {
                    OPPOFIOL.a(AFCKELYG.m, var2_2, 0, 0, var3_3 >> 16, var4_4 >> 16, var6_6 >> 7, var7_7 >> 7);
                    var4_4 += var11_12;
                    var3_3 += var9_10;
                    var7_7 += var12_13;
                    var6_6 += var10_11;
                    var2_2 += AFCKELYG.n;
lbl342:
                    // 2 sources

                } while (--var1_1 >= 0);
                return;
            }
            var3_3 = var5_5 <<= 16;
            var6_6 = var8_8 <<= 15;
            if (var2_2 < 0) {
                var3_3 -= var11_12 * var2_2;
                var5_5 -= var13_14 * var2_2;
                var6_6 -= var12_13 * var2_2;
                var8_8 -= var14_15 * var2_2;
                var2_2 = 0;
            }
            var4_4 <<= 16;
            var7_7 <<= 15;
            if (var1_1 < 0) {
                var4_4 -= var9_10 * var1_1;
                var7_7 -= var10_11 * var1_1;
                var1_1 = 0;
            }
            if (var11_12 >= var13_14) break block58;
            var0 -= var1_1;
            var1_1 -= var2_2;
            var2_2 = OPPOFIOL.L[var2_2];
            if (!var15_9) ** GOTO lbl371
            do {
                OPPOFIOL.a(AFCKELYG.m, var2_2, 0, 0, var3_3 >> 16, var5_5 >> 16, var6_6 >> 7, var8_8 >> 7);
                var3_3 += var11_12;
                var5_5 += var13_14;
                var6_6 += var12_13;
                var8_8 += var14_15;
                var2_2 += AFCKELYG.n;
lbl371:
                // 2 sources

            } while (--var1_1 >= 0);
            if (!var15_9) ** GOTO lbl380
            do {
                OPPOFIOL.a(AFCKELYG.m, var2_2, 0, 0, var4_4 >> 16, var5_5 >> 16, var7_7 >> 7, var8_8 >> 7);
                var4_4 += var9_10;
                var5_5 += var13_14;
                var7_7 += var10_11;
                var8_8 += var14_15;
                var2_2 += AFCKELYG.n;
lbl380:
                // 2 sources

            } while (--var0 >= 0);
            return;
        }
        var0 -= var1_1;
        var1_1 -= var2_2;
        var2_2 = OPPOFIOL.L[var2_2];
        if (!var15_9) ** GOTO lbl394
        do {
            OPPOFIOL.a(AFCKELYG.m, var2_2, 0, 0, var5_5 >> 16, var3_3 >> 16, var8_8 >> 7, var6_6 >> 7);
            var3_3 += var11_12;
            var5_5 += var13_14;
            var6_6 += var12_13;
            var8_8 += var14_15;
            var2_2 += AFCKELYG.n;
lbl394:
            // 2 sources

        } while (--var1_1 >= 0);
        if (!var15_9) ** GOTO lbl403
        do {
            OPPOFIOL.a(AFCKELYG.m, var2_2, 0, 0, var5_5 >> 16, var4_4 >> 16, var8_8 >> 7, var7_7 >> 7);
            var4_4 += var9_10;
            var5_5 += var13_14;
            var7_7 += var10_11;
            var8_8 += var14_15;
            var2_2 += AFCKELYG.n;
lbl403:
            // 2 sources

        } while (--var0 >= 0);
    }

    /*
     * Unable to fully structure code
     */
    public static final void a(int[] var0, int var1_1, int var2_2, int var3_3, int var4_4, int var5_5, int var6_6, int var7_7) {
        block26: {
            block33: {
                block32: {
                    block30: {
                        block31: {
                            block27: {
                                block29: {
                                    block28: {
                                        var11_8 = XHHRODPC.l;
                                        if (!OPPOFIOL.D) break block26;
                                        if (!OPPOFIOL.B) break block27;
                                        if (var5_5 - var4_4 <= 3) break block28;
                                        var8_9 = (var7_7 - var6_6) / (var5_5 - var4_4);
                                        if (!var11_8) break block29;
                                    }
                                    var8_9 = 0;
                                }
                                if (var5_5 > AFCKELYG.t) {
                                    var5_5 = AFCKELYG.t;
                                }
                                if (var4_4 < 0) {
                                    var6_6 -= var4_4 * var8_9;
                                    var4_4 = 0;
                                }
                                if (var4_4 >= var5_5) {
                                    return;
                                }
                                var1_1 += var4_4;
                                var3_3 = var5_5 - var4_4 >> 2;
                                var8_9 <<= 2;
                                if (!var11_8) break block30;
                            }
                            if (var4_4 >= var5_5) {
                                return;
                            }
                            var1_1 += var4_4;
                            var3_3 = var5_5 - var4_4 >> 2;
                            if (var3_3 <= 0) break block31;
                            var8_9 = (var7_7 - var6_6) * OPPOFIOL.H[var3_3] >> 15;
                            if (!var11_8) break block30;
                        }
                        var8_9 = 0;
                    }
                    if (OPPOFIOL.E != 0) break block32;
                    if (!var11_8) ** GOTO lbl41
                    do {
                        var2_2 = OPPOFIOL.V[var6_6 >> 8];
                        var6_6 += var8_9;
                        var0[var1_1++] = var2_2;
                        var0[var1_1++] = var2_2;
                        var0[var1_1++] = var2_2;
                        var0[var1_1++] = var2_2;
lbl41:
                        // 2 sources

                    } while (--var3_3 >= 0);
                    var3_3 = var5_5 - var4_4 & 3;
                    if (var3_3 > 0) {
                        var2_2 = OPPOFIOL.V[var6_6 >> 8];
                        do {
                            var0[var1_1++] = var2_2;
                        } while (--var3_3 > 0);
                        return;
                    }
                    break block33;
                }
                var9_11 = OPPOFIOL.E;
                var10_13 = 256 - OPPOFIOL.E;
                if (!var11_8) ** GOTO lbl62
                do {
                    var2_2 = OPPOFIOL.V[var6_6 >> 8];
                    var6_6 += var8_9;
                    var2_2 = ((var2_2 & 0xFF00FF) * var10_13 >> 8 & 0xFF00FF) + ((var2_2 & 65280) * var10_13 >> 8 & 65280);
                    var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var9_11 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var9_11 >> 8 & 65280);
                    var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var9_11 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var9_11 >> 8 & 65280);
                    var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var9_11 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var9_11 >> 8 & 65280);
                    var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var9_11 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var9_11 >> 8 & 65280);
lbl62:
                    // 2 sources

                } while (--var3_3 >= 0);
                var3_3 = var5_5 - var4_4 & 3;
                if (var3_3 > 0) {
                    var2_2 = OPPOFIOL.V[var6_6 >> 8];
                    var2_2 = ((var2_2 & 0xFF00FF) * var10_13 >> 8 & 0xFF00FF) + ((var2_2 & 65280) * var10_13 >> 8 & 65280);
                    do {
                        var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var9_11 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var9_11 >> 8 & 65280);
                    } while (--var3_3 > 0);
                }
            }
            return;
        }
        if (var4_4 >= var5_5) {
            return;
        }
        var8_10 = (var7_7 - var6_6) / (var5_5 - var4_4);
        if (OPPOFIOL.B) {
            if (var5_5 > AFCKELYG.t) {
                var5_5 = AFCKELYG.t;
            }
            if (var4_4 < 0) {
                var6_6 -= var4_4 * var8_10;
                var4_4 = 0;
            }
            if (var4_4 >= var5_5) {
                return;
            }
        }
        var1_1 += var4_4;
        var3_3 = var5_5 - var4_4;
        if (OPPOFIOL.E == 0) {
            do {
                var0[var1_1++] = OPPOFIOL.V[var6_6 >> 8];
                var6_6 += var8_10;
            } while (--var3_3 > 0);
            return;
        }
        var9_12 = OPPOFIOL.E;
        var10_14 = 256 - OPPOFIOL.E;
        do {
            var2_2 = OPPOFIOL.V[var6_6 >> 8];
            var6_6 += var8_10;
            var2_2 = ((var2_2 & 0xFF00FF) * var10_14 >> 8 & 0xFF00FF) + ((var2_2 & 65280) * var10_14 >> 8 & 65280);
            var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var9_12 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var9_12 >> 8 & 65280);
        } while (--var3_3 > 0);
    }

    /*
     * Unable to fully structure code
     */
    public static final void c(int var0, int var1_1, int var2_2, int var3_3, int var4_4, int var5_5, int var6_6) {
        block58: {
            block56: {
                block57: {
                    block52: {
                        block55: {
                            block53: {
                                block54: {
                                    block48: {
                                        block51: {
                                            block49: {
                                                block50: {
                                                    var10_7 = XHHRODPC.l;
                                                    var7_8 = 0;
                                                    if (var1_1 != var0) {
                                                        var7_8 = (var4_4 - var3_3 << 16) / (var1_1 - var0);
                                                    }
                                                    var8_9 = 0;
                                                    if (var2_2 != var1_1) {
                                                        var8_9 = (var5_5 - var4_4 << 16) / (var2_2 - var1_1);
                                                    }
                                                    var9_10 = 0;
                                                    if (var2_2 != var0) {
                                                        var9_10 = (var3_3 - var5_5 << 16) / (var0 - var2_2);
                                                    }
                                                    if (var0 > var1_1 || var0 > var2_2) break block48;
                                                    if (var0 >= AFCKELYG.q) {
                                                        return;
                                                    }
                                                    if (var1_1 > AFCKELYG.q) {
                                                        var1_1 = AFCKELYG.q;
                                                    }
                                                    if (var2_2 > AFCKELYG.q) {
                                                        var2_2 = AFCKELYG.q;
                                                    }
                                                    if (var1_1 >= var2_2) break block49;
                                                    var5_5 = var3_3 <<= 16;
                                                    if (var0 < 0) {
                                                        var5_5 -= var9_10 * var0;
                                                        var3_3 -= var7_8 * var0;
                                                        var0 = 0;
                                                    }
                                                    var4_4 <<= 16;
                                                    if (var1_1 < 0) {
                                                        var4_4 -= var8_9 * var1_1;
                                                        var1_1 = 0;
                                                    }
                                                    if ((var0 == var1_1 || var9_10 >= var7_8) && (var0 != var1_1 || var9_10 <= var8_9)) break block50;
                                                    var2_2 -= var1_1;
                                                    var1_1 -= var0;
                                                    var0 = OPPOFIOL.L[var0];
                                                    if (!var10_7) ** GOTO lbl38
                                                    do {
                                                        OPPOFIOL.a(AFCKELYG.m, var0, var6_6, 0, var5_5 >> 16, var3_3 >> 16);
                                                        var5_5 += var9_10;
                                                        var3_3 += var7_8;
                                                        var0 += AFCKELYG.n;
lbl38:
                                                        // 2 sources

                                                    } while (--var1_1 >= 0);
                                                    if (!var10_7) ** GOTO lbl45
                                                    do {
                                                        OPPOFIOL.a(AFCKELYG.m, var0, var6_6, 0, var5_5 >> 16, var4_4 >> 16);
                                                        var5_5 += var9_10;
                                                        var4_4 += var8_9;
                                                        var0 += AFCKELYG.n;
lbl45:
                                                        // 2 sources

                                                    } while (--var2_2 >= 0);
                                                    return;
                                                }
                                                var2_2 -= var1_1;
                                                var1_1 -= var0;
                                                var0 = OPPOFIOL.L[var0];
                                                if (!var10_7) ** GOTO lbl57
                                                do {
                                                    OPPOFIOL.a(AFCKELYG.m, var0, var6_6, 0, var3_3 >> 16, var5_5 >> 16);
                                                    var5_5 += var9_10;
                                                    var3_3 += var7_8;
                                                    var0 += AFCKELYG.n;
lbl57:
                                                    // 2 sources

                                                } while (--var1_1 >= 0);
                                                if (!var10_7) ** GOTO lbl64
                                                do {
                                                    OPPOFIOL.a(AFCKELYG.m, var0, var6_6, 0, var4_4 >> 16, var5_5 >> 16);
                                                    var5_5 += var9_10;
                                                    var4_4 += var8_9;
                                                    var0 += AFCKELYG.n;
lbl64:
                                                    // 2 sources

                                                } while (--var2_2 >= 0);
                                                return;
                                            }
                                            var4_4 = var3_3 <<= 16;
                                            if (var0 < 0) {
                                                var4_4 -= var9_10 * var0;
                                                var3_3 -= var7_8 * var0;
                                                var0 = 0;
                                            }
                                            var5_5 <<= 16;
                                            if (var2_2 < 0) {
                                                var5_5 -= var8_9 * var2_2;
                                                var2_2 = 0;
                                            }
                                            if ((var0 == var2_2 || var9_10 >= var7_8) && (var0 != var2_2 || var8_9 <= var7_8)) break block51;
                                            var1_1 -= var2_2;
                                            var2_2 -= var0;
                                            var0 = OPPOFIOL.L[var0];
                                            if (!var10_7) ** GOTO lbl86
                                            do {
                                                OPPOFIOL.a(AFCKELYG.m, var0, var6_6, 0, var4_4 >> 16, var3_3 >> 16);
                                                var4_4 += var9_10;
                                                var3_3 += var7_8;
                                                var0 += AFCKELYG.n;
lbl86:
                                                // 2 sources

                                            } while (--var2_2 >= 0);
                                            if (!var10_7) ** GOTO lbl93
                                            do {
                                                OPPOFIOL.a(AFCKELYG.m, var0, var6_6, 0, var5_5 >> 16, var3_3 >> 16);
                                                var5_5 += var8_9;
                                                var3_3 += var7_8;
                                                var0 += AFCKELYG.n;
lbl93:
                                                // 2 sources

                                            } while (--var1_1 >= 0);
                                            return;
                                        }
                                        var1_1 -= var2_2;
                                        var2_2 -= var0;
                                        var0 = OPPOFIOL.L[var0];
                                        if (!var10_7) ** GOTO lbl105
                                        do {
                                            OPPOFIOL.a(AFCKELYG.m, var0, var6_6, 0, var3_3 >> 16, var4_4 >> 16);
                                            var4_4 += var9_10;
                                            var3_3 += var7_8;
                                            var0 += AFCKELYG.n;
lbl105:
                                            // 2 sources

                                        } while (--var2_2 >= 0);
                                        if (!var10_7) ** GOTO lbl112
                                        do {
                                            OPPOFIOL.a(AFCKELYG.m, var0, var6_6, 0, var3_3 >> 16, var5_5 >> 16);
                                            var5_5 += var8_9;
                                            var3_3 += var7_8;
                                            var0 += AFCKELYG.n;
lbl112:
                                            // 2 sources

                                        } while (--var1_1 >= 0);
                                        return;
                                    }
                                    if (var1_1 > var2_2) break block52;
                                    if (var1_1 >= AFCKELYG.q) {
                                        return;
                                    }
                                    if (var2_2 > AFCKELYG.q) {
                                        var2_2 = AFCKELYG.q;
                                    }
                                    if (var0 > AFCKELYG.q) {
                                        var0 = AFCKELYG.q;
                                    }
                                    if (var2_2 >= var0) break block53;
                                    var3_3 = var4_4 <<= 16;
                                    if (var1_1 < 0) {
                                        var3_3 -= var7_8 * var1_1;
                                        var4_4 -= var8_9 * var1_1;
                                        var1_1 = 0;
                                    }
                                    var5_5 <<= 16;
                                    if (var2_2 < 0) {
                                        var5_5 -= var9_10 * var2_2;
                                        var2_2 = 0;
                                    }
                                    if ((var1_1 == var2_2 || var7_8 >= var8_9) && (var1_1 != var2_2 || var7_8 <= var9_10)) break block54;
                                    var0 -= var2_2;
                                    var2_2 -= var1_1;
                                    var1_1 = OPPOFIOL.L[var1_1];
                                    if (!var10_7) ** GOTO lbl142
                                    do {
                                        OPPOFIOL.a(AFCKELYG.m, var1_1, var6_6, 0, var3_3 >> 16, var4_4 >> 16);
                                        var3_3 += var7_8;
                                        var4_4 += var8_9;
                                        var1_1 += AFCKELYG.n;
lbl142:
                                        // 2 sources

                                    } while (--var2_2 >= 0);
                                    if (!var10_7) ** GOTO lbl149
                                    do {
                                        OPPOFIOL.a(AFCKELYG.m, var1_1, var6_6, 0, var3_3 >> 16, var5_5 >> 16);
                                        var3_3 += var7_8;
                                        var5_5 += var9_10;
                                        var1_1 += AFCKELYG.n;
lbl149:
                                        // 2 sources

                                    } while (--var0 >= 0);
                                    return;
                                }
                                var0 -= var2_2;
                                var2_2 -= var1_1;
                                var1_1 = OPPOFIOL.L[var1_1];
                                if (!var10_7) ** GOTO lbl161
                                do {
                                    OPPOFIOL.a(AFCKELYG.m, var1_1, var6_6, 0, var4_4 >> 16, var3_3 >> 16);
                                    var3_3 += var7_8;
                                    var4_4 += var8_9;
                                    var1_1 += AFCKELYG.n;
lbl161:
                                    // 2 sources

                                } while (--var2_2 >= 0);
                                if (!var10_7) ** GOTO lbl168
                                do {
                                    OPPOFIOL.a(AFCKELYG.m, var1_1, var6_6, 0, var5_5 >> 16, var3_3 >> 16);
                                    var3_3 += var7_8;
                                    var5_5 += var9_10;
                                    var1_1 += AFCKELYG.n;
lbl168:
                                    // 2 sources

                                } while (--var0 >= 0);
                                return;
                            }
                            var5_5 = var4_4 <<= 16;
                            if (var1_1 < 0) {
                                var5_5 -= var7_8 * var1_1;
                                var4_4 -= var8_9 * var1_1;
                                var1_1 = 0;
                            }
                            var3_3 <<= 16;
                            if (var0 < 0) {
                                var3_3 -= var9_10 * var0;
                                var0 = 0;
                            }
                            if (var7_8 >= var8_9) break block55;
                            var2_2 -= var0;
                            var0 -= var1_1;
                            var1_1 = OPPOFIOL.L[var1_1];
                            if (!var10_7) ** GOTO lbl190
                            do {
                                OPPOFIOL.a(AFCKELYG.m, var1_1, var6_6, 0, var5_5 >> 16, var4_4 >> 16);
                                var5_5 += var7_8;
                                var4_4 += var8_9;
                                var1_1 += AFCKELYG.n;
lbl190:
                                // 2 sources

                            } while (--var0 >= 0);
                            if (!var10_7) ** GOTO lbl197
                            do {
                                OPPOFIOL.a(AFCKELYG.m, var1_1, var6_6, 0, var3_3 >> 16, var4_4 >> 16);
                                var3_3 += var9_10;
                                var4_4 += var8_9;
                                var1_1 += AFCKELYG.n;
lbl197:
                                // 2 sources

                            } while (--var2_2 >= 0);
                            return;
                        }
                        var2_2 -= var0;
                        var0 -= var1_1;
                        var1_1 = OPPOFIOL.L[var1_1];
                        if (!var10_7) ** GOTO lbl209
                        do {
                            OPPOFIOL.a(AFCKELYG.m, var1_1, var6_6, 0, var4_4 >> 16, var5_5 >> 16);
                            var5_5 += var7_8;
                            var4_4 += var8_9;
                            var1_1 += AFCKELYG.n;
lbl209:
                            // 2 sources

                        } while (--var0 >= 0);
                        if (!var10_7) ** GOTO lbl216
                        do {
                            OPPOFIOL.a(AFCKELYG.m, var1_1, var6_6, 0, var4_4 >> 16, var3_3 >> 16);
                            var3_3 += var9_10;
                            var4_4 += var8_9;
                            var1_1 += AFCKELYG.n;
lbl216:
                            // 2 sources

                        } while (--var2_2 >= 0);
                        return;
                    }
                    if (var2_2 >= AFCKELYG.q) {
                        return;
                    }
                    if (var0 > AFCKELYG.q) {
                        var0 = AFCKELYG.q;
                    }
                    if (var1_1 > AFCKELYG.q) {
                        var1_1 = AFCKELYG.q;
                    }
                    if (var0 >= var1_1) break block56;
                    var4_4 = var5_5 <<= 16;
                    if (var2_2 < 0) {
                        var4_4 -= var8_9 * var2_2;
                        var5_5 -= var9_10 * var2_2;
                        var2_2 = 0;
                    }
                    var3_3 <<= 16;
                    if (var0 < 0) {
                        var3_3 -= var7_8 * var0;
                        var0 = 0;
                    }
                    if (var8_9 >= var9_10) break block57;
                    var1_1 -= var0;
                    var0 -= var2_2;
                    var2_2 = OPPOFIOL.L[var2_2];
                    if (!var10_7) ** GOTO lbl245
                    do {
                        OPPOFIOL.a(AFCKELYG.m, var2_2, var6_6, 0, var4_4 >> 16, var5_5 >> 16);
                        var4_4 += var8_9;
                        var5_5 += var9_10;
                        var2_2 += AFCKELYG.n;
lbl245:
                        // 2 sources

                    } while (--var0 >= 0);
                    if (!var10_7) ** GOTO lbl252
                    do {
                        OPPOFIOL.a(AFCKELYG.m, var2_2, var6_6, 0, var4_4 >> 16, var3_3 >> 16);
                        var4_4 += var8_9;
                        var3_3 += var7_8;
                        var2_2 += AFCKELYG.n;
lbl252:
                        // 2 sources

                    } while (--var1_1 >= 0);
                    return;
                }
                var1_1 -= var0;
                var0 -= var2_2;
                var2_2 = OPPOFIOL.L[var2_2];
                if (!var10_7) ** GOTO lbl264
                do {
                    OPPOFIOL.a(AFCKELYG.m, var2_2, var6_6, 0, var5_5 >> 16, var4_4 >> 16);
                    var4_4 += var8_9;
                    var5_5 += var9_10;
                    var2_2 += AFCKELYG.n;
lbl264:
                    // 2 sources

                } while (--var0 >= 0);
                if (!var10_7) ** GOTO lbl271
                do {
                    OPPOFIOL.a(AFCKELYG.m, var2_2, var6_6, 0, var3_3 >> 16, var4_4 >> 16);
                    var4_4 += var8_9;
                    var3_3 += var7_8;
                    var2_2 += AFCKELYG.n;
lbl271:
                    // 2 sources

                } while (--var1_1 >= 0);
                return;
            }
            var3_3 = var5_5 <<= 16;
            if (var2_2 < 0) {
                var3_3 -= var8_9 * var2_2;
                var5_5 -= var9_10 * var2_2;
                var2_2 = 0;
            }
            var4_4 <<= 16;
            if (var1_1 < 0) {
                var4_4 -= var7_8 * var1_1;
                var1_1 = 0;
            }
            if (var8_9 >= var9_10) break block58;
            var0 -= var1_1;
            var1_1 -= var2_2;
            var2_2 = OPPOFIOL.L[var2_2];
            if (!var10_7) ** GOTO lbl293
            do {
                OPPOFIOL.a(AFCKELYG.m, var2_2, var6_6, 0, var3_3 >> 16, var5_5 >> 16);
                var3_3 += var8_9;
                var5_5 += var9_10;
                var2_2 += AFCKELYG.n;
lbl293:
                // 2 sources

            } while (--var1_1 >= 0);
            if (!var10_7) ** GOTO lbl300
            do {
                OPPOFIOL.a(AFCKELYG.m, var2_2, var6_6, 0, var4_4 >> 16, var5_5 >> 16);
                var4_4 += var7_8;
                var5_5 += var9_10;
                var2_2 += AFCKELYG.n;
lbl300:
                // 2 sources

            } while (--var0 >= 0);
            return;
        }
        var0 -= var1_1;
        var1_1 -= var2_2;
        var2_2 = OPPOFIOL.L[var2_2];
        if (!var10_7) ** GOTO lbl312
        do {
            OPPOFIOL.a(AFCKELYG.m, var2_2, var6_6, 0, var5_5 >> 16, var3_3 >> 16);
            var3_3 += var8_9;
            var5_5 += var9_10;
            var2_2 += AFCKELYG.n;
lbl312:
            // 2 sources

        } while (--var1_1 >= 0);
        if (!var10_7) ** GOTO lbl319
        do {
            OPPOFIOL.a(AFCKELYG.m, var2_2, var6_6, 0, var5_5 >> 16, var4_4 >> 16);
            var4_4 += var7_8;
            var5_5 += var9_10;
            var2_2 += AFCKELYG.n;
lbl319:
            // 2 sources

        } while (--var0 >= 0);
    }

    /*
     * Unable to fully structure code
     */
    public static final void a(int[] var0, int var1_1, int var2_2, int var3_3, int var4_4, int var5_5) {
        block8: {
            var8_6 = XHHRODPC.l;
            if (OPPOFIOL.B) {
                if (var5_5 > AFCKELYG.t) {
                    var5_5 = AFCKELYG.t;
                }
                if (var4_4 < 0) {
                    var4_4 = 0;
                }
            }
            if (var4_4 >= var5_5) {
                return;
            }
            var1_1 += var4_4;
            var3_3 = var5_5 - var4_4 >> 2;
            if (OPPOFIOL.E != 0) break block8;
            if (!var8_6) ** GOTO lbl18
            do {
                var0[var1_1++] = var2_2;
                var0[var1_1++] = var2_2;
                var0[var1_1++] = var2_2;
                var0[var1_1++] = var2_2;
lbl18:
                // 2 sources

            } while (--var3_3 >= 0);
            var3_3 = var5_5 - var4_4 & 3;
            if (!var8_6) ** GOTO lbl23
            do {
                var0[var1_1++] = var2_2;
lbl23:
                // 2 sources

            } while (--var3_3 >= 0);
            return;
        }
        var6_7 = OPPOFIOL.E;
        var7_8 = 256 - OPPOFIOL.E;
        var2_2 = ((var2_2 & 0xFF00FF) * var7_8 >> 8 & 0xFF00FF) + ((var2_2 & 65280) * var7_8 >> 8 & 65280);
        if (!var8_6) ** GOTO lbl35
        do {
            var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var6_7 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var6_7 >> 8 & 65280);
            var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var6_7 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var6_7 >> 8 & 65280);
            var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var6_7 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var6_7 >> 8 & 65280);
            var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var6_7 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var6_7 >> 8 & 65280);
lbl35:
            // 2 sources

        } while (--var3_3 >= 0);
        var3_3 = var5_5 - var4_4 & 3;
        if (!var8_6) ** GOTO lbl40
        do {
            var0[var1_1++] = var2_2 + ((var0[var1_1] & 0xFF00FF) * var6_7 >> 8 & 0xFF00FF) + ((var0[var1_1] & 65280) * var6_7 >> 8 & 65280);
lbl40:
            // 2 sources

        } while (--var3_3 >= 0);
    }

    /*
     * Unable to fully structure code
     */
    public static final void a(int var0, int var1_1, int var2_2, int var3_3, int var4_4, int var5_5, int var6_6, int var7_7, int var8_8, int var9_9, int var10_10, int var11_11, int var12_12, int var13_13, int var14_14, int var15_15, int var16_16, int var17_17, int var18_18) {
        block58: {
            block56: {
                block57: {
                    block52: {
                        block55: {
                            block53: {
                                block54: {
                                    block48: {
                                        block51: {
                                            block49: {
                                                block50: {
                                                    var36_19 = XHHRODPC.l;
                                                    var19_20 = OPPOFIOL.d(var18_18);
                                                    OPPOFIOL.C = OPPOFIOL.O[var18_18] == false;
                                                    var10_10 = var9_9 - var10_10;
                                                    var13_13 = var12_12 - var13_13;
                                                    var16_16 = var15_15 - var16_16;
                                                    var20_21 = (var11_11 -= var9_9) * var12_12 - (var14_14 -= var12_12) * var9_9 << 14;
                                                    var21_22 = var14_14 * var15_15 - (var17_17 -= var15_15) * var12_12 << 8;
                                                    var22_23 = var17_17 * var9_9 - var11_11 * var15_15 << 5;
                                                    var23_24 = var10_10 * var12_12 - var13_13 * var9_9 << 14;
                                                    var24_25 = var13_13 * var15_15 - var16_16 * var12_12 << 8;
                                                    var25_26 = var16_16 * var9_9 - var10_10 * var15_15 << 5;
                                                    var26_27 = var13_13 * var11_11 - var10_10 * var14_14 << 14;
                                                    var27_28 = var16_16 * var14_14 - var13_13 * var17_17 << 8;
                                                    var28_29 = var10_10 * var17_17 - var16_16 * var11_11 << 5;
                                                    var29_30 = 0;
                                                    var30_31 = 0;
                                                    if (var1_1 != var0) {
                                                        var29_30 = (var4_4 - var3_3 << 16) / (var1_1 - var0);
                                                        var30_31 = (var7_7 - var6_6 << 16) / (var1_1 - var0);
                                                    }
                                                    var31_32 = 0;
                                                    var32_33 = 0;
                                                    if (var2_2 != var1_1) {
                                                        var31_32 = (var5_5 - var4_4 << 16) / (var2_2 - var1_1);
                                                        var32_33 = (var8_8 - var7_7 << 16) / (var2_2 - var1_1);
                                                    }
                                                    var33_34 = 0;
                                                    var34_35 = 0;
                                                    if (var2_2 != var0) {
                                                        var33_34 = (var3_3 - var5_5 << 16) / (var0 - var2_2);
                                                        var34_35 = (var6_6 - var8_8 << 16) / (var0 - var2_2);
                                                    }
                                                    if (var0 > var1_1 || var0 > var2_2) break block48;
                                                    if (var0 >= AFCKELYG.q) {
                                                        return;
                                                    }
                                                    if (var1_1 > AFCKELYG.q) {
                                                        var1_1 = AFCKELYG.q;
                                                    }
                                                    if (var2_2 > AFCKELYG.q) {
                                                        var2_2 = AFCKELYG.q;
                                                    }
                                                    if (var1_1 >= var2_2) break block49;
                                                    var5_5 = var3_3 <<= 16;
                                                    var8_8 = var6_6 <<= 16;
                                                    if (var0 < 0) {
                                                        var5_5 -= var33_34 * var0;
                                                        var3_3 -= var29_30 * var0;
                                                        var8_8 -= var34_35 * var0;
                                                        var6_6 -= var30_31 * var0;
                                                        var0 = 0;
                                                    }
                                                    var4_4 <<= 16;
                                                    var7_7 <<= 16;
                                                    if (var1_1 < 0) {
                                                        var4_4 -= var31_32 * var1_1;
                                                        var7_7 -= var32_33 * var1_1;
                                                        var1_1 = 0;
                                                    }
                                                    var35_36 = var0 - OPPOFIOL.G;
                                                    var20_21 += var22_23 * var35_36;
                                                    var23_24 += var25_26 * var35_36;
                                                    var26_27 += var28_29 * var35_36;
                                                    if ((var0 == var1_1 || var33_34 >= var29_30) && (var0 != var1_1 || var33_34 <= var31_32)) break block50;
                                                    var2_2 -= var1_1;
                                                    var1_1 -= var0;
                                                    var0 = OPPOFIOL.L[var0];
                                                    if (!var36_19) ** GOTO lbl72
                                                    do {
                                                        OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var0, var5_5 >> 16, var3_3 >> 16, var8_8 >> 8, var6_6 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                                        var5_5 += var33_34;
                                                        var3_3 += var29_30;
                                                        var8_8 += var34_35;
                                                        var6_6 += var30_31;
                                                        var0 += AFCKELYG.n;
                                                        var20_21 += var22_23;
                                                        var23_24 += var25_26;
                                                        var26_27 += var28_29;
lbl72:
                                                        // 2 sources

                                                    } while (--var1_1 >= 0);
                                                    if (!var36_19) ** GOTO lbl84
                                                    do {
                                                        OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var0, var5_5 >> 16, var4_4 >> 16, var8_8 >> 8, var7_7 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                                        var5_5 += var33_34;
                                                        var4_4 += var31_32;
                                                        var8_8 += var34_35;
                                                        var7_7 += var32_33;
                                                        var0 += AFCKELYG.n;
                                                        var20_21 += var22_23;
                                                        var23_24 += var25_26;
                                                        var26_27 += var28_29;
lbl84:
                                                        // 2 sources

                                                    } while (--var2_2 >= 0);
                                                    return;
                                                }
                                                var2_2 -= var1_1;
                                                var1_1 -= var0;
                                                var0 = OPPOFIOL.L[var0];
                                                if (!var36_19) ** GOTO lbl101
                                                do {
                                                    OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var0, var3_3 >> 16, var5_5 >> 16, var6_6 >> 8, var8_8 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                                    var5_5 += var33_34;
                                                    var3_3 += var29_30;
                                                    var8_8 += var34_35;
                                                    var6_6 += var30_31;
                                                    var0 += AFCKELYG.n;
                                                    var20_21 += var22_23;
                                                    var23_24 += var25_26;
                                                    var26_27 += var28_29;
lbl101:
                                                    // 2 sources

                                                } while (--var1_1 >= 0);
                                                if (!var36_19) ** GOTO lbl113
                                                do {
                                                    OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var0, var4_4 >> 16, var5_5 >> 16, var7_7 >> 8, var8_8 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                                    var5_5 += var33_34;
                                                    var4_4 += var31_32;
                                                    var8_8 += var34_35;
                                                    var7_7 += var32_33;
                                                    var0 += AFCKELYG.n;
                                                    var20_21 += var22_23;
                                                    var23_24 += var25_26;
                                                    var26_27 += var28_29;
lbl113:
                                                    // 2 sources

                                                } while (--var2_2 >= 0);
                                                return;
                                            }
                                            var4_4 = var3_3 <<= 16;
                                            var7_7 = var6_6 <<= 16;
                                            if (var0 < 0) {
                                                var4_4 -= var33_34 * var0;
                                                var3_3 -= var29_30 * var0;
                                                var7_7 -= var34_35 * var0;
                                                var6_6 -= var30_31 * var0;
                                                var0 = 0;
                                            }
                                            var5_5 <<= 16;
                                            var8_8 <<= 16;
                                            if (var2_2 < 0) {
                                                var5_5 -= var31_32 * var2_2;
                                                var8_8 -= var32_33 * var2_2;
                                                var2_2 = 0;
                                            }
                                            var35_37 = var0 - OPPOFIOL.G;
                                            var20_21 += var22_23 * var35_37;
                                            var23_24 += var25_26 * var35_37;
                                            var26_27 += var28_29 * var35_37;
                                            if ((var0 == var2_2 || var33_34 >= var29_30) && (var0 != var2_2 || var31_32 <= var29_30)) break block51;
                                            var1_1 -= var2_2;
                                            var2_2 -= var0;
                                            var0 = OPPOFIOL.L[var0];
                                            if (!var36_19) ** GOTO lbl149
                                            do {
                                                OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var0, var4_4 >> 16, var3_3 >> 16, var7_7 >> 8, var6_6 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                                var4_4 += var33_34;
                                                var3_3 += var29_30;
                                                var7_7 += var34_35;
                                                var6_6 += var30_31;
                                                var0 += AFCKELYG.n;
                                                var20_21 += var22_23;
                                                var23_24 += var25_26;
                                                var26_27 += var28_29;
lbl149:
                                                // 2 sources

                                            } while (--var2_2 >= 0);
                                            if (!var36_19) ** GOTO lbl161
                                            do {
                                                OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var0, var5_5 >> 16, var3_3 >> 16, var8_8 >> 8, var6_6 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                                var5_5 += var31_32;
                                                var3_3 += var29_30;
                                                var8_8 += var32_33;
                                                var6_6 += var30_31;
                                                var0 += AFCKELYG.n;
                                                var20_21 += var22_23;
                                                var23_24 += var25_26;
                                                var26_27 += var28_29;
lbl161:
                                                // 2 sources

                                            } while (--var1_1 >= 0);
                                            return;
                                        }
                                        var1_1 -= var2_2;
                                        var2_2 -= var0;
                                        var0 = OPPOFIOL.L[var0];
                                        if (!var36_19) ** GOTO lbl178
                                        do {
                                            OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var0, var3_3 >> 16, var4_4 >> 16, var6_6 >> 8, var7_7 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                            var4_4 += var33_34;
                                            var3_3 += var29_30;
                                            var7_7 += var34_35;
                                            var6_6 += var30_31;
                                            var0 += AFCKELYG.n;
                                            var20_21 += var22_23;
                                            var23_24 += var25_26;
                                            var26_27 += var28_29;
lbl178:
                                            // 2 sources

                                        } while (--var2_2 >= 0);
                                        if (!var36_19) ** GOTO lbl190
                                        do {
                                            OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var0, var3_3 >> 16, var5_5 >> 16, var6_6 >> 8, var8_8 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                            var5_5 += var31_32;
                                            var3_3 += var29_30;
                                            var8_8 += var32_33;
                                            var6_6 += var30_31;
                                            var0 += AFCKELYG.n;
                                            var20_21 += var22_23;
                                            var23_24 += var25_26;
                                            var26_27 += var28_29;
lbl190:
                                            // 2 sources

                                        } while (--var1_1 >= 0);
                                        return;
                                    }
                                    if (var1_1 > var2_2) break block52;
                                    if (var1_1 >= AFCKELYG.q) {
                                        return;
                                    }
                                    if (var2_2 > AFCKELYG.q) {
                                        var2_2 = AFCKELYG.q;
                                    }
                                    if (var0 > AFCKELYG.q) {
                                        var0 = AFCKELYG.q;
                                    }
                                    if (var2_2 >= var0) break block53;
                                    var3_3 = var4_4 <<= 16;
                                    var6_6 = var7_7 <<= 16;
                                    if (var1_1 < 0) {
                                        var3_3 -= var29_30 * var1_1;
                                        var4_4 -= var31_32 * var1_1;
                                        var6_6 -= var30_31 * var1_1;
                                        var7_7 -= var32_33 * var1_1;
                                        var1_1 = 0;
                                    }
                                    var5_5 <<= 16;
                                    var8_8 <<= 16;
                                    if (var2_2 < 0) {
                                        var5_5 -= var33_34 * var2_2;
                                        var8_8 -= var34_35 * var2_2;
                                        var2_2 = 0;
                                    }
                                    var35_38 = var1_1 - OPPOFIOL.G;
                                    var20_21 += var22_23 * var35_38;
                                    var23_24 += var25_26 * var35_38;
                                    var26_27 += var28_29 * var35_38;
                                    if ((var1_1 == var2_2 || var29_30 >= var31_32) && (var1_1 != var2_2 || var29_30 <= var33_34)) break block54;
                                    var0 -= var2_2;
                                    var2_2 -= var1_1;
                                    var1_1 = OPPOFIOL.L[var1_1];
                                    if (!var36_19) ** GOTO lbl234
                                    do {
                                        OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var1_1, var3_3 >> 16, var4_4 >> 16, var6_6 >> 8, var7_7 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                        var3_3 += var29_30;
                                        var4_4 += var31_32;
                                        var6_6 += var30_31;
                                        var7_7 += var32_33;
                                        var1_1 += AFCKELYG.n;
                                        var20_21 += var22_23;
                                        var23_24 += var25_26;
                                        var26_27 += var28_29;
lbl234:
                                        // 2 sources

                                    } while (--var2_2 >= 0);
                                    if (!var36_19) ** GOTO lbl246
                                    do {
                                        OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var1_1, var3_3 >> 16, var5_5 >> 16, var6_6 >> 8, var8_8 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                        var3_3 += var29_30;
                                        var5_5 += var33_34;
                                        var6_6 += var30_31;
                                        var8_8 += var34_35;
                                        var1_1 += AFCKELYG.n;
                                        var20_21 += var22_23;
                                        var23_24 += var25_26;
                                        var26_27 += var28_29;
lbl246:
                                        // 2 sources

                                    } while (--var0 >= 0);
                                    return;
                                }
                                var0 -= var2_2;
                                var2_2 -= var1_1;
                                var1_1 = OPPOFIOL.L[var1_1];
                                if (!var36_19) ** GOTO lbl263
                                do {
                                    OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var1_1, var4_4 >> 16, var3_3 >> 16, var7_7 >> 8, var6_6 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                    var3_3 += var29_30;
                                    var4_4 += var31_32;
                                    var6_6 += var30_31;
                                    var7_7 += var32_33;
                                    var1_1 += AFCKELYG.n;
                                    var20_21 += var22_23;
                                    var23_24 += var25_26;
                                    var26_27 += var28_29;
lbl263:
                                    // 2 sources

                                } while (--var2_2 >= 0);
                                if (!var36_19) ** GOTO lbl275
                                do {
                                    OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var1_1, var5_5 >> 16, var3_3 >> 16, var8_8 >> 8, var6_6 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                    var3_3 += var29_30;
                                    var5_5 += var33_34;
                                    var6_6 += var30_31;
                                    var8_8 += var34_35;
                                    var1_1 += AFCKELYG.n;
                                    var20_21 += var22_23;
                                    var23_24 += var25_26;
                                    var26_27 += var28_29;
lbl275:
                                    // 2 sources

                                } while (--var0 >= 0);
                                return;
                            }
                            var5_5 = var4_4 <<= 16;
                            var8_8 = var7_7 <<= 16;
                            if (var1_1 < 0) {
                                var5_5 -= var29_30 * var1_1;
                                var4_4 -= var31_32 * var1_1;
                                var8_8 -= var30_31 * var1_1;
                                var7_7 -= var32_33 * var1_1;
                                var1_1 = 0;
                            }
                            var3_3 <<= 16;
                            var6_6 <<= 16;
                            if (var0 < 0) {
                                var3_3 -= var33_34 * var0;
                                var6_6 -= var34_35 * var0;
                                var0 = 0;
                            }
                            var35_39 = var1_1 - OPPOFIOL.G;
                            var20_21 += var22_23 * var35_39;
                            var23_24 += var25_26 * var35_39;
                            var26_27 += var28_29 * var35_39;
                            if (var29_30 >= var31_32) break block55;
                            var2_2 -= var0;
                            var0 -= var1_1;
                            var1_1 = OPPOFIOL.L[var1_1];
                            if (!var36_19) ** GOTO lbl311
                            do {
                                OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var1_1, var5_5 >> 16, var4_4 >> 16, var8_8 >> 8, var7_7 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                var5_5 += var29_30;
                                var4_4 += var31_32;
                                var8_8 += var30_31;
                                var7_7 += var32_33;
                                var1_1 += AFCKELYG.n;
                                var20_21 += var22_23;
                                var23_24 += var25_26;
                                var26_27 += var28_29;
lbl311:
                                // 2 sources

                            } while (--var0 >= 0);
                            if (!var36_19) ** GOTO lbl323
                            do {
                                OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var1_1, var3_3 >> 16, var4_4 >> 16, var6_6 >> 8, var7_7 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                                var3_3 += var33_34;
                                var4_4 += var31_32;
                                var6_6 += var34_35;
                                var7_7 += var32_33;
                                var1_1 += AFCKELYG.n;
                                var20_21 += var22_23;
                                var23_24 += var25_26;
                                var26_27 += var28_29;
lbl323:
                                // 2 sources

                            } while (--var2_2 >= 0);
                            return;
                        }
                        var2_2 -= var0;
                        var0 -= var1_1;
                        var1_1 = OPPOFIOL.L[var1_1];
                        if (!var36_19) ** GOTO lbl340
                        do {
                            OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var1_1, var4_4 >> 16, var5_5 >> 16, var7_7 >> 8, var8_8 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                            var5_5 += var29_30;
                            var4_4 += var31_32;
                            var8_8 += var30_31;
                            var7_7 += var32_33;
                            var1_1 += AFCKELYG.n;
                            var20_21 += var22_23;
                            var23_24 += var25_26;
                            var26_27 += var28_29;
lbl340:
                            // 2 sources

                        } while (--var0 >= 0);
                        if (!var36_19) ** GOTO lbl352
                        do {
                            OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var1_1, var4_4 >> 16, var3_3 >> 16, var7_7 >> 8, var6_6 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                            var3_3 += var33_34;
                            var4_4 += var31_32;
                            var6_6 += var34_35;
                            var7_7 += var32_33;
                            var1_1 += AFCKELYG.n;
                            var20_21 += var22_23;
                            var23_24 += var25_26;
                            var26_27 += var28_29;
lbl352:
                            // 2 sources

                        } while (--var2_2 >= 0);
                        return;
                    }
                    if (var2_2 >= AFCKELYG.q) {
                        return;
                    }
                    if (var0 > AFCKELYG.q) {
                        var0 = AFCKELYG.q;
                    }
                    if (var1_1 > AFCKELYG.q) {
                        var1_1 = AFCKELYG.q;
                    }
                    if (var0 >= var1_1) break block56;
                    var4_4 = var5_5 <<= 16;
                    var7_7 = var8_8 <<= 16;
                    if (var2_2 < 0) {
                        var4_4 -= var31_32 * var2_2;
                        var5_5 -= var33_34 * var2_2;
                        var7_7 -= var32_33 * var2_2;
                        var8_8 -= var34_35 * var2_2;
                        var2_2 = 0;
                    }
                    var3_3 <<= 16;
                    var6_6 <<= 16;
                    if (var0 < 0) {
                        var3_3 -= var29_30 * var0;
                        var6_6 -= var30_31 * var0;
                        var0 = 0;
                    }
                    var35_40 = var2_2 - OPPOFIOL.G;
                    var20_21 += var22_23 * var35_40;
                    var23_24 += var25_26 * var35_40;
                    var26_27 += var28_29 * var35_40;
                    if (var31_32 >= var33_34) break block57;
                    var1_1 -= var0;
                    var0 -= var2_2;
                    var2_2 = OPPOFIOL.L[var2_2];
                    if (!var36_19) ** GOTO lbl395
                    do {
                        OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var2_2, var4_4 >> 16, var5_5 >> 16, var7_7 >> 8, var8_8 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                        var4_4 += var31_32;
                        var5_5 += var33_34;
                        var7_7 += var32_33;
                        var8_8 += var34_35;
                        var2_2 += AFCKELYG.n;
                        var20_21 += var22_23;
                        var23_24 += var25_26;
                        var26_27 += var28_29;
lbl395:
                        // 2 sources

                    } while (--var0 >= 0);
                    if (!var36_19) ** GOTO lbl407
                    do {
                        OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var2_2, var4_4 >> 16, var3_3 >> 16, var7_7 >> 8, var6_6 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                        var4_4 += var31_32;
                        var3_3 += var29_30;
                        var7_7 += var32_33;
                        var6_6 += var30_31;
                        var2_2 += AFCKELYG.n;
                        var20_21 += var22_23;
                        var23_24 += var25_26;
                        var26_27 += var28_29;
lbl407:
                        // 2 sources

                    } while (--var1_1 >= 0);
                    return;
                }
                var1_1 -= var0;
                var0 -= var2_2;
                var2_2 = OPPOFIOL.L[var2_2];
                if (!var36_19) ** GOTO lbl424
                do {
                    OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var2_2, var5_5 >> 16, var4_4 >> 16, var8_8 >> 8, var7_7 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                    var4_4 += var31_32;
                    var5_5 += var33_34;
                    var7_7 += var32_33;
                    var8_8 += var34_35;
                    var2_2 += AFCKELYG.n;
                    var20_21 += var22_23;
                    var23_24 += var25_26;
                    var26_27 += var28_29;
lbl424:
                    // 2 sources

                } while (--var0 >= 0);
                if (!var36_19) ** GOTO lbl436
                do {
                    OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var2_2, var3_3 >> 16, var4_4 >> 16, var6_6 >> 8, var7_7 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                    var4_4 += var31_32;
                    var3_3 += var29_30;
                    var7_7 += var32_33;
                    var6_6 += var30_31;
                    var2_2 += AFCKELYG.n;
                    var20_21 += var22_23;
                    var23_24 += var25_26;
                    var26_27 += var28_29;
lbl436:
                    // 2 sources

                } while (--var1_1 >= 0);
                return;
            }
            var3_3 = var5_5 <<= 16;
            var6_6 = var8_8 <<= 16;
            if (var2_2 < 0) {
                var3_3 -= var31_32 * var2_2;
                var5_5 -= var33_34 * var2_2;
                var6_6 -= var32_33 * var2_2;
                var8_8 -= var34_35 * var2_2;
                var2_2 = 0;
            }
            var4_4 <<= 16;
            var7_7 <<= 16;
            if (var1_1 < 0) {
                var4_4 -= var29_30 * var1_1;
                var7_7 -= var30_31 * var1_1;
                var1_1 = 0;
            }
            var35_41 = var2_2 - OPPOFIOL.G;
            var20_21 += var22_23 * var35_41;
            var23_24 += var25_26 * var35_41;
            var26_27 += var28_29 * var35_41;
            if (var31_32 >= var33_34) break block58;
            var0 -= var1_1;
            var1_1 -= var2_2;
            var2_2 = OPPOFIOL.L[var2_2];
            if (!var36_19) ** GOTO lbl472
            do {
                OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var2_2, var3_3 >> 16, var5_5 >> 16, var6_6 >> 8, var8_8 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                var3_3 += var31_32;
                var5_5 += var33_34;
                var6_6 += var32_33;
                var8_8 += var34_35;
                var2_2 += AFCKELYG.n;
                var20_21 += var22_23;
                var23_24 += var25_26;
                var26_27 += var28_29;
lbl472:
                // 2 sources

            } while (--var1_1 >= 0);
            if (!var36_19) ** GOTO lbl484
            do {
                OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var2_2, var4_4 >> 16, var5_5 >> 16, var7_7 >> 8, var8_8 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
                var4_4 += var29_30;
                var5_5 += var33_34;
                var7_7 += var30_31;
                var8_8 += var34_35;
                var2_2 += AFCKELYG.n;
                var20_21 += var22_23;
                var23_24 += var25_26;
                var26_27 += var28_29;
lbl484:
                // 2 sources

            } while (--var0 >= 0);
            return;
        }
        var0 -= var1_1;
        var1_1 -= var2_2;
        var2_2 = OPPOFIOL.L[var2_2];
        if (!var36_19) ** GOTO lbl501
        do {
            OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var2_2, var5_5 >> 16, var3_3 >> 16, var8_8 >> 8, var6_6 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
            var3_3 += var31_32;
            var5_5 += var33_34;
            var6_6 += var32_33;
            var8_8 += var34_35;
            var2_2 += AFCKELYG.n;
            var20_21 += var22_23;
            var23_24 += var25_26;
            var26_27 += var28_29;
lbl501:
            // 2 sources

        } while (--var1_1 >= 0);
        if (!var36_19) ** GOTO lbl513
        do {
            OPPOFIOL.a(AFCKELYG.m, var19_20, 0, 0, var2_2, var5_5 >> 16, var4_4 >> 16, var8_8 >> 8, var7_7 >> 8, var20_21, var23_24, var26_27, var21_22, var24_25, var27_28);
            var4_4 += var29_30;
            var5_5 += var33_34;
            var7_7 += var30_31;
            var8_8 += var34_35;
            var2_2 += AFCKELYG.n;
            var20_21 += var22_23;
            var23_24 += var25_26;
            var26_27 += var28_29;
lbl513:
            // 2 sources

        } while (--var0 >= 0);
    }

    /*
     * Unable to fully structure code
     */
    public static final void a(int[] var0, int[] var1_1, int var2_2, int var3_3, int var4_4, int var5_5, int var6_6, int var7_7, int var8_8, int var9_9, int var10_10, int var11_11, int var12_12, int var13_13, int var14_14) {
        block80: {
            block81: {
                block78: {
                    block79: {
                        block69: {
                            block72: {
                                block73: {
                                    block70: {
                                        block71: {
                                            block66: {
                                                block68: {
                                                    block67: {
                                                        block65: {
                                                            var25_15 = XHHRODPC.l;
                                                            if (var5_5 >= var6_6) {
                                                                return;
                                                            }
                                                            if (!OPPOFIOL.B) break block65;
                                                            var15_16 = (var8_8 - var7_7) / (var6_6 - var5_5);
                                                            if (var6_6 > AFCKELYG.t) {
                                                                var6_6 = AFCKELYG.t;
                                                            }
                                                            if (var5_5 < 0) {
                                                                var7_7 -= var5_5 * var15_16;
                                                                var5_5 = 0;
                                                            }
                                                            if (var5_5 >= var6_6) {
                                                                return;
                                                            }
                                                            var16_17 = var6_6 - var5_5 >> 3;
                                                            var15_16 <<= 12;
                                                            var7_7 <<= 9;
                                                            if (!var25_15) break block66;
                                                        }
                                                        if (var6_6 - var5_5 <= 7) break block67;
                                                        var16_17 = var6_6 - var5_5 >> 3;
                                                        var15_16 = (var8_8 - var7_7) * OPPOFIOL.H[var16_17] >> 6;
                                                        if (!var25_15) break block68;
                                                    }
                                                    var16_17 = 0;
                                                    var15_16 = 0;
                                                }
                                                var7_7 <<= 9;
                                            }
                                            var4_4 += var5_5;
                                            if (!OPPOFIOL.A) break block69;
                                            var17_18 = 0;
                                            var18_20 = 0;
                                            var20_22 = var5_5 - OPPOFIOL.F;
                                            var9_9 += (var12_12 >> 3) * var20_22;
                                            var10_10 += (var13_13 >> 3) * var20_22;
                                            var19_24 = (var11_11 += (var14_14 >> 3) * var20_22) >> 12;
                                            if (var19_24 == 0) break block70;
                                            var2_2 = var9_9 / var19_24;
                                            var3_3 = var10_10 / var19_24;
                                            if (var2_2 >= 0) break block71;
                                            var2_2 = 0;
                                            if (!var25_15) break block70;
                                        }
                                        if (var2_2 > 4032) {
                                            var2_2 = 4032;
                                        }
                                    }
                                    var9_9 += var12_12;
                                    var10_10 += var13_13;
                                    var19_24 = (var11_11 += var14_14) >> 12;
                                    if (var19_24 == 0) break block72;
                                    var17_18 = var9_9 / var19_24;
                                    var18_20 = var10_10 / var19_24;
                                    if (var17_18 >= 7) break block73;
                                    var17_18 = 7;
                                    if (!var25_15) break block72;
                                }
                                if (var17_18 > 4032) {
                                    var17_18 = 4032;
                                }
                            }
                            var21_26 = var17_18 - var2_2 >> 3;
                            var22_28 = var18_20 - var3_3 >> 3;
                            var2_2 += (var7_7 & 0x600000) >> 3;
                            var23_30 = var7_7 >> 23;
                            if (!OPPOFIOL.C) ** GOTO lbl153
                            if (!var25_15) ** GOTO lbl93
                            do {
                                block74: {
                                    block75: {
                                        var0[var4_4++] = var1_1[(var3_3 & 4032) + (var2_2 >> 6)] >>> var23_30;
                                        var0[var4_4++] = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        var0[var4_4++] = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        var0[var4_4++] = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        var0[var4_4++] = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        var0[var4_4++] = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        var0[var4_4++] = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        var0[var4_4++] = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        var2_2 = var17_18;
                                        var3_3 = var18_20;
                                        var9_9 += var12_12;
                                        var10_10 += var13_13;
                                        var19_24 = (var11_11 += var14_14) >> 12;
                                        if (var19_24 == 0) break block74;
                                        var17_18 = var9_9 / var19_24;
                                        var18_20 = var10_10 / var19_24;
                                        if (var17_18 >= 7) break block75;
                                        var17_18 = 7;
                                        if (!var25_15) break block74;
                                    }
                                    if (var17_18 > 4032) {
                                        var17_18 = 4032;
                                    }
                                }
                                var21_26 = var17_18 - var2_2 >> 3;
                                var22_28 = var18_20 - var3_3 >> 3;
                                var2_2 += ((var7_7 += var15_16) & 0x600000) >> 3;
                                var23_30 = var7_7 >> 23;
lbl93:
                                // 2 sources

                            } while (var16_17-- > 0);
                            var16_17 = var6_6 - var5_5 & 7;
                            if (!var25_15) ** GOTO lbl100
                            do {
                                var0[var4_4++] = var1_1[(var3_3 & 4032) + (var2_2 >> 6)] >>> var23_30;
                                var2_2 += var21_26;
                                var3_3 += var22_28;
lbl100:
                                // 2 sources

                            } while (var16_17-- > 0);
                            return;
lbl-1000:
                            // 1 sources

                            {
                                block76: {
                                    block77: {
                                        var24_32 = var1_1[(var3_3 & 4032) + (var2_2 >> 6)] >>> var23_30;
                                        if (var24_32 != 0) {
                                            var0[var4_4] = var24_32;
                                        }
                                        ++var4_4;
                                        var24_32 = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        if (var24_32 != 0) {
                                            var0[var4_4] = var24_32;
                                        }
                                        ++var4_4;
                                        var24_32 = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        if (var24_32 != 0) {
                                            var0[var4_4] = var24_32;
                                        }
                                        ++var4_4;
                                        var24_32 = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        if (var24_32 != 0) {
                                            var0[var4_4] = var24_32;
                                        }
                                        ++var4_4;
                                        var24_32 = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        if (var24_32 != 0) {
                                            var0[var4_4] = var24_32;
                                        }
                                        ++var4_4;
                                        var24_32 = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        if (var24_32 != 0) {
                                            var0[var4_4] = var24_32;
                                        }
                                        ++var4_4;
                                        var24_32 = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        if (var24_32 != 0) {
                                            var0[var4_4] = var24_32;
                                        }
                                        ++var4_4;
                                        var24_32 = var1_1[((var3_3 += var22_28) & 4032) + ((var2_2 += var21_26) >> 6)] >>> var23_30;
                                        if (var24_32 != 0) {
                                            var0[var4_4] = var24_32;
                                        }
                                        ++var4_4;
                                        var2_2 = var17_18;
                                        var3_3 = var18_20;
                                        var9_9 += var12_12;
                                        var10_10 += var13_13;
                                        var19_24 = (var11_11 += var14_14) >> 12;
                                        if (var19_24 == 0) break block76;
                                        var17_18 = var9_9 / var19_24;
                                        var18_20 = var10_10 / var19_24;
                                        if (var17_18 >= 7) break block77;
                                        var17_18 = 7;
                                        if (!var25_15) break block76;
                                    }
                                    if (var17_18 > 4032) {
                                        var17_18 = 4032;
                                    }
                                }
                                var21_26 = var17_18 - var2_2 >> 3;
                                var22_28 = var18_20 - var3_3 >> 3;
                                var2_2 += ((var7_7 += var15_16) & 0x600000) >> 3;
                                var23_30 = var7_7 >> 23;
lbl153:
                                // 2 sources

                                ** while (var16_17-- > 0)
                            }
lbl154:
                            // 1 sources

                            var16_17 = var6_6 - var5_5 & 7;
                            if (!var25_15) ** GOTO lbl162
                            do {
                                if ((var24_32 = var1_1[(var3_3 & 4032) + (var2_2 >> 6)] >>> var23_30) != 0) {
                                    var0[var4_4] = var24_32;
                                }
                                ++var4_4;
                                var2_2 += var21_26;
                                var3_3 += var22_28;
lbl162:
                                // 2 sources

                            } while (var16_17-- > 0);
                            return;
                        }
                        var17_19 = 0;
                        var18_21 = 0;
                        var20_23 = var5_5 - OPPOFIOL.F;
                        var9_9 += (var12_12 >> 3) * var20_23;
                        var10_10 += (var13_13 >> 3) * var20_23;
                        var19_25 = (var11_11 += (var14_14 >> 3) * var20_23) >> 14;
                        if (var19_25 == 0) break block78;
                        var2_2 = var9_9 / var19_25;
                        var3_3 = var10_10 / var19_25;
                        if (var2_2 >= 0) break block79;
                        var2_2 = 0;
                        if (!var25_15) break block78;
                    }
                    if (var2_2 > 16256) {
                        var2_2 = 16256;
                    }
                }
                var9_9 += var12_12;
                var10_10 += var13_13;
                var19_25 = (var11_11 += var14_14) >> 14;
                if (var19_25 == 0) break block80;
                var17_19 = var9_9 / var19_25;
                var18_21 = var10_10 / var19_25;
                if (var17_19 >= 7) break block81;
                var17_19 = 7;
                if (!var25_15) break block80;
            }
            if (var17_19 > 16256) {
                var17_19 = 16256;
            }
        }
        var21_27 = var17_19 - var2_2 >> 3;
        var22_29 = var18_21 - var3_3 >> 3;
        var2_2 += var7_7 & 0x600000;
        var23_31 = var7_7 >> 23;
        if (!OPPOFIOL.C) ** GOTO lbl288
        if (!var25_15) ** GOTO lbl228
        do {
            block82: {
                block83: {
                    var0[var4_4++] = var1_1[(var3_3 & 16256) + (var2_2 >> 7)] >>> var23_31;
                    var0[var4_4++] = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    var0[var4_4++] = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    var0[var4_4++] = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    var0[var4_4++] = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    var0[var4_4++] = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    var0[var4_4++] = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    var0[var4_4++] = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    var2_2 = var17_19;
                    var3_3 = var18_21;
                    var9_9 += var12_12;
                    var10_10 += var13_13;
                    var19_25 = (var11_11 += var14_14) >> 14;
                    if (var19_25 == 0) break block82;
                    var17_19 = var9_9 / var19_25;
                    var18_21 = var10_10 / var19_25;
                    if (var17_19 >= 7) break block83;
                    var17_19 = 7;
                    if (!var25_15) break block82;
                }
                if (var17_19 > 16256) {
                    var17_19 = 16256;
                }
            }
            var21_27 = var17_19 - var2_2 >> 3;
            var22_29 = var18_21 - var3_3 >> 3;
            var2_2 += (var7_7 += var15_16) & 0x600000;
            var23_31 = var7_7 >> 23;
lbl228:
            // 2 sources

        } while (var16_17-- > 0);
        var16_17 = var6_6 - var5_5 & 7;
        if (!var25_15) ** GOTO lbl235
        do {
            var0[var4_4++] = var1_1[(var3_3 & 16256) + (var2_2 >> 7)] >>> var23_31;
            var2_2 += var21_27;
            var3_3 += var22_29;
lbl235:
            // 2 sources

        } while (var16_17-- > 0);
        return;
lbl-1000:
        // 1 sources

        {
            block84: {
                block85: {
                    var24_33 = var1_1[(var3_3 & 16256) + (var2_2 >> 7)] >>> var23_31;
                    if (var24_33 != 0) {
                        var0[var4_4] = var24_33;
                    }
                    ++var4_4;
                    var24_33 = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    if (var24_33 != 0) {
                        var0[var4_4] = var24_33;
                    }
                    ++var4_4;
                    var24_33 = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    if (var24_33 != 0) {
                        var0[var4_4] = var24_33;
                    }
                    ++var4_4;
                    var24_33 = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    if (var24_33 != 0) {
                        var0[var4_4] = var24_33;
                    }
                    ++var4_4;
                    var24_33 = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    if (var24_33 != 0) {
                        var0[var4_4] = var24_33;
                    }
                    ++var4_4;
                    var24_33 = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    if (var24_33 != 0) {
                        var0[var4_4] = var24_33;
                    }
                    ++var4_4;
                    var24_33 = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    if (var24_33 != 0) {
                        var0[var4_4] = var24_33;
                    }
                    ++var4_4;
                    var24_33 = var1_1[((var3_3 += var22_29) & 16256) + ((var2_2 += var21_27) >> 7)] >>> var23_31;
                    if (var24_33 != 0) {
                        var0[var4_4] = var24_33;
                    }
                    ++var4_4;
                    var2_2 = var17_19;
                    var3_3 = var18_21;
                    var9_9 += var12_12;
                    var10_10 += var13_13;
                    var19_25 = (var11_11 += var14_14) >> 14;
                    if (var19_25 == 0) break block84;
                    var17_19 = var9_9 / var19_25;
                    var18_21 = var10_10 / var19_25;
                    if (var17_19 >= 7) break block85;
                    var17_19 = 7;
                    if (!var25_15) break block84;
                }
                if (var17_19 > 16256) {
                    var17_19 = 16256;
                }
            }
            var21_27 = var17_19 - var2_2 >> 3;
            var22_29 = var18_21 - var3_3 >> 3;
            var2_2 += (var7_7 += var15_16) & 0x600000;
            var23_31 = var7_7 >> 23;
lbl288:
            // 2 sources

            ** while (var16_17-- > 0)
        }
lbl289:
        // 1 sources

        var16_17 = var6_6 - var5_5 & 7;
        if (!var25_15) ** GOTO lbl297
        do {
            if ((var24_33 = var1_1[(var3_3 & 16256) + (var2_2 >> 7)] >>> var23_31) != 0) {
                var0[var4_4] = var24_33;
            }
            ++var4_4;
            var2_2 += var21_27;
            var3_3 += var22_29;
lbl297:
            // 2 sources

        } while (var16_17-- > 0);
    }

    static {
        D = true;
        H = new int[512];
        I = new int[2048];
        J = new int[2048];
        K = new int[2048];
        int n = 1;
        while (n < 512) {
            OPPOFIOL.H[n] = 32768 / n;
            ++n;
        }
        int n2 = 1;
        while (n2 < 2048) {
            OPPOFIOL.I[n2] = 65536 / n2;
            ++n2;
        }
        int n3 = 0;
        while (n3 < 2048) {
            OPPOFIOL.J[n3] = (int)(65536.0 * Math.sin((double)n3 * 0.0030679615));
            OPPOFIOL.K[n3] = (int)(65536.0 * Math.cos((double)n3 * 0.0030679615));
            ++n3;
        }
        N = new DSMJIEPN[50];
        O = new boolean[50];
        P = new int[50];
        S = new int[50][];
        T = new int[50];
        V = new int[65536];
        W = new int[50][];
    }
}

