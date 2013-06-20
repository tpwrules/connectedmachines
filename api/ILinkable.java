package tpw_rules.connectedmachines.api;


import tpw_rules.connectedmachines.tile.TileController;
import tpw_rules.connectedmachines.util.WCoord;

public interface ILinkable {
    public void setLink(TileController newController, WCoord controllerPos);
    public TileController getLink();
}
