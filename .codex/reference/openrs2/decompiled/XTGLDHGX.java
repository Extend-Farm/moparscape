/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public final class XTGLDHGX {
    private byte a = 0;
    private int b = 9;
    private int c = -29508;
    public byte[] d;
    public int e;
    public int[] f;
    public int[] g;
    public int[] h;
    public int[] i;
    private boolean j;

    public XTGLDHGX(int n, byte[] byArray) {
        try {
            if (n != 44820) {
                throw new NullPointerException();
            }
            this.a(byArray, this.a);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("19672, " + n + ", " + byArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(byte[] byArray, byte by) {
        boolean bl = MBMGIXGO.L;
        try {
            MBMGIXGO mBMGIXGO;
            block8: {
                block7: {
                    mBMGIXGO = new MBMGIXGO(byArray, 891);
                    int n = mBMGIXGO.g();
                    int n2 = mBMGIXGO.g();
                    if (n2 == n) break block7;
                    byte[] byArray2 = new byte[n];
                    HZTFWEML.a(byArray2, n, byArray, n2, 6);
                    this.d = byArray2;
                    mBMGIXGO = new MBMGIXGO(this.d, 891);
                    this.j = true;
                    if (!bl) break block8;
                }
                this.d = byArray;
                this.j = false;
            }
            this.e = mBMGIXGO.e();
            if (by != 0) {
                return;
            }
            this.f = new int[this.e];
            this.g = new int[this.e];
            this.h = new int[this.e];
            this.i = new int[this.e];
            int n = mBMGIXGO.z + this.e * 10;
            int n3 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                this.f[n3] = mBMGIXGO.h();
                this.g[n3] = mBMGIXGO.g();
                this.h[n3] = mBMGIXGO.g();
                this.i[n3] = n;
                n += this.h[n3];
                ++n3;
            } while (n3 < this.e);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("33190, " + byArray + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public byte[] a(String var1_1, byte[] var2_2) {
        var7_3 = MBMGIXGO.L;
        var3_4 = 0;
        var1_1 = var1_1.toUpperCase();
        var4_5 = 0;
        if (!var7_3) ** GOTO lbl9
        do {
            var3_4 = var3_4 * 61 + var1_1.charAt(var4_5) - 32;
            ++var4_5;
lbl9:
            // 2 sources

        } while (var4_5 < var1_1.length());
        var5_6 = 0;
        if (!var7_3) ** GOTO lbl31
        do {
            block4: {
                block6: {
                    block5: {
                        if (this.f[var5_6] != var3_4) break block4;
                        if (var2_2 == null) {
                            var2_2 = new byte[this.g[var5_6]];
                        }
                        if (this.j) break block5;
                        HZTFWEML.a(var2_2, this.g[var5_6], this.d, this.h[var5_6], this.i[var5_6]);
                        if (!var7_3) break block6;
                    }
                    var6_7 = 0;
                    if (!var7_3) ** GOTO lbl26
                    do {
                        var2_2[var6_7] = this.d[this.i[var5_6] + var6_7];
                        ++var6_7;
lbl26:
                        // 2 sources

                    } while (var6_7 < this.g[var5_6]);
                }
                return var2_2;
            }
            ++var5_6;
lbl31:
            // 2 sources

        } while (var5_6 < this.e);
        return null;
    }
}

