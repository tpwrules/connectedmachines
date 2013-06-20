package tpw_rules.connectedmachines.tile;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.api.ILinkable;
import tpw_rules.connectedmachines.api.IPowerProvider;
import tpw_rules.connectedmachines.api.LinkFinder;
import tpw_rules.connectedmachines.network.ITileEntityPacketHandler;
import tpw_rules.connectedmachines.network.InputPacket;
import tpw_rules.connectedmachines.network.OutputPacket;
import tpw_rules.connectedmachines.network.PacketType;
import tpw_rules.connectedmachines.util.InventoryUtil;
import tpw_rules.connectedmachines.util.WCoord;

public class TileConnectedGenerator extends TileEntity implements ILinkable, ITileEntityPacketHandler, IInventory, IPowerProvider {
    public ForgeDirection facing;
    public TileController link;
    public WCoord linkCoord;

    private ItemStack[] inv;

    private int powerBuffer;

    public TileConnectedGenerator() {
        facing = ForgeDirection.UP;
        linkCoord = new WCoord(this.worldObj, 0, -1, 0);
        inv = new ItemStack[9];
        powerBuffer = 0;
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
    public int getSizeInventory() {
        return inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inv[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inv[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = getStackInSlot(slot);
        if (stack == null) return null;
        if (stack.stackSize <= amount) {
            setInventorySlotContents(slot, null);
        } else {
            stack = stack.splitStack(amount);
            if (stack.stackSize == 0)
                setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
            setInventorySlotContents(slot, null);
        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public boolean isStackValidForSlot(int slot, ItemStack stack) {
        return TileEntityFurnace.isItemFuel(stack);
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public String getInvName() {
        return "Connected Generator";
    }

    @Override
    public boolean isInvNameLocalized() {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        facing = ForgeDirection.getOrientation(tag.getByte("facing"));
        if (tag.hasKey("inventory"))
            inv = InventoryUtil.readInventory(tag.getTagList("inventory"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte("facing", (byte)facing.ordinal());
        tag.setTag("inventory", InventoryUtil.writeInventory(inv));
    }

    @Override
    public int getPower(int request_) {
        int request = (request_/10)+1;
        int output = powerBuffer;
        if (output >= request) {
            powerBuffer = output-request;
            return output*10;
        }
        int remaining = request-output;
        for (int slot = 0; slot < getSizeInventory(); slot++) {
            ItemStack stack = getStackInSlot(slot);
            if (stack == null) continue;
            int burnTime = TileEntityFurnace.getItemBurnTime(stack);
            int itemCount = remaining/burnTime;
            if (itemCount*burnTime < remaining)
                itemCount++;
            if (itemCount > stack.getMaxStackSize())
                itemCount = stack.getMaxStackSize();
            ItemStack fuel = decrStackSize(slot, itemCount);
            output += (fuel.stackSize*burnTime);
            remaining = (request-output);
            if (output >= request) break;
        }
        if (output >= request)
            powerBuffer = output-request;
        else
            powerBuffer = 0;
        return output*10;
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
        linkCoord.writeToNBT(tag, "link");
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }
}
