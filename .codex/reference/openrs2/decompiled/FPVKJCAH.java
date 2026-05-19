/*
 * Decompiled with CFR 0.152.
 */
import java.awt.Frame;
import java.awt.Graphics;
import sign.signlink;

public final class FPVKJCAH
extends Frame {
    private boolean a = true;
    KHACHIFW b;

    public FPVKJCAH(KHACHIFW kHACHIFW, int n, byte by, int n2) {
        try {
            this.b = kHACHIFW;
            this.setTitle("Jagex");
            this.setResizable(false);
            this.show();
            if (by != 5) {
                this.a = !this.a;
            }
            this.toFront();
            this.resize(n + 8, n2 + 28);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("41013, " + kHACHIFW + ", " + n + ", " + by + ", " + n2 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public Graphics getGraphics() {
        Graphics graphics = super.getGraphics();
        graphics.translate(4, 24);
        return graphics;
    }

    public final void update(Graphics graphics) {
        this.b.update(graphics);
    }

    public final void paint(Graphics graphics) {
        this.b.paint(graphics);
    }
}

