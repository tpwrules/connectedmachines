package tpw_rules.connectedmachines.network;


import net.minecraftforge.common.DimensionManager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class InputPacket extends Packet {
    public DataInputStream data;

    public PacketType type;
    public ITileEntityPacketHandler tile;

    public InputPacket(byte[] inData) {
        this.data = new DataInputStream(new ByteArrayInputStream(inData));
        try {
            type = PacketType.values[data.readByte()];
            int dim = data.readInt();
            int x = data.readInt();
            int y = data.readInt();
            int z = data.readInt();
            if (y != -1) {
                tile = (ITileEntityPacketHandler) DimensionManager.getWorld(dim).getBlockTileEntity(x, y, z);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void dispatch() {
        if (tile != null) {
            tile.handlePacket(this);
        } else {
            PacketHandler.handlePacket(this);
        }
    }
}
