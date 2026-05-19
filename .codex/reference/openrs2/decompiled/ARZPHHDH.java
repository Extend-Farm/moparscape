/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public final class ARZPHHDH {
    private boolean a = false;
    private int b = -373;
    private int c;
    private PKVMXVTO[] d;

    public ARZPHHDH(int n, int n2) {
        try {
            if (n >= 0) {
                throw new NullPointerException();
            }
            this.c = n2;
            this.d = new PKVMXVTO[n2];
            int n3 = 0;
            while (n3 < n2) {
                PKVMXVTO pKVMXVTO;
                pKVMXVTO.c = pKVMXVTO = (this.d[n3] = new PKVMXVTO());
                pKVMXVTO.d = pKVMXVTO;
                ++n3;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("52921, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public PKVMXVTO a(long l) {
        PKVMXVTO pKVMXVTO = this.d[(int)(l & (long)(this.c - 1))];
        PKVMXVTO pKVMXVTO2 = pKVMXVTO.c;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && PPOHBEGB.h == 0) continue;
            if (pKVMXVTO2.b == l) {
                return pKVMXVTO2;
            }
            pKVMXVTO2 = pKVMXVTO2.c;
        } while (pKVMXVTO2 != pKVMXVTO);
        return null;
    }

    public void a(PKVMXVTO pKVMXVTO, long l, byte by) {
        try {
            if (pKVMXVTO.d != null) {
                pKVMXVTO.a();
            }
            PKVMXVTO pKVMXVTO2 = this.d[(int)(l & (long)(this.c - 1))];
            if (by != 7) {
                return;
            }
            pKVMXVTO.d = pKVMXVTO2.d;
            pKVMXVTO.c = pKVMXVTO2;
            pKVMXVTO.d.c = pKVMXVTO;
            pKVMXVTO.c.d = pKVMXVTO;
            pKVMXVTO.b = l;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("91499, " + pKVMXVTO + ", " + l + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

