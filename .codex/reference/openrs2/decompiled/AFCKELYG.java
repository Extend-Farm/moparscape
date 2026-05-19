/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class AFCKELYG
extends PPOHBEGB {
    private static int i = 1;
    private static boolean j = true;
    private static int k = -12499;
    private static boolean l;
    public static int[] m;
    public static int n;
    public static int o;
    public static int p;
    public static int q;
    public static int r;
    public static int s;
    public static int t;
    public static int u;
    public static int v;
    public static boolean w;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, int n2, int n3, int[] nArray) {
        try {
            m = nArray;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !w) continue;
                i = 275;
            } while (n3 >= 0);
            AFCKELYG.n = n2;
            o = n;
            AFCKELYG.a(n, 0, false, n2, 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("11606, " + n + ", " + n2 + ", " + n3 + ", " + nArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static void a(int n) {
        try {
            if (n < 4 || n > 4) {
                return;
            }
            r = 0;
            p = 0;
            s = AFCKELYG.n;
            q = o;
            t = s - 1;
            u = s / 2;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("33573, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static void a(int n, int n2, boolean bl, int n3, int n4) {
        try {
            if (n2 < 0) {
                n2 = 0;
            }
            if (n4 < 0) {
                n4 = 0;
            }
            if (n3 > AFCKELYG.n) {
                n3 = AFCKELYG.n;
            }
            if (n > o) {
                n = o;
            }
            r = n2;
            p = n4;
            s = n3;
            q = n;
            t = s - 1;
            if (bl) {
                i = 458;
            }
            u = s / 2;
            v = q / 2;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("69929, " + n + ", " + n2 + ", " + bl + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(boolean bl) {
        try {
            if (!bl) {
                l = !l;
            }
            int n = AFCKELYG.n * o;
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !w) continue;
                AFCKELYG.m[n2] = 0;
                ++n2;
            } while (n2 < n);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("70033, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        boolean bl = w;
        try {
            if (n7 < r) {
                n3 -= r - n7;
                n7 = r;
            }
            if (n2 < p) {
                n4 -= p - n2;
                n2 = p;
            }
            if (n7 + n3 > s) {
                n3 = s - n7;
            }
            if (n2 + n4 > q) {
                n4 = q - n2;
            }
            int n8 = 256 - n5;
            int n9 = (n >> 16 & 0xFF) * n5;
            int n10 = (n >> 8 & 0xFF) * n5;
            int n11 = (n & 0xFF) * n5;
            int n12 = AFCKELYG.n - n3;
            int n13 = n7 + n2 * AFCKELYG.n;
            int n14 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n15 = -n3;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    int n16 = (m[n13] >> 16 & 0xFF) * n8;
                    int n17 = (m[n13] >> 8 & 0xFF) * n8;
                    int n18 = (m[n13] & 0xFF) * n8;
                    int n19 = (n9 + n16 >> 8 << 16) + (n10 + n17 >> 8 << 8) + (n11 + n18 >> 8);
                    AFCKELYG.m[n13++] = n19;
                    ++n15;
                } while (n15 < 0);
                n13 += n12;
                ++n14;
            } while (n14 < n4);
            if (n6 == 0) return;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("38767, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, int n2, int n3, int n4, int n5, int n6) {
        boolean bl = w;
        try {
            if (n3 < r) {
                n5 -= r - n3;
                n3 = r;
            }
            if (n2 < p) {
                n -= p - n2;
                n2 = p;
            }
            if (n3 + n5 > s) {
                n5 = s - n3;
            }
            if (n2 + n > q) {
                n = q - n2;
            }
            int n7 = AFCKELYG.n - n5;
            int n8 = n3 + n2 * AFCKELYG.n;
            if (n6 != 0) {
                i = -374;
            }
            int n9 = -n;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !bl) continue;
                int n10 = -n5;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl) continue;
                    AFCKELYG.m[n8++] = n4;
                    ++n10;
                } while (n10 < 0);
                n8 += n7;
                ++n9;
            } while (n9 < 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("38357, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static void a(int n, int n2, int n3, int n4, int n5, boolean bl) {
        try {
            AFCKELYG.a(n5, n4, n2, n, (byte)4);
            AFCKELYG.a(n5 + n3 - 1, n4, n2, n, (byte)4);
            AFCKELYG.a(n5, n4, n3, n, k);
            AFCKELYG.a(n5, n4, n3, n + n2 - 1, k);
            if (!bl) {
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("84944, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void b(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        try {
            AFCKELYG.a(n4, n5, n, true, n3, n6);
            if (n7 != -17319) {
                int n8 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && !w) continue;
                    ++n8;
                } while (n8 > 0);
            }
            AFCKELYG.a(n4, n5, n + n2 - 1, true, n3, n6);
            if (n2 < 3) return;
            AFCKELYG.a(n4, n6, n3, n + 1, (byte)3, n2 - 2);
            AFCKELYG.a(n4, n6 + n5 - 1, n3, n + 1, (byte)3, n2 - 2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("34302, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, int n2, int n3, int n4, byte by) {
        try {
            if (n < p || n >= q) {
                return;
            }
            if (n4 < r) {
                n3 -= r - n4;
                n4 = r;
            }
            if (n4 + n3 > s) {
                n3 = s - n4;
            }
            int n5 = n4 + n * AFCKELYG.n;
            if (by != 4) {
                return;
            }
            int n6 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !w) continue;
                AFCKELYG.m[n5 + n6] = n2;
                ++n6;
            } while (n6 < n3);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("10424, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, int n2, int n3, boolean bl, int n4, int n5) {
        try {
            if (n3 < p || n3 >= q) {
                return;
            }
            if (n5 < r) {
                n2 -= r - n5;
                n5 = r;
            }
            if (n5 + n2 > s) {
                n2 = s - n5;
            }
            int n6 = 256 - n4;
            int n7 = (n >> 16 & 0xFF) * n4;
            int n8 = (n >> 8 & 0xFF) * n4;
            int n9 = (n & 0xFF) * n4;
            int n10 = n5 + n3 * AFCKELYG.n;
            if (!bl) {
                i = 86;
            }
            int n11 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && !w) continue;
                int n12 = (m[n10] >> 16 & 0xFF) * n6;
                int n13 = (m[n10] >> 8 & 0xFF) * n6;
                int n14 = (m[n10] & 0xFF) * n6;
                int n15 = (n7 + n12 >> 8 << 16) + (n8 + n13 >> 8 << 8) + (n9 + n14 >> 8);
                AFCKELYG.m[n10++] = n15;
                ++n11;
            } while (n11 < n2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("81344, " + n + ", " + n2 + ", " + n3 + ", " + bl + ", " + n4 + ", " + n5 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, int n2, int n3, int n4, int n5) {
        try {
            if (n4 < r || n4 >= s) {
                return;
            }
            if (n < p) {
                n3 -= p - n;
                n = p;
            }
            if (n + n3 > q) {
                n3 = q - n;
            }
            int n6 = n4 + n * AFCKELYG.n;
            if (n5 != k) {
                return;
            }
            int n7 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !w) continue;
                AFCKELYG.m[n6 + n7 * AFCKELYG.n] = n2;
                ++n7;
            } while (n7 < n3);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("63803, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(int n, int n2, int n3, int n4, byte by, int n5) {
        try {
            if (n2 < r || n2 >= s) {
                return;
            }
            if (n4 < p) {
                n5 -= p - n4;
                n4 = p;
            }
            if (n4 + n5 > q) {
                n5 = q - n4;
            }
            int n6 = 256 - n3;
            int n7 = (n >> 16 & 0xFF) * n3;
            int n8 = (n >> 8 & 0xFF) * n3;
            int n9 = (n & 0xFF) * n3;
            if (by != 3) {
                return;
            }
            int n10 = n2 + n4 * AFCKELYG.n;
            int n11 = 0;
            boolean bl = true;
            do {
                int n12;
                if (bl && !(bl = false) && !w) continue;
                int n13 = (m[n10] >> 16 & 0xFF) * n6;
                int n14 = (m[n10] >> 8 & 0xFF) * n6;
                int n15 = (m[n10] & 0xFF) * n6;
                AFCKELYG.m[n10] = n12 = (n7 + n13 >> 8 << 16) + (n8 + n14 >> 8 << 8) + (n9 + n15 >> 8);
                n10 += AFCKELYG.n;
                ++n11;
            } while (n11 < n5);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("1088, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + by + ", " + n5 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

