package info.coremodding.gps.api.exceptions;

/**
 * @author James
 *         The exception for when a packet doesn't exist in some sort of call.
 */
public class PacketDoesntExistException extends Exception
{
    
    /**
     * The type of packet missing
     */
    public final String       type;
    
    /**
     * The serial ID of the exception
     */
    private static final long serialVersionUID = -5704474023650918495L;
    
    /**
     * @param type
     *            The type of the non-existent packet
     */
    public PacketDoesntExistException(String type)
    {
        this.type = type;
    }
    
    @Override
    public String getMessage()
    {
        return "Packet of type " + this.type + " does not exist.";
    }
}
