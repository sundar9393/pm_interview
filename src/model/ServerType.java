package model;

public enum ServerType {

    LARGE(1),
    LARGE_X(2),
    LARGE_2X(4),
    LARGE_4X(8),
    LARGE_8X(16),
    LARGE_10X(32);


    private final int cpus;

    private ServerType(int cpus) {
        this.cpus = cpus;
    }

    public int getCpus() {
        return this.cpus;
    }



    public static ServerType getServerType(String serverType) {

        if (null == serverType) return null;

        switch (serverType.toLowerCase()) {
            case "large":
                return LARGE;
            case "large_x":
                return LARGE_X;
            case "large_2x" :
                return LARGE_2X;
            case "large_4x":
                return LARGE_4X;
            case "large_8x":
                return LARGE_8X;
            case "large_10x":
                return LARGE_10X;
            default:
                return null;
        }

    }



}
