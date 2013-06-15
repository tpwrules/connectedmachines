package tpw_rules.connectedmachines.network;


public enum PacketType {
    MACHINE_LINK_REDRAW;

    // store int -> enum map because java is drain-bamaged and can't do it automatically
    public static PacketType[] values = PacketType.values();
}
