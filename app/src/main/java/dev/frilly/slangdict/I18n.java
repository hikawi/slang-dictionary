package dev.frilly.slangdict;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import dev.frilly.slangdict.gui.Translatable;

/**
 * A class for internalization purposes. Basically language changing.
 */
public class I18n {

    private final List<Translatable> translatables;

    private Locale locale;
    private ResourceBundle bundle;

    public I18n() {
        this.translatables = new ArrayList<>();
        this.locale = Locale.ENGLISH;
        this.bundle = ResourceBundle.getBundle("i18n", this.locale);
    }

    /**
     * Sets the locale and loads the corresponding language pack.
     * 
     * @param locale The locale.
     */
    public void setLocale(final Locale locale) {
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle("i18n", locale);
        updateTranslations();
    }

    /**
     * Returns the translation for the provided key.
     * 
     * @param key The key
     * @return The translation
     */
    public String tl(final String key) {
        return this.bundle.getString(key);
    }

    /**
     * Registers a translatable.
     * 
     * @param translatable The translatable component.
     */
    public void register(final Translatable translatable) {
        this.translatables.add(translatable);
    }

    /**
     * Invokes updates on all registered translatables.
     */
    public void updateTranslations() {
        this.translatables.forEach(Translatable::updateTranslations);
    }

}
