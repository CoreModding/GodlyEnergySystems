package info.coremodding.gps.api.exceptions;

/**
 * @author James
 *         A value was not valid. This could be a method parameter, constructor
 *         parameter, or anything else with an invalid value.
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
