package tokyo.peya.plugin.peyangplugindebugger.networking;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import tokyo.peya.lib.pygdebug.common.Packet;
import tokyo.peya.lib.pygdebug.common.PacketBase;
import tokyo.peya.plugin.peyangplugindebugger.utils.PacketEncodeUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class NetworkRouter implements PluginMessageListener
{
    public static final String NAMESPACE_ROOT;


    static {
        NAMESPACE_ROOT = "pygdebug:";
    }

    private final Plugin plugin;

    private final Map<String, NetworkHandler> handlers;

    public NetworkRouter(Plugin plugin)
    {
        this.plugin = plugin;
        this.handlers = new HashMap<>();
    }

    public void registerHandler(NetworkHandler handler)
    {
        String name = handler.getName();

        if (this.handlers.containsKey(name))
            throw new IllegalArgumentException("Handler already registered: " + name);

        this.handlers.put(name, handler);
    }

    public void init()
    {
        this.handlers.forEach((name, handler) -> {
            name = NAMESPACE_ROOT + name;
            Bukkit.getMessenger().registerIncomingPluginChannel(this.plugin, name, this);
            Bukkit.getMessenger().registerOutgoingPluginChannel(this.plugin, name);
            handler.onInitialized();
        });
    }

    public void dispose()
    {
        this.handlers.forEach((name, handler) -> {
            name = NAMESPACE_ROOT + name;
            Bukkit.getMessenger().unregisterOutgoingPluginChannel(this.plugin, name);
            Bukkit.getMessenger().unregisterIncomingPluginChannel(this.plugin, name);
        });
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message)
    {
        String name = channel.substring(NAMESPACE_ROOT.length());
        NetworkHandler handler = this.handlers.get(name);
        if (handler == null)
            return;

        handler.onReceived(this, player, message);
    }

    public void sendPluginMessage(NetworkHandler handler, Player player, byte[] message)
    {
        String name = NAMESPACE_ROOT + handler.getName();

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos))
        {
            dos.writeByte(0);
            dos.write(message);
            dos.writeByte(0);

            player.sendPluginMessage(this.plugin, name, baos.toByteArray());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendPluginMessage(NetworkHandler handler, Player player, PacketBase message)
    {
        this.sendPluginMessage(handler, player, message);
    }

    public void sendPluginMessage(String name, Player player, PacketBase message)
    {
        name = NAMESPACE_ROOT + name;

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos))
        {
            dos.writeByte(getId(message));
            dos.write(PacketEncodeUtils.encodeMessage(message));
            dos.writeByte(0);

            player.sendPluginMessage(this.plugin, name, baos.toByteArray());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static byte getId(PacketBase message)
    {
        for (Annotation annotation : message.getClass().getAnnotations())
            if (annotation instanceof Packet)
                return ((Packet) annotation).value();

        return -1;
    }
}

