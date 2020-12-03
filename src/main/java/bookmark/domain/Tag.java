
package bookmark.domain;

/**
 * Representing book.
 */
public class Tag {
    
    private final String name;
    private int id;
    
    /**
    * Create tag with name
    *
    * @param tagName
    */
    public Tag(String tagName) {
        this.name = tagName;
    }
    
    /**
    * Create tag with id and name
    *
    * @param tagId
    * @param tagName
    */
    public Tag(int tagId, String tagName) {
        this.name = tagName;
        this.id = tagId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    
}
