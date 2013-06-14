package tpw_rules.connectedmachines.tile;


import net.minecraft.tileentity.TileEntity;
import tpw_rules.connectedmachines.api.ILinkable;

public class TileLinkable extends TileEntity implements ILinkable {
    public boolean linkConnected;
    public TileController tileController;

    public void setLink(TileController newController) {
        tileController = newController;
        linkConnected = !(newController == null);
    }
}
