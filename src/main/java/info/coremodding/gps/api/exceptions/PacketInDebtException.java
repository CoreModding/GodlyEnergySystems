package info.coremodding.gps.api.exceptions;

/**
 * @author James
 *         Thrown when packet has negative values in amount (in debt)
 */
public class PacketInDebtException extends Exception
{
    
    /**
     * The type of packet with the negative amount
     */
    public final String       type;
    
    /**
     * The serial ID of the exception
     */
    private static final long serialVersionUID = -8749530807758018701L;
    
    /**
     * @param type
     *            The type of the in debt packet
     */
    public PacketInDebtException(String type)
    {
        this.type = type;
    }
    
    @Override
    public String getMessage()
    {
        return "Packet of type " + this.type + " has negative amount values.";
    }
}
