package tpw_rules.connectedmachines.util;


import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class WCoord {
    public int x, y, z;
    public IBlockAccess world;

    public WCoord(IBlockAccess world, int x, int y, int z) {
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

