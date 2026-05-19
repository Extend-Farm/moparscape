/*
 * Decompiled with CFR 0.152.
 */
package sign;

import java.applet.Applet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public final class signlink
implements Runnable {
    private static final int clientversion = 317;
    public static int uid;
    public static int storeid;
    public static RandomAccessFile cache_dat;
    public static RandomAccessFile[] cache_idx;
    public static boolean sunjava;
    public static Applet mainapp;
    private static boolean active;
    private static int threadliveid;
    private static InetAddress socketip;
    private static int socketreq;
    private static Socket socket;
    private static int threadreqpri;
    private static Runnable threadreq;
    private static String dnsreq;
    public static String dns;
    private static String urlreq;
    private static DataInputStream urlstream;
    private static int savelen;
    private static String savereq;
    private static byte[] savebuf;
    private static boolean midiplay;
    private static int midipos;
    public static String midi;
    public static int midivol;
    public static int midifade;
    private static boolean waveplay;
    private static int wavepos;
    public static String wave;
    public static int wavevol;
    public static boolean reporterror;
    public static String errorname;

    public static final void startpriv(InetAddress inetAddress) {
        threadliveid = (int)(Math.random() * 9.9999999E7);
        if (active) {
            try {
                Thread.sleep(500L);
            }
            catch (Exception exception) {}
            active = false;
        }
        socketreq = 0;
        threadreq = null;
        dnsreq = null;
        savereq = null;
        urlreq = null;
        socketip = inetAddress;
        Thread thread = new Thread(new signlink());
        thread.setDaemon(true);
        thread.start();
        while (!active) {
            try {
                Thread.sleep(50L);
            }
            catch (Exception exception) {}
        }
    }

    public final void run() {
        active = true;
        String string = signlink.findcachedir();
        uid = signlink.getuid(string);
        try {
            File file = new File(String.valueOf(string) + "main_file_cache.dat");
            if (file.exists() && file.length() > 0x3200000L) {
                file.delete();
            }
            cache_dat = new RandomAccessFile(String.valueOf(string) + "main_file_cache.dat", "rw");
            int n = 0;
            while (n < 5) {
                signlink.cache_idx[n] = new RandomAccessFile(String.valueOf(string) + "main_file_cache.idx" + n, "rw");
                ++n;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        int n = threadliveid;
        while (threadliveid == n) {
            if (socketreq != 0) {
                try {
                    socket = new Socket(socketip, socketreq);
                }
                catch (Exception exception) {
                    socket = null;
                }
                socketreq = 0;
            } else if (threadreq != null) {
                Thread thread = new Thread(threadreq);
                thread.setDaemon(true);
                thread.start();
                thread.setPriority(threadreqpri);
                threadreq = null;
            } else if (dnsreq != null) {
                try {
                    dns = InetAddress.getByName(dnsreq).getHostName();
                }
                catch (Exception exception) {
                    dns = "unknown";
                }
                dnsreq = null;
            } else if (savereq != null) {
                if (savebuf != null) {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(string) + savereq);
                        fileOutputStream.write(savebuf, 0, savelen);
                        fileOutputStream.close();
                    }
                    catch (Exception exception) {}
                }
                if (waveplay) {
                    wave = String.valueOf(string) + savereq;
                    waveplay = false;
                }
                if (midiplay) {
                    midi = String.valueOf(string) + savereq;
                    midiplay = false;
                }
                savereq = null;
            } else if (urlreq != null) {
                try {
                    urlstream = new DataInputStream(new URL(mainapp.getCodeBase(), urlreq).openStream());
                }
                catch (Exception exception) {
                    urlstream = null;
                }
                urlreq = null;
            }
            try {
                Thread.sleep(50L);
            }
            catch (Exception exception) {}
        }
    }

    public static final String findcachedir() {
        String[] stringArray = new String[]{"c:/windows/", "c:/winnt/", "d:/windows/", "d:/winnt/", "e:/windows/", "e:/winnt/", "f:/windows/", "f:/winnt/", "c:/", "~/", "/tmp/", "", "c:/rscache", "/rscache"};
        if (storeid < 32 || storeid > 34) {
            storeid = 32;
        }
        String string = ".file_store_" + storeid;
        int n = 0;
        while (n < stringArray.length) {
            try {
                File file;
                String string2 = stringArray[n];
                if ((string2.length() <= 0 || (file = new File(string2)).exists()) && ((file = new File(String.valueOf(string2) + string)).exists() || file.mkdir())) {
                    return String.valueOf(string2) + string + "/";
                }
            }
            catch (Exception exception) {}
            ++n;
        }
        return null;
    }

    private static final int getuid(String string) {
        Object object;
        try {
            object = new File(String.valueOf(string) + "uid.dat");
            if (!((File)object).exists() || ((File)object).length() < 4L) {
                DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(String.valueOf(string) + "uid.dat"));
                dataOutputStream.writeInt((int)(Math.random() * 9.9999999E7));
                dataOutputStream.close();
            }
        }
        catch (Exception exception) {}
        try {
            object = new DataInputStream(new FileInputStream(String.valueOf(string) + "uid.dat"));
            int n = ((DataInputStream)object).readInt();
            ((FilterInputStream)object).close();
            return n + 1;
        }
        catch (Exception exception) {
            return 0;
        }
    }

    public static final synchronized Socket opensocket(int n) throws IOException {
        socketreq = n;
        while (socketreq != 0) {
            try {
                Thread.sleep(50L);
            }
            catch (Exception exception) {}
        }
        if (socket == null) {
            throw new IOException("could not open socket");
        }
        return socket;
    }

    public static final synchronized DataInputStream openurl(String string) throws IOException {
        urlreq = string;
        while (urlreq != null) {
            try {
                Thread.sleep(50L);
            }
            catch (Exception exception) {}
        }
        if (urlstream == null) {
            throw new IOException("could not open: " + string);
        }
        return urlstream;
    }

    public static final synchronized void dnslookup(String string) {
        dns = string;
        dnsreq = string;
    }

    public static final synchronized void startthread(Runnable runnable, int n) {
        threadreqpri = n;
        threadreq = runnable;
    }

    public static final synchronized boolean wavesave(byte[] byArray, int n) {
        if (n > 2000000) {
            return false;
        }
        if (savereq != null) {
            return false;
        }
        wavepos = (wavepos + 1) % 5;
        savelen = n;
        savebuf = byArray;
        waveplay = true;
        savereq = "sound" + wavepos + ".wav";
        return true;
    }

    public static final synchronized boolean wavereplay() {
        if (savereq != null) {
            return false;
        }
        savebuf = null;
        waveplay = true;
        savereq = "sound" + wavepos + ".wav";
        return true;
    }

    public static final synchronized void midisave(byte[] byArray, int n) {
        if (n > 2000000) {
            return;
        }
        if (savereq != null) {
            return;
        }
        midipos = (midipos + 1) % 5;
        savelen = n;
        savebuf = byArray;
        midiplay = true;
        savereq = "jingle" + midipos + ".mid";
    }

    public static final void reporterror(String string) {
        if (!reporterror) {
            return;
        }
        if (!active) {
            return;
        }
        System.out.println("Error: " + string);
        try {
            string = string.replace(':', '_');
            string = string.replace('@', '_');
            string = string.replace('&', '_');
            string = string.replace('#', '_');
            DataInputStream dataInputStream = signlink.openurl("reporterror" + 317 + ".cgi?error=" + errorname + " " + string);
            dataInputStream.readLine();
            dataInputStream.close();
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    static {
        storeid = 32;
        cache_dat = null;
        cache_idx = new RandomAccessFile[5];
        mainapp = null;
        socket = null;
        threadreqpri = 1;
        threadreq = null;
        dnsreq = null;
        dns = null;
        urlreq = null;
        urlstream = null;
        savereq = null;
        savebuf = null;
        midi = null;
        wave = null;
        reporterror = true;
        errorname = "";
    }
}

