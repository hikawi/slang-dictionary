package dev.frilly.slangdict;

/**
 * A data class for the "word" object.
 */
public class Word {

    public String word = "null";
    public String definition = "null";
    public boolean locked = false;
    public boolean favorite = false;

    @Override
    public String toString() {
        return "Word{" +
            "word='" + word + '\'' +
            ", definition='" + definition + '\'' +
            ", locked=" + locked +
            ", favorite=" + favorite +
            '}';
    }

}
