/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;
import sign.signlink;

public final class YXVQXWYR
extends AFCKELYG {
    private boolean x;
    private int y;
    private int z;
    private int A;
    private boolean B;
    private int C;
    private boolean D;
    byte[][] E;
    int[] F;
    int[] G;
    int[] H;
    int[] I;
    public int[] J;
    public int K;
    Random L;
    boolean M;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public YXVQXWYR(boolean bl, String string, int n, XTGLDHGX xTGLDHGX) {
        block17: {
            boolean bl2 = AFCKELYG.w;
            this.x = false;
            this.y = 445;
            this.z = -471;
            this.A = -471;
            this.B = false;
            this.C = 3;
            this.D = false;
            this.E = new byte[256][];
            this.F = new int[256];
            this.G = new int[256];
            this.H = new int[256];
            this.I = new int[256];
            this.J = new int[256];
            this.L = new Random();
            this.M = false;
            try {
                MBMGIXGO mBMGIXGO = new MBMGIXGO(xTGLDHGX.a(String.valueOf(string) + ".dat", null), 891);
                MBMGIXGO mBMGIXGO2 = new MBMGIXGO(xTGLDHGX.a("index.dat", null), 891);
                int n2 = -1;
                if (n != 0) {
                    this.D = !this.D;
                }
                mBMGIXGO2.z = mBMGIXGO.e() + 4;
                int n3 = mBMGIXGO2.c();
                if (n3 > 0) {
                    mBMGIXGO2.z += 3 * (n3 - 1);
                }
                int n4 = 0;
                boolean bl3 = true;
                do {
                    int n5;
                    int n6;
                    int n7;
                    int n8;
                    block19: {
                        int n9;
                        block18: {
                            if (bl3 && !(bl3 = false) && !bl2) continue;
                            n2 = n4;
                            this.H[n4] = mBMGIXGO2.c();
                            this.I[n4] = mBMGIXGO2.c();
                            n8 = this.F[n4] = mBMGIXGO2.e();
                            n7 = this.G[n4] = mBMGIXGO2.e();
                            n9 = mBMGIXGO2.c();
                            int n10 = n8 * n7;
                            this.E[n4] = new byte[n10];
                            if (n9 != 0) break block18;
                            n6 = 0;
                            boolean bl4 = true;
                            do {
                                if (bl4 && !(bl4 = false) && !bl2) continue;
                                this.E[n4][n6] = mBMGIXGO.d();
                                ++n6;
                            } while (n6 < n10);
                            if (!bl2) break block19;
                        }
                        if (n9 == 1) {
                            n6 = 0;
                            boolean bl5 = true;
                            do {
                                if (bl5 && !(bl5 = false) && !bl2) continue;
                                n5 = 0;
                                boolean bl6 = true;
                                do {
                                    if (bl6 && !(bl6 = false) && !bl2) continue;
                                    this.E[n4][n6 + n5 * n8] = mBMGIXGO.d();
                                    ++n5;
                                } while (n5 < n7);
                                ++n6;
                            } while (n6 < n8);
                        }
                    }
                    if (n7 > this.K && n4 < 128) {
                        this.K = n7;
                    }
                    this.H[n4] = 1;
                    this.J[n4] = n8 + 2;
                    n6 = 0;
                    n5 = n7 / 7;
                    boolean bl7 = true;
                    do {
                        if (bl7 && !(bl7 = false) && !bl2) continue;
                        n6 += this.E[n4][n5 * n8];
                        ++n5;
                    } while (n5 < n7);
                    if (n6 <= n7 / 7) {
                        int n11 = n4;
                        this.J[n11] = this.J[n11] - 1;
                        this.H[n4] = 0;
                    }
                    n6 = 0;
                    int n12 = n7 / 7;
                    boolean bl8 = true;
                    do {
                        if (bl8 && !(bl8 = false) && !bl2) continue;
                        n6 += this.E[n4][n8 - 1 + n12 * n8];
                        ++n12;
                    } while (n12 < n7);
                    if (n6 <= n7 / 7) {
                        int n13 = n4;
                        this.J[n13] = this.J[n13] - 1;
                    }
                    ++n4;
                } while (n4 < 256);
                if (bl) {
                    this.J[32] = this.J[73];
                    return;
                }
                this.J[32] = this.J[105];
                if (!PKVMXVTO.e) break block17;
                AFCKELYG.w = !bl2;
            }
            catch (RuntimeException runtimeException) {
                signlink.reporterror("60040, " + bl + ", " + string + ", " + n + ", " + xTGLDHGX + ", " + runtimeException.toString());
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
    public void a(String string, int n, int n2, byte by, int n3) {
        try {
            this.b(n2, string, n3, 822, n - this.a(string, true));
            if (by == -80) return;
            int n4 = 1;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !AFCKELYG.w) continue;
                ++n4;
            } while (n4 > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("2575, " + string + ", " + n + ", " + n2 + ", " + by + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, String string, int n2, int n3, int n4) {
        try {
            if (n2 != 23693) {
                this.C = 467;
            }
            this.b(n, string, n3, 822, n4 - this.a(string, true) / 2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("47504, " + n + ", " + string + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, int n2, int n3, String string, int n4, boolean bl) {
        try {
            n3 = 74 / n3;
            this.a(false, bl, n2 - this.a(this.y, string) / 2, n, string, n4);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("93658, " + n + ", " + n2 + ", " + n3 + ", " + string + ", " + n4 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int a(int n, String string) {
        boolean bl = AFCKELYG.w;
        try {
            n = 75 / n;
            if (string == null) {
                return 0;
            }
            int n2 = 0;
            int n3 = 0;
            boolean bl2 = true;
            do {
                block8: {
                    block7: {
                        if (bl2 && !(bl2 = false) && !bl) continue;
                        if (string.charAt(n3) != '@' || n3 + 4 >= string.length() || string.charAt(n3 + 4) != '@') break block7;
                        n3 += 4;
                        if (!bl) break block8;
                    }
                    n2 += this.J[string.charAt(n3)];
                }
                ++n3;
            } while (n3 < string.length());
            return n2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("11394, " + n + ", " + string + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int a(String string, boolean bl) {
        boolean bl2 = AFCKELYG.w;
        try {
            int n;
            if (!bl) {
                n = 1;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl2) continue;
                    ++n;
                } while (n > 0);
            }
            if (string == null) {
                return 0;
            }
            n = 0;
            int n2 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl2) continue;
                n += this.J[string.charAt(n2)];
                ++n2;
            } while (n2 < string.length());
            return n;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("57519, " + string + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void b(int n, String string, int n2, int n3, int n4) {
        try {
            if (string == null) {
                return;
            }
            n2 -= this.K;
            int n5 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !AFCKELYG.w) continue;
                char c = string.charAt(n5);
                if (c != ' ') {
                    this.a(this.E[c], n4 + this.H[c], n2 + this.I[c], this.F[c], this.G[c], n);
                }
                n4 += this.J[c];
                ++n5;
            } while (n5 < string.length());
            n3 = 50 / n3;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("25570, " + n + ", " + string + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, boolean bl, String string, int n2, int n3, int n4) {
        try {
            if (!bl) {
                boolean bl2 = this.D = !this.D;
            }
            if (string == null) {
                return;
            }
            n2 -= this.a(string, true) / 2;
            n4 -= this.K;
            int n5 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !AFCKELYG.w) continue;
                char c = string.charAt(n5);
                if (c != ' ') {
                    this.a(this.E[c], n2 + this.H[c], n4 + this.I[c] + (int)(Math.sin((double)n5 / 2.0 + (double)n3 / 5.0) * 5.0), this.F[c], this.G[c], n);
                }
                n2 += this.J[c];
                ++n5;
            } while (n5 < string.length());
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("73127, " + n + ", " + bl + ", " + string + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, String string, int n2, int n3, byte by, int n4) {
        try {
            if (string == null) {
                return;
            }
            n -= this.a(string, true) / 2;
            n3 -= this.K;
            if (by != 5) {
                return;
            }
            int n5 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !AFCKELYG.w) continue;
                char c = string.charAt(n5);
                if (c != ' ') {
                    this.a(this.E[c], n + this.H[c] + (int)(Math.sin((double)n5 / 5.0 + (double)n2 / 5.0) * 5.0), n3 + this.I[c] + (int)(Math.sin((double)n5 / 3.0 + (double)n2 / 5.0) * 5.0), this.F[c], this.G[c], n4);
                }
                n += this.J[c];
                ++n5;
            } while (n5 < string.length());
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("43081, " + n + ", " + string + ", " + n2 + ", " + n3 + ", " + by + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, String string, boolean bl, int n2, int n3, int n4, int n5) {
        boolean bl2 = AFCKELYG.w;
        try {
            if (!bl) {
                int n6 = 1;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl2) continue;
                    ++n6;
                } while (n6 > 0);
            }
            if (string == null) {
                return;
            }
            double d = 7.0 - (double)n / 8.0;
            if (d < 0.0) {
                d = 0.0;
            }
            n4 -= this.a(string, true) / 2;
            n3 -= this.K;
            int n7 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && !bl2) continue;
                char c = string.charAt(n7);
                if (c != ' ') {
                    this.a(this.E[c], n4 + this.H[c], n3 + this.I[c] + (int)(Math.sin((double)n7 / 1.5 + (double)n2) * d), this.F[c], this.G[c], n5);
                }
                n4 += this.J[c];
                ++n7;
            } while (n7 < string.length());
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("85722, " + n + ", " + string + ", " + bl + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(boolean bl, boolean bl2, int n, int n2, String string, int n3) {
        boolean bl3 = AFCKELYG.w;
        try {
            this.M = false;
            int n4 = n;
            if (string == null) {
                return;
            }
            n3 -= this.K;
            int n5 = 0;
            boolean bl4 = true;
            do {
                block12: {
                    int n6;
                    block11: {
                        if (bl4 && !(bl4 = false) && !bl3) continue;
                        if (string.charAt(n5) != '@' || n5 + 4 >= string.length() || string.charAt(n5 + 4) != '@') break block11;
                        n6 = this.a(string.substring(n5 + 1, n5 + 4), this.z);
                        if (n6 != -1) {
                            n2 = n6;
                        }
                        n5 += 4;
                        if (!bl3) break block12;
                    }
                    if ((n6 = string.charAt(n5)) != 32) {
                        if (bl2) {
                            this.a(this.E[n6], n + this.H[n6] + 1, n3 + this.I[n6] + 1, this.F[n6], this.G[n6], 0);
                        }
                        this.a(this.E[n6], n + this.H[n6], n3 + this.I[n6], this.F[n6], this.G[n6], n2);
                    }
                    n += this.J[n6];
                }
                ++n5;
            } while (n5 < string.length());
            if (bl) {
                return;
            }
            if (!this.M) return;
            AFCKELYG.a(n3 + (int)((double)this.K * 0.7), 0x800000, n - n4, n4, (byte)4);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("94541, " + bl + ", " + bl2 + ", " + n + ", " + n2 + ", " + string + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(boolean bl, int n, int n2, String string, int n3, int n4, int n5) {
        boolean bl2 = AFCKELYG.w;
        try {
            if (string == null) {
                return;
            }
            this.L.setSeed(n3);
            int n6 = 192 + (this.L.nextInt() & 0x1F);
            n5 -= this.K;
            n4 = 10 / n4;
            int n7 = 0;
            boolean bl3 = true;
            do {
                block12: {
                    int n8;
                    block11: {
                        if (bl3 && !(bl3 = false) && !bl2) continue;
                        if (string.charAt(n7) != '@' || n7 + 4 >= string.length() || string.charAt(n7 + 4) != '@') break block11;
                        n8 = this.a(string.substring(n7 + 1, n7 + 4), this.z);
                        if (n8 != -1) {
                            n2 = n8;
                        }
                        n7 += 4;
                        if (!bl2) break block12;
                    }
                    if ((n8 = string.charAt(n7)) != 32) {
                        if (bl) {
                            this.a(192, n + this.H[n8] + 1, this.E[n8], this.F[n8], n5 + this.I[n8] + 1, this.G[n8], false, 0);
                        }
                        this.a(n6, n + this.H[n8], this.E[n8], this.F[n8], n5 + this.I[n8], this.G[n8], false, n2);
                    }
                    n += this.J[n8];
                    if ((this.L.nextInt() & 3) == 0) {
                        ++n;
                    }
                }
                ++n7;
            } while (n7 < string.length());
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("38011, " + bl + ", " + n + ", " + n2 + ", " + string + ", " + n3 + ", " + n4 + ", " + n5 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int a(String string, int n) {
        try {
            if (n >= 0) {
                boolean bl = this.x = !this.x;
            }
            if (string.equals("red")) {
                return 0xFF0000;
            }
            if (string.equals("gre")) {
                return 65280;
            }
            if (string.equals("blu")) {
                return 255;
            }
            if (string.equals("yel")) {
                return 0xFFFF00;
            }
            if (string.equals("cya")) {
                return 65535;
            }
            if (string.equals("mag")) {
                return 0xFF00FF;
            }
            if (string.equals("whi")) {
                return 0xFFFFFF;
            }
            if (string.equals("bla")) {
                return 0;
            }
            if (string.equals("lre")) {
                return 16748608;
            }
            if (string.equals("dre")) {
                return 0x800000;
            }
            if (string.equals("dbl")) {
                return 128;
            }
            if (string.equals("or1")) {
                return 0xFFB000;
            }
            if (string.equals("or2")) {
                return 0xFF7000;
            }
            if (string.equals("or3")) {
                return 0xFF3000;
            }
            if (string.equals("gr1")) {
                return 0xC0FF00;
            }
            if (string.equals("gr2")) {
                return 0x80FF00;
            }
            if (string.equals("gr3")) {
                return 0x40FF00;
            }
            if (string.equals("str")) {
                this.M = true;
            }
            if (string.equals("end")) {
                this.M = false;
            }
            return -1;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("14212, " + string + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private void a(byte[] byArray, int n, int n2, int n3, int n4, int n5) {
        int n6;
        int n7 = n + n2 * AFCKELYG.n;
        int n8 = AFCKELYG.n - n3;
        int n9 = 0;
        int n10 = 0;
        if (n2 < AFCKELYG.p) {
            n6 = AFCKELYG.p - n2;
            n4 -= n6;
            n2 = AFCKELYG.p;
            n10 += n6 * n3;
            n7 += n6 * AFCKELYG.n;
        }
        if (n2 + n4 >= AFCKELYG.q) {
            n4 -= n2 + n4 - AFCKELYG.q + 1;
        }
        if (n < AFCKELYG.r) {
            n6 = AFCKELYG.r - n;
            n3 -= n6;
            n = AFCKELYG.r;
            n10 += n6;
            n7 += n6;
            n9 += n6;
            n8 += n6;
        }
        if (n + n3 >= AFCKELYG.s) {
            n6 = n + n3 - AFCKELYG.s + 1;
            n3 -= n6;
            n9 += n6;
            n8 += n6;
        }
        if (n3 <= 0 || n4 <= 0) {
            return;
        }
        this.a(AFCKELYG.m, byArray, n5, n10, n7, n3, n4, n8, n9);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private void a(int[] nArray, byte[] byArray, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
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
                                                if (byArray[n2++] == 0) break block13;
                                                nArray[n3++] = n;
                                                if (!bl) break block14;
                                            }
                                            ++n3;
                                        }
                                        if (byArray[n2++] == 0) break block15;
                                        nArray[n3++] = n;
                                        if (!bl) break block16;
                                    }
                                    ++n3;
                                }
                                if (byArray[n2++] == 0) break block17;
                                nArray[n3++] = n;
                                if (!bl) break block18;
                            }
                            ++n3;
                        }
                        if (byArray[n2++] == 0) break block19;
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
                        if (byArray[n2++] == 0) break block21;
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
    private void a(int n, int n2, byte[] byArray, int n3, int n4, int n5, boolean bl, int n6) {
        try {
            int n7;
            int n8 = n2 + n4 * AFCKELYG.n;
            int n9 = AFCKELYG.n - n3;
            int n10 = 0;
            int n11 = 0;
            if (n4 < AFCKELYG.p) {
                n7 = AFCKELYG.p - n4;
                n5 -= n7;
                n4 = AFCKELYG.p;
                n11 += n7 * n3;
                n8 += n7 * AFCKELYG.n;
            }
            if (n4 + n5 >= AFCKELYG.q) {
                n5 -= n4 + n5 - AFCKELYG.q + 1;
            }
            if (n2 < AFCKELYG.r) {
                n7 = AFCKELYG.r - n2;
                n3 -= n7;
                n2 = AFCKELYG.r;
                n11 += n7;
                n8 += n7;
                n10 += n7;
                n9 += n7;
            }
            if (n2 + n3 >= AFCKELYG.s) {
                n7 = n2 + n3 - AFCKELYG.s + 1;
                n3 -= n7;
                n10 += n7;
                n9 += n7;
            }
            if (n3 <= 0) return;
            if (n5 <= 0) {
                return;
            }
            this.a(byArray, n5, n8, AFCKELYG.m, 520, n11, n3, n10, n9, n6, n);
            if (!bl) return;
            n7 = 1;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !AFCKELYG.w) continue;
                ++n7;
            } while (n7 > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("67426, " + n + ", " + n2 + ", " + byArray + ", " + n3 + ", " + n4 + ", " + n5 + ", " + bl + ", " + n6 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(byte[] byArray, int n, int n2, int[] nArray, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        boolean bl = AFCKELYG.w;
        try {
            n3 = 98 / n3;
            n8 = ((n8 & 0xFF00FF) * n9 & 0xFF00FF00) + ((n8 & 0xFF00) * n9 & 0xFF0000) >> 8;
            n9 = 256 - n9;
            int n10 = -n;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n11 = -n5;
                boolean bl3 = true;
                do {
                    block7: {
                        block6: {
                            if (bl3 && !(bl3 = false) && !bl) continue;
                            if (byArray[n4++] == 0) break block6;
                            int n12 = nArray[n2];
                            nArray[n2++] = (((n12 & 0xFF00FF) * n9 & 0xFF00FF00) + ((n12 & 0xFF00) * n9 & 0xFF0000) >> 8) + n8;
                            if (!bl) break block7;
                        }
                        ++n2;
                    }
                    ++n11;
                } while (n11 < 0);
                n2 += n7;
                n4 += n6;
                ++n10;
            } while (n10 < 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("45065, " + byArray + ", " + n + ", " + n2 + ", " + nArray + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + n9 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

