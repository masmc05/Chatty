package ru.brikster.chatty.api.chat;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brikster.chatty.api.chat.command.ChatCommand;
import ru.brikster.chatty.api.chat.message.strategy.MessageTransformStrategy;
import ru.brikster.chatty.api.chat.range.Ranges;

import java.util.*;
import java.util.function.Predicate;

public interface Chat {

    /**
     * Name of chat from plugin configuration
     *
     * @return name of chat
     */
    @NotNull
    String getName();

    @NotNull
    String getDisplayName();

    /**
     * Chat format has the following variables:
     * 1) {player} - player nickname
     * 2) {prefix}, {suffix} - prefix and suffix of player
     * 3) %<placeholder>% - various placeholders from PlaceholderAPI
     *
     * @return chat format specified in configuration
     */
    @NotNull
    Component getFormat();

    @NotNull Set<ChatStyle> getStyles();

    @NotNull String getSymbol();

    @Nullable ChatCommand getCommand();

    default boolean isSymbolAllowed() {
        return !getSymbol().isEmpty();
    }

    default boolean isCommandAllowed() {
        return getCommand() != null;
    }

    boolean isSendNobodyHeardYou();

    boolean isParseLinks();

    boolean isEnableSpy();

    Component getSpyFormat();

    int getCooldown();

    @Nullable
    Sound getSound();

    @NotNull List<@NotNull MessageTransformStrategy<?>> getStrategies();

    void addStrategy(@NotNull MessageTransformStrategy<?> strategy);

    boolean removeStrategy(@NotNull MessageTransformStrategy<?> strategy);

    /**
     * Range param for the chat messages
     * -3 is used for multiserver messaging (when "general.bungeecord" is true)
     * -2 is used for cross-world chats
     * -1 is for global single-world chats
     * 0 and higher for ranged local-chats
     *
     * @return range value for this chat
     * @see Ranges#isApplicable(Player, Player, int)
     */
    int getRange();

    /**
     * Permission requiring can be disable in configuration
     * If permission is enable, player must has "chatty.chat.<chat>" permission to use it
     *
     * @return whether permission required or not
     */
    boolean isPermissionRequired();

    boolean hasSymbolWritePermission(Player sender);

    boolean hasCommandWritePermission(Player sender);

    boolean hasReadPermission(Player sender);

    @NotNull
    Predicate<Player> getRecipientPredicate(@Nullable Player sender);

    /**
     * Creates collection of online players chat can see message from this chat
     *
     * @param sender message sender
     * @return collection of chat recipients
     */
    @NotNull
    default Collection<? extends Player> getRecipients(@Nullable Player sender) {
        Set<Player> onlinePlayers = new HashSet<>(Bukkit.getOnlinePlayers());
        onlinePlayers.removeIf(player -> !getRecipientPredicate(sender).test(player));
        return Collections.unmodifiableCollection(onlinePlayers);
    }

    /**
     * This method let you send any message to the chat participants (without applying {@link Chat#getFormat()})
     *
     * @param plugin  plugin that uses Chatty API
     * @param message legacy message with section symbol to send
     */
    default void sendMessage(Plugin plugin, String message) {
        sendMessage(plugin, LegacyComponentSerializer.legacySection().deserialize(message), recipient -> true);
    }

    /**
     * This method let you send any message to the chat participants (without applying {@link Chat#getFormat()})
     *
     * @param plugin  plugin that uses Chatty API
     * @param message message to send
     */
    default void sendMessage(Plugin plugin, Component message) {
        sendMessage(plugin, message, recipient -> true);
    }

    /**
     * This method let you send any message to the chat participants (without applying {@link Chat#getFormat()}).
     * Additional param (recipientPredicate) can be used for secondary filtration of recipients, for example, with
     * your permission.
     *
     * @param plugin             plugin that uses Chatty API
     * @param message            message to send
     * @param recipientPredicate predicate for message recipient
     */
    void sendMessage(Plugin plugin, Component message, Predicate<CommandSender> recipientPredicate);

}