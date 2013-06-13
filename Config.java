package tpw_rules.connectedmachines;


import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.Configuration;

public class Config {
    public static int blockControllerID;

    public static void loadConfig(FMLPreInitializationEvent e) {
        Configuration config = new Configuration(e.getSuggestedConfigurationFile());
        config.load();

        blockControllerID = config.getBlock("Controller Block", 2600).getInt();
    }
}
