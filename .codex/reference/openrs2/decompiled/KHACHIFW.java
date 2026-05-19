/*
 * Decompiled with CFR 0.152.
 */
import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import sign.signlink;

public class KHACHIFW
extends Applet
implements Runnable,
MouseListener,
MouseMotionListener,
KeyListener,
FocusListener,
WindowListener {
    private boolean a = true;
    private int b = 24869;
    private int c = 748;
    private int d;
    private int e = 20;
    public int f = 1;
    private long[] g = new long[10];
    public int h;
    public boolean i = false;
    public int j;
    public int k;
    public Graphics l;
    public IVIFZQBK m;
    public CXGZMTJK[] n = new CXGZMTJK[6];
    public FPVKJCAH o;
    public boolean p = true;
    public boolean q = true;
    public int r;
    public int s;
    public int t;
    public int u;
    public int v;
    public int w;
    public int x;
    public long y;
    public int z;
    public int A;
    public int B;
    public long C;
    public int[] D = new int[128];
    private int[] E = new int[128];
    private int F;
    private int G;
    public static int H;

    public final void a(int n, boolean bl, int n2) {
        try {
            this.j = n2;
            this.k = n;
            if (bl) {
                return;
            }
            this.o = new FPVKJCAH(this, this.j, 5, this.k);
            this.l = this.f(0).getGraphics();
            this.m = new IVIFZQBK(this.j, this.k, this.f(0), 0);
            this.a(this, 1);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("61841, " + n + ", " + bl + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void b(int n, boolean bl, int n2) {
        try {
            this.j = n2;
            this.k = n;
            this.l = this.f(0).getGraphics();
            this.m = new IVIFZQBK(this.j, this.k, this.f(0), 0);
            this.a(this, 1);
            if (bl) {
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("44058, " + n + ", " + bl + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Exception decompiling
     */
    public void run() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[CATCHBLOCK]], but top level block is 5[DOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public final void a(int n) {
        block7: {
            try {
                this.d = -2;
                this.d(493);
                if (n != 4747) {
                    return;
                }
                if (this.o == null) break block7;
                try {
                    Thread.sleep(1000L);
                }
                catch (Exception exception) {}
                try {
                    System.exit(0);
                    return;
                }
                catch (Throwable throwable) {
                    return;
                }
            }
            catch (RuntimeException runtimeException) {
                signlink.reporterror("13735, " + n + ", " + runtimeException.toString());
                throw new RuntimeException();
            }
        }
    }

    public final void a(boolean bl, int n) {
        try {
            if (bl) {
                return;
            }
            this.e = 1000 / n;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("88292, " + bl + ", " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void start() {
        if (this.d >= 0) {
            this.d = 0;
        }
    }

    public final void stop() {
        if (this.d >= 0) {
            this.d = 4000 / this.e;
        }
    }

    public final void destroy() {
        this.d = -1;
        try {
            Thread.sleep(5000L);
        }
        catch (Exception exception) {}
        if (this.d == -1) {
            this.a(4747);
        }
    }

    public final void update(Graphics graphics) {
        if (this.l == null) {
            this.l = graphics;
        }
        this.p = true;
        this.a((byte)1);
    }

    public final void paint(Graphics graphics) {
        if (this.l == null) {
            this.l = graphics;
        }
        this.p = true;
        this.a((byte)1);
    }

    public final void mousePressed(MouseEvent mouseEvent) {
        int n = mouseEvent.getX();
        int n2 = mouseEvent.getY();
        if (this.o != null) {
            n -= 4;
            n2 -= 22;
        }
        this.r = 0;
        this.w = n;
        this.x = n2;
        this.y = System.currentTimeMillis();
        if (mouseEvent.isMetaDown()) {
            this.v = 2;
            this.s = 2;
            return;
        }
        this.v = 1;
        this.s = 1;
    }

    public final void mouseReleased(MouseEvent mouseEvent) {
        this.r = 0;
        this.s = 0;
    }

    public final void mouseClicked(MouseEvent mouseEvent) {
    }

    public final void mouseEntered(MouseEvent mouseEvent) {
    }

    public final void mouseExited(MouseEvent mouseEvent) {
        this.r = 0;
        this.t = -1;
        this.u = -1;
    }

    public final void mouseDragged(MouseEvent mouseEvent) {
        int n = mouseEvent.getX();
        int n2 = mouseEvent.getY();
        if (this.o != null) {
            n -= 4;
            n2 -= 22;
        }
        this.r = 0;
        this.t = n;
        this.u = n2;
    }

    public final void mouseMoved(MouseEvent mouseEvent) {
        int n = mouseEvent.getX();
        int n2 = mouseEvent.getY();
        if (this.o != null) {
            n -= 4;
            n2 -= 22;
        }
        this.r = 0;
        this.t = n;
        this.u = n2;
    }

    public final void keyPressed(KeyEvent keyEvent) {
        int n = H;
        this.r = 0;
        int n2 = keyEvent.getKeyCode();
        int n3 = keyEvent.getKeyChar();
        if (n3 < 30) {
            n3 = 0;
        }
        if (n2 == 37) {
            n3 = 1;
        }
        if (n2 == 39) {
            n3 = 2;
        }
        if (n2 == 38) {
            n3 = 3;
        }
        if (n2 == 40) {
            n3 = 4;
        }
        if (n2 == 17) {
            n3 = 5;
        }
        if (n2 == 8) {
            n3 = 8;
        }
        if (n2 == 127) {
            n3 = 8;
        }
        if (n2 == 9) {
            n3 = 9;
        }
        if (n2 == 10) {
            n3 = 10;
        }
        if (n2 >= 112 && n2 <= 123) {
            n3 = 1008 + n2 - 112;
        }
        if (n2 == 36) {
            n3 = 1000;
        }
        if (n2 == 35) {
            n3 = 1001;
        }
        if (n2 == 33) {
            n3 = 1002;
        }
        if (n2 == 34) {
            n3 = 1003;
        }
        if (n3 > 0 && n3 < 128) {
            this.D[n3] = 1;
        }
        if (n3 > 4) {
            this.E[this.G] = n3;
            this.G = this.G + 1 & 0x7F;
        }
        if (PKVMXVTO.e) {
            H = ++n;
        }
    }

    public final void keyReleased(KeyEvent keyEvent) {
        this.r = 0;
        int n = keyEvent.getKeyCode();
        int n2 = keyEvent.getKeyChar();
        if (n2 < 30) {
            n2 = 0;
        }
        if (n == 37) {
            n2 = 1;
        }
        if (n == 39) {
            n2 = 2;
        }
        if (n == 38) {
            n2 = 3;
        }
        if (n == 40) {
            n2 = 4;
        }
        if (n == 17) {
            n2 = 5;
        }
        if (n == 8) {
            n2 = 8;
        }
        if (n == 127) {
            n2 = 8;
        }
        if (n == 9) {
            n2 = 9;
        }
        if (n == 10) {
            n2 = 10;
        }
        if (n2 > 0 && n2 < 128) {
            this.D[n2] = 0;
        }
    }

    public final void keyTyped(KeyEvent keyEvent) {
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final int b(int n) {
        int n2 = H;
        try {
            int n3;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && n2 == 0) continue;
                n3 = 1;
                boolean bl2 = true;
                do {
                    if (bl2 && !(bl2 = false) && n2 == 0) continue;
                    ++n3;
                } while (n3 > 0);
            } while (n >= 0);
            n3 = -1;
            if (this.G != this.F) {
                n3 = this.E[this.F];
                this.F = this.F + 1 & 0x7F;
            }
            return n3;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("66235, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public final void focusGained(FocusEvent focusEvent) {
        this.q = true;
        this.p = true;
        this.a((byte)1);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     */
    public final void focusLost(FocusEvent focusEvent) {
        this.q = false;
        int n = 0;
        boolean bl = true;
        do {
            if (bl && !(bl = false) && H == 0) continue;
            this.D[n] = 0;
            ++n;
        } while (n < 128);
    }

    public final void windowActivated(WindowEvent windowEvent) {
    }

    public final void windowClosed(WindowEvent windowEvent) {
    }

    public final void windowClosing(WindowEvent windowEvent) {
        this.destroy();
    }

    public final void windowDeactivated(WindowEvent windowEvent) {
    }

    public final void windowDeiconified(WindowEvent windowEvent) {
    }

    public final void windowIconified(WindowEvent windowEvent) {
    }

    public final void windowOpened(WindowEvent windowEvent) {
    }

    public void a() {
    }

    public void c(int n) {
        try {
            if (n != this.b) {
                return;
            }
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("38427, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void d(int n) {
        try {
            n = 91 / n;
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("49106, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void e(int n) {
        try {
            if (n == 0) return;
            int n2 = 1;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && H == 0) continue;
                ++n2;
            } while (n2 > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("62415, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(byte by) {
        try {
            if (by == 1) {
                by = 0;
                return;
            }
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("6639, " + by + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Component f(int n) {
        try {
            if (n != 0) {
                int n2 = 1;
                boolean bl = true;
                do {
                    if (bl && !(bl = false) && H == 0) continue;
                    ++n2;
                } while (n2 > 0);
            }
            if (this.o != null) {
                return this.o;
            }
            return this;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("82353, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(Runnable runnable, int n) {
        Thread thread = new Thread(runnable);
        thread.start();
        thread.setPriority(n);
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n, byte by, String string) {
        int n2 = H;
        try {
            boolean bl = true;
            do {
                if (bl && !(bl = false)) {
                    if (n2 == 0) continue;
                    PKVMXVTO.e = !PKVMXVTO.e;
                }
                this.l = this.f(0).getGraphics();
                try {
                    this.f(0).repaint();
                }
                catch (Exception exception) {}
                try {
                    Thread.sleep(1000L);
                }
                catch (Exception exception) {}
            } while (this.l == null);
            Font font = new Font("Helvetica", 1, 13);
            FontMetrics fontMetrics = this.f(0).getFontMetrics(font);
            Font font2 = new Font("Helvetica", 0, 13);
            this.f(0).getFontMetrics(font2);
            if (this.p) {
                this.l.setColor(Color.black);
                this.l.fillRect(0, 0, this.j, this.k);
                this.p = false;
            }
            Color color = new Color(140, 17, 17);
            int n3 = this.k / 2 - 18;
            this.l.setColor(color);
            this.l.drawRect(this.j / 2 - 152, n3, 304, 34);
            this.l.fillRect(this.j / 2 - 150, n3 + 2, n * 3, 30);
            this.l.setColor(Color.black);
            if (by != 4) {
                return;
            }
            this.l.fillRect(this.j / 2 - 150 + n * 3, n3 + 2, 300 - n * 3, 30);
            this.l.setFont(font);
            this.l.setColor(Color.white);
            this.l.drawString(string, (this.j - fontMetrics.stringWidth(string)) / 2, n3 + 22);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("49633, " + n + ", " + by + ", " + string + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }
}

