/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public final class JOCFVBOI {
    private int a = -436;
    private int b = -431;
    private int c;
    private int[] d;
    private int[] e;
    private int f;
    private int g;
    private int h;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public JOCFVBOI(int n, int[] nArray) {
        try {
            this.e = new int[256];
            this.d = new int[256];
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !MBMGIXGO.L) continue;
                this.d[n2] = nArray[n2];
                ++n2;
            } while (n2 < nArray.length);
            if (n >= 0) {
                this.b = -242;
            }
            this.c();
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("7019, " + n + ", " + nArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final int a() {
        if (this.c-- == 0) {
            this.b();
            this.c = 255;
        }
        return this.d[this.c];
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private final void b() {
        boolean bl = MBMGIXGO.L;
        this.g += ++this.h;
        int n = 0;
        boolean bl2 = true;
        do {
            int n2;
            int n3;
            block6: {
                block8: {
                    block7: {
                        block5: {
                            if (bl2 && !(bl2 = false) && !bl) continue;
                            n3 = this.e[n];
                            if ((n & 3) != 0) break block5;
                            this.f ^= this.f << 13;
                            if (!bl) break block6;
                        }
                        if ((n & 3) != 1) break block7;
                        this.f ^= this.f >>> 6;
                        if (!bl) break block6;
                    }
                    if ((n & 3) != 2) break block8;
                    this.f ^= this.f << 2;
                    if (!bl) break block6;
                }
                if ((n & 3) == 3) {
                    this.f ^= this.f >>> 16;
                }
            }
            this.f += this.e[n + 128 & 0xFF];
            this.e[n] = n2 = this.e[(n3 & 0x3FC) >> 2] + this.f + this.g;
            this.d[n] = this.g = this.e[(n2 >> 8 & 0x3FC) >> 2] + n3;
            ++n;
        } while (n < 256);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    private final void c() {
        boolean bl = MBMGIXGO.L;
        int n = -1640531527;
        int n2 = -1640531527;
        int n3 = -1640531527;
        int n4 = -1640531527;
        int n5 = -1640531527;
        int n6 = -1640531527;
        int n7 = -1640531527;
        int n8 = -1640531527;
        int n9 = 0;
        boolean bl2 = true;
        do {
            if (bl2 && !(bl2 = false) && !bl) continue;
            n5 += (n8 ^= n7 << 11);
            n7 += n6;
            n4 += (n7 ^= n6 >>> 2);
            n6 += n5;
            n3 += (n6 ^= n5 << 8);
            n5 += n4;
            n2 += (n5 ^= n4 >>> 16);
            n4 += n3;
            n += (n4 ^= n3 << 10);
            n3 += n2;
            n8 += (n3 ^= n2 >>> 4);
            n2 += n;
            n7 += (n2 ^= n << 8);
            n += n8;
            n6 += (n ^= n8 >>> 9);
            n8 += n7;
            ++n9;
        } while (n9 < 4);
        n9 = 0;
        boolean bl3 = true;
        do {
            if (bl3 && !(bl3 = false) && !bl) continue;
            n8 += this.d[n9];
            n7 += this.d[n9 + 1];
            n6 += this.d[n9 + 2];
            n5 += this.d[n9 + 3];
            n4 += this.d[n9 + 4];
            n3 += this.d[n9 + 5];
            n2 += this.d[n9 + 6];
            n += this.d[n9 + 7];
            n5 += (n8 ^= n7 << 11);
            n7 += n6;
            n4 += (n7 ^= n6 >>> 2);
            n6 += n5;
            n3 += (n6 ^= n5 << 8);
            n5 += n4;
            n2 += (n5 ^= n4 >>> 16);
            n4 += n3;
            n += (n4 ^= n3 << 10);
            n3 += n2;
            n8 += (n3 ^= n2 >>> 4);
            n2 += n;
            n7 += (n2 ^= n << 8);
            n += n8;
            n6 += (n ^= n8 >>> 9);
            this.e[n9] = n8 += n7;
            this.e[n9 + 1] = n7;
            this.e[n9 + 2] = n6;
            this.e[n9 + 3] = n5;
            this.e[n9 + 4] = n4;
            this.e[n9 + 5] = n3;
            this.e[n9 + 6] = n2;
            this.e[n9 + 7] = n;
            n9 += 8;
        } while (n9 < 256);
        n9 = 0;
        boolean bl4 = true;
        do {
            if (bl4 && !(bl4 = false) && !bl) continue;
            n8 += this.e[n9];
            n7 += this.e[n9 + 1];
            n6 += this.e[n9 + 2];
            n5 += this.e[n9 + 3];
            n4 += this.e[n9 + 4];
            n3 += this.e[n9 + 5];
            n2 += this.e[n9 + 6];
            n += this.e[n9 + 7];
            n5 += (n8 ^= n7 << 11);
            n7 += n6;
            n4 += (n7 ^= n6 >>> 2);
            n6 += n5;
            n3 += (n6 ^= n5 << 8);
            n5 += n4;
            n2 += (n5 ^= n4 >>> 16);
            n4 += n3;
            n += (n4 ^= n3 << 10);
            n3 += n2;
            n8 += (n3 ^= n2 >>> 4);
            n2 += n;
            n7 += (n2 ^= n << 8);
            n += n8;
            n6 += (n ^= n8 >>> 9);
            this.e[n9] = n8 += n7;
            this.e[n9 + 1] = n7;
            this.e[n9 + 2] = n6;
            this.e[n9 + 3] = n5;
            this.e[n9 + 4] = n4;
            this.e[n9 + 5] = n3;
            this.e[n9 + 6] = n2;
            this.e[n9 + 7] = n;
            n9 += 8;
        } while (n9 < 256);
        this.b();
        this.c = 256;
    }
}

