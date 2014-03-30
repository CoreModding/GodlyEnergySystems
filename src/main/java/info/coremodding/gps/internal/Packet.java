package info.coremodding.gps.internal;

import info.coremodding.gps.api.IncorrectValueException;
import info.coremodding.gps.api.Machine;
import info.coremodding.gps.api.PacketDoesntExistException;
import info.coremodding.gps.api.PacketInDebtException;

import java.util.ArrayList;

/**
 * @author James
 *         A packet of energy or items
 */
public class Packet
{
    
    /**
     * All of the subpackets this contains
     */
    private final ArrayList<SubPacket> subpackets = new ArrayList<>();
    
    /**
     * How much space this packet takes up
     */
    public int                         size       = 0;
    
    /**
     * @return The sub packets of this packet
     */
    public ArrayList<SubPacket> getSubs()
    {
        return this.subpackets;
    }
    
    /**
     * @param packet
     *            The packet to merge with this
     */
    public void mergePacket(SubPacket packet)
    {
        for (SubPacket packet2 : this.subpackets)
        {
            if (packet2.type.equals(packet.type))
            {
                packet2.amount += packet.amount;
                return;
            }
        }
        this.subpackets.add(packet);
    }
    
    /**
     * Creates a new subpacket and adds it to the packet
     * 
     * @param type
     *            The type of the sub packet. Eg: Item:Stone:0, Fluid:Water
     *            It should be formatted Type:Name:Meta
     * @param amount
     *            The amount of units of this packet
     * @param size
     *            How much space this packet takes up
     */
    public void newPacket(String type, int amount,
            @SuppressWarnings("hiding") int size)
    {
        SubPacket packet = new SubPacket(type, amount, size);
        for (SubPacket packet2 : this.subpackets)
        {
            if (packet2.type.equals(packet.type))
            {
                packet2.amount += packet.amount;
                return;
            }
        }
        this.subpackets.add(packet);
    }
    
    /**
     * @param type
     *            The type of packet to remove from
     * @param amount
     *            The amount to remove from that packet
     * @throws PacketDoesntExistException
     *             The packet of the given type does not exist
     * @throws PacketInDebtException
     *             The packet's amount is below 0
     */
    public void removeAmount(String type, int amount)
            throws PacketDoesntExistException, PacketInDebtException
    {
        for (SubPacket packet : this.subpackets)
        {
            if (packet.type.equals(type))
            {
                packet.amount -= amount;
                if (packet.amount < 1) { throw new PacketInDebtException(type); }
                return;
            }
        }
        throw new PacketDoesntExistException(type);
    }
    
    /**
     * @param type
     *            The type of packet to add to
     * @param amount
     *            The amount to add to that packet
     * @throws PacketDoesntExistException
     *             The packet of the given type does not exist
     * @throws IncorrectValueException
     *             The packet's amount is below 0
     */
    public void addAmount(String type, int amount)
            throws PacketDoesntExistException, IncorrectValueException
    {
        if (amount < 0) { throw new IncorrectValueException(); }
        for (SubPacket packet : this.subpackets)
        {
            if (packet.type.equals(type))
            {
                packet.amount += amount;
                return;
            }
        }
        throw new PacketDoesntExistException(type);
    }
    
    /**
     * @param type
     *            The type of packet to remove
     * @throws PacketDoesntExistException
     *             The packet of the given type does not exist
     */
    public void removePacket(String type) throws PacketDoesntExistException
    {
        for (SubPacket packet : this.subpackets)
        {
            if (packet.type.equals(type))
            {
                packet = null;
                return;
            }
        }
        throw new PacketDoesntExistException(type);
    }
    
    /**
     * @author James
     *         A sub-packet
     */
    public static class SubPacket
    {
        
        /**
         * How much space one unit of the packet takes up
         */
        public final int    size;
        
        /**
         * The name of the packet
         */
        public final String type;
        
        /**
         * How many units packet contains
         */
        public int          amount;
        
        /**
         * The last machine the packet was at
         */
        public Machine      lastAt;
        
        /**
         * The machine the packet is currently at
         */
        public Machine      currentlyAt;
        
        /**
         * @param type
         *            The type of the sub packet. Eg: Item:Stone:0, Fluid:Water
         *            It should be formatted Type:Name:Meta
         * @param amount
         *            The amount of units of this packet
         * @param size
         *            How much space this packet takes up
         */
        public SubPacket(String type, int amount, int size)
        {
            this.size = size;
            this.type = type;
        }
        
        /**
         * @param machine
         *            The machine to notify the packet it was last at
         */
        public void notify(Machine machine)
        {
            this.lastAt = this.currentlyAt;
            this.currentlyAt = machine;
        }
    }
}
