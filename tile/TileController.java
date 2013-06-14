package tpw_rules.connectedmachines.tile;


public class TileController extends TileLinkable {
    public TileController() {
        setLink(this); // we are linked to ourselves
    }
}
