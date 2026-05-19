/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class CDEJWOSB {
    private static int a = -12499;
    private static int b = -192;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static int a(int n, int n2, int n3, boolean bl) {
        try {
            n &= 3;
            if (bl) {
                int n4 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && LKGEGIEW.t == 0) continue;
                    ++n4;
                } while (n4 > 0);
            }
            if (n == 0) {
                return n3;
            }
            if (n == 1) {
                return n2;
            }
            if (n == 2) {
                return 7 - n3;
            }
            return 7 - n2;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("92720, " + n + ", " + n2 + ", " + n3 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static int a(int n, int n2, int n3, int n4) {
        try {
            if (n3 >= 0) {
                b = -146;
            }
            if ((n2 &= 3) == 0) {
                return n;
            }
            if (n2 == 1) {
                return 7 - n4;
            }
            if (n2 == 2) {
                return 7 - n;
            }
            return n4;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("51053, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static int a(int n, int n2, int n3, byte by, int n4, int n5) {
        try {
            if (by != 113) {
                return a;
            }
            if ((n &= 3) == 0) {
                return n3;
            }
            if (n == 1) {
                return n4;
            }
            if (n == 2) {
                return 7 - n3 - (n5 - 1);
            }
            return 7 - n4 - (n2 - 1);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("92592, " + n + ", " + n2 + ", " + n3 + ", " + by + ", " + n4 + ", " + n5 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static int a(int n, int n2, int n3, int n4, int n5, int n6) {
        try {
            n4 &= 3;
            if (n >= 0) {
                return b;
            }
            if (n4 == 0) {
                return n2;
            }
            if (n4 == 1) {
                return 7 - n6 - (n5 - 1);
            }
            if (n4 == 2) {
                return 7 - n2 - (n3 - 1);
            }
            return n6;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("62488, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

