package tpw_rules.connectedmachines.tile;


import net.minecraft.tileentity.TileEntity;
import tpw_rules.connectedmachines.api.ILinkable;

public class TileController extends TileEntity implements ILinkable {
    public boolean linkConnected = false;
    public TileController tileController = null;

    public void setLink(TileController newController) {
        tileController = newController;
        linkConnected = !(newController == null);
    }
}
