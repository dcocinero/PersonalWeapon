package net.wax0n.personalweapon.utils;

import java.util.ArrayList;

public class CostsAndLimits {

    private static final int[] upCostsDamage = {20, 40};
    private static final int upCostsSpeed = 8;
    private static final int upCostsSmite = 6;
    private static final int upCostsArthropod = 6;
    private static final int upCostsFire = 4;
    private static final int upCostsLooting = 4;
    private static final int upCostsKnockback = 6;
    private static final int upCostsSweep = 6;

    public static int getDamagePointsCost(int damagePoints){
        if (damagePoints < upCostsDamage[0]){
            return 1;
        } else if (damagePoints < upCostsDamage[1]) {
            return 2;
        }
        return 3;
    }
    public static int getSpeedPointsCost(int speedPoints){
        if (speedPoints < upCostsSpeed){
            return 3;
        }
        return 6;
    }
    public static int getSmitePointsCost(int smitePoints){
        if (smitePoints < upCostsSmite){
            return 6;
        }
        return 10;
    }
    public static int getArthropodPointsCost(int arthropodPoints){
        if (arthropodPoints < upCostsArthropod){
            return 4;
        }
        return 8;
    }
    public static int getFirePointsCost(int firePoints){
        if (firePoints < upCostsFire){
            return 4;
        }
        return 8;
    }
    public static int getLootingPointsCost(int lootingPoints){
        if (lootingPoints < upCostsLooting){
            return 6;
        }
        return 10;
    }
    public static int getKnockbackPointsCost(int knockbackPoints){
        if (knockbackPoints < upCostsKnockback){
            return 4;
        }
        return 8;
    }

    public static int getSweepPointsCost(int sweepPoints){
        if (sweepPoints < upCostsSweep){
            return 4;
        }
        return 8;
    }
    public static int getLimitDamage(){
        return 63;
    }
    public static int getLimitSpeed(){
        return 14;
    }
    public static int getLimitSmite(){
        return 8;
    }
    public static int getLimitArthropod(){
        return 8;
    }
    public static int getLimitFire(){
        return 6;
    }
    public static int getLimitLooting(){
        return 6;
    }
    public static int getLimitKnockback(){
        return 8;
    }
    public static int getLimitSweep(){
        return 8;
    }


}
