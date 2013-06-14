package tpw_rules.connectedmachines.api;


import tpw_rules.connectedmachines.tile.TileController;

public interface ILinkable {
    public void setLink(TileController newController);
}
