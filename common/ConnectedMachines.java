package tpw_rules.connectedmachines.common;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import tpw_rules.connectedmachines.block.*;
import tpw_rules.connectedmachines.util.Config;

@Mod(modid="connectedmachines", name="Connected Machines", version="1.0")

public class ConnectedMachines {
    @Mod.Instance
    public static ConnectedMachines instance;

    @SidedProxy(clientSide="tpw_rules.connectedmachines.client.ClientProxy", serverSide="tpw_rules.connectedmachines.common.CommonProxy")
    public static CommonProxy proxy = new CommonProxy();

    // block instances
    public static BlockController blockController;

    public static Config config;

    public static TabMachines tabMachines;

    @Mod.PreInit
    public void PreInitialize(FMLPreInitializationEvent e) {
        config = new Config();
        config.loadConfig(e);
    }

    @Mod.Init
    public void Initialize(FMLInitializationEvent e) {
        tabMachines = new TabMachines();
        blockController = new BlockController(config.blockControllerID);
    }
}
