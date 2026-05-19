/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class KVCQPLIW {
    private int a;
    public int b;
    public int[] c;
    public int[][] d;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public KVCQPLIW(int n, MBMGIXGO mBMGIXGO) {
        boolean bl = XHHRODPC.l;
        this.a = -588;
        try {
            this.b = mBMGIXGO.c();
            this.c = new int[this.b];
            this.d = new int[this.b][];
            if (n != 0) {
                this.a = 203;
            }
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                this.c[n2] = mBMGIXGO.c();
                ++n2;
            } while (n2 < this.b);
            int n3 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                int n4 = mBMGIXGO.c();
                this.d[n3] = new int[n4];
                int n5 = 0;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && !bl) continue;
                    this.d[n3][n5] = mBMGIXGO.c();
                    ++n5;
                } while (n5 < n4);
                ++n3;
            } while (n3 < this.b);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("59011, " + n + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

