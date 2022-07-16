package tokyo.peya.plugin.peyangplugindebugger.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.jetbrains.annotations.Nullable;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import tokyo.peya.plugin.peyangplugindebugger.PeyangPluginDebugger;

import java.io.IOException;

public class PacketEncodeUtils
{
    private static final ObjectMapper MAPPER;

    private static final ObjectWriter WRITER;

    static {
        MAPPER = new ObjectMapper(new MessagePackFactory());
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        WRITER = MAPPER.writer().withoutAttribute("id");
    }

    public static <CLS> @Nullable CLS decodeMessage(byte[] value, Class<CLS> clazz)
    {
        try
        {
            byte[] data = new byte[value.length - 1];
            System.arraycopy(value, 1, data, 0, data.length);
            return MAPPER.readValue(data, clazz);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            PeyangPluginDebugger.LOGGER.warn("Failed to decode message: " + new String(value));
            return null;
        }
    }

    public static byte[] encodeMessage(Object value)
    {
        try
        {
            return WRITER.writeValueAsBytes(value);
        }
        catch (IOException e)
        {
            PeyangPluginDebugger.LOGGER.warn("Failed to encode message: " + value);
            throw new RuntimeException(e);
        }
    }
}
