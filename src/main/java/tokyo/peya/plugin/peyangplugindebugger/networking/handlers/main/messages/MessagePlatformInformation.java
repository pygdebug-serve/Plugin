package tokyo.peya.plugin.peyangplugindebugger.networking.handlers.main.messages;

import lombok.Builder;
import lombok.Value;
import tokyo.peya.plugin.peyangplugindebugger.networking.OutgoingMessage;

import java.util.UUID;

@Value
@Builder
public class MessagePlatformInformation implements OutgoingMessage
{

    ServerInfo server;

    InfoWithVendor os;
    InfoWithVendor java;

    CPUInformation cpu;

    @Override
    public byte getId()
    {
        return 0x00;
    }

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
