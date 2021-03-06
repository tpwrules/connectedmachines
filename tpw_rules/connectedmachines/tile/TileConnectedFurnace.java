package tpw_rules.connectedmachines.tile;


import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.api.IConnectedMachine;
import tpw_rules.connectedmachines.api.ILinkable;
import tpw_rules.connectedmachines.api.IPowerConsumer;
import tpw_rules.connectedmachines.api.LinkFinder;
import tpw_rules.connectedmachines.network.ITileEntityPacketHandler;
import tpw_rules.connectedmachines.network.InputPacket;
import tpw_rules.connectedmachines.network.OutputPacket;
import tpw_rules.connectedmachines.network.PacketType;
import tpw_rules.connectedmachines.util.InventoryUtil;
import tpw_rules.connectedmachines.util.WCoord;

public class TileConnectedFurnace extends TileEntity implements ILinkable, ITileEntityPacketHandler, IPowerConsumer, IConnectedMachine {
    public ForgeDirection facing;
    public TileController link;
    public WCoord linkCoord;

    public String groupName;

    public int smeltTime;

    public ItemStack[] inv;

    public boolean cooking;

    public TileConnectedFurnace() {
        facing = ForgeDirection.UP;
        smeltTime = 0;
        inv = new ItemStack[2];
        groupName = "Default";
    }

    @Override
    public void setLink(TileController link, WCoord linkCoord) {
        this.link = link;
        this.linkCoord = linkCoord;
        OutputPacket packet = new OutputPacket(PacketType.UPDATE_LINK_STATE, 16, this);
        if (link == null)
            new WCoord(this.worldObj, 0, -1, 0).writeToPacket(packet.data);
        else
            linkCoord.writeToPacket(packet.data);
        packet.sendDimension();
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;
        if (link == null) return;
        if (inv[1] != null)
            inv[1] = link.yieldFinishedItem(groupName, inv[1]);
        if (!link.consumePower(1)) return;
        if (inv[0] == null && inv[1] == null) {
            inv[0] = link.getItemsForOperation(groupName, this);
            if (inv[0] != null)
                smeltTime = 100;
        }
        if (smeltTime == 0) return;
        if (!link.consumePower(9)) {
            if (cooking) {
                cooking = false;
                sendFurnaceState();
            }
            return;
        }
        if (--smeltTime == 0) {
            inv[1] = FurnaceRecipes.smelting().getSmeltingResult(inv[0]).copy();
            inv[0] = null;
            cooking = false;
            sendFurnaceState();
        }
        if (!cooking && smeltTime > 0) {
            cooking = true;
            sendFurnaceState();
        }
    }

    @Override
    public int getOperationStackSize(ItemStack stack) {
        ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(stack);
        if (result == null) return 0;
        return result.stackSize;
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

    public void sendFurnaceState() {
        OutputPacket packet = new OutputPacket(PacketType.MACHINE_UPDATE, 1, this);
        try {
            packet.data.writeBoolean(cooking);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        packet.sendDimension();
    }

    @Override
    public int getBufferSize() {
        return 100;
    }

    @Override
    public String getGUIName() {
        return "Connected Furnace";
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public void setGroupName(String name) {
        groupName = name;
        OutputPacket packet = new OutputPacket(PacketType.GROUP_UPDATE, 64, this);
        try {
            packet.data.writeUTF(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        packet.sendServer();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        facing = ForgeDirection.getOrientation(tag.getByte("facing"));
        groupName = tag.getString("groupName");
        if (groupName.equals("")) groupName = "Default";
        if (tag.hasKey("inventory"))
            inv = InventoryUtil.readInventory(tag.getTagList("inventory"));
        smeltTime = tag.getInteger("smeltTime");
        cooking = smeltTime > 0;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte("facing", (byte)facing.ordinal());
        tag.setString("groupName", groupName);
        tag.setTag("inventory", InventoryUtil.writeInventory(inv));
        tag.setInteger("smeltTime", smeltTime);
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
            case GROUP_UPDATE:
                try {
                    groupName = packet.data.readUTF();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case MACHINE_UPDATE:
                try {
                    cooking = packet.data.readBoolean();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
                break;
        }
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        this.readFromNBT(packet.customParam1);
        linkCoord = WCoord.readFromNBT(packet.customParam1, "link");
        if (linkCoord.y >= 0) {
            link = (TileController)linkCoord.getTileEntity();
            worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        if (linkCoord == null)
            linkCoord = new WCoord(this.worldObj, 0, -1, 0);
        linkCoord.writeToNBT(tag, "link");
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }
}
