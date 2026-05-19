/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

final class VBAXKVMG {
    int[] a;
    int[] b;
    int[] c;
    int[] d;
    int[] e;
    int[] f;
    int[] g;
    int[] h;
    int[] i;
    int[] j;
    boolean k;
    int l;
    int m;
    int n;
    int o;
    static int[] p = new int[6];
    static int[] q = new int[6];
    static int[] r = new int[6];
    static int[] s = new int[6];
    static int[] t = new int[6];
    static int[] u;
    static int[] v;
    static int[] w;
    static final int[][] x;
    static final int[][] y;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public VBAXKVMG(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12, int n13, int n14, int n15, int n16, int n17, int n18, int n19, int n20) {
        block29: {
            boolean bl = XHHRODPC.l;
            this.k = true;
            try {
                int n21;
                int n22;
                int n23;
                int n24;
                int n25;
                int n26;
                if (n13 != n12 || n13 != n4 || n13 != n11) {
                    this.k = false;
                }
                this.l = n14;
                this.m = n7;
                this.n = n9;
                this.o = n20;
                int n27 = 128;
                int n28 = n27 / 2;
                int n29 = n27 / 4;
                int n30 = n27 * 3 / 4;
                int[] nArray = x[n14];
                int n31 = nArray.length;
                this.a = new int[n31];
                this.b = new int[n31];
                this.c = new int[n31];
                int[] nArray2 = new int[n31];
                int[] nArray3 = new int[n31];
                int n32 = n19 * n27;
                int n33 = n * n27;
                int n34 = 0;
                boolean bl2 = true;
                do {
                    block31: {
                        block45: {
                            int n35;
                            block44: {
                                block43: {
                                    block42: {
                                        block41: {
                                            block40: {
                                                block39: {
                                                    block38: {
                                                        block37: {
                                                            block36: {
                                                                block35: {
                                                                    block34: {
                                                                        block33: {
                                                                            block32: {
                                                                                block30: {
                                                                                    if (bl2 && !(bl2 = false) && !bl) continue;
                                                                                    n35 = nArray[n34];
                                                                                    if ((n35 & 1) == 0 && n35 <= 8) {
                                                                                        n35 = (n35 - n7 - n7 - 1 & 7) + 1;
                                                                                    }
                                                                                    if (n35 > 8 && n35 <= 12) {
                                                                                        n35 = (n35 - 9 - n7 & 3) + 9;
                                                                                    }
                                                                                    if (n35 > 12 && n35 <= 16) {
                                                                                        n35 = (n35 - 13 - n7 & 3) + 13;
                                                                                    }
                                                                                    if (n35 != 1) break block30;
                                                                                    n26 = n32;
                                                                                    n25 = n33;
                                                                                    n24 = n13;
                                                                                    n23 = n8;
                                                                                    n22 = n2;
                                                                                    if (!bl) break block31;
                                                                                }
                                                                                if (n35 != 2) break block32;
                                                                                n26 = n32 + n28;
                                                                                n25 = n33;
                                                                                n24 = n13 + n12 >> 1;
                                                                                n23 = n8 + n17 >> 1;
                                                                                n22 = n2 + n16 >> 1;
                                                                                if (!bl) break block31;
                                                                            }
                                                                            if (n35 != 3) break block33;
                                                                            n26 = n32 + n27;
                                                                            n25 = n33;
                                                                            n24 = n12;
                                                                            n23 = n17;
                                                                            n22 = n16;
                                                                            if (!bl) break block31;
                                                                        }
                                                                        if (n35 != 4) break block34;
                                                                        n26 = n32 + n27;
                                                                        n25 = n33 + n28;
                                                                        n24 = n12 + n4 >> 1;
                                                                        n23 = n17 + n10 >> 1;
                                                                        n22 = n16 + n6 >> 1;
                                                                        if (!bl) break block31;
                                                                    }
                                                                    if (n35 != 5) break block35;
                                                                    n26 = n32 + n27;
                                                                    n25 = n33 + n27;
                                                                    n24 = n4;
                                                                    n23 = n10;
                                                                    n22 = n6;
                                                                    if (!bl) break block31;
                                                                }
                                                                if (n35 != 6) break block36;
                                                                n26 = n32 + n28;
                                                                n25 = n33 + n27;
                                                                n24 = n4 + n11 >> 1;
                                                                n23 = n10 + n3 >> 1;
                                                                n22 = n6 + n15 >> 1;
                                                                if (!bl) break block31;
                                                            }
                                                            if (n35 != 7) break block37;
                                                            n26 = n32;
                                                            n25 = n33 + n27;
                                                            n24 = n11;
                                                            n23 = n3;
                                                            n22 = n15;
                                                            if (!bl) break block31;
                                                        }
                                                        if (n35 != 8) break block38;
                                                        n26 = n32;
                                                        n25 = n33 + n28;
                                                        n24 = n11 + n13 >> 1;
                                                        n23 = n3 + n8 >> 1;
                                                        n22 = n15 + n2 >> 1;
                                                        if (!bl) break block31;
                                                    }
                                                    if (n35 != 9) break block39;
                                                    n26 = n32 + n28;
                                                    n25 = n33 + n29;
                                                    n24 = n13 + n12 >> 1;
                                                    n23 = n8 + n17 >> 1;
                                                    n22 = n2 + n16 >> 1;
                                                    if (!bl) break block31;
                                                }
                                                if (n35 != 10) break block40;
                                                n26 = n32 + n30;
                                                n25 = n33 + n28;
                                                n24 = n12 + n4 >> 1;
                                                n23 = n17 + n10 >> 1;
                                                n22 = n16 + n6 >> 1;
                                                if (!bl) break block31;
                                            }
                                            if (n35 != 11) break block41;
                                            n26 = n32 + n28;
                                            n25 = n33 + n30;
                                            n24 = n4 + n11 >> 1;
                                            n23 = n10 + n3 >> 1;
                                            n22 = n6 + n15 >> 1;
                                            if (!bl) break block31;
                                        }
                                        if (n35 != 12) break block42;
                                        n26 = n32 + n29;
                                        n25 = n33 + n28;
                                        n24 = n11 + n13 >> 1;
                                        n23 = n3 + n8 >> 1;
                                        n22 = n15 + n2 >> 1;
                                        if (!bl) break block31;
                                    }
                                    if (n35 != 13) break block43;
                                    n26 = n32 + n29;
                                    n25 = n33 + n29;
                                    n24 = n13;
                                    n23 = n8;
                                    n22 = n2;
                                    if (!bl) break block31;
                                }
                                if (n35 != 14) break block44;
                                n26 = n32 + n30;
                                n25 = n33 + n29;
                                n24 = n12;
                                n23 = n17;
                                n22 = n16;
                                if (!bl) break block31;
                            }
                            if (n35 != 15) break block45;
                            n26 = n32 + n30;
                            n25 = n33 + n30;
                            n24 = n4;
                            n23 = n10;
                            n22 = n6;
                            if (!bl) break block31;
                        }
                        n26 = n32 + n29;
                        n25 = n33 + n30;
                        n24 = n11;
                        n23 = n3;
                        n22 = n15;
                    }
                    this.a[n34] = n26;
                    this.b[n34] = n24;
                    this.c[n34] = n25;
                    nArray2[n34] = n23;
                    nArray3[n34] = n22;
                    ++n34;
                } while (n34 < n31);
                int[] nArray4 = y[n14];
                n26 = nArray4.length / 4;
                this.g = new int[n26];
                this.h = new int[n26];
                this.i = new int[n26];
                this.d = new int[n26];
                this.e = new int[n26];
                this.f = new int[n26];
                if (n5 != -1) {
                    this.j = new int[n26];
                }
                n25 = 0;
                n24 = 0;
                boolean bl3 = true;
                do {
                    block47: {
                        int n36;
                        block46: {
                            if (bl3 && !(bl3 = false) && !bl) continue;
                            n23 = nArray4[n25];
                            n22 = nArray4[n25 + 1];
                            n21 = nArray4[n25 + 2];
                            n36 = nArray4[n25 + 3];
                            n25 += 4;
                            if (n22 < 4) {
                                n22 = n22 - n7 & 3;
                            }
                            if (n21 < 4) {
                                n21 = n21 - n7 & 3;
                            }
                            if (n36 < 4) {
                                n36 = n36 - n7 & 3;
                            }
                            this.g[n24] = n22;
                            this.h[n24] = n21;
                            this.i[n24] = n36;
                            if (n23 != 0) break block46;
                            this.d[n24] = nArray2[n22];
                            this.e[n24] = nArray2[n21];
                            this.f[n24] = nArray2[n36];
                            if (this.j == null) break block47;
                            this.j[n24] = -1;
                            if (!bl) break block47;
                        }
                        this.d[n24] = nArray3[n22];
                        this.e[n24] = nArray3[n21];
                        this.f[n24] = nArray3[n36];
                        if (this.j != null) {
                            this.j[n24] = n5;
                        }
                    }
                    ++n24;
                } while (n24 < n26);
                n23 = n13;
                n22 = n12;
                if (n12 < n23) {
                    n23 = n12;
                }
                if (n12 > n22) {
                    n22 = n12;
                }
                if (n4 < n23) {
                    n23 = n4;
                }
                if (n4 > n22) {
                    n22 = n4;
                }
                if (n11 < n23) {
                    n23 = n11;
                }
                if (n11 > n22) {
                    n22 = n11;
                }
                n23 /= 14;
                n22 /= 14;
                if (n18 < 3 || n18 > 3) {
                    n21 = 1;
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && !bl) continue;
                        ++n21;
                    } while (n21 > 0);
                }
                if (!PKVMXVTO.e) break block29;
                XHHRODPC.l = !bl;
            }
            catch (RuntimeException runtimeException) {
                signlink.reporterror("18048, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + n9 + ", " + n10 + ", " + n11 + ", " + n12 + ", " + n13 + ", " + n14 + ", " + n15 + ", " + n16 + ", " + n17 + ", " + n18 + ", " + n19 + ", " + n20 + ", " + runtimeException.toString());
                throw new RuntimeException();
            }
        }
    }

    static {
        int[] nArray = new int[2];
        nArray[0] = 1;
        u = nArray;
        v = new int[]{2, 1};
        w = new int[]{3, 3};
        x = new int[][]{{1, 3, 5, 7}, {1, 3, 5, 7}, {1, 3, 5, 7}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 2, 6}, {1, 3, 5, 7, 2, 8}, {1, 3, 5, 7, 2, 8}, {1, 3, 5, 7, 11, 12}, {1, 3, 5, 7, 11, 12}, {1, 3, 5, 7, 13, 14}};
        int[][] nArrayArray = new int[13][];
        int[] nArray2 = new int[8];
        nArray2[1] = 1;
        nArray2[2] = 2;
        nArray2[3] = 3;
        nArray2[6] = 1;
        nArray2[7] = 3;
        nArrayArray[0] = nArray2;
        int[] nArray3 = new int[8];
        nArray3[0] = 1;
        nArray3[1] = 1;
        nArray3[2] = 2;
        nArray3[3] = 3;
        nArray3[4] = 1;
        nArray3[6] = 1;
        nArray3[7] = 3;
        nArrayArray[1] = nArray3;
        int[] nArray4 = new int[8];
        nArray4[1] = 1;
        nArray4[2] = 2;
        nArray4[3] = 3;
        nArray4[4] = 1;
        nArray4[6] = 1;
        nArray4[7] = 3;
        nArrayArray[2] = nArray4;
        int[] nArray5 = new int[12];
        nArray5[2] = 1;
        nArray5[3] = 2;
        nArray5[6] = 2;
        nArray5[7] = 4;
        nArray5[8] = 1;
        nArray5[10] = 4;
        nArray5[11] = 3;
        nArrayArray[3] = nArray5;
        int[] nArray6 = new int[12];
        nArray6[2] = 1;
        nArray6[3] = 4;
        nArray6[6] = 4;
        nArray6[7] = 3;
        nArray6[8] = 1;
        nArray6[9] = 1;
        nArray6[10] = 2;
        nArray6[11] = 4;
        nArrayArray[4] = nArray6;
        int[] nArray7 = new int[12];
        nArray7[2] = 4;
        nArray7[3] = 3;
        nArray7[4] = 1;
        nArray7[6] = 1;
        nArray7[7] = 2;
        nArray7[8] = 1;
        nArray7[10] = 2;
        nArray7[11] = 4;
        nArrayArray[5] = nArray7;
        int[] nArray8 = new int[12];
        nArray8[1] = 1;
        nArray8[2] = 2;
        nArray8[3] = 4;
        nArray8[4] = 1;
        nArray8[6] = 1;
        nArray8[7] = 4;
        nArray8[8] = 1;
        nArray8[10] = 4;
        nArray8[11] = 3;
        nArrayArray[6] = nArray8;
        int[] nArray9 = new int[16];
        nArray9[1] = 4;
        nArray9[2] = 1;
        nArray9[3] = 2;
        nArray9[5] = 4;
        nArray9[6] = 2;
        nArray9[7] = 5;
        nArray9[8] = 1;
        nArray9[10] = 4;
        nArray9[11] = 5;
        nArray9[12] = 1;
        nArray9[14] = 5;
        nArray9[15] = 3;
        nArrayArray[7] = nArray9;
        int[] nArray10 = new int[16];
        nArray10[1] = 4;
        nArray10[2] = 1;
        nArray10[3] = 2;
        nArray10[5] = 4;
        nArray10[6] = 2;
        nArray10[7] = 3;
        nArray10[9] = 4;
        nArray10[10] = 3;
        nArray10[11] = 5;
        nArray10[12] = 1;
        nArray10[14] = 4;
        nArray10[15] = 5;
        nArrayArray[8] = nArray10;
        int[] nArray11 = new int[16];
        nArray11[2] = 4;
        nArray11[3] = 5;
        nArray11[4] = 1;
        nArray11[5] = 4;
        nArray11[6] = 1;
        nArray11[7] = 2;
        nArray11[8] = 1;
        nArray11[9] = 4;
        nArray11[10] = 2;
        nArray11[11] = 3;
        nArray11[12] = 1;
        nArray11[13] = 4;
        nArray11[14] = 3;
        nArray11[15] = 5;
        nArrayArray[9] = nArray11;
        int[] nArray12 = new int[24];
        nArray12[2] = 1;
        nArray12[3] = 5;
        nArray12[5] = 1;
        nArray12[6] = 4;
        nArray12[7] = 5;
        nArray12[9] = 1;
        nArray12[10] = 2;
        nArray12[11] = 4;
        nArray12[12] = 1;
        nArray12[14] = 5;
        nArray12[15] = 3;
        nArray12[16] = 1;
        nArray12[17] = 5;
        nArray12[18] = 4;
        nArray12[19] = 3;
        nArray12[20] = 1;
        nArray12[21] = 4;
        nArray12[22] = 2;
        nArray12[23] = 3;
        nArrayArray[10] = nArray12;
        int[] nArray13 = new int[24];
        nArray13[0] = 1;
        nArray13[2] = 1;
        nArray13[3] = 5;
        nArray13[4] = 1;
        nArray13[5] = 1;
        nArray13[6] = 4;
        nArray13[7] = 5;
        nArray13[8] = 1;
        nArray13[9] = 1;
        nArray13[10] = 2;
        nArray13[11] = 4;
        nArray13[14] = 5;
        nArray13[15] = 3;
        nArray13[17] = 5;
        nArray13[18] = 4;
        nArray13[19] = 3;
        nArray13[21] = 4;
        nArray13[22] = 2;
        nArray13[23] = 3;
        nArrayArray[11] = nArray13;
        int[] nArray14 = new int[24];
        nArray14[0] = 1;
        nArray14[2] = 5;
        nArray14[3] = 4;
        nArray14[4] = 1;
        nArray14[6] = 1;
        nArray14[7] = 5;
        nArray14[10] = 4;
        nArray14[11] = 3;
        nArray14[13] = 4;
        nArray14[14] = 5;
        nArray14[15] = 3;
        nArray14[17] = 5;
        nArray14[18] = 2;
        nArray14[19] = 3;
        nArray14[21] = 1;
        nArray14[22] = 2;
        nArray14[23] = 5;
        nArrayArray[12] = nArray14;
        y = nArrayArray;
    }
}

