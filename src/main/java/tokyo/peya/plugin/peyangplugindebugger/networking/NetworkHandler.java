package tokyo.peya.plugin.peyangplugindebugger.networking;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import tokyo.peya.lib.pygdebug.common.PacketBase;
import tokyo.peya.plugin.peyangplugindebugger.PeyangPluginDebugger;

import java.io.IOException;

public interface NetworkHandler
{
     ObjectMapper MAPPER = new ObjectMapper(new MessagePackFactory());


    String getName();
    void onReceived(NetworkRouter router, Player sender, byte[] data);


    default void onInitialized() {}

    default void send(NetworkRouter router, Player receiver, byte[] data)
    {
        router.sendPluginMessage(this, receiver, data);
    }

    default void send(NetworkRouter router, Player player, Object object)
    {
        this.send(router, player, this.encodeMessage(object));
    }

    default void send(NetworkRouter router, Player player, PacketBase message)
    {
        router.sendPluginMessage(this, player, message);

    }

    default <T> @Nullable T decodeMessage(byte[] value)
    {
        try
        {
            return MAPPER.readValue(value, new TypeReference<T>(){});
        }
        catch (IOException e)
        {
            e.printStackTrace();
            PeyangPluginDebugger.LOGGER.warn("Failed to decode message: " + new String(value));
            return null;
        }
    }

    default byte[] encodeMessage(Object value)
    {
        try
        {
            return MAPPER.writeValueAsBytes(value);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            PeyangPluginDebugger.LOGGER.warn("Failed to encode message: " + value);
            return null;
        }
    }
}
