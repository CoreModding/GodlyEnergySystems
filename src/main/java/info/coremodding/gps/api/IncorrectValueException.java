package info.coremodding.gps.api;

/**
 * @author James
 *         A value was not valid
 */
public class IncorrectValueException extends Exception
{
    
    /**
     * The serial ID of the exception
     */
    private static final long serialVersionUID = 3371843116640306697L;
    
    @Override
    public String getMessage()
    {
        return "The value given is not valid!";
    }
}
