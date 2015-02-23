package getMyPrioritiesRight;

public class Element {
    
    

    private final String name;

    private final double value;
    
 

    /**
     * Creates a new element instance with name "name" and value "value"
     * 
     * @param name the name of this element instance 
     * @param value the value of this element instance 
     */
    public Element(String name, double value) {
        this.name = name;
        this.value = value;

    }

    /**
     * 
     * @return this element instance's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @return this element instance's value
     */
    public double getValue() {
        return this.value;
    }
    
    /**
     * 
     * returns a helpful string representation of this element instance 
     */
    @Override 
    public String toString(){
        return "Element N: " + this.name + " V: " + this.value;
                
    }
   
}
