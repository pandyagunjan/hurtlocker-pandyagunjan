import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Used https://regexr.com/ and pasted the text file to find the right regular expression with grouping
public class JerkSonParserList {

    String[] parseSingleProduct;
    ArrayList<Product> products= new ArrayList();
    int countOfErrors=0;
    int milkCounter=0;
    int cookieCounter=0;
    int breadCounter=0;
    int appleCounter=0;
    //LinkedHashMap<Double ,Integer> prices = new LinkedHashMap<>();
    //Will try later to see if only one hashMap can be used.
    LinkedHashMap<Double ,Integer> priceMapMilk = new LinkedHashMap<>();
    LinkedHashMap<Double ,Integer> priceMapCookies = new LinkedHashMap<>();
    LinkedHashMap<Double ,Integer> priceMapBread = new LinkedHashMap<>();
    LinkedHashMap<Double ,Integer> priceMapApples = new LinkedHashMap<>();
    LinkedHashMap<String ,LinkedHashMap<Double,Integer>> productMap=new LinkedHashMap<>();
    StringBuilder str = new StringBuilder();

    public void parseTheLineAndPrint(String output) throws IOException {
        String[] splittedLines;
        //Split output based on ##
        splittedLines = SplitBasedOnDelimeter(output);
        //Parse each string array element to make product list
        parseAndCreateProductList(splittedLines);
        //Remove the errors rows from the list and capture the count of errors.
        countOfErrors=products.size();
        countOfErrors= countOfErrors-removeItems(products).size();
        //Based on the Name : Apples ,Cookies, Milk Or Bread, populate the HashMaps
        populateMapsBasedonName(); //returns boolean, not capturing it as added for TDD only.
        formatOutPut(); // Method to format the output
        System.out.println(str);
        WriteStringOnFile();//Write the string to a file
    }

    private Boolean WriteStringOnFile() throws IOException {
        File newTextFile = new File("MyPrettyOutput.txt");
        FileWriter fw = new FileWriter(newTextFile);
        fw.write(str.toString());
        fw.close();
        return true;
    }

    //Split each product entry by ;@^%*! to get Name,Price,Type,Expiration and add product in product list.
    private void parseAndCreateProductList(String[] splittedLines) {
        //Variable to catch the returned group data and store in object
        String expiration;
        String name;
        Double price;
        String type;
        for(int i = 0; i < splittedLines.length; i++) {
            parseSingleProduct = splittedLines[i].split("[;@^%*!]");
            name=findAndReturnName();
            price= findAndReturnPrice();
            type= findAndReturnType();
            expiration= findAndReturnExpirationDate();
           products.add(new Product(name,price,type,expiration));
        }
    }
//Format the output matching to the expected output.txt file
    private void formatOutPut() {
        for(String s : productMap.keySet())
        {
            if(s.equals("Milk")) {
                str.append(String.format("name:%8s        seen:%2d times\n", s, milkCounter));
                str.append("=============        =============\n");
                for(Map.Entry milk : priceMapMilk.entrySet())
                {
                    str.append(String.format("Price:%7s        seen:%2d time%s\n", milk.getKey(), milk.getValue(),plural((Integer) milk.getValue())));
                    str.append("-------------        -------------\n");

                }
            }
            else
            if(s.equals("Bread")) {
                str.append(String.format("\nname:%8s        seen:%2d times\n", s, breadCounter));
                str.append("=============        =============\n");
                for(Map.Entry bread : priceMapBread.entrySet())
                {
                    str.append(String.format("Price:%7s        seen:%2d time%s\n", bread.getKey(), bread.getValue(),plural((Integer) bread.getValue())));
                    str.append("-------------        -------------\n");
                }
            }
            else
            if(s.equals("Apples")) {
                str.append(String.format("\nname:%8s        seen:%2d times\n", s, appleCounter));
                str.append("=============        =============\n");
                for(Map.Entry apples : priceMapApples.entrySet())
                {
                    str.append(String.format("Price:%7s        seen:%2d time%s\n", apples.getKey(), apples.getValue(),plural((Integer) apples.getValue())));
                    str.append("-------------        -------------\n");
                }
            }
           else
            if(s.equals("Cookies")) {
                str.append(String.format("\nname:%8s        seen:%2d times\n", s, cookieCounter));
                str.append("=============        =============\n");
                for(Map.Entry cookies : priceMapCookies.entrySet())
                {
                    str.append(String.format("Price:%7s        seen:%2d time%s\n", cookies.getKey(), cookies.getValue(),plural((Integer) cookies.getValue())));
                    str.append("-------------        -------------\n");
                }
            }
        }

        str.append(String.format("\nErrors               seen: %d time%s" , countOfErrors,plural(countOfErrors)));
    }

    private String plural(Integer value) {
        return value >1 ? "s" : "";
    }


    private Boolean populateMapsBasedonName() {
        Boolean flag= false;
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if(p.getName().equalsIgnoreCase("Milk"))
            {
                flag = populateMilkHashMap(p);
            }
           else
            if(p.getName().equals("Co0kieS") || p.getName().equalsIgnoreCase("Cookies"))
            {
                flag = populateCookiesHashMap(p);
            }
            else
            if(p.getName().equalsIgnoreCase("Bread"))
            {
                flag = populateBreadHashMap(p);
            }
            else
            if(p.getName().equalsIgnoreCase("Apples"))
            {
                flag = populateApplesHashMap(p);
            }
        }
       return flag;
    }


    private Boolean populateApplesHashMap(Product p) {
        Boolean flag;
        appleCounter++;
        Integer temp=priceMapApples.getOrDefault(p.getPrice(),0);
        priceMapApples.put(p.getPrice(), temp+1);
        productMap.put("Apples" , priceMapApples);
        flag=true;
        return flag;
    }

    private Boolean populateBreadHashMap(Product p) {
        Boolean flag;
        breadCounter++;
        Integer temp=priceMapBread.getOrDefault(p.getPrice(),0);
        priceMapBread.put(p.getPrice(), temp+1);
        productMap.put("Bread" , priceMapBread);
        flag=true;
        return flag;
    }


    private Boolean populateCookiesHashMap(Product p) {
        Boolean flag;
        cookieCounter++;
        Integer temp=priceMapCookies.getOrDefault(p.getPrice(),0);
        priceMapCookies.put(p.getPrice(), temp+1);
        productMap.put("Cookies" , priceMapCookies);
        flag=true;
        return flag;
    }


    private Boolean populateMilkHashMap(Product p) {
        Boolean flag;
        milkCounter++;
        Integer temp=priceMapMilk.getOrDefault(p.getPrice(),0);
        priceMapMilk.put(p.getPrice(), temp+1);
        productMap.put("Milk" , priceMapMilk);
        flag=true;
        return flag;
    }


    private String findAndReturnExpirationDate() {

        Pattern expiration = Pattern.compile("([Ee][Xx][Pp][Ii][Rr][Aa][Tt][Ii][Oo][Nn]):(\\d{1,2}/\\d{1,2}/\\d{4})");
        Matcher matcherExpiration = expiration.matcher(parseSingleProduct[3]);
        if (matcherExpiration.find()) {
            // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            // Does not work so changed to String instead of Date in POJO
            return matcherExpiration.group(2);
        }
        else return "";
    }

    private String findAndReturnType() {
        //using + instead of * because if 0 entries found we want to return ""
        Pattern type = Pattern.compile("([Tt][Yy][Pp][Ee]):(\\w+)");
        Matcher matcherType = type.matcher(parseSingleProduct[2]);
        if (matcherType.find()) {
            return matcherType.group(2);
        }
        else
            return "";
    }

    private Double findAndReturnPrice() {
        //using + instead of * because if 0 entries found we want to return 0
        Pattern price = Pattern.compile("([Pp][Rr][Ii][Cc][Ee]):(\\d*\\.\\d{2})");
        Matcher matcherPrice = price.matcher(parseSingleProduct[1]);
        if (matcherPrice.find()) {
            return Double.parseDouble(matcherPrice.group(2));
        }
        else {
           // throw new IllegalArgumentException()
            return 0d;
        }

    }

    private String findAndReturnName() {
        //using + instead of * because if 0 entries found we want to return ""
        Pattern name = Pattern.compile("([Nn][Aa][Mm][Ee]):([a-zA-Z0-9]+)");
        Matcher matcher = name.matcher(parseSingleProduct[0]);
        if (matcher.find()) {
            return matcher.group(2);
        }
        else
           return "";

    }

    private String[] SplitBasedOnDelimeter(String output) {
        return output.split("#{2}");
    }

    public ArrayList<Product> removeItems(ArrayList<Product> list) {
        ArrayList<Product> output = list;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getName().equals("") || list.get(i).getPrice() == 0d || list.get(i).getType().equals("") || list.get(i).getExpiration().equals("")){
                output.remove(list.get(i));
            }
        }
        return output;
    }


}
