package tokyo.peya.plugin.peyangplugindebugger.networking;

import org.bukkit.entity.Player;
import tokyo.peya.lib.pygdebug.common.PacketBase;
import tokyo.peya.plugin.peyangplugindebugger.utils.PacketEncodeUtils;

public interface NetworkHandler
{
    String getName();
    void onReceived(NetworkRouter router, Player sender, byte[] data);


    default void onInitialized() {}

    default void send(NetworkRouter router, Player receiver, byte[] data)
    {
        router.sendPluginMessage(this, receiver, data);
    }

    default void send(NetworkRouter router, Player player, Object object)
    {
        this.send(router, player, PacketEncodeUtils.encodeMessage(object));
    }

    default void send(NetworkRouter router, Player player, PacketBase message)
    {
        router.sendPluginMessage(this, player, message);

    }

}
