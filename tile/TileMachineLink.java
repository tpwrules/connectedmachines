package tpw_rules.connectedmachines.tile;


import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.util.Util;
import tpw_rules.connectedmachines.util.WCoord;

public class TileMachineLink extends TileEntity {
    public boolean checkNeighbors; // set when the link needs to check its neighbors for connections
    public boolean[] connectedNeighbors;

    public TileMachineLink() {
        checkNeighbors = true;
        connectedNeighbors = new boolean[6]; // which neighbors we are connected to
    }

    @Override
    public void updateEntity() {
        if (checkNeighbors) { // update neighbors if necessary
            performNeighborCheck();
            Util.log("Change!", connectedNeighbors[ForgeDirection.DOWN.ordinal()]);
            worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord); // make sure we look connected
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
