package info.coremodding.gps.api.meu;

import info.coremodding.api.locations.Location;
import info.coremodding.gps.api.machine.Machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The power manager for the mod
 */
public class MEUSystemManager
{
    
    /**
     * @param loc
     *            The location
     * @return Gets the machine at the given location
     */
    public static Machine getAt(Location loc)
    {
        if (loc.getWorld() != null) if (loc.getWorld().getTileEntity(
                loc.getX(), loc.getY(), loc.getZ()) instanceof Machine) return ((Machine) loc
                .getWorld().getTileEntity(loc.getX(), loc.getY(), loc.getZ()));
        return null;
    }
    
    /**
     * @param loc
     *            The location
     * @return Touching machines
     */
    public static List<Machine> getTouching(Location loc)
    {
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
    
    /**
     * @param _packets
     *            The packets to merge
     * @return The packets merged
     */
    public static List<MEUPacket> mergePackets(List<MEUPacket> _packets)
    {
        HashMap<String, MEUPacket> packets = new HashMap<>();
        for (MEUPacket packet1 : _packets)
        {
            if (packets.get(packet1.getName()) == null) packets.put(
                    packet1.getName(), packet1);
            else
            {
                MEUPacket old = packets.get(packet1.getName());
                old.setMeta(old.getMeta() + packet1.getMeta());
                packets.remove(old.getName());
                packets.put(old.getName(), old);
            }
        }
        ArrayList<MEUPacket> returnable = new ArrayList<>();
        for (MEUPacket packet : packets.values())
        {
            returnable.add(packet);
        }
        return returnable;
    }
}
