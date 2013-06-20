package tpw_rules.connectedmachines.gui;


public enum GuiType {
    GUI_CONNECTED_GENERATOR;

    // store int -> enum map because java is drain-bamaged and can't do it automatically
    public static GuiType[] values = GuiType.values();
}
