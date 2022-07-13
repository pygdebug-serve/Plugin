package tokyo.peya.plugin.peyangplugindebugger.networking;

public interface OutgoingMessage
{
    /**
     * ID is MUST BE UNIQUE.
     * @return id.
     */
    byte getId();
}
