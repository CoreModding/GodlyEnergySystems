package info.coremodding.gps.api;

import info.coremodding.api.locations.GPSLocation;
import info.coremodding.gps.internal.Packet;

import java.util.ArrayList;

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
    public Packet MachinePacket;
    
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
                            machine.MachinePacket.mergePacket(packet);
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
        GPSLocation loc = new GPSLocation(this.xCoord, this.yCoord,
                this.zCoord, this.worldObj);
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
}
