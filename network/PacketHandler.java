package tpw_rules.connectedmachines.network;


import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketHandler implements IPacketHandler {
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        new InputPacket(packet.data).dispatch();
    }

    public static void handlePacket(InputPacket packet) {
    }
}
