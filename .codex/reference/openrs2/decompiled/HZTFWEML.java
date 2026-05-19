/*
 * Decompiled with CFR 0.152.
 */
public final class HZTFWEML {
    private static QPNUVGRI a = new QPNUVGRI();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static int a(byte[] byArray, int n, byte[] byArray2, int n2, int n3) {
        int n4;
        boolean bl = MBMGIXGO.L;
        QPNUVGRI qPNUVGRI = a;
        synchronized (qPNUVGRI) {
            HZTFWEML.a.j = byArray2;
            HZTFWEML.a.k = n3;
            HZTFWEML.a.o = byArray;
            HZTFWEML.a.p = 0;
            HZTFWEML.a.l = n2;
            HZTFWEML.a.q = n;
            HZTFWEML.a.x = 0;
            HZTFWEML.a.w = 0;
            HZTFWEML.a.m = 0;
            HZTFWEML.a.n = 0;
            HZTFWEML.a.r = 0;
            HZTFWEML.a.s = 0;
            HZTFWEML.a.z = 0;
            HZTFWEML.b(a);
            n4 = n -= HZTFWEML.a.q;
            Object var7_8 = null;
        }
        if (!PKVMXVTO.e) return n4;
        MBMGIXGO.L = !bl;
        return n4;
    }

    /*
     * Unable to fully structure code
     */
    private static void a(QPNUVGRI var0) {
        var15_1 = MBMGIXGO.L;
        var2_2 = var0.t;
        var3_3 = var0.u;
        var4_4 = var0.E;
        var5_5 = var0.C;
        var6_6 = QPNUVGRI.H;
        var7_7 = var0.B;
        var8_8 = var0.o;
        var9_9 = var0.p;
        var11_11 = var10_10 = var0.q;
        var12_12 = var0.V + 1;
        block0: while (true) lbl-1000:
        // 4 sources

        {
            if (var3_3 <= 0) ** GOTO lbl27
            while (var10_10 != 0) {
                block11: {
                    block10: {
                        if (var3_3 != 1) {
                            var8_8[var9_9] = var2_2;
                            --var3_3;
                            ++var9_9;
                            --var10_10;
                            if (!var15_1) continue;
                        }
                        if (var10_10 == 0) {
                            var3_3 = 1;
                            if (!var15_1) break block0;
                        }
                        var8_8[var9_9] = var2_2;
                        ++var9_9;
                        --var10_10;
lbl27:
                        // 2 sources

                        var14_14 = true;
                        if (!var15_1) ** GOTO lbl57
                        do {
                            var14_14 = false;
                            if (var4_4 == var12_12) {
                                var3_3 = 0;
                                if (!var15_1) break block0;
                            }
                            var2_2 = (byte)var5_5;
                            var7_7 = var6_6[var7_7];
                            var1_13 = (byte)(var7_7 & 255);
                            var7_7 >>= 8;
                            ++var4_4;
                            if (var1_13 != var5_5) {
                                var5_5 = var1_13;
                                if (var10_10 == 0) {
                                    var3_3 = 1;
                                    if (!var15_1) break block0;
                                }
                                var8_8[var9_9] = var2_2;
                                ++var9_9;
                                --var10_10;
                                var14_14 = true;
                                if (!var15_1) continue;
                            }
                            if (var4_4 != var12_12) continue;
                            if (var10_10 == 0) {
                                var3_3 = 1;
                                if (!var15_1) break block0;
                            }
                            var8_8[var9_9] = var2_2;
                            ++var9_9;
                            --var10_10;
                            var14_14 = true;
lbl57:
                            // 4 sources

                        } while (var14_14);
                        var3_3 = 2;
                        var7_7 = var6_6[var7_7];
                        var1_13 = (byte)(var7_7 & 255);
                        var7_7 >>= 8;
                        if (++var4_4 == var12_12) continue block0;
                        if (var1_13 == var5_5) break block10;
                        var5_5 = var1_13;
                        if (!var15_1) ** GOTO lbl-1000
                    }
                    var3_3 = 3;
                    var7_7 = var6_6[var7_7];
                    var1_13 = (byte)(var7_7 & 255);
                    var7_7 >>= 8;
                    if (++var4_4 == var12_12) continue block0;
                    if (var1_13 == var5_5) break block11;
                    var5_5 = var1_13;
                    if (!var15_1) ** GOTO lbl-1000
                }
                var7_7 = var6_6[var7_7];
                var1_13 = (byte)(var7_7 & 255);
                var7_7 >>= 8;
                ++var4_4;
                var3_3 = (var1_13 & 255) + 4;
                var7_7 = var6_6[var7_7];
                var5_5 = (byte)(var7_7 & 255);
                var7_7 >>= 8;
                ++var4_4;
                if (!var15_1) continue block0;
            }
            break;
        }
        var13_15 = var0.r;
        var0.r += var11_11 - var10_10;
        if (var0.r < var13_15) {
            ++var0.s;
        }
        var0.t = var2_2;
        var0.u = var3_3;
        var0.E = var4_4;
        var0.C = var5_5;
        QPNUVGRI.H = var6_6;
        var0.B = var7_7;
        var0.o = var8_8;
        var0.p = var9_9;
        var0.q = var10_10;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private static void b(QPNUVGRI qPNUVGRI) {
        boolean bl = MBMGIXGO.L;
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        int n13 = 0;
        int n14 = 0;
        int n15 = 0;
        int n16 = 0;
        int n17 = 0;
        byte by = 0;
        byte by2 = 0;
        int n18 = 0;
        int[] nArray = null;
        int[] nArray2 = null;
        int[] nArray3 = null;
        qPNUVGRI.y = 1;
        if (QPNUVGRI.H == null) {
            QPNUVGRI.H = new int[qPNUVGRI.y * 100000];
        }
        boolean bl2 = true;
        while (bl2) {
            byte by3;
            block62: {
                block61: {
                    by3 = HZTFWEML.c(qPNUVGRI);
                    if (by3 == 23) {
                        return;
                    }
                    by3 = HZTFWEML.c(qPNUVGRI);
                    by3 = HZTFWEML.c(qPNUVGRI);
                    by3 = HZTFWEML.c(qPNUVGRI);
                    by3 = HZTFWEML.c(qPNUVGRI);
                    by3 = HZTFWEML.c(qPNUVGRI);
                    ++qPNUVGRI.z;
                    by3 = HZTFWEML.c(qPNUVGRI);
                    by3 = HZTFWEML.c(qPNUVGRI);
                    by3 = HZTFWEML.c(qPNUVGRI);
                    by3 = HZTFWEML.c(qPNUVGRI);
                    by3 = HZTFWEML.d(qPNUVGRI);
                    if (by3 == 0) break block61;
                    qPNUVGRI.v = true;
                    if (!bl) break block62;
                }
                qPNUVGRI.v = false;
            }
            if (qPNUVGRI.v) {
                System.out.println("PANIC! RANDOMISED BLOCK!");
            }
            qPNUVGRI.A = 0;
            by3 = HZTFWEML.c(qPNUVGRI);
            qPNUVGRI.A = qPNUVGRI.A << 8 | by3 & 0xFF;
            by3 = HZTFWEML.c(qPNUVGRI);
            qPNUVGRI.A = qPNUVGRI.A << 8 | by3 & 0xFF;
            by3 = HZTFWEML.c(qPNUVGRI);
            qPNUVGRI.A = qPNUVGRI.A << 8 | by3 & 0xFF;
            n = 0;
            boolean bl3 = true;
            do {
                block64: {
                    block63: {
                        if (bl3 && !(bl3 = false) && !bl) continue;
                        by3 = HZTFWEML.d(qPNUVGRI);
                        if (by3 != 1) break block63;
                        qPNUVGRI.K[n] = true;
                        if (!bl) break block64;
                    }
                    qPNUVGRI.K[n] = false;
                }
                ++n;
            } while (n < 16);
            n = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl) continue;
                qPNUVGRI.J[n] = false;
                ++n;
            } while (n < 256);
            n = 0;
            boolean bl5 = true;
            do {
                if (bl5 && !(bl5 = false) && !bl) continue;
                if (qPNUVGRI.K[n]) {
                    n2 = 0;
                    boolean bl6 = true;
                    do {
                        if (bl6 && !(bl6 = false) && !bl) continue;
                        by3 = HZTFWEML.d(qPNUVGRI);
                        if (by3 == 1) {
                            qPNUVGRI.J[n * 16 + n2] = true;
                        }
                        ++n2;
                    } while (n2 < 16);
                }
                ++n;
            } while (n < 16);
            HZTFWEML.e(qPNUVGRI);
            n4 = qPNUVGRI.I + 2;
            n5 = HZTFWEML.a(3, qPNUVGRI);
            n6 = HZTFWEML.a(15, qPNUVGRI);
            n = 0;
            boolean bl7 = true;
            do {
                if (bl7 && !(bl7 = false) && !bl) continue;
                n2 = 0;
                while ((by3 = HZTFWEML.d(qPNUVGRI)) != 0) {
                    ++n2;
                    if (!bl) continue;
                }
                qPNUVGRI.P[n] = (byte)n2;
                ++n;
            } while (n < n6);
            byte[] byArray = new byte[6];
            int n19 = 0;
            boolean bl8 = true;
            do {
                if (bl8 && !(bl8 = false) && !bl) continue;
                byArray[n19] = n19;
                n19 = (byte)(n19 + 1);
            } while (n19 < n5);
            n = 0;
            boolean bl9 = true;
            do {
                if (bl9 && !(bl9 = false) && !bl) continue;
                n19 = qPNUVGRI.P[n];
                byte by4 = byArray[n19];
                boolean bl10 = true;
                do {
                    if (bl10 && !(bl10 = false) && !bl) continue;
                    byArray[n19] = byArray[n19 - 1];
                    n19 = (byte)(n19 - 1);
                } while (n19 > 0);
                byArray[0] = by4;
                qPNUVGRI.O[n] = by4;
                ++n;
            } while (n < n6);
            n3 = 0;
            boolean bl11 = true;
            do {
                if (bl11 && !(bl11 = false) && !bl) continue;
                n15 = HZTFWEML.a(5, qPNUVGRI);
                n = 0;
                boolean bl12 = true;
                while (true) {
                    if (!bl12 || (bl12 = false) || bl) {
                        by3 = HZTFWEML.d(qPNUVGRI);
                        if (by3 != 0) {
                            by3 = HZTFWEML.d(qPNUVGRI);
                            if (by3 == 0) {
                                ++n15;
                                if (!bl) continue;
                            }
                            --n15;
                            if (!bl) continue;
                        }
                        qPNUVGRI.Q[n3][n] = (byte)n15;
                        ++n;
                    }
                    if (n >= n4) break;
                }
                ++n3;
            } while (n3 < n5);
            n3 = 0;
            boolean bl13 = true;
            do {
                if (bl13 && !(bl13 = false) && !bl) continue;
                int n20 = 32;
                byte by5 = 0;
                n = 0;
                boolean bl14 = true;
                do {
                    if (bl14 && !(bl14 = false) && !bl) continue;
                    if (qPNUVGRI.Q[n3][n] > by5) {
                        by5 = qPNUVGRI.Q[n3][n];
                    }
                    if (qPNUVGRI.Q[n3][n] < n20) {
                        n20 = qPNUVGRI.Q[n3][n];
                    }
                    ++n;
                } while (n < n4);
                HZTFWEML.a(qPNUVGRI.R[n3], qPNUVGRI.S[n3], qPNUVGRI.T[n3], qPNUVGRI.Q[n3], n20, by5, n4);
                qPNUVGRI.U[n3] = n20;
                ++n3;
            } while (n3 < n5);
            n7 = qPNUVGRI.I + 1;
            n11 = 100000 * qPNUVGRI.y;
            n8 = -1;
            n9 = 0;
            n = 0;
            boolean bl15 = true;
            do {
                if (bl15 && !(bl15 = false) && !bl) continue;
                qPNUVGRI.D[n] = 0;
                ++n;
            } while (n <= 255);
            int n21 = 4095;
            int n22 = 15;
            boolean bl16 = true;
            do {
                if (bl16 && !(bl16 = false) && !bl) continue;
                int n23 = 15;
                boolean bl17 = true;
                do {
                    if (bl17 && !(bl17 = false) && !bl) continue;
                    qPNUVGRI.M[n21] = (byte)(n22 * 16 + n23);
                    --n21;
                    --n23;
                } while (n23 >= 0);
                qPNUVGRI.N[n22] = n21 + 1;
                --n22;
            } while (n22 >= 0);
            n12 = 0;
            if (n9 == 0) {
                n9 = 50;
                by2 = qPNUVGRI.O[++n8];
                n18 = qPNUVGRI.U[by2];
                nArray = qPNUVGRI.R[by2];
                nArray3 = qPNUVGRI.T[by2];
                nArray2 = qPNUVGRI.S[by2];
            }
            --n9;
            n16 = n18;
            n17 = HZTFWEML.a(n16, qPNUVGRI);
            while (n17 > nArray[n16]) {
                ++n16;
                by = HZTFWEML.d(qPNUVGRI);
                n17 = n17 << 1 | by;
                if (!bl) continue;
            }
            n10 = nArray3[n17 - nArray2[n16]];
            while (n10 != n7) {
                block69: {
                    int n24;
                    int n25;
                    block68: {
                        block65: {
                            if (n10 != 0 && n10 != 1) break block65;
                            n13 = -1;
                            n14 = 1;
                            do {
                                block67: {
                                    block66: {
                                        if (n10 != 0) break block66;
                                        n13 += n14;
                                        if (!bl) break block67;
                                    }
                                    if (n10 == 1) {
                                        n13 += 2 * n14;
                                    }
                                }
                                n14 *= 2;
                                if (n9 == 0) {
                                    n9 = 50;
                                    by2 = qPNUVGRI.O[++n8];
                                    n18 = qPNUVGRI.U[by2];
                                    nArray = qPNUVGRI.R[by2];
                                    nArray3 = qPNUVGRI.T[by2];
                                    nArray2 = qPNUVGRI.S[by2];
                                }
                                --n9;
                                n16 = n18;
                                n17 = HZTFWEML.a(n16, qPNUVGRI);
                                while (n17 > nArray[n16]) {
                                    ++n16;
                                    by = HZTFWEML.d(qPNUVGRI);
                                    n17 = n17 << 1 | by;
                                    if (!bl) continue;
                                }
                            } while ((n10 = nArray3[n17 - nArray2[n16]]) == 0 || n10 == 1);
                            by3 = qPNUVGRI.L[qPNUVGRI.M[qPNUVGRI.N[0]] & 0xFF];
                            int n26 = by3 & 0xFF;
                            qPNUVGRI.D[n26] = qPNUVGRI.D[n26] + ++n13;
                            boolean bl18 = true;
                            do {
                                if (bl18 && !(bl18 = false) && !bl) continue;
                                QPNUVGRI.H[n12] = by3 & 0xFF;
                                ++n12;
                                --n13;
                            } while (n13 > 0);
                            if (!bl) continue;
                        }
                        if ((n25 = n10 - 1) >= 16) break block68;
                        n24 = qPNUVGRI.N[0];
                        by3 = qPNUVGRI.M[n24 + n25];
                        boolean bl19 = true;
                        do {
                            if (bl19 && !(bl19 = false) && !bl) continue;
                            int n27 = n24 + n25;
                            qPNUVGRI.M[n27] = qPNUVGRI.M[n27 - 1];
                            qPNUVGRI.M[n27 - 1] = qPNUVGRI.M[n27 - 2];
                            qPNUVGRI.M[n27 - 2] = qPNUVGRI.M[n27 - 3];
                            qPNUVGRI.M[n27 - 3] = qPNUVGRI.M[n27 - 4];
                            n25 -= 4;
                        } while (n25 > 3);
                        boolean bl20 = true;
                        do {
                            if (bl20 && !(bl20 = false) && !bl) continue;
                            qPNUVGRI.M[n24 + n25] = qPNUVGRI.M[n24 + n25 - 1];
                            --n25;
                        } while (n25 > 0);
                        qPNUVGRI.M[n24] = by3;
                        if (!bl) break block69;
                    }
                    int n28 = n25 / 16;
                    int n29 = n25 % 16;
                    n24 = qPNUVGRI.N[n28] + n29;
                    by3 = qPNUVGRI.M[n24];
                    boolean bl21 = true;
                    do {
                        if (bl21 && !(bl21 = false) && !bl) continue;
                        qPNUVGRI.M[n24] = qPNUVGRI.M[n24 - 1];
                        --n24;
                    } while (n24 > qPNUVGRI.N[n28]);
                    int n30 = n28;
                    qPNUVGRI.N[n30] = qPNUVGRI.N[n30] + 1;
                    boolean bl22 = true;
                    do {
                        if (bl22 && !(bl22 = false) && !bl) continue;
                        int n31 = n28;
                        qPNUVGRI.N[n31] = qPNUVGRI.N[n31] - 1;
                        qPNUVGRI.M[qPNUVGRI.N[n28]] = qPNUVGRI.M[qPNUVGRI.N[n28 - 1] + 16 - 1];
                        --n28;
                    } while (n28 > 0);
                    qPNUVGRI.N[0] = qPNUVGRI.N[0] - 1;
                    qPNUVGRI.M[qPNUVGRI.N[0]] = by3;
                    if (qPNUVGRI.N[0] == 0) {
                        int n32 = 4095;
                        int n33 = 15;
                        boolean bl23 = true;
                        do {
                            if (bl23 && !(bl23 = false) && !bl) continue;
                            int n34 = 15;
                            boolean bl24 = true;
                            do {
                                if (bl24 && !(bl24 = false) && !bl) continue;
                                qPNUVGRI.M[n32] = qPNUVGRI.M[qPNUVGRI.N[n33] + n34];
                                --n32;
                                --n34;
                            } while (n34 >= 0);
                            qPNUVGRI.N[n33] = n32 + 1;
                            --n33;
                        } while (n33 >= 0);
                    }
                }
                int n35 = qPNUVGRI.L[by3 & 0xFF] & 0xFF;
                qPNUVGRI.D[n35] = qPNUVGRI.D[n35] + 1;
                QPNUVGRI.H[n12] = qPNUVGRI.L[by3 & 0xFF] & 0xFF;
                ++n12;
                if (n9 == 0) {
                    n9 = 50;
                    by2 = qPNUVGRI.O[++n8];
                    n18 = qPNUVGRI.U[by2];
                    nArray = qPNUVGRI.R[by2];
                    nArray3 = qPNUVGRI.T[by2];
                    nArray2 = qPNUVGRI.S[by2];
                }
                --n9;
                n16 = n18;
                n17 = HZTFWEML.a(n16, qPNUVGRI);
                while (n17 > nArray[n16]) {
                    ++n16;
                    by = HZTFWEML.d(qPNUVGRI);
                    n17 = n17 << 1 | by;
                    if (!bl) continue;
                }
                n10 = nArray3[n17 - nArray2[n16]];
                if (!bl) continue;
            }
            qPNUVGRI.u = 0;
            qPNUVGRI.t = 0;
            qPNUVGRI.F[0] = 0;
            n = 1;
            boolean bl25 = true;
            do {
                if (bl25 && !(bl25 = false) && !bl) continue;
                qPNUVGRI.F[n] = qPNUVGRI.D[n - 1];
                ++n;
            } while (n <= 256);
            n = 1;
            boolean bl26 = true;
            do {
                if (bl26 && !(bl26 = false) && !bl) continue;
                int n36 = n;
                qPNUVGRI.F[n36] = qPNUVGRI.F[n36] + qPNUVGRI.F[n - 1];
                ++n;
            } while (n <= 256);
            n = 0;
            boolean bl27 = true;
            do {
                if (bl27 && !(bl27 = false) && !bl) continue;
                by3 = (byte)(QPNUVGRI.H[n] & 0xFF);
                int n37 = qPNUVGRI.F[by3 & 0xFF];
                QPNUVGRI.H[n37] = QPNUVGRI.H[n37] | n << 8;
                int n38 = by3 & 0xFF;
                qPNUVGRI.F[n38] = qPNUVGRI.F[n38] + 1;
                ++n;
            } while (n < n12);
            qPNUVGRI.B = QPNUVGRI.H[qPNUVGRI.A] >> 8;
            qPNUVGRI.E = 0;
            qPNUVGRI.B = QPNUVGRI.H[qPNUVGRI.B];
            qPNUVGRI.C = (byte)(qPNUVGRI.B & 0xFF);
            qPNUVGRI.B >>= 8;
            ++qPNUVGRI.E;
            qPNUVGRI.V = n12;
            HZTFWEML.a(qPNUVGRI);
            if (qPNUVGRI.E != qPNUVGRI.V + 1) return;
            if (qPNUVGRI.u != 0) return;
            bl2 = true;
            if (!bl) continue;
            return;
        }
    }

    private static byte c(QPNUVGRI qPNUVGRI) {
        return (byte)HZTFWEML.a(8, qPNUVGRI);
    }

    private static byte d(QPNUVGRI qPNUVGRI) {
        return (byte)HZTFWEML.a(1, qPNUVGRI);
    }

    private static int a(int n, QPNUVGRI qPNUVGRI) {
        int n2;
        while (true) {
            if (qPNUVGRI.x >= n) {
                int n3 = qPNUVGRI.w >> qPNUVGRI.x - n & (1 << n) - 1;
                qPNUVGRI.x -= n;
                n2 = n3;
                if (!MBMGIXGO.L) break;
            }
            qPNUVGRI.w = qPNUVGRI.w << 8 | qPNUVGRI.j[qPNUVGRI.k] & 0xFF;
            qPNUVGRI.x += 8;
            ++qPNUVGRI.k;
            --qPNUVGRI.l;
            ++qPNUVGRI.m;
            if (qPNUVGRI.m != 0) continue;
            ++qPNUVGRI.n;
        }
        return n2;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private static void e(QPNUVGRI qPNUVGRI) {
        qPNUVGRI.I = 0;
        int n = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && !MBMGIXGO.L) continue;
            if (qPNUVGRI.J[n]) {
                qPNUVGRI.L[qPNUVGRI.I] = (byte)n;
                ++qPNUVGRI.I;
            }
            ++n;
        } while (n < 256);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private static void a(int[] nArray, int[] nArray2, int[] nArray3, byte[] byArray, int n, int n2, int n3) {
        boolean bl = MBMGIXGO.L;
        int n4 = 0;
        int n5 = n;
        boolean bl2 = true;
        do {
            if (bl2 && !(bl2 = false) && !bl) continue;
            int n6 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                if (byArray[n6] == n5) {
                    nArray3[n4] = n6;
                    ++n4;
                }
                ++n6;
            } while (n6 < n3);
            ++n5;
        } while (n5 <= n2);
        n5 = 0;
        boolean bl4 = true;
        do {
            if (bl4 && !(bl4 = false) && !bl) continue;
            nArray2[n5] = 0;
            ++n5;
        } while (n5 < 23);
        n5 = 0;
        boolean bl5 = true;
        do {
            if (bl5 && !(bl5 = false) && !bl) continue;
            int n7 = byArray[n5] + 1;
            nArray2[n7] = nArray2[n7] + 1;
            ++n5;
        } while (n5 < n3);
        n5 = 1;
        boolean bl6 = true;
        do {
            if (bl6 && !(bl6 = false) && !bl) continue;
            int n8 = n5;
            nArray2[n8] = nArray2[n8] + nArray2[n5 - 1];
            ++n5;
        } while (n5 < 23);
        n5 = 0;
        boolean bl7 = true;
        do {
            if (bl7 && !(bl7 = false) && !bl) continue;
            nArray[n5] = 0;
            ++n5;
        } while (n5 < 23);
        int n9 = 0;
        n5 = n;
        boolean bl8 = true;
        do {
            if (bl8 && !(bl8 = false) && !bl) continue;
            nArray[n5] = (n9 += nArray2[n5 + 1] - nArray2[n5]) - 1;
            n9 <<= 1;
            ++n5;
        } while (n5 <= n2);
        n5 = n + 1;
        boolean bl9 = true;
        do {
            if (bl9 && !(bl9 = false) && !bl) continue;
            nArray2[n5] = (nArray[n5 - 1] + 1 << 1) - nArray2[n5];
            ++n5;
        } while (n5 <= n2);
    }
}

