package tpw_rules.connectedmachines.network;


public enum PacketType {
    MACHINE_LINK_REDRAW,
    UPDATE_LINK_STATE,
    GUI_UPDATE,
    GUI_CHANGE,
    NAME_UPDATE,
    GROUP_UPDATE,
    MACHINE_UPDATE;

    // store int -> enum map because java is drain-bamaged and can't do it automatically
    public static PacketType[] values = PacketType.values();
}
