package sample.java8.ch5;

public class Trader {

    private String name;
    private String city;
    
    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Trader: "+this.name + " in " + this.city;
    }
}
