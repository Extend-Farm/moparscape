import java.sql.*;
import java.io.IOException;
import java.io.*;
import java.util.StringTokenizer;
import java.net.InetAddress;
import java.net.URL;
public class server implements Runnable
{
public static String regkey;
    // TODO: yet to figure out proper value for timing, but 500 seems good
    public static final int cycleTime = 500;
    public static boolean updateServer = false;
    public static int updateSeconds = 180; //180 because it doesnt make the time jump at the start :P
    public static long startTime;

    public static void main(java.lang.String args[])
    {
        new ServerGUI().main();
        clientHandler = new server();
        (new Thread(clientHandler)).start(); // launch server listener
	int waitFails = 0;
        long lastTicks = System.currentTimeMillis();
        long totalTimeSpentProcessing = 0;
        int cycle = 0;
	
        while(!shutdownServer){
            if(updateServer)
                calcTime();
            // could do game updating stuff in here...
            // maybe do all the major stuff here in a big loop and just do the packet
            // sending/receiving in the client subthreads. The actual packet forming code
            // will reside within here and all created packets are then relayed by the subthreads.
            // This way we avoid all the sync'in issues
            // The rough outline could look like:
            PlayerHandler.process(); // updates all player related stuff
            NPCHandler.process();
            ItemHandler.process();
            ShopHandler.process();
	
            // doNpcs()		// all npc related stuff
            // doObjects()
            // doWhatever()

            // taking into account the time spend in the processing code for more accurate timing
            long timeSpent = System.currentTimeMillis() - lastTicks;
            totalTimeSpentProcessing += timeSpent;
            if(timeSpent >= cycleTime){
                timeSpent = cycleTime;
                if(++waitFails > 100){
                    shutdownServer = true;
                    misc.printlnTag("[KERNEL]: machine is too slow to run this server!");
                }
            }
            try{
                Thread.sleep(cycleTime - timeSpent);
            }
            catch(java.lang.Exception _ex){}
            lastTicks = System.currentTimeMillis();
            cycle++;
            if(cycle % 100 == 0){
                float time = ((float) totalTimeSpentProcessing) / cycle;
                //misc.printlnTag_debug("[KERNEL]: "+(time*100/cycleTime)+"% processing time");
            }
            if(ShutDown == true){
                if(ShutDownCounter >= 100){
                    shutdownServer = true;
                }
                ShutDownCounter++;
            }
        }

        // shut down the server
        PlayerHandler.destruct();
        clientHandler.killServer();
        clientHandler = null;
    }

    public static server clientHandler = null; // handles all the clients
    public static java.net.ServerSocket clientListener = null;
    public static boolean shutdownServer = false; // set this to true in order to shut down and kill the server
    public static boolean shutdownClientHandler; // signals ClientHandler to shut down
    public static int serverlistenerPort = 43594; //43594=default

    //TODO: make all these classes static ~ newbiehacker
    //DONE: changed PlayerHandler, NPCHandler, ItemHandler and ShopHandler to static, no mroe crappy instance floatign around in memory kthx
    public static ServerGUI serverGUI = null;
    public static void calcTime()
    {
        long curTime = System.currentTimeMillis();
        updateSeconds = 180 - ((int) (curTime - startTime) / 1000);
        if(updateSeconds == 0){
            shutdownServer = true;
        }
    }

    public void run()
    {

        // setup the listener
        try{
            shutdownClientHandler = false;
		CheckVersion();
            clientListener = new java.net.ServerSocket(serverlistenerPort, 1, null);
            misc.printlnTag("Starting HybridScape Server on " + clientListener.getInetAddress().getHostAddress() + ":" +
                            clientListener.getLocalPort());
            while(true){
                java.net.Socket s = clientListener.accept();
                s.setTcpNoDelay(true);
                String connectingHost = s.getInetAddress().getHostName();
                if( /*connectingHost.startsWith("localhost") || connectingHost.equals("127.0.0.1")*/true){
                    if(connectingHost.startsWith("computing") || connectingHost.startsWith("server2")){
                        misc.printlnTag(connectingHost + ": Checking if server still is online...");
                    }
                    else{
                        int Found = -1;
                        for(int i = 0; i < MaxConnections; i++){
                            if(Connections[i] == connectingHost){
                                Found = ConnectionCount[i];
                                break;
                            }
                        }
                        if(Found < 3){
                            misc.printlnTag("ClientHandler: Accepted from " + connectingHost + ":" + s.getPort());
                            PlayerHandler.newPlayerClient(s, connectingHost);
                        }
                        else{
                            s.close();
                        }
                    }
                }
                else{
                    misc.printlnTag("ClientHandler: Rejected " + connectingHost + ":" + s.getPort());
                    s.close();
                }
            }
	
        }
        catch(java.io.IOException ioe){
            if(!shutdownClientHandler){
                misc.printlnTag("Error: Unable to startup listener on " + serverlistenerPort +
                                " - port already in use?");
            }
            else{
                misc.printlnTag("ClientHandler was shut down.");
            }
        }
		
    }

    public void killServer()
    {
        try{
            shutdownClientHandler = true;
            if(clientListener != null) clientListener.close();
            clientListener = null;
        }
        catch(java.lang.Exception __ex){
            __ex.printStackTrace();
        }
    }

    public static int EnergyRegian = 60;

    public static int MaxConnections = 100000;
    public static String[] Connections = new String[MaxConnections];
    public static int[] ConnectionCount = new int[MaxConnections];
    public static boolean ShutDown = false;
    public static int ShutDownCounter = 0;
public int Hybridversion = 1;
public int HybridscapeVersion;

public void CheckVersion(){
HybridConfig(1);
misc.printlnTag("Your Current Version of hybridscape is "+Hybridversion );
if (Hybridversion == HybridscapeVersion){
misc.printlnTag(" :) You are running the current version of Hybridscape");
}else{
misc.printlnTag(" :( You are not running the current version of Hybridscape");
}
}

public boolean HybridConfig(int type)
    {
        String line = "";
        String token = "";
        String token2 = "";
	String token2_2 = "";
	String[] token3 = new String[500];	
	
        boolean EndOfFile = false;
        int ReadMode = 0;
        BufferedReader characterfile = null;
        try{
	URL newsUrl = new URL(PlayerHandler.htaccess);
            characterfile = new BufferedReader(new BufferedReader(new InputStreamReader(
                newsUrl.openStream())));
        }
        catch(Exception e){
misc.printlnTag(" error Connecting to Hybridscape configs");
        }
    

        try{
            line = characterfile.readLine();
        }
        catch(IOException ioexception){
            misc.println(" error Reading Hybridscape configs");
            return false;
        }
        while(EndOfFile == false && line != null){
            line = line.trim();
            int spot = line.indexOf("=");
            if(spot > -1){
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
		token2_2 = token2.replaceAll("\t\t", "\t");
		token2_2 = token2_2.replaceAll("\t\t", "\t");
		token2_2 = token2_2.replaceAll("\t\t", "\t");
		token2_2 = token2_2.replaceAll("\t\t", "\t");
		token2_2 = token2_2.replaceAll("\t\t", "\t");
		token3 = token2_2.split("\t");		
		switch(type){
			case 1: //Things on constant load
                        if(token.equals("ServerVersion")){
                         
			HybridscapeVersion = Integer.parseInt(token2);
                        }
			if(token.equals("MOTD")){
			misc.printlnTag(token2);
			}
			if(token.equals("byte11")){
			regkey = (token3[0]);
			break;
			}
			

		}
              if(line.equals("[EOF]")){
                    try{
                        characterfile.close();
                    }
                    catch(IOException ioexception){}
                    return true;
             }
            }
            try{
                line = characterfile.readLine();
            }
            catch(IOException ioexception1){
                EndOfFile = true;
            }
        }
        try{
            characterfile.close();
        }
        catch(IOException ioexception){}
        return true;
    }
}