package tokyo.peya.plugin.peyangplugindebugger;

import tokyo.peya.lib.pygdebug.common.Packet;
import tokyo.peya.lib.pygdebug.common.PacketBase;

public class Utils
{
    public static byte getId(PacketBase message)
    {
        return getId(message.getClass());
    }

    public static byte getId(Class<? extends PacketBase> messageClass)
    {
        return messageClass.getAnnotation(Packet.class).value();
    }
}
