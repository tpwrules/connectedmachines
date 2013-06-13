package tpw_rules.connectedmachines.common;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import tpw_rules.connectedmachines.Config;
import tpw_rules.connectedmachines.block.*;

@Mod(modid="connectedmachines", name="Connected Machines", version="1.0")

public class ConnectedMachines {
    @Mod.Instance
    public static ConnectedMachines instance;

    @SidedProxy(clientSide="tpw_rules.connectedmachines.client.ClientProxy", serverSide="tpw_rules.connectedmachines.common.CommonProxy")
    public static CommonProxy proxy = new CommonProxy();

    // block instances
    public static BlockController blockController;

    public static Config config;

    @Mod.PreInit
    public void PreInitialize(FMLPreInitializationEvent e) {
        config = new Config();
        config.loadConfig(e);
    }

    @Mod.Init
    public void Initialize(FMLInitializationEvent e) {
        // do block registration
        GameRegistry.registerBlock(blockController, "CMController");
        LanguageRegistry.addName(blockController, "Machine Controller");
    }
}
