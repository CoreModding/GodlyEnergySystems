package info.coremodding.gps.api;

import info.coremodding.gps.api.exceptions.PacketDoesntExistException;
import info.coremodding.gps.api.exceptions.PacketTooLargeException;
import info.coremodding.gps.internal.Location;
import info.coremodding.gps.internal.Packet;
import info.coremodding.gps.internal.Serializer;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * @author James
 *         The base machine class
 * 
 *         All GPS machines MUST extend it, or a class extending it.
 */
public abstract class Machine extends TileEntity
{
    
    /**
     * The packet that contains all of this machine's contents
     */
    public Packet MachinePacket = new Packet();
    
    @Override
    public void updateEntity()
    {
        Machine[] touching = getTouching().toArray(new Machine[] {});
        for (Machine machine : touching)
        {
            Packet.SubPacket[] packets = this.MachinePacket.getSubs().toArray(
                    new Packet.SubPacket[] {});
            for (Packet.SubPacket packet : packets)
            {
                if (!packet.lastAt.equals(machine))
                {
                    if (ShouldAttemptToSend(packet, machine))
                    {
                        if (machine.ShouldAccept(packet, this))
                        {
                            try
                            {
                                machine.MachinePacket.mergePacket(packet,
                                        machine);
                            } catch (PacketTooLargeException e1)
                            {
                                // Whoops, let's just not do that...
                            }
                            try
                            {
                                this.MachinePacket.removePacket(packet.type);
                            } catch (PacketDoesntExistException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        Update();
    }
    
    /**
     * Where you handle what you'd normally do in updateEntity, as we override
     * it to handle packet transfer. You can still override it, but you MUST
     * create a way to handle packets in it.
     */
    public abstract void Update();
    
    /**
     * Determine if you want to send a packet of the given value to the given
     * machine.
     * Returning true does NOT mean it will be sent. Other checks must still be
     * made.
     * 
     * @param packet
     *            The packet to check
     * @param machine
     *            The machine to check
     * @return Should the packet be sent to the machine?
     */
    public abstract boolean ShouldAttemptToSend(Packet.SubPacket packet,
            Machine machine);
    
    /**
     * Determine if you want to obtain a packet of the given value from the
     * machine given.
     * Returning true does NOT mean it will be sent. Other checks must still be
     * made.
     * 
     * @param packet
     *            The packet to accept
     * @param machine
     *            The machine that sent it
     * @return Should we accept the packet from this machine?
     */
    public abstract boolean ShouldAccept(Packet.SubPacket packet,
            Machine machine);
    
    /**
     * @return Machine instances of tile entities touching this machine. Used
     *         for packet transfer stuff. It could also be used for interacting
     *         with other machines if called by you.
     */
    public ArrayList<Machine> getTouching()
    {
        Location loc = new Location(this.xCoord, this.yCoord, this.zCoord,
                this.worldObj);
        if (loc.getWorld() != null)
        {
            ArrayList<Machine> touching = new ArrayList<>();
            if (loc.getWorld()
                    .getTileEntity(loc.getX(), loc.getY(), loc.getZ()) instanceof Machine) touching
                    .add((Machine) loc.getWorld().getTileEntity(loc.getX() + 1,
                            loc.getY(), loc.getZ()));
            if (loc.getWorld()
                    .getTileEntity(loc.getX(), loc.getY(), loc.getZ()) instanceof Machine) touching
                    .add((Machine) loc.getWorld().getTileEntity(loc.getX() - 1,
                            loc.getY(), loc.getZ()));
            if (loc.getWorld()
                    .getTileEntity(loc.getX(), loc.getY(), loc.getZ()) instanceof Machine) touching
                    .add((Machine) loc.getWorld().getTileEntity(loc.getX(),
                            loc.getY() + 1, loc.getZ()));
            if (loc.getWorld()
                    .getTileEntity(loc.getX(), loc.getY(), loc.getZ()) instanceof Machine) touching
                    .add((Machine) loc.getWorld().getTileEntity(loc.getX(),
                            loc.getY() - 1, loc.getZ()));
            if (loc.getWorld()
                    .getTileEntity(loc.getX(), loc.getY(), loc.getZ()) instanceof Machine) touching
                    .add((Machine) loc.getWorld().getTileEntity(loc.getX(),
                            loc.getY(), loc.getZ() + 1));
            if (loc.getWorld()
                    .getTileEntity(loc.getX(), loc.getY(), loc.getZ()) instanceof Machine) touching
                    .add((Machine) loc.getWorld().getTileEntity(loc.getX(),
                            loc.getY(), loc.getZ() - 1));
            return touching;
        }
        return null;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        try
        {
            nbt.setByteArray("MachinePacket",
                    Serializer.serialize(this.MachinePacket));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        writeNBT(nbt);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        try
        {
            this.MachinePacket = (Packet) Serializer.deserialize(nbt
                    .getByteArray("MachinePacket"));
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        readNBT(nbt);
    }
    
    /**
     * As we handle the normal writeToNBT, we want you to use this one. You
     * could override the normal one, but you MUST know what you are doing and
     * want to manually implement packet saving.
     * 
     * @param nbt
     *            The NBT to write to
     */
    public abstract void writeNBT(NBTTagCompound nbt);
    
    /**
     * As we handle the normal readFromNBT, we want you to use this one. You
     * could override the normal one, but you MUST know what you are doing and
     * want to manually implement packet reading.
     * 
     * @param nbt
     *            The NBT to read from
     */
    public abstract void readNBT(NBTTagCompound nbt);
    
    /**
     * @return The maximum size units this can hold. At this point there is
     *         nothing stating how much something should take up, but this will
     *         be determined later (high chance of it being whatever values you
     *         implement!)
     */
    public abstract int maxStorage();
}
