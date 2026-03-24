// Reconstructed from Class7.class in moparclient.jar (CFR 0.152); adjusted for javac (casts, buffer types).
final class Class7 {
    private static int anInt123 = (int)(Math.random() * 17.0) - 8;
    private int[] anIntArray124;
    private int[] anIntArray125;
    private int[] anIntArray126;
    private int[] anIntArray127;
    private int[] anIntArray128;
    private int[][][] anIntArrayArrayArray129;
    private byte[][][] aByteArrayArrayArray130;
    static int anInt131;
    private boolean aBoolean132 = true;
    private static int anInt133;
    private byte[][][] aByteArrayArrayArray134;
    private int[][][] anIntArrayArrayArray135;
    private byte[][][] aByteArrayArrayArray136;
    private static final int[] anIntArray137;
    private static int anInt138;
    private int[][] anIntArrayArray139;
    private static final int[] anIntArray140;
    private static boolean aBoolean141;
    private byte[][][] aByteArrayArrayArray142;
    private boolean aBoolean143 = false;
    private static final int[] anIntArray144;
    static int anInt145;
    private int anInt146;
    private int anInt147;
    private byte[][][] aByteArrayArrayArray148;
    private byte[][][] aByteArrayArrayArray149;
    private int anInt150 = -53;
    static boolean aBoolean151;
    private static final int[] anIntArray152;
    private static int anInt153;

    public Class7(byte[][][] byArray, int n, int n2, int n3, int[][][] nArray) {
        anInt145 = 99;
        this.anInt146 = n3;
        this.anInt147 = n2;
        while (n >= 0) {
            anInt153 = -320;
        }
        this.anIntArrayArrayArray129 = nArray;
        this.aByteArrayArrayArray149 = byArray;
        this.aByteArrayArrayArray142 = new byte[4][this.anInt146][this.anInt147];
        this.aByteArrayArrayArray130 = new byte[4][this.anInt146][this.anInt147];
        this.aByteArrayArrayArray136 = new byte[4][this.anInt146][this.anInt147];
        this.aByteArrayArrayArray148 = new byte[4][this.anInt146][this.anInt147];
        this.anIntArrayArrayArray135 = new int[4][this.anInt146 + 1][this.anInt147 + 1];
        this.aByteArrayArrayArray134 = new byte[4][this.anInt146 + 1][this.anInt147 + 1];
        this.anIntArrayArray139 = new int[this.anInt146 + 1][this.anInt147 + 1];
        this.anIntArray124 = new int[this.anInt147];
        this.anIntArray125 = new int[this.anInt147];
        this.anIntArray126 = new int[this.anInt147];
        this.anIntArray127 = new int[this.anInt147];
        this.anIntArray128 = new int[this.anInt147];
    }

    private static final int method170(int n, int n2) {
        int n3 = n + n2 * 57;
        n3 = n3 << 13 ^ n3;
        int n4 = n3 * (n3 * n3 * 15731 + 789221) + 1376312589 & Integer.MAX_VALUE;
        return n4 >> 19 & 0xFF;
    }

    public final void method171(Class11[] class11Array, Class25 class25, int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        int n16;
        int n17;
        int n18;
        int n19;
        int n20 = 0;
        while (n20 < 4) {
            n19 = 0;
            while (n19 < 104) {
                int n21 = 0;
                while (n21 < 104) {
                    if ((this.aByteArrayArrayArray149[n20][n19][n21] & 1) == 1) {
                        n18 = n20;
                        if ((this.aByteArrayArrayArray149[1][n19][n21] & 2) == 2) {
                            --n18;
                        }
                        if (n18 >= 0) {
                            class11Array[n18].method213(n21, 0, n19);
                        }
                    }
                    ++n21;
                }
                ++n19;
            }
            ++n20;
        }
        if (n < 2 || n > 2) {
            anInt138 = 329;
        }
        if ((anInt123 += (int)(Math.random() * 5.0) - 2) < -8) {
            anInt123 = -8;
        }
        if (anInt123 > 8) {
            anInt123 = 8;
        }
        if ((anInt133 += (int)(Math.random() * 5.0) - 2) < -16) {
            anInt133 = -16;
        }
        if (anInt133 > 16) {
            anInt133 = 16;
        }
        n19 = 0;
        while (n19 < 4) {
            byte[][] byArray = this.aByteArrayArrayArray134[n19];
            n18 = 96;
            n17 = 768;
            n16 = -50;
            n15 = -10;
            n14 = -50;
            n13 = (int)Math.sqrt(n16 * n16 + n15 * n15 + n14 * n14);
            n12 = n17 * n13 >> 8;
            n11 = 1;
            while (n11 < this.anInt147 - 1) {
                n10 = 1;
                while (n10 < this.anInt146 - 1) {
                    n9 = this.anIntArrayArrayArray129[n19][n10 + 1][n11] - this.anIntArrayArrayArray129[n19][n10 - 1][n11];
                    n8 = this.anIntArrayArrayArray129[n19][n10][n11 + 1] - this.anIntArrayArrayArray129[n19][n10][n11 - 1];
                    n7 = (int)Math.sqrt(n9 * n9 + 65536 + n8 * n8);
                    n6 = (n9 << 8) / n7;
                    n5 = 65536 / n7;
                    n4 = (n8 << 8) / n7;
                    n3 = n18 + (n16 * n6 + n15 * n5 + n14 * n4) / n12;
                    n2 = (byArray[n10 - 1][n11] >> 2) + (byArray[n10 + 1][n11] >> 3) + (byArray[n10][n11 - 1] >> 2) + (byArray[n10][n11 + 1] >> 3) + (byArray[n10][n11] >> 1);
                    this.anIntArrayArray139[n10][n11] = n3 - n2;
                    ++n10;
                }
                ++n11;
            }
            n10 = 0;
            while (n10 < this.anInt147) {
                this.anIntArray124[n10] = 0;
                this.anIntArray125[n10] = 0;
                this.anIntArray126[n10] = 0;
                this.anIntArray127[n10] = 0;
                this.anIntArray128[n10] = 0;
                ++n10;
            }
            n9 = -5;
            while (n9 < this.anInt146 + 5) {
                n8 = 0;
                while (n8 < this.anInt147) {
                    int n22;
                    n7 = n9 + 5;
                    if (n7 >= 0 && n7 < this.anInt146 && (n6 = this.aByteArrayArrayArray142[n19][n7][n8] & 0xFF) > 0) {
                        Class22 class22 = Class22.aClass22Array388[n6 - 1];
                        int n23 = n8;
                        this.anIntArray124[n23] = this.anIntArray124[n23] + class22.anInt397;
                        int n24 = n8;
                        this.anIntArray125[n24] = this.anIntArray125[n24] + class22.anInt395;
                        int n25 = n8;
                        this.anIntArray126[n25] = this.anIntArray126[n25] + class22.anInt396;
                        int n26 = n8;
                        this.anIntArray127[n26] = this.anIntArray127[n26] + class22.anInt398;
                        int n27 = n8;
                        this.anIntArray128[n27] = this.anIntArray128[n27] + 1;
                    }
                    if ((n6 = n9 - 5) >= 0 && n6 < this.anInt146 && (n22 = this.aByteArrayArrayArray142[n19][n6][n8] & 0xFF) > 0) {
                        Class22 class22 = Class22.aClass22Array388[n22 - 1];
                        int n28 = n8;
                        this.anIntArray124[n28] = this.anIntArray124[n28] - class22.anInt397;
                        int n29 = n8;
                        this.anIntArray125[n29] = this.anIntArray125[n29] - class22.anInt395;
                        int n30 = n8;
                        this.anIntArray126[n30] = this.anIntArray126[n30] - class22.anInt396;
                        int n31 = n8;
                        this.anIntArray127[n31] = this.anIntArray127[n31] - class22.anInt398;
                        int n32 = n8;
                        this.anIntArray128[n32] = this.anIntArray128[n32] - 1;
                    }
                    ++n8;
                }
                if (n9 >= 1 && n9 < this.anInt146 - 1) {
                    n7 = 0;
                    n6 = 0;
                    int n33 = 0;
                    int n34 = 0;
                    n3 = 0;
                    n2 = -5;
                    while (n2 < this.anInt147 + 5) {
                        int n35;
                        int n36 = n2 + 5;
                        if (n36 >= 0 && n36 < this.anInt147) {
                            n7 += this.anIntArray124[n36];
                            n6 += this.anIntArray125[n36];
                            n33 += this.anIntArray126[n36];
                            n34 += this.anIntArray127[n36];
                            n3 += this.anIntArray128[n36];
                        }
                        if ((n35 = n2 - 5) >= 0 && n35 < this.anInt147) {
                            n7 -= this.anIntArray124[n35];
                            n6 -= this.anIntArray125[n35];
                            n33 -= this.anIntArray126[n35];
                            n34 -= this.anIntArray127[n35];
                            n3 -= this.anIntArray128[n35];
                        }
                        if (n2 >= 1 && n2 < this.anInt147 - 1 && (!aBoolean151 || (this.aByteArrayArrayArray149[0][n9][n2] & 2) != 0 || (this.aByteArrayArrayArray149[n19][n9][n2] & 0x10) == 0 && this.method182(n2, n19, n9, 0) == anInt131)) {
                            if (n19 < anInt145) {
                                anInt145 = n19;
                            }
                            int n37 = this.aByteArrayArrayArray142[n19][n9][n2] & 0xFF;
                            int n38 = this.aByteArrayArrayArray130[n19][n9][n2] & 0xFF;
                            if (n37 > 0 || n38 > 0) {
                                int n39;
                                int n40;
                                int n41;
                                int n42 = this.anIntArrayArrayArray129[n19][n9][n2];
                                int n43 = this.anIntArrayArrayArray129[n19][n9 + 1][n2];
                                int n44 = this.anIntArrayArrayArray129[n19][n9 + 1][n2 + 1];
                                int n45 = this.anIntArrayArrayArray129[n19][n9][n2 + 1];
                                int n46 = this.anIntArrayArray139[n9][n2];
                                int n47 = this.anIntArrayArray139[n9 + 1][n2];
                                int n48 = this.anIntArrayArray139[n9 + 1][n2 + 1];
                                int n49 = this.anIntArrayArray139[n9][n2 + 1];
                                int n50 = -1;
                                int n51 = -1;
                                if (n37 > 0) {
                                    n41 = n7 * 256 / n34;
                                    n40 = n6 / n3;
                                    n39 = n33 / n3;
                                    n50 = this.method177(n41, n40, n39);
                                    n41 = n41 + anInt123 & 0xFF;
                                    if ((n39 += anInt133) < 0) {
                                        n39 = 0;
                                    } else if (n39 > 255) {
                                        n39 = 255;
                                    }
                                    n51 = this.method177(n41, n40, n39);
                                }
                                if (n19 > 0) {
                                    n41 = 1;
                                    if (n37 == 0 && this.aByteArrayArrayArray136[n19][n9][n2] != 0) {
                                        n41 = 0;
                                    }
                                    if (n38 > 0 && !Class22.aClass22Array388[n38 - 1].aBoolean393) {
                                        n41 = 0;
                                    }
                                    if (n41 != 0 && n42 == n43 && n42 == n44 && n42 == n45) {
                                        int[] nArray = this.anIntArrayArrayArray135[n19][n9];
                                        int n52 = n2;
                                        nArray[n52] = nArray[n52] | 0x924;
                                    }
                                }
                                n41 = 0;
                                if (n50 != -1) {
                                    n41 = Class30_Sub2_Sub1_Sub3.anIntArray1482[Class7.method187(n51, 96)];
                                }
                                if (n38 == 0) {
                                    class25.method279(n19, n9, n2, 0, 0, -1, n42, n43, n44, n45, Class7.method187(n50, n46), Class7.method187(n50, n47), Class7.method187(n50, n48), Class7.method187(n50, n49), 0, 0, 0, 0, n41, 0);
                                } else {
                                    int n53;
                                    int n54;
                                    n40 = this.aByteArrayArrayArray136[n19][n9][n2] + 1;
                                    n39 = this.aByteArrayArrayArray148[n19][n9][n2];
                                    Class22 class22 = Class22.aClass22Array388[n38 - 1];
                                    int n55 = class22.anInt391;
                                    if (n55 >= 0) {
                                        n54 = Class30_Sub2_Sub1_Sub3.method369((int)n55, (int)12660);
                                        n53 = -1;
                                    } else if (class22.anInt390 == 0xFF00FF) {
                                        n54 = 0;
                                        n53 = -2;
                                        n55 = -1;
                                    } else {
                                        n53 = this.method177(class22.anInt394, class22.anInt395, class22.anInt396);
                                        n54 = Class30_Sub2_Sub1_Sub3.anIntArray1482[this.method185(class22.anInt399, 96)];
                                    }
                                    class25.method279(n19, n9, n2, n40, n39, n55, n42, n43, n44, n45, Class7.method187(n50, n46), Class7.method187(n50, n47), Class7.method187(n50, n48), Class7.method187(n50, n49), this.method185(n53, n46), this.method185(n53, n47), this.method185(n53, n48), this.method185(n53, n49), n41, n54);
                                }
                            }
                        }
                        ++n2;
                    }
                }
                ++n9;
            }
            n8 = 1;
            while (n8 < this.anInt147 - 1) {
                n7 = 1;
                while (n7 < this.anInt146 - 1) {
                    class25.method278(n19, n7, n8, this.method182(n8, n19, n7, 0));
                    ++n7;
                }
                ++n8;
            }
            ++n19;
        }
        class25.method305(-10, (byte)3, 64, -50, 768, -50);
        int n56 = 0;
        while (n56 < this.anInt146) {
            n18 = 0;
            while (n18 < this.anInt147) {
                if ((this.aByteArrayArrayArray149[1][n56][n18] & 2) == 2) {
                    class25.method276(n18, n56, -438);
                }
                ++n18;
            }
            ++n56;
        }
        n18 = 1;
        n17 = 2;
        n16 = 4;
        n15 = 0;
        while (n15 < 4) {
            if (n15 > 0) {
                n18 <<= 3;
                n17 <<= 3;
                n16 <<= 3;
            }
            n14 = 0;
            while (n14 <= n15) {
                n13 = 0;
                while (n13 <= this.anInt147) {
                    n12 = 0;
                    while (n12 <= this.anInt146) {
                        if ((this.anIntArrayArrayArray135[n14][n12][n13] & n18) != 0) {
                            n11 = n13;
                            n10 = n13;
                            n9 = n14;
                            n8 = n14;
                            while (n11 > 0 && (this.anIntArrayArrayArray135[n14][n12][n11 - 1] & n18) != 0) {
                                --n11;
                            }
                            while (n10 < this.anInt147 && (this.anIntArrayArrayArray135[n14][n12][n10 + 1] & n18) != 0) {
                                ++n10;
                            }
                            block20: while (n9 > 0) {
                                n7 = n11;
                                while (n7 <= n10) {
                                    if ((this.anIntArrayArrayArray135[n9 - 1][n12][n7] & n18) == 0) break block20;
                                    ++n7;
                                }
                                --n9;
                            }
                            block22: while (n8 < n15) {
                                n7 = n11;
                                while (n7 <= n10) {
                                    if ((this.anIntArrayArrayArray135[n8 + 1][n12][n7] & n18) == 0) break block22;
                                    ++n7;
                                }
                                ++n8;
                            }
                            n7 = (n8 + 1 - n9) * (n10 - n11 + 1);
                            if (n7 >= 8) {
                                n6 = 240;
                                n5 = this.anIntArrayArrayArray129[n8][n12][n11] - n6;
                                n4 = this.anIntArrayArrayArray129[n9][n12][n11];
                                Class25.method277((int)n15, (int)(n12 * 128), (int)n4, (int)(n12 * 128), (int)(n10 * 128 + 128), (int)n5, (int)this.anInt150, (int)(n11 * 128), (int)1);
                                n3 = n9;
                                while (n3 <= n8) {
                                    n2 = n11;
                                    while (n2 <= n10) {
                                        int[] nArray = this.anIntArrayArrayArray135[n3][n12];
                                        int n57 = n2++;
                                        nArray[n57] = nArray[n57] & ~n18;
                                    }
                                    ++n3;
                                }
                            }
                        }
                        if ((this.anIntArrayArrayArray135[n14][n12][n13] & n17) != 0) {
                            n11 = n12;
                            n10 = n12;
                            n9 = n14;
                            n8 = n14;
                            while (n11 > 0 && (this.anIntArrayArrayArray135[n14][n11 - 1][n13] & n17) != 0) {
                                --n11;
                            }
                            while (n10 < this.anInt146 && (this.anIntArrayArrayArray135[n14][n10 + 1][n13] & n17) != 0) {
                                ++n10;
                            }
                            block28: while (n9 > 0) {
                                n7 = n11;
                                while (n7 <= n10) {
                                    if ((this.anIntArrayArrayArray135[n9 - 1][n7][n13] & n17) == 0) break block28;
                                    ++n7;
                                }
                                --n9;
                            }
                            block30: while (n8 < n15) {
                                n7 = n11;
                                while (n7 <= n10) {
                                    if ((this.anIntArrayArrayArray135[n8 + 1][n7][n13] & n17) == 0) break block30;
                                    ++n7;
                                }
                                ++n8;
                            }
                            n7 = (n8 + 1 - n9) * (n10 - n11 + 1);
                            if (n7 >= 8) {
                                n6 = 240;
                                n5 = this.anIntArrayArrayArray129[n8][n11][n13] - n6;
                                n4 = this.anIntArrayArrayArray129[n9][n11][n13];
                                Class25.method277((int)n15, (int)(n11 * 128), (int)n4, (int)(n10 * 128 + 128), (int)(n13 * 128), (int)n5, (int)this.anInt150, (int)(n13 * 128), (int)2);
                                n3 = n9;
                                while (n3 <= n8) {
                                    n2 = n11;
                                    while (n2 <= n10) {
                                        int[] nArray = this.anIntArrayArrayArray135[n3][n2];
                                        int n58 = n13;
                                        nArray[n58] = nArray[n58] & ~n17;
                                        ++n2;
                                    }
                                    ++n3;
                                }
                            }
                        }
                        if ((this.anIntArrayArrayArray135[n14][n12][n13] & n16) != 0) {
                            n11 = n12;
                            n10 = n12;
                            n9 = n13;
                            n8 = n13;
                            while (n9 > 0 && (this.anIntArrayArrayArray135[n14][n12][n9 - 1] & n16) != 0) {
                                --n9;
                            }
                            while (n8 < this.anInt147 && (this.anIntArrayArrayArray135[n14][n12][n8 + 1] & n16) != 0) {
                                ++n8;
                            }
                            block36: while (n11 > 0) {
                                n7 = n9;
                                while (n7 <= n8) {
                                    if ((this.anIntArrayArrayArray135[n14][n11 - 1][n7] & n16) == 0) break block36;
                                    ++n7;
                                }
                                --n11;
                            }
                            block38: while (n10 < this.anInt146) {
                                n7 = n9;
                                while (n7 <= n8) {
                                    if ((this.anIntArrayArrayArray135[n14][n10 + 1][n7] & n16) == 0) break block38;
                                    ++n7;
                                }
                                ++n10;
                            }
                            if ((n10 - n11 + 1) * (n8 - n9 + 1) >= 4) {
                                n7 = this.anIntArrayArrayArray129[n14][n11][n9];
                                Class25.method277((int)n15, (int)(n11 * 128), (int)n7, (int)(n10 * 128 + 128), (int)(n8 * 128 + 128), (int)n7, (int)this.anInt150, (int)(n9 * 128), (int)4);
                                n6 = n11;
                                while (n6 <= n10) {
                                    n5 = n9;
                                    while (n5 <= n8) {
                                        int[] nArray = this.anIntArrayArrayArray135[n14][n6];
                                        int n59 = n5++;
                                        nArray[n59] = nArray[n59] & ~n16;
                                    }
                                    ++n6;
                                }
                            }
                        }
                        ++n12;
                    }
                    ++n13;
                }
                ++n14;
            }
            ++n15;
        }
    }

    private static final int method172(int n, int n2) {
        int n3 = Class7.method176(n + 45365, n2 + 91923, 4) - 128 + (Class7.method176(n + 10294, n2 + 37821, 2) - 128 >> 1) + (Class7.method176(n, n2, 1) - 128 >> 2);
        if ((n3 = (int)((double)n3 * 0.3) + 35) < 10) {
            n3 = 10;
        } else if (n3 > 60) {
            n3 = 60;
        }
        return n3;
    }

    public static final void method173(byte by, Class30_Sub2_Sub2 class30_Sub2_Sub2, Class42_Sub1 class42_Sub1) {
        int n;
        int n2 = -1;
        if (by != -107) {
            boolean bl = aBoolean141 = !aBoolean141;
        }
        while ((n = class30_Sub2_Sub2.method422()) != 0) {
            int n3;
            Class46 class46 = Class46.method572((int)(n2 += n));
            class46.method574(class42_Sub1, -235);
            while ((n3 = class30_Sub2_Sub2.method422()) != 0) {
                class30_Sub2_Sub2.method408();
            }
        }
    }

    public final void method174(int n, int n2, int n3, int n4, int n5) {
        int n6 = n;
        while (n6 <= n + n2) {
            int n7 = n5;
            while (n7 <= n5 + n4) {
                if (n7 >= 0 && n7 < this.anInt146 && n6 >= 0 && n6 < this.anInt147) {
                    this.aByteArrayArrayArray134[0][n7][n6] = 127;
                    if (n7 == n5 && n7 > 0) {
                        this.anIntArrayArrayArray129[0][n7][n6] = this.anIntArrayArrayArray129[0][n7 - 1][n6];
                    }
                    if (n7 == n5 + n4 && n7 < this.anInt146 - 1) {
                        this.anIntArrayArrayArray129[0][n7][n6] = this.anIntArrayArrayArray129[0][n7 + 1][n6];
                    }
                    if (n6 == n && n6 > 0) {
                        this.anIntArrayArrayArray129[0][n7][n6] = this.anIntArrayArrayArray129[0][n7][n6 - 1];
                    }
                    if (n6 == n + n2 && n6 < this.anInt147 - 1) {
                        this.anIntArrayArrayArray129[0][n7][n6] = this.anIntArrayArrayArray129[0][n7][n6 + 1];
                    }
                }
                ++n7;
            }
            ++n6;
        }
        if (n3 != 0) {
            anInt153 = 284;
        }
    }

    private final void method175(int n, Class25 class25, Class11 class11, int n2, int n3, int n4, int n5, boolean bl, int n6) {
        int n7;
        if (aBoolean151 && (this.aByteArrayArrayArray149[0][n4][n] & 2) == 0) {
            if ((this.aByteArrayArrayArray149[n3][n4][n] & 0x10) != 0) {
                return;
            }
            if (this.method182(n, n3, n4, 0) != anInt131) {
                return;
            }
        }
        if (n3 < anInt145) {
            anInt145 = n3;
        }
        int n8 = this.anIntArrayArrayArray129[n3][n4][n];
        int n9 = this.anIntArrayArrayArray129[n3][n4 + 1][n];
        int n10 = this.anIntArrayArrayArray129[n3][n4 + 1][n + 1];
        int n11 = this.anIntArrayArrayArray129[n3][n4][n + 1];
        int n12 = n8 + n9 + n10 + n11 >> 2;
        Class46 class46 = Class46.method572((int)n5);
        int n13 = n4 + (n << 7) + (n5 << 14) + 0x40000000;
        if (!class46.aBoolean778) {
            n13 += Integer.MIN_VALUE;
        }
        byte by = (byte)((n6 << 6) + n2);
        if (bl) {
            return;
        }
        if (n2 == 22) {
            if (aBoolean151 && !class46.aBoolean778 && !class46.aBoolean736) {
                return;
            }
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(22, n6, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, n6, 22, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method280(n3, n12, n, 68, (Class30_Sub2_Sub4)object, by, n13, n4);
            if (class46.aBoolean767 && class46.aBoolean778 && class11 != null) {
                class11.method213(n, 0, n4);
            }
            return;
        }
        if (n2 == 10 || n2 == 11) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(10, n6, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, n6, 10, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            if (object != null) {
                Object object2;
                int n14;
                int n15;
                int n16 = 0;
                if (n2 == 11) {
                    n16 += 256;
                }
                if (n6 == 1 || n6 == 3) {
                    n15 = class46.anInt761;
                    n14 = class46.anInt744;
                } else {
                    n15 = class46.anInt744;
                    n14 = class46.anInt761;
                }
                if (class25.method284(n13, by, n12, n14, (Class30_Sub2_Sub4)object, n15, n3, n16, (byte)110, n, n4) && class46.aBoolean779 && (object2 = object instanceof Class30_Sub2_Sub4_Sub6 ? object : class46.method578(10, n6, n8, n9, n10, n11, -1)) != null) {
                    int n17 = 0;
                    while (n17 <= n15) {
                        int n18 = 0;
                        while (n18 <= n14) {
                            int n19 = ((Class30_Sub2_Sub4_Sub6)object2).anInt1650 / 4;
                            if (n19 > 30) {
                                n19 = 30;
                            }
                            if (n19 > this.aByteArrayArrayArray134[n3][n4 + n17][n + n18]) {
                                this.aByteArrayArrayArray134[n3][n4 + n17][n + n18] = (byte)n19;
                            }
                            ++n18;
                        }
                        ++n17;
                    }
                }
            }
            if (class46.aBoolean767 && class11 != null) {
                class11.method212(class46.aBoolean757, anInt138, class46.anInt744, class46.anInt761, n4, n, n6);
            }
            return;
        }
        if (n2 >= 12) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(n2, n6, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, n6, n2, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method284(n13, by, n12, 1, (Class30_Sub2_Sub4)object, 1, n3, 0, (byte)110, n, n4);
            if (n2 >= 12 && n2 <= 17 && n2 != 13 && n3 > 0) {
                int[] nArray = this.anIntArrayArrayArray135[n3][n4];
                int n20 = n;
                nArray[n20] = nArray[n20] | 0x924;
            }
            if (class46.aBoolean767 && class11 != null) {
                class11.method212(class46.aBoolean757, anInt138, class46.anInt744, class46.anInt761, n4, n, n6);
            }
            return;
        }
        if (n2 == 0) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(0, n6, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, n6, 0, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method282(anIntArray152[n6], (Class30_Sub2_Sub4)object, true, n13, n, by, n4, null, n12, 0, n3);
            if (n6 == 0) {
                if (class46.aBoolean779) {
                    this.aByteArrayArrayArray134[n3][n4][n] = 50;
                    this.aByteArrayArrayArray134[n3][n4][n + 1] = 50;
                }
                if (class46.aBoolean764) {
                    int[] nArray = this.anIntArrayArrayArray135[n3][n4];
                    int n21 = n;
                    nArray[n21] = nArray[n21] | 0x249;
                }
            } else if (n6 == 1) {
                if (class46.aBoolean779) {
                    this.aByteArrayArrayArray134[n3][n4][n + 1] = 50;
                    this.aByteArrayArrayArray134[n3][n4 + 1][n + 1] = 50;
                }
                if (class46.aBoolean764) {
                    int[] nArray = this.anIntArrayArrayArray135[n3][n4];
                    int n22 = n + 1;
                    nArray[n22] = nArray[n22] | 0x492;
                }
            } else if (n6 == 2) {
                if (class46.aBoolean779) {
                    this.aByteArrayArrayArray134[n3][n4 + 1][n] = 50;
                    this.aByteArrayArrayArray134[n3][n4 + 1][n + 1] = 50;
                }
                if (class46.aBoolean764) {
                    int[] nArray = this.anIntArrayArrayArray135[n3][n4 + 1];
                    int n23 = n;
                    nArray[n23] = nArray[n23] | 0x249;
                }
            } else if (n6 == 3) {
                if (class46.aBoolean779) {
                    this.aByteArrayArrayArray134[n3][n4][n] = 50;
                    this.aByteArrayArrayArray134[n3][n4 + 1][n] = 50;
                }
                if (class46.aBoolean764) {
                    int[] nArray = this.anIntArrayArrayArray135[n3][n4];
                    int n24 = n;
                    nArray[n24] = nArray[n24] | 0x492;
                }
            }
            if (class46.aBoolean767 && class11 != null) {
                class11.method211(n, n6, n4, n2, (byte)1, class46.aBoolean757);
            }
            if (class46.anInt775 != 16) {
                class25.method290(n, 441, class46.anInt775, n4, n3);
            }
            return;
        }
        if (n2 == 1) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(1, n6, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, n6, 1, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method282(anIntArray140[n6], (Class30_Sub2_Sub4)object, true, n13, n, by, n4, null, n12, 0, n3);
            if (class46.aBoolean779) {
                if (n6 == 0) {
                    this.aByteArrayArrayArray134[n3][n4][n + 1] = 50;
                } else if (n6 == 1) {
                    this.aByteArrayArrayArray134[n3][n4 + 1][n + 1] = 50;
                } else if (n6 == 2) {
                    this.aByteArrayArrayArray134[n3][n4 + 1][n] = 50;
                } else if (n6 == 3) {
                    this.aByteArrayArrayArray134[n3][n4][n] = 50;
                }
            }
            if (class46.aBoolean767 && class11 != null) {
                class11.method211(n, n6, n4, n2, (byte)1, class46.aBoolean757);
            }
            return;
        }
        if (n2 == 2) {
            Class30_Sub2_Sub4 class30_Sub2_Sub4_Sub6;
            Class30_Sub2_Sub4 class30_Sub2_Sub4_Sub62;
            int n25 = n6 + 1 & 3;
            if (class46.anInt781 == -1 && class46.anIntArray759 == null) {
                class30_Sub2_Sub4_Sub62 = class46.method578(2, 4 + n6, n8, n9, n10, n11, -1);
                class30_Sub2_Sub4_Sub6 = class46.method578(2, n25, n8, n9, n10, n11, -1);
            } else {
                class30_Sub2_Sub4_Sub62 = new Class30_Sub2_Sub4_Sub5(n5, 4 + n6, 2, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
                class30_Sub2_Sub4_Sub6 = new Class30_Sub2_Sub4_Sub5(n5, n25, 2, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            }
            class25.method282(anIntArray152[n6], (Class30_Sub2_Sub4)class30_Sub2_Sub4_Sub62, true, n13, n, by, n4, (Class30_Sub2_Sub4)class30_Sub2_Sub4_Sub6, n12, anIntArray152[n25], n3);
            if (class46.aBoolean764) {
                if (n6 == 0) {
                    int[] nArray = this.anIntArrayArrayArray135[n3][n4];
                    int n26 = n;
                    nArray[n26] = nArray[n26] | 0x249;
                    int[] nArray2 = this.anIntArrayArrayArray135[n3][n4];
                    int n27 = n + 1;
                    nArray2[n27] = nArray2[n27] | 0x492;
                } else if (n6 == 1) {
                    int[] nArray = this.anIntArrayArrayArray135[n3][n4];
                    int n28 = n + 1;
                    nArray[n28] = nArray[n28] | 0x492;
                    int[] nArray3 = this.anIntArrayArrayArray135[n3][n4 + 1];
                    int n29 = n;
                    nArray3[n29] = nArray3[n29] | 0x249;
                } else if (n6 == 2) {
                    int[] nArray = this.anIntArrayArrayArray135[n3][n4 + 1];
                    int n30 = n;
                    nArray[n30] = nArray[n30] | 0x249;
                    int[] nArray4 = this.anIntArrayArrayArray135[n3][n4];
                    int n31 = n;
                    nArray4[n31] = nArray4[n31] | 0x492;
                } else if (n6 == 3) {
                    int[] nArray = this.anIntArrayArrayArray135[n3][n4];
                    int n32 = n;
                    nArray[n32] = nArray[n32] | 0x492;
                    int[] nArray5 = this.anIntArrayArrayArray135[n3][n4];
                    int n33 = n;
                    nArray5[n33] = nArray5[n33] | 0x249;
                }
            }
            if (class46.aBoolean767 && class11 != null) {
                class11.method211(n, n6, n4, n2, (byte)1, class46.aBoolean757);
            }
            if (class46.anInt775 != 16) {
                class25.method290(n, 441, class46.anInt775, n4, n3);
            }
            return;
        }
        if (n2 == 3) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(3, n6, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, n6, 3, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method282(anIntArray140[n6], (Class30_Sub2_Sub4)object, true, n13, n, by, n4, null, n12, 0, n3);
            if (class46.aBoolean779) {
                if (n6 == 0) {
                    this.aByteArrayArrayArray134[n3][n4][n + 1] = 50;
                } else if (n6 == 1) {
                    this.aByteArrayArrayArray134[n3][n4 + 1][n + 1] = 50;
                } else if (n6 == 2) {
                    this.aByteArrayArrayArray134[n3][n4 + 1][n] = 50;
                } else if (n6 == 3) {
                    this.aByteArrayArrayArray134[n3][n4][n] = 50;
                }
            }
            if (class46.aBoolean767 && class11 != null) {
                class11.method211(n, n6, n4, n2, (byte)1, class46.aBoolean757);
            }
            return;
        }
        if (n2 == 9) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(n2, n6, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, n6, n2, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method284(n13, by, n12, 1, (Class30_Sub2_Sub4)object, 1, n3, 0, (byte)110, n, n4);
            if (class46.aBoolean767 && class11 != null) {
                class11.method212(class46.aBoolean757, anInt138, class46.anInt744, class46.anInt761, n4, n, n6);
            }
            return;
        }
        if (class46.aBoolean762) {
            if (n6 == 1) {
                n7 = n11;
                n11 = n10;
                n10 = n9;
                n9 = n8;
                n8 = n7;
            } else if (n6 == 2) {
                n7 = n11;
                n11 = n9;
                n9 = n7;
                n7 = n10;
                n10 = n8;
                n8 = n7;
            } else if (n6 == 3) {
                n7 = n11;
                n11 = n8;
                n8 = n9;
                n9 = n10;
                n10 = n7;
            }
        }
        if (n2 == 4) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(4, 0, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, 0, 4, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method283(n13, n, n6 * 512, -460, n3, 0, n12, (Class30_Sub2_Sub4)object, n4, by, 0, anIntArray152[n6]);
            return;
        }
        if (n2 == 5) {
            n7 = 16;
            int n34 = class25.method300(n3, n4, n);
            if (n34 > 0) {
                n7 = Class46.method572((int)(n34 >> 14 & Short.MAX_VALUE)).anInt775;
            }
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(4, 0, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, 0, 4, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method283(n13, n, n6 * 512, -460, n3, anIntArray137[n6] * n7, n12, (Class30_Sub2_Sub4)object, n4, by, anIntArray144[n6] * n7, anIntArray152[n6]);
            return;
        }
        if (n2 == 6) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(4, 0, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, 0, 4, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method283(n13, n, n6, -460, n3, 0, n12, (Class30_Sub2_Sub4)object, n4, by, 0, 256);
            return;
        }
        if (n2 == 7) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(4, 0, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, 0, 4, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method283(n13, n, n6, -460, n3, 0, n12, (Class30_Sub2_Sub4)object, n4, by, 0, 512);
            return;
        }
        if (n2 == 8) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(4, 0, n8, n9, n10, n11, -1) : new Class30_Sub2_Sub4_Sub5(n5, 0, 4, n9, (byte)7, n10, n8, n11, class46.anInt781, true);
            class25.method283(n13, n, n6, -460, n3, 0, n12, (Class30_Sub2_Sub4)object, n4, by, 0, 768);
        }
    }

    private static final int method176(int n, int n2, int n3) {
        int n4 = n / n3;
        int n5 = n & n3 - 1;
        int n6 = n2 / n3;
        int n7 = n2 & n3 - 1;
        int n8 = Class7.method186(n4, n6);
        int n9 = Class7.method186(n4 + 1, n6);
        int n10 = Class7.method186(n4, n6 + 1);
        int n11 = Class7.method186(n4 + 1, n6 + 1);
        int n12 = Class7.method184(n8, n9, n5, n3);
        int n13 = Class7.method184(n10, n11, n5, n3);
        return Class7.method184(n12, n13, n7, n3);
    }

    private final int method177(int n, int n2, int n3) {
        if (n3 > 179) {
            n2 /= 2;
        }
        if (n3 > 192) {
            n2 /= 2;
        }
        if (n3 > 217) {
            n2 /= 2;
        }
        if (n3 > 243) {
            n2 /= 2;
        }
        int n4 = (n / 4 << 10) + (n2 / 32 << 7) + n3 / 2;
        return n4;
    }

    public static final boolean method178(int n, int n2, int n3) {
        Class46 class46 = Class46.method572((int)n);
        if (n3 != 8) {
            int n4 = 1;
            while (n4 > 0) {
                ++n4;
            }
        }
        if (n2 == 11) {
            n2 = 10;
        }
        if (n2 >= 5 && n2 <= 8) {
            n2 = 4;
        }
        return class46.method577(n2, true);
    }

    public final void method179(int n, int n2, Class11[] class11Array, int n3, int n4, int n5, byte[] byArray, int n6, int n7, int n8) {
        int n9;
        int n10 = 0;
        while (n10 < 8) {
            n9 = 0;
            while (n9 < 8) {
                if (n4 + n10 > 0 && n4 + n10 < 103 && n8 + n9 > 0 && n8 + n9 < 103) {
                    int[] nArray = class11Array[n7].anIntArrayArray294[n4 + n10];
                    int n11 = n8 + n9;
                    nArray[n11] = nArray[n11] & 0xFEFFFFFF;
                }
                ++n9;
            }
            ++n10;
        }
        if (n3 < 9 || n3 > 9) {
            n9 = 1;
            while (n9 > 0) {
                ++n9;
            }
        }
        Class30_Sub2_Sub2 class30_Sub2_Sub2 = new Class30_Sub2_Sub2(byArray, 891);
        int n12 = 0;
        while (n12 < 4) {
            int n13 = 0;
            while (n13 < 64) {
                int n14 = 0;
                while (n14 < 64) {
                    if (n12 == n && n13 >= n5 && n13 < n5 + 8 && n14 >= n6 && n14 < n6 + 8) {
                        this.method181(n8 + Class4.method156((int)(n14 & 7), (int)n2, (int)-383, (int)(n13 & 7)), 0, class30_Sub2_Sub2, n4 + Class4.method155((int)n2, (int)(n14 & 7), (int)(n13 & 7), (boolean)false), n7, n2, 942, 0);
                    } else {
                        this.method181(-1, 0, class30_Sub2_Sub2, -1, 0, 0, 942, 0);
                    }
                    ++n14;
                }
                ++n13;
            }
            ++n12;
        }
    }

    public final void method180(byte[] byArray, int n, int n2, int n3, int n4, byte by, Class11[] class11Array) {
        int n5;
        int n6 = 0;
        while (n6 < 4) {
            int n7 = 0;
            while (n7 < 64) {
                n5 = 0;
                while (n5 < 64) {
                    if (n2 + n7 > 0 && n2 + n7 < 103 && n + n5 > 0 && n + n5 < 103) {
                        int[] nArray = class11Array[n6].anIntArrayArray294[n2 + n7];
                        int n8 = n + n5;
                        nArray[n8] = nArray[n8] & 0xFEFFFFFF;
                    }
                    ++n5;
                }
                ++n7;
            }
            ++n6;
        }
        Class30_Sub2_Sub2 class30_Sub2_Sub2 = new Class30_Sub2_Sub2(byArray, 891);
        n5 = 0;
        while (n5 < 4) {
            int n9 = 0;
            while (n9 < 64) {
                int n10 = 0;
                while (n10 < 64) {
                    this.method181(n10 + n, n4, class30_Sub2_Sub2, n9 + n2, n5, 0, 942, n3);
                    ++n10;
                }
                ++n9;
            }
            ++n5;
        }
        if (by != 4) {
            this.aBoolean143 = !this.aBoolean143;
        }
    }

    private final void method181(int n, int n2, Class30_Sub2_Sub2 class30_Sub2_Sub2, int n3, int n4, int n5, int n6, int n7) {
        int n8;
        n6 = 36 / n6;
        if (n3 >= 0 && n3 < 104 && n >= 0 && n < 104) {
            this.aByteArrayArrayArray149[n4][n3][n] = 0;
            while (true) {
                int n9;
                if ((n9 = class30_Sub2_Sub2.method408()) == 0) {
                    if (n4 == 0) {
                        this.anIntArrayArrayArray129[0][n3][n] = -Class7.method172(932731 + n3 + n7, 556238 + n + n2) * 8;
                        return;
                    }
                    this.anIntArrayArrayArray129[n4][n3][n] = this.anIntArrayArrayArray129[n4 - 1][n3][n] - 240;
                    return;
                }
                if (n9 == 1) {
                    int n10 = class30_Sub2_Sub2.method408();
                    if (n10 == 1) {
                        n10 = 0;
                    }
                    if (n4 == 0) {
                        this.anIntArrayArrayArray129[0][n3][n] = -n10 * 8;
                        return;
                    }
                    this.anIntArrayArrayArray129[n4][n3][n] = this.anIntArrayArrayArray129[n4 - 1][n3][n] - n10 * 8;
                    return;
                }
                if (n9 <= 49) {
                    this.aByteArrayArrayArray130[n4][n3][n] = class30_Sub2_Sub2.method409();
                    this.aByteArrayArrayArray136[n4][n3][n] = (byte)((n9 - 2) / 4);
                    this.aByteArrayArrayArray148[n4][n3][n] = (byte)(n9 - 2 + n5 & 3);
                    continue;
                }
                if (n9 <= 81) {
                    this.aByteArrayArrayArray149[n4][n3][n] = (byte)(n9 - 49);
                    continue;
                }
                this.aByteArrayArrayArray142[n4][n3][n] = (byte)(n9 - 81);
            }
        }
        while ((n8 = class30_Sub2_Sub2.method408()) != 0) {
            if (n8 == 1) {
                class30_Sub2_Sub2.method408();
                return;
            }
            if (n8 > 49) continue;
            class30_Sub2_Sub2.method408();
        }
    }

    public int method182(int n, int n2, int n3, int n4) {
        if (n4 != 0) {
            return 2;
        }
        if ((this.aByteArrayArrayArray149[n2][n3][n] & 8) != 0) {
            return 0;
        }
        if (n2 > 0 && (this.aByteArrayArrayArray149[1][n3][n] & 2) != 0) {
            return n2 - 1;
        }
        return n2;
    }

    public final void method183(Class11[] class11Array, Class25 class25, int n, int n2, int n3, boolean bl, int n4, byte[] byArray, int n5, int n6, int n7) {
        int n8;
        Class30_Sub2_Sub2 class30_Sub2_Sub2 = new Class30_Sub2_Sub2(byArray, 891);
        int n9 = -1;
        if (!bl) {
            boolean bl2 = this.aBoolean143 = !this.aBoolean143;
        }
        while ((n8 = class30_Sub2_Sub2.method422()) != 0) {
            int n10;
            n9 += n8;
            int n11 = 0;
            while ((n10 = class30_Sub2_Sub2.method422()) != 0) {
                int n12 = (n11 += n10 - 1) & 0x3F;
                int n13 = n11 >> 6 & 0x3F;
                int n14 = n11 >> 12;
                int n15 = class30_Sub2_Sub2.method408();
                int n16 = n15 >> 2;
                int n17 = n15 & 3;
                if (n14 != n || n13 < n5 || n13 >= n5 + 8 || n12 < n3 || n12 >= n3 + 8) continue;
                Class46 class46 = Class46.method572((int)n9);
                int n18 = n2 + Class4.method157((int)n6, (int)class46.anInt761, (int)(n13 & 7), (byte)113, (int)(n12 & 7), (int)class46.anInt744);
                int n19 = n7 + Class4.method158((int)-433, (int)(n12 & 7), (int)class46.anInt761, (int)n6, (int)class46.anInt744, (int)(n13 & 7));
                if (n18 <= 0 || n19 <= 0 || n18 >= 103 || n19 >= 103) continue;
                int n20 = n14;
                if ((this.aByteArrayArrayArray149[1][n18][n19] & 2) == 2) {
                    --n20;
                }
                Class11 class11 = null;
                if (n20 >= 0) {
                    class11 = class11Array[n20];
                }
                this.method175(n19, class25, class11, n16, n4, n18, n9, false, n17 + n6 & 3);
            }
        }
    }

    private static final int method184(int n, int n2, int n3, int n4) {
        int n5 = 65536 - Class30_Sub2_Sub1_Sub3.anIntArray1471[n3 * 1024 / n4] >> 1;
        return (n * (65536 - n5) >> 16) + (n2 * n5 >> 16);
    }

    private final int method185(int n, int n2) {
        if (n == -2) {
            return 12345678;
        }
        if (n == -1) {
            if (n2 < 0) {
                n2 = 0;
            } else if (n2 > 127) {
                n2 = 127;
            }
            n2 = 127 - n2;
            return n2;
        }
        if ((n2 = n2 * (n & 0x7F) / 128) < 2) {
            n2 = 2;
        } else if (n2 > 126) {
            n2 = 126;
        }
        return (n & 0xFF80) + n2;
    }

    private static final int method186(int n, int n2) {
        int n3 = Class7.method170(n - 1, n2 - 1) + Class7.method170(n + 1, n2 - 1) + Class7.method170(n - 1, n2 + 1) + Class7.method170(n + 1, n2 + 1);
        int n4 = Class7.method170(n - 1, n2) + Class7.method170(n + 1, n2) + Class7.method170(n, n2 - 1) + Class7.method170(n, n2 + 1);
        int n5 = Class7.method170(n, n2);
        return n3 / 16 + n4 / 8 + n5 / 4;
    }

    private static final int method187(int n, int n2) {
        if (n == -1) {
            return 12345678;
        }
        if ((n2 = n2 * (n & 0x7F) / 128) < 2) {
            n2 = 2;
        } else if (n2 > 126) {
            n2 = 126;
        }
        return (n & 0xFF80) + n2;
    }

    public static final void method188(Class25 class25, int n, int n2, int n3, int n4, Class11 class11, int[][][] nArray, int n5, int n6, int n7, byte by) {
        int n8;
        int n9 = nArray[n4][n5][n2];
        int n10 = nArray[n4][n5 + 1][n2];
        int n11 = nArray[n4][n5 + 1][n2 + 1];
        int n12 = nArray[n4][n5][n2 + 1];
        if (by != 93) {
            anInt153 = -145;
        }
        int n13 = n9 + n10 + n11 + n12 >> 2;
        Class46 class46 = Class46.method572((int)n6);
        int n14 = n5 + (n2 << 7) + (n6 << 14) + 0x40000000;
        if (!class46.aBoolean778) {
            n14 += Integer.MIN_VALUE;
        }
        byte by2 = (byte)((n << 6) + n3);
        if (n3 == 22) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(22, n, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, n, 22, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method280(n7, n13, n2, 68, (Class30_Sub2_Sub4)object, by2, n14, n5);
            if (class46.aBoolean767 && class46.aBoolean778) {
                class11.method213(n2, 0, n5);
            }
            return;
        }
        if (n3 == 10 || n3 == 11) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(10, n, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, n, 10, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            if (object != null) {
                int n15;
                int n16;
                int n17 = 0;
                if (n3 == 11) {
                    n17 += 256;
                }
                if (n == 1 || n == 3) {
                    n16 = class46.anInt761;
                    n15 = class46.anInt744;
                } else {
                    n16 = class46.anInt744;
                    n15 = class46.anInt761;
                }
                class25.method284(n14, by2, n13, n15, (Class30_Sub2_Sub4)object, n16, n7, n17, (byte)110, n2, n5);
            }
            if (class46.aBoolean767) {
                class11.method212(class46.aBoolean757, anInt138, class46.anInt744, class46.anInt761, n5, n2, n);
            }
            return;
        }
        if (n3 >= 12) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(n3, n, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, n, n3, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method284(n14, by2, n13, 1, (Class30_Sub2_Sub4)object, 1, n7, 0, (byte)110, n2, n5);
            if (class46.aBoolean767) {
                class11.method212(class46.aBoolean757, anInt138, class46.anInt744, class46.anInt761, n5, n2, n);
            }
            return;
        }
        if (n3 == 0) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(0, n, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, n, 0, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method282(anIntArray152[n], (Class30_Sub2_Sub4)object, true, n14, n2, by2, n5, null, n13, 0, n7);
            if (class46.aBoolean767) {
                class11.method211(n2, n, n5, n3, (byte)1, class46.aBoolean757);
            }
            return;
        }
        if (n3 == 1) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(1, n, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, n, 1, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method282(anIntArray140[n], (Class30_Sub2_Sub4)object, true, n14, n2, by2, n5, null, n13, 0, n7);
            if (class46.aBoolean767) {
                class11.method211(n2, n, n5, n3, (byte)1, class46.aBoolean757);
            }
            return;
        }
        if (n3 == 2) {
            Class30_Sub2_Sub4 class30_Sub2_Sub4_Sub6;
            Class30_Sub2_Sub4 class30_Sub2_Sub4_Sub62;
            int n18 = n + 1 & 3;
            if (class46.anInt781 == -1 && class46.anIntArray759 == null) {
                class30_Sub2_Sub4_Sub62 = class46.method578(2, 4 + n, n9, n10, n11, n12, -1);
                class30_Sub2_Sub4_Sub6 = class46.method578(2, n18, n9, n10, n11, n12, -1);
            } else {
                class30_Sub2_Sub4_Sub62 = new Class30_Sub2_Sub4_Sub5(n6, 4 + n, 2, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
                class30_Sub2_Sub4_Sub6 = new Class30_Sub2_Sub4_Sub5(n6, n18, 2, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            }
            class25.method282(anIntArray152[n], (Class30_Sub2_Sub4)class30_Sub2_Sub4_Sub62, true, n14, n2, by2, n5, (Class30_Sub2_Sub4)class30_Sub2_Sub4_Sub6, n13, anIntArray152[n18], n7);
            if (class46.aBoolean767) {
                class11.method211(n2, n, n5, n3, (byte)1, class46.aBoolean757);
            }
            return;
        }
        if (n3 == 3) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(3, n, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, n, 3, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method282(anIntArray140[n], (Class30_Sub2_Sub4)object, true, n14, n2, by2, n5, null, n13, 0, n7);
            if (class46.aBoolean767) {
                class11.method211(n2, n, n5, n3, (byte)1, class46.aBoolean757);
            }
            return;
        }
        if (n3 == 9) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(n3, n, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, n, n3, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method284(n14, by2, n13, 1, (Class30_Sub2_Sub4)object, 1, n7, 0, (byte)110, n2, n5);
            if (class46.aBoolean767) {
                class11.method212(class46.aBoolean757, anInt138, class46.anInt744, class46.anInt761, n5, n2, n);
            }
            return;
        }
        if (class46.aBoolean762) {
            if (n == 1) {
                n8 = n12;
                n12 = n11;
                n11 = n10;
                n10 = n9;
                n9 = n8;
            } else if (n == 2) {
                n8 = n12;
                n12 = n10;
                n10 = n8;
                n8 = n11;
                n11 = n9;
                n9 = n8;
            } else if (n == 3) {
                n8 = n12;
                n12 = n9;
                n9 = n10;
                n10 = n11;
                n11 = n8;
            }
        }
        if (n3 == 4) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(4, 0, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, 0, 4, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method283(n14, n2, n * 512, -460, n7, 0, n13, (Class30_Sub2_Sub4)object, n5, by2, 0, anIntArray152[n]);
            return;
        }
        if (n3 == 5) {
            n8 = 16;
            int n19 = class25.method300(n7, n5, n2);
            if (n19 > 0) {
                n8 = Class46.method572((int)(n19 >> 14 & Short.MAX_VALUE)).anInt775;
            }
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(4, 0, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, 0, 4, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method283(n14, n2, n * 512, -460, n7, anIntArray137[n] * n8, n13, (Class30_Sub2_Sub4)object, n5, by2, anIntArray144[n] * n8, anIntArray152[n]);
            return;
        }
        if (n3 == 6) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(4, 0, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, 0, 4, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method283(n14, n2, n, -460, n7, 0, n13, (Class30_Sub2_Sub4)object, n5, by2, 0, 256);
            return;
        }
        if (n3 == 7) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(4, 0, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, 0, 4, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method283(n14, n2, n, -460, n7, 0, n13, (Class30_Sub2_Sub4)object, n5, by2, 0, 512);
            return;
        }
        if (n3 == 8) {
            Object object = class46.anInt781 == -1 && class46.anIntArray759 == null ? class46.method578(4, 0, n9, n10, n11, n12, -1) : new Class30_Sub2_Sub4_Sub5(n6, 0, 4, n10, (byte)7, n11, n9, n12, class46.anInt781, true);
            class25.method283(n14, n2, n, -460, n7, 0, n13, (Class30_Sub2_Sub4)object, n5, by2, 0, 768);
        }
    }

    public static final boolean method189(int n, byte[] byArray, int n2, int n3) {
        int n4;
        if (n3 < 6 || n3 > 6) {
            throw new NullPointerException();
        }
        boolean bl = true;
        Class30_Sub2_Sub2 class30_Sub2_Sub2 = new Class30_Sub2_Sub2(byArray, 891);
        int n5 = -1;
        block0: while ((n4 = class30_Sub2_Sub2.method422()) != 0) {
            n5 += n4;
            int n6 = 0;
            boolean bl2 = false;
            while (true) {
                int n7;
                if (bl2) {
                    n7 = class30_Sub2_Sub2.method422();
                    if (n7 == 0) continue block0;
                    class30_Sub2_Sub2.method408();
                    continue;
                }
                n7 = class30_Sub2_Sub2.method422();
                if (n7 == 0) continue block0;
                int n8 = (n6 += n7 - 1) & 0x3F;
                int n9 = n6 >> 6 & 0x3F;
                int n10 = class30_Sub2_Sub2.method408() >> 2;
                int n11 = n9 + n;
                int n12 = n8 + n2;
                if (n11 <= 0 || n12 <= 0 || n11 >= 103 || n12 >= 103) continue;
                Class46 class46 = Class46.method572((int)n5);
                if (n10 == 22 && aBoolean151 && !class46.aBoolean778 && !class46.aBoolean736) continue;
                bl &= class46.method579(true);
                bl2 = true;
            }
        }
        return bl;
    }

    public final void method190(int n, Class11[] class11Array, int n2, int n3, Class25 class25, byte[] byArray) {
        int n4;
        if (n3 < 7 || n3 > 7) {
            return;
        }
        Class30_Sub2_Sub2 class30_Sub2_Sub2 = new Class30_Sub2_Sub2(byArray, 891);
        int n5 = -1;
        while ((n4 = class30_Sub2_Sub2.method422()) != 0) {
            int n6;
            n5 += n4;
            int n7 = 0;
            while ((n6 = class30_Sub2_Sub2.method422()) != 0) {
                int n8 = (n7 += n6 - 1) & 0x3F;
                int n9 = n7 >> 6 & 0x3F;
                int n10 = n7 >> 12;
                int n11 = class30_Sub2_Sub2.method408();
                int n12 = n11 >> 2;
                int n13 = n11 & 3;
                int n14 = n9 + n;
                int n15 = n8 + n2;
                if (n14 <= 0 || n15 <= 0 || n14 >= 103 || n15 >= 103) continue;
                int n16 = n10;
                if ((this.aByteArrayArrayArray149[1][n14][n15] & 2) == 2) {
                    --n16;
                }
                Class11 class11 = null;
                if (n16 >= 0) {
                    class11 = class11Array[n16];
                }
                this.method175(n15, class25, class11, n12, n10, n14, n5, false, n13);
            }
        }
    }

    static {
        anInt133 = (int)(Math.random() * 33.0) - 16;
        int[] nArray = new int[4];
        nArray[0] = 1;
        nArray[2] = -1;
        anIntArray137 = nArray;
        anInt138 = 323;
        anIntArray140 = new int[]{16, 32, 64, 128};
        int[] nArray2 = new int[4];
        nArray2[1] = -1;
        nArray2[3] = 1;
        anIntArray144 = nArray2;
        anInt145 = 99;
        aBoolean151 = true;
        anIntArray152 = new int[]{1, 2, 4, 8};
        anInt153 = -388;
    }
}
