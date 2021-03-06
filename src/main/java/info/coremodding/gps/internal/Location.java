package info.coremodding.gps.internal;

import net.minecraft.world.World;

/**
 * @author James A location for easy transfer
 */
public class Location
{
    
    /**
     * The world
     */
    private World world = null;
    
    /**
     * The x postion
     */
    private int   x     = 0;
    
    /**
     * The y position
     */
    private int   y     = -1;
    
    /**
     * The z position
     */
    private int   z     = 0;
    
    /**
     * @param x
     *            The x position
     * @param z
     *            The z position
     */
    public Location(int x, int z)
    {
        this.setZ(z);
        this.setX(x);
    }
    
    /**
     * @param x
     *            The x position
     * @param y
     *            The y position
     * @param z
     *            The z position
     */
    public Location(int x, int y, int z)
    {
        this.setY(y);
        this.setZ(z);
        this.setX(x);
    }
    
    /**
     * @param x
     *            The x position
     * @param y
     *            The y position
     * @param z
     *            The z position
     * @param world
     *            The world
     */
    public Location(int x, int y, int z, World world)
    {
        this.setY(y);
        this.setZ(z);
        this.setX(x);
        this.setWorld(world);
    }
    
    /**
     * @param x
     *            The x position
     * @param z
     *            The z position
     * @param world
     *            The world
     */
    public Location(int x, int z, World world)
    {
        this.setZ(z);
        this.setX(x);
        this.setWorld(world);
    }
    
    /**
     * @return The world
     */
    public World getWorld()
    {
        return this.world;
    }
    
    /**
     * @return The x position
     */
    public int getX()
    {
        return this.x;
    }
    
    /**
     * @return The y position
     */
    public int getY()
    {
        return this.y;
    }
    
    /**
     * @return The z position
     */
    public int getZ()
    {
        return this.z;
    }
    
    /**
     * @param world
     *            The world
     */
    void setWorld(World world)
    {
        this.world = world;
    }
    
    /**
     * @param x
     *            The x position
     */
    void setX(int x)
    {
        this.x = x;
    }
    
    /**
     * @param y
     *            The y position
     */
    void setY(int y)
    {
        this.y = y;
    }
    
    /**
     * @param z
     *            The z position
     */
    void setZ(int z)
    {
        this.z = z;
    }
}
