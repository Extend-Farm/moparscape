/**
 * <p>Title: misc</p>
 *
 * <p>Description: Holds all methods that we didn't know where to put</p>
 *
 * <p>Copyright: Copyright (c) 2006 winterlove</p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * @author winterlove
 * @version 1.0
 */
public class misc
{
    private misc()
    {}

    /**
     * <p>Changes the first letter in a word to Uppercase and all the other letter to LowerCase</p>
     * @param sentence String
     * @return String
     */
    public static String fixCase(String sentence)
    {
        String firstLetter = sentence.substring(0, 1);
        String wordRest = sentence.substring(1);
        return firstLetter.toUpperCase() + wordRest.toLowerCase();
    }

    /**
     * <p>Print's a String to the console</p>
     * @param str String
     */
    public static void println(String str)
    {
        System.out.println(str);
    }

    /**
     * <p>Print's a String to the ServerGUI's console window</p>
     * @param str String
     */
    public static void printlnTag(String str)
    {
        ServerGUI.addConsole(str, true);
        javax.swing.JScrollBar jsb = ServerGUI.serverConsoleScroll.getVerticalScrollBar();
        jsb.setValue(jsb.getMaximum());
    }

    /**
     * <p>Print's a String to rhe console</p>
     * @param str String
     */
    public static void println_debug(String str)
    {
        System.out.println(str);
    }

    /**
     * <p>Returns a Hex String for the specified byte[]</p>
     * @param data byte[]
     * @return String
     */
    public static String Hex(byte data[])
    {
        return Hex(data, 0, data.length);
    }

    /**
     * <p>Returns a Hex String for the specified byte[]</p>
     * <p>Starting at the specified offset, and ending after it has the length it needs.</P>
     * @param data byte[]
     * @param offset int
     * @param len int
     * @return String
     */
    public static String Hex(byte data[], int offset, int len)
    {
        String temp = "";
        for(int cntr = 0; cntr < len; cntr++){
            int num = data[offset + cntr] & 0xFF;
            String myStr;
            if(num < 16) myStr = "0";
            else myStr = "";
            temp += myStr + Integer.toHexString(num) + " ";
        }
        return temp.toUpperCase().trim();
    }

    /**
     * <p>Returns an int representation of the Hex String taken from data</p>
     * @param data byte[]
     * @param offset int
     * @param len int
     * @return int
     */
    public static int HexToInt(byte data[], int offset, int len)
    {
        int temp = 0;
        int i = 1000;
        for(int cntr = 0; cntr < len; cntr++){
            int num = (data[offset + cntr] & 0xFF) * i;
            temp += (int) num;
            if(i > 1)
                i = i / 1000;
        }
        return temp;
    }

    /**
     * <p>Returns a random int between 0 and range</p>
     * @param range int
     * @return int
     */
    public static int random(int range)
    {
        return(int) (java.lang.Math.random() * (range + 1));
    }

    /**
     * <p>Returns a random int between 0 and range,</p>
     * <p>may (randomly) return a negative int</p>
     * @param range int
     * @return int
     */
    public static int randomNeg(int range)
    {
        int negOrPos = (int) (java.lang.Math.random() * (2));
        if(negOrPos == 1)
            return(int) (java.lang.Math.random() * (range + 1));
        else
            return(int) (java.lang.Math.random() * ((range * -1) - 1));
    }

    /**
     * <p>Can return anything between 1 and range</p>
     * @param range int
     * @return int
     */
    public static int random2(int range)
    { //1 till range
        return(int) ((java.lang.Math.random() * range) + 1);
    }

    /**
     *
     * @param range int
     * @return int
     */
    public static int random3(int range)
    { //0 till range
        return(int) (java.lang.Math.random() * range);
    }

    /**
     * <p>Same as random :s</p>
     * @param range int
     * @return int
     */
    public static int random4(int range)
    { //0 till range (range INCLUDED)
        return(int) (java.lang.Math.random() * (range + 1));
    }

    /**
     * <p>Returns a long representation of this String</p>
     * @param s String
     * @return long
     */
    public static long playerNameToInt64(String s)
    {
        long l = 0L;
        for(int i = 0; i < s.length() && i < 12; i++){
            char c = s.charAt(i);
            l *= 37L;
            if(c >= 'A' && c <= 'Z') l += (1 + c) - 65;
            else if(c >= 'a' && c <= 'z') l += (1 + c) - 97;
            else if(c >= '0' && c <= '9') l += (27 + c) - 48;
        }
        while(l % 37L == 0L && l != 0L) l /= 37L;
        return l;
    }

    private static char decodeBuf[] = new char[4096];

    /**
     * <p>Returns a String representation of the packed text stored in packedData</p>
     * <p>Size is the size of the text</p>
     * @param packedData byte[]
     * @param size int
     * @return String
     */
    public static String textUnpack(byte packedData[], int size)
    {
        int idx = 0, highNibble = -1;
        for(int i = 0; i < size * 2; i++){
            int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
            if(highNibble == -1){
                if(val < 13) decodeBuf[idx++] = xlateTable[val];
                else highNibble = val;
            }
            else{
                decodeBuf[idx++] = xlateTable[((highNibble << 4) + val) - 195];
                highNibble = -1;
            }
        }

        return new String(decodeBuf, 0, idx);
    }

    /**
     * <p>Converts the first letter in each word to capitals</p>
     * <p>All other leters in that word are changed to lower-case</p>
     * @param text String
     * @return String
     */
    public static String optimizeText(String text)
    {
        char buf[] = text.toCharArray();
        boolean endMarker = true; // marks the end of a sentence to make the next char capital
        for(int i = 0; i < buf.length; i++){
            char c = buf[i];
            if(endMarker && c >= 'a' && c <= 'z'){
                buf[i] -= 0x20; // transform lower case into upper case
                endMarker = false;
            }
            if(c == '.' || c == '!' || c == '?') endMarker = true;
        }
        return new String(buf, 0, buf.length);
    }

    /**
     * <p>Packs the text into packedData as a byte[] representing text</p>
     * @param packedData byte[]
     * @param text String
     */
    public static void textPack(byte packedData[], java.lang.String text)
    {
        if(text.length() > 80) text = text.substring(0, 80);
        text = text.toLowerCase();

        int carryOverNibble = -1;
        int ofs = 0;
        for(int idx = 0; idx < text.length(); idx++){
            char c = text.charAt(idx);
            int tableIdx = 0;
            for(int i = 0; i < xlateTable.length; i++){
                if(c == xlateTable[i]){
                    tableIdx = i;
                    break;
                }
            }
            if(tableIdx > 12) tableIdx += 195;
            if(carryOverNibble == -1){
                if(tableIdx < 13) carryOverNibble = tableIdx;
                else packedData[ofs++] = (byte) (tableIdx);
            }
            else if(tableIdx < 13){
                packedData[ofs++] = (byte) ((carryOverNibble << 4) + tableIdx);
                carryOverNibble = -1;
            }
            else{
                packedData[ofs++] = (byte) ((carryOverNibble << 4) + (tableIdx >> 4));
                carryOverNibble = tableIdx & 0xf;
            }
        }

        if(carryOverNibble != -1) packedData[ofs++] = (byte) (carryOverNibble << 4);
    }

    public static char xlateTable[] = {
        ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r',
        'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p',
        'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2',
        '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?',
        '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\',
        '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[',
        ']'
    };

    /**
     * <p>Gets the direction between the two given points</p>
     * <p>Valid directions are N:0, NE:2, E:4, SE:6, S:8, SW:10, W:12, NW:14</p>
     * <p>The invalid (inbetween) direction are 1,3,5,7,9,11,13,15 i.e. odd integers</p>
     * <p>Returns -1, if src and dest are the same</p>
     * @param srcX int
     * @param srcY int
     * @param destX int
     * @param destY int
     * @return int
     */
    public static int direction(int srcX, int srcY, int destX, int destY)
    {
        int dx = destX - srcX, dy = destY - srcY;
        // a lot of cases that have to be considered here ... is there a more sophisticated (and quick!) way?
        if(dx < 0){
            if(dy < 0){
                if(dx < dy)return 11;
                else if(dx > dy)return 9;
                else return 10; // dx == dy
            }
            else if(dy > 0){
                if( -dx < dy)return 15;
                else if( -dx > dy)return 13;
                else return 14; // -dx == dy
            }
            else{ // dy == 0
                return 12;
            }
        }
        else if(dx > 0){
            if(dy < 0){
                if(dx < -dy)return 7;
                else if(dx > -dy)return 5;
                else return 6; // dx == -dy
            }
            else if(dy > 0){
                if(dx < dy)return 1;
                else if(dx > dy)return 3;
                else return 2; // dx == dy
            }
            else{ // dy == 0
                return 4;
            }
        }
        else{ // dx == 0
            if(dy < 0){
                return 8;
            }
            else if(dy > 0){
                return 0;
            }
            else{ // dy == 0
                return -1; // src and dest are the same
            }
        }
    }

    public static byte directionDeltaX[] = new byte[]{0, 1, 1, 1, 0, -1, -1, -1};
    public static byte directionDeltaY[] = new byte[]{1, 1, 0, -1, -1, -1, 0, 1};

    // translates our direction convention to the one used in the protocol
    public static byte xlateDirectionToClient[] = new byte[]{1, 2, 4, 7, 6, 5, 3, 0};
}
