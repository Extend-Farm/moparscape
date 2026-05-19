/*
 * Decompiled with CFR 0.152.
 */
import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.zip.CRC32;
import sign.signlink;

public final class client
extends KHACHIFW {
    private int I;
    private static byte J = (byte)77;
    private long K;
    private int[][] L;
    private int[] M;
    private LHGXPZPG[][][] N;
    private int[] O;
    private int[] P;
    private boolean Q;
    private volatile boolean R;
    private Socket S;
    private int T;
    private MBMGIXGO U;
    private CWNCPMLX[] V;
    private int W;
    int[] X;
    private int Y;
    private int Z;
    int[] ab;
    private int bb;
    private int cb;
    private int db;
    private String eb;
    private int fb;
    private static int gb;
    private MBMGIXGO hb;
    private boolean ib;
    private static int jb;
    private int[] kb;
    private int[] lb;
    private int[] mb;
    private int[] nb;
    private static int ob;
    private int pb;
    private static BigInteger qb;
    private int rb;
    private int sb;
    private int tb;
    private int ub;
    private int vb;
    private int wb;
    private int xb;
    private int[] yb;
    private DSMJIEPN zb;
    private DSMJIEPN Ab;
    private DSMJIEPN Bb;
    private DSMJIEPN Cb;
    private DSMJIEPN Db;
    private CXGZMTJK Eb;
    private CXGZMTJK Fb;
    private boolean Gb;
    private int[] Hb;
    private int Ib;
    private int Jb;
    private boolean[] Kb;
    private int Lb;
    private int Mb;
    ZIJPRJEC Nb;
    private volatile boolean Ob;
    private String Pb;
    private int Qb;
    private int Rb;
    private int Sb;
    private boolean Tb;
    private int Ub;
    private String Vb;
    private int Wb;
    private int Xb;
    private DLZHLHNK[] Yb;
    private int Zb;
    int[] ac;
    private int bc;
    private int[] cc;
    private MBMGIXGO[] dc;
    private int ec;
    private int fc;
    private int gc;
    private int hc;
    private int ic;
    private int[][] jc;
    private int kc;
    private IVIFZQBK lc;
    private IVIFZQBK mc;
    private IVIFZQBK nc;
    private IVIFZQBK oc;
    private IVIFZQBK pc;
    private IVIFZQBK qc;
    private IVIFZQBK rc;
    private IVIFZQBK sc;
    private IVIFZQBK tc;
    private byte[] uc;
    private int vc;
    private int wc;
    private int xc;
    private int yc;
    private int zc;
    private int Ac;
    private static boolean Bc;
    private byte Cc;
    private int Dc;
    private int[] Ec;
    private byte Fc;
    private static int Gc;
    private long[] Hc;
    private boolean Ic;
    private int Jc;
    private int[] Kc;
    private int[][] Lc;
    private CRC32 Mc;
    private CXGZMTJK Nc;
    private CXGZMTJK Oc;
    private int Pc;
    private int Qc;
    private int Rc;
    private int Sc;
    private int Tc;
    private int Uc;
    private int Vc;
    private static int Wc;
    private static int Xc;
    private int[] Yc;
    private String[] Zc;
    private String[] ad;
    private int bd;
    private NYFUGYQS cd;
    private DSMJIEPN[] dd;
    private int ed;
    private int fd;
    private int gd;
    private int hd;
    private int id;
    private long jd;
    boolean kd;
    private long[] ld;
    private int md;
    private static int nd;
    static int od;
    private static boolean pd;
    private static boolean qd;
    private int rd;
    private volatile boolean sd;
    private int td;
    private int ud;
    private int[] vd;
    private DSMJIEPN wd;
    private DSMJIEPN xd;
    private int[] yd;
    private int[] zd;
    IGSLDTHC[] Ad;
    public int[] Bd;
    private boolean Cd;
    private byte Dd;
    private int Ed;
    private int Fd;
    private int[] Gd;
    private int[] Hd;
    private int[] Id;
    private int[] Jd;
    private int[] Kd;
    private int[] Ld;
    private int[] Md;
    private String[] Nd;
    private int Od;
    private int Pd;
    private static int Qd;
    private CXGZMTJK[] Rd;
    private int Sd;
    private int Td;
    private int[] Ud;
    private boolean Vd;
    private int Wd;
    private static boolean Xd;
    private boolean Yd;
    private int Zd;
    private int ae;
    private int be;
    private int ce;
    private int de;
    private JOCFVBOI ee;
    private CXGZMTJK fe;
    private int ge;
    static final int[][] he;
    private String ie;
    private static int je;
    private int ke;
    private int le;
    private int me;
    private int ne;
    private int oe;
    private int pe;
    private byte qe;
    private LHGXPZPG re;
    private int se;
    private int te;
    private int ue;
    private boolean ve;
    private int we;
    private static int[] xe;
    private int ye;
    private int ze;
    int Ae;
    private int Be;
    private DSMJIEPN Ce;
    private DSMJIEPN De;
    private int Ee;
    private DSMJIEPN Fe;
    private DSMJIEPN Ge;
    private DSMJIEPN He;
    private int[] Ie;
    private boolean Je;
    private static BigInteger Ke;
    private CXGZMTJK[] Le;
    private int Me;
    private int Ne;
    private int Oe;
    private int Pe;
    private int Qe;
    private int Re;
    private int Se;
    private int Te;
    private int Ue;
    private boolean Ve;
    private int[] We;
    private int[] Xe;
    private int Ye;
    private boolean Ze;
    private int af;
    private String bf;
    private int cf;
    private static int df;
    private int[] ef;
    private XTGLDHGX ff;
    private int gf;
    private int hf;
    private LHGXPZPG jf;
    private int[] kf;
    private int lf;
    private DUCMKFAY mf;
    private DSMJIEPN[] nf;
    static int of;
    private int pf;
    private int qf;
    private int rf;
    private int[] sf;
    private int tf;
    private int uf;
    private GHOWLKWN vf;
    private int wf;
    private int xf;
    private int yf;
    private int[] zf;
    private int[] Af;
    private CXGZMTJK Bf;
    private CXGZMTJK Cf;
    private CXGZMTJK Df;
    private CXGZMTJK Ef;
    private CXGZMTJK Ff;
    private int Gf;
    private boolean Hf;
    private int If;
    private String[] Jf;
    private MBMGIXGO Kf;
    private int Lf;
    private int Mf;
    private int Nf;
    private int Of;
    private int Pf;
    private int Qf;
    private int[] Rf;
    private int[] Sf;
    private int[] Tf;
    private int[] Uf;
    private int[] Vf;
    private CXGZMTJK[] Wf;
    private static int Xf;
    private static int Yf;
    private int Zf;
    private int ag;
    private int bg;
    private int cg;
    private int dg;
    private boolean eg;
    private int fg;
    private int gg;
    private boolean hg;
    private IVIFZQBK ig;
    private IVIFZQBK jg;
    private IVIFZQBK kg;
    private IVIFZQBK lg;
    private IVIFZQBK mg;
    private IVIFZQBK ng;
    private IVIFZQBK og;
    private IVIFZQBK pg;
    private IVIFZQBK qg;
    private int rg;
    private static int sg;
    private int tg;
    private int ug;
    private int vg;
    private String wg;
    private CXGZMTJK xg;
    private IVIFZQBK yg;
    private IVIFZQBK zg;
    private IVIFZQBK Ag;
    static DLZHLHNK Bg;
    private String[] Cg;
    private boolean[] Dg;
    private int[][][] Eg;
    private int[] Fg;
    private int Gg;
    private int Hg;
    private int Ig;
    private static int Jg;
    private int Kg;
    private int Lg;
    private int Mg;
    private int Ng;
    String Og;
    private CXGZMTJK[] Pg;
    private boolean Qg;
    private static int Rg;
    private DSMJIEPN Sg;
    private DSMJIEPN Tg;
    private DSMJIEPN Ug;
    private DSMJIEPN Vg;
    private DSMJIEPN Wg;
    private int Xg;
    private boolean Yg;
    private CXGZMTJK[] Zg;
    private boolean ah;
    private DSMJIEPN[] bh;
    private boolean ch;
    private int dh;
    private static int eh;
    static boolean fh;
    public boolean gh;
    private boolean hh;
    private boolean ih;
    private boolean jh;
    static int kh;
    private static String lh;
    private IVIFZQBK mh;
    private IVIFZQBK nh;
    private IVIFZQBK oh;
    private IVIFZQBK ph;
    private int qh;
    private NQABEVLK rh;
    private int sh;
    private int th;
    private int uh;
    private long vh;
    private String wh;
    private String xh;
    private static int yh;
    private boolean zh;
    private final int[] Ah;
    private int Bh;
    private LHGXPZPG Ch;
    private int[] Dh;
    private int[] Eh;
    private int[] Fh;
    private byte[][] Gh;
    private int Hh;
    private int Ih;
    private int Jh;
    private int Kh;
    private static int Lh;
    private int Mh;
    private int[] Nh;
    private int[] Oh;
    private MBMGIXGO Ph;
    private int Qh;
    private byte Rh;
    private int Sh;
    private DSMJIEPN Th;
    private DSMJIEPN Uh;
    private DSMJIEPN Vh;
    private String[] Wh;
    private static byte Xh;
    private CXGZMTJK Yh;
    private CXGZMTJK Zh;
    private int[] ai;
    static final int[] bi;
    public static boolean ci;
    private boolean di;
    private int[] ei;
    private int fi;
    private int gi;
    private int hi;
    private int ii;
    private String ji;
    private int ki;
    private int[][][] li;
    private long mi;
    private int ni;
    private byte oi;
    private int pi;
    private DSMJIEPN[] qi;
    long ri;
    private int si;
    private int ti;
    private boolean ui;
    private static boolean vi;
    private int wi;
    private static int xi;
    private int yi;
    private boolean zi;
    private int[] Ai;
    private FTPNODIB[] Bi;
    private static boolean Ci;
    public static int[] Di;
    private boolean Ei;
    private int[] Fi;
    private int[] Gi;
    private int[] Hi;
    int Ii;
    int Ji;
    private final int Ki = 100;
    private int[] Li;
    private int[] Mi;
    private boolean Ni;
    private int Oi;
    private int Pi;
    private int Qi;
    private int Ri;
    private byte[][] Si;
    private int Ti;
    private int Ui;
    private int[] Vi;
    private int Wi;
    private boolean Xi;
    private int Yi;
    private int Zi;
    private boolean aj;
    private boolean bj;
    private int cj;
    private byte[][][] dj;
    private int ej;
    private static int fj;
    private int gj;
    private int hj;
    private CXGZMTJK ij;
    private int jj;
    private int kj;
    private String lj;
    private String mj;
    private int nj;
    private int oj;
    private YXVQXWYR pj;
    private YXVQXWYR qj;
    private YXVQXWYR rj;
    private YXVQXWYR sj;
    private byte tj;
    private int uj;
    private int vj;
    private boolean wj;
    private int xj;
    private int yj;
    private int[] zj;
    private int[] Aj;
    private int Bj;
    private int Cj;
    private int Dj;
    private int Ej;
    String Fj;
    private int Gj;
    private static int Hj;
    private int Ij;
    public static int Jj;

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final String a(int n, int n2) {
        int n3 = Jj;
        try {
            String string;
            block9: {
                block8: {
                    string = String.valueOf(n);
                    int n4 = string.length() - 3;
                    boolean bl = true;
                    do {
                        if (bl && !(bl = false) && n3 == 0) continue;
                        string = String.valueOf(string.substring(0, n4)) + "," + string.substring(n4);
                        n4 -= 3;
                    } while (n4 > 0);
                    if (n2 != 0) {
                        boolean bl2 = vi = !vi;
                    }
                    if (string.length() <= 8) break block8;
                    string = "@gre@" + string.substring(0, string.length() - 8) + " million @whi@(" + string + ")";
                    if (n3 == 0) break block9;
                }
                if (string.length() > 4) {
                    string = "@cya@" + string.substring(0, string.length() - 4) + "K @whi@(" + string + ")";
                }
            }
            return " " + string;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("24548, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void g(int n) {
        try {
            signlink.midifade = 0;
            signlink.midi = "stop";
            if (n <= 0) {
                this.di = !this.di;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("83254, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void h(int n) {
        int n2 = Jj;
        try {
            if (n <= 0) {
                this.pi = this.ee.a();
            }
            int n3 = 5;
            this.Rf[8] = 0;
            int n4 = 0;
            boolean bl = true;
            do {
                String string;
                block19: {
                    if (bl && !(bl = false) && n2 == 0) continue;
                    string = "Unknown problem";
                    this.a(20, (byte)4, "Connecting to web server");
                    try {
                        DataInputStream dataInputStream = this.b("crc" + (int)(Math.random() * 9.9999999E7) + "-" + 317);
                        MBMGIXGO mBMGIXGO = new MBMGIXGO(new byte[40], 891);
                        dataInputStream.readFully(mBMGIXGO.y, 0, 40);
                        dataInputStream.close();
                        int n5 = 0;
                        boolean bl2 = true;
                        do {
                            if (bl2 && !(bl2 = false) && n2 == 0) continue;
                            this.Rf[n5] = mBMGIXGO.h();
                            ++n5;
                        } while (n5 < 9);
                        int n6 = mBMGIXGO.h();
                        int n7 = 1234;
                        int n8 = 0;
                        boolean bl3 = true;
                        do {
                            if (bl3 && !(bl3 = false) && n2 == 0) continue;
                            n7 = (n7 << 1) + this.Rf[n8];
                            ++n8;
                        } while (n8 < 9);
                        if (n6 != n7) {
                            string = "checksum problem";
                            this.Rf[8] = 0;
                        }
                    }
                    catch (EOFException eOFException) {
                        string = "EOF problem";
                        this.Rf[8] = 0;
                    }
                    catch (IOException iOException) {
                        string = "connection problem";
                        this.Rf[8] = 0;
                    }
                    catch (Exception exception) {
                        string = "logic problem";
                        this.Rf[8] = 0;
                        if (signlink.reporterror) break block19;
                        return;
                    }
                }
                if (this.Rf[8] != 0) continue;
                ++n4;
                int n9 = n3;
                boolean bl4 = true;
                do {
                    block21: {
                        block20: {
                            if (bl4 && !(bl4 = false) && n2 == 0) continue;
                            if (n4 < 10) break block20;
                            this.a(10, (byte)4, "Game updated - please reload page");
                            n9 = 10;
                            if (n2 == 0) break block21;
                        }
                        this.a(10, (byte)4, String.valueOf(string) + " - Will retry in " + n9 + " secs.");
                    }
                    try {
                        Thread.sleep(1000L);
                    }
                    catch (Exception exception) {}
                    --n9;
                } while (n9 > 0);
                if ((n3 *= 2) > 60) {
                    n3 = 60;
                }
                boolean bl5 = this.Gb = !this.Gb;
            } while (this.Rf[8] == 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("50895, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final boolean b(int n, int n2) {
        try {
            if (n2 < 0) {
                return false;
            }
            int n3 = this.Uf[n2];
            if (n != 9) {
                this.me = -1;
            }
            if (n3 >= 2000) {
                n3 -= 2000;
            }
            return n3 == 337;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("93711, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void i(int n) {
        int n2 = Jj;
        try {
            block31: {
                String string;
                YXVQXWYR yXVQXWYR;
                block38: {
                    block37: {
                        block36: {
                            block35: {
                                block34: {
                                    block33: {
                                        block32: {
                                            block30: {
                                                this.ph.a(0);
                                                OPPOFIOL.L = this.Dh;
                                                this.Vh.a(0, 16083, 0);
                                                if (!this.bj) break block30;
                                                this.rj.a(0, this.wg, 23693, 40, 239);
                                                this.rj.a(128, String.valueOf(this.ji) + "*", 23693, 60, 239);
                                                if (n2 == 0) break block31;
                                            }
                                            if (this.wi != 1) break block32;
                                            this.rj.a(0, "Enter amount:", 23693, 40, 239);
                                            this.rj.a(128, String.valueOf(this.ie) + "*", 23693, 60, 239);
                                            if (n2 == 0) break block31;
                                        }
                                        if (this.wi != 2) break block33;
                                        this.rj.a(0, "Enter name:", 23693, 40, 239);
                                        this.rj.a(128, String.valueOf(this.ie) + "*", 23693, 60, 239);
                                        if (n2 == 0) break block31;
                                    }
                                    if (this.eb == null) break block34;
                                    this.rj.a(0, this.eb, 23693, 40, 239);
                                    this.rj.a(128, "Click to continue", 23693, 60, 239);
                                    if (n2 == 0) break block31;
                                }
                                if (this.vj == -1) break block35;
                                this.a(8, 0, 0, DUCMKFAY.d[this.vj], 0);
                                if (n2 == 0) break block31;
                            }
                            if (this.Ue == -1) break block36;
                            this.a(8, 0, 0, DUCMKFAY.d[this.Ue], 0);
                            if (n2 == 0) break block31;
                        }
                        yXVQXWYR = this.qj;
                        int n3 = 0;
                        AFCKELYG.a(77, 0, false, 463, 0);
                        int n4 = 0;
                        boolean bl = true;
                        do {
                            if (bl && !(bl = false) && n2 == 0) continue;
                            if (this.ad[n4] != null) {
                                int n5;
                                int n6 = this.Yc[n4];
                                int n7 = 70 - n3 * 14 + this.Qf;
                                String string2 = this.Zc[n4];
                                int n8 = 0;
                                if (string2 != null && string2.startsWith("@cr1@")) {
                                    string2 = string2.substring(5);
                                    n8 = 1;
                                }
                                if (string2 != null && string2.startsWith("@cr2@")) {
                                    string2 = string2.substring(5);
                                    n8 = 2;
                                }
                                if (n6 == 0) {
                                    if (n7 > 0 && n7 < 110) {
                                        yXVQXWYR.b(0, this.ad[n4], n7, 822, 4);
                                    }
                                    ++n3;
                                }
                                if ((n6 == 1 || n6 == 2) && (n6 == 1 || this.Gj == 0 || this.Gj == 1 && this.a(false, string2))) {
                                    if (n7 > 0 && n7 < 110) {
                                        n5 = 4;
                                        if (n8 == 1) {
                                            this.qi[0].a(n5, 16083, n7 - 12);
                                            n5 += 14;
                                        }
                                        if (n8 == 2) {
                                            this.qi[1].a(n5, 16083, n7 - 12);
                                            n5 += 14;
                                        }
                                        yXVQXWYR.b(0, String.valueOf(string2) + ":", n7, 822, n5);
                                        yXVQXWYR.b(255, this.ad[n4], n7, 822, n5 += yXVQXWYR.a(this.rg, string2) + 8);
                                    }
                                    ++n3;
                                }
                                if ((n6 == 3 || n6 == 7) && this.Sh == 0 && (n6 == 7 || this.fb == 0 || this.fb == 1 && this.a(false, string2))) {
                                    if (n7 > 0 && n7 < 110) {
                                        n5 = 4;
                                        yXVQXWYR.b(0, "From", n7, 822, n5);
                                        n5 += yXVQXWYR.a(this.rg, "From ");
                                        if (n8 == 1) {
                                            this.qi[0].a(n5, 16083, n7 - 12);
                                            n5 += 14;
                                        }
                                        if (n8 == 2) {
                                            this.qi[1].a(n5, 16083, n7 - 12);
                                            n5 += 14;
                                        }
                                        yXVQXWYR.b(0, String.valueOf(string2) + ":", n7, 822, n5);
                                        yXVQXWYR.b(0x800000, this.ad[n4], n7, 822, n5 += yXVQXWYR.a(this.rg, string2) + 8);
                                    }
                                    ++n3;
                                }
                                if (n6 == 4 && (this.Ti == 0 || this.Ti == 1 && this.a(false, string2))) {
                                    if (n7 > 0 && n7 < 110) {
                                        yXVQXWYR.b(0x800080, String.valueOf(string2) + " " + this.ad[n4], n7, 822, 4);
                                    }
                                    ++n3;
                                }
                                if (n6 == 5 && this.Sh == 0 && this.fb < 2) {
                                    if (n7 > 0 && n7 < 110) {
                                        yXVQXWYR.b(0x800000, this.ad[n4], n7, 822, 4);
                                    }
                                    ++n3;
                                }
                                if (n6 == 6 && this.Sh == 0 && this.fb < 2) {
                                    if (n7 > 0 && n7 < 110) {
                                        yXVQXWYR.b(0, "To " + string2 + ":", n7, 822, 4);
                                        yXVQXWYR.b(0x800000, this.ad[n4], n7, 822, 12 + yXVQXWYR.a(this.rg, "To " + string2));
                                    }
                                    ++n3;
                                }
                                if (n6 == 8 && (this.Ti == 0 || this.Ti == 1 && this.a(false, string2))) {
                                    if (n7 > 0 && n7 < 110) {
                                        yXVQXWYR.b(8270336, String.valueOf(string2) + " " + this.ad[n4], n7, 822, 4);
                                    }
                                    ++n3;
                                }
                            }
                            ++n4;
                        } while (n4 < 100);
                        AFCKELYG.a(4);
                        this.ii = n3 * 14 + 7;
                        if (this.ii < 78) {
                            this.ii = 78;
                        }
                        this.a(519, 77, this.ii - this.Qf - 77, 0, 463, this.ii);
                        if (Bg == null || client.Bg.yb == null) break block37;
                        string = client.Bg.yb;
                        if (n2 == 0) break block38;
                    }
                    string = ZTQFNQRH.a(-45804, this.wh);
                }
                yXVQXWYR.b(0, String.valueOf(string) + ":", 90, 822, 4);
                yXVQXWYR.b(255, String.valueOf(this.Vb) + "*", 90, 822, 6 + yXVQXWYR.a(this.rg, String.valueOf(string) + ": "));
                AFCKELYG.a(77, 0, 479, 0, (byte)4);
            }
            if (this.Tb && this.ed == 2) {
                this.e((byte)9);
            }
            this.ph.a(357, 23680, this.l, 17);
            this.oh.a(0);
            OPPOFIOL.L = this.Fh;
            if (n < 6 || n > 6) {
                this.Vd = !this.Vd;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("19689, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void init() {
        block7: {
            block6: {
                String string;
                int n;
                block5: {
                    block4: {
                        n = Jj;
                        nd = Integer.parseInt(this.getParameter("nodeid"));
                        od = Integer.parseInt(this.getParameter("portoff"));
                        String string2 = this.getParameter("lowmem");
                        if (string2 == null || !string2.equals("1")) break block4;
                        client.m((byte)77);
                        if (n == 0) break block5;
                    }
                    client.e(false);
                }
                if ((string = this.getParameter("free")) == null || !string.equals("1")) break block6;
                pd = false;
                if (n == 0) break block7;
            }
            pd = true;
        }
        this.b(503, false, 765);
    }

    public final void a(Runnable runnable, int n) {
        if (n > 10) {
            n = 10;
        }
        if (signlink.mainapp != null) {
            signlink.startthread(runnable, n);
            return;
        }
        super.a(runnable, n);
    }

    public final Socket j(int n) throws IOException {
        if (signlink.mainapp != null) {
            return signlink.opensocket(n);
        }
        return new Socket(InetAddress.getByName(this.getCodeBase().getHost()), n);
    }

    /*
     * Unable to fully structure code
     */
    public final void k(int var1_1) {
        block28: {
            try {
                block30: {
                    block29: {
                        if (var1_1 != 4) {
                            this.me = this.Kf.c();
                        }
                        if (this.Nf != 0) {
                            return;
                        }
                        var2_2 = this.z;
                        if (this.Lg == 1 && this.A >= 516 && this.B >= 160 && this.A <= 765 && this.B <= 205) {
                            var2_2 = 0;
                        }
                        if (!this.Tb) break block29;
                        if (var2_2 != 1) {
                            var3_4 = this.t;
                            var4_6 = this.u;
                            if (this.ed == 0) {
                                var3_4 -= 4;
                                var4_6 -= 4;
                            }
                            if (this.ed == 1) {
                                var3_4 -= 553;
                                var4_6 -= 205;
                            }
                            if (this.ed == 2) {
                                var3_4 -= 17;
                                var4_6 -= 357;
                            }
                            if (var3_4 < this.fd - 10 || var3_4 > this.fd + this.hd + 10 || var4_6 < this.gd - 10 || var4_6 > this.gd + this.id + 10) {
                                this.Tb = false;
                                if (this.ed == 1) {
                                    this.ch = true;
                                }
                                if (this.ed == 2) {
                                    this.ui = true;
                                }
                            }
                        }
                        if (var2_2 != 1) break block30;
                        var3_4 = this.fd;
                        var4_6 = this.gd;
                        var5_8 = this.hd;
                        var6_10 = this.A;
                        var7_12 = this.B;
                        if (this.ed == 0) {
                            var6_10 -= 4;
                            var7_12 -= 4;
                        }
                        if (this.ed == 1) {
                            var6_10 -= 553;
                            var7_12 -= 205;
                        }
                        if (this.ed == 2) {
                            var6_10 -= 17;
                            var7_12 -= 357;
                        }
                        var8_13 = -1;
                        var9_14 = 0;
                        if (client.Jj == 0) ** GOTO lbl51
                        do {
                            var10_15 = var4_6 + 31 + (this.Ig - 1 - var9_14) * 15;
                            if (var6_10 > var3_4 && var6_10 < var3_4 + var5_8 && var7_12 > var10_15 - 13 && var7_12 < var10_15 + 3) {
                                var8_13 = var9_14;
                            }
                            ++var9_14;
lbl51:
                            // 2 sources

                        } while (var9_14 < this.Ig);
                        if (var8_13 != -1) {
                            this.b(var8_13, false);
                        }
                        this.Tb = false;
                        if (this.ed == 1) {
                            this.ch = true;
                        }
                        if (this.ed == 2) {
                            this.ui = true;
                            return;
                        }
                        break block30;
                    }
                    if (var2_2 == 1 && this.Ig > 0 && ((var3_5 = this.Uf[this.Ig - 1]) == 632 || var3_5 == 78 || var3_5 == 867 || var3_5 == 431 || var3_5 == 53 || var3_5 == 74 || var3_5 == 454 || var3_5 == 539 || var3_5 == 493 || var3_5 == 847 || var3_5 == 447 || var3_5 == 1125)) {
                        var4_7 = this.Sf[this.Ig - 1];
                        var5_9 = this.Tf[this.Ig - 1];
                        var6_11 = DUCMKFAY.d[var5_9];
                        if (var6_11.ab || var6_11.C) {
                            this.Ni = false;
                            this.Td = 0;
                            this.Lf = var5_9;
                            this.Mf = var4_7;
                            this.Nf = 2;
                            this.Of = this.A;
                            this.Pf = this.B;
                            if (DUCMKFAY.d[var5_9].D == this.rb) {
                                this.Nf = 1;
                            }
                            if (DUCMKFAY.d[var5_9].D == this.vj) {
                                this.Nf = 3;
                            }
                            return;
                        }
                    }
                    if (var2_2 == 1 && (this.Yi == 1 || this.b(9, this.Ig - 1)) && this.Ig > 2) {
                        var2_2 = 2;
                    }
                    if (var2_2 == 1 && this.Ig > 0) {
                        this.b(this.Ig - 1, false);
                    }
                    if (var2_2 != 2 || this.Ig <= 0) break block28;
                    this.l(true);
                }
                return;
            }
            catch (RuntimeException var2_3) {
                signlink.reporterror("37524, " + var1_1 + ", " + var2_3.toString());
                throw new RuntimeException();
            }
        }
    }

    public final void a(boolean bl, int n, byte[] byArray) {
        try {
            signlink.midifade = bl ? 1 : 0;
            signlink.midisave(byArray, byArray.length);
            if (n != 0) {
                this.me = this.Kf.c();
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("51005, " + bl + ", " + n + ", " + byArray + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(boolean bl) {
        int n = Jj;
        try {
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            try {
                block52: {
                    block51: {
                        int n7;
                        this.Pd = -1;
                        this.jf.c();
                        this.re.c();
                        OPPOFIOL.c(gb);
                        this.b(false);
                        this.cd.b(619);
                        System.gc();
                        int n8 = 0;
                        boolean bl2 = true;
                        do {
                            if (bl2 && !(bl2 = false) && n == 0) continue;
                            this.Bi[n8].a();
                            ++n8;
                        } while (n8 < 4);
                        n6 = 0;
                        boolean bl3 = true;
                        do {
                            if (bl3 && !(bl3 = false) && n == 0) continue;
                            int n9 = 0;
                            boolean bl4 = true;
                            do {
                                if (bl4 && !(bl4 = false) && n == 0) continue;
                                n5 = 0;
                                boolean bl5 = true;
                                do {
                                    if (bl5 && !(bl5 = false) && n == 0) continue;
                                    this.dj[n6][n9][n5] = 0;
                                    ++n5;
                                } while (n5 < 104);
                                ++n9;
                            } while (n9 < 104);
                            ++n6;
                        } while (n6 < 4);
                        CRRWDRTI cRRWDRTI = new CRRWDRTI(this.dj, -60, 104, 104, this.li);
                        n5 = this.Gh.length;
                        this.Ph.a((byte)6, 0);
                        if (!this.ih) {
                            byte[] byArray;
                            n4 = 0;
                            boolean bl6 = true;
                            do {
                                if (bl6 && !(bl6 = false) && n == 0) continue;
                                n3 = (this.Fi[n4] >> 8) * 64 - this.Me;
                                n2 = (this.Fi[n4] & 0xFF) * 64 - this.Ne;
                                byArray = this.Gh[n4];
                                if (byArray != null) {
                                    cRRWDRTI.a(byArray, n2, n3, (this.wf - 6) * 8, (this.xf - 6) * 8, (byte)4, this.Bi);
                                }
                                ++n4;
                            } while (n4 < n5);
                            n3 = 0;
                            boolean bl7 = true;
                            do {
                                if (bl7 && !(bl7 = false) && n == 0) continue;
                                n2 = (this.Fi[n3] >> 8) * 64 - this.Me;
                                int n10 = (this.Fi[n3] & 0xFF) * 64 - this.Ne;
                                byte[] byArray2 = this.Gh[n3];
                                if (byArray2 == null && this.xf < 800) {
                                    cRRWDRTI.a(n10, 64, 0, 64, n2);
                                }
                                ++n3;
                            } while (n3 < n5);
                            if (++Yf > 160) {
                                Yf = 0;
                                this.Ph.a((byte)6, 238);
                                this.Ph.a(96);
                            }
                            this.Ph.a((byte)6, 0);
                            n2 = 0;
                            boolean bl8 = true;
                            do {
                                if (bl8 && !(bl8 = false) && n == 0) continue;
                                byArray = this.Si[n2];
                                if (byArray != null) {
                                    int n11 = (this.Fi[n2] >> 8) * 64 - this.Me;
                                    n7 = (this.Fi[n2] & 0xFF) * 64 - this.Ne;
                                    cRRWDRTI.a(n11, this.Bi, n7, 7, this.cd, byArray);
                                }
                                ++n2;
                            } while (n2 < n5);
                        }
                        if (this.ih) {
                            int n12;
                            int n13;
                            int n14;
                            int n15;
                            n4 = 0;
                            boolean bl9 = true;
                            do {
                                if (bl9 && !(bl9 = false) && n == 0) continue;
                                n3 = 0;
                                boolean bl10 = true;
                                do {
                                    if (bl10 && !(bl10 = false) && n == 0) continue;
                                    n2 = 0;
                                    boolean bl11 = true;
                                    do {
                                        if (bl11 && !(bl11 = false) && n == 0) continue;
                                        int n16 = this.Eg[n4][n3][n2];
                                        if (n16 != -1) {
                                            int n17 = n16 >> 24 & 3;
                                            n7 = n16 >> 1 & 3;
                                            n15 = n16 >> 14 & 0x3FF;
                                            n14 = n16 >> 3 & 0x7FF;
                                            n13 = (n15 / 8 << 8) + n14 / 8;
                                            n12 = 0;
                                            boolean bl12 = true;
                                            do {
                                                if (bl12 && !(bl12 = false) && n == 0) continue;
                                                if (this.Fi[n12] == n13 && this.Gh[n12] != null) {
                                                    cRRWDRTI.a(n17, n7, this.Bi, 9, n3 * 8, (n15 & 7) * 8, this.Gh[n12], (n14 & 7) * 8, n4, n2 * 8);
                                                    if (n == 0) break;
                                                }
                                                ++n12;
                                            } while (n12 < this.Fi.length);
                                        }
                                        ++n2;
                                    } while (n2 < 13);
                                    ++n3;
                                } while (n3 < 13);
                                ++n4;
                            } while (n4 < 4);
                            n3 = 0;
                            boolean bl13 = true;
                            do {
                                if (bl13 && !(bl13 = false) && n == 0) continue;
                                n2 = 0;
                                boolean bl14 = true;
                                do {
                                    if (bl14 && !(bl14 = false) && n == 0) continue;
                                    int n18 = this.Eg[0][n3][n2];
                                    if (n18 == -1) {
                                        cRRWDRTI.a(n2 * 8, 8, 0, 8, n3 * 8);
                                    }
                                    ++n2;
                                } while (n2 < 13);
                                ++n3;
                            } while (n3 < 13);
                            this.Ph.a((byte)6, 0);
                            n2 = 0;
                            boolean bl15 = true;
                            do {
                                if (bl15 && !(bl15 = false) && n == 0) continue;
                                int n19 = 0;
                                boolean bl16 = true;
                                do {
                                    if (bl16 && !(bl16 = false) && n == 0) continue;
                                    int n20 = 0;
                                    boolean bl17 = true;
                                    do {
                                        if (bl17 && !(bl17 = false) && n == 0) continue;
                                        n7 = this.Eg[n2][n19][n20];
                                        if (n7 != -1) {
                                            n15 = n7 >> 24 & 3;
                                            n14 = n7 >> 1 & 3;
                                            n13 = n7 >> 14 & 0x3FF;
                                            n12 = n7 >> 3 & 0x7FF;
                                            int n21 = (n13 / 8 << 8) + n12 / 8;
                                            int n22 = 0;
                                            boolean bl18 = true;
                                            do {
                                                if (bl18 && !(bl18 = false) && n == 0) continue;
                                                if (this.Fi[n22] == n21 && this.Si[n22] != null) {
                                                    cRRWDRTI.a(this.Bi, this.cd, n15, n19 * 8, (n12 & 7) * 8, true, n2, this.Si[n22], (n13 & 7) * 8, n14, n20 * 8);
                                                    if (n == 0) break;
                                                }
                                                ++n22;
                                            } while (n22 < this.Fi.length);
                                        }
                                        ++n20;
                                    } while (n20 < 13);
                                    ++n19;
                                } while (n19 < 13);
                                ++n2;
                            } while (n2 < 4);
                        }
                        this.Ph.a((byte)6, 0);
                        cRRWDRTI.a(this.Bi, this.cd, 2);
                        this.oh.a(0);
                        this.Ph.a((byte)6, 0);
                        n4 = CRRWDRTI.w;
                        if (n4 > this.Ac) {
                            n4 = this.Ac;
                        }
                        if (n4 < this.Ac - 1) {
                            n4 = this.Ac - 1;
                        }
                        if (!qd) break block51;
                        this.cd.a(CRRWDRTI.w, -34686);
                        if (n == 0) break block52;
                    }
                    this.cd.a(0, -34686);
                }
                n3 = 0;
                boolean bl19 = true;
                do {
                    if (bl19 && !(bl19 = false) && n == 0) continue;
                    n2 = 0;
                    boolean bl20 = true;
                    do {
                        if (bl20 && !(bl20 = false) && n == 0) continue;
                        this.c(n3, n2);
                        ++n2;
                    } while (n2 < 104);
                    ++n3;
                } while (n3 < 104);
                if (++df > 98) {
                    df = 0;
                    this.Ph.a((byte)6, 150);
                }
                this.v(-919);
            }
            catch (Exception exception) {
                // empty catch block
            }
            YZDBYLRM.X.a();
            this.gh &= bl;
            if (this.o != null) {
                this.Ph.a((byte)6, 210);
                this.Ph.d(1057001181);
            }
            if (qd && signlink.cache_dat != null) {
                int n23 = this.vf.a(79, 0);
                n6 = 0;
                boolean bl21 = true;
                do {
                    if (bl21 && !(bl21 = false) && n == 0) continue;
                    int n24 = this.vf.c(n6, -203);
                    if ((n24 & 0x79) == 0) {
                        ZKARKDQW.a(116, n6);
                    }
                    ++n6;
                } while (n6 < n23);
            }
            System.gc();
            OPPOFIOL.a(20, true);
            this.vf.d(0);
            int n25 = (this.wf - 6) / 8 - 1;
            n6 = (this.wf + 6) / 8 + 1;
            int n26 = (this.xf - 6) / 8 - 1;
            n5 = (this.xf + 6) / 8 + 1;
            if (this.Qg) {
                n25 = 49;
                n6 = 50;
                n26 = 49;
                n5 = 50;
            }
            n4 = n25;
            boolean bl22 = true;
            do {
                if (bl22 && !(bl22 = false) && n == 0) continue;
                n3 = n26;
                boolean bl23 = true;
                do {
                    if (bl23 && !(bl23 = false) && n == 0) continue;
                    if (n4 == n25 || n4 == n6 || n3 == n26 || n3 == n5) {
                        int n27;
                        n2 = this.vf.a(0, 0, n3, n4);
                        if (n2 != -1) {
                            this.vf.a(n2, 3, false);
                        }
                        if ((n27 = this.vf.a(1, 0, n3, n4)) != -1) {
                            this.vf.a(n27, 3, false);
                        }
                    }
                    ++n3;
                } while (n3 <= n5);
                ++n4;
            } while (n4 <= n6);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("81650, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void b(boolean bl) {
        try {
            YZDBYLRM.X.a();
            YZDBYLRM.S.a();
            CKDEJADD.O.a();
            DJRMEMXO.f.a();
            DJRMEMXO.e.a();
            if (bl) {
                this.me = -1;
            }
            DLZHLHNK.zb.a();
            MUDLUUBC.p.a();
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("63488, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void b(boolean bl, int n) {
        int n2 = Jj;
        try {
            int n3;
            int n4;
            int n5;
            int[] nArray = this.ij.I;
            int n6 = nArray.length;
            int n7 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                nArray[n7] = 0;
                ++n7;
            } while (n7 < n6);
            int n8 = 1;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n2 == 0) continue;
                n5 = 24628 + (103 - n8) * 512 * 4;
                n4 = 1;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && n2 == 0) continue;
                    if ((this.dj[n][n4][n8] & 0x18) == 0) {
                        this.cd.a(nArray, n5, 512, n, n4, n8);
                    }
                    if (n < 3 && (this.dj[n + 1][n4][n8] & 8) != 0) {
                        this.cd.a(nArray, n5, 512, n + 1, n4, n8);
                    }
                    n5 += 4;
                    ++n4;
                } while (n4 < 103);
                ++n8;
            } while (n8 < 103);
            n5 = (238 + (int)(Math.random() * 20.0) - 10 << 16) + (238 + (int)(Math.random() * 20.0) - 10 << 8) + (238 + (int)(Math.random() * 20.0) - 10);
            n4 = 238 + (int)(Math.random() * 20.0) - 10 << 16;
            this.ij.b(0);
            int n9 = 1;
            boolean bl5 = true;
            do {
                if (bl5 && !(bl5 = false) && n2 == 0) continue;
                n3 = 1;
                boolean bl6 = true;
                do {
                    if (bl6 && !(bl6 = false) && n2 == 0) continue;
                    if ((this.dj[n][n3][n9] & 0x18) == 0) {
                        this.b(n9, -960, n5, n3, n4, n);
                    }
                    if (n < 3 && (this.dj[n + 1][n3][n9] & 8) != 0) {
                        this.b(n9, -960, n5, n3, n4, n + 1);
                    }
                    ++n3;
                } while (n3 < 103);
                ++n9;
            } while (n9 < 103);
            this.oh.a(0);
            this.gh &= bl;
            this.yf = 0;
            n3 = 0;
            boolean bl7 = true;
            do {
                if (bl7 && !(bl7 = false) && n2 == 0) continue;
                int n10 = 0;
                boolean bl8 = true;
                do {
                    if (bl8 && !(bl8 = false) && n2 == 0) continue;
                    int n11 = this.cd.e(this.Ac, n3, n10);
                    if (n11 != 0) {
                        n11 = n11 >> 14 & Short.MAX_VALUE;
                        int n12 = YZDBYLRM.a((int)n11).k;
                        if (n12 >= 0) {
                            int n13 = n3;
                            int n14 = n10;
                            if (n12 != 22 && n12 != 29 && n12 != 34 && n12 != 36 && n12 != 46 && n12 != 47 && n12 != 48) {
                                int n15 = 104;
                                int n16 = 104;
                                int[][] nArray2 = this.Bi[this.Ac].m;
                                int n17 = 0;
                                boolean bl9 = true;
                                do {
                                    if (bl9 && !(bl9 = false) && n2 == 0) continue;
                                    int n18 = (int)(Math.random() * 4.0);
                                    if (n18 == 0 && n13 > 0 && n13 > n3 - 3 && (nArray2[n13 - 1][n14] & 0x1280108) == 0) {
                                        --n13;
                                    }
                                    if (n18 == 1 && n13 < n15 - 1 && n13 < n3 + 3 && (nArray2[n13 + 1][n14] & 0x1280180) == 0) {
                                        ++n13;
                                    }
                                    if (n18 == 2 && n14 > 0 && n14 > n10 - 3 && (nArray2[n13][n14 - 1] & 0x1280102) == 0) {
                                        --n14;
                                    }
                                    if (n18 == 3 && n14 < n16 - 1 && n14 < n10 + 3 && (nArray2[n13][n14 + 1] & 0x1280120) == 0) {
                                        ++n14;
                                    }
                                    ++n17;
                                } while (n17 < 10);
                            }
                            this.Pg[this.yf] = this.Le[n12];
                            this.zf[this.yf] = n13;
                            this.Af[this.yf] = n14;
                            ++this.yf;
                        }
                    }
                    ++n10;
                } while (n10 < 104);
                ++n3;
            } while (n3 < 104);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("35531, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void c(int n, int n2) {
        Object object;
        LHGXPZPG lHGXPZPG = this.N[this.Ac][n][n2];
        if (lHGXPZPG == null) {
            this.cd.b(this.Ac, n, n2);
            return;
        }
        int n3 = -99999999;
        HNKCWGJM hNKCWGJM = null;
        HNKCWGJM hNKCWGJM2 = (HNKCWGJM)lHGXPZPG.b();
        while (hNKCWGJM2 != null) {
            object = DJRMEMXO.b(hNKCWGJM2.m);
            int n4 = ((DJRMEMXO)object).b;
            if (((DJRMEMXO)object).w) {
                n4 *= hNKCWGJM2.n + 1;
            }
            if (n4 > n3) {
                n3 = n4;
                hNKCWGJM = hNKCWGJM2;
            }
            hNKCWGJM2 = (HNKCWGJM)lHGXPZPG.a(false);
        }
        lHGXPZPG.a(-493, hNKCWGJM);
        object = null;
        HNKCWGJM hNKCWGJM3 = null;
        hNKCWGJM2 = (HNKCWGJM)lHGXPZPG.b();
        while (hNKCWGJM2 != null) {
            if (hNKCWGJM2.m != hNKCWGJM.m && object == null) {
                object = hNKCWGJM2;
            }
            if (hNKCWGJM2.m != hNKCWGJM.m && hNKCWGJM2.m != ((HNKCWGJM)object).m && hNKCWGJM3 == null) {
                hNKCWGJM3 = hNKCWGJM2;
            }
            hNKCWGJM2 = (HNKCWGJM)lHGXPZPG.a(false);
        }
        int n5 = n + (n2 << 7) + 0x60000000;
        this.cd.a((byte)7, n, n5, (XHHRODPC)object, this.a(this.Ac, n2 * 128 + 64, true, n * 128 + 64), hNKCWGJM3, hNKCWGJM, this.Ac, n2);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void c(boolean bl, int n) {
        try {
            int n2 = 0;
            boolean bl2 = true;
            do {
                block7: {
                    int n3;
                    CWNCPMLX cWNCPMLX;
                    block8: {
                        if (bl2 && !(bl2 = false) && Jj == 0) continue;
                        cWNCPMLX = this.V[this.X[n2]];
                        n3 = 0x20000000 + (this.X[n2] << 14);
                        if (cWNCPMLX == null || !cWNCPMLX.b(vi) || cWNCPMLX.vb.M != bl) break block7;
                        int n4 = cWNCPMLX.kb >> 7;
                        int n5 = cWNCPMLX.lb >> 7;
                        if (n4 < 0 || n4 >= 104 || n5 < 0 || n5 >= 104) break block7;
                        if (cWNCPMLX.ab != 1 || (cWNCPMLX.kb & 0x7F) != 64 || (cWNCPMLX.lb & 0x7F) != 64) break block8;
                        if (this.Lc[n4][n5] == this.kj) break block7;
                        this.Lc[n4][n5] = this.kj;
                    }
                    if (!cWNCPMLX.vb.D) {
                        n3 += Integer.MIN_VALUE;
                    }
                    this.cd.a(this.Ac, cWNCPMLX.mb, (byte)6, this.a(this.Ac, cWNCPMLX.lb, true, cWNCPMLX.kb), n3, cWNCPMLX.lb, (cWNCPMLX.ab - 1) * 64 + 60, cWNCPMLX.kb, cWNCPMLX, cWNCPMLX.bb);
                }
                ++n2;
            } while (n2 < this.W);
            if (n == -30815) return;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("19775, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final boolean l(int n) {
        try {
            if (n != 11456) {
                throw new NullPointerException();
            }
            return signlink.wavereplay();
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("23302, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private final void a(String string) {
        System.out.println(string);
        try {
            this.getAppletContext().showDocument(new URL(this.getCodeBase(), "loaderror_" + string + ".html"));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        while (true) {
            try {
                while (true) {
                    Thread.sleep(1000L);
                }
            }
            catch (Exception exception) {
                continue;
            }
            break;
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, int n2, DUCMKFAY dUCMKFAY, int n3, int n4, int n5, int n6) {
        int n7 = Jj;
        try {
            if (dUCMKFAY.db != 0 || dUCMKFAY.H == null || dUCMKFAY.hb) {
                return;
            }
            if (n3 < n || n5 < n4 || n3 > n + dUCMKFAY.n || n5 > n4 + dUCMKFAY.ib) {
                return;
            }
            int n8 = dUCMKFAY.H.length;
            if (n2 != 13037) {
                this.N = null;
            }
            int n9 = 0;
            boolean bl = true;
            do {
                block48: {
                    DUCMKFAY dUCMKFAY2;
                    int n10;
                    int n11;
                    block47: {
                        block45: {
                            block46: {
                                if (bl && !(bl = false) && n7 == 0) continue;
                                n11 = dUCMKFAY.I[n9] + n;
                                n10 = dUCMKFAY.nb[n9] + n4 - n6;
                                dUCMKFAY2 = DUCMKFAY.d[dUCMKFAY.H[n9]];
                                if (dUCMKFAY2.x < 0 && dUCMKFAY2.j == 0 || n3 < (n11 += dUCMKFAY2.eb) || n5 < (n10 += dUCMKFAY2.gb) || n3 >= n11 + dUCMKFAY2.n || n5 >= n10 + dUCMKFAY2.ib) break block45;
                                if (dUCMKFAY2.x < 0) break block46;
                                this.Ub = dUCMKFAY2.x;
                                if (n7 == 0) break block45;
                            }
                            this.Ub = dUCMKFAY2.R;
                        }
                        if (dUCMKFAY2.db != 0) break block47;
                        this.a(n11, 13037, dUCMKFAY2, n3, n10, n5, dUCMKFAY2.r);
                        if (dUCMKFAY2.cb <= dUCMKFAY2.ib) break block48;
                        this.a(n11 + dUCMKFAY2.n, dUCMKFAY2.ib, n3, n5, dUCMKFAY2, n10, true, dUCMKFAY2.cb, 0);
                        if (n7 == 0) break block48;
                    }
                    if (dUCMKFAY2.k == 1 && n3 >= n11 && n5 >= n10 && n3 < n11 + dUCMKFAY2.n && n5 < n10 + dUCMKFAY2.ib) {
                        boolean bl2 = false;
                        if (dUCMKFAY2.h != 0) {
                            bl2 = this.a(dUCMKFAY2, false);
                        }
                        if (!bl2) {
                            this.Wh[this.Ig] = dUCMKFAY2.o;
                            this.Uf[this.Ig] = 315;
                            this.Tf[this.Ig] = dUCMKFAY2.R;
                            ++this.Ig;
                        }
                    }
                    if (dUCMKFAY2.k == 2 && this.Lg == 0 && n3 >= n11 && n5 >= n10 && n3 < n11 + dUCMKFAY2.n && n5 < n10 + dUCMKFAY2.ib) {
                        String string = dUCMKFAY2.p;
                        if (string.indexOf(" ") != -1) {
                            string = string.substring(0, string.indexOf(" "));
                        }
                        this.Wh[this.Ig] = String.valueOf(string) + " @gre@" + dUCMKFAY2.l;
                        this.Uf[this.Ig] = 626;
                        this.Tf[this.Ig] = dUCMKFAY2.R;
                        ++this.Ig;
                    }
                    if (dUCMKFAY2.k == 3 && n3 >= n11 && n5 >= n10 && n3 < n11 + dUCMKFAY2.n && n5 < n10 + dUCMKFAY2.ib) {
                        this.Wh[this.Ig] = "Close";
                        this.Uf[this.Ig] = 200;
                        this.Tf[this.Ig] = dUCMKFAY2.R;
                        ++this.Ig;
                    }
                    if (dUCMKFAY2.k == 4 && n3 >= n11 && n5 >= n10 && n3 < n11 + dUCMKFAY2.n && n5 < n10 + dUCMKFAY2.ib) {
                        this.Wh[this.Ig] = dUCMKFAY2.o;
                        this.Uf[this.Ig] = 169;
                        this.Tf[this.Ig] = dUCMKFAY2.R;
                        ++this.Ig;
                    }
                    if (dUCMKFAY2.k == 5 && n3 >= n11 && n5 >= n10 && n3 < n11 + dUCMKFAY2.n && n5 < n10 + dUCMKFAY2.ib) {
                        this.Wh[this.Ig] = dUCMKFAY2.o;
                        this.Uf[this.Ig] = 646;
                        this.Tf[this.Ig] = dUCMKFAY2.R;
                        ++this.Ig;
                    }
                    if (dUCMKFAY2.k == 6 && !this.Yg && n3 >= n11 && n5 >= n10 && n3 < n11 + dUCMKFAY2.n && n5 < n10 + dUCMKFAY2.ib) {
                        this.Wh[this.Ig] = dUCMKFAY2.o;
                        this.Uf[this.Ig] = 679;
                        this.Tf[this.Ig] = dUCMKFAY2.R;
                        ++this.Ig;
                    }
                    if (dUCMKFAY2.db != 2) break block48;
                    int n12 = 0;
                    int n13 = 0;
                    boolean bl3 = true;
                    do {
                        if (bl3 && !(bl3 = false) && n7 == 0) continue;
                        int n14 = 0;
                        boolean bl4 = true;
                        do {
                            block49: {
                                int n15;
                                DJRMEMXO dJRMEMXO;
                                block52: {
                                    block51: {
                                        block50: {
                                            if (bl4 && !(bl4 = false) && n7 == 0) continue;
                                            int n16 = n11 + n14 * (32 + dUCMKFAY2.y);
                                            int n17 = n10 + n13 * (32 + dUCMKFAY2.L);
                                            if (n12 < 20) {
                                                n16 += dUCMKFAY2.i[n12];
                                                n17 += dUCMKFAY2.O[n12];
                                            }
                                            if (n3 < n16 || n5 < n17 || n3 >= n16 + 32 || n5 >= n17 + 32) break block49;
                                            this.tf = n12;
                                            this.uf = dUCMKFAY2.R;
                                            if (dUCMKFAY2.U[n12] <= 0) break block49;
                                            dJRMEMXO = DJRMEMXO.b(dUCMKFAY2.U[n12] - 1);
                                            if (this.Bj != 1 || !dUCMKFAY2.Q) break block50;
                                            if (dUCMKFAY2.R == this.Dj && n12 == this.Cj) break block49;
                                            this.Wh[this.Ig] = "Use " + this.Fj + " with @lre@" + dJRMEMXO.q;
                                            this.Uf[this.Ig] = 870;
                                            this.Vf[this.Ig] = dJRMEMXO.d;
                                            this.Sf[this.Ig] = n12;
                                            this.Tf[this.Ig] = dUCMKFAY2.R;
                                            ++this.Ig;
                                            if (n7 == 0) break block49;
                                        }
                                        if (this.Lg != 1 || !dUCMKFAY2.Q) break block51;
                                        if ((this.Ng & 0x10) != 16) break block49;
                                        this.Wh[this.Ig] = String.valueOf(this.Og) + " @lre@" + dJRMEMXO.q;
                                        this.Uf[this.Ig] = 543;
                                        this.Vf[this.Ig] = dJRMEMXO.d;
                                        this.Sf[this.Ig] = n12;
                                        this.Tf[this.Ig] = dUCMKFAY2.R;
                                        ++this.Ig;
                                        if (n7 == 0) break block49;
                                    }
                                    if (!dUCMKFAY2.Q) break block52;
                                    n15 = 4;
                                    boolean bl5 = true;
                                    do {
                                        block54: {
                                            block53: {
                                                if (bl5 && !(bl5 = false) && n7 == 0) continue;
                                                if (dJRMEMXO.J == null || dJRMEMXO.J[n15] == null) break block53;
                                                this.Wh[this.Ig] = String.valueOf(dJRMEMXO.J[n15]) + " @lre@" + dJRMEMXO.q;
                                                if (n15 == 3) {
                                                    this.Uf[this.Ig] = 493;
                                                }
                                                if (n15 == 4) {
                                                    this.Uf[this.Ig] = 847;
                                                }
                                                this.Vf[this.Ig] = dJRMEMXO.d;
                                                this.Sf[this.Ig] = n12;
                                                this.Tf[this.Ig] = dUCMKFAY2.R;
                                                ++this.Ig;
                                                if (n7 == 0) break block54;
                                            }
                                            if (n15 == 4) {
                                                this.Wh[this.Ig] = "Drop @lre@" + dJRMEMXO.q;
                                                this.Uf[this.Ig] = 847;
                                                this.Vf[this.Ig] = dJRMEMXO.d;
                                                this.Sf[this.Ig] = n12;
                                                this.Tf[this.Ig] = dUCMKFAY2.R;
                                                ++this.Ig;
                                            }
                                        }
                                        --n15;
                                    } while (n15 >= 3);
                                }
                                if (dUCMKFAY2.J) {
                                    this.Wh[this.Ig] = "Use @lre@" + dJRMEMXO.q;
                                    this.Uf[this.Ig] = 447;
                                    this.Vf[this.Ig] = dJRMEMXO.d;
                                    this.Sf[this.Ig] = n12;
                                    this.Tf[this.Ig] = dUCMKFAY2.R;
                                    ++this.Ig;
                                }
                                if (dUCMKFAY2.Q && dJRMEMXO.J != null) {
                                    n15 = 2;
                                    boolean bl6 = true;
                                    do {
                                        if (bl6 && !(bl6 = false) && n7 == 0) continue;
                                        if (dJRMEMXO.J[n15] != null) {
                                            this.Wh[this.Ig] = String.valueOf(dJRMEMXO.J[n15]) + " @lre@" + dJRMEMXO.q;
                                            if (n15 == 0) {
                                                this.Uf[this.Ig] = 74;
                                            }
                                            if (n15 == 1) {
                                                this.Uf[this.Ig] = 454;
                                            }
                                            if (n15 == 2) {
                                                this.Uf[this.Ig] = 539;
                                            }
                                            this.Vf[this.Ig] = dJRMEMXO.d;
                                            this.Sf[this.Ig] = n12;
                                            this.Tf[this.Ig] = dUCMKFAY2.R;
                                            ++this.Ig;
                                        }
                                        --n15;
                                    } while (n15 >= 0);
                                }
                                if (dUCMKFAY2.s != null) {
                                    n15 = 4;
                                    boolean bl7 = true;
                                    do {
                                        if (bl7 && !(bl7 = false) && n7 == 0) continue;
                                        if (dUCMKFAY2.s[n15] != null) {
                                            this.Wh[this.Ig] = String.valueOf(dUCMKFAY2.s[n15]) + " @lre@" + dJRMEMXO.q;
                                            if (n15 == 0) {
                                                this.Uf[this.Ig] = 632;
                                            }
                                            if (n15 == 1) {
                                                this.Uf[this.Ig] = 78;
                                            }
                                            if (n15 == 2) {
                                                this.Uf[this.Ig] = 867;
                                            }
                                            if (n15 == 3) {
                                                this.Uf[this.Ig] = 431;
                                            }
                                            if (n15 == 4) {
                                                this.Uf[this.Ig] = 53;
                                            }
                                            this.Vf[this.Ig] = dJRMEMXO.d;
                                            this.Sf[this.Ig] = n12;
                                            this.Tf[this.Ig] = dUCMKFAY2.R;
                                            ++this.Ig;
                                        }
                                        --n15;
                                    } while (n15 >= 0);
                                }
                                this.Wh[this.Ig] = "Examine @lre@" + dJRMEMXO.q;
                                this.Uf[this.Ig] = 1125;
                                this.Vf[this.Ig] = dJRMEMXO.d;
                                this.Sf[this.Ig] = n12;
                                this.Tf[this.Ig] = dUCMKFAY2.R;
                                ++this.Ig;
                            }
                            ++n12;
                            ++n14;
                        } while (n14 < dUCMKFAY2.n);
                        ++n13;
                    } while (n13 < dUCMKFAY2.ib);
                }
                ++n9;
            } while (n9 < n8);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("38948, " + n + ", " + n2 + ", " + dUCMKFAY + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(int n, int n2, int n3, int n4, int n5, int n6) {
        try {
            this.Ce.a(n5, 16083, n4);
            this.De.a(n5, 16083, n4 + n2 - 16);
            AFCKELYG.a(n2 - 32, n4 + 16, n5, this.ge, 16, 0);
            int n7 = (n2 - 32) * n2 / n6;
            if (n7 < 8) {
                n7 = 8;
            }
            int n8 = (n2 - 32 - n7) * n3 / (n6 - n2);
            AFCKELYG.a(n7, n4 + 16 + n8, n5, this.qf, 16, 0);
            AFCKELYG.a(n4 + 16 + n8, this.kc, n7, n5, this.Kg);
            AFCKELYG.a(n4 + 16 + n8, this.kc, n7, n5 + 1, this.Kg);
            AFCKELYG.a(n4 + 16 + n8, this.kc, 16, n5, (byte)4);
            AFCKELYG.a(n4 + 17 + n8, this.kc, 16, n5, (byte)4);
            if (n <= 0) {
                this.gg = this.ee.a();
            }
            AFCKELYG.a(n4 + 16 + n8, this.Jc, n7, n5 + 15, this.Kg);
            AFCKELYG.a(n4 + 17 + n8, this.Jc, n7 - 1, n5 + 14, this.Kg);
            AFCKELYG.a(n4 + 15 + n8 + n7, this.Jc, 16, n5, (byte)4);
            AFCKELYG.a(n4 + 14 + n8 + n7, this.Jc, 15, n5 + 1, (byte)4);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("99728, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void a(MBMGIXGO mBMGIXGO, int n, int n2) {
        int n3 = Jj;
        try {
            int n4;
            this.Z = 0;
            this.bc = 0;
            if (n2 <= 0) {
                this.Lb = this.ee.a();
            }
            this.b(mBMGIXGO, -45, n);
            this.a(n, mBMGIXGO, (byte)2);
            this.a(n, mBMGIXGO, true);
            int n5 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n3 == 0) continue;
                n4 = this.ab[n5];
                if (this.V[n4].X != kh) {
                    this.V[n4].vb = null;
                    this.V[n4] = null;
                }
                ++n5;
            } while (n5 < this.Z);
            if (mBMGIXGO.z != n) {
                signlink.reporterror(String.valueOf(this.wh) + " size mismatch in getnpcpos - pos:" + mBMGIXGO.z + " psize:" + n);
                throw new RuntimeException("eek");
            }
            n4 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n3 == 0) continue;
                if (this.V[this.X[n4]] == null) {
                    signlink.reporterror(String.valueOf(this.wh) + " null entry in npc list - pos:" + n4 + " size:" + this.W);
                    throw new RuntimeException("eek");
                }
                ++n4;
            } while (n4 < this.W);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("78640, " + mBMGIXGO + ", " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void c(boolean bl) {
        int n = Jj;
        try {
            int n2;
            block13: {
                block14: {
                    this.gh &= bl;
                    if (this.z != 1) return;
                    if (this.A >= 6 && this.A <= 106 && this.B >= 467 && this.B <= 499) {
                        this.Gj = (this.Gj + 1) % 4;
                        this.Ei = true;
                        this.ui = true;
                        this.Ph.a((byte)6, 95);
                        this.Ph.a(this.Gj);
                        this.Ph.a(this.fb);
                        this.Ph.a(this.Ti);
                    }
                    if (this.A >= 135 && this.A <= 235 && this.B >= 467 && this.B <= 499) {
                        this.fb = (this.fb + 1) % 3;
                        this.Ei = true;
                        this.ui = true;
                        this.Ph.a((byte)6, 95);
                        this.Ph.a(this.Gj);
                        this.Ph.a(this.fb);
                        this.Ph.a(this.Ti);
                    }
                    if (this.A >= 273 && this.A <= 373 && this.B >= 467 && this.B <= 499) {
                        this.Ti = (this.Ti + 1) % 3;
                        this.Ei = true;
                        this.ui = true;
                        this.Ph.a((byte)6, 95);
                        this.Ph.a(this.Gj);
                        this.Ph.a(this.fb);
                        this.Ph.a(this.Ti);
                    }
                    if (this.A < 412 || this.A > 512 || this.B < 467 || this.B > 499) break block13;
                    if (this.rb != -1) break block14;
                    this.M(537);
                    this.Pb = "";
                    this.hh = false;
                    n2 = 0;
                    boolean bl2 = true;
                    do {
                        if (bl2 && !(bl2 = false) && n == 0) continue;
                        if (DUCMKFAY.d[n2] != null && DUCMKFAY.d[n2].h == 600) {
                            this.Bh = this.rb = DUCMKFAY.d[n2].D;
                            if (n == 0) break block13;
                        }
                        ++n2;
                    } while (n2 < DUCMKFAY.d.length);
                    if (n == 0) break block13;
                }
                this.a("Please close the interface you have open before using 'report abuse'", 0, "", this.Vd);
            }
            if (++Wc <= 1386) return;
            Wc = 0;
            this.Ph.a((byte)6, 165);
            this.Ph.a(0);
            n2 = this.Ph.z;
            this.Ph.a(139);
            this.Ph.a(150);
            this.Ph.b(32131);
            this.Ph.a((int)(Math.random() * 256.0));
            this.Ph.b(3250);
            this.Ph.a(177);
            this.Ph.b(24859);
            this.Ph.a(119);
            if ((int)(Math.random() * 2.0) == 0) {
                this.Ph.b(47234);
            }
            if ((int)(Math.random() * 2.0) == 0) {
                this.Ph.a(21);
            }
            this.Ph.a(this.Ph.z - n2, (byte)0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("30699, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void d(boolean bl, int n) {
        try {
            int n2;
            int n3;
            block28: {
                block30: {
                    block29: {
                        n3 = VGXVBFVC.d[n].l;
                        if (n3 == 0) {
                            return;
                        }
                        n2 = this.Bd[n];
                        if (bl) {
                            this.rd = this.ee.a();
                        }
                        if (n3 == 1) {
                            if (n2 == 1) {
                                OPPOFIOL.a(0.9, Xh);
                            }
                            if (n2 == 2) {
                                OPPOFIOL.a(0.8, Xh);
                            }
                            if (n2 == 3) {
                                OPPOFIOL.a(0.7, Xh);
                            }
                            if (n2 == 4) {
                                OPPOFIOL.a(0.6, Xh);
                            }
                            DJRMEMXO.e.a();
                            this.aj = true;
                        }
                        if (n3 != 3) break block28;
                        boolean bl2 = this.ah;
                        if (n2 == 0) {
                            this.a((byte)0, this.ah, 0);
                            this.ah = true;
                        }
                        if (n2 == 1) {
                            this.a((byte)0, this.ah, -400);
                            this.ah = true;
                        }
                        if (n2 == 2) {
                            this.a((byte)0, this.ah, -800);
                            this.ah = true;
                        }
                        if (n2 == 3) {
                            this.a((byte)0, this.ah, -1200);
                            this.ah = true;
                        }
                        if (n2 == 4) {
                            this.ah = false;
                        }
                        if (this.ah == bl2 || qd) break block28;
                        if (!this.ah) break block29;
                        this.yi = this.md;
                        this.zi = true;
                        this.vf.b(2, this.yi);
                        if (Jj == 0) break block30;
                    }
                    this.g(860);
                }
                this.ej = 0;
            }
            if (n3 == 4) {
                if (n2 == 0) {
                    this.ib = true;
                    this.a((byte)2, 0);
                }
                if (n2 == 1) {
                    this.ib = true;
                    this.a((byte)2, -400);
                }
                if (n2 == 2) {
                    this.ib = true;
                    this.a((byte)2, -800);
                }
                if (n2 == 3) {
                    this.ib = true;
                    this.a((byte)2, -1200);
                }
                if (n2 == 4) {
                    this.ib = false;
                }
            }
            if (n3 == 5) {
                this.Yi = n2;
            }
            if (n3 == 6) {
                this.Ui = n2;
            }
            if (n3 == 8) {
                this.Sh = n2;
                this.ui = true;
            }
            if (n3 == 9) {
                this.vc = n2;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("64621, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void m(int n) {
        int n2 = Jj;
        try {
            void var3_10;
            int n3;
            this.Ed = 0;
            int n4 = -1;
            boolean bl = true;
            do {
                block75: {
                    void var3_5;
                    block78: {
                        block77: {
                            block76: {
                                block73: {
                                    block74: {
                                        block72: {
                                            if (bl && !(bl = false) && n2 == 0) continue;
                                            if (n4 != -1) break block72;
                                            DLZHLHNK dLZHLHNK = Bg;
                                            if (n2 == 0) break block73;
                                        }
                                        if (n4 >= this.Zb) break block74;
                                        DLZHLHNK dLZHLHNK = this.Yb[this.ac[n4]];
                                        if (n2 == 0) break block73;
                                    }
                                    CWNCPMLX cWNCPMLX = this.V[this.X[n4 - this.Zb]];
                                }
                                if (var3_5 == null || !var3_5.b(vi)) break block75;
                                if (!(var3_5 instanceof CWNCPMLX)) break block76;
                                CKDEJADD cKDEJADD = ((CWNCPMLX)var3_5).vb;
                                if (cKDEJADD.H != null) {
                                    cKDEJADD = cKDEJADD.b(this.Lb);
                                }
                                if (cKDEJADD == null) break block75;
                            }
                            if (n4 >= this.Zb) break block77;
                            int n5 = 30;
                            DLZHLHNK dLZHLHNK = (DLZHLHNK)var3_5;
                            if (dLZHLHNK.Bb != 0) {
                                this.a(true, (GQOSZKJC)var3_5, var3_5.t + 15);
                                if (this.td > -1) {
                                    n3 = 0;
                                    boolean bl2 = true;
                                    do {
                                        if (bl2 && !(bl2 = false) && n2 == 0) continue;
                                        if ((dLZHLHNK.Bb & 1 << n3) != 0) {
                                            this.Wf[n3].b(this.td - 12, 16083, this.ud - n5);
                                            n5 -= 25;
                                        }
                                        ++n3;
                                    } while (n3 < 8);
                                }
                            }
                            if (n4 < 0 || this.pb != 10 || this.Pc != this.ac[n4]) break block78;
                            this.a(true, (GQOSZKJC)var3_5, var3_5.t + 15);
                            if (this.td <= -1) break block78;
                            this.Wf[7].b(this.td - 12, 16083, this.ud - n5);
                            if (n2 == 0) break block78;
                        }
                        CKDEJADD cKDEJADD = ((CWNCPMLX)var3_5).vb;
                        if (cKDEJADD.u >= 0 && cKDEJADD.u < this.Wf.length) {
                            this.a(true, (GQOSZKJC)var3_5, var3_5.t + 15);
                            if (this.td > -1) {
                                this.Wf[cKDEJADD.u].b(this.td - 12, 16083, this.ud - 30);
                            }
                        }
                        if (this.pb == 1 && this.ti == this.X[n4 - this.Zb] && kh % 20 < 10) {
                            this.a(true, (GQOSZKJC)var3_5, var3_5.t + 15);
                            if (this.td > -1) {
                                this.Wf[2].b(this.td - 12, 16083, this.ud - 28);
                            }
                        }
                    }
                    if (var3_5.s != null && (n4 >= this.Zb || this.Gj == 0 || this.Gj == 3 || this.Gj == 1 && this.a(false, ((DLZHLHNK)var3_5).yb))) {
                        this.a(true, (GQOSZKJC)var3_5, var3_5.t);
                        if (this.td > -1 && this.Ed < this.Fd) {
                            this.Jd[this.Ed] = this.rj.a(var3_5.s, true) / 2;
                            this.Id[this.Ed] = this.rj.K;
                            this.Gd[this.Ed] = this.td;
                            this.Hd[this.Ed] = this.ud;
                            this.Kd[this.Ed] = var3_5.z;
                            this.Ld[this.Ed] = var3_5.R;
                            this.Md[this.Ed] = var3_5.V;
                            this.Nd[this.Ed++] = var3_5.s;
                            if (this.Ui == 0 && var3_5.R >= 1 && var3_5.R <= 3) {
                                int n6 = this.Ed;
                                this.Id[n6] = this.Id[n6] + 10;
                                int n7 = this.Ed;
                                this.Hd[n7] = this.Hd[n7] + 5;
                            }
                            if (this.Ui == 0 && var3_5.R == 4) {
                                this.Jd[this.Ed] = 60;
                            }
                            if (this.Ui == 0 && var3_5.R == 5) {
                                int n8 = this.Ed;
                                this.Id[n8] = this.Id[n8] + 5;
                            }
                        }
                    }
                    if (var3_5.S > kh) {
                        this.a(true, (GQOSZKJC)var3_5, var3_5.t + 15);
                        if (this.td > -1) {
                            int n9 = var3_5.T * 30 / var3_5.U;
                            if (n9 > 30) {
                                n9 = 30;
                            }
                            AFCKELYG.a(5, this.ud - 3, this.td - 15, 65280, n9, 0);
                            AFCKELYG.a(5, this.ud - 3, this.td - 15 + n9, 0xFF0000, 30 - n9, 0);
                        }
                    }
                    int n10 = 0;
                    boolean bl3 = true;
                    do {
                        if (bl3 && !(bl3 = false) && n2 == 0) continue;
                        if (var3_5.C[n10] > kh) {
                            this.a(true, (GQOSZKJC)var3_5, var3_5.t / 2);
                            if (this.td > -1) {
                                if (n10 == 1) {
                                    this.ud -= 20;
                                }
                                if (n10 == 2) {
                                    this.td -= 15;
                                    this.ud -= 10;
                                }
                                if (n10 == 3) {
                                    this.td += 15;
                                    this.ud -= 10;
                                }
                                this.Rd[var3_5.B[n10]].b(this.td - 12, 16083, this.ud - 12);
                                this.pj.a(0, String.valueOf(var3_5.A[n10]), 23693, this.ud + 4, this.td);
                                this.pj.a(0xFFFFFF, String.valueOf(var3_5.A[n10]), 23693, this.ud + 3, this.td - 1);
                            }
                        }
                        ++n10;
                    } while (n10 < 4);
                }
                ++n4;
            } while (n4 < this.Zb + this.W);
            if (n != 0) {
                this.a();
            }
            boolean bl4 = false;
            boolean bl5 = true;
            do {
                block89: {
                    String string;
                    block79: {
                        int n11;
                        int n12;
                        block91: {
                            int n13;
                            block90: {
                                block86: {
                                    block88: {
                                        block87: {
                                            block83: {
                                                block85: {
                                                    block84: {
                                                        block80: {
                                                            block82: {
                                                                block81: {
                                                                    int n14;
                                                                    if (bl5 && !(bl5 = false) && n2 == 0) continue;
                                                                    int n15 = this.Gd[var3_10];
                                                                    int n16 = this.Hd[var3_10];
                                                                    n3 = this.Jd[var3_10];
                                                                    int n17 = this.Id[var3_10];
                                                                    boolean bl6 = true;
                                                                    boolean bl7 = true;
                                                                    do {
                                                                        if (bl7 && !(bl7 = false) && n2 == 0) continue;
                                                                        bl6 = false;
                                                                        int n18 = 0;
                                                                        boolean bl8 = true;
                                                                        do {
                                                                            if (bl8 && !(bl8 = false) && n2 == 0) continue;
                                                                            if (n14 + 2 > this.Hd[n18] - this.Id[n18] && n14 - n17 < this.Hd[n18] + 2 && n15 - n3 < this.Gd[n18] + this.Jd[n18] && n15 + n3 > this.Gd[n18] - this.Jd[n18] && this.Hd[n18] - this.Id[n18] < n14) {
                                                                                n14 = this.Hd[n18] - this.Id[n18];
                                                                                bl6 = true;
                                                                            }
                                                                            ++n18;
                                                                        } while (n18 < var3_10);
                                                                    } while (bl6);
                                                                    this.td = this.Gd[var3_10];
                                                                    this.ud = this.Hd[var3_10] = n14;
                                                                    string = this.Nd[var3_10];
                                                                    if (this.Ui != 0) break block79;
                                                                    n12 = 0xFFFF00;
                                                                    if (this.Kd[var3_10] < 6) {
                                                                        n12 = this.vd[this.Kd[var3_10]];
                                                                    }
                                                                    if (this.Kd[var3_10] == 6) {
                                                                        int n19 = n12 = this.kj % 20 < 10 ? 0xFF0000 : 0xFFFF00;
                                                                    }
                                                                    if (this.Kd[var3_10] == 7) {
                                                                        int n20 = n12 = this.kj % 20 < 10 ? 255 : 65535;
                                                                    }
                                                                    if (this.Kd[var3_10] == 8) {
                                                                        int n21 = n12 = this.kj % 20 < 10 ? 45056 : 0x80FF80;
                                                                    }
                                                                    if (this.Kd[var3_10] != 9) break block80;
                                                                    n13 = 150 - this.Md[var3_10];
                                                                    if (n13 >= 50) break block81;
                                                                    n12 = 0xFF0000 + 1280 * n13;
                                                                    if (n2 == 0) break block80;
                                                                }
                                                                if (n13 >= 100) break block82;
                                                                n12 = 0xFFFF00 - 327680 * (n13 - 50);
                                                                if (n2 == 0) break block80;
                                                            }
                                                            if (n13 < 150) {
                                                                n12 = 65280 + 5 * (n13 - 100);
                                                            }
                                                        }
                                                        if (this.Kd[var3_10] != 10) break block83;
                                                        n13 = 150 - this.Md[var3_10];
                                                        if (n13 >= 50) break block84;
                                                        n12 = 0xFF0000 + 5 * n13;
                                                        if (n2 == 0) break block83;
                                                    }
                                                    if (n13 >= 100) break block85;
                                                    n12 = 0xFF00FF - 327680 * (n13 - 50);
                                                    if (n2 == 0) break block83;
                                                }
                                                if (n13 < 150) {
                                                    n12 = 255 + 327680 * (n13 - 100) - 5 * (n13 - 100);
                                                }
                                            }
                                            if (this.Kd[var3_10] != 11) break block86;
                                            n13 = 150 - this.Md[var3_10];
                                            if (n13 >= 50) break block87;
                                            n12 = 0xFFFFFF - 327685 * n13;
                                            if (n2 == 0) break block86;
                                        }
                                        if (n13 >= 100) break block88;
                                        n12 = 65280 + 327685 * (n13 - 50);
                                        if (n2 == 0) break block86;
                                    }
                                    if (n13 < 150) {
                                        n12 = 0xFFFFFF - 327680 * (n13 - 100);
                                    }
                                }
                                if (this.Ld[var3_10] == 0) {
                                    this.rj.a(0, string, 23693, this.ud + 1, this.td);
                                    this.rj.a(n12, string, 23693, this.ud, this.td);
                                }
                                if (this.Ld[var3_10] == 1) {
                                    this.rj.a(0, true, string, this.td, this.kj, this.ud + 1);
                                    this.rj.a(n12, true, string, this.td, this.kj, this.ud);
                                }
                                if (this.Ld[var3_10] == 2) {
                                    this.rj.a(this.td, string, this.kj, this.ud + 1, this.Rh, 0);
                                    this.rj.a(this.td, string, this.kj, this.ud, this.Rh, n12);
                                }
                                if (this.Ld[var3_10] == 3) {
                                    this.rj.a(150 - this.Md[var3_10], string, true, this.kj, this.ud + 1, this.td, 0);
                                    this.rj.a(150 - this.Md[var3_10], string, true, this.kj, this.ud, this.td, n12);
                                }
                                if (this.Ld[var3_10] == 4) {
                                    n13 = this.rj.a(string, true);
                                    n11 = (150 - this.Md[var3_10]) * (n13 + 100) / 150;
                                    AFCKELYG.a(334, this.td - 50, false, this.td + 50, 0);
                                    this.rj.b(0, string, this.ud + 1, 822, this.td + 50 - n11);
                                    this.rj.b(n12, string, this.ud, 822, this.td + 50 - n11);
                                    AFCKELYG.a(4);
                                }
                                if (this.Ld[var3_10] != 5) break block89;
                                n13 = 150 - this.Md[var3_10];
                                n11 = 0;
                                if (n13 >= 25) break block90;
                                n11 = n13 - 25;
                                if (n2 == 0) break block91;
                            }
                            if (n13 > 125) {
                                n11 = n13 - 125;
                            }
                        }
                        AFCKELYG.a(this.ud + 5, 0, false, 512, this.ud - this.rj.K - 1);
                        this.rj.a(0, string, 23693, this.ud + 1 + n11, this.td);
                        this.rj.a(n12, string, 23693, this.ud + n11, this.td);
                        AFCKELYG.a(4);
                        if (n2 == 0) break block89;
                    }
                    this.rj.a(0, string, 23693, this.ud + 1, this.td);
                    this.rj.a(0xFFFF00, string, 23693, this.ud, this.td);
                }
                ++var3_10;
            } while (var3_10 < this.Ed);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("52196, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(boolean bl, long l) {
        int n = Jj;
        try {
            if (l == 0L) {
                return;
            }
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n == 0) continue;
                if (this.ld[n2] == l) {
                    --this.hc;
                    this.ch = true;
                    int n3 = n2;
                    boolean bl3 = true;
                    do {
                        if (bl3 && !(bl3 = false) && n == 0) continue;
                        this.Jf[n3] = this.Jf[n3 + 1];
                        this.M[n3] = this.M[n3 + 1];
                        this.ld[n3] = this.ld[n3 + 1];
                        ++n3;
                    } while (n3 < this.hc);
                    this.Ph.a((byte)6, 215);
                    this.Ph.a(5, l);
                    if (n == 0) break;
                }
                ++n2;
            } while (n2 < this.hc);
            if (!bl) return;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("18622, " + bl + ", " + l + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void b(byte by) {
        try {
            block9: {
                block8: {
                    this.mh.a(0);
                    OPPOFIOL.L = this.Eh;
                    if (by != -81) {
                        return;
                    }
                    this.Th.a(0, 16083, 0);
                    if (this.Mh == -1) break block8;
                    this.a(8, 0, 0, DUCMKFAY.d[this.Mh], 0);
                    if (Jj == 0) break block9;
                }
                if (this.Fg[this.si] != -1) {
                    this.a(8, 0, 0, DUCMKFAY.d[this.Fg[this.si]], 0);
                }
            }
            if (this.Tb && this.ed == 1) {
                this.e((byte)9);
            }
            this.mh.a(205, 23680, this.l, 553);
            this.oh.a(0);
            OPPOFIOL.L = this.Fh;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("56062, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void d(int n, int n2) {
        int n3 = Jj;
        try {
            int n4;
            byte[] byArray;
            byte[] byArray2;
            int n5;
            int n6;
            DSMJIEPN dSMJIEPN;
            if (n <= 0) {
                this.me = -1;
            }
            if (qd) return;
            if (OPPOFIOL.T[17] >= n2) {
                dSMJIEPN = OPPOFIOL.N[17];
                n6 = dSMJIEPN.D * dSMJIEPN.E - 1;
                n5 = dSMJIEPN.D * this.bd * 2;
                byArray2 = dSMJIEPN.B;
                byArray = this.uc;
                n4 = 0;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && n3 == 0) continue;
                    byArray[n4] = byArray2[n4 - n5 & n6];
                    ++n4;
                } while (n4 <= n6);
                dSMJIEPN.B = byArray;
                this.uc = byArray2;
                OPPOFIOL.b(17, -477);
                if (++ob > 1235) {
                    ob = 0;
                    this.Ph.a((byte)6, 226);
                    this.Ph.a(0);
                    int n7 = this.Ph.z;
                    this.Ph.b(58722);
                    this.Ph.a(240);
                    this.Ph.b((int)(Math.random() * 65536.0));
                    this.Ph.a((int)(Math.random() * 256.0));
                    if ((int)(Math.random() * 2.0) == 0) {
                        this.Ph.b(51825);
                    }
                    this.Ph.a((int)(Math.random() * 256.0));
                    this.Ph.b((int)(Math.random() * 65536.0));
                    this.Ph.b(7130);
                    this.Ph.b((int)(Math.random() * 65536.0));
                    this.Ph.b(61657);
                    this.Ph.a(this.Ph.z - n7, (byte)0);
                }
            }
            if (OPPOFIOL.T[24] >= n2) {
                dSMJIEPN = OPPOFIOL.N[24];
                n6 = dSMJIEPN.D * dSMJIEPN.E - 1;
                n5 = dSMJIEPN.D * this.bd * 2;
                byArray2 = dSMJIEPN.B;
                byArray = this.uc;
                n4 = 0;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && n3 == 0) continue;
                    byArray[n4] = byArray2[n4 - n5 & n6];
                    ++n4;
                } while (n4 <= n6);
                dSMJIEPN.B = byArray;
                this.uc = byArray2;
                OPPOFIOL.b(24, -477);
            }
            if (OPPOFIOL.T[34] < n2) return;
            dSMJIEPN = OPPOFIOL.N[34];
            n6 = dSMJIEPN.D * dSMJIEPN.E - 1;
            n5 = dSMJIEPN.D * this.bd * 2;
            byArray2 = dSMJIEPN.B;
            byArray = this.uc;
            n4 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n3 == 0) continue;
                byArray[n4] = byArray2[n4 - n5 & n6];
                ++n4;
            } while (n4 <= n6);
            dSMJIEPN.B = byArray;
            this.uc = byArray2;
            OPPOFIOL.b(34, -477);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("86342, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void c(byte by) {
        int n = Jj;
        try {
            int n2;
            if (by != -92) {
                this.Ph.a(214);
            }
            int n3 = -1;
            boolean bl = true;
            do {
                DLZHLHNK dLZHLHNK;
                block13: {
                    block12: {
                        if (bl && !(bl = false) && n == 0) continue;
                        if (n3 != -1) break block12;
                        n2 = this.Xb;
                        if (n == 0) break block13;
                    }
                    n2 = this.ac[n3];
                }
                if ((dLZHLHNK = this.Yb[n2]) != null && dLZHLHNK.V > 0) {
                    --dLZHLHNK.V;
                    if (dLZHLHNK.V == 0) {
                        dLZHLHNK.s = null;
                    }
                }
                ++n3;
            } while (n3 < this.Zb);
            n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n == 0) continue;
                int n4 = this.X[n2];
                CWNCPMLX cWNCPMLX = this.V[n4];
                if (cWNCPMLX != null && cWNCPMLX.V > 0) {
                    --cWNCPMLX.V;
                    if (cWNCPMLX.V == 0) {
                        cWNCPMLX.s = null;
                    }
                }
                ++n2;
            } while (n2 < this.W);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("18071, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void d(byte by) {
        try {
            int n;
            int n2;
            int n3;
            int n4;
            int n5;
            block32: {
                block31: {
                    int n6 = this.Zf * 128 + 64;
                    int n7 = this.ag * 128 + 64;
                    int n8 = this.a(this.Ac, n7, true, n6) - this.bg;
                    if (this.sb < n6) {
                        this.sb += this.cg + (n6 - this.sb) * this.dg / 1000;
                        if (this.sb > n6) {
                            this.sb = n6;
                        }
                    }
                    if (this.sb > n6) {
                        this.sb -= this.cg + (this.sb - n6) * this.dg / 1000;
                        if (this.sb < n6) {
                            this.sb = n6;
                        }
                    }
                    if (this.tb < n8) {
                        this.tb += this.cg + (n8 - this.tb) * this.dg / 1000;
                        if (this.tb > n8) {
                            this.tb = n8;
                        }
                    }
                    if (this.tb > n8) {
                        this.tb -= this.cg + (this.tb - n8) * this.dg / 1000;
                        if (this.tb < n8) {
                            this.tb = n8;
                        }
                    }
                    if (this.ub < n7) {
                        this.ub += this.cg + (n7 - this.ub) * this.dg / 1000;
                        if (this.ub > n7) {
                            this.ub = n7;
                        }
                    }
                    if (this.ub > n7) {
                        this.ub -= this.cg + (this.ub - n7) * this.dg / 1000;
                        if (this.ub < n7) {
                            this.ub = n7;
                        }
                    }
                    n6 = this.Zd * 128 + 64;
                    n7 = this.ae * 128 + 64;
                    n8 = this.a(this.Ac, n7, true, n6) - this.be;
                    n5 = n6 - this.sb;
                    int n9 = n8 - this.tb;
                    n4 = n7 - this.ub;
                    int n10 = (int)Math.sqrt(n5 * n5 + n4 * n4);
                    n3 = (int)(Math.atan2(n9, n10) * 325.949) & 0x7FF;
                    if (by != 5) break block31;
                    by = 0;
                    if (Jj == 0) break block32;
                }
                Bc = !Bc;
            }
            int n11 = (int)(Math.atan2(n5, n4) * -325.949) & 0x7FF;
            if (n3 < 128) {
                n3 = 128;
            }
            if (n3 > 383) {
                n3 = 383;
            }
            if (this.vb < n3) {
                this.vb += this.ce + (n3 - this.vb) * this.de / 1000;
                if (this.vb > n3) {
                    this.vb = n3;
                }
            }
            if (this.vb > n3) {
                this.vb -= this.ce + (this.vb - n3) * this.de / 1000;
                if (this.vb < n3) {
                    this.vb = n3;
                }
            }
            if ((n2 = n11 - this.wb) > 1024) {
                n2 -= 2048;
            }
            if (n2 < -1024) {
                n2 += 2048;
            }
            if (n2 > 0) {
                this.wb += this.ce + n2 * this.de / 1000;
                this.wb &= 0x7FF;
            }
            if (n2 < 0) {
                this.wb -= this.ce + -n2 * this.de / 1000;
                this.wb &= 0x7FF;
            }
            if ((n = n11 - this.wb) > 1024) {
                n -= 2048;
            }
            if (n < -1024) {
                n += 2048;
            }
            if (n < 0 && n2 > 0 || n > 0 && n2 < 0) {
                this.wb = n11;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("71397, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void e(byte by) {
        int n = Jj;
        try {
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            block11: {
                block10: {
                    n6 = this.fd;
                    n5 = this.gd;
                    n4 = this.hd;
                    n3 = this.id;
                    n2 = 6116423;
                    AFCKELYG.a(n3, n5, n6, n2, n4, 0);
                    if (by != 9) break block10;
                    by = 0;
                    if (n == 0) break block11;
                }
                return;
            }
            AFCKELYG.a(16, n5 + 1, n6 + 1, 0, n4 - 2, 0);
            AFCKELYG.a(n6 + 1, n4 - 2, n3 - 19, 0, n5 + 18, true);
            this.rj.b(n2, "Choose Option", n5 + 14, 822, n6 + 3);
            int n7 = this.t;
            int n8 = this.u;
            if (this.ed == 0) {
                n7 -= 4;
                n8 -= 4;
            }
            if (this.ed == 1) {
                n7 -= 553;
                n8 -= 205;
            }
            if (this.ed == 2) {
                n7 -= 17;
                n8 -= 357;
            }
            int n9 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n == 0) continue;
                int n10 = n5 + 31 + (this.Ig - 1 - n9) * 15;
                int n11 = 0xFFFFFF;
                if (n7 > n6 && n7 < n6 + n4 && n8 > n10 - 13 && n8 < n10 + 3) {
                    n11 = 0xFFFF00;
                }
                this.rj.a(false, true, n6 + 3, n11, this.Wh[n9], n10);
                ++n9;
            } while (n9 < this.Ig);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("82996, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(byte by, long l) {
        int n = Jj;
        try {
            if (l == 0L) {
                return;
            }
            if (this.hc >= 100 && this.Ye != 1) {
                this.a("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "", this.Vd);
                return;
            }
            if (this.hc >= 200) {
                this.a("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "", this.Vd);
                return;
            }
            String string = ZTQFNQRH.a(-45804, ZTQFNQRH.a(l, (byte)-99));
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n == 0) continue;
                if (this.ld[n2] == l) {
                    this.a(String.valueOf(string) + " is already on your friend list", 0, "", this.Vd);
                    return;
                }
                ++n2;
            } while (n2 < this.hc);
            if (by != 68) {
                this.me = -1;
            }
            int n3 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n == 0) continue;
                if (this.Hc[n3] == l) {
                    this.a("Please remove " + string + " from your ignore list first", 0, "", this.Vd);
                    return;
                }
                ++n3;
            } while (n3 < this.I);
            if (string.equals(client.Bg.yb)) {
                return;
            }
            this.Jf[this.hc] = string;
            this.ld[this.hc] = l;
            this.M[this.hc] = 0;
            ++this.hc;
            this.ch = true;
            this.Ph.a((byte)6, 188);
            this.Ph.a(5, l);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("15283, " + by + ", " + l + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final int a(int n, int n2, boolean bl, int n3) {
        try {
            this.gh &= bl;
            int n4 = n3 >> 7;
            int n5 = n2 >> 7;
            if (n4 < 0 || n5 < 0 || n4 > 103 || n5 > 103) {
                return 0;
            }
            int n6 = n;
            if (n6 < 3 && (this.dj[1][n4][n5] & 2) == 2) {
                ++n6;
            }
            int n7 = n3 & 0x7F;
            int n8 = n2 & 0x7F;
            int n9 = this.li[n6][n4][n5] * (128 - n7) + this.li[n6][n4 + 1][n5] * n7 >> 7;
            int n10 = this.li[n6][n4][n5 + 1] * (128 - n7) + this.li[n6][n4 + 1][n5 + 1] * n7 >> 7;
            return n9 * (128 - n8) + n10 * n8 >> 7;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("48438, " + n + ", " + n2 + ", " + bl + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private static final String e(int n, int n2) {
        try {
            if (n != -33245) {
                gb = -65;
            }
            if (n2 < 100000) {
                return String.valueOf(n2);
            }
            if (n2 < 10000000) {
                return String.valueOf(n2 / 1000) + "K";
            }
            return String.valueOf(n2 / 1000000) + "M";
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("13837, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void d(boolean bl) {
        try {
            try {
                if (this.rh != null) {
                    this.rh.a();
                }
            }
            catch (Exception exception) {}
            this.rh = null;
            if (!bl) {
                return;
            }
            this.gh = false;
            this.T = 0;
            this.wh = "";
            this.xh = "";
            this.b(false);
            this.cd.b(619);
            int n = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && Jj == 0) continue;
                this.Bi[n].a();
                ++n;
            } while (n < 4);
            System.gc();
            this.g(860);
            this.md = -1;
            this.yi = -1;
            this.ej = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("91154, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void n(int n) {
        int n2 = Jj;
        try {
            if (n != 0) {
                this.me = -1;
            }
            this.Je = true;
            int n3 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                this.sf[n3] = -1;
                int n4 = 0;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n2 == 0) continue;
                    if (!TAVAECED.c[n4].i && TAVAECED.c[n4].d == n3 + (this.Ze ? 0 : 7)) {
                        this.sf[n3] = n4;
                        if (n2 == 0) break;
                    }
                    ++n4;
                } while (n4 < TAVAECED.b);
                ++n3;
            } while (n3 < 7);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("51214, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void a(int n, MBMGIXGO mBMGIXGO, byte by) {
        int n2 = Jj;
        try {
            int n3;
            if (by != 2) {
                n3 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && n2 == 0) continue;
                    ++n3;
                } while (n3 > 0);
            }
            while (mBMGIXGO.A + 21 < n * 8 && (n3 = mBMGIXGO.c(14, 0)) != 16383) {
                int n4;
                if (this.V[n3] == null) {
                    this.V[n3] = new CWNCPMLX();
                }
                CWNCPMLX cWNCPMLX = this.V[n3];
                this.X[this.W++] = n3;
                cWNCPMLX.X = kh;
                int n5 = mBMGIXGO.c(5, 0);
                if (n5 > 15) {
                    n5 -= 32;
                }
                if ((n4 = mBMGIXGO.c(5, 0)) > 15) {
                    n4 -= 32;
                }
                int n6 = mBMGIXGO.c(1, 0);
                cWNCPMLX.vb = CKDEJADD.a(mBMGIXGO.c(12, 0));
                int n7 = mBMGIXGO.c(1, 0);
                if (n7 == 1) {
                    this.cc[this.bc++] = n3;
                }
                cWNCPMLX.ab = cWNCPMLX.vb.n;
                cWNCPMLX.q = cWNCPMLX.vb.y;
                cWNCPMLX.ob = cWNCPMLX.vb.m;
                cWNCPMLX.pb = cWNCPMLX.vb.d;
                cWNCPMLX.qb = cWNCPMLX.vb.C;
                cWNCPMLX.rb = cWNCPMLX.vb.a;
                cWNCPMLX.x = cWNCPMLX.vb.w;
                cWNCPMLX.a(client.Bg.m[0] + n4, client.Bg.n[0] + n5, n6 == 1, false);
                if (n2 == 0) continue;
            }
            mBMGIXGO.a(true);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("75258, " + n + ", " + mBMGIXGO + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void c(int n) {
        try {
            block9: {
                block8: {
                    if (this.Xi || this.Ic || this.zh) {
                        return;
                    }
                    ++kh;
                    if (this.gh) break block8;
                    this.o(true);
                    if (Jj == 0) break block9;
                }
                this.u(this.pi);
            }
            this.f(false);
            if (n != this.lf) {
                Bc = !Bc;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("20652, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, boolean bl) {
        int n2 = Jj;
        try {
            int n3;
            if (client.Bg.kb >> 7 == this.gj && client.Bg.lb >> 7 == this.hj) {
                this.gj = 0;
            }
            int n4 = this.Zb;
            if (n != 0) {
                n3 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n2 == 0) continue;
                    ++n3;
                } while (n3 > 0);
            }
            if (bl) {
                n4 = 1;
            }
            n3 = 0;
            boolean bl3 = true;
            do {
                block18: {
                    int n5;
                    DLZHLHNK dLZHLHNK;
                    block20: {
                        int n6;
                        int n7;
                        block19: {
                            block17: {
                                block16: {
                                    if (bl3 && !(bl3 = false) && n2 == 0) continue;
                                    if (!bl) break block16;
                                    dLZHLHNK = Bg;
                                    n5 = this.Xb << 14;
                                    if (n2 == 0) break block17;
                                }
                                dLZHLHNK = this.Yb[this.ac[n3]];
                                n5 = this.ac[n3] << 14;
                            }
                            if (dLZHLHNK == null || !dLZHLHNK.b(vi)) break block18;
                            dLZHLHNK.ub = false;
                            if ((qd && this.Zb > 50 || this.Zb > 200) && !bl && dLZHLHNK.D == dLZHLHNK.x) {
                                dLZHLHNK.ub = true;
                            }
                            n7 = dLZHLHNK.kb >> 7;
                            n6 = dLZHLHNK.lb >> 7;
                            if (n7 < 0 || n7 >= 104 || n6 < 0 || n6 >= 104) break block18;
                            if (dLZHLHNK.Jb == null || kh < dLZHLHNK.Cb || kh >= dLZHLHNK.Db) break block19;
                            dLZHLHNK.ub = false;
                            dLZHLHNK.Eb = this.a(this.Ac, dLZHLHNK.lb, true, dLZHLHNK.kb);
                            this.cd.a(60, this.Ac, dLZHLHNK.lb, dLZHLHNK, dLZHLHNK.mb, dLZHLHNK.Rb, dLZHLHNK.kb, dLZHLHNK.Eb, dLZHLHNK.Ob, dLZHLHNK.Qb, n5, dLZHLHNK.Pb, (byte)35);
                            if (n2 == 0) break block18;
                        }
                        if ((dLZHLHNK.kb & 0x7F) != 64 || (dLZHLHNK.lb & 0x7F) != 64) break block20;
                        if (this.Lc[n7][n6] == this.kj) break block18;
                        this.Lc[n7][n6] = this.kj;
                    }
                    dLZHLHNK.Eb = this.a(this.Ac, dLZHLHNK.lb, true, dLZHLHNK.kb);
                    this.cd.a(this.Ac, dLZHLHNK.mb, (byte)6, dLZHLHNK.Eb, n5, dLZHLHNK.lb, 60, dLZHLHNK.kb, dLZHLHNK, dLZHLHNK.bb);
                }
                ++n3;
            } while (n3 < n4);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("79491, " + n + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final boolean a(int var1_1, DUCMKFAY var2_2) {
        var7_3 = client.Jj;
        try {
            block23: {
                if (var1_1 <= 0) {
                    this.me = -1;
                }
                var3_4 = var2_2.h;
                if (this.ic == 2) {
                    if (var3_4 == 201) {
                        this.ui = true;
                        this.wi = 0;
                        this.bj = true;
                        this.ji = "";
                        this.rf = 1;
                        this.wg = "Enter name of friend to add to list";
                    }
                    if (var3_4 == 202) {
                        this.ui = true;
                        this.wi = 0;
                        this.bj = true;
                        this.ji = "";
                        this.rf = 2;
                        this.wg = "Enter name of friend to delete from list";
                    }
                }
                if (var3_4 == 205) {
                    this.pe = 250;
                    return true;
                }
                if (var3_4 == 501) {
                    this.ui = true;
                    this.wi = 0;
                    this.bj = true;
                    this.ji = "";
                    this.rf = 4;
                    this.wg = "Enter name of player to add to list";
                }
                if (var3_4 == 502) {
                    this.ui = true;
                    this.wi = 0;
                    this.bj = true;
                    this.ji = "";
                    this.rf = 5;
                    this.wg = "Enter name of player to delete from list";
                }
                if (var3_4 >= 300 && var3_4 <= 313) {
                    var4_6 = (var3_4 - 300) / 2;
                    var5_7 = var3_4 & 1;
                    var6_8 = this.sf[var4_6];
                    if (var6_8 != -1) {
                        do {
                            if (var5_7 == 0 && --var6_8 < 0) {
                                var6_8 = TAVAECED.b - 1;
                            }
                            if (var5_7 != 1 || ++var6_8 < TAVAECED.b) continue;
                            var6_8 = 0;
                        } while (TAVAECED.c[var6_8].i || TAVAECED.c[var6_8].d != var4_6 + (this.Ze != false ? 0 : 7));
                        this.sf[var4_6] = var6_8;
                        this.Je = true;
                    }
                }
                if (var3_4 >= 314 && var3_4 <= 323) {
                    var4_6 = (var3_4 - 314) / 2;
                    var5_7 = var3_4 & 1;
                    var6_8 = this.Ud[var4_6];
                    if (var5_7 == 0 && --var6_8 < 0) {
                        var6_8 = client.he[var4_6].length - 1;
                    }
                    if (var5_7 == 1 && ++var6_8 >= client.he[var4_6].length) {
                        var6_8 = 0;
                    }
                    this.Ud[var4_6] = var6_8;
                    this.Je = true;
                }
                if (var3_4 == 324 && !this.Ze) {
                    this.Ze = true;
                    this.n(0);
                }
                if (var3_4 == 325 && this.Ze) {
                    this.Ze = false;
                    this.n(0);
                }
                if (var3_4 != 326) break block23;
                this.Ph.a((byte)6, 101);
                this.Ph.a(this.Ze != false ? 0 : 1);
                var4_6 = 0;
                if (var7_3 == 0) ** GOTO lbl75
                do {
                    this.Ph.a(this.sf[var4_6]);
                    ++var4_6;
lbl75:
                    // 2 sources

                } while (var4_6 < 7);
                var5_7 = 0;
                if (var7_3 == 0) ** GOTO lbl81
                do {
                    this.Ph.a(this.Ud[var5_7]);
                    ++var5_7;
lbl81:
                    // 2 sources

                } while (var5_7 < 5);
                return true;
            }
            if (var3_4 == 613) {
                v0 = this.hh = this.hh == false;
            }
            if (var3_4 >= 601 && var3_4 <= 612) {
                this.M(537);
                if (this.Pb.length() > 0) {
                    this.Ph.a((byte)6, 218);
                    this.Ph.a(5, ZTQFNQRH.a(this.Pb));
                    this.Ph.a(var3_4 - 601);
                    this.Ph.a(this.hh != false ? 1 : 0);
                }
            }
            return false;
        }
        catch (RuntimeException var3_5) {
            signlink.reporterror("3833, " + var1_1 + ", " + var2_2 + ", " + var3_5.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void a(int n, byte by, MBMGIXGO mBMGIXGO) {
        int n2 = Jj;
        try {
            block8: {
                block7: {
                    if (by != 2) break block7;
                    by = 0;
                    if (n2 == 0) break block8;
                }
                return;
            }
            int n3 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                int n4 = this.cc[n3];
                DLZHLHNK dLZHLHNK = this.Yb[n4];
                int n5 = mBMGIXGO.c();
                if ((n5 & 0x40) != 0) {
                    n5 += mBMGIXGO.c() << 8;
                }
                this.a(n5, n4, mBMGIXGO, this.Fc, dLZHLHNK);
                ++n3;
            } while (n3 < this.bc);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("72508, " + n + ", " + by + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void b(int n, int n2, int n3, int n4, int n5, int n6) {
        int n7 = Jj;
        try {
            int n8;
            int n9;
            int n10;
            block43: {
                int n11;
                block45: {
                    int n12;
                    int n13;
                    block44: {
                        int n14;
                        Object object;
                        block30: {
                            block42: {
                                block41: {
                                    block40: {
                                        block36: {
                                            block39: {
                                                block38: {
                                                    block37: {
                                                        block32: {
                                                            block35: {
                                                                block34: {
                                                                    block33: {
                                                                        block31: {
                                                                            n10 = this.cd.c(n6, n4, n);
                                                                            if (n2 >= 0) {
                                                                                return;
                                                                            }
                                                                            if (n10 == 0) break block30;
                                                                            n9 = this.cd.g(n6, n4, n, n10);
                                                                            n13 = n9 >> 6 & 3;
                                                                            n12 = n9 & 0x1F;
                                                                            n8 = n3;
                                                                            if (n10 > 0) {
                                                                                n8 = n5;
                                                                            }
                                                                            object = this.ij.I;
                                                                            n11 = 24624 + n4 * 4 + (103 - n) * 512 * 4;
                                                                            n14 = n10 >> 14 & Short.MAX_VALUE;
                                                                            YZDBYLRM yZDBYLRM = YZDBYLRM.a(n14);
                                                                            if (yZDBYLRM.w == -1) break block31;
                                                                            DSMJIEPN dSMJIEPN = this.nf[yZDBYLRM.w];
                                                                            if (dSMJIEPN == null) break block30;
                                                                            int n15 = (yZDBYLRM.i * 4 - dSMJIEPN.D) / 2;
                                                                            int n16 = (yZDBYLRM.z * 4 - dSMJIEPN.E) / 2;
                                                                            dSMJIEPN.a(48 + n4 * 4 + n15, 16083, 48 + (104 - n - yZDBYLRM.z) * 4 + n16);
                                                                            if (n7 == 0) break block30;
                                                                        }
                                                                        if (n12 != 0 && n12 != 2) break block32;
                                                                        if (n13 != 0) break block33;
                                                                        object[n11] = n8;
                                                                        object[n11 + 512] = n8;
                                                                        object[n11 + 1024] = n8;
                                                                        object[n11 + 1536] = n8;
                                                                        if (n7 == 0) break block32;
                                                                    }
                                                                    if (n13 != 1) break block34;
                                                                    object[n11] = n8;
                                                                    object[n11 + 1] = n8;
                                                                    object[n11 + 2] = n8;
                                                                    object[n11 + 3] = n8;
                                                                    if (n7 == 0) break block32;
                                                                }
                                                                if (n13 != 2) break block35;
                                                                object[n11 + 3] = n8;
                                                                object[n11 + 3 + 512] = n8;
                                                                object[n11 + 3 + 1024] = n8;
                                                                object[n11 + 3 + 1536] = n8;
                                                                if (n7 == 0) break block32;
                                                            }
                                                            if (n13 == 3) {
                                                                object[n11 + 1536] = n8;
                                                                object[n11 + 1536 + 1] = n8;
                                                                object[n11 + 1536 + 2] = n8;
                                                                object[n11 + 1536 + 3] = n8;
                                                            }
                                                        }
                                                        if (n12 != 3) break block36;
                                                        if (n13 != 0) break block37;
                                                        object[n11] = n8;
                                                        if (n7 == 0) break block36;
                                                    }
                                                    if (n13 != 1) break block38;
                                                    object[n11 + 3] = n8;
                                                    if (n7 == 0) break block36;
                                                }
                                                if (n13 != 2) break block39;
                                                object[n11 + 3 + 1536] = n8;
                                                if (n7 == 0) break block36;
                                            }
                                            if (n13 == 3) {
                                                object[n11 + 1536] = n8;
                                            }
                                        }
                                        if (n12 != 2) break block30;
                                        if (n13 != 3) break block40;
                                        object[n11] = n8;
                                        object[n11 + 512] = n8;
                                        object[n11 + 1024] = n8;
                                        object[n11 + 1536] = n8;
                                        if (n7 == 0) break block30;
                                    }
                                    if (n13 != 0) break block41;
                                    object[n11] = n8;
                                    object[n11 + 1] = n8;
                                    object[n11 + 2] = n8;
                                    object[n11 + 3] = n8;
                                    if (n7 == 0) break block30;
                                }
                                if (n13 != 1) break block42;
                                object[n11 + 3] = n8;
                                object[n11 + 3 + 512] = n8;
                                object[n11 + 3 + 1024] = n8;
                                object[n11 + 3 + 1536] = n8;
                                if (n7 == 0) break block30;
                            }
                            if (n13 == 2) {
                                object[n11 + 1536] = n8;
                                object[n11 + 1536 + 1] = n8;
                                object[n11 + 1536 + 2] = n8;
                                object[n11 + 1536 + 3] = n8;
                            }
                        }
                        if ((n10 = this.cd.d(n6, n4, n)) == 0) break block43;
                        n9 = this.cd.g(n6, n4, n, n10);
                        n13 = n9 >> 6 & 3;
                        n12 = n9 & 0x1F;
                        n8 = n10 >> 14 & Short.MAX_VALUE;
                        object = YZDBYLRM.a(n8);
                        if (object.w == -1) break block44;
                        DSMJIEPN dSMJIEPN = this.nf[object.w];
                        if (dSMJIEPN == null) break block43;
                        n14 = (object.i * 4 - dSMJIEPN.D) / 2;
                        int n17 = (object.z * 4 - dSMJIEPN.E) / 2;
                        dSMJIEPN.a(48 + n4 * 4 + n14, 16083, 48 + (104 - n - object.z) * 4 + n17);
                        if (n7 == 0) break block43;
                    }
                    if (n12 != 9) break block43;
                    n11 = 0xEEEEEE;
                    if (n10 > 0) {
                        n11 = 0xEE0000;
                    }
                    int[] nArray = this.ij.I;
                    int n18 = 24624 + n4 * 4 + (103 - n) * 512 * 4;
                    if (n13 != 0 && n13 != 2) break block45;
                    nArray[n18 + 1536] = n11;
                    nArray[n18 + 1024 + 1] = n11;
                    nArray[n18 + 512 + 2] = n11;
                    nArray[n18 + 3] = n11;
                    if (n7 == 0) break block43;
                }
                nArray[n18] = n11;
                nArray[n18 + 512 + 1] = n11;
                nArray[n18 + 1024 + 2] = n11;
                nArray[n18 + 1536 + 3] = n11;
            }
            if ((n10 = this.cd.e(n6, n4, n)) != 0) {
                DSMJIEPN dSMJIEPN;
                n9 = n10 >> 14 & Short.MAX_VALUE;
                YZDBYLRM yZDBYLRM = YZDBYLRM.a(n9);
                if (yZDBYLRM.w != -1 && (dSMJIEPN = this.nf[yZDBYLRM.w]) != null) {
                    n8 = (yZDBYLRM.i * 4 - dSMJIEPN.D) / 2;
                    int n19 = (yZDBYLRM.z * 4 - dSMJIEPN.E) / 2;
                    dSMJIEPN.a(48 + n4 * 4 + n8, 16083, 48 + (104 - n - yZDBYLRM.z) * 4 + n19);
                    return;
                }
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("19786, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void o(int n) {
        int n2 = Jj;
        try {
            int n3;
            block23: {
                block22: {
                    this.wd = new DSMJIEPN(this.ff, "titlebox", 0);
                    if (n <= 0) {
                        Ci = !Ci;
                    }
                    this.xd = new DSMJIEPN(this.ff, "titlebutton", 0);
                    this.bh = new DSMJIEPN[12];
                    int n4 = 0;
                    try {
                        n4 = Integer.parseInt(this.getParameter("fl_icon"));
                    }
                    catch (Exception exception) {}
                    if (n4 != 0) break block22;
                    n3 = 0;
                    boolean bl = true;
                    do {
                        if (bl && !(bl = false) && n2 == 0) continue;
                        this.bh[n3] = new DSMJIEPN(this.ff, "runes", n3);
                        ++n3;
                    } while (n3 < 12);
                    if (n2 == 0) break block23;
                }
                n3 = 0;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && n2 == 0) continue;
                    this.bh[n3] = new DSMJIEPN(this.ff, "runes", 12 + (n3 & 3));
                    ++n3;
                } while (n3 < 12);
            }
            this.Yh = new CXGZMTJK(128, 265);
            this.Zh = new CXGZMTJK(128, 265);
            n3 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                this.Yh.I[n3] = this.lg.c[n3];
                ++n3;
            } while (n3 < 33920);
            int n5 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                this.Zh.I[n5] = this.mg.c[n5];
                ++n5;
            } while (n5 < 33920);
            this.lb = new int[256];
            int n6 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n2 == 0) continue;
                this.lb[n6] = n6 * 262144;
                ++n6;
            } while (n6 < 64);
            int n7 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && n2 == 0) continue;
                this.lb[n7 + 64] = 0xFF0000 + 1024 * n7;
                ++n7;
            } while (n7 < 64);
            int n8 = 0;
            boolean bl5 = true;
            do {
                if (bl5 && !(bl5 = false) && n2 == 0) continue;
                this.lb[n8 + 128] = 0xFFFF00 + 4 * n8;
                ++n8;
            } while (n8 < 64);
            int n9 = 0;
            boolean bl6 = true;
            do {
                if (bl6 && !(bl6 = false) && n2 == 0) continue;
                this.lb[n9 + 192] = 0xFFFFFF;
                ++n9;
            } while (n9 < 64);
            this.mb = new int[256];
            int n10 = 0;
            boolean bl7 = true;
            do {
                if (bl7 && !(bl7 = false) && n2 == 0) continue;
                this.mb[n10] = n10 * 1024;
                ++n10;
            } while (n10 < 64);
            int n11 = 0;
            boolean bl8 = true;
            do {
                if (bl8 && !(bl8 = false) && n2 == 0) continue;
                this.mb[n11 + 64] = 65280 + 4 * n11;
                ++n11;
            } while (n11 < 64);
            int n12 = 0;
            boolean bl9 = true;
            do {
                if (bl9 && !(bl9 = false) && n2 == 0) continue;
                this.mb[n12 + 128] = 65535 + 262144 * n12;
                ++n12;
            } while (n12 < 64);
            int n13 = 0;
            boolean bl10 = true;
            do {
                if (bl10 && !(bl10 = false) && n2 == 0) continue;
                this.mb[n13 + 192] = 0xFFFFFF;
                ++n13;
            } while (n13 < 64);
            this.nb = new int[256];
            int n14 = 0;
            boolean bl11 = true;
            do {
                if (bl11 && !(bl11 = false) && n2 == 0) continue;
                this.nb[n14] = n14 * 4;
                ++n14;
            } while (n14 < 64);
            int n15 = 0;
            boolean bl12 = true;
            do {
                if (bl12 && !(bl12 = false) && n2 == 0) continue;
                this.nb[n15 + 64] = 255 + 262144 * n15;
                ++n15;
            } while (n15 < 64);
            int n16 = 0;
            boolean bl13 = true;
            do {
                if (bl13 && !(bl13 = false) && n2 == 0) continue;
                this.nb[n16 + 128] = 0xFF00FF + 1024 * n16;
                ++n16;
            } while (n16 < 64);
            int n17 = 0;
            boolean bl14 = true;
            do {
                if (bl14 && !(bl14 = false) && n2 == 0) continue;
                this.nb[n17 + 192] = 0xFFFFFF;
                ++n17;
            } while (n17 < 64);
            this.kb = new int[256];
            this.Nh = new int[32768];
            this.Oh = new int[32768];
            this.a((DSMJIEPN)null, -135);
            this.O = new int[32768];
            this.P = new int[32768];
            this.a(10, (byte)4, "Connecting to fileserver");
            if (this.R) return;
            this.Ob = true;
            this.R = true;
            this.a(this, 2);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("57668, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static final void e(boolean bl) {
        try {
            NYFUGYQS.h = false;
            OPPOFIOL.A = false;
            qd = false;
            CRRWDRTI.C = false;
            if (bl) {
                Bc = !Bc;
            }
            YZDBYLRM.q = false;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("27524, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static final void main(String[] stringArray) {
        int n = Jj;
        try {
            block13: {
                block14: {
                    block12: {
                        block10: {
                            block11: {
                                block9: {
                                    System.out.println("RS2 user client - release #" + 317);
                                    if (stringArray.length != 5) {
                                        System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
                                        return;
                                    }
                                    nd = Integer.parseInt(stringArray[0]);
                                    od = Integer.parseInt(stringArray[1]);
                                    if (!stringArray[2].equals("lowmem")) break block9;
                                    client.m((byte)77);
                                    if (n == 0) break block10;
                                }
                                if (!stringArray[2].equals("highmem")) break block11;
                                client.e(false);
                                if (n == 0) break block10;
                            }
                            System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
                            return;
                        }
                        if (!stringArray[3].equals("free")) break block12;
                        pd = false;
                        if (n == 0) break block13;
                    }
                    if (!stringArray[3].equals("members")) break block14;
                    pd = true;
                    if (n == 0) break block13;
                }
                System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
                return;
            }
            signlink.storeid = Integer.parseInt(stringArray[4]);
            signlink.startpriv(InetAddress.getLocalHost());
            client client2 = new client();
            client2.a(503, false, 765);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void p(int n) {
        try {
            int n2;
            if (n != -48877) {
                return;
            }
            if (qd && this.Be == 2 && CRRWDRTI.i != this.Ac) {
                this.oh.a(0);
                this.qj.a(0, "Loading - please wait.", 23693, 151, 257);
                this.qj.a(0xFFFFFF, "Loading - please wait.", 23693, 150, 256);
                this.oh.a(4, 23680, this.l, 4);
                this.Be = 1;
                this.K = System.currentTimeMillis();
            }
            if (this.Be == 1 && (n2 = this.f((byte)-95)) != 0 && System.currentTimeMillis() - this.K > 360000L) {
                signlink.reporterror(String.valueOf(this.wh) + " glcfb " + this.mi + "," + n2 + "," + qd + "," + this.Ad[0] + "," + this.vf.a() + "," + this.Ac + "," + this.wf + "," + this.xf);
                this.K = System.currentTimeMillis();
            }
            if (this.Be == 2 && this.Ac != this.Pd) {
                this.Pd = this.Ac;
                this.b(true, this.Ac);
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("51136, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final int f(byte by) {
        int n = Jj;
        try {
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n == 0) continue;
                if (this.Gh[n2] == null && this.Gi[n2] != -1) {
                    return -1;
                }
                if (this.Si[n2] == null && this.Hi[n2] != -1) {
                    return -2;
                }
                ++n2;
            } while (n2 < this.Gh.length);
            boolean bl2 = true;
            if (by != -95) {
                return 0;
            }
            int n3 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n == 0) continue;
                byte[] byArray = this.Si[n3];
                if (byArray != null) {
                    int n4 = (this.Fi[n3] >> 8) * 64 - this.Me;
                    int n5 = (this.Fi[n3] & 0xFF) * 64 - this.Ne;
                    if (this.ih) {
                        n4 = 10;
                        n5 = 10;
                    }
                    bl2 &= CRRWDRTI.a(n4, byArray, n5, 6);
                }
                ++n3;
            } while (n3 < this.Gh.length);
            if (!bl2) {
                return -3;
            }
            if (this.Hf) {
                return -4;
            }
            this.Be = 2;
            CRRWDRTI.i = this.Ac;
            this.a(true);
            this.Ph.a((byte)6, 121);
            return 0;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("50361, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void q(int n) {
        int n2 = Jj;
        try {
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                this.a();
            } while (n >= 0);
            SWTXAYDT sWTXAYDT = (SWTXAYDT)this.re.b();
            boolean bl2 = true;
            do {
                block10: {
                    block11: {
                        void var3_9;
                        block13: {
                            int n3;
                            block12: {
                                CWNCPMLX cWNCPMLX;
                                block9: {
                                    if (bl2 && !(bl2 = false) && n2 == 0) continue;
                                    if (sWTXAYDT.M == this.Ac && kh <= sWTXAYDT.n) break block9;
                                    sWTXAYDT.a();
                                    if (n2 == 0) break block10;
                                }
                                if (kh < sWTXAYDT.m) break block10;
                                if (sWTXAYDT.F > 0 && (cWNCPMLX = this.V[sWTXAYDT.F - 1]) != null && cWNCPMLX.kb >= 0 && cWNCPMLX.kb < 13312 && cWNCPMLX.lb >= 0 && cWNCPMLX.lb < 13312) {
                                    sWTXAYDT.a(kh, cWNCPMLX.lb, this.a(sWTXAYDT.M, cWNCPMLX.lb, true, cWNCPMLX.kb) - sWTXAYDT.y, cWNCPMLX.kb, (byte)-83);
                                }
                                if (sWTXAYDT.F >= 0) break block11;
                                n3 = -sWTXAYDT.F - 1;
                                if (n3 != this.Sb) break block12;
                                DLZHLHNK dLZHLHNK = Bg;
                                if (n2 == 0) break block13;
                            }
                            DLZHLHNK dLZHLHNK = this.Yb[n3];
                        }
                        if (var3_9 != null && var3_9.kb >= 0 && var3_9.kb < 13312 && var3_9.lb >= 0 && var3_9.lb < 13312) {
                            sWTXAYDT.a(kh, var3_9.lb, this.a(sWTXAYDT.M, var3_9.lb, true, var3_9.kb) - sWTXAYDT.y, var3_9.kb, (byte)-83);
                        }
                    }
                    sWTXAYDT.a(this.bd, this.ye);
                    this.cd.a(this.Ac, sWTXAYDT.K, (byte)6, (int)sWTXAYDT.C, -1, (int)sWTXAYDT.B, 60, (int)sWTXAYDT.A, sWTXAYDT, false);
                }
                sWTXAYDT = (SWTXAYDT)this.re.a(false);
            } while (sWTXAYDT != null);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("65179, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final AppletContext getAppletContext() {
        if (signlink.mainapp != null) {
            return signlink.mainapp.getAppletContext();
        }
        return super.getAppletContext();
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void r(int n) {
        int n2 = Jj;
        try {
            byte[] byArray = this.ff.a("title.dat", null);
            CXGZMTJK cXGZMTJK = new CXGZMTJK(byArray, this);
            this.lg.a(0);
            cXGZMTJK.a(0, 0, -32357);
            this.mg.a(0);
            cXGZMTJK.a(-637, 0, -32357);
            this.ig.a(0);
            cXGZMTJK.a(-128, 0, -32357);
            this.jg.a(0);
            cXGZMTJK.a(-202, -371, -32357);
            this.kg.a(0);
            cXGZMTJK.a(-202, -171, -32357);
            this.ng.a(0);
            cXGZMTJK.a(0, -265, -32357);
            this.og.a(0);
            cXGZMTJK.a(-562, -265, -32357);
            this.pg.a(0);
            cXGZMTJK.a(-128, -171, -32357);
            this.qg.a(0);
            cXGZMTJK.a(-562, -171, -32357);
            int[] nArray = new int[cXGZMTJK.J];
            int n3 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                int n4 = 0;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n2 == 0) continue;
                    nArray[n4] = cXGZMTJK.I[cXGZMTJK.J - n4 - 1 + cXGZMTJK.J * n3];
                    ++n4;
                } while (n4 < cXGZMTJK.J);
                int n5 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && n2 == 0) continue;
                    cXGZMTJK.I[n5 + cXGZMTJK.J * n3] = nArray[n5];
                    ++n5;
                } while (n5 < cXGZMTJK.J);
                ++n3;
            } while (n3 < cXGZMTJK.K);
            this.lg.a(0);
            cXGZMTJK.a(382, 0, -32357);
            this.mg.a(0);
            cXGZMTJK.a(-255, 0, -32357);
            this.ig.a(0);
            cXGZMTJK.a(254, 0, -32357);
            this.jg.a(0);
            cXGZMTJK.a(180, -371, -32357);
            this.kg.a(0);
            cXGZMTJK.a(180, -171, -32357);
            this.ng.a(0);
            cXGZMTJK.a(382, -265, -32357);
            this.og.a(0);
            cXGZMTJK.a(-180, -265, -32357);
            this.pg.a(0);
            cXGZMTJK.a(254, -171, -32357);
            this.qg.a(0);
            if (n != 0) {
                return;
            }
            cXGZMTJK.a(-180, -171, -32357);
            cXGZMTJK = new CXGZMTJK(this.ff, "logo", 0);
            this.ig.a(0);
            cXGZMTJK.b(382 - cXGZMTJK.J / 2 - 128, 16083, 18);
            cXGZMTJK = null;
            byArray = null;
            nArray = null;
            System.gc();
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("96255, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void f(boolean bl) {
        int n = Jj;
        try {
            if (bl) {
                this.Rb = -72;
            }
            while (true) {
                PHKHJKBS pHKHJKBS;
                if ((pHKHJKBS = this.vf.c()) == null) {
                    return;
                }
                if (pHKHJKBS.i == 0) {
                    ZKARKDQW.a(pHKHJKBS.j, -4036, pHKHJKBS.k);
                    if ((this.vf.c(pHKHJKBS.k, -203) & 0x62) != 0) {
                        this.ch = true;
                        if (this.vj != -1) {
                            this.ui = true;
                        }
                    }
                }
                if (pHKHJKBS.i == 1 && pHKHJKBS.j != null) {
                    SQHJOGRT.a(pHKHJKBS.j, false);
                }
                if (pHKHJKBS.i == 2 && pHKHJKBS.k == this.yi && pHKHJKBS.j != null) {
                    this.a(this.zi, 0, pHKHJKBS.j);
                }
                if (pHKHJKBS.i == 3 && this.Be == 1) {
                    int n2 = 0;
                    boolean bl2 = true;
                    do {
                        if (bl2 && !(bl2 = false) && n == 0) continue;
                        if (this.Gi[n2] == pHKHJKBS.k) {
                            this.Gh[n2] = pHKHJKBS.j;
                            if (pHKHJKBS.j != null) break;
                            this.Gi[n2] = -1;
                            if (n == 0) break;
                        }
                        if (this.Hi[n2] == pHKHJKBS.k) {
                            this.Si[n2] = pHKHJKBS.j;
                            if (pHKHJKBS.j != null) break;
                            this.Hi[n2] = -1;
                            if (n == 0) break;
                        }
                        ++n2;
                    } while (n2 < this.Gh.length);
                }
                if (pHKHJKBS.i != 93 || !this.vf.d(pHKHJKBS.k, -520)) continue;
                CRRWDRTI.a((byte)-107, new MBMGIXGO(pHKHJKBS.j, 891), this.vf);
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("21105, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void s(int n) {
        int n2 = Jj;
        try {
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            int n8 = 256;
            int n9 = 10;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                n7 = (int)(Math.random() * 100.0);
                if (n7 < 50) {
                    this.O[n9 + (n8 - 2 << 7)] = 255;
                }
                ++n9;
            } while (n9 < 117);
            if (n != 25106) {
                this.a();
            }
            n7 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                n6 = (int)(Math.random() * 124.0) + 2;
                n5 = (int)(Math.random() * 128.0) + 128;
                n4 = n6 + (n5 << 7);
                this.O[n4] = 192;
                ++n7;
            } while (n7 < 100);
            n6 = 1;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n2 == 0) continue;
                n5 = 1;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && n2 == 0) continue;
                    n4 = n5 + (n6 << 7);
                    this.P[n4] = (this.O[n4 - 1] + this.O[n4 + 1] + this.O[n4 - 128] + this.O[n4 + 128]) / 4;
                    ++n5;
                } while (n5 < 127);
                ++n6;
            } while (n6 < n8 - 1);
            this.uj += 128;
            if (this.uj > this.Nh.length) {
                this.uj -= this.Nh.length;
                n5 = (int)(Math.random() * 12.0);
                this.a(this.bh[n5], -135);
            }
            n5 = 1;
            boolean bl5 = true;
            do {
                if (bl5 && !(bl5 = false) && n2 == 0) continue;
                n4 = 1;
                boolean bl6 = true;
                do {
                    if (bl6 && !(bl6 = false) && n2 == 0) continue;
                    n3 = n4 + (n5 << 7);
                    int n10 = this.P[n3 + 128] - this.Nh[n3 + this.uj & this.Nh.length - 1] / 5;
                    if (n10 < 0) {
                        n10 = 0;
                    }
                    this.O[n3] = n10;
                    ++n4;
                } while (n4 < 127);
                ++n5;
            } while (n5 < n8 - 1);
            n4 = 0;
            boolean bl7 = true;
            do {
                if (bl7 && !(bl7 = false) && n2 == 0) continue;
                this.zd[n4] = this.zd[n4 + 1];
                ++n4;
            } while (n4 < n8 - 1);
            this.zd[n8 - 1] = (int)(Math.sin((double)kh / 14.0) * 16.0 + Math.sin((double)kh / 15.0) * 14.0 + Math.sin((double)kh / 16.0) * 12.0);
            if (this.Se > 0) {
                this.Se -= 4;
            }
            if (this.Te > 0) {
                this.Te -= 4;
            }
            if (this.Se != 0) return;
            if (this.Te != 0) return;
            n3 = (int)(Math.random() * 2000.0);
            if (n3 == 0) {
                this.Se = 1024;
            }
            if (n3 != 1) return;
            this.Te = 1024;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("52615, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final boolean a(byte[] byArray, byte by, int n) {
        try {
            if (by != 116) {
                throw new NullPointerException();
            }
            if (byArray == null) {
                return true;
            }
            return signlink.wavesave(byArray, n);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("75318, " + byArray + ", " + by + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void a(int var1_1, byte var2_2) {
        try {
            var3_3 = DUCMKFAY.d[var1_1];
            var4_5 = 0;
            if (client.Jj == 0) ** GOTO lbl12
            while (var3_3.H[var4_5] != -1) {
                var5_6 = DUCMKFAY.d[var3_3.H[var4_5]];
                if (var5_6.db == 1) {
                    this.a(var5_6.R, (byte)6);
                }
                var5_6.N = 0;
                var5_6.b = 0;
                ++var4_5;
lbl12:
                // 2 sources

                if (var4_5 < var3_3.H.length) continue;
            }
            if (var2_2 == 6) {
                var2_2 = 0;
                return;
            }
            return;
        }
        catch (RuntimeException var3_4) {
            signlink.reporterror("61586, " + var1_1 + ", " + var2_2 + ", " + var3_4.toString());
            throw new RuntimeException();
        }
    }

    public final void t(int n) {
        try {
            if (this.pb != 2) {
                return;
            }
            this.b((this.Qc - this.Me << 7) + this.Tc, this.Sc * 2, this.Jb, (this.Rc - this.Ne << 7) + this.Uc);
            if (n >= 0) {
                boolean bl = vi = !vi;
            }
            if (this.td > -1 && kh % 20 < 10) {
                this.Wf[2].b(this.td - 12, 16083, this.ud - 28);
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("10525, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void u(int var1_1) {
        var13_2 = client.Jj;
        try {
            block118: {
                block120: {
                    block124: {
                        block119: {
                            block122: {
                                block123: {
                                    block121: {
                                        block113: {
                                            block117: {
                                                block116: {
                                                    block115: {
                                                        block114: {
                                                            block103: {
                                                                if (this.fg > 1) {
                                                                    --this.fg;
                                                                }
                                                                if (this.pe > 0) {
                                                                    --this.pe;
                                                                }
                                                                var2_3 = 0;
                                                                if (var13_2 == 0) ** GOTO lbl11
                                                                while (this.p(true)) {
                                                                    ++var2_3;
lbl11:
                                                                    // 2 sources

                                                                    if (var2_3 < 5) continue;
                                                                }
                                                                if (!this.gh) {
                                                                    return;
                                                                }
                                                                var3_5 = this.Nb.b;
                                                                synchronized (var3_5) {
                                                                    block102: {
                                                                        block112: {
                                                                            if (!client.ci) break block102;
                                                                            if (this.z == 0 && this.Nb.f < 40) break block103;
                                                                            this.Ph.a((byte)6, 45);
                                                                            this.Ph.a(0);
                                                                            var5_10 = this.Ph.z;
                                                                            var6_11 = 0;
                                                                            var7_12 = 0;
                                                                            if (var13_2 == 0) ** GOTO lbl70
                                                                            while (var5_10 - this.Ph.z < 240) {
                                                                                block109: {
                                                                                    block111: {
                                                                                        block110: {
                                                                                            block108: {
                                                                                                block107: {
                                                                                                    block106: {
                                                                                                        block105: {
                                                                                                            block104: {
                                                                                                                ++var6_11;
                                                                                                                var8_13 = this.Nb.c[var7_12];
                                                                                                                if (var8_13 >= 0) break block104;
                                                                                                                var8_13 = 0;
                                                                                                                if (var13_2 == 0) break block105;
                                                                                                            }
                                                                                                            if (var8_13 > 502) {
                                                                                                                var8_13 = 502;
                                                                                                            }
                                                                                                        }
                                                                                                        if ((var9_14 = this.Nb.e[var7_12]) >= 0) break block106;
                                                                                                        var9_14 = 0;
                                                                                                        if (var13_2 == 0) break block107;
                                                                                                    }
                                                                                                    if (var9_14 > 764) {
                                                                                                        var9_14 = 764;
                                                                                                    }
                                                                                                }
                                                                                                var10_15 = var8_13 * 765 + var9_14;
                                                                                                if (this.Nb.c[var7_12] == -1 && this.Nb.e[var7_12] == -1) {
                                                                                                    var9_14 = -1;
                                                                                                    var8_13 = -1;
                                                                                                    var10_15 = 524287;
                                                                                                }
                                                                                                if (var9_14 != this.Ii || var8_13 != this.Ji) break block108;
                                                                                                if (this.Ae >= 2047) break block109;
                                                                                                ++this.Ae;
                                                                                                if (var13_2 == 0) break block109;
                                                                                            }
                                                                                            var11_16 = var9_14 - this.Ii;
                                                                                            this.Ii = var9_14;
                                                                                            var12_17 = var8_13 - this.Ji;
                                                                                            this.Ji = var8_13;
                                                                                            if (this.Ae >= 8 || var11_16 < -32 || var11_16 > 31 || var12_17 < -32 || var12_17 > 31) break block110;
                                                                                            this.Ph.b((this.Ae << 12) + ((var11_16 += 32) << 6) + (var12_17 += 32));
                                                                                            this.Ae = 0;
                                                                                            if (var13_2 == 0) break block109;
                                                                                        }
                                                                                        if (this.Ae >= 8) break block111;
                                                                                        this.Ph.c(0x800000 + (this.Ae << 19) + var10_15);
                                                                                        this.Ae = 0;
                                                                                        if (var13_2 == 0) break block109;
                                                                                    }
                                                                                    this.Ph.d(-1073741824 + (this.Ae << 19) + var10_15);
                                                                                    this.Ae = 0;
                                                                                }
                                                                                ++var7_12;
lbl70:
                                                                                // 2 sources

                                                                                if (var7_12 < this.Nb.f) continue;
                                                                            }
                                                                            this.Ph.a(this.Ph.z - var5_10, (byte)0);
                                                                            if (var6_11 < this.Nb.f) break block112;
                                                                            this.Nb.f = 0;
                                                                            if (var13_2 == 0) break block103;
                                                                        }
                                                                        this.Nb.f -= var6_11;
                                                                        var8_13 = 0;
                                                                        if (var13_2 == 0) ** GOTO lbl83
                                                                        do {
                                                                            this.Nb.e[var8_13] = this.Nb.e[var8_13 + var6_11];
                                                                            this.Nb.c[var8_13] = this.Nb.c[var8_13 + var6_11];
                                                                            ++var8_13;
lbl83:
                                                                            // 2 sources

                                                                        } while (var8_13 < this.Nb.f);
                                                                        if (var13_2 == 0) break block103;
                                                                    }
                                                                    this.Nb.f = 0;
                                                                }
                                                            }
                                                            if (this.z == 0) break block113;
                                                            var3_6 = (this.C - this.ri) / 50L;
                                                            if (var3_6 > 4095L) {
                                                                var3_6 = 4095L;
                                                            }
                                                            this.ri = this.C;
                                                            var5_10 = this.B;
                                                            if (var5_10 >= 0) break block114;
                                                            var5_10 = 0;
                                                            if (var13_2 == 0) break block115;
                                                        }
                                                        if (var5_10 > 502) {
                                                            var5_10 = 502;
                                                        }
                                                    }
                                                    if ((var6_11 = this.A) >= 0) break block116;
                                                    var6_11 = 0;
                                                    if (var13_2 == 0) break block117;
                                                }
                                                if (var6_11 > 764) {
                                                    var6_11 = 764;
                                                }
                                            }
                                            var7_12 = var5_10 * 765 + var6_11;
                                            var8_13 = 0;
                                            if (this.z == 2) {
                                                var8_13 = 1;
                                            }
                                            var9_14 = (int)var3_6;
                                            this.Ph.a((byte)6, 241);
                                            this.Ph.d((var9_14 << 20) + (var8_13 << 19) + var7_12);
                                        }
                                        if (this.ue > 0) {
                                            --this.ue;
                                        }
                                        if (this.D[1] == 1 || this.D[2] == 1 || this.D[3] == 1 || this.D[4] == 1) {
                                            this.ve = true;
                                        }
                                        if (this.ve && this.ue <= 0) {
                                            this.ue = 20;
                                            this.ve = false;
                                            this.Ph.a((byte)6, 86);
                                            this.Ph.b(this.Hh);
                                            this.Ph.f(-431, this.Ih);
                                        }
                                        if (this.q && !this.kd) {
                                            this.kd = true;
                                            this.Ph.a((byte)6, 3);
                                            this.Ph.a(1);
                                        }
                                        if (!this.q && this.kd) {
                                            this.kd = false;
                                            this.Ph.a((byte)6, 3);
                                            this.Ph.a(0);
                                        }
                                        this.p(-48877);
                                        this.j((byte)8);
                                        this.h(false);
                                        ++this.ne;
                                        if (this.ne > 750) {
                                            this.x(-670);
                                        }
                                        this.i((byte)-74);
                                        this.F(-8066);
                                        this.c((byte)-92);
                                        ++this.bd;
                                        if (this.zc != 0) {
                                            this.yc += 20;
                                            if (this.yc >= 400) {
                                                this.zc = 0;
                                            }
                                        }
                                        if (this.Ri != 0) {
                                            ++this.Oi;
                                            if (this.Oi >= 15) {
                                                if (this.Ri == 2) {
                                                    this.ch = true;
                                                }
                                                if (this.Ri == 3) {
                                                    this.ui = true;
                                                }
                                                this.Ri = 0;
                                            }
                                        }
                                        if (this.Nf == 0) break block118;
                                        ++this.Td;
                                        if (this.t > this.Of + 5 || this.t < this.Of - 5 || this.u > this.Pf + 5 || this.u < this.Pf - 5) {
                                            this.Ni = true;
                                        }
                                        if (this.s != 0) break block118;
                                        if (this.Nf == 2) {
                                            this.ch = true;
                                        }
                                        if (this.Nf == 3) {
                                            this.ui = true;
                                        }
                                        this.Nf = 0;
                                        if (!this.Ni || this.Td < 5) break block119;
                                        this.uf = -1;
                                        this.D(0);
                                        if (this.uf != this.Lf || this.tf == this.Mf) break block120;
                                        var3_7 = DUCMKFAY.d[this.Lf];
                                        var4_18 = 0;
                                        if (this.vc == 1 && var3_7.h == 206) {
                                            var4_18 = 1;
                                        }
                                        if (var3_7.U[this.tf] <= 0) {
                                            var4_18 = 0;
                                        }
                                        if (!var3_7.C) break block121;
                                        var5_10 = this.Mf;
                                        var6_11 = this.tf;
                                        var3_7.U[var6_11] = var3_7.U[var5_10];
                                        var3_7.T[var6_11] = var3_7.T[var5_10];
                                        var3_7.U[var5_10] = -1;
                                        var3_7.T[var5_10] = 0;
                                        if (var13_2 == 0) break block122;
                                    }
                                    if (var4_18 != 1) break block123;
                                    var5_10 = this.Mf;
                                    var6_11 = this.tf;
                                    if (var13_2 == 0) ** GOTO lbl200
                                    do {
                                        if (var5_10 > var6_11) {
                                            var3_7.a(var5_10, (byte)9, var5_10 - 1);
                                            --var5_10;
                                            if (var13_2 == 0) continue;
                                        }
                                        if (var5_10 >= var6_11) continue;
                                        var3_7.a(var5_10, (byte)9, var5_10 + 1);
                                        ++var5_10;
lbl200:
                                        // 4 sources

                                    } while (var5_10 != var6_11);
                                    if (var13_2 == 0) break block122;
                                }
                                var3_7.a(this.Mf, (byte)9, this.tf);
                            }
                            this.Ph.a((byte)6, 214);
                            this.Ph.g(0, this.Lf);
                            this.Ph.d(var4_18, 0);
                            this.Ph.g(0, this.Mf);
                            this.Ph.b(true, this.tf);
                            if (var13_2 == 0) break block120;
                        }
                        if (this.Yi != 1 && !this.b(9, this.Ig - 1) || this.Ig <= 2) break block124;
                        this.l(true);
                        if (var13_2 == 0) break block120;
                    }
                    if (this.Ig > 0) {
                        this.b(this.Ig - 1, false);
                    }
                }
                this.Oi = 10;
                this.z = 0;
            }
            if (NYFUGYQS.P != -1) {
                var3_8 = NYFUGYQS.P;
                var4_18 = NYFUGYQS.Q;
                var5_10 = (int)this.a(0, 0, 0, -11308, 0, client.Bg.n[0], 0, 0, var4_18, client.Bg.m[0], true, var3_8);
                NYFUGYQS.P = -1;
                if (var5_10 != 0) {
                    this.wc = this.A;
                    this.xc = this.B;
                    this.zc = 1;
                    this.yc = 0;
                }
            }
            if (this.z == 1 && this.eb != null) {
                this.eb = null;
                this.ui = true;
                this.z = 0;
            }
            this.k(4);
            this.i(true);
            this.B(19);
            this.c(true);
            if (this.s == 1 || this.z == 1) {
                ++this.ki;
            }
            if (this.Be == 2) {
                this.G(3);
            }
            if (this.Be == 2 && this.jh) {
                this.d((byte)5);
            }
            var3_9 = 0;
            if (var13_2 == 0) ** GOTO lbl251
            do {
                v1 = var3_9++;
                this.Ie[v1] = this.Ie[v1] + 1;
lbl251:
                // 2 sources

            } while (var3_9 < 5);
            this.A(732);
            ++this.r;
            if (this.r > 4500) {
                this.pe = 250;
                this.r -= 500;
                this.Ph.a((byte)6, 202);
            }
            ++this.Sd;
            if (var1_1 >= 0) {
                this.N = null;
            }
            if (this.Sd > 500) {
                this.Sd = 0;
                var4_18 = (int)(Math.random() * 8.0);
                if ((var4_18 & 1) == 1) {
                    this.xj += this.yj;
                }
                if ((var4_18 & 2) == 2) {
                    this.Gg += this.Hg;
                }
                if ((var4_18 & 4) == 4) {
                    this.ec += this.fc;
                }
            }
            if (this.xj < -50) {
                this.yj = 2;
            }
            if (this.xj > 50) {
                this.yj = -2;
            }
            if (this.Gg < -55) {
                this.Hg = 2;
            }
            if (this.Gg > 55) {
                this.Hg = -2;
            }
            if (this.ec < -40) {
                this.fc = 1;
            }
            if (this.ec > 40) {
                this.fc = -1;
            }
            ++this.Zi;
            if (this.Zi > 500) {
                this.Zi = 0;
                var4_18 = (int)(Math.random() * 8.0);
                if ((var4_18 & 1) == 1) {
                    this.gi += this.hi;
                }
                if ((var4_18 & 2) == 2) {
                    this.th += this.uh;
                }
            }
            if (this.gi < -60) {
                this.hi = 2;
            }
            if (this.gi > 60) {
                this.hi = -2;
            }
            if (this.th < -20) {
                this.uh = 1;
            }
            if (this.th > 10) {
                this.uh = -1;
            }
            ++this.oe;
            if (this.oe > 50) {
                this.Ph.a((byte)6, 0);
            }
            try {
                if (this.rh != null && this.Ph.z > 0) {
                    this.rh.a(this.Ph.z, 0, this.Ph.y, 0);
                    this.Ph.z = 0;
                    this.oe = 0;
                    return;
                }
            }
            catch (IOException v2) {
                this.x(-670);
                return;
            }
            catch (Exception var4_19) {
                this.d(true);
                return;
            }
        }
        catch (RuntimeException var2_4) {
            signlink.reporterror("71747, " + var1_1 + ", " + var2_4.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void v(int n) {
        int n2 = Jj;
        try {
            DYMVKFXP dYMVKFXP = (DYMVKFXP)this.Ch.b();
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                int n3 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n2 == 0) continue;
                    ++n3;
                } while (n3 > 0);
            } while (n >= 0);
            boolean bl3 = true;
            do {
                block8: {
                    block7: {
                        if (bl3 && !(bl3 = false) && n2 == 0) continue;
                        if (dYMVKFXP.i != -1) break block7;
                        dYMVKFXP.q = 0;
                        this.a(false, dYMVKFXP);
                        if (n2 == 0) break block8;
                    }
                    dYMVKFXP.a();
                }
                dYMVKFXP = (DYMVKFXP)this.Ch.a(false);
            } while (dYMVKFXP != null);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("6061, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void w(int n) {
        try {
            if (this.ig != null) {
                return;
            }
            this.m = null;
            this.ph = null;
            this.nh = null;
            this.mh = null;
            this.oh = null;
            this.yg = null;
            this.zg = null;
            this.Ag = null;
            this.lg = new IVIFZQBK(128, 265, this.f(0), 0);
            AFCKELYG.a(this.di);
            this.mg = new IVIFZQBK(128, 265, this.f(0), 0);
            AFCKELYG.a(this.di);
            this.ig = new IVIFZQBK(509, 171, this.f(0), 0);
            AFCKELYG.a(this.di);
            this.jg = new IVIFZQBK(360, 132, this.f(0), 0);
            AFCKELYG.a(this.di);
            this.kg = new IVIFZQBK(360, 200, this.f(0), 0);
            AFCKELYG.a(this.di);
            this.ng = new IVIFZQBK(202, 238, this.f(0), 0);
            if (n < 0 || n > 0) {
                this.N = null;
            }
            AFCKELYG.a(this.di);
            this.og = new IVIFZQBK(203, 238, this.f(0), 0);
            AFCKELYG.a(this.di);
            this.pg = new IVIFZQBK(74, 94, this.f(0), 0);
            AFCKELYG.a(this.di);
            this.qg = new IVIFZQBK(75, 94, this.f(0), 0);
            AFCKELYG.a(this.di);
            if (this.ff != null) {
                this.r(0);
                this.o(215);
            }
            this.aj = true;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("33128, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, byte by, String string) {
        try {
            this.Gf = n;
            this.bf = string;
            this.w(0);
            if (this.ff == null) {
                super.a(n, (byte)4, string);
                return;
            }
            this.kg.a(0);
            int n2 = 360;
            int n3 = 200;
            int n4 = 20;
            this.rj.a(0xFFFFFF, "RuneScape is loading - please wait...", 23693, n3 / 2 - 26 - n4, n2 / 2);
            int n5 = n3 / 2 - 18 - n4;
            AFCKELYG.a(n2 / 2 - 152, 304, 34, 0x8C1111, n5, true);
            AFCKELYG.a(n2 / 2 - 151, 302, 32, 0, n5 + 1, true);
            AFCKELYG.a(30, n5 + 2, n2 / 2 - 150, 0x8C1111, n * 3, 0);
            AFCKELYG.a(30, n5 + 2, n2 / 2 - 150 + n * 3, 0, 300 - n * 3, 0);
            this.rj.a(0xFFFFFF, string, 23693, n3 / 2 + 5 - n4, n2 / 2);
            this.kg.a(171, 23680, this.l, 202);
            if (by != 4) {
                int n6 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && Jj == 0) continue;
                    ++n6;
                } while (n6 > 0);
            }
            if (!this.aj) return;
            this.aj = false;
            if (!this.R) {
                this.lg.a(0, 23680, this.l, 0);
                this.mg.a(0, 23680, this.l, 637);
            }
            this.ig.a(0, 23680, this.l, 128);
            this.jg.a(371, 23680, this.l, 202);
            this.ng.a(265, 23680, this.l, 0);
            this.og.a(265, 23680, this.l, 562);
            this.pg.a(171, 23680, this.l, 128);
            this.qg.a(171, 23680, this.l, 562);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("47669, " + n + ", " + by + ", " + string + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(int n, int n2, int n3, int n4, DUCMKFAY dUCMKFAY, int n5, boolean bl, int n6, int n7) {
        block17: {
            try {
                block19: {
                    block18: {
                        if (!this.Cd) break block18;
                        this.Wd = 32;
                        if (Jj == 0) break block19;
                    }
                    this.Wd = 0;
                }
                this.Cd = false;
                this.le += n7;
                if (n3 >= n && n3 < n + 16 && n4 >= n5 && n4 < n5 + 16) {
                    dUCMKFAY.r -= this.ki * 4;
                    if (bl) {
                        this.ch = true;
                        return;
                    }
                } else if (n3 >= n && n3 < n + 16 && n4 >= n5 + n2 - 16 && n4 < n5 + n2) {
                    dUCMKFAY.r += this.ki * 4;
                    if (bl) {
                        this.ch = true;
                        return;
                    }
                } else {
                    if (n3 < n - this.Wd || n3 >= n + 16 + this.Wd || n4 < n5 + 16 || n4 >= n5 + n2 - 16 || this.ki <= 0) break block17;
                    int n8 = (n2 - 32) * n2 / n6;
                    if (n8 < 8) {
                        n8 = 8;
                    }
                    int n9 = n4 - n5 - 16 - n8 / 2;
                    int n10 = n2 - 32 - n8;
                    dUCMKFAY.r = (n6 - n2) * n9 / n10;
                    if (bl) {
                        this.ch = true;
                    }
                    this.Cd = true;
                }
                return;
            }
            catch (RuntimeException runtimeException) {
                signlink.reporterror("45715, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + dUCMKFAY + ", " + n5 + ", " + bl + ", " + n6 + ", " + n7 + ", " + runtimeException.toString());
                throw new RuntimeException();
            }
        }
    }

    public final boolean a(int n, int n2, int n3, int n4) {
        int n5 = Jj;
        try {
            block14: {
                int n6;
                int n7;
                block11: {
                    int n8;
                    int n9;
                    YZDBYLRM yZDBYLRM;
                    block13: {
                        block12: {
                            int n10 = n >> 14 & Short.MAX_VALUE;
                            int n11 = this.cd.g(this.Ac, n3, n2, n);
                            if (n4 >= 0) {
                                throw new NullPointerException();
                            }
                            if (n11 == -1) {
                                return false;
                            }
                            n7 = n11 & 0x1F;
                            n6 = n11 >> 6 & 3;
                            if (n7 != 10 && n7 != 11 && n7 != 22) break block11;
                            yZDBYLRM = YZDBYLRM.a(n10);
                            if (n6 != 0 && n6 != 2) break block12;
                            n9 = yZDBYLRM.i;
                            n8 = yZDBYLRM.z;
                            if (n5 == 0) break block13;
                        }
                        n9 = yZDBYLRM.z;
                        n8 = yZDBYLRM.i;
                    }
                    int n12 = yZDBYLRM.G;
                    if (n6 != 0) {
                        n12 = (n12 << n6 & 0xF) + (n12 >> 4 - n6);
                    }
                    this.a(2, 0, n8, -11308, 0, client.Bg.n[0], n9, n12, n2, client.Bg.m[0], false, n3);
                    if (n5 == 0) break block14;
                }
                this.a(2, n6, 0, -11308, n7 + 1, client.Bg.n[0], 0, 0, n2, client.Bg.m[0], false, n3);
            }
            this.wc = this.A;
            this.xc = this.B;
            this.zc = 2;
            this.yc = 0;
            return true;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("61218, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final XTGLDHGX a(int n, String string, String string2, int n2, byte by, int n3) {
        int n4 = Jj;
        try {
            Object object;
            int n5;
            byte[] byArray = null;
            int n6 = 5;
            try {
                if (this.Ad[0] != null) {
                    byArray = this.Ad[0].a(true, n);
                }
            }
            catch (Exception exception) {}
            if (byArray != null) {
                this.Mc.reset();
                this.Mc.update(byArray);
                n5 = (int)this.Mc.getValue();
                if (n5 != n2) {
                    byArray = null;
                }
            }
            if (byArray != null) {
                return new XTGLDHGX(44820, byArray);
            }
            n5 = 0;
            boolean bl = true;
            do {
                block34: {
                    if (bl && !(bl = false) && n4 == 0) continue;
                    object = "Unknown error";
                    this.a(n3, (byte)4, "Requesting " + string);
                    DataInputStream dataInputStream = null;
                    try {
                        int n7;
                        int n8 = 0;
                        dataInputStream = this.b(String.valueOf(string2) + n2);
                        byte[] byArray2 = new byte[6];
                        dataInputStream.readFully(byArray2, 0, 6);
                        MBMGIXGO mBMGIXGO = new MBMGIXGO(byArray2, 891);
                        mBMGIXGO.z = 3;
                        int n9 = mBMGIXGO.g() + 6;
                        int n10 = 6;
                        byArray = new byte[n9];
                        int n11 = 0;
                        boolean bl2 = true;
                        do {
                            if (bl2 && !(bl2 = false) && n4 == 0) continue;
                            byArray[n11] = byArray2[n11];
                            ++n11;
                        } while (n11 < 6);
                        boolean bl3 = true;
                        do {
                            int n12;
                            if (bl3 && !(bl3 = false) && n4 == 0) continue;
                            n7 = n9 - n10;
                            if (n7 > 1000) {
                                n7 = 1000;
                            }
                            if ((n12 = dataInputStream.read(byArray, n10, n7)) < 0) {
                                object = "Length error: " + n10 + "/" + n9;
                                throw new IOException("EOF");
                            }
                            int n13 = (n10 += n12) * 100 / n9;
                            if (n13 != n8) {
                                this.a(n3, (byte)4, "Loading " + string + " - " + n13 + "%");
                            }
                            n8 = n13;
                        } while (n10 < n9);
                        dataInputStream.close();
                        try {
                            if (this.Ad[0] != null) {
                                this.Ad[0].a(byArray.length, byArray, (byte)2, n);
                            }
                        }
                        catch (Exception exception) {
                            this.Ad[0] = null;
                        }
                        if (byArray == null) break block34;
                        this.Mc.reset();
                        this.Mc.update(byArray);
                        n7 = (int)this.Mc.getValue();
                        if (n7 != n2) {
                            byArray = null;
                            ++n5;
                            object = "Checksum error: " + n7;
                        }
                    }
                    catch (IOException iOException) {
                        if (((String)object).equals("Unknown error")) {
                            object = "Connection error";
                        }
                        byArray = null;
                    }
                    catch (NullPointerException nullPointerException) {
                        object = "Null error";
                        byArray = null;
                        if (!signlink.reporterror) {
                            return null;
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                        object = "Bounds error";
                        byArray = null;
                        if (!signlink.reporterror) {
                            return null;
                        }
                    }
                    catch (Exception exception) {
                        object = "Unexpected error";
                        byArray = null;
                        if (signlink.reporterror) break block34;
                        return null;
                    }
                }
                if (byArray != null) continue;
                int n14 = n6;
                boolean bl4 = true;
                do {
                    block36: {
                        block35: {
                            if (bl4 && !(bl4 = false) && n4 == 0) continue;
                            if (n5 < 3) break block35;
                            this.a(n3, (byte)4, "Game updated - please reload page");
                            n14 = 10;
                            if (n4 == 0) break block36;
                        }
                        this.a(n3, (byte)4, String.valueOf(object) + " - Retrying in " + n14);
                    }
                    try {
                        Thread.sleep(1000L);
                    }
                    catch (Exception exception) {}
                    --n14;
                } while (n14 > 0);
                if ((n6 *= 2) > 60) {
                    n6 = 60;
                }
                boolean bl5 = this.Gb = !this.Gb;
            } while (byArray == null);
            object = new XTGLDHGX(44820, byArray);
            if (by == -41) return object;
            throw new NullPointerException();
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("44021, " + n + ", " + string + ", " + string2 + ", " + n2 + ", " + by + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void x(int n) {
        try {
            if (this.pe > 0) {
                this.d(true);
                return;
            }
            this.oh.a(0);
            this.qj.a(0, "Connection lost", 23693, 144, 257);
            this.qj.a(0xFFFFFF, "Connection lost", 23693, 143, 256);
            this.qj.a(0, "Please wait - attempting to reestablish", 23693, 159, 257);
            this.qj.a(0xFFFFFF, "Please wait - attempting to reestablish", 23693, 158, 256);
            boolean bl = true;
            do {
                if (bl && !(bl = false) && Jj == 0) continue;
                this.Ph.a(164);
            } while (n >= 0);
            this.oh.a(4, 23680, this.l, 4);
            this.ze = 0;
            this.gj = 0;
            NQABEVLK nQABEVLK = this.rh;
            this.gh = false;
            this.Qe = 0;
            this.a(this.wh, this.xh, true);
            if (!this.gh) {
                this.d(true);
            }
            try {
                nQABEVLK.a();
                return;
            }
            catch (Exception exception) {
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("43851, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void b(int n, boolean bl) {
        int n2 = Jj;
        try {
            block157: {
                String string;
                block159: {
                    DJRMEMXO dJRMEMXO;
                    block158: {
                        int n3;
                        int n4;
                        int n5;
                        int n6;
                        block154: {
                            String string2;
                            block156: {
                                YZDBYLRM yZDBYLRM;
                                block155: {
                                    block150: {
                                        String string3;
                                        block152: {
                                            DJRMEMXO dJRMEMXO2;
                                            block153: {
                                                block151: {
                                                    CWNCPMLX cWNCPMLX;
                                                    String string4;
                                                    int n7;
                                                    DLZHLHNK dLZHLHNK;
                                                    int n8;
                                                    long l;
                                                    block143: {
                                                        block149: {
                                                            String string5;
                                                            int n9;
                                                            DLZHLHNK dLZHLHNK2;
                                                            DLZHLHNK dLZHLHNK3;
                                                            DLZHLHNK dLZHLHNK4;
                                                            CWNCPMLX cWNCPMLX2;
                                                            block146: {
                                                                String string6;
                                                                block148: {
                                                                    CKDEJADD cKDEJADD;
                                                                    block147: {
                                                                        CWNCPMLX cWNCPMLX3;
                                                                        CWNCPMLX cWNCPMLX4;
                                                                        CWNCPMLX cWNCPMLX5;
                                                                        CWNCPMLX cWNCPMLX6;
                                                                        int n10;
                                                                        Object object;
                                                                        block144: {
                                                                            block145: {
                                                                                if (n < 0) {
                                                                                    return;
                                                                                }
                                                                                if (this.wi != 0) {
                                                                                    this.wi = 0;
                                                                                    this.ui = true;
                                                                                }
                                                                                n6 = this.Sf[n];
                                                                                n5 = this.Tf[n];
                                                                                n4 = this.Uf[n];
                                                                                n3 = this.Vf[n];
                                                                                if (n4 >= 2000) {
                                                                                    n4 -= 2000;
                                                                                }
                                                                                if (n4 == 582 && (object = this.V[n3]) != null) {
                                                                                    this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, ((GQOSZKJC)object).n[0], client.Bg.m[0], false, ((GQOSZKJC)object).m[0]);
                                                                                    this.wc = this.A;
                                                                                    this.xc = this.B;
                                                                                    this.zc = 2;
                                                                                    this.yc = 0;
                                                                                    this.Ph.a((byte)6, 57);
                                                                                    this.Ph.f(-431, this.Ej);
                                                                                    this.Ph.f(-431, n3);
                                                                                    this.Ph.b(true, this.Cj);
                                                                                    this.Ph.f(-431, this.Dj);
                                                                                }
                                                                                if (n4 == 234) {
                                                                                    boolean bl2 = this.a(2, 0, 0, -11308, 0, client.Bg.n[0], 0, 0, n5, client.Bg.m[0], false, n6);
                                                                                    if (!bl2) {
                                                                                        bl2 = this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, n5, client.Bg.m[0], false, n6);
                                                                                    }
                                                                                    this.wc = this.A;
                                                                                    this.xc = this.B;
                                                                                    this.zc = 2;
                                                                                    this.yc = 0;
                                                                                    this.Ph.a((byte)6, 236);
                                                                                    this.Ph.b(true, n5 + this.Ne);
                                                                                    this.Ph.b(n3);
                                                                                    this.Ph.b(true, n6 + this.Me);
                                                                                }
                                                                                if (n4 == 62 && this.a(n3, n5, n6, -770)) {
                                                                                    this.Ph.a((byte)6, 192);
                                                                                    this.Ph.b(this.Dj);
                                                                                    this.Ph.b(true, n3 >> 14 & Short.MAX_VALUE);
                                                                                    this.Ph.g(0, n5 + this.Ne);
                                                                                    this.Ph.b(true, this.Cj);
                                                                                    this.Ph.g(0, n6 + this.Me);
                                                                                    this.Ph.b(this.Ej);
                                                                                }
                                                                                if (n4 == 511) {
                                                                                    boolean bl3 = this.a(2, 0, 0, -11308, 0, client.Bg.n[0], 0, 0, n5, client.Bg.m[0], false, n6);
                                                                                    if (!bl3) {
                                                                                        bl3 = this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, n5, client.Bg.m[0], false, n6);
                                                                                    }
                                                                                    this.wc = this.A;
                                                                                    this.xc = this.B;
                                                                                    this.zc = 2;
                                                                                    this.yc = 0;
                                                                                    this.Ph.a((byte)6, 25);
                                                                                    this.Ph.b(true, this.Dj);
                                                                                    this.Ph.f(-431, this.Ej);
                                                                                    this.Ph.b(n3);
                                                                                    this.Ph.f(-431, n5 + this.Ne);
                                                                                    this.Ph.g(0, this.Cj);
                                                                                    this.Ph.b(n6 + this.Me);
                                                                                }
                                                                                if (n4 == 74) {
                                                                                    this.Ph.a((byte)6, 122);
                                                                                    this.Ph.g(0, n5);
                                                                                    this.Ph.f(-431, n6);
                                                                                    this.Ph.b(true, n3);
                                                                                    this.Oi = 0;
                                                                                    this.Pi = n5;
                                                                                    this.Qi = n6;
                                                                                    this.Ri = 2;
                                                                                    if (DUCMKFAY.d[n5].D == this.rb) {
                                                                                        this.Ri = 1;
                                                                                    }
                                                                                    if (DUCMKFAY.d[n5].D == this.vj) {
                                                                                        this.Ri = 3;
                                                                                    }
                                                                                }
                                                                                if (n4 == 315) {
                                                                                    object = DUCMKFAY.d[n5];
                                                                                    n10 = 1;
                                                                                    if (((DUCMKFAY)object).h > 0) {
                                                                                        n10 = this.a(505, (DUCMKFAY)object);
                                                                                    }
                                                                                    if (n10 != 0) {
                                                                                        this.Ph.a((byte)6, 185);
                                                                                        this.Ph.b(n5);
                                                                                    }
                                                                                }
                                                                                if (n4 == 561 && (object = this.Yb[n3]) != null) {
                                                                                    this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, ((GQOSZKJC)object).n[0], client.Bg.m[0], false, ((GQOSZKJC)object).m[0]);
                                                                                    this.wc = this.A;
                                                                                    this.xc = this.B;
                                                                                    this.zc = 2;
                                                                                    this.yc = 0;
                                                                                    if ((Lh += n3) >= 90) {
                                                                                        this.Ph.a((byte)6, 136);
                                                                                        Lh = 0;
                                                                                    }
                                                                                    this.Ph.a((byte)6, 128);
                                                                                    this.Ph.b(n3);
                                                                                }
                                                                                if (n4 == 20 && (object = this.V[n3]) != null) {
                                                                                    this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, ((GQOSZKJC)object).n[0], client.Bg.m[0], false, ((GQOSZKJC)object).m[0]);
                                                                                    this.wc = this.A;
                                                                                    this.xc = this.B;
                                                                                    this.zc = 2;
                                                                                    this.yc = 0;
                                                                                    this.Ph.a((byte)6, 155);
                                                                                    this.Ph.b(true, n3);
                                                                                }
                                                                                if (n4 == 779 && (object = this.Yb[n3]) != null) {
                                                                                    this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, ((GQOSZKJC)object).n[0], client.Bg.m[0], false, ((GQOSZKJC)object).m[0]);
                                                                                    this.wc = this.A;
                                                                                    this.xc = this.B;
                                                                                    this.zc = 2;
                                                                                    this.yc = 0;
                                                                                    this.Ph.a((byte)6, 153);
                                                                                    this.Ph.b(true, n3);
                                                                                }
                                                                                if (n4 != 516) break block144;
                                                                                if (this.Tb) break block145;
                                                                                this.cd.a(false, this.B - 4, this.A - 4);
                                                                                if (n2 == 0) break block144;
                                                                            }
                                                                            this.cd.a(false, n5 - 4, n6 - 4);
                                                                        }
                                                                        if (n4 == 1062) {
                                                                            if ((Gc += this.Me) >= 113) {
                                                                                this.Ph.a((byte)6, 183);
                                                                                this.Ph.c(15086193);
                                                                                Gc = 0;
                                                                            }
                                                                            this.a(n3, n5, n6, -770);
                                                                            this.Ph.a((byte)6, 228);
                                                                            this.Ph.f(-431, n3 >> 14 & Short.MAX_VALUE);
                                                                            this.Ph.f(-431, n5 + this.Ne);
                                                                            this.Ph.b(n6 + this.Me);
                                                                        }
                                                                        if (n4 == 679 && !this.Yg) {
                                                                            this.Ph.a((byte)6, 40);
                                                                            this.Ph.b(n5);
                                                                            this.Yg = true;
                                                                        }
                                                                        if (n4 == 431) {
                                                                            this.Ph.a((byte)6, 129);
                                                                            this.Ph.f(-431, n6);
                                                                            this.Ph.b(n5);
                                                                            this.Ph.f(-431, n3);
                                                                            this.Oi = 0;
                                                                            this.Pi = n5;
                                                                            this.Qi = n6;
                                                                            this.Ri = 2;
                                                                            if (DUCMKFAY.d[n5].D == this.rb) {
                                                                                this.Ri = 1;
                                                                            }
                                                                            if (DUCMKFAY.d[n5].D == this.vj) {
                                                                                this.Ri = 3;
                                                                            }
                                                                        }
                                                                        if ((n4 == 337 || n4 == 42 || n4 == 792 || n4 == 322) && (n10 = ((String)(object = this.Wh[n])).indexOf("@whi@")) != -1) {
                                                                            l = ZTQFNQRH.a(((String)object).substring(n10 + 5).trim());
                                                                            if (n4 == 337) {
                                                                                this.a((byte)68, l);
                                                                            }
                                                                            if (n4 == 42) {
                                                                                this.a(l, 4);
                                                                            }
                                                                            if (n4 == 792) {
                                                                                this.a(false, l);
                                                                            }
                                                                            if (n4 == 322) {
                                                                                this.a(3, l);
                                                                            }
                                                                        }
                                                                        if (n4 == 53) {
                                                                            this.Ph.a((byte)6, 135);
                                                                            this.Ph.b(true, n6);
                                                                            this.Ph.f(-431, n5);
                                                                            this.Ph.b(true, n3);
                                                                            this.Oi = 0;
                                                                            this.Pi = n5;
                                                                            this.Qi = n6;
                                                                            this.Ri = 2;
                                                                            if (DUCMKFAY.d[n5].D == this.rb) {
                                                                                this.Ri = 1;
                                                                            }
                                                                            if (DUCMKFAY.d[n5].D == this.vj) {
                                                                                this.Ri = 3;
                                                                            }
                                                                        }
                                                                        if (n4 == 539) {
                                                                            this.Ph.a((byte)6, 16);
                                                                            this.Ph.f(-431, n3);
                                                                            this.Ph.g(0, n6);
                                                                            this.Ph.g(0, n5);
                                                                            this.Oi = 0;
                                                                            this.Pi = n5;
                                                                            this.Qi = n6;
                                                                            this.Ri = 2;
                                                                            if (DUCMKFAY.d[n5].D == this.rb) {
                                                                                this.Ri = 1;
                                                                            }
                                                                            if (DUCMKFAY.d[n5].D == this.vj) {
                                                                                this.Ri = 3;
                                                                            }
                                                                        }
                                                                        if ((n4 == 484 || n4 == 6) && (n10 = ((String)(object = this.Wh[n])).indexOf("@whi@")) != -1) {
                                                                            object = ((String)object).substring(n10 + 5).trim();
                                                                            String string7 = ZTQFNQRH.a(-45804, ZTQFNQRH.a(ZTQFNQRH.a((String)object), (byte)-99));
                                                                            boolean bl4 = false;
                                                                            n8 = 0;
                                                                            boolean bl5 = true;
                                                                            do {
                                                                                if (bl5 && !(bl5 = false) && n2 == 0) continue;
                                                                                DLZHLHNK dLZHLHNK5 = this.Yb[this.ac[n8]];
                                                                                if (dLZHLHNK5 != null && dLZHLHNK5.yb != null && dLZHLHNK5.yb.equalsIgnoreCase(string7)) {
                                                                                    this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, dLZHLHNK5.n[0], client.Bg.m[0], false, dLZHLHNK5.m[0]);
                                                                                    if (n4 == 484) {
                                                                                        this.Ph.a((byte)6, 139);
                                                                                        this.Ph.b(true, this.ac[n8]);
                                                                                    }
                                                                                    if (n4 == 6) {
                                                                                        if ((Lh += n3) >= 90) {
                                                                                            this.Ph.a((byte)6, 136);
                                                                                            Lh = 0;
                                                                                        }
                                                                                        this.Ph.a((byte)6, 128);
                                                                                        this.Ph.b(this.ac[n8]);
                                                                                    }
                                                                                    bl4 = true;
                                                                                    if (n2 == 0) break;
                                                                                }
                                                                                ++n8;
                                                                            } while (n8 < this.Zb);
                                                                            if (!bl4) {
                                                                                this.a("Unable to find " + string7, 0, "", this.Vd);
                                                                            }
                                                                        }
                                                                        if (n4 == 870) {
                                                                            this.Ph.a((byte)6, 53);
                                                                            this.Ph.b(n6);
                                                                            this.Ph.f(-431, this.Cj);
                                                                            this.Ph.g(0, n3);
                                                                            this.Ph.b(this.Dj);
                                                                            this.Ph.b(true, this.Ej);
                                                                            this.Ph.b(n5);
                                                                            this.Oi = 0;
                                                                            this.Pi = n5;
                                                                            this.Qi = n6;
                                                                            this.Ri = 2;
                                                                            if (DUCMKFAY.d[n5].D == this.rb) {
                                                                                this.Ri = 1;
                                                                            }
                                                                            if (DUCMKFAY.d[n5].D == this.vj) {
                                                                                this.Ri = 3;
                                                                            }
                                                                        }
                                                                        if (n4 == 847) {
                                                                            this.Ph.a((byte)6, 87);
                                                                            this.Ph.f(-431, n3);
                                                                            this.Ph.b(n5);
                                                                            this.Ph.f(-431, n6);
                                                                            this.Oi = 0;
                                                                            this.Pi = n5;
                                                                            this.Qi = n6;
                                                                            this.Ri = 2;
                                                                            if (DUCMKFAY.d[n5].D == this.rb) {
                                                                                this.Ri = 1;
                                                                            }
                                                                            if (DUCMKFAY.d[n5].D == this.vj) {
                                                                                this.Ri = 3;
                                                                            }
                                                                        }
                                                                        if (n4 == 626) {
                                                                            String string8;
                                                                            object = DUCMKFAY.d[n5];
                                                                            this.Lg = 1;
                                                                            this.Mg = n5;
                                                                            this.Ng = ((DUCMKFAY)object).E;
                                                                            this.Bj = 0;
                                                                            this.ch = true;
                                                                            String string9 = ((DUCMKFAY)object).p;
                                                                            if (string9.indexOf(" ") != -1) {
                                                                                string9 = string9.substring(0, string9.indexOf(" "));
                                                                            }
                                                                            if ((string8 = ((DUCMKFAY)object).p).indexOf(" ") != -1) {
                                                                                string8 = string8.substring(string8.indexOf(" ") + 1);
                                                                            }
                                                                            this.Og = String.valueOf(string9) + " " + ((DUCMKFAY)object).l + " " + string8;
                                                                            if (this.Ng == 16) {
                                                                                this.ch = true;
                                                                                this.si = 3;
                                                                                this.eg = true;
                                                                            }
                                                                            return;
                                                                        }
                                                                        if (n4 == 78) {
                                                                            this.Ph.a((byte)6, 117);
                                                                            this.Ph.g(0, n5);
                                                                            this.Ph.g(0, n3);
                                                                            this.Ph.b(true, n6);
                                                                            this.Oi = 0;
                                                                            this.Pi = n5;
                                                                            this.Qi = n6;
                                                                            this.Ri = 2;
                                                                            if (DUCMKFAY.d[n5].D == this.rb) {
                                                                                this.Ri = 1;
                                                                            }
                                                                            if (DUCMKFAY.d[n5].D == this.vj) {
                                                                                this.Ri = 3;
                                                                            }
                                                                        }
                                                                        if (n4 == 27 && (object = this.Yb[n3]) != null) {
                                                                            this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, ((GQOSZKJC)object).n[0], client.Bg.m[0], false, ((GQOSZKJC)object).m[0]);
                                                                            this.wc = this.A;
                                                                            this.xc = this.B;
                                                                            this.zc = 2;
                                                                            this.yc = 0;
                                                                            if ((Qd += n3) >= 54) {
                                                                                this.Ph.a((byte)6, 189);
                                                                                this.Ph.a(234);
                                                                                Qd = 0;
                                                                            }
                                                                            this.Ph.a((byte)6, 73);
                                                                            this.Ph.b(true, n3);
                                                                        }
                                                                        if (n4 == 213) {
                                                                            boolean bl6 = this.a(2, 0, 0, -11308, 0, client.Bg.n[0], 0, 0, n5, client.Bg.m[0], false, n6);
                                                                            if (!bl6) {
                                                                                bl6 = this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, n5, client.Bg.m[0], false, n6);
                                                                            }
                                                                            this.wc = this.A;
                                                                            this.xc = this.B;
                                                                            this.zc = 2;
                                                                            this.yc = 0;
                                                                            this.Ph.a((byte)6, 79);
                                                                            this.Ph.b(true, n5 + this.Ne);
                                                                            this.Ph.b(n3);
                                                                            this.Ph.f(-431, n6 + this.Me);
                                                                        }
                                                                        if (n4 == 632) {
                                                                            this.Ph.a((byte)6, 145);
                                                                            this.Ph.f(-431, n5);
                                                                            this.Ph.f(-431, n6);
                                                                            this.Ph.f(-431, n3);
                                                                            this.Oi = 0;
                                                                            this.Pi = n5;
                                                                            this.Qi = n6;
                                                                            this.Ri = 2;
                                                                            if (DUCMKFAY.d[n5].D == this.rb) {
                                                                                this.Ri = 1;
                                                                            }
                                                                            if (DUCMKFAY.d[n5].D == this.vj) {
                                                                                this.Ri = 3;
                                                                            }
                                                                        }
                                                                        if (n4 == 493) {
                                                                            this.Ph.a((byte)6, 75);
                                                                            this.Ph.g(0, n5);
                                                                            this.Ph.b(true, n6);
                                                                            this.Ph.f(-431, n3);
                                                                            this.Oi = 0;
                                                                            this.Pi = n5;
                                                                            this.Qi = n6;
                                                                            this.Ri = 2;
                                                                            if (DUCMKFAY.d[n5].D == this.rb) {
                                                                                this.Ri = 1;
                                                                            }
                                                                            if (DUCMKFAY.d[n5].D == this.vj) {
                                                                                this.Ri = 3;
                                                                            }
                                                                        }
                                                                        if (n4 == 652) {
                                                                            boolean bl7 = this.a(2, 0, 0, -11308, 0, client.Bg.n[0], 0, 0, n5, client.Bg.m[0], false, n6);
                                                                            if (!bl7) {
                                                                                bl7 = this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, n5, client.Bg.m[0], false, n6);
                                                                            }
                                                                            this.wc = this.A;
                                                                            this.xc = this.B;
                                                                            this.zc = 2;
                                                                            this.yc = 0;
                                                                            this.Ph.a((byte)6, 156);
                                                                            this.Ph.f(-431, n6 + this.Me);
                                                                            this.Ph.b(true, n5 + this.Ne);
                                                                            this.Ph.g(0, n3);
                                                                        }
                                                                        if (n4 == 94) {
                                                                            boolean bl8 = this.a(2, 0, 0, -11308, 0, client.Bg.n[0], 0, 0, n5, client.Bg.m[0], false, n6);
                                                                            if (!bl8) {
                                                                                bl8 = this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, n5, client.Bg.m[0], false, n6);
                                                                            }
                                                                            this.wc = this.A;
                                                                            this.xc = this.B;
                                                                            this.zc = 2;
                                                                            this.yc = 0;
                                                                            this.Ph.a((byte)6, 181);
                                                                            this.Ph.b(true, n5 + this.Ne);
                                                                            this.Ph.b(n3);
                                                                            this.Ph.b(true, n6 + this.Me);
                                                                            this.Ph.f(-431, this.Mg);
                                                                        }
                                                                        if (n4 == 646) {
                                                                            this.Ph.a((byte)6, 185);
                                                                            this.Ph.b(n5);
                                                                            DUCMKFAY dUCMKFAY = DUCMKFAY.d[n5];
                                                                            if (dUCMKFAY.t != null && dUCMKFAY.t[0][0] == 5 && this.Bd[n10 = dUCMKFAY.t[0][1]] != dUCMKFAY.f[0]) {
                                                                                this.Bd[n10] = dUCMKFAY.f[0];
                                                                                this.d(false, n10);
                                                                                this.ch = true;
                                                                            }
                                                                        }
                                                                        if (n4 == 225 && (cWNCPMLX6 = this.V[n3]) != null) {
                                                                            this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, cWNCPMLX6.n[0], client.Bg.m[0], false, cWNCPMLX6.m[0]);
                                                                            this.wc = this.A;
                                                                            this.xc = this.B;
                                                                            this.zc = 2;
                                                                            this.yc = 0;
                                                                            if ((xi += n3) >= 85) {
                                                                                this.Ph.a((byte)6, 230);
                                                                                this.Ph.a(239);
                                                                                xi = 0;
                                                                            }
                                                                            this.Ph.a((byte)6, 17);
                                                                            this.Ph.g(0, n3);
                                                                        }
                                                                        if (n4 == 965 && (cWNCPMLX5 = this.V[n3]) != null) {
                                                                            this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, cWNCPMLX5.n[0], client.Bg.m[0], false, cWNCPMLX5.m[0]);
                                                                            this.wc = this.A;
                                                                            this.xc = this.B;
                                                                            this.zc = 2;
                                                                            this.yc = 0;
                                                                            if (++Jg >= 96) {
                                                                                this.Ph.a((byte)6, 152);
                                                                                this.Ph.a(88);
                                                                                Jg = 0;
                                                                            }
                                                                            this.Ph.a((byte)6, 21);
                                                                            this.Ph.b(n3);
                                                                        }
                                                                        if (n4 == 413 && (cWNCPMLX4 = this.V[n3]) != null) {
                                                                            this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, cWNCPMLX4.n[0], client.Bg.m[0], false, cWNCPMLX4.m[0]);
                                                                            this.wc = this.A;
                                                                            this.xc = this.B;
                                                                            this.zc = 2;
                                                                            this.yc = 0;
                                                                            this.Ph.a((byte)6, 131);
                                                                            this.Ph.g(0, n3);
                                                                            this.Ph.f(-431, this.Mg);
                                                                        }
                                                                        if (n4 == 200) {
                                                                            this.M(537);
                                                                        }
                                                                        if (n4 != 1025 || (cWNCPMLX3 = this.V[n3]) == null) break block146;
                                                                        cKDEJADD = cWNCPMLX3.vb;
                                                                        if (cKDEJADD.H != null) {
                                                                            cKDEJADD = cKDEJADD.b(this.Lb);
                                                                        }
                                                                        if (cKDEJADD == null) break block146;
                                                                        if (cKDEJADD.I == null) break block147;
                                                                        string6 = new String(cKDEJADD.I);
                                                                        if (n2 == 0) break block148;
                                                                    }
                                                                    string6 = "It's a " + cKDEJADD.k + ".";
                                                                }
                                                                this.a(string6, 0, "", this.Vd);
                                                            }
                                                            if (n4 == 900) {
                                                                this.a(n3, n5, n6, -770);
                                                                this.Ph.a((byte)6, 252);
                                                                this.Ph.g(0, n3 >> 14 & Short.MAX_VALUE);
                                                                this.Ph.b(true, n5 + this.Ne);
                                                                this.Ph.f(-431, n6 + this.Me);
                                                            }
                                                            if (n4 == 412 && (cWNCPMLX2 = this.V[n3]) != null) {
                                                                this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, cWNCPMLX2.n[0], client.Bg.m[0], false, cWNCPMLX2.m[0]);
                                                                this.wc = this.A;
                                                                this.xc = this.B;
                                                                this.zc = 2;
                                                                this.yc = 0;
                                                                this.Ph.a((byte)6, 72);
                                                                this.Ph.f(-431, n3);
                                                            }
                                                            if (n4 == 365 && (dLZHLHNK4 = this.Yb[n3]) != null) {
                                                                this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, dLZHLHNK4.n[0], client.Bg.m[0], false, dLZHLHNK4.m[0]);
                                                                this.wc = this.A;
                                                                this.xc = this.B;
                                                                this.zc = 2;
                                                                this.yc = 0;
                                                                this.Ph.a((byte)6, 249);
                                                                this.Ph.f(-431, n3);
                                                                this.Ph.b(true, this.Mg);
                                                            }
                                                            if (n4 == 729 && (dLZHLHNK3 = this.Yb[n3]) != null) {
                                                                this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, dLZHLHNK3.n[0], client.Bg.m[0], false, dLZHLHNK3.m[0]);
                                                                this.wc = this.A;
                                                                this.xc = this.B;
                                                                this.zc = 2;
                                                                this.yc = 0;
                                                                this.Ph.a((byte)6, 39);
                                                                this.Ph.b(true, n3);
                                                            }
                                                            if (n4 == 577 && (dLZHLHNK2 = this.Yb[n3]) != null) {
                                                                this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, dLZHLHNK2.n[0], client.Bg.m[0], false, dLZHLHNK2.m[0]);
                                                                this.wc = this.A;
                                                                this.xc = this.B;
                                                                this.zc = 2;
                                                                this.yc = 0;
                                                                this.Ph.a((byte)6, 139);
                                                                this.Ph.b(true, n3);
                                                            }
                                                            if (n4 == 956 && this.a(n3, n5, n6, -770)) {
                                                                this.Ph.a((byte)6, 35);
                                                                this.Ph.b(true, n6 + this.Me);
                                                                this.Ph.f(-431, this.Mg);
                                                                this.Ph.f(-431, n5 + this.Ne);
                                                                this.Ph.b(true, n3 >> 14 & Short.MAX_VALUE);
                                                            }
                                                            if (n4 == 567) {
                                                                boolean bl9 = this.a(2, 0, 0, -11308, 0, client.Bg.n[0], 0, 0, n5, client.Bg.m[0], false, n6);
                                                                if (!bl9) {
                                                                    bl9 = this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, n5, client.Bg.m[0], false, n6);
                                                                }
                                                                this.wc = this.A;
                                                                this.xc = this.B;
                                                                this.zc = 2;
                                                                this.yc = 0;
                                                                this.Ph.a((byte)6, 23);
                                                                this.Ph.b(true, n5 + this.Ne);
                                                                this.Ph.b(true, n3);
                                                                this.Ph.b(true, n6 + this.Me);
                                                            }
                                                            if (n4 == 867) {
                                                                if ((n3 & 3) == 0) {
                                                                    ++yh;
                                                                }
                                                                if (yh >= 59) {
                                                                    this.Ph.a((byte)6, 200);
                                                                    this.Ph.b(25501);
                                                                    yh = 0;
                                                                }
                                                                this.Ph.a((byte)6, 43);
                                                                this.Ph.b(true, n5);
                                                                this.Ph.f(-431, n3);
                                                                this.Ph.f(-431, n6);
                                                                this.Oi = 0;
                                                                this.Pi = n5;
                                                                this.Qi = n6;
                                                                this.Ri = 2;
                                                                if (DUCMKFAY.d[n5].D == this.rb) {
                                                                    this.Ri = 1;
                                                                }
                                                                if (DUCMKFAY.d[n5].D == this.vj) {
                                                                    this.Ri = 3;
                                                                }
                                                            }
                                                            if (n4 == 543) {
                                                                this.Ph.a((byte)6, 237);
                                                                this.Ph.b(n6);
                                                                this.Ph.f(-431, n3);
                                                                this.Ph.b(n5);
                                                                this.Ph.f(-431, this.Mg);
                                                                this.Oi = 0;
                                                                this.Pi = n5;
                                                                this.Qi = n6;
                                                                this.Ri = 2;
                                                                if (DUCMKFAY.d[n5].D == this.rb) {
                                                                    this.Ri = 1;
                                                                }
                                                                if (DUCMKFAY.d[n5].D == this.vj) {
                                                                    this.Ri = 3;
                                                                }
                                                            }
                                                            if (n4 != 606 || (n9 = (string5 = this.Wh[n]).indexOf("@whi@")) == -1) break block143;
                                                            if (this.rb != -1) break block149;
                                                            this.M(537);
                                                            this.Pb = string5.substring(n9 + 5).trim();
                                                            this.hh = false;
                                                            int n11 = 0;
                                                            boolean bl10 = true;
                                                            do {
                                                                if (bl10 && !(bl10 = false) && n2 == 0) continue;
                                                                if (DUCMKFAY.d[n11] != null && DUCMKFAY.d[n11].h == 600) {
                                                                    this.Bh = this.rb = DUCMKFAY.d[n11].D;
                                                                    if (n2 == 0) break block143;
                                                                }
                                                                ++n11;
                                                            } while (n11 < DUCMKFAY.d.length);
                                                            if (n2 == 0) break block143;
                                                        }
                                                        this.a("Please close the interface you have open before using 'report abuse'", 0, "", this.Vd);
                                                    }
                                                    if (n4 == 491 && (dLZHLHNK = this.Yb[n3]) != null) {
                                                        this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, dLZHLHNK.n[0], client.Bg.m[0], false, dLZHLHNK.m[0]);
                                                        this.wc = this.A;
                                                        this.xc = this.B;
                                                        this.zc = 2;
                                                        this.yc = 0;
                                                        this.Ph.a((byte)6, 14);
                                                        this.Ph.f(-431, this.Dj);
                                                        this.Ph.b(n3);
                                                        this.Ph.b(this.Ej);
                                                        this.Ph.b(true, this.Cj);
                                                    }
                                                    if (n4 == 639 && (n7 = (string4 = this.Wh[n]).indexOf("@whi@")) != -1) {
                                                        l = ZTQFNQRH.a(string4.substring(n7 + 5).trim());
                                                        n8 = -1;
                                                        int n12 = 0;
                                                        boolean bl11 = true;
                                                        do {
                                                            if (bl11 && !(bl11 = false) && n2 == 0) continue;
                                                            if (this.ld[n12] == l) {
                                                                n8 = n12;
                                                                if (n2 == 0) break;
                                                            }
                                                            ++n12;
                                                        } while (n12 < this.hc);
                                                        if (n8 != -1 && this.M[n8] > 0) {
                                                            this.ui = true;
                                                            this.wi = 0;
                                                            this.bj = true;
                                                            this.ji = "";
                                                            this.rf = 3;
                                                            this.jd = this.ld[n8];
                                                            this.wg = "Enter message to send to " + this.Jf[n8];
                                                        }
                                                    }
                                                    if (n4 == 454) {
                                                        this.Ph.a((byte)6, 41);
                                                        this.Ph.b(n3);
                                                        this.Ph.f(-431, n6);
                                                        this.Ph.f(-431, n5);
                                                        this.Oi = 0;
                                                        this.Pi = n5;
                                                        this.Qi = n6;
                                                        this.Ri = 2;
                                                        if (DUCMKFAY.d[n5].D == this.rb) {
                                                            this.Ri = 1;
                                                        }
                                                        if (DUCMKFAY.d[n5].D == this.vj) {
                                                            this.Ri = 3;
                                                        }
                                                    }
                                                    if (n4 == 478 && (cWNCPMLX = this.V[n3]) != null) {
                                                        this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, cWNCPMLX.n[0], client.Bg.m[0], false, cWNCPMLX.m[0]);
                                                        this.wc = this.A;
                                                        this.xc = this.B;
                                                        this.zc = 2;
                                                        this.yc = 0;
                                                        if ((n3 & 3) == 0) {
                                                            ++eh;
                                                        }
                                                        if (eh >= 53) {
                                                            this.Ph.a((byte)6, 85);
                                                            this.Ph.a(66);
                                                            eh = 0;
                                                        }
                                                        this.Ph.a((byte)6, 18);
                                                        this.Ph.b(true, n3);
                                                    }
                                                    if (n4 == 113) {
                                                        this.a(n3, n5, n6, -770);
                                                        this.Ph.a((byte)6, 70);
                                                        this.Ph.b(true, n6 + this.Me);
                                                        this.Ph.b(n5 + this.Ne);
                                                        this.Ph.g(0, n3 >> 14 & Short.MAX_VALUE);
                                                    }
                                                    if (n4 == 872) {
                                                        this.a(n3, n5, n6, -770);
                                                        this.Ph.a((byte)6, 234);
                                                        this.Ph.g(0, n6 + this.Me);
                                                        this.Ph.f(-431, n3 >> 14 & Short.MAX_VALUE);
                                                        this.Ph.g(0, n5 + this.Ne);
                                                    }
                                                    if (n4 == 502) {
                                                        this.a(n3, n5, n6, -770);
                                                        this.Ph.a((byte)6, 132);
                                                        this.Ph.g(0, n6 + this.Me);
                                                        this.Ph.b(n3 >> 14 & Short.MAX_VALUE);
                                                        this.Ph.f(-431, n5 + this.Ne);
                                                    }
                                                    if (n4 != 1125) break block150;
                                                    dJRMEMXO2 = DJRMEMXO.b(n3);
                                                    DUCMKFAY dUCMKFAY = DUCMKFAY.d[n5];
                                                    if (dUCMKFAY == null || dUCMKFAY.T[n6] < 100000) break block151;
                                                    string3 = String.valueOf(dUCMKFAY.T[n6]) + " x " + dJRMEMXO2.q;
                                                    if (n2 == 0) break block152;
                                                }
                                                if (dJRMEMXO2.y == null) break block153;
                                                string3 = new String(dJRMEMXO2.y);
                                                if (n2 == 0) break block152;
                                            }
                                            string3 = "It's a " + dJRMEMXO2.q + ".";
                                        }
                                        this.a(string3, 0, "", this.Vd);
                                    }
                                    if (n4 == 169) {
                                        this.Ph.a((byte)6, 185);
                                        this.Ph.b(n5);
                                        DUCMKFAY dUCMKFAY = DUCMKFAY.d[n5];
                                        if (dUCMKFAY.t != null && dUCMKFAY.t[0][0] == 5) {
                                            int n13 = dUCMKFAY.t[0][1];
                                            this.Bd[n13] = 1 - this.Bd[n13];
                                            this.d(false, n13);
                                            this.ch = true;
                                        }
                                    }
                                    if (n4 == 447) {
                                        this.Bj = 1;
                                        this.Cj = n6;
                                        this.Dj = n5;
                                        this.Ej = n3;
                                        this.Fj = DJRMEMXO.b((int)n3).q;
                                        this.Lg = 0;
                                        this.ch = true;
                                        return;
                                    }
                                    if (n4 != 1226) break block154;
                                    int n14 = n3 >> 14 & Short.MAX_VALUE;
                                    yZDBYLRM = YZDBYLRM.a(n14);
                                    if (yZDBYLRM.P == null) break block155;
                                    string2 = new String(yZDBYLRM.P);
                                    if (n2 == 0) break block156;
                                }
                                string2 = "It's a " + yZDBYLRM.d + ".";
                            }
                            this.a(string2, 0, "", this.Vd);
                        }
                        if (n4 == 244) {
                            boolean bl12 = this.a(2, 0, 0, -11308, 0, client.Bg.n[0], 0, 0, n5, client.Bg.m[0], false, n6);
                            if (!bl12) {
                                bl12 = this.a(2, 0, 1, -11308, 0, client.Bg.n[0], 1, 0, n5, client.Bg.m[0], false, n6);
                            }
                            this.wc = this.A;
                            this.xc = this.B;
                            this.zc = 2;
                            this.yc = 0;
                            this.Ph.a((byte)6, 253);
                            this.Ph.b(true, n6 + this.Me);
                            this.Ph.g(0, n5 + this.Ne);
                            this.Ph.f(-431, n3);
                        }
                        if (n4 != 1448) break block157;
                        dJRMEMXO = DJRMEMXO.b(n3);
                        if (dJRMEMXO.y == null) break block158;
                        string = new String(dJRMEMXO.y);
                        if (n2 == 0) break block159;
                    }
                    string = "It's a " + dJRMEMXO.q + ".";
                }
                this.a(string, 0, "", this.Vd);
            }
            this.Bj = 0;
            if (bl) {
                return;
            }
            this.Lg = 0;
            this.ch = true;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("53, " + n + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void y(int n) {
        try {
            this.Wi = 0;
            int n2 = (client.Bg.kb >> 7) + this.Me;
            int n3 = (client.Bg.lb >> 7) + this.Ne;
            n = 58 / n;
            if (n2 >= 3053 && n2 <= 3156 && n3 >= 3056 && n3 <= 3136) {
                this.Wi = 1;
            }
            if (n2 >= 3072 && n2 <= 3118 && n3 >= 9492 && n3 <= 9535) {
                this.Wi = 1;
            }
            if (this.Wi == 1 && n2 >= 3139 && n2 <= 3199 && n3 >= 3008 && n3 <= 3062) {
                this.Wi = 0;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("12723, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void run() {
        if (this.Ob) {
            this.l((byte)59);
            return;
        }
        super.run();
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void z(int n) {
        int n2 = Jj;
        try {
            if (this.Bj == 0 && this.Lg == 0) {
                this.Wh[this.Ig] = "Walk here";
                this.Uf[this.Ig] = 516;
                this.Sf[this.Ig] = this.t;
                this.Tf[this.Ig] = this.u;
                ++this.Ig;
            }
            int n3 = -1;
            int n4 = 0;
            boolean bl = true;
            do {
                block39: {
                    DLZHLHNK dLZHLHNK;
                    int n5;
                    Object object;
                    int n6;
                    int n7;
                    int n8;
                    int n9;
                    block40: {
                        int n10;
                        block42: {
                            block41: {
                                if (bl && !(bl = false) && n2 == 0) continue;
                                n10 = ZKARKDQW.Ib[n4];
                                n9 = n10 & 0x7F;
                                n8 = n10 >> 7 & 0x7F;
                                n7 = n10 >> 29 & 3;
                                n6 = n10 >> 14 & Short.MAX_VALUE;
                                if (n10 == n3) break block39;
                                n3 = n10;
                                if (n7 != 2 || this.cd.g(this.Ac, n9, n8, n10) < 0) break block40;
                                object = YZDBYLRM.a(n6);
                                if (((YZDBYLRM)object).x != null) {
                                    object = ((YZDBYLRM)object).b(true);
                                }
                                if (object == null) break block39;
                                if (this.Bj != 1) break block41;
                                this.Wh[this.Ig] = "Use " + this.Fj + " with @cya@" + ((YZDBYLRM)object).d;
                                this.Uf[this.Ig] = 62;
                                this.Vf[this.Ig] = n10;
                                this.Sf[this.Ig] = n9;
                                this.Tf[this.Ig] = n8;
                                ++this.Ig;
                                if (n2 == 0) break block40;
                            }
                            if (this.Lg != 1) break block42;
                            if ((this.Ng & 4) != 4) break block40;
                            this.Wh[this.Ig] = String.valueOf(this.Og) + " @cya@" + ((YZDBYLRM)object).d;
                            this.Uf[this.Ig] = 956;
                            this.Vf[this.Ig] = n10;
                            this.Sf[this.Ig] = n9;
                            this.Tf[this.Ig] = n8;
                            ++this.Ig;
                            if (n2 == 0) break block40;
                        }
                        if (((YZDBYLRM)object).Y != null) {
                            n5 = 4;
                            boolean bl2 = true;
                            do {
                                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                                if (((YZDBYLRM)object).Y[n5] != null) {
                                    this.Wh[this.Ig] = String.valueOf(((YZDBYLRM)object).Y[n5]) + " @cya@" + ((YZDBYLRM)object).d;
                                    if (n5 == 0) {
                                        this.Uf[this.Ig] = 502;
                                    }
                                    if (n5 == 1) {
                                        this.Uf[this.Ig] = 900;
                                    }
                                    if (n5 == 2) {
                                        this.Uf[this.Ig] = 113;
                                    }
                                    if (n5 == 3) {
                                        this.Uf[this.Ig] = 872;
                                    }
                                    if (n5 == 4) {
                                        this.Uf[this.Ig] = 1062;
                                    }
                                    this.Vf[this.Ig] = n10;
                                    this.Sf[this.Ig] = n9;
                                    this.Tf[this.Ig] = n8;
                                    ++this.Ig;
                                }
                                --n5;
                            } while (n5 >= 0);
                        }
                        this.Wh[this.Ig] = "Examine @cya@" + ((YZDBYLRM)object).d;
                        this.Uf[this.Ig] = 1226;
                        this.Vf[this.Ig] = ((YZDBYLRM)object).s << 14;
                        this.Sf[this.Ig] = n9;
                        this.Tf[this.Ig] = n8;
                        ++this.Ig;
                    }
                    if (n7 == 1) {
                        object = this.V[n6];
                        if (((CWNCPMLX)object).vb.n == 1 && (((GQOSZKJC)object).kb & 0x7F) == 64 && (((GQOSZKJC)object).lb & 0x7F) == 64) {
                            n5 = 0;
                            boolean bl3 = true;
                            do {
                                if (bl3 && !(bl3 = false) && n2 == 0) continue;
                                CWNCPMLX cWNCPMLX = this.V[this.X[n5]];
                                if (cWNCPMLX != null && cWNCPMLX != object && cWNCPMLX.vb.n == 1 && cWNCPMLX.kb == ((GQOSZKJC)object).kb && cWNCPMLX.lb == ((GQOSZKJC)object).lb) {
                                    this.a(cWNCPMLX.vb, this.X[n5], false, n8, n9);
                                }
                                ++n5;
                            } while (n5 < this.W);
                            int n11 = 0;
                            boolean bl4 = true;
                            do {
                                if (bl4 && !(bl4 = false) && n2 == 0) continue;
                                dLZHLHNK = this.Yb[this.ac[n11]];
                                if (dLZHLHNK != null && dLZHLHNK.kb == ((GQOSZKJC)object).kb && dLZHLHNK.lb == ((GQOSZKJC)object).lb) {
                                    this.a(n9, this.ac[n11], dLZHLHNK, false, n8);
                                }
                                ++n11;
                            } while (n11 < this.Zb);
                        }
                        this.a(((CWNCPMLX)object).vb, n6, false, n8, n9);
                    }
                    if (n7 == 0) {
                        object = this.Yb[n6];
                        if ((((GQOSZKJC)object).kb & 0x7F) == 64 && (((GQOSZKJC)object).lb & 0x7F) == 64) {
                            n5 = 0;
                            boolean bl5 = true;
                            do {
                                if (bl5 && !(bl5 = false) && n2 == 0) continue;
                                CWNCPMLX cWNCPMLX = this.V[this.X[n5]];
                                if (cWNCPMLX != null && cWNCPMLX.vb.n == 1 && cWNCPMLX.kb == ((GQOSZKJC)object).kb && cWNCPMLX.lb == ((GQOSZKJC)object).lb) {
                                    this.a(cWNCPMLX.vb, this.X[n5], false, n8, n9);
                                }
                                ++n5;
                            } while (n5 < this.W);
                            int n12 = 0;
                            boolean bl6 = true;
                            do {
                                if (bl6 && !(bl6 = false) && n2 == 0) continue;
                                dLZHLHNK = this.Yb[this.ac[n12]];
                                if (dLZHLHNK != null && dLZHLHNK != object && dLZHLHNK.kb == ((GQOSZKJC)object).kb && dLZHLHNK.lb == ((GQOSZKJC)object).lb) {
                                    this.a(n9, this.ac[n12], dLZHLHNK, false, n8);
                                }
                                ++n12;
                            } while (n12 < this.Zb);
                        }
                        this.a(n9, n6, (DLZHLHNK)object, false, n8);
                    }
                    if (n7 != 3 || (object = this.N[this.Ac][n9][n8]) == null) break block39;
                    HNKCWGJM hNKCWGJM = (HNKCWGJM)((LHGXPZPG)object).a(5);
                    boolean bl7 = true;
                    do {
                        block44: {
                            DJRMEMXO dJRMEMXO;
                            block45: {
                                block43: {
                                    if (bl7 && !(bl7 = false) && n2 == 0) continue;
                                    dJRMEMXO = DJRMEMXO.b(hNKCWGJM.m);
                                    if (this.Bj != 1) break block43;
                                    this.Wh[this.Ig] = "Use " + this.Fj + " with @lre@" + dJRMEMXO.q;
                                    this.Uf[this.Ig] = 511;
                                    this.Vf[this.Ig] = hNKCWGJM.m;
                                    this.Sf[this.Ig] = n9;
                                    this.Tf[this.Ig] = n8;
                                    ++this.Ig;
                                    if (n2 == 0) break block44;
                                }
                                if (this.Lg != 1) break block45;
                                if ((this.Ng & 1) != 1) break block44;
                                this.Wh[this.Ig] = String.valueOf(this.Og) + " @lre@" + dJRMEMXO.q;
                                this.Uf[this.Ig] = 94;
                                this.Vf[this.Ig] = hNKCWGJM.m;
                                this.Sf[this.Ig] = n9;
                                this.Tf[this.Ig] = n8;
                                ++this.Ig;
                                if (n2 == 0) break block44;
                            }
                            int n13 = 4;
                            boolean bl8 = true;
                            do {
                                block47: {
                                    block46: {
                                        if (bl8 && !(bl8 = false) && n2 == 0) continue;
                                        if (dJRMEMXO.o == null || dJRMEMXO.o[n13] == null) break block46;
                                        this.Wh[this.Ig] = String.valueOf(dJRMEMXO.o[n13]) + " @lre@" + dJRMEMXO.q;
                                        if (n13 == 0) {
                                            this.Uf[this.Ig] = 652;
                                        }
                                        if (n13 == 1) {
                                            this.Uf[this.Ig] = 567;
                                        }
                                        if (n13 == 2) {
                                            this.Uf[this.Ig] = 234;
                                        }
                                        if (n13 == 3) {
                                            this.Uf[this.Ig] = 244;
                                        }
                                        if (n13 == 4) {
                                            this.Uf[this.Ig] = 213;
                                        }
                                        this.Vf[this.Ig] = hNKCWGJM.m;
                                        this.Sf[this.Ig] = n9;
                                        this.Tf[this.Ig] = n8;
                                        ++this.Ig;
                                        if (n2 == 0) break block47;
                                    }
                                    if (n13 == 2) {
                                        this.Wh[this.Ig] = "Take @lre@" + dJRMEMXO.q;
                                        this.Uf[this.Ig] = 234;
                                        this.Vf[this.Ig] = hNKCWGJM.m;
                                        this.Sf[this.Ig] = n9;
                                        this.Tf[this.Ig] = n8;
                                        ++this.Ig;
                                    }
                                }
                                --n13;
                            } while (n13 >= 0);
                            this.Wh[this.Ig] = "Examine @lre@" + dJRMEMXO.q;
                            this.Uf[this.Ig] = 1448;
                            this.Vf[this.Ig] = hNKCWGJM.m;
                            this.Sf[this.Ig] = n9;
                            this.Tf[this.Ig] = n8;
                            ++this.Ig;
                        }
                        hNKCWGJM = (HNKCWGJM)((LHGXPZPG)object).b(8);
                    } while (hNKCWGJM != null);
                }
                ++n4;
            } while (n4 < ZKARKDQW.Hb);
            if (n == 33660) return;
            this.me = this.Kf.c();
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("26026, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void d(int n) {
        int n2 = Jj;
        try {
            signlink.reporterror = false;
            try {
                if (this.rh != null) {
                    this.rh.a();
                }
            }
            catch (Exception exception) {}
            this.rh = null;
            this.g(860);
            if (this.Nb != null) {
                this.Nb.d = false;
            }
            this.Nb = null;
            this.vf.b();
            this.vf = null;
            this.U = null;
            this.Ph = null;
            this.hb = null;
            this.Kf = null;
            this.Fi = null;
            this.Gh = null;
            this.Si = null;
            this.Gi = null;
            this.Hi = null;
            this.li = null;
            this.dj = null;
            this.cd = null;
            this.Bi = null;
            this.jc = null;
            this.L = null;
            this.zj = null;
            this.Aj = null;
            this.uc = null;
            this.mh = null;
            this.nh = null;
            this.oh = null;
            this.ph = null;
            this.yg = null;
            this.zg = null;
            this.Ag = null;
            this.lc = null;
            this.mc = null;
            this.nc = null;
            this.oc = null;
            this.pc = null;
            this.qc = null;
            this.rc = null;
            this.sc = null;
            this.tc = null;
            this.Th = null;
            this.Uh = null;
            this.Vh = null;
            this.Fe = null;
            this.Ge = null;
            this.He = null;
            this.dd = null;
            this.Sg = null;
            this.Tg = null;
            this.Ug = null;
            this.Vg = null;
            this.Wg = null;
            this.zb = null;
            this.Ab = null;
            this.Bb = null;
            this.Cb = null;
            this.Db = null;
            this.xg = null;
            this.Rd = null;
            this.Wf = null;
            this.Zg = null;
            this.Bf = null;
            this.Cf = null;
            this.Df = null;
            this.Ef = null;
            this.Ff = null;
            this.nf = null;
            this.Le = null;
            this.Lc = null;
            this.Yb = null;
            this.ac = null;
            this.cc = null;
            this.dc = null;
            this.ab = null;
            this.V = null;
            this.X = null;
            this.N = null;
            this.Ch = null;
            n = 55 / n;
            this.re = null;
            this.jf = null;
            this.Sf = null;
            this.Tf = null;
            this.Uf = null;
            this.Vf = null;
            this.Wh = null;
            this.Bd = null;
            this.zf = null;
            this.Af = null;
            this.Pg = null;
            this.ij = null;
            this.Jf = null;
            this.ld = null;
            this.M = null;
            this.lg = null;
            this.mg = null;
            this.ig = null;
            this.jg = null;
            this.kg = null;
            this.ng = null;
            this.og = null;
            this.pg = null;
            this.qg = null;
            this.I(3);
            YZDBYLRM.b(-501);
            CKDEJADD.c(-501);
            DJRMEMXO.a(-501);
            MNHKFPQO.d = null;
            TAVAECED.c = null;
            DUCMKFAY.d = null;
            OIBEELAZ.a = null;
            LKGEGIEW.d = null;
            MUDLUUBC.d = null;
            MUDLUUBC.p = null;
            VGXVBFVC.d = null;
            this.m = null;
            DLZHLHNK.zb = null;
            OPPOFIOL.b(-501);
            NYFUGYQS.a(-501);
            ZKARKDQW.b(-501);
            SQHJOGRT.b(-501);
            System.gc();
            if (PKVMXVTO.e) {
                Jj = ++n2;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("35760, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void g(byte by) {
        try {
            block8: {
                block7: {
                    System.out.println("============");
                    System.out.println("flame-cycle:" + this.fi);
                    if (this.vf != null) {
                        System.out.println("Od-cycle:" + this.vf.m);
                    }
                    System.out.println("loop-cycle:" + kh);
                    System.out.println("draw-cycle:" + of);
                    System.out.println("ptype:" + this.me);
                    if (by != 1) break block7;
                    by = 0;
                    if (Jj == 0) break block8;
                }
                this.rd = 281;
            }
            System.out.println("psize:" + this.le);
            if (this.rh != null) {
                this.rh.a((byte)1);
            }
            this.i = true;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("16696, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final Component f(int n) {
        try {
            this.le += n;
            if (signlink.mainapp != null) {
                return signlink.mainapp;
            }
            if (this.o != null) {
                return this.o;
            }
            return this;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("60139, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void A(int n) {
        int n2 = Jj;
        try {
            int n3;
            n = 55 / n;
            while ((n3 = this.b(-796)) != -1) {
                block52: {
                    block71: {
                        block72: {
                            block70: {
                                int n4;
                                int n5;
                                block66: {
                                    String string;
                                    block69: {
                                        block68: {
                                            block67: {
                                                block65: {
                                                    block54: {
                                                        block64: {
                                                            block63: {
                                                                block62: {
                                                                    block61: {
                                                                        block60: {
                                                                            block59: {
                                                                                block58: {
                                                                                    block57: {
                                                                                        block56: {
                                                                                            block55: {
                                                                                                block53: {
                                                                                                    block51: {
                                                                                                        if (this.rb != -1 && this.rb == this.Bh) {
                                                                                                            if (n3 == 8 && this.Pb.length() > 0) {
                                                                                                                this.Pb = this.Pb.substring(0, this.Pb.length() - 1);
                                                                                                            }
                                                                                                            if (!(n3 >= 97 && n3 <= 122 || n3 >= 65 && n3 <= 90 || n3 >= 48 && n3 <= 57) && n3 != 32 || this.Pb.length() >= 12) continue;
                                                                                                            this.Pb = String.valueOf(this.Pb) + (char)n3;
                                                                                                            if (n2 == 0) continue;
                                                                                                        }
                                                                                                        if (this.bj) {
                                                                                                            long l;
                                                                                                            if (n3 >= 32 && n3 <= 122 && this.ji.length() < 80) {
                                                                                                                this.ji = String.valueOf(this.ji) + (char)n3;
                                                                                                                this.ui = true;
                                                                                                            }
                                                                                                            if (n3 == 8 && this.ji.length() > 0) {
                                                                                                                this.ji = this.ji.substring(0, this.ji.length() - 1);
                                                                                                                this.ui = true;
                                                                                                            }
                                                                                                            if (n3 != 13 && n3 != 10) continue;
                                                                                                            this.bj = false;
                                                                                                            this.ui = true;
                                                                                                            if (this.rf == 1) {
                                                                                                                l = ZTQFNQRH.a(this.ji);
                                                                                                                this.a((byte)68, l);
                                                                                                            }
                                                                                                            if (this.rf == 2 && this.hc > 0) {
                                                                                                                l = ZTQFNQRH.a(this.ji);
                                                                                                                this.a(false, l);
                                                                                                            }
                                                                                                            if (this.rf == 3 && this.ji.length() > 0) {
                                                                                                                this.Ph.a((byte)6, 126);
                                                                                                                this.Ph.a(0);
                                                                                                                int n6 = this.Ph.z;
                                                                                                                this.Ph.a(5, this.jd);
                                                                                                                RTHTIIVA.a(this.ji, this.wj, this.Ph);
                                                                                                                this.Ph.a(this.Ph.z - n6, (byte)0);
                                                                                                                this.ji = RTHTIIVA.a(this.ji, 0);
                                                                                                                this.ji = RKAYAFDQ.a(this.ji, 0);
                                                                                                                this.a(this.ji, 6, ZTQFNQRH.a(-45804, ZTQFNQRH.a(this.jd, (byte)-99)), this.Vd);
                                                                                                                if (this.fb == 2) {
                                                                                                                    this.fb = 1;
                                                                                                                    this.Ei = true;
                                                                                                                    this.Ph.a((byte)6, 95);
                                                                                                                    this.Ph.a(this.Gj);
                                                                                                                    this.Ph.a(this.fb);
                                                                                                                    this.Ph.a(this.Ti);
                                                                                                                }
                                                                                                            }
                                                                                                            if (this.rf == 4 && this.I < 100) {
                                                                                                                long l2 = ZTQFNQRH.a(this.ji);
                                                                                                                this.a(l2, 4);
                                                                                                            }
                                                                                                            if (this.rf != 5 || this.I <= 0) continue;
                                                                                                            long l3 = ZTQFNQRH.a(this.ji);
                                                                                                            this.a(3, l3);
                                                                                                            if (n2 == 0) continue;
                                                                                                        }
                                                                                                        if (this.wi == 1) {
                                                                                                            if (n3 >= 48 && n3 <= 57 && this.ie.length() < 10) {
                                                                                                                this.ie = String.valueOf(this.ie) + (char)n3;
                                                                                                                this.ui = true;
                                                                                                            }
                                                                                                            if (n3 == 8 && this.ie.length() > 0) {
                                                                                                                this.ie = this.ie.substring(0, this.ie.length() - 1);
                                                                                                                this.ui = true;
                                                                                                            }
                                                                                                            if (n3 != 13 && n3 != 10) continue;
                                                                                                            if (this.ie.length() > 0) {
                                                                                                                int n7 = 0;
                                                                                                                try {
                                                                                                                    n7 = Integer.parseInt(this.ie);
                                                                                                                }
                                                                                                                catch (Exception exception) {}
                                                                                                                this.Ph.a((byte)6, 208);
                                                                                                                this.Ph.d(n7);
                                                                                                            }
                                                                                                            this.wi = 0;
                                                                                                            this.ui = true;
                                                                                                            if (n2 == 0) continue;
                                                                                                        }
                                                                                                        if (this.wi == 2) {
                                                                                                            if (n3 >= 32 && n3 <= 122 && this.ie.length() < 12) {
                                                                                                                this.ie = String.valueOf(this.ie) + (char)n3;
                                                                                                                this.ui = true;
                                                                                                            }
                                                                                                            if (n3 == 8 && this.ie.length() > 0) {
                                                                                                                this.ie = this.ie.substring(0, this.ie.length() - 1);
                                                                                                                this.ui = true;
                                                                                                            }
                                                                                                            if (n3 != 13 && n3 != 10) continue;
                                                                                                            if (this.ie.length() > 0) {
                                                                                                                this.Ph.a((byte)6, 60);
                                                                                                                this.Ph.a(5, ZTQFNQRH.a(this.ie));
                                                                                                            }
                                                                                                            this.wi = 0;
                                                                                                            this.ui = true;
                                                                                                            if (n2 == 0) continue;
                                                                                                        }
                                                                                                        if (this.vj != -1) continue;
                                                                                                        if (n3 >= 32 && n3 <= 122 && this.Vb.length() < 80) {
                                                                                                            this.Vb = String.valueOf(this.Vb) + (char)n3;
                                                                                                            this.ui = true;
                                                                                                        }
                                                                                                        if (n3 == 8 && this.Vb.length() > 0) {
                                                                                                            this.Vb = this.Vb.substring(0, this.Vb.length() - 1);
                                                                                                            this.ui = true;
                                                                                                        }
                                                                                                        if (n3 != 13 && n3 != 10 || this.Vb.length() <= 0) continue;
                                                                                                        if (this.xb == 2) {
                                                                                                            if (this.Vb.equals("::clientdrop")) {
                                                                                                                this.x(-670);
                                                                                                            }
                                                                                                            if (this.Vb.equals("::lag")) {
                                                                                                                this.g((byte)1);
                                                                                                            }
                                                                                                            if (this.Vb.equals("::prefetchmusic")) {
                                                                                                                int n8 = 0;
                                                                                                                boolean bl = true;
                                                                                                                do {
                                                                                                                    if (bl && !(bl = false) && n2 == 0) continue;
                                                                                                                    this.vf.a((byte)1, 2, n8, (byte)8);
                                                                                                                    ++n8;
                                                                                                                } while (n8 < this.vf.a(79, 2));
                                                                                                            }
                                                                                                            if (this.Vb.equals("::fpson")) {
                                                                                                                fh = true;
                                                                                                            }
                                                                                                            if (this.Vb.equals("::fpsoff")) {
                                                                                                                fh = false;
                                                                                                            }
                                                                                                            if (this.Vb.equals("::noclip")) {
                                                                                                                int n9 = 0;
                                                                                                                boolean bl = true;
                                                                                                                do {
                                                                                                                    if (bl && !(bl = false) && n2 == 0) continue;
                                                                                                                    n5 = 1;
                                                                                                                    boolean bl2 = true;
                                                                                                                    do {
                                                                                                                        if (bl2 && !(bl2 = false) && n2 == 0) continue;
                                                                                                                        n4 = 1;
                                                                                                                        boolean bl3 = true;
                                                                                                                        do {
                                                                                                                            if (bl3 && !(bl3 = false) && n2 == 0) continue;
                                                                                                                            this.Bi[n9].m[n5][n4] = 0;
                                                                                                                            ++n4;
                                                                                                                        } while (n4 < 103);
                                                                                                                        ++n5;
                                                                                                                    } while (n5 < 103);
                                                                                                                    ++n9;
                                                                                                                } while (n9 < 4);
                                                                                                            }
                                                                                                        }
                                                                                                        if (!this.Vb.startsWith("::")) break block51;
                                                                                                        this.Ph.a((byte)6, 103);
                                                                                                        this.Ph.a(this.Vb.length() - 1);
                                                                                                        this.Ph.a(this.Vb.substring(2));
                                                                                                        if (n2 == 0) break block52;
                                                                                                    }
                                                                                                    string = this.Vb.toLowerCase();
                                                                                                    n5 = 0;
                                                                                                    if (!string.startsWith("yellow:")) break block53;
                                                                                                    n5 = 0;
                                                                                                    this.Vb = this.Vb.substring(7);
                                                                                                    if (n2 == 0) break block54;
                                                                                                }
                                                                                                if (!string.startsWith("red:")) break block55;
                                                                                                n5 = 1;
                                                                                                this.Vb = this.Vb.substring(4);
                                                                                                if (n2 == 0) break block54;
                                                                                            }
                                                                                            if (!string.startsWith("green:")) break block56;
                                                                                            n5 = 2;
                                                                                            this.Vb = this.Vb.substring(6);
                                                                                            if (n2 == 0) break block54;
                                                                                        }
                                                                                        if (!string.startsWith("cyan:")) break block57;
                                                                                        n5 = 3;
                                                                                        this.Vb = this.Vb.substring(5);
                                                                                        if (n2 == 0) break block54;
                                                                                    }
                                                                                    if (!string.startsWith("purple:")) break block58;
                                                                                    n5 = 4;
                                                                                    this.Vb = this.Vb.substring(7);
                                                                                    if (n2 == 0) break block54;
                                                                                }
                                                                                if (!string.startsWith("white:")) break block59;
                                                                                n5 = 5;
                                                                                this.Vb = this.Vb.substring(6);
                                                                                if (n2 == 0) break block54;
                                                                            }
                                                                            if (!string.startsWith("flash1:")) break block60;
                                                                            n5 = 6;
                                                                            this.Vb = this.Vb.substring(7);
                                                                            if (n2 == 0) break block54;
                                                                        }
                                                                        if (!string.startsWith("flash2:")) break block61;
                                                                        n5 = 7;
                                                                        this.Vb = this.Vb.substring(7);
                                                                        if (n2 == 0) break block54;
                                                                    }
                                                                    if (!string.startsWith("flash3:")) break block62;
                                                                    n5 = 8;
                                                                    this.Vb = this.Vb.substring(7);
                                                                    if (n2 == 0) break block54;
                                                                }
                                                                if (!string.startsWith("glow1:")) break block63;
                                                                n5 = 9;
                                                                this.Vb = this.Vb.substring(6);
                                                                if (n2 == 0) break block54;
                                                            }
                                                            if (!string.startsWith("glow2:")) break block64;
                                                            n5 = 10;
                                                            this.Vb = this.Vb.substring(6);
                                                            if (n2 == 0) break block54;
                                                        }
                                                        if (string.startsWith("glow3:")) {
                                                            n5 = 11;
                                                            this.Vb = this.Vb.substring(6);
                                                        }
                                                    }
                                                    string = this.Vb.toLowerCase();
                                                    n4 = 0;
                                                    if (!string.startsWith("wave:")) break block65;
                                                    n4 = 1;
                                                    this.Vb = this.Vb.substring(5);
                                                    if (n2 == 0) break block66;
                                                }
                                                if (!string.startsWith("wave2:")) break block67;
                                                n4 = 2;
                                                this.Vb = this.Vb.substring(6);
                                                if (n2 == 0) break block66;
                                            }
                                            if (!string.startsWith("shake:")) break block68;
                                            n4 = 3;
                                            this.Vb = this.Vb.substring(6);
                                            if (n2 == 0) break block66;
                                        }
                                        if (!string.startsWith("scroll:")) break block69;
                                        n4 = 4;
                                        this.Vb = this.Vb.substring(7);
                                        if (n2 == 0) break block66;
                                    }
                                    if (string.startsWith("slide:")) {
                                        n4 = 5;
                                        this.Vb = this.Vb.substring(6);
                                    }
                                }
                                this.Ph.a((byte)6, 4);
                                this.Ph.a(0);
                                int n10 = this.Ph.z;
                                this.Ph.e(301, n4);
                                this.Ph.e(301, n5);
                                this.U.z = 0;
                                RTHTIIVA.a(this.Vb, this.wj, this.U);
                                this.Ph.a(0, this.oi, this.U.y, this.U.z);
                                this.Ph.a(this.Ph.z - n10, (byte)0);
                                this.Vb = RTHTIIVA.a(this.Vb, 0);
                                client.Bg.s = this.Vb = RKAYAFDQ.a(this.Vb, 0);
                                client.Bg.z = n5;
                                client.Bg.R = n4;
                                client.Bg.V = 150;
                                if (this.xb != 2) break block70;
                                this.a(client.Bg.s, 2, "@cr2@" + client.Bg.yb, this.Vd);
                                if (n2 == 0) break block71;
                            }
                            if (this.xb != 1) break block72;
                            this.a(client.Bg.s, 2, "@cr1@" + client.Bg.yb, this.Vd);
                            if (n2 == 0) break block71;
                        }
                        this.a(client.Bg.s, 2, client.Bg.yb, this.Vd);
                    }
                    if (this.Gj == 2) {
                        this.Gj = 3;
                        this.Ei = true;
                        this.Ph.a((byte)6, 95);
                        this.Ph.a(this.Gj);
                        this.Ph.a(this.fb);
                        this.Ph.a(this.Ti);
                    }
                }
                this.Vb = "";
                this.ui = true;
                if (n2 == 0) continue;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("88130, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, int n2, int n3) {
        try {
            if (n3 != this.Y) {
                this.Y = this.ee.a();
            }
            int n4 = 0;
            int n5 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && Jj == 0) continue;
                if (this.ad[n5] != null) {
                    int n6 = this.Yc[n5];
                    int n7 = 70 - n4 * 14 + this.Qf + 4;
                    if (n7 < -20) break;
                    String string = this.Zc[n5];
                    int n8 = 0;
                    if (string != null && string.startsWith("@cr1@")) {
                        string = string.substring(5);
                        n8 = 1;
                    }
                    if (string != null && string.startsWith("@cr2@")) {
                        string = string.substring(5);
                        n8 = 2;
                    }
                    if (n6 == 0) {
                        ++n4;
                    }
                    if ((n6 == 1 || n6 == 2) && (n6 == 1 || this.Gj == 0 || this.Gj == 1 && this.a(false, string))) {
                        if (n2 > n7 - 14 && n2 <= n7 && !string.equals(client.Bg.yb)) {
                            if (this.xb >= 1) {
                                this.Wh[this.Ig] = "Report abuse @whi@" + string;
                                this.Uf[this.Ig] = 606;
                                ++this.Ig;
                            }
                            this.Wh[this.Ig] = "Add ignore @whi@" + string;
                            this.Uf[this.Ig] = 42;
                            ++this.Ig;
                            this.Wh[this.Ig] = "Add friend @whi@" + string;
                            this.Uf[this.Ig] = 337;
                            ++this.Ig;
                        }
                        ++n4;
                    }
                    if ((n6 == 3 || n6 == 7) && this.Sh == 0 && (n6 == 7 || this.fb == 0 || this.fb == 1 && this.a(false, string))) {
                        if (n2 > n7 - 14 && n2 <= n7) {
                            if (this.xb >= 1) {
                                this.Wh[this.Ig] = "Report abuse @whi@" + string;
                                this.Uf[this.Ig] = 606;
                                ++this.Ig;
                            }
                            this.Wh[this.Ig] = "Add ignore @whi@" + string;
                            this.Uf[this.Ig] = 42;
                            ++this.Ig;
                            this.Wh[this.Ig] = "Add friend @whi@" + string;
                            this.Uf[this.Ig] = 337;
                            ++this.Ig;
                        }
                        ++n4;
                    }
                    if (n6 == 4 && (this.Ti == 0 || this.Ti == 1 && this.a(false, string))) {
                        if (n2 > n7 - 14 && n2 <= n7) {
                            this.Wh[this.Ig] = "Accept trade @whi@" + string;
                            this.Uf[this.Ig] = 484;
                            ++this.Ig;
                        }
                        ++n4;
                    }
                    if ((n6 == 5 || n6 == 6) && this.Sh == 0 && this.fb < 2) {
                        ++n4;
                    }
                    if (n6 == 8 && (this.Ti == 0 || this.Ti == 1 && this.a(false, string))) {
                        if (n2 > n7 - 14 && n2 <= n7) {
                            this.Wh[this.Ig] = "Accept challenge @whi@" + string;
                            this.Uf[this.Ig] = 6;
                            ++this.Ig;
                        }
                        ++n4;
                    }
                }
                ++n5;
            } while (n5 < 100);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("86869, " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void b(int var1_1, DUCMKFAY var2_2) {
        var10_3 = client.Jj;
        try {
            block101: {
                block104: {
                    block102: {
                        block103: {
                            block94: {
                                block99: {
                                    block100: {
                                        block98: {
                                            block97: {
                                                block95: {
                                                    block96: {
                                                        block89: {
                                                            block90: {
                                                                block92: {
                                                                    block93: {
                                                                        block91: {
                                                                            block86: {
                                                                                block87: {
                                                                                    block88: {
                                                                                        block84: {
                                                                                            block85: {
                                                                                                block78: {
                                                                                                    block82: {
                                                                                                        block83: {
                                                                                                            block81: {
                                                                                                                block80: {
                                                                                                                    block79: {
                                                                                                                        block75: {
                                                                                                                            block77: {
                                                                                                                                block76: {
                                                                                                                                    var3_4 = var2_2.h;
                                                                                                                                    if (var1_1 <= 0) {
                                                                                                                                        this.Ph.a(49);
                                                                                                                                    }
                                                                                                                                    if ((var3_4 < 1 || var3_4 > 100) && (var3_4 < 701 || var3_4 > 800)) break block75;
                                                                                                                                    if (var3_4 == 1 && this.ic == 0) {
                                                                                                                                        var2_2.P = "Loading friend list";
                                                                                                                                        var2_2.k = 0;
                                                                                                                                        return;
                                                                                                                                    }
                                                                                                                                    if (var3_4 == 1 && this.ic == 1) {
                                                                                                                                        var2_2.P = "Connecting to friendserver";
                                                                                                                                        var2_2.k = 0;
                                                                                                                                        return;
                                                                                                                                    }
                                                                                                                                    if (var3_4 == 2 && this.ic != 2) {
                                                                                                                                        var2_2.P = "Please wait...";
                                                                                                                                        var2_2.k = 0;
                                                                                                                                        return;
                                                                                                                                    }
                                                                                                                                    var4_6 = this.hc;
                                                                                                                                    if (this.ic != 2) {
                                                                                                                                        var4_6 = 0;
                                                                                                                                    }
                                                                                                                                    if (var3_4 <= 700) break block76;
                                                                                                                                    var3_4 -= 601;
                                                                                                                                    if (var10_3 == 0) break block77;
                                                                                                                                }
                                                                                                                                --var3_4;
                                                                                                                            }
                                                                                                                            if (var3_4 >= var4_6) {
                                                                                                                                var2_2.P = "";
                                                                                                                                var2_2.k = 0;
                                                                                                                                return;
                                                                                                                            }
                                                                                                                            var2_2.P = this.Jf[var3_4];
                                                                                                                            var2_2.k = 1;
                                                                                                                            return;
                                                                                                                        }
                                                                                                                        if ((var3_4 < 101 || var3_4 > 200) && (var3_4 < 801 || var3_4 > 900)) break block78;
                                                                                                                        var4_7 = this.hc;
                                                                                                                        if (this.ic != 2) {
                                                                                                                            var4_7 = 0;
                                                                                                                        }
                                                                                                                        if (var3_4 <= 800) break block79;
                                                                                                                        var3_4 -= 701;
                                                                                                                        if (var10_3 == 0) break block80;
                                                                                                                    }
                                                                                                                    var3_4 -= 101;
                                                                                                                }
                                                                                                                if (var3_4 >= var4_7) {
                                                                                                                    var2_2.P = "";
                                                                                                                    var2_2.k = 0;
                                                                                                                    return;
                                                                                                                }
                                                                                                                if (this.M[var3_4] != 0) break block81;
                                                                                                                var2_2.P = "@red@Offline";
                                                                                                                if (var10_3 == 0) break block82;
                                                                                                            }
                                                                                                            if (this.M[var3_4] != client.nd) break block83;
                                                                                                            var2_2.P = "@gre@World-" + (this.M[var3_4] - 9);
                                                                                                            if (var10_3 == 0) break block82;
                                                                                                        }
                                                                                                        var2_2.P = "@yel@World-" + (this.M[var3_4] - 9);
                                                                                                    }
                                                                                                    var2_2.k = 1;
                                                                                                    return;
                                                                                                }
                                                                                                if (var3_4 == 203) {
                                                                                                    var4_8 = this.hc;
                                                                                                    if (this.ic != 2) {
                                                                                                        var4_8 = 0;
                                                                                                    }
                                                                                                    var2_2.cb = var4_8 * 15 + 20;
                                                                                                    if (var2_2.cb <= var2_2.ib) {
                                                                                                        var2_2.cb = var2_2.ib + 1;
                                                                                                    }
                                                                                                    return;
                                                                                                }
                                                                                                if (var3_4 >= 401 && var3_4 <= 500) {
                                                                                                    if ((var3_4 -= 401) == 0 && this.ic == 0) {
                                                                                                        var2_2.P = "Loading ignore list";
                                                                                                        var2_2.k = 0;
                                                                                                        return;
                                                                                                    }
                                                                                                    if (var3_4 == 1 && this.ic == 0) {
                                                                                                        var2_2.P = "Please wait...";
                                                                                                        var2_2.k = 0;
                                                                                                        return;
                                                                                                    }
                                                                                                    var4_9 = this.I;
                                                                                                    if (this.ic == 0) {
                                                                                                        var4_9 = 0;
                                                                                                    }
                                                                                                    if (var3_4 >= var4_9) {
                                                                                                        var2_2.P = "";
                                                                                                        var2_2.k = 0;
                                                                                                        return;
                                                                                                    }
                                                                                                    var2_2.P = ZTQFNQRH.a(-45804, ZTQFNQRH.a(this.Hc[var3_4], (byte)-99));
                                                                                                    var2_2.k = 1;
                                                                                                    return;
                                                                                                }
                                                                                                if (var3_4 == 503) {
                                                                                                    var2_2.cb = this.I * 15 + 20;
                                                                                                    if (var2_2.cb <= var2_2.ib) {
                                                                                                        var2_2.cb = var2_2.ib + 1;
                                                                                                    }
                                                                                                    return;
                                                                                                }
                                                                                                if (var3_4 != 327) break block84;
                                                                                                var2_2.lb = 150;
                                                                                                var2_2.mb = (int)(Math.sin((double)client.kh / 40.0) * 256.0) & 2047;
                                                                                                if (!this.Je) break block85;
                                                                                                var4_10 = 0;
                                                                                                if (var10_3 == 0) ** GOTO lbl105
                                                                                                do {
                                                                                                    if ((var5_12 = this.sf[var4_10]) >= 0 && !TAVAECED.c[var5_12].a((byte)2)) {
                                                                                                        return;
                                                                                                    }
                                                                                                    ++var4_10;
lbl105:
                                                                                                    // 2 sources

                                                                                                } while (var4_10 < 7);
                                                                                                this.Je = false;
                                                                                                var5_13 = new ZKARKDQW[7];
                                                                                                var6_14 = 0;
                                                                                                var7_15 = 0;
                                                                                                if (var10_3 == 0) ** GOTO lbl115
                                                                                                do {
                                                                                                    if ((var8_16 = this.sf[var7_15]) >= 0) {
                                                                                                        var5_13[var6_14++] = TAVAECED.c[var8_16].a(false);
                                                                                                    }
                                                                                                    ++var7_15;
lbl115:
                                                                                                    // 2 sources

                                                                                                } while (var7_15 < 7);
                                                                                                var8_17 = new ZKARKDQW(var6_14, var5_13, -38);
                                                                                                var9_18 = 0;
                                                                                                if (var10_3 == 0) ** GOTO lbl125
                                                                                                do {
                                                                                                    if (this.Ud[var9_18] != 0) {
                                                                                                        var8_17.e(client.he[var9_18][0], client.he[var9_18][this.Ud[var9_18]]);
                                                                                                        if (var9_18 == 1) {
                                                                                                            var8_17.e(client.bi[0], client.bi[this.Ud[var9_18]]);
                                                                                                        }
                                                                                                    }
                                                                                                    ++var9_18;
lbl125:
                                                                                                    // 2 sources

                                                                                                } while (var9_18 < 5);
                                                                                                var8_17.a((byte)-71);
                                                                                                var8_17.c(LKGEGIEW.d[client.Bg.x].f[0], 40542);
                                                                                                var8_17.a(64, 850, -30, -50, -30, true);
                                                                                                var2_2.A = 5;
                                                                                                var2_2.B = 0;
                                                                                                DUCMKFAY.a(0, this.Yd, 5, var8_17);
                                                                                            }
                                                                                            return;
                                                                                        }
                                                                                        if (var3_4 == 324) {
                                                                                            if (this.Nc == null) {
                                                                                                this.Nc = var2_2.a;
                                                                                                this.Oc = var2_2.bb;
                                                                                            }
                                                                                            if (this.Ze) {
                                                                                                var2_2.a = this.Oc;
                                                                                                return;
                                                                                            }
                                                                                            var2_2.a = this.Nc;
                                                                                            return;
                                                                                        }
                                                                                        if (var3_4 == 325) {
                                                                                            if (this.Nc == null) {
                                                                                                this.Nc = var2_2.a;
                                                                                                this.Oc = var2_2.bb;
                                                                                            }
                                                                                            if (this.Ze) {
                                                                                                var2_2.a = this.Nc;
                                                                                                return;
                                                                                            }
                                                                                            var2_2.a = this.Oc;
                                                                                            return;
                                                                                        }
                                                                                        if (var3_4 == 600) {
                                                                                            var2_2.P = this.Pb;
                                                                                            if (client.kh % 20 < 10) {
                                                                                                var2_2.P = String.valueOf(var2_2.P) + "|";
                                                                                                return;
                                                                                            }
                                                                                            var2_2.P = String.valueOf(var2_2.P) + " ";
                                                                                            return;
                                                                                        }
                                                                                        if (var3_4 != 613) break block86;
                                                                                        if (this.xb < 1) break block87;
                                                                                        if (!this.hh) break block88;
                                                                                        var2_2.z = 0xFF0000;
                                                                                        var2_2.P = "Moderator option: Mute player for 48 hours: <ON>";
                                                                                        if (var10_3 == 0) break block86;
                                                                                    }
                                                                                    var2_2.z = 0xFFFFFF;
                                                                                    var2_2.P = "Moderator option: Mute player for 48 hours: <OFF>";
                                                                                    if (var10_3 == 0) break block86;
                                                                                }
                                                                                var2_2.P = "";
                                                                            }
                                                                            if (var3_4 != 650 && var3_4 != 655) break block89;
                                                                            if (this.Qh == 0) break block90;
                                                                            if (this.ke != 0) break block91;
                                                                            var4_11 = "earlier today";
                                                                            if (var10_3 == 0) break block92;
                                                                        }
                                                                        if (this.ke != 1) break block93;
                                                                        var4_11 = "yesterday";
                                                                        if (var10_3 == 0) break block92;
                                                                    }
                                                                    var4_11 = String.valueOf(this.ke) + " days ago";
                                                                }
                                                                var2_2.P = "You last logged in " + var4_11 + " from: " + signlink.dns;
                                                                if (var10_3 == 0) break block89;
                                                            }
                                                            var2_2.P = "";
                                                        }
                                                        if (var3_4 == 651) {
                                                            if (this.dh == 0) {
                                                                var2_2.P = "0 unread messages";
                                                                var2_2.z = 0xFFFF00;
                                                            }
                                                            if (this.dh == 1) {
                                                                var2_2.P = "1 unread message";
                                                                var2_2.z = 65280;
                                                            }
                                                            if (this.dh > 1) {
                                                                var2_2.P = String.valueOf(this.dh) + " unread messages";
                                                                var2_2.z = 65280;
                                                            }
                                                        }
                                                        if (var3_4 != 652) break block94;
                                                        if (this.qh != 201) break block95;
                                                        if (this.vg != 1) break block96;
                                                        var2_2.P = "@yel@This is a non-members world: @whi@Since you are a member we";
                                                        if (var10_3 == 0) break block94;
                                                    }
                                                    var2_2.P = "";
                                                    if (var10_3 == 0) break block94;
                                                }
                                                if (this.qh != 200) break block97;
                                                var2_2.P = "You have not yet set any password recovery questions.";
                                                if (var10_3 == 0) break block94;
                                            }
                                            if (this.qh != 0) break block98;
                                            var4_11 = "Earlier today";
                                            if (var10_3 == 0) break block99;
                                        }
                                        if (this.qh != 1) break block100;
                                        var4_11 = "Yesterday";
                                        if (var10_3 == 0) break block99;
                                    }
                                    var4_11 = String.valueOf(this.qh) + " days ago";
                                }
                                var2_2.P = String.valueOf(var4_11) + " you changed your recovery questions";
                            }
                            if (var3_4 != 653) break block101;
                            if (this.qh != 201) break block102;
                            if (this.vg != 1) break block103;
                            var2_2.P = "@whi@recommend you use a members world instead. You may use";
                            if (var10_3 == 0) break block101;
                        }
                        var2_2.P = "";
                        if (var10_3 == 0) break block101;
                    }
                    if (this.qh != 200) break block104;
                    var2_2.P = "We strongly recommend you do so now to secure your account.";
                    if (var10_3 == 0) break block101;
                }
                var2_2.P = "If you do not remember making this change then cancel it immediately";
            }
            if (var3_4 == 654) {
                if (this.qh == 201) {
                    if (this.vg == 1) {
                        var2_2.P = "@whi@this world but member benefits are unavailable whilst here.";
                        return;
                    }
                    var2_2.P = "";
                    return;
                }
                if (this.qh == 200) {
                    var2_2.P = "Do this from the 'account management' area on our front webpage";
                    return;
                }
                var2_2.P = "Do this from the 'account management' area on our front webpage";
                return;
            }
        }
        catch (RuntimeException var3_5) {
            signlink.reporterror("36496, " + var1_1 + ", " + var2_2 + ", " + var3_5.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void h(byte by) {
        try {
            if (this.Sh == 0) {
                return;
            }
            YXVQXWYR yXVQXWYR = this.qj;
            if (by != this.tj) {
                Ci = !Ci;
            }
            int n = 0;
            if (this.fg != 0) {
                n = 1;
            }
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && Jj == 0) continue;
                if (this.ad[n2] != null) {
                    int n3;
                    int n4 = this.Yc[n2];
                    String string = this.Zc[n2];
                    int n5 = 0;
                    if (string != null && string.startsWith("@cr1@")) {
                        string = string.substring(5);
                        n5 = 1;
                    }
                    if (string != null && string.startsWith("@cr2@")) {
                        string = string.substring(5);
                        n5 = 2;
                    }
                    if ((n4 == 3 || n4 == 7) && (n4 == 7 || this.fb == 0 || this.fb == 1 && this.a(false, string))) {
                        n3 = 329 - n * 13;
                        int n6 = 4;
                        yXVQXWYR.b(0, "From", n3, 822, n6);
                        yXVQXWYR.b(65535, "From", n3 - 1, 822, n6);
                        n6 += yXVQXWYR.a(this.rg, "From ");
                        if (n5 == 1) {
                            this.qi[0].a(n6, 16083, n3 - 12);
                            n6 += 14;
                        }
                        if (n5 == 2) {
                            this.qi[1].a(n6, 16083, n3 - 12);
                            n6 += 14;
                        }
                        yXVQXWYR.b(0, String.valueOf(string) + ": " + this.ad[n2], n3, 822, n6);
                        yXVQXWYR.b(65535, String.valueOf(string) + ": " + this.ad[n2], n3 - 1, 822, n6);
                        if (++n >= 5) {
                            return;
                        }
                    }
                    if (n4 == 5 && this.fb < 2) {
                        n3 = 329 - n * 13;
                        yXVQXWYR.b(0, this.ad[n2], n3, 822, 4);
                        yXVQXWYR.b(65535, this.ad[n2], n3 - 1, 822, 4);
                        if (++n >= 5) {
                            return;
                        }
                    }
                    if (n4 == 6 && this.fb < 2) {
                        n3 = 329 - n * 13;
                        yXVQXWYR.b(0, "To " + string + ": " + this.ad[n2], n3, 822, 4);
                        yXVQXWYR.b(65535, "To " + string + ": " + this.ad[n2], n3 - 1, 822, 4);
                        if (++n >= 5) {
                            return;
                        }
                    }
                }
                ++n2;
            } while (n2 < 100);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("85217, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(String string, int n, String string2, boolean bl) {
        try {
            if (bl) {
                return;
            }
            if (n == 0 && this.Ue != -1) {
                this.eb = string;
                this.z = 0;
            }
            if (this.vj == -1) {
                this.ui = true;
            }
            int n2 = 99;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && Jj == 0) continue;
                this.Yc[n2] = this.Yc[n2 - 1];
                this.Zc[n2] = this.Zc[n2 - 1];
                this.ad[n2] = this.ad[n2 - 1];
                --n2;
            } while (n2 > 0);
            this.Yc[0] = n;
            this.Zc[0] = string2;
            this.ad[0] = string;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("56346, " + string + ", " + n + ", " + string2 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void B(int n) {
        try {
            n = 72 / n;
            if (this.z == 1) {
                if (this.A >= 539 && this.A <= 573 && this.B >= 169 && this.B < 205 && this.Fg[0] != -1) {
                    this.ch = true;
                    this.si = 0;
                    this.eg = true;
                }
                if (this.A >= 569 && this.A <= 599 && this.B >= 168 && this.B < 205 && this.Fg[1] != -1) {
                    this.ch = true;
                    this.si = 1;
                    this.eg = true;
                }
                if (this.A >= 597 && this.A <= 627 && this.B >= 168 && this.B < 205 && this.Fg[2] != -1) {
                    this.ch = true;
                    this.si = 2;
                    this.eg = true;
                }
                if (this.A >= 625 && this.A <= 669 && this.B >= 168 && this.B < 203 && this.Fg[3] != -1) {
                    this.ch = true;
                    this.si = 3;
                    this.eg = true;
                }
                if (this.A >= 666 && this.A <= 696 && this.B >= 168 && this.B < 205 && this.Fg[4] != -1) {
                    this.ch = true;
                    this.si = 4;
                    this.eg = true;
                }
                if (this.A >= 694 && this.A <= 724 && this.B >= 168 && this.B < 205 && this.Fg[5] != -1) {
                    this.ch = true;
                    this.si = 5;
                    this.eg = true;
                }
                if (this.A >= 722 && this.A <= 756 && this.B >= 169 && this.B < 205 && this.Fg[6] != -1) {
                    this.ch = true;
                    this.si = 6;
                    this.eg = true;
                }
                if (this.A >= 540 && this.A <= 574 && this.B >= 466 && this.B < 502 && this.Fg[7] != -1) {
                    this.ch = true;
                    this.si = 7;
                    this.eg = true;
                }
                if (this.A >= 572 && this.A <= 602 && this.B >= 466 && this.B < 503 && this.Fg[8] != -1) {
                    this.ch = true;
                    this.si = 8;
                    this.eg = true;
                }
                if (this.A >= 599 && this.A <= 629 && this.B >= 466 && this.B < 503 && this.Fg[9] != -1) {
                    this.ch = true;
                    this.si = 9;
                    this.eg = true;
                }
                if (this.A >= 627 && this.A <= 671 && this.B >= 467 && this.B < 502 && this.Fg[10] != -1) {
                    this.ch = true;
                    this.si = 10;
                    this.eg = true;
                }
                if (this.A >= 669 && this.A <= 699 && this.B >= 466 && this.B < 503 && this.Fg[11] != -1) {
                    this.ch = true;
                    this.si = 11;
                    this.eg = true;
                }
                if (this.A >= 696 && this.A <= 726 && this.B >= 466 && this.B < 503 && this.Fg[12] != -1) {
                    this.ch = true;
                    this.si = 12;
                    this.eg = true;
                }
                if (this.A >= 724 && this.A <= 758 && this.B >= 466 && this.B < 502 && this.Fg[13] != -1) {
                    this.ch = true;
                    this.si = 13;
                    this.eg = true;
                    return;
                }
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("30484, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void C(int n) {
        try {
            if (this.ph != null) {
                return;
            }
            this.I(3);
            this.m = null;
            this.ig = null;
            this.jg = null;
            this.kg = null;
            this.lg = null;
            this.mg = null;
            this.ng = null;
            this.og = null;
            this.pg = null;
            this.qg = null;
            this.ph = new IVIFZQBK(479, 96, this.f(0), 0);
            this.nh = new IVIFZQBK(172, 156, this.f(0), 0);
            AFCKELYG.a(this.di);
            this.Uh.a(0, 16083, 0);
            this.mh = new IVIFZQBK(190, 261, this.f(0), 0);
            this.oh = new IVIFZQBK(512, 334, this.f(0), 0);
            AFCKELYG.a(this.di);
            this.yg = new IVIFZQBK(496, 50, this.f(0), 0);
            if (n != 1) {
                this.a();
            }
            this.zg = new IVIFZQBK(269, 37, this.f(0), 0);
            this.Ag = new IVIFZQBK(249, 45, this.f(0), 0);
            this.aj = true;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("35544, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final String g(boolean bl) {
        try {
            this.gh &= bl;
            if (signlink.mainapp != null) {
                return signlink.mainapp.getDocumentBase().getHost().toLowerCase();
            }
            if (this.o != null) {
                return "runescape.com";
            }
            return super.getDocumentBase().getHost().toLowerCase();
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("82775, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(CXGZMTJK cXGZMTJK, int n, int n2, int n3) {
        try {
            int n4 = n3 * n3 + n2 * n2;
            if (n >= 0) {
                this.a();
            }
            if (n4 > 4225 && n4 < 90000) {
                int n5 = this.Ih + this.gi & 0x7FF;
                int n6 = ZKARKDQW.Jb[n5];
                int n7 = ZKARKDQW.Kb[n5];
                n6 = n6 * 256 / (this.th + 256);
                n7 = n7 * 256 / (this.th + 256);
                int n8 = n2 * n6 + n3 * n7 >> 16;
                int n9 = n2 * n7 - n3 * n6 >> 16;
                double d = Math.atan2(n8, n9);
                int n10 = (int)(Math.sin(d) * 63.0);
                int n11 = (int)(Math.cos(d) * 57.0);
                this.fe.a(83 - n11 - 20, 15, 20, 15, 41960, 256, 20, d, 94 + n10 + 4 - 10);
                return;
            }
            this.a(cXGZMTJK, n3, n2, false);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("37468, " + cXGZMTJK + ", " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void D(int n) {
        int n2 = Jj;
        try {
            block24: {
                block25: {
                    block22: {
                        block23: {
                            block20: {
                                block21: {
                                    if (this.Nf != 0) {
                                        return;
                                    }
                                    this.Wh[0] = "Cancel";
                                    this.Uf[0] = 1107;
                                    this.Ig = 1;
                                    this.n(false);
                                    this.Ub = 0;
                                    if (this.t <= 4 || this.u <= 4 || this.t >= 516 || this.u >= 338) break block20;
                                    if (this.rb == -1) break block21;
                                    this.a(4, 13037, DUCMKFAY.d[this.rb], this.t, 4, this.u, 0);
                                    if (n2 == 0) break block20;
                                }
                                this.z(33660);
                            }
                            if (this.Ub != this.Ee) {
                                this.Ee = this.Ub;
                            }
                            this.Ub = 0;
                            if (this.t <= 553 || this.u <= 205 || this.t >= 743 || this.u >= 466) break block22;
                            if (this.Mh == -1) break block23;
                            this.a(553, 13037, DUCMKFAY.d[this.Mh], this.t, 205, this.u, 0);
                            if (n2 == 0) break block22;
                        }
                        if (this.Fg[this.si] != -1) {
                            this.a(553, 13037, DUCMKFAY.d[this.Fg[this.si]], this.t, 205, this.u, 0);
                        }
                    }
                    if (this.Ub != this.af) {
                        this.ch = true;
                        this.af = this.Ub;
                    }
                    this.Ub = 0;
                    if (this.t <= 17 || this.u <= 357 || this.t >= 496 || this.u >= 453) break block24;
                    if (this.vj == -1) break block25;
                    this.a(17, 13037, DUCMKFAY.d[this.vj], this.t, 357, this.u, 0);
                    if (n2 == 0) break block24;
                }
                if (this.u < 434 && this.t < 426) {
                    this.a(this.t - 17, this.u - 357, 9);
                }
            }
            if (this.vj != -1 && this.Ub != this.Re) {
                this.ui = true;
                this.Re = this.Ub;
            }
            boolean bl = false;
            this.le += n;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                bl = true;
                int n3 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && n2 == 0) continue;
                    if (this.Uf[n3] < 1000 && this.Uf[n3 + 1] > 1000) {
                        String string = this.Wh[n3];
                        this.Wh[n3] = this.Wh[n3 + 1];
                        this.Wh[n3 + 1] = string;
                        int n4 = this.Uf[n3];
                        this.Uf[n3] = this.Uf[n3 + 1];
                        this.Uf[n3 + 1] = n4;
                        n4 = this.Sf[n3];
                        this.Sf[n3] = this.Sf[n3 + 1];
                        this.Sf[n3 + 1] = n4;
                        n4 = this.Tf[n3];
                        this.Tf[n3] = this.Tf[n3 + 1];
                        this.Tf[n3 + 1] = n4;
                        n4 = this.Vf[n3];
                        this.Vf[n3] = this.Vf[n3 + 1];
                        this.Vf[n3 + 1] = n4;
                        bl = false;
                    }
                    ++n3;
                } while (n3 < this.Ig - 1);
            } while (!bl);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("40707, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final int a(boolean bl, int n, int n2, int n3) {
        try {
            if (!bl) {
                this.N = null;
            }
            int n4 = 256 - n3;
            return ((n & 0xFF00FF) * n4 + (n2 & 0xFF00FF) * n3 & 0xFF00FF00) + ((n & 0xFF00) * n4 + (n2 & 0xFF00) * n3 & 0xFF0000) >> 8;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("76365, " + bl + ", " + n + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void a(String var1_1, String var2_2, boolean var3_3) {
        var16_4 = client.Jj;
        signlink.errorname = var1_1;
        try {
            block49: {
                block48: {
                    block45: {
                        block47: {
                            block46: {
                                if (!var3_3) {
                                    this.lj = "";
                                    this.mj = "Connecting to server...";
                                    this.a(true, false);
                                }
                                this.rh = new NQABEVLK(this, -978, this.j(43594 + client.od));
                                var4_5 = ZTQFNQRH.a(var1_1);
                                var6_6 = (int)(var4_5 >> 16 & 31L);
                                this.Ph.z = 0;
                                this.Ph.a(14);
                                this.Ph.a(var6_6);
                                this.rh.a(2, 0, this.Ph.y, 0);
                                var7_7 = 0;
                                if (var16_4 == 0) ** GOTO lbl21
                                do {
                                    this.rh.b();
                                    ++var7_7;
lbl21:
                                    // 2 sources

                                } while (var7_7 < 8);
                                var9_9 = var8_8 = this.rh.b();
                                if (var8_8 != 0) break block45;
                                this.rh.a(this.Kf.y, 0, 8);
                                this.Kf.z = 0;
                                this.mi = this.Kf.e(-35089);
                                var10_10 = new int[]{(int)(Math.random() * 9.9999999E7), (int)(Math.random() * 9.9999999E7), (int)(this.mi >> 32), (int)this.mi};
                                this.Ph.z = 0;
                                this.Ph.a(10);
                                this.Ph.d(var10_10[0]);
                                this.Ph.d(var10_10[1]);
                                this.Ph.d(var10_10[2]);
                                this.Ph.d(var10_10[3]);
                                this.Ph.d(signlink.uid);
                                this.Ph.a(var1_1);
                                this.Ph.a(var2_2);
                                this.Ph.a(client.Ke, client.qb, (byte)0);
                                this.hb.z = 0;
                                if (!var3_3) break block46;
                                this.hb.a(18);
                                if (var16_4 == 0) break block47;
                            }
                            this.hb.a(16);
                        }
                        this.hb.a(this.Ph.z + 36 + 1 + 1 + 2);
                        this.hb.a(255);
                        this.hb.b(317);
                        this.hb.a(client.qd != false ? 1 : 0);
                        var11_13 = 0;
                        if (var16_4 == 0) ** GOTO lbl54
                        do {
                            this.hb.d(this.Rf[var11_13]);
                            ++var11_13;
lbl54:
                            // 2 sources

                        } while (var11_13 < 9);
                        this.hb.a(this.Ph.y, this.Ph.z, true, 0);
                        this.Ph.D = new JOCFVBOI(-436, var10_10);
                        var12_14 = 0;
                        if (var16_4 == 0) ** GOTO lbl62
                        do {
                            v0 = var12_14++;
                            var10_10[v0] = var10_10[v0] + 50;
lbl62:
                            // 2 sources

                        } while (var12_14 < 4);
                        this.ee = new JOCFVBOI(-436, var10_10);
                        this.rh.a(this.hb.z, 0, this.hb.y, 0);
                        var8_8 = this.rh.b();
                    }
                    if (var8_8 == 1) {
                        try {
                            Thread.sleep(2000L);
                        }
                        catch (Exception v1) {}
                        this.a(var1_1, var2_2, var3_3);
                        return;
                    }
                    if (var8_8 != 2) break block48;
                    this.xb = this.rh.b();
                    client.ci = this.rh.b() == 1;
                    this.ri = 0L;
                    this.Ae = 0;
                    this.Nb.f = 0;
                    this.q = true;
                    this.kd = true;
                    this.gh = true;
                    this.Ph.z = 0;
                    this.Kf.z = 0;
                    this.me = -1;
                    this.bb = -1;
                    this.cb = -1;
                    this.db = -1;
                    this.le = 0;
                    this.ne = 0;
                    this.fg = 0;
                    this.pe = 0;
                    this.pb = 0;
                    this.Ig = 0;
                    this.Tb = false;
                    this.r = 0;
                    var10_11 = 0;
                    if (var16_4 == 0) ** GOTO lbl102
                    do {
                        this.ad[var10_11] = null;
                        ++var10_11;
lbl102:
                        // 2 sources

                    } while (var10_11 < 100);
                    this.Bj = 0;
                    this.Lg = 0;
                    this.Be = 0;
                    this.pf = 0;
                    this.xj = (int)(Math.random() * 100.0) - 50;
                    this.Gg = (int)(Math.random() * 110.0) - 55;
                    this.ec = (int)(Math.random() * 80.0) - 40;
                    this.gi = (int)(Math.random() * 120.0) - 60;
                    this.th = (int)(Math.random() * 30.0) - 20;
                    this.Ih = (int)(Math.random() * 20.0) - 10 & 2047;
                    this.ze = 0;
                    this.Pd = -1;
                    this.gj = 0;
                    this.hj = 0;
                    this.Zb = 0;
                    this.W = 0;
                    var11_13 = 0;
                    if (var16_4 == 0) ** GOTO lbl125
                    do {
                        this.Yb[var11_13] = null;
                        this.dc[var11_13] = null;
                        ++var11_13;
lbl125:
                        // 2 sources

                    } while (var11_13 < this.Wb);
                    var12_14 = 0;
                    if (var16_4 == 0) ** GOTO lbl131
                    do {
                        this.V[var12_14] = null;
                        ++var12_14;
lbl131:
                        // 2 sources

                    } while (var12_14 < 16384);
                    client.Bg = this.Yb[this.Xb] = new DLZHLHNK();
                    this.re.c();
                    this.jf.c();
                    var13_15 = 0;
                    if (var16_4 == 0) ** GOTO lbl150
                    do {
                        var14_16 = 0;
                        if (var16_4 == 0) ** GOTO lbl148
                        do {
                            var15_17 = 0;
                            if (var16_4 == 0) ** GOTO lbl146
                            do {
                                this.N[var13_15][var14_16][var15_17] = null;
                                ++var15_17;
lbl146:
                                // 2 sources

                            } while (var15_17 < 104);
                            ++var14_16;
lbl148:
                            // 2 sources

                        } while (var14_16 < 104);
                        ++var13_15;
lbl150:
                        // 2 sources

                    } while (var13_15 < 4);
                    this.Ch = new LHGXPZPG(169);
                    this.ic = 0;
                    this.hc = 0;
                    this.Ue = -1;
                    this.vj = -1;
                    this.rb = -1;
                    this.Mh = -1;
                    this.we = -1;
                    this.Yg = false;
                    this.si = 3;
                    this.wi = 0;
                    this.Tb = false;
                    this.bj = false;
                    this.eb = null;
                    this.hf = 0;
                    this.gf = -1;
                    this.Ze = true;
                    this.n(0);
                    var14_16 = 0;
                    if (var16_4 == 0) ** GOTO lbl174
                    do {
                        this.Ud[var14_16] = 0;
                        ++var14_16;
lbl174:
                        // 2 sources

                    } while (var14_16 < 5);
                    var15_17 = 0;
                    if (var16_4 == 0) ** GOTO lbl181
                    do {
                        this.Cg[var15_17] = null;
                        this.Dg[var15_17] = false;
                        ++var15_17;
lbl181:
                        // 2 sources

                    } while (var15_17 < 5);
                    client.yh = 0;
                    client.Jg = 0;
                    client.Qd = 0;
                    client.Hj = 0;
                    client.Gc = 0;
                    client.Lh = 0;
                    client.eh = 0;
                    client.xi = 0;
                    client.Xc = 0;
                    client.fj = 0;
                    this.C(1);
                    return;
                }
                if (var8_8 == 3) {
                    this.lj = "";
                    this.mj = "Invalid username or password.";
                    return;
                }
                if (var8_8 == 4) {
                    this.lj = "Your account has been disabled.";
                    this.mj = "Please check your message-centre for details.";
                    return;
                }
                if (var8_8 == 5) {
                    this.lj = "Your account is already logged in.";
                    this.mj = "Try again in 60 secs...";
                    return;
                }
                if (var8_8 == 6) {
                    this.lj = "RuneScape has been updated!";
                    this.mj = "Please reload this page.";
                    return;
                }
                if (var8_8 == 7) {
                    this.lj = "This world is full.";
                    this.mj = "Please use a different world.";
                    return;
                }
                if (var8_8 == 8) {
                    this.lj = "Unable to connect.";
                    this.mj = "Login server offline.";
                    return;
                }
                if (var8_8 == 9) {
                    this.lj = "Login limit exceeded.";
                    this.mj = "Too many connections from your address.";
                    return;
                }
                if (var8_8 == 10) {
                    this.lj = "Unable to connect.";
                    this.mj = "Bad session id.";
                    return;
                }
                if (var8_8 == 11) {
                    this.mj = "Login server rejected session.";
                    this.mj = "Please try again.";
                    return;
                }
                if (var8_8 == 12) {
                    this.lj = "You need a members account to login to this world.";
                    this.mj = "Please subscribe, or use a different world.";
                    return;
                }
                if (var8_8 == 13) {
                    this.lj = "Could not complete login.";
                    this.mj = "Please try using a different world.";
                    return;
                }
                if (var8_8 == 14) {
                    this.lj = "The server is being updated.";
                    this.mj = "Please wait 1 minute and try again.";
                    return;
                }
                if (var8_8 == 15) {
                    this.gh = true;
                    this.Ph.z = 0;
                    this.Kf.z = 0;
                    this.me = -1;
                    this.bb = -1;
                    this.cb = -1;
                    this.db = -1;
                    this.le = 0;
                    this.ne = 0;
                    this.fg = 0;
                    this.Ig = 0;
                    this.Tb = false;
                    this.K = System.currentTimeMillis();
                    return;
                }
                if (var8_8 == 16) {
                    this.lj = "Login attempts exceeded.";
                    this.mj = "Please wait 1 minute and try again.";
                    return;
                }
                if (var8_8 == 17) {
                    this.lj = "You are standing in a members-only area.";
                    this.mj = "To play on this world move to a free area first";
                    return;
                }
                if (var8_8 == 20) {
                    this.lj = "Invalid loginserver requested";
                    this.mj = "Please try using a different world.";
                    return;
                }
                if (var8_8 != 21) break block49;
                var10_12 = this.rh.b();
                if (var16_4 == 0) ** GOTO lbl282
                do {
                    this.lj = "You have only just left another world";
                    this.mj = "Your profile will be transferred in: " + var10_12 + " seconds";
                    this.a(true, false);
                    try {
                        Thread.sleep(1000L);
                    }
                    catch (Exception v2) {}
                    --var10_12;
lbl282:
                    // 2 sources

                } while (var10_12 >= 0);
                this.a(var1_1, var2_2, var3_3);
                return;
            }
            if (var8_8 == -1) {
                if (var9_9 == 0) {
                    if (this.Qe < 2) {
                        try {
                            Thread.sleep(2000L);
                        }
                        catch (Exception v3) {}
                        ++this.Qe;
                        this.a(var1_1, var2_2, var3_3);
                        return;
                    }
                    this.lj = "No response from loginserver";
                    this.mj = "Please wait 1 minute and try again.";
                    return;
                }
                this.lj = "No response from server";
                this.mj = "Please try using a different world.";
                return;
            }
            System.out.println("response:" + var8_8);
            this.lj = "Unexpected server response";
            this.mj = "Please try using a different world.";
            return;
        }
        catch (IOException v4) {
            this.lj = "";
            this.mj = "Error connecting to server.";
            return;
        }
    }

    /*
     * Unable to fully structure code
     */
    public final boolean a(int var1_1, int var2_2, int var3_3, int var4_4, int var5_5, int var6_6, int var7_7, int var8_8, int var9_9, int var10_10, boolean var11_11, int var12_12) {
        var28_13 = client.Jj;
        try {
            block46: {
                block41: {
                    block39: {
                        block40: {
                            var13_14 = 104;
                            var14_16 = 104;
                            var15_17 = 0;
                            if (var28_13 == 0) ** GOTO lbl16
                            do {
                                var16_18 = 0;
                                if (var28_13 == 0) ** GOTO lbl14
                                do {
                                    this.jc[var15_17][var16_18] = 0;
                                    this.L[var15_17][var16_18] = 99999999;
                                    ++var16_18;
lbl14:
                                    // 2 sources

                                } while (var16_18 < var14_16);
                                ++var15_17;
lbl16:
                                // 2 sources

                            } while (var15_17 < var13_14);
                            var16_18 = var10_10;
                            var17_19 = var6_6;
                            this.jc[var10_10][var6_6] = 99;
                            this.L[var10_10][var6_6] = 0;
                            var18_20 = 0;
                            var19_21 = 0;
                            this.zj[var18_20] = var10_10;
                            this.Aj[var18_20++] = var6_6;
                            var20_22 = false;
                            var21_23 = this.zj.length;
                            var22_24 = this.Bi[this.Ac].m;
                            if (var28_13 == 0) ** GOTO lbl95
                            do {
                                var16_18 = this.zj[var19_21];
                                var17_19 = this.Aj[var19_21];
                                var19_21 = (var19_21 + 1) % var21_23;
                                if (var16_18 == var12_12 && var17_19 == var9_9) {
                                    var20_22 = true;
                                    if (var28_13 == 0) break;
                                }
                                if (var5_5 != 0) {
                                    if ((var5_5 < 5 || var5_5 == 10) && this.Bi[this.Ac].a(var12_12, var16_18, var17_19, 0, var2_2, var5_5 - 1, var9_9)) {
                                        var20_22 = true;
                                        if (var28_13 == 0) break;
                                    }
                                    if (var5_5 < 10 && this.Bi[this.Ac].b(var12_12, var9_9, var17_19, var5_5 - 1, var2_2, var16_18, 0)) {
                                        var20_22 = true;
                                        if (var28_13 == 0) break;
                                    }
                                }
                                if (var7_7 != 0 && var3_3 != 0 && this.Bi[this.Ac].a((byte)1, var9_9, var12_12, var16_18, var3_3, var8_8, var7_7, var17_19)) {
                                    var20_22 = true;
                                    if (var28_13 == 0) break;
                                }
                                var23_25 = this.L[var16_18][var17_19] + 1;
                                if (var16_18 > 0 && this.jc[var16_18 - 1][var17_19] == 0 && (var22_24[var16_18 - 1][var17_19] & 19398920) == 0) {
                                    this.zj[var18_20] = var16_18 - 1;
                                    this.Aj[var18_20] = var17_19;
                                    var18_20 = (var18_20 + 1) % var21_23;
                                    this.jc[var16_18 - 1][var17_19] = 2;
                                    this.L[var16_18 - 1][var17_19] = var23_25;
                                }
                                if (var16_18 < var13_14 - 1 && this.jc[var16_18 + 1][var17_19] == 0 && (var22_24[var16_18 + 1][var17_19] & 19399040) == 0) {
                                    this.zj[var18_20] = var16_18 + 1;
                                    this.Aj[var18_20] = var17_19;
                                    var18_20 = (var18_20 + 1) % var21_23;
                                    this.jc[var16_18 + 1][var17_19] = 8;
                                    this.L[var16_18 + 1][var17_19] = var23_25;
                                }
                                if (var17_19 > 0 && this.jc[var16_18][var17_19 - 1] == 0 && (var22_24[var16_18][var17_19 - 1] & 19398914) == 0) {
                                    this.zj[var18_20] = var16_18;
                                    this.Aj[var18_20] = var17_19 - 1;
                                    var18_20 = (var18_20 + 1) % var21_23;
                                    this.jc[var16_18][var17_19 - 1] = 1;
                                    this.L[var16_18][var17_19 - 1] = var23_25;
                                }
                                if (var17_19 < var14_16 - 1 && this.jc[var16_18][var17_19 + 1] == 0 && (var22_24[var16_18][var17_19 + 1] & 19398944) == 0) {
                                    this.zj[var18_20] = var16_18;
                                    this.Aj[var18_20] = var17_19 + 1;
                                    var18_20 = (var18_20 + 1) % var21_23;
                                    this.jc[var16_18][var17_19 + 1] = 4;
                                    this.L[var16_18][var17_19 + 1] = var23_25;
                                }
                                if (var16_18 > 0 && var17_19 > 0 && this.jc[var16_18 - 1][var17_19 - 1] == 0 && (var22_24[var16_18 - 1][var17_19 - 1] & 19398926) == 0 && (var22_24[var16_18 - 1][var17_19] & 19398920) == 0 && (var22_24[var16_18][var17_19 - 1] & 19398914) == 0) {
                                    this.zj[var18_20] = var16_18 - 1;
                                    this.Aj[var18_20] = var17_19 - 1;
                                    var18_20 = (var18_20 + 1) % var21_23;
                                    this.jc[var16_18 - 1][var17_19 - 1] = 3;
                                    this.L[var16_18 - 1][var17_19 - 1] = var23_25;
                                }
                                if (var16_18 < var13_14 - 1 && var17_19 > 0 && this.jc[var16_18 + 1][var17_19 - 1] == 0 && (var22_24[var16_18 + 1][var17_19 - 1] & 19399043) == 0 && (var22_24[var16_18 + 1][var17_19] & 19399040) == 0 && (var22_24[var16_18][var17_19 - 1] & 19398914) == 0) {
                                    this.zj[var18_20] = var16_18 + 1;
                                    this.Aj[var18_20] = var17_19 - 1;
                                    var18_20 = (var18_20 + 1) % var21_23;
                                    this.jc[var16_18 + 1][var17_19 - 1] = 9;
                                    this.L[var16_18 + 1][var17_19 - 1] = var23_25;
                                }
                                if (var16_18 > 0 && var17_19 < var14_16 - 1 && this.jc[var16_18 - 1][var17_19 + 1] == 0 && (var22_24[var16_18 - 1][var17_19 + 1] & 19398968) == 0 && (var22_24[var16_18 - 1][var17_19] & 19398920) == 0 && (var22_24[var16_18][var17_19 + 1] & 19398944) == 0) {
                                    this.zj[var18_20] = var16_18 - 1;
                                    this.Aj[var18_20] = var17_19 + 1;
                                    var18_20 = (var18_20 + 1) % var21_23;
                                    this.jc[var16_18 - 1][var17_19 + 1] = 6;
                                    this.L[var16_18 - 1][var17_19 + 1] = var23_25;
                                }
                                if (var16_18 >= var13_14 - 1 || var17_19 >= var14_16 - 1 || this.jc[var16_18 + 1][var17_19 + 1] != 0 || (var22_24[var16_18 + 1][var17_19 + 1] & 19399136) != 0 || (var22_24[var16_18 + 1][var17_19] & 19399040) != 0 || (var22_24[var16_18][var17_19 + 1] & 19398944) != 0) continue;
                                this.zj[var18_20] = var16_18 + 1;
                                this.Aj[var18_20] = var17_19 + 1;
                                var18_20 = (var18_20 + 1) % var21_23;
                                this.jc[var16_18 + 1][var17_19 + 1] = 12;
                                this.L[var16_18 + 1][var17_19 + 1] = var23_25;
lbl95:
                                // 3 sources

                            } while (var19_21 != var18_20);
                            this.jj = 0;
                            if (var20_22) break block39;
                            if (!var11_11) break block40;
                            var23_25 = 100;
                            var24_26 = 1;
                            if (var28_13 == 0) ** GOTO lbl121
                            do {
                                var25_27 = var12_12 - var24_26;
                                if (var28_13 == 0) ** GOTO lbl118
                                do {
                                    var26_28 = var9_9 - var24_26;
                                    if (var28_13 == 0) ** GOTO lbl116
                                    do {
                                        if (var25_27 >= 0 && var26_28 >= 0 && var25_27 < 104 && var26_28 < 104 && this.L[var25_27][var26_28] < var23_25) {
                                            var23_25 = this.L[var25_27][var26_28];
                                            var16_18 = var25_27;
                                            var17_19 = var26_28;
                                            this.jj = 1;
                                            var20_22 = true;
                                        }
                                        ++var26_28;
lbl116:
                                        // 2 sources

                                    } while (var26_28 <= var9_9 + var24_26);
                                    ++var25_27;
lbl118:
                                    // 2 sources

                                } while (var25_27 <= var12_12 + var24_26);
                                if (var20_22) break;
                                ++var24_26;
lbl121:
                                // 2 sources

                            } while (var24_26 < 2);
                        }
                        if (!var20_22) {
                            return false;
                        }
                    }
                    var19_21 = 0;
                    this.zj[var19_21] = var16_18;
                    this.Aj[var19_21++] = var17_19;
                    if (var4_4 == -11308) break block41;
                    var25_27 = 1;
                    if (var28_13 == 0) ** GOTO lbl134
                    do {
                        ++var25_27;
lbl134:
                        // 2 sources

                    } while (var25_27 > 0);
                }
                var23_25 = var24_26 = this.jc[var16_18][var17_19];
                if (var28_13 == 0) ** GOTO lbl158
                do {
                    block45: {
                        block44: {
                            block43: {
                                block42: {
                                    if (var23_25 != var24_26) {
                                        var24_26 = var23_25;
                                        this.zj[var19_21] = var16_18;
                                        this.Aj[var19_21++] = var17_19;
                                    }
                                    if ((var23_25 & 2) == 0) break block42;
                                    ++var16_18;
                                    if (var28_13 == 0) break block43;
                                }
                                if ((var23_25 & 8) != 0) {
                                    --var16_18;
                                }
                            }
                            if ((var23_25 & 1) == 0) break block44;
                            ++var17_19;
                            if (var28_13 == 0) break block45;
                        }
                        if ((var23_25 & 4) != 0) {
                            --var17_19;
                        }
                    }
                    var23_25 = this.jc[var16_18][var17_19];
lbl158:
                    // 2 sources

                } while (var16_18 != var10_10 || var17_19 != var6_6);
                if (var19_21 <= 0) break block46;
                var21_23 = var19_21;
                if (var21_23 > 25) {
                    var21_23 = 25;
                }
                var25_27 = this.zj[--var19_21];
                var26_28 = this.Aj[var19_21];
                if ((client.Hj += var21_23) >= 92) {
                    this.Ph.a((byte)6, 36);
                    this.Ph.d(0);
                    client.Hj = 0;
                }
                if (var1_1 == 0) {
                    this.Ph.a((byte)6, 164);
                    this.Ph.a(var21_23 + var21_23 + 3);
                }
                if (var1_1 == 1) {
                    this.Ph.a((byte)6, 248);
                    this.Ph.a(var21_23 + var21_23 + 3 + 14);
                }
                if (var1_1 == 2) {
                    this.Ph.a((byte)6, 98);
                    this.Ph.a(var21_23 + var21_23 + 3);
                }
                this.Ph.g(0, var25_27 + this.Me);
                this.gj = this.zj[0];
                this.hj = this.Aj[0];
                var27_29 = 1;
                if (var28_13 == 0) ** GOTO lbl187
                do {
                    this.Ph.a(this.zj[--var19_21] - var25_27);
                    this.Ph.a(this.Aj[var19_21] - var26_28);
                    ++var27_29;
lbl187:
                    // 2 sources

                } while (var27_29 < var21_23);
                this.Ph.b(true, var26_28 + this.Ne);
                this.Ph.d(this.D[5] == 1 ? 1 : 0, 0);
                return true;
            }
            return var1_1 != 1;
        }
        catch (RuntimeException var13_15) {
            signlink.reporterror("85388, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_4 + ", " + var5_5 + ", " + var6_6 + ", " + var7_7 + ", " + var8_8 + ", " + var9_9 + ", " + var10_10 + ", " + var11_11 + ", " + var12_12 + ", " + var13_15.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void a(int n, MBMGIXGO mBMGIXGO, boolean bl) {
        int n2 = Jj;
        try {
            int n3 = 0;
            boolean bl2 = true;
            do {
                int n4;
                int n5;
                int n6;
                CWNCPMLX cWNCPMLX;
                block19: {
                    block20: {
                        if (bl2 && !(bl2 = false) && n2 == 0) continue;
                        int n7 = this.cc[n3];
                        cWNCPMLX = this.V[n7];
                        n6 = mBMGIXGO.c();
                        if ((n6 & 0x10) == 0) break block19;
                        n5 = mBMGIXGO.c((byte)108);
                        if (n5 == 65535) {
                            n5 = -1;
                        }
                        n4 = mBMGIXGO.c();
                        if (n5 != cWNCPMLX.M || n5 == -1) break block20;
                        int n8 = LKGEGIEW.d[n5].r;
                        if (n8 == 1) {
                            cWNCPMLX.N = 0;
                            cWNCPMLX.O = 0;
                            cWNCPMLX.P = n4;
                            cWNCPMLX.Q = 0;
                        }
                        if (n8 != 2) break block19;
                        cWNCPMLX.Q = 0;
                        if (n2 == 0) break block19;
                    }
                    if (n5 == -1 || cWNCPMLX.M == -1 || LKGEGIEW.d[n5].l >= LKGEGIEW.d[cWNCPMLX.M].l) {
                        cWNCPMLX.M = n5;
                        cWNCPMLX.N = 0;
                        cWNCPMLX.O = 0;
                        cWNCPMLX.P = n4;
                        cWNCPMLX.Q = 0;
                        cWNCPMLX.cb = cWNCPMLX.L;
                    }
                }
                if ((n6 & 8) != 0) {
                    n5 = mBMGIXGO.g(0);
                    n4 = mBMGIXGO.b(false);
                    cWNCPMLX.a(-35698, n4, n5, kh);
                    cWNCPMLX.S = kh + 300;
                    cWNCPMLX.T = mBMGIXGO.g(0);
                    cWNCPMLX.U = mBMGIXGO.c();
                }
                if ((n6 & 0x80) != 0) {
                    cWNCPMLX.G = mBMGIXGO.e();
                    n5 = mBMGIXGO.h();
                    cWNCPMLX.K = n5 >> 16;
                    cWNCPMLX.J = kh + (n5 & 0xFFFF);
                    cWNCPMLX.H = 0;
                    cWNCPMLX.I = 0;
                    if (cWNCPMLX.J > kh) {
                        cWNCPMLX.H = -1;
                    }
                    if (cWNCPMLX.G == 65535) {
                        cWNCPMLX.G = -1;
                    }
                }
                if ((n6 & 0x20) != 0) {
                    cWNCPMLX.o = mBMGIXGO.e();
                    if (cWNCPMLX.o == 65535) {
                        cWNCPMLX.o = -1;
                    }
                }
                if ((n6 & 1) != 0) {
                    cWNCPMLX.s = mBMGIXGO.i();
                    cWNCPMLX.V = 100;
                }
                if ((n6 & 0x40) != 0) {
                    n5 = mBMGIXGO.b(false);
                    n4 = mBMGIXGO.h(2);
                    cWNCPMLX.a(-35698, n4, n5, kh);
                    cWNCPMLX.S = kh + 300;
                    cWNCPMLX.T = mBMGIXGO.h(2);
                    cWNCPMLX.U = mBMGIXGO.b(false);
                }
                if ((n6 & 2) != 0) {
                    cWNCPMLX.vb = CKDEJADD.a(mBMGIXGO.d((byte)-74));
                    cWNCPMLX.ab = cWNCPMLX.vb.n;
                    cWNCPMLX.q = cWNCPMLX.vb.y;
                    cWNCPMLX.ob = cWNCPMLX.vb.m;
                    cWNCPMLX.pb = cWNCPMLX.vb.d;
                    cWNCPMLX.qb = cWNCPMLX.vb.C;
                    cWNCPMLX.rb = cWNCPMLX.vb.a;
                    cWNCPMLX.x = cWNCPMLX.vb.w;
                }
                if ((n6 & 4) != 0) {
                    cWNCPMLX.Y = mBMGIXGO.c((byte)108);
                    cWNCPMLX.Z = mBMGIXGO.c((byte)108);
                }
                ++n3;
            } while (n3 < this.bc);
            this.gh &= bl;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("51191, " + n + ", " + mBMGIXGO + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(CKDEJADD cKDEJADD, int n, boolean bl, int n2, int n3) {
        int n4 = Jj;
        try {
            int n5;
            if (this.Ig >= 400) {
                return;
            }
            if (cKDEJADD.H != null) {
                cKDEJADD = cKDEJADD.b(this.Lb);
            }
            if (cKDEJADD == null) {
                return;
            }
            if (!cKDEJADD.D) {
                return;
            }
            String string = cKDEJADD.k;
            if (bl) {
                boolean bl2 = Bc = !Bc;
            }
            if (cKDEJADD.g != 0) {
                string = String.valueOf(string) + client.a(client.Bg.Ab, cKDEJADD.g, true) + " (level-" + cKDEJADD.g + ")";
            }
            if (this.Bj == 1) {
                this.Wh[this.Ig] = "Use " + this.Fj + " with @yel@" + string;
                this.Uf[this.Ig] = 582;
                this.Vf[this.Ig] = n;
                this.Sf[this.Ig] = n3;
                this.Tf[this.Ig] = n2;
                ++this.Ig;
                return;
            }
            if (this.Lg == 1) {
                if ((this.Ng & 2) != 2) return;
                this.Wh[this.Ig] = String.valueOf(this.Og) + " @yel@" + string;
                this.Uf[this.Ig] = 413;
                this.Vf[this.Ig] = n;
                this.Sf[this.Ig] = n3;
                this.Tf[this.Ig] = n2;
                ++this.Ig;
                return;
            }
            if (cKDEJADD.l != null) {
                n5 = 4;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && n4 == 0) continue;
                    if (cKDEJADD.l[n5] != null && !cKDEJADD.l[n5].equalsIgnoreCase("attack")) {
                        this.Wh[this.Ig] = String.valueOf(cKDEJADD.l[n5]) + " @yel@" + string;
                        if (n5 == 0) {
                            this.Uf[this.Ig] = 20;
                        }
                        if (n5 == 1) {
                            this.Uf[this.Ig] = 412;
                        }
                        if (n5 == 2) {
                            this.Uf[this.Ig] = 225;
                        }
                        if (n5 == 3) {
                            this.Uf[this.Ig] = 965;
                        }
                        if (n5 == 4) {
                            this.Uf[this.Ig] = 478;
                        }
                        this.Vf[this.Ig] = n;
                        this.Sf[this.Ig] = n3;
                        this.Tf[this.Ig] = n2;
                        ++this.Ig;
                    }
                    --n5;
                } while (n5 >= 0);
            }
            if (cKDEJADD.l != null) {
                n5 = 4;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && n4 == 0) continue;
                    if (cKDEJADD.l[n5] != null && cKDEJADD.l[n5].equalsIgnoreCase("attack")) {
                        int n6 = 0;
                        if (cKDEJADD.g > client.Bg.Ab) {
                            n6 = 2000;
                        }
                        this.Wh[this.Ig] = String.valueOf(cKDEJADD.l[n5]) + " @yel@" + string;
                        if (n5 == 0) {
                            this.Uf[this.Ig] = 20 + n6;
                        }
                        if (n5 == 1) {
                            this.Uf[this.Ig] = 412 + n6;
                        }
                        if (n5 == 2) {
                            this.Uf[this.Ig] = 225 + n6;
                        }
                        if (n5 == 3) {
                            this.Uf[this.Ig] = 965 + n6;
                        }
                        if (n5 == 4) {
                            this.Uf[this.Ig] = 478 + n6;
                        }
                        this.Vf[this.Ig] = n;
                        this.Sf[this.Ig] = n3;
                        this.Tf[this.Ig] = n2;
                        ++this.Ig;
                    }
                    --n5;
                } while (n5 >= 0);
            }
            this.Wh[this.Ig] = "Examine @yel@" + string;
            this.Uf[this.Ig] = 1025;
            this.Vf[this.Ig] = n;
            this.Sf[this.Ig] = n3;
            this.Tf[this.Ig] = n2;
            ++this.Ig;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("36696, " + cKDEJADD + ", " + n + ", " + bl + ", " + n2 + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, int n2, DLZHLHNK dLZHLHNK, boolean bl, int n3) {
        int n4 = Jj;
        try {
            int n5;
            String string;
            block24: {
                block25: {
                    block23: {
                        block22: {
                            block21: {
                                if (dLZHLHNK == Bg) {
                                    return;
                                }
                                if (this.Ig >= 400) {
                                    return;
                                }
                                if (bl) {
                                    return;
                                }
                                if (dLZHLHNK.Sb != 0) break block21;
                                string = String.valueOf(dLZHLHNK.yb) + client.a(client.Bg.Ab, dLZHLHNK.Ab, true) + " (level-" + dLZHLHNK.Ab + ")";
                                if (n4 == 0) break block22;
                            }
                            string = String.valueOf(dLZHLHNK.yb) + " (skill-" + dLZHLHNK.Sb + ")";
                        }
                        if (this.Bj != 1) break block23;
                        this.Wh[this.Ig] = "Use " + this.Fj + " with @whi@" + string;
                        this.Uf[this.Ig] = 491;
                        this.Vf[this.Ig] = n2;
                        this.Sf[this.Ig] = n;
                        this.Tf[this.Ig] = n3;
                        ++this.Ig;
                        if (n4 == 0) break block24;
                    }
                    if (this.Lg != 1) break block25;
                    if ((this.Ng & 8) != 8) break block24;
                    this.Wh[this.Ig] = String.valueOf(this.Og) + " @whi@" + string;
                    this.Uf[this.Ig] = 365;
                    this.Vf[this.Ig] = n2;
                    this.Sf[this.Ig] = n;
                    this.Tf[this.Ig] = n3;
                    ++this.Ig;
                    if (n4 == 0) break block24;
                }
                n5 = 4;
                boolean bl2 = true;
                do {
                    block26: {
                        int n6;
                        block28: {
                            block27: {
                                block29: {
                                    if (bl2 && !(bl2 = false) && n4 == 0) continue;
                                    if (this.Cg[n5] == null) break block26;
                                    this.Wh[this.Ig] = String.valueOf(this.Cg[n5]) + " @whi@" + string;
                                    n6 = 0;
                                    if (!this.Cg[n5].equalsIgnoreCase("attack")) break block27;
                                    if (dLZHLHNK.Ab > client.Bg.Ab) {
                                        n6 = 2000;
                                    }
                                    if (client.Bg.wb == 0 || dLZHLHNK.wb == 0) break block28;
                                    if (client.Bg.wb != dLZHLHNK.wb) break block29;
                                    n6 = 2000;
                                    if (n4 == 0) break block28;
                                }
                                n6 = 0;
                                if (n4 == 0) break block28;
                            }
                            if (this.Dg[n5]) {
                                n6 = 2000;
                            }
                        }
                        if (n5 == 0) {
                            this.Uf[this.Ig] = 561 + n6;
                        }
                        if (n5 == 1) {
                            this.Uf[this.Ig] = 779 + n6;
                        }
                        if (n5 == 2) {
                            this.Uf[this.Ig] = 27 + n6;
                        }
                        if (n5 == 3) {
                            this.Uf[this.Ig] = 577 + n6;
                        }
                        if (n5 == 4) {
                            this.Uf[this.Ig] = 729 + n6;
                        }
                        this.Vf[this.Ig] = n2;
                        this.Sf[this.Ig] = n;
                        this.Tf[this.Ig] = n3;
                        ++this.Ig;
                    }
                    --n5;
                } while (n5 >= 0);
            }
            n5 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n4 == 0) continue;
                if (this.Uf[n5] == 516) {
                    this.Wh[n5] = "Walk here @whi@" + string;
                    return;
                }
                ++n5;
            } while (n5 < this.Ig);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("46972, " + n + ", " + n2 + ", " + dLZHLHNK + ", " + bl + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void a(boolean bl, DYMVKFXP dYMVKFXP) {
        try {
            int n;
            int n2 = 0;
            int n3 = -1;
            int n4 = 0;
            int n5 = 0;
            if (dYMVKFXP.k == 0) {
                n2 = this.cd.c(dYMVKFXP.j, dYMVKFXP.l, dYMVKFXP.m);
            }
            if (dYMVKFXP.k == 1) {
                n2 = this.cd.f(dYMVKFXP.j, dYMVKFXP.l, 0, dYMVKFXP.m);
            }
            if (dYMVKFXP.k == 2) {
                n2 = this.cd.d(dYMVKFXP.j, dYMVKFXP.l, dYMVKFXP.m);
            }
            if (dYMVKFXP.k == 3) {
                n2 = this.cd.e(dYMVKFXP.j, dYMVKFXP.l, dYMVKFXP.m);
            }
            if (n2 != 0) {
                n = this.cd.g(dYMVKFXP.j, dYMVKFXP.l, dYMVKFXP.m, n2);
                n3 = n2 >> 14 & Short.MAX_VALUE;
                n4 = n & 0x1F;
                n5 = n >> 6;
            }
            dYMVKFXP.n = n3;
            dYMVKFXP.p = n4;
            if (bl) {
                n = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && Jj == 0) continue;
                    ++n;
                } while (n > 0);
            }
            dYMVKFXP.o = n5;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("69945, " + bl + ", " + dYMVKFXP + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void h(boolean bl) {
        int n = Jj;
        try {
            if (bl) {
                this.me = -1;
            }
            int n2 = 0;
            boolean bl2 = true;
            do {
                block18: {
                    block16: {
                        block17: {
                            boolean bl3;
                            block15: {
                                if (bl2 && !(bl2 = false) && n == 0) continue;
                                if (this.Vi[n2] > 0) break block16;
                                bl3 = false;
                                try {
                                    if (this.ei[n2] == this.Ib && this.Mi[n2] == this.Ij) {
                                        if (this.l(11456)) break block15;
                                        bl3 = true;
                                        if (n == 0) break block15;
                                    }
                                    MBMGIXGO mBMGIXGO = JHDAGNBV.a(this.Mi[n2], this.ei[n2], false);
                                    if (System.currentTimeMillis() + (long)(mBMGIXGO.z / 22) <= this.vh + (long)(this.cj / 22)) break block15;
                                    this.cj = mBMGIXGO.z;
                                    this.vh = System.currentTimeMillis();
                                    if (this.a(mBMGIXGO.y, (byte)116, mBMGIXGO.z)) {
                                        this.Ib = this.ei[n2];
                                        this.Ij = this.Mi[n2];
                                        if (n == 0) break block15;
                                    }
                                    bl3 = true;
                                }
                                catch (Exception exception) {
                                    // empty catch block
                                }
                            }
                            if (bl3 && this.Vi[n2] != -5) break block17;
                            --this.pf;
                            int n3 = n2;
                            boolean bl4 = true;
                            do {
                                if (bl4 && !(bl4 = false) && n == 0) continue;
                                this.ei[n3] = this.ei[n3 + 1];
                                this.Mi[n3] = this.Mi[n3 + 1];
                                this.Vi[n3] = this.Vi[n3 + 1];
                                ++n3;
                            } while (n3 < this.pf);
                            --n2;
                            if (n == 0) break block18;
                        }
                        this.Vi[n2] = -5;
                        if (n == 0) break block18;
                    }
                    int n4 = n2;
                    this.Vi[n4] = this.Vi[n4] - 1;
                }
                ++n2;
            } while (n2 < this.pf);
            if (this.ej <= 0) return;
            this.ej -= 20;
            if (this.ej < 0) {
                this.ej = 0;
            }
            if (this.ej != 0) return;
            if (!this.ah) return;
            if (qd) return;
            this.yi = this.md;
            this.zi = true;
            this.vf.b(2, this.yi);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("65150, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a() {
        int n = Jj;
        this.a(20, (byte)4, "Starting up");
        if (signlink.sunjava) {
            this.f = 5;
        }
        if (Xd) {
            this.Xi = true;
            return;
        }
        Xd = true;
        boolean bl = false;
        String string = this.g(true);
        if (string.endsWith("jagex.com")) {
            bl = true;
        }
        if (string.endsWith("runescape.com")) {
            bl = true;
        }
        if (string.endsWith("192.168.1.2")) {
            bl = true;
        }
        if (string.endsWith("192.168.1.229")) {
            bl = true;
        }
        if (string.endsWith("192.168.1.228")) {
            bl = true;
        }
        if (string.endsWith("192.168.1.227")) {
            bl = true;
        }
        if (string.endsWith("192.168.1.226")) {
            bl = true;
        }
        if (string.endsWith("127.0.0.1")) {
            bl = true;
        }
        if (!bl) {
            this.zh = true;
            return;
        }
        if (signlink.cache_dat != null) {
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n == 0) continue;
                this.Ad[n2] = new IGSLDTHC(500000, signlink.cache_dat, signlink.cache_idx[n2], n2 + 1, true);
                ++n2;
            } while (n2 < 5);
        }
        try {
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            int n10;
            this.h(533);
            this.ff = this.a(1, "title screen", "title", this.Rf[1], (byte)-41, 25);
            this.pj = new YXVQXWYR(false, "p11_full", 0, this.ff);
            this.qj = new YXVQXWYR(false, "p12_full", 0, this.ff);
            this.rj = new YXVQXWYR(false, "b12_full", 0, this.ff);
            this.sj = new YXVQXWYR(true, "q8_full", 0, this.ff);
            this.r(0);
            this.o(215);
            XTGLDHGX xTGLDHGX = this.a(2, "config", "config", this.Rf[2], (byte)-41, 30);
            XTGLDHGX xTGLDHGX2 = this.a(3, "interface", "interface", this.Rf[3], (byte)-41, 35);
            XTGLDHGX xTGLDHGX3 = this.a(4, "2d graphics", "media", this.Rf[4], (byte)-41, 40);
            XTGLDHGX xTGLDHGX4 = this.a(6, "textures", "textures", this.Rf[6], (byte)-41, 45);
            XTGLDHGX xTGLDHGX5 = this.a(7, "chat system", "wordenc", this.Rf[7], (byte)-41, 50);
            XTGLDHGX xTGLDHGX6 = this.a(8, "sound effects", "sounds", this.Rf[8], (byte)-41, 55);
            this.dj = new byte[4][104][104];
            this.li = new int[4][105][105];
            this.cd = new NYFUGYQS(104, 43, 104, this.li, 4);
            int n11 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n == 0) continue;
                this.Bi[n11] = new FTPNODIB(104, 104, true);
                ++n11;
            } while (n11 < 4);
            this.ij = new CXGZMTJK(512, 512);
            XTGLDHGX xTGLDHGX7 = this.a(5, "update list", "versionlist", this.Rf[5], (byte)-41, 60);
            this.a(60, (byte)4, "Connecting to update server");
            this.vf = new GHOWLKWN();
            this.vf.a(xTGLDHGX7, this);
            SQHJOGRT.a(this.vf.c(0));
            ZKARKDQW.a(this.vf.a(79, 0), this.vf);
            if (!qd) {
                this.yi = 0;
                try {
                    this.yi = Integer.parseInt(this.getParameter("music"));
                }
                catch (Exception exception) {}
                this.zi = true;
                this.vf.b(2, this.yi);
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && n == 0) continue;
                    this.f(false);
                    try {
                        Thread.sleep(100L);
                    }
                    catch (Exception exception) {}
                    if (this.vf.u <= 3) continue;
                    this.a("ondemand");
                    return;
                } while (this.vf.a() > 0);
            }
            this.a(65, (byte)4, "Requesting animations");
            int n12 = this.vf.a(79, 1);
            int n13 = 0;
            boolean bl5 = true;
            do {
                if (bl5 && !(bl5 = false) && n == 0) continue;
                this.vf.b(1, n13);
                ++n13;
            } while (n13 < n12);
            boolean bl6 = true;
            do {
                if (bl6 && !(bl6 = false) && n == 0) continue;
                n10 = n12 - this.vf.a();
                if (n10 > 0) {
                    this.a(65, (byte)4, "Loading animations - " + n10 * 100 / n12 + "%");
                }
                this.f(false);
                try {
                    Thread.sleep(100L);
                }
                catch (Exception exception) {}
                if (this.vf.u <= 3) continue;
                this.a("ondemand");
                return;
            } while (this.vf.a() > 0);
            this.a(70, (byte)4, "Requesting models");
            n12 = this.vf.a(79, 0);
            n10 = 0;
            boolean bl7 = true;
            do {
                if (bl7 && !(bl7 = false) && n == 0) continue;
                n9 = this.vf.c(n10, -203);
                if ((n9 & 1) != 0) {
                    this.vf.b(0, n10);
                }
                ++n10;
            } while (n10 < n12);
            n12 = this.vf.a();
            boolean bl8 = true;
            do {
                if (bl8 && !(bl8 = false) && n == 0) continue;
                n9 = n12 - this.vf.a();
                if (n9 > 0) {
                    this.a(70, (byte)4, "Loading models - " + n9 * 100 / n12 + "%");
                }
                this.f(false);
                try {
                    Thread.sleep(100L);
                }
                catch (Exception exception) {}
            } while (this.vf.a() > 0);
            if (this.Ad[0] != null) {
                this.a(75, (byte)4, "Requesting maps");
                this.vf.b(3, this.vf.a(0, 0, 48, 47));
                this.vf.b(3, this.vf.a(1, 0, 48, 47));
                this.vf.b(3, this.vf.a(0, 0, 48, 48));
                this.vf.b(3, this.vf.a(1, 0, 48, 48));
                this.vf.b(3, this.vf.a(0, 0, 48, 49));
                this.vf.b(3, this.vf.a(1, 0, 48, 49));
                this.vf.b(3, this.vf.a(0, 0, 47, 47));
                this.vf.b(3, this.vf.a(1, 0, 47, 47));
                this.vf.b(3, this.vf.a(0, 0, 47, 48));
                this.vf.b(3, this.vf.a(1, 0, 47, 48));
                this.vf.b(3, this.vf.a(0, 0, 148, 48));
                this.vf.b(3, this.vf.a(1, 0, 148, 48));
                n12 = this.vf.a();
                boolean bl9 = true;
                do {
                    if (bl9 && !(bl9 = false) && n == 0) continue;
                    n9 = n12 - this.vf.a();
                    if (n9 > 0) {
                        this.a(75, (byte)4, "Loading maps - " + n9 * 100 / n12 + "%");
                    }
                    this.f(false);
                    try {
                        Thread.sleep(100L);
                    }
                    catch (Exception exception) {}
                } while (this.vf.a() > 0);
            }
            n12 = this.vf.a(79, 0);
            n9 = 0;
            boolean bl10 = true;
            do {
                block84: {
                    block89: {
                        block88: {
                            block87: {
                                block86: {
                                    block85: {
                                        block83: {
                                            if (bl10 && !(bl10 = false) && n == 0) continue;
                                            n8 = this.vf.c(n9, -203);
                                            n7 = 0;
                                            if ((n8 & 8) == 0) break block83;
                                            n7 = 10;
                                            if (n == 0) break block84;
                                        }
                                        if ((n8 & 0x20) == 0) break block85;
                                        n7 = 9;
                                        if (n == 0) break block84;
                                    }
                                    if ((n8 & 0x10) == 0) break block86;
                                    n7 = 8;
                                    if (n == 0) break block84;
                                }
                                if ((n8 & 0x40) == 0) break block87;
                                n7 = 7;
                                if (n == 0) break block84;
                            }
                            if ((n8 & 0x80) == 0) break block88;
                            n7 = 6;
                            if (n == 0) break block84;
                        }
                        if ((n8 & 2) == 0) break block89;
                        n7 = 5;
                        if (n == 0) break block84;
                    }
                    if ((n8 & 4) != 0) {
                        n7 = 4;
                    }
                }
                if ((n8 & 1) != 0) {
                    n7 = 3;
                }
                if (n7 != 0) {
                    this.vf.a((byte)n7, 0, n9, (byte)8);
                }
                ++n9;
            } while (n9 < n12);
            this.vf.a(pd, 0);
            if (!qd) {
                n12 = this.vf.a(79, 2);
                n8 = 1;
                boolean bl11 = true;
                do {
                    if (bl11 && !(bl11 = false) && n == 0) continue;
                    if (this.vf.e(n8, 5)) {
                        this.vf.a((byte)1, 2, n8, (byte)8);
                    }
                    ++n8;
                } while (n8 < n12);
            }
            this.a(80, (byte)4, "Unpacking media");
            this.Th = new DSMJIEPN(xTGLDHGX3, "invback", 0);
            this.Vh = new DSMJIEPN(xTGLDHGX3, "chatback", 0);
            this.Uh = new DSMJIEPN(xTGLDHGX3, "mapback", 0);
            this.Fe = new DSMJIEPN(xTGLDHGX3, "backbase1", 0);
            this.Ge = new DSMJIEPN(xTGLDHGX3, "backbase2", 0);
            this.He = new DSMJIEPN(xTGLDHGX3, "backhmid1", 0);
            n8 = 0;
            boolean bl12 = true;
            do {
                if (bl12 && !(bl12 = false) && n == 0) continue;
                this.dd[n8] = new DSMJIEPN(xTGLDHGX3, "sideicons", n8);
                ++n8;
            } while (n8 < 13);
            this.xg = new CXGZMTJK(xTGLDHGX3, "compass", 0);
            this.fe = new CXGZMTJK(xTGLDHGX3, "mapedge", 0);
            this.fe.c(5059);
            try {
                n7 = 0;
                boolean bl13 = true;
                do {
                    if (bl13 && !(bl13 = false) && n == 0) continue;
                    this.nf[n7] = new DSMJIEPN(xTGLDHGX3, "mapscene", n7);
                    ++n7;
                } while (n7 < 100);
            }
            catch (Exception exception) {}
            try {
                n7 = 0;
                boolean bl14 = true;
                do {
                    if (bl14 && !(bl14 = false) && n == 0) continue;
                    this.Le[n7] = new CXGZMTJK(xTGLDHGX3, "mapfunction", n7);
                    ++n7;
                } while (n7 < 100);
            }
            catch (Exception exception) {}
            try {
                n7 = 0;
                boolean bl15 = true;
                do {
                    if (bl15 && !(bl15 = false) && n == 0) continue;
                    this.Rd[n7] = new CXGZMTJK(xTGLDHGX3, "hitmarks", n7);
                    ++n7;
                } while (n7 < 20);
            }
            catch (Exception exception) {}
            try {
                n7 = 0;
                boolean bl16 = true;
                do {
                    if (bl16 && !(bl16 = false) && n == 0) continue;
                    this.Wf[n7] = new CXGZMTJK(xTGLDHGX3, "headicons", n7);
                    ++n7;
                } while (n7 < 20);
            }
            catch (Exception exception) {}
            this.Eb = new CXGZMTJK(xTGLDHGX3, "mapmarker", 0);
            this.Fb = new CXGZMTJK(xTGLDHGX3, "mapmarker", 1);
            n7 = 0;
            boolean bl17 = true;
            do {
                if (bl17 && !(bl17 = false) && n == 0) continue;
                this.Zg[n7] = new CXGZMTJK(xTGLDHGX3, "cross", n7);
                ++n7;
            } while (n7 < 8);
            this.Bf = new CXGZMTJK(xTGLDHGX3, "mapdots", 0);
            this.Cf = new CXGZMTJK(xTGLDHGX3, "mapdots", 1);
            this.Df = new CXGZMTJK(xTGLDHGX3, "mapdots", 2);
            this.Ef = new CXGZMTJK(xTGLDHGX3, "mapdots", 3);
            this.Ff = new CXGZMTJK(xTGLDHGX3, "mapdots", 4);
            this.Ce = new DSMJIEPN(xTGLDHGX3, "scrollbar", 0);
            this.De = new DSMJIEPN(xTGLDHGX3, "scrollbar", 1);
            this.Sg = new DSMJIEPN(xTGLDHGX3, "redstone1", 0);
            this.Tg = new DSMJIEPN(xTGLDHGX3, "redstone2", 0);
            this.Ug = new DSMJIEPN(xTGLDHGX3, "redstone3", 0);
            this.Vg = new DSMJIEPN(xTGLDHGX3, "redstone1", 0);
            this.Vg.b(0);
            this.Wg = new DSMJIEPN(xTGLDHGX3, "redstone2", 0);
            this.Wg.b(0);
            this.zb = new DSMJIEPN(xTGLDHGX3, "redstone1", 0);
            this.zb.d(true);
            this.Ab = new DSMJIEPN(xTGLDHGX3, "redstone2", 0);
            this.Ab.d(true);
            this.Bb = new DSMJIEPN(xTGLDHGX3, "redstone3", 0);
            this.Bb.d(true);
            this.Cb = new DSMJIEPN(xTGLDHGX3, "redstone1", 0);
            this.Cb.b(0);
            this.Cb.d(true);
            this.Db = new DSMJIEPN(xTGLDHGX3, "redstone2", 0);
            this.Db.b(0);
            this.Db.d(true);
            int n14 = 0;
            boolean bl18 = true;
            do {
                if (bl18 && !(bl18 = false) && n == 0) continue;
                this.qi[n14] = new DSMJIEPN(xTGLDHGX3, "mod_icons", n14);
                ++n14;
            } while (n14 < 2);
            CXGZMTJK cXGZMTJK = new CXGZMTJK(xTGLDHGX3, "backleft1", 0);
            this.lc = new IVIFZQBK(cXGZMTJK.J, cXGZMTJK.K, this.f(0), 0);
            cXGZMTJK.a(0, 0, -32357);
            cXGZMTJK = new CXGZMTJK(xTGLDHGX3, "backleft2", 0);
            this.mc = new IVIFZQBK(cXGZMTJK.J, cXGZMTJK.K, this.f(0), 0);
            cXGZMTJK.a(0, 0, -32357);
            cXGZMTJK = new CXGZMTJK(xTGLDHGX3, "backright1", 0);
            this.nc = new IVIFZQBK(cXGZMTJK.J, cXGZMTJK.K, this.f(0), 0);
            cXGZMTJK.a(0, 0, -32357);
            cXGZMTJK = new CXGZMTJK(xTGLDHGX3, "backright2", 0);
            this.oc = new IVIFZQBK(cXGZMTJK.J, cXGZMTJK.K, this.f(0), 0);
            cXGZMTJK.a(0, 0, -32357);
            cXGZMTJK = new CXGZMTJK(xTGLDHGX3, "backtop1", 0);
            this.pc = new IVIFZQBK(cXGZMTJK.J, cXGZMTJK.K, this.f(0), 0);
            cXGZMTJK.a(0, 0, -32357);
            cXGZMTJK = new CXGZMTJK(xTGLDHGX3, "backvmid1", 0);
            this.qc = new IVIFZQBK(cXGZMTJK.J, cXGZMTJK.K, this.f(0), 0);
            cXGZMTJK.a(0, 0, -32357);
            cXGZMTJK = new CXGZMTJK(xTGLDHGX3, "backvmid2", 0);
            this.rc = new IVIFZQBK(cXGZMTJK.J, cXGZMTJK.K, this.f(0), 0);
            cXGZMTJK.a(0, 0, -32357);
            cXGZMTJK = new CXGZMTJK(xTGLDHGX3, "backvmid3", 0);
            this.sc = new IVIFZQBK(cXGZMTJK.J, cXGZMTJK.K, this.f(0), 0);
            cXGZMTJK.a(0, 0, -32357);
            cXGZMTJK = new CXGZMTJK(xTGLDHGX3, "backhmid2", 0);
            this.tc = new IVIFZQBK(cXGZMTJK.J, cXGZMTJK.K, this.f(0), 0);
            cXGZMTJK.a(0, 0, -32357);
            int n15 = (int)(Math.random() * 21.0) - 10;
            int n16 = (int)(Math.random() * 21.0) - 10;
            int n17 = (int)(Math.random() * 21.0) - 10;
            int n18 = (int)(Math.random() * 41.0) - 20;
            int n19 = 0;
            boolean bl19 = true;
            do {
                if (bl19 && !(bl19 = false) && n == 0) continue;
                if (this.Le[n19] != null) {
                    this.Le[n19].a(n15 + n18, n16 + n18, n17 + n18, 0);
                }
                if (this.nf[n19] != null) {
                    this.nf[n19].a(n15 + n18, n16 + n18, n17 + n18, 0);
                }
                ++n19;
            } while (n19 < 100);
            this.a(83, (byte)4, "Unpacking textures");
            OPPOFIOL.a(xTGLDHGX4, 0);
            OPPOFIOL.a(0.8, Xh);
            OPPOFIOL.a(20, true);
            this.a(86, (byte)4, "Unpacking config");
            LKGEGIEW.a(0, xTGLDHGX);
            YZDBYLRM.a(xTGLDHGX);
            MNHKFPQO.a(0, xTGLDHGX);
            DJRMEMXO.a(xTGLDHGX);
            CKDEJADD.a(xTGLDHGX);
            TAVAECED.a(0, xTGLDHGX);
            MUDLUUBC.a(0, xTGLDHGX);
            VGXVBFVC.a(0, xTGLDHGX);
            SXYSOXTR.a(0, xTGLDHGX);
            DJRMEMXO.C = pd;
            if (!qd) {
                this.a(90, (byte)4, "Unpacking sounds");
                byte[] byArray = xTGLDHGX6.a("sounds.dat", null);
                MBMGIXGO mBMGIXGO = new MBMGIXGO(byArray, 891);
                JHDAGNBV.a(0, mBMGIXGO);
            }
            this.a(95, (byte)4, "Unpacking interfaces");
            YXVQXWYR[] yXVQXWYRArray = new YXVQXWYR[]{this.pj, this.qj, this.rj, this.sj};
            DUCMKFAY.a(xTGLDHGX2, yXVQXWYRArray, (byte)-84, xTGLDHGX3);
            this.a(100, (byte)4, "Preparing game engine");
            int n20 = 0;
            boolean bl20 = true;
            do {
                if (bl20 && !(bl20 = false) && n == 0) continue;
                n6 = 999;
                n5 = 0;
                n4 = 0;
                boolean bl21 = true;
                do {
                    block91: {
                        block90: {
                            if (bl21 && !(bl21 = false) && n == 0) continue;
                            if (this.Uh.B[n4 + n20 * this.Uh.D] != 0) break block90;
                            if (n6 != 999) break block91;
                            n6 = n4;
                            if (n == 0) break block91;
                        }
                        if (n6 != 999) {
                            n5 = n4;
                            if (n == 0) break;
                        }
                    }
                    ++n4;
                } while (n4 < 34);
                this.yd[n20] = n6;
                this.kf[n20] = n5 - n6;
                ++n20;
            } while (n20 < 33);
            n6 = 5;
            boolean bl22 = true;
            do {
                if (bl22 && !(bl22 = false) && n == 0) continue;
                n5 = 999;
                n4 = 0;
                n3 = 25;
                boolean bl23 = true;
                do {
                    block93: {
                        block92: {
                            if (bl23 && !(bl23 = false) && n == 0) continue;
                            if (this.Uh.B[n3 + n6 * this.Uh.D] != 0 || n3 <= 34 && n6 <= 34) break block92;
                            if (n5 != 999) break block93;
                            n5 = n3;
                            if (n == 0) break block93;
                        }
                        if (n5 != 999) {
                            n4 = n3;
                            if (n == 0) break;
                        }
                    }
                    ++n3;
                } while (n3 < 172);
                this.ef[n6 - 5] = n5 - 25;
                this.Ai[n6 - 5] = n4 - n5;
                ++n6;
            } while (n6 < 156);
            OPPOFIOL.a(-950, 479, 96);
            this.Dh = OPPOFIOL.L;
            OPPOFIOL.a(-950, 190, 261);
            this.Eh = OPPOFIOL.L;
            OPPOFIOL.a(-950, 512, 334);
            this.Fh = OPPOFIOL.L;
            int[] nArray = new int[9];
            n4 = 0;
            boolean bl24 = true;
            do {
                if (bl24 && !(bl24 = false) && n == 0) continue;
                n3 = 128 + n4 * 32 + 15;
                int n21 = 600 + n3 * 3;
                int n22 = OPPOFIOL.J[n3];
                nArray[n4] = n21 * n22 >> 16;
                ++n4;
            } while (n4 < 9);
            NYFUGYQS.a(500, 800, 512, 334, nArray, Ci);
            RKAYAFDQ.a(xTGLDHGX5);
            this.Nb = new ZIJPRJEC(this, Xf);
            this.a(this.Nb, 10);
            WBWOBAFW.x = this;
            YZDBYLRM.D = this;
            CKDEJADD.B = this;
            return;
        }
        catch (Exception exception) {
            signlink.reporterror("loaderror " + this.bf + " " + this.Gf);
            this.Ic = true;
            return;
        }
    }

    private final void a(MBMGIXGO mBMGIXGO, int n, byte by) {
        int n2 = Jj;
        try {
            int n3;
            block12: {
                block11: {
                    if (by != 8) break block11;
                    by = 0;
                    if (n2 == 0) break block12;
                }
                this.ug = -50;
            }
            while (mBMGIXGO.A + 10 < n * 8 && (n3 = mBMGIXGO.c(11, 0)) != 2047) {
                int n4;
                if (this.Yb[n3] == null) {
                    this.Yb[n3] = new DLZHLHNK();
                    if (this.dc[n3] != null) {
                        this.Yb[n3].a(0, this.dc[n3]);
                    }
                }
                this.ac[this.Zb++] = n3;
                DLZHLHNK dLZHLHNK = this.Yb[n3];
                dLZHLHNK.X = kh;
                int n5 = mBMGIXGO.c(1, 0);
                if (n5 == 1) {
                    this.cc[this.bc++] = n3;
                }
                int n6 = mBMGIXGO.c(1, 0);
                int n7 = mBMGIXGO.c(5, 0);
                if (n7 > 15) {
                    n7 -= 32;
                }
                if ((n4 = mBMGIXGO.c(5, 0)) > 15) {
                    n4 -= 32;
                }
                dLZHLHNK.a(client.Bg.m[0] + n4, client.Bg.n[0] + n7, n6 == 1, false);
                if (n2 == 0) continue;
            }
            mBMGIXGO.a(true);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("22315, " + mBMGIXGO + ", " + n + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void i(boolean bl) {
        try {
            this.gh &= bl;
            if (this.ze != 0) {
                return;
            }
            if (this.z == 1) {
                int n;
                int n2 = this.A - 25 - 550;
                int n3 = this.B - 5 - 4;
                if (n2 >= 0 && n3 >= 0 && n2 < 146 && n3 < 151) {
                    int n4;
                    int n5;
                    boolean bl2;
                    n = this.Ih + this.gi & 0x7FF;
                    int n6 = OPPOFIOL.J[n];
                    int n7 = OPPOFIOL.K[n];
                    int n8 = (n3 -= 75) * (n7 = n7 * (this.th + 256) >> 8) - (n2 -= 73) * (n6 = n6 * (this.th + 256) >> 8) >> 11;
                    int n9 = client.Bg.lb - n8 >> 7;
                    if (bl2 = this.a(1, 0, 0, -11308, 0, client.Bg.n[0], 0, 0, n9, client.Bg.m[0], true, n5 = client.Bg.kb + (n4 = n3 * n6 + n2 * n7 >> 11) >> 7)) {
                        this.Ph.a(n2);
                        this.Ph.a(n3);
                        this.Ph.b(this.Ih);
                        this.Ph.a(57);
                        this.Ph.a(this.gi);
                        this.Ph.a(this.th);
                        this.Ph.a(89);
                        this.Ph.b(client.Bg.kb);
                        this.Ph.b(client.Bg.lb);
                        this.Ph.a(this.jj);
                        this.Ph.a(63);
                    }
                }
                if (++sg > 1151) {
                    sg = 0;
                    this.Ph.a((byte)6, 246);
                    this.Ph.a(0);
                    n = this.Ph.z;
                    if ((int)(Math.random() * 2.0) == 0) {
                        this.Ph.a(101);
                    }
                    this.Ph.a(197);
                    this.Ph.b((int)(Math.random() * 65536.0));
                    this.Ph.a((int)(Math.random() * 256.0));
                    this.Ph.a(67);
                    this.Ph.b(14214);
                    if ((int)(Math.random() * 2.0) == 0) {
                        this.Ph.b(29487);
                    }
                    this.Ph.b((int)(Math.random() * 65536.0));
                    if ((int)(Math.random() * 2.0) == 0) {
                        this.Ph.a(220);
                    }
                    this.Ph.a(180);
                    this.Ph.a(this.Ph.z - n, (byte)0);
                    return;
                }
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("13593, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final String f(int n, int n2) {
        try {
            if (n <= 0) {
                this.me = this.Kf.c();
            }
            if (n2 < 999999999) {
                return String.valueOf(n2);
            }
            return "*";
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("1025, " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void E(int n) {
        try {
            int n2;
            if (n != -13873) {
                int n3 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && Jj == 0) continue;
                    ++n3;
                } while (n3 > 0);
            }
            Graphics graphics = this.f(0).getGraphics();
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, 765, 503);
            this.a(false, 1);
            if (this.Ic) {
                this.R = false;
                graphics.setFont(new Font("Helvetica", 1, 16));
                graphics.setColor(Color.yellow);
                n2 = 35;
                graphics.drawString("Sorry, an error has occured whilst loading RuneScape", 30, n2);
                graphics.setColor(Color.white);
                graphics.drawString("To fix this try the following (in order):", 30, n2 += 50);
                graphics.setColor(Color.white);
                graphics.setFont(new Font("Helvetica", 1, 12));
                graphics.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, n2 += 50);
                graphics.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30, n2 += 30);
                graphics.drawString("3: Try using a different game-world", 30, n2 += 30);
                graphics.drawString("4: Try rebooting your computer", 30, n2 += 30);
                graphics.drawString("5: Try selecting a different version of Java from the play-game menu", 30, n2 += 30);
            }
            if (this.zh) {
                this.R = false;
                graphics.setFont(new Font("Helvetica", 1, 20));
                graphics.setColor(Color.white);
                graphics.drawString("Error - unable to load game!", 50, 50);
                graphics.drawString("To play RuneScape make sure you play from", 50, 100);
                graphics.drawString("http://www.runescape.com", 50, 150);
            }
            if (!this.Xi) return;
            this.R = false;
            graphics.setColor(Color.yellow);
            n2 = 35;
            graphics.drawString("Error a copy of RuneScape already appears to be loaded", 30, n2);
            graphics.setColor(Color.white);
            graphics.drawString("To fix this try the following (in order):", 30, n2 += 50);
            graphics.setColor(Color.white);
            graphics.setFont(new Font("Helvetica", 1, 12));
            graphics.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, n2 += 50);
            graphics.drawString("2: Try rebooting your computer, and reloading", 30, n2 += 30);
            n2 += 30;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("4031, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final URL getCodeBase() {
        if (signlink.mainapp != null) {
            return signlink.mainapp.getCodeBase();
        }
        try {
            if (this.o != null) {
                return new URL("http://127.0.0.1:" + (80 + od));
            }
        }
        catch (Exception exception) {}
        return super.getCodeBase();
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void F(int n) {
        try {
            int n2 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && Jj == 0) continue;
                int n3 = this.X[n2];
                CWNCPMLX cWNCPMLX = this.V[n3];
                if (cWNCPMLX != null) {
                    this.a(46988, (int)cWNCPMLX.vb.n, cWNCPMLX);
                }
                ++n2;
            } while (n2 < this.W);
            if (n == -8066) return;
            this.pi = 148;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("41621, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(int n, int n2, GQOSZKJC gQOSZKJC) {
        int n3 = Jj;
        try {
            block9: {
                block10: {
                    block8: {
                        if (n != 46988) {
                            this.me = -1;
                        }
                        if (gQOSZKJC.kb < 128 || gQOSZKJC.lb < 128 || gQOSZKJC.kb >= 13184 || gQOSZKJC.lb >= 13184) {
                            gQOSZKJC.M = -1;
                            gQOSZKJC.G = -1;
                            gQOSZKJC.hb = 0;
                            gQOSZKJC.ib = 0;
                            gQOSZKJC.kb = gQOSZKJC.m[0] * 128 + gQOSZKJC.ab * 64;
                            gQOSZKJC.lb = gQOSZKJC.n[0] * 128 + gQOSZKJC.ab * 64;
                            gQOSZKJC.a(true);
                        }
                        if (gQOSZKJC == Bg && (gQOSZKJC.kb < 1536 || gQOSZKJC.lb < 1536 || gQOSZKJC.kb >= 11776 || gQOSZKJC.lb >= 11776)) {
                            gQOSZKJC.M = -1;
                            gQOSZKJC.G = -1;
                            gQOSZKJC.hb = 0;
                            gQOSZKJC.ib = 0;
                            gQOSZKJC.kb = gQOSZKJC.m[0] * 128 + gQOSZKJC.ab * 64;
                            gQOSZKJC.lb = gQOSZKJC.n[0] * 128 + gQOSZKJC.ab * 64;
                            gQOSZKJC.a(true);
                        }
                        if (gQOSZKJC.hb <= kh) break block8;
                        this.a(gQOSZKJC, true);
                        if (n3 == 0) break block9;
                    }
                    if (gQOSZKJC.ib < kh) break block10;
                    this.a(gQOSZKJC, this.qe);
                    if (n3 == 0) break block9;
                }
                this.a((byte)34, gQOSZKJC);
            }
            this.a(gQOSZKJC, -843);
            this.b(gQOSZKJC, -805);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("29397, " + n + ", " + n2 + ", " + gQOSZKJC + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(GQOSZKJC gQOSZKJC, boolean bl) {
        try {
            int n = gQOSZKJC.hb - kh;
            int n2 = gQOSZKJC.db * 128 + gQOSZKJC.ab * 64;
            int n3 = gQOSZKJC.fb * 128 + gQOSZKJC.ab * 64;
            gQOSZKJC.kb += (n2 - gQOSZKJC.kb) / n;
            if (!bl) {
                return;
            }
            gQOSZKJC.lb += (n3 - gQOSZKJC.lb) / n;
            gQOSZKJC.p = 0;
            if (gQOSZKJC.jb == 0) {
                gQOSZKJC.w = 1024;
            }
            if (gQOSZKJC.jb == 1) {
                gQOSZKJC.w = 1536;
            }
            if (gQOSZKJC.jb == 2) {
                gQOSZKJC.w = 0;
            }
            if (gQOSZKJC.jb == 3) {
                gQOSZKJC.w = 512;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("99888, " + gQOSZKJC + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(GQOSZKJC gQOSZKJC, byte by) {
        try {
            if (gQOSZKJC.ib == kh || gQOSZKJC.M == -1 || gQOSZKJC.P != 0 || gQOSZKJC.O + 1 > LKGEGIEW.d[gQOSZKJC.M].a(gQOSZKJC.N, (byte)-39)) {
                int n = gQOSZKJC.ib - gQOSZKJC.hb;
                int n2 = kh - gQOSZKJC.hb;
                int n3 = gQOSZKJC.db * 128 + gQOSZKJC.ab * 64;
                int n4 = gQOSZKJC.fb * 128 + gQOSZKJC.ab * 64;
                int n5 = gQOSZKJC.eb * 128 + gQOSZKJC.ab * 64;
                int n6 = gQOSZKJC.gb * 128 + gQOSZKJC.ab * 64;
                gQOSZKJC.kb = (n3 * (n - n2) + n5 * n2) / n;
                gQOSZKJC.lb = (n4 * (n - n2) + n6 * n2) / n;
            }
            gQOSZKJC.p = 0;
            if (gQOSZKJC.jb == 0) {
                gQOSZKJC.w = 1024;
            }
            if (gQOSZKJC.jb == 1) {
                gQOSZKJC.w = 1536;
            }
            if (gQOSZKJC.jb == 2) {
                gQOSZKJC.w = 0;
            }
            if (gQOSZKJC.jb == 3) {
                gQOSZKJC.w = 512;
            }
            gQOSZKJC.mb = gQOSZKJC.w;
            if (by != this.qe) {
                Xf = -383;
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("10794, " + gQOSZKJC + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(byte by, GQOSZKJC gQOSZKJC) {
        int n = Jj;
        try {
            int n2;
            int n3;
            block57: {
                int n4;
                int n5;
                block56: {
                    block55: {
                        int n6;
                        block54: {
                            int n7;
                            block52: {
                                int n8;
                                block53: {
                                    block51: {
                                        block45: {
                                            block50: {
                                                block47: {
                                                    block49: {
                                                        block48: {
                                                            block43: {
                                                                block46: {
                                                                    block44: {
                                                                        gQOSZKJC.D = gQOSZKJC.x;
                                                                        if (gQOSZKJC.L == 0) {
                                                                            gQOSZKJC.p = 0;
                                                                            return;
                                                                        }
                                                                        if (gQOSZKJC.M != -1 && gQOSZKJC.P == 0) {
                                                                            LKGEGIEW lKGEGIEW = LKGEGIEW.d[gQOSZKJC.M];
                                                                            if (gQOSZKJC.cb > 0 && lKGEGIEW.p == 0) {
                                                                                ++gQOSZKJC.p;
                                                                                return;
                                                                            }
                                                                            if (gQOSZKJC.cb <= 0 && lKGEGIEW.q == 0) {
                                                                                ++gQOSZKJC.p;
                                                                                return;
                                                                            }
                                                                        }
                                                                        n6 = gQOSZKJC.kb;
                                                                        n5 = gQOSZKJC.lb;
                                                                        n3 = gQOSZKJC.m[gQOSZKJC.L - 1] * 128 + gQOSZKJC.ab * 64;
                                                                        n2 = gQOSZKJC.n[gQOSZKJC.L - 1] * 128 + gQOSZKJC.ab * 64;
                                                                        if (n3 - n6 > 256 || n3 - n6 < -256 || n2 - n5 > 256 || n2 - n5 < -256) {
                                                                            gQOSZKJC.kb = n3;
                                                                            gQOSZKJC.lb = n2;
                                                                            return;
                                                                        }
                                                                        if (n6 >= n3) break block43;
                                                                        if (n5 >= n2) break block44;
                                                                        gQOSZKJC.w = 1280;
                                                                        if (n == 0) break block45;
                                                                    }
                                                                    if (n5 <= n2) break block46;
                                                                    gQOSZKJC.w = 1792;
                                                                    if (n == 0) break block45;
                                                                }
                                                                gQOSZKJC.w = 1536;
                                                                if (n == 0) break block45;
                                                            }
                                                            if (n6 <= n3) break block47;
                                                            if (n5 >= n2) break block48;
                                                            gQOSZKJC.w = 768;
                                                            if (n == 0) break block45;
                                                        }
                                                        if (n5 <= n2) break block49;
                                                        gQOSZKJC.w = 256;
                                                        if (n == 0) break block45;
                                                    }
                                                    gQOSZKJC.w = 512;
                                                    if (n == 0) break block45;
                                                }
                                                if (n5 >= n2) break block50;
                                                gQOSZKJC.w = 1024;
                                                if (n == 0) break block45;
                                            }
                                            gQOSZKJC.w = 0;
                                        }
                                        if ((n8 = gQOSZKJC.w - gQOSZKJC.mb & 0x7FF) > 1024) {
                                            n8 -= 2048;
                                        }
                                        n7 = gQOSZKJC.pb;
                                        if (n8 < -256 || n8 > 256) break block51;
                                        n7 = gQOSZKJC.ob;
                                        if (n == 0) break block52;
                                    }
                                    if (n8 < 256 || n8 >= 768) break block53;
                                    n7 = gQOSZKJC.rb;
                                    if (n == 0) break block52;
                                }
                                if (n8 >= -768 && n8 <= -256) {
                                    n7 = gQOSZKJC.qb;
                                }
                            }
                            if (n7 == -1) {
                                n7 = gQOSZKJC.ob;
                            }
                            gQOSZKJC.D = n7;
                            n4 = 4;
                            if (by != 34) {
                                Xf = 285;
                            }
                            if (gQOSZKJC.mb != gQOSZKJC.w && gQOSZKJC.o == -1 && gQOSZKJC.q != 0) {
                                n4 = 2;
                            }
                            if (gQOSZKJC.L > 2) {
                                n4 = 6;
                            }
                            if (gQOSZKJC.L > 3) {
                                n4 = 8;
                            }
                            if (gQOSZKJC.p > 0 && gQOSZKJC.L > 1) {
                                n4 = 8;
                                --gQOSZKJC.p;
                            }
                            if (gQOSZKJC.nb[gQOSZKJC.L - 1]) {
                                n4 <<= 1;
                            }
                            if (n4 >= 8 && gQOSZKJC.D == gQOSZKJC.ob && gQOSZKJC.r != -1) {
                                gQOSZKJC.D = gQOSZKJC.r;
                            }
                            if (n6 >= n3) break block54;
                            gQOSZKJC.kb += n4;
                            if (gQOSZKJC.kb <= n3) break block55;
                            gQOSZKJC.kb = n3;
                            if (n == 0) break block55;
                        }
                        if (n6 > n3) {
                            gQOSZKJC.kb -= n4;
                            if (gQOSZKJC.kb < n3) {
                                gQOSZKJC.kb = n3;
                            }
                        }
                    }
                    if (n5 >= n2) break block56;
                    gQOSZKJC.lb += n4;
                    if (gQOSZKJC.lb <= n2) break block57;
                    gQOSZKJC.lb = n2;
                    if (n == 0) break block57;
                }
                if (n5 > n2) {
                    gQOSZKJC.lb -= n4;
                    if (gQOSZKJC.lb < n2) {
                        gQOSZKJC.lb = n2;
                    }
                }
            }
            if (gQOSZKJC.kb == n3 && gQOSZKJC.lb == n2) {
                --gQOSZKJC.L;
                if (gQOSZKJC.cb > 0) {
                    --gQOSZKJC.cb;
                    return;
                }
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("41887, " + by + ", " + gQOSZKJC + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(GQOSZKJC gQOSZKJC, int n) {
        block18: {
            int n2 = Jj;
            try {
                block20: {
                    block21: {
                        int n3;
                        block19: {
                            int n4;
                            int n5;
                            CWNCPMLX cWNCPMLX;
                            if (n >= 0) {
                                return;
                            }
                            if (gQOSZKJC.q == 0) {
                                return;
                            }
                            if (gQOSZKJC.o != -1 && gQOSZKJC.o < 32768 && (cWNCPMLX = this.V[gQOSZKJC.o]) != null) {
                                n5 = gQOSZKJC.kb - cWNCPMLX.kb;
                                n4 = gQOSZKJC.lb - cWNCPMLX.lb;
                                if (n5 != 0 || n4 != 0) {
                                    gQOSZKJC.w = (int)(Math.atan2(n5, n4) * 325.949) & 0x7FF;
                                }
                            }
                            if (gQOSZKJC.o >= 32768) {
                                DLZHLHNK dLZHLHNK;
                                int n6 = gQOSZKJC.o - 32768;
                                if (n6 == this.Sb) {
                                    n6 = this.Xb;
                                }
                                if ((dLZHLHNK = this.Yb[n6]) != null) {
                                    n4 = gQOSZKJC.kb - dLZHLHNK.kb;
                                    int n7 = gQOSZKJC.lb - dLZHLHNK.lb;
                                    if (n4 != 0 || n7 != 0) {
                                        gQOSZKJC.w = (int)(Math.atan2(n4, n7) * 325.949) & 0x7FF;
                                    }
                                }
                            }
                            if (!(gQOSZKJC.Y == 0 && gQOSZKJC.Z == 0 || gQOSZKJC.L != 0 && gQOSZKJC.p <= 0)) {
                                int n8 = gQOSZKJC.kb - (gQOSZKJC.Y - this.Me - this.Me) * 64;
                                n5 = gQOSZKJC.lb - (gQOSZKJC.Z - this.Ne - this.Ne) * 64;
                                if (n8 != 0 || n5 != 0) {
                                    gQOSZKJC.w = (int)(Math.atan2(n8, n5) * 325.949) & 0x7FF;
                                }
                                gQOSZKJC.Y = 0;
                                gQOSZKJC.Z = 0;
                            }
                            if ((n3 = gQOSZKJC.w - gQOSZKJC.mb & 0x7FF) == 0) break block18;
                            if (n3 >= gQOSZKJC.q && n3 <= 2048 - gQOSZKJC.q) break block19;
                            gQOSZKJC.mb = gQOSZKJC.w;
                            if (n2 == 0) break block20;
                        }
                        if (n3 <= 1024) break block21;
                        gQOSZKJC.mb -= gQOSZKJC.q;
                        if (n2 == 0) break block20;
                    }
                    gQOSZKJC.mb += gQOSZKJC.q;
                }
                gQOSZKJC.mb &= 0x7FF;
                if (gQOSZKJC.D == gQOSZKJC.x && gQOSZKJC.mb != gQOSZKJC.w) {
                    if (gQOSZKJC.y != -1) {
                        gQOSZKJC.D = gQOSZKJC.y;
                        return;
                    }
                    gQOSZKJC.D = gQOSZKJC.ob;
                    return;
                }
            }
            catch (RuntimeException runtimeException) {
                signlink.reporterror("73745, " + gQOSZKJC + ", " + n + ", " + runtimeException.toString());
                throw new RuntimeException();
            }
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void b(GQOSZKJC gQOSZKJC, int n) {
        int n2 = Jj;
        try {
            LKGEGIEW lKGEGIEW;
            if (n >= 0) {
                Bc = !Bc;
            }
            gQOSZKJC.bb = false;
            if (gQOSZKJC.D != -1) {
                lKGEGIEW = LKGEGIEW.d[gQOSZKJC.D];
                ++gQOSZKJC.F;
                if (gQOSZKJC.E < lKGEGIEW.e && gQOSZKJC.F > lKGEGIEW.a(gQOSZKJC.E, (byte)-39)) {
                    gQOSZKJC.F = 0;
                    ++gQOSZKJC.E;
                }
                if (gQOSZKJC.E >= lKGEGIEW.e) {
                    gQOSZKJC.F = 0;
                    gQOSZKJC.E = 0;
                }
            }
            if (gQOSZKJC.G != -1 && kh >= gQOSZKJC.J) {
                if (gQOSZKJC.H < 0) {
                    gQOSZKJC.H = 0;
                }
                lKGEGIEW = MUDLUUBC.d[gQOSZKJC.G].h;
                ++gQOSZKJC.I;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && n2 == 0) continue;
                    gQOSZKJC.I -= lKGEGIEW.a(gQOSZKJC.H, (byte)-39);
                    ++gQOSZKJC.H;
                } while (gQOSZKJC.H < lKGEGIEW.e && gQOSZKJC.I > lKGEGIEW.a(gQOSZKJC.H, (byte)-39));
                if (gQOSZKJC.H >= lKGEGIEW.e && (gQOSZKJC.H < 0 || gQOSZKJC.H >= lKGEGIEW.e)) {
                    gQOSZKJC.G = -1;
                }
            }
            if (gQOSZKJC.M != -1 && gQOSZKJC.P <= 1) {
                lKGEGIEW = LKGEGIEW.d[gQOSZKJC.M];
                if (lKGEGIEW.p == 1 && gQOSZKJC.cb > 0 && gQOSZKJC.hb <= kh && gQOSZKJC.ib < kh) {
                    gQOSZKJC.P = 1;
                    return;
                }
            }
            if (gQOSZKJC.M != -1 && gQOSZKJC.P == 0) {
                lKGEGIEW = LKGEGIEW.d[gQOSZKJC.M];
                ++gQOSZKJC.O;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && n2 == 0) continue;
                    gQOSZKJC.O -= lKGEGIEW.a(gQOSZKJC.N, (byte)-39);
                    ++gQOSZKJC.N;
                } while (gQOSZKJC.N < lKGEGIEW.e && gQOSZKJC.O > lKGEGIEW.a(gQOSZKJC.N, (byte)-39));
                if (gQOSZKJC.N >= lKGEGIEW.e) {
                    gQOSZKJC.N -= lKGEGIEW.i;
                    ++gQOSZKJC.Q;
                    if (gQOSZKJC.Q >= lKGEGIEW.o) {
                        gQOSZKJC.M = -1;
                    }
                    if (gQOSZKJC.N < 0 || gQOSZKJC.N >= lKGEGIEW.e) {
                        gQOSZKJC.M = -1;
                    }
                }
                gQOSZKJC.bb = lKGEGIEW.k;
            }
            if (gQOSZKJC.P <= 0) return;
            --gQOSZKJC.P;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("56331, " + gQOSZKJC + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void j(boolean bl) {
        try {
            int n;
            if (!bl) {
                this.Vc = this.ee.a();
            }
            if (this.aj) {
                this.aj = false;
                this.lc.a(4, 23680, this.l, 0);
                this.mc.a(357, 23680, this.l, 0);
                this.nc.a(4, 23680, this.l, 722);
                this.oc.a(205, 23680, this.l, 743);
                this.pc.a(0, 23680, this.l, 0);
                this.qc.a(4, 23680, this.l, 516);
                this.rc.a(205, 23680, this.l, 516);
                this.sc.a(357, 23680, this.l, 496);
                this.tc.a(338, 23680, this.l, 0);
                this.ch = true;
                this.ui = true;
                this.eg = true;
                this.Ei = true;
                if (this.Be != 2) {
                    this.oh.a(4, 23680, this.l, 4);
                    this.nh.a(4, 23680, this.l, 550);
                }
            }
            if (this.Be == 2) {
                this.n((byte)1);
            }
            if (this.Tb && this.ed == 1) {
                this.ch = true;
            }
            if (this.Mh != -1 && (n = this.c(this.bd, false, this.Mh)) != 0) {
                this.ch = true;
            }
            if (this.Ri == 2) {
                this.ch = true;
            }
            if (this.Nf == 2) {
                this.ch = true;
            }
            if (this.ch) {
                this.b((byte)-81);
                this.ch = false;
            }
            if (this.vj == -1) {
                this.mf.r = this.ii - this.Qf - 77;
                if (this.t > 448 && this.t < 560 && this.u > 332) {
                    this.a(463, 77, this.t - 17, this.u - 357, this.mf, 0, false, this.ii, 0);
                }
                if ((n = this.ii - 77 - this.mf.r) < 0) {
                    n = 0;
                }
                if (n > this.ii - 77) {
                    n = this.ii - 77;
                }
                if (this.Qf != n) {
                    this.Qf = n;
                    this.ui = true;
                }
            }
            if (this.vj != -1 && (n = this.c(this.bd, false, this.vj)) != 0) {
                this.ui = true;
            }
            if (this.Ri == 3) {
                this.ui = true;
            }
            if (this.Nf == 3) {
                this.ui = true;
            }
            if (this.eb != null) {
                this.ui = true;
            }
            if (this.Tb && this.ed == 2) {
                this.ui = true;
            }
            if (this.ui) {
                this.i(6);
                this.ui = false;
            }
            if (this.Be == 2) {
                this.m(false);
                this.nh.a(4, 23680, this.l, 550);
            }
            if (this.gf != -1) {
                this.eg = true;
            }
            if (this.eg) {
                if (this.gf != -1 && this.gf == this.si) {
                    this.gf = -1;
                    this.Ph.a((byte)6, 120);
                    this.Ph.a(this.si);
                }
                this.eg = false;
                this.Ag.a(0);
                this.He.a(0, 16083, 0);
                if (this.Mh == -1) {
                    if (this.Fg[this.si] != -1) {
                        if (this.si == 0) {
                            this.Sg.a(22, 16083, 10);
                        }
                        if (this.si == 1) {
                            this.Tg.a(54, 16083, 8);
                        }
                        if (this.si == 2) {
                            this.Tg.a(82, 16083, 8);
                        }
                        if (this.si == 3) {
                            this.Ug.a(110, 16083, 8);
                        }
                        if (this.si == 4) {
                            this.Wg.a(153, 16083, 8);
                        }
                        if (this.si == 5) {
                            this.Wg.a(181, 16083, 8);
                        }
                        if (this.si == 6) {
                            this.Vg.a(209, 16083, 9);
                        }
                    }
                    if (this.Fg[0] != -1 && (this.gf != 0 || kh % 20 < 10)) {
                        this.dd[0].a(29, 16083, 13);
                    }
                    if (this.Fg[1] != -1 && (this.gf != 1 || kh % 20 < 10)) {
                        this.dd[1].a(53, 16083, 11);
                    }
                    if (this.Fg[2] != -1 && (this.gf != 2 || kh % 20 < 10)) {
                        this.dd[2].a(82, 16083, 11);
                    }
                    if (this.Fg[3] != -1 && (this.gf != 3 || kh % 20 < 10)) {
                        this.dd[3].a(115, 16083, 12);
                    }
                    if (this.Fg[4] != -1 && (this.gf != 4 || kh % 20 < 10)) {
                        this.dd[4].a(153, 16083, 13);
                    }
                    if (this.Fg[5] != -1 && (this.gf != 5 || kh % 20 < 10)) {
                        this.dd[5].a(180, 16083, 11);
                    }
                    if (this.Fg[6] != -1 && (this.gf != 6 || kh % 20 < 10)) {
                        this.dd[6].a(208, 16083, 13);
                    }
                }
                this.Ag.a(160, 23680, this.l, 516);
                this.zg.a(0);
                this.Ge.a(0, 16083, 0);
                if (this.Mh == -1) {
                    if (this.Fg[this.si] != -1) {
                        if (this.si == 7) {
                            this.zb.a(42, 16083, 0);
                        }
                        if (this.si == 8) {
                            this.Ab.a(74, 16083, 0);
                        }
                        if (this.si == 9) {
                            this.Ab.a(102, 16083, 0);
                        }
                        if (this.si == 10) {
                            this.Bb.a(130, 16083, 1);
                        }
                        if (this.si == 11) {
                            this.Db.a(173, 16083, 0);
                        }
                        if (this.si == 12) {
                            this.Db.a(201, 16083, 0);
                        }
                        if (this.si == 13) {
                            this.Cb.a(229, 16083, 0);
                        }
                    }
                    if (this.Fg[8] != -1 && (this.gf != 8 || kh % 20 < 10)) {
                        this.dd[7].a(74, 16083, 2);
                    }
                    if (this.Fg[9] != -1 && (this.gf != 9 || kh % 20 < 10)) {
                        this.dd[8].a(102, 16083, 3);
                    }
                    if (this.Fg[10] != -1 && (this.gf != 10 || kh % 20 < 10)) {
                        this.dd[9].a(137, 16083, 4);
                    }
                    if (this.Fg[11] != -1 && (this.gf != 11 || kh % 20 < 10)) {
                        this.dd[10].a(174, 16083, 2);
                    }
                    if (this.Fg[12] != -1 && (this.gf != 12 || kh % 20 < 10)) {
                        this.dd[11].a(201, 16083, 2);
                    }
                    if (this.Fg[13] != -1 && (this.gf != 13 || kh % 20 < 10)) {
                        this.dd[12].a(226, 16083, 2);
                    }
                }
                this.zg.a(466, 23680, this.l, 496);
                this.oh.a(0);
            }
            if (this.Ei) {
                this.Ei = false;
                this.yg.a(0);
                this.Fe.a(0, 16083, 0);
                this.qj.a(0xFFFFFF, 55, this.Vc, "Public chat", 28, true);
                if (this.Gj == 0) {
                    this.qj.a(65280, 55, this.Vc, "On", 41, true);
                }
                if (this.Gj == 1) {
                    this.qj.a(0xFFFF00, 55, this.Vc, "Friends", 41, true);
                }
                if (this.Gj == 2) {
                    this.qj.a(0xFF0000, 55, this.Vc, "Off", 41, true);
                }
                if (this.Gj == 3) {
                    this.qj.a(65535, 55, this.Vc, "Hide", 41, true);
                }
                this.qj.a(0xFFFFFF, 184, this.Vc, "Private chat", 28, true);
                if (this.fb == 0) {
                    this.qj.a(65280, 184, this.Vc, "On", 41, true);
                }
                if (this.fb == 1) {
                    this.qj.a(0xFFFF00, 184, this.Vc, "Friends", 41, true);
                }
                if (this.fb == 2) {
                    this.qj.a(0xFF0000, 184, this.Vc, "Off", 41, true);
                }
                this.qj.a(0xFFFFFF, 324, this.Vc, "Trade/compete", 28, true);
                if (this.Ti == 0) {
                    this.qj.a(65280, 324, this.Vc, "On", 41, true);
                }
                if (this.Ti == 1) {
                    this.qj.a(0xFFFF00, 324, this.Vc, "Friends", 41, true);
                }
                if (this.Ti == 2) {
                    this.qj.a(0xFF0000, 324, this.Vc, "Off", 41, true);
                }
                this.qj.a(0xFFFFFF, 458, this.Vc, "Report abuse", 33, true);
                this.yg.a(453, 23680, this.l, 0);
                this.oh.a(0);
            }
            this.bd = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("25270, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final boolean a(DUCMKFAY dUCMKFAY, boolean bl) {
        int n = Jj;
        try {
            int n2;
            block7: {
                block9: {
                    block11: {
                        block10: {
                            block8: {
                                n2 = dUCMKFAY.h;
                                if (bl) {
                                    this.a();
                                }
                                if ((n2 < 1 || n2 > 200) && (n2 < 701 || n2 > 900)) break block7;
                                if (n2 < 801) break block8;
                                n2 -= 701;
                                if (n == 0) break block9;
                            }
                            if (n2 < 701) break block10;
                            n2 -= 601;
                            if (n == 0) break block9;
                        }
                        if (n2 < 101) break block11;
                        n2 -= 101;
                        if (n == 0) break block9;
                    }
                    --n2;
                }
                this.Wh[this.Ig] = "Remove @whi@" + this.Jf[n2];
                this.Uf[this.Ig] = 792;
                ++this.Ig;
                this.Wh[this.Ig] = "Message @whi@" + this.Jf[n2];
                this.Uf[this.Ig] = 639;
                ++this.Ig;
                return true;
            }
            if (n2 >= 401 && n2 <= 500) {
                this.Wh[this.Ig] = "Remove @whi@" + dUCMKFAY.P;
                this.Uf[this.Ig] = 322;
                ++this.Ig;
                return true;
            }
            return false;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("96874, " + dUCMKFAY + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void k(boolean bl) {
        int n = Jj;
        try {
            OJEALINP oJEALINP = (OJEALINP)this.jf.b();
            this.gh &= bl;
            boolean bl2 = true;
            do {
                block6: {
                    block7: {
                        block5: {
                            if (bl2 && !(bl2 = false) && n == 0) continue;
                            if (oJEALINP.m == this.Ac && !oJEALINP.t) break block5;
                            oJEALINP.a();
                            if (n == 0) break block6;
                        }
                        if (kh < oJEALINP.q) break block6;
                        oJEALINP.a(this.bd, true);
                        if (!oJEALINP.t) break block7;
                        oJEALINP.a();
                        if (n == 0) break block6;
                    }
                    this.cd.a(oJEALINP.m, 0, (byte)6, oJEALINP.p, -1, oJEALINP.o, 60, oJEALINP.n, oJEALINP, false);
                }
                oJEALINP = (OJEALINP)this.jf.a(false);
            } while (oJEALINP != null);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("28956, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(int n, int n2, int n3, DUCMKFAY dUCMKFAY, int n4) {
        int n5 = Jj;
        try {
            if (n != 8) {
                boolean bl = this.Vd = !this.Vd;
            }
            if (dUCMKFAY.db != 0 || dUCMKFAY.H == null) {
                return;
            }
            if (dUCMKFAY.hb && this.Ee != dUCMKFAY.R && this.af != dUCMKFAY.R && this.Re != dUCMKFAY.R) {
                return;
            }
            int n6 = AFCKELYG.r;
            int n7 = AFCKELYG.p;
            int n8 = AFCKELYG.s;
            int n9 = AFCKELYG.q;
            AFCKELYG.a(n4 + dUCMKFAY.ib, n3, false, n3 + dUCMKFAY.n, n4);
            int n10 = dUCMKFAY.H.length;
            int n11 = 0;
            boolean bl = true;
            do {
                block73: {
                    int n12;
                    int n13;
                    int n14;
                    DUCMKFAY dUCMKFAY2;
                    int n15;
                    int n16;
                    block96: {
                        ZKARKDQW zKARKDQW;
                        int n17;
                        int n18;
                        block100: {
                            int n19;
                            boolean bl2;
                            block99: {
                                block98: {
                                    block97: {
                                        block93: {
                                            CXGZMTJK cXGZMTJK;
                                            block95: {
                                                block94: {
                                                    block86: {
                                                        String string;
                                                        YXVQXWYR yXVQXWYR;
                                                        int n20;
                                                        block88: {
                                                            block87: {
                                                                block80: {
                                                                    int n21;
                                                                    block85: {
                                                                        block83: {
                                                                            block84: {
                                                                                block82: {
                                                                                    block81: {
                                                                                        block74: {
                                                                                            block72: {
                                                                                                if (bl && !(bl = false) && n5 == 0) continue;
                                                                                                n16 = dUCMKFAY.I[n11] + n3;
                                                                                                n15 = dUCMKFAY.nb[n11] + n4 - n2;
                                                                                                dUCMKFAY2 = DUCMKFAY.d[dUCMKFAY.H[n11]];
                                                                                                n16 += dUCMKFAY2.eb;
                                                                                                n15 += dUCMKFAY2.gb;
                                                                                                if (dUCMKFAY2.h > 0) {
                                                                                                    this.b(950, dUCMKFAY2);
                                                                                                }
                                                                                                if (dUCMKFAY2.db != 0) break block72;
                                                                                                if (dUCMKFAY2.r > dUCMKFAY2.cb - dUCMKFAY2.ib) {
                                                                                                    dUCMKFAY2.r = dUCMKFAY2.cb - dUCMKFAY2.ib;
                                                                                                }
                                                                                                if (dUCMKFAY2.r < 0) {
                                                                                                    dUCMKFAY2.r = 0;
                                                                                                }
                                                                                                this.a(8, dUCMKFAY2.r, n16, dUCMKFAY2, n15);
                                                                                                if (dUCMKFAY2.cb <= dUCMKFAY2.ib) break block73;
                                                                                                this.a(519, dUCMKFAY2.ib, dUCMKFAY2.r, n15, n16 + dUCMKFAY2.n, dUCMKFAY2.cb);
                                                                                                if (n5 == 0) break block73;
                                                                                            }
                                                                                            if (dUCMKFAY2.db == 1) break block73;
                                                                                            if (dUCMKFAY2.db != 2) break block74;
                                                                                            n21 = 0;
                                                                                            n14 = 0;
                                                                                            boolean bl3 = true;
                                                                                            do {
                                                                                                if (bl3 && !(bl3 = false) && n5 == 0) continue;
                                                                                                int n22 = 0;
                                                                                                boolean bl4 = true;
                                                                                                do {
                                                                                                    block76: {
                                                                                                        CXGZMTJK cXGZMTJK2;
                                                                                                        int n23;
                                                                                                        block75: {
                                                                                                            int n24;
                                                                                                            CXGZMTJK cXGZMTJK3;
                                                                                                            int n25;
                                                                                                            block78: {
                                                                                                                block79: {
                                                                                                                    block77: {
                                                                                                                        if (bl4 && !(bl4 = false) && n5 == 0) continue;
                                                                                                                        n13 = n16 + n22 * (32 + dUCMKFAY2.y);
                                                                                                                        n23 = n15 + n14 * (32 + dUCMKFAY2.L);
                                                                                                                        if (n21 < 20) {
                                                                                                                            n13 += dUCMKFAY2.i[n21];
                                                                                                                            n23 += dUCMKFAY2.O[n21];
                                                                                                                        }
                                                                                                                        if (dUCMKFAY2.U[n21] <= 0) break block75;
                                                                                                                        n25 = 0;
                                                                                                                        n20 = 0;
                                                                                                                        n12 = dUCMKFAY2.U[n21] - 1;
                                                                                                                        if ((n13 <= AFCKELYG.r - 32 || n13 >= AFCKELYG.s || n23 <= AFCKELYG.p - 32 || n23 >= AFCKELYG.q) && (this.Nf == 0 || this.Mf != n21)) break block76;
                                                                                                                        int n26 = 0;
                                                                                                                        if (this.Bj == 1 && this.Cj == n21 && this.Dj == dUCMKFAY2.R) {
                                                                                                                            n26 = 0xFFFFFF;
                                                                                                                        }
                                                                                                                        if ((cXGZMTJK3 = DJRMEMXO.a(n12, dUCMKFAY2.T[n21], n26, 9)) == null) break block76;
                                                                                                                        if (this.Nf == 0 || this.Mf != n21 || this.Lf != dUCMKFAY2.R) break block77;
                                                                                                                        n25 = this.t - this.Of;
                                                                                                                        n20 = this.u - this.Pf;
                                                                                                                        if (n25 < 5 && n25 > -5) {
                                                                                                                            n25 = 0;
                                                                                                                        }
                                                                                                                        if (n20 < 5 && n20 > -5) {
                                                                                                                            n20 = 0;
                                                                                                                        }
                                                                                                                        if (this.Td < 5) {
                                                                                                                            n25 = 0;
                                                                                                                            n20 = 0;
                                                                                                                        }
                                                                                                                        cXGZMTJK3.a(n13 + n25, n23 + n20, 128, this.Ve);
                                                                                                                        if (n23 + n20 < AFCKELYG.p && dUCMKFAY.r > 0) {
                                                                                                                            n24 = this.bd * (AFCKELYG.p - n23 - n20) / 3;
                                                                                                                            if (n24 > this.bd * 10) {
                                                                                                                                n24 = this.bd * 10;
                                                                                                                            }
                                                                                                                            if (n24 > dUCMKFAY.r) {
                                                                                                                                n24 = dUCMKFAY.r;
                                                                                                                            }
                                                                                                                            dUCMKFAY.r -= n24;
                                                                                                                            this.Pf += n24;
                                                                                                                        }
                                                                                                                        if (n23 + n20 + 32 <= AFCKELYG.q || dUCMKFAY.r >= dUCMKFAY.cb - dUCMKFAY.ib) break block78;
                                                                                                                        n24 = this.bd * (n23 + n20 + 32 - AFCKELYG.q) / 3;
                                                                                                                        if (n24 > this.bd * 10) {
                                                                                                                            n24 = this.bd * 10;
                                                                                                                        }
                                                                                                                        if (n24 > dUCMKFAY.cb - dUCMKFAY.ib - dUCMKFAY.r) {
                                                                                                                            n24 = dUCMKFAY.cb - dUCMKFAY.ib - dUCMKFAY.r;
                                                                                                                        }
                                                                                                                        dUCMKFAY.r += n24;
                                                                                                                        this.Pf -= n24;
                                                                                                                        if (n5 == 0) break block78;
                                                                                                                    }
                                                                                                                    if (this.Ri == 0 || this.Qi != n21 || this.Pi != dUCMKFAY2.R) break block79;
                                                                                                                    cXGZMTJK3.a(n13, n23, 128, this.Ve);
                                                                                                                    if (n5 == 0) break block78;
                                                                                                                }
                                                                                                                cXGZMTJK3.b(n13, 16083, n23);
                                                                                                            }
                                                                                                            if (cXGZMTJK3.N != 33 && dUCMKFAY2.T[n21] == 1) break block76;
                                                                                                            n24 = dUCMKFAY2.T[n21];
                                                                                                            this.pj.b(0, client.e(-33245, n24), n23 + 10 + n20, 822, n13 + 1 + n25);
                                                                                                            this.pj.b(0xFFFF00, client.e(-33245, n24), n23 + 9 + n20, 822, n13 + n25);
                                                                                                            if (n5 == 0) break block76;
                                                                                                        }
                                                                                                        if (dUCMKFAY2.c != null && n21 < 20 && (cXGZMTJK2 = dUCMKFAY2.c[n21]) != null) {
                                                                                                            cXGZMTJK2.b(n13, 16083, n23);
                                                                                                        }
                                                                                                    }
                                                                                                    ++n21;
                                                                                                    ++n22;
                                                                                                } while (n22 < dUCMKFAY2.n);
                                                                                                ++n14;
                                                                                            } while (n14 < dUCMKFAY2.ib);
                                                                                            if (n5 == 0) break block73;
                                                                                        }
                                                                                        if (dUCMKFAY2.db != 3) break block80;
                                                                                        n14 = 0;
                                                                                        if (this.Re == dUCMKFAY2.R || this.af == dUCMKFAY2.R || this.Ee == dUCMKFAY2.R) {
                                                                                            n14 = 1;
                                                                                        }
                                                                                        if (!this.b(dUCMKFAY2, false)) break block81;
                                                                                        n21 = dUCMKFAY2.m;
                                                                                        if (n14 == 0 || dUCMKFAY2.G == 0) break block82;
                                                                                        n21 = dUCMKFAY2.G;
                                                                                        if (n5 == 0) break block82;
                                                                                    }
                                                                                    n21 = dUCMKFAY2.z;
                                                                                    if (n14 != 0 && dUCMKFAY2.j != 0) {
                                                                                        n21 = dUCMKFAY2.j;
                                                                                    }
                                                                                }
                                                                                if (dUCMKFAY2.V != 0) break block83;
                                                                                if (!dUCMKFAY2.u) break block84;
                                                                                AFCKELYG.a(dUCMKFAY2.ib, n15, n16, n21, dUCMKFAY2.n, 0);
                                                                                if (n5 == 0) break block73;
                                                                            }
                                                                            AFCKELYG.a(n16, dUCMKFAY2.n, dUCMKFAY2.ib, n21, n15, true);
                                                                            if (n5 == 0) break block73;
                                                                        }
                                                                        if (!dUCMKFAY2.u) break block85;
                                                                        AFCKELYG.a(n21, n15, dUCMKFAY2.n, dUCMKFAY2.ib, 256 - (dUCMKFAY2.V & 0xFF), 0, n16);
                                                                        if (n5 == 0) break block73;
                                                                    }
                                                                    AFCKELYG.b(n15, dUCMKFAY2.ib, 256 - (dUCMKFAY2.V & 0xFF), n21, dUCMKFAY2.n, n16, -17319);
                                                                    if (n5 == 0) break block73;
                                                                }
                                                                if (dUCMKFAY2.db != 4) break block86;
                                                                yXVQXWYR = dUCMKFAY2.K;
                                                                string = dUCMKFAY2.P;
                                                                n13 = 0;
                                                                if (this.Re == dUCMKFAY2.R || this.af == dUCMKFAY2.R || this.Ee == dUCMKFAY2.R) {
                                                                    n13 = 1;
                                                                }
                                                                if (!this.b(dUCMKFAY2, false)) break block87;
                                                                n14 = dUCMKFAY2.m;
                                                                if (n13 != 0 && dUCMKFAY2.G != 0) {
                                                                    n14 = dUCMKFAY2.G;
                                                                }
                                                                if (dUCMKFAY2.v.length() <= 0) break block88;
                                                                string = dUCMKFAY2.v;
                                                                if (n5 == 0) break block88;
                                                            }
                                                            n14 = dUCMKFAY2.z;
                                                            if (n13 != 0 && dUCMKFAY2.j != 0) {
                                                                n14 = dUCMKFAY2.j;
                                                            }
                                                        }
                                                        if (dUCMKFAY2.k == 6 && this.Yg) {
                                                            string = "Please wait...";
                                                            n14 = dUCMKFAY2.z;
                                                        }
                                                        if (AFCKELYG.n == 479) {
                                                            if (n14 == 0xFFFF00) {
                                                                n14 = 255;
                                                            }
                                                            if (n14 == 49152) {
                                                                n14 = 0xFFFFFF;
                                                            }
                                                        }
                                                        int n27 = n15 + yXVQXWYR.K;
                                                        boolean bl5 = true;
                                                        do {
                                                            block92: {
                                                                String string2;
                                                                block91: {
                                                                    block90: {
                                                                        block89: {
                                                                            if (bl5 && !(bl5 = false) && n5 == 0) continue;
                                                                            if (string.indexOf("%") != -1) {
                                                                                while ((n20 = string.indexOf("%1")) != -1) {
                                                                                    string = String.valueOf(string.substring(0, n20)) + this.f(369, this.a(341, dUCMKFAY2, 0)) + string.substring(n20 + 2);
                                                                                    if (n5 == 0) continue;
                                                                                }
                                                                                while ((n20 = string.indexOf("%2")) != -1) {
                                                                                    string = String.valueOf(string.substring(0, n20)) + this.f(369, this.a(341, dUCMKFAY2, 1)) + string.substring(n20 + 2);
                                                                                    if (n5 == 0) continue;
                                                                                }
                                                                                while ((n20 = string.indexOf("%3")) != -1) {
                                                                                    string = String.valueOf(string.substring(0, n20)) + this.f(369, this.a(341, dUCMKFAY2, 2)) + string.substring(n20 + 2);
                                                                                    if (n5 == 0) continue;
                                                                                }
                                                                                while ((n20 = string.indexOf("%4")) != -1) {
                                                                                    string = String.valueOf(string.substring(0, n20)) + this.f(369, this.a(341, dUCMKFAY2, 3)) + string.substring(n20 + 2);
                                                                                    if (n5 == 0) continue;
                                                                                }
                                                                                while ((n20 = string.indexOf("%5")) != -1) {
                                                                                    string = String.valueOf(string.substring(0, n20)) + this.f(369, this.a(341, dUCMKFAY2, 4)) + string.substring(n20 + 2);
                                                                                    if (n5 == 0) continue;
                                                                                }
                                                                            }
                                                                            if ((n20 = string.indexOf("\\n")) == -1) break block89;
                                                                            string2 = string.substring(0, n20);
                                                                            string = string.substring(n20 + 2);
                                                                            if (n5 == 0) break block90;
                                                                        }
                                                                        string2 = string;
                                                                        string = "";
                                                                    }
                                                                    if (!dUCMKFAY2.q) break block91;
                                                                    yXVQXWYR.a(n14, n16 + dUCMKFAY2.n / 2, this.Vc, string2, n27, dUCMKFAY2.jb);
                                                                    if (n5 == 0) break block92;
                                                                }
                                                                yXVQXWYR.a(false, dUCMKFAY2.jb, n16, n14, string2, n27);
                                                            }
                                                            n27 += yXVQXWYR.K;
                                                        } while (string.length() > 0);
                                                        if (n5 == 0) break block73;
                                                    }
                                                    if (dUCMKFAY2.db != 5) break block93;
                                                    if (!this.b(dUCMKFAY2, false)) break block94;
                                                    cXGZMTJK = dUCMKFAY2.bb;
                                                    if (n5 == 0) break block95;
                                                }
                                                cXGZMTJK = dUCMKFAY2.a;
                                            }
                                            if (cXGZMTJK == null) break block73;
                                            cXGZMTJK.b(n16, 16083, n15);
                                            if (n5 == 0) break block73;
                                        }
                                        if (dUCMKFAY2.db != 6) break block96;
                                        n18 = OPPOFIOL.F;
                                        n14 = OPPOFIOL.G;
                                        OPPOFIOL.F = n16 + dUCMKFAY2.n / 2;
                                        OPPOFIOL.G = n15 + dUCMKFAY2.ib / 2;
                                        n17 = OPPOFIOL.J[dUCMKFAY2.lb] * dUCMKFAY2.kb >> 16;
                                        n13 = OPPOFIOL.K[dUCMKFAY2.lb] * dUCMKFAY2.kb >> 16;
                                        bl2 = this.b(dUCMKFAY2, false);
                                        if (!bl2) break block97;
                                        n19 = dUCMKFAY2.Z;
                                        if (n5 == 0) break block98;
                                    }
                                    n19 = dUCMKFAY2.Y;
                                }
                                if (n19 != -1) break block99;
                                zKARKDQW = dUCMKFAY2.a(0, -1, -1, bl2);
                                if (n5 == 0) break block100;
                            }
                            LKGEGIEW lKGEGIEW = LKGEGIEW.d[n19];
                            zKARKDQW = dUCMKFAY2.a(0, lKGEGIEW.g[dUCMKFAY2.N], lKGEGIEW.f[dUCMKFAY2.N], bl2);
                        }
                        if (zKARKDQW != null) {
                            zKARKDQW.a(0, dUCMKFAY2.mb, 0, dUCMKFAY2.lb, 0, n17, n13);
                        }
                        OPPOFIOL.F = n18;
                        OPPOFIOL.G = n14;
                        if (n5 == 0) break block73;
                    }
                    if (dUCMKFAY2.db != 7) break block73;
                    YXVQXWYR yXVQXWYR = dUCMKFAY2.K;
                    n14 = 0;
                    int n28 = 0;
                    boolean bl6 = true;
                    do {
                        if (bl6 && !(bl6 = false) && n5 == 0) continue;
                        n13 = 0;
                        boolean bl7 = true;
                        do {
                            block101: {
                                int n29;
                                String string;
                                block102: {
                                    if (bl7 && !(bl7 = false) && n5 == 0) continue;
                                    if (dUCMKFAY2.U[n14] <= 0) break block101;
                                    DJRMEMXO dJRMEMXO = DJRMEMXO.b(dUCMKFAY2.U[n14] - 1);
                                    string = dJRMEMXO.q;
                                    if (dJRMEMXO.w || dUCMKFAY2.T[n14] != 1) {
                                        string = String.valueOf(string) + " x" + client.a(dUCMKFAY2.T[n14], 0);
                                    }
                                    n29 = n16 + n13 * (115 + dUCMKFAY2.y);
                                    n12 = n15 + n28 * (12 + dUCMKFAY2.L);
                                    if (!dUCMKFAY2.q) break block102;
                                    yXVQXWYR.a(dUCMKFAY2.z, n29 + dUCMKFAY2.n / 2, this.Vc, string, n12, dUCMKFAY2.jb);
                                    if (n5 == 0) break block101;
                                }
                                yXVQXWYR.a(false, dUCMKFAY2.jb, n29, dUCMKFAY2.z, string, n12);
                            }
                            ++n14;
                            ++n13;
                        } while (n13 < dUCMKFAY2.n);
                        ++n28;
                    } while (n28 < dUCMKFAY2.ib);
                }
                ++n11;
            } while (n11 < n10);
            AFCKELYG.a(n9, n6, false, n8, n7);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("5217, " + n + ", " + n2 + ", " + n3 + ", " + dUCMKFAY + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(DSMJIEPN dSMJIEPN, int n) {
        int n2 = Jj;
        try {
            int n3;
            int n4;
            int n5;
            int n6 = 256;
            if (n >= 0) {
                this.Ph.a(126);
            }
            int n7 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                this.Nh[n7] = 0;
                ++n7;
            } while (n7 < this.Nh.length);
            int n8 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                n5 = (int)(Math.random() * 128.0 * (double)n6);
                this.Nh[n5] = (int)(Math.random() * 256.0);
                ++n8;
            } while (n8 < 5000);
            n5 = 0;
            boolean bl3 = true;
            do {
                if (bl3 && !(bl3 = false) && n2 == 0) continue;
                n4 = 1;
                boolean bl4 = true;
                do {
                    if (bl4 && !(bl4 = false) && n2 == 0) continue;
                    int n9 = 1;
                    boolean bl5 = true;
                    do {
                        if (bl5 && !(bl5 = false) && n2 == 0) continue;
                        n3 = n9 + (n4 << 7);
                        this.Oh[n3] = (this.Nh[n3 - 1] + this.Nh[n3 + 1] + this.Nh[n3 - 128] + this.Nh[n3 + 128]) / 4;
                        ++n9;
                    } while (n9 < 127);
                    ++n4;
                } while (n4 < n6 - 1);
                int[] nArray = this.Nh;
                this.Nh = this.Oh;
                this.Oh = nArray;
                ++n5;
            } while (n5 < 20);
            if (dSMJIEPN == null) return;
            n4 = 0;
            int n10 = 0;
            boolean bl6 = true;
            do {
                if (bl6 && !(bl6 = false) && n2 == 0) continue;
                n3 = 0;
                boolean bl7 = true;
                do {
                    if (bl7 && !(bl7 = false) && n2 == 0) continue;
                    if (dSMJIEPN.B[n4++] != 0) {
                        int n11 = n3 + 16 + dSMJIEPN.F;
                        int n12 = n10 + 16 + dSMJIEPN.G;
                        int n13 = n11 + (n12 << 7);
                        this.Nh[n13] = 0;
                    }
                    ++n3;
                } while (n3 < dSMJIEPN.D);
                ++n10;
            } while (n10 < dSMJIEPN.E);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("98023, " + dSMJIEPN + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void a(int n, int n2, MBMGIXGO mBMGIXGO, byte by, DLZHLHNK dLZHLHNK) {
        int n3 = Jj;
        try {
            int n4;
            int n5;
            int n6;
            block38: {
                block40: {
                    block39: {
                        block36: {
                            block37: {
                                if (by != 25) {
                                    this.N = null;
                                }
                                if ((n & 0x400) != 0) {
                                    dLZHLHNK.db = mBMGIXGO.h(2);
                                    dLZHLHNK.fb = mBMGIXGO.h(2);
                                    dLZHLHNK.eb = mBMGIXGO.h(2);
                                    dLZHLHNK.gb = mBMGIXGO.h(2);
                                    dLZHLHNK.hb = mBMGIXGO.d((byte)-74) + kh;
                                    dLZHLHNK.ib = mBMGIXGO.c(true) + kh;
                                    dLZHLHNK.jb = mBMGIXGO.h(2);
                                    dLZHLHNK.a(true);
                                }
                                if ((n & 0x100) != 0) {
                                    dLZHLHNK.G = mBMGIXGO.c((byte)108);
                                    n6 = mBMGIXGO.h();
                                    dLZHLHNK.K = n6 >> 16;
                                    dLZHLHNK.J = kh + (n6 & 0xFFFF);
                                    dLZHLHNK.H = 0;
                                    dLZHLHNK.I = 0;
                                    if (dLZHLHNK.J > kh) {
                                        dLZHLHNK.H = -1;
                                    }
                                    if (dLZHLHNK.G == 65535) {
                                        dLZHLHNK.G = -1;
                                    }
                                }
                                if ((n & 8) == 0) break block36;
                                n6 = mBMGIXGO.c((byte)108);
                                if (n6 == 65535) {
                                    n6 = -1;
                                }
                                n5 = mBMGIXGO.b(false);
                                if (n6 != dLZHLHNK.M || n6 == -1) break block37;
                                n4 = LKGEGIEW.d[n6].r;
                                if (n4 == 1) {
                                    dLZHLHNK.N = 0;
                                    dLZHLHNK.O = 0;
                                    dLZHLHNK.P = n5;
                                    dLZHLHNK.Q = 0;
                                }
                                if (n4 != 2) break block36;
                                dLZHLHNK.Q = 0;
                                if (n3 == 0) break block36;
                            }
                            if (n6 == -1 || dLZHLHNK.M == -1 || LKGEGIEW.d[n6].l >= LKGEGIEW.d[dLZHLHNK.M].l) {
                                dLZHLHNK.M = n6;
                                dLZHLHNK.N = 0;
                                dLZHLHNK.O = 0;
                                dLZHLHNK.P = n5;
                                dLZHLHNK.Q = 0;
                                dLZHLHNK.cb = dLZHLHNK.L;
                            }
                        }
                        if ((n & 4) == 0) break block38;
                        dLZHLHNK.s = mBMGIXGO.i();
                        if (dLZHLHNK.s.charAt(0) != '~') break block39;
                        dLZHLHNK.s = dLZHLHNK.s.substring(1);
                        this.a(dLZHLHNK.s, 2, dLZHLHNK.yb, this.Vd);
                        if (n3 == 0) break block40;
                    }
                    if (dLZHLHNK == Bg) {
                        this.a(dLZHLHNK.s, 2, dLZHLHNK.yb, this.Vd);
                    }
                }
                dLZHLHNK.z = 0;
                dLZHLHNK.R = 0;
                dLZHLHNK.V = 150;
            }
            if ((n & 0x80) != 0) {
                int n7;
                block35: {
                    n6 = mBMGIXGO.c((byte)108);
                    n5 = mBMGIXGO.c();
                    n4 = mBMGIXGO.b(false);
                    n7 = mBMGIXGO.z;
                    if (dLZHLHNK.yb != null && dLZHLHNK.Fb) {
                        long l = ZTQFNQRH.a(dLZHLHNK.yb);
                        boolean bl = false;
                        if (n5 <= 1) {
                            int n8 = 0;
                            boolean bl2 = true;
                            do {
                                if (bl2 && !(bl2 = false) && n3 == 0) continue;
                                if (this.Hc[n8] == l) {
                                    bl = true;
                                    if (n3 == 0) break;
                                }
                                ++n8;
                            } while (n8 < this.I);
                        }
                        if (!bl && this.Wi == 0) {
                            try {
                                this.U.z = 0;
                                mBMGIXGO.a(n4, 0, true, this.U.y);
                                this.U.z = 0;
                                String string = RTHTIIVA.a(n4, true, this.U);
                                dLZHLHNK.s = string = RKAYAFDQ.a(string, 0);
                                dLZHLHNK.z = n6 >> 8;
                                dLZHLHNK.R = n6 & 0xFF;
                                dLZHLHNK.V = 150;
                                if (n5 == 2 || n5 == 3) {
                                    this.a(string, 1, "@cr2@" + dLZHLHNK.yb, this.Vd);
                                    if (n3 == 0) break block35;
                                }
                                if (n5 == 1) {
                                    this.a(string, 1, "@cr1@" + dLZHLHNK.yb, this.Vd);
                                    if (n3 == 0) break block35;
                                }
                                this.a(string, 2, dLZHLHNK.yb, this.Vd);
                            }
                            catch (Exception exception) {
                                signlink.reporterror("cde2");
                            }
                        }
                    }
                }
                mBMGIXGO.z = n7 + n4;
            }
            if ((n & 1) != 0) {
                dLZHLHNK.o = mBMGIXGO.c((byte)108);
                if (dLZHLHNK.o == 65535) {
                    dLZHLHNK.o = -1;
                }
            }
            if ((n & 0x10) != 0) {
                n6 = mBMGIXGO.b(false);
                byte[] byArray = new byte[n6];
                MBMGIXGO mBMGIXGO2 = new MBMGIXGO(byArray, 891);
                mBMGIXGO.a(n6, this.Cc, 0, byArray);
                this.dc[n2] = mBMGIXGO2;
                dLZHLHNK.a(0, mBMGIXGO2);
            }
            if ((n & 2) != 0) {
                dLZHLHNK.Y = mBMGIXGO.d((byte)-74);
                dLZHLHNK.Z = mBMGIXGO.c((byte)108);
            }
            if ((n & 0x20) != 0) {
                n6 = mBMGIXGO.c();
                int n9 = mBMGIXGO.g(0);
                dLZHLHNK.a(-35698, n9, n6, kh);
                dLZHLHNK.S = kh + 300;
                dLZHLHNK.T = mBMGIXGO.b(false);
                dLZHLHNK.U = mBMGIXGO.c();
            }
            if ((n & 0x200) == 0) return;
            n6 = mBMGIXGO.c();
            int n10 = mBMGIXGO.h(2);
            dLZHLHNK.a(-35698, n10, n6, kh);
            dLZHLHNK.S = kh + 300;
            dLZHLHNK.T = mBMGIXGO.c();
            dLZHLHNK.U = mBMGIXGO.b(false);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("50326, " + n + ", " + n2 + ", " + mBMGIXGO + ", " + by + ", " + dLZHLHNK + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void G(int n) {
        int n2 = Jj;
        try {
            if (n != 3) {
                this.me = -1;
            }
            try {
                int n3;
                block32: {
                    block33: {
                        block31: {
                            block29: {
                                block30: {
                                    block28: {
                                        int n4 = client.Bg.kb + this.xj;
                                        int n5 = client.Bg.lb + this.Gg;
                                        if (this.se - n4 < -500 || this.se - n4 > 500 || this.te - n5 < -500 || this.te - n5 > 500) {
                                            this.se = n4;
                                            this.te = n5;
                                        }
                                        if (this.se != n4) {
                                            this.se += (n4 - this.se) / 16;
                                        }
                                        if (this.te != n5) {
                                            this.te += (n5 - this.te) / 16;
                                        }
                                        if (this.D[1] != 1) break block28;
                                        this.Jh += (-24 - this.Jh) / 2;
                                        if (n2 == 0) break block29;
                                    }
                                    if (this.D[2] != 1) break block30;
                                    this.Jh += (24 - this.Jh) / 2;
                                    if (n2 == 0) break block29;
                                }
                                this.Jh /= 2;
                            }
                            if (this.D[3] != 1) break block31;
                            this.Kh += (12 - this.Kh) / 2;
                            if (n2 == 0) break block32;
                        }
                        if (this.D[4] != 1) break block33;
                        this.Kh += (-12 - this.Kh) / 2;
                        if (n2 == 0) break block32;
                    }
                    this.Kh /= 2;
                }
                this.Ih = this.Ih + this.Jh / 2 & 0x7FF;
                this.Hh += this.Kh / 2;
                if (this.Hh < 128) {
                    this.Hh = 128;
                }
                if (this.Hh > 383) {
                    this.Hh = 383;
                }
                int n6 = this.se >> 7;
                int n7 = this.te >> 7;
                int n8 = this.a(this.Ac, this.te, true, this.se);
                int n9 = 0;
                if (n6 > 3 && n7 > 3 && n6 < 100 && n7 < 100) {
                    n3 = n6 - 4;
                    boolean bl = true;
                    do {
                        if (bl && !(bl = false) && n2 == 0) continue;
                        int n10 = n7 - 4;
                        boolean bl2 = true;
                        do {
                            int n11;
                            if (bl2 && !(bl2 = false) && n2 == 0) continue;
                            int n12 = this.Ac;
                            if (n12 < 3 && (this.dj[1][n3][n10] & 2) == 2) {
                                ++n12;
                            }
                            if ((n11 = n8 - this.li[n12][n3][n10]) > n9) {
                                n9 = n11;
                            }
                            ++n10;
                        } while (n10 <= n7 + 4);
                        ++n3;
                    } while (n3 <= n6 + 4);
                }
                if (++je > 1512) {
                    je = 0;
                    this.Ph.a((byte)6, 77);
                    this.Ph.a(0);
                    n3 = this.Ph.z;
                    this.Ph.a((int)(Math.random() * 256.0));
                    this.Ph.a(101);
                    this.Ph.a(233);
                    this.Ph.b(45092);
                    if ((int)(Math.random() * 2.0) == 0) {
                        this.Ph.b(35784);
                    }
                    this.Ph.a((int)(Math.random() * 256.0));
                    this.Ph.a(64);
                    this.Ph.a(38);
                    this.Ph.b((int)(Math.random() * 65536.0));
                    this.Ph.b((int)(Math.random() * 65536.0));
                    this.Ph.a(this.Ph.z - n3, (byte)0);
                }
                if ((n3 = n9 * 192) > 98048) {
                    n3 = 98048;
                }
                if (n3 < 32768) {
                    n3 = 32768;
                }
                if (n3 > this.Od) {
                    this.Od += (n3 - this.Od) / 24;
                    return;
                }
                if (n3 >= this.Od) return;
                this.Od += (n3 - this.Od) / 80;
                return;
            }
            catch (Exception exception) {
                signlink.reporterror("glfc_ex " + client.Bg.kb + "," + client.Bg.lb + "," + this.se + "," + this.te + "," + this.wf + "," + this.xf + "," + this.Me + "," + this.Ne);
                throw new RuntimeException("eek");
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("25141, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void e(int n) {
        try {
            block8: {
                block7: {
                    if (this.Xi || this.Ic || this.zh) {
                        this.E(-13873);
                        return;
                    }
                    ++of;
                    if (n != 0) {
                        this.me = -1;
                    }
                    if (this.gh) break block7;
                    this.a(false, false);
                    if (Jj == 0) break block8;
                }
                this.j(true);
            }
            this.ki = 0;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("24097, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean a(boolean bl, String string) {
        try {
            if (string == null) {
                return false;
            }
            int n = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && Jj == 0) continue;
                if (string.equalsIgnoreCase(this.Jf[n])) {
                    return true;
                }
                ++n;
            } while (n < this.hc);
            if (bl) {
                this.Ph.a(138);
            }
            return string.equalsIgnoreCase(client.Bg.yb);
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("24883, " + bl + ", " + string + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public static final String a(int n, int n2, boolean bl) {
        try {
            if (!bl) {
                throw new NullPointerException();
            }
            int n3 = n - n2;
            if (n3 < -9) {
                return "@red@";
            }
            if (n3 < -6) {
                return "@or3@";
            }
            if (n3 < -3) {
                return "@or2@";
            }
            if (n3 < 0) {
                return "@or1@";
            }
            if (n3 > 9) {
                return "@gre@";
            }
            if (n3 > 6) {
                return "@gr3@";
            }
            if (n3 > 3) {
                return "@gr2@";
            }
            if (n3 > 0) {
                return "@gr1@";
            }
            return "@yel@";
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("19760, " + n + ", " + n2 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(byte by, int n) {
        try {
            block5: {
                block4: {
                    if (by != 2) break block4;
                    by = 0;
                    if (Jj == 0) break block5;
                }
                this.a();
            }
            signlink.wavevol = n;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("83178, " + by + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void H(int n) {
        block22: {
            int n2 = Jj;
            try {
                block26: {
                    int n3;
                    int n4;
                    block25: {
                        block24: {
                            block23: {
                                if (n != 8) {
                                    this.Vc = 130;
                                }
                                this.h((byte)-13);
                                if (this.zc == 1) {
                                    this.Zg[this.yc / 100].b(this.wc - 8 - 4, 16083, this.xc - 8 - 4);
                                    if (++Rg > 67) {
                                        Rg = 0;
                                        this.Ph.a((byte)6, 78);
                                    }
                                }
                                if (this.zc == 2) {
                                    this.Zg[4 + this.yc / 100].b(this.wc - 8 - 4, 16083, this.xc - 8 - 4);
                                }
                                if (this.we != -1) {
                                    this.c(this.bd, false, this.we);
                                    this.a(8, 0, 0, DUCMKFAY.d[this.we], 0);
                                }
                                if (this.rb != -1) {
                                    this.c(this.bd, false, this.rb);
                                    this.a(8, 0, 0, DUCMKFAY.d[this.rb], 0);
                                }
                                this.y(184);
                                if (this.Tb) break block23;
                                this.D(0);
                                this.L(45706);
                                if (n2 == 0) break block24;
                            }
                            if (this.ed == 0) {
                                this.e((byte)9);
                            }
                        }
                        if (this.hf == 1) {
                            this.Wf[1].b(472, 16083, 296);
                        }
                        if (fh) {
                            n4 = 507;
                            n3 = 20;
                            int n5 = 0xFFFF00;
                            if (this.h < 15) {
                                n5 = 0xFF0000;
                            }
                            this.qj.a("Fps:" + this.h, n4, n5, (byte)-80, n3);
                            n3 += 15;
                            Runtime runtime = Runtime.getRuntime();
                            int n6 = (int)((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
                            n5 = 0xFFFF00;
                            if (n6 > 0x2000000 && qd) {
                                n5 = 0xFF0000;
                            }
                            this.qj.a("Mem:" + n6 + "k", n4, 0xFFFF00, (byte)-80, n3);
                            n3 += 15;
                        }
                        if (this.fg == 0) break block22;
                        n4 = this.fg / 50;
                        n3 = n4 / 60;
                        if ((n4 %= 60) >= 10) break block25;
                        this.qj.b(0xFFFF00, "System update in: " + n3 + ":0" + n4, 329, 822, 4);
                        if (n2 == 0) break block26;
                    }
                    this.qj.b(0xFFFF00, "System update in: " + n3 + ":" + n4, 329, 822, 4);
                }
                if (++jb > 75) {
                    jb = 0;
                    this.Ph.a((byte)6, 148);
                    return;
                }
            }
            catch (RuntimeException runtimeException) {
                signlink.reporterror("53284, " + n + ", " + runtimeException.toString());
                throw new RuntimeException();
            }
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(long l, int n) {
        int n2 = Jj;
        try {
            if (l == 0L) {
                return;
            }
            if (this.I >= 100) {
                this.a("Your ignore list is full. Max of 100 hit", 0, "", this.Vd);
                return;
            }
            String string = ZTQFNQRH.a(-45804, ZTQFNQRH.a(l, (byte)-99));
            int n3 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                if (this.Hc[n3] == l) {
                    this.a(String.valueOf(string) + " is already on your ignore list", 0, "", this.Vd);
                    return;
                }
                ++n3;
            } while (n3 < this.I);
            if (n < 4 || n > 4) {
                return;
            }
            int n4 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n2 == 0) continue;
                if (this.ld[n4] == l) {
                    this.a("Please remove " + string + " from your friend list first", 0, "", this.Vd);
                    return;
                }
                ++n4;
            } while (n4 < this.hc);
            this.Hc[this.I++] = l;
            this.ch = true;
            this.Ph.a((byte)6, 133);
            this.Ph.a(5, l);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("45688, " + l + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void i(byte by) {
        int n = Jj;
        try {
            if (by != this.Dd) {
                return;
            }
            int n2 = -1;
            boolean bl = true;
            do {
                DLZHLHNK dLZHLHNK;
                int n3;
                block9: {
                    block8: {
                        if (bl && !(bl = false) && n == 0) continue;
                        if (n2 != -1) break block8;
                        n3 = this.Xb;
                        if (n == 0) break block9;
                    }
                    n3 = this.ac[n2];
                }
                if ((dLZHLHNK = this.Yb[n3]) != null) {
                    this.a(46988, 1, dLZHLHNK);
                }
                ++n2;
            } while (n2 < this.Zb);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("2450, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void j(byte by) {
        int n = Jj;
        try {
            block13: {
                block12: {
                    if (by != 8) break block12;
                    by = 0;
                    if (n == 0) break block13;
                }
                this.Ph.a(101);
            }
            if (this.Be != 2) return;
            DYMVKFXP dYMVKFXP = (DYMVKFXP)this.Ch.b();
            boolean bl = true;
            do {
                block15: {
                    block16: {
                        block14: {
                            if (bl && !(bl = false) && n == 0) continue;
                            if (dYMVKFXP.i > 0) {
                                --dYMVKFXP.i;
                            }
                            if (dYMVKFXP.i != 0) break block14;
                            if (dYMVKFXP.n >= 0 && !CRRWDRTI.c(dYMVKFXP.n, dYMVKFXP.p, 8)) break block15;
                            this.a(dYMVKFXP.m, dYMVKFXP.j, dYMVKFXP.o, dYMVKFXP.p, dYMVKFXP.l, dYMVKFXP.k, dYMVKFXP.n, 4);
                            dYMVKFXP.a();
                            if (n == 0) break block15;
                        }
                        if (dYMVKFXP.q > 0) {
                            --dYMVKFXP.q;
                        }
                        if (dYMVKFXP.q != 0 || dYMVKFXP.l < 1 || dYMVKFXP.m < 1 || dYMVKFXP.l > 102 || dYMVKFXP.m > 102 || dYMVKFXP.f >= 0 && !CRRWDRTI.c(dYMVKFXP.f, dYMVKFXP.h, 8)) break block15;
                        this.a(dYMVKFXP.m, dYMVKFXP.j, dYMVKFXP.g, dYMVKFXP.h, dYMVKFXP.l, dYMVKFXP.k, dYMVKFXP.f, 4);
                        dYMVKFXP.q = -1;
                        if (dYMVKFXP.f != dYMVKFXP.n || dYMVKFXP.n != -1) break block16;
                        dYMVKFXP.a();
                        if (n == 0) break block15;
                    }
                    if (dYMVKFXP.f == dYMVKFXP.n && dYMVKFXP.g == dYMVKFXP.o && dYMVKFXP.h == dYMVKFXP.p) {
                        dYMVKFXP.a();
                    }
                }
                dYMVKFXP = (DYMVKFXP)this.Ch.a(false);
            } while (dYMVKFXP != null);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("99295, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void l(boolean bl) {
        int n = Jj;
        try {
            int n2;
            int n3;
            int n4;
            block33: {
                int n5;
                block32: {
                    block31: {
                        block30: {
                            block25: {
                                block29: {
                                    block28: {
                                        block27: {
                                            block26: {
                                                n4 = this.rj.a(this.rg, "Choose Option");
                                                this.gh &= bl;
                                                int n6 = 0;
                                                boolean bl2 = true;
                                                do {
                                                    if (bl2 && !(bl2 = false) && n == 0) continue;
                                                    n5 = this.rj.a(this.rg, this.Wh[n6]);
                                                    if (n5 > n4) {
                                                        n4 = n5;
                                                    }
                                                    ++n6;
                                                } while (n6 < this.Ig);
                                                n4 += 8;
                                                n5 = 15 * this.Ig + 21;
                                                if (this.A > 4 && this.B > 4 && this.A < 516 && this.B < 338) {
                                                    n3 = this.A - 4 - n4 / 2;
                                                    if (n3 + n4 > 512) {
                                                        n3 = 512 - n4;
                                                    }
                                                    if (n3 < 0) {
                                                        n3 = 0;
                                                    }
                                                    if ((n2 = this.B - 4) + n5 > 334) {
                                                        n2 = 334 - n5;
                                                    }
                                                    if (n2 < 0) {
                                                        n2 = 0;
                                                    }
                                                    this.Tb = true;
                                                    this.ed = 0;
                                                    this.fd = n3;
                                                    this.gd = n2;
                                                    this.hd = n4;
                                                    this.id = 15 * this.Ig + 22;
                                                }
                                                if (this.A <= 553 || this.B <= 205 || this.A >= 743 || this.B >= 466) break block25;
                                                n3 = this.A - 553 - n4 / 2;
                                                if (n3 >= 0) break block26;
                                                n3 = 0;
                                                if (n == 0) break block27;
                                            }
                                            if (n3 + n4 > 190) {
                                                n3 = 190 - n4;
                                            }
                                        }
                                        if ((n2 = this.B - 205) >= 0) break block28;
                                        n2 = 0;
                                        if (n == 0) break block29;
                                    }
                                    if (n2 + n5 > 261) {
                                        n2 = 261 - n5;
                                    }
                                }
                                this.Tb = true;
                                this.ed = 1;
                                this.fd = n3;
                                this.gd = n2;
                                this.hd = n4;
                                this.id = 15 * this.Ig + 22;
                            }
                            if (this.A <= 17) return;
                            if (this.B <= 357) return;
                            if (this.A >= 496) return;
                            if (this.B >= 453) return;
                            n3 = this.A - 17 - n4 / 2;
                            if (n3 >= 0) break block30;
                            n3 = 0;
                            if (n == 0) break block31;
                        }
                        if (n3 + n4 > 479) {
                            n3 = 479 - n4;
                        }
                    }
                    if ((n2 = this.B - 357) >= 0) break block32;
                    n2 = 0;
                    if (n == 0) break block33;
                }
                if (n2 + n5 > 96) {
                    n2 = 96 - n5;
                }
            }
            this.Tb = true;
            this.ed = 2;
            this.fd = n3;
            this.gd = n2;
            this.hd = n4;
            this.id = 15 * this.Ig + 22;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("40223, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private final void b(MBMGIXGO mBMGIXGO, int n, byte by) {
        try {
            int n2;
            block15: {
                block14: {
                    mBMGIXGO.f(this.tg);
                    if (by != 5) break block14;
                    by = 0;
                    if (Jj == 0) break block15;
                }
                this.me = mBMGIXGO.c();
            }
            if ((n2 = mBMGIXGO.c(1, 0)) == 0) {
                return;
            }
            int n3 = mBMGIXGO.c(2, 0);
            if (n3 == 0) {
                this.cc[this.bc++] = this.Xb;
                return;
            }
            if (n3 == 1) {
                int n4 = mBMGIXGO.c(3, 0);
                Bg.a(false, (byte)20, n4);
                int n5 = mBMGIXGO.c(1, 0);
                if (n5 == 1) {
                    this.cc[this.bc++] = this.Xb;
                }
                return;
            }
            if (n3 == 2) {
                int n6 = mBMGIXGO.c(3, 0);
                Bg.a(true, (byte)20, n6);
                int n7 = mBMGIXGO.c(3, 0);
                Bg.a(true, (byte)20, n7);
                int n8 = mBMGIXGO.c(1, 0);
                if (n8 == 1) {
                    this.cc[this.bc++] = this.Xb;
                }
                return;
            }
            if (n3 == 3) {
                this.Ac = mBMGIXGO.c(2, 0);
                int n9 = mBMGIXGO.c(1, 0);
                int n10 = mBMGIXGO.c(1, 0);
                if (n10 == 1) {
                    this.cc[this.bc++] = this.Xb;
                }
                int n11 = mBMGIXGO.c(7, 0);
                int n12 = mBMGIXGO.c(7, 0);
                Bg.a(n12, n11, n9 == 1, false);
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("453, " + mBMGIXGO + ", " + n + ", " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void I(int n) {
        try {
            this.R = false;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && Jj == 0) continue;
                this.R = false;
                try {
                    Thread.sleep(50L);
                }
                catch (Exception exception) {}
            } while (this.sd);
            this.wd = null;
            this.xd = null;
            this.bh = null;
            this.kb = null;
            this.lb = null;
            this.mb = null;
            this.nb = null;
            this.Nh = null;
            this.Oh = null;
            this.O = null;
            this.P = null;
            this.Yh = null;
            this.Zh = null;
            if (n < 3 || n > 3) {
                this.N = null;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("81448, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final boolean c(int var1_1, boolean var2_2, int var3_3) {
        var11_4 = client.Jj;
        try {
            var4_5 = false;
            if (var2_2) {
                throw new NullPointerException();
            }
            var5_7 = DUCMKFAY.d[var3_3];
            var6_8 = 0;
            if (var11_4 == 0) ** GOTO lbl36
            while (var5_7.H[var6_8] != -1) {
                block11: {
                    block13: {
                        block12: {
                            var7_9 = DUCMKFAY.d[var5_7.H[var6_8]];
                            if (var7_9.db == 1) {
                                var4_5 |= this.c(var1_1, false, var7_9.R);
                            }
                            if (var7_9.db != 6 || var7_9.Y == -1 && var7_9.Z == -1) break block11;
                            var8_10 = this.b(var7_9, false);
                            if (!var8_10) break block12;
                            var9_11 = var7_9.Z;
                            if (var11_4 == 0) break block13;
                        }
                        var9_11 = var7_9.Y;
                    }
                    if (var9_11 == -1) break block11;
                    var10_12 = LKGEGIEW.d[var9_11];
                    var7_9.b += var1_1;
                    if (var11_4 == 0) ** GOTO lbl33
                    do {
                        var7_9.b -= var10_12.a(var7_9.N, (byte)-39) + 1;
                        ++var7_9.N;
                        if (var7_9.N >= var10_12.e) {
                            var7_9.N -= var10_12.i;
                            if (var7_9.N < 0 || var7_9.N >= var10_12.e) {
                                var7_9.N = 0;
                            }
                        }
                        var4_5 = true;
lbl33:
                        // 2 sources

                    } while (var7_9.b > var10_12.a(var7_9.N, (byte)-39));
                }
                ++var6_8;
lbl36:
                // 2 sources

                if (var6_8 < var5_7.H.length) continue;
            }
            return var4_5;
        }
        catch (RuntimeException var4_6) {
            signlink.reporterror("91882, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_6.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final int J(int n) {
        int n2 = Jj;
        try {
            int n3;
            block30: {
                int n4;
                int n5;
                int n6;
                int n7;
                int n8;
                int n9;
                int n10;
                int n11;
                block35: {
                    block34: {
                        block33: {
                            block32: {
                                block31: {
                                    if (n <= 0) {
                                        vi = !vi;
                                    }
                                    n3 = 3;
                                    if (this.vb >= 310) break block30;
                                    n11 = this.sb >> 7;
                                    n10 = this.ub >> 7;
                                    n9 = client.Bg.kb >> 7;
                                    n8 = client.Bg.lb >> 7;
                                    if ((this.dj[this.Ac][n11][n10] & 4) != 0) {
                                        n3 = this.Ac;
                                    }
                                    if (n9 <= n11) break block31;
                                    n7 = n9 - n11;
                                    if (n2 == 0) break block32;
                                }
                                n7 = n11 - n9;
                            }
                            if (n8 <= n10) break block33;
                            n6 = n8 - n10;
                            if (n2 == 0) break block34;
                        }
                        n6 = n10 - n8;
                    }
                    if (n7 <= n6) break block35;
                    n5 = n6 * 65536 / n7;
                    n4 = 32768;
                    boolean bl = true;
                    do {
                        block39: {
                            block38: {
                                block37: {
                                    block36: {
                                        if (bl && !(bl = false) && n2 == 0) continue;
                                        if (n11 >= n9) break block36;
                                        ++n11;
                                        if (n2 == 0) break block37;
                                    }
                                    if (n11 > n9) {
                                        --n11;
                                    }
                                }
                                if ((this.dj[this.Ac][n11][n10] & 4) != 0) {
                                    n3 = this.Ac;
                                }
                                if ((n4 += n5) < 65536) continue;
                                n4 -= 65536;
                                if (n10 >= n8) break block38;
                                ++n10;
                                if (n2 == 0) break block39;
                            }
                            if (n10 > n8) {
                                --n10;
                            }
                        }
                        if ((this.dj[this.Ac][n11][n10] & 4) == 0) continue;
                        n3 = this.Ac;
                    } while (n11 != n9);
                    if (n2 == 0) break block30;
                }
                n5 = n7 * 65536 / n6;
                n4 = 32768;
                boolean bl = true;
                do {
                    block43: {
                        block42: {
                            block41: {
                                block40: {
                                    if (bl && !(bl = false) && n2 == 0) continue;
                                    if (n10 >= n8) break block40;
                                    ++n10;
                                    if (n2 == 0) break block41;
                                }
                                if (n10 > n8) {
                                    --n10;
                                }
                            }
                            if ((this.dj[this.Ac][n11][n10] & 4) != 0) {
                                n3 = this.Ac;
                            }
                            if ((n4 += n5) < 65536) continue;
                            n4 -= 65536;
                            if (n11 >= n9) break block42;
                            ++n11;
                            if (n2 == 0) break block43;
                        }
                        if (n11 > n9) {
                            --n11;
                        }
                    }
                    if ((this.dj[this.Ac][n11][n10] & 4) == 0) continue;
                    n3 = this.Ac;
                } while (n10 != n8);
            }
            if ((this.dj[this.Ac][client.Bg.kb >> 7][client.Bg.lb >> 7] & 4) == 0) return n3;
            return this.Ac;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("62088, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final int K(int n) {
        try {
            boolean bl = true;
            do {
                if (bl && !(bl = false) && Jj == 0) continue;
                this.Ph.a(21);
            } while (n >= 0);
            int n2 = this.a(this.Ac, this.ub, true, this.sb);
            if (n2 - this.tb < 800 && (this.dj[this.Ac][this.sb >> 7][this.ub >> 7] & 4) != 0) {
                return this.Ac;
            }
            return 3;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("3005, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void a(int var1_1, long var2_2) {
        var6_3 = client.Jj;
        try {
            if (var1_1 != 3) {
                this.a();
            }
            if (var2_2 == 0L) {
                return;
            }
            var4_4 = 0;
            if (var6_3 == 0) ** GOTO lbl24
            do {
                block6: {
                    if (this.Hc[var4_4] != var2_2) break block6;
                    --this.I;
                    this.ch = true;
                    var5_6 = var4_4;
                    if (var6_3 == 0) ** GOTO lbl18
                    do {
                        this.Hc[var5_6] = this.Hc[var5_6 + 1];
                        ++var5_6;
lbl18:
                        // 2 sources

                    } while (var5_6 < this.I);
                    this.Ph.a((byte)6, 74);
                    this.Ph.a(5, var2_2);
                    return;
                }
                ++var4_4;
lbl24:
                // 2 sources

            } while (var4_4 < this.I);
            return;
        }
        catch (RuntimeException var4_5) {
            signlink.reporterror("47229, " + var1_1 + ", " + var2_2 + ", " + var4_5.toString());
            throw new RuntimeException();
        }
    }

    public final String getParameter(String string) {
        if (signlink.mainapp != null) {
            return signlink.mainapp.getParameter(string);
        }
        return super.getParameter(string);
    }

    public final void a(byte by, boolean bl, int n) {
        try {
            block8: {
                block7: {
                    if (by != 0) break block7;
                    by = 0;
                    if (Jj == 0) break block8;
                }
                this.N = null;
            }
            signlink.midivol = n;
            if (bl) {
                signlink.midi = "voladjust";
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("30156, " + by + ", " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final int a(int n, DUCMKFAY dUCMKFAY, int n2) {
        int n3 = Jj;
        try {
            n = 91 / n;
            if (dUCMKFAY.t == null || n2 >= dUCMKFAY.t.length) {
                return -2;
            }
            try {
                int[] nArray = dUCMKFAY.t[n2];
                int n4 = 0;
                int n5 = 0;
                int n6 = 0;
                while (true) {
                    int n7;
                    int n8;
                    DUCMKFAY dUCMKFAY2;
                    int n9 = nArray[n5++];
                    int n10 = 0;
                    int n11 = 0;
                    if (n9 == 0) {
                        return n4;
                    }
                    if (n9 == 1) {
                        n10 = this.Ec[nArray[n5++]];
                    }
                    if (n9 == 2) {
                        n10 = this.We[nArray[n5++]];
                    }
                    if (n9 == 3) {
                        n10 = this.yb[nArray[n5++]];
                    }
                    if (n9 == 4) {
                        dUCMKFAY2 = DUCMKFAY.d[nArray[n5++]];
                        if ((n8 = nArray[n5++]) >= 0 && n8 < DJRMEMXO.X && (!DJRMEMXO.b((int)n8).h || pd)) {
                            n7 = 0;
                            boolean bl = true;
                            do {
                                if (bl && !(bl = false) && n3 == 0) continue;
                                if (dUCMKFAY2.U[n7] == n8 + 1) {
                                    n10 += dUCMKFAY2.T[n7];
                                }
                                ++n7;
                            } while (n7 < dUCMKFAY2.U.length);
                        }
                    }
                    if (n9 == 5) {
                        n10 = this.Bd[nArray[n5++]];
                    }
                    if (n9 == 6) {
                        n10 = xe[this.We[nArray[n5++]] - 1];
                    }
                    if (n9 == 7) {
                        n10 = this.Bd[nArray[n5++]] * 100 / 46875;
                    }
                    if (n9 == 8) {
                        n10 = client.Bg.Ab;
                    }
                    if (n9 == 9) {
                        int n12 = 0;
                        boolean bl = true;
                        do {
                            if (bl && !(bl = false) && n3 == 0) continue;
                            if (YUXCUCXD.c[n12]) {
                                n10 += this.We[n12];
                            }
                            ++n12;
                        } while (n12 < YUXCUCXD.a);
                    }
                    if (n9 == 10) {
                        dUCMKFAY2 = DUCMKFAY.d[nArray[n5++]];
                        if ((n8 = nArray[n5++] + 1) >= 0 && n8 < DJRMEMXO.X && (!DJRMEMXO.b((int)n8).h || pd)) {
                            n7 = 0;
                            boolean bl = true;
                            do {
                                if (bl && !(bl = false) && n3 == 0) continue;
                                if (dUCMKFAY2.U[n7] == n8) {
                                    n10 = 999999999;
                                    if (n3 == 0) break;
                                }
                                ++n7;
                            } while (n7 < dUCMKFAY2.U.length);
                        }
                    }
                    if (n9 == 11) {
                        n10 = this.Xg;
                    }
                    if (n9 == 12) {
                        n10 = this.Mb;
                    }
                    if (n9 == 13) {
                        int n13;
                        int n14 = n10 = ((n13 = this.Bd[nArray[n5++]]) & 1 << (n8 = nArray[n5++])) != 0 ? 1 : 0;
                    }
                    if (n9 == 14) {
                        int n15 = nArray[n5++];
                        SXYSOXTR sXYSOXTR = SXYSOXTR.c[n15];
                        n7 = sXYSOXTR.e;
                        int n16 = sXYSOXTR.f;
                        int n17 = sXYSOXTR.g;
                        int n18 = Di[n17 - n16];
                        n10 = this.Bd[n7] >> n16 & n18;
                    }
                    if (n9 == 15) {
                        n11 = 1;
                    }
                    if (n9 == 16) {
                        n11 = 2;
                    }
                    if (n9 == 17) {
                        n11 = 3;
                    }
                    if (n9 == 18) {
                        n10 = (client.Bg.kb >> 7) + this.Me;
                    }
                    if (n9 == 19) {
                        n10 = (client.Bg.lb >> 7) + this.Ne;
                    }
                    if (n9 == 20) {
                        n10 = nArray[n5++];
                    }
                    if (n11 == 0) {
                        if (n6 == 0) {
                            n4 += n10;
                        }
                        if (n6 == 1) {
                            n4 -= n10;
                        }
                        if (n6 == 2 && n10 != 0) {
                            n4 /= n10;
                        }
                        if (n6 == 3) {
                            n4 *= n10;
                        }
                        n6 = 0;
                        if (n3 == 0) continue;
                    }
                    n6 = n11;
                }
            }
            catch (Exception exception) {
                return -1;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("72370, " + n + ", " + dUCMKFAY + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void L(int n) {
        int n2 = Jj;
        try {
            String string;
            block9: {
                block10: {
                    block8: {
                        if (this.Ig < 2 && this.Bj == 0 && this.Lg == 0) {
                            return;
                        }
                        if (this.Bj != 1 || this.Ig >= 2) break block8;
                        string = "Use " + this.Fj + " with...";
                        if (n2 == 0) break block9;
                    }
                    if (this.Lg != 1 || this.Ig >= 2) break block10;
                    string = String.valueOf(this.Og) + "...";
                    if (n2 == 0) break block9;
                }
                string = this.Wh[this.Ig - 1];
            }
            if (this.Ig > 2) {
                string = String.valueOf(string) + "@whi@ / " + (this.Ig - 2) + " more options";
            }
            this.rj.a(true, 4, 0xFFFFFF, string, kh / 1000, 973, 15);
            if (n == 45706) return;
            int n3 = 1;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                ++n3;
            } while (n3 > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("86922, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void m(boolean var1_1) {
        var15_2 = client.Jj;
        try {
            block25: {
                if (var1_1) {
                    return;
                }
                this.nh.a(0);
                if (this.ze != 2) break block25;
                var2_3 = this.Uh.B;
                var3_6 = AFCKELYG.m;
                var4_8 = var2_3.length;
                var5_10 = 0;
                if (var15_2 == 0) ** GOTO lbl16
                do {
                    if (var2_3[var5_10] == 0) {
                        var3_6[var5_10] = 0;
                    }
                    ++var5_10;
lbl16:
                    // 2 sources

                } while (var5_10 < var4_8);
                this.xg.b(33, this.Ih, this.kf, 256, this.yd, -236, 25, 0, 0, 33, 25);
                this.oh.a(0);
                return;
            }
            var2_4 = this.Ih + this.gi & 2047;
            var3_7 = 48 + client.Bg.kb / 32;
            var4_9 = 464 - client.Bg.lb / 32;
            this.ij.b(151, var2_4, this.Ai, 256 + this.th, this.ef, -236, var4_9, 5, 25, 146, var3_7);
            this.xg.b(33, this.Ih, this.kf, 256, this.yd, -236, 25, 0, 0, 33, 25);
            var5_11 = 0;
            if (var15_2 == 0) ** GOTO lbl33
            do {
                var3_7 = this.zf[var5_11] * 4 + 2 - client.Bg.kb / 32;
                var4_9 = this.Af[var5_11] * 4 + 2 - client.Bg.lb / 32;
                this.a(this.Pg[var5_11], var3_7, var4_9, false);
                ++var5_11;
lbl33:
                // 2 sources

            } while (var5_11 < this.yf);
            var6_12 = 0;
            if (var15_2 == 0) ** GOTO lbl47
            do {
                var7_13 = 0;
                if (var15_2 == 0) ** GOTO lbl45
                do {
                    if ((var8_14 = this.N[this.Ac][var6_12][var7_13]) != null) {
                        var3_7 = var6_12 * 4 + 2 - client.Bg.kb / 32;
                        var4_9 = var7_13 * 4 + 2 - client.Bg.lb / 32;
                        this.a(this.Bf, var3_7, var4_9, false);
                    }
                    ++var7_13;
lbl45:
                    // 2 sources

                } while (var7_13 < 104);
                ++var6_12;
lbl47:
                // 2 sources

            } while (var6_12 < 104);
            var7_13 = 0;
            if (var15_2 == 0) ** GOTO lbl60
            do {
                if ((var8_14 = this.V[this.X[var7_13]]) != null && var8_14.b(client.vi)) {
                    var9_16 = var8_14.vb;
                    if (var9_16.H != null) {
                        var9_16 = var9_16.b(this.Lb);
                    }
                    if (var9_16 != null && var9_16.G && var9_16.D) {
                        var3_7 = var8_14.kb / 32 - client.Bg.kb / 32;
                        var4_9 = var8_14.lb / 32 - client.Bg.lb / 32;
                        this.a(this.Cf, var3_7, var4_9, false);
                    }
                }
                ++var7_13;
lbl60:
                // 2 sources

            } while (var7_13 < this.W);
            var8_15 = 0;
            if (var15_2 == 0) ** GOTO lbl91
            do {
                block26: {
                    block28: {
                        block27: {
                            if ((var9_16 = this.Yb[this.ac[var8_15]]) == null || !var9_16.b(client.vi)) break block26;
                            var3_7 = var9_16.kb / 32 - client.Bg.kb / 32;
                            var4_9 = var9_16.lb / 32 - client.Bg.lb / 32;
                            var10_17 = false;
                            var11_18 = ZTQFNQRH.a(var9_16.yb);
                            var13_19 = 0;
                            if (var15_2 == 0) ** GOTO lbl76
                            do {
                                if (var11_18 == this.ld[var13_19] && this.M[var13_19] != 0) {
                                    var10_17 = true;
                                    if (var15_2 == 0) break;
                                }
                                ++var13_19;
lbl76:
                                // 2 sources

                            } while (var13_19 < this.hc);
                            var14_20 = false;
                            if (client.Bg.wb != 0 && var9_16.wb != 0 && client.Bg.wb == var9_16.wb) {
                                var14_20 = true;
                            }
                            if (!var10_17) break block27;
                            this.a(this.Ef, var3_7, var4_9, false);
                            if (var15_2 == 0) break block26;
                        }
                        if (!var14_20) break block28;
                        this.a(this.Ff, var3_7, var4_9, false);
                        if (var15_2 == 0) break block26;
                    }
                    this.a(this.Df, var3_7, var4_9, false);
                }
                ++var8_15;
lbl91:
                // 2 sources

            } while (var8_15 < this.Zb);
            if (this.pb != 0 && client.kh % 20 < 10) {
                if (this.pb == 1 && this.ti >= 0 && this.ti < this.V.length && (var9_16 = this.V[this.ti]) != null) {
                    var3_7 = var9_16.kb / 32 - client.Bg.kb / 32;
                    var4_9 = var9_16.lb / 32 - client.Bg.lb / 32;
                    this.a(this.Fb, -760, var4_9, var3_7);
                }
                if (this.pb == 2) {
                    var3_7 = (this.Qc - this.Me) * 4 + 2 - client.Bg.kb / 32;
                    var4_9 = (this.Rc - this.Ne) * 4 + 2 - client.Bg.lb / 32;
                    this.a(this.Fb, -760, var4_9, var3_7);
                }
                if (this.pb == 10 && this.Pc >= 0 && this.Pc < this.Yb.length && (var9_16 = this.Yb[this.Pc]) != null) {
                    var3_7 = var9_16.kb / 32 - client.Bg.kb / 32;
                    var4_9 = var9_16.lb / 32 - client.Bg.lb / 32;
                    this.a(this.Fb, -760, var4_9, var3_7);
                }
            }
            if (this.gj != 0) {
                var3_7 = this.gj * 4 + 2 - client.Bg.kb / 32;
                var4_9 = this.hj * 4 + 2 - client.Bg.lb / 32;
                this.a(this.Eb, var3_7, var4_9, false);
            }
            AFCKELYG.a(3, 78, 97, 0xFFFFFF, 3, 0);
            this.oh.a(0);
            return;
        }
        catch (RuntimeException var2_5) {
            signlink.reporterror("83200, " + var1_1 + ", " + var2_5.toString());
            throw new RuntimeException();
        }
    }

    public final void a(boolean bl, GQOSZKJC gQOSZKJC, int n) {
        try {
            if (!bl) {
                this.me = this.Kf.c();
            }
            this.b(gQOSZKJC.kb, n, this.Jb, gQOSZKJC.lb);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("30100, " + bl + ", " + gQOSZKJC + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void b(int n, int n2, int n3, int n4) {
        try {
            if (n < 128 || n4 < 128 || n > 13056 || n4 > 13056) {
                this.td = -1;
                this.ud = -1;
                return;
            }
            int n5 = this.a(this.Ac, n4, true, n) - n2;
            n -= this.sb;
            n5 -= this.tb;
            int n6 = ZKARKDQW.Jb[this.vb];
            int n7 = ZKARKDQW.Kb[this.vb];
            int n8 = ZKARKDQW.Jb[this.wb];
            int n9 = ZKARKDQW.Kb[this.wb];
            int n10 = (n4 -= this.ub) * n8 + n * n9 >> 16;
            n4 = n4 * n9 - n * n8 >> 16;
            n = n10;
            if (n3 >= 0) {
                this.Ph.a(27);
            }
            n10 = n5 * n7 - n4 * n6 >> 16;
            n4 = n5 * n6 + n4 * n7 >> 16;
            n5 = n10;
            if (n4 >= 50) {
                this.td = OPPOFIOL.F + (n << 9) / n4;
                this.ud = OPPOFIOL.G + (n5 << 9) / n4;
                return;
            }
            this.td = -1;
            this.ud = -1;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("97939, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void n(boolean bl) {
        try {
            if (this.Sh == 0) {
                return;
            }
            int n = 0;
            if (bl) {
                this.me = -1;
            }
            if (this.fg != 0) {
                n = 1;
            }
            int n2 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && Jj == 0) continue;
                if (this.ad[n2] != null) {
                    int n3 = this.Yc[n2];
                    String string = this.Zc[n2];
                    int n4 = 0;
                    if (string != null && string.startsWith("@cr1@")) {
                        string = string.substring(5);
                        n4 = 1;
                    }
                    if (string != null && string.startsWith("@cr2@")) {
                        string = string.substring(5);
                        n4 = 2;
                    }
                    if ((n3 == 3 || n3 == 7) && (n3 == 7 || this.fb == 0 || this.fb == 1 && this.a(false, string))) {
                        int n5 = 329 - n * 13;
                        if (this.t > 4 && this.u - 4 > n5 - 10 && this.u - 4 <= n5 + 3) {
                            int n6 = this.qj.a(this.rg, "From:  " + string + this.ad[n2]) + 25;
                            if (n6 > 450) {
                                n6 = 450;
                            }
                            if (this.t < 4 + n6) {
                                if (this.xb >= 1) {
                                    this.Wh[this.Ig] = "Report abuse @whi@" + string;
                                    this.Uf[this.Ig] = 2606;
                                    ++this.Ig;
                                }
                                this.Wh[this.Ig] = "Add ignore @whi@" + string;
                                this.Uf[this.Ig] = 2042;
                                ++this.Ig;
                                this.Wh[this.Ig] = "Add friend @whi@" + string;
                                this.Uf[this.Ig] = 2337;
                                ++this.Ig;
                            }
                        }
                        if (++n >= 5) {
                            return;
                        }
                    }
                    if ((n3 == 5 || n3 == 6) && this.fb < 2 && ++n >= 5) {
                        return;
                    }
                }
                ++n2;
            } while (n2 < 100);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("61314, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10) {
        try {
            DYMVKFXP dYMVKFXP = null;
            DYMVKFXP dYMVKFXP2 = (DYMVKFXP)this.Ch.b();
            boolean bl = true;
            do {
                if (bl && !(bl = false) && Jj == 0) continue;
                if (dYMVKFXP2.j == n8 && dYMVKFXP2.l == n9 && dYMVKFXP2.m == n6 && dYMVKFXP2.k == n5) {
                    dYMVKFXP = dYMVKFXP2;
                    break;
                }
                dYMVKFXP2 = (DYMVKFXP)this.Ch.a(false);
            } while (dYMVKFXP2 != null);
            if (dYMVKFXP == null) {
                dYMVKFXP = new DYMVKFXP();
                dYMVKFXP.j = n8;
                dYMVKFXP.k = n5;
                dYMVKFXP.l = n9;
                dYMVKFXP.m = n6;
                this.a(false, dYMVKFXP);
                this.Ch.a(dYMVKFXP);
            }
            dYMVKFXP.f = n3;
            dYMVKFXP.h = n7;
            dYMVKFXP.g = n4;
            dYMVKFXP.q = n10;
            dYMVKFXP.i = n2;
            if (n > 0) return;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("83646, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + n9 + ", " + n10 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean b(DUCMKFAY dUCMKFAY, boolean bl) {
        try {
            if (bl) {
                this.Rb = -211;
            }
            if (dUCMKFAY.M == null) {
                return false;
            }
            int n = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && Jj == 0) continue;
                int n2 = this.a(341, dUCMKFAY, n);
                int n3 = dUCMKFAY.f[n];
                if (dUCMKFAY.M[n] == 2 ? n2 >= n3 : (dUCMKFAY.M[n] == 3 ? n2 <= n3 : (dUCMKFAY.M[n] == 4 ? n2 == n3 : n2 != n3))) {
                    return false;
                }
                ++n;
            } while (n < dUCMKFAY.M.length);
            return true;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("43472, " + dUCMKFAY + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final DataInputStream b(String string) throws IOException {
        if (!this.Gb) {
            if (signlink.mainapp != null) {
                return signlink.openurl(string);
            }
            return new DataInputStream(new URL(this.getCodeBase(), string).openStream());
        }
        if (this.S != null) {
            try {
                this.S.close();
            }
            catch (Exception exception) {}
            this.S = null;
        }
        this.S = this.j(43595);
        this.S.setSoTimeout(10000);
        InputStream inputStream = this.S.getInputStream();
        OutputStream outputStream = this.S.getOutputStream();
        outputStream.write(("JAGGRAB /" + string + "\n\n").getBytes());
        return new DataInputStream(inputStream);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void k(byte by) {
        int n = Jj;
        try {
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            int n10;
            block29: {
                block30: {
                    block25: {
                        n10 = 256;
                        if (this.Se <= 0) break block25;
                        n9 = 0;
                        boolean bl = true;
                        do {
                            block27: {
                                block28: {
                                    block26: {
                                        if (bl && !(bl = false) && n == 0) continue;
                                        if (this.Se <= 768) break block26;
                                        this.kb[n9] = this.a(true, this.lb[n9], this.mb[n9], 1024 - this.Se);
                                        if (n == 0) break block27;
                                    }
                                    if (this.Se <= 256) break block28;
                                    this.kb[n9] = this.mb[n9];
                                    if (n == 0) break block27;
                                }
                                this.kb[n9] = this.a(true, this.mb[n9], this.lb[n9], 256 - this.Se);
                            }
                            ++n9;
                        } while (n9 < 256);
                        if (n == 0) break block29;
                    }
                    if (this.Te <= 0) break block30;
                    n9 = 0;
                    boolean bl = true;
                    do {
                        block32: {
                            block33: {
                                block31: {
                                    if (bl && !(bl = false) && n == 0) continue;
                                    if (this.Te <= 768) break block31;
                                    this.kb[n9] = this.a(true, this.lb[n9], this.nb[n9], 1024 - this.Te);
                                    if (n == 0) break block32;
                                }
                                if (this.Te <= 256) break block33;
                                this.kb[n9] = this.nb[n9];
                                if (n == 0) break block32;
                            }
                            this.kb[n9] = this.a(true, this.nb[n9], this.lb[n9], 256 - this.Te);
                        }
                        ++n9;
                    } while (n9 < 256);
                    if (n == 0) break block29;
                }
                n9 = 0;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && n == 0) continue;
                    this.kb[n9] = this.lb[n9];
                    ++n9;
                } while (n9 < 256);
            }
            n9 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n == 0) continue;
                this.lg.c[n9] = this.Yh.I[n9];
                ++n9;
            } while (n9 < 33920);
            int n11 = 0;
            int n12 = 1152;
            int n13 = 1;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n == 0) continue;
                n8 = this.zd[n13] * (n10 - n13) / n10;
                n7 = 22 + n8;
                if (n7 < 0) {
                    n7 = 0;
                }
                n11 += n7;
                n6 = n7;
                boolean bl3 = true;
                do {
                    block35: {
                        block34: {
                            if (bl3 && !(bl3 = false) && n == 0) continue;
                            if ((n5 = this.O[n11++]) == 0) break block34;
                            n4 = n5;
                            n3 = 256 - n5;
                            n5 = this.kb[n5];
                            n2 = this.lg.c[n12];
                            this.lg.c[n12++] = ((n5 & 0xFF00FF) * n4 + (n2 & 0xFF00FF) * n3 & 0xFF00FF00) + ((n5 & 0xFF00) * n4 + (n2 & 0xFF00) * n3 & 0xFF0000) >> 8;
                            if (n == 0) break block35;
                        }
                        ++n12;
                    }
                    ++n6;
                } while (n6 < 128);
                n12 += n7;
                ++n13;
            } while (n13 < n10 - 1);
            this.lg.a(0, 23680, this.l, 0);
            n8 = 0;
            boolean bl4 = true;
            do {
                if (bl4 && !(bl4 = false) && n == 0) continue;
                this.mg.c[n8] = this.Zh.I[n8];
                ++n8;
            } while (n8 < 33920);
            n11 = 0;
            n12 = 1176;
            n7 = 1;
            boolean bl5 = true;
            do {
                if (bl5 && !(bl5 = false) && n == 0) continue;
                n6 = this.zd[n7] * (n10 - n7) / n10;
                n5 = 103 - n6;
                n12 += n6;
                n4 = 0;
                boolean bl6 = true;
                do {
                    block37: {
                        block36: {
                            if (bl6 && !(bl6 = false) && n == 0) continue;
                            if ((n3 = this.O[n11++]) == 0) break block36;
                            n2 = n3;
                            int n14 = 256 - n3;
                            n3 = this.kb[n3];
                            int n15 = this.mg.c[n12];
                            this.mg.c[n12++] = ((n3 & 0xFF00FF) * n2 + (n15 & 0xFF00FF) * n14 & 0xFF00FF00) + ((n3 & 0xFF00) * n2 + (n15 & 0xFF00) * n14 & 0xFF0000) >> 8;
                            if (n == 0) break block37;
                        }
                        ++n12;
                    }
                    ++n4;
                } while (n4 < n5);
                n11 += 128 - n5;
                n12 += 128 - n5 - n6;
                ++n7;
            } while (n7 < n10 - 1);
            this.mg.a(0, 23680, this.l, 637);
            if (by == 9) return;
            this.me = this.Kf.c();
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("45513, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void a(byte by, int n, MBMGIXGO mBMGIXGO) {
        int n2 = Jj;
        try {
            int n3;
            int n4 = mBMGIXGO.c(8, 0);
            if (n4 < this.Zb) {
                n3 = n4;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && n2 == 0) continue;
                    this.ab[this.Z++] = this.ac[n3];
                    ++n3;
                } while (n3 < this.Zb);
            }
            if (n4 > this.Zb) {
                signlink.reporterror(String.valueOf(this.wh) + " Too many players");
                throw new RuntimeException("eek");
            }
            this.Zb = 0;
            n3 = 0;
            boolean bl = true;
            do {
                block12: {
                    int n5;
                    int n6;
                    block15: {
                        int n7;
                        int n8;
                        DLZHLHNK dLZHLHNK;
                        block14: {
                            block13: {
                                block11: {
                                    if (bl && !(bl = false) && n2 == 0) continue;
                                    n6 = this.ac[n3];
                                    dLZHLHNK = this.Yb[n6];
                                    int n9 = mBMGIXGO.c(1, 0);
                                    if (n9 != 0) break block11;
                                    this.ac[this.Zb++] = n6;
                                    dLZHLHNK.X = kh;
                                    if (n2 == 0) break block12;
                                }
                                if ((n5 = mBMGIXGO.c(2, 0)) != 0) break block13;
                                this.ac[this.Zb++] = n6;
                                dLZHLHNK.X = kh;
                                this.cc[this.bc++] = n6;
                                if (n2 == 0) break block12;
                            }
                            if (n5 != 1) break block14;
                            this.ac[this.Zb++] = n6;
                            dLZHLHNK.X = kh;
                            n8 = mBMGIXGO.c(3, 0);
                            dLZHLHNK.a(false, (byte)20, n8);
                            n7 = mBMGIXGO.c(1, 0);
                            if (n7 != 1) break block12;
                            this.cc[this.bc++] = n6;
                            if (n2 == 0) break block12;
                        }
                        if (n5 != 2) break block15;
                        this.ac[this.Zb++] = n6;
                        dLZHLHNK.X = kh;
                        n8 = mBMGIXGO.c(3, 0);
                        dLZHLHNK.a(true, (byte)20, n8);
                        n7 = mBMGIXGO.c(3, 0);
                        dLZHLHNK.a(true, (byte)20, n7);
                        int n10 = mBMGIXGO.c(1, 0);
                        if (n10 != 1) break block12;
                        this.cc[this.bc++] = n6;
                        if (n2 == 0) break block12;
                    }
                    if (n5 == 3) {
                        this.ab[this.Z++] = n6;
                    }
                }
                ++n3;
            } while (n3 < n4);
            if (by == 2) return;
            this.Vc = -80;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("47230, " + by + ", " + n + ", " + mBMGIXGO + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(boolean bl, boolean bl2) {
        try {
            int n;
            int n2;
            int n3;
            int n4;
            int n5;
            block11: {
                block13: {
                    block12: {
                        this.w(0);
                        this.kg.a(0);
                        this.wd.a(0, 16083, 0);
                        n5 = 360;
                        n4 = 200;
                        if (bl2) {
                            return;
                        }
                        if (this.T == 0) {
                            n3 = n4 / 2 + 80;
                            this.pj.a(7711145, n5 / 2, this.Vc, this.vf.e, n3, true);
                            n3 = n4 / 2 - 20;
                            this.rj.a(0xFFFF00, n5 / 2, this.Vc, "Welcome to RuneScape", n3, true);
                            n3 += 30;
                            n2 = n5 / 2 - 80;
                            n = n4 / 2 + 20;
                            this.xd.a(n2 - 73, 16083, n - 20);
                            this.rj.a(0xFFFFFF, n2, this.Vc, "New User", n + 5, true);
                            n2 = n5 / 2 + 80;
                            this.xd.a(n2 - 73, 16083, n - 20);
                            this.rj.a(0xFFFFFF, n2, this.Vc, "Existing User", n + 5, true);
                        }
                        if (this.T != 2) break block11;
                        n3 = n4 / 2 - 40;
                        if (this.lj.length() <= 0) break block12;
                        this.rj.a(0xFFFF00, n5 / 2, this.Vc, this.lj, n3 - 15, true);
                        this.rj.a(0xFFFF00, n5 / 2, this.Vc, this.mj, n3, true);
                        n3 += 30;
                        if (Jj == 0) break block13;
                    }
                    this.rj.a(0xFFFF00, n5 / 2, this.Vc, this.mj, n3 - 7, true);
                    n3 += 30;
                }
                this.rj.a(false, true, n5 / 2 - 90, 0xFFFFFF, "Username: " + this.wh + (this.ni == 0 & kh % 40 < 20 ? "@yel@|" : ""), n3);
                this.rj.a(false, true, n5 / 2 - 88, 0xFFFFFF, "Password: " + ZTQFNQRH.a(this.xh, 0) + (this.ni == 1 & kh % 40 < 20 ? "@yel@|" : ""), n3 += 15);
                n3 += 15;
                if (!bl) {
                    n2 = n5 / 2 - 80;
                    n = n4 / 2 + 50;
                    this.xd.a(n2 - 73, 16083, n - 20);
                    this.rj.a(0xFFFFFF, n2, this.Vc, "Login", n + 5, true);
                    n2 = n5 / 2 + 80;
                    this.xd.a(n2 - 73, 16083, n - 20);
                    this.rj.a(0xFFFFFF, n2, this.Vc, "Cancel", n + 5, true);
                }
            }
            if (this.T == 3) {
                this.rj.a(0xFFFF00, n5 / 2, this.Vc, "Create a free account", n4 / 2 - 60, true);
                n3 = n4 / 2 - 35;
                this.rj.a(0xFFFFFF, n5 / 2, this.Vc, "To create a new account you need to", n3, true);
                this.rj.a(0xFFFFFF, n5 / 2, this.Vc, "go back to the main RuneScape webpage", n3 += 15, true);
                this.rj.a(0xFFFFFF, n5 / 2, this.Vc, "and choose the red 'create account'", n3 += 15, true);
                this.rj.a(0xFFFFFF, n5 / 2, this.Vc, "button at the top right of that page.", n3 += 15, true);
                n3 += 15;
                n2 = n5 / 2;
                n = n4 / 2 + 50;
                this.xd.a(n2 - 73, 16083, n - 20);
                this.rj.a(0xFFFFFF, n2, this.Vc, "Cancel", n + 5, true);
            }
            this.kg.a(171, 23680, this.l, 202);
            if (this.aj) {
                this.aj = false;
                this.ig.a(0, 23680, this.l, 128);
                this.jg.a(371, 23680, this.l, 202);
                this.ng.a(265, 23680, this.l, 0);
                this.og.a(265, 23680, this.l, 562);
                this.pg.a(171, 23680, this.l, 128);
                this.qg.a(171, 23680, this.l, 562);
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("92290, " + bl + ", " + bl2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void l(byte by) {
        try {
            this.sd = true;
            if (by != 59) {
                this.lf = -186;
            }
            try {
                long l = System.currentTimeMillis();
                int n = 0;
                int n2 = 20;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && Jj == 0) continue;
                    ++this.fi;
                    this.s(25106);
                    this.s(25106);
                    this.k((byte)9);
                    if (++n > 10) {
                        long l2 = System.currentTimeMillis();
                        int n3 = (int)(l2 - l) / 10 - n2;
                        n2 = 40 - n3;
                        if (n2 < 5) {
                            n2 = 5;
                        }
                        n = 0;
                        l = l2;
                    }
                    try {
                        Thread.sleep(n2);
                    }
                    catch (Exception exception) {}
                } while (this.R);
            }
            catch (Exception exception) {}
            this.sd = false;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("48378, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(byte by) {
        try {
            this.aj = true;
            if (by == 1) {
                by = 0;
                return;
            }
            this.pi = this.ee.a();
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("97636, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void a(int var1_1, MBMGIXGO var2_2, int var3_3) {
        var29_4 = client.Jj;
        try {
            block49: {
                block51: {
                    block50: {
                        block45: {
                            block46: {
                                block47: {
                                    block48: {
                                        block43: {
                                            block44: {
                                                block41: {
                                                    block42: {
                                                        if (var29_4 == 0) ** GOTO lbl6
                                                        do {
                                                            var3_3 = -1;
lbl6:
                                                            // 2 sources

                                                        } while (var1_1 >= 0);
                                                        if (var3_3 != 84) break block41;
                                                        var4_5 = var2_2.c();
                                                        var5_8 = this.nj + (var4_5 >> 4 & 7);
                                                        var6_10 = this.oj + (var4_5 & 7);
                                                        var7_12 = var2_2.e();
                                                        var8_14 = var2_2.e();
                                                        var9_17 = var2_2.e();
                                                        if (var5_8 < 0 || var6_10 < 0 || var5_8 >= 104 || var6_10 >= 104 || (var10_21 = this.N[this.Ac][var5_8][var6_10]) == null) break block42;
                                                        var11_25 = (HNKCWGJM)var10_21.b();
                                                        if (var29_4 == 0) ** GOTO lbl22
                                                        do {
                                                            if (var11_25.m == (var7_12 & 32767) && var11_25.n == var8_14) {
                                                                var11_25.n = var9_17;
                                                                if (var29_4 == 0) break;
                                                            }
                                                            var11_25 = (HNKCWGJM)var10_21.a(false);
lbl22:
                                                            // 2 sources

                                                        } while (var11_25 != null);
                                                        this.c(var5_8, var6_10);
                                                    }
                                                    return;
                                                }
                                                if (var3_3 == 105) {
                                                    var4_6 = var2_2.c();
                                                    var5_9 = this.nj + (var4_6 >> 4 & 7);
                                                    var6_11 = this.oj + (var4_6 & 7);
                                                    var7_13 = var2_2.e();
                                                    var8_15 = var2_2.c();
                                                    var9_18 = var8_15 >> 4 & 15;
                                                    var10_22 = var8_15 & 7;
                                                    if (client.Bg.m[0] >= var5_9 - var9_18 && client.Bg.m[0] <= var5_9 + var9_18 && client.Bg.n[0] >= var6_11 - var9_18 && client.Bg.n[0] <= var6_11 + var9_18 && this.ib && !client.qd && this.pf < 50) {
                                                        this.ei[this.pf] = var7_13;
                                                        this.Mi[this.pf] = var10_22;
                                                        this.Vi[this.pf] = JHDAGNBV.f[var7_13];
                                                        ++this.pf;
                                                    }
                                                }
                                                if (var3_3 == 215) {
                                                    var4_6 = var2_2.c(true);
                                                    var5_9 = var2_2.h(2);
                                                    var6_11 = this.nj + (var5_9 >> 4 & 7);
                                                    var7_13 = this.oj + (var5_9 & 7);
                                                    var8_15 = var2_2.c(true);
                                                    var9_18 = var2_2.e();
                                                    if (var6_11 >= 0 && var7_13 >= 0 && var6_11 < 104 && var7_13 < 104 && var8_15 != this.Sb) {
                                                        var10_23 = new HNKCWGJM();
                                                        var10_23.m = var4_6;
                                                        var10_23.n = var9_18;
                                                        if (this.N[this.Ac][var6_11][var7_13] == null) {
                                                            this.N[this.Ac][var6_11][var7_13] = new LHGXPZPG(169);
                                                        }
                                                        this.N[this.Ac][var6_11][var7_13].a(var10_23);
                                                        this.c(var6_11, var7_13);
                                                    }
                                                    return;
                                                }
                                                if (var3_3 != 156) break block43;
                                                var4_6 = var2_2.g(0);
                                                var5_9 = this.nj + (var4_6 >> 4 & 7);
                                                var6_11 = this.oj + (var4_6 & 7);
                                                var7_13 = var2_2.e();
                                                if (var5_9 < 0 || var6_11 < 0 || var5_9 >= 104 || var6_11 >= 104 || (var8_16 = this.N[this.Ac][var5_9][var6_11]) == null) break block44;
                                                var9_19 = (HNKCWGJM)var8_16.b();
                                                if (var29_4 == 0) ** GOTO lbl69
                                                do {
                                                    if (var9_19.m == (var7_13 & 32767)) {
                                                        var9_19.a();
                                                        if (var29_4 == 0) break;
                                                    }
                                                    var9_19 = (HNKCWGJM)var8_16.a(false);
lbl69:
                                                    // 2 sources

                                                } while (var9_19 != null);
                                                if (var8_16.b() == null) {
                                                    this.N[this.Ac][var5_9][var6_11] = null;
                                                }
                                                this.c(var5_9, var6_11);
                                            }
                                            return;
                                        }
                                        if (var3_3 != 160) break block45;
                                        var4_6 = var2_2.h(2);
                                        var5_9 = this.nj + (var4_6 >> 4 & 7);
                                        var6_11 = this.oj + (var4_6 & 7);
                                        var7_13 = var2_2.h(2);
                                        var8_15 = var7_13 >> 2;
                                        var9_18 = var7_13 & 3;
                                        var10_22 = this.Ah[var8_15];
                                        var11_26 = var2_2.c(true);
                                        if (var5_9 < 0 || var6_11 < 0 || var5_9 >= 103 || var6_11 >= 103) break block46;
                                        var12_28 = this.li[this.Ac][var5_9][var6_11];
                                        var13_30 = this.li[this.Ac][var5_9 + 1][var6_11];
                                        var14_32 = this.li[this.Ac][var5_9 + 1][var6_11 + 1];
                                        var15_34 = this.li[this.Ac][var5_9][var6_11 + 1];
                                        if (var10_22 != 0 || (var16_36 = this.cd.a(this.Ac, var5_9, var6_11, false)) == null) break block47;
                                        var17_38 = var16_36.h >> 14 & 32767;
                                        if (var8_15 != 2) break block48;
                                        var16_36.f = new WBWOBAFW(var17_38, 4 + var9_18, 2, var13_30, 7, var14_32, var12_28, var15_34, var11_26, false);
                                        var16_36.g = new WBWOBAFW(var17_38, var9_18 + 1 & 3, 2, var13_30, 7, var14_32, var12_28, var15_34, var11_26, false);
                                        if (var29_4 == 0) break block47;
                                    }
                                    var16_36.f = new WBWOBAFW(var17_38, var9_18, var8_15, var13_30, 7, var14_32, var12_28, var15_34, var11_26, false);
                                }
                                if (var10_22 == 1 && (var16_36 = this.cd.d(var5_9, 866, var6_11, this.Ac)) != null) {
                                    var16_36.f = new WBWOBAFW(var16_36.g >> 14 & 32767, 0, 4, var13_30, 7, var14_32, var12_28, var15_34, var11_26, false);
                                }
                                if (var10_22 == 2) {
                                    var16_36 = this.cd.a(var5_9, var6_11, (byte)4, this.Ac);
                                    if (var8_15 == 11) {
                                        var8_15 = 10;
                                    }
                                    if (var16_36 != null) {
                                        var16_36.e = new WBWOBAFW(var16_36.m >> 14 & 32767, var9_18, var8_15, var13_30, 7, var14_32, var12_28, var15_34, var11_26, false);
                                    }
                                }
                                if (var10_22 == 3 && (var16_36 = this.cd.e(var6_11, var5_9, this.Ac, 0)) != null) {
                                    var16_36.d = new WBWOBAFW(var16_36.e >> 14 & 32767, var9_18, 22, var13_30, 7, var14_32, var12_28, var15_34, var11_26, false);
                                }
                            }
                            return;
                        }
                        if (var3_3 != 147) break block49;
                        var4_6 = var2_2.h(2);
                        var5_9 = this.nj + (var4_6 >> 4 & 7);
                        var6_11 = this.oj + (var4_6 & 7);
                        var7_13 = var2_2.e();
                        var8_15 = var2_2.i(0);
                        var9_18 = var2_2.c((byte)108);
                        var10_22 = var2_2.b((byte)-57);
                        var11_27 = var2_2.e();
                        var12_29 = var2_2.h(2);
                        var13_31 = var12_29 >> 2;
                        var14_33 = var12_29 & 3;
                        var15_35 = this.Ah[var13_31];
                        var16_37 = var2_2.d();
                        var17_39 = var2_2.e();
                        var18_41 = var2_2.b((byte)-57);
                        if (var7_13 != this.Sb) break block50;
                        var19_42 = client.Bg;
                        if (var29_4 == 0) break block51;
                    }
                    var19_42 = this.Yb[var7_13];
                }
                if (var19_42 != null && (var25_48 = (var20_43 = YZDBYLRM.a(var17_39)).a(var13_31, var14_33, var21_44 = this.li[this.Ac][var5_9][var6_11], var22_45 = this.li[this.Ac][var5_9 + 1][var6_11], var23_46 = this.li[this.Ac][var5_9 + 1][var6_11 + 1], var24_47 = this.li[this.Ac][var5_9][var6_11 + 1], -1)) != null) {
                    this.a(404, var11_27 + 1, -1, 0, var15_35, var6_11, 0, this.Ac, var5_9, var9_18 + 1);
                    var19_42.Cb = var9_18 + client.kh;
                    var19_42.Db = var11_27 + client.kh;
                    var19_42.Jb = var25_48;
                    var26_49 = var20_43.i;
                    var27_50 = var20_43.z;
                    if (var14_33 == 1 || var14_33 == 3) {
                        var26_49 = var20_43.z;
                        var27_50 = var20_43.i;
                    }
                    var19_42.Gb = var5_9 * 128 + var26_49 * 64;
                    var19_42.Ib = var6_11 * 128 + var27_50 * 64;
                    var19_42.Hb = this.a(this.Ac, var19_42.Ib, true, var19_42.Gb);
                    if (var16_37 > var8_15) {
                        var28_51 = var16_37;
                        var16_37 = var8_15;
                        var8_15 = var28_51;
                    }
                    if (var18_41 > var10_22) {
                        var28_51 = var18_41;
                        var18_41 = var10_22;
                        var10_22 = var28_51;
                    }
                    var19_42.Ob = var5_9 + var16_37;
                    var19_42.Qb = var5_9 + var8_15;
                    var19_42.Pb = var6_11 + var18_41;
                    var19_42.Rb = var6_11 + var10_22;
                }
            }
            if (var3_3 == 151) {
                var4_6 = var2_2.g(0);
                var5_9 = this.nj + (var4_6 >> 4 & 7);
                var6_11 = this.oj + (var4_6 & 7);
                var7_13 = var2_2.c((byte)108);
                var8_15 = var2_2.h(2);
                var9_18 = var8_15 >> 2;
                var10_22 = var8_15 & 3;
                var11_27 = this.Ah[var9_18];
                if (var5_9 >= 0 && var6_11 >= 0 && var5_9 < 104 && var6_11 < 104) {
                    this.a(404, -1, var7_13, var10_22, var11_27, var6_11, var9_18, this.Ac, var5_9, 0);
                }
                return;
            }
            if (var3_3 == 4) {
                var4_6 = var2_2.c();
                var5_9 = this.nj + (var4_6 >> 4 & 7);
                var6_11 = this.oj + (var4_6 & 7);
                var7_13 = var2_2.e();
                var8_15 = var2_2.c();
                var9_18 = var2_2.e();
                if (var5_9 >= 0 && var6_11 >= 0 && var5_9 < 104 && var6_11 < 104) {
                    var5_9 = var5_9 * 128 + 64;
                    var6_11 = var6_11 * 128 + 64;
                    var10_24 = new OJEALINP(this.Ac, client.kh, 6, var9_18, var7_13, this.a(this.Ac, var6_11, true, var5_9) - var8_15, var6_11, var5_9);
                    this.jf.a(var10_24);
                }
                return;
            }
            if (var3_3 == 44) {
                var4_6 = var2_2.d((byte)-74);
                var5_9 = var2_2.e();
                var6_11 = var2_2.c();
                var7_13 = this.nj + (var6_11 >> 4 & 7);
                var8_15 = this.oj + (var6_11 & 7);
                if (var7_13 >= 0 && var8_15 >= 0 && var7_13 < 104 && var8_15 < 104) {
                    var9_20 = new HNKCWGJM();
                    var9_20.m = var4_6;
                    var9_20.n = var5_9;
                    if (this.N[this.Ac][var7_13][var8_15] == null) {
                        this.N[this.Ac][var7_13][var8_15] = new LHGXPZPG(169);
                    }
                    this.N[this.Ac][var7_13][var8_15].a(var9_20);
                    this.c(var7_13, var8_15);
                }
                return;
            }
            if (var3_3 == 101) {
                var4_6 = var2_2.b(false);
                var5_9 = var4_6 >> 2;
                var6_11 = var4_6 & 3;
                var7_13 = this.Ah[var5_9];
                var8_15 = var2_2.c();
                var9_18 = this.nj + (var8_15 >> 4 & 7);
                var10_22 = this.oj + (var8_15 & 7);
                if (var9_18 >= 0 && var10_22 >= 0 && var9_18 < 104 && var10_22 < 104) {
                    this.a(404, -1, -1, var6_11, var7_13, var10_22, var5_9, this.Ac, var9_18, 0);
                }
                return;
            }
            if (var3_3 == 117) {
                var4_6 = var2_2.c();
                var5_9 = this.nj + (var4_6 >> 4 & 7);
                var6_11 = this.oj + (var4_6 & 7);
                var7_13 = var5_9 + var2_2.d();
                var8_15 = var6_11 + var2_2.d();
                var9_18 = var2_2.f();
                var10_22 = var2_2.e();
                var11_27 = var2_2.c() * 4;
                var12_29 = var2_2.c() * 4;
                var13_31 = var2_2.e();
                var14_33 = var2_2.e();
                var15_35 = var2_2.c();
                var16_37 = var2_2.c();
                if (var5_9 >= 0 && var6_11 >= 0 && var5_9 < 104 && var6_11 < 104 && var7_13 >= 0 && var8_15 >= 0 && var7_13 < 104 && var8_15 < 104 && var10_22 != 65535) {
                    var5_9 = var5_9 * 128 + 64;
                    var6_11 = var6_11 * 128 + 64;
                    var7_13 = var7_13 * 128 + 64;
                    var8_15 = var8_15 * 128 + 64;
                    var17_40 = new SWTXAYDT(var15_35, var12_29, 46883, var13_31 + client.kh, var14_33 + client.kh, var16_37, this.Ac, this.a(this.Ac, var6_11, true, var5_9) - var11_27, var6_11, var5_9, var9_18, var10_22);
                    var17_40.a(var13_31 + client.kh, var8_15, this.a(this.Ac, var8_15, true, var7_13) - var12_29, var7_13, (byte)-83);
                    this.re.a(var17_40);
                }
                return;
            }
        }
        catch (RuntimeException var4_7) {
            signlink.reporterror("29026, " + var1_1 + ", " + var2_2 + ", " + var3_3 + ", " + var4_7.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void m(byte by) {
        try {
            NYFUGYQS.h = true;
            if (by != J) {
                int n = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && Jj == 0) continue;
                    ++n;
                } while (n > 0);
            }
            OPPOFIOL.A = true;
            qd = true;
            CRRWDRTI.C = true;
            YZDBYLRM.q = true;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("23911, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void b(MBMGIXGO mBMGIXGO, int n, int n2) {
        int n3 = Jj;
        try {
            int n4;
            if (n >= 0) {
                this.tg = -7;
            }
            mBMGIXGO.f(this.tg);
            int n5 = mBMGIXGO.c(8, 0);
            if (n5 < this.W) {
                n4 = n5;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && n3 == 0) continue;
                    this.ab[this.Z++] = this.X[n4];
                    ++n4;
                } while (n4 < this.W);
            }
            if (n5 > this.W) {
                signlink.reporterror(String.valueOf(this.wh) + " Too many npcs");
                throw new RuntimeException("eek");
            }
            this.W = 0;
            n4 = 0;
            boolean bl = true;
            do {
                block13: {
                    int n6;
                    int n7;
                    block16: {
                        int n8;
                        int n9;
                        CWNCPMLX cWNCPMLX;
                        block15: {
                            block14: {
                                block12: {
                                    if (bl && !(bl = false) && n3 == 0) continue;
                                    n7 = this.X[n4];
                                    cWNCPMLX = this.V[n7];
                                    int n10 = mBMGIXGO.c(1, 0);
                                    if (n10 != 0) break block12;
                                    this.X[this.W++] = n7;
                                    cWNCPMLX.X = kh;
                                    if (n3 == 0) break block13;
                                }
                                if ((n6 = mBMGIXGO.c(2, 0)) != 0) break block14;
                                this.X[this.W++] = n7;
                                cWNCPMLX.X = kh;
                                this.cc[this.bc++] = n7;
                                if (n3 == 0) break block13;
                            }
                            if (n6 != 1) break block15;
                            this.X[this.W++] = n7;
                            cWNCPMLX.X = kh;
                            n9 = mBMGIXGO.c(3, 0);
                            cWNCPMLX.a(false, (byte)20, n9);
                            n8 = mBMGIXGO.c(1, 0);
                            if (n8 != 1) break block13;
                            this.cc[this.bc++] = n7;
                            if (n3 == 0) break block13;
                        }
                        if (n6 != 2) break block16;
                        this.X[this.W++] = n7;
                        cWNCPMLX.X = kh;
                        n9 = mBMGIXGO.c(3, 0);
                        cWNCPMLX.a(true, (byte)20, n9);
                        n8 = mBMGIXGO.c(3, 0);
                        cWNCPMLX.a(true, (byte)20, n8);
                        int n11 = mBMGIXGO.c(1, 0);
                        if (n11 != 1) break block13;
                        this.cc[this.bc++] = n7;
                        if (n3 == 0) break block13;
                    }
                    if (n6 == 3) {
                        this.ab[this.Z++] = n7;
                    }
                }
                ++n4;
            } while (n4 < n5);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("60808, " + mBMGIXGO + ", " + n + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void o(boolean bl) {
        int n = Jj;
        try {
            int n2;
            if (!bl) {
                this.N = null;
            }
            if (this.T == 0) {
                int n3 = this.j / 2 - 80;
                int n4 = this.k / 2 + 20;
                if (this.z == 1 && this.A >= n3 - 75 && this.A <= n3 + 75 && this.B >= (n4 += 20) - 20 && this.B <= n4 + 20) {
                    this.T = 3;
                    this.ni = 0;
                }
                n3 = this.j / 2 + 80;
                if (this.z != 1) return;
                if (this.A < n3 - 75) return;
                if (this.A > n3 + 75) return;
                if (this.B < n4 - 20) return;
                if (this.B > n4 + 20) return;
                this.lj = "";
                this.mj = "Enter your username & password.";
                this.T = 2;
                this.ni = 0;
                return;
            }
            if (this.T != 2) {
                if (this.T != 3) return;
                int n5 = this.j / 2;
                int n6 = this.k / 2 + 50;
                if (this.z != 1) return;
                if (this.A < n5 - 75) return;
                if (this.A > n5 + 75) return;
                if (this.B < (n6 += 20) - 20) return;
                if (this.B > n6 + 20) return;
                this.T = 0;
                return;
            }
            int n7 = this.k / 2 - 40;
            n7 += 30;
            if (this.z == 1 && this.B >= (n7 += 25) - 15 && this.B < n7) {
                this.ni = 0;
            }
            if (this.z == 1 && this.B >= (n7 += 15) - 15 && this.B < n7) {
                this.ni = 1;
            }
            n7 += 15;
            int n8 = this.j / 2 - 80;
            int n9 = this.k / 2 + 50;
            if (this.z == 1 && this.A >= n8 - 75 && this.A <= n8 + 75 && this.B >= (n9 += 20) - 20 && this.B <= n9 + 20) {
                this.Qe = 0;
                this.a(this.wh, this.xh, false);
                if (this.gh) {
                    return;
                }
            }
            n8 = this.j / 2 + 80;
            if (this.z == 1 && this.A >= n8 - 75 && this.A <= n8 + 75 && this.B >= n9 - 20 && this.B <= n9 + 20) {
                this.T = 0;
                this.wh = "";
                this.xh = "";
            }
            while ((n2 = this.b(-796)) != -1) {
                boolean bl2 = false;
                int n10 = 0;
                boolean bl3 = true;
                do {
                    if (bl3 && !(bl3 = false) && n == 0) continue;
                    if (n2 == lh.charAt(n10)) {
                        bl2 = true;
                        if (n == 0) break;
                    }
                    ++n10;
                } while (n10 < lh.length());
                if (this.ni == 0) {
                    if (n2 == 8 && this.wh.length() > 0) {
                        this.wh = this.wh.substring(0, this.wh.length() - 1);
                    }
                    if (n2 == 9 || n2 == 10 || n2 == 13) {
                        this.ni = 1;
                    }
                    if (bl2) {
                        this.wh = String.valueOf(this.wh) + (char)n2;
                    }
                    if (this.wh.length() <= 12) continue;
                    this.wh = this.wh.substring(0, 12);
                    if (n == 0) continue;
                }
                if (this.ni != 1) continue;
                if (n2 == 8 && this.xh.length() > 0) {
                    this.xh = this.xh.substring(0, this.xh.length() - 1);
                }
                if (n2 == 9 || n2 == 10 || n2 == 13) {
                    this.ni = 0;
                }
                if (bl2) {
                    this.xh = String.valueOf(this.xh) + (char)n2;
                }
                if (this.xh.length() <= 20) continue;
                this.xh = this.xh.substring(0, 20);
                if (n != 0) return;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("8370, " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(CXGZMTJK cXGZMTJK, int n, int n2, boolean bl) {
        try {
            int n3 = this.Ih + this.gi & 0x7FF;
            int n4 = n * n + n2 * n2;
            if (bl) {
                return;
            }
            if (n4 > 6400) {
                return;
            }
            int n5 = ZKARKDQW.Jb[n3];
            int n6 = ZKARKDQW.Kb[n3];
            n5 = n5 * 256 / (this.th + 256);
            n6 = n6 * 256 / (this.th + 256);
            int n7 = n2 * n5 + n * n6 >> 16;
            int n8 = n2 * n6 - n * n5 >> 16;
            if (n4 > 2500) {
                cXGZMTJK.a(this.Uh, false, 83 - n8 - cXGZMTJK.O / 2 - 4, 94 + n7 - cXGZMTJK.N / 2 + 4);
                return;
            }
            cXGZMTJK.b(94 + n7 - cXGZMTJK.N / 2 + 4, 16083, 83 - n8 - cXGZMTJK.O / 2 - 4);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("45113, " + cXGZMTJK + ", " + n + ", " + n2 + ", " + bl + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    private final void a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        try {
            if (n8 < 4 || n8 > 4) {
                this.me = this.Kf.c();
            }
            if (n5 >= 1 && n >= 1 && n5 <= 102 && n <= 102) {
                int n9;
                if (qd && n2 != this.Ac) {
                    return;
                }
                int n10 = 0;
                int n11 = -1;
                int n12 = 0;
                int n13 = 0;
                if (n6 == 0) {
                    n10 = this.cd.c(n2, n5, n);
                }
                if (n6 == 1) {
                    n10 = this.cd.f(n2, n5, 0, n);
                }
                if (n6 == 2) {
                    n10 = this.cd.d(n2, n5, n);
                }
                if (n6 == 3) {
                    n10 = this.cd.e(n2, n5, n);
                }
                if (n10 != 0) {
                    YZDBYLRM yZDBYLRM;
                    n9 = this.cd.g(n2, n5, n, n10);
                    n11 = n10 >> 14 & Short.MAX_VALUE;
                    n12 = n9 & 0x1F;
                    n13 = n9 >> 6;
                    if (n6 == 0) {
                        this.cd.a(n5, n2, n, (byte)-119);
                        yZDBYLRM = YZDBYLRM.a(n11);
                        if (yZDBYLRM.F) {
                            this.Bi[n2].a(n13, n12, yZDBYLRM.v, true, n5, n);
                        }
                    }
                    if (n6 == 1) {
                        this.cd.b(0, n, n2, n5);
                    }
                    if (n6 == 2) {
                        this.cd.c(n2, -978, n5, n);
                        yZDBYLRM = YZDBYLRM.a(n11);
                        if (n5 + yZDBYLRM.i > 103 || n + yZDBYLRM.i > 103 || n5 + yZDBYLRM.z > 103 || n + yZDBYLRM.z > 103) {
                            return;
                        }
                        if (yZDBYLRM.F) {
                            this.Bi[n2].a(n13, yZDBYLRM.i, n5, n, (byte)6, yZDBYLRM.z, yZDBYLRM.v);
                        }
                    }
                    if (n6 == 3) {
                        this.cd.a((byte)9, n2, n, n5);
                        yZDBYLRM = YZDBYLRM.a(n11);
                        if (yZDBYLRM.F && yZDBYLRM.Q) {
                            this.Bi[n2].c(360, n, n5);
                        }
                    }
                }
                if (n7 >= 0) {
                    n9 = n2;
                    if (n9 < 3 && (this.dj[1][n5][n] & 2) == 2) {
                        ++n9;
                    }
                    CRRWDRTI.a(this.cd, n3, n, n4, n9, this.Bi[n2], this.li, n5, n7, n2, (byte)93);
                    return;
                }
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("56911, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void b(int n, MBMGIXGO mBMGIXGO, int n2) {
        int n3 = Jj;
        try {
            int n4;
            this.Z = 0;
            if (n2 != 9759) {
                this.me = mBMGIXGO.c();
            }
            this.bc = 0;
            this.b(mBMGIXGO, n, (byte)5);
            this.a((byte)2, n, mBMGIXGO);
            this.a(mBMGIXGO, n, (byte)8);
            this.a(n, (byte)2, mBMGIXGO);
            int n5 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n3 == 0) continue;
                n4 = this.ab[n5];
                if (this.Yb[n4].X != kh) {
                    this.Yb[n4] = null;
                }
                ++n5;
            } while (n5 < this.Z);
            if (mBMGIXGO.z != n) {
                signlink.reporterror("Error packet size mismatch in getplayer pos:" + mBMGIXGO.z + " psize:" + n);
                throw new RuntimeException("eek");
            }
            n4 = 0;
            boolean bl2 = true;
            do {
                if (bl2 && !(bl2 = false) && n3 == 0) continue;
                if (this.Yb[this.ac[n4]] == null) {
                    signlink.reporterror(String.valueOf(this.wh) + " null entry in pl list - pos:" + n4 + " size:" + this.Zb);
                    throw new RuntimeException("eek");
                }
                ++n4;
            } while (n4 < this.Zb);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("70865, " + n + ", " + mBMGIXGO + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void a(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        try {
            int n8;
            int n9;
            int n10;
            int n11 = 2048 - n3 & 0x7FF;
            int n12 = 2048 - n6 & 0x7FF;
            int n13 = 0;
            int n14 = 0;
            int n15 = n2;
            if (n != 0) {
                this.a();
            }
            if (n11 != 0) {
                n10 = ZKARKDQW.Jb[n11];
                n9 = ZKARKDQW.Kb[n11];
                n8 = n14 * n9 - n15 * n10 >> 16;
                n15 = n14 * n10 + n15 * n9 >> 16;
                n14 = n8;
            }
            if (n12 != 0) {
                n10 = ZKARKDQW.Jb[n12];
                n9 = ZKARKDQW.Kb[n12];
                n8 = n15 * n10 + n13 * n9 >> 16;
                n15 = n15 * n9 - n13 * n10 >> 16;
                n13 = n8;
            }
            this.sb = n4 - n13;
            this.tb = n5 - n14;
            this.ub = n7 - n15;
            this.vb = n3;
            this.wb = n6;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("69735, " + n + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Unable to fully structure code
     */
    public final boolean p(boolean var1_1) {
        var20_2 = client.Jj;
        try {
            if (!var1_1) {
                this.N = null;
            }
            if (this.rh == null) {
                return false;
            }
            try {
                block261: {
                    block260: {
                        block256: {
                            block257: {
                                block259: {
                                    block258: {
                                        block254: {
                                            block217: {
                                                block255: {
                                                    block253: {
                                                        block252: {
                                                            block251: {
                                                                block246: {
                                                                    block248: {
                                                                        block250: {
                                                                            block249: {
                                                                                block247: {
                                                                                    block245: {
                                                                                        block232: {
                                                                                            block239: {
                                                                                                block236: {
                                                                                                    block233: {
                                                                                                        block231: {
                                                                                                            block230: {
                                                                                                                block229: {
                                                                                                                    block228: {
                                                                                                                        block225: {
                                                                                                                            block227: {
                                                                                                                                block226: {
                                                                                                                                    block224: {
                                                                                                                                        block222: {
                                                                                                                                            block223: {
                                                                                                                                                block220: {
                                                                                                                                                    block221: {
                                                                                                                                                        block218: {
                                                                                                                                                            block219: {
                                                                                                                                                                var2_3 = this.rh.c();
                                                                                                                                                                if (var2_3 == 0) {
                                                                                                                                                                    return false;
                                                                                                                                                                }
                                                                                                                                                                if (this.me == -1) {
                                                                                                                                                                    this.rh.a(this.Kf.y, 0, 1);
                                                                                                                                                                    this.me = this.Kf.y[0] & 255;
                                                                                                                                                                    if (this.ee != null) {
                                                                                                                                                                        this.me = this.me - this.ee.a() & 255;
                                                                                                                                                                    }
                                                                                                                                                                    this.le = QDBYELAJ.b[this.me];
                                                                                                                                                                    --var2_3;
                                                                                                                                                                }
                                                                                                                                                                if (this.le != -1) break block218;
                                                                                                                                                                if (var2_3 <= 0) break block219;
                                                                                                                                                                this.rh.a(this.Kf.y, 0, 1);
                                                                                                                                                                this.le = this.Kf.y[0] & 255;
                                                                                                                                                                --var2_3;
                                                                                                                                                                if (var20_2 == 0) break block218;
                                                                                                                                                            }
                                                                                                                                                            return false;
                                                                                                                                                        }
                                                                                                                                                        if (this.le != -2) break block220;
                                                                                                                                                        if (var2_3 <= 1) break block221;
                                                                                                                                                        this.rh.a(this.Kf.y, 0, 2);
                                                                                                                                                        this.Kf.z = 0;
                                                                                                                                                        this.le = this.Kf.e();
                                                                                                                                                        var2_3 -= 2;
                                                                                                                                                        if (var20_2 == 0) break block220;
                                                                                                                                                    }
                                                                                                                                                    return false;
                                                                                                                                                }
                                                                                                                                                if (var2_3 < this.le) {
                                                                                                                                                    return false;
                                                                                                                                                }
                                                                                                                                                this.Kf.z = 0;
                                                                                                                                                this.rh.a(this.Kf.y, 0, this.le);
                                                                                                                                                this.ne = 0;
                                                                                                                                                this.db = this.cb;
                                                                                                                                                this.cb = this.bb;
                                                                                                                                                this.bb = this.me;
                                                                                                                                                if (this.me == 81) {
                                                                                                                                                    this.b(this.le, this.Kf, 9759);
                                                                                                                                                    this.Hf = false;
                                                                                                                                                    this.me = -1;
                                                                                                                                                    return true;
                                                                                                                                                }
                                                                                                                                                if (this.me != 176) break block222;
                                                                                                                                                this.qh = this.Kf.b(false);
                                                                                                                                                this.dh = this.Kf.c(true);
                                                                                                                                                this.vg = this.Kf.c();
                                                                                                                                                this.Qh = this.Kf.e(true);
                                                                                                                                                this.ke = this.Kf.e();
                                                                                                                                                if (this.Qh == 0 || this.rb != -1) break block223;
                                                                                                                                                signlink.dnslookup(ZTQFNQRH.a(this.Qh, true));
                                                                                                                                                this.M(537);
                                                                                                                                                var3_6 = 650;
                                                                                                                                                if (this.qh != 201 || this.vg == 1) {
                                                                                                                                                    var3_6 = 655;
                                                                                                                                                }
                                                                                                                                                this.Pb = "";
                                                                                                                                                this.hh = false;
                                                                                                                                                var4_48 = 0;
                                                                                                                                                if (var20_2 == 0) ** GOTO lbl71
                                                                                                                                                do {
                                                                                                                                                    if (DUCMKFAY.d[var4_48] != null && DUCMKFAY.d[var4_48].h == var3_6) {
                                                                                                                                                        this.rb = DUCMKFAY.d[var4_48].D;
                                                                                                                                                        if (var20_2 == 0) break;
                                                                                                                                                    }
                                                                                                                                                    ++var4_48;
lbl71:
                                                                                                                                                    // 2 sources

                                                                                                                                                } while (var4_48 < DUCMKFAY.d.length);
                                                                                                                                            }
                                                                                                                                            this.me = -1;
                                                                                                                                            return true;
                                                                                                                                        }
                                                                                                                                        if (this.me != 64) break block224;
                                                                                                                                        this.nj = this.Kf.b(false);
                                                                                                                                        this.oj = this.Kf.h(2);
                                                                                                                                        var3_7 = this.nj;
                                                                                                                                        if (var20_2 == 0) ** GOTO lbl91
                                                                                                                                        do {
                                                                                                                                            var4_49 = this.oj;
                                                                                                                                            if (var20_2 == 0) ** GOTO lbl89
                                                                                                                                            do {
                                                                                                                                                if (this.N[this.Ac][var3_7][var4_49] != null) {
                                                                                                                                                    this.N[this.Ac][var3_7][var4_49] = null;
                                                                                                                                                    this.c(var3_7, var4_49);
                                                                                                                                                }
                                                                                                                                                ++var4_49;
lbl89:
                                                                                                                                                // 2 sources

                                                                                                                                            } while (var4_49 < this.oj + 8);
                                                                                                                                            ++var3_7;
lbl91:
                                                                                                                                            // 2 sources

                                                                                                                                        } while (var3_7 < this.nj + 8);
                                                                                                                                        var4_50 = (DYMVKFXP)this.Ch.b();
                                                                                                                                        if (var20_2 == 0) ** GOTO lbl98
                                                                                                                                        do {
                                                                                                                                            if (var4_50.l >= this.nj && var4_50.l < this.nj + 8 && var4_50.m >= this.oj && var4_50.m < this.oj + 8 && var4_50.j == this.Ac) {
                                                                                                                                                var4_50.i = 0;
                                                                                                                                            }
                                                                                                                                            var4_50 = (DYMVKFXP)this.Ch.a(false);
lbl98:
                                                                                                                                            // 2 sources

                                                                                                                                        } while (var4_50 != null);
                                                                                                                                        this.me = -1;
                                                                                                                                        return true;
                                                                                                                                    }
                                                                                                                                    if (this.me != 185) break block225;
                                                                                                                                    var3_8 = this.Kf.d((byte)-74);
                                                                                                                                    DUCMKFAY.d[var3_8].A = 3;
                                                                                                                                    if (client.Bg.tb != null) break block226;
                                                                                                                                    DUCMKFAY.d[var3_8].B = (client.Bg.vb[0] << 25) + (client.Bg.vb[4] << 20) + (client.Bg.Mb[0] << 15) + (client.Bg.Mb[8] << 10) + (client.Bg.Mb[11] << 5) + client.Bg.Mb[1];
                                                                                                                                    if (var20_2 == 0) break block227;
                                                                                                                                }
                                                                                                                                DUCMKFAY.d[var3_8].B = (int)(305419896L + client.Bg.tb.x);
                                                                                                                            }
                                                                                                                            this.me = -1;
                                                                                                                            return true;
                                                                                                                        }
                                                                                                                        if (this.me != 107) break block228;
                                                                                                                        this.jh = false;
                                                                                                                        var3_9 = 0;
                                                                                                                        if (var20_2 == 0) ** GOTO lbl121
                                                                                                                        do {
                                                                                                                            this.Kb[var3_9] = false;
                                                                                                                            ++var3_9;
lbl121:
                                                                                                                            // 2 sources

                                                                                                                        } while (var3_9 < 5);
                                                                                                                        this.me = -1;
                                                                                                                        return true;
                                                                                                                    }
                                                                                                                    if (this.me != 72) break block229;
                                                                                                                    var3_10 = this.Kf.c((byte)108);
                                                                                                                    var4_51 = DUCMKFAY.d[var3_10];
                                                                                                                    var5_78 = 0;
                                                                                                                    if (var20_2 == 0) ** GOTO lbl134
                                                                                                                    do {
                                                                                                                        var4_51.U[var5_78] = -1;
                                                                                                                        var4_51.U[var5_78] = 0;
                                                                                                                        ++var5_78;
lbl134:
                                                                                                                        // 2 sources

                                                                                                                    } while (var5_78 < var4_51.U.length);
                                                                                                                    this.me = -1;
                                                                                                                    return true;
                                                                                                                }
                                                                                                                if (this.me != 214) break block230;
                                                                                                                this.I = this.le / 8;
                                                                                                                var3_11 = 0;
                                                                                                                if (var20_2 == 0) ** GOTO lbl145
                                                                                                                do {
                                                                                                                    this.Hc[var3_11] = this.Kf.e(-35089);
                                                                                                                    ++var3_11;
lbl145:
                                                                                                                    // 2 sources

                                                                                                                } while (var3_11 < this.I);
                                                                                                                this.me = -1;
                                                                                                                return true;
                                                                                                            }
                                                                                                            if (this.me == 166) {
                                                                                                                this.jh = true;
                                                                                                                this.Zf = this.Kf.c();
                                                                                                                this.ag = this.Kf.c();
                                                                                                                this.bg = this.Kf.e();
                                                                                                                this.cg = this.Kf.c();
                                                                                                                this.dg = this.Kf.c();
                                                                                                                if (this.dg >= 100) {
                                                                                                                    this.sb = this.Zf * 128 + 64;
                                                                                                                    this.ub = this.ag * 128 + 64;
                                                                                                                    this.tb = this.a(this.Ac, this.ub, true, this.sb) - this.bg;
                                                                                                                }
                                                                                                                this.me = -1;
                                                                                                                return true;
                                                                                                            }
                                                                                                            if (this.me != 134) break block231;
                                                                                                            this.ch = true;
                                                                                                            var3_12 = this.Kf.c();
                                                                                                            var4_52 = this.Kf.e((byte)41);
                                                                                                            var5_79 = this.Kf.c();
                                                                                                            this.yb[var3_12] = var4_52;
                                                                                                            this.Ec[var3_12] = var5_79;
                                                                                                            this.We[var3_12] = 1;
                                                                                                            var6_96 = 0;
                                                                                                            if (var20_2 == 0) ** GOTO lbl176
                                                                                                            do {
                                                                                                                if (var4_52 >= client.xe[var6_96]) {
                                                                                                                    this.We[var3_12] = var6_96 + 2;
                                                                                                                }
                                                                                                                ++var6_96;
lbl176:
                                                                                                                // 2 sources

                                                                                                            } while (var6_96 < 98);
                                                                                                            this.me = -1;
                                                                                                            return true;
                                                                                                        }
                                                                                                        if (this.me == 71) {
                                                                                                            var3_13 = this.Kf.e();
                                                                                                            var4_53 = this.Kf.g(0);
                                                                                                            if (var3_13 == 65535) {
                                                                                                                var3_13 = -1;
                                                                                                            }
                                                                                                            this.Fg[var4_53] = var3_13;
                                                                                                            this.ch = true;
                                                                                                            this.eg = true;
                                                                                                            this.me = -1;
                                                                                                            return true;
                                                                                                        }
                                                                                                        if (this.me == 74) {
                                                                                                            var3_14 = this.Kf.c((byte)108);
                                                                                                            if (var3_14 == 65535) {
                                                                                                                var3_14 = -1;
                                                                                                            }
                                                                                                            if (var3_14 != this.md && this.ah && !client.qd && this.ej == 0) {
                                                                                                                this.yi = var3_14;
                                                                                                                this.zi = true;
                                                                                                                this.vf.b(2, this.yi);
                                                                                                            }
                                                                                                            this.md = var3_14;
                                                                                                            this.me = -1;
                                                                                                            return true;
                                                                                                        }
                                                                                                        if (this.me == 121) {
                                                                                                            var3_15 = this.Kf.d((byte)-74);
                                                                                                            var4_54 = this.Kf.c(true);
                                                                                                            if (this.ah && !client.qd) {
                                                                                                                this.yi = var3_15;
                                                                                                                this.zi = false;
                                                                                                                this.vf.b(2, this.yi);
                                                                                                                this.ej = var4_54;
                                                                                                            }
                                                                                                            this.me = -1;
                                                                                                            return true;
                                                                                                        }
                                                                                                        if (this.me == 109) {
                                                                                                            this.d(true);
                                                                                                            this.me = -1;
                                                                                                            return false;
                                                                                                        }
                                                                                                        if (this.me == 70) {
                                                                                                            var3_16 = this.Kf.f();
                                                                                                            var4_55 = this.Kf.j(-665);
                                                                                                            var5_80 = this.Kf.c((byte)108);
                                                                                                            var6_97 = DUCMKFAY.d[var5_80];
                                                                                                            var6_97.eb = var3_16;
                                                                                                            var6_97.gb = var4_55;
                                                                                                            this.me = -1;
                                                                                                            return true;
                                                                                                        }
                                                                                                        if (this.me != 73 && this.me != 241) break block232;
                                                                                                        var3_17 = this.wf;
                                                                                                        var4_56 = this.xf;
                                                                                                        if (this.me == 73) {
                                                                                                            var3_17 = this.Kf.c(true);
                                                                                                            var4_56 = this.Kf.e();
                                                                                                            this.ih = false;
                                                                                                        }
                                                                                                        if (this.me != 241) break block233;
                                                                                                        var4_56 = this.Kf.c(true);
                                                                                                        this.Kf.f(this.tg);
                                                                                                        var5_81 = 0;
                                                                                                        if (var20_2 == 0) ** GOTO lbl254
                                                                                                        do {
                                                                                                            var6_98 = 0;
                                                                                                            if (var20_2 == 0) ** GOTO lbl252
                                                                                                            do {
                                                                                                                var7_110 = 0;
                                                                                                                if (var20_2 == 0) ** GOTO lbl250
                                                                                                                do {
                                                                                                                    block235: {
                                                                                                                        block234: {
                                                                                                                            if ((var8_118 = this.Kf.c(1, 0)) != 1) break block234;
                                                                                                                            this.Eg[var5_81][var6_98][var7_110] = this.Kf.c(26, 0);
                                                                                                                            if (var20_2 == 0) break block235;
                                                                                                                        }
                                                                                                                        this.Eg[var5_81][var6_98][var7_110] = -1;
                                                                                                                    }
                                                                                                                    ++var7_110;
lbl250:
                                                                                                                    // 2 sources

                                                                                                                } while (var7_110 < 13);
                                                                                                                ++var6_98;
lbl252:
                                                                                                                // 2 sources

                                                                                                            } while (var6_98 < 13);
                                                                                                            ++var5_81;
lbl254:
                                                                                                            // 2 sources

                                                                                                        } while (var5_81 < 4);
                                                                                                        this.Kf.a(true);
                                                                                                        var3_17 = this.Kf.e();
                                                                                                        this.ih = true;
                                                                                                    }
                                                                                                    if (this.wf == var3_17 && this.xf == var4_56 && this.Be == 2) {
                                                                                                        this.me = -1;
                                                                                                        return true;
                                                                                                    }
                                                                                                    this.wf = var3_17;
                                                                                                    this.xf = var4_56;
                                                                                                    this.Me = (this.wf - 6) * 8;
                                                                                                    this.Ne = (this.xf - 6) * 8;
                                                                                                    this.Qg = false;
                                                                                                    if ((this.wf / 8 == 48 || this.wf / 8 == 49) && this.xf / 8 == 48) {
                                                                                                        this.Qg = true;
                                                                                                    }
                                                                                                    if (this.wf / 8 == 48 && this.xf / 8 == 148) {
                                                                                                        this.Qg = true;
                                                                                                    }
                                                                                                    this.Be = 1;
                                                                                                    this.K = System.currentTimeMillis();
                                                                                                    this.oh.a(0);
                                                                                                    this.qj.a(0, "Loading - please wait.", 23693, 151, 257);
                                                                                                    this.qj.a(0xFFFFFF, "Loading - please wait.", 23693, 150, 256);
                                                                                                    this.oh.a(4, 23680, this.l, 4);
                                                                                                    if (this.me != 73) break block236;
                                                                                                    var5_81 = 0;
                                                                                                    var6_98 = (this.wf - 6) / 8;
                                                                                                    if (var20_2 == 0) ** GOTO lbl289
                                                                                                    do {
                                                                                                        var7_110 = (this.xf - 6) / 8;
                                                                                                        if (var20_2 == 0) ** GOTO lbl287
                                                                                                        do {
                                                                                                            ++var5_81;
                                                                                                            ++var7_110;
lbl287:
                                                                                                            // 2 sources

                                                                                                        } while (var7_110 <= (this.xf + 6) / 8);
                                                                                                        ++var6_98;
lbl289:
                                                                                                        // 2 sources

                                                                                                    } while (var6_98 <= (this.wf + 6) / 8);
                                                                                                    this.Gh = new byte[var5_81][];
                                                                                                    this.Si = new byte[var5_81][];
                                                                                                    this.Fi = new int[var5_81];
                                                                                                    this.Gi = new int[var5_81];
                                                                                                    this.Hi = new int[var5_81];
                                                                                                    var5_81 = 0;
                                                                                                    var7_110 = (this.wf - 6) / 8;
                                                                                                    if (var20_2 == 0) ** GOTO lbl318
                                                                                                    do {
                                                                                                        var8_118 = (this.xf - 6) / 8;
                                                                                                        if (var20_2 == 0) ** GOTO lbl316
                                                                                                        do {
                                                                                                            block238: {
                                                                                                                block237: {
                                                                                                                    this.Fi[var5_81] = (var7_110 << 8) + var8_118;
                                                                                                                    if (!this.Qg || var8_118 != 49 && var8_118 != 149 && var8_118 != 147 && var7_110 != 50 && (var7_110 != 49 || var8_118 != 47)) break block237;
                                                                                                                    this.Gi[var5_81] = -1;
                                                                                                                    this.Hi[var5_81] = -1;
                                                                                                                    ++var5_81;
                                                                                                                    if (var20_2 == 0) break block238;
                                                                                                                }
                                                                                                                if ((var9_125 = (this.Gi[var5_81] = this.vf.a(0, 0, var8_118, var7_110))) != -1) {
                                                                                                                    this.vf.b(3, var9_125);
                                                                                                                }
                                                                                                                if ((var10_134 = (this.Hi[var5_81] = this.vf.a(1, 0, var8_118, var7_110))) != -1) {
                                                                                                                    this.vf.b(3, var10_134);
                                                                                                                }
                                                                                                                ++var5_81;
                                                                                                            }
                                                                                                            ++var8_118;
lbl316:
                                                                                                            // 2 sources

                                                                                                        } while (var8_118 <= (this.xf + 6) / 8);
                                                                                                        ++var7_110;
lbl318:
                                                                                                        // 2 sources

                                                                                                    } while (var7_110 <= (this.wf + 6) / 8);
                                                                                                }
                                                                                                if (this.me != 241) break block239;
                                                                                                var5_81 = 0;
                                                                                                var6_99 = new int[676];
                                                                                                var7_110 = 0;
                                                                                                if (var20_2 == 0) ** GOTO lbl352
                                                                                                do {
                                                                                                    var8_118 = 0;
                                                                                                    if (var20_2 == 0) ** GOTO lbl350
                                                                                                    do {
                                                                                                        var9_125 = 0;
                                                                                                        if (var20_2 == 0) ** GOTO lbl348
                                                                                                        do {
                                                                                                            block240: {
                                                                                                                if ((var10_134 = this.Eg[var7_110][var8_118][var9_125]) == -1) break block240;
                                                                                                                var11_136 = var10_134 >> 14 & 1023;
                                                                                                                var12_138 = var10_134 >> 3 & 2047;
                                                                                                                var13_140 = (var11_136 / 8 << 8) + var12_138 / 8;
                                                                                                                var14_141 = 0;
                                                                                                                if (var20_2 == 0) ** GOTO lbl343
                                                                                                                do {
                                                                                                                    if (var6_99[var14_141] == var13_140) {
                                                                                                                        var13_140 = -1;
                                                                                                                        if (var20_2 == 0) break;
                                                                                                                    }
                                                                                                                    ++var14_141;
lbl343:
                                                                                                                    // 2 sources

                                                                                                                } while (var14_141 < var5_81);
                                                                                                                if (var13_140 != -1) {
                                                                                                                    var6_99[var5_81++] = var13_140;
                                                                                                                }
                                                                                                            }
                                                                                                            ++var9_125;
lbl348:
                                                                                                            // 2 sources

                                                                                                        } while (var9_125 < 13);
                                                                                                        ++var8_118;
lbl350:
                                                                                                        // 2 sources

                                                                                                    } while (var8_118 < 13);
                                                                                                    ++var7_110;
lbl352:
                                                                                                    // 2 sources

                                                                                                } while (var7_110 < 4);
                                                                                                this.Gh = new byte[var5_81][];
                                                                                                this.Si = new byte[var5_81][];
                                                                                                this.Fi = new int[var5_81];
                                                                                                this.Gi = new int[var5_81];
                                                                                                this.Hi = new int[var5_81];
                                                                                                var8_118 = 0;
                                                                                                if (var20_2 == 0) ** GOTO lbl366
                                                                                                do {
                                                                                                    if ((var12_138 = (this.Gi[var8_118] = this.vf.a(0, 0, var11_136 = (var9_125 = (this.Fi[var8_118] = var6_99[var8_118])) & 255, var10_134 = var9_125 >> 8 & 255))) != -1) {
                                                                                                        this.vf.b(3, var12_138);
                                                                                                    }
                                                                                                    if ((var13_140 = (this.Hi[var8_118] = this.vf.a(1, 0, var11_136, var10_134))) != -1) {
                                                                                                        this.vf.b(3, var13_140);
                                                                                                    }
                                                                                                    ++var8_118;
lbl366:
                                                                                                    // 2 sources

                                                                                                } while (var8_118 < var5_81);
                                                                                            }
                                                                                            var5_81 = this.Me - this.Oe;
                                                                                            var6_100 = this.Ne - this.Pe;
                                                                                            this.Oe = this.Me;
                                                                                            this.Pe = this.Ne;
                                                                                            var7_110 = 0;
                                                                                            if (var20_2 == 0) ** GOTO lbl388
                                                                                            do {
                                                                                                block241: {
                                                                                                    if ((var8_119 = this.V[var7_110]) == null) break block241;
                                                                                                    var9_125 = 0;
                                                                                                    if (var20_2 == 0) ** GOTO lbl383
                                                                                                    do {
                                                                                                        v0 = var9_125;
                                                                                                        var8_119.m[v0] = var8_119.m[v0] - var5_81;
                                                                                                        v1 = var9_125++;
                                                                                                        var8_119.n[v1] = var8_119.n[v1] - var6_100;
lbl383:
                                                                                                        // 2 sources

                                                                                                    } while (var9_125 < 10);
                                                                                                    var8_119.kb -= var5_81 * 128;
                                                                                                    var8_119.lb -= var6_100 * 128;
                                                                                                }
                                                                                                ++var7_110;
lbl388:
                                                                                                // 2 sources

                                                                                            } while (var7_110 < 16384);
                                                                                            var8_120 = 0;
                                                                                            if (var20_2 == 0) ** GOTO lbl405
                                                                                            do {
                                                                                                block242: {
                                                                                                    if ((var9_126 = this.Yb[var8_120]) == null) break block242;
                                                                                                    var10_134 = 0;
                                                                                                    if (var20_2 == 0) ** GOTO lbl400
                                                                                                    do {
                                                                                                        v2 = var10_134;
                                                                                                        var9_126.m[v2] = var9_126.m[v2] - var5_81;
                                                                                                        v3 = var10_134++;
                                                                                                        var9_126.n[v3] = var9_126.n[v3] - var6_100;
lbl400:
                                                                                                        // 2 sources

                                                                                                    } while (var10_134 < 10);
                                                                                                    var9_126.kb -= var5_81 * 128;
                                                                                                    var9_126.lb -= var6_100 * 128;
                                                                                                }
                                                                                                ++var8_120;
lbl405:
                                                                                                // 2 sources

                                                                                            } while (var8_120 < this.Wb);
                                                                                            this.Hf = true;
                                                                                            var9_127 = 0;
                                                                                            var10_134 = 104;
                                                                                            var11_136 = 1;
                                                                                            if (var5_81 < 0) {
                                                                                                var9_127 = 103;
                                                                                                var10_134 = -1;
                                                                                                var11_136 = -1;
                                                                                            }
                                                                                            var12_138 = 0;
                                                                                            var13_140 = 104;
                                                                                            var14_141 = 1;
                                                                                            if (var6_100 < 0) {
                                                                                                var12_138 = 103;
                                                                                                var13_140 = -1;
                                                                                                var14_141 = -1;
                                                                                            }
                                                                                            var15_142 = var9_127;
                                                                                            if (var20_2 == 0) ** GOTO lbl443
                                                                                            do {
                                                                                                var16_143 = var12_138;
                                                                                                if (var20_2 == 0) ** GOTO lbl441
                                                                                                do {
                                                                                                    var17_145 = var15_142 + var5_81;
                                                                                                    var18_146 = var16_143 + var6_100;
                                                                                                    var19_147 = 0;
                                                                                                    if (var20_2 == 0) ** GOTO lbl439
                                                                                                    do {
                                                                                                        block244: {
                                                                                                            block243: {
                                                                                                                if (var17_145 < 0 || var18_146 < 0 || var17_145 >= 104 || var18_146 >= 104) break block243;
                                                                                                                this.N[var19_147][var15_142][var16_143] = this.N[var19_147][var17_145][var18_146];
                                                                                                                if (var20_2 == 0) break block244;
                                                                                                            }
                                                                                                            this.N[var19_147][var15_142][var16_143] = null;
                                                                                                        }
                                                                                                        ++var19_147;
lbl439:
                                                                                                        // 2 sources

                                                                                                    } while (var19_147 < 4);
                                                                                                    var16_143 += var14_141;
lbl441:
                                                                                                    // 2 sources

                                                                                                } while (var16_143 != var13_140);
                                                                                                var15_142 += var11_136;
lbl443:
                                                                                                // 2 sources

                                                                                            } while (var15_142 != var10_134);
                                                                                            var16_144 = (DYMVKFXP)this.Ch.b();
                                                                                            if (var20_2 == 0) ** GOTO lbl452
                                                                                            do {
                                                                                                var16_144.l -= var5_81;
                                                                                                var16_144.m -= var6_100;
                                                                                                if (var16_144.l < 0 || var16_144.m < 0 || var16_144.l >= 104 || var16_144.m >= 104) {
                                                                                                    var16_144.a();
                                                                                                }
                                                                                                var16_144 = (DYMVKFXP)this.Ch.a(false);
lbl452:
                                                                                                // 2 sources

                                                                                            } while (var16_144 != null);
                                                                                            if (this.gj != 0) {
                                                                                                this.gj -= var5_81;
                                                                                                this.hj -= var6_100;
                                                                                            }
                                                                                            this.jh = false;
                                                                                            this.me = -1;
                                                                                            return true;
                                                                                        }
                                                                                        if (this.me == 208) {
                                                                                            var3_18 = this.Kf.j(-665);
                                                                                            if (var3_18 >= 0) {
                                                                                                this.a(var3_18, (byte)6);
                                                                                            }
                                                                                            this.we = var3_18;
                                                                                            this.me = -1;
                                                                                            return true;
                                                                                        }
                                                                                        if (this.me == 99) {
                                                                                            this.ze = this.Kf.c();
                                                                                            this.me = -1;
                                                                                            return true;
                                                                                        }
                                                                                        if (this.me == 75) {
                                                                                            var3_19 = this.Kf.d((byte)-74);
                                                                                            var4_57 = this.Kf.d((byte)-74);
                                                                                            DUCMKFAY.d[var4_57].A = 2;
                                                                                            DUCMKFAY.d[var4_57].B = var3_19;
                                                                                            this.me = -1;
                                                                                            return true;
                                                                                        }
                                                                                        if (this.me == 114) {
                                                                                            this.fg = this.Kf.c((byte)108) * 30;
                                                                                            this.me = -1;
                                                                                            return true;
                                                                                        }
                                                                                        if (this.me != 60) break block245;
                                                                                        this.oj = this.Kf.c();
                                                                                        this.nj = this.Kf.b(false);
                                                                                        if (var20_2 == 0) ** GOTO lbl489
                                                                                        do {
                                                                                            var3_20 = this.Kf.c();
                                                                                            this.a(this.ug, this.Kf, var3_20);
lbl489:
                                                                                            // 2 sources

                                                                                        } while (this.Kf.z < this.le);
                                                                                        this.me = -1;
                                                                                        return true;
                                                                                    }
                                                                                    if (this.me == 35) {
                                                                                        var3_21 = this.Kf.c();
                                                                                        var4_58 = this.Kf.c();
                                                                                        var5_82 = this.Kf.c();
                                                                                        var6_101 = this.Kf.c();
                                                                                        this.Kb[var3_21] = true;
                                                                                        this.Hb[var3_21] = var4_58;
                                                                                        this.ai[var3_21] = var5_82;
                                                                                        this.Kc[var3_21] = var6_101;
                                                                                        this.Ie[var3_21] = 0;
                                                                                        this.me = -1;
                                                                                        return true;
                                                                                    }
                                                                                    if (this.me == 174) {
                                                                                        var3_22 = this.Kf.e();
                                                                                        var4_59 = this.Kf.c();
                                                                                        var5_83 = this.Kf.e();
                                                                                        if (this.ib && !client.qd && this.pf < 50) {
                                                                                            this.ei[this.pf] = var3_22;
                                                                                            this.Mi[this.pf] = var4_59;
                                                                                            this.Vi[this.pf] = var5_83 + JHDAGNBV.f[var3_22];
                                                                                            ++this.pf;
                                                                                        }
                                                                                        this.me = -1;
                                                                                        return true;
                                                                                    }
                                                                                    if (this.me == 104) {
                                                                                        var3_23 = this.Kf.b(false);
                                                                                        var4_60 = this.Kf.g(0);
                                                                                        var5_84 = this.Kf.i();
                                                                                        if (var3_23 >= 1 && var3_23 <= 5) {
                                                                                            if (var5_84.equalsIgnoreCase("null")) {
                                                                                                var5_84 = null;
                                                                                            }
                                                                                            this.Cg[var3_23 - 1] = var5_84;
                                                                                            this.Dg[var3_23 - 1] = var4_60 == 0;
                                                                                        }
                                                                                        this.me = -1;
                                                                                        return true;
                                                                                    }
                                                                                    if (this.me == 78) {
                                                                                        this.gj = 0;
                                                                                        this.me = -1;
                                                                                        return true;
                                                                                    }
                                                                                    if (this.me != 253) break block246;
                                                                                    var3_24 = this.Kf.i();
                                                                                    if (!var3_24.endsWith(":tradereq:")) break block247;
                                                                                    var4_61 = var3_24.substring(0, var3_24.indexOf(":"));
                                                                                    var5_85 = ZTQFNQRH.a(var4_61);
                                                                                    var7_111 = false;
                                                                                    var8_121 = 0;
                                                                                    if (var20_2 == 0) ** GOTO lbl544
                                                                                    do {
                                                                                        if (this.Hc[var8_121] == var5_85) {
                                                                                            var7_111 = true;
                                                                                            if (var20_2 == 0) break;
                                                                                        }
                                                                                        ++var8_121;
lbl544:
                                                                                        // 2 sources

                                                                                    } while (var8_121 < this.I);
                                                                                    if (var7_111 || this.Wi != 0) break block248;
                                                                                    this.a("wishes to trade with you.", 4, var4_61, this.Vd);
                                                                                    if (var20_2 == 0) break block248;
                                                                                }
                                                                                if (!var3_24.endsWith(":duelreq:")) break block249;
                                                                                var4_61 = var3_24.substring(0, var3_24.indexOf(":"));
                                                                                var5_85 = ZTQFNQRH.a(var4_61);
                                                                                var7_111 = false;
                                                                                var8_121 = 0;
                                                                                if (var20_2 == 0) ** GOTO lbl560
                                                                                do {
                                                                                    if (this.Hc[var8_121] == var5_85) {
                                                                                        var7_111 = true;
                                                                                        if (var20_2 == 0) break;
                                                                                    }
                                                                                    ++var8_121;
lbl560:
                                                                                    // 2 sources

                                                                                } while (var8_121 < this.I);
                                                                                if (var7_111 || this.Wi != 0) break block248;
                                                                                this.a("wishes to duel with you.", 8, var4_61, this.Vd);
                                                                                if (var20_2 == 0) break block248;
                                                                            }
                                                                            if (!var3_24.endsWith(":chalreq:")) break block250;
                                                                            var4_61 = var3_24.substring(0, var3_24.indexOf(":"));
                                                                            var5_85 = ZTQFNQRH.a(var4_61);
                                                                            var7_111 = false;
                                                                            var8_121 = 0;
                                                                            if (var20_2 == 0) ** GOTO lbl576
                                                                            do {
                                                                                if (this.Hc[var8_121] == var5_85) {
                                                                                    var7_111 = true;
                                                                                    if (var20_2 == 0) break;
                                                                                }
                                                                                ++var8_121;
lbl576:
                                                                                // 2 sources

                                                                            } while (var8_121 < this.I);
                                                                            if (var7_111 || this.Wi != 0) break block248;
                                                                            var9_128 = var3_24.substring(var3_24.indexOf(":") + 1, var3_24.length() - 9);
                                                                            this.a(var9_128, 8, var4_61, this.Vd);
                                                                            if (var20_2 == 0) break block248;
                                                                        }
                                                                        this.a(var3_24, 0, "", this.Vd);
                                                                    }
                                                                    this.me = -1;
                                                                    return true;
                                                                }
                                                                if (this.me != 1) break block251;
                                                                var3_25 = 0;
                                                                if (var20_2 == 0) ** GOTO lbl594
                                                                do {
                                                                    if (this.Yb[var3_25] != null) {
                                                                        this.Yb[var3_25].M = -1;
                                                                    }
                                                                    ++var3_25;
lbl594:
                                                                    // 2 sources

                                                                } while (var3_25 < this.Yb.length);
                                                                var4_62 = 0;
                                                                if (var20_2 == 0) ** GOTO lbl601
                                                                do {
                                                                    if (this.V[var4_62] != null) {
                                                                        this.V[var4_62].M = -1;
                                                                    }
                                                                    ++var4_62;
lbl601:
                                                                    // 2 sources

                                                                } while (var4_62 < this.V.length);
                                                                this.me = -1;
                                                                return true;
                                                            }
                                                            if (this.me != 50) break block252;
                                                            var3_26 = this.Kf.e(-35089);
                                                            var5_86 = this.Kf.c();
                                                            var6_102 = ZTQFNQRH.a(-45804, ZTQFNQRH.a(var3_26, (byte)-99));
                                                            var7_112 = 0;
                                                            if (var20_2 == 0) ** GOTO lbl623
                                                            do {
                                                                if (var3_26 == this.ld[var7_112]) {
                                                                    if (this.M[var7_112] != var5_86) {
                                                                        this.M[var7_112] = var5_86;
                                                                        this.ch = true;
                                                                        if (var5_86 > 0) {
                                                                            this.a(String.valueOf(var6_102) + " has logged in.", 5, "", this.Vd);
                                                                        }
                                                                        if (var5_86 == 0) {
                                                                            this.a(String.valueOf(var6_102) + " has logged out.", 5, "", this.Vd);
                                                                        }
                                                                    }
                                                                    var6_102 = null;
                                                                    break;
                                                                }
                                                                ++var7_112;
lbl623:
                                                                // 2 sources

                                                            } while (var7_112 < this.hc);
                                                            if (var6_102 != null && this.hc < 200) {
                                                                this.ld[this.hc] = var3_26;
                                                                this.Jf[this.hc] = var6_102;
                                                                this.M[this.hc] = var5_86;
                                                                ++this.hc;
                                                                this.ch = true;
                                                            }
                                                            var8_122 = false;
                                                            if (var20_2 == 0) ** GOTO lbl651
                                                            do {
                                                                var8_122 = true;
                                                                var9_129 = 0;
                                                                if (var20_2 == 0) ** GOTO lbl650
                                                                do {
                                                                    if (this.M[var9_129] != client.nd && this.M[var9_129 + 1] == client.nd || this.M[var9_129] == 0 && this.M[var9_129 + 1] != 0) {
                                                                        var10_135 = this.M[var9_129];
                                                                        this.M[var9_129] = this.M[var9_129 + 1];
                                                                        this.M[var9_129 + 1] = var10_135;
                                                                        var11_137 = this.Jf[var9_129];
                                                                        this.Jf[var9_129] = this.Jf[var9_129 + 1];
                                                                        this.Jf[var9_129 + 1] = var11_137;
                                                                        var12_139 = this.ld[var9_129];
                                                                        this.ld[var9_129] = this.ld[var9_129 + 1];
                                                                        this.ld[var9_129 + 1] = var12_139;
                                                                        this.ch = true;
                                                                        var8_122 = false;
                                                                    }
                                                                    ++var9_129;
lbl650:
                                                                    // 2 sources

                                                                } while (var9_129 < this.hc - 1);
lbl651:
                                                                // 2 sources

                                                            } while (!var8_122);
                                                            this.me = -1;
                                                            return true;
                                                        }
                                                        if (this.me == 110) {
                                                            if (this.si == 12) {
                                                                this.ch = true;
                                                            }
                                                            this.Xg = this.Kf.c();
                                                            this.me = -1;
                                                            return true;
                                                        }
                                                        if (this.me == 254) {
                                                            this.pb = this.Kf.c();
                                                            if (this.pb == 1) {
                                                                this.ti = this.Kf.e();
                                                            }
                                                            if (this.pb >= 2 && this.pb <= 6) {
                                                                if (this.pb == 2) {
                                                                    this.Tc = 64;
                                                                    this.Uc = 64;
                                                                }
                                                                if (this.pb == 3) {
                                                                    this.Tc = 0;
                                                                    this.Uc = 64;
                                                                }
                                                                if (this.pb == 4) {
                                                                    this.Tc = 128;
                                                                    this.Uc = 64;
                                                                }
                                                                if (this.pb == 5) {
                                                                    this.Tc = 64;
                                                                    this.Uc = 0;
                                                                }
                                                                if (this.pb == 6) {
                                                                    this.Tc = 64;
                                                                    this.Uc = 128;
                                                                }
                                                                this.pb = 2;
                                                                this.Qc = this.Kf.e();
                                                                this.Rc = this.Kf.e();
                                                                this.Sc = this.Kf.c();
                                                            }
                                                            if (this.pb == 10) {
                                                                this.Pc = this.Kf.e();
                                                            }
                                                            this.me = -1;
                                                            return true;
                                                        }
                                                        if (this.me == 248) {
                                                            var3_27 = this.Kf.c(true);
                                                            var4_63 = this.Kf.e();
                                                            if (this.vj != -1) {
                                                                this.vj = -1;
                                                                this.ui = true;
                                                            }
                                                            if (this.wi != 0) {
                                                                this.wi = 0;
                                                                this.ui = true;
                                                            }
                                                            this.rb = var3_27;
                                                            this.Mh = var4_63;
                                                            this.ch = true;
                                                            this.eg = true;
                                                            this.Yg = false;
                                                            this.me = -1;
                                                            return true;
                                                        }
                                                        if (this.me == 79) {
                                                            var3_28 = this.Kf.c((byte)108);
                                                            var4_64 = this.Kf.c(true);
                                                            var5_87 = DUCMKFAY.d[var3_28];
                                                            if (var5_87 != null && var5_87.db == 0) {
                                                                if (var4_64 < 0) {
                                                                    var4_64 = 0;
                                                                }
                                                                if (var4_64 > var5_87.cb - var5_87.ib) {
                                                                    var4_64 = var5_87.cb - var5_87.ib;
                                                                }
                                                                var5_87.r = var4_64;
                                                            }
                                                            this.me = -1;
                                                            return true;
                                                        }
                                                        if (this.me != 68) break block253;
                                                        var3_29 = 0;
                                                        if (var20_2 == 0) ** GOTO lbl726
                                                        do {
                                                            if (this.Bd[var3_29] != this.Xe[var3_29]) {
                                                                this.Bd[var3_29] = this.Xe[var3_29];
                                                                this.d(false, var3_29);
                                                                this.ch = true;
                                                            }
                                                            ++var3_29;
lbl726:
                                                            // 2 sources

                                                        } while (var3_29 < this.Bd.length);
                                                        this.me = -1;
                                                        return true;
                                                    }
                                                    if (this.me != 196) break block254;
                                                    var3_30 = this.Kf.e(-35089);
                                                    var5_88 = this.Kf.h();
                                                    var6_103 = this.Kf.c();
                                                    var7_113 = false;
                                                    var8_123 = 0;
                                                    if (var20_2 == 0) ** GOTO lbl742
                                                    do {
                                                        if (this.Li[var8_123] == var5_88) {
                                                            var7_113 = true;
                                                            if (var20_2 == 0) break;
                                                        }
                                                        ++var8_123;
lbl742:
                                                        // 2 sources

                                                    } while (var8_123 < 100);
                                                    if (var6_103 > 1) break block255;
                                                    var9_130 = 0;
                                                    if (var20_2 == 0) ** GOTO lbl751
                                                    do {
                                                        if (this.Hc[var9_130] == var3_30) {
                                                            var7_113 = true;
                                                            if (var20_2 == 0) break;
                                                        }
                                                        ++var9_130;
lbl751:
                                                        // 2 sources

                                                    } while (var9_130 < this.I);
                                                }
                                                if (!var7_113 && this.Wi == 0) {
                                                    try {
                                                        this.Li[this.sh] = var5_88;
                                                        this.sh = (this.sh + 1) % 100;
                                                        var9_131 = RTHTIIVA.a(this.le - 13, true, this.Kf);
                                                        if (var6_103 != 3) {
                                                            var9_131 = RKAYAFDQ.a(var9_131, 0);
                                                        }
                                                        if (var6_103 == 2 || var6_103 == 3) {
                                                            this.a(var9_131, 7, "@cr2@" + ZTQFNQRH.a(-45804, ZTQFNQRH.a(var3_30, (byte)-99)), this.Vd);
                                                            if (var20_2 == 0) break block217;
                                                        }
                                                        if (var6_103 == 1) {
                                                            this.a(var9_131, 7, "@cr1@" + ZTQFNQRH.a(-45804, ZTQFNQRH.a(var3_30, (byte)-99)), this.Vd);
                                                            if (var20_2 == 0) break block217;
                                                        }
                                                        this.a(var9_131, 3, ZTQFNQRH.a(-45804, ZTQFNQRH.a(var3_30, (byte)-99)), this.Vd);
                                                    }
                                                    catch (Exception var9_132) {
                                                        signlink.reporterror("cde1");
                                                    }
                                                }
                                            }
                                            this.me = -1;
                                            return true;
                                        }
                                        if (this.me == 85) {
                                            this.oj = this.Kf.b(false);
                                            this.nj = this.Kf.b(false);
                                            this.me = -1;
                                            return true;
                                        }
                                        if (this.me != 24) break block256;
                                        this.gf = this.Kf.h(2);
                                        if (this.gf != this.si) break block257;
                                        if (this.gf != 3) break block258;
                                        this.si = 1;
                                        if (var20_2 == 0) break block259;
                                    }
                                    this.si = 3;
                                }
                                this.ch = true;
                            }
                            this.me = -1;
                            return true;
                        }
                        if (this.me == 246) {
                            var3_31 = this.Kf.c((byte)108);
                            var4_65 = this.Kf.e();
                            var5_89 = this.Kf.e();
                            if (var5_89 == 65535) {
                                DUCMKFAY.d[var3_31].A = 0;
                                this.me = -1;
                                return true;
                            }
                            var6_104 = DJRMEMXO.b(var5_89);
                            DUCMKFAY.d[var3_31].A = 4;
                            DUCMKFAY.d[var3_31].B = var5_89;
                            DUCMKFAY.d[var3_31].lb = var6_104.K;
                            DUCMKFAY.d[var3_31].mb = var6_104.S;
                            DUCMKFAY.d[var3_31].kb = var6_104.B * 100 / var4_65;
                            this.me = -1;
                            return true;
                        }
                        if (this.me == 171) {
                            var3_32 = this.Kf.c() == 1;
                            var4_66 = this.Kf.e();
                            DUCMKFAY.d[var4_66].hb = var3_32;
                            this.me = -1;
                            return true;
                        }
                        if (this.me == 142) {
                            var3_33 = this.Kf.c((byte)108);
                            this.a(var3_33, (byte)6);
                            if (this.vj != -1) {
                                this.vj = -1;
                                this.ui = true;
                            }
                            if (this.wi != 0) {
                                this.wi = 0;
                                this.ui = true;
                            }
                            this.Mh = var3_33;
                            this.ch = true;
                            this.eg = true;
                            this.rb = -1;
                            this.Yg = false;
                            this.me = -1;
                            return true;
                        }
                        if (this.me == 126) {
                            var3_34 = this.Kf.i();
                            var4_67 = this.Kf.c(true);
                            DUCMKFAY.d[var4_67].P = var3_34;
                            if (DUCMKFAY.d[var4_67].D == this.Fg[this.si]) {
                                this.ch = true;
                            }
                            this.me = -1;
                            return true;
                        }
                        if (this.me == 206) {
                            this.Gj = this.Kf.c();
                            this.fb = this.Kf.c();
                            this.Ti = this.Kf.c();
                            this.Ei = true;
                            this.ui = true;
                            this.me = -1;
                            return true;
                        }
                        if (this.me == 240) {
                            if (this.si == 12) {
                                this.ch = true;
                            }
                            this.Mb = this.Kf.f();
                            this.me = -1;
                            return true;
                        }
                        if (this.me == 8) {
                            var3_35 = this.Kf.d((byte)-74);
                            var4_68 = this.Kf.e();
                            DUCMKFAY.d[var3_35].A = 1;
                            DUCMKFAY.d[var3_35].B = var4_68;
                            this.me = -1;
                            return true;
                        }
                        if (this.me == 122) {
                            var3_36 = this.Kf.d((byte)-74);
                            var4_69 = this.Kf.d((byte)-74);
                            var5_90 = var4_69 >> 10 & 31;
                            var6_105 = var4_69 >> 5 & 31;
                            var7_114 = var4_69 & 31;
                            DUCMKFAY.d[var3_36].z = (var5_90 << 19) + (var6_105 << 11) + (var7_114 << 3);
                            this.me = -1;
                            return true;
                        }
                        if (this.me != 53) break block260;
                        this.ch = true;
                        var3_37 = this.Kf.e();
                        var4_70 = DUCMKFAY.d[var3_37];
                        var5_91 = this.Kf.e();
                        var6_106 = 0;
                        if (var20_2 == 0) ** GOTO lbl882
                        do {
                            if ((var7_115 = this.Kf.c()) == 255) {
                                var7_115 = this.Kf.e(true);
                            }
                            var4_70.U[var6_106] = this.Kf.d((byte)-74);
                            var4_70.T[var6_106] = var7_115;
                            ++var6_106;
lbl882:
                            // 2 sources

                        } while (var6_106 < var5_91);
                        var7_115 = var5_91;
                        if (var20_2 == 0) ** GOTO lbl889
                        do {
                            var4_70.U[var7_115] = 0;
                            var4_70.T[var7_115] = 0;
                            ++var7_115;
lbl889:
                            // 2 sources

                        } while (var7_115 < var4_70.U.length);
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 230) {
                        var3_38 = this.Kf.c(true);
                        var4_71 = this.Kf.e();
                        var5_92 = this.Kf.e();
                        var6_107 = this.Kf.d((byte)-74);
                        DUCMKFAY.d[var4_71].lb = var5_92;
                        DUCMKFAY.d[var4_71].mb = var6_107;
                        DUCMKFAY.d[var4_71].kb = var3_38;
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 221) {
                        this.ic = this.Kf.c();
                        this.ch = true;
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 177) {
                        this.jh = true;
                        this.Zd = this.Kf.c();
                        this.ae = this.Kf.c();
                        this.be = this.Kf.e();
                        this.ce = this.Kf.c();
                        this.de = this.Kf.c();
                        if (this.de >= 100) {
                            var3_39 = this.Zd * 128 + 64;
                            var4_72 = this.ae * 128 + 64;
                            var5_93 = this.a(this.Ac, var4_72, true, var3_39) - this.be;
                            var6_108 = var3_39 - this.sb;
                            var7_116 = var5_93 - this.tb;
                            var8_124 = var4_72 - this.ub;
                            var9_133 = (int)Math.sqrt(var6_108 * var6_108 + var8_124 * var8_124);
                            this.vb = (int)(Math.atan2(var7_116, var9_133) * 325.949) & 2047;
                            this.wb = (int)(Math.atan2(var6_108, var8_124) * -325.949) & 2047;
                            if (this.vb < 128) {
                                this.vb = 128;
                            }
                            if (this.vb > 383) {
                                this.vb = 383;
                            }
                        }
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 249) {
                        this.Ye = this.Kf.g(0);
                        this.Sb = this.Kf.d((byte)-74);
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 65) {
                        this.a(this.Kf, this.le, 973);
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 27) {
                        this.bj = false;
                        this.wi = 1;
                        this.ie = "";
                        this.ui = true;
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 187) {
                        this.bj = false;
                        this.wi = 2;
                        this.ie = "";
                        this.ui = true;
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 97) {
                        var3_40 = this.Kf.e();
                        this.a(var3_40, (byte)6);
                        if (this.Mh != -1) {
                            this.Mh = -1;
                            this.ch = true;
                            this.eg = true;
                        }
                        if (this.vj != -1) {
                            this.vj = -1;
                            this.ui = true;
                        }
                        if (this.wi != 0) {
                            this.wi = 0;
                            this.ui = true;
                        }
                        this.rb = var3_40;
                        this.Yg = false;
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 218) {
                        this.Ue = var3_41 = this.Kf.d(false);
                        this.ui = true;
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 87) {
                        var3_42 = this.Kf.c((byte)108);
                        this.Xe[var3_42] = var4_73 = this.Kf.e((byte)41);
                        if (this.Bd[var3_42] != var4_73) {
                            this.Bd[var3_42] = var4_73;
                            this.d(false, var3_42);
                            this.ch = true;
                            if (this.Ue != -1) {
                                this.ui = true;
                            }
                        }
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 36) {
                        var3_43 = this.Kf.c((byte)108);
                        var4_74 = this.Kf.d();
                        this.Xe[var3_43] = var4_74;
                        if (this.Bd[var3_43] != var4_74) {
                            this.Bd[var3_43] = var4_74;
                            this.d(false, var3_43);
                            this.ch = true;
                            if (this.Ue != -1) {
                                this.ui = true;
                            }
                        }
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 61) {
                        this.hf = this.Kf.c();
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 200) {
                        var3_44 = this.Kf.e();
                        var4_75 = this.Kf.f();
                        var5_94 = DUCMKFAY.d[var3_44];
                        var5_94.Y = var4_75;
                        if (var4_75 == -1) {
                            var5_94.N = 0;
                            var5_94.b = 0;
                        }
                        this.me = -1;
                        return true;
                    }
                    if (this.me == 219) {
                        if (this.Mh != -1) {
                            this.Mh = -1;
                            this.ch = true;
                            this.eg = true;
                        }
                        if (this.vj != -1) {
                            this.vj = -1;
                            this.ui = true;
                        }
                        if (this.wi != 0) {
                            this.wi = 0;
                            this.ui = true;
                        }
                        this.rb = -1;
                        this.Yg = false;
                        this.me = -1;
                        return true;
                    }
                    if (this.me != 34) break block261;
                    this.ch = true;
                    var3_45 = this.Kf.e();
                    var4_76 = DUCMKFAY.d[var3_45];
                    if (var20_2 == 0) ** GOTO lbl1042
                    do {
                        var5_95 = this.Kf.k();
                        var6_109 = this.Kf.e();
                        var7_117 = this.Kf.c();
                        if (var7_117 == 255) {
                            var7_117 = this.Kf.h();
                        }
                        if (var5_95 < 0 || var5_95 >= var4_76.U.length) continue;
                        var4_76.U[var5_95] = var6_109;
                        var4_76.T[var5_95] = var7_117;
lbl1042:
                        // 3 sources

                    } while (this.Kf.z < this.le);
                    this.me = -1;
                    return true;
                }
                if (this.me == 105 || this.me == 84 || this.me == 147 || this.me == 215 || this.me == 4 || this.me == 117 || this.me == 156 || this.me == 44 || this.me == 160 || this.me == 101 || this.me == 151) {
                    this.a(this.ug, this.Kf, this.me);
                    this.me = -1;
                    return true;
                }
                if (this.me == 106) {
                    this.si = this.Kf.b(false);
                    this.ch = true;
                    this.eg = true;
                    this.me = -1;
                    return true;
                }
                if (this.me == 164) {
                    var3_46 = this.Kf.c((byte)108);
                    this.a(var3_46, (byte)6);
                    if (this.Mh != -1) {
                        this.Mh = -1;
                        this.ch = true;
                        this.eg = true;
                    }
                    this.vj = var3_46;
                    this.ui = true;
                    this.rb = -1;
                    this.Yg = false;
                    this.me = -1;
                    return true;
                }
                signlink.reporterror("T1 - " + this.me + "," + this.le + " - " + this.cb + "," + this.db);
                this.d(true);
            }
            catch (IOException v4) {
                this.x(-670);
            }
            catch (Exception var2_4) {
                var3_47 = "T2 - " + this.me + "," + this.cb + "," + this.db + " - " + this.le + "," + (this.Me + client.Bg.m[0]) + "," + (this.Ne + client.Bg.n[0]) + " - ";
                var4_77 = 0;
                if (var20_2 == 0) ** GOTO lbl1082
                do {
                    var3_47 = String.valueOf(var3_47) + this.Kf.y[var4_77] + ",";
                    ++var4_77;
lbl1082:
                    // 2 sources

                } while (var4_77 < this.le && var4_77 < 50);
                signlink.reporterror(var3_47);
                this.d(true);
            }
            return true;
        }
        catch (RuntimeException var2_5) {
            signlink.reporterror("32862, " + var1_1 + ", " + var2_5.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void n(byte by) {
        int n = Jj;
        try {
            int n2;
            int n3;
            int n4;
            block19: {
                block18: {
                    ++this.kj;
                    this.a(0, true);
                    this.c(true, this.Qb);
                    this.a(0, false);
                    this.c(false, this.Qb);
                    this.q(-948);
                    this.k(true);
                    if (!this.jh) {
                        n4 = this.Hh;
                        if (this.Od / 256 > n4) {
                            n4 = this.Od / 256;
                        }
                        if (this.Kb[4] && this.ai[4] + 128 > n4) {
                            n4 = this.ai[4] + 128;
                        }
                        n3 = this.Ih + this.ec & 0x7FF;
                        this.a(0, 600 + n4 * 3, n4, this.se, this.a(this.Ac, client.Bg.lb, true, client.Bg.kb) - 50, n3, this.te);
                    }
                    if (this.jh) break block18;
                    n4 = this.J(111);
                    if (n == 0) break block19;
                }
                n4 = this.K(this.If);
            }
            n3 = this.sb;
            int n5 = this.tb;
            int n6 = this.ub;
            int n7 = this.vb;
            int n8 = this.wb;
            int n9 = 0;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n == 0) continue;
                if (this.Kb[n9]) {
                    n2 = (int)(Math.random() * (double)(this.Hb[n9] * 2 + 1) - (double)this.Hb[n9] + Math.sin((double)this.Ie[n9] * ((double)this.Kc[n9] / 100.0)) * (double)this.ai[n9]);
                    if (n9 == 0) {
                        this.sb += n2;
                    }
                    if (n9 == 1) {
                        this.tb += n2;
                    }
                    if (n9 == 2) {
                        this.ub += n2;
                    }
                    if (n9 == 3) {
                        this.wb = this.wb + n2 & 0x7FF;
                    }
                    if (n9 == 4) {
                        this.vb += n2;
                        if (this.vb < 128) {
                            this.vb = 128;
                        }
                        if (this.vb > 383) {
                            this.vb = 383;
                        }
                    }
                }
                ++n9;
            } while (n9 < 5);
            n2 = OPPOFIOL.U;
            ZKARKDQW.Eb = true;
            if (by != 1) {
                return;
            }
            ZKARKDQW.Hb = 0;
            ZKARKDQW.Fb = this.t - 4;
            ZKARKDQW.Gb = this.u - 4;
            AFCKELYG.a(this.di);
            this.cd.a(this.sb, this.ub, this.wb, this.tb, n4, this.vb, false);
            this.cd.a((byte)104);
            this.m(this.gc);
            this.t(-252);
            this.d(854, n2);
            this.H(8);
            this.oh.a(4, 23680, this.l, 4);
            this.sb = n3;
            this.tb = n5;
            this.ub = n6;
            this.vb = n7;
            this.wb = n8;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("97263, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void M(int n) {
        try {
            this.Ph.a((byte)6, 130);
            if (this.Mh != -1) {
                this.Mh = -1;
                this.ch = true;
                this.Yg = false;
                this.eg = true;
            }
            if (this.vj != -1) {
                this.vj = -1;
                this.ui = true;
                this.Yg = false;
            }
            this.rb = -1;
            if (n <= 0) {
                this.Ph.a(13);
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("33125, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public client() {
        int n = Jj;
        this.L = new int[104][104];
        this.M = new int[200];
        this.N = new LHGXPZPG[4][104][104];
        this.Q = true;
        this.R = false;
        this.U = new MBMGIXGO(new byte[5000], 891);
        this.V = new CWNCPMLX[16384];
        this.X = new int[16384];
        this.Y = 9;
        this.ab = new int[1000];
        this.hb = MBMGIXGO.a(1, 9);
        this.ib = true;
        this.rb = -1;
        this.yb = new int[YUXCUCXD.a];
        this.Gb = false;
        this.Hb = new int[5];
        this.Ib = -1;
        this.Jb = -680;
        this.Kb = new boolean[5];
        this.Lb = 1834;
        this.Ob = false;
        this.Pb = "";
        this.Qb = -30815;
        this.Rb = 533;
        this.Sb = -1;
        this.Tb = false;
        this.Vb = "";
        this.Wb = 2048;
        this.Xb = 2047;
        this.Yb = new DLZHLHNK[this.Wb];
        this.ac = new int[this.Wb];
        this.cc = new int[this.Wb];
        this.dc = new MBMGIXGO[this.Wb];
        this.fc = 1;
        this.jc = new int[104][104];
        this.kc = 7759444;
        this.uc = new byte[16384];
        this.Cc = (byte)14;
        this.Dc = 732;
        this.Ec = new int[YUXCUCXD.a];
        this.Fc = (byte)25;
        this.Hc = new long[100];
        this.Ic = false;
        this.Jc = 3353893;
        this.Kc = new int[5];
        this.Lc = new int[104][104];
        this.Mc = new CRC32();
        this.Vc = 748;
        this.Yc = new int[100];
        this.Zc = new String[100];
        this.ad = new String[100];
        this.dd = new DSMJIEPN[13];
        this.kd = true;
        this.ld = new long[200];
        this.md = -1;
        this.sd = false;
        this.td = -1;
        this.ud = -1;
        this.vd = new int[]{0xFFFF00, 0xFF0000, 65280, 65535, 0xFF00FF, 0xFFFFFF};
        this.yd = new int[33];
        this.zd = new int[256];
        this.Ad = new IGSLDTHC[5];
        this.Bd = new int[2000];
        this.Cd = false;
        this.Dd = (byte)-74;
        this.Fd = 50;
        this.Gd = new int[this.Fd];
        this.Hd = new int[this.Fd];
        this.Id = new int[this.Fd];
        this.Jd = new int[this.Fd];
        this.Kd = new int[this.Fd];
        this.Ld = new int[this.Fd];
        this.Md = new int[this.Fd];
        this.Nd = new String[this.Fd];
        this.Pd = -1;
        this.Rd = new CXGZMTJK[20];
        this.Ud = new int[5];
        this.Vd = false;
        this.Yd = false;
        this.ge = 2301979;
        this.ie = "";
        this.qe = (byte)24;
        this.re = new LHGXPZPG(169);
        this.ve = false;
        this.we = -1;
        this.Ie = new int[5];
        this.Je = false;
        this.Le = new CXGZMTJK[100];
        this.Ue = -1;
        this.Ve = false;
        this.We = new int[YUXCUCXD.a];
        this.Xe = new int[2000];
        this.Ze = true;
        this.cf = 111;
        this.ef = new int[151];
        this.gf = -1;
        this.jf = new LHGXPZPG(169);
        this.kf = new int[33];
        this.lf = 24869;
        this.mf = new DUCMKFAY();
        this.nf = new DSMJIEPN[100];
        this.qf = 5063219;
        this.sf = new int[7];
        this.zf = new int[1000];
        this.Af = new int[1000];
        this.Hf = false;
        this.If = -733;
        this.Jf = new String[200];
        this.Kf = MBMGIXGO.a(1, 9);
        this.Rf = new int[9];
        this.Sf = new int[500];
        this.Tf = new int[500];
        this.Uf = new int[500];
        this.Vf = new int[500];
        this.Wf = new CXGZMTJK[20];
        this.eg = false;
        this.gg = 519;
        this.hg = false;
        this.rg = 445;
        this.tg = -29508;
        this.ug = -77;
        this.wg = "";
        this.Cg = new String[5];
        this.Dg = new boolean[5];
        this.Eg = new int[4][13][13];
        this.Fg = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        this.Hg = 2;
        this.Kg = -12499;
        this.Pg = new CXGZMTJK[1000];
        this.Qg = false;
        this.Yg = false;
        this.Zg = new CXGZMTJK[8];
        this.ah = true;
        this.ch = false;
        this.gh = false;
        this.hh = false;
        this.ih = false;
        this.jh = false;
        this.uh = 1;
        this.wh = "";
        this.xh = "";
        this.zh = false;
        int[] nArray = new int[23];
        nArray[4] = 1;
        nArray[5] = 1;
        nArray[6] = 1;
        nArray[7] = 1;
        nArray[8] = 1;
        nArray[9] = 2;
        nArray[10] = 2;
        nArray[11] = 2;
        nArray[12] = 2;
        nArray[13] = 2;
        nArray[14] = 2;
        nArray[15] = 2;
        nArray[16] = 2;
        nArray[17] = 2;
        nArray[18] = 2;
        nArray[19] = 2;
        nArray[20] = 2;
        nArray[21] = 2;
        nArray[22] = 3;
        this.Ah = nArray;
        this.Bh = -1;
        this.Ch = new LHGXPZPG(169);
        this.Hh = 128;
        this.Mh = -1;
        this.Ph = MBMGIXGO.a(1, 9);
        this.Rh = (byte)5;
        this.Wh = new String[500];
        this.ai = new int[5];
        this.di = true;
        this.ei = new int[50];
        this.hi = 2;
        this.ii = 78;
        this.ji = "";
        this.oi = (byte)6;
        this.pi = -589;
        this.qi = new DSMJIEPN[2];
        this.si = 3;
        this.ui = false;
        this.zi = true;
        this.Ai = new int[151];
        this.Bi = new FTPNODIB[4];
        this.Ei = false;
        this.Ki = 100;
        this.Li = new int[100];
        this.Mi = new int[50];
        this.Ni = false;
        this.Vi = new int[50];
        this.Xi = false;
        this.aj = false;
        this.bj = false;
        this.lj = "";
        this.mj = "";
        this.tj = (byte)-13;
        this.vj = -1;
        this.wj = true;
        this.yj = 2;
        this.zj = new int[4000];
        this.Aj = new int[4000];
        this.Ij = -1;
        if (n != 0) {
            PKVMXVTO.e = !PKVMXVTO.e;
        }
    }

    static {
        qb = new BigInteger("7162900525229798032761816791230527296329313291232324290237849263501208207972894053929065636522363163621000728841182238772712427862772219676577293600221789");
        Bc = true;
        nd = 10;
        pd = true;
        he = new int[][]{{6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193}, {8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239}, {25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003}, {4626, 11146, 6439, 12, 4758, 10270}, {4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574}};
        xe = new int[99];
        int n = 0;
        int n2 = 0;
        while (n2 < 99) {
            int n3 = n2 + 1;
            int n4 = (int)((double)n3 + 300.0 * Math.pow(2.0, (double)n3 / 7.0));
            client.xe[n2] = (n += n4) / 4;
            ++n2;
        }
        Ke = new BigInteger("58778699976184461502525193738213253649000149147835990136706041084440742975821");
        Xf = -192;
        lh = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\u00a3$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
        Xh = (byte)9;
        bi = new int[]{9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 58654, 5027, 1457, 16565, 34991, 25486};
        vi = true;
        Di = new int[32];
        n = 2;
        n2 = 0;
        while (n2 < 32) {
            client.Di[n2] = n - 1;
            n += n;
            ++n2;
        }
    }
}
