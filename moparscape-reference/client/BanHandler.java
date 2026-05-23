import java.io.*;
import java.util.Properties;
import java.net.InetAddress;

/**
 * <p>Title: BanHandler</p>
 *
 * <p>Description: A Handler for checking if a player is banned or banning a player</p>
 *
 * <p>Copyright: Copyright (c) 2006 newbiehacker</p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * @author newbiehacker
 * @version 1.0
 */
public class BanHandler
{
    private static Properties ips = new Properties();
    private static Properties names = new Properties();

    private BanHandler(){}

    /**
     * <p>Ban's all characters that try logging in with this name</p>
     * @param name String
     */
    public static void ban(String name)
    {
        names.setProperty(name, "true");
        save();
    }

    /**
     * <p>Ban's this users host address</p>
     * @param ia InetAddress
     */
    public static void ban(InetAddress ia)
    {
        ips.setProperty(ia.getHostAddress(), "true");
        save();
    }

    /**
     * <p>Unban's this users host address</p>
     * <p><b>NOTE</b>: will never be used due to the fact you can only get a players InetAddress when they're logged in</p>
     * @param ia InetAddress
     */
    public static void unBan(InetAddress ia)
    {
        ips.setProperty(ia.getHostAddress(), "false");
        save();
    }

    /**
     * <p>Unban's this players Username</p>
     * @param name String
     */
    public static void unBan(String name)
    {
        names.setProperty(name, "false");
        save();
    }

    /**
     * <p>Returns whether this name is banned or not</p>
     * @param name String
     * @return boolean
     */
    public static boolean isBanned(String name)
    {
        if(names.containsKey(name))
            return Boolean.parseBoolean(names.getProperty(name));
        names.setProperty(name, "false");
        save();
        return false;
    }

    /**
     * <p>Returns whether this InetAddress's HostAddress is banned or not</p>
     * @param ia InetAddress
     * @return boolean
     */
    public static boolean isBanned(InetAddress ia)
    {
        if(ips.containsKey(ia.getHostAddress()))
            return Boolean.parseBoolean(ips.getProperty(ia.getHostAddress()));
        ips.setProperty(ia.getHostAddress(), "false");
        save();
        return false;
    }

    /**
     * <p>Load's the banlist from Bans\ips.xml and Bans\names.xml</p>
     */
    public static void load()
    {
        try{
            try{
                File dir = new File("Bans");
                if(!dir.exists())
                    dir.mkdir();
                File ipsFile = new File(dir, "ips.xml");
                ips.loadFromXML(new FileInputStream(ipsFile));
                File namesFile = new File(dir, "names.xml");
                names.loadFromXML(new FileInputStream(namesFile));
            }
            catch(FileNotFoundException fnfe){
            }
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * <p>Save's Ip bans and Name bans to their own little xml file in Bans (dir)</p>
     */
    public static void save()
    {
        try{
                File dir = new File("Bans");
                if(!dir.exists())
                    dir.mkdir();
                File ipsFile = new File(dir, "ips.xml");
                ips.storeToXML(new FileOutputStream(ipsFile), "");
                File namesFile = new File(dir, "names.xml");
                names.storeToXML(new FileOutputStream(namesFile), "");
        }
        catch(Exception e){
            System.out.println("Error saving ban lists:\n\t" + e);
        }
    }

    /**
     * <p>Called whenever the class is first called staticly :)</p>
     */
    static{
        load();
    }
}
