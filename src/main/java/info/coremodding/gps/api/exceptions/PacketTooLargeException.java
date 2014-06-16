package info.coremodding.gps.api.exceptions;

/**
 * @author James
 *         The packet didnt fit in the machine
 */
public class PacketTooLargeException extends Exception
{
    
    /**
     * The serial ID for this exception
     */
    private static final long serialVersionUID = 2696098039901047936L;
    
    @Override
    public String getMessage()
    {
        return "The packet did not fit in the machine!";
    }
}
