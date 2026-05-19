/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

final class DLZHLHNK
extends GQOSZKJC {
    long sb = -1L;
    CKDEJADD tb;
    boolean ub = false;
    int[] vb = new int[5];
    int wb;
    int xb;
    String yb;
    static GCPOSBWX zb = new GCPOSBWX(false, 260);
    int Ab;
    int Bb;
    int Cb;
    int Db;
    int Eb;
    boolean Fb = false;
    int Gb;
    int Hb;
    int Ib;
    ZKARKDQW Jb;
    private int Kb = 9;
    private boolean Lb = true;
    int[] Mb = new int[12];
    long Nb;
    int Ob;
    int Pb;
    int Qb;
    int Rb;
    int Sb;

    public final ZKARKDQW a(int n) {
        int n2 = client.Jj;
        try {
            ZKARKDQW zKARKDQW;
            block17: {
                Object object;
                block22: {
                    block23: {
                        block21: {
                            ZKARKDQW[] zKARKDQWArray;
                            block19: {
                                block20: {
                                    block18: {
                                        if (n != 4016) {
                                            boolean bl = this.Lb = !this.Lb;
                                        }
                                        if (!this.Fb) {
                                            return null;
                                        }
                                        zKARKDQW = this.b(0);
                                        if (zKARKDQW == null) {
                                            return null;
                                        }
                                        this.t = zKARKDQW.k;
                                        zKARKDQW.fb = true;
                                        if (this.ub) {
                                            return zKARKDQW;
                                        }
                                        if (this.G != -1 && this.H != -1 && (zKARKDQWArray = ((MUDLUUBC)(object = MUDLUUBC.d[this.G])).a()) != null) {
                                            ZKARKDQW zKARKDQW2 = new ZKARKDQW(9, true, SQHJOGRT.a(this.H, false), false, (ZKARKDQW)zKARKDQWArray);
                                            zKARKDQW2.a(0, -this.K, 16384, 0);
                                            zKARKDQW2.a((byte)-71);
                                            zKARKDQW2.c(((MUDLUUBC)object).h.f[this.H], 40542);
                                            zKARKDQW2.eb = null;
                                            zKARKDQW2.db = null;
                                            if (((MUDLUUBC)object).k != 128 || ((MUDLUUBC)object).l != 128) {
                                                zKARKDQW2.b(((MUDLUUBC)object).k, ((MUDLUUBC)object).k, this.Kb, ((MUDLUUBC)object).l);
                                            }
                                            zKARKDQW2.a(64 + ((MUDLUUBC)object).n, 850 + ((MUDLUUBC)object).o, -30, -50, -30, true);
                                            ZKARKDQW[] zKARKDQWArray2 = new ZKARKDQW[]{zKARKDQW, zKARKDQW2};
                                            zKARKDQW = new ZKARKDQW(2, -819, true, zKARKDQWArray2);
                                        }
                                        if (this.Jb == null) break block17;
                                        if (client.kh >= this.Db) {
                                            this.Jb = null;
                                        }
                                        if (client.kh < this.Cb || client.kh >= this.Db) break block17;
                                        object = this.Jb;
                                        ((ZKARKDQW)object).a(this.Gb - this.kb, this.Hb - this.Eb, 16384, this.Ib - this.lb);
                                        if (this.w != 512) break block18;
                                        ((ZKARKDQW)object).e(360);
                                        ((ZKARKDQW)object).e(360);
                                        ((ZKARKDQW)object).e(360);
                                        if (n2 == 0) break block19;
                                    }
                                    if (this.w != 1024) break block20;
                                    ((ZKARKDQW)object).e(360);
                                    ((ZKARKDQW)object).e(360);
                                    if (n2 == 0) break block19;
                                }
                                if (this.w == 1536) {
                                    ((ZKARKDQW)object).e(360);
                                }
                            }
                            zKARKDQWArray = new ZKARKDQW[]{zKARKDQW, object};
                            zKARKDQW = new ZKARKDQW(2, -819, true, zKARKDQWArray);
                            if (this.w != 512) break block21;
                            ((ZKARKDQW)object).e(360);
                            if (n2 == 0) break block22;
                        }
                        if (this.w != 1024) break block23;
                        ((ZKARKDQW)object).e(360);
                        ((ZKARKDQW)object).e(360);
                        if (n2 == 0) break block22;
                    }
                    if (this.w == 1536) {
                        ((ZKARKDQW)object).e(360);
                        ((ZKARKDQW)object).e(360);
                        ((ZKARKDQW)object).e(360);
                    }
                }
                ((ZKARKDQW)object).a(this.kb - this.Gb, this.Eb - this.Hb, 16384, this.lb - this.Ib);
            }
            zKARKDQW.fb = true;
            return zKARKDQW;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("59276, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, MBMGIXGO mBMGIXGO) {
        int n2 = client.Jj;
        try {
            int n3;
            int n4;
            int n5;
            mBMGIXGO.z = 0;
            this.xb = mBMGIXGO.c();
            this.Bb = mBMGIXGO.c();
            if (n != 0) {
                return;
            }
            this.tb = null;
            this.wb = 0;
            int n6 = 0;
            boolean bl = true;
            do {
                block24: {
                    block23: {
                        if (bl && !(bl = false) && n2 == 0) continue;
                        n5 = mBMGIXGO.c();
                        if (n5 != 0) break block23;
                        this.Mb[n6] = 0;
                        if (n2 == 0) break block24;
                    }
                    n4 = mBMGIXGO.c();
                    this.Mb[n6] = (n5 << 8) + n4;
                    if (n6 == 0 && this.Mb[0] == 65535) {
                        this.tb = CKDEJADD.a(mBMGIXGO.e());
                        if (n2 == 0) break;
                    }
                    if (this.Mb[n6] >= 512 && this.Mb[n6] - 512 < DJRMEMXO.X && (n3 = DJRMEMXO.b((int)(this.Mb[n6] - 512)).W) != 0) {
                        this.wb = n3;
                    }
                }
                ++n6;
            } while (n6 < 12);
            n5 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                n4 = mBMGIXGO.c();
                if (n4 < 0 || n4 >= client.he[n5].length) {
                    n4 = 0;
                }
                this.vb[n5] = n4;
                ++n5;
            } while (n5 < 5);
            this.x = mBMGIXGO.e();
            if (this.x == 65535) {
                this.x = -1;
            }
            this.y = mBMGIXGO.e();
            if (this.y == 65535) {
                this.y = -1;
            }
            this.ob = mBMGIXGO.e();
            if (this.ob == 65535) {
                this.ob = -1;
            }
            this.pb = mBMGIXGO.e();
            if (this.pb == 65535) {
                this.pb = -1;
            }
            this.qb = mBMGIXGO.e();
            if (this.qb == 65535) {
                this.qb = -1;
            }
            this.rb = mBMGIXGO.e();
            if (this.rb == 65535) {
                this.rb = -1;
            }
            this.r = mBMGIXGO.e();
            if (this.r == 65535) {
                this.r = -1;
            }
            this.yb = ZTQFNQRH.a(-45804, ZTQFNQRH.a(mBMGIXGO.e(-35089), (byte)-99));
            this.Ab = mBMGIXGO.c();
            this.Sb = mBMGIXGO.e();
            this.Fb = true;
            this.Nb = 0L;
            n4 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n2 == 0) continue;
                this.Nb <<= 4;
                if (this.Mb[n4] >= 256) {
                    this.Nb += (long)(this.Mb[n4] - 256);
                }
                ++n4;
            } while (n4 < 12);
            if (this.Mb[0] >= 256) {
                this.Nb += (long)(this.Mb[0] - 256 >> 4);
            }
            if (this.Mb[1] >= 256) {
                this.Nb += (long)(this.Mb[1] - 256 >> 8);
            }
            n3 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && n2 == 0) continue;
                this.Nb <<= 3;
                this.Nb += (long)this.vb[n3];
                ++n3;
            } while (n3 < 5);
            this.Nb <<= 1;
            this.Nb += (long)this.xb;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("28951, " + n + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final ZKARKDQW b(int n) {
        int n2 = client.Jj;
        try {
            ZKARKDQW zKARKDQW;
            block40: {
                int n3;
                block39: {
                    int n4;
                    int n5;
                    int n6;
                    Object object;
                    int n7;
                    int n8;
                    int n9;
                    long l;
                    block38: {
                        block37: {
                            if (this.tb != null) {
                                int n10 = -1;
                                if (this.M >= 0 && this.P == 0) {
                                    n10 = LKGEGIEW.d[this.M].f[this.N];
                                    if (n2 == 0) return this.tb.a(0, -1, n10, null);
                                }
                                if (this.D < 0) return this.tb.a(0, -1, n10, null);
                                n10 = LKGEGIEW.d[this.D].f[this.E];
                                return this.tb.a(0, -1, n10, null);
                            }
                            l = this.Nb;
                            n3 = -1;
                            n9 = -1;
                            n8 = -1;
                            n7 = -1;
                            if (this.M < 0 || this.P != 0) break block37;
                            object = LKGEGIEW.d[this.M];
                            n3 = ((LKGEGIEW)object).f[this.N];
                            if (this.D >= 0 && this.D != this.x) {
                                n9 = LKGEGIEW.d[this.D].f[this.E];
                            }
                            if (((LKGEGIEW)object).m >= 0) {
                                n8 = ((LKGEGIEW)object).m;
                                l += (long)(n8 - this.Mb[5] << 40);
                            }
                            if (((LKGEGIEW)object).n < 0) break block38;
                            n7 = ((LKGEGIEW)object).n;
                            l += (long)(n7 - this.Mb[3] << 48);
                            if (n2 == 0) break block38;
                        }
                        if (this.D >= 0) {
                            n3 = LKGEGIEW.d[this.D].f[this.E];
                        }
                    }
                    object = (ZKARKDQW)zb.a(l);
                    if (n != 0) {
                        n6 = 1;
                        boolean bl = true;
                        do {
                            if (bl && !(bl = false) && n2 == 0) continue;
                            ++n6;
                        } while (n6 > 0);
                    }
                    if (object == null) {
                        n6 = 0;
                        n5 = 0;
                        boolean bl = true;
                        do {
                            if (bl && !(bl = false) && n2 == 0) continue;
                            n4 = this.Mb[n5];
                            if (n7 >= 0 && n5 == 3) {
                                n4 = n7;
                            }
                            if (n8 >= 0 && n5 == 5) {
                                n4 = n8;
                            }
                            if (n4 >= 256 && n4 < 512 && !TAVAECED.c[n4 - 256].a((byte)2)) {
                                n6 = 1;
                            }
                            if (n4 >= 512 && !DJRMEMXO.b(n4 - 512).c(40903, this.xb)) {
                                n6 = 1;
                            }
                            ++n5;
                        } while (n5 < 12);
                        if (n6 != 0) {
                            if (this.sb != -1L) {
                                object = (ZKARKDQW)zb.a(this.sb);
                            }
                            if (object == null) {
                                return null;
                            }
                        }
                    }
                    if (object == null) {
                        int n11;
                        ZKARKDQW[] zKARKDQWArray = new ZKARKDQW[12];
                        n5 = 0;
                        n4 = 0;
                        boolean bl = true;
                        do {
                            ZKARKDQW zKARKDQW2;
                            if (bl && !(bl = false) && n2 == 0) continue;
                            n11 = this.Mb[n4];
                            if (n7 >= 0 && n4 == 3) {
                                n11 = n7;
                            }
                            if (n8 >= 0 && n4 == 5) {
                                n11 = n8;
                            }
                            if (n11 >= 256 && n11 < 512 && (zKARKDQW2 = TAVAECED.c[n11 - 256].a(false)) != null) {
                                zKARKDQWArray[n5++] = zKARKDQW2;
                            }
                            if (n11 >= 512 && (zKARKDQW2 = DJRMEMXO.b(n11 - 512).a(false, this.xb)) != null) {
                                zKARKDQWArray[n5++] = zKARKDQW2;
                            }
                            ++n4;
                        } while (n4 < 12);
                        object = new ZKARKDQW(n5, zKARKDQWArray, -38);
                        n11 = 0;
                        boolean bl2 = true;
                        do {
                            if (bl2 && !(bl2 = false) && n2 == 0) continue;
                            if (this.vb[n11] != 0) {
                                ((ZKARKDQW)object).e(client.he[n11][0], client.he[n11][this.vb[n11]]);
                                if (n11 == 1) {
                                    ((ZKARKDQW)object).e(client.bi[0], client.bi[this.vb[n11]]);
                                }
                            }
                            ++n11;
                        } while (n11 < 5);
                        ((ZKARKDQW)object).a((byte)-71);
                        ((ZKARKDQW)object).a(64, 850, -30, -50, -30, true);
                        zb.a((PPOHBEGB)object, l, (byte)2);
                        this.sb = l;
                    }
                    if (this.ub) {
                        return object;
                    }
                    zKARKDQW = ZKARKDQW.t;
                    zKARKDQW.a(7, (ZKARKDQW)object, SQHJOGRT.a(n3, false) & SQHJOGRT.a(n9, false));
                    if (n3 == -1 || n9 == -1) break block39;
                    zKARKDQW.a(-20491, LKGEGIEW.d[this.M].j, n9, n3);
                    if (n2 == 0) break block40;
                }
                if (n3 != -1) {
                    zKARKDQW.c(n3, 40542);
                }
            }
            zKARKDQW.a(false);
            zKARKDQW.eb = null;
            zKARKDQW.db = null;
            return zKARKDQW;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("88397, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final boolean b(boolean bl) {
        try {
            if (!bl) {
                throw new NullPointerException();
            }
            return this.Fb;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("88114, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final ZKARKDQW a(byte by) {
        int n = client.Jj;
        try {
            if (by != -41) {
                this.Kb = 132;
            }
            if (!this.Fb) {
                return null;
            }
            if (this.tb != null) {
                return this.tb.a(true);
            }
            boolean bl = false;
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n == 0) continue;
                int n3 = this.Mb[n2];
                if (n3 >= 256 && n3 < 512 && !TAVAECED.c[n3 - 256].b(false)) {
                    bl = true;
                }
                if (n3 >= 512 && !DJRMEMXO.b(n3 - 512).a(-2836, this.xb)) {
                    bl = true;
                }
                ++n2;
            } while (n2 < 12);
            if (bl) {
                return null;
            }
            ZKARKDQW[] zKARKDQWArray = new ZKARKDQW[12];
            int n4 = 0;
            int n5 = 0;
            boolean bl3 = true;
            do {
                ZKARKDQW zKARKDQW;
                if (bl3 && !(bl3 = false) && n == 0) continue;
                int n6 = this.Mb[n5];
                if (n6 >= 256 && n6 < 512 && (zKARKDQW = TAVAECED.c[n6 - 256].a(0)) != null) {
                    zKARKDQWArray[n4++] = zKARKDQW;
                }
                if (n6 >= 512 && (zKARKDQW = DJRMEMXO.b(n6 - 512).b(-705, this.xb)) != null) {
                    zKARKDQWArray[n4++] = zKARKDQW;
                }
                ++n5;
            } while (n5 < 12);
            ZKARKDQW zKARKDQW = new ZKARKDQW(n4, zKARKDQWArray, -38);
            int n7 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && n == 0) continue;
                if (this.vb[n7] != 0) {
                    zKARKDQW.e(client.he[n7][0], client.he[n7][this.vb[n7]]);
                    if (n7 == 1) {
                        zKARKDQW.e(client.bi[0], client.bi[this.vb[n7]]);
                    }
                }
                ++n7;
            } while (n7 < 5);
            return zKARKDQW;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("26259, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    DLZHLHNK() {
    }
}

