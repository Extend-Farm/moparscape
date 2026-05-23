import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RemoteBootstrapConfigProvider {

    public RemoteBootstrapConfig loadRootConfig(String remoteBootstrapConfigUrl) {
        List<ParsedEntry> entries = readEntries(remoteBootstrapConfigUrl);
        if (entries == null) {
            return null;
        }

        int serverVersion = -1;
        String motd = null;
        String remoteClientBootstrapConfigUrl = null;
        for (ParsedEntry entry : entries) {
            if (entry.key().equals("ServerVersion")) {
                serverVersion = parseIntOrDefault(entry.rawValue(), serverVersion);
            } else if (entry.key().equals("MOTD")) {
                motd = entry.rawValue();
            } else if (entry.key().equals("byte11") && entry.values().length > 0) {
                remoteClientBootstrapConfigUrl = entry.values()[0];
            }
        }
        return new RemoteBootstrapConfig(serverVersion, motd, remoteClientBootstrapConfigUrl);
    }

    public RemoteClientBootstrapOverrides loadClientOverrides(String remoteClientBootstrapConfigUrl) {
        List<ParsedEntry> entries = readEntries(remoteClientBootstrapConfigUrl);
        if (entries == null) {
            return null;
        }

        List<RemoteClientBootstrapOperation> operations = new ArrayList<>();
        for (ParsedEntry entry : entries) {
            String key = entry.key();
            String[] values = entry.values();
            if (key.equals("Config1") && values.length >= 2) {
                operations.add(new ClientConfigOverride(
                    parseIntOrDefault(values[0], -1),
                    parseIntOrDefault(values[1], -1)
                ));
            } else if (key.equals("Config2")) {
                operations.add(new ClientMessage(entry.rawValue()));
            } else if (key.equals("sidebar") && values.length >= 2) {
                operations.add(new SidebarOverride(
                    parseIntOrDefault(values[0], -1),
                    parseIntOrDefault(values[1], -1)
                ));
            }
        }
        return new RemoteClientBootstrapOverrides(operations);
    }

    private List<ParsedEntry> readEntries(String configUrl) {
        if (configUrl == null || configUrl.trim().length() == 0) {
            return null;
        }

        List<ParsedEntry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(configUrl).openStream()))) {
            String line;
            try {
                line = reader.readLine();
            } catch (Exception exception) {
                return null;
            }

            while (line != null) {
                String trimmedLine = line.trim();
                if (trimmedLine.length() == 0) {
                    line = readNextLine(reader);
                    continue;
                }
                int separatorIndex = trimmedLine.indexOf('=');
                if (separatorIndex >= 0) {
                    String key = trimmedLine.substring(0, separatorIndex).trim();
                    String rawValue = trimmedLine.substring(separatorIndex + 1).trim();
                    entries.add(new ParsedEntry(key, rawValue, splitValues(rawValue)));
                }
                line = readNextLine(reader);
            }
        } catch (Exception exception) {
            return null;
        }
        return entries;
    }

    private static String[] splitValues(String rawValue) {
        String normalizedValue = rawValue;
        while (normalizedValue.contains("\t\t")) {
            normalizedValue = normalizedValue.replace("\t\t", "\t");
        }
        return normalizedValue.split("\t");
    }

    private static int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    private static String readNextLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (Exception exception) {
            return null;
        }
    }

    private record ParsedEntry(String key, String rawValue, String[] values) {
    }

    public record RemoteBootstrapConfig(int serverVersion, String motd, String remoteClientBootstrapConfigUrl) {
    }

    public sealed interface RemoteClientBootstrapOperation
        permits ClientConfigOverride, ClientMessage, SidebarOverride {
    }

    public record ClientConfigOverride(int configId, int value)
        implements RemoteClientBootstrapOperation {
    }

    public record ClientMessage(String message) implements RemoteClientBootstrapOperation {
    }

    public record SidebarOverride(int menuId, int interfaceId)
        implements RemoteClientBootstrapOperation {
    }

    public record RemoteClientBootstrapOverrides(List<RemoteClientBootstrapOperation> operations) {
    }
}
