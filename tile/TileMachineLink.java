package tpw_rules.connectedmachines.tile;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
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
            boolean connected = new WCoord(this).move(side).getBlockID() == Config.blockMachineLinkID;
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
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        // retrieve which neighbors we are connected to
        byte[] sides = tag.getByteArray("connectedNeighbors");
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            connectedNeighbors[side.ordinal()] = sides[side.ordinal()] != 0;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        // write which neighbors we are connected to
        byte[] sides = new byte[6];
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            sides[side.ordinal()] = connectedNeighbors[side.ordinal()] ? (byte)1 : (byte)0;
        }
        tag.setByteArray("connectedNeighbors", sides);
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

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        this.readFromNBT(packet.customParam1);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }
}
