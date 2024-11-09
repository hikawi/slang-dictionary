package dev.frilly.slangdict;

import java.util.Locale;

/**
 * A configuration holding this application's data.
 */
public final class Configuration {

    private static Locale locale = Locale.ENGLISH;
    private static boolean autosave = true;

    private Configuration() {
    }

    public static boolean isAutosave() {
        return autosave;
    }

    public static void setAutosave(boolean autosave) {
        Configuration.autosave = autosave;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        Configuration.locale = locale;
    }

}
