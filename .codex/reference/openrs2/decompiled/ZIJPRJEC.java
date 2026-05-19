/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class ZIJPRJEC
implements Runnable {
    public client a;
    public Object b = new Object();
    public int[] c = new int[500];
    public boolean d = true;
    public int[] e = new int[500];
    public int f;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void run() {
        boolean bl = true;
        do {
            if (bl && !(bl = false) && client.Jj == 0) continue;
            Object object = this.b;
            synchronized (object) {
                if (this.f < 500) {
                    this.e[this.f] = this.a.t;
                    this.c[this.f] = this.a.u;
                    ++this.f;
                }
            }
            try {
                Thread.sleep(50L);
            }
            catch (Exception exception) {}
        } while (this.d);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ZIJPRJEC(client client2, int n) {
        try {
            if (client.Jj != 0 || n >= 0) {
                throw new NullPointerException();
            }
            this.a = client2;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("5657, " + client2 + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

