package tokyo.peya.plugin.peyangplugindebugger.networking.handlers.main.messages;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class MessagePlatformInformation
{
    static final String TYPE = "platform_information";

    ServerInfo server;

    InfoWithVendor os;
    InfoWithVendor java;

    CPUInformation cpu;

    @Value
    @Builder
    public static class ServerInfo
    {
        String name;
        String version;
        String minecraftVersion;
        boolean onlineMode;
    }


    @Value
    @Builder
    public static class InfoWithVendor
    {
        String name;
        String arch;
        String version;
    }

    @Value
    @Builder
    public static class CPUInformation
    {
        String name;
        int cores;
        int threads;
    }

}
