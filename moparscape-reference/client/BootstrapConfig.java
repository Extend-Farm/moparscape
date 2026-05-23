public final class BootstrapConfig {

    public static final int SERVER_PORT = 43594;
    public static final String REMOTE_BOOTSTRAP_CONFIG_URL = "http://hybridscape.no-ip.org/hybridconfig.txt";
    public static final int MODERN_MAGIC_TAB_INTERFACE_ID = 1151;
    public static final int ANCIENT_MAGIC_TAB_INTERFACE_ID = 12855;

    private static final SidebarInterface[] DEFAULT_SIDEBAR_INTERFACES = {
        new SidebarInterface(1, 3917),
        new SidebarInterface(2, 638),
        new SidebarInterface(3, 3213),
        new SidebarInterface(4, 1644),
        new SidebarInterface(5, 5608),
        new SidebarInterface(8, 5065),
        new SidebarInterface(9, 5715),
        new SidebarInterface(10, 2449),
        new SidebarInterface(11, 4445),
        new SidebarInterface(12, 147),
        new SidebarInterface(13, 962)
    };

    private BootstrapConfig() {
    }

    public static int getMagicSidebarInterfaceId(boolean useAncientMagics) {
        return useAncientMagics ? ANCIENT_MAGIC_TAB_INTERFACE_ID : MODERN_MAGIC_TAB_INTERFACE_ID;
    }

    public static void applyDefaultSidebarInterfaces(client player) {
        for (SidebarInterface sidebarInterface : DEFAULT_SIDEBAR_INTERFACES) {
            player.setSidebarInterface(sidebarInterface.menuId(), sidebarInterface.interfaceId());
        }
        player.setSidebarInterface(6, getMagicSidebarInterfaceId(player.playerAncientMagics));
    }

    private record SidebarInterface(int menuId, int interfaceId) {
    }
}
