package algat.model.parser.special.rules;

/**
 * A rule that defines a type of object a lesson can be composed of and how to build it from the information found in a lesson file.
 *
 */
public abstract class SpecialRule {

    /**
     * @return The unique that should identify the concrete specialElements of rule while parsing a lesson file.
     */
    public abstract String getUniqueId();

    /**
     * Defines if the the head should be allowed to be styled.
     * If a concrete rule isn't allowed an then receives a head with unescaped styling characters throws an error.
     * @return true if allowed, false otherwise.
     */
    public abstract boolean allowsHeadStyling();



}
