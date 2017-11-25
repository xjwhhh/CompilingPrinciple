package lexicalParser;

import java.io.*;
import java.util.ArrayList;

public class Analyser {
    static String[] keywords={"public","protected","private","class","static","void","main","String","int","double","float","char",
    "if","else","else if","do","while","try","catch","finally","case","switch","case","break","for"};

    static String[] operation={"[","]","(",")","++","--","!","~","*","/","%","+","-","<<",">>","<>","<","=",">","==","!=","|","&","^","&&","||","+=","-=","<=",">="};

    static String[] punctuation={",","{","}",";","."};

    static ArrayList<Character> input=new ArrayList<>();

    static int inputPointer=0;


    public static void getInput(){
        String inputFile = "C:\\Users\\xjwhh\\IdeaProjects_Ultimate\\CompilingPrinciple\\src\\lexicalParser\\input.txt";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
            String line="";
            char[] temp = null;
            while((line=br.readLine())!=null){
                temp = line.toCharArray();
                for (int i = 0; i < temp.length; i++) {
                    if (temp[i] == ' '|| temp[i]=='\t'||temp[i]=='\n') {
                        continue;
                    }
                    input.add(temp[i]);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Token scanner(){
        ArrayList<Character> words=new ArrayList<>();
        char ch=input.get(inputPointer++);
        //字母开头，可能是保留字或者标识符
        if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){
            while ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z')
                    || (ch >= 'A' && ch <= 'Z')) {
                words.add(ch);
                ch=input.get(inputPointer++);
                //判断是否是关键字
                for(int i=0;i<keywords.length;i++) {
                    //关键字
                    if (charsToString(words).equals(keywords[i])){
                        inputPointer--;
                        return new Token("keyword",keywords[i]);
                    }
                }
            }
            //标识符
            inputPointer--;
            return new Token("id",charsToString(words));
        }

        //数字开头,是数字
        else if (ch >= '0' && ch <= '9') {
            int num = 0;
            while (ch >= '0' && ch <= '9') {
                num = num * 10 + ch - '0';
                ch = input.get(inputPointer++);
            }
            inputPointer--;
            //超过int范围
            if (num < 0) {
                return new Token("error","integer overflow");
            }
            else{
                return new Token("int",String.valueOf(num));
            }
        }

        //其余符号开头
        else {
            words.add(ch);
            switch (ch){
                case '"':
                    do{
                        ch=input.get(inputPointer++);
                        words.add(ch);
                    }while(ch!='"');
                    return new Token("literal",charsToString(words));
                case '+':
                    ch=input.get(inputPointer++);
                    if(ch=='+'){
                        return new Token("operation","++");
                    }
                    else if(ch=='='){
                        return new Token("operation","+=");
                    }

                    else {
                        inputPointer--;
                        return new Token("operation", "+");
                    }

                case '-':
                    ch=input.get(inputPointer++);
                    if(ch=='-'){
                        return new Token("operation","--");
                    }
                    else if(ch=='='){
                        return new Token("operation","-=");
                    }
                    else if (ch >= '0' && ch <= '9') { // 可能是负数
                        int num = 0;
                        while (ch >= '0' && ch <= '9') {
                            num = num * 10 + ch - '0';
                            ch = input.get(inputPointer++);
                        }
                        return new Token("int",String.valueOf(num*(-1)));
                    }
                    else{
                        return new Token("operation","-");
                    }
                case '=':
                    ch=input.get(inputPointer++);
                    if(ch=='='){
                        return new Token("operation","==");
                    }
                    else {
                        inputPointer--;
                        return new Token("operation","=");
                    }
                case '!':
                    ch=input.get(inputPointer++);
                    if(ch=='='){
                        return new Token("operation","!=");
                    }
                    else {
                        inputPointer--;
                        return new Token("operation","!");
                    }
                case '<':
                    ch=input.get(inputPointer++);
                    if(ch=='='){
                        return new Token("operation","<=");
                    }
                    else if(ch=='<'){
                        return new Token("operation","<<");
                    }
                    else if(ch=='>'){
                        return new Token("operation","<>");
                    }
                    else {
                        inputPointer--;
                        return new Token("operation","<");
                    }
                case '>':
                    ch=input.get(inputPointer++);
                    if(ch=='='){
                        return new Token("operation",">=");
                    }
                    else if(ch=='>'){
                        return new Token("operation",">>");
                    }
                    else {
                        inputPointer--;
                        return new Token("operation",">");
                    }
                case '&':
                    ch=input.get(inputPointer++);
                    if(ch=='&'){
                        return new Token("operation","&&");
                    }
                    else {
                        inputPointer--;
                        return new Token("operation","&");
                    }

                case '|':
                    ch=input.get(inputPointer++);
                    if(ch=='|'){
                        return new Token("operation","||");
                    }
                    else {
                        inputPointer--;
                        return new Token("operation","|");
                    }
                case '[':
                case ']':
                case '(':
                case ')':
                case '~':
                case '*':
                case '/':
                case '%':
                case '^':
                    return new Token("operation",String.valueOf(ch));
                case '{':
                case '}':
                case ',':
                case ';':
                case '.':
                    return new Token("punctuation",String.valueOf(ch));
                default:
                    return new Token("error","undefined token");

            }
        }

    }

    public static String charsToString(ArrayList<Character> list){
        char[] chars=new char[list.size()];
        for(int i=0;i<list.size();i++){
            chars[i]=list.get(i);
        }
        String s=String.valueOf(chars);
        return s;
    }

    public ArrayList<Token> getTokens(){
        ArrayList<Token> tokens=new ArrayList<>();
        getInput();
        while(inputPointer<input.size()){
            Token token=scanner();
            tokens.add(token);
        }
        return tokens;
    }



    public static void main(String[] args){
        getInput();
        while(inputPointer<input.size()){
            Token token=scanner();
            System.out.println(token.toString());
        }
    }

}
