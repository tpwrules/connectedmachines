package tpw_rules.connectedmachines.util;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.Random;

public class InventoryUtil {
    public static void vomitItems(World world, int x, int y, int z) {
        Random rand = new Random();

        IInventory inv = (IInventory)world.getBlockTileEntity(x, y, z);
        if (inv == null) return;

        for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if (stack == null) continue;

            EntityItem entityItem = new EntityItem(world,
                    x+(rand.nextFloat()*.8+.1), y+(rand.nextFloat()*.8+.1),
                    z+(rand.nextFloat()*.8+.1), new ItemStack(stack.itemID,
                    stack.stackSize, stack.getItemDamage()));
            if (stack.hasTagCompound())
                entityItem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
            entityItem.motionX = rand.nextGaussian() * .05;
            entityItem.motionY = rand.nextGaussian() * .05 + .2;
            entityItem.motionZ = rand.nextGaussian() * .05;
            world.spawnEntityInWorld(entityItem);
        }
    }

    public static ItemStack[] readInventory(NBTTagList tagList) {
        NBTTagCompound tag = (NBTTagCompound)tagList.tagAt(0);
        ItemStack[] inv = new ItemStack[tag.getInteger("size")];

        for (int i = 1; i < tagList.tagCount(); i++) {
            tag = (NBTTagCompound)tagList.tagAt(i);
            byte slot = tag.getByte("slot");
            if (slot >= 0 && slot < inv.length) {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        return inv;
    }

    public static NBTTagList writeInventory(ItemStack[] inv) {
        NBTTagList tagList = new NBTTagList();
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("size", inv.length);
        tagList.appendTag(tag);

        for (int slot = 0; slot < inv.length; slot++) {
            if (inv[slot] == null) continue;
            tag = new NBTTagCompound();
            tag.setByte("slot", (byte)slot);
            inv[slot].writeToNBT(tag);
            tagList.appendTag(tag);
        }

        return tagList;
    }
}
