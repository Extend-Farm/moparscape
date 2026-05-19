/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

final class HNKCWGJM
extends XHHRODPC {
    public int m;
    public int n;

    public final ZKARKDQW a(int n) {
        try {
            DJRMEMXO dJRMEMXO = DJRMEMXO.b(this.m);
            if (n != 4016) {
                throw new NullPointerException();
            }
            return dJRMEMXO.c(this.n);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("2596, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    HNKCWGJM() {
    }
}

