package info.coremodding.gps.api.meu;

/**
 * The base for most packets
 */
public abstract class MEUPacket
{
    
    private int    amount = 0;
    private String meta   = "";
    
    /**
     * @param add
     *            The amount to add
     * @return The new amount
     */
    public MEUPacket addAmount(int add)
    {
        this.amount += add;
        return this;
    }
    
    /**
     * @return The amount this packet has
     */
    public int getAmount()
    {
        return this.amount;
    }
    
    /**
     * This would be used in clear pipes, or an amount bar, ect
     * 
     * @return What color should the MEU show up as?
     */
    @SuppressWarnings("static-method")
    public String getColor()
    {
        return "FFFFFF";
    }
    
    /**
     * @return The meta of this packet
     */
    public String getMeta()
    {
        return this.meta;
    }
    
    /**
     * @return The name of the packet
     */
    public abstract String getName();
    
    /**
     * @param meta
     *            The meta to set to
     * @return The instance of this
     */
    public MEUPacket setMeta(String meta)
    {
        this.meta = meta;
        return this;
    }
}
