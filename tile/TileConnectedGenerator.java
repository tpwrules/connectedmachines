package tpw_rules.connectedmachines.tile;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.api.ILinkable;
import tpw_rules.connectedmachines.api.LinkFinder;
import tpw_rules.connectedmachines.util.WCoord;

public class TileConnectedGenerator extends TileEntity implements ILinkable {
    public ForgeDirection facing;
    public TileController link;
    public WCoord linkPos;

    public TileConnectedGenerator() {
        facing = ForgeDirection.UP;
    }

    @Override
    public void setLink(TileController link, WCoord linkPos) {
        this.link = link;
        this.linkPos = linkPos;
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
