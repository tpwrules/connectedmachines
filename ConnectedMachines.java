package tpw_rules.connectedmachines;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid="connected_machines", name="Connected Machines", version="1.0")

public class ConnectedMachines {
    @Mod.Instance
    public static ConnectedMachines instance;

    @SidedProxy(clientSide="tpw_rules.connectedmachines.ClientProxy", serverSide="tpw_rules.connectedmachines.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Init
    public void Initialize(FMLInitializationEvent e) {

    }
}
