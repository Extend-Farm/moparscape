/*
 * Decompiled with CFR 0.152.
 */
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import sign.signlink;

public final class IVIFZQBK
implements ImageProducer,
ImageObserver {
    private int a;
    private boolean b = true;
    public int[] c;
    public int d;
    public int e;
    ColorModel f;
    ImageConsumer g;
    public Image h;

    public IVIFZQBK(int n, int n2, Component component, int n3) {
        try {
            this.d = n;
            this.e = n2;
            this.c = new int[n * n2];
            this.f = new DirectColorModel(32, 0xFF0000, 65280, 255);
            this.h = component.createImage(this);
            this.a();
            component.prepareImage(this.h, this);
            if (n3 != 0) {
                this.b = !this.b;
            }
            this.a();
            component.prepareImage(this.h, this);
            this.a();
            component.prepareImage(this.h, this);
            this.a(0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("55701, " + n + ", " + n2 + ", " + component + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    /*
     * Handled impossible loop by adding 'first' condition
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(int n) {
        try {
            AFCKELYG.a(this.e, this.d, -293, this.c);
            if (n == 0) return;
            int n2 = 1;
            boolean bl = true;
            do {
                if (bl && !(bl = false) && !AFCKELYG.w) continue;
                ++n2;
            } while (n2 > 0);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("61030, " + n + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public void a(int n, int n2, Graphics graphics, int n3) {
        try {
            this.a();
            if (n2 != 23680) {
                this.a = -169;
            }
            graphics.drawImage(this.h, n3, n, this);
            return;
        }
        catch (RuntimeException runtimeException) {
            signlink.reporterror("99028, " + n + ", " + n2 + ", " + graphics + ", " + n3 + ", " + runtimeException.toString());
            throw new RuntimeException();
        }
    }

    public synchronized void addConsumer(ImageConsumer imageConsumer) {
        this.g = imageConsumer;
        imageConsumer.setDimensions(this.d, this.e);
        imageConsumer.setProperties(null);
        imageConsumer.setColorModel(this.f);
        imageConsumer.setHints(14);
    }

    public synchronized boolean isConsumer(ImageConsumer imageConsumer) {
        return this.g == imageConsumer;
    }

    public synchronized void removeConsumer(ImageConsumer imageConsumer) {
        if (this.g == imageConsumer) {
            this.g = null;
        }
    }

    public void startProduction(ImageConsumer imageConsumer) {
        this.addConsumer(imageConsumer);
    }

    public void requestTopDownLeftRightResend(ImageConsumer imageConsumer) {
        System.out.println("TDLR");
    }

    public synchronized void a() {
        if (this.g == null) {
            return;
        }
        this.g.setPixels(0, 0, this.d, this.e, this.f, this.c, 0, this.d);
        this.g.imageComplete(2);
    }

    public boolean imageUpdate(Image image, int n, int n2, int n3, int n4, int n5) {
        return true;
    }
}

