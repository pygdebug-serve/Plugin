package tokyo.peya.plugin.peyangplugindebugger.networking.handlers.main;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketPlatformInformation;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketServerStatus;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkHandler;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkRouter;

import java.util.Arrays;

@AllArgsConstructor
public class InformationSender
{
    private final NetworkHandler handler;

    public void sendInfo(PacketInformationRequest request, NetworkRouter router, Player sender)
    {
        switch (request.getAction())
        {
            case PLATFORM:
                this.handler.send(router, sender, buildPlatformInfo());
                break;
            case SERVER_STATUS:
                this.handler.send(router, sender, buildServerStatus());
                break;
        }
    }

    private static PacketServerStatus buildServerStatus()
    {
        return PacketServerStatus.builder()
                .players(Bukkit.getOnlinePlayers().stream()
                        .parallel()
                        .map(player -> new PacketServerStatus.PlayerInformation(
                                player.getName(),
                                player.getUniqueId(),
                                player.getAddress() == null ? "Unknown": player.getAddress().getHostString()))
                        .toArray(PacketServerStatus.PlayerInformation[]::new))
                .worlds(Bukkit.getWorlds().stream()
                        .parallel()
                        .map(world -> new PacketServerStatus.WorldInformation(
                                world.getName(),
                                world.getEntities().size(),
                                world.getLoadedChunks().length))
                        .toArray(PacketServerStatus.WorldInformation[]::new))
                .plugins(Arrays.stream(Bukkit.getPluginManager().getPlugins())
                        .parallel()
                        .map(plugin -> new PacketServerStatus.PluginInformation(
                                plugin.getName(),
                                plugin.getDescription().getVersion(),
                                plugin.isEnabled()))
                        .toArray(PacketServerStatus.PluginInformation[]::new))
                .load(PacketServerStatus.ServerLoad.builder()
                        .tps1(Bukkit.getTPS()[0])
                        .tps5(Bukkit.getTPS()[1])
                        .tps15(Bukkit.getTPS()[2])
                        .ram(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                        .ramMax(Runtime.getRuntime().maxMemory())
                        .cpu(Runtime.getRuntime().availableProcessors())
                        .build())
                .build();
    }

    private static PacketPlatformInformation buildPlatformInfo()
    {
        return PacketPlatformInformation.builder()
                .server(PacketPlatformInformation.ServerInfo.builder()
                        .name(Bukkit.getName())
                        .version(Bukkit.getVersion())
                        .minecraftVersion(Bukkit.getMinecraftVersion())
                        .onlineMode(Bukkit.getOnlineMode())
                        .build())
                .os(PacketPlatformInformation.InfoWithVendor.builder()
                        .name(System.getProperty("os.name"))
                        .arch(System.getProperty("os.arch"))
                        .version(System.getProperty("os.version"))
                        .build())
                .java(PacketPlatformInformation.InfoWithVendor.builder()
                        .name(System.getProperty("java.vendor"))
                        .arch(System.getProperty("java.vm.name"))
                        .version(System.getProperty("java.version"))
                        .build())
                .cpu(PacketPlatformInformation.CPUInformation.builder()
                        .name(System.getProperty("os.arch"))
                        .cores(Runtime.getRuntime().availableProcessors())
                        .threads(Runtime.getRuntime().availableProcessors())
                        .build())
                .build();

    }
}
