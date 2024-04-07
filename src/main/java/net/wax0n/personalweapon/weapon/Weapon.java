package net.wax0n.personalweapon.weapon;

import net.wax0n.personalweapon.utils.CostsAndLimits;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.A;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Weapon extends ItemStack {
    private final double DAMAGE_BASE = 3.4;
    private final double SPEED_BASE = 0.8;
    private String name;
    private int level;
    private double xpToUp;
    private double xp;
    private final double BASE_XP = 50;
    private int freePoints;
    private int usedPoints;
    private int damagePoints;
    private final double DAMAGE_FACTOR = 0.2;
    private int speedPoints;
    private final double SPEED_FACTOR = 0.1;
    private int smitePoints;
    private int arthropodPoints;
    private int firePoints;
    private int lootingPoints;
    private int sweepPoints;
    private int knockbackPoints;
    private int customModelData;
    private long resetSkillTime;
    private long resetTextureTime;

    private NamespacedKey namespacedKey;


    // New weapon
    public Weapon(ItemStack baseModel, String name, NamespacedKey namespacedKey) {
        super(baseModel);
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.level = 1;
        this.namespacedKey = namespacedKey;
        this.resetSkillTime = 0;
        this.resetTextureTime = 0;

        setXpToUp();
        setMetaItem();
    }

    // load weapon
    public Weapon(ItemStack baseModel, String name, int level, double xp, int freePoints, int damagePoints, int speedPoints,
                  int smitePoints, int arthropodPoints, int firePoints, int lootingPoints, int sweepPoints,
                  int knockbackPoints, int customModelData, long resetSkillTime, long resetTextureTime, NamespacedKey namespacedKey) {
        super(baseModel);
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.level = level;
        this.xp = xp;
        this.freePoints = freePoints;
        this.damagePoints = damagePoints;
        this.speedPoints = speedPoints;
        this.smitePoints = smitePoints;
        this.arthropodPoints = arthropodPoints;
        this.firePoints = firePoints;
        this.lootingPoints = lootingPoints;
        this.sweepPoints = sweepPoints;
        this.knockbackPoints = knockbackPoints;
        this.namespacedKey = namespacedKey;
        this.customModelData = customModelData;
        this.resetSkillTime = resetSkillTime;
        this.resetTextureTime = resetTextureTime;

        setXpToUp();
        setMetaItem();
        setTotalUsedPoints();
    }

    private void setMetaItem(){
        ItemMeta itemMeta = this.getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setUnbreakable(true);
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "SpecialItem");

        // custom model data
        if (customModelData != 0){
            itemMeta.setCustomModelData(customModelData);
        }

        this.setItemMeta(itemMeta);

        //lore
        updateLore();

        //points
        setPoints();
    }
    private void updateLore(){
        ItemMeta itemMeta = this.getItemMeta();
        List<String> lore = new ArrayList<>();
        double damage = Math.round((DAMAGE_BASE + (damagePoints * DAMAGE_FACTOR)) * 100.0)/100.0;
        double speed = Math.round((SPEED_BASE + (speedPoints * SPEED_FACTOR)) * 100.0)/100.0;
        lore.add(ChatColor.GOLD + "Level " + ChatColor.GOLD + level);
        lore.add(ChatColor.GRAY + "When in Main Hand:");
        lore.add(ChatColor.DARK_GREEN + String.valueOf(damage) + " Attack Damage");
        lore.add(ChatColor.DARK_GREEN + String.valueOf(speed) + " Attack Speed");
        itemMeta.setLore(lore);
        this.setItemMeta(itemMeta);
    }

    private void setPoints() {
        updateDamage();
        updateSpeed();
        updateSmite();
        updateArthropod();
        updateFire();
        updateLooting();
        updateSweep();
        updateKnockback();
    }

    private void updateDamage(){
        ItemMeta itemMeta = this.getItemMeta();
        double damage = Math.round((DAMAGE_BASE + (damagePoints * DAMAGE_FACTOR)) * 100.0)/100.0;
        itemMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        AttributeModifier attribDamage = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", (-1 + damage), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attribDamage);
        this.setItemMeta(itemMeta);
    }
    private void updateSpeed(){
        ItemMeta itemMeta = this.getItemMeta();
        double speed = Math.round((SPEED_BASE + (speedPoints * SPEED_FACTOR)) * 100.0)/100.0;
        itemMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
        AttributeModifier attribSpeed = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", (-4 + speed), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attribSpeed);
        this.setItemMeta(itemMeta);
    }

    private void updateSmite(){
        if (smitePoints == 0) return;
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, smitePoints, true);
        this.setItemMeta(itemMeta);
    }

    private void updateArthropod(){
        if (arthropodPoints == 0) return;
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, arthropodPoints, true);
        this.setItemMeta(itemMeta);
    }

    private void updateFire(){
        if (firePoints == 0) return;
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addEnchant(Enchantment.FIRE_ASPECT, firePoints, true);
        this.setItemMeta(itemMeta);
    }

    private void updateLooting(){
        if (lootingPoints == 0) return;
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, lootingPoints, true);
        this.setItemMeta(itemMeta);
    }

    private void updateSweep(){
        if (sweepPoints == 0) return;
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addEnchant(Enchantment.SWEEPING_EDGE, sweepPoints, true);
        this.setItemMeta(itemMeta);
    }

    private void updateKnockback(){
        if (knockbackPoints == 0) return;
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addEnchant(Enchantment.KNOCKBACK, knockbackPoints, true);
        this.setItemMeta(itemMeta);
    }

    // Level methods
    public void addXp(double xp){
        this.xp +=  xp;
        if (this.xp >= xpToUp) {
            upLevel();
        }
    }

    private void upLevel(){
        this.level += 1;
        this.xp = Math.round((xp - xpToUp) * 100.0) / 100.0;
        setXpToUp();
        this.freePoints += 1;
        updateLore();
        if (xp >= xpToUp){
            upLevel();
        }
    }

    private void setXpToUp(){
        if (level <= 100 ){
            this.xpToUp = Math.round((Math.pow(level, 0.5) * BASE_XP) * 100.0) / 100.0;
        } else {
            this.xpToUp = Math.round(((10 * level) - 500) * 100.0) / 100.0;
        }
    }

    // Up Points Methods

    public boolean updatePoint(String pointToUpdate) {
        switch (pointToUpdate){
            case "damage":
                if (freePoints >= CostsAndLimits.getDamagePointsCost(damagePoints) &&
                        damagePoints < CostsAndLimits.getLimitDamage()) {
                    this.freePoints -= CostsAndLimits.getDamagePointsCost(damagePoints);
                    this.damagePoints += 1;
                    updateDamage();
                    updateLore();
                    setTotalUsedPoints();
                    return true;
                } else{
                    return false;
                }
            case "speed":
                if (freePoints >= CostsAndLimits.getSpeedPointsCost(speedPoints) &&
                        speedPoints < CostsAndLimits.getLimitSpeed()) {
                    this.freePoints -= CostsAndLimits.getSpeedPointsCost(speedPoints);
                    this.speedPoints += 1;
                    updateSpeed();
                    updateLore();
                    setTotalUsedPoints();
                    return true;
                } else {
                    return false;
                }
            case "fire":
                if (freePoints >= CostsAndLimits.getFirePointsCost(firePoints) &&
                        firePoints < CostsAndLimits.getLimitFire()) {
                    this.freePoints -= CostsAndLimits.getFirePointsCost(firePoints);
                    this.firePoints += 1;
                    updateFire();
                    setTotalUsedPoints();
                    return true;
                } else {
                    return false;
                }
            case "smite":
                if (freePoints >= CostsAndLimits.getSmitePointsCost(smitePoints) &&
                        smitePoints < CostsAndLimits.getLimitSmite()) {
                    this.freePoints -= CostsAndLimits.getSmitePointsCost(smitePoints);
                    this.smitePoints += 1;
                    updateSmite();
                    setTotalUsedPoints();
                    return true;
                } else {
                    return false;
                }
            case "arthropod":
                if (freePoints >= CostsAndLimits.getArthropodPointsCost(arthropodPoints) &&
                        arthropodPoints < CostsAndLimits.getLimitArthropod()) {
                    this.freePoints -= CostsAndLimits.getArthropodPointsCost(arthropodPoints);
                    this.arthropodPoints += 1;
                    updateArthropod();
                    setTotalUsedPoints();
                    return true;
                } else {
                    return false;
                }
            case "knockback":
                    if (freePoints >= CostsAndLimits.getKnockbackPointsCost(knockbackPoints) &&
                            knockbackPoints < CostsAndLimits.getLimitKnockback()) {
                    this.freePoints -= CostsAndLimits.getKnockbackPointsCost(knockbackPoints);
                    this.knockbackPoints += 1;
                    updateKnockback();
                    setTotalUsedPoints();
                    return true;
                } else {
                    return false;
                }
            case "looting":
                if (freePoints >= CostsAndLimits.getLootingPointsCost(lootingPoints) &&
                        lootingPoints < CostsAndLimits.getLimitLooting()) {
                    this.freePoints -= CostsAndLimits.getLootingPointsCost(lootingPoints);
                    this.lootingPoints += 1;
                    updateLooting();
                    setTotalUsedPoints();
                    return true;
                } else {
                    return false;
                }
            case "sweep":
                if (freePoints >= CostsAndLimits.getSweepPointsCost(sweepPoints) &&
                        sweepPoints < CostsAndLimits.getLimitSweep()) {
                    this.freePoints -= CostsAndLimits.getSweepPointsCost(sweepPoints);
                    this.sweepPoints += 1;
                    updateSweep();
                    setTotalUsedPoints();
                    return true;
                } else {
                    return false;
                }
        }
        return false;
    }

    // edit name
    public void renameWeapon(String name){
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        ItemMeta itemMeta = this.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.name));
        this.setItemMeta(itemMeta);
    }

    // getters
    public int getUsedPoints() {
        return usedPoints;
    }

    public int getFreePoints() {
        return freePoints;
    }

    public int getDamagePoints() {
        return damagePoints;
    }

    public int getSpeedPoints() {
        return speedPoints;
    }

    public int getSmitePoints() {
        return smitePoints;
    }

    public int getArthropodPoints() {
        return arthropodPoints;
    }

    public int getFirePoints() {
        return firePoints;
    }

    public int getLootingPoints() {
        return lootingPoints;
    }

    public int getSweepPoints() {
        return sweepPoints;
    }

    public int getKnockbackPoints() {
        return knockbackPoints;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public double getXpToUp() {
        return xpToUp;
    }

    public double getDAMAGE_BASE() {
        return DAMAGE_BASE;
    }

    public double getSPEED_BASE() {
        return SPEED_BASE;
    }

    public double getDAMAGE_FACTOR() {
        return DAMAGE_FACTOR;
    }

    public double getSPEED_FACTOR() {
        return SPEED_FACTOR;
    }


    // calculate used points
    private void setTotalUsedPoints(){
        int total = 0;
        int damage = damagePoints;
        while (damage != 0){
            total += CostsAndLimits.getDamagePointsCost(damage-1);
            damage--;
        }

        int speed = speedPoints;
        while (speed != 0){
            total += CostsAndLimits.getSpeedPointsCost(speed-1);
            speed--;
        }

        int fire = firePoints;
        while (fire != 0){
            total += CostsAndLimits.getFirePointsCost(fire-1);
            fire--;
        }

        int smite = smitePoints;
        while (smite != 0){
            total += CostsAndLimits.getSmitePointsCost(smite-1);
            smite--;
        }

        int arthropod = arthropodPoints;
        while (arthropod != 0){
            total += CostsAndLimits.getArthropodPointsCost(arthropod-1);
            arthropod--;
        }

        int looting = lootingPoints;
        while (looting != 0){
            total += CostsAndLimits.getLootingPointsCost(looting-1);
            looting--;
        }

        int knockback = knockbackPoints;
        while (knockback != 0){
            total += CostsAndLimits.getKnockbackPointsCost(knockback-1);
            knockback--;
        }

        int sweep = sweepPoints;
        while (sweep != 0){
            total += CostsAndLimits.getSweepPointsCost(sweep-1);
            sweep--;
        }
        usedPoints = total;
    }

    public void resetPoints(){
        if (usedPoints == 0) return;
        this.freePoints = this.usedPoints + this.freePoints;
        this.usedPoints = 0;
        this.damagePoints = 0;
        this.speedPoints = 0;
        this.firePoints = 0;
        this.knockbackPoints = 0;
        this.smitePoints = 0;
        this.lootingPoints = 0;
        this.arthropodPoints = 0;
        this.sweepPoints = 0;
        this.resetSkillTime = Instant.now().getEpochSecond();
    }

    public long getResetSkillTime(){
        return resetSkillTime;
    }

    public long getResetTextureTime(){
        return resetTextureTime;
    }

    public void changeTexture(ItemStack baseModel){
        this.setType(baseModel.getType());
        this.customModelData = baseModel.getItemMeta().getCustomModelData();
        setMetaItem();
    }

    public void setTimeResetTexture(){
        this.resetTextureTime = Instant.now().getEpochSecond();
    }
}
