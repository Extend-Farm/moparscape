/*
 * Decompiled with CFR 0.152.
 */
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import sign.signlink;

public final class CXGZMTJK
extends AFCKELYG {
    private boolean x;
    private int y;
    private int z;
    private int A;
    private byte B;
    private boolean C;
    private int D;
    private boolean E;
    private boolean F;
    private boolean G;
    private boolean H;
    public int[] I;
    public int J;
    public int K;
    public int L;
    public int M;
    public int N;
    public int O;

    public CXGZMTJK(int n, int n2) {
        this.x = false;
        this.y = 24869;
        this.A = -32357;
        this.B = (byte)3;
        this.C = false;
        this.D = -388;
        this.E = false;
        this.F = true;
        this.G = true;
        this.H = false;
        this.I = new int[n * n2];
        this.J = this.N = n;
        this.K = this.O = n2;
        this.M = 0;
        this.L = 0;
    }

    public CXGZMTJK(byte[] byArray, Component component) {
        this.x = false;
        this.y = 24869;
        this.A = -32357;
        this.B = (byte)3;
        this.C = false;
        this.D = -388;
        this.E = false;
        this.F = true;
        this.G = true;
        this.H = false;
        try {
            Image image = Toolkit.getDefaultToolkit().createImage(byArray);
            MediaTracker mediaTracker = new MediaTracker(component);
            mediaTracker.addImage(image, 0);
            mediaTracker.waitForAll();
            this.J = image.getWidth(component);
            this.K = image.getHeight(component);
            this.N = this.J;
            this.O = this.K;
            this.L = 0;
            this.M = 0;
            this.I = new int[this.J * this.K];
            PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, this.J, this.K, this.I, 0, this.J);
            pixelGrabber.grabPixels();
            return;
        }
        catch (Exception exception) {
            System.out.println("Error converting jpg");
            return;
        }
    }

    /*
     * Unable to fully structure code
     */
    public CXGZMTJK(XTGLDHGX var1_1, String var2_2, int var3_3) {
        block7: {
            block6: {
                var14_4 = AFCKELYG.w;
                super();
                this.x = false;
                this.y = 24869;
                this.A = -32357;
                this.B = (byte)3;
                this.C = false;
                this.D = -388;
                this.E = false;
                this.F = true;
                this.G = true;
                this.H = false;
                var4_5 = new MBMGIXGO(var1_1.a(String.valueOf(var2_2) + ".dat", null), 891);
                var5_6 = new MBMGIXGO(var1_1.a("index.dat", null), 891);
                var5_6.z = var4_5.e();
                this.N = var5_6.e();
                this.O = var5_6.e();
                var6_7 = var5_6.c();
                var7_8 = new int[var6_7];
                var8_9 = 0;
                if (!var14_4) ** GOTO lbl28
                PKVMXVTO.e = PKVMXVTO.e == false;
                do {
                    var7_8[var8_9 + 1] = var5_6.g();
                    if (var7_8[var8_9 + 1] == 0) {
                        var7_8[var8_9 + 1] = 1;
                    }
                    ++var8_9;
lbl28:
                    // 2 sources

                } while (var8_9 < var6_7 - 1);
                var9_10 = 0;
                if (!var14_4) ** GOTO lbl36
                do {
                    var5_6.z += 2;
                    var4_5.z += var5_6.e() * var5_6.e();
                    ++var5_6.z;
                    ++var9_10;
lbl36:
                    // 2 sources

                } while (var9_10 < var3_3);
                this.L = var5_6.c();
                this.M = var5_6.c();
                this.J = var5_6.e();
                this.K = var5_6.e();
                var10_11 = var5_6.c();
                var11_12 = this.J * this.K;
                this.I = new int[var11_12];
                if (var10_11 != 0) break block6;
                var12_13 = 0;
                if (!var14_4) ** GOTO lbl50
                do {
                    this.I[var12_13] = var7_8[var4_5.c()];
                    ++var12_13;
lbl50:
                    // 2 sources

                } while (var12_13 < var11_12);
                return;
            }
            if (var10_11 != 1) break block7;
            var12_14 = 0;
            if (!var14_4) ** GOTO lbl64
            do {
                var13_15 = 0;
                if (!var14_4) ** GOTO lbl62
                do {
                    this.I[var12_14 + var13_15 * this.J] = var7_8[var4_5.c()];
                    ++var13_15;
lbl62:
                    // 2 sources

                } while (var13_15 < this.K);
                ++var12_14;
lbl64:
                // 2 sources

            } while (var12_14 < this.J);
        }
    }

    public void b(int n) {
        try {
            if (n != 0) {
                this.H = !this.H;
            }
            AFCKELYG.a(this.K, this.J, -293, this.I);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("12951, " + n + ", " + runtimeException.toString());
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
            if (n4 != 0) {
                this.z = 314;
            }
            int n5 = 0;
            boolean bl2 = true;
            do {
                block16: {
                    int n6;
                    int n7;
                    int n8;
                    block22: {
                        block21: {
                            int n9;
                            block20: {
                                block19: {
                                    block18: {
                                        block17: {
                                            if (bl2 && !(bl2 = false) && !bl) continue;
                                            n9 = this.I[n5];
                                            if (n9 == 0) break block16;
                                            n8 = n9 >> 16 & 0xFF;
                                            if ((n8 += n) >= 1) break block17;
                                            n8 = 1;
                                            if (!bl) break block18;
                                        }
                                        if (n8 > 255) {
                                            n8 = 255;
                                        }
                                    }
                                    n7 = n9 >> 8 & 0xFF;
                                    if ((n7 += n2) >= 1) break block19;
                                    n7 = 1;
                                    if (!bl) break block20;
                                }
                                if (n7 > 255) {
                                    n7 = 255;
                                }
                            }
                            n6 = n9 & 0xFF;
                            if ((n6 += n3) >= 1) break block21;
                            n6 = 1;
                            if (!bl) break block22;
                        }
                        if (n6 > 255) {
                            n6 = 255;
                        }
                    }
                    this.I[n5] = (n8 << 16) + (n7 << 8) + n6;
                }
                ++n5;
            } while (n5 < this.I.length);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("66849, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void c(int n) {
        boolean bl = AFCKELYG.w;
        try {
            int[] nArray = new int[this.N * this.O];
            if (n != 5059) {
                this.y = -247;
            }
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n3 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    nArray[(n2 + this.M) * this.N + (n3 + this.L)] = this.I[n2 * this.J + n3];
                    ++n3;
                } while (n3 < this.J);
                ++n2;
            } while (n2 < this.K);
            this.I = nArray;
            this.J = this.N;
            this.K = this.O;
            this.L = 0;
            this.M = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("76028, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, int n2, int n3) {
        try {
            int n4;
            int n5 = (n += this.L) + (n2 += this.M) * AFCKELYG.n;
            int n6 = 0;
            int n7 = this.K;
            int n8 = this.J;
            int n9 = AFCKELYG.n - n8;
            int n10 = 0;
            if (n3 != this.A) {
                boolean bl = this.H = !this.H;
            }
            if (n2 < AFCKELYG.p) {
                n4 = AFCKELYG.p - n2;
                n7 -= n4;
                n2 = AFCKELYG.p;
                n6 += n4 * n8;
                n5 += n4 * AFCKELYG.n;
            }
            if (n2 + n7 > AFCKELYG.q) {
                n7 -= n2 + n7 - AFCKELYG.q;
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
            this.a(n5, n8, n7, n10, n6, 28339, n9, this.I, AFCKELYG.m);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("6343, " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int[] nArray, int[] nArray2) {
        boolean bl = AFCKELYG.w;
        try {
            int n8 = -(n2 >> 2);
            n2 = -(n2 & 3);
            int n9 = -n3;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n10 = n8;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    nArray2[n++] = nArray[n5++];
                    nArray2[n++] = nArray[n5++];
                    nArray2[n++] = nArray[n5++];
                    nArray2[n++] = nArray[n5++];
                    ++n10;
                } while (n10 < 0);
                int n11 = n2;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && !bl) continue;
                    nArray2[n++] = nArray[n5++];
                    ++n11;
                } while (n11 < 0);
                n += n7;
                n5 += n4;
                ++n9;
            } while (n9 < 0);
            if (n6 == 28339) return;
            this.A = 90;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("6170, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + nArray + ", " + nArray2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void b(int n, int n2, int n3) {
        try {
            int n4;
            n += this.L;
            n3 += this.M;
            if (n2 != 16083) {
                return;
            }
            int n5 = n + n3 * AFCKELYG.n;
            int n6 = 0;
            int n7 = this.K;
            int n8 = this.J;
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
            this.a(AFCKELYG.m, this.I, 0, n6, n5, n8, n7, n9, n10);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("50442, " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private void a(int[] nArray, int[] nArray2, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        boolean bl = AFCKELYG.w;
        int n8 = -(n4 >> 2);
        n4 = -(n4 & 3);
        int n9 = -n5;
        boolean bl2 = true;
        do {
            if (bl2 && !(bl2 = false) && !bl) continue;
            int n10 = n8;
            boolean bl3 = true;
            do {
                block20: {
                    block19: {
                        block18: {
                            block17: {
                                block16: {
                                    block15: {
                                        block14: {
                                            block13: {
                                                if (bl3 && !(bl3 = false) && !bl) continue;
                                                if ((n = nArray2[n2++]) == 0) break block13;
                                                nArray[n3++] = n;
                                                if (!bl) break block14;
                                            }
                                            ++n3;
                                        }
                                        if ((n = nArray2[n2++]) == 0) break block15;
                                        nArray[n3++] = n;
                                        if (!bl) break block16;
                                    }
                                    ++n3;
                                }
                                if ((n = nArray2[n2++]) == 0) break block17;
                                nArray[n3++] = n;
                                if (!bl) break block18;
                            }
                            ++n3;
                        }
                        if ((n = nArray2[n2++]) == 0) break block19;
                        nArray[n3++] = n;
                        if (!bl) break block20;
                    }
                    ++n3;
                }
                ++n10;
            } while (n10 < 0);
            int n11 = n4;
            boolean bl4 = true;
            do {
                block22: {
                    block21: {
                        if (bl4 && !(bl4 = false) && !bl) continue;
                        if ((n = nArray2[n2++]) == 0) break block21;
                        nArray[n3++] = n;
                        if (!bl) break block22;
                    }
                    ++n3;
                }
                ++n11;
            } while (n11 < 0);
            n3 += n6;
            n2 += n7;
            ++n9;
        } while (n9 < 0);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2, int n3, boolean bl) {
        try {
            int n4;
            int n5;
            n += this.L;
            if (bl) {
                n5 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && !AFCKELYG.w) continue;
                    ++n5;
                } while (n5 > 0);
            }
            n5 = n + (n2 += this.M) * AFCKELYG.n;
            int n6 = 0;
            int n7 = this.K;
            int n8 = this.J;
            int n9 = AFCKELYG.n - n8;
            int n10 = 0;
            if (n2 < AFCKELYG.p) {
                n4 = AFCKELYG.p - n2;
                n7 -= n4;
                n2 = AFCKELYG.p;
                n6 += n4 * n8;
                n5 += n4 * AFCKELYG.n;
            }
            if (n2 + n7 > AFCKELYG.q) {
                n7 -= n2 + n7 - AFCKELYG.q;
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
            if (n8 > 0 && n7 > 0) {
                this.a(n6, n8, AFCKELYG.m, 0, this.I, n10, n7, n9, n3, n5, 8);
                return;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("91057, " + n + ", " + n2 + ", " + n3 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(int n, int n2, int[] nArray, int n3, int[] nArray2, int n4, int n5, int n6, int n7, int n8, int n9) {
        boolean bl = AFCKELYG.w;
        try {
            int n10 = 256 - n7;
            int n11 = -n5;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n12 = -n2;
                boolean bl3 = true;
                do {
                    block9: {
                        block8: {
                            if (bl3 && !(bl3 = false) && !bl) continue;
                            if ((n3 = nArray2[n++]) == 0) break block8;
                            int n13 = nArray[n8];
                            nArray[n8++] = ((n3 & 0xFF00FF) * n7 + (n13 & 0xFF00FF) * n10 & 0xFF00FF00) + ((n3 & 0xFF00) * n7 + (n13 & 0xFF00) * n10 & 0xFF0000) >> 8;
                            if (!bl) break block9;
                        }
                        ++n8;
                    }
                    ++n12;
                } while (n12 < 0);
                n8 += n6;
                n += n4;
                ++n11;
            } while (n11 < 0);
            if (n9 < 8 || n9 > 8) {
                this.x = !this.x;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("76998, " + n + ", " + n2 + ", " + nArray + ", " + n3 + ", " + nArray2 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + n9 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void b(int n, int n2, int[] nArray, int n3, int[] nArray2, int n4, int n5, int n6, int n7, int n8, int n9) {
        boolean bl = AFCKELYG.w;
        try {
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                this.D = 362;
            } while (n4 >= 0);
            try {
                int n10 = -n8 / 2;
                int n11 = -n / 2;
                int n12 = (int)(Math.sin((double)n2 / 326.11) * 65536.0);
                int n13 = (int)(Math.cos((double)n2 / 326.11) * 65536.0);
                n12 = n12 * n3 >> 8;
                n13 = n13 * n3 >> 8;
                int n14 = (n9 << 16) + (n11 * n12 + n10 * n13);
                int n15 = (n5 << 16) + (n11 * n13 - n10 * n12);
                int n16 = n7 + n6 * AFCKELYG.n;
                n6 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    int n17 = nArray2[n6];
                    int n18 = n16 + n17;
                    int n19 = n14 + n13 * n17;
                    int n20 = n15 - n12 * n17;
                    n7 = -nArray[n6];
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && !bl) continue;
                        AFCKELYG.m[n18++] = this.I[(n19 >> 16) + (n20 >> 16) * this.J];
                        n19 += n13;
                        n20 -= n12;
                        ++n7;
                    } while (n7 < 0);
                    n14 += n12;
                    n15 += n13;
                    n16 += AFCKELYG.n;
                    ++n6;
                } while (n6 < n);
                return;
            }
            catch (Exception exception) {
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("8964, " + n + ", " + n2 + ", " + nArray + ", " + n3 + ", " + nArray2 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + n9 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2, int n3, int n4, int n5, int n6, int n7, double d, int n8) {
        boolean bl = AFCKELYG.w;
        try {
            if (n5 != 41960) {
                return;
            }
            try {
                int n9 = -n3 / 2;
                int n10 = -n7 / 2;
                int n11 = (int)(Math.sin(d) * 65536.0);
                int n12 = (int)(Math.cos(d) * 65536.0);
                n11 = n11 * n6 >> 8;
                n12 = n12 * n6 >> 8;
                int n13 = (n4 << 16) + (n10 * n11 + n9 * n12);
                int n14 = (n2 << 16) + (n10 * n12 - n9 * n11);
                int n15 = n8 + n * AFCKELYG.n;
                n = 0;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && !bl) continue;
                    int n16 = n15;
                    int n17 = n13;
                    int n18 = n14;
                    n8 = -n3;
                    boolean bl3 = true;
                    do {
                        block11: {
                            block10: {
                                if (bl3 && !(bl3 = false) && !bl) continue;
                                int n19 = this.I[(n17 >> 16) + (n18 >> 16) * this.J];
                                if (n19 == 0) break block10;
                                AFCKELYG.m[n16++] = n19;
                                if (!bl) break block11;
                            }
                            ++n16;
                        }
                        n17 += n12;
                        n18 -= n11;
                        ++n8;
                    } while (n8 < 0);
                    n13 += n11;
                    n14 += n12;
                    n15 += AFCKELYG.n;
                    ++n;
                } while (n < n7);
                return;
            }
            catch (Exception exception) {
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("71953, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + d + ", " + n8 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(DSMJIEPN dSMJIEPN, boolean bl, int n, int n2) {
        try {
            int n3;
            int n4 = (n2 += this.L) + (n += this.M) * AFCKELYG.n;
            int n5 = 0;
            if (bl) {
                this.y = -364;
            }
            int n6 = this.K;
            int n7 = this.J;
            int n8 = AFCKELYG.n - n7;
            int n9 = 0;
            if (n < AFCKELYG.p) {
                n3 = AFCKELYG.p - n;
                n6 -= n3;
                n = AFCKELYG.p;
                n5 += n3 * n7;
                n4 += n3 * AFCKELYG.n;
            }
            if (n + n6 > AFCKELYG.q) {
                n6 -= n + n6 - AFCKELYG.q;
            }
            if (n2 < AFCKELYG.r) {
                n3 = AFCKELYG.r - n2;
                n7 -= n3;
                n2 = AFCKELYG.r;
                n5 += n3;
                n4 += n3;
                n9 += n3;
                n8 += n3;
            }
            if (n2 + n7 > AFCKELYG.s) {
                n3 = n2 + n7 - AFCKELYG.s;
                n7 -= n3;
                n9 += n3;
                n8 += n3;
            }
            if (n7 <= 0 || n6 <= 0) {
                return;
            }
            this.a(this.I, n7, dSMJIEPN.B, n6, AFCKELYG.m, 0, this.F, n8, n4, n9, n5);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("70668, " + dSMJIEPN + ", " + bl + ", " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(int[] nArray, int n, byte[] byArray, int n2, int[] nArray2, int n3, boolean bl, int n4, int n5, int n6, int n7) {
        boolean bl2 = AFCKELYG.w;
        try {
            int n8;
            int n9 = -(n >> 2);
            if (!bl) {
                n8 = 1;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl2) continue;
                    ++n8;
                } while (n8 > 0);
            }
            n = -(n & 3);
            n8 = -n2;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl2) continue;
                int n10 = n9;
                boolean bl5 = true;
                do {
                    block29: {
                        block28: {
                            block27: {
                                block26: {
                                    block25: {
                                        block24: {
                                            block23: {
                                                block22: {
                                                    if (bl5 && !(bl5 = false) && !bl2) continue;
                                                    if ((n3 = nArray[n7++]) == 0 || byArray[n5] != 0) break block22;
                                                    nArray2[n5++] = n3;
                                                    if (!bl2) break block23;
                                                }
                                                ++n5;
                                            }
                                            if ((n3 = nArray[n7++]) == 0 || byArray[n5] != 0) break block24;
                                            nArray2[n5++] = n3;
                                            if (!bl2) break block25;
                                        }
                                        ++n5;
                                    }
                                    if ((n3 = nArray[n7++]) == 0 || byArray[n5] != 0) break block26;
                                    nArray2[n5++] = n3;
                                    if (!bl2) break block27;
                                }
                                ++n5;
                            }
                            if ((n3 = nArray[n7++]) == 0 || byArray[n5] != 0) break block28;
                            nArray2[n5++] = n3;
                            if (!bl2) break block29;
                        }
                        ++n5;
                    }
                    ++n10;
                } while (n10 < 0);
                int n11 = n;
                boolean bl6 = true;
                do {
                    block31: {
                        block30: {
                            if (bl6 && !(bl6 = false) && !bl2) continue;
                            if ((n3 = nArray[n7++]) == 0 || byArray[n5] != 0) break block30;
                            nArray2[n5++] = n3;
                            if (!bl2) break block31;
                        }
                        ++n5;
                    }
                    ++n11;
                } while (n11 < 0);
                n5 += n4;
                n7 += n6;
                ++n8;
            } while (n8 < 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("37704, " + nArray + ", " + n + ", " + byArray + ", " + n2 + ", " + nArray2 + ", " + n3 + ", " + bl + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

