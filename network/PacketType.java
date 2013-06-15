package tpw_rules.connectedmachines.network;


public enum PacketType {
    // messages that go to the default handler

    // boundary message type
    TILE_ENTITY_MESSAGE_BOUNDARY,

    // messages destined for tileentities
    MACHINE_LINK_REDRAW
}
