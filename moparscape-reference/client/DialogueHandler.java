import java.io.*;

/**
 * <p>Title: DialogueHandler</p>
 *
 * <p>Description: Handles Dialogues</p>
 *
 * <p>Copyright: Copyright (c) 2006 <b>&lt;unknown author&gt;</b></p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DialogueHandler
{
    public static int maxDialogues = 1000;
    public static int[] DialogueType = new int[maxDialogues];
    public static int[] DialogueNextDia = new int[maxDialogues];
    public static int[][] DialogueNextOption = new int[maxDialogues][4];
    public static int[][] DialogueSword = new int[maxDialogues][3];
    public static String[][] DialogueText = new String[maxDialogues][4];
    public static String[] DialogueQuestion = new String[maxDialogues];

    private DialogueHandler(){}
    /**
     * <p>Loads all stuff that needs to be loaded</p>
     */
    static
    {
        for(int i = 0; i < maxDialogues; i++){
            DialogueType[i] = -1;
            DialogueNextDia[i] = -1;
            DialogueNextOption[i][0] = 0;
            DialogueNextOption[i][1] = 0;
            DialogueNextOption[i][2] = 0;
            DialogueNextOption[i][3] = 0;
            DialogueSword[i][0] = -1;
            DialogueSword[i][1] = -1;
            DialogueSword[i][2] = -1;
            DialogueText[i][0] = "";
            DialogueText[i][1] = "";
            DialogueText[i][2] = "";
            DialogueText[i][3] = "";
            DialogueQuestion[i] = "";
        }
        loadDialogues("dialogues.cfg");
    }

    /**
     * <p>Creates a new Dialogue</p>
     * @param ID int
     * @param Type int
     * @param NextDia int
     * @param Text1 String
     * @param Text2 String
     * @param Text3 String
     */
    public static void NewDialogue(int ID, int Type, int NextDia, String Text1, String Text2, String Text3)
    {
        Text1 = Text1.replaceAll("EMPTY", "");
        Text1 = Text1.replaceAll("_", " ");
        Text2 = Text2.replaceAll("EMPTY", "");
        Text2 = Text2.replaceAll("_", " ");
        Text3 = Text3.replaceAll("EMPTY", "");
        Text3 = Text3.replaceAll("_", " ");
        DialogueType[ID] = Type;
        DialogueNextDia[ID] = NextDia;
        DialogueText[ID][1] = Text1;
        DialogueText[ID][2] = Text2;
        DialogueText[ID][3] = Text3;
    }

    /**
     * <p>Creates a new dialogue with options for the user</p>
     * @param ID int
     * @param Type int
     * @param NextOption1 int
     * @param NextOption2 int
     * @param NextOption3 int
     * @param SWClose int
     * @param SWFar int
     * @param Question String
     * @param Option1 String
     * @param Option2 String
     * @param Option3 String
     */
    public static void NewDialogue(int ID, int Type, int NextOption1, int NextOption2, int NextOption3, int SWClose,
                                   int SWFar, String Question, String Option1, String Option2, String Option3)
    {
        Option1 = Option1.replaceAll("EMPTY", "");
        Option1 = Option1.replaceAll("_", " ");
        Option2 = Option2.replaceAll("EMPTY", "");
        Option2 = Option2.replaceAll("_", " ");
        Option3 = Option3.replaceAll("EMPTY", "");
        Option3 = Option3.replaceAll("_", " ");
        Question = Question.replaceAll("EMPTY", "");
        Question = Question.replaceAll("_", " ");
        misc.println("T" + ID + ":" + Type);
        DialogueType[ID] = Type;
        DialogueNextOption[ID][1] = NextOption1;
        DialogueNextOption[ID][2] = NextOption2;
        DialogueNextOption[ID][3] = NextOption3;
        DialogueText[ID][1] = Option1;
        DialogueText[ID][2] = Option2;
        DialogueText[ID][3] = Option3;
        DialogueQuestion[ID] = Question;
        DialogueSword[ID][1] = SWClose;
        DialogueSword[ID][2] = SWFar;

    }

    /**
     * <p>Loads all the dialogues from file</p>
     * @param FileName String
     * @return boolean
     */
    public static boolean loadDialogues(String FileName)
    {
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        boolean EndOfFile = false;
        int ReadMode = 0;
        BufferedReader characterfile = null;
        try{
            characterfile = new BufferedReader(new FileReader("./" + FileName));
        }
        catch(FileNotFoundException fileex){
            misc.println(FileName + ": file not found.");
            return false;
        }
        try{
            line = characterfile.readLine();
        }
        catch(IOException ioexception){
            misc.println(FileName + ": error loading file.");
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
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                if(token.equals("dialogue")){
                    if(Integer.parseInt(token3[1]) == 1 || Integer.parseInt(token3[1]) == 3){
                        NewDialogue(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]),
                                    token3[3], token3[4], token3[5]);
                    }
                    else if(Integer.parseInt(token3[1]) == 2){
                        NewDialogue(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]),
                                    Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), Integer.parseInt(token3[5]),
                                    Integer.parseInt(token3[6]), token3[7], token3[8], token3[9], token3[10]);
                    }
                }
            }
            else{
                if(line.equals("[ENDOFDIALOGUELIST]")){
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
        return false;
    }

    /**
     * <p>Placed in most classes for debug purposes</p>
     * @param str String
     */
    public static void println(String str)
    {
        System.out.println(str);
    }
}
