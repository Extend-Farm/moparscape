/**
 * <p>Title: NPC</p>
 *
 * <p>Description: NPC Class</p>
 *
 * <p>Copyright: Copyright (c) 2006 whitefang</p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * @author whitefang
 * @version 1.0
 */

public class NPC
{
    public int npcId;
    public int npcType;

    public int absX, absY;
    public int heightLevel;
    public int makeX, makeY, moverangeX1, moverangeY1, moverangeX2, moverangeY2, moveX, moveY, direction, walkingType;
    public int spawnX, spawnY;
    public int HP, MaxHP, hitDiff, MaxHit, animNumber, actionTimer, StartKilling;
    public boolean IsDead, DeadApply, NeedRespawn, IsUnderAttack, IsClose;
    public int[] Killing = new int[PlayerHandler.maxPlayers];

    public boolean RandomWalk;
    public boolean dirUpdateRequired;
    public boolean animUpdateRequired;
    public boolean hitUpdateRequired;
    public boolean updateRequired;
    public boolean textUpdateRequired;
    public String textUpdate;

    /**
     * Constructs a new NPC object with the specified ID and Type</p>
     * @param _npcId int
     * @param _npcType int
     */
    public NPC(int _npcId, int _npcType)
    {
        npcId = _npcId;
        npcType = _npcType;
        direction = -1;
        IsDead = false;
        DeadApply = false;
        actionTimer = 0;
        RandomWalk = true;
        StartKilling = 0;
        IsUnderAttack = false;
        IsClose = false;
        for(int i = 0; i < Killing.length; i++){
            Killing[i] = 0;
        }
    }

    /**
     * <p>Updates this NPC Object's movement using the specified <code>stream</code></p>
     * @param str stream
     */
    public void updateNPCMovement(stream str)
    {
        if(direction == -1){
            // don't have to update the npc position, because the npc is just standing
            if(updateRequired){
                // tell client there's an update block appended at the end
                str.writeBits(1, 1);
                str.writeBits(2, 0);
            }
            else{
                str.writeBits(1, 0);
            }
        }
        else{
            // send "walking packet"
            str.writeBits(1, 1);
            str.writeBits(2, 1); // updateType
            str.writeBits(3, misc.xlateDirectionToClient[direction]);
            if(updateRequired){
                str.writeBits(1, 1); // tell client there's an update block appended at the end
            }
            else{
                str.writeBits(1, 0);
            }
        }
    }

    /**
     * <p>Appends this NPC's update to <code>str</code></p>
     * @param str stream
     */
    public void appendNPCUpdateBlock(stream str)
    {
        if(!updateRequired)return; // nothing required
        int updateMask = 0;
        if(textUpdateRequired) updateMask |= 1;
        if(animUpdateRequired) updateMask |= 0x10;
        if(hitUpdateRequired) updateMask |= 0x40;
        if(dirUpdateRequired) updateMask |= 0x20;

        /*if(updateMask >= 0x100) {
         // byte isn't sufficient
         updateMask |= 0x40;			// indication for the client that updateMask is stored in a word
         str.writeByte(updateMask & 0xFF);
         str.writeByte(updateMask >> 8);
           } else {*/
        str.writeByte(updateMask);
        //}

        // now writing the various update blocks itself - note that their order crucial
        if(textUpdateRequired){
            str.writeString(textUpdate);
        }
        if(animUpdateRequired) appendAnimUpdate(str);
        if(hitUpdateRequired) appendHitUpdate(str);
        if(dirUpdateRequired) appendDirUpdate(str);
        // TODO: add the various other update blocks
    }

    /**
     * <p>So we know we don't have to update anymore at the moment</p>
     */
    public void clearUpdateFlags()
    {
        updateRequired = false;
        textUpdateRequired = false;
        hitUpdateRequired = false;
        animUpdateRequired = false;
        dirUpdateRequired = false;
        textUpdate = null;
        moveX = 0;
        moveY = 0;
        direction = -1;
    }

    /**
     * <p>Get's the direction of this NPC's next movement</p>
     * <p>Returns 0-7 for next walking direction or -1, if we're not moving</p>
     */
    public int getNextWalkingDirection()
    {
        int dir;
        dir = misc.direction(absX, absY, (absX + moveX), (absY + moveY));
        if(dir == -1)return -1;
        dir >>= 1;
        absX += moveX;
        absY += moveY;
        return dir;
    }

    /**
     * <p>Set's <code>direction</code> to <code>getNextWalkingDirection</code></p>
     */
    public void getNextNPCMovement()
    {
        direction = -1;
        direction = getNextWalkingDirection();
    }

    /**
     * <p>Sends our "hit" to the other NPC/Player object</p>
     * @param str stream
     */
    protected void appendHitUpdate(stream str)
    {
        try{
            HP -= hitDiff;
            if(HP <= 0){
                IsDead = true;
            }
            str.writeByteC(hitDiff); // What the perseon got 'hit' for
            if(hitDiff > 0){
                str.writeByteS(1); // 0: red hitting - 1: blue hitting
            }
            else{
                str.writeByteS(0); // 0: red hitting - 1: blue hitting
            }
            str.writeByteS(HP); // Their current hp, for HP bar
            str.writeByteC(MaxHP); // Their max hp, for HP bar
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * <p>Updates this NPC's current animation</p>
     * @param str stream
     */
    public void appendAnimUpdate(stream str)
    {
        str.writeWordBigEndian(animNumber);
        str.writeByte(1);
    }

    /**
     * <p>Notifies host client of our direction</p>
     * @param str stream
     */
    public void appendDirUpdate(stream str)
    {
        str.writeWord(direction);
    }
}
