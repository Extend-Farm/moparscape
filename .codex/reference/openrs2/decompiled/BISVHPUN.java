/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public final class BISVHPUN {
    private boolean a = false;
    private int b = -589;
    public PPOHBEGB c = new PPOHBEGB();
    private PPOHBEGB d;

    public BISVHPUN(int n) {
        try {
            if (n != 0) {
                this.b = -25;
            }
            this.c.f = this.c;
            this.c.g = this.c;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("656, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(PPOHBEGB pPOHBEGB) {
        if (pPOHBEGB.g != null) {
            pPOHBEGB.b();
        }
        pPOHBEGB.g = this.c.g;
        pPOHBEGB.f = this.c;
        pPOHBEGB.g.f = pPOHBEGB;
        pPOHBEGB.f.g = pPOHBEGB;
    }

    public PPOHBEGB a() {
        PPOHBEGB pPOHBEGB = this.c.f;
        if (pPOHBEGB == this.c) {
            return null;
        }
        pPOHBEGB.b();
        return pPOHBEGB;
    }

    public PPOHBEGB b() {
        PPOHBEGB pPOHBEGB = this.c.f;
        if (pPOHBEGB == this.c) {
            this.d = null;
            return null;
        }
        this.d = pPOHBEGB.f;
        return pPOHBEGB;
    }

    public PPOHBEGB a(boolean bl) {
        try {
            if (bl) {
                throw new NullPointerException();
            }
            PPOHBEGB pPOHBEGB = this.d;
            if (pPOHBEGB == this.c) {
                this.d = null;
                return null;
            }
            this.d = pPOHBEGB.f;
            return pPOHBEGB;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("42563, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public int c() {
        int n = 0;
        PPOHBEGB pPOHBEGB = this.c.f;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && PPOHBEGB.h == 0) continue;
            ++n;
            pPOHBEGB = pPOHBEGB.f;
        } while (pPOHBEGB != this.c);
        return n;
    }
}

