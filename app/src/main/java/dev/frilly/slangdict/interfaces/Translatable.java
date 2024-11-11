package dev.frilly.slangdict.interfaces;

/**
 * An interface that denotes a component is translatable, and shall have
 * translations loaded when locale changes.
 */
public interface Translatable {

    /**
     * Update the translations.
     */
    void updateTranslations();

}
