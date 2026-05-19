/*
 * Decompiled with CFR 0.152.
 */
import java.io.IOException;
import java.io.RandomAccessFile;
import sign.signlink;

public final class IGSLDTHC {
    private int a = 923;
    private boolean b = true;
    static byte[] c = new byte[520];
    RandomAccessFile d;
    RandomAccessFile e;
    int f;
    int g = 65000;

    public IGSLDTHC(int n, RandomAccessFile randomAccessFile, RandomAccessFile randomAccessFile2, int n2, boolean bl) {
        try {
            this.f = n2;
            if (!bl) {
                throw new NullPointerException();
            }
            this.d = randomAccessFile;
            this.e = randomAccessFile2;
            this.g = n;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("33536, " + n + ", " + randomAccessFile + ", " + randomAccessFile2 + ", " + n2 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public synchronized byte[] a(boolean bl, int n) {
        boolean bl2 = MBMGIXGO.L;
        try {
            if (!bl) {
                throw new NullPointerException();
            }
            try {
                int n2;
                this.a(this.e, -660, n * 6);
                int n3 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && !bl2) continue;
                    n2 = this.e.read(c, n3, 6 - n3);
                    if (n2 == -1) {
                        return null;
                    }
                    n3 += n2;
                } while (n3 < 6);
                n2 = ((c[0] & 0xFF) << 16) + ((c[1] & 0xFF) << 8) + (c[2] & 0xFF);
                int n4 = ((c[3] & 0xFF) << 16) + ((c[4] & 0xFF) << 8) + (c[5] & 0xFF);
                if (n2 < 0 || n2 > this.g) {
                    return null;
                }
                if (n4 <= 0 || (long)n4 > this.d.length() / 520L) {
                    return null;
                }
                byte[] byArray = new byte[n2];
                int n5 = 0;
                int n6 = 0;
                boolean bl4 = true;
                do {
                    int n7;
                    if (bl4 && !(bl4 = false) && !bl2) continue;
                    if (n4 == 0) {
                        return null;
                    }
                    this.a(this.d, -660, n4 * 520);
                    n3 = 0;
                    int n8 = n2 - n5;
                    boolean bl5 = true;
                    do {
                        if (bl5 && !(bl5 = false)) {
                            if (n8 <= 512) continue;
                            n8 = 512;
                            if (!bl2) continue;
                        }
                        if ((n7 = this.d.read(c, n3, n8 + 8 - n3)) == -1) {
                            return null;
                        }
                        n3 += n7;
                    } while (n3 < n8 + 8);
                    n7 = ((c[0] & 0xFF) << 8) + (c[1] & 0xFF);
                    int n9 = ((c[2] & 0xFF) << 8) + (c[3] & 0xFF);
                    int n10 = ((c[4] & 0xFF) << 16) + ((c[5] & 0xFF) << 8) + (c[6] & 0xFF);
                    int n11 = c[7] & 0xFF;
                    if (n7 != n || n9 != n6 || n11 != this.f) {
                        return null;
                    }
                    if (n10 < 0 || (long)n10 > this.d.length() / 520L) {
                        return null;
                    }
                    int n12 = 0;
                    boolean bl6 = true;
                    do {
                        if (bl6 && !(bl6 = false) && !bl2) continue;
                        byArray[n5++] = c[n12 + 8];
                        ++n12;
                    } while (n12 < n8);
                    n4 = n10;
                    ++n6;
                } while (n5 < n2);
                return byArray;
            }
            catch (IOException iOException) {
                return null;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("78178, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public synchronized boolean a(int n, byte[] byArray, byte by, int n2) {
        try {
            boolean bl;
            block7: {
                block6: {
                    bl = this.a(true, 923, n2, n, byArray);
                    if (by != 2) break block6;
                    by = 0;
                    if (!MBMGIXGO.L) break block7;
                }
                boolean bl2 = this.b = !this.b;
            }
            if (!bl) {
                bl = this.a(false, 923, n2, n, byArray);
            }
            return bl;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("94130, " + n + ", " + byArray + ", " + by + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    private synchronized boolean a(boolean var1_1, int var2_2, int var3_3, int var4_4, byte[] var5_5) {
        var14_6 = MBMGIXGO.L;
        try {
            var2_2 = 27 / var2_2;
            try {
                block19: {
                    block18: {
                        if (!var1_1) break block18;
                        this.a(this.e, -660, var3_3 * 6);
                        var7_7 = 0;
                        if (!var14_6) ** GOTO lbl13
                        do {
                            if ((var8_8 = this.e.read(IGSLDTHC.c, var7_7, 6 - var7_7)) == -1) {
                                return false;
                            }
                            var7_7 += var8_8;
lbl13:
                            // 2 sources

                        } while (var7_7 < 6);
                        var6_9 = ((IGSLDTHC.c[3] & 255) << 16) + ((IGSLDTHC.c[4] & 255) << 8) + (IGSLDTHC.c[5] & 255);
                        if (var6_9 <= 0 || (long)var6_9 > this.d.length() / 520L) {
                            return false;
                        }
                        break block19;
                    }
                    var6_9 = (int)((this.d.length() + 519L) / 520L);
                    if (var6_9 == 0) {
                        var6_9 = 1;
                    }
                }
                IGSLDTHC.c[0] = (byte)(var4_4 >> 16);
                IGSLDTHC.c[1] = (byte)(var4_4 >> 8);
                IGSLDTHC.c[2] = (byte)var4_4;
                IGSLDTHC.c[3] = (byte)(var6_9 >> 16);
                IGSLDTHC.c[4] = (byte)(var6_9 >> 8);
                IGSLDTHC.c[5] = (byte)var6_9;
                this.a(this.e, -660, var3_3 * 6);
                this.e.write(IGSLDTHC.c, 0, 6);
                var7_7 = 0;
                var8_8 = 0;
                if (!var14_6) ** GOTO lbl79
                do {
                    block20: {
                        var9_11 = 0;
                        if (!var1_1) break block20;
                        this.a(this.d, -660, var6_9 * 520);
                        var10_12 = 0;
                        if (!var14_6) ** GOTO lbl42
                        while ((var11_13 = this.d.read(IGSLDTHC.c, var10_12, 8 - var10_12)) != -1) {
                            var10_12 += var11_13;
lbl42:
                            // 2 sources

                            if (var10_12 < 8) continue;
                        }
                        if (var10_12 == 8) {
                            var11_13 = ((IGSLDTHC.c[0] & 255) << 8) + (IGSLDTHC.c[1] & 255);
                            var12_14 = ((IGSLDTHC.c[2] & 255) << 8) + (IGSLDTHC.c[3] & 255);
                            var9_11 = ((IGSLDTHC.c[4] & 255) << 16) + ((IGSLDTHC.c[5] & 255) << 8) + (IGSLDTHC.c[6] & 255);
                            var13_15 = IGSLDTHC.c[7] & 255;
                            if (var11_13 != var3_3 || var12_14 != var8_8 || var13_15 != this.f) {
                                return false;
                            }
                            if (var9_11 < 0 || (long)var9_11 > this.d.length() / 520L) {
                                return false;
                            }
                        }
                    }
                    if (var9_11 == 0) {
                        var1_1 = false;
                        var9_11 = (int)((this.d.length() + 519L) / 520L);
                        if (var9_11 == 0) {
                            ++var9_11;
                        }
                        if (var9_11 == var6_9) {
                            ++var9_11;
                        }
                    }
                    if (var4_4 - var7_7 <= 512) {
                        var9_11 = 0;
                    }
                    IGSLDTHC.c[0] = (byte)(var3_3 >> 8);
                    IGSLDTHC.c[1] = (byte)var3_3;
                    IGSLDTHC.c[2] = (byte)(var8_8 >> 8);
                    IGSLDTHC.c[3] = (byte)var8_8;
                    IGSLDTHC.c[4] = (byte)(var9_11 >> 16);
                    IGSLDTHC.c[5] = (byte)(var9_11 >> 8);
                    IGSLDTHC.c[6] = (byte)var9_11;
                    IGSLDTHC.c[7] = (byte)this.f;
                    this.a(this.d, -660, var6_9 * 520);
                    this.d.write(IGSLDTHC.c, 0, 8);
                    var10_12 = var4_4 - var7_7;
                    if (var10_12 > 512) {
                        var10_12 = 512;
                    }
                    this.d.write(var5_5, var7_7, var10_12);
                    var7_7 += var10_12;
                    var6_9 = var9_11;
                    ++var8_8;
lbl79:
                    // 2 sources

                } while (var7_7 < var4_4);
                return true;
            }
            catch (IOException v0) {
                return false;
            }
        }
        catch (RuntimeException var6_10) {
            signlink.reporterror("3304, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_4 + ", " + var5_5 + ", " + var6_10.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public synchronized void a(RandomAccessFile randomAccessFile, int n, int n2) throws IOException {
        try {
            if (MBMGIXGO.L || n >= 0) {
                return;
            }
            if (n2 < 0 || n2 > 0x3C00000) {
                System.out.println("Badseek - pos:" + n2 + " len:" + randomAccessFile.length());
                n2 = 0x3C00000;
                try {
                    Thread.sleep(1000L);
                }
                catch (Exception exception) {}
            }
            randomAccessFile.seek(n2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("97845, " + randomAccessFile + ", " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

