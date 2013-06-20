package tpw_rules.connectedmachines.tile;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.api.ILinkable;
<<<<<<< HEAD
=======
import tpw_rules.connectedmachines.api.LinkFinder;
import tpw_rules.connectedmachines.network.ITileEntityPacketHandler;
import tpw_rules.connectedmachines.network.InputPacket;
import tpw_rules.connectedmachines.network.OutputPacket;
import tpw_rules.connectedmachines.network.PacketType;
>>>>>>> 85c4699f419471c45a4a2943c61ecb98b96b494a
import tpw_rules.connectedmachines.util.WCoord;

public class TileConnectedFurnace extends TileEntity implements ILinkable, ITileEntityPacketHandler {
    public ForgeDirection facing;
    public TileController link;
<<<<<<< HEAD
    public WCoord linkPos;
=======
    public WCoord linkCoord;
>>>>>>> 85c4699f419471c45a4a2943c61ecb98b96b494a

    public TileConnectedFurnace() {
        facing = ForgeDirection.UP;
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
        LinkFinder.updateNetwork(this);
    }

    @Override
    public void broken() {
        if (link != null)
            link.resetNetwork();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        facing = ForgeDirection.getOrientation(tag.getByte("facing"));
        linkPos = WCoord.readFromNBT(tag, "linkPos");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte("facing", (byte)facing.ordinal());
        linkPos.writeToNBT(tag, "linkPos");
    }

    @Override
    public void handlePacket(InputPacket packet) {
        switch (packet.type) {
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
