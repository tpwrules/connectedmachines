package tpw_rules.connectedmachines.gui;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import tpw_rules.connectedmachines.tile.TileInputOutput;

public class ContainerInputOutput extends Container {
    protected TileInputOutput tileEntity;

    public ContainerInputOutput(InventoryPlayer inventoryPlayer, TileInputOutput te) {
        tileEntity = te;

        int i;
        int j;

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 3; ++j)
            {
                this.addSlotToContainer(new Slot(te, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 30+84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 30+142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack stack;
        Slot slotObject = (Slot)inventorySlots.get(slot);
        if (slotObject == null || !slotObject.getHasStack()) return null;

        ItemStack slotStack = slotObject.getStack();
        stack = slotStack.copy();

        // merge with player inventory if source is TE
        if (slot < 9) {
            if (!this.mergeItemStack(slotStack, 9, 45, true))
                return null;
        } else if (!this.mergeItemStack(slotStack, 0, 9, false)) {
            return null;
        }

        if (slotStack.stackSize == 0)
            slotObject.putStack(null);
        else
            slotObject.onSlotChanged();

        if (slotStack.stackSize == stack.stackSize)
            return null;

        slotObject.onPickupFromSlot(player, slotStack);

        return stack;
    }
}
