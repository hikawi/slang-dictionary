package dev.frilly.slangdict.events;

import dev.frilly.slangdict.interfaces.Cancellable;

/**
 * An event that is called when the HP bar takes damage.
 */
public final class DamageEvent extends Event implements Cancellable {

    private final double       currentHp;
    private final DamageReason damageReason;
    private       double       damage;
    private       boolean      cancelled;

    public DamageEvent(
        double currentHp, double damage, DamageReason damageReason) {
        this.currentHp    = currentHp;
        this.damage       = damage;
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

    public enum DamageReason {

        CLOCK,
        INCORRECT,
        SELF;

    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
