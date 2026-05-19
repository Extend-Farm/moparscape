/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class SQHJOGRT {
    private static int a = -715;
    private static SQHJOGRT[] b;
    public int c;
    public KVCQPLIW d;
    public int e;
    public int[] f;
    public int[] g;
    public int[] h;
    public int[] i;
    private static boolean[] j;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public static void a(int n) {
        b = new SQHJOGRT[n + 1];
        j = new boolean[n + 1];
        int n2 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && !XHHRODPC.l) continue;
            SQHJOGRT.j[n2] = true;
            ++n2;
        } while (n2 < n + 1);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(byte[] byArray, boolean bl) {
        boolean bl2 = XHHRODPC.l;
        try {
            MBMGIXGO mBMGIXGO = new MBMGIXGO(byArray, 891);
            mBMGIXGO.z = byArray.length - 8;
            int n = mBMGIXGO.e();
            int n2 = mBMGIXGO.e();
            int n3 = mBMGIXGO.e();
            int n4 = mBMGIXGO.e();
            int n5 = 0;
            MBMGIXGO mBMGIXGO2 = new MBMGIXGO(byArray, 891);
            mBMGIXGO2.z = n5;
            MBMGIXGO mBMGIXGO3 = new MBMGIXGO(byArray, 891);
            mBMGIXGO3.z = n5 += n + 2;
            MBMGIXGO mBMGIXGO4 = new MBMGIXGO(byArray, 891);
            mBMGIXGO4.z = n5 += n2;
            MBMGIXGO mBMGIXGO5 = new MBMGIXGO(byArray, 891);
            mBMGIXGO5.z = n5 += n3;
            MBMGIXGO mBMGIXGO6 = new MBMGIXGO(byArray, 891);
            mBMGIXGO6.z = n5 += n4;
            if (bl) {
                int n6 = 1;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl2) continue;
                    ++n6;
                } while (n6 > 0);
            }
            KVCQPLIW kVCQPLIW = new KVCQPLIW(0, mBMGIXGO6);
            int n7 = mBMGIXGO2.e();
            int[] nArray = new int[500];
            int[] nArray2 = new int[500];
            int[] nArray3 = new int[500];
            int[] nArray4 = new int[500];
            int n8 = 0;
            boolean bl4 = true;
            do {
                int n9;
                if (bl4 && !(bl4 = false) && !bl2) continue;
                int n10 = mBMGIXGO2.e();
                SQHJOGRT sQHJOGRT = SQHJOGRT.b[n10] = new SQHJOGRT();
                sQHJOGRT.c = mBMGIXGO5.c();
                sQHJOGRT.d = kVCQPLIW;
                int n11 = mBMGIXGO2.c();
                int n12 = -1;
                int n13 = 0;
                int n14 = 0;
                boolean bl5 = true;
                do {
                    block24: {
                        block30: {
                            int n15;
                            block29: {
                                block28: {
                                    block27: {
                                        block26: {
                                            block25: {
                                                if (bl5 && !(bl5 = false) && !bl2) continue;
                                                n9 = mBMGIXGO3.c();
                                                if (n9 <= 0) break block24;
                                                if (kVCQPLIW.c[n14] != 0) {
                                                    n15 = n14 - 1;
                                                    boolean bl6 = true;
                                                    do {
                                                        if (bl6 && !(bl6 = false) && !bl2) continue;
                                                        if (kVCQPLIW.c[n15] == 0) {
                                                            nArray[n13] = n15;
                                                            nArray2[n13] = 0;
                                                            nArray3[n13] = 0;
                                                            nArray4[n13] = 0;
                                                            ++n13;
                                                            if (!bl2) break;
                                                        }
                                                        --n15;
                                                    } while (n15 > n12);
                                                }
                                                nArray[n13] = n14;
                                                n15 = 0;
                                                if (kVCQPLIW.c[n14] == 3) {
                                                    n15 = 128;
                                                }
                                                if ((n9 & 1) == 0) break block25;
                                                nArray2[n13] = mBMGIXGO4.j();
                                                if (!bl2) break block26;
                                            }
                                            nArray2[n13] = n15;
                                        }
                                        if ((n9 & 2) == 0) break block27;
                                        nArray3[n13] = mBMGIXGO4.j();
                                        if (!bl2) break block28;
                                    }
                                    nArray3[n13] = n15;
                                }
                                if ((n9 & 4) == 0) break block29;
                                nArray4[n13] = mBMGIXGO4.j();
                                if (!bl2) break block30;
                            }
                            nArray4[n13] = n15;
                        }
                        n12 = n14;
                        ++n13;
                        if (kVCQPLIW.c[n14] == 5) {
                            SQHJOGRT.j[n10] = false;
                        }
                    }
                    ++n14;
                } while (n14 < n11);
                sQHJOGRT.e = n13;
                sQHJOGRT.f = new int[n13];
                sQHJOGRT.g = new int[n13];
                sQHJOGRT.h = new int[n13];
                sQHJOGRT.i = new int[n13];
                n9 = 0;
                boolean bl7 = true;
                do {
                    if (bl7 && !(bl7 = false) && !bl2) continue;
                    sQHJOGRT.f[n9] = nArray[n9];
                    sQHJOGRT.g[n9] = nArray2[n9];
                    sQHJOGRT.h[n9] = nArray3[n9];
                    sQHJOGRT.i[n9] = nArray4[n9];
                    ++n9;
                } while (n9 < n13);
                ++n8;
            } while (n8 < n7);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("72235, " + byArray + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void b(int n) {
        try {
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !XHHRODPC.l) continue;
                a = 90;
            } while (n >= 0);
            b = null;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("26556, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static SQHJOGRT a(int n, int n2) {
        try {
            if (n != 9) {
                throw new NullPointerException();
            }
            if (b == null) {
                return null;
            }
            return b[n2];
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("96908, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static boolean a(int n, boolean bl) {
        try {
            if (bl) {
                a = -79;
            }
            return n == -1;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("73746, " + n + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

