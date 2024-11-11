package dev.frilly.slangdict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import dev.frilly.slangdict.interfaces.Translatable;

/**
 * Internalization class for requesting resource bundles, using locales.
 */
public final class I18n {

    private static Locale locale = Locale.ENGLISH;
    private static Map<Locale, ResourceBundle> bundles = new HashMap<>();
    private static List<Translatable> translatables = new ArrayList<>();

    static {
        setLocale(Locale.ENGLISH);
    }

    private I18n() {
    }

    /**
     * Changes the current locale of the application.
     * 
     * @param locale The locale to change to.
     */
    public static void setLocale(Locale locale) {
        I18n.locale = locale;

        try {
            I18n.bundles.put(locale, ResourceBundle.getBundle("i18n", locale));
        } catch (Exception e) {
            System.err.printf("Tried locale %s but couldn't find.", locale.getLanguage());
        }

        reloadTranslations();
    }

    /**
     * Translates a key with printf like arguments.
     * 
     * @param key  The key
     * @param args The arguments to pass on.
     * @return The formatted string
     */
    public static String tl(final String key, final Object... args) {
        try {
            final var value = bundles.get(locale).getString(key);
            return value.formatted(args);
        } catch (Exception e) {
            e.printStackTrace();
            return key;
        }
    }

    /**
     * Clears all cached bundles.
     */
    public static void clearBundles() {
        bundles.clear();
    }

    /**
     * Register a translatable to be reloaded when locales change.
     * 
     * @param translatable The translatable.
     */
    public static void register(final Translatable translatable) {
        translatables.add(translatable);
    }

    /**
     * Unregister a translatable from being reloaded.
     * 
     * @param translatable The translatable.
     */
    public static void unregister(final Translatable translatable) {
        translatables.remove(translatable);
    }

    /**
     * Reloads all translatable components.
     */
    public static void reloadTranslations() {
        translatables.forEach(Translatable::updateTranslations);
    }

}
