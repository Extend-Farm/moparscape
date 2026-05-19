/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

final class CRRWDRTI {
    private static int a = (int)(Math.random() * 17.0) - 8;
    private int[] b;
    private int[] c;
    private int[] d;
    private int[] e;
    private int[] f;
    private int[][][] g;
    private byte[][][] h;
    static int i;
    private boolean j = true;
    private static int k;
    private byte[][][] l;
    private int[][][] m;
    private byte[][][] n;
    private static final int[] o;
    private static int p;
    private int[][] q;
    private static final int[] r;
    private static boolean s;
    private byte[][][] t;
    private boolean u = false;
    private static final int[] v;
    static int w;
    private int x;
    private int y;
    private byte[][][] z;
    private byte[][][] A;
    private int B = -53;
    static boolean C;
    private static final int[] D;
    private static int E;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public CRRWDRTI(byte[][][] byArray, int n, int n2, int n3, int[][][] nArray) {
        try {
            w = 99;
            this.x = n3;
            this.y = n2;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && client.Jj == 0) continue;
                E = -320;
            } while (n >= 0);
            this.g = nArray;
            this.A = byArray;
            this.t = new byte[4][this.x][this.y];
            this.h = new byte[4][this.x][this.y];
            this.n = new byte[4][this.x][this.y];
            this.z = new byte[4][this.x][this.y];
            this.m = new int[4][this.x + 1][this.y + 1];
            this.l = new byte[4][this.x + 1][this.y + 1];
            this.q = new int[this.x + 1][this.y + 1];
            this.b = new int[this.y];
            this.c = new int[this.y];
            this.d = new int[this.y];
            this.e = new int[this.y];
            this.f = new int[this.y];
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("40803, " + byArray + ", " + n + ", " + n2 + ", " + n3 + ", " + nArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final int a(int n, int n2) {
        int n3 = n + n2 * 57;
        n3 = n3 << 13 ^ n3;
        int n4 = n3 * (n3 * n3 * 15731 + 789221) + 1376312589 & Integer.MAX_VALUE;
        return n4 >> 19 & 0xFF;
    }

    /*
     * Exception decompiling
     */
    public final void a(FTPNODIB[] var1_1, NYFUGYQS var2_2, int var3_3) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [22[DOLOOP]], but top level block is 23[WHILELOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private static final int b(int n, int n2) {
        int n3;
        block5: {
            block4: {
                n3 = CRRWDRTI.a(n + 45365, n2 + 91923, 4) - 128 + (CRRWDRTI.a(n + 10294, n2 + 37821, 2) - 128 >> 1) + (CRRWDRTI.a(n, n2, 1) - 128 >> 2);
                if ((n3 = (int)((double)n3 * 0.3) + 35) >= 10) break block4;
                n3 = 10;
                if (client.Jj == 0) break block5;
            }
            if (n3 > 60) {
                n3 = 60;
            }
        }
        return n3;
    }

    public static final void a(byte by, MBMGIXGO mBMGIXGO, GHOWLKWN gHOWLKWN) {
        int n = client.Jj;
        try {
            int n2;
            int n3 = -1;
            if (by != -107) {
                boolean bl = s = !s;
            }
            while ((n2 = mBMGIXGO.k()) != 0) {
                int n4;
                YZDBYLRM yZDBYLRM = YZDBYLRM.a(n3 += n2);
                yZDBYLRM.a(gHOWLKWN, -235);
                while ((n4 = mBMGIXGO.k()) != 0) {
                    mBMGIXGO.c();
                    if (n == 0) continue;
                }
                if (n == 0) continue;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("73405, " + by + ", " + mBMGIXGO + ", " + gHOWLKWN + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, int n2, int n3, int n4, int n5) {
        int n6 = client.Jj;
        try {
            int n7 = n;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n6 == 0) continue;
                int n8 = n5;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n6 == 0) continue;
                    if (n8 >= 0 && n8 < this.x && n7 >= 0 && n7 < this.y) {
                        this.l[0][n8][n7] = 127;
                        if (n8 == n5 && n8 > 0) {
                            this.g[0][n8][n7] = this.g[0][n8 - 1][n7];
                        }
                        if (n8 == n5 + n4 && n8 < this.x - 1) {
                            this.g[0][n8][n7] = this.g[0][n8 + 1][n7];
                        }
                        if (n7 == n && n7 > 0) {
                            this.g[0][n8][n7] = this.g[0][n8][n7 - 1];
                        }
                        if (n7 == n + n2 && n7 < this.y - 1) {
                            this.g[0][n8][n7] = this.g[0][n8][n7 + 1];
                        }
                    }
                    ++n8;
                } while (n8 <= n5 + n4);
                ++n7;
            } while (n7 <= n + n2);
            if (n3 == 0) return;
            E = 284;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("73091, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    private final void a(int var1_1, NYFUGYQS var2_2, FTPNODIB var3_3, int var4_4, int var5_5, int var6_6, int var7_7, boolean var8_8, int var9_9) {
        block96: {
            var26_10 = client.Jj;
            try {
                block158: {
                    block157: {
                        block154: {
                            block156: {
                                block155: {
                                    block151: {
                                        block153: {
                                            block152: {
                                                block148: {
                                                    block150: {
                                                        block149: {
                                                            block145: {
                                                                block147: {
                                                                    block146: {
                                                                        block142: {
                                                                            block144: {
                                                                                block143: {
                                                                                    block139: {
                                                                                        block141: {
                                                                                            block140: {
                                                                                                block132: {
                                                                                                    block135: {
                                                                                                        block138: {
                                                                                                            block137: {
                                                                                                                block136: {
                                                                                                                    block134: {
                                                                                                                        block133: {
                                                                                                                            block125: {
                                                                                                                                block128: {
                                                                                                                                    block131: {
                                                                                                                                        block130: {
                                                                                                                                            block129: {
                                                                                                                                                block127: {
                                                                                                                                                    block126: {
                                                                                                                                                        block118: {
                                                                                                                                                            block121: {
                                                                                                                                                                block124: {
                                                                                                                                                                    block123: {
                                                                                                                                                                        block122: {
                                                                                                                                                                            block120: {
                                                                                                                                                                                block119: {
                                                                                                                                                                                    block111: {
                                                                                                                                                                                        block115: {
                                                                                                                                                                                            block117: {
                                                                                                                                                                                                block116: {
                                                                                                                                                                                                    block114: {
                                                                                                                                                                                                        block113: {
                                                                                                                                                                                                            block112: {
                                                                                                                                                                                                                block108: {
                                                                                                                                                                                                                    block110: {
                                                                                                                                                                                                                        block109: {
                                                                                                                                                                                                                            block100: {
                                                                                                                                                                                                                                block103: {
                                                                                                                                                                                                                                    block107: {
                                                                                                                                                                                                                                        block106: {
                                                                                                                                                                                                                                            block105: {
                                                                                                                                                                                                                                                block104: {
                                                                                                                                                                                                                                                    block102: {
                                                                                                                                                                                                                                                        block101: {
                                                                                                                                                                                                                                                            block97: {
                                                                                                                                                                                                                                                                block99: {
                                                                                                                                                                                                                                                                    block98: {
                                                                                                                                                                                                                                                                        if (CRRWDRTI.C && (this.A[0][var6_6][var1_1] & 2) == 0) {
                                                                                                                                                                                                                                                                            if ((this.A[var5_5][var6_6][var1_1] & 16) != 0) {
                                                                                                                                                                                                                                                                                return;
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                            if (this.a(var1_1, var5_5, var6_6, 0) != CRRWDRTI.i) {
                                                                                                                                                                                                                                                                                return;
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        if (var5_5 < CRRWDRTI.w) {
                                                                                                                                                                                                                                                                            CRRWDRTI.w = var5_5;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        var10_11 = this.g[var5_5][var6_6][var1_1];
                                                                                                                                                                                                                                                                        var11_13 = this.g[var5_5][var6_6 + 1][var1_1];
                                                                                                                                                                                                                                                                        var12_14 = this.g[var5_5][var6_6 + 1][var1_1 + 1];
                                                                                                                                                                                                                                                                        var13_15 = this.g[var5_5][var6_6][var1_1 + 1];
                                                                                                                                                                                                                                                                        var14_16 = var10_11 + var11_13 + var12_14 + var13_15 >> 2;
                                                                                                                                                                                                                                                                        var15_17 = YZDBYLRM.a(var7_7);
                                                                                                                                                                                                                                                                        var16_18 = var6_6 + (var1_1 << 7) + (var7_7 << 14) + 0x40000000;
                                                                                                                                                                                                                                                                        if (!var15_17.Q) {
                                                                                                                                                                                                                                                                            var16_18 += -2147483648;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        var17_19 = (byte)((var9_9 << 6) + var4_4);
                                                                                                                                                                                                                                                                        if (var8_8) {
                                                                                                                                                                                                                                                                            return;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        if (var4_4 != 22) break block97;
                                                                                                                                                                                                                                                                        if (CRRWDRTI.C && !var15_17.Q && !var15_17.a) {
                                                                                                                                                                                                                                                                            return;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        if (var15_17.T != -1 || var15_17.x != null) break block98;
                                                                                                                                                                                                                                                                        var18_20 /* !! */  = var15_17.a(22, var9_9, var10_11, var11_13, var12_14, var13_15, -1);
                                                                                                                                                                                                                                                                        if (var26_10 == 0) break block99;
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    var18_20 /* !! */  = new WBWOBAFW(var7_7, var9_9, 22, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                var2_2.a(var5_5, var14_16, var1_1, 68, var18_20 /* !! */ , var17_19, var16_18, var6_6);
                                                                                                                                                                                                                                                                if (var15_17.F && var15_17.Q && var3_3 != null) {
                                                                                                                                                                                                                                                                    var3_3.a(var1_1, 0, var6_6);
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                return;
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                            if (var4_4 != 10 && var4_4 != 11) break block100;
                                                                                                                                                                                                                                                            if (var15_17.T != -1 || var15_17.x != null) break block101;
                                                                                                                                                                                                                                                            var18_21 /* !! */  = var15_17.a(10, var9_9, var10_11, var11_13, var12_14, var13_15, -1);
                                                                                                                                                                                                                                                            if (var26_10 == 0) break block102;
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        var18_21 /* !! */  = new WBWOBAFW(var7_7, var9_9, 10, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                    if (var18_21 /* !! */  == null) break block103;
                                                                                                                                                                                                                                                    var21_33 = 0;
                                                                                                                                                                                                                                                    if (var4_4 == 11) {
                                                                                                                                                                                                                                                        var21_33 += 256;
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                    if (var9_9 != 1 && var9_9 != 3) break block104;
                                                                                                                                                                                                                                                    var19_34 = var15_17.z;
                                                                                                                                                                                                                                                    var20_37 = var15_17.i;
                                                                                                                                                                                                                                                    if (var26_10 == 0) break block105;
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                var19_34 = var15_17.i;
                                                                                                                                                                                                                                                var20_37 = var15_17.z;
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            if (!var2_2.a(var16_18, var17_19, var14_16, var20_37, var18_21 /* !! */ , var19_34, var5_5, var21_33, (byte)110, var1_1, var6_6) || !var15_17.R) break block103;
                                                                                                                                                                                                                                            if (!(var18_21 /* !! */  instanceof ZKARKDQW)) break block106;
                                                                                                                                                                                                                                            var22_40 /* !! */  = var18_21 /* !! */ ;
                                                                                                                                                                                                                                            if (var26_10 == 0) break block107;
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        var22_40 /* !! */  = var15_17.a(10, var9_9, var10_11, var11_13, var12_14, var13_15, -1);
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (var22_40 /* !! */  == null) break block103;
                                                                                                                                                                                                                                    var23_41 = 0;
                                                                                                                                                                                                                                    if (var26_10 == 0) ** GOTO lbl76
                                                                                                                                                                                                                                    do {
                                                                                                                                                                                                                                        var24_42 = 0;
                                                                                                                                                                                                                                        if (var26_10 == 0) ** GOTO lbl74
                                                                                                                                                                                                                                        do {
                                                                                                                                                                                                                                            if ((var25_43 = var22_40 /* !! */ .W / 4) > 30) {
                                                                                                                                                                                                                                                var25_43 = 30;
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            if (var25_43 > this.l[var5_5][var6_6 + var23_41][var1_1 + var24_42]) {
                                                                                                                                                                                                                                                this.l[var5_5][var6_6 + var23_41][var1_1 + var24_42] = (byte)var25_43;
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            ++var24_42;
lbl74:
                                                                                                                                                                                                                                            // 2 sources

                                                                                                                                                                                                                                        } while (var24_42 <= var20_37);
                                                                                                                                                                                                                                        ++var23_41;
lbl76:
                                                                                                                                                                                                                                        // 2 sources

                                                                                                                                                                                                                                    } while (var23_41 <= var19_34);
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                if (var15_17.F && var3_3 != null) {
                                                                                                                                                                                                                                    var3_3.a(var15_17.v, CRRWDRTI.p, var15_17.i, var15_17.z, var6_6, var1_1, var9_9);
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                return;
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (var4_4 < 12) break block108;
                                                                                                                                                                                                                            if (var15_17.T != -1 || var15_17.x != null) break block109;
                                                                                                                                                                                                                            var18_22 /* !! */  = var15_17.a(var4_4, var9_9, var10_11, var11_13, var12_14, var13_15, -1);
                                                                                                                                                                                                                            if (var26_10 == 0) break block110;
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        var18_22 /* !! */  = new WBWOBAFW(var7_7, var9_9, var4_4, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    var2_2.a(var16_18, var17_19, var14_16, 1, var18_22 /* !! */ , 1, var5_5, 0, (byte)110, var1_1, var6_6);
                                                                                                                                                                                                                    if (var4_4 >= 12 && var4_4 <= 17 && var4_4 != 13 && var5_5 > 0) {
                                                                                                                                                                                                                        v0 = this.m[var5_5][var6_6];
                                                                                                                                                                                                                        v1 = var1_1;
                                                                                                                                                                                                                        v0[v1] = v0[v1] | 2340;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    if (var15_17.F && var3_3 != null) {
                                                                                                                                                                                                                        var3_3.a(var15_17.v, CRRWDRTI.p, var15_17.i, var15_17.z, var6_6, var1_1, var9_9);
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    return;
                                                                                                                                                                                                                }
                                                                                                                                                                                                                if (var4_4 != 0) break block111;
                                                                                                                                                                                                                if (var15_17.T != -1 || var15_17.x != null) break block112;
                                                                                                                                                                                                                var18_23 /* !! */  = var15_17.a(0, var9_9, var10_11, var11_13, var12_14, var13_15, -1);
                                                                                                                                                                                                                if (var26_10 == 0) break block113;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            var18_23 /* !! */  = new WBWOBAFW(var7_7, var9_9, 0, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                                                                                                                                                                        }
                                                                                                                                                                                                        var2_2.a(CRRWDRTI.D[var9_9], var18_23 /* !! */ , true, var16_18, var1_1, var17_19, var6_6, null, var14_16, 0, var5_5);
                                                                                                                                                                                                        if (var9_9 != 0) break block114;
                                                                                                                                                                                                        if (var15_17.R) {
                                                                                                                                                                                                            this.l[var5_5][var6_6][var1_1] = 50;
                                                                                                                                                                                                            this.l[var5_5][var6_6][var1_1 + 1] = 50;
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (!var15_17.C) break block115;
                                                                                                                                                                                                        v2 = this.m[var5_5][var6_6];
                                                                                                                                                                                                        v3 = var1_1;
                                                                                                                                                                                                        v2[v3] = v2[v3] | 585;
                                                                                                                                                                                                        if (var26_10 == 0) break block115;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (var9_9 != 1) break block116;
                                                                                                                                                                                                    if (var15_17.R) {
                                                                                                                                                                                                        this.l[var5_5][var6_6][var1_1 + 1] = 50;
                                                                                                                                                                                                        this.l[var5_5][var6_6 + 1][var1_1 + 1] = 50;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (!var15_17.C) break block115;
                                                                                                                                                                                                    v4 = this.m[var5_5][var6_6];
                                                                                                                                                                                                    v5 = var1_1 + 1;
                                                                                                                                                                                                    v4[v5] = v4[v5] | 1170;
                                                                                                                                                                                                    if (var26_10 == 0) break block115;
                                                                                                                                                                                                }
                                                                                                                                                                                                if (var9_9 != 2) break block117;
                                                                                                                                                                                                if (var15_17.R) {
                                                                                                                                                                                                    this.l[var5_5][var6_6 + 1][var1_1] = 50;
                                                                                                                                                                                                    this.l[var5_5][var6_6 + 1][var1_1 + 1] = 50;
                                                                                                                                                                                                }
                                                                                                                                                                                                if (!var15_17.C) break block115;
                                                                                                                                                                                                v6 = this.m[var5_5][var6_6 + 1];
                                                                                                                                                                                                v7 = var1_1;
                                                                                                                                                                                                v6[v7] = v6[v7] | 585;
                                                                                                                                                                                                if (var26_10 == 0) break block115;
                                                                                                                                                                                            }
                                                                                                                                                                                            if (var9_9 == 3) {
                                                                                                                                                                                                if (var15_17.R) {
                                                                                                                                                                                                    this.l[var5_5][var6_6][var1_1] = 50;
                                                                                                                                                                                                    this.l[var5_5][var6_6 + 1][var1_1] = 50;
                                                                                                                                                                                                }
                                                                                                                                                                                                if (var15_17.C) {
                                                                                                                                                                                                    v8 = this.m[var5_5][var6_6];
                                                                                                                                                                                                    v9 = var1_1;
                                                                                                                                                                                                    v8[v9] = v8[v9] | 1170;
                                                                                                                                                                                                }
                                                                                                                                                                                            }
                                                                                                                                                                                        }
                                                                                                                                                                                        if (var15_17.F && var3_3 != null) {
                                                                                                                                                                                            var3_3.a(var1_1, var9_9, var6_6, var4_4, (byte)1, var15_17.v);
                                                                                                                                                                                        }
                                                                                                                                                                                        if (var15_17.N != 16) {
                                                                                                                                                                                            var2_2.a(var1_1, 441, var15_17.N, var6_6, var5_5);
                                                                                                                                                                                        }
                                                                                                                                                                                        return;
                                                                                                                                                                                    }
                                                                                                                                                                                    if (var4_4 != 1) break block118;
                                                                                                                                                                                    if (var15_17.T != -1 || var15_17.x != null) break block119;
                                                                                                                                                                                    var18_24 /* !! */  = var15_17.a(1, var9_9, var10_11, var11_13, var12_14, var13_15, -1);
                                                                                                                                                                                    if (var26_10 == 0) break block120;
                                                                                                                                                                                }
                                                                                                                                                                                var18_24 /* !! */  = new WBWOBAFW(var7_7, var9_9, 1, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                                                                                                                                            }
                                                                                                                                                                            var2_2.a(CRRWDRTI.r[var9_9], var18_24 /* !! */ , true, var16_18, var1_1, var17_19, var6_6, null, var14_16, 0, var5_5);
                                                                                                                                                                            if (!var15_17.R) break block121;
                                                                                                                                                                            if (var9_9 != 0) break block122;
                                                                                                                                                                            this.l[var5_5][var6_6][var1_1 + 1] = 50;
                                                                                                                                                                            if (var26_10 == 0) break block121;
                                                                                                                                                                        }
                                                                                                                                                                        if (var9_9 != 1) break block123;
                                                                                                                                                                        this.l[var5_5][var6_6 + 1][var1_1 + 1] = 50;
                                                                                                                                                                        if (var26_10 == 0) break block121;
                                                                                                                                                                    }
                                                                                                                                                                    if (var9_9 != 2) break block124;
                                                                                                                                                                    this.l[var5_5][var6_6 + 1][var1_1] = 50;
                                                                                                                                                                    if (var26_10 == 0) break block121;
                                                                                                                                                                }
                                                                                                                                                                if (var9_9 == 3) {
                                                                                                                                                                    this.l[var5_5][var6_6][var1_1] = 50;
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                            if (var15_17.F && var3_3 != null) {
                                                                                                                                                                var3_3.a(var1_1, var9_9, var6_6, var4_4, (byte)1, var15_17.v);
                                                                                                                                                            }
                                                                                                                                                            return;
                                                                                                                                                        }
                                                                                                                                                        if (var4_4 != 2) break block125;
                                                                                                                                                        var18_25 = var9_9 + 1 & 3;
                                                                                                                                                        if (var15_17.T != -1 || var15_17.x != null) break block126;
                                                                                                                                                        var19_35 /* !! */  = var15_17.a(2, 4 + var9_9, var10_11, var11_13, var12_14, var13_15, -1);
                                                                                                                                                        var20_38 /* !! */  = var15_17.a(2, var18_25, var10_11, var11_13, var12_14, var13_15, -1);
                                                                                                                                                        if (var26_10 == 0) break block127;
                                                                                                                                                    }
                                                                                                                                                    var19_35 /* !! */  = new WBWOBAFW(var7_7, 4 + var9_9, 2, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                                                                                                                    var20_38 /* !! */  = new WBWOBAFW(var7_7, var18_25, 2, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                                                                                                                }
                                                                                                                                                var2_2.a(CRRWDRTI.D[var9_9], var19_35 /* !! */ , true, var16_18, var1_1, var17_19, var6_6, var20_38 /* !! */ , var14_16, CRRWDRTI.D[var18_25], var5_5);
                                                                                                                                                if (!var15_17.C) break block128;
                                                                                                                                                if (var9_9 != 0) break block129;
                                                                                                                                                v10 = this.m[var5_5][var6_6];
                                                                                                                                                v11 = var1_1;
                                                                                                                                                v10[v11] = v10[v11] | 585;
                                                                                                                                                v12 = this.m[var5_5][var6_6];
                                                                                                                                                v13 = var1_1 + 1;
                                                                                                                                                v12[v13] = v12[v13] | 1170;
                                                                                                                                                if (var26_10 == 0) break block128;
                                                                                                                                            }
                                                                                                                                            if (var9_9 != 1) break block130;
                                                                                                                                            v14 = this.m[var5_5][var6_6];
                                                                                                                                            v15 = var1_1 + 1;
                                                                                                                                            v14[v15] = v14[v15] | 1170;
                                                                                                                                            v16 = this.m[var5_5][var6_6 + 1];
                                                                                                                                            v17 = var1_1;
                                                                                                                                            v16[v17] = v16[v17] | 585;
                                                                                                                                            if (var26_10 == 0) break block128;
                                                                                                                                        }
                                                                                                                                        if (var9_9 != 2) break block131;
                                                                                                                                        v18 = this.m[var5_5][var6_6 + 1];
                                                                                                                                        v19 = var1_1;
                                                                                                                                        v18[v19] = v18[v19] | 585;
                                                                                                                                        v20 = this.m[var5_5][var6_6];
                                                                                                                                        v21 = var1_1;
                                                                                                                                        v20[v21] = v20[v21] | 1170;
                                                                                                                                        if (var26_10 == 0) break block128;
                                                                                                                                    }
                                                                                                                                    if (var9_9 == 3) {
                                                                                                                                        v22 = this.m[var5_5][var6_6];
                                                                                                                                        v23 = var1_1;
                                                                                                                                        v22[v23] = v22[v23] | 1170;
                                                                                                                                        v24 = this.m[var5_5][var6_6];
                                                                                                                                        v25 = var1_1;
                                                                                                                                        v24[v25] = v24[v25] | 585;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                if (var15_17.F && var3_3 != null) {
                                                                                                                                    var3_3.a(var1_1, var9_9, var6_6, var4_4, (byte)1, var15_17.v);
                                                                                                                                }
                                                                                                                                if (var15_17.N != 16) {
                                                                                                                                    var2_2.a(var1_1, 441, var15_17.N, var6_6, var5_5);
                                                                                                                                }
                                                                                                                                return;
                                                                                                                            }
                                                                                                                            if (var4_4 != 3) break block132;
                                                                                                                            if (var15_17.T != -1 || var15_17.x != null) break block133;
                                                                                                                            var18_26 /* !! */  = var15_17.a(3, var9_9, var10_11, var11_13, var12_14, var13_15, -1);
                                                                                                                            if (var26_10 == 0) break block134;
                                                                                                                        }
                                                                                                                        var18_26 /* !! */  = new WBWOBAFW(var7_7, var9_9, 3, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                                                                                    }
                                                                                                                    var2_2.a(CRRWDRTI.r[var9_9], var18_26 /* !! */ , true, var16_18, var1_1, var17_19, var6_6, null, var14_16, 0, var5_5);
                                                                                                                    if (!var15_17.R) break block135;
                                                                                                                    if (var9_9 != 0) break block136;
                                                                                                                    this.l[var5_5][var6_6][var1_1 + 1] = 50;
                                                                                                                    if (var26_10 == 0) break block135;
                                                                                                                }
                                                                                                                if (var9_9 != 1) break block137;
                                                                                                                this.l[var5_5][var6_6 + 1][var1_1 + 1] = 50;
                                                                                                                if (var26_10 == 0) break block135;
                                                                                                            }
                                                                                                            if (var9_9 != 2) break block138;
                                                                                                            this.l[var5_5][var6_6 + 1][var1_1] = 50;
                                                                                                            if (var26_10 == 0) break block135;
                                                                                                        }
                                                                                                        if (var9_9 == 3) {
                                                                                                            this.l[var5_5][var6_6][var1_1] = 50;
                                                                                                        }
                                                                                                    }
                                                                                                    if (var15_17.F && var3_3 != null) {
                                                                                                        var3_3.a(var1_1, var9_9, var6_6, var4_4, (byte)1, var15_17.v);
                                                                                                    }
                                                                                                    return;
                                                                                                }
                                                                                                if (var4_4 != 9) break block139;
                                                                                                if (var15_17.T != -1 || var15_17.x != null) break block140;
                                                                                                var18_27 /* !! */  = var15_17.a(var4_4, var9_9, var10_11, var11_13, var12_14, var13_15, -1);
                                                                                                if (var26_10 == 0) break block141;
                                                                                            }
                                                                                            var18_27 /* !! */  = new WBWOBAFW(var7_7, var9_9, var4_4, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                                                        }
                                                                                        var2_2.a(var16_18, var17_19, var14_16, 1, var18_27 /* !! */ , 1, var5_5, 0, (byte)110, var1_1, var6_6);
                                                                                        if (var15_17.F && var3_3 != null) {
                                                                                            var3_3.a(var15_17.v, CRRWDRTI.p, var15_17.i, var15_17.z, var6_6, var1_1, var9_9);
                                                                                        }
                                                                                        return;
                                                                                    }
                                                                                    if (!var15_17.A) break block142;
                                                                                    if (var9_9 != 1) break block143;
                                                                                    var18_28 = var13_15;
                                                                                    var13_15 = var12_14;
                                                                                    var12_14 = var11_13;
                                                                                    var11_13 = var10_11;
                                                                                    var10_11 = var18_28;
                                                                                    if (var26_10 == 0) break block142;
                                                                                }
                                                                                if (var9_9 != 2) break block144;
                                                                                var18_28 = var13_15;
                                                                                var13_15 = var11_13;
                                                                                var11_13 = var18_28;
                                                                                var18_28 = var12_14;
                                                                                var12_14 = var10_11;
                                                                                var10_11 = var18_28;
                                                                                if (var26_10 == 0) break block142;
                                                                            }
                                                                            if (var9_9 == 3) {
                                                                                var18_28 = var13_15;
                                                                                var13_15 = var10_11;
                                                                                var10_11 = var11_13;
                                                                                var11_13 = var12_14;
                                                                                var12_14 = var18_28;
                                                                            }
                                                                        }
                                                                        if (var4_4 != 4) break block145;
                                                                        if (var15_17.T != -1 || var15_17.x != null) break block146;
                                                                        var18_29 /* !! */  = var15_17.a(4, 0, var10_11, var11_13, var12_14, var13_15, -1);
                                                                        if (var26_10 == 0) break block147;
                                                                    }
                                                                    var18_29 /* !! */  = new WBWOBAFW(var7_7, 0, 4, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                                }
                                                                var2_2.a(var16_18, var1_1, var9_9 * 512, -460, var5_5, 0, var14_16, var18_29 /* !! */ , var6_6, var17_19, 0, CRRWDRTI.D[var9_9]);
                                                                return;
                                                            }
                                                            if (var4_4 != 5) break block148;
                                                            var18_28 = 16;
                                                            var19_36 = var2_2.c(var5_5, var6_6, var1_1);
                                                            if (var19_36 > 0) {
                                                                var18_28 = YZDBYLRM.a((int)(var19_36 >> 14 & 32767)).N;
                                                            }
                                                            if (var15_17.T != -1 || var15_17.x != null) break block149;
                                                            var20_39 /* !! */  = var15_17.a(4, 0, var10_11, var11_13, var12_14, var13_15, -1);
                                                            if (var26_10 == 0) break block150;
                                                        }
                                                        var20_39 /* !! */  = new WBWOBAFW(var7_7, 0, 4, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                                    }
                                                    var2_2.a(var16_18, var1_1, var9_9 * 512, -460, var5_5, CRRWDRTI.o[var9_9] * var18_28, var14_16, var20_39 /* !! */ , var6_6, var17_19, CRRWDRTI.v[var9_9] * var18_28, CRRWDRTI.D[var9_9]);
                                                    return;
                                                }
                                                if (var4_4 != 6) break block151;
                                                if (var15_17.T != -1 || var15_17.x != null) break block152;
                                                var18_30 /* !! */  = var15_17.a(4, 0, var10_11, var11_13, var12_14, var13_15, -1);
                                                if (var26_10 == 0) break block153;
                                            }
                                            var18_30 /* !! */  = new WBWOBAFW(var7_7, 0, 4, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                                        }
                                        var2_2.a(var16_18, var1_1, var9_9, -460, var5_5, 0, var14_16, var18_30 /* !! */ , var6_6, var17_19, 0, 256);
                                        return;
                                    }
                                    if (var4_4 != 7) break block154;
                                    if (var15_17.T != -1 || var15_17.x != null) break block155;
                                    var18_31 /* !! */  = var15_17.a(4, 0, var10_11, var11_13, var12_14, var13_15, -1);
                                    if (var26_10 == 0) break block156;
                                }
                                var18_31 /* !! */  = new WBWOBAFW(var7_7, 0, 4, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                            }
                            var2_2.a(var16_18, var1_1, var9_9, -460, var5_5, 0, var14_16, var18_31 /* !! */ , var6_6, var17_19, 0, 512);
                            return;
                        }
                        if (var4_4 != 8) break block96;
                        if (var15_17.T != -1 || var15_17.x != null) break block157;
                        var18_32 /* !! */  = var15_17.a(4, 0, var10_11, var11_13, var12_14, var13_15, -1);
                        if (var26_10 == 0) break block158;
                    }
                    var18_32 /* !! */  = new WBWOBAFW(var7_7, 0, 4, var11_13, 7, var12_14, var10_11, var13_15, var15_17.T, true);
                }
                var2_2.a(var16_18, var1_1, var9_9, -460, var5_5, 0, var14_16, var18_32 /* !! */ , var6_6, var17_19, 0, 768);
                return;
            }
            catch (RuntimeException var10_12) {
                signlink.reporterror("16863, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_4 + ", " + var5_5 + ", " + var6_6 + ", " + var7_7 + ", " + var8_8 + ", " + var9_9 + ", " + var10_12.toString());
                throw new RuntimeException();
            }
        }
    }

    private static final int a(int n, int n2, int n3) {
        int n4 = n / n3;
        int n5 = n & n3 - 1;
        int n6 = n2 / n3;
        int n7 = n2 & n3 - 1;
        int n8 = CRRWDRTI.d(n4, n6);
        int n9 = CRRWDRTI.d(n4 + 1, n6);
        int n10 = CRRWDRTI.d(n4, n6 + 1);
        int n11 = CRRWDRTI.d(n4 + 1, n6 + 1);
        int n12 = CRRWDRTI.b(n8, n9, n5, n3);
        int n13 = CRRWDRTI.b(n10, n11, n5, n3);
        return CRRWDRTI.b(n12, n13, n7, n3);
    }

    private final int b(int n, int n2, int n3) {
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

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final boolean c(int n, int n2, int n3) {
        try {
            YZDBYLRM yZDBYLRM = YZDBYLRM.a(n);
            if (n3 != 8) {
                int n4 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && client.Jj == 0) continue;
                    ++n4;
                } while (n4 > 0);
            }
            if (n2 == 11) {
                n2 = 10;
            }
            if (n2 >= 5 && n2 <= 8) {
                n2 = 4;
            }
            return yZDBYLRM.a(n2, true);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("51637, " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, int n2, FTPNODIB[] fTPNODIBArray, int n3, int n4, int n5, byte[] byArray, int n6, int n7, int n8) {
        int n9 = client.Jj;
        try {
            int n10;
            int n11 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n9 == 0) continue;
                n10 = 0;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n9 == 0) continue;
                    if (n4 + n11 > 0 && n4 + n11 < 103 && n8 + n10 > 0 && n8 + n10 < 103) {
                        int[] nArray = fTPNODIBArray[n7].m[n4 + n11];
                        int n12 = n8 + n10;
                        nArray[n12] = nArray[n12] & 0xFEFFFFFF;
                    }
                    ++n10;
                } while (n10 < 8);
                ++n11;
            } while (n11 < 8);
            if (n3 < 9 || n3 > 9) {
                n10 = 1;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && n9 == 0) continue;
                    ++n10;
                } while (n10 > 0);
            }
            MBMGIXGO mBMGIXGO = new MBMGIXGO(byArray, 891);
            int n13 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && n9 == 0) continue;
                int n14 = 0;
                boolean bl5 = true;
                do {
                    if (bl5 && !(bl5 = false) && n9 == 0) continue;
                    int n15 = 0;
                    boolean bl6 = true;
                    do {
                        block15: {
                            block14: {
                                if (bl6 && !(bl6 = false) && n9 == 0) continue;
                                if (n13 != n || n14 < n5 || n14 >= n5 + 8 || n15 < n6 || n15 >= n6 + 8) break block14;
                                this.a(n8 + CDEJWOSB.a(n15 & 7, n2, -383, n14 & 7), 0, mBMGIXGO, n4 + CDEJWOSB.a(n2, n15 & 7, n14 & 7, false), n7, n2, 942, 0);
                                if (n9 == 0) break block15;
                            }
                            this.a(-1, 0, mBMGIXGO, -1, 0, 0, 942, 0);
                        }
                        ++n15;
                    } while (n15 < 64);
                    ++n14;
                } while (n14 < 64);
                ++n13;
            } while (n13 < 4);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("28153, " + n + ", " + n2 + ", " + fTPNODIBArray + ", " + n3 + ", " + n4 + ", " + n5 + ", " + byArray + ", " + n6 + ", " + n7 + ", " + n8 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(byte[] byArray, int n, int n2, int n3, int n4, byte by, FTPNODIB[] fTPNODIBArray) {
        int n5 = client.Jj;
        try {
            int n6;
            int n7 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n5 == 0) continue;
                int n8 = 0;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n5 == 0) continue;
                    n6 = 0;
                    boolean bl3 = true;
                    do {
                        if (bl3 && !(bl3 = false) && n5 == 0) continue;
                        if (n2 + n8 > 0 && n2 + n8 < 103 && n + n6 > 0 && n + n6 < 103) {
                            int[] nArray = fTPNODIBArray[n7].m[n2 + n8];
                            int n9 = n + n6;
                            nArray[n9] = nArray[n9] & 0xFEFFFFFF;
                        }
                        ++n6;
                    } while (n6 < 64);
                    ++n8;
                } while (n8 < 64);
                ++n7;
            } while (n7 < 4);
            MBMGIXGO mBMGIXGO = new MBMGIXGO(byArray, 891);
            n6 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && n5 == 0) continue;
                int n10 = 0;
                boolean bl5 = true;
                do {
                    if (bl5 && !(bl5 = false) && n5 == 0) continue;
                    int n11 = 0;
                    boolean bl6 = true;
                    do {
                        if (bl6 && !(bl6 = false) && n5 == 0) continue;
                        this.a(n11 + n, n4, mBMGIXGO, n10 + n2, n6, 0, 942, n3);
                        ++n11;
                    } while (n11 < 64);
                    ++n10;
                } while (n10 < 64);
                ++n6;
            } while (n6 < 4);
            if (by == 4) return;
            this.u = !this.u;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("41691, " + byArray + ", " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + by + ", " + fTPNODIBArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private final void a(int var1_1, int var2_2, MBMGIXGO var3_3, int var4_4, int var5_5, int var6_6, int var7_7, int var8_8) {
        var11_9 = client.Jj;
        try {
            block10: {
                var7_7 = 36 / var7_7;
                if (var4_4 < 0 || var4_4 >= 104 || var1_1 < 0 || var1_1 >= 104) break block10;
                this.A[var5_5][var4_4][var1_1] = 0;
                do lbl-1000:
                // 4 sources

                {
                    block12: {
                        block11: {
                            if ((var9_10 = var3_3.c()) == 0) {
                                if (var5_5 == 0) {
                                    this.g[0][var4_4][var1_1] = -CRRWDRTI.b(932731 + var4_4 + var8_8, 556238 + var1_1 + var2_2) * 8;
                                    return;
                                }
                                this.g[var5_5][var4_4][var1_1] = this.g[var5_5 - 1][var4_4][var1_1] - 240;
                                return;
                            }
                            if (var9_10 == 1) {
                                var10_12 = var3_3.c();
                                if (var10_12 == 1) {
                                    var10_12 = 0;
                                }
                                if (var5_5 == 0) {
                                    this.g[0][var4_4][var1_1] = -var10_12 * 8;
                                    return;
                                }
                                this.g[var5_5][var4_4][var1_1] = this.g[var5_5 - 1][var4_4][var1_1] - var10_12 * 8;
                                return;
                            }
                            if (var9_10 > 49) break block11;
                            this.h[var5_5][var4_4][var1_1] = var3_3.d();
                            this.n[var5_5][var4_4][var1_1] = (byte)((var9_10 - 2) / 4);
                            this.z[var5_5][var4_4][var1_1] = (byte)(var9_10 - 2 + var6_6 & 3);
                            if (var11_9 == 0) ** GOTO lbl-1000
                        }
                        if (var9_10 > 81) break block12;
                        this.A[var5_5][var4_4][var1_1] = (byte)(var9_10 - 49);
                        if (var11_9 == 0) ** GOTO lbl-1000
                    }
                    this.t[var5_5][var4_4][var1_1] = (byte)(var9_10 - 81);
                } while (var11_9 == 0);
            }
            while ((var9_10 = var3_3.c()) != 0) {
                if (var9_10 == 1) {
                    var3_3.c();
                    return;
                }
                if (var9_10 > 49) continue;
                var3_3.c();
                if (var11_9 == 0) continue;
            }
            return;
        }
        catch (RuntimeException var9_11) {
            signlink.reporterror("30203, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_4 + ", " + var5_5 + ", " + var6_6 + ", " + var7_7 + ", " + var8_8 + ", " + var9_11.toString());
            throw new RuntimeException();
        }
    }

    public int a(int n, int n2, int n3, int n4) {
        try {
            if (n4 != 0) {
                return 2;
            }
            if ((this.A[n2][n3][n] & 8) != 0) {
                return 0;
            }
            if (n2 > 0 && (this.A[1][n3][n] & 2) != 0) {
                return n2 - 1;
            }
            return n2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("5828, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(FTPNODIB[] fTPNODIBArray, NYFUGYQS nYFUGYQS, int n, int n2, int n3, boolean bl, int n4, byte[] byArray, int n5, int n6, int n7) {
        int n8 = client.Jj;
        try {
            int n9;
            MBMGIXGO mBMGIXGO = new MBMGIXGO(byArray, 891);
            int n10 = -1;
            if (!bl) {
                boolean bl2 = this.u = !this.u;
            }
            while ((n9 = mBMGIXGO.k()) != 0) {
                int n11;
                n10 += n9;
                int n12 = 0;
                while ((n11 = mBMGIXGO.k()) != 0) {
                    int n13 = (n12 += n11 - 1) & 0x3F;
                    int n14 = n12 >> 6 & 0x3F;
                    int n15 = n12 >> 12;
                    int n16 = mBMGIXGO.c();
                    int n17 = n16 >> 2;
                    int n18 = n16 & 3;
                    if (n15 != n || n14 < n5 || n14 >= n5 + 8 || n13 < n3 || n13 >= n3 + 8) continue;
                    YZDBYLRM yZDBYLRM = YZDBYLRM.a(n10);
                    int n19 = n2 + CDEJWOSB.a(n6, yZDBYLRM.z, n14 & 7, (byte)113, n13 & 7, yZDBYLRM.i);
                    int n20 = n7 + CDEJWOSB.a(-433, n13 & 7, yZDBYLRM.z, n6, yZDBYLRM.i, n14 & 7);
                    if (n19 <= 0 || n20 <= 0 || n19 >= 103 || n20 >= 103) continue;
                    int n21 = n15;
                    if ((this.A[1][n19][n20] & 2) == 2) {
                        --n21;
                    }
                    FTPNODIB fTPNODIB = null;
                    if (n21 >= 0) {
                        fTPNODIB = fTPNODIBArray[n21];
                    }
                    this.a(n20, nYFUGYQS, fTPNODIB, n17, n4, n19, n10, false, n18 + n6 & 3);
                    if (n8 == 0) continue;
                }
                if (n8 == 0) continue;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("1483, " + fTPNODIBArray + ", " + nYFUGYQS + ", " + n + ", " + n2 + ", " + n3 + ", " + bl + ", " + n4 + ", " + byArray + ", " + n5 + ", " + n6 + ", " + n7 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final int b(int n, int n2, int n3, int n4) {
        int n5 = 65536 - OPPOFIOL.K[n3 * 1024 / n4] >> 1;
        return (n * (65536 - n5) >> 16) + (n2 * n5 >> 16);
    }

    private final int c(int n, int n2) {
        block13: {
            block12: {
                int n3;
                block9: {
                    block11: {
                        block10: {
                            n3 = client.Jj;
                            if (n == -2) {
                                return 12345678;
                            }
                            if (n != -1) break block9;
                            if (n2 >= 0) break block10;
                            n2 = 0;
                            if (n3 == 0) break block11;
                        }
                        if (n2 > 127) {
                            n2 = 127;
                        }
                    }
                    n2 = 127 - n2;
                    return n2;
                }
                if ((n2 = n2 * (n & 0x7F) / 128) >= 2) break block12;
                n2 = 2;
                if (n3 == 0) break block13;
            }
            if (n2 > 126) {
                n2 = 126;
            }
        }
        return (n & 0xFF80) + n2;
    }

    private static final int d(int n, int n2) {
        int n3 = CRRWDRTI.a(n - 1, n2 - 1) + CRRWDRTI.a(n + 1, n2 - 1) + CRRWDRTI.a(n - 1, n2 + 1) + CRRWDRTI.a(n + 1, n2 + 1);
        int n4 = CRRWDRTI.a(n - 1, n2) + CRRWDRTI.a(n + 1, n2) + CRRWDRTI.a(n, n2 - 1) + CRRWDRTI.a(n, n2 + 1);
        int n5 = CRRWDRTI.a(n, n2);
        return n3 / 16 + n4 / 8 + n5 / 4;
    }

    private static final int e(int n, int n2) {
        block6: {
            block5: {
                if (n == -1) {
                    return 12345678;
                }
                if ((n2 = n2 * (n & 0x7F) / 128) >= 2) break block5;
                n2 = 2;
                if (client.Jj == 0) break block6;
            }
            if (n2 > 126) {
                n2 = 126;
            }
        }
        return (n & 0xFF80) + n2;
    }

    public static final void a(NYFUGYQS nYFUGYQS, int n, int n2, int n3, int n4, FTPNODIB fTPNODIB, int[][][] nArray, int n5, int n6, int n7, byte by) {
        block60: {
            int n8 = client.Jj;
            try {
                XHHRODPC xHHRODPC;
                byte by2;
                int n9;
                int n10;
                block104: {
                    YZDBYLRM yZDBYLRM;
                    int n11;
                    int n12;
                    int n13;
                    int n14;
                    block103: {
                        block100: {
                            XHHRODPC xHHRODPC2;
                            block102: {
                                block101: {
                                    block97: {
                                        XHHRODPC xHHRODPC3;
                                        block99: {
                                            block98: {
                                                block94: {
                                                    XHHRODPC xHHRODPC4;
                                                    int n15;
                                                    block96: {
                                                        block95: {
                                                            block91: {
                                                                XHHRODPC xHHRODPC5;
                                                                block93: {
                                                                    block92: {
                                                                        block88: {
                                                                            block90: {
                                                                                block89: {
                                                                                    block85: {
                                                                                        XHHRODPC xHHRODPC6;
                                                                                        block87: {
                                                                                            block86: {
                                                                                                block82: {
                                                                                                    XHHRODPC xHHRODPC7;
                                                                                                    block84: {
                                                                                                        block83: {
                                                                                                            block79: {
                                                                                                                XHHRODPC xHHRODPC8;
                                                                                                                XHHRODPC xHHRODPC9;
                                                                                                                int n16;
                                                                                                                block81: {
                                                                                                                    block80: {
                                                                                                                        block76: {
                                                                                                                            XHHRODPC xHHRODPC10;
                                                                                                                            block78: {
                                                                                                                                block77: {
                                                                                                                                    block73: {
                                                                                                                                        XHHRODPC xHHRODPC11;
                                                                                                                                        block75: {
                                                                                                                                            block74: {
                                                                                                                                                block70: {
                                                                                                                                                    XHHRODPC xHHRODPC12;
                                                                                                                                                    block72: {
                                                                                                                                                        block71: {
                                                                                                                                                            block64: {
                                                                                                                                                                block67: {
                                                                                                                                                                    int n17;
                                                                                                                                                                    int n18;
                                                                                                                                                                    int n19;
                                                                                                                                                                    XHHRODPC xHHRODPC13;
                                                                                                                                                                    block69: {
                                                                                                                                                                        block68: {
                                                                                                                                                                            block66: {
                                                                                                                                                                                block65: {
                                                                                                                                                                                    block61: {
                                                                                                                                                                                        XHHRODPC xHHRODPC14;
                                                                                                                                                                                        block63: {
                                                                                                                                                                                            block62: {
                                                                                                                                                                                                n14 = nArray[n4][n5][n2];
                                                                                                                                                                                                n13 = nArray[n4][n5 + 1][n2];
                                                                                                                                                                                                n12 = nArray[n4][n5 + 1][n2 + 1];
                                                                                                                                                                                                n11 = nArray[n4][n5][n2 + 1];
                                                                                                                                                                                                if (by != 93) {
                                                                                                                                                                                                    E = -145;
                                                                                                                                                                                                }
                                                                                                                                                                                                n10 = n14 + n13 + n12 + n11 >> 2;
                                                                                                                                                                                                yZDBYLRM = YZDBYLRM.a(n6);
                                                                                                                                                                                                n9 = n5 + (n2 << 7) + (n6 << 14) + 0x40000000;
                                                                                                                                                                                                if (!yZDBYLRM.Q) {
                                                                                                                                                                                                    n9 += Integer.MIN_VALUE;
                                                                                                                                                                                                }
                                                                                                                                                                                                by2 = (byte)((n << 6) + n3);
                                                                                                                                                                                                if (n3 != 22) break block61;
                                                                                                                                                                                                if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block62;
                                                                                                                                                                                                xHHRODPC14 = yZDBYLRM.a(22, n, n14, n13, n12, n11, -1);
                                                                                                                                                                                                if (n8 == 0) break block63;
                                                                                                                                                                                            }
                                                                                                                                                                                            xHHRODPC14 = new WBWOBAFW(n6, n, 22, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                                                                                                                                                        }
                                                                                                                                                                                        nYFUGYQS.a(n7, n10, n2, 68, xHHRODPC14, by2, n9, n5);
                                                                                                                                                                                        if (yZDBYLRM.F && yZDBYLRM.Q) {
                                                                                                                                                                                            fTPNODIB.a(n2, 0, n5);
                                                                                                                                                                                        }
                                                                                                                                                                                        return;
                                                                                                                                                                                    }
                                                                                                                                                                                    if (n3 != 10 && n3 != 11) break block64;
                                                                                                                                                                                    if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block65;
                                                                                                                                                                                    xHHRODPC13 = yZDBYLRM.a(10, n, n14, n13, n12, n11, -1);
                                                                                                                                                                                    if (n8 == 0) break block66;
                                                                                                                                                                                }
                                                                                                                                                                                xHHRODPC13 = new WBWOBAFW(n6, n, 10, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                                                                                                                                            }
                                                                                                                                                                            if (xHHRODPC13 == null) break block67;
                                                                                                                                                                            n19 = 0;
                                                                                                                                                                            if (n3 == 11) {
                                                                                                                                                                                n19 += 256;
                                                                                                                                                                            }
                                                                                                                                                                            if (n != 1 && n != 3) break block68;
                                                                                                                                                                            n18 = yZDBYLRM.z;
                                                                                                                                                                            n17 = yZDBYLRM.i;
                                                                                                                                                                            if (n8 == 0) break block69;
                                                                                                                                                                        }
                                                                                                                                                                        n18 = yZDBYLRM.i;
                                                                                                                                                                        n17 = yZDBYLRM.z;
                                                                                                                                                                    }
                                                                                                                                                                    nYFUGYQS.a(n9, by2, n10, n17, xHHRODPC13, n18, n7, n19, (byte)110, n2, n5);
                                                                                                                                                                }
                                                                                                                                                                if (yZDBYLRM.F) {
                                                                                                                                                                    fTPNODIB.a(yZDBYLRM.v, p, yZDBYLRM.i, yZDBYLRM.z, n5, n2, n);
                                                                                                                                                                }
                                                                                                                                                                return;
                                                                                                                                                            }
                                                                                                                                                            if (n3 < 12) break block70;
                                                                                                                                                            if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block71;
                                                                                                                                                            xHHRODPC12 = yZDBYLRM.a(n3, n, n14, n13, n12, n11, -1);
                                                                                                                                                            if (n8 == 0) break block72;
                                                                                                                                                        }
                                                                                                                                                        xHHRODPC12 = new WBWOBAFW(n6, n, n3, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                                                                                                                    }
                                                                                                                                                    nYFUGYQS.a(n9, by2, n10, 1, xHHRODPC12, 1, n7, 0, (byte)110, n2, n5);
                                                                                                                                                    if (yZDBYLRM.F) {
                                                                                                                                                        fTPNODIB.a(yZDBYLRM.v, p, yZDBYLRM.i, yZDBYLRM.z, n5, n2, n);
                                                                                                                                                    }
                                                                                                                                                    return;
                                                                                                                                                }
                                                                                                                                                if (n3 != 0) break block73;
                                                                                                                                                if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block74;
                                                                                                                                                xHHRODPC11 = yZDBYLRM.a(0, n, n14, n13, n12, n11, -1);
                                                                                                                                                if (n8 == 0) break block75;
                                                                                                                                            }
                                                                                                                                            xHHRODPC11 = new WBWOBAFW(n6, n, 0, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                                                                                                        }
                                                                                                                                        nYFUGYQS.a(D[n], xHHRODPC11, true, n9, n2, by2, n5, null, n10, 0, n7);
                                                                                                                                        if (yZDBYLRM.F) {
                                                                                                                                            fTPNODIB.a(n2, n, n5, n3, (byte)1, yZDBYLRM.v);
                                                                                                                                        }
                                                                                                                                        return;
                                                                                                                                    }
                                                                                                                                    if (n3 != 1) break block76;
                                                                                                                                    if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block77;
                                                                                                                                    xHHRODPC10 = yZDBYLRM.a(1, n, n14, n13, n12, n11, -1);
                                                                                                                                    if (n8 == 0) break block78;
                                                                                                                                }
                                                                                                                                xHHRODPC10 = new WBWOBAFW(n6, n, 1, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                                                                                            }
                                                                                                                            nYFUGYQS.a(r[n], xHHRODPC10, true, n9, n2, by2, n5, null, n10, 0, n7);
                                                                                                                            if (yZDBYLRM.F) {
                                                                                                                                fTPNODIB.a(n2, n, n5, n3, (byte)1, yZDBYLRM.v);
                                                                                                                            }
                                                                                                                            return;
                                                                                                                        }
                                                                                                                        if (n3 != 2) break block79;
                                                                                                                        n16 = n + 1 & 3;
                                                                                                                        if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block80;
                                                                                                                        xHHRODPC9 = yZDBYLRM.a(2, 4 + n, n14, n13, n12, n11, -1);
                                                                                                                        xHHRODPC8 = yZDBYLRM.a(2, n16, n14, n13, n12, n11, -1);
                                                                                                                        if (n8 == 0) break block81;
                                                                                                                    }
                                                                                                                    xHHRODPC9 = new WBWOBAFW(n6, 4 + n, 2, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                                                                                    xHHRODPC8 = new WBWOBAFW(n6, n16, 2, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                                                                                }
                                                                                                                nYFUGYQS.a(D[n], xHHRODPC9, true, n9, n2, by2, n5, xHHRODPC8, n10, D[n16], n7);
                                                                                                                if (yZDBYLRM.F) {
                                                                                                                    fTPNODIB.a(n2, n, n5, n3, (byte)1, yZDBYLRM.v);
                                                                                                                }
                                                                                                                return;
                                                                                                            }
                                                                                                            if (n3 != 3) break block82;
                                                                                                            if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block83;
                                                                                                            xHHRODPC7 = yZDBYLRM.a(3, n, n14, n13, n12, n11, -1);
                                                                                                            if (n8 == 0) break block84;
                                                                                                        }
                                                                                                        xHHRODPC7 = new WBWOBAFW(n6, n, 3, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                                                                    }
                                                                                                    nYFUGYQS.a(r[n], xHHRODPC7, true, n9, n2, by2, n5, null, n10, 0, n7);
                                                                                                    if (yZDBYLRM.F) {
                                                                                                        fTPNODIB.a(n2, n, n5, n3, (byte)1, yZDBYLRM.v);
                                                                                                    }
                                                                                                    return;
                                                                                                }
                                                                                                if (n3 != 9) break block85;
                                                                                                if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block86;
                                                                                                xHHRODPC6 = yZDBYLRM.a(n3, n, n14, n13, n12, n11, -1);
                                                                                                if (n8 == 0) break block87;
                                                                                            }
                                                                                            xHHRODPC6 = new WBWOBAFW(n6, n, n3, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                                                        }
                                                                                        nYFUGYQS.a(n9, by2, n10, 1, xHHRODPC6, 1, n7, 0, (byte)110, n2, n5);
                                                                                        if (yZDBYLRM.F) {
                                                                                            fTPNODIB.a(yZDBYLRM.v, p, yZDBYLRM.i, yZDBYLRM.z, n5, n2, n);
                                                                                        }
                                                                                        return;
                                                                                    }
                                                                                    if (!yZDBYLRM.A) break block88;
                                                                                    if (n != 1) break block89;
                                                                                    n15 = n11;
                                                                                    n11 = n12;
                                                                                    n12 = n13;
                                                                                    n13 = n14;
                                                                                    n14 = n15;
                                                                                    if (n8 == 0) break block88;
                                                                                }
                                                                                if (n != 2) break block90;
                                                                                n15 = n11;
                                                                                n11 = n13;
                                                                                n13 = n15;
                                                                                n15 = n12;
                                                                                n12 = n14;
                                                                                n14 = n15;
                                                                                if (n8 == 0) break block88;
                                                                            }
                                                                            if (n == 3) {
                                                                                n15 = n11;
                                                                                n11 = n14;
                                                                                n14 = n13;
                                                                                n13 = n12;
                                                                                n12 = n15;
                                                                            }
                                                                        }
                                                                        if (n3 != 4) break block91;
                                                                        if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block92;
                                                                        xHHRODPC5 = yZDBYLRM.a(4, 0, n14, n13, n12, n11, -1);
                                                                        if (n8 == 0) break block93;
                                                                    }
                                                                    xHHRODPC5 = new WBWOBAFW(n6, 0, 4, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                                }
                                                                nYFUGYQS.a(n9, n2, n * 512, -460, n7, 0, n10, xHHRODPC5, n5, by2, 0, D[n]);
                                                                return;
                                                            }
                                                            if (n3 != 5) break block94;
                                                            n15 = 16;
                                                            int n20 = nYFUGYQS.c(n7, n5, n2);
                                                            if (n20 > 0) {
                                                                n15 = YZDBYLRM.a((int)(n20 >> 14 & Short.MAX_VALUE)).N;
                                                            }
                                                            if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block95;
                                                            xHHRODPC4 = yZDBYLRM.a(4, 0, n14, n13, n12, n11, -1);
                                                            if (n8 == 0) break block96;
                                                        }
                                                        xHHRODPC4 = new WBWOBAFW(n6, 0, 4, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                                    }
                                                    nYFUGYQS.a(n9, n2, n * 512, -460, n7, o[n] * n15, n10, xHHRODPC4, n5, by2, v[n] * n15, D[n]);
                                                    return;
                                                }
                                                if (n3 != 6) break block97;
                                                if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block98;
                                                xHHRODPC3 = yZDBYLRM.a(4, 0, n14, n13, n12, n11, -1);
                                                if (n8 == 0) break block99;
                                            }
                                            xHHRODPC3 = new WBWOBAFW(n6, 0, 4, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                                        }
                                        nYFUGYQS.a(n9, n2, n, -460, n7, 0, n10, xHHRODPC3, n5, by2, 0, 256);
                                        return;
                                    }
                                    if (n3 != 7) break block100;
                                    if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block101;
                                    xHHRODPC2 = yZDBYLRM.a(4, 0, n14, n13, n12, n11, -1);
                                    if (n8 == 0) break block102;
                                }
                                xHHRODPC2 = new WBWOBAFW(n6, 0, 4, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                            }
                            nYFUGYQS.a(n9, n2, n, -460, n7, 0, n10, xHHRODPC2, n5, by2, 0, 512);
                            return;
                        }
                        if (n3 != 8) break block60;
                        if (yZDBYLRM.T != -1 || yZDBYLRM.x != null) break block103;
                        xHHRODPC = yZDBYLRM.a(4, 0, n14, n13, n12, n11, -1);
                        if (n8 == 0) break block104;
                    }
                    xHHRODPC = new WBWOBAFW(n6, 0, 4, n13, 7, n12, n14, n11, yZDBYLRM.T, true);
                }
                nYFUGYQS.a(n9, n2, n, -460, n7, 0, n10, xHHRODPC, n5, by2, 0, 768);
                return;
            }
            catch (RuntimeException runtimeException) {
                signlink.reporterror("89985, " + nYFUGYQS + ", " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + fTPNODIB + ", " + nArray + ", " + n5 + ", " + n6 + ", " + n7 + ", " + by + ", " + runtimeException.toString());
                throw new RuntimeException();
            }
        }
    }

    public static final boolean a(int n, byte[] byArray, int n2, int n3) {
        int n4 = client.Jj;
        try {
            int n5;
            if (n3 < 6 || n3 > 6) {
                throw new NullPointerException();
            }
            boolean bl = true;
            MBMGIXGO mBMGIXGO = new MBMGIXGO(byArray, 891);
            int n6 = -1;
            while ((n5 = mBMGIXGO.k()) != 0) {
                n6 += n5;
                int n7 = 0;
                boolean bl2 = false;
                while (true) {
                    int n8;
                    if (bl2) {
                        n8 = mBMGIXGO.k();
                        if (n8 == 0) break;
                        mBMGIXGO.c();
                        if (n4 == 0) continue;
                    }
                    if ((n8 = mBMGIXGO.k()) == 0) break;
                    int n9 = (n7 += n8 - 1) & 0x3F;
                    int n10 = n7 >> 6 & 0x3F;
                    int n11 = mBMGIXGO.c() >> 2;
                    int n12 = n10 + n;
                    int n13 = n9 + n2;
                    if (n12 <= 0 || n13 <= 0 || n12 >= 103 || n13 >= 103) continue;
                    YZDBYLRM yZDBYLRM = YZDBYLRM.a(n6);
                    if (n11 != 22 || !C || yZDBYLRM.Q || yZDBYLRM.a) {
                        bl &= yZDBYLRM.a(true);
                        bl2 = true;
                    }
                    if (n4 != 0) break;
                }
                if (n4 == 0) continue;
            }
            return bl;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("14134, " + n + ", " + byArray + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(int n, FTPNODIB[] fTPNODIBArray, int n2, int n3, NYFUGYQS nYFUGYQS, byte[] byArray) {
        int n4 = client.Jj;
        try {
            int n5;
            if (n3 < 7 || n3 > 7) {
                return;
            }
            MBMGIXGO mBMGIXGO = new MBMGIXGO(byArray, 891);
            int n6 = -1;
            while ((n5 = mBMGIXGO.k()) != 0) {
                int n7;
                n6 += n5;
                int n8 = 0;
                while ((n7 = mBMGIXGO.k()) != 0) {
                    int n9 = (n8 += n7 - 1) & 0x3F;
                    int n10 = n8 >> 6 & 0x3F;
                    int n11 = n8 >> 12;
                    int n12 = mBMGIXGO.c();
                    int n13 = n12 >> 2;
                    int n14 = n12 & 3;
                    int n15 = n10 + n;
                    int n16 = n9 + n2;
                    if (n15 <= 0 || n16 <= 0 || n15 >= 103 || n16 >= 103) continue;
                    int n17 = n11;
                    if ((this.A[1][n15][n16] & 2) == 2) {
                        --n17;
                    }
                    FTPNODIB fTPNODIB = null;
                    if (n17 >= 0) {
                        fTPNODIB = fTPNODIBArray[n17];
                    }
                    this.a(n16, nYFUGYQS, fTPNODIB, n13, n11, n15, n6, false, n14);
                    if (n4 == 0) continue;
                }
                if (n4 == 0) continue;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("18363, " + n + ", " + fTPNODIBArray + ", " + n2 + ", " + n3 + ", " + nYFUGYQS + ", " + byArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    static {
        k = (int)(Math.random() * 33.0) - 16;
        int[] nArray = new int[4];
        nArray[0] = 1;
        nArray[2] = -1;
        o = nArray;
        p = 323;
        r = new int[]{16, 32, 64, 128};
        int[] nArray2 = new int[4];
        nArray2[1] = -1;
        nArray2[3] = 1;
        v = nArray2;
        w = 99;
        C = true;
        D = new int[]{1, 2, 4, 8};
        E = -388;
    }
}

