package misson_improbable;

public class Variable {
    
    private final String name;
    private final double probability;
    
    public Variable(String name, double probability){
        this.name = name;
        this.probability = probability;       
    }
    
    public String getName(){
        return this.name;
    }
    
    public double getProbability(){
        return this.probability;
    }
    
   
    

}
