package dev.frilly.slangdict;

import java.util.List;

/**
 * A data class for the "word" object.
 */
public class Word {

    public String word;
    public List<String> definition;
    public boolean locked = false;
    public boolean favorite = false;

}
