package tpw_rules.connectedmachines.util;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;

public class WCoord {
    public int x, y, z;
    public World world;

    public WCoord(World world, int x, int y, int z) {
        this.world = world;
        this.x = x; this.y = y; this.z = z;
    }

    public WCoord(TileEntity tile) {
        this.fromTileEntity(tile);
    }

    public void fromTileEntity(TileEntity tile) {
        this.world = tile.worldObj;
        x = tile.xCoord; y = tile.yCoord; z = tile.zCoord;
    }

    public WCoord copy() {
        return new WCoord(world, x, y, z);
    }

    public TileEntity getTileEntity() {
        return world.getBlockTileEntity(x, y, z);
    }

    public int getBlockID() {
        return world.getBlockId(x, y, z);
    }

    public static WCoord readFromNBT(NBTTagCompound tag, String name) {
        int[] coords = tag.getIntArray(name);
        return new WCoord(DimensionManager.getWorld(coords[0]), coords[1], coords[2], coords[3]);
    }

    public void writeToNBT(NBTTagCompound tag, String name) {
        int[] coords = {world.getWorldInfo().getDimension(), x, y, z};
        tag.setIntArray(name, coords);
    }

    public WCoord move(ForgeDirection dir) {
        switch (dir) {
            case EAST:
                x++;
                break;
            case WEST:
                x--;
                break;
            case UP:
                y++;
                break;
            case DOWN:
                y--;
                break;
            case SOUTH:
                z++;
                break;
            case NORTH:
                z--;
                break;
        }
        return this;
    }
}

