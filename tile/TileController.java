package tpw_rules.connectedmachines.tile;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.api.*;
import tpw_rules.connectedmachines.network.ITileEntityPacketHandler;
import tpw_rules.connectedmachines.network.InputPacket;
import tpw_rules.connectedmachines.network.OutputPacket;
import tpw_rules.connectedmachines.network.PacketType;
import tpw_rules.connectedmachines.util.WCoord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TileController extends TileEntity implements ILinkable, IPowerConsumer, ITileEntityPacketHandler {
    public ForgeDirection facing;

    public ArrayList<ILinkable> links;

    public HashMap<String, TileInputOutput> ioPorts;
    public ArrayList<String> ioPortList;

    public HashMap<String, String[]> groups;

    public int powerBuffer;
    public int powerBufferMax;

    private boolean guiUpdateNecessary;

    public TileController() {
        facing = ForgeDirection.UP;
        ioPorts = new HashMap<String, TileInputOutput>();
        ioPortList = new ArrayList<String>();
        groups = new HashMap<String, String[]>();
        String[] t = {"DefaultI", "DefaultO"};
        groups.put("Default", t);
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
            if (machine instanceof TileInputOutput)
                ioPorts.put(((TileInputOutput)machine).name, (TileInputOutput)machine);
            else if (machine instanceof IPowerConsumer)
                powerBufferMax += ((IPowerConsumer)machine).getBufferSize();
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

    public ItemStack getItemsForOperation(String group, IConnectedMachine machine) {
        if (!groups.containsKey(group)) return null;
        String inputName = groups.get(group)[0];
        if (!ioPorts.containsKey(inputName)) return null;
        TileInputOutput inputPort = ioPorts.get(inputName);
        ItemStack output = null;
        int remaining = 0;
        for (int slot = 0; slot < inputPort.getSizeInventory(); slot++) {
            ItemStack stack = inputPort.getStackInSlot(slot);
            if (stack == null) continue;
            if (remaining == 0)
                remaining = machine.getOperationStackSize(stack);
            if (remaining > 0 && remaining <= stack.stackSize) {
                output = stack.splitStack(remaining);
                if (stack.stackSize == 0)
                    inputPort.setInventorySlotContents(slot, null);
                inputPort.onInventoryChanged();
                break;
            }
        }
        return output;
    }

    public ItemStack yieldFinishedItem(String group, ItemStack stack) {
        if (!groups.containsKey(group)) return stack;
        String outputName = groups.get(group)[2];
        if (!ioPorts.containsKey(outputName)) return stack;
        TileInputOutput outputPort = ioPorts.get(outputName);
        boolean changed = false;
        for (int slot = 0; slot < outputPort.getSizeInventory(); slot++) {
            ItemStack outStack = outputPort.getStackInSlot(slot);
            if (outStack == null) continue;
            if (!outStack.isStackable()) continue;
            if (!(stack.itemID == outStack.itemID && (!stack.getHasSubtypes() || stack.getItemDamage() == outStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, outStack))) {
                continue; // stacks aren't equal
            }
            if (stack.stackSize+outStack.stackSize <= outStack.getMaxStackSize()) {
                outStack.stackSize += stack.stackSize;
                outputPort.onInventoryChanged();
                return null;
            } else {
                stack.stackSize -= outStack.getMaxStackSize()-outStack.stackSize;
                outStack.stackSize = outStack.getMaxStackSize();
                changed = true;
            }
        }
        for (int slot = 0; slot < outputPort.getSizeInventory(); slot++) {
            ItemStack outStack = outputPort.getStackInSlot(slot);
            if (outStack != null) continue;
            changed = true;
            outputPort.setInventorySlotContents(slot, stack);
            stack = null;
        }
        if (changed)
            outputPort.onInventoryChanged();
        return stack;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        facing = ForgeDirection.getOrientation(tag.getByte("facing"));
        powerBuffer = tag.getInteger("power");
        NBTTagList groupList = tag.getTagList("groups");
        if (groupList == null) return;
        groups.clear();
        for (int i = 0; i < groupList.tagCount(); i++) {
            NBTTagList oneGroup = (NBTTagList)groupList.tagAt(i);
            String name = oneGroup.tagAt(0).toString();
            String[] data = {oneGroup.tagAt(1).toString(), oneGroup.tagAt(2).toString()};
            groups.put(name, data);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte("facing", (byte)facing.ordinal());
        tag.setInteger("power", powerBuffer);
        NBTTagList groupList = new NBTTagList();
        for (Map.Entry<String, String[]> entry : groups.entrySet()) {
            NBTTagList oneGroup = new NBTTagList();
            oneGroup.appendTag(new NBTTagString("harp", entry.getKey()));
            oneGroup.appendTag(new NBTTagString("darp", entry.getValue()[0]));
            oneGroup.appendTag(new NBTTagString("larp", entry.getValue()[1]));
            groupList.appendTag(oneGroup);
        }
        tag.setTag("groups", groupList);
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
        ioPortList.clear();
        NBTTagList nameList = packet.customParam1.getTagList("names");
        for (int i = 0; i < nameList.tagCount(); i++) {
            ioPortList.add(nameList.tagAt(i).toString());
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        NBTTagList nameList = new NBTTagList();
        for (String name : ioPorts.keySet()) {
            nameList.appendTag(new NBTTagString("farp", name));
        }
        tag.setTag("names", nameList);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }
}
