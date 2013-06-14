package tpw_rules.connectedmachines.util;


import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.Configuration;

public class Config {
    public static int blockControllerID;
    public static int blockMachineLinkID;

    public static void loadConfig(FMLPreInitializationEvent e) {
        Configuration config = new Configuration(e.getSuggestedConfigurationFile());
        config.load();

        blockControllerID = config.getBlock("Controller Block", 2600).getInt();
        blockMachineLinkID = config.getBlock("Machine Link", 2601).getInt();

        config.save();
    }
}