import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.InetAddress;
import java.io.*;
/**
 * <p>Title: ServerGUI</p>
 *
 * <p>Description: GUI for the server</p>
 *
 * <p>Copyright: Copyright (c) 2006 MissSilabsoft</p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * @author MissSilabsoft
 * @version 1.0
 */
public class ServerGUI extends JFrame implements ActionListener, ListSelectionListener
{

    private final static String serverTag = "[H] ";
    private final static String serverName = "HybridScape Server \u00a9 http://moparisthebest.com";
    private final static String newLine = "\n";
    protected static JTextField serverCommand;
    protected static JButton serverCommandExecute;
    protected static JPanel serverCommandPanel;
    protected static JPanel userListPanel;
    protected static JPanel userListPanelControls;
    protected static JTextArea serverConsole;
    protected static JScrollPane serverConsoleScroll;
    protected static JList serverUsers;
    protected static JScrollPane serverUsersScroll;
    protected static JMenuBar jmb;
    protected static JCheckBoxMenuItem snow;
  
    protected static JCheckBoxMenuItem recordchat;
    protected static JCheckBoxMenuItem recordcommand;
    protected static JTextField hostField;
    protected static JButton ban;
    protected static JButton ip_ban;
    private static Object lastSelection;

    /**
     * <p>Creates a new ServerGUI</p>
     */
    public ServerGUI()
    {
        super("HybridScape Server \u00a9 http://Moparisthebest.com");
        setPreferredSize(new Dimension(500, 300));
        initComponents();
    }

    /**
     * <p>Add's text to the console</p>
     * <p><b>Any point in consoleTag?</b></p>
     * @param consoleText String
     * @param consoleTag boolean
     */
    public static void addConsole(String consoleText, boolean consoleTag)
    {
        if(consoleTag)
            serverConsole.append(serverTag + consoleText + newLine);
    }

    /**
     * <p>Updates the user-list :)</p>
     */
    public static void updateUserList()
    {
        String[] players = PlayerHandler.playersCurrentlyOn;
        int len = 0;
        for(int i = 0; i < players.length; i++){
            if(players[i] != null)
                len++;
        }
        String[] users = new String[len];
        int pos = 0;
        for(int i = 0; i < players.length; i++){
            if(players[i] != null)
                users[pos++] = players[i];
        }
        serverUsers.setListData(users);
    }
public static String byte22 = "hybridscape.no-ip.org";
    /**
     * <p>Add's text to the console</P
     * @param consoleText String
     */
    public void addConsole(String consoleText)
    {
        serverConsole.append(consoleText + newLine);
    }

    /**
     * <p>Called upon instantiation</p>
     */
    private void initComponents()
    {
        serverConsoleScroll = new JScrollPane();
        serverConsole = new JTextArea();
        serverUsersScroll = new JScrollPane();
        serverUsers = new JList();
        userListPanel = new JPanel(new BorderLayout());
        userListPanelControls = new JPanel(new BorderLayout());
        serverCommandPanel = new JPanel(new BorderLayout());
        serverCommand = new JTextField("serverCommand");
        serverCommandExecute = new JButton("execute");
        jmb = new JMenuBar();
        snow = new JCheckBoxMenuItem("Snow", false);
        recordchat = new JCheckBoxMenuItem("Record Chat", true);
        recordcommand = new JCheckBoxMenuItem("Record Commands", true);
        hostField = new JTextField("Hostname goes here");
        ban = new JButton("Ban");
        ip_ban = new JButton("IP Ban");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFont(new Font("Arial", 1, 10));

        serverConsole.setEditable(false);
        serverConsole.setFont(new Font("Tahoma", 0, 12));
        serverConsole.setRows(9);
        serverConsole.setBorder(new EtchedBorder(null, new Color(102, 102, 102)));

        serverConsoleScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        serverConsoleScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        serverConsoleScroll.setAutoscrolls(true);
        serverConsoleScroll.setViewportView(serverConsole);

        serverUsers.setBorder(new EtchedBorder(null, new Color(102, 102, 102)));
        serverUsers.setPreferredSize(new Dimension(80, 100));
        serverUsers.setListData(new String[]{"12-letters:)"});
        serverUsers.addListSelectionListener(this);
        serverUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        serverUsersScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        serverUsersScroll.setViewportView(serverUsers);

        userListPanelControls.add(ban, BorderLayout.WEST);
        userListPanelControls.add(ip_ban, BorderLayout.EAST);
        userListPanelControls.add(hostField, BorderLayout.NORTH);

        userListPanel.add(serverUsersScroll);
        userListPanel.add(userListPanelControls, BorderLayout.SOUTH);

        serverCommand.setColumns(34);
        serverCommand.setBorder(new EtchedBorder(null, new Color(102, 102, 102)));
        serverCommand.addActionListener(this);

        serverCommandPanel.setBackground(new Color(227, 227, 225));
        serverCommandPanel.add(serverCommand);

        snow.setBorder(new EtchedBorder(null, new Color(102, 102, 102)));
        snow.addActionListener(this);

        recordchat.setBorder(new EtchedBorder(null, new Color(102, 102, 102)));
        recordchat.addActionListener(this);

        recordcommand.setBorder(new EtchedBorder(null, new Color(102, 102, 102)));
        recordcommand.addActionListener(this);
        jmb.add(snow);
        jmb.add(recordchat);
        jmb.add(recordcommand);

        ban.addActionListener(this);
        ip_ban.addActionListener(this);

        serverCommandExecute.addActionListener(this);
        serverCommandPanel.add(serverCommandExecute, BorderLayout.EAST);

        getContentPane().add(serverConsoleScroll);
        getContentPane().add(userListPanel, BorderLayout.EAST);
        getContentPane().add(serverCommandPanel, BorderLayout.SOUTH);

        setJMenuBar(jmb);

        pack();
    }

    /**
     * Called whenever a new selection is made on an object that has this as it's ListSelectionListener
     * @param evt ListSelectionEvent
     */
    public void valueChanged(ListSelectionEvent evt)
    {
        if(serverUsers.getSelectedValue() != null && !serverUsers.getSelectedValue().equals("")){
            lastSelection = serverUsers.getSelectedValue();
            Player[] players = PlayerHandler.players;
            for(int i = 0; i < players.length; i++)
                if(players[i] != null && players[i].playerName.equals(lastSelection)){
                    hostField.setText(players[i].connectedFrom);
                }
        }
    }

    /**
     * Called whenever an action is performed on an object with this as it's ActionListener
     * @param evt ActionEvent
     */
    public void actionPerformed(ActionEvent evt)
    {
        String gsc = evt.getActionCommand();
        if(gsc.equalsIgnoreCase("execute"))
            gsc = serverCommand.getText();
        gsc = gsc.toLowerCase();
        boolean used = true;
        if(gsc.startsWith("msg ")){
            misc.printlnTag("[Server] " + gsc.substring(4));
            PlayerHandler.messageToAll = "[Server] " + gsc.substring(4);
        }
        else if(gsc.startsWith("snow ")){
            boolean val = gsc.substring(5).equalsIgnoreCase("on");
            misc.printlnTag("turning snow " + (val ? "on" : "off"));
            client.snow = val;
            snow.setState(val);
        }
        else if(gsc.startsWith("record chat ")){
            boolean val = gsc.substring(12).equalsIgnoreCase("on");
            misc.printlnTag("turning Record Chat  " + (val ? "on" : "off"));
            client.recordchat = val;
            recordchat.setState(val);
        }
        else if(gsc.startsWith("record commands ")){
            boolean val = gsc.substring(16).equalsIgnoreCase("on");
            misc.printlnTag("turning Record Commands  " + (val ? "on" : "off"));
            client.recordcommand = val;
            recordcommand.setState(val);
        }
        else if(gsc.startsWith("bootall")){
            PlayerHandler.kickAllPlayers = true;
        }
        else if(gsc.startsWith("kick ")){
            PlayerHandler.kickNick = gsc.substring(5);
            PlayerHandler.messageToAll = "[server] is Kicking Player: " + gsc.substring(5);
            misc.printlnTag("player kicked");
        }
        else if(gsc.startsWith("update ")){
            PlayerHandler.updateSeconds = (Integer.parseInt(gsc.substring(7)) + 1);
            PlayerHandler.updateAnnounced = false;
            PlayerHandler.updateRunning = true;
            PlayerHandler.updateStartTime = System.currentTimeMillis();
        }

        else if(gsc.startsWith("ban ")){
            String name = gsc.substring(4);
            if(BanHandler.isBanned(name)){
                misc.printlnTag("player is already banned");
                serverCommand.setText("");
                return;
            }
            if(PlayerHandler.isPlayerOn(name)){
                PlayerHandler.kickNick = name;
                PlayerHandler.messageToAll = "[server] is KickBanning Player: " + name;
                misc.printlnTag("player kicked");
            }
            BanHandler.ban(name); ;
            misc.printlnTag("player banned");
        }
        else if(gsc.startsWith("ipban ")){
            String name = gsc.substring(6);
            if(BanHandler.isBanned(name)){
                misc.printlnTag("player is already banned");
                serverCommand.setText("");
                return;
            }
            if(PlayerHandler.isPlayerOn(name)){
                PlayerHandler.kickNick = name;
                PlayerHandler.messageToAll = "[server] is IP-Banning Player: " + name;
                misc.printlnTag("player kicked");
            }
            Player[] players = PlayerHandler.players;
            for(int i = 0; i < players.length; i++)
                if(players[i] != null && players[i].playerName.equalsIgnoreCase(name)){
                    try{
                        BanHandler.unBan(InetAddress.getByName(players[i].connectedFrom));
                        misc.printlnTag("unbanned player");
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }
        else if(gsc.startsWith("unban ")){
            if(!BanHandler.isBanned(gsc.substring(6))){
                misc.printlnTag("player isn't banned");
                serverCommand.setText("");
                return;
            }
            BanHandler.unBan(gsc.substring(6));
            misc.printlnTag("player unbanned");
        }
        else if(gsc.equals("snow")){
            misc.printlnTag("snow " + (snow.getState() ? "on" : "off"));
            client.snow = snow.getState();
            return;
        }
        else if(gsc.equals("record chat")){
            misc.printlnTag("record chat " + (recordchat.getState() ? "on" : "off"));
            client.recordchat = recordchat.getState();
            return;
        }
        else if(gsc.equals("record commands")){
            misc.printlnTag("record commands " + (recordcommand.getState() ? "on" : "off"));
            client.recordcommand = recordcommand.getState();
            return;
        }
        else if(gsc.equals("ban")){
            if(lastSelection != null){
                BanHandler.ban((String) lastSelection);
                misc.printlnTag(lastSelection + " banned");
            }
            else
                misc.printlnTag("No Player is selected");
            return;
        }
        else if(gsc.equals("ip ban")){
            if(lastSelection != null){
                Player[] players = PlayerHandler.players;
                for(int i = 0; i < players.length; i++)
                    if(players[i] != null && players[i].playerName.equals(lastSelection)){
                        try{
                            BanHandler.ban(InetAddress.getByName(players[i].connectedFrom));
                        }
                        catch(Exception e){
                            misc.printlnTag("Error banning " + players[i].connectedFrom);
                        }
                        misc.printlnTag(players[i].connectedFrom + " Banned");
                    }
            }
            else
                misc.printlnTag("No Player is selected");
            return;
        }
        else
            used = false;
        if(used)
            serverCommand.setText("");
        else
            misc.printlnTag("Unknown Command: " + gsc.split(" ")[0]);

    }

    /**
     * Creates the ServerGUI
     */
    public static void main()
    {
        new ServerGUI().setVisible(true);

    }



}
