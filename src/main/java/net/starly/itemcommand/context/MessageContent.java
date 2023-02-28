package net.starly.itemcommand.context;

import net.starly.core.jb.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MessageContent {

    public enum MessageType {
        ERROR("errorMessages"), NORMAL("messages");

        private final String key;

        MessageType(String key) {
            this.key = key;
        }
    }

    private static MessageContent messageContent;
    private Map<MessageType, Map<String, String>> map = new HashMap<>();

    private MessageContent() {}

    public static MessageContent getMessageContent() {
        if (messageContent == null) messageContent = new MessageContent();
        return messageContent;
    }

    public void initializing(FileConfiguration file) {
        map.clear();
        Arrays.stream(MessageType.values()).map(values -> new Pair<>(values, file.getConfigurationSection(values.key)))
                .forEach(this::initializingMessages);
    }

    private void initializingMessages(Pair<MessageType, ConfigurationSection> pair) {
        Map<String, String> messages = map.computeIfAbsent(pair.getFirst(), (unused) -> new HashMap<>());
        pair.getSecond().getKeys(false).forEach(key ->
                messages.put(key, ChatColor.translateAlternateColorCodes('&', pair.getSecond().getString(key))));
    }


    public String getMessage(MessageType type, String key) { return map.get(type).get(key); }

    public String getMessageAfterPrefix(MessageType type, String key) { return map.get(MessageType.NORMAL).get("prefix") + getMessage(type, key); }
}
