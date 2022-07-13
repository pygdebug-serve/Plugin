package tokyo.peya.plugin.peyangplugindebugger.networking.handlers.main;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkHandler;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkRouter;
import tokyo.peya.plugin.peyangplugindebugger.networking.handlers.main.messages.MessagePlatformInformation;
import tokyo.peya.plugin.peyangplugindebugger.networking.handlers.main.messages.MessageServerStatus;

import java.util.Arrays;

@AllArgsConstructor
public class InformationSender
{
    private final NetworkHandler handler;

    public void sendInfo(MainNetworkHandler.Request request, NetworkRouter router, Player sender)
    {
        switch (request.getAction())
        {
            case INFO_PLATFORM:
                this.handler.send(router, sender, buildPlatformInfo());
                break;
            case INFO_STATUS:
                this.handler.send(router, sender, buildServerStatus());
                break;
        }
    }

    private static MessageServerStatus buildServerStatus()
    {
        return MessageServerStatus.builder()
                .players(Bukkit.getOnlinePlayers().stream()
                        .parallel()
                        .map(player -> new MessageServerStatus.PlayerInformation(
                                player.getName(),
                                player.getUniqueId(),
                                player.getAddress() == null ? "Unknown": player.getAddress().getHostString()))
                        .toArray(MessageServerStatus.PlayerInformation[]::new))
                .worlds(Bukkit.getWorlds().stream()
                        .parallel()
                        .map(world -> new MessageServerStatus.WorldInformation(
                                world.getName(),
                                world.getEntities().size(),
                                world.getLoadedChunks().length))
                        .toArray(MessageServerStatus.WorldInformation[]::new))
                .plugins(Arrays.stream(Bukkit.getPluginManager().getPlugins())
                        .parallel()
                        .map(plugin -> new MessageServerStatus.PluginInformation(
                                plugin.getName(),
                                plugin.getDescription().getVersion(),
                                plugin.isEnabled()))
                        .toArray(MessageServerStatus.PluginInformation[]::new))
                .load(MessageServerStatus.ServerLoad.builder()
                        .tps1(Bukkit.getTPS()[0])
                        .tps5(Bukkit.getTPS()[1])
                        .tps15(Bukkit.getTPS()[2])
                        .ram(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                        .ramMax(Runtime.getRuntime().maxMemory())
                        .cpu(Runtime.getRuntime().availableProcessors())
                        .build())
                .build();
    }

    private static MessagePlatformInformation buildPlatformInfo()
    {
        return MessagePlatformInformation.builder()
                .server(MessagePlatformInformation.ServerInfo.builder()
                        .name(Bukkit.getName())
                        .version(Bukkit.getVersion())
                        .minecraftVersion(Bukkit.getMinecraftVersion())
                        .onlineMode(Bukkit.getOnlineMode())
                        .build())
                .os(MessagePlatformInformation.InfoWithVendor.builder()
                        .name(System.getProperty("os.name"))
                        .arch(System.getProperty("os.arch"))
                        .version(System.getProperty("os.version"))
                        .build())
                .java(MessagePlatformInformation.InfoWithVendor.builder()
                        .name(System.getProperty("java.vendor"))
                        .arch(System.getProperty("java.vm.name"))
                        .version(System.getProperty("java.version"))
                        .build())
                .cpu(MessagePlatformInformation.CPUInformation.builder()
                        .name(System.getProperty("os.arch"))
                        .cores(Runtime.getRuntime().availableProcessors())
                        .threads(Runtime.getRuntime().availableProcessors())
                        .build())
                .build();

    }
}
