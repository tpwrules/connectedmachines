package tpw_rules.connectedmachines.tile;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.network.ITileEntityPacketHandler;
import tpw_rules.connectedmachines.network.InputPacket;
import tpw_rules.connectedmachines.network.OutputPacket;
import tpw_rules.connectedmachines.network.PacketType;
import tpw_rules.connectedmachines.util.Config;
import tpw_rules.connectedmachines.util.WCoord;

public class TileMachineLink extends TileEntity implements ITileEntityPacketHandler {
    public boolean checkNeighbors; // set when the link needs to check its neighbors for connections
    public boolean[] connectedNeighbors;
    public boolean special = false;

    public TileMachineLink() {
        checkNeighbors = true;
        connectedNeighbors = new boolean[6]; // which neighbors we are connected to
    }

    @Override
    public void updateEntity() {
        if (checkNeighbors && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) { // update neighbors if necessary
            performNeighborCheck();
        } else {
            checkNeighbors = false;
        }
    }

    public void performNeighborCheck() {
        // create packet to send sync data
        OutputPacket packet = new OutputPacket(PacketType.MACHINE_LINK_REDRAW, 0, this);
        // check all sides for connections
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            boolean connected = new WCoord(this).adjacent(side).getBlockID() == Config.blockMachineLinkID;
            connectedNeighbors[side.ordinal()] = connected;
            try {
                packet.data.writeBoolean(connected);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        packet.sendDimension(); // send the packet off to the client
        checkNeighbors = false; // we don't need to check neighbors anymore
    }

    @Override
    public void handlePacket(InputPacket packet) {
        switch (packet.type) {
            case MACHINE_LINK_REDRAW:
                for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    try {
                        connectedNeighbors[side.ordinal()] = packet.data.readBoolean();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
                break;
        }
    }
}
