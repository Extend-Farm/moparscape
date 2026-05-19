/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

final class CWNCPMLX
extends GQOSZKJC {
    private int sb;
    private boolean tb = false;
    private int ub = 9;
    CKDEJADD vb;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final ZKARKDQW b(int n) {
        try {
            int n2;
            if (n != 0) {
                n2 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && client.Jj == 0) continue;
                    ++n2;
                } while (n2 > 0);
            }
            if (this.M >= 0 && this.P == 0) {
                n2 = LKGEGIEW.d[this.M].f[this.N];
                int n3 = -1;
                if (this.D >= 0 && this.D != this.x) {
                    n3 = LKGEGIEW.d[this.D].f[this.E];
                }
                return this.vb.a(0, n3, n2, LKGEGIEW.d[this.M].j);
            }
            n2 = -1;
            if (this.D >= 0) {
                n2 = LKGEGIEW.d[this.D].f[this.E];
            }
            return this.vb.a(0, -1, n2, null);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("92140, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final ZKARKDQW a(int n) {
        try {
            MUDLUUBC mUDLUUBC;
            ZKARKDQW zKARKDQW;
            if (this.vb == null) {
                return null;
            }
            ZKARKDQW zKARKDQW2 = this.b(0);
            if (zKARKDQW2 == null) {
                return null;
            }
            this.t = zKARKDQW2.k;
            if (n != 4016) {
                this.sb = -403;
            }
            if (this.G != -1 && this.H != -1 && (zKARKDQW = (mUDLUUBC = MUDLUUBC.d[this.G]).a()) != null) {
                int n2 = mUDLUUBC.h.f[this.H];
                ZKARKDQW zKARKDQW3 = new ZKARKDQW(9, true, SQHJOGRT.a(n2, false), false, zKARKDQW);
                zKARKDQW3.a(0, -this.K, 16384, 0);
                zKARKDQW3.a((byte)-71);
                zKARKDQW3.c(n2, 40542);
                zKARKDQW3.eb = null;
                zKARKDQW3.db = null;
                if (mUDLUUBC.k != 128 || mUDLUUBC.l != 128) {
                    zKARKDQW3.b(mUDLUUBC.k, mUDLUUBC.k, this.ub, mUDLUUBC.l);
                }
                zKARKDQW3.a(64 + mUDLUUBC.n, 850 + mUDLUUBC.o, -30, -50, -30, true);
                ZKARKDQW[] zKARKDQWArray = new ZKARKDQW[]{zKARKDQW2, zKARKDQW3};
                zKARKDQW2 = new ZKARKDQW(2, -819, true, zKARKDQWArray);
            }
            if (this.vb.n == 1) {
                zKARKDQW2.fb = true;
            }
            return zKARKDQW2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("13848, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final boolean b(boolean bl) {
        try {
            if (!bl) {
                boolean bl2 = this.tb = !this.tb;
            }
            return this.vb != null;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("58947, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    CWNCPMLX() {
    }
}

