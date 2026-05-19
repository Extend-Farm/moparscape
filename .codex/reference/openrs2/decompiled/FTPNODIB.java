/*
 * Decompiled with CFR 0.152.
 */
import sign.signlink;

public class FTPNODIB {
    private boolean a;
    private int b;
    private boolean c;
    private int d;
    private boolean e;
    private boolean f;
    private byte g;
    private byte h;
    public int i;
    public int j;
    public int k;
    public int l;
    public int[][] m;

    public FTPNODIB(int n, int n2, boolean bl) {
        int n3 = LKGEGIEW.t;
        this.a = true;
        this.b = -32357;
        this.c = false;
        this.d = 7;
        this.e = true;
        this.f = true;
        this.g = (byte)2;
        this.h = (byte)-101;
        try {
            this.i = 0;
            this.j = 0;
            this.k = n;
            this.l = n2;
            this.m = new int[this.k][this.l];
            this.a();
            if (!bl) {
                this.b = -496;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("28792, " + n + ", " + n2 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
        if (n3 != 0) {
            PKVMXVTO.e = !PKVMXVTO.e;
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public void a() {
        int n = LKGEGIEW.t;
        int n2 = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && n == 0) continue;
            int n3 = 0;
            boolean bl2 = true;
            do {
                block5: {
                    block4: {
                        if (bl2 && !(bl2 = false) && n == 0) continue;
                        if (n2 != 0 && n3 != 0 && n2 != this.k - 1 && n3 != this.l - 1) break block4;
                        this.m[n2][n3] = 0xFFFFFF;
                        if (n == 0) break block5;
                    }
                    this.m[n2][n3] = 0x1000000;
                }
                ++n3;
            } while (n3 < this.l);
            ++n2;
        } while (n2 < this.k);
    }

    public void a(int n, int n2, int n3, int n4, byte by, boolean bl) {
        try {
            block40: {
                block39: {
                    n3 -= this.i;
                    n -= this.j;
                    if (by != 1) break block39;
                    by = 0;
                    if (LKGEGIEW.t == 0) break block40;
                }
                return;
            }
            if (n4 == 0) {
                if (n2 == 0) {
                    this.b(n3, n, 128);
                    this.b(n3 - 1, n, 8);
                }
                if (n2 == 1) {
                    this.b(n3, n, 2);
                    this.b(n3, n + 1, 32);
                }
                if (n2 == 2) {
                    this.b(n3, n, 8);
                    this.b(n3 + 1, n, 128);
                }
                if (n2 == 3) {
                    this.b(n3, n, 32);
                    this.b(n3, n - 1, 2);
                }
            }
            if (n4 == 1 || n4 == 3) {
                if (n2 == 0) {
                    this.b(n3, n, 1);
                    this.b(n3 - 1, n + 1, 16);
                }
                if (n2 == 1) {
                    this.b(n3, n, 4);
                    this.b(n3 + 1, n + 1, 64);
                }
                if (n2 == 2) {
                    this.b(n3, n, 16);
                    this.b(n3 + 1, n - 1, 1);
                }
                if (n2 == 3) {
                    this.b(n3, n, 64);
                    this.b(n3 - 1, n - 1, 4);
                }
            }
            if (n4 == 2) {
                if (n2 == 0) {
                    this.b(n3, n, 130);
                    this.b(n3 - 1, n, 8);
                    this.b(n3, n + 1, 32);
                }
                if (n2 == 1) {
                    this.b(n3, n, 10);
                    this.b(n3, n + 1, 32);
                    this.b(n3 + 1, n, 128);
                }
                if (n2 == 2) {
                    this.b(n3, n, 40);
                    this.b(n3 + 1, n, 128);
                    this.b(n3, n - 1, 2);
                }
                if (n2 == 3) {
                    this.b(n3, n, 160);
                    this.b(n3, n - 1, 2);
                    this.b(n3 - 1, n, 8);
                }
            }
            if (bl) {
                if (n4 == 0) {
                    if (n2 == 0) {
                        this.b(n3, n, 65536);
                        this.b(n3 - 1, n, 4096);
                    }
                    if (n2 == 1) {
                        this.b(n3, n, 1024);
                        this.b(n3, n + 1, 16384);
                    }
                    if (n2 == 2) {
                        this.b(n3, n, 4096);
                        this.b(n3 + 1, n, 65536);
                    }
                    if (n2 == 3) {
                        this.b(n3, n, 16384);
                        this.b(n3, n - 1, 1024);
                    }
                }
                if (n4 == 1 || n4 == 3) {
                    if (n2 == 0) {
                        this.b(n3, n, 512);
                        this.b(n3 - 1, n + 1, 8192);
                    }
                    if (n2 == 1) {
                        this.b(n3, n, 2048);
                        this.b(n3 + 1, n + 1, 32768);
                    }
                    if (n2 == 2) {
                        this.b(n3, n, 8192);
                        this.b(n3 + 1, n - 1, 512);
                    }
                    if (n2 == 3) {
                        this.b(n3, n, 32768);
                        this.b(n3 - 1, n - 1, 2048);
                    }
                }
                if (n4 == 2) {
                    if (n2 == 0) {
                        this.b(n3, n, 66560);
                        this.b(n3 - 1, n, 4096);
                        this.b(n3, n + 1, 16384);
                    }
                    if (n2 == 1) {
                        this.b(n3, n, 5120);
                        this.b(n3, n + 1, 16384);
                        this.b(n3 + 1, n, 65536);
                    }
                    if (n2 == 2) {
                        this.b(n3, n, 20480);
                        this.b(n3 + 1, n, 65536);
                        this.b(n3, n - 1, 1024);
                    }
                    if (n2 == 3) {
                        this.b(n3, n, 81920);
                        this.b(n3, n - 1, 1024);
                        this.b(n3 - 1, n, 4096);
                        return;
                    }
                }
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("53275, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + by + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(boolean bl, int n, int n2, int n3, int n4, int n5, int n6) {
        int n7 = LKGEGIEW.t;
        try {
            int n8;
            int n9 = 256;
            if (bl) {
                n9 += 131072;
            }
            n4 -= this.i;
            n = 14 / n;
            n5 -= this.j;
            if (n6 == 1 || n6 == 3) {
                n8 = n2;
                n2 = n3;
                n3 = n8;
            }
            n8 = n4;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n7 == 0) continue;
                if (n8 >= 0 && n8 < this.k) {
                    int n10 = n5;
                    boolean bl3 = true;
                    do {
                        if (bl3 && !(bl3 = false) && n7 == 0) continue;
                        if (n10 >= 0 && n10 < this.l) {
                            this.b(n8, n10, n9);
                        }
                        ++n10;
                    } while (n10 < n5 + n3);
                }
                ++n8;
            } while (n8 < n4 + n2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("32403, " + bl + ", " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, int n2, int n3) {
        try {
            n3 -= this.i;
            if (n2 != 0) {
                this.a = !this.a;
            }
            int[] nArray = this.m[n3];
            int n4 = n -= this.j;
            nArray[n4] = nArray[n4] | 0x200000;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("33794, " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private void b(int n, int n2, int n3) {
        int[] nArray = this.m[n];
        int n4 = n2;
        nArray[n4] = nArray[n4] | n3;
    }

    public void a(int n, int n2, boolean bl, boolean bl2, int n3, int n4) {
        try {
            n3 -= this.i;
            n4 -= this.j;
            if (!bl2) {
                return;
            }
            if (n2 == 0) {
                if (n == 0) {
                    this.a(128, n3, n4, 933);
                    this.a(8, n3 - 1, n4, 933);
                }
                if (n == 1) {
                    this.a(2, n3, n4, 933);
                    this.a(32, n3, n4 + 1, 933);
                }
                if (n == 2) {
                    this.a(8, n3, n4, 933);
                    this.a(128, n3 + 1, n4, 933);
                }
                if (n == 3) {
                    this.a(32, n3, n4, 933);
                    this.a(2, n3, n4 - 1, 933);
                }
            }
            if (n2 == 1 || n2 == 3) {
                if (n == 0) {
                    this.a(1, n3, n4, 933);
                    this.a(16, n3 - 1, n4 + 1, 933);
                }
                if (n == 1) {
                    this.a(4, n3, n4, 933);
                    this.a(64, n3 + 1, n4 + 1, 933);
                }
                if (n == 2) {
                    this.a(16, n3, n4, 933);
                    this.a(1, n3 + 1, n4 - 1, 933);
                }
                if (n == 3) {
                    this.a(64, n3, n4, 933);
                    this.a(4, n3 - 1, n4 - 1, 933);
                }
            }
            if (n2 == 2) {
                if (n == 0) {
                    this.a(130, n3, n4, 933);
                    this.a(8, n3 - 1, n4, 933);
                    this.a(32, n3, n4 + 1, 933);
                }
                if (n == 1) {
                    this.a(10, n3, n4, 933);
                    this.a(32, n3, n4 + 1, 933);
                    this.a(128, n3 + 1, n4, 933);
                }
                if (n == 2) {
                    this.a(40, n3, n4, 933);
                    this.a(128, n3 + 1, n4, 933);
                    this.a(2, n3, n4 - 1, 933);
                }
                if (n == 3) {
                    this.a(160, n3, n4, 933);
                    this.a(2, n3, n4 - 1, 933);
                    this.a(8, n3 - 1, n4, 933);
                }
            }
            if (bl) {
                if (n2 == 0) {
                    if (n == 0) {
                        this.a(65536, n3, n4, 933);
                        this.a(4096, n3 - 1, n4, 933);
                    }
                    if (n == 1) {
                        this.a(1024, n3, n4, 933);
                        this.a(16384, n3, n4 + 1, 933);
                    }
                    if (n == 2) {
                        this.a(4096, n3, n4, 933);
                        this.a(65536, n3 + 1, n4, 933);
                    }
                    if (n == 3) {
                        this.a(16384, n3, n4, 933);
                        this.a(1024, n3, n4 - 1, 933);
                    }
                }
                if (n2 == 1 || n2 == 3) {
                    if (n == 0) {
                        this.a(512, n3, n4, 933);
                        this.a(8192, n3 - 1, n4 + 1, 933);
                    }
                    if (n == 1) {
                        this.a(2048, n3, n4, 933);
                        this.a(32768, n3 + 1, n4 + 1, 933);
                    }
                    if (n == 2) {
                        this.a(8192, n3, n4, 933);
                        this.a(512, n3 + 1, n4 - 1, 933);
                    }
                    if (n == 3) {
                        this.a(32768, n3, n4, 933);
                        this.a(2048, n3 - 1, n4 - 1, 933);
                    }
                }
                if (n2 == 2) {
                    if (n == 0) {
                        this.a(66560, n3, n4, 933);
                        this.a(4096, n3 - 1, n4, 933);
                        this.a(16384, n3, n4 + 1, 933);
                    }
                    if (n == 1) {
                        this.a(5120, n3, n4, 933);
                        this.a(16384, n3, n4 + 1, 933);
                        this.a(65536, n3 + 1, n4, 933);
                    }
                    if (n == 2) {
                        this.a(20480, n3, n4, 933);
                        this.a(65536, n3 + 1, n4, 933);
                        this.a(1024, n3, n4 - 1, 933);
                    }
                    if (n == 3) {
                        this.a(81920, n3, n4, 933);
                        this.a(1024, n3, n4 - 1, 933);
                        this.a(4096, n3 - 1, n4, 933);
                        return;
                    }
                }
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("6952, " + n + ", " + n2 + ", " + bl + ", " + bl2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, int n2, int n3, int n4, byte by, int n5, boolean bl) {
        int n6 = LKGEGIEW.t;
        try {
            int n7;
            int n8 = 256;
            if (bl) {
                n8 += 131072;
            }
            n3 -= this.i;
            n4 -= this.j;
            if (by != 6) {
                boolean bl2 = this.a = !this.a;
            }
            if (n == 1 || n == 3) {
                n7 = n2;
                n2 = n5;
                n5 = n7;
            }
            n7 = n3;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n6 == 0) continue;
                if (n7 >= 0 && n7 < this.k) {
                    int n9 = n4;
                    boolean bl4 = true;
                    do {
                        if (bl4 && !(bl4 = false) && n6 == 0) continue;
                        if (n9 >= 0 && n9 < this.l) {
                            this.a(n8, n7, n9, 933);
                        }
                        ++n9;
                    } while (n9 < n4 + n5);
                }
                ++n7;
            } while (n7 < n3 + n2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("45433, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + by + ", " + n5 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private void a(int n, int n2, int n3, int n4) {
        try {
            n4 = 36 / n4;
            int[] nArray = this.m[n2];
            int n5 = n3;
            nArray[n5] = nArray[n5] & 0xFFFFFF - n;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("42695, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void c(int n, int n2, int n3) {
        try {
            n = 6 / n;
            int[] nArray = this.m[n3 -= this.i];
            int n4 = n2 -= this.j;
            nArray[n4] = nArray[n4] & 0xDFFFFF;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("36463, " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public boolean a(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        try {
            if (n4 != 0) {
                boolean bl = this.a = !this.a;
            }
            if (n2 == n && n3 == n7) {
                return true;
            }
            n2 -= this.i;
            n3 -= this.j;
            n -= this.i;
            n7 -= this.j;
            if (n6 == 0) {
                if (n5 == 0) {
                    if (n2 == n - 1 && n3 == n7) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 + 1 && (this.m[n2][n3] & 0x1280120) == 0) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 - 1 && (this.m[n2][n3] & 0x1280102) == 0) {
                        return true;
                    }
                } else if (n5 == 1) {
                    if (n2 == n && n3 == n7 + 1) {
                        return true;
                    }
                    if (n2 == n - 1 && n3 == n7 && (this.m[n2][n3] & 0x1280108) == 0) {
                        return true;
                    }
                    if (n2 == n + 1 && n3 == n7 && (this.m[n2][n3] & 0x1280180) == 0) {
                        return true;
                    }
                } else if (n5 == 2) {
                    if (n2 == n + 1 && n3 == n7) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 + 1 && (this.m[n2][n3] & 0x1280120) == 0) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 - 1 && (this.m[n2][n3] & 0x1280102) == 0) {
                        return true;
                    }
                } else if (n5 == 3) {
                    if (n2 == n && n3 == n7 - 1) {
                        return true;
                    }
                    if (n2 == n - 1 && n3 == n7 && (this.m[n2][n3] & 0x1280108) == 0) {
                        return true;
                    }
                    if (n2 == n + 1 && n3 == n7 && (this.m[n2][n3] & 0x1280180) == 0) {
                        return true;
                    }
                }
            }
            if (n6 == 2) {
                if (n5 == 0) {
                    if (n2 == n - 1 && n3 == n7) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 + 1) {
                        return true;
                    }
                    if (n2 == n + 1 && n3 == n7 && (this.m[n2][n3] & 0x1280180) == 0) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 - 1 && (this.m[n2][n3] & 0x1280102) == 0) {
                        return true;
                    }
                } else if (n5 == 1) {
                    if (n2 == n - 1 && n3 == n7 && (this.m[n2][n3] & 0x1280108) == 0) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 + 1) {
                        return true;
                    }
                    if (n2 == n + 1 && n3 == n7) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 - 1 && (this.m[n2][n3] & 0x1280102) == 0) {
                        return true;
                    }
                } else if (n5 == 2) {
                    if (n2 == n - 1 && n3 == n7 && (this.m[n2][n3] & 0x1280108) == 0) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 + 1 && (this.m[n2][n3] & 0x1280120) == 0) {
                        return true;
                    }
                    if (n2 == n + 1 && n3 == n7) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 - 1) {
                        return true;
                    }
                } else if (n5 == 3) {
                    if (n2 == n - 1 && n3 == n7) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 + 1 && (this.m[n2][n3] & 0x1280120) == 0) {
                        return true;
                    }
                    if (n2 == n + 1 && n3 == n7 && (this.m[n2][n3] & 0x1280180) == 0) {
                        return true;
                    }
                    if (n2 == n && n3 == n7 - 1) {
                        return true;
                    }
                }
            }
            if (n6 == 9) {
                if (n2 == n && n3 == n7 + 1 && (this.m[n2][n3] & 0x20) == 0) {
                    return true;
                }
                if (n2 == n && n3 == n7 - 1 && (this.m[n2][n3] & 2) == 0) {
                    return true;
                }
                if (n2 == n - 1 && n3 == n7 && (this.m[n2][n3] & 8) == 0) {
                    return true;
                }
                if (n2 == n + 1 && n3 == n7 && (this.m[n2][n3] & 0x80) == 0) {
                    return true;
                }
            }
            return false;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("46464, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public boolean b(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        try {
            if (n7 != 0) {
                throw new NullPointerException();
            }
            if (n6 == n && n3 == n2) {
                return true;
            }
            n6 -= this.i;
            n3 -= this.j;
            n -= this.i;
            n2 -= this.j;
            if (n4 == 6 || n4 == 7) {
                if (n4 == 7) {
                    n5 = n5 + 2 & 3;
                }
                if (n5 == 0) {
                    if (n6 == n + 1 && n3 == n2 && (this.m[n6][n3] & 0x80) == 0) {
                        return true;
                    }
                    if (n6 == n && n3 == n2 - 1 && (this.m[n6][n3] & 2) == 0) {
                        return true;
                    }
                } else if (n5 == 1) {
                    if (n6 == n - 1 && n3 == n2 && (this.m[n6][n3] & 8) == 0) {
                        return true;
                    }
                    if (n6 == n && n3 == n2 - 1 && (this.m[n6][n3] & 2) == 0) {
                        return true;
                    }
                } else if (n5 == 2) {
                    if (n6 == n - 1 && n3 == n2 && (this.m[n6][n3] & 8) == 0) {
                        return true;
                    }
                    if (n6 == n && n3 == n2 + 1 && (this.m[n6][n3] & 0x20) == 0) {
                        return true;
                    }
                } else if (n5 == 3) {
                    if (n6 == n + 1 && n3 == n2 && (this.m[n6][n3] & 0x80) == 0) {
                        return true;
                    }
                    if (n6 == n && n3 == n2 + 1 && (this.m[n6][n3] & 0x20) == 0) {
                        return true;
                    }
                }
            }
            if (n4 == 8) {
                if (n6 == n && n3 == n2 + 1 && (this.m[n6][n3] & 0x20) == 0) {
                    return true;
                }
                if (n6 == n && n3 == n2 - 1 && (this.m[n6][n3] & 2) == 0) {
                    return true;
                }
                if (n6 == n - 1 && n3 == n2 && (this.m[n6][n3] & 8) == 0) {
                    return true;
                }
                if (n6 == n + 1 && n3 == n2 && (this.m[n6][n3] & 0x80) == 0) {
                    return true;
                }
            }
            return false;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("67003, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public boolean a(byte by, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        try {
            if (by != 1) {
                throw new NullPointerException();
            }
            int n8 = n2 + n6 - 1;
            int n9 = n + n4 - 1;
            if (n3 >= n2 && n3 <= n8 && n7 >= n && n7 <= n9) {
                return true;
            }
            if (n3 == n2 - 1 && n7 >= n && n7 <= n9 && (this.m[n3 - this.i][n7 - this.j] & 8) == 0 && (n5 & 8) == 0) {
                return true;
            }
            if (n3 == n8 + 1 && n7 >= n && n7 <= n9 && (this.m[n3 - this.i][n7 - this.j] & 0x80) == 0 && (n5 & 2) == 0) {
                return true;
            }
            if (n7 == n - 1 && n3 >= n2 && n3 <= n8 && (this.m[n3 - this.i][n7 - this.j] & 2) == 0 && (n5 & 4) == 0) {
                return true;
            }
            return n7 == n9 + 1 && n3 >= n2 && n3 <= n8 && (this.m[n3 - this.i][n7 - this.j] & 0x20) == 0 && (n5 & 1) == 0;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("42625, " + by + ", " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

