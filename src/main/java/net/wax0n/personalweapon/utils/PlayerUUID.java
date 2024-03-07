package net.wax0n.personalweapon.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PlayerUUID {

    public static UUID getUUID(Player player){
        if (Bukkit.getOnlineMode()){
            return player.getUniqueId();
        }
       return UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(StandardCharsets.UTF_8));
    }
}
