/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public final class LHGXPZPG {
    private boolean a = false;
    private int b = -77;
    public PKVMXVTO c = new PKVMXVTO();
    private PKVMXVTO d;

    public LHGXPZPG(int n) {
        try {
            if (n <= 0) {
                this.a = !this.a;
            }
            this.c.c = this.c;
            this.c.d = this.c;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("91809, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(PKVMXVTO pKVMXVTO) {
        if (pKVMXVTO.d != null) {
            pKVMXVTO.a();
        }
        pKVMXVTO.d = this.c.d;
        pKVMXVTO.c = this.c;
        pKVMXVTO.d.c = pKVMXVTO;
        pKVMXVTO.c.d = pKVMXVTO;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, PKVMXVTO pKVMXVTO) {
        int n2 = PPOHBEGB.h;
        try {
            if (pKVMXVTO.d != null) {
                pKVMXVTO.a();
            }
            pKVMXVTO.d = this.c;
            pKVMXVTO.c = this.c.c;
            boolean bl = true;
            do {
                if (bl && !(bl = false)) {
                    if (n2 == 0) continue;
                    PKVMXVTO.e = !PKVMXVTO.e;
                }
                boolean bl2 = this.a = !this.a;
            } while (n >= 0);
            pKVMXVTO.d.c = pKVMXVTO;
            pKVMXVTO.c.d = pKVMXVTO;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("66209, " + n + ", " + pKVMXVTO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public PKVMXVTO a() {
        PKVMXVTO pKVMXVTO = this.c.c;
        if (pKVMXVTO == this.c) {
            return null;
        }
        pKVMXVTO.a();
        return pKVMXVTO;
    }

    public PKVMXVTO b() {
        PKVMXVTO pKVMXVTO = this.c.c;
        if (pKVMXVTO == this.c) {
            this.d = null;
            return null;
        }
        this.d = pKVMXVTO.c;
        return pKVMXVTO;
    }

    public PKVMXVTO a(int n) {
        try {
            if (n < 5 || n > 5) {
                throw new NullPointerException();
            }
            PKVMXVTO pKVMXVTO = this.c.d;
            if (pKVMXVTO == this.c) {
                this.d = null;
                return null;
            }
            this.d = pKVMXVTO.d;
            return pKVMXVTO;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("79197, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public PKVMXVTO a(boolean bl) {
        try {
            PKVMXVTO pKVMXVTO = this.d;
            if (bl) {
                this.b = 48;
            }
            if (pKVMXVTO == this.c) {
                this.d = null;
                return null;
            }
            this.d = pKVMXVTO.c;
            return pKVMXVTO;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("91709, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public PKVMXVTO b(int n) {
        try {
            PKVMXVTO pKVMXVTO = this.d;
            if (pKVMXVTO == this.c) {
                this.d = null;
                return null;
            }
            this.d = pKVMXVTO.d;
            if (n != 8) {
                throw new NullPointerException();
            }
            return pKVMXVTO;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("43802, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void c() {
        if (this.c.c == this.c) {
            return;
        }
        PKVMXVTO pKVMXVTO;
        while ((pKVMXVTO = this.c.c) != this.c) {
            pKVMXVTO.a();
        }
        return;
    }
}

