/*
 * Decompiled with CFR 0.152.
 */
import java.math.BigInteger;
import sign.signlink;

public final class MBMGIXGO
extends PPOHBEGB {
    private int i = 891;
    private int j = 9;
    private byte k = (byte)14;
    private int l = -29508;
    private int m = 881;
    private byte n = (byte)8;
    private int o = 657;
    private boolean p = false;
    private int q = -715;
    private byte r = (byte)-57;
    private byte s = (byte)108;
    private byte t = (byte)3;
    private boolean u = false;
    private int v = -373;
    private boolean w = false;
    private boolean x = true;
    public byte[] y;
    public int z;
    public int A;
    private static int[] B = new int[256];
    private static final int[] C;
    public JOCFVBOI D;
    private static int E;
    private static int F;
    private static int G;
    private static LHGXPZPG H;
    private static LHGXPZPG I;
    private static LHGXPZPG J;
    private static char[] K;
    public static boolean L;

    public static MBMGIXGO a(int n, int n2) {
        boolean bl = L;
        try {
            MBMGIXGO mBMGIXGO;
            block16: {
                block17: {
                    block15: {
                        LHGXPZPG lHGXPZPG = I;
                        synchronized (lHGXPZPG) {
                            MBMGIXGO mBMGIXGO2 = null;
                            if (n == 0 && E > 0) {
                                --E;
                                mBMGIXGO2 = (MBMGIXGO)H.a();
                            } else if (n == 1 && F > 0) {
                                --F;
                                mBMGIXGO2 = (MBMGIXGO)I.a();
                            } else if (n == 2 && G > 0) {
                                --G;
                                mBMGIXGO2 = (MBMGIXGO)J.a();
                            }
                            if (mBMGIXGO2 != null) {
                                mBMGIXGO2.z = 0;
                                MBMGIXGO mBMGIXGO3 = mBMGIXGO2;
                                Object var4_8 = null;
                                return mBMGIXGO3;
                            }
                        }
                        if (n2 < 9 || n2 > 9) {
                            throw new NullPointerException();
                        }
                        mBMGIXGO = new MBMGIXGO(false);
                        mBMGIXGO.z = 0;
                        if (n != 0) break block15;
                        mBMGIXGO.y = new byte[100];
                        if (!bl) break block16;
                    }
                    if (n != 1) break block17;
                    mBMGIXGO.y = new byte[5000];
                    if (!bl) break block16;
                }
                mBMGIXGO.y = new byte[30000];
            }
            return mBMGIXGO;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("94514, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private MBMGIXGO(boolean bl) {
        try {
            if (bl) {
                throw new NullPointerException();
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("46414, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public MBMGIXGO(byte[] byArray, int n) {
        try {
            if (n <= 0) {
                throw new NullPointerException();
            }
            this.y = byArray;
            this.z = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("38214, " + byArray + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(byte by, int n) {
        try {
            if (by != 6) {
                int n2 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && !L) continue;
                    ++n2;
                } while (n2 > 0);
            }
            this.y[this.z++] = (byte)(n + this.D.a());
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("9884, " + by + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n) {
        this.y[this.z++] = (byte)n;
    }

    public void b(int n) {
        this.y[this.z++] = (byte)(n >> 8);
        this.y[this.z++] = (byte)n;
    }

    public void a(boolean bl, int n) {
        try {
            this.y[this.z++] = (byte)n;
            this.y[this.z++] = (byte)(n >> 8);
            if (!bl) {
                this.i = -142;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("83632, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void c(int n) {
        this.y[this.z++] = (byte)(n >> 16);
        this.y[this.z++] = (byte)(n >> 8);
        this.y[this.z++] = (byte)n;
    }

    public void d(int n) {
        this.y[this.z++] = (byte)(n >> 24);
        this.y[this.z++] = (byte)(n >> 16);
        this.y[this.z++] = (byte)(n >> 8);
        this.y[this.z++] = (byte)n;
    }

    public void b(int n, int n2) {
        try {
            this.y[this.z++] = (byte)n2;
            this.y[this.z++] = (byte)(n2 >> 8);
            if (n != 0) {
                return;
            }
            this.y[this.z++] = (byte)(n2 >> 16);
            this.y[this.z++] = (byte)(n2 >> 24);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("9235, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, long l) {
        block3: {
            boolean bl = L;
            try {
                this.y[this.z++] = (byte)(l >> 56);
                this.y[this.z++] = (byte)(l >> 48);
                this.y[this.z++] = (byte)(l >> 40);
                this.y[this.z++] = (byte)(l >> 32);
                if (n < 5 || n > 5) {
                    this.v = 409;
                }
                this.y[this.z++] = (byte)(l >> 24);
                this.y[this.z++] = (byte)(l >> 16);
                this.y[this.z++] = (byte)(l >> 8);
                this.y[this.z++] = (byte)l;
                if (!bl) break block3;
                PKVMXVTO.e = !PKVMXVTO.e;
            }
            catch (RuntimeException runtimeException) {
                signlink.reporterror("14395, " + n + ", " + l + ", " + runtimeException.toString());
                throw new RuntimeException();
            }
        }
    }

    public void a(String string) {
        string.getBytes(0, string.length(), this.y, this.z);
        this.z += string.length();
        this.y[this.z++] = 10;
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(byte[] byArray, int n, boolean bl, int n2) {
        try {
            if (!bl) {
                this.u = !this.u;
            }
            int n3 = n2;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !L) continue;
                this.y[this.z++] = byArray[n3];
                ++n3;
            } while (n3 < n2 + n);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("70990, " + byArray + ", " + n + ", " + bl + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, byte by) {
        try {
            this.y[this.z - n - 1] = (byte)n;
            if (by == 0) {
                by = 0;
                return;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("82134, " + n + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int c() {
        return this.y[this.z++] & 0xFF;
    }

    public byte d() {
        return this.y[this.z++];
    }

    public int e() {
        this.z += 2;
        return ((this.y[this.z - 2] & 0xFF) << 8) + (this.y[this.z - 1] & 0xFF);
    }

    public int f() {
        this.z += 2;
        int n = ((this.y[this.z - 2] & 0xFF) << 8) + (this.y[this.z - 1] & 0xFF);
        if (n > Short.MAX_VALUE) {
            n -= 65536;
        }
        return n;
    }

    public int g() {
        this.z += 3;
        return ((this.y[this.z - 3] & 0xFF) << 16) + ((this.y[this.z - 2] & 0xFF) << 8) + (this.y[this.z - 1] & 0xFF);
    }

    public int h() {
        this.z += 4;
        return ((this.y[this.z - 4] & 0xFF) << 24) + ((this.y[this.z - 3] & 0xFF) << 16) + ((this.y[this.z - 2] & 0xFF) << 8) + (this.y[this.z - 1] & 0xFF);
    }

    public long e(int n) {
        try {
            long l = (long)this.h() & 0xFFFFFFFFL;
            if (n != -35089) {
                this.w = !this.w;
            }
            long l2 = (long)this.h() & 0xFFFFFFFFL;
            return (l << 32) + l2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("20297, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public String i() {
        int n = this.z;
        while (this.y[this.z++] != 10) {
        }
        return new String(this.y, n, this.z - n - 1);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] a(byte by) {
        try {
            int n = this.z;
            while (this.y[this.z++] != 10) {
            }
            byte[] byArray = new byte[this.z - n - 1];
            if (by != 30) {
                this.x = !this.x;
            }
            int n2 = n;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !L) continue;
                byArray[n2 - n] = this.y[n2];
                ++n2;
            } while (n2 < this.z - 1);
            return byArray;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("54420, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, byte by, int n2, byte[] byArray) {
        boolean bl = L;
        try {
            int n3;
            if (by != 14) {
                n3 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && !bl) continue;
                    ++n3;
                } while (n3 > 0);
            }
            n3 = n2;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && !bl) continue;
                byArray[n3] = this.y[this.z++];
                ++n3;
            } while (n3 < n2 + n);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("47406, " + n + ", " + by + ", " + n2 + ", " + byArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void f(int n) {
        try {
            this.A = this.z * 8;
            if (n == this.l) return;
            int n2 = 1;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !L) continue;
                ++n2;
            } while (n2 > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("30072, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int c(int n, int n2) {
        boolean bl = L;
        try {
            int n3;
            block8: {
                int n4;
                int n5;
                block7: {
                    n5 = this.A >> 3;
                    n4 = 8 - (this.A & 7);
                    n3 = 0;
                    if (n2 != 0) {
                        this.w = !this.w;
                    }
                    this.A += n;
                    boolean bl2 = true;
                    do {
                        if (bl2 && !(bl2 = false) && !bl) continue;
                        n3 += (this.y[n5++] & C[n4]) << n - n4;
                        n -= n4;
                        n4 = 8;
                    } while (n > n4);
                    if (n != n4) break block7;
                    n3 += this.y[n5] & C[n4];
                    if (!bl) break block8;
                }
                n3 += this.y[n5] >> n4 - n & C[n];
            }
            return n3;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("74666, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(boolean bl) {
        try {
            this.z = (this.A + 7) / 8;
            if (bl) return;
            int n = 1;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !L) continue;
                ++n;
            } while (n > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("96565, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int j() {
        int n = this.y[this.z] & 0xFF;
        if (n < 128) {
            return this.c() - 64;
        }
        return this.e() - 49152;
    }

    public int k() {
        int n = this.y[this.z] & 0xFF;
        if (n < 128) {
            return this.c();
        }
        return this.e() - 32768;
    }

    public void a(BigInteger bigInteger, BigInteger bigInteger2, byte by) {
        try {
            int n = this.z;
            this.z = 0;
            byte[] byArray = new byte[n];
            this.a(n, this.k, 0, byArray);
            BigInteger bigInteger3 = new BigInteger(byArray);
            if (by != 0) {
                this.w = !this.w;
            }
            BigInteger bigInteger4 = bigInteger3.modPow(bigInteger, bigInteger2);
            byte[] byArray2 = bigInteger4.toByteArray();
            this.z = 0;
            this.a(byArray2.length);
            this.a(byArray2, byArray2.length, true, 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("13312, " + bigInteger + ", " + bigInteger2 + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void d(int n, int n2) {
        try {
            this.y[this.z++] = (byte)(-n);
            if (n2 == 0) return;
            int n3 = 1;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !L) continue;
                ++n3;
            } while (n3 > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("41426, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void e(int n, int n2) {
        try {
            this.y[this.z++] = (byte)(128 - n2);
            n = 90 / n;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("90775, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int g(int n) {
        try {
            if (n != 0) {
                return this.o;
            }
            return this.y[this.z++] - 128 & 0xFF;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("58967, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int b(boolean bl) {
        try {
            if (bl) {
                this.i = 310;
            }
            return -this.y[this.z++] & 0xFF;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("92864, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int h(int n) {
        try {
            if (n != 2) {
                this.x = !this.x;
            }
            return 128 - this.y[this.z++] & 0xFF;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("10465, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public byte b(byte by) {
        try {
            if (by != this.r) {
                throw new NullPointerException();
            }
            return -this.y[this.z++];
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("12463, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte i(int n) {
        try {
            if (n != 0) {
                int n2 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && !L) continue;
                    ++n2;
                } while (n2 > 0);
            }
            return (byte)(128 - this.y[this.z++]);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("7843, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void b(boolean bl, int n) {
        try {
            this.y[this.z++] = (byte)n;
            this.y[this.z++] = (byte)(n >> 8);
            if (!bl) {
                this.u = !this.u;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("38812, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void f(int n, int n2) {
        try {
            this.y[this.z++] = (byte)(n2 >> 8);
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !L) continue;
                this.v = 376;
            } while (n >= 0);
            this.y[this.z++] = (byte)(n2 + 128);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("18114, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void g(int n, int n2) {
        try {
            this.y[this.z++] = (byte)(n2 + 128);
            if (n != 0) {
                this.v = -238;
            }
            this.y[this.z++] = (byte)(n2 >> 8);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("91835, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int c(byte by) {
        try {
            this.z += 2;
            if (by != this.s) {
                return 3;
            }
            return ((this.y[this.z - 1] & 0xFF) << 8) + (this.y[this.z - 2] & 0xFF);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("82184, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int c(boolean bl) {
        try {
            if (!bl) {
                int n = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && !L) continue;
                    ++n;
                } while (n > 0);
            }
            this.z += 2;
            return ((this.y[this.z - 2] & 0xFF) << 8) + (this.y[this.z - 1] - 128 & 0xFF);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("52310, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int d(byte by) {
        try {
            this.z += 2;
            if (by != -74) {
                int n = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && !L) continue;
                    ++n;
                } while (n > 0);
            }
            return ((this.y[this.z - 1] & 0xFF) << 8) + (this.y[this.z - 2] - 128 & 0xFF);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("92143, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int j(int n) {
        try {
            this.z += 2;
            if (n >= 0) {
                return 2;
            }
            int n2 = ((this.y[this.z - 1] & 0xFF) << 8) + (this.y[this.z - 2] & 0xFF);
            if (n2 > Short.MAX_VALUE) {
                n2 -= 65536;
            }
            return n2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("19924, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int d(boolean bl) {
        try {
            int n;
            if (bl) {
                n = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && !L) continue;
                    ++n;
                } while (n > 0);
            }
            this.z += 2;
            n = ((this.y[this.z - 1] & 0xFF) << 8) + (this.y[this.z - 2] - 128 & 0xFF);
            if (n > Short.MAX_VALUE) {
                n -= 65536;
            }
            return n;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("99952, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int e(byte by) {
        try {
            if (by != 41) {
                return 3;
            }
            this.z += 4;
            return ((this.y[this.z - 2] & 0xFF) << 24) + ((this.y[this.z - 1] & 0xFF) << 16) + ((this.y[this.z - 4] & 0xFF) << 8) + (this.y[this.z - 3] & 0xFF);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("11599, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public int e(boolean bl) {
        try {
            if (!bl) {
                this.p = !this.p;
            }
            this.z += 4;
            return ((this.y[this.z - 3] & 0xFF) << 24) + ((this.y[this.z - 4] & 0xFF) << 16) + ((this.y[this.z - 1] & 0xFF) << 8) + (this.y[this.z - 2] & 0xFF);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("83720, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, byte by, byte[] byArray, int n2) {
        try {
            if (by != 6) {
                this.p = !this.p;
            }
            int n3 = n + n2 - 1;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !L) continue;
                this.y[this.z++] = (byte)(byArray[n3] + 128);
                --n3;
            } while (n3 >= n);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("26182, " + n + ", " + by + ", " + byArray + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2, boolean bl, byte[] byArray) {
        try {
            if (!bl) {
                this.p = !this.p;
            }
            int n3 = n2 + n - 1;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !L) continue;
                byArray[n3] = this.y[this.z++];
                --n3;
            } while (n3 >= n2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("6879, " + n + ", " + n2 + ", " + bl + ", " + byArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    static {
        int n = 0;
        while (n < 256) {
            int n2 = n;
            int n3 = 0;
            while (n3 < 8) {
                n2 = (n2 & 1) == 1 ? n2 >>> 1 ^ 0xEDB88320 : (n2 >>>= 1);
                ++n3;
            }
            MBMGIXGO.B[n] = n2;
            ++n;
        }
        int[] nArray = new int[33];
        nArray[1] = 1;
        nArray[2] = 3;
        nArray[3] = 7;
        nArray[4] = 15;
        nArray[5] = 31;
        nArray[6] = 63;
        nArray[7] = 127;
        nArray[8] = 255;
        nArray[9] = 511;
        nArray[10] = 1023;
        nArray[11] = 2047;
        nArray[12] = 4095;
        nArray[13] = 8191;
        nArray[14] = 16383;
        nArray[15] = Short.MAX_VALUE;
        nArray[16] = 65535;
        nArray[17] = 131071;
        nArray[18] = 262143;
        nArray[19] = 524287;
        nArray[20] = 1048575;
        nArray[21] = 0x1FFFFF;
        nArray[22] = 0x3FFFFF;
        nArray[23] = 0x7FFFFF;
        nArray[24] = 0xFFFFFF;
        nArray[25] = 0x1FFFFFF;
        nArray[26] = 0x3FFFFFF;
        nArray[27] = 0x7FFFFFF;
        nArray[28] = 0xFFFFFFF;
        nArray[29] = 0x1FFFFFFF;
        nArray[30] = 0x3FFFFFFF;
        nArray[31] = Integer.MAX_VALUE;
        nArray[32] = -1;
        C = nArray;
        H = new LHGXPZPG(169);
        I = new LHGXPZPG(169);
        J = new LHGXPZPG(169);
        K = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    }
}

