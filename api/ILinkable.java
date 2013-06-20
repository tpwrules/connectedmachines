package tpw_rules.connectedmachines.api;


import tpw_rules.connectedmachines.tile.TileController;
import tpw_rules.connectedmachines.util.WCoord;

public interface ILinkable {
<<<<<<< HEAD
    public void setLink(TileController newController, WCoord controllerPos);
=======
    public void setLink(TileController newController, WCoord controllerCoord);
>>>>>>> 85c4699f419471c45a4a2943c61ecb98b96b494a
    public TileController getLink();
    public void placed();
    public void broken();
}
