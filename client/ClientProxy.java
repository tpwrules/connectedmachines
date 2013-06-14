package tpw_rules.connectedmachines.client;


import cpw.mods.fml.client.registry.RenderingRegistry;
import tpw_rules.connectedmachines.block.BlockMachineLink;
import tpw_rules.connectedmachines.common.CommonProxy;
import tpw_rules.connectedmachines.common.ConnectedMachines;
import tpw_rules.connectedmachines.render.RenderMachineLink;

public class ClientProxy extends CommonProxy {
    public void registerRenderers() {
        int renderID;
        // machine link
        renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderMachineLink renderMachineLink = new RenderMachineLink(renderID);
        ConnectedMachines.blockMachineLink.renderID = renderID;
        RenderingRegistry.registerBlockHandler(renderMachineLink);
    }
}
