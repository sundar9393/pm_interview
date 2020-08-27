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


}
