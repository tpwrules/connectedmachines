package tpw_rules.connectedmachines.tile;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.api.ILinkable;
import tpw_rules.connectedmachines.api.IPowerConsumer;
import tpw_rules.connectedmachines.api.IPowerProvider;
import tpw_rules.connectedmachines.api.LinkFinder;
import tpw_rules.connectedmachines.util.WCoord;

import java.util.ArrayList;

public class TileController extends TileEntity implements ILinkable, IPowerConsumer {
    public ForgeDirection facing;

    public ArrayList<ILinkable> links;

    private int powerBuffer;
    private int powerBufferMax;

    public TileController() {
        facing = ForgeDirection.UP;
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;
        if (links == null)
            findMachines();
        if (links == null)
            return;
        for (ILinkable machine : links) {
            if (machine instanceof IPowerProvider) {
                if (powerBuffer < powerBufferMax)
                    powerBuffer += ((IPowerProvider)machine).getPower(powerBufferMax-powerBuffer);
            }
        }
        consumePower(5);
    }

    @Override
    public void setLink(TileController link, WCoord linkCoord) {
    }

    @Override
    public TileController getLink() {
        return this;
    }

    @Override
    public void placed() {

    }

    @Override
    public void broken() {
        LinkFinder.findMachines(this, true);
    }

    public void findMachines() {
        links = LinkFinder.findMachines(this, false);
        if (links == null) return;
        powerBufferMax = 0;
        for (ILinkable machine : links) {
            if (machine instanceof IPowerConsumer)
                powerBufferMax += ((IPowerConsumer)machine).getBufferSize();
        }
    }

    public void resetNetwork() {
        LinkFinder.findMachines(this, true);
        links = null;
    }

    @Override
    public int getBufferSize() {
        return 100;
    }

    public boolean consumePower(int amount) {
        if (powerBuffer < amount)
            return false;
        powerBuffer -= amount;
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        facing = ForgeDirection.getOrientation(tag.getByte("facing"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte("facing", (byte)facing.ordinal());
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
