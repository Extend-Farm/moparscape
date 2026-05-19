/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public final class GCPOSBWX {
    private boolean a = false;
    private static int b;
    private boolean c = false;
    private int d;
    private int e;
    private PPOHBEGB f = new PPOHBEGB();
    private int g;
    private int h;
    private ARZPHHDH i;
    private BISVHPUN j = new BISVHPUN(b);

    public GCPOSBWX(boolean bl, int n) {
        try {
            this.g = n;
            this.h = n;
            this.i = new ARZPHHDH(-877, 1024);
            if (bl) {
                b = -225;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("52366, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public PPOHBEGB a(long l) {
        PPOHBEGB pPOHBEGB;
        block3: {
            block2: {
                pPOHBEGB = (PPOHBEGB)this.i.a(l);
                if (pPOHBEGB == null) break block2;
                this.j.a(pPOHBEGB);
                ++this.e;
                if (PPOHBEGB.h == 0) break block3;
            }
            ++this.d;
        }
        return pPOHBEGB;
    }

    public void a(PPOHBEGB pPOHBEGB, long l, byte by) {
        int n = PPOHBEGB.h;
        try {
            block8: {
                block7: {
                    if (by != 2) {
                        boolean bl = this.c = !this.c;
                    }
                    if (this.h != 0) break block7;
                    PPOHBEGB pPOHBEGB2 = this.j.a();
                    pPOHBEGB2.a();
                    pPOHBEGB2.b();
                    if (pPOHBEGB2 != this.f) break block8;
                    pPOHBEGB2 = this.j.a();
                    pPOHBEGB2.a();
                    pPOHBEGB2.b();
                    if (n == 0) break block8;
                }
                --this.h;
            }
            this.i.a(pPOHBEGB, l, (byte)7);
            this.j.a(pPOHBEGB);
            if (PKVMXVTO.e) {
                PPOHBEGB.h = ++n;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("47547, " + pPOHBEGB + ", " + l + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a() {
        PPOHBEGB pPOHBEGB;
        while ((pPOHBEGB = this.j.a()) != null) {
            pPOHBEGB.a();
            pPOHBEGB.b();
            if (PPOHBEGB.h == 0) continue;
        }
        this.h = this.g;
    }
}

