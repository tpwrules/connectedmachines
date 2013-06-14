package tpw_rules.connectedmachines.api;


import tpw_rules.connectedmachines.tile.TileController;

public interface ILinkable {
    public boolean connected = false;
    public TileController tileController = null;
}
