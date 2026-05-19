/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class XHHRODPC
extends PPOHBEGB {
    private int i = 923;
    RJXWGZGD[] j;
    public int k = 1000;
    public static boolean l;

    public void a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        ZKARKDQW zKARKDQW = this.a(4016);
        if (zKARKDQW != null) {
            this.k = zKARKDQW.k;
            zKARKDQW.a(n, n2, n3, n4, n5, n6, n7, n8, n9);
        }
    }

    public ZKARKDQW a(int n) {
        try {
            if (n != 4016) {
                this.i = -185;
            }
            return null;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("75653, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

