package tpw_rules.connectedmachines.tile;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.api.ILinkable;
import tpw_rules.connectedmachines.api.LinkFinder;
import tpw_rules.connectedmachines.network.ITileEntityPacketHandler;
import tpw_rules.connectedmachines.network.InputPacket;
import tpw_rules.connectedmachines.network.OutputPacket;
import tpw_rules.connectedmachines.network.PacketType;
import tpw_rules.connectedmachines.util.WCoord;

public class TileMachineLink extends TileEntity implements ITileEntityPacketHandler, ILinkable {
    public boolean[] connectedNeighbors;

    public TileController link;
<<<<<<< HEAD
    public WCoord linkPos;
=======
    public WCoord linkCoord;
>>>>>>> 85c4699f419471c45a4a2943c61ecb98b96b494a

    public TileMachineLink() {
        connectedNeighbors = new boolean[6]; // which neighbors we are connected to
    }

    @Override
<<<<<<< HEAD
    public void setLink(TileController link, WCoord linkPos) {
        this.link = link;
        this.linkPos = linkPos;
=======
    public void setLink(TileController link, WCoord linkCoord) {
        this.link = link;
        this.linkCoord = linkCoord;
        OutputPacket packet = new OutputPacket(PacketType.UPDATE_LINK_STATE, 16, this);
        if (link == null)
            new WCoord(this.worldObj, 0, -1, 0).writeToPacket(packet.data);
        else
            linkCoord.writeToPacket(packet.data);
        packet.sendDimension();
>>>>>>> 85c4699f419471c45a4a2943c61ecb98b96b494a
    }

    @Override
    public TileController getLink() {
        return this.link;
    }

    @Override
    public void placed() {
        performNeighborCheck();
        LinkFinder.updateNetwork(this);
    }

    @Override
    public void broken() {
        if (link != null)
            link.resetNetwork();
    }

    @Override
    public void updateEntity() {
    }

    public void performNeighborCheck() {
        // create packet to send sync data
        OutputPacket packet = new OutputPacket(PacketType.MACHINE_LINK_REDRAW, 0, this);
        // check all sides for connections
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            boolean connected = (new WCoord(this).move(side).getTileEntity()) instanceof ILinkable;
            connectedNeighbors[side.ordinal()] = connected;
            try {
                packet.data.writeBoolean(connected);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        packet.sendDimension(); // send the packet off to the client
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
            case UPDATE_LINK_STATE:
                linkCoord = WCoord.readFromPacket(packet.data);
                if (linkCoord.y >= 0)
                    link = (TileController)linkCoord.getTileEntity();
                else
                    link = null;
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
