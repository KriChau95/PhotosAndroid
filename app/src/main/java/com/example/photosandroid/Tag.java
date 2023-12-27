package com.example.photosandroid;

import java.io.Serializable;

/**
 * The {@code Tag} class represents a tag with a name and a corresponding value.
 * Tags are used to categorize and annotate photos.
 * <p>
 * @author Krishaan Chaudhary & Joshua Clayton
 * This class implements the {@link Serializable} interface to allow for serialization.
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String value;

    /**
     * Tag Constructor - Constructs a Tag with a given name and value.
     *
     * @param name  The name of the tag.
     * @param value The value of the tag.
     */
    public Tag(String name, String value){
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name of the tag.
     *
     * @return The name of the tag.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the value of the tag.
     *
     * @return The value of the tag.
     */
    public String getValue(){
        return this.value;
    }

    /**
     * Returns a string representation of the tag in the format "name:value".
     *
     * @return A string representation of the tag.
     */
    public String toString(){
        return name + ":" + value;
    }

    /**
     * Overriding the Object class equal method to check for equality between tags.
     *
     * @param o The object to compare to.
     * @return {@code true} if the tags' names and values are equal, {@code false} otherwise.
     */
    public boolean equals(Object o){
        if (o == null || !(o instanceof Tag)){
            return false;
        }
        Tag other = (Tag) o;
        return this.name.equals(other.getName()) && this.value.equals(other.getValue());
    }
}
