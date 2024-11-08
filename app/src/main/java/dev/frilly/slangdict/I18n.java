package dev.frilly.slangdict;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import dev.frilly.slangdict.gui.Translatable;

/**
 * A class for internalization purposes. Basically language changing.
 */
public final class I18n {

    private static final List<Translatable> translatables = new ArrayList<>();

    private static Locale locale = Locale.ENGLISH;
    private static ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);

    /**
     * Sets the locale and loads the corresponding language pack.
     * 
     * @param locale The locale.
     */
    public static void setLocale(final Locale locale) {
        I18n.locale = locale;
        I18n.bundle = ResourceBundle.getBundle("i18n", locale);
        updateTranslations();
    }

    /**
     * Returns the translation for the provided key.
     * 
     * @param key The key
     * @return The translation
     */
    public static String tl(final String key) {
        return bundle.getString(key);
    }

    /**
     * Registers a translatable.
     * 
     * @param translatable The translatable component.
     */
    public static void register(final Translatable translatable) {
        translatables.add(translatable);
    }

    /**
     * Invokes updates on all registered translatables.
     */
    public static void updateTranslations() {
        translatables.forEach(Translatable::updateTranslations);
    }

}
