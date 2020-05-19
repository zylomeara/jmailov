package main.java.enums;

public enum ChannelsEnum {
    Gray(1), Color(3);

    private final int channelCount;

    ChannelsEnum(int channelCount) {
        this.channelCount = channelCount;
    }

    public int get() {
        return channelCount;
    }
}
