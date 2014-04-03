package info.coremodding.gps.internal;

import info.coremodding.gps.api.Machine;
import info.coremodding.gps.api.exceptions.IncorrectValueException;
import info.coremodding.gps.api.exceptions.PacketDoesntExistException;
import info.coremodding.gps.api.exceptions.PacketInDebtException;
import info.coremodding.gps.api.exceptions.PacketTooLargeException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author James
 *         A packet of energy or items
 */
public class Packet implements Serializable
{
    
    /**
     * The serial ID for this class
     */
    private static final long          serialVersionUID = -4279079202475122829L;
    
    /**
     * All of the subpackets this contains
     */
    private final ArrayList<SubPacket> subpackets       = new ArrayList<>();
    
    /**
     * How much space this packet takes up
     */
    public int                         size             = 0;
    
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
     * @param machine
     *            The machine to transfer to
     * @throws PacketTooLargeException
     *             The packet was too large to merge
     */
    public void mergePacket(SubPacket packet, Machine machine)
            throws PacketTooLargeException
    {
        if (calcSize(packet) >= machine.maxStorage()) { throw new PacketTooLargeException(); }
        for (SubPacket packet2 : this.subpackets)
        {
            if (packet2.type.equals(packet.type))
            {
                packet2.amount += packet.amount;
                packet2.notify(machine);
                return;
            }
        }
        this.subpackets.add(packet);
        calcSize();
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
     * @param machine
     *            The machine to merge with
     * @throws PacketTooLargeException
     *             The packet was too large to create
     */
    public void newPacket(String type, int amount,
            @SuppressWarnings("hiding") int size, Machine machine)
            throws PacketTooLargeException
    {
        SubPacket packet = new SubPacket(type, amount, size);
        if (calcSize(packet) >= machine.maxStorage()) { throw new PacketTooLargeException(); }
        for (SubPacket packet2 : this.subpackets)
        {
            if (packet2.type.equals(packet.type))
            {
                packet2.amount += packet.amount;
                packet2.notify(machine);
                return;
            }
        }
        this.subpackets.add(packet);
        calcSize();
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
                calcSize();
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
     * @param machine
     *            The machine to add to
     * @throws PacketDoesntExistException
     *             The packet of the given type does not exist
     * @throws IncorrectValueException
     *             The packet's amount is below 0
     * @throws PacketTooLargeException
     *             The amount was too large to add
     */
    public void addAmount(String type, int amount, Machine machine)
            throws PacketDoesntExistException, IncorrectValueException,
            PacketTooLargeException
    {
        if (amount < 0) { throw new IncorrectValueException(); }
        for (SubPacket packet : this.subpackets)
        {
            if (packet.type.equals(type))
            {
                if (calcSize(packet) >= machine.maxStorage()) { throw new PacketTooLargeException(); }
                packet.amount += amount;
                packet.notify(machine);
                calcSize();
                return;
            }
        }
        throw new PacketDoesntExistException(type);
    }
    
    private void calcSize()
    {
        int _size = 0;
        for (SubPacket packet : this.subpackets)
        {
            _size += packet.amount * packet.size;
        }
        this.size = _size;
    }
    
    private int calcSize(SubPacket trypacket)
    {
        int _size = 0;
        for (SubPacket packet : this.subpackets)
        {
            _size += packet.amountsize;
        }
        return _size + trypacket.amountsize;
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
                calcSize();
                return;
            }
        }
        throw new PacketDoesntExistException(type);
    }
    
    /**
     * @author James
     *         A sub-packet
     */
    public static class SubPacket implements Serializable
    {
        
        /**
         * The serial ID
         */
        private static final long serialVersionUID = -1986100268185813328L;
        
        /**
         * How much space one unit of the packet takes up
         */
        public final int          size;
        
        /**
         * The name of the packet
         */
        public final String       type;
        
        /**
         * How many units packet contains
         */
        public int                amount;
        
        /**
         * The amount * the size
         */
        public int                amountsize;
        
        /**
         * The last machine the packet was at
         */
        public Machine            lastAt;
        
        /**
         * The machine the packet is currently at
         */
        public Machine            currentlyAt;
        
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
            this.amount = amount;
            calcSize();
        }
        
        /**
         * @param machine
         *            The machine to notify the packet it was last at
         */
        public void notify(Machine machine)
        {
            this.lastAt = this.currentlyAt;
            this.currentlyAt = machine;
            calcSize();
        }
        
        private void calcSize()
        {
            this.amountsize = this.amount * this.size;
        }
    }
}
