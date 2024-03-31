package ru.brikster.chatty.api.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brikster.chatty.api.chat.prefix.PrefixProvider;


/**
 * Event that allows a custom prefix provider to be registered
 */
public final class ChattyPrefixProviderPreRegisterEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private @Nullable PrefixProvider prefixProvider;

    public ChattyPrefixProviderPreRegisterEvent() {
        super(!Bukkit.isPrimaryThread());
        this.prefixProvider = null;
    }

    /**
     * Get the prefix provider that will be registered
     * @return prefix provider, or null for default
     */
    public @Nullable PrefixProvider getPrefixProvider() {
        return prefixProvider;
    }

    /**
     * Set the prefix provider that will be registered
     * @param prefixProvider prefix provider, or null for default
     */
    public void setPrefixProvider(@Nullable PrefixProvider prefixProvider) {
        this.prefixProvider = prefixProvider;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
