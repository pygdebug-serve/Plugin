package tokyo.peya.plugin.peyangplugindebugger.debugger;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminals;
import org.bukkit.entity.Player;
import tokyo.peya.lib.pygdebug.common.debugger.DebuggerOption;
import tokyo.peya.plugin.peyangplugindebugger.PeyangPluginDebugger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DebuggerOptionLoader
{
    private static final Path DEBUG_OPTIONS_BASE_PATH;
    private static final ObjectMapper MAPPER;

    static {
        DEBUG_OPTIONS_BASE_PATH = Paths.get(PeyangPluginDebugger.INSTANCE.getDataFolder().getAbsolutePath(), "debug_options");
        MAPPER = new ObjectMapper();

        try
        {
            Files.createDirectories(DEBUG_OPTIONS_BASE_PATH);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static Path getDebuggerOptionFilePath(Player player)
    {
        return Paths.get(DEBUG_OPTIONS_BASE_PATH.toString(), player.getUniqueId() + ".json");
    }

    public static DebuggerOption tryLoadDebuggerOption(Player player)
    {
        Path path = getDebuggerOptionFilePath(player);


        if (!Files.exists(path))
            return new DebuggerOption();

        try(InputStream fis = Files.newInputStream(path))
        {
            DebuggerOption option = MAPPER.readValue(fis, DebuggerOption.class);
            Terminals.ofConsole().info("Player " + player.getName() + "'s debugger option has been restored.");
            return option;
        }
        catch (IOException e)
        {

            Terminals.ofConsole().warn("Failed to create debugger option file for player " +
                    player.getName() + ":" + e.getMessage());
            return new DebuggerOption();
        }
    }

    public static void saveDebuggerOption(Player player, DebuggerOption option)
    {
        Path path = getDebuggerOptionFilePath(player);

        try(FileWriter fw = new FileWriter(path.toFile()))
        {
            MAPPER.writeValue(fw, option);

            Terminals.ofConsole().info("Player " + player.getName() + "'s debugger option has been saved.");
        }
        catch (IOException e)
        {
            Terminals.ofConsole().warn("Failed to create debugger option file for player " +
                    player.getName() + ":" + e.getMessage());
        }
    }
}
