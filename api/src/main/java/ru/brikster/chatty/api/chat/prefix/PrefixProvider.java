package ru.brikster.chatty.api.chat.prefix;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

public interface PrefixProvider {

    @Nullable String getPrefix(OfflinePlayer player);

    @Nullable String getSuffix(OfflinePlayer player);

}
