package tpw_rules.connectedmachines.util;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;

import java.io.DataInputStream;
import java.io.DataOutputStream;

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

    public static WCoord readFromPacket(DataInputStream data) {
        try {
            return new WCoord(DimensionManager.getWorld(data.readInt()), data.readInt(), data.readInt(), data.readInt());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToPacket(DataOutputStream data) {
        try {
            data.writeInt(world.getWorldInfo().getDimension());
            data.writeInt(x);
            data.writeInt(y);
            data.writeInt(z);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public WCoord adjacent(ForgeDirection dir) {
        switch (dir) {
            case EAST:
                return new WCoord(world, x+1, y, z);
            case WEST:
                return new WCoord(world, x-1, y, z);
            case UP:
                return new WCoord(world, x, y+1, z);
            case DOWN:
                return new WCoord(world, x, y-1, z);
            case SOUTH:
                return new WCoord(world, x, y, z+1);
            case NORTH:
                return new WCoord(world, x, y, z-1);
        }
        return null;
    }

    @Override
    public int hashCode() {
        return world.getWorldInfo().getDimension() + x ^ (y << 8) ^ (z << 16);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof WCoord))
            return false;
        WCoord coord = (WCoord)other;
        return (world == coord.world) && (x == coord.x) && (y == coord.y) && (z == coord.z);
    }
}

