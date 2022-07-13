package tokyo.peya.plugin.peyangplugindebugger.networking.handlers.main.messages;

import lombok.Builder;
import lombok.Value;
import tokyo.peya.plugin.peyangplugindebugger.networking.OutgoingMessage;

import java.util.UUID;

@Value
@Builder
public class MessageServerStatus implements OutgoingMessage
{
    PluginInformation[] plugins;
    PlayerInformation[] players;
    WorldInformation[] worlds;

    ServerLoad load;

    @Override
    public byte getId()
    {
        return 0x01;
    }

    @Value
    @Builder
    public static class ServerLoad
    {
        double tps1;
        double tps5;
        double tps15;

        long ram;
        long ramMax;

        double cpu;
    }

    @Value
    public static class WorldInformation
    {
        String name;
        long entities;
        long chunks;
    }

    @Value
    public static class PluginInformation
    {
        String name;
        String version;
        boolean isEnabled;
    }

    @Value
    public static class PlayerInformation
    {
        String name;
        UUID uuid;
        String ip;
    }
}
