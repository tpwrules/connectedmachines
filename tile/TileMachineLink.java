package tpw_rules.connectedmachines.tile;


import com.sun.corba.se.impl.orb.ParserTable;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.util.WCoord;

public class TileMachineLink extends TileEntity {
    public boolean checkNeighbors; // set when the link needs to check its neighbors for connections
    public boolean[] connectedNeighbors;

    public TileMachineLink() {
        checkNeighbors = true; // we need to check our neighbors
        connectedNeighbors = new boolean[6]; // which neighbors we are connected to
    }

    @Override
    public void updateEntity() {
        if (checkNeighbors) { // update neighbors if necessary
            performNeighborCheck();
        }
    }

    public void performNeighborCheck() {
        // check all sides for connections
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity tile = new WCoord(this).adjacent(side).getTileEntity();
            connectedNeighbors[side.ordinal()] = tile instanceof TileMachineLink;
        }
        checkNeighbors = false; // we don't need to check neighbors anymore
    }
}
