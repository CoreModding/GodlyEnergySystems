package info.coremodding.gps;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * @author James
 *         <p>
 *         The main class
 */
@Mod(modid = "zeus_GodlyPowerSystems", name = "Godly Power Systems", version = "Alpha Dev 1")
public class GodlyPowerSystems {

    /**
     * The mod instance used by forge
     */
    @Instance("zeus_GodlyPowerSystems")
    public static GodlyPowerSystems gps;

    /**
     * @param evt The event that triggered the method
     */
    @EventHandler
    public void preinit(FMLPreInitializationEvent evt) {

    }
}
