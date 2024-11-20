package dev.frilly.slangdict.gui.events;

/**
 * An event that is called when the HP bar takes damage.
 */
public final class DamageEvent extends Event {

    private final double currentHp;
    private double damage;

    public DamageEvent(double currentHp, double damage) {
        this.currentHp = currentHp;
        this.damage = damage;
    }

    public double getCurrentHp() {
        return currentHp;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
    
}
