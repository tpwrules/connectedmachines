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
import tpw_rules.connectedmachines.network.ITileEntityPacketHandler;
import tpw_rules.connectedmachines.network.InputPacket;
import tpw_rules.connectedmachines.network.OutputPacket;
import tpw_rules.connectedmachines.network.PacketType;
import tpw_rules.connectedmachines.util.WCoord;

import java.util.ArrayList;
import java.util.HashMap;

public class TileController extends TileEntity implements ILinkable, IPowerConsumer, ITileEntityPacketHandler {
    public ForgeDirection facing;

    public ArrayList<ILinkable> links;
    public HashMap<String, TileInputOutput> ioPorts;
    public ArrayList<String> ioPortList;

    public int powerBuffer;
    public int powerBufferMax;

    private boolean guiUpdateNecessary;

    public TileController() {
        facing = ForgeDirection.UP;
        ioPorts = new HashMap<String, TileInputOutput>();
        ioPortList = new ArrayList<String>();
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
        guiUpdateNecessary = true;
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

    public void sendGuiUpdate() {
        if (!guiUpdateNecessary) return;
        guiUpdateNecessary = false;
        OutputPacket packet = new OutputPacket(PacketType.GUI_UPDATE, 8, this);
        try {
            packet.data.writeInt(powerBuffer);
            packet.data.writeInt(powerBufferMax);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        packet.sendDimension();
    }

    public void findMachines() {
        links = LinkFinder.findMachines(this, false);
        if (links == null) return;
        powerBufferMax = 0;
        ioPorts.clear();
        for (ILinkable machine : links) {
            if (machine instanceof IPowerConsumer)
                powerBufferMax += ((IPowerConsumer)machine).getBufferSize();
            else if (machine instanceof TileInputOutput) {
                ioPorts.put(((TileInputOutput)machine).name, (TileInputOutput)machine);
            }
        }
        OutputPacket packet = new OutputPacket(PacketType.NAME_UPDATE, 64, this);
        try {
            packet.data.writeBoolean(true);
            packet.data.writeInt(ioPorts.size());
            for (String name : ioPorts.keySet()) {
                packet.data.writeUTF(name);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        packet.sendDimension();
    }

    public void changeIOName(TileInputOutput tile, String next) {
        ioPorts.remove(tile.name);
        ioPorts.put(next, tile);
        OutputPacket packet = new OutputPacket(PacketType.NAME_UPDATE, 64, this);
        try {
            packet.data.writeBoolean(false);
            packet.data.writeUTF(tile.name);
            packet.data.writeUTF(next);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        packet.sendDimension();
        tile.name = next;
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
        powerBuffer = tag.getInteger("power");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte("facing", (byte)facing.ordinal());
        tag.setInteger("power", powerBuffer);
    }

    @Override
    public void handlePacket(InputPacket packet) {
        try {
            switch (packet.type) {
                case GUI_UPDATE:
                    powerBuffer = packet.data.readInt();
                    powerBufferMax = packet.data.readInt();
                    break;
                case NAME_UPDATE:
                    if (packet.data.readBoolean()) {
                        ioPortList.clear();
                        int count = packet.data.readInt();
                        for (int i = 0; i < count; i++) {
                            ioPortList.add(packet.data.readUTF());
                        }
                    } else {
                        ioPortList.remove(packet.data.readUTF());
                        ioPortList.add(packet.data.readUTF());
                    }
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
