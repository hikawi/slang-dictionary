package dev.frilly.slangdict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A configuration holding this application's data.
 */
public final class Configuration {

    private static Locale locale = Locale.ENGLISH;
    private static boolean autosave = true;

    private static Set<String> favoriteWords = new HashSet<>();
    private static List<String> words = new ArrayList<>(); // For natural ordering of words.
    private static Map<String, String> dictionary = new HashMap<>(); // maps word -> definition.

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

    public static void clearWords() {
        dictionary.clear();
    }

    public static boolean hasWord(final String word) {
        return dictionary.containsKey(word.toLowerCase()); // For O(1) lookup.
    }

    public static void removeAt(final int idx) {
        final var key = words.remove(idx);
        dictionary.remove(key);
    }

    public static void putWord(final String key, final String definition) {
        words.add(key);
        dictionary.put(key.toLowerCase(), definition);
    }

    public static String getDefinition(final String key) {
        return dictionary.get(key.toLowerCase());
    }

    public static void sortWords() {
        Collections.sort(words);
    }

    public static List<Entry<String, String>> getWordsEntries() {
        return words.stream().map(e -> Map.entry(e, getDefinition(e))).toList();
    }

    public static void clearFavorites() {
        favoriteWords.clear();
    }

    public static void addFavorite(final String word) {
        favoriteWords.add(word.toLowerCase());
    }

    public static void removeFavorite(final String word) {
        favoriteWords.remove(word.toLowerCase());
    }

    public static boolean isFavorite(final String word) {
        return favoriteWords.contains(word.toLowerCase());
    }

}
