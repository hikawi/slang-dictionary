package dev.frilly.slangdict.events;

import dev.frilly.slangdict.interfaces.Cancellable;

/**
 * An event that is called when the HP bar takes damage.
 */
public final class DamageEvent extends Event implements Cancellable {

    public enum DamageReason {

        CLOCK,
        INCORRECT,
        SELF;

    }

    private final double currentHp;
    private double damage;
    private boolean cancelled;
    private final DamageReason damageReason;

    public DamageEvent(double currentHp, double damage, DamageReason damageReason) {
        this.currentHp = currentHp;
        this.damage = damage;
        this.damageReason = damageReason;
    }

    public DamageReason getDamageReason() {
        return damageReason;
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

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

}
