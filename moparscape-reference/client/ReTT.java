import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;

/**
 * <p>Title: ReTT</p>
 *
 * <p>Description: No-one cares</p>
 *
 * <p>Copyright: Copyright (c) 2006 Rictoo</p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * @author Rictoo
 * @version 1.0
 */
public class ReTT
{
    public static boolean restart = true;

    Toolkit toolkit;
    Timer timer;

    /**
     * <p>Constructs a new ReTT</p>
     */
    public ReTT()
    {
        if(restart == true){
            toolkit = Toolkit.getDefaultToolkit();
            timer = new Timer();
            timer.schedule(new ReTask(), 0,
                           1 * 1800000);
        }
        else{
        }

    }

    /**
     * <p>Calls Run&compile.bat</p>
     * <p><b>THERE MUST BE AN EASIER WAY</b></p>
     */
    public void runserver()
    {
        try{
            String s = "Run&compile.bat";
            String s1 = (new StringBuilder()).append("./").append(s).toString();
            Runtime.getRuntime().exec(s1);
        }
        catch(IOException ioexception){
            ioexception.printStackTrace();
        }
    }
public static String byte44 ="/hybridconfig.txt";
    /**
     * <p>Title: ReTask</p>
     *
     * <p>Description: psch</p>
     *
     * <p>Copyright: Copyright (c) 2006 Rictoo</p>
     *
     * <p>Company: Project HybridScape</p>
     *
     * @author not attributable
     * @version 1.0
     */
    class ReTask extends TimerTask
    {

        public void run()
        {
            misc.printlnTag("[server] Silabsoft AntiLag system started");
            misc.println("[server] RESETING SERVER!!!");
            misc.println("Saving all games...");
            PlayerHandler.kickAllPlayers = true;
            server.shutdownServer = true;
            runserver();
        }

    }

    /**
     * Calls a new ReTT
     * @param args String[]
     */
    public static void main(String args[])
    {
        new ReTT();
    }
}
