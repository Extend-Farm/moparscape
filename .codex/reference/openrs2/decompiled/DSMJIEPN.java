/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public final class DSMJIEPN
extends AFCKELYG {
    private int x;
    private boolean y;
    private int z;
    private byte A;
    public byte[] B;
    public int[] C;
    public int D;
    public int E;
    public int F;
    public int G;
    public int H;
    public int I;

    /*
     * Unable to fully structure code
     */
    public DSMJIEPN(XTGLDHGX var1_1, String var2_2, int var3_3) {
        block6: {
            block5: {
                var13_4 = AFCKELYG.w;
                super();
                this.y = false;
                this.z = 360;
                this.A = (byte)3;
                var4_5 = new MBMGIXGO(var1_1.a(String.valueOf(var2_2) + ".dat", null), 891);
                var5_6 = new MBMGIXGO(var1_1.a("index.dat", null), 891);
                var5_6.z = var4_5.e();
                this.H = var5_6.e();
                this.I = var5_6.e();
                var6_7 = var5_6.c();
                this.C = new int[var6_7];
                var7_8 = 0;
                if (!var13_4) ** GOTO lbl18
                do {
                    this.C[var7_8 + 1] = var5_6.g();
                    ++var7_8;
lbl18:
                    // 2 sources

                } while (var7_8 < var6_7 - 1);
                var8_9 = 0;
                if (!var13_4) ** GOTO lbl26
                do {
                    var5_6.z += 2;
                    var4_5.z += var5_6.e() * var5_6.e();
                    ++var5_6.z;
                    ++var8_9;
lbl26:
                    // 2 sources

                } while (var8_9 < var3_3);
                this.F = var5_6.c();
                this.G = var5_6.c();
                this.D = var5_6.e();
                this.E = var5_6.e();
                var9_10 = var5_6.c();
                var10_11 = this.D * this.E;
                this.B = new byte[var10_11];
                if (var9_10 != 0) break block5;
                var11_12 = 0;
                if (!var13_4) ** GOTO lbl40
                do {
                    this.B[var11_12] = var4_5.d();
                    ++var11_12;
lbl40:
                    // 2 sources

                } while (var11_12 < var10_11);
                return;
            }
            if (var9_10 != 1) break block6;
            var11_13 = 0;
            if (!var13_4) ** GOTO lbl54
            do {
                var12_14 = 0;
                if (!var13_4) ** GOTO lbl52
                do {
                    this.B[var11_13 + var12_14 * this.D] = var4_5.d();
                    ++var12_14;
lbl52:
                    // 2 sources

                } while (var12_14 < this.E);
                ++var11_13;
lbl54:
                // 2 sources

            } while (var11_13 < this.D);
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void b(boolean bl) {
        boolean bl2 = AFCKELYG.w;
        try {
            this.H /= 2;
            this.I /= 2;
            byte[] byArray = new byte[this.H * this.I];
            int n = 0;
            int n2 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl2) continue;
                int n3 = 0;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && !bl2) continue;
                    byArray[(n3 + this.F >> 1) + (n2 + this.G >> 1) * this.H] = this.B[n++];
                    ++n3;
                } while (n3 < this.D);
                ++n2;
            } while (n2 < this.E);
            this.B = byArray;
            this.D = this.H;
            this.E = this.I;
            this.F = 0;
            if (bl) {
                return;
            }
            this.G = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("3441, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void c(boolean bl) {
        boolean bl2 = AFCKELYG.w;
        try {
            if (this.D == this.H && this.E == this.I) {
                return;
            }
            byte[] byArray = new byte[this.H * this.I];
            if (bl) {
                return;
            }
            int n = 0;
            int n2 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl2) continue;
                int n3 = 0;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && !bl2) continue;
                    byArray[n3 + this.F + (n2 + this.G) * this.H] = this.B[n++];
                    ++n3;
                } while (n3 < this.D);
                ++n2;
            } while (n2 < this.E);
            this.B = byArray;
            this.D = this.H;
            this.E = this.I;
            this.F = 0;
            this.G = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("98615, " + bl + ", " + runtimeException.toString());
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
        boolean bl = AFCKELYG.w;
        try {
            if (n != 0) {
                return;
            }
            byte[] byArray = new byte[this.D * this.E];
            int n2 = 0;
            int n3 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n4 = this.D - 1;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    byArray[n2++] = this.B[n4 + n3 * this.D];
                    --n4;
                } while (n4 >= 0);
                ++n3;
            } while (n3 < this.E);
            this.B = byArray;
            this.F = this.H - this.D - this.F;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("29743, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void d(boolean bl) {
        boolean bl2 = AFCKELYG.w;
        try {
            byte[] byArray = new byte[this.D * this.E];
            int n = 0;
            int n2 = this.E - 1;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl2) continue;
                int n3 = 0;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && !bl2) continue;
                    byArray[n++] = this.B[n3 + n2 * this.D];
                    ++n3;
                } while (n3 < this.D);
                --n2;
            } while (n2 >= 0);
            this.B = byArray;
            if (!bl) {
                this.x = -48;
            }
            this.G = this.I - this.E - this.G;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("69044, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2, int n3, int n4) {
        boolean bl = AFCKELYG.w;
        try {
            int n5 = 0;
            boolean bl2 = true;
            do {
                int n6;
                int n7;
                int n8;
                block20: {
                    block19: {
                        block18: {
                            block17: {
                                block16: {
                                    block15: {
                                        if (bl2 && !(bl2 = false) && !bl) continue;
                                        n8 = this.C[n5] >> 16 & 0xFF;
                                        if ((n8 += n) >= 0) break block15;
                                        n8 = 0;
                                        if (!bl) break block16;
                                    }
                                    if (n8 > 255) {
                                        n8 = 255;
                                    }
                                }
                                n7 = this.C[n5] >> 8 & 0xFF;
                                if ((n7 += n2) >= 0) break block17;
                                n7 = 0;
                                if (!bl) break block18;
                            }
                            if (n7 > 255) {
                                n7 = 255;
                            }
                        }
                        n6 = this.C[n5] & 0xFF;
                        if ((n6 += n3) >= 0) break block19;
                        n6 = 0;
                        if (!bl) break block20;
                    }
                    if (n6 > 255) {
                        n6 = 255;
                    }
                }
                this.C[n5] = (n8 << 16) + (n7 << 8) + n6;
                ++n5;
            } while (n5 < this.C.length);
            if (n4 == 0) return;
            this.x = 69;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("3108, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, int n2, int n3) {
        try {
            int n4;
            int n5 = (n += this.F) + (n3 += this.G) * AFCKELYG.n;
            int n6 = 0;
            if (n2 != 16083) {
                return;
            }
            int n7 = this.E;
            int n8 = this.D;
            int n9 = AFCKELYG.n - n8;
            int n10 = 0;
            if (n3 < AFCKELYG.p) {
                n4 = AFCKELYG.p - n3;
                n7 -= n4;
                n3 = AFCKELYG.p;
                n6 += n4 * n8;
                n5 += n4 * AFCKELYG.n;
            }
            if (n3 + n7 > AFCKELYG.q) {
                n7 -= n3 + n7 - AFCKELYG.q;
            }
            if (n < AFCKELYG.r) {
                n4 = AFCKELYG.r - n;
                n8 -= n4;
                n = AFCKELYG.r;
                n6 += n4;
                n5 += n4;
                n10 += n4;
                n9 += n4;
            }
            if (n + n8 > AFCKELYG.s) {
                n4 = n + n8 - AFCKELYG.s;
                n8 -= n4;
                n10 += n4;
                n9 += n4;
            }
            if (n8 <= 0 || n7 <= 0) {
                return;
            }
            this.a(n7, (byte)9, AFCKELYG.m, this.B, n9, n5, n8, n6, this.C, n10);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("58630, " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(int n, byte by, int[] nArray, byte[] byArray, int n2, int n3, int n4, int n5, int[] nArray2, int n6) {
        boolean bl = AFCKELYG.w;
        try {
            if (by != 9) {
                this.y = !this.y;
            }
            int n7 = -(n4 >> 2);
            n4 = -(n4 & 3);
            int n8 = -n;
            boolean bl2 = true;
            do {
                int n9;
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n10 = n7;
                boolean bl3 = true;
                do {
                    block28: {
                        block27: {
                            block26: {
                                block25: {
                                    block24: {
                                        block23: {
                                            block22: {
                                                block21: {
                                                    if (bl3 && !(bl3 = false) && !bl) continue;
                                                    if ((n9 = byArray[n5++]) == 0) break block21;
                                                    nArray[n3++] = nArray2[n9 & 0xFF];
                                                    if (!bl) break block22;
                                                }
                                                ++n3;
                                            }
                                            if ((n9 = byArray[n5++]) == 0) break block23;
                                            nArray[n3++] = nArray2[n9 & 0xFF];
                                            if (!bl) break block24;
                                        }
                                        ++n3;
                                    }
                                    if ((n9 = byArray[n5++]) == 0) break block25;
                                    nArray[n3++] = nArray2[n9 & 0xFF];
                                    if (!bl) break block26;
                                }
                                ++n3;
                            }
                            if ((n9 = byArray[n5++]) == 0) break block27;
                            nArray[n3++] = nArray2[n9 & 0xFF];
                            if (!bl) break block28;
                        }
                        ++n3;
                    }
                    ++n10;
                } while (n10 < 0);
                n9 = n4;
                boolean bl4 = true;
                do {
                    block30: {
                        block29: {
                            byte by2;
                            if (bl4 && !(bl4 = false) && !bl) continue;
                            if ((by2 = byArray[n5++]) == 0) break block29;
                            nArray[n3++] = nArray2[by2 & 0xFF];
                            if (!bl) break block30;
                        }
                        ++n3;
                    }
                    ++n9;
                } while (n9 < 0);
                n3 += n2;
                n5 += n6;
                ++n8;
            } while (n8 < 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("41026, " + n + ", " + by + ", " + nArray + ", " + byArray + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + nArray2 + ", " + n6 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

