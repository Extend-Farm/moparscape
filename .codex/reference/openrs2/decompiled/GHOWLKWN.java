/*
 * Decompiled with CFR 0.152.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;
import sign.signlink;

public class GHOWLKWN
extends VJKFYAWG
implements Runnable {
    private int b;
    private LHGXPZPG c = new LHGXPZPG(169);
    private int d;
    public String e = "";
    private int f;
    private long g;
    private boolean h = true;
    private int[] i;
    private CRC32 j = new CRC32();
    private byte[] k = new byte[500];
    private int l = 923;
    public int m;
    private byte[][] n = new byte[4][];
    private client o;
    private LHGXPZPG p = new LHGXPZPG(169);
    private static int q;
    private int r;
    private int s;
    private int[] t;
    public int u;
    private int[] v;
    private int w;
    private int x = 13603;
    private boolean y = true;
    private OutputStream z;
    private boolean A = false;
    private int[] B;
    private boolean C = false;
    private LHGXPZPG D = new LHGXPZPG(169);
    private byte[] E = new byte[65000];
    private int[] F;
    private BISVHPUN G = new BISVHPUN(q);
    private InputStream H;
    private Socket I;
    private int[][] J = new int[4][];
    private int[][] K = new int[4][];
    private int L;
    private int M;
    private LHGXPZPG N = new LHGXPZPG(169);
    private PHKHJKBS O;
    private LHGXPZPG P = new LHGXPZPG(169);
    private int[] Q;
    private byte[] R;
    private int S;

    private final boolean a(int n, byte by, int n2, byte[] byArray) {
        try {
            if (byArray == null || byArray.length < 2) {
                return false;
            }
            int n3 = byArray.length - 2;
            int n4 = ((byArray[n3] & 0xFF) << 8) + (byArray[n3 + 1] & 0xFF);
            if (by != 3) {
                this.h = !this.h;
            }
            this.j.reset();
            this.j.update(byArray, 0, n3);
            int n5 = (int)this.j.getValue();
            if (n4 != n) {
                return false;
            }
            return n5 == n2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("46053, " + n + ", " + by + ", " + n2 + ", " + byArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void b(int n) {
        int n2 = client.Jj;
        try {
            if (n >= 0) {
                this.h = !this.h;
            }
            try {
                int n3;
                int n4;
                int n5;
                block32: {
                    int n6;
                    int n7;
                    block33: {
                        block34: {
                            n5 = this.H.available();
                            if (this.s != 0 || n5 < 6) break block32;
                            this.C = true;
                            int n8 = 0;
                            boolean bl = true;
                            do {
                                if (bl && !(bl = false) && n2 == 0) continue;
                                n8 += this.H.read(this.k, n8, 6 - n8);
                            } while (n8 < 6);
                            n4 = this.k[0] & 0xFF;
                            n3 = ((this.k[1] & 0xFF) << 8) + (this.k[2] & 0xFF);
                            n7 = ((this.k[3] & 0xFF) << 8) + (this.k[4] & 0xFF);
                            n6 = this.k[5] & 0xFF;
                            this.O = null;
                            PHKHJKBS pHKHJKBS = (PHKHJKBS)this.c.b();
                            boolean bl2 = true;
                            do {
                                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                                if (pHKHJKBS.i == n4 && pHKHJKBS.k == n3) {
                                    this.O = pHKHJKBS;
                                }
                                if (this.O != null) {
                                    pHKHJKBS.m = 0;
                                }
                                pHKHJKBS = (PHKHJKBS)this.c.a(false);
                            } while (pHKHJKBS != null);
                            if (this.O == null) break block33;
                            this.S = 0;
                            if (n7 != 0) break block34;
                            signlink.reporterror("Rej: " + n4 + "," + n3);
                            this.O.j = null;
                            if (this.O.l) {
                                LHGXPZPG lHGXPZPG = this.D;
                                synchronized (lHGXPZPG) {
                                    this.D.a(this.O);
                                }
                            } else {
                                this.O.a();
                            }
                            this.O = null;
                            if (n2 == 0) break block33;
                        }
                        if (this.O.j == null && n6 == 0) {
                            this.O.j = new byte[n7];
                        }
                        if (this.O.j == null && n6 != 0) {
                            throw new IOException("missing start of file");
                        }
                    }
                    this.r = n6 * 500;
                    this.s = 500;
                    if (this.s > n7 - n6 * 500) {
                        this.s = n7 - n6 * 500;
                    }
                }
                if (this.s <= 0) return;
                if (n5 < this.s) return;
                this.C = true;
                byte[] byArray = this.k;
                n4 = 0;
                if (this.O != null) {
                    byArray = this.O.j;
                    n4 = this.r;
                }
                n3 = 0;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && n2 == 0) continue;
                    n3 += this.H.read(byArray, n3 + n4, this.s - n3);
                } while (n3 < this.s);
                if (this.s + this.r >= byArray.length && this.O != null) {
                    if (this.o.Ad[0] != null) {
                        this.o.Ad[this.O.i + 1].a(byArray.length, byArray, (byte)2, this.O.k);
                    }
                    if (!this.O.l && this.O.i == 3) {
                        this.O.l = true;
                        this.O.i = 93;
                    }
                    if (this.O.l) {
                        LHGXPZPG lHGXPZPG = this.D;
                        synchronized (lHGXPZPG) {
                            this.D.a(this.O);
                        }
                    } else {
                        this.O.a();
                    }
                }
                this.s = 0;
                return;
            }
            catch (IOException iOException) {
                try {
                    this.I.close();
                }
                catch (Exception exception) {}
                this.I = null;
                this.H = null;
                this.z = null;
                this.s = 0;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("81261, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public final void a(XTGLDHGX xTGLDHGX, client client2) {
        int n;
        Object object;
        int n2;
        int n3 = client.Jj;
        String[] stringArray = new String[]{"model_version", "anim_version", "midi_version", "map_version"};
        int n4 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && n3 == 0) continue;
            byte[] byArray = xTGLDHGX.a(stringArray[n4], null);
            n2 = byArray.length / 2;
            object = new MBMGIXGO(byArray, 891);
            this.J[n4] = new int[n2];
            this.n[n4] = new byte[n2];
            n = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n3 == 0) continue;
                this.J[n4][n] = ((MBMGIXGO)object).e();
                ++n;
            } while (n < n2);
            ++n4;
        } while (n4 < 4);
        String[] stringArray2 = new String[]{"model_crc", "anim_crc", "midi_crc", "map_crc"};
        n2 = 0;
        boolean bl3 = true;
        do {
            if (bl3 && !(bl3 = false) && n3 == 0) continue;
            object = xTGLDHGX.a(stringArray2[n2], null);
            n = ((Object)object).length / 4;
            MBMGIXGO mBMGIXGO = new MBMGIXGO((byte[])object, 891);
            this.K[n2] = new int[n];
            int n5 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && n3 == 0) continue;
                this.K[n2][n5] = mBMGIXGO.h();
                ++n5;
            } while (n5 < n);
            ++n2;
        } while (n2 < 4);
        object = xTGLDHGX.a("model_index", null);
        n = this.J[0].length;
        this.R = new byte[n];
        int n6 = 0;
        boolean bl5 = true;
        do {
            block11: {
                block10: {
                    if (bl5 && !(bl5 = false) && n3 == 0) continue;
                    if (n6 >= ((Object)object).length) break block10;
                    this.R[n6] = (byte)object[n6];
                    if (n3 == 0) break block11;
                }
                this.R[n6] = 0;
            }
            ++n6;
        } while (n6 < n);
        object = xTGLDHGX.a("map_index", null);
        MBMGIXGO mBMGIXGO = new MBMGIXGO((byte[])object, 891);
        n = ((Object)object).length / 7;
        this.Q = new int[n];
        this.v = new int[n];
        this.i = new int[n];
        this.B = new int[n];
        int n7 = 0;
        boolean bl6 = true;
        do {
            if (bl6 && !(bl6 = false) && n3 == 0) continue;
            this.Q[n7] = mBMGIXGO.e();
            this.v[n7] = mBMGIXGO.e();
            this.i[n7] = mBMGIXGO.e();
            this.B[n7] = mBMGIXGO.c();
            ++n7;
        } while (n7 < n);
        object = xTGLDHGX.a("anim_index", null);
        mBMGIXGO = new MBMGIXGO((byte[])object, 891);
        n = ((Object)object).length / 2;
        this.F = new int[n];
        int n8 = 0;
        boolean bl7 = true;
        do {
            if (bl7 && !(bl7 = false) && n3 == 0) continue;
            this.F[n8] = mBMGIXGO.e();
            ++n8;
        } while (n8 < n);
        object = xTGLDHGX.a("midi_index", null);
        mBMGIXGO = new MBMGIXGO((byte[])object, 891);
        n = ((Object)object).length;
        this.t = new int[n];
        int n9 = 0;
        boolean bl8 = true;
        do {
            if (bl8 && !(bl8 = false) && n3 == 0) continue;
            this.t[n9] = mBMGIXGO.c();
            ++n9;
        } while (n9 < n);
        this.o = client2;
        this.y = true;
        this.o.a(this, 2);
    }

    public final int a() {
        BISVHPUN bISVHPUN = this.G;
        synchronized (bISVHPUN) {
            int n = this.G.c();
            Object var3_3 = null;
            return n;
        }
    }

    public final void b() {
        this.y = false;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(boolean bl, int n) {
        try {
            int n2 = this.Q.length;
            if (n != 0) {
                q = 20;
            }
            int n3 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && client.Jj == 0) continue;
                if (bl || this.B[n3] != 0) {
                    this.a((byte)2, 3, this.i[n3], (byte)8);
                    this.a((byte)2, 3, this.v[n3], (byte)8);
                }
                ++n3;
            } while (n3 < n2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("6116, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final int a(int n, int n2) {
        try {
            if (n <= 0) {
                this.A = !this.A;
            }
            return this.J[n2].length;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("72363, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void a(int n, PHKHJKBS pHKHJKBS) {
        int n2 = client.Jj;
        try {
            if (n < 8 || n > 8) {
                this.x = -339;
            }
            try {
                block15: {
                    block16: {
                        block14: {
                            if (this.I == null) {
                                long l = System.currentTimeMillis();
                                if (l - this.g < 4000L) {
                                    return;
                                }
                                this.g = l;
                                this.I = this.o.j(43594 + client.od);
                                this.H = this.I.getInputStream();
                                this.z = this.I.getOutputStream();
                                this.z.write(15);
                                int n3 = 0;
                                boolean bl = true;
                                do {
                                    if (bl && !(bl = false) && n2 == 0) continue;
                                    this.H.read();
                                    ++n3;
                                } while (n3 < 8);
                                this.S = 0;
                            }
                            this.k[0] = (byte)pHKHJKBS.i;
                            this.k[1] = (byte)(pHKHJKBS.k >> 8);
                            this.k[2] = (byte)pHKHJKBS.k;
                            if (!pHKHJKBS.l) break block14;
                            this.k[3] = 2;
                            if (n2 == 0) break block15;
                        }
                        if (this.o.gh) break block16;
                        this.k[3] = 1;
                        if (n2 == 0) break block15;
                    }
                    this.k[3] = 0;
                }
                this.z.write(this.k, 0, 4);
                this.f = 0;
                this.u = -10000;
                return;
            }
            catch (IOException iOException) {
                try {
                    this.I.close();
                }
                catch (Exception exception) {}
                this.I = null;
                this.H = null;
                this.z = null;
                this.s = 0;
                ++this.u;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("96894, " + n + ", " + pHKHJKBS + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final int c(int n) {
        try {
            if (n != 0) {
                this.x = -76;
            }
            return this.F.length;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("92552, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void b(int n, int n2) {
        if (n < 0 || n > this.J.length || n2 < 0 || n2 > this.J[n].length) {
            return;
        }
        if (this.J[n][n2] == 0) {
            return;
        }
        BISVHPUN bISVHPUN = this.G;
        synchronized (bISVHPUN) {
            PHKHJKBS pHKHJKBS = (PHKHJKBS)this.G.b();
            boolean bl = true;
            do {
                if (bl && !(bl = false) && client.Jj == 0) continue;
                if (pHKHJKBS.i == n && pHKHJKBS.k == n2) {
                    return;
                }
                pHKHJKBS = (PHKHJKBS)this.G.a(false);
            } while (pHKHJKBS != null);
            pHKHJKBS = new PHKHJKBS();
            pHKHJKBS.i = n;
            pHKHJKBS.k = n2;
            pHKHJKBS.l = true;
            LHGXPZPG lHGXPZPG = this.P;
            synchronized (lHGXPZPG) {
                this.P.a(pHKHJKBS);
            }
            this.G.a(pHKHJKBS);
            return;
        }
    }

    public final int c(int n, int n2) {
        try {
            if (n2 >= 0) {
                q = -7;
            }
            return this.R[n] & 0xFF;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("46001, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void run() {
        var5_1 = client.Jj;
        try {
            if (var5_1 == 0) ** GOTO lbl79
            do {
                block22: {
                    block21: {
                        block20: {
                            ++this.m;
                            var1_2 = 20;
                            if (this.d == 0 && this.o.Ad[0] != null) {
                                var1_2 = 50;
                            }
                            try {
                                Thread.sleep(var1_2);
                            }
                            catch (Exception v0) {}
                            this.C = true;
                            var2_4 = 0;
                            if (var5_1 == 0) ** GOTO lbl25
                            while (this.C) {
                                this.C = false;
                                this.b(true);
                                this.a(false);
                                if (this.L == 0 && var2_4 >= 5) break;
                                this.a((byte)-56);
                                if (this.H != null) {
                                    this.b(-369);
                                }
                                ++var2_4;
lbl25:
                                // 2 sources

                                if (var2_4 < 100) continue;
                            }
                            var3_5 = false;
                            var4_6 = (PHKHJKBS)this.c.b();
                            if (var5_1 == 0) ** GOTO lbl37
                            do {
                                if (var4_6.l) {
                                    var3_5 = true;
                                    ++var4_6.m;
                                    if (var4_6.m > 50) {
                                        var4_6.m = 0;
                                        this.a(8, var4_6);
                                    }
                                }
                                var4_6 = (PHKHJKBS)this.c.a(false);
lbl37:
                                // 2 sources

                            } while (var4_6 != null);
                            if (var3_5) break block20;
                            var4_6 = (PHKHJKBS)this.c.b();
                            if (var5_1 == 0) ** GOTO lbl48
                            do {
                                var3_5 = true;
                                ++var4_6.m;
                                if (var4_6.m > 50) {
                                    var4_6.m = 0;
                                    this.a(8, var4_6);
                                }
                                var4_6 = (PHKHJKBS)this.c.a(false);
lbl48:
                                // 2 sources

                            } while (var4_6 != null);
                        }
                        if (!var3_5) break block21;
                        ++this.S;
                        if (this.S <= 750) break block22;
                        try {
                            this.I.close();
                        }
                        catch (Exception v1) {}
                        this.I = null;
                        this.H = null;
                        this.z = null;
                        this.s = 0;
                        if (var5_1 == 0) break block22;
                    }
                    this.S = 0;
                    this.e = "";
                }
                if (!this.o.gh || this.I == null || this.z == null || this.d <= 0 && this.o.Ad[0] != null) continue;
                ++this.f;
                if (this.f <= 500) continue;
                this.f = 0;
                this.k[0] = 0;
                this.k[1] = 0;
                this.k[2] = 0;
                this.k[3] = 10;
                try {
                    this.z.write(this.k, 0, 4);
                }
                catch (IOException v2) {
                    this.S = 5000;
                }
lbl79:
                // 5 sources

            } while (this.y);
            return;
        }
        catch (Exception var1_3) {
            signlink.reporterror("od_ex " + var1_3.getMessage());
            return;
        }
    }

    public final void a(int n, int n2, boolean bl) {
        try {
            if (this.o.Ad[0] == null) {
                return;
            }
            if (this.J[n2][n] == 0) {
                return;
            }
            if (this.n[n2][n] == 0) {
                return;
            }
            if (this.d == 0) {
                return;
            }
            PHKHJKBS pHKHJKBS = new PHKHJKBS();
            pHKHJKBS.i = n2;
            pHKHJKBS.k = n;
            if (bl) {
                q = -423;
            }
            pHKHJKBS.l = false;
            LHGXPZPG lHGXPZPG = this.p;
            synchronized (lHGXPZPG) {
                this.p.a(pHKHJKBS);
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("1578, " + n + ", " + n2 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Exception decompiling
     */
    public final PHKHJKBS c() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[DOLOOP]], but top level block is 13[SIMPLE_IF_TAKEN]
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

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final int a(int n, int n2, int n3, int n4) {
        try {
            if (n2 != 0) {
                return q;
            }
            int n5 = (n4 << 8) + n3;
            int n6 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && client.Jj == 0) continue;
                if (this.Q[n6] == n5) {
                    if (n == 0) {
                        return this.v[n6];
                    }
                    return this.i[n6];
                }
                ++n6;
            } while (n6 < this.Q.length);
            return -1;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("78844, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(int n) {
        this.b(0, n);
    }

    public final void a(byte by, int n, int n2, byte by2) {
        try {
            block10: {
                block9: {
                    if (by2 != 8) break block9;
                    by2 = 0;
                    if (client.Jj == 0) break block10;
                }
                this.l = 237;
            }
            if (this.o.Ad[0] == null) {
                return;
            }
            if (this.J[n][n2] == 0) {
                return;
            }
            byte[] byArray = this.o.Ad[n + 1].a(true, n2);
            if (this.a(this.J[n][n2], (byte)3, this.K[n][n2], byArray)) {
                return;
            }
            this.n[n][n2] = by;
            if (by > this.d) {
                this.d = by;
            }
            ++this.b;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("88351, " + by + ", " + n + ", " + n2 + ", " + by2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean d(int n, int n2) {
        int n3 = client.Jj;
        try {
            if (n3 != 0 || n2 >= 0) {
                throw new NullPointerException();
            }
            int n4 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n3 == 0) continue;
                if (this.i[n4] == n) {
                    return true;
                }
                ++n4;
            } while (n4 < this.Q.length);
            return false;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("5492, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private final void a(boolean var1_1) {
        var3_2 = client.Jj;
        try {
            this.L = 0;
            if (var1_1) {
                return;
            }
            this.M = 0;
            var2_3 = (PHKHJKBS)this.c.b();
            if (var3_2 == 0) ** GOTO lbl17
            do {
                block10: {
                    block9: {
                        if (!var2_3.l) break block9;
                        ++this.L;
                        if (var3_2 == 0) break block10;
                    }
                    ++this.M;
                }
                var2_3 = (PHKHJKBS)this.c.a(false);
lbl17:
                // 2 sources

            } while (var2_3 != null);
            if (var3_2 == 0) ** GOTO lbl27
            while ((var2_3 = (PHKHJKBS)this.N.a()) != null) {
                if (this.n[var2_3.i][var2_3.k] != 0) {
                    ++this.w;
                }
                this.n[var2_3.i][var2_3.k] = 0;
                this.c.a(var2_3);
                ++this.L;
                this.a(8, var2_3);
                this.C = true;
lbl27:
                // 2 sources

                if (this.L < 10) continue;
            }
            return;
        }
        catch (RuntimeException var2_4) {
            signlink.reporterror("89950, " + var1_1 + ", " + var2_4.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void d(int n) {
        try {
            if (n != 0) {
                int n2 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && client.Jj == 0) continue;
                    ++n2;
                } while (n2 > 0);
            }
            LHGXPZPG lHGXPZPG = this.p;
            synchronized (lHGXPZPG) {
                this.p.c();
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("26326, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private final void b(boolean var1_1) {
        try {
            if (!var1_1) {
                return;
            }
            var3_2 = this.P;
            synchronized (var3_2) {
                var2_3 = (PHKHJKBS)this.P.a();
                // MONITOREXIT @DISABLED, blocks:[0, 1, 4] lbl8 : MonitorExitStatement: MONITOREXIT : var3_2
                if (true) ** GOTO lbl39
            }
            do {
                this.C = true;
                var3_2 = null;
                if (this.o.Ad[0] != null) {
                    var3_2 = this.o.Ad[var2_3.i + 1].a(true, var2_3.k);
                }
                if (!this.a(this.J[var2_3.i][var2_3.k], (byte)3, this.K[var2_3.i][var2_3.k], (byte[])var3_2)) {
                    var3_2 = null;
                }
                var4_5 = this.P;
                synchronized (var4_5) {
                    block19: {
                        block18: {
                            if (var3_2 != null) break block18;
                            this.N.a(var2_3);
                            if (client.Jj == 0) break block19;
                        }
                        var2_3.j = (byte[])var3_2;
                        var6_6 = this.D;
                        synchronized (var6_6) {
                            this.D.a(var2_3);
                        }
                    }
                    var2_3 = (PHKHJKBS)this.P.a();
                }
lbl39:
                // 2 sources

            } while (var2_3 != null);
            return;
        }
        catch (RuntimeException var2_4) {
            signlink.reporterror("20446, " + var1_1 + ", " + var2_4.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private final void a(byte var1_1) {
        block20: {
            var7_2 = client.Jj;
            try {
                if (var1_1 == -56) ** GOTO lbl67
                var2_3 = 1;
                if (var7_2 == 0) ** GOTO lbl8
                do {
                    ++var2_3;
lbl8:
                    // 2 sources

                } while (var2_3 > 0);
                if (var7_2 == 0) ** GOTO lbl67
                while (this.d != 0) {
                    var3_7 = this.p;
                    synchronized (var3_7) {
                        var2_4 = (PHKHJKBS)this.p.a();
                        // MONITOREXIT @DISABLED, blocks:[0, 1, 3, 9] lbl15 : MonitorExitStatement: MONITOREXIT : var3_7
                        if (true) ** GOTO lbl38
                    }
                    do {
                        if (this.n[var2_4.i][var2_4.k] != 0) {
                            this.n[var2_4.i][var2_4.k] = 0;
                            this.c.a(var2_4);
                            this.a(8, var2_4);
                            this.C = true;
                            if (this.w < this.b) {
                                ++this.w;
                            }
                            this.e = "Loading extra files - " + this.w * 100 / this.b + "%";
                            ++this.M;
                            if (this.M == 10) {
                                return;
                            }
                        }
                        var3_7 = this.p;
                        synchronized (var3_7) {
                            var2_4 = (PHKHJKBS)this.p.a();
                        }
lbl38:
                        // 2 sources

                    } while (var2_4 != null);
                    var3_6 = 0;
                    if (var7_2 == 0) ** GOTO lbl65
                    do {
                        var4_8 = this.n[var3_6];
                        var5_9 = var4_8.length;
                        var6_10 = 0;
                        if (var7_2 == 0) ** GOTO lbl63
                        do {
                            if (var4_8[var6_10] == this.d) {
                                var4_8[var6_10] = 0;
                                var2_4 = new PHKHJKBS();
                                var2_4.i = var3_6;
                                var2_4.k = var6_10;
                                var2_4.l = false;
                                this.c.a(var2_4);
                                this.a(8, var2_4);
                                this.C = true;
                                if (this.w < this.b) {
                                    ++this.w;
                                }
                                this.e = "Loading extra files - " + this.w * 100 / this.b + "%";
                                ++this.M;
                                if (this.M == 10) {
                                    return;
                                }
                            }
                            ++var6_10;
lbl63:
                            // 2 sources

                        } while (var6_10 < var5_9);
                        ++var3_6;
lbl65:
                        // 2 sources

                    } while (var3_6 < 4);
                    --this.d;
lbl67:
                    // 3 sources

                    if (this.L == 0) {
                        if (this.M < 10) continue;
                    }
                    break block20;
                }
                return;
            }
            catch (RuntimeException var2_5) {
                signlink.reporterror("32156, " + var1_1 + ", " + var2_5.toString());
                throw new RuntimeException();
            }
        }
    }

    public final boolean e(int n, int n2) {
        try {
            if (n2 != 5) {
                q = 169;
            }
            return this.t[n] == 1;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("54888, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

