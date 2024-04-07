package net.wax0n.personalweapon.weapon;

import net.wax0n.personalweapon.utils.CostsAndLimits;
import net.wax0n.personalweapon.utils.PlayerUUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class WeaponManager {

    private final NamespacedKey namespacedKey;
    private final HashMap<UUID, ArrayList<Weapon>> listWeapons;
    private final HashMap<UUID, Integer> selectedWeapon;


    public WeaponManager(NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
        listWeapons = new HashMap<>();
        selectedWeapon = new HashMap<>();
    }

    public void createWeapon(Player player, ItemStack itemStack, String name){
        UUID uuid = PlayerUUID.getUUID(player);
        listWeapons.computeIfAbsent(uuid, k -> new ArrayList<>());
        listWeapons.get(uuid).add(new Weapon(itemStack, name, namespacedKey));

        selectedWeapon.put(uuid,  listWeapons.get(uuid).size()-1);
    }

    public void deleteWeapon(Player player){
        UUID uuid = PlayerUUID.getUUID(player);
        if (listWeapons.get(uuid) == null || listWeapons.get(uuid).isEmpty()){
            return;
        }

        selectedWeapon.putIfAbsent(uuid, 0);
        int index = selectedWeapon.get(uuid);
        listWeapons.get(uuid).remove(index);
        if (hasWeapons(player)){
            selectedWeapon.put(uuid,  listWeapons.get(uuid).size()-1);
        } else {
            selectedWeapon.remove(uuid);
        }
    }

    public Weapon getWeapon(Player player){
        UUID uuid = PlayerUUID.getUUID(player);
        if (listWeapons.get(uuid) == null || listWeapons.get(uuid).isEmpty()){
            throw new RuntimeException("Player doesnt have any weapon");
        }
        selectedWeapon.putIfAbsent(uuid, 0);
        return listWeapons.get(uuid).get(selectedWeapon.get(uuid));
    }

    public int getAmountWeapon(Player player){
        return listWeapons.get(PlayerUUID.getUUID(player)).size();
    }


    // quitar el arma actual al cambiar
    public void changeSelectedWeapon(Player player, int index){
        UUID uuid = PlayerUUID.getUUID(player);
        if (listWeapons.get(uuid) == null || listWeapons.get(uuid).isEmpty()){
            return;
        }
        selectedWeapon.put(uuid, index);
    }


    // delete weapons on a player inventory
    public void removeWeaponItem(Player player){
        for (ItemStack itemStack : player.getInventory()){
            if (itemStack == null) continue;
            if (isEspecialItem(itemStack)){
                player.getInventory().remove(itemStack);
            }
        }
    }

    //search weapons on a player inventory
    public boolean checkSpecialItem (Player player){
        for (ItemStack itemStack : player.getInventory()){
            if (itemStack == null) continue;
            if (isEspecialItem(itemStack)){
                return true;
            }
        }
        return false;
    }

    public boolean isEspecialItem(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (!itemStack.hasItemMeta()) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING)) {
            return itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING).equals("SpecialItem");
        }
        return false;
    }

    public void giveWeapon(Player player){
        UUID uuid = PlayerUUID.getUUID(player);
        if (listWeapons.get(uuid) == null || listWeapons.get(uuid).isEmpty()){
            return;
        }
        selectedWeapon.putIfAbsent(uuid, 0);

        PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() != -1) {
            inventory.addItem(listWeapons.get(uuid).get(selectedWeapon.get(uuid)));
        } else {
            player.sendMessage("Your inventory is full");
        }
    }

    public void updateWeapon(Player player){
        removeWeaponItem(player);
        giveWeapon(player);
    }

    public void renameWeapon(Player player, String name){
        UUID uuid = PlayerUUID.getUUID(player);
        if (listWeapons.get(uuid) == null || listWeapons.get(uuid).isEmpty()){
            return;
        }
        getWeapon(player).renameWeapon(name);
        updateWeapon(player);
    }

    public void infoWeapon(Player sender, Player infoPlayer){
        Weapon weapon = getWeapon(infoPlayer);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l[&r" + weapon.getName() + "&f&l]"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Level " + weapon.getLevel()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aXP " +
                (Math.round(weapon.getXp() * 100.0) / 100.0) + "/" + (Math.round(weapon.getXpToUp() * 100.0) / 100.0) ));

        double damage = Math.round((weapon.getDAMAGE_BASE() + (weapon.getDamagePoints() * weapon.getDAMAGE_FACTOR())) * 100.0) / 100.0;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Damage " + damage));

        double speed =  Math.round((weapon.getSPEED_BASE() + (weapon.getSpeedPoints() * weapon.getSPEED_FACTOR())) * 100.0) / 100.0;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Speed " + speed));

        if (weapon.getSmitePoints() != 0)
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Smite " +
                    weapon.getSmitePoints() + "/" + CostsAndLimits.getLimitSmite()));
        if (weapon.getArthropodPoints() != 0)
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5Bane of Arthropod " +
                    weapon.getArthropodPoints() + "/" + CostsAndLimits.getLimitArthropod()));
        if (weapon.getFirePoints() != 0)
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFire Aspect " +
                    weapon.getFirePoints() + "/" + CostsAndLimits.getLimitFire()));
        if (weapon.getLootingPoints() != 0)
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLooting " +
                    weapon.getLootingPoints() + "/" + CostsAndLimits.getLimitLooting()));
        if (weapon.getKnockbackPoints() != 0)
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cKnockback " +
                    weapon.getKnockbackPoints() + "/" + CostsAndLimits.getLimitKnockback()));
        if (weapon.getSweepPoints() != 0)
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSweeping Edge " +
                    weapon.getSweepPoints() + "/" + CostsAndLimits.getLimitSweep()));
    }


    public boolean hasWeapons(Player player){
        UUID uuid = PlayerUUID.getUUID(player);
        if(listWeapons.get(uuid) == null || listWeapons.get(uuid).isEmpty()){
            return false;
        }
        return true;
    }


    public ArrayList<Weapon> getListWeapons(Player player) {
        UUID uuid = PlayerUUID.getUUID(player);
        if (hasWeapons(player)){
            return listWeapons.get(uuid);
        }
        return new ArrayList<>();
    }

    public int getSelectedWeapon(Player player) {
        UUID uuid = PlayerUUID.getUUID(player);
        selectedWeapon.putIfAbsent(uuid, 0);
        return selectedWeapon.get(uuid);
    }

    public void clearWeapons() {
        listWeapons.clear();
    }

    public void loadWeaponList(Player player, ArrayList<Weapon> weapons, int selectedIndex) {
        UUID uuid = PlayerUUID.getUUID(player);
        listWeapons.put(uuid, weapons);
        selectedWeapon.put(uuid, selectedIndex);
    }

    public void resetSelectedWeapon(Player player){
        UUID uuid = PlayerUUID.getUUID(player);
        selectedWeapon.putIfAbsent(uuid, 0);
        listWeapons.get(uuid).get(selectedWeapon.get(uuid)).resetPoints();
    }

    public Boolean checkWeaponResetSkillTimer(Player player, long needTime){
        Weapon weapon = getWeapon(player);
        long nowTime = Instant.now().getEpochSecond();
        return nowTime - weapon.getResetSkillTime() >= needTime;
    }

    public Boolean checkWeaponResetTextureTimer(Player player, long needTime){
        Weapon weapon = getWeapon(player);
        long nowTime = Instant.now().getEpochSecond();
        return nowTime - weapon.getResetTextureTime() >= needTime;
    }

    public long leftResetTime(Player player, long timer){
        Weapon weapon = getWeapon(player);
        long nowTime = Instant.now().getEpochSecond();
        return timer - (nowTime - weapon.getResetSkillTime());
    }

    public long leftTextureTime(Player player, long timer){
        Weapon weapon = getWeapon(player);
        long nowTime = Instant.now().getEpochSecond();
        return timer - (nowTime - weapon.getResetTextureTime());
    }

    public void changeTextureWeapon(Player player, ItemStack item){
        getWeapon(player).changeTexture(item);
    }
}
