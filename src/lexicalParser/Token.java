package lexicalParser;

public class Token {
    String type;
    String property;

    public Token(String type,String property){
        this.type=type;
        this.property=property;
    }

    public String toString(){
        return "<"+this.type+","+this.property+">";
    }

    public String getType() {
        return type;
    }

    public String getProperty() {
        return property;
    }
}
