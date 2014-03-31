package info.coremodding.gps.api;

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
                                // Whoops
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
     * Where you handle what you'd normally do in updateEntity.
     */
    public abstract void Update();
    
    /**
     * @param packet
     *            The packet to check
     * @param machine
     *            The machine to check
     * @return Should the packet be sent to the machine?
     */
    public abstract boolean ShouldAttemptToSend(Packet.SubPacket packet,
            Machine machine);
    
    /**
     * @param packet
     *            The packet to accept
     * @param machine
     *            The machine that sent it
     * @return Should we accept the packet from this machine?
     */
    public abstract boolean ShouldAccept(Packet.SubPacket packet,
            Machine machine);
    
    /**
     * @return Touching machines
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
     * @param nbt
     *            The NBT to write to
     */
    public abstract void writeNBT(NBTTagCompound nbt);
    
    /**
     * @param nbt
     *            The NBT to read from
     */
    public abstract void readNBT(NBTTagCompound nbt);
    
    /**
     * @return The maximum size units this can hold
     */
    public abstract int maxStorage();
}
