/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class NYFUGYQS {
    private boolean a;
    private int b;
    private static int c = 360;
    private static int d = 1;
    private static int e = -460;
    private boolean f;
    private boolean g;
    public static boolean h = true;
    int i;
    int j;
    int k;
    int[][][] l;
    QTKGMFHL[][][] m;
    int n;
    int o;
    OPNPFUJE[] p;
    int[][][] q;
    static int r;
    static int s;
    static int t;
    static int u;
    static int v;
    static int w;
    static int x;
    static int y;
    static int z;
    static int A;
    static int B;
    static int C;
    static int D;
    static int E;
    static int F;
    static int G;
    static OPNPFUJE[] H;
    static final int[] I;
    static final int[] J;
    static final int[] K;
    static final int[] L;
    static boolean M;
    static int N;
    static int O;
    public static int P;
    public static int Q;
    static int R;
    static int[] S;
    static ZARDZRHZ[][] T;
    public static int U;
    static ZARDZRHZ[] V;
    static LHGXPZPG W;
    static final int[] X;
    static final int[] Y;
    static final int[] Z;
    static final int[] ab;
    static final int[] bb;
    static final int[] cb;
    static final int[] db;
    static final int[] eb;
    int[] fb;
    int[] gb;
    int hb;
    int[][] ib;
    int[][] jb;
    static boolean[][][][] kb;
    static boolean[][] lb;
    static int mb;
    static int nb;
    static int ob;
    static int pb;
    static int qb;
    static int rb;

    public NYFUGYQS(int n, byte by, int n2, int[][][] nArray, int n3) {
        block3: {
            boolean bl = XHHRODPC.l;
            this.a = true;
            this.f = true;
            this.g = false;
            this.p = new OPNPFUJE[5000];
            this.fb = new int[10000];
            this.gb = new int[10000];
            int[][] nArrayArray = new int[13][];
            nArrayArray[0] = new int[16];
            nArrayArray[1] = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            int[] nArray2 = new int[16];
            nArray2[0] = 1;
            nArray2[4] = 1;
            nArray2[5] = 1;
            nArray2[8] = 1;
            nArray2[9] = 1;
            nArray2[10] = 1;
            nArray2[12] = 1;
            nArray2[13] = 1;
            nArray2[14] = 1;
            nArray2[15] = 1;
            nArrayArray[2] = nArray2;
            int[] nArray3 = new int[16];
            nArray3[0] = 1;
            nArray3[1] = 1;
            nArray3[4] = 1;
            nArray3[5] = 1;
            nArray3[8] = 1;
            nArray3[12] = 1;
            nArrayArray[3] = nArray3;
            int[] nArray4 = new int[16];
            nArray4[2] = 1;
            nArray4[3] = 1;
            nArray4[6] = 1;
            nArray4[7] = 1;
            nArray4[11] = 1;
            nArray4[15] = 1;
            nArrayArray[4] = nArray4;
            int[] nArray5 = new int[16];
            nArray5[1] = 1;
            nArray5[2] = 1;
            nArray5[3] = 1;
            nArray5[5] = 1;
            nArray5[6] = 1;
            nArray5[7] = 1;
            nArray5[8] = 1;
            nArray5[9] = 1;
            nArray5[10] = 1;
            nArray5[11] = 1;
            nArray5[12] = 1;
            nArray5[13] = 1;
            nArray5[14] = 1;
            nArray5[15] = 1;
            nArrayArray[5] = nArray5;
            int[] nArray6 = new int[16];
            nArray6[0] = 1;
            nArray6[1] = 1;
            nArray6[2] = 1;
            nArray6[4] = 1;
            nArray6[5] = 1;
            nArray6[6] = 1;
            nArray6[8] = 1;
            nArray6[9] = 1;
            nArray6[10] = 1;
            nArray6[11] = 1;
            nArray6[12] = 1;
            nArray6[13] = 1;
            nArray6[14] = 1;
            nArray6[15] = 1;
            nArrayArray[6] = nArray6;
            int[] nArray7 = new int[16];
            nArray7[0] = 1;
            nArray7[1] = 1;
            nArray7[4] = 1;
            nArray7[5] = 1;
            nArray7[8] = 1;
            nArray7[9] = 1;
            nArray7[12] = 1;
            nArray7[13] = 1;
            nArrayArray[7] = nArray7;
            int[] nArray8 = new int[16];
            nArray8[8] = 1;
            nArray8[12] = 1;
            nArray8[13] = 1;
            nArrayArray[8] = nArray8;
            int[] nArray9 = new int[16];
            nArray9[0] = 1;
            nArray9[1] = 1;
            nArray9[2] = 1;
            nArray9[3] = 1;
            nArray9[4] = 1;
            nArray9[5] = 1;
            nArray9[6] = 1;
            nArray9[7] = 1;
            nArray9[9] = 1;
            nArray9[10] = 1;
            nArray9[11] = 1;
            nArray9[14] = 1;
            nArray9[15] = 1;
            nArrayArray[9] = nArray9;
            int[] nArray10 = new int[16];
            nArray10[0] = 1;
            nArray10[1] = 1;
            nArray10[2] = 1;
            nArray10[3] = 1;
            nArray10[4] = 1;
            nArray10[5] = 1;
            nArray10[8] = 1;
            nArray10[12] = 1;
            nArrayArray[10] = nArray10;
            int[] nArray11 = new int[16];
            nArray11[6] = 1;
            nArray11[7] = 1;
            nArray11[9] = 1;
            nArray11[10] = 1;
            nArray11[11] = 1;
            nArray11[13] = 1;
            nArray11[14] = 1;
            nArray11[15] = 1;
            nArrayArray[11] = nArray11;
            int[] nArray12 = new int[16];
            nArray12[9] = 1;
            nArray12[10] = 1;
            nArray12[12] = 1;
            nArray12[13] = 1;
            nArray12[14] = 1;
            nArray12[15] = 1;
            nArrayArray[12] = nArray12;
            this.ib = nArrayArray;
            int[][] nArrayArray2 = new int[4][];
            int[] nArray13 = new int[16];
            nArray13[1] = 1;
            nArray13[2] = 2;
            nArray13[3] = 3;
            nArray13[4] = 4;
            nArray13[5] = 5;
            nArray13[6] = 6;
            nArray13[7] = 7;
            nArray13[8] = 8;
            nArray13[9] = 9;
            nArray13[10] = 10;
            nArray13[11] = 11;
            nArray13[12] = 12;
            nArray13[13] = 13;
            nArray13[14] = 14;
            nArray13[15] = 15;
            nArrayArray2[0] = nArray13;
            int[] nArray14 = new int[16];
            nArray14[0] = 12;
            nArray14[1] = 8;
            nArray14[2] = 4;
            nArray14[4] = 13;
            nArray14[5] = 9;
            nArray14[6] = 5;
            nArray14[7] = 1;
            nArray14[8] = 14;
            nArray14[9] = 10;
            nArray14[10] = 6;
            nArray14[11] = 2;
            nArray14[12] = 15;
            nArray14[13] = 11;
            nArray14[14] = 7;
            nArray14[15] = 3;
            nArrayArray2[1] = nArray14;
            int[] nArray15 = new int[16];
            nArray15[0] = 15;
            nArray15[1] = 14;
            nArray15[2] = 13;
            nArray15[3] = 12;
            nArray15[4] = 11;
            nArray15[5] = 10;
            nArray15[6] = 9;
            nArray15[7] = 8;
            nArray15[8] = 7;
            nArray15[9] = 6;
            nArray15[10] = 5;
            nArray15[11] = 4;
            nArray15[12] = 3;
            nArray15[13] = 2;
            nArray15[14] = 1;
            nArrayArray2[2] = nArray15;
            int[] nArray16 = new int[16];
            nArray16[0] = 3;
            nArray16[1] = 7;
            nArray16[2] = 11;
            nArray16[3] = 15;
            nArray16[4] = 2;
            nArray16[5] = 6;
            nArray16[6] = 10;
            nArray16[7] = 14;
            nArray16[8] = 1;
            nArray16[9] = 5;
            nArray16[10] = 9;
            nArray16[11] = 13;
            nArray16[13] = 4;
            nArray16[14] = 8;
            nArray16[15] = 12;
            nArrayArray2[3] = nArray16;
            this.jb = nArrayArray2;
            try {
                this.i = n3;
                this.j = n2;
                this.k = n;
                if (by != 43) {
                    throw new NullPointerException();
                }
                this.m = new QTKGMFHL[n3][n2][n];
                this.q = new int[n3][n2 + 1][n + 1];
                this.l = nArray;
                this.b(619);
                if (!bl) break block3;
                PKVMXVTO.e = !PKVMXVTO.e;
            }
            catch (RuntimeException runtimeException) {
                signlink.reporterror("29434, " + n + ", " + by + ", " + n2 + ", " + nArray + ", " + n3 + ", " + runtimeException.toString());
                throw new RuntimeException();
            }
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n) {
        try {
            H = null;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                d = -333;
            } while (n >= 0);
            S = null;
            T = null;
            W = null;
            kb = null;
            lb = null;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("40481, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void b(int n) {
        boolean bl = XHHRODPC.l;
        try {
            int n2;
            int n3;
            int n4 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                n3 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    n2 = 0;
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && !bl) continue;
                        this.m[n4][n3][n2] = null;
                        ++n2;
                    } while (n2 < this.k);
                    ++n3;
                } while (n3 < this.j);
                ++n4;
            } while (n4 < this.i);
            n = 37 / n;
            n3 = 0;
            boolean bl5 = true;
            do {
                if (bl5 && !(bl5 = false) && !bl) continue;
                n2 = 0;
                boolean bl6 = true;
                do {
                    if (bl6 && !(bl6 = false) && !bl) continue;
                    NYFUGYQS.T[n3][n2] = null;
                    ++n2;
                } while (n2 < S[n3]);
                NYFUGYQS.S[n3] = 0;
                ++n3;
            } while (n3 < R);
            n2 = 0;
            boolean bl7 = true;
            do {
                if (bl7 && !(bl7 = false) && !bl) continue;
                this.p[n2] = null;
                ++n2;
            } while (n2 < this.o);
            this.o = 0;
            int n5 = 0;
            boolean bl8 = true;
            do {
                if (bl8 && !(bl8 = false) && !bl) continue;
                NYFUGYQS.H[n5] = null;
                ++n5;
            } while (n5 < H.length);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("83723, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2) {
        boolean bl = XHHRODPC.l;
        try {
            if (n2 != -34686) {
                return;
            }
            this.n = n;
            int n3 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n4 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    if (this.m[n][n3][n4] == null) {
                        this.m[n][n3][n4] = new QTKGMFHL(n, n3, n4);
                    }
                    ++n4;
                } while (n4 < this.k);
                ++n3;
            } while (n3 < this.j);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("44284, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2, int n3) {
        boolean bl = XHHRODPC.l;
        try {
            QTKGMFHL qTKGMFHL = this.m[0][n2][n];
            int n4 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                this.m[n4][n2][n] = this.m[n4 + 1][n2][n];
                QTKGMFHL qTKGMFHL2 = this.m[n4][n2][n];
                if (qTKGMFHL2 != null) {
                    --qTKGMFHL2.g;
                    int n5 = 0;
                    boolean bl3 = true;
                    do {
                        if (bl3 && !(bl3 = false) && !bl) continue;
                        OPNPFUJE oPNPFUJE = qTKGMFHL2.r[n5];
                        if ((oPNPFUJE.m >> 29 & 3) == 2 && oPNPFUJE.g == n2 && oPNPFUJE.i == n) {
                            --oPNPFUJE.a;
                        }
                        ++n5;
                    } while (n5 < qTKGMFHL2.q);
                }
                ++n4;
            } while (n4 < 3);
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl) continue;
                int n6 = 1;
                boolean bl5 = true;
                do {
                    if (bl5 && !(bl5 = false) && !bl) continue;
                    ++n6;
                } while (n6 > 0);
            } while (n3 >= 0);
            if (this.m[0][n2][n] == null) {
                this.m[0][n2][n] = new QTKGMFHL(0, n2, n);
            }
            this.m[0][n2][n].C = qTKGMFHL;
            this.m[3][n2][n] = null;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("63341, " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        try {
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                d = -170;
            } while (n7 >= 0);
            ZARDZRHZ zARDZRHZ = new ZARDZRHZ();
            zARDZRHZ.a = n2 / 128;
            zARDZRHZ.b = n4 / 128;
            zARDZRHZ.c = n8 / 128;
            zARDZRHZ.d = n5 / 128;
            zARDZRHZ.e = n9;
            zARDZRHZ.f = n2;
            zARDZRHZ.g = n4;
            zARDZRHZ.h = n8;
            zARDZRHZ.i = n5;
            zARDZRHZ.j = n6;
            zARDZRHZ.k = n3;
            int n10 = n;
            int n11 = S[n10];
            S[n10] = n11 + 1;
            NYFUGYQS.T[n][n11] = zARDZRHZ;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("14863, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + n9 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, int n2, int n3, int n4) {
        QTKGMFHL qTKGMFHL = this.m[n][n2][n3];
        if (qTKGMFHL == null) {
            return;
        }
        this.m[n][n2][n3].u = n4;
    }

    /*
     * Unable to fully structure code
     */
    public void a(int var1_1, int var2_2, int var3_3, int var4_4, int var5_5, int var6_6, int var7_7, int var8_8, int var9_9, int var10_10, int var11_11, int var12_12, int var13_13, int var14_14, int var15_15, int var16_16, int var17_17, int var18_18, int var19_19, int var20_20) {
        block7: {
            block6: {
                var23_21 = XHHRODPC.l;
                if (var4_4 != 0) break block6;
                var21_22 = new XPBACSMK(var11_11, var12_12, var13_13, var14_14, -1, var19_19, false);
                var22_25 = var1_1;
                if (!var23_21) ** GOTO lbl10
                do {
                    if (this.m[var22_25][var2_2][var3_3] == null) {
                        this.m[var22_25][var2_2][var3_3] = new QTKGMFHL(var22_25, var2_2, var3_3);
                    }
                    --var22_25;
lbl10:
                    // 2 sources

                } while (var22_25 >= 0);
                this.m[var1_1][var2_2][var3_3].k = var21_22;
                return;
            }
            if (var4_4 != 1) break block7;
            var21_23 = new XPBACSMK(var15_15, var16_16, var17_17, var18_18, var6_6, var20_20, var7_7 == var8_8 && var7_7 == var9_9 && var7_7 == var10_10);
            var22_26 = var1_1;
            if (!var23_21) ** GOTO lbl22
            do {
                if (this.m[var22_26][var2_2][var3_3] == null) {
                    this.m[var22_26][var2_2][var3_3] = new QTKGMFHL(var22_26, var2_2, var3_3);
                }
                --var22_26;
lbl22:
                // 2 sources

            } while (var22_26 >= 0);
            this.m[var1_1][var2_2][var3_3].k = var21_23;
            return;
        }
        var21_24 = new VBAXKVMG(var3_3, var15_15, var14_14, var9_9, var6_6, var17_17, var5_5, var11_11, var19_19, var13_13, var10_10, var8_8, var7_7, var4_4, var18_18, var16_16, var12_12, 3, var2_2, var20_20);
        var22_27 = var1_1;
        if (!var23_21) ** GOTO lbl33
        do {
            if (this.m[var22_27][var2_2][var3_3] == null) {
                this.m[var22_27][var2_2][var3_3] = new QTKGMFHL(var22_27, var2_2, var3_3);
            }
            --var22_27;
lbl33:
            // 2 sources

        } while (var22_27 >= 0);
        this.m[var1_1][var2_2][var3_3].l = var21_24;
    }

    public void a(int n, int n2, int n3, int n4, XHHRODPC xHHRODPC, byte by, int n5, int n6) {
        try {
            if (xHHRODPC == null) {
                return;
            }
            ZIKPHIFI zIKPHIFI = new ZIKPHIFI();
            zIKPHIFI.d = xHHRODPC;
            zIKPHIFI.b = n6 * 128 + 64;
            zIKPHIFI.c = n3 * 128 + 64;
            if (n4 <= 0) {
                this.g = !this.g;
            }
            zIKPHIFI.a = n2;
            zIKPHIFI.e = n5;
            zIKPHIFI.f = by;
            if (this.m[n][n6][n3] == null) {
                this.m[n][n6][n3] = new QTKGMFHL(n, n6, n3);
            }
            this.m[n][n6][n3].o = zIKPHIFI;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("11891, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + xHHRODPC + ", " + by + ", " + n5 + ", " + n6 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(byte by, int n, int n2, XHHRODPC xHHRODPC, int n3, XHHRODPC xHHRODPC2, XHHRODPC xHHRODPC3, int n4, int n5) {
        try {
            BMEXSMOV bMEXSMOV = new BMEXSMOV();
            bMEXSMOV.d = xHHRODPC3;
            bMEXSMOV.b = n * 128 + 64;
            bMEXSMOV.c = n5 * 128 + 64;
            if (by != 7) {
                return;
            }
            bMEXSMOV.a = n3;
            bMEXSMOV.g = n2;
            bMEXSMOV.e = xHHRODPC;
            bMEXSMOV.f = xHHRODPC2;
            int n6 = 0;
            QTKGMFHL qTKGMFHL = this.m[n4][n][n5];
            if (qTKGMFHL != null) {
                int n7 = 0;
                boolean bl = true;
                do {
                    int n8;
                    if (bl && !(bl = false) && !XHHRODPC.l) continue;
                    if (qTKGMFHL.r[n7].e instanceof ZKARKDQW && (n8 = ((ZKARKDQW)qTKGMFHL.r[n7].e).ab) > n6) {
                        n6 = n8;
                    }
                    ++n7;
                } while (n7 < qTKGMFHL.q);
            }
            bMEXSMOV.h = n6;
            if (this.m[n4][n][n5] == null) {
                this.m[n4][n][n5] = new QTKGMFHL(n4, n, n5);
            }
            this.m[n4][n][n5].p = bMEXSMOV;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("4061, " + by + ", " + n + ", " + n2 + ", " + xHHRODPC + ", " + n3 + ", " + xHHRODPC2 + ", " + xHHRODPC3 + ", " + n4 + ", " + n5 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, XHHRODPC xHHRODPC, boolean bl, int n2, int n3, byte by, int n4, XHHRODPC xHHRODPC2, int n5, int n6, int n7) {
        try {
            if (!bl) {
                boolean bl2 = this.f = !this.f;
            }
            if (xHHRODPC == null && xHHRODPC2 == null) {
                return;
            }
            FEHPTPDG fEHPTPDG = new FEHPTPDG();
            fEHPTPDG.h = n2;
            fEHPTPDG.i = by;
            fEHPTPDG.b = n4 * 128 + 64;
            fEHPTPDG.c = n3 * 128 + 64;
            fEHPTPDG.a = n5;
            fEHPTPDG.f = xHHRODPC;
            fEHPTPDG.g = xHHRODPC2;
            fEHPTPDG.d = n;
            fEHPTPDG.e = n6;
            int n8 = n7;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !XHHRODPC.l) continue;
                if (this.m[n8][n4][n3] == null) {
                    this.m[n8][n4][n3] = new QTKGMFHL(n8, n4, n3);
                }
                --n8;
            } while (n8 >= 0);
            this.m[n7][n4][n3].m = fEHPTPDG;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("65870, " + n + ", " + xHHRODPC + ", " + bl + ", " + n2 + ", " + n3 + ", " + by + ", " + n4 + ", " + xHHRODPC2 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2, int n3, int n4, int n5, int n6, int n7, XHHRODPC xHHRODPC, int n8, byte by, int n9, int n10) {
        boolean bl = XHHRODPC.l;
        try {
            if (xHHRODPC == null) {
                return;
            }
            OFQAEXFV oFQAEXFV = new OFQAEXFV();
            oFQAEXFV.g = n;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                boolean bl3 = this.g = !this.g;
            } while (n4 >= 0);
            oFQAEXFV.h = by;
            oFQAEXFV.b = n8 * 128 + 64 + n6;
            oFQAEXFV.c = n2 * 128 + 64 + n9;
            oFQAEXFV.a = n7;
            oFQAEXFV.f = xHHRODPC;
            oFQAEXFV.d = n10;
            oFQAEXFV.e = n3;
            int n11 = n5;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl) continue;
                if (this.m[n11][n8][n2] == null) {
                    this.m[n11][n8][n2] = new QTKGMFHL(n11, n8, n2);
                }
                --n11;
            } while (n11 >= 0);
            this.m[n5][n8][n2].n = oFQAEXFV;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("21785, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + xHHRODPC + ", " + n8 + ", " + by + ", " + n9 + ", " + n10 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public boolean a(int n, byte by, int n2, int n3, XHHRODPC xHHRODPC, int n4, int n5, int n6, byte by2, int n7, int n8) {
        try {
            if (by2 != 110) {
                c = 250;
            }
            if (xHHRODPC == null) {
                return true;
            }
            int n9 = n8 * 128 + 64 * n4;
            int n10 = n7 * 128 + 64 * n3;
            return this.a(n5, n8, n7, n4, n3, n9, n10, n2, xHHRODPC, n6, false, n, by);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("94021, " + n + ", " + by + ", " + n2 + ", " + n3 + ", " + xHHRODPC + ", " + n4 + ", " + n5 + ", " + n6 + ", " + by2 + ", " + n7 + ", " + n8 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public boolean a(int n, int n2, byte by, int n3, int n4, int n5, int n6, int n7, XHHRODPC xHHRODPC, boolean bl) {
        try {
            int n8;
            int n9;
            block12: {
                block11: {
                    if (xHHRODPC == null) {
                        return true;
                    }
                    n9 = n7 - n6;
                    n8 = n5 - n6;
                    int n10 = n7 + n6;
                    int n11 = n5 + n6;
                    if (bl) {
                        if (n2 > 640 && n2 < 1408) {
                            n11 += 128;
                        }
                        if (n2 > 1152 && n2 < 1920) {
                            n10 += 128;
                        }
                        if (n2 > 1664 || n2 < 384) {
                            n8 -= 128;
                        }
                        if (n2 > 128 && n2 < 896) {
                            n9 -= 128;
                        }
                    }
                    n9 /= 128;
                    if (by != 6) break block11;
                    by = 0;
                    if (!XHHRODPC.l) break block12;
                }
                throw new NullPointerException();
            }
            return this.a(n, n9, n8 /= 128, (n10 /= 128) - n9 + 1, (n11 /= 128) - n8 + 1, n7, n5, n3, xHHRODPC, n2, true, n4, (byte)0);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("2234, " + n + ", " + n2 + ", " + by + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + xHHRODPC + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean a(int n, int n2, int n3, XHHRODPC xHHRODPC, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, byte by) {
        try {
            if (by != 35) {
                int n12 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && !XHHRODPC.l) continue;
                    ++n12;
                } while (n12 > 0);
            }
            if (xHHRODPC == null) {
                return true;
            }
            return this.a(n2, n8, n11, n9 - n8 + 1, n5 - n11 + 1, n6, n3, n7, xHHRODPC, n4, true, n10, (byte)0);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("43595, " + n + ", " + n2 + ", " + n3 + ", " + xHHRODPC + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + n9 + ", " + n10 + ", " + n11 + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private boolean a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, XHHRODPC xHHRODPC, int n9, boolean bl, int n10, byte by) {
        boolean bl2 = XHHRODPC.l;
        int n11 = n2;
        boolean bl3 = true;
        do {
            if (bl3 && !(bl3 = false) && !bl2) continue;
            int n12 = n3;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl2) continue;
                if (n11 < 0 || n12 < 0 || n11 >= this.j || n12 >= this.k) {
                    return false;
                }
                QTKGMFHL qTKGMFHL = this.m[n][n11][n12];
                if (qTKGMFHL != null && qTKGMFHL.q >= 5) {
                    return false;
                }
                ++n12;
            } while (n12 < n3 + n5);
            ++n11;
        } while (n11 < n2 + n4);
        OPNPFUJE oPNPFUJE = new OPNPFUJE();
        oPNPFUJE.m = n10;
        oPNPFUJE.n = by;
        oPNPFUJE.a = n;
        oPNPFUJE.c = n6;
        oPNPFUJE.d = n7;
        oPNPFUJE.b = n8;
        oPNPFUJE.e = xHHRODPC;
        oPNPFUJE.f = n9;
        oPNPFUJE.g = n2;
        oPNPFUJE.i = n3;
        oPNPFUJE.h = n2 + n4 - 1;
        oPNPFUJE.j = n3 + n5 - 1;
        int n13 = n2;
        boolean bl5 = true;
        do {
            if (bl5 && !(bl5 = false) && !bl2) continue;
            int n14 = n3;
            boolean bl6 = true;
            do {
                if (bl6 && !(bl6 = false) && !bl2) continue;
                int n15 = 0;
                if (n13 > n2) {
                    ++n15;
                }
                if (n13 < n2 + n4 - 1) {
                    n15 += 4;
                }
                if (n14 > n3) {
                    n15 += 8;
                }
                if (n14 < n3 + n5 - 1) {
                    n15 += 2;
                }
                int n16 = n;
                boolean bl7 = true;
                do {
                    if (bl7 && !(bl7 = false) && !bl2) continue;
                    if (this.m[n16][n13][n14] == null) {
                        this.m[n16][n13][n14] = new QTKGMFHL(n16, n13, n14);
                    }
                    --n16;
                } while (n16 >= 0);
                QTKGMFHL qTKGMFHL = this.m[n][n13][n14];
                qTKGMFHL.r[qTKGMFHL.q] = oPNPFUJE;
                qTKGMFHL.s[qTKGMFHL.q] = n15;
                qTKGMFHL.t |= n15;
                ++qTKGMFHL.q;
                ++n14;
            } while (n14 < n3 + n5);
            ++n13;
        } while (n13 < n2 + n4);
        if (bl) {
            this.p[this.o++] = oPNPFUJE;
        }
        return true;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(byte by) {
        try {
            if (by != 104) {
                this.g = !this.g;
            }
            int n = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                OPNPFUJE oPNPFUJE = this.p[n];
                this.a(-997, oPNPFUJE);
                this.p[n] = null;
                ++n;
            } while (n < this.o);
            this.o = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("28445, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(int n, OPNPFUJE oPNPFUJE) {
        boolean bl = XHHRODPC.l;
        try {
            if (n >= 0) {
                return;
            }
            int n2 = oPNPFUJE.g;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n3 = oPNPFUJE.i;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    QTKGMFHL qTKGMFHL = this.m[oPNPFUJE.a][n2][n3];
                    if (qTKGMFHL != null) {
                        int n4;
                        int n5 = 0;
                        boolean bl4 = true;
                        do {
                            if (bl4 && !(bl4 = false) && !bl) continue;
                            if (qTKGMFHL.r[n5] == oPNPFUJE) {
                                --qTKGMFHL.q;
                                n4 = n5;
                                boolean bl5 = true;
                                do {
                                    if (bl5 && !(bl5 = false) && !bl) continue;
                                    qTKGMFHL.r[n4] = qTKGMFHL.r[n4 + 1];
                                    qTKGMFHL.s[n4] = qTKGMFHL.s[n4 + 1];
                                    ++n4;
                                } while (n4 < qTKGMFHL.q);
                                qTKGMFHL.r[qTKGMFHL.q] = null;
                                if (!bl) break;
                            }
                            ++n5;
                        } while (n5 < qTKGMFHL.q);
                        qTKGMFHL.t = 0;
                        n4 = 0;
                        boolean bl6 = true;
                        do {
                            if (bl6 && !(bl6 = false) && !bl) continue;
                            qTKGMFHL.t |= qTKGMFHL.s[n4];
                            ++n4;
                        } while (n4 < qTKGMFHL.q);
                    }
                    ++n3;
                } while (n3 <= oPNPFUJE.j);
                ++n2;
            } while (n2 <= oPNPFUJE.h);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("43159, " + n + ", " + oPNPFUJE + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, int n2, int n3, int n4, int n5) {
        try {
            QTKGMFHL qTKGMFHL = this.m[n5][n4][n];
            if (n2 <= 0) {
                boolean bl = this.a = !this.a;
            }
            if (qTKGMFHL == null) {
                return;
            }
            OFQAEXFV oFQAEXFV = qTKGMFHL.n;
            if (oFQAEXFV == null) {
                return;
            }
            int n6 = n4 * 128 + 64;
            int n7 = n * 128 + 64;
            oFQAEXFV.b = n6 + (oFQAEXFV.b - n6) * n3 / 16;
            oFQAEXFV.c = n7 + (oFQAEXFV.c - n7) * n3 / 16;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("49418, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, int n2, int n3, byte by) {
        try {
            QTKGMFHL qTKGMFHL = this.m[n2][n][n3];
            if (by != -119) {
                boolean bl = this.f = !this.f;
            }
            if (qTKGMFHL == null) {
                return;
            }
            qTKGMFHL.m = null;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("67055, " + n + ", " + n2 + ", " + n3 + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void b(int n, int n2, int n3, int n4) {
        try {
            QTKGMFHL qTKGMFHL = this.m[n3][n4][n2];
            if (qTKGMFHL == null) {
                return;
            }
            qTKGMFHL.n = null;
            if (n != 0) {
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("18618, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void c(int n, int n2, int n3, int n4) {
        boolean bl = XHHRODPC.l;
        try {
            QTKGMFHL qTKGMFHL;
            if (n2 >= 0) {
                int n5 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && !bl) continue;
                    ++n5;
                } while (n5 > 0);
            }
            if ((qTKGMFHL = this.m[n][n3][n4]) == null) {
                return;
            }
            int n6 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                OPNPFUJE oPNPFUJE = qTKGMFHL.r[n6];
                if ((oPNPFUJE.m >> 29 & 3) == 2 && oPNPFUJE.g == n3 && oPNPFUJE.i == n4) {
                    this.a(-997, oPNPFUJE);
                    return;
                }
                ++n6;
            } while (n6 < qTKGMFHL.q);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("59016, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(byte by, int n, int n2, int n3) {
        try {
            QTKGMFHL qTKGMFHL = this.m[n][n3][n2];
            if (qTKGMFHL == null) {
                return;
            }
            qTKGMFHL.o = null;
            if (by == 9) {
                by = 0;
                return;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("33570, " + by + ", " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void b(int n, int n2, int n3) {
        QTKGMFHL qTKGMFHL = this.m[n][n2][n3];
        if (qTKGMFHL == null) {
            return;
        }
        qTKGMFHL.p = null;
    }

    public FEHPTPDG a(int n, int n2, int n3, boolean bl) {
        try {
            QTKGMFHL qTKGMFHL;
            if (bl) {
                e = -195;
            }
            if ((qTKGMFHL = this.m[n][n2][n3]) == null) {
                return null;
            }
            return qTKGMFHL.m;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("18783, " + n + ", " + n2 + ", " + n3 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public OFQAEXFV d(int n, int n2, int n3, int n4) {
        try {
            if (n2 <= 0) {
                throw new NullPointerException();
            }
            QTKGMFHL qTKGMFHL = this.m[n4][n][n3];
            if (qTKGMFHL == null) {
                return null;
            }
            return qTKGMFHL.n;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("62763, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public OPNPFUJE a(int n, int n2, byte by, int n3) {
        boolean bl = XHHRODPC.l;
        try {
            QTKGMFHL qTKGMFHL = this.m[n3][n][n2];
            if (qTKGMFHL == null) {
                return null;
            }
            int n4 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                OPNPFUJE oPNPFUJE = qTKGMFHL.r[n4];
                if ((oPNPFUJE.m >> 29 & 3) == 2 && oPNPFUJE.g == n && oPNPFUJE.i == n2) {
                    return oPNPFUJE;
                }
                ++n4;
            } while (n4 < qTKGMFHL.q);
            if (by == 4) {
                by = 0;
                if (!bl) return null;
            }
            d = -376;
            return null;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("5110, " + n + ", " + n2 + ", " + by + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ZIKPHIFI e(int n, int n2, int n3, int n4) {
        try {
            QTKGMFHL qTKGMFHL = this.m[n3][n2][n];
            if (n4 != 0) {
                int n5 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && !XHHRODPC.l) continue;
                    ++n5;
                } while (n5 > 0);
            }
            if (qTKGMFHL != null && qTKGMFHL.o != null) {
                return qTKGMFHL.o;
            }
            return null;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("50413, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int c(int n, int n2, int n3) {
        QTKGMFHL qTKGMFHL = this.m[n][n2][n3];
        if (qTKGMFHL == null || qTKGMFHL.m == null) {
            return 0;
        }
        return qTKGMFHL.m.h;
    }

    public int f(int n, int n2, int n3, int n4) {
        try {
            QTKGMFHL qTKGMFHL = this.m[n][n2][n4];
            if (n3 != 0) {
                return this.b;
            }
            if (qTKGMFHL == null || qTKGMFHL.n == null) {
                return 0;
            }
            return qTKGMFHL.n.g;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("18367, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public int d(int n, int n2, int n3) {
        QTKGMFHL qTKGMFHL = this.m[n][n2][n3];
        if (qTKGMFHL == null) {
            return 0;
        }
        int n4 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && !XHHRODPC.l) continue;
            OPNPFUJE oPNPFUJE = qTKGMFHL.r[n4];
            if ((oPNPFUJE.m >> 29 & 3) == 2 && oPNPFUJE.g == n2 && oPNPFUJE.i == n3) {
                return oPNPFUJE.m;
            }
            ++n4;
        } while (n4 < qTKGMFHL.q);
        return 0;
    }

    public int e(int n, int n2, int n3) {
        QTKGMFHL qTKGMFHL = this.m[n][n2][n3];
        if (qTKGMFHL == null || qTKGMFHL.o == null) {
            return 0;
        }
        return qTKGMFHL.o.e;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public int g(int n, int n2, int n3, int n4) {
        QTKGMFHL qTKGMFHL = this.m[n][n2][n3];
        if (qTKGMFHL == null) {
            return -1;
        }
        if (qTKGMFHL.m != null && qTKGMFHL.m.h == n4) {
            return qTKGMFHL.m.i & 0xFF;
        }
        if (qTKGMFHL.n != null && qTKGMFHL.n.g == n4) {
            return qTKGMFHL.n.h & 0xFF;
        }
        if (qTKGMFHL.o != null && qTKGMFHL.o.e == n4) {
            return qTKGMFHL.o.f & 0xFF;
        }
        int n5 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && !XHHRODPC.l) continue;
            if (qTKGMFHL.r[n5].m == n4) {
                return qTKGMFHL.r[n5].n & 0xFF;
            }
            ++n5;
        } while (n5 < qTKGMFHL.q);
        return -1;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, byte by, int n2, int n3, int n4, int n5) {
        boolean bl = XHHRODPC.l;
        try {
            int n6 = (int)Math.sqrt(n3 * n3 + n * n + n5 * n5);
            int n7 = n4 * n6 >> 8;
            if (by != 3) {
                this.f = !this.f;
            }
            int n8 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n9 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    int n10 = 0;
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && !bl) continue;
                        QTKGMFHL qTKGMFHL = this.m[n8][n9][n10];
                        if (qTKGMFHL != null) {
                            Object object;
                            FEHPTPDG fEHPTPDG = qTKGMFHL.m;
                            if (fEHPTPDG != null && fEHPTPDG.f != null && fEHPTPDG.f.j != null) {
                                this.a(n8, 1, 1, n9, (byte)115, n10, (ZKARKDQW)fEHPTPDG.f);
                                if (fEHPTPDG.g != null && fEHPTPDG.g.j != null) {
                                    this.a(n8, 1, 1, n9, (byte)115, n10, (ZKARKDQW)fEHPTPDG.g);
                                    this.a((ZKARKDQW)fEHPTPDG.f, (ZKARKDQW)fEHPTPDG.g, 0, 0, 0, false);
                                    ((ZKARKDQW)fEHPTPDG.g).a(n2, n7, n3, n, n5);
                                }
                                ((ZKARKDQW)fEHPTPDG.f).a(n2, n7, n3, n, n5);
                            }
                            int n11 = 0;
                            boolean bl5 = true;
                            do {
                                if (bl5 && !(bl5 = false) && !bl) continue;
                                object = qTKGMFHL.r[n11];
                                if (object != null && ((OPNPFUJE)object).e != null && ((OPNPFUJE)object).e.j != null) {
                                    this.a(n8, ((OPNPFUJE)object).h - ((OPNPFUJE)object).g + 1, ((OPNPFUJE)object).j - ((OPNPFUJE)object).i + 1, n9, (byte)115, n10, (ZKARKDQW)((OPNPFUJE)object).e);
                                    ((ZKARKDQW)((OPNPFUJE)object).e).a(n2, n7, n3, n, n5);
                                }
                                ++n11;
                            } while (n11 < qTKGMFHL.q);
                            object = qTKGMFHL.o;
                            if (object != null && ((ZIKPHIFI)object).d.j != null) {
                                this.a(n9, n8, (ZKARKDQW)((ZIKPHIFI)object).d, (byte)37, n10);
                                ((ZKARKDQW)((ZIKPHIFI)object).d).a(n2, n7, n3, n, n5);
                            }
                        }
                        ++n10;
                    } while (n10 < this.k);
                    ++n9;
                } while (n9 < this.j);
                ++n8;
            } while (n8 < this.i);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("77749, " + n + ", " + by + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(int n, int n2, ZKARKDQW zKARKDQW, byte by, int n3) {
        try {
            QTKGMFHL qTKGMFHL;
            QTKGMFHL qTKGMFHL2;
            QTKGMFHL qTKGMFHL3;
            if (by != 37) {
                int n4 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && !XHHRODPC.l) continue;
                    ++n4;
                } while (n4 > 0);
            }
            if (n < this.j && (qTKGMFHL3 = this.m[n2][n + 1][n3]) != null && qTKGMFHL3.o != null && qTKGMFHL3.o.d.j != null) {
                this.a(zKARKDQW, (ZKARKDQW)qTKGMFHL3.o.d, 128, 0, 0, true);
            }
            if (n3 < this.j && (qTKGMFHL2 = this.m[n2][n][n3 + 1]) != null && qTKGMFHL2.o != null && qTKGMFHL2.o.d.j != null) {
                this.a(zKARKDQW, (ZKARKDQW)qTKGMFHL2.o.d, 0, 0, 128, true);
            }
            if (n < this.j && n3 < this.k && (qTKGMFHL = this.m[n2][n + 1][n3 + 1]) != null && qTKGMFHL.o != null && qTKGMFHL.o.d.j != null) {
                this.a(zKARKDQW, (ZKARKDQW)qTKGMFHL.o.d, 128, 0, 128, true);
            }
            if (n >= this.j) return;
            if (n3 <= 0) return;
            QTKGMFHL qTKGMFHL4 = this.m[n2][n + 1][n3 - 1];
            if (qTKGMFHL4 == null) return;
            if (qTKGMFHL4.o == null) return;
            if (qTKGMFHL4.o.d.j == null) return;
            this.a(zKARKDQW, (ZKARKDQW)qTKGMFHL4.o.d, 128, 0, -128, true);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("40901, " + n + ", " + n2 + ", " + zKARKDQW + ", " + by + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(int n, int n2, int n3, int n4, byte by, int n5, ZKARKDQW zKARKDQW) {
        boolean bl = XHHRODPC.l;
        try {
            boolean bl2 = true;
            if (by != 115) {
                c = 350;
            }
            int n6 = n4;
            int n7 = n4 + n2;
            int n8 = n5 - 1;
            int n9 = n5 + n3;
            int n10 = n;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                if (n10 != this.i) {
                    int n11 = n6;
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && !bl) continue;
                        if (n11 >= 0 && n11 < this.j) {
                            int n12 = n8;
                            boolean bl5 = true;
                            do {
                                QTKGMFHL qTKGMFHL;
                                if (bl5 && !(bl5 = false) && !bl) continue;
                                if (n12 >= 0 && n12 < this.k && (!bl2 || n11 >= n7 || n12 >= n9 || n12 < n5 && n11 != n4) && (qTKGMFHL = this.m[n10][n11][n12]) != null) {
                                    int n13 = (this.l[n10][n11][n12] + this.l[n10][n11 + 1][n12] + this.l[n10][n11][n12 + 1] + this.l[n10][n11 + 1][n12 + 1]) / 4 - (this.l[n][n4][n5] + this.l[n][n4 + 1][n5] + this.l[n][n4][n5 + 1] + this.l[n][n4 + 1][n5 + 1]) / 4;
                                    FEHPTPDG fEHPTPDG = qTKGMFHL.m;
                                    if (fEHPTPDG != null && fEHPTPDG.f != null && fEHPTPDG.f.j != null) {
                                        this.a(zKARKDQW, (ZKARKDQW)fEHPTPDG.f, (n11 - n4) * 128 + (1 - n2) * 64, n13, (n12 - n5) * 128 + (1 - n3) * 64, bl2);
                                    }
                                    if (fEHPTPDG != null && fEHPTPDG.g != null && fEHPTPDG.g.j != null) {
                                        this.a(zKARKDQW, (ZKARKDQW)fEHPTPDG.g, (n11 - n4) * 128 + (1 - n2) * 64, n13, (n12 - n5) * 128 + (1 - n3) * 64, bl2);
                                    }
                                    int n14 = 0;
                                    boolean bl6 = true;
                                    do {
                                        if (bl6 && !(bl6 = false) && !bl) continue;
                                        OPNPFUJE oPNPFUJE = qTKGMFHL.r[n14];
                                        if (oPNPFUJE != null && oPNPFUJE.e != null && oPNPFUJE.e.j != null) {
                                            int n15 = oPNPFUJE.h - oPNPFUJE.g + 1;
                                            int n16 = oPNPFUJE.j - oPNPFUJE.i + 1;
                                            this.a(zKARKDQW, (ZKARKDQW)oPNPFUJE.e, (oPNPFUJE.g - n4) * 128 + (n15 - n2) * 64, n13, (oPNPFUJE.i - n5) * 128 + (n16 - n3) * 64, bl2);
                                        }
                                        ++n14;
                                    } while (n14 < qTKGMFHL.q);
                                }
                                ++n12;
                            } while (n12 <= n9);
                        }
                        ++n11;
                    } while (n11 <= n7);
                    --n6;
                    bl2 = false;
                }
                ++n10;
            } while (n10 <= n + 1);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("11529, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + by + ", " + n5 + ", " + zKARKDQW + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private void a(ZKARKDQW zKARKDQW, ZKARKDQW zKARKDQW2, int n, int n2, int n3, boolean bl) {
        boolean bl2 = XHHRODPC.l;
        ++this.hb;
        int n4 = 0;
        int[] nArray = zKARKDQW2.z;
        int n5 = zKARKDQW2.y;
        int n6 = 0;
        boolean bl3 = true;
        do {
            int n7;
            int n8;
            int n9;
            if (bl3 && !(bl3 = false) && !bl2) continue;
            RJXWGZGD rJXWGZGD = zKARKDQW.j[n6];
            RJXWGZGD rJXWGZGD2 = zKARKDQW.gb[n6];
            if (rJXWGZGD2.d != 0 && (n9 = zKARKDQW.A[n6] - n2) <= zKARKDQW2.X && (n8 = zKARKDQW.z[n6] - n) >= zKARKDQW2.S && n8 <= zKARKDQW2.T && (n7 = zKARKDQW.B[n6] - n3) >= zKARKDQW2.V && n7 <= zKARKDQW2.U) {
                int n10 = 0;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && !bl2) continue;
                    RJXWGZGD rJXWGZGD3 = zKARKDQW2.j[n10];
                    RJXWGZGD rJXWGZGD4 = zKARKDQW2.gb[n10];
                    if (n8 == nArray[n10] && n7 == zKARKDQW2.B[n10] && n9 == zKARKDQW2.A[n10] && rJXWGZGD4.d != 0) {
                        rJXWGZGD.a += rJXWGZGD4.a;
                        rJXWGZGD.b += rJXWGZGD4.b;
                        rJXWGZGD.c += rJXWGZGD4.c;
                        rJXWGZGD.d += rJXWGZGD4.d;
                        rJXWGZGD3.a += rJXWGZGD2.a;
                        rJXWGZGD3.b += rJXWGZGD2.b;
                        rJXWGZGD3.c += rJXWGZGD2.c;
                        rJXWGZGD3.d += rJXWGZGD2.d;
                        ++n4;
                        this.fb[n6] = this.hb;
                        this.gb[n10] = this.hb;
                    }
                    ++n10;
                } while (n10 < n5);
            }
            ++n6;
        } while (n6 < zKARKDQW.y);
        if (n4 < 3 || !bl) {
            return;
        }
        int n11 = 0;
        boolean bl5 = true;
        do {
            if (bl5 && !(bl5 = false) && !bl2) continue;
            if (this.fb[zKARKDQW.D[n11]] == this.hb && this.fb[zKARKDQW.E[n11]] == this.hb && this.fb[zKARKDQW.F[n11]] == this.hb) {
                zKARKDQW.J[n11] = -1;
            }
            ++n11;
        } while (n11 < zKARKDQW.C);
        int n12 = 0;
        boolean bl6 = true;
        do {
            if (bl6 && !(bl6 = false) && !bl2) continue;
            if (this.gb[zKARKDQW2.D[n12]] == this.hb && this.gb[zKARKDQW2.E[n12]] == this.hb && this.gb[zKARKDQW2.F[n12]] == this.hb) {
                zKARKDQW2.J[n12] = -1;
            }
            ++n12;
        } while (n12 < zKARKDQW2.C);
    }

    /*
     * Unable to fully structure code
     */
    public void a(int[] var1_1, int var2_2, int var3_3, int var4_4, int var5_5, int var6_6) {
        block11: {
            block10: {
                var18_7 = XHHRODPC.l;
                var7_8 = this.m[var4_4][var5_5][var6_6];
                if (var7_8 == null) {
                    return;
                }
                var8_9 = var7_8.k;
                if (var8_9 == null) break block10;
                var9_10 = var8_9.g;
                if (var9_10 == 0) {
                    return;
                }
                var10_12 = 0;
                if (!var18_7) ** GOTO lbl19
                do {
                    var1_1[var2_2] = var9_10;
                    var1_1[var2_2 + 1] = var9_10;
                    var1_1[var2_2 + 2] = var9_10;
                    var1_1[var2_2 + 3] = var9_10;
                    var2_2 += var3_3;
                    ++var10_12;
lbl19:
                    // 2 sources

                } while (var10_12 < 4);
                return;
            }
            var9_11 = var7_8.l;
            if (var9_11 == null) {
                return;
            }
            var10_13 = var9_11.l;
            var11_14 = var9_11.m;
            var12_15 = var9_11.n;
            var13_16 = var9_11.o;
            var14_17 = this.ib[var10_13];
            var15_18 = this.jb[var11_14];
            var16_19 = 0;
            if (var12_15 == 0) break block11;
            var17_20 = 0;
            if (!var18_7) ** GOTO lbl42
            do {
                var1_1[var2_2] = var14_17[var15_18[var16_19++]] == 0 ? var12_15 : var13_16;
                var1_1[var2_2 + 1] = var14_17[var15_18[var16_19++]] == 0 ? var12_15 : var13_16;
                var1_1[var2_2 + 2] = var14_17[var15_18[var16_19++]] == 0 ? var12_15 : var13_16;
                var1_1[var2_2 + 3] = var14_17[var15_18[var16_19++]] == 0 ? var12_15 : var13_16;
                var2_2 += var3_3;
                ++var17_20;
lbl42:
                // 2 sources

            } while (var17_20 < 4);
            return;
        }
        var17_21 = 0;
        if (!var18_7) ** GOTO lbl58
        do {
            if (var14_17[var15_18[var16_19++]] != 0) {
                var1_1[var2_2] = var13_16;
            }
            if (var14_17[var15_18[var16_19++]] != 0) {
                var1_1[var2_2 + 1] = var13_16;
            }
            if (var14_17[var15_18[var16_19++]] != 0) {
                var1_1[var2_2 + 2] = var13_16;
            }
            if (var14_17[var15_18[var16_19++]] != 0) {
                var1_1[var2_2 + 3] = var13_16;
            }
            var2_2 += var3_3;
            ++var17_21;
lbl58:
            // 2 sources

        } while (var17_21 < 4);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, int n2, int n3, int n4, int[] nArray, boolean bl) {
        boolean bl2 = XHHRODPC.l;
        try {
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            int n10;
            int n11;
            ob = 0;
            pb = 0;
            qb = n3;
            rb = n4;
            mb = n3 / 2;
            nb = n4 / 2;
            boolean[][][][] blArray = new boolean[9][32][53][53];
            if (bl) {
                e = 168;
            }
            int n12 = 128;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl2) continue;
                n11 = 0;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && !bl2) continue;
                    D = ZKARKDQW.Jb[n12];
                    E = ZKARKDQW.Kb[n12];
                    F = ZKARKDQW.Jb[n11];
                    G = ZKARKDQW.Kb[n11];
                    n10 = (n12 - 128) / 32;
                    n9 = n11 / 64;
                    n8 = -26;
                    boolean bl5 = true;
                    do {
                        if (bl5 && !(bl5 = false) && !bl2) continue;
                        n7 = -26;
                        boolean bl6 = true;
                        do {
                            if (bl6 && !(bl6 = false) && !bl2) continue;
                            n6 = n8 * 128;
                            n5 = n7 * 128;
                            boolean bl7 = false;
                            int n13 = -n;
                            boolean bl8 = true;
                            do {
                                if (bl8 && !(bl8 = false) && !bl2) continue;
                                if (NYFUGYQS.b((byte)9, nArray[n10] + n13, n5, n6)) {
                                    bl7 = true;
                                    if (!bl2) break;
                                }
                                n13 += 128;
                            } while (n13 <= n2);
                            blArray[n10][n9][n8 + 25 + 1][n7 + 25 + 1] = bl7;
                            ++n7;
                        } while (n7 <= 26);
                        ++n8;
                    } while (n8 <= 26);
                    n11 += 64;
                } while (n11 < 2048);
                n12 += 32;
            } while (n12 <= 384);
            n11 = 0;
            boolean bl9 = true;
            do {
                if (bl9 && !(bl9 = false) && !bl2) continue;
                n10 = 0;
                boolean bl10 = true;
                do {
                    if (bl10 && !(bl10 = false) && !bl2) continue;
                    n9 = -25;
                    boolean bl11 = true;
                    do {
                        if (bl11 && !(bl11 = false) && !bl2) continue;
                        n8 = -25;
                        boolean bl12 = true;
                        do {
                            if (bl12 && !(bl12 = false) && !bl2) continue;
                            n7 = 0;
                            n6 = -1;
                            boolean bl13 = true;
                            block11: do {
                                if (bl13 && !(bl13 = false) && !bl2) continue;
                                n5 = -1;
                                boolean bl14 = true;
                                do {
                                    if (bl14 && !(bl14 = false) && !bl2) continue;
                                    if (blArray[n11][n10][n9 + n6 + 25 + 1][n8 + n5 + 25 + 1]) {
                                        n7 = 1;
                                        if (!bl2) break block11;
                                    }
                                    if (blArray[n11][(n10 + 1) % 31][n9 + n6 + 25 + 1][n8 + n5 + 25 + 1]) {
                                        n7 = 1;
                                        if (!bl2) break block11;
                                    }
                                    if (blArray[n11 + 1][n10][n9 + n6 + 25 + 1][n8 + n5 + 25 + 1]) {
                                        n7 = 1;
                                        if (!bl2) break block11;
                                    }
                                    if (blArray[n11 + 1][(n10 + 1) % 31][n9 + n6 + 25 + 1][n8 + n5 + 25 + 1]) {
                                        n7 = 1;
                                        if (!bl2) break block11;
                                    }
                                    ++n5;
                                } while (n5 <= 1);
                                ++n6;
                            } while (n6 <= 1);
                            NYFUGYQS.kb[n11][n10][n9 + 25][n8 + 25] = n7;
                            ++n8;
                        } while (n8 < 25);
                        ++n9;
                    } while (n9 < 25);
                    ++n10;
                } while (n10 < 32);
                ++n11;
            } while (n11 < 8);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("5468, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + nArray + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static boolean b(byte by, int n, int n2, int n3) {
        try {
            int n4 = n2 * F + n3 * G >> 16;
            int n5 = n2 * G - n3 * F >> 16;
            if (by != 9) {
                c = -346;
            }
            int n6 = n * D + n5 * E >> 16;
            int n7 = n * E - n5 * D >> 16;
            if (n6 < 50 || n6 > 3500) {
                return false;
            }
            int n8 = mb + (n4 << 9) / n6;
            int n9 = nb + (n7 << 9) / n6;
            return n8 >= ob && n8 <= qb && n9 >= pb && n9 <= rb;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("3265, " + by + ", " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(boolean bl, int n, int n2) {
        try {
            M = true;
            N = n2;
            O = n;
            P = -1;
            Q = -1;
            if (!bl) return;
            int n3 = 1;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !XHHRODPC.l) continue;
                ++n3;
            } while (n3 > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("24272, " + bl + ", " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        boolean bl2 = XHHRODPC.l;
        try {
            int n7;
            int n8;
            int n9;
            int n10;
            int n11;
            block49: {
                block48: {
                    block47: {
                        block46: {
                            if (n >= 0) break block46;
                            n = 0;
                            if (!bl2) break block47;
                        }
                        if (n >= this.j * 128) {
                            n = this.j * 128 - 1;
                        }
                    }
                    if (n2 >= 0) break block48;
                    n2 = 0;
                    if (!bl2) break block49;
                }
                if (n2 >= this.k * 128) {
                    n2 = this.k * 128 - 1;
                }
            }
            ++t;
            D = ZKARKDQW.Jb[n6];
            E = ZKARKDQW.Kb[n6];
            if (bl) {
                return;
            }
            F = ZKARKDQW.Jb[n3];
            G = ZKARKDQW.Kb[n3];
            lb = kb[(n6 - 128) / 32][n3 / 64];
            A = n;
            B = n4;
            C = n2;
            y = n / 128;
            z = n2 / 128;
            s = n5;
            u = y - 25;
            if (u < 0) {
                u = 0;
            }
            if ((w = z - 25) < 0) {
                w = 0;
            }
            if ((v = y + 25) > this.j) {
                v = this.j;
            }
            if ((x = z + 25) > this.k) {
                x = this.k;
            }
            this.c(0);
            r = 0;
            int n12 = this.n;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl2) continue;
                QTKGMFHL[][] qTKGMFHLArray = this.m[n12];
                int n13 = u;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && !bl2) continue;
                    n11 = w;
                    boolean bl5 = true;
                    do {
                        block50: {
                            block53: {
                                block52: {
                                    QTKGMFHL qTKGMFHL;
                                    block51: {
                                        if (bl5 && !(bl5 = false) && !bl2) continue;
                                        qTKGMFHL = qTKGMFHLArray[n13][n11];
                                        if (qTKGMFHL == null) break block50;
                                        if (qTKGMFHL.u <= n5 && (lb[n13 - y + 25][n11 - z + 25] || this.l[n12][n13][n11] - n4 >= 2000)) break block51;
                                        qTKGMFHL.v = false;
                                        qTKGMFHL.w = false;
                                        qTKGMFHL.y = 0;
                                        if (!bl2) break block50;
                                    }
                                    qTKGMFHL.v = true;
                                    qTKGMFHL.w = true;
                                    if (qTKGMFHL.q <= 0) break block52;
                                    qTKGMFHL.x = true;
                                    if (!bl2) break block53;
                                }
                                qTKGMFHL.x = false;
                            }
                            ++r;
                        }
                        ++n11;
                    } while (n11 < x);
                    ++n13;
                } while (n13 < v);
                ++n12;
            } while (n12 < this.i);
            int n14 = this.n;
            boolean bl6 = true;
            do {
                if (bl6 && !(bl6 = false) && !bl2) continue;
                QTKGMFHL[][] qTKGMFHLArray = this.m[n14];
                n11 = -25;
                boolean bl7 = true;
                do {
                    if (bl7 && !(bl7 = false) && !bl2) continue;
                    int n15 = y + n11;
                    n10 = y - n11;
                    if (n15 >= u || n10 < v) {
                        n9 = -25;
                        boolean bl8 = true;
                        do {
                            QTKGMFHL qTKGMFHL;
                            if (bl8 && !(bl8 = false) && !bl2) continue;
                            n8 = z + n9;
                            n7 = z - n9;
                            if (n15 >= u) {
                                if (n8 >= w && (qTKGMFHL = qTKGMFHLArray[n15][n8]) != null && qTKGMFHL.v) {
                                    this.a(qTKGMFHL, true);
                                }
                                if (n7 < x && (qTKGMFHL = qTKGMFHLArray[n15][n7]) != null && qTKGMFHL.v) {
                                    this.a(qTKGMFHL, true);
                                }
                            }
                            if (n10 < v) {
                                if (n8 >= w && (qTKGMFHL = qTKGMFHLArray[n10][n8]) != null && qTKGMFHL.v) {
                                    this.a(qTKGMFHL, true);
                                }
                                if (n7 < x && (qTKGMFHL = qTKGMFHLArray[n10][n7]) != null && qTKGMFHL.v) {
                                    this.a(qTKGMFHL, true);
                                }
                            }
                            if (r == 0) {
                                M = false;
                                return;
                            }
                            ++n9;
                        } while (n9 <= 0);
                    }
                    ++n11;
                } while (n11 <= 0);
                ++n14;
            } while (n14 < this.i);
            int n16 = this.n;
            boolean bl9 = true;
            do {
                if (bl9 && !(bl9 = false) && !bl2) continue;
                QTKGMFHL[][] qTKGMFHLArray = this.m[n16];
                int n17 = -25;
                boolean bl10 = true;
                do {
                    if (bl10 && !(bl10 = false) && !bl2) continue;
                    n10 = y + n17;
                    n9 = y - n17;
                    if (n10 >= u || n9 < v) {
                        n8 = -25;
                        boolean bl11 = true;
                        do {
                            QTKGMFHL qTKGMFHL;
                            if (bl11 && !(bl11 = false) && !bl2) continue;
                            n7 = z + n8;
                            int n18 = z - n8;
                            if (n10 >= u) {
                                if (n7 >= w && (qTKGMFHL = qTKGMFHLArray[n10][n7]) != null && qTKGMFHL.v) {
                                    this.a(qTKGMFHL, false);
                                }
                                if (n18 < x && (qTKGMFHL = qTKGMFHLArray[n10][n18]) != null && qTKGMFHL.v) {
                                    this.a(qTKGMFHL, false);
                                }
                            }
                            if (n9 < v) {
                                if (n7 >= w && (qTKGMFHL = qTKGMFHLArray[n9][n7]) != null && qTKGMFHL.v) {
                                    this.a(qTKGMFHL, false);
                                }
                                if (n18 < x && (qTKGMFHL = qTKGMFHLArray[n9][n18]) != null && qTKGMFHL.v) {
                                    this.a(qTKGMFHL, false);
                                }
                            }
                            if (r == 0) {
                                M = false;
                                return;
                            }
                            ++n8;
                        } while (n8 <= 0);
                    }
                    ++n17;
                } while (n17 <= 0);
                ++n16;
            } while (n16 < this.i);
            M = false;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("93114, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(QTKGMFHL qTKGMFHL, boolean bl) {
        boolean bl2 = XHHRODPC.l;
        W.a(qTKGMFHL);
        while (true) {
            QTKGMFHL qTKGMFHL2;
            QTKGMFHL qTKGMFHL3;
            QTKGMFHL qTKGMFHL4;
            QTKGMFHL qTKGMFHL5;
            QTKGMFHL qTKGMFHL6;
            QTKGMFHL[][] qTKGMFHLArray;
            int n;
            int n2;
            int n3;
            block156: {
                FEHPTPDG fEHPTPDG;
                int n4;
                QTKGMFHL qTKGMFHL7;
                block157: {
                    int n5;
                    int n6;
                    int n7;
                    OFQAEXFV oFQAEXFV;
                    int n8;
                    int n9;
                    int n10;
                    int n11;
                    block162: {
                        block161: {
                            block160: {
                                block159: {
                                    block158: {
                                        QTKGMFHL qTKGMFHL8;
                                        QTKGMFHL qTKGMFHL9;
                                        QTKGMFHL qTKGMFHL10;
                                        QTKGMFHL qTKGMFHL11;
                                        block150: {
                                            int n12;
                                            Object object;
                                            block127: {
                                                boolean bl3;
                                                block144: {
                                                    int n13;
                                                    int n14;
                                                    int n15;
                                                    OFQAEXFV oFQAEXFV2;
                                                    block149: {
                                                        block148: {
                                                            block147: {
                                                                block146: {
                                                                    block145: {
                                                                        int n16;
                                                                        block138: {
                                                                            block141: {
                                                                                block139: {
                                                                                    int n17;
                                                                                    block143: {
                                                                                        block142: {
                                                                                            block140: {
                                                                                                block133: {
                                                                                                    block137: {
                                                                                                        block136: {
                                                                                                            block135: {
                                                                                                                block134: {
                                                                                                                    block132: {
                                                                                                                        block131: {
                                                                                                                            block128: {
                                                                                                                                FEHPTPDG fEHPTPDG2;
                                                                                                                                QTKGMFHL qTKGMFHL12;
                                                                                                                                block130: {
                                                                                                                                    block129: {
                                                                                                                                        if ((qTKGMFHL7 = (QTKGMFHL)W.a()) == null) {
                                                                                                                                            return;
                                                                                                                                        }
                                                                                                                                        if (!qTKGMFHL7.w) continue;
                                                                                                                                        n3 = qTKGMFHL7.h;
                                                                                                                                        n2 = qTKGMFHL7.i;
                                                                                                                                        n = qTKGMFHL7.g;
                                                                                                                                        n4 = qTKGMFHL7.j;
                                                                                                                                        qTKGMFHLArray = this.m[n];
                                                                                                                                        if (!qTKGMFHL7.v) break block127;
                                                                                                                                        if (bl) {
                                                                                                                                            if (n > 0 && (qTKGMFHL12 = this.m[n - 1][n3][n2]) != null && qTKGMFHL12.w || n3 <= y && n3 > u && (qTKGMFHL12 = qTKGMFHLArray[n3 - 1][n2]) != null && qTKGMFHL12.w && (qTKGMFHL12.v || (qTKGMFHL7.t & 1) == 0) || n3 >= y && n3 < v - 1 && (qTKGMFHL12 = qTKGMFHLArray[n3 + 1][n2]) != null && qTKGMFHL12.w && (qTKGMFHL12.v || (qTKGMFHL7.t & 4) == 0) || n2 <= z && n2 > w && (qTKGMFHL12 = qTKGMFHLArray[n3][n2 - 1]) != null && qTKGMFHL12.w && (qTKGMFHL12.v || (qTKGMFHL7.t & 8) == 0) || n2 >= z && n2 < x - 1 && (qTKGMFHL12 = qTKGMFHLArray[n3][n2 + 1]) != null && qTKGMFHL12.w && (qTKGMFHL12.v || (qTKGMFHL7.t & 2) == 0)) {
                                                                                                                                                continue;
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            bl = true;
                                                                                                                                        }
                                                                                                                                        qTKGMFHL7.v = false;
                                                                                                                                        if (qTKGMFHL7.C == null) break block128;
                                                                                                                                        qTKGMFHL12 = qTKGMFHL7.C;
                                                                                                                                        if (qTKGMFHL12.k == null) break block129;
                                                                                                                                        if (this.g(0, n3, n2)) break block130;
                                                                                                                                        this.a(qTKGMFHL12.k, 0, D, E, F, G, n3, n2);
                                                                                                                                        if (!bl2) break block130;
                                                                                                                                    }
                                                                                                                                    if (qTKGMFHL12.l != null && !this.g(0, n3, n2)) {
                                                                                                                                        this.a(n3, (byte)99, D, F, qTKGMFHL12.l, E, n2, G);
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                if ((fEHPTPDG2 = qTKGMFHL12.m) != null) {
                                                                                                                                    fEHPTPDG2.f.a(0, D, E, F, G, fEHPTPDG2.b - A, fEHPTPDG2.a - B, fEHPTPDG2.c - C, fEHPTPDG2.h);
                                                                                                                                }
                                                                                                                                n16 = 0;
                                                                                                                                boolean bl4 = true;
                                                                                                                                do {
                                                                                                                                    if (bl4 && !(bl4 = false) && !bl2) continue;
                                                                                                                                    object = qTKGMFHL12.r[n16];
                                                                                                                                    if (object != null) {
                                                                                                                                        ((OPNPFUJE)object).e.a(((OPNPFUJE)object).f, D, E, F, G, ((OPNPFUJE)object).c - A, ((OPNPFUJE)object).b - B, ((OPNPFUJE)object).d - C, ((OPNPFUJE)object).m);
                                                                                                                                    }
                                                                                                                                    ++n16;
                                                                                                                                } while (n16 < qTKGMFHL12.q);
                                                                                                                            }
                                                                                                                            bl3 = false;
                                                                                                                            if (qTKGMFHL7.k == null) break block131;
                                                                                                                            if (this.g(n4, n3, n2)) break block132;
                                                                                                                            bl3 = true;
                                                                                                                            this.a(qTKGMFHL7.k, n4, D, E, F, G, n3, n2);
                                                                                                                            if (!bl2) break block132;
                                                                                                                        }
                                                                                                                        if (qTKGMFHL7.l != null && !this.g(n4, n3, n2)) {
                                                                                                                            bl3 = true;
                                                                                                                            this.a(n3, (byte)99, D, F, qTKGMFHL7.l, E, n2, G);
                                                                                                                        }
                                                                                                                    }
                                                                                                                    n17 = 0;
                                                                                                                    n16 = 0;
                                                                                                                    object = qTKGMFHL7.m;
                                                                                                                    oFQAEXFV2 = qTKGMFHL7.n;
                                                                                                                    if (object == null && oFQAEXFV2 == null) break block133;
                                                                                                                    if (y != n3) break block134;
                                                                                                                    ++n17;
                                                                                                                    if (!bl2) break block135;
                                                                                                                }
                                                                                                                if (y < n3) {
                                                                                                                    n17 += 2;
                                                                                                                }
                                                                                                            }
                                                                                                            if (z != n2) break block136;
                                                                                                            n17 += 3;
                                                                                                            if (!bl2) break block137;
                                                                                                        }
                                                                                                        if (z > n2) {
                                                                                                            n17 += 6;
                                                                                                        }
                                                                                                    }
                                                                                                    n16 = X[n17];
                                                                                                    qTKGMFHL7.B = Z[n17];
                                                                                                }
                                                                                                if (object == null) break block138;
                                                                                                if ((((FEHPTPDG)object).d & Y[n17]) == 0) break block139;
                                                                                                if (((FEHPTPDG)object).d != 16) break block140;
                                                                                                qTKGMFHL7.y = 3;
                                                                                                qTKGMFHL7.z = ab[n17];
                                                                                                qTKGMFHL7.A = 3 - qTKGMFHL7.z;
                                                                                                if (!bl2) break block141;
                                                                                            }
                                                                                            if (((FEHPTPDG)object).d != 32) break block142;
                                                                                            qTKGMFHL7.y = 6;
                                                                                            qTKGMFHL7.z = bb[n17];
                                                                                            qTKGMFHL7.A = 6 - qTKGMFHL7.z;
                                                                                            if (!bl2) break block141;
                                                                                        }
                                                                                        if (((FEHPTPDG)object).d != 64) break block143;
                                                                                        qTKGMFHL7.y = 12;
                                                                                        qTKGMFHL7.z = cb[n17];
                                                                                        qTKGMFHL7.A = 12 - qTKGMFHL7.z;
                                                                                        if (!bl2) break block141;
                                                                                    }
                                                                                    qTKGMFHL7.y = 9;
                                                                                    qTKGMFHL7.z = db[n17];
                                                                                    qTKGMFHL7.A = 9 - qTKGMFHL7.z;
                                                                                    if (!bl2) break block141;
                                                                                }
                                                                                qTKGMFHL7.y = 0;
                                                                            }
                                                                            if ((((FEHPTPDG)object).d & n16) != 0 && !this.h(n4, n3, n2, ((FEHPTPDG)object).d)) {
                                                                                ((FEHPTPDG)object).f.a(0, D, E, F, G, ((FEHPTPDG)object).b - A, ((FEHPTPDG)object).a - B, ((FEHPTPDG)object).c - C, ((FEHPTPDG)object).h);
                                                                            }
                                                                            if ((((FEHPTPDG)object).e & n16) != 0 && !this.h(n4, n3, n2, ((FEHPTPDG)object).e)) {
                                                                                ((FEHPTPDG)object).g.a(0, D, E, F, G, ((FEHPTPDG)object).b - A, ((FEHPTPDG)object).a - B, ((FEHPTPDG)object).c - C, ((FEHPTPDG)object).h);
                                                                            }
                                                                        }
                                                                        if (oFQAEXFV2 == null || this.i(n4, n3, n2, oFQAEXFV2.f.k)) break block144;
                                                                        if ((oFQAEXFV2.d & n16) == 0) break block145;
                                                                        oFQAEXFV2.f.a(oFQAEXFV2.e, D, E, F, G, oFQAEXFV2.b - A, oFQAEXFV2.a - B, oFQAEXFV2.c - C, oFQAEXFV2.g);
                                                                        if (!bl2) break block144;
                                                                    }
                                                                    if ((oFQAEXFV2.d & 0x300) == 0) break block144;
                                                                    n11 = oFQAEXFV2.b - A;
                                                                    n10 = oFQAEXFV2.a - B;
                                                                    n9 = oFQAEXFV2.c - C;
                                                                    n8 = oFQAEXFV2.e;
                                                                    if (n8 != 1 && n8 != 2) break block146;
                                                                    n15 = -n11;
                                                                    if (!bl2) break block147;
                                                                }
                                                                n15 = n11;
                                                            }
                                                            if (n8 != 2 && n8 != 3) break block148;
                                                            n12 = -n9;
                                                            if (!bl2) break block149;
                                                        }
                                                        n12 = n9;
                                                    }
                                                    if ((oFQAEXFV2.d & 0x100) != 0 && n12 < n15) {
                                                        n14 = n11 + I[n8];
                                                        n13 = n9 + J[n8];
                                                        oFQAEXFV2.f.a(n8 * 512 + 256, D, E, F, G, n14, n10, n13, oFQAEXFV2.g);
                                                    }
                                                    if ((oFQAEXFV2.d & 0x200) != 0 && n12 > n15) {
                                                        n14 = n11 + K[n8];
                                                        n13 = n9 + L[n8];
                                                        oFQAEXFV2.f.a(n8 * 512 + 1280 & 0x7FF, D, E, F, G, n14, n10, n13, oFQAEXFV2.g);
                                                    }
                                                }
                                                if (bl3) {
                                                    BMEXSMOV bMEXSMOV;
                                                    ZIKPHIFI zIKPHIFI = qTKGMFHL7.o;
                                                    if (zIKPHIFI != null) {
                                                        zIKPHIFI.d.a(0, D, E, F, G, zIKPHIFI.b - A, zIKPHIFI.a - B, zIKPHIFI.c - C, zIKPHIFI.e);
                                                    }
                                                    if ((bMEXSMOV = qTKGMFHL7.p) != null && bMEXSMOV.h == 0) {
                                                        if (bMEXSMOV.e != null) {
                                                            bMEXSMOV.e.a(0, D, E, F, G, bMEXSMOV.b - A, bMEXSMOV.a - B, bMEXSMOV.c - C, bMEXSMOV.g);
                                                        }
                                                        if (bMEXSMOV.f != null) {
                                                            bMEXSMOV.f.a(0, D, E, F, G, bMEXSMOV.b - A, bMEXSMOV.a - B, bMEXSMOV.c - C, bMEXSMOV.g);
                                                        }
                                                        if (bMEXSMOV.d != null) {
                                                            bMEXSMOV.d.a(0, D, E, F, G, bMEXSMOV.b - A, bMEXSMOV.a - B, bMEXSMOV.c - C, bMEXSMOV.g);
                                                        }
                                                    }
                                                }
                                                if ((n11 = qTKGMFHL7.t) != 0) {
                                                    QTKGMFHL qTKGMFHL13;
                                                    QTKGMFHL qTKGMFHL14;
                                                    QTKGMFHL qTKGMFHL15;
                                                    QTKGMFHL qTKGMFHL16;
                                                    if (n3 < y && (n11 & 4) != 0 && (qTKGMFHL16 = qTKGMFHLArray[n3 + 1][n2]) != null && qTKGMFHL16.w) {
                                                        W.a(qTKGMFHL16);
                                                    }
                                                    if (n2 < z && (n11 & 2) != 0 && (qTKGMFHL15 = qTKGMFHLArray[n3][n2 + 1]) != null && qTKGMFHL15.w) {
                                                        W.a(qTKGMFHL15);
                                                    }
                                                    if (n3 > y && (n11 & 1) != 0 && (qTKGMFHL14 = qTKGMFHLArray[n3 - 1][n2]) != null && qTKGMFHL14.w) {
                                                        W.a(qTKGMFHL14);
                                                    }
                                                    if (n2 > z && (n11 & 8) != 0 && (qTKGMFHL13 = qTKGMFHLArray[n3][n2 - 1]) != null && qTKGMFHL13.w) {
                                                        W.a(qTKGMFHL13);
                                                    }
                                                }
                                            }
                                            if (qTKGMFHL7.y != 0) {
                                                boolean bl5 = true;
                                                int n18 = 0;
                                                boolean bl6 = true;
                                                do {
                                                    if (bl6 && !(bl6 = false) && !bl2) continue;
                                                    if (qTKGMFHL7.r[n18].l != t && (qTKGMFHL7.s[n18] & qTKGMFHL7.y) == qTKGMFHL7.z) {
                                                        bl5 = false;
                                                        if (!bl2) break;
                                                    }
                                                    ++n18;
                                                } while (n18 < qTKGMFHL7.q);
                                                if (bl5) {
                                                    FEHPTPDG fEHPTPDG3 = qTKGMFHL7.m;
                                                    if (!this.h(n4, n3, n2, fEHPTPDG3.d)) {
                                                        fEHPTPDG3.f.a(0, D, E, F, G, fEHPTPDG3.b - A, fEHPTPDG3.a - B, fEHPTPDG3.c - C, fEHPTPDG3.h);
                                                    }
                                                    qTKGMFHL7.y = 0;
                                                }
                                            }
                                            if (!qTKGMFHL7.x) break block150;
                                            try {
                                                int n19 = qTKGMFHL7.q;
                                                qTKGMFHL7.x = false;
                                                int n20 = 0;
                                                int n21 = 0;
                                                boolean bl7 = true;
                                                do {
                                                    block126: {
                                                        block151: {
                                                            if (bl7 && !(bl7 = false) && !bl2) continue;
                                                            object = qTKGMFHL7.r[n21];
                                                            if (((OPNPFUJE)object).l == t) break block126;
                                                            int n22 = ((OPNPFUJE)object).g;
                                                            boolean bl8 = true;
                                                            do {
                                                                if (bl8 && !(bl8 = false) && !bl2) continue;
                                                                n11 = ((OPNPFUJE)object).i;
                                                                boolean bl9 = true;
                                                                do {
                                                                    if (bl9 && !(bl9 = false) && !bl2) continue;
                                                                    QTKGMFHL qTKGMFHL17 = qTKGMFHLArray[n22][n11];
                                                                    if (qTKGMFHL17.v) {
                                                                        qTKGMFHL7.x = true;
                                                                        if (!bl2) break block126;
                                                                    }
                                                                    if (qTKGMFHL17.y != 0) {
                                                                        n9 = 0;
                                                                        if (n22 > ((OPNPFUJE)object).g) {
                                                                            ++n9;
                                                                        }
                                                                        if (n22 < ((OPNPFUJE)object).h) {
                                                                            n9 += 4;
                                                                        }
                                                                        if (n11 > ((OPNPFUJE)object).i) {
                                                                            n9 += 8;
                                                                        }
                                                                        if (n11 < ((OPNPFUJE)object).j) {
                                                                            n9 += 2;
                                                                        }
                                                                        if ((n9 & qTKGMFHL17.y) == qTKGMFHL7.A) {
                                                                            qTKGMFHL7.x = true;
                                                                            if (!bl2) break block126;
                                                                        }
                                                                    }
                                                                    ++n11;
                                                                } while (n11 <= ((OPNPFUJE)object).j);
                                                                ++n22;
                                                            } while (n22 <= ((OPNPFUJE)object).h);
                                                            NYFUGYQS.H[n20++] = object;
                                                            int n23 = ((OPNPFUJE)object).h - y;
                                                            n11 = y - ((OPNPFUJE)object).g;
                                                            if (n23 > n11) {
                                                                n11 = n23;
                                                            }
                                                            if ((n8 = ((OPNPFUJE)object).j - z) <= (n9 = z - ((OPNPFUJE)object).i)) break block151;
                                                            ((OPNPFUJE)object).k = n11 + n8;
                                                            if (!bl2) break block126;
                                                        }
                                                        ((OPNPFUJE)object).k = n11 + n9;
                                                    }
                                                    ++n21;
                                                } while (n21 < n19);
                                                boolean bl10 = true;
                                                do {
                                                    if (bl10 && !(bl10 = false) && !bl2) continue;
                                                    int n24 = -50;
                                                    int n25 = -1;
                                                    n11 = 0;
                                                    boolean bl11 = true;
                                                    do {
                                                        block152: {
                                                            int n26;
                                                            OPNPFUJE oPNPFUJE;
                                                            block153: {
                                                                if (bl11 && !(bl11 = false) && !bl2) continue;
                                                                oPNPFUJE = H[n11];
                                                                if (oPNPFUJE.l == t) break block152;
                                                                if (oPNPFUJE.k <= n24) break block153;
                                                                n24 = oPNPFUJE.k;
                                                                n25 = n11;
                                                                if (!bl2) break block152;
                                                            }
                                                            if (oPNPFUJE.k == n24 && (n9 = oPNPFUJE.c - A) * n9 + (n8 = oPNPFUJE.d - C) * n8 > (n26 = NYFUGYQS.H[n25].c - A) * n26 + (n12 = NYFUGYQS.H[n25].d - C) * n12) {
                                                                n25 = n11;
                                                            }
                                                        }
                                                        ++n11;
                                                    } while (n11 < n20);
                                                    if (n25 == -1) break;
                                                    OPNPFUJE oPNPFUJE = H[n25];
                                                    oPNPFUJE.l = t;
                                                    if (!this.a(n4, oPNPFUJE.g, oPNPFUJE.h, oPNPFUJE.i, oPNPFUJE.j, oPNPFUJE.e.k)) {
                                                        oPNPFUJE.e.a(oPNPFUJE.f, D, E, F, G, oPNPFUJE.c - A, oPNPFUJE.b - B, oPNPFUJE.d - C, oPNPFUJE.m);
                                                    }
                                                    n9 = oPNPFUJE.g;
                                                    boolean bl12 = true;
                                                    do {
                                                        if (bl12 && !(bl12 = false) && !bl2) continue;
                                                        n8 = oPNPFUJE.i;
                                                        boolean bl13 = true;
                                                        do {
                                                            block155: {
                                                                QTKGMFHL qTKGMFHL18;
                                                                block154: {
                                                                    if (bl13 && !(bl13 = false) && !bl2) continue;
                                                                    qTKGMFHL18 = qTKGMFHLArray[n9][n8];
                                                                    if (qTKGMFHL18.y == 0) break block154;
                                                                    W.a(qTKGMFHL18);
                                                                    if (!bl2) break block155;
                                                                }
                                                                if ((n9 != n3 || n8 != n2) && qTKGMFHL18.w) {
                                                                    W.a(qTKGMFHL18);
                                                                }
                                                            }
                                                            ++n8;
                                                        } while (n8 <= oPNPFUJE.j);
                                                        ++n9;
                                                    } while (n9 <= oPNPFUJE.h);
                                                } while (n20 > 0);
                                                if (qTKGMFHL7.x) {
                                                    continue;
                                                }
                                            }
                                            catch (Exception exception) {
                                                qTKGMFHL7.x = false;
                                            }
                                        }
                                        if (!qTKGMFHL7.w || qTKGMFHL7.y != 0 || n3 <= y && n3 > u && (qTKGMFHL11 = qTKGMFHLArray[n3 - 1][n2]) != null && qTKGMFHL11.w || n3 >= y && n3 < v - 1 && (qTKGMFHL10 = qTKGMFHLArray[n3 + 1][n2]) != null && qTKGMFHL10.w || n2 <= z && n2 > w && (qTKGMFHL9 = qTKGMFHLArray[n3][n2 - 1]) != null && qTKGMFHL9.w || n2 >= z && n2 < x - 1 && (qTKGMFHL8 = qTKGMFHLArray[n3][n2 + 1]) != null && qTKGMFHL8.w) continue;
                                        qTKGMFHL7.w = false;
                                        --r;
                                        BMEXSMOV bMEXSMOV = qTKGMFHL7.p;
                                        if (bMEXSMOV != null && bMEXSMOV.h != 0) {
                                            if (bMEXSMOV.e != null) {
                                                bMEXSMOV.e.a(0, D, E, F, G, bMEXSMOV.b - A, bMEXSMOV.a - B - bMEXSMOV.h, bMEXSMOV.c - C, bMEXSMOV.g);
                                            }
                                            if (bMEXSMOV.f != null) {
                                                bMEXSMOV.f.a(0, D, E, F, G, bMEXSMOV.b - A, bMEXSMOV.a - B - bMEXSMOV.h, bMEXSMOV.c - C, bMEXSMOV.g);
                                            }
                                            if (bMEXSMOV.d != null) {
                                                bMEXSMOV.d.a(0, D, E, F, G, bMEXSMOV.b - A, bMEXSMOV.a - B - bMEXSMOV.h, bMEXSMOV.c - C, bMEXSMOV.g);
                                            }
                                        }
                                        if (qTKGMFHL7.B == 0) break block156;
                                        oFQAEXFV = qTKGMFHL7.n;
                                        if (oFQAEXFV == null || this.i(n4, n3, n2, oFQAEXFV.f.k)) break block157;
                                        if ((oFQAEXFV.d & qTKGMFHL7.B) == 0) break block158;
                                        oFQAEXFV.f.a(oFQAEXFV.e, D, E, F, G, oFQAEXFV.b - A, oFQAEXFV.a - B, oFQAEXFV.c - C, oFQAEXFV.g);
                                        if (!bl2) break block157;
                                    }
                                    if ((oFQAEXFV.d & 0x300) == 0) break block157;
                                    n7 = oFQAEXFV.b - A;
                                    n6 = oFQAEXFV.a - B;
                                    n5 = oFQAEXFV.c - C;
                                    n11 = oFQAEXFV.e;
                                    if (n11 != 1 && n11 != 2) break block159;
                                    n10 = -n7;
                                    if (!bl2) break block160;
                                }
                                n10 = n7;
                            }
                            if (n11 != 2 && n11 != 3) break block161;
                            n9 = -n5;
                            if (!bl2) break block162;
                        }
                        n9 = n5;
                    }
                    if ((oFQAEXFV.d & 0x100) != 0 && n9 >= n10) {
                        n8 = n7 + I[n11];
                        int n27 = n5 + J[n11];
                        oFQAEXFV.f.a(n11 * 512 + 256, D, E, F, G, n8, n6, n27, oFQAEXFV.g);
                    }
                    if ((oFQAEXFV.d & 0x200) != 0 && n9 <= n10) {
                        n8 = n7 + K[n11];
                        int n28 = n5 + L[n11];
                        oFQAEXFV.f.a(n11 * 512 + 1280 & 0x7FF, D, E, F, G, n8, n6, n28, oFQAEXFV.g);
                    }
                }
                if ((fEHPTPDG = qTKGMFHL7.m) != null) {
                    if ((fEHPTPDG.e & qTKGMFHL7.B) != 0 && !this.h(n4, n3, n2, fEHPTPDG.e)) {
                        fEHPTPDG.g.a(0, D, E, F, G, fEHPTPDG.b - A, fEHPTPDG.a - B, fEHPTPDG.c - C, fEHPTPDG.h);
                    }
                    if ((fEHPTPDG.d & qTKGMFHL7.B) != 0 && !this.h(n4, n3, n2, fEHPTPDG.d)) {
                        fEHPTPDG.f.a(0, D, E, F, G, fEHPTPDG.b - A, fEHPTPDG.a - B, fEHPTPDG.c - C, fEHPTPDG.h);
                    }
                }
            }
            if (n < this.i - 1 && (qTKGMFHL6 = this.m[n + 1][n3][n2]) != null && qTKGMFHL6.w) {
                W.a(qTKGMFHL6);
            }
            if (n3 < y && (qTKGMFHL5 = qTKGMFHLArray[n3 + 1][n2]) != null && qTKGMFHL5.w) {
                W.a(qTKGMFHL5);
            }
            if (n2 < z && (qTKGMFHL4 = qTKGMFHLArray[n3][n2 + 1]) != null && qTKGMFHL4.w) {
                W.a(qTKGMFHL4);
            }
            if (n3 > y && (qTKGMFHL3 = qTKGMFHLArray[n3 - 1][n2]) != null && qTKGMFHL3.w) {
                W.a(qTKGMFHL3);
            }
            if (n2 <= z || (qTKGMFHL2 = qTKGMFHLArray[n3][n2 - 1]) == null || !qTKGMFHL2.w) continue;
            W.a(qTKGMFHL2);
        }
    }

    public void a(XPBACSMK xPBACSMK, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        int n16;
        int n17;
        int n18;
        int n19;
        int n20;
        int n21;
        int n22;
        int n23;
        block17: {
            int n24;
            int n25;
            block19: {
                int n26;
                int n27;
                int n28;
                boolean bl;
                block20: {
                    block18: {
                        bl = XHHRODPC.l;
                        n22 = n23 = (n6 << 7) - A;
                        n20 = n21 = (n7 << 7) - C;
                        n19 = n28 = n22 + 128;
                        n27 = n18 = n20 + 128;
                        n17 = this.l[n][n6][n7] - B;
                        n16 = this.l[n][n6 + 1][n7] - B;
                        n26 = this.l[n][n6 + 1][n7 + 1] - B;
                        n15 = this.l[n][n6][n7 + 1] - B;
                        int n29 = n20 * n4 + n22 * n5 >> 16;
                        n20 = n20 * n5 - n22 * n4 >> 16;
                        n22 = n29;
                        n29 = n17 * n3 - n20 * n2 >> 16;
                        n20 = n17 * n2 + n20 * n3 >> 16;
                        n17 = n29;
                        if (n20 < 50) {
                            return;
                        }
                        n29 = n21 * n4 + n19 * n5 >> 16;
                        n21 = n21 * n5 - n19 * n4 >> 16;
                        n19 = n29;
                        n29 = n16 * n3 - n21 * n2 >> 16;
                        n21 = n16 * n2 + n21 * n3 >> 16;
                        n16 = n29;
                        if (n21 < 50) {
                            return;
                        }
                        n29 = n27 * n4 + n28 * n5 >> 16;
                        n27 = n27 * n5 - n28 * n4 >> 16;
                        n28 = n29;
                        n29 = n26 * n3 - n27 * n2 >> 16;
                        n27 = n26 * n2 + n27 * n3 >> 16;
                        n26 = n29;
                        if (n27 < 50) {
                            return;
                        }
                        n29 = n18 * n4 + n23 * n5 >> 16;
                        n18 = n18 * n5 - n23 * n4 >> 16;
                        n23 = n29;
                        n29 = n15 * n3 - n18 * n2 >> 16;
                        n18 = n15 * n2 + n18 * n3 >> 16;
                        n15 = n29;
                        if (n18 < 50) {
                            return;
                        }
                        n14 = OPPOFIOL.F + (n22 << 9) / n20;
                        n13 = OPPOFIOL.G + (n17 << 9) / n20;
                        n12 = OPPOFIOL.F + (n19 << 9) / n21;
                        n11 = OPPOFIOL.G + (n16 << 9) / n21;
                        n25 = OPPOFIOL.F + (n28 << 9) / n27;
                        n24 = OPPOFIOL.G + (n26 << 9) / n27;
                        n10 = OPPOFIOL.F + (n23 << 9) / n18;
                        n9 = OPPOFIOL.G + (n15 << 9) / n18;
                        OPPOFIOL.E = 0;
                        if ((n25 - n10) * (n11 - n9) - (n24 - n9) * (n12 - n10) <= 0) break block17;
                        OPPOFIOL.B = false;
                        if (n25 < 0 || n10 < 0 || n12 < 0 || n25 > AFCKELYG.t || n10 > AFCKELYG.t || n12 > AFCKELYG.t) {
                            OPPOFIOL.B = true;
                        }
                        if (M && this.a(N, O, n24, n9, n11, n25, n10, n12)) {
                            P = n6;
                            Q = n7;
                        }
                        if (xPBACSMK.e != -1) break block18;
                        if (xPBACSMK.c == 12345678) break block17;
                        OPPOFIOL.a(n24, n9, n11, n25, n10, n12, xPBACSMK.c, xPBACSMK.d, xPBACSMK.b);
                        if (!bl) break block17;
                    }
                    if (h) break block19;
                    if (!xPBACSMK.f) break block20;
                    OPPOFIOL.a(n24, n9, n11, n25, n10, n12, xPBACSMK.c, xPBACSMK.d, xPBACSMK.b, n22, n19, n23, n17, n16, n15, n20, n21, n18, xPBACSMK.e);
                    if (!bl) break block17;
                }
                OPPOFIOL.a(n24, n9, n11, n25, n10, n12, xPBACSMK.c, xPBACSMK.d, xPBACSMK.b, n28, n23, n19, n26, n15, n16, n27, n18, n21, xPBACSMK.e);
                if (!bl) break block17;
            }
            n8 = eb[xPBACSMK.e];
            OPPOFIOL.a(n24, n9, n11, n25, n10, n12, this.f(-361, n8, xPBACSMK.c), this.f(-361, n8, xPBACSMK.d), this.f(-361, n8, xPBACSMK.b));
        }
        if ((n14 - n12) * (n9 - n11) - (n13 - n11) * (n10 - n12) > 0) {
            OPPOFIOL.B = false;
            if (n14 < 0 || n12 < 0 || n10 < 0 || n14 > AFCKELYG.t || n12 > AFCKELYG.t || n10 > AFCKELYG.t) {
                OPPOFIOL.B = true;
            }
            if (M && this.a(N, O, n13, n11, n9, n14, n12, n10)) {
                P = n6;
                Q = n7;
            }
            if (xPBACSMK.e == -1) {
                if (xPBACSMK.a != 12345678) {
                    OPPOFIOL.a(n13, n11, n9, n14, n12, n10, xPBACSMK.a, xPBACSMK.b, xPBACSMK.d);
                    return;
                }
            } else {
                if (!h) {
                    OPPOFIOL.a(n13, n11, n9, n14, n12, n10, xPBACSMK.a, xPBACSMK.b, xPBACSMK.d, n22, n19, n23, n17, n16, n15, n20, n21, n18, xPBACSMK.e);
                    return;
                }
                n8 = eb[xPBACSMK.e];
                OPPOFIOL.a(n13, n11, n9, n14, n12, n10, this.f(-361, n8, xPBACSMK.a), this.f(-361, n8, xPBACSMK.b), this.f(-361, n8, xPBACSMK.d));
            }
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, byte by, int n2, int n3, VBAXKVMG vBAXKVMG, int n4, int n5, int n6) {
        boolean bl = XHHRODPC.l;
        try {
            int n7;
            int n8;
            int n9;
            int n10;
            int n11 = vBAXKVMG.a.length;
            if (by != 99) {
                return;
            }
            int n12 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                n10 = vBAXKVMG.a[n12] - A;
                n9 = vBAXKVMG.b[n12] - B;
                n8 = vBAXKVMG.c[n12] - C;
                n7 = n8 * n3 + n10 * n6 >> 16;
                n8 = n8 * n6 - n10 * n3 >> 16;
                n10 = n7;
                n7 = n9 * n4 - n8 * n2 >> 16;
                n8 = n9 * n2 + n8 * n4 >> 16;
                n9 = n7;
                if (n8 < 50) {
                    return;
                }
                if (vBAXKVMG.j != null) {
                    VBAXKVMG.r[n12] = n10;
                    VBAXKVMG.s[n12] = n9;
                    VBAXKVMG.t[n12] = n8;
                }
                VBAXKVMG.p[n12] = OPPOFIOL.F + (n10 << 9) / n8;
                VBAXKVMG.q[n12] = OPPOFIOL.G + (n9 << 9) / n8;
                ++n12;
            } while (n12 < n11);
            OPPOFIOL.E = 0;
            n11 = vBAXKVMG.g.length;
            n10 = 0;
            boolean bl3 = true;
            do {
                block12: {
                    int n13;
                    int n14;
                    int n15;
                    int n16;
                    int n17;
                    int n18;
                    block14: {
                        block15: {
                            block13: {
                                if (bl3 && !(bl3 = false) && !bl) continue;
                                n9 = vBAXKVMG.g[n10];
                                n18 = VBAXKVMG.p[n9];
                                n8 = vBAXKVMG.h[n10];
                                n17 = VBAXKVMG.p[n8];
                                n7 = vBAXKVMG.i[n10];
                                n16 = VBAXKVMG.q[n7];
                                n15 = VBAXKVMG.q[n8];
                                n14 = VBAXKVMG.q[n9];
                                n13 = VBAXKVMG.p[n7];
                                if ((n18 - n17) * (n16 - n15) - (n14 - n15) * (n13 - n17) <= 0) break block12;
                                OPPOFIOL.B = false;
                                if (n18 < 0 || n17 < 0 || n13 < 0 || n18 > AFCKELYG.t || n17 > AFCKELYG.t || n13 > AFCKELYG.t) {
                                    OPPOFIOL.B = true;
                                }
                                if (M && this.a(N, O, n14, n15, n16, n18, n17, n13)) {
                                    P = n;
                                    Q = n5;
                                }
                                if (vBAXKVMG.j != null && vBAXKVMG.j[n10] != -1) break block13;
                                if (vBAXKVMG.d[n10] == 12345678) break block12;
                                OPPOFIOL.a(n14, n15, n16, n18, n17, n13, vBAXKVMG.d[n10], vBAXKVMG.e[n10], vBAXKVMG.f[n10]);
                                if (!bl) break block12;
                            }
                            if (h) break block14;
                            if (!vBAXKVMG.k) break block15;
                            OPPOFIOL.a(n14, n15, n16, n18, n17, n13, vBAXKVMG.d[n10], vBAXKVMG.e[n10], vBAXKVMG.f[n10], VBAXKVMG.r[0], VBAXKVMG.r[1], VBAXKVMG.r[3], VBAXKVMG.s[0], VBAXKVMG.s[1], VBAXKVMG.s[3], VBAXKVMG.t[0], VBAXKVMG.t[1], VBAXKVMG.t[3], vBAXKVMG.j[n10]);
                            if (!bl) break block12;
                        }
                        OPPOFIOL.a(n14, n15, n16, n18, n17, n13, vBAXKVMG.d[n10], vBAXKVMG.e[n10], vBAXKVMG.f[n10], VBAXKVMG.r[n9], VBAXKVMG.r[n8], VBAXKVMG.r[n7], VBAXKVMG.s[n9], VBAXKVMG.s[n8], VBAXKVMG.s[n7], VBAXKVMG.t[n9], VBAXKVMG.t[n8], VBAXKVMG.t[n7], vBAXKVMG.j[n10]);
                        if (!bl) break block12;
                    }
                    int n19 = eb[vBAXKVMG.j[n10]];
                    OPPOFIOL.a(n14, n15, n16, n18, n17, n13, this.f(-361, n19, vBAXKVMG.d[n10]), this.f(-361, n19, vBAXKVMG.e[n10]), this.f(-361, n19, vBAXKVMG.f[n10]));
                }
                ++n10;
            } while (n10 < n11);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("37932, " + n + ", " + by + ", " + n2 + ", " + n3 + ", " + vBAXKVMG + ", " + n4 + ", " + n5 + ", " + n6 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int f(int n, int n2, int n3) {
        try {
            block8: {
                block7: {
                    n3 = 127 - n3;
                    n3 = n3 * (n2 & 0x7F) / 160;
                    if (n >= 0) {
                        return this.b;
                    }
                    if (n3 >= 2) break block7;
                    n3 = 2;
                    if (!XHHRODPC.l) break block8;
                }
                if (n3 > 126) {
                    n3 = 126;
                }
            }
            return (n2 & 0xFF80) + n3;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("11440, " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public boolean a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (n2 < n3 && n2 < n4 && n2 < n5) {
            return false;
        }
        if (n2 > n3 && n2 > n4 && n2 > n5) {
            return false;
        }
        if (n < n6 && n < n7 && n < n8) {
            return false;
        }
        if (n > n6 && n > n7 && n > n8) {
            return false;
        }
        int n9 = (n2 - n3) * (n7 - n6) - (n - n6) * (n4 - n3);
        int n10 = (n2 - n5) * (n6 - n8) - (n - n8) * (n3 - n5);
        int n11 = (n2 - n4) * (n8 - n7) - (n - n7) * (n5 - n4);
        return n9 * n11 > 0 && n11 * n10 > 0;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void c(int n) {
        boolean bl = XHHRODPC.l;
        try {
            if (n != 0) {
                return;
            }
            int n2 = S[s];
            ZARDZRHZ[] zARDZRHZArray = T[s];
            U = 0;
            int n3 = 0;
            boolean bl2 = true;
            do {
                block23: {
                    int n4;
                    int n5;
                    int n6;
                    int n7;
                    int n8;
                    ZARDZRHZ zARDZRHZ;
                    block26: {
                        block28: {
                            block27: {
                                block22: {
                                    block25: {
                                        block24: {
                                            if (bl2 && !(bl2 = false) && !bl) continue;
                                            zARDZRHZ = zARDZRHZArray[n3];
                                            if (zARDZRHZ.e != 1) break block22;
                                            n8 = zARDZRHZ.a - y + 25;
                                            if (n8 < 0 || n8 > 50) break block23;
                                            n7 = zARDZRHZ.c - z + 25;
                                            if (n7 < 0) {
                                                n7 = 0;
                                            }
                                            if ((n6 = zARDZRHZ.d - z + 25) > 50) {
                                                n6 = 50;
                                            }
                                            n5 = 0;
                                            boolean bl3 = true;
                                            do {
                                                if (bl3 && !(bl3 = false) && !bl) continue;
                                                if (!lb[n8][n7++]) continue;
                                                n5 = 1;
                                                if (!bl) break;
                                            } while (n7 <= n6);
                                            if (n5 == 0) break block23;
                                            n4 = A - zARDZRHZ.f;
                                            if (n4 <= 32) break block24;
                                            zARDZRHZ.l = 1;
                                            if (!bl) break block25;
                                        }
                                        if (n4 >= -32) break block23;
                                        zARDZRHZ.l = 2;
                                        n4 = -n4;
                                    }
                                    zARDZRHZ.o = (zARDZRHZ.h - C << 8) / n4;
                                    zARDZRHZ.p = (zARDZRHZ.i - C << 8) / n4;
                                    zARDZRHZ.q = (zARDZRHZ.j - B << 8) / n4;
                                    zARDZRHZ.r = (zARDZRHZ.k - B << 8) / n4;
                                    NYFUGYQS.V[NYFUGYQS.U++] = zARDZRHZ;
                                    if (!bl) break block23;
                                }
                                if (zARDZRHZ.e != 2) break block26;
                                n8 = zARDZRHZ.c - z + 25;
                                if (n8 < 0 || n8 > 50) break block23;
                                n7 = zARDZRHZ.a - y + 25;
                                if (n7 < 0) {
                                    n7 = 0;
                                }
                                if ((n6 = zARDZRHZ.b - y + 25) > 50) {
                                    n6 = 50;
                                }
                                n5 = 0;
                                boolean bl4 = true;
                                do {
                                    if (bl4 && !(bl4 = false) && !bl) continue;
                                    if (!lb[n7++][n8]) continue;
                                    n5 = 1;
                                    if (!bl) break;
                                } while (n7 <= n6);
                                if (n5 == 0) break block23;
                                n4 = C - zARDZRHZ.h;
                                if (n4 <= 32) break block27;
                                zARDZRHZ.l = 3;
                                if (!bl) break block28;
                            }
                            if (n4 >= -32) break block23;
                            zARDZRHZ.l = 4;
                            n4 = -n4;
                        }
                        zARDZRHZ.m = (zARDZRHZ.f - A << 8) / n4;
                        zARDZRHZ.n = (zARDZRHZ.g - A << 8) / n4;
                        zARDZRHZ.q = (zARDZRHZ.j - B << 8) / n4;
                        zARDZRHZ.r = (zARDZRHZ.k - B << 8) / n4;
                        NYFUGYQS.V[NYFUGYQS.U++] = zARDZRHZ;
                        if (!bl) break block23;
                    }
                    if (zARDZRHZ.e == 4 && (n8 = zARDZRHZ.j - B) > 128) {
                        n7 = zARDZRHZ.c - z + 25;
                        if (n7 < 0) {
                            n7 = 0;
                        }
                        if ((n6 = zARDZRHZ.d - z + 25) > 50) {
                            n6 = 50;
                        }
                        if (n7 <= n6) {
                            n5 = zARDZRHZ.a - y + 25;
                            if (n5 < 0) {
                                n5 = 0;
                            }
                            if ((n4 = zARDZRHZ.b - y + 25) > 50) {
                                n4 = 50;
                            }
                            boolean bl5 = false;
                            int n9 = n5;
                            boolean bl6 = true;
                            block5: do {
                                if (bl6 && !(bl6 = false) && !bl) continue;
                                int n10 = n7;
                                boolean bl7 = true;
                                do {
                                    if (bl7 && !(bl7 = false) && !bl) continue;
                                    if (lb[n9][n10]) {
                                        bl5 = true;
                                        if (!bl) break block5;
                                    }
                                    ++n10;
                                } while (n10 <= n6);
                                ++n9;
                            } while (n9 <= n4);
                            if (bl5) {
                                zARDZRHZ.l = 5;
                                zARDZRHZ.m = (zARDZRHZ.f - A << 8) / n8;
                                zARDZRHZ.n = (zARDZRHZ.g - A << 8) / n8;
                                zARDZRHZ.o = (zARDZRHZ.h - C << 8) / n8;
                                zARDZRHZ.p = (zARDZRHZ.i - C << 8) / n8;
                                NYFUGYQS.V[NYFUGYQS.U++] = zARDZRHZ;
                            }
                        }
                    }
                }
                ++n3;
            } while (n3 < n2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("66730, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private boolean g(int n, int n2, int n3) {
        int n4 = this.q[n][n2][n3];
        if (n4 == -t) {
            return false;
        }
        if (n4 == t) {
            return true;
        }
        int n5 = n2 << 7;
        int n6 = n3 << 7;
        if (this.h(n5 + 1, this.l[n][n2][n3], n6 + 1) && this.h(n5 + 128 - 1, this.l[n][n2 + 1][n3], n6 + 1) && this.h(n5 + 128 - 1, this.l[n][n2 + 1][n3 + 1], n6 + 128 - 1) && this.h(n5 + 1, this.l[n][n2][n3 + 1], n6 + 128 - 1)) {
            this.q[n][n2][n3] = t;
            return true;
        }
        this.q[n][n2][n3] = -t;
        return false;
    }

    private boolean h(int n, int n2, int n3, int n4) {
        if (!this.g(n, n2, n3)) {
            return false;
        }
        int n5 = n2 << 7;
        int n6 = n3 << 7;
        int n7 = this.l[n][n2][n3] - 1;
        int n8 = n7 - 120;
        int n9 = n7 - 230;
        int n10 = n7 - 238;
        if (n4 < 16) {
            if (n4 == 1) {
                if (n5 > A) {
                    if (!this.h(n5, n7, n6)) {
                        return false;
                    }
                    if (!this.h(n5, n7, n6 + 128)) {
                        return false;
                    }
                }
                if (n > 0) {
                    if (!this.h(n5, n8, n6)) {
                        return false;
                    }
                    if (!this.h(n5, n8, n6 + 128)) {
                        return false;
                    }
                }
                if (!this.h(n5, n9, n6)) {
                    return false;
                }
                return this.h(n5, n9, n6 + 128);
            }
            if (n4 == 2) {
                if (n6 < C) {
                    if (!this.h(n5, n7, n6 + 128)) {
                        return false;
                    }
                    if (!this.h(n5 + 128, n7, n6 + 128)) {
                        return false;
                    }
                }
                if (n > 0) {
                    if (!this.h(n5, n8, n6 + 128)) {
                        return false;
                    }
                    if (!this.h(n5 + 128, n8, n6 + 128)) {
                        return false;
                    }
                }
                if (!this.h(n5, n9, n6 + 128)) {
                    return false;
                }
                return this.h(n5 + 128, n9, n6 + 128);
            }
            if (n4 == 4) {
                if (n5 < A) {
                    if (!this.h(n5 + 128, n7, n6)) {
                        return false;
                    }
                    if (!this.h(n5 + 128, n7, n6 + 128)) {
                        return false;
                    }
                }
                if (n > 0) {
                    if (!this.h(n5 + 128, n8, n6)) {
                        return false;
                    }
                    if (!this.h(n5 + 128, n8, n6 + 128)) {
                        return false;
                    }
                }
                if (!this.h(n5 + 128, n9, n6)) {
                    return false;
                }
                return this.h(n5 + 128, n9, n6 + 128);
            }
            if (n4 == 8) {
                if (n6 > C) {
                    if (!this.h(n5, n7, n6)) {
                        return false;
                    }
                    if (!this.h(n5 + 128, n7, n6)) {
                        return false;
                    }
                }
                if (n > 0) {
                    if (!this.h(n5, n8, n6)) {
                        return false;
                    }
                    if (!this.h(n5 + 128, n8, n6)) {
                        return false;
                    }
                }
                if (!this.h(n5, n9, n6)) {
                    return false;
                }
                return this.h(n5 + 128, n9, n6);
            }
        }
        if (!this.h(n5 + 64, n10, n6 + 64)) {
            return false;
        }
        if (n4 == 16) {
            return this.h(n5, n9, n6 + 128);
        }
        if (n4 == 32) {
            return this.h(n5 + 128, n9, n6 + 128);
        }
        if (n4 == 64) {
            return this.h(n5 + 128, n9, n6);
        }
        if (n4 == 128) {
            return this.h(n5, n9, n6);
        }
        System.out.println("Warning unsupported wall type");
        return true;
    }

    private boolean i(int n, int n2, int n3, int n4) {
        if (!this.g(n, n2, n3)) {
            return false;
        }
        int n5 = n2 << 7;
        int n6 = n3 << 7;
        return this.h(n5 + 1, this.l[n][n2][n3] - n4, n6 + 1) && this.h(n5 + 128 - 1, this.l[n][n2 + 1][n3] - n4, n6 + 1) && this.h(n5 + 128 - 1, this.l[n][n2 + 1][n3 + 1] - n4, n6 + 128 - 1) && this.h(n5 + 1, this.l[n][n2][n3 + 1] - n4, n6 + 128 - 1);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private boolean a(int n, int n2, int n3, int n4, int n5, int n6) {
        int n7;
        boolean bl = XHHRODPC.l;
        if (n2 == n3 && n4 == n5) {
            if (!this.g(n, n2, n4)) {
                return false;
            }
            int n8 = n2 << 7;
            int n9 = n4 << 7;
            return this.h(n8 + 1, this.l[n][n2][n4] - n6, n9 + 1) && this.h(n8 + 128 - 1, this.l[n][n2 + 1][n4] - n6, n9 + 1) && this.h(n8 + 128 - 1, this.l[n][n2 + 1][n4 + 1] - n6, n9 + 128 - 1) && this.h(n8 + 1, this.l[n][n2][n4 + 1] - n6, n9 + 128 - 1);
        }
        int n10 = n2;
        boolean bl2 = true;
        do {
            if (bl2 && !(bl2 = false) && !bl) continue;
            n7 = n4;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                if (this.q[n][n10][n7] == -t) {
                    return false;
                }
                ++n7;
            } while (n7 <= n5);
            ++n10;
        } while (n10 <= n3);
        n7 = (n2 << 7) + 1;
        int n11 = this.l[n][n2][n4] - n6;
        int n12 = (n4 << 7) + 2;
        if (!this.h(n7, n11, n12)) {
            return false;
        }
        int n13 = (n3 << 7) - 1;
        if (!this.h(n13, n11, n12)) {
            return false;
        }
        int n14 = (n5 << 7) - 1;
        if (!this.h(n7, n11, n14)) {
            return false;
        }
        return this.h(n13, n11, n14);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private boolean h(int n, int n2, int n3) {
        int n4 = 0;
        boolean bl = true;
        do {
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            if (bl && !(bl = false) && !XHHRODPC.l) continue;
            ZARDZRHZ zARDZRHZ = V[n4];
            if (zARDZRHZ.l == 1) {
                n9 = zARDZRHZ.f - n;
                if (n9 > 0) {
                    n8 = zARDZRHZ.h + (zARDZRHZ.o * n9 >> 8);
                    n7 = zARDZRHZ.i + (zARDZRHZ.p * n9 >> 8);
                    n6 = zARDZRHZ.j + (zARDZRHZ.q * n9 >> 8);
                    n5 = zARDZRHZ.k + (zARDZRHZ.r * n9 >> 8);
                    if (n3 >= n8 && n3 <= n7 && n2 >= n6 && n2 <= n5) {
                        return true;
                    }
                }
            } else if (zARDZRHZ.l == 2) {
                n9 = n - zARDZRHZ.f;
                if (n9 > 0) {
                    n8 = zARDZRHZ.h + (zARDZRHZ.o * n9 >> 8);
                    n7 = zARDZRHZ.i + (zARDZRHZ.p * n9 >> 8);
                    n6 = zARDZRHZ.j + (zARDZRHZ.q * n9 >> 8);
                    n5 = zARDZRHZ.k + (zARDZRHZ.r * n9 >> 8);
                    if (n3 >= n8 && n3 <= n7 && n2 >= n6 && n2 <= n5) {
                        return true;
                    }
                }
            } else if (zARDZRHZ.l == 3) {
                n9 = zARDZRHZ.h - n3;
                if (n9 > 0) {
                    n8 = zARDZRHZ.f + (zARDZRHZ.m * n9 >> 8);
                    n7 = zARDZRHZ.g + (zARDZRHZ.n * n9 >> 8);
                    n6 = zARDZRHZ.j + (zARDZRHZ.q * n9 >> 8);
                    n5 = zARDZRHZ.k + (zARDZRHZ.r * n9 >> 8);
                    if (n >= n8 && n <= n7 && n2 >= n6 && n2 <= n5) {
                        return true;
                    }
                }
            } else if (zARDZRHZ.l == 4) {
                n9 = n3 - zARDZRHZ.h;
                if (n9 > 0) {
                    n8 = zARDZRHZ.f + (zARDZRHZ.m * n9 >> 8);
                    n7 = zARDZRHZ.g + (zARDZRHZ.n * n9 >> 8);
                    n6 = zARDZRHZ.j + (zARDZRHZ.q * n9 >> 8);
                    n5 = zARDZRHZ.k + (zARDZRHZ.r * n9 >> 8);
                    if (n >= n8 && n <= n7 && n2 >= n6 && n2 <= n5) {
                        return true;
                    }
                }
            } else if (zARDZRHZ.l == 5 && (n9 = n2 - zARDZRHZ.j) > 0) {
                n8 = zARDZRHZ.f + (zARDZRHZ.m * n9 >> 8);
                n7 = zARDZRHZ.g + (zARDZRHZ.n * n9 >> 8);
                n6 = zARDZRHZ.h + (zARDZRHZ.o * n9 >> 8);
                n5 = zARDZRHZ.i + (zARDZRHZ.p * n9 >> 8);
                if (n >= n8 && n <= n7 && n3 >= n6 && n3 <= n5) {
                    return true;
                }
            }
            ++n4;
        } while (n4 < U);
        return false;
    }

    static {
        H = new OPNPFUJE[100];
        I = new int[]{53, -53, -53, 53};
        J = new int[]{-53, -53, 53, 53};
        K = new int[]{-45, 45, 45, -45};
        L = new int[]{45, 45, -45, -45};
        P = -1;
        Q = -1;
        R = 4;
        S = new int[R];
        T = new ZARDZRHZ[R][500];
        V = new ZARDZRHZ[500];
        W = new LHGXPZPG(169);
        X = new int[]{19, 55, 38, 155, 255, 110, 137, 205, 76};
        int[] nArray = new int[9];
        nArray[0] = 160;
        nArray[1] = 192;
        nArray[2] = 80;
        nArray[3] = 96;
        nArray[5] = 144;
        nArray[6] = 80;
        nArray[7] = 48;
        nArray[8] = 160;
        Y = nArray;
        int[] nArray2 = new int[9];
        nArray2[0] = 76;
        nArray2[1] = 8;
        nArray2[2] = 137;
        nArray2[3] = 4;
        nArray2[5] = 1;
        nArray2[6] = 38;
        nArray2[7] = 2;
        nArray2[8] = 19;
        Z = nArray2;
        int[] nArray3 = new int[9];
        nArray3[2] = 2;
        nArray3[5] = 2;
        nArray3[6] = 1;
        nArray3[7] = 1;
        ab = nArray3;
        int[] nArray4 = new int[9];
        nArray4[0] = 2;
        nArray4[3] = 2;
        nArray4[7] = 4;
        nArray4[8] = 4;
        bb = nArray4;
        int[] nArray5 = new int[9];
        nArray5[1] = 4;
        nArray5[2] = 4;
        nArray5[3] = 8;
        nArray5[6] = 8;
        cb = nArray5;
        int[] nArray6 = new int[9];
        nArray6[0] = 1;
        nArray6[1] = 1;
        nArray6[5] = 8;
        nArray6[8] = 8;
        db = nArray6;
        eb = new int[]{41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 43086, 41, 41, 41, 41, 41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41, 41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 3131, 41, 41, 41};
        kb = new boolean[8][32][51][51];
    }
}

