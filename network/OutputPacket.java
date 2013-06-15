package tpw_rules.connectedmachines.network;


import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;


public class OutputPacket extends Packet {
    public ByteArrayOutputStream bos;
    public DataOutputStream data;

    public PacketType type;
    public TileEntity tile;

    public OutputPacket(PacketType type, int size) {
        this(type, size, null); // initialize ourselves with no destination tileentities
    }

    public OutputPacket(PacketType type, int size, TileEntity tile) {
        this.tile = tile;
        this.type = type;
        bos = new ByteArrayOutputStream(size+17);
        data = new DataOutputStream(bos);
        try {
            data.writeByte((byte)(type.ordinal()));
            if (tile != null) {
                data.writeInt(tile.worldObj.getWorldInfo().getDimension());
                data.writeInt(tile.xCoord);
                data.writeInt(tile.yCoord);
                data.writeInt(tile.zCoord);
            } else {
                data.writeInt(0);
                data.writeInt(-1);
                data.writeInt(0);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Packet250CustomPayload getMinecraftPacket() {
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "ConnectedMachines";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        return packet;
    }

    public void sendEverywhere() {
        PacketDispatcher.sendPacketToAllPlayers(getMinecraftPacket());
    }

    public void sendServer() {
        PacketDispatcher.sendPacketToServer(getMinecraftPacket());
    }
}
