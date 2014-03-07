package info.coremodding.gps.api.machine;

import info.coremodding.api.locations.Location;
import info.coremodding.gps.api.meu.MEUPacket;
import info.coremodding.gps.api.meu.MEUSystemManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * @author James
 *         <p>
 *         All machines extend this
 */
public abstract class Machine extends TileEntity
{
    
    private List<MEUPacket> packets;
    
    /**
     * @param packet
     *            The packet to deposit
     * @return The new energy
     */
    int depositEnergy(MEUPacket packet)
    {
        this.packets.add(packet);
        this.packets = MEUSystemManager.mergePackets(this.packets);
        return packet.getAmount();
    }
    
    /**
     * @return These packets
     */
    protected List<MEUPacket> getPackets()
    {
        return this.packets;
    }
    
    /**
     * We handle standard power management. You do whatever else here!
     */
    public abstract void onUpdate();
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        int max = nbt.getInteger("packet_amount");
        for (int i = 0; i < max; i++)
        {
            try
            {
                Class<?> packetpre = Class.forName(nbt.getString(i + "_name"));
                if (packetpre != null)
                {
                    if (packetpre.isAssignableFrom(MEUPacket.class))
                    {
                        try
                        {
                            MEUPacket packet = (MEUPacket) packetpre
                                    .newInstance();
                            packet.setMeta(nbt.getString(i + "_meta"));
                            packet.addAmount(nbt.getInteger(i + "_amount"));
                            if (packet.getMeta() == null
                                    || packet.getAmount() == 0) System.out
                                    .println("Errored packet? Meta is null or amount is 0. Demolishing.");
                            else this.packets.add(packet);
                        } catch (InstantiationException e)
                        {
                            System.out
                                    .println("Packet has weird params as an only constructor! There should be none!");
                        } catch (IllegalAccessException e)
                        {
                            System.out
                                    .println("Packet does not have public constructor with no params! There should be!");
                        }
                    }
                }
            } catch (ClassNotFoundException e)
            {
                System.out
                        .println("Invalid class for packet. Has a mod been removed? If not, report to zeus.");
            }
        }
    }
    
    /**
     * @param packet
     *            The packet to remove
     * @return The new energy
     */
    public int removeEnergy(MEUPacket packet)
    {
        packet.addAmount(-packet.getAmount());
        this.packets.add(packet);
        this.packets = MEUSystemManager.mergePackets(this.packets);
        return -packet.getAmount();
    }
    
    /**
     * @param packets
     *            The new packets
     */
    void setPackets(List<MEUPacket> packets)
    {
        this.packets = packets;
    }
    
    /**
     * @param packet
     *            The packet to pass
     * @param machine
     *            The machine to send to
     * @return Should the machine pass energy to the packet and machine given.
     */
    public abstract boolean shouldPass(MEUPacket packet, Machine machine);
    
    @Override
    public void updateEntity()
    {
        List<Machine> machines = MEUSystemManager.getTouching(new Location(
                this.xCoord, this.yCoord, this.zCoord, this.worldObj));
        List<MEUPacket> _packets = this.getPackets();
        for (Machine machine : machines)
        {
            for (MEUPacket packet : _packets)
            {
                packet.addAmount(-machine.depositEnergy(packet));
            }
        }
        this.setPackets(_packets);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        int i = 0;
        for (MEUPacket packet : this.packets)
        {
            nbt.setString(i + "_name", packet.getClass().getPackage()
                    + packet.getClass().getName());
            nbt.setString(i + "_meta", packet.getMeta());
            nbt.setInteger(i + "_amount", packet.getAmount());
            i++;
        }
        nbt.setInteger("packet_amount", i);
    }
}
