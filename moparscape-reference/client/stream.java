/**
 * <p>Title: stream</p>
 *
 * <p>Description: handles the stream between server and client</p>
 *
 * <p>Copyright: Copyright (c) 2006 winterlove</p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * @author winterlove
 * @version 1.0
 */

public class stream
{

    /**
     * <p>Instantiates a new stream object</p>
     */
    public stream()
    {
    }

    /**
     * <p>Creates a new stream object with the specified buffer</p>
     * @param abyte0 byte[]
     */
    public stream(byte abyte0[])
    {
        buffer = abyte0;
        currentOffset = 0;
    }

    /**
     * <p>Returns a byte read from the buffer - 128</p>
     * @return byte
     */
    public byte readSignedByteA()
    {
        return(byte) (buffer[currentOffset++] - 128);
    }


    /**
     * <p>Returns a negative value of the byte read from the buffer</p>
     * @return byte
     */
    public byte readSignedByteC()
    {
        return(byte) ( -buffer[currentOffset++]);
    }

    /**
     * <p>Returns 128 - the value read from the buffer</p>
     * @return byte
     */
    public byte readSignedByteS()
    {
        return(byte) (128 - buffer[currentOffset++]);
    }

    /**
     * <p>Returns the value read from the buffer - 128 & 0xff</p>
     * @return int
     */
    public int readUnsignedByteA()
    {
        return buffer[currentOffset++] - 128 & 0xff;
    }

    /**
     * <p>Returns negative the value read from the buffer  & 0xff</p>
     * @return int
     */
    public int readUnsignedByteC()
    {
        return -buffer[currentOffset++] & 0xff;
    }

    /**
     * <p>Returns 128 - the value read from the buffer  & 0xff</p>
     * @return int
     */
    public int readUnsignedByteS()
    {
        return 128 - buffer[currentOffset++] & 0xff;
    }

    /**
     * <p>Adds i + 128 to the buffer</p>
     * @param i int
     */
    public void writeByteA(int i)
    {
        buffer[currentOffset++] = (byte) (i + 128);
    }

    /**
     * <p>Adds 128 - i to the buffer</p>
     * @param i int
     */
    public void writeByteS(int i)
    {
        buffer[currentOffset++] = (byte) (128 - i);
    }

    /**
     * <p>Adds -i to the buffer</p>
     * @param i int
     */
    public void writeByteC(int i)
    {
        buffer[currentOffset++] = (byte) ( -i);
    }

    /**
     * <p>Returns a BigEndian word read from the buffer</p>
     * @return int
     */
    public int readSignedWordBigEndian()
    {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
        if(i > 32767){
            i -= 0x10000;
        }
        return i;
    }

    /**
     * <p>Returns a signed word read from the buffer</p>
     * @return int
     */
    public int readSignedWordA()
    {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] - 128 & 0xff);
        if(i > 32767){
            i -= 0x10000;
        }
        return i;
    }

    /**
     * <p>Returns a BigEndian word read from the buffer</p>
     * @return int
     */
    public int readSignedWordBigEndianA()
    {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
        if(i > 32767)
            i -= 0x10000;
        return i;
    }

    /**
     * <p>Returns a BigEndian word read from the buffer</p>
     * @return int
     */
    public int readUnsignedWordBigEndian()
    {
        currentOffset += 2;
        return((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
    }

    /**
     * <p>Returns a word read from the buffer</p>
     * @return int
     */
    public int readUnsignedWordA()
    {
        currentOffset += 2;
        return((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] - 128 & 0xff);
    }

    /**
     * <p>Returns a BigEndian word read from the buffer</p>
     * @return int
     */
    public int readUnsignedWordBigEndianA()
    {
        currentOffset += 2;
        return((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
    }

    /**
     * <p>Writes a BigEndian word to the buffer</p>
     * @param i int
     */
    public void writeWordBigEndianA(int i)
    {
        buffer[currentOffset++] = (byte) (i + 128);
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    /**
     * <p>Writes a word to the buffer</p>
     * @param i int
     */
    public void writeWordA(int i)
    {
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) (i + 128);
    }

    /**
     * <p>Writes a BigEndian word to the buffer</p>
     * @param i int
     */
    public void writeWordBigEndian_dup(int i)
    {
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    /**
     * <p>Reads a DWord from the buffer</p>
     * @return int
     */
    public int readDWord_v1()
    {
        currentOffset += 4;
        return((buffer[currentOffset - 2] & 0xff) << 24) + ((buffer[currentOffset - 1] & 0xff) << 16) +
            ((buffer[currentOffset - 4] & 0xff) << 8) + (buffer[currentOffset - 3] & 0xff);
    }

    /**
     * <p>Reads a DWord from the buffer</p>
     * @return int
     */
    public int readDWord_v2()
    {
        currentOffset += 4;
        return((buffer[currentOffset - 3] & 0xff) << 24) + ((buffer[currentOffset - 4] & 0xff) << 16) +
            ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
    }

    /**
     * <p>Writes a DWord to the buffer</p>
     * @param i int
     */
    public void writeDWord_v1(int i)
    {
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 24);
        buffer[currentOffset++] = (byte) (i >> 16);
    }

    /**
     * <p>Writes a DWord to the buffer</p>
     * @param i int
     */
    public void writeDWord_v2(int i)
    {
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 24);
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    /**
     * <p>Reads the reversed bytes from the buffer to abyte0</p>
     * @param abyte0 byte[]
     * @param i int
     * @param j int
     */
    public void readBytes_reverse(byte abyte0[], int i, int j)
    {
        for(int k = (j + i) - 1; k >= j; k--){
            abyte0[k] = buffer[currentOffset++];
        }
    }

    /**
     * <p>Writes the bytes from the abyte0 reversed to the buffer</p>
     * @param abyte0 byte[]
     * @param i int
     * @param j int
     */
    public void writeBytes_reverse(byte abyte0[], int i, int j)
    {
        for(int k = (j + i) - 1; k >= j; k--)
            buffer[currentOffset++] = abyte0[k];

    }

    /**
     * <p>Reads the reversed bytes from the buffer to abyte0</p>
     * @param abyte0 byte[]
     * @param i int
     * @param j int
     */
    public void readBytes_reverseA(byte abyte0[], int i, int j)
    {
        for(int k = (j + i) - 1; k >= j; k--)
            abyte0[k] = (byte) (buffer[currentOffset++] - 128);

    }

    /**
     * <p>Writes the bytes from the abyte0 reversed to the buffer</p>
     * @param abyte0 byte[]
     * @param i int
     * @param j int
     */
    public void writeBytes_reverseA(byte abyte0[], int i, int j)
    {
        for(int k = (j + i) - 1; k >= j; k--)
            buffer[currentOffset++] = (byte) (abyte0[k] + 128);

    }

    /**
     * <p>Writes a frame to the buffer</p>
     * @param id int
     */
    public void createFrame(int id)
    {
        buffer[currentOffset++] = (byte) (id + packetEncryption.getNextKey());
    }

    private static final int frameStackSize = 10;
    private int frameStackPtr = -1;
    private int frameStack[] = new int[frameStackSize];

    /**
     * <p>Creates a variable sized frame</p>
     * @param id int
     */
    public void createFrameVarSize(int id)
    { // creates a variable sized frame
        buffer[currentOffset++] = (byte) (id + packetEncryption.getNextKey());
        buffer[currentOffset++] = 0; // placeholder for size byte
        if(frameStackPtr >= frameStackSize - 1){
            throw new RuntimeException("Stack overflow");
        }
        else frameStack[++frameStackPtr] = currentOffset;
    }

    /**
     * <p>Creates a variable sized frame</p>
     * @param id int
     */
    public void createFrameVarSizeWord(int id)
    { // creates a variable sized frame
        buffer[currentOffset++] = (byte) (id + packetEncryption.getNextKey());
        writeWord(0); // placeholder for size word
        if(frameStackPtr >= frameStackSize - 1){
            throw new RuntimeException("Stack overflow");
        }
        else frameStack[++frameStackPtr] = currentOffset;
    }

    /**
     * <p>Ends a variable sized frame</p>
     */
    public void endFrameVarSize()
    { // ends a variable sized frame
        if(frameStackPtr < 0)throw new RuntimeException("Stack empty");
        else writeFrameSize(currentOffset - frameStack[frameStackPtr--]);
    }

    /**
     * <p>Ends a variable sized frame</p>
     */
    public void endFrameVarSizeWord()
    { // ends a variable sized frame
        if(frameStackPtr < 0)throw new RuntimeException("Stack empty");
        else writeFrameSizeWord(currentOffset - frameStack[frameStackPtr--]);
    }

    /**
     * <p>Writes a byte to the buffer</p>
     * @param i int
     */
    public void writeByte(int i)
    {
        buffer[currentOffset++] = (byte) i;
    }

    /**
     * <p>Writes a word to the buffer</p>
     * @param i int
     */
    public void writeWord(int i)
    {
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    /**
     * <p>Writes a BigEndian word to the buffer</p>
     * @param i int
     */
    public void writeWordBigEndian(int i)
    {
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    /**
     * <p>Writes to the buffer 3 times, after manipulating i a bit</p>
     * @param i int
     */
    public void write3Byte(int i)
    {
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    /**
     * <p>Writes a DWord to the buffer</p>
     * @param i int
     */
    public void writeDWord(int i)
    {
        buffer[currentOffset++] = (byte) (i >> 24);
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    /**
     * <p>Writes a DWord BigEndian</p>
     * @param i int
     */
    public void writeDWordBigEndian(int i)
    {
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 24);
    }

    /**
     * <p>Writes a QWord</p>
     * @param l long
     */
    public void writeQWord(long l)
    {
        buffer[currentOffset++] = (byte) (int) (l >> 56);
        buffer[currentOffset++] = (byte) (int) (l >> 48);
        buffer[currentOffset++] = (byte) (int) (l >> 40);
        buffer[currentOffset++] = (byte) (int) (l >> 32);
        buffer[currentOffset++] = (byte) (int) (l >> 24);
        buffer[currentOffset++] = (byte) (int) (l >> 16);
        buffer[currentOffset++] = (byte) (int) (l >> 8);
        buffer[currentOffset++] = (byte) (int) l;
    }

    /**
     * <p>Writes the bytes from s to the buffer</p>
     * @param s String
     */
    public void writeString(java.lang.String s)
    {
        s.getBytes(0, s.length(), buffer, currentOffset);
        currentOffset += s.length();
        buffer[currentOffset++] = 10;
    }

    /**
     * <p>Writes abyte0 to the buffer</p>
     * @param abyte0 byte[]
     * @param i int
     * @param j int
     */
    public void writeBytes(byte abyte0[], int i, int j)
    {
        for(int k = j; k < j + i; k++)
            buffer[currentOffset++] = abyte0[k];

    }

    /**
     * <p>Writes a frame size to the buffer</p>
     * @param i int
     */
    public void writeFrameSize(int i)
    {
        buffer[currentOffset - i - 1] = (byte) i;
    }

    /**
     * <p>Writes a frame size word to the buffer</p>
     * @param i int
     */
    public void writeFrameSizeWord(int i)
    {
        buffer[currentOffset - i - 2] = (byte) (i >> 8);
        buffer[currentOffset - i - 1] = (byte) i;
    }

    /**
     * <p>Returns an unsigned byte read from the buffer</p>
     * @return int
     */
    public int readUnsignedByte()
    {
        return buffer[currentOffset++] & 0xff;
    }

    /**
     * <p>Returns a byte read directly from the buffer</p>
     * @return byte
     */
    public byte readSignedByte()
    {
        return buffer[currentOffset++];
    }

    /**
     * <p>Reads an unsigned word directly from the buffer</p>
     * @return int
     */
    public int readUnsignedWord()
    {
        currentOffset += 2;
        return((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
    }

    /**
     * <p>Reads a signed word from the buffer</p>
     * @return int
     */
    public int readSignedWord()
    {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
        if(i > 32767){
            i -= 0x10000;
        }
        return i;
    }

    /**
     * <p>Returns a DWord read from the buffer</p>
     * @return int
     */
    public int readDWord()
    {
        currentOffset += 4;
        return((buffer[currentOffset - 4] & 0xff) << 24) + ((buffer[currentOffset - 3] & 0xff) << 16) +
            ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
    }

    /**
     * <p>Reads a QWord from the buffer</p>
     * @return long
     */
    public long readQWord()
    {
        long l = (long) readDWord() & 0xffffffffL;
        long l1 = (long) readDWord() & 0xffffffffL;
        return(l << 32) + l1;
    }

    /**
     * <p>Returns a String read from the buffer</p>
     * @return String
     */
    public java.lang.String readString()
    {
        int i = currentOffset;
        while(buffer[currentOffset++] != 10);
        return new String(buffer, i, currentOffset - i - 1);
    }

    /**
     * <p>Reads the bytes in the buffer into abyte0</p>
     * @param abyte0 byte[]
     * @param i int
     * @param j int
     */
    public void readBytes(byte abyte0[], int i, int j)
    {
        for(int k = j; k < j + i; k++)
            abyte0[k] = buffer[currentOffset++];

    }

    /**
     * <p>Allows the ability to write bits to the buffer</p>
     */
    public void initBitAccess()
    {
        bitPosition = currentOffset * 8;
    }

    /**
     * <p>Writes the specified bits to the buffer</p>
     * @param numBits int
     * @param value int
     */
    public void writeBits(int numBits, int value)
    {
        int bytePos = bitPosition >> 3;
        int bitOffset = 8 - (bitPosition & 7);
        bitPosition += numBits;

        for(; numBits > bitOffset; bitOffset = 8){
            buffer[bytePos] &= ~bitMaskOut[bitOffset]; // mask out the desired area
            buffer[bytePos++] |= (value >> (numBits - bitOffset)) & bitMaskOut[bitOffset];

            numBits -= bitOffset;
        }
        if(numBits == bitOffset){
            buffer[bytePos] &= ~bitMaskOut[bitOffset];
            buffer[bytePos] |= value & bitMaskOut[bitOffset];
        }
        else{
            buffer[bytePos] &= ~(bitMaskOut[numBits] << (bitOffset - numBits));
            buffer[bytePos] |= (value & bitMaskOut[numBits]) << (bitOffset - numBits);
        }
    }

    /**
     * <p>Removes the ability to read bits from the buffer</p>
     */
    public void finishBitAccess()
    {
        currentOffset = (bitPosition + 7) / 8;
    }

    public byte buffer[] = null;
    public int currentOffset = 0;
    public int bitPosition = 0;

    public static int bitMaskOut[] = new int[32];

    /**
     * <p>Called whenever the class is loaded into memory</p>
     */
    static
    {
        for(int i = 0; i < 32; i++)
            bitMaskOut[i] = (1 << i) - 1;
    }

    public Cryption packetEncryption = null;
}
