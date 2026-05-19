/*
 * Decompiled with CFR 0.152.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import sign.signlink;

public final class NQABEVLK
implements Runnable {
    private int a = -53;
    private boolean b = true;
    private int c = 519;
    private InputStream d;
    private OutputStream e;
    private Socket f;
    private boolean g = false;
    KHACHIFW h;
    private byte[] i;
    private int j;
    private int k;
    private boolean l = false;
    private boolean m = false;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public NQABEVLK(KHACHIFW kHACHIFW, int n, Socket socket) throws IOException {
        try {
            boolean bl = true;
            do {
                if (bl && !(bl = false) && KHACHIFW.H == 0) continue;
                boolean bl2 = this.b = !this.b;
            } while (n >= 0);
            this.h = kHACHIFW;
            this.f = socket;
            this.f.setSoTimeout(30000);
            this.f.setTcpNoDelay(true);
            this.d = this.f.getInputStream();
            this.e = this.f.getOutputStream();
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("56019, " + kHACHIFW + ", " + n + ", " + socket + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a() {
        this.g = true;
        try {
            if (this.d != null) {
                this.d.close();
            }
            if (this.e != null) {
                this.e.close();
            }
            if (this.f != null) {
                this.f.close();
            }
        }
        catch (IOException iOException) {
            System.out.println("Error closing stream");
        }
        this.l = false;
        NQABEVLK nQABEVLK = this;
        synchronized (nQABEVLK) {
            this.notify();
        }
        this.i = null;
    }

    public int b() throws IOException {
        if (this.g) {
            return 0;
        }
        return this.d.read();
    }

    public int c() throws IOException {
        if (this.g) {
            return 0;
        }
        return this.d.available();
    }

    /*
     * Unable to fully structure code
     */
    public void a(byte[] var1_1, int var2_2, int var3_3) throws IOException {
        if (!this.g) ** GOTO lbl8
        return;
lbl-1000:
        // 1 sources

        {
            var4_4 = this.d.read(var1_1, var2_2, var3_3);
            if (var4_4 <= 0) {
                throw new IOException("EOF");
            }
            var2_2 += var4_4;
            var3_3 -= var4_4;
lbl8:
            // 2 sources

            ** while (var3_3 > 0)
        }
lbl9:
        // 1 sources

    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2, byte[] byArray, int n3) throws IOException {
        try {
            if (this.g) {
                return;
            }
            if (this.m) {
                this.m = false;
                throw new IOException("Error in writer thread");
            }
            if (this.i == null) {
                this.i = new byte[5000];
            }
            NQABEVLK nQABEVLK = this;
            synchronized (nQABEVLK) {
                int n4 = 0;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && KHACHIFW.H == 0) continue;
                    this.i[this.k] = byArray[n4 + n3];
                    this.k = (this.k + 1) % 5000;
                    if (this.k == (this.j + 4900) % 5000) {
                        throw new IOException("buffer overflow");
                    }
                    ++n4;
                } while (n4 < n);
                if (!this.l) {
                    this.l = true;
                    this.h.a(this, 3);
                }
                this.notify();
            }
            if (n2 == 0) return;
            this.c = 255;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("71200, " + n + ", " + n2 + ", " + byArray + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void run() {
        boolean bl = true;
        do {
            int n;
            int n2;
            block16: {
                int n3;
                if (bl && !(bl = false) && (n3 = KHACHIFW.H) == 0) continue;
                NQABEVLK nQABEVLK = this;
                synchronized (nQABEVLK) {
                    block15: {
                        if (this.k == this.j) {
                            try {
                                this.wait();
                            }
                            catch (InterruptedException interruptedException) {}
                        }
                        if (!this.l) {
                            return;
                        }
                        n2 = this.j;
                        if (this.k < this.j) break block15;
                        n = this.k - this.j;
                        if (n3 == 0) break block16;
                    }
                    n = 5000 - this.j;
                }
            }
            if (n <= 0) continue;
            try {
                this.e.write(this.i, n2, n);
            }
            catch (IOException iOException) {
                this.m = true;
            }
            this.j = (this.j + n) % 5000;
            try {
                if (this.k != this.j) continue;
                this.e.flush();
            }
            catch (IOException iOException) {
                this.m = true;
            }
        } while (this.l);
    }

    public void a(byte by) {
        try {
            if (by != 1) {
                this.a = 457;
            }
            System.out.println("dummy:" + this.g);
            System.out.println("tcycl:" + this.j);
            System.out.println("tnum:" + this.k);
            System.out.println("writer:" + this.l);
            System.out.println("ioerror:" + this.m);
            try {
                System.out.println("available:" + this.c());
                return;
            }
            catch (IOException iOException) {
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("41293, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

