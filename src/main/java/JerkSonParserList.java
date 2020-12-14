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
    LinkedHashMap<Double ,Integer> priceMapMilk = new LinkedHashMap<>();
    LinkedHashMap<Double ,Integer> priceMapCookies = new LinkedHashMap<>();
    LinkedHashMap<Double ,Integer> priceMapBread = new LinkedHashMap<>();
    LinkedHashMap<Double ,Integer> priceMapApples = new LinkedHashMap<>();
    LinkedHashMap<String ,LinkedHashMap<Double,Integer>> productMap=new LinkedHashMap<>();

    public void pasreTheLine(String output) {
        //Variable to catch the returned group data and store in object
        String name;
        String type;
        Double price;
        String expiration;
        //Counter of errors for display

        String[] splittedLines;
        ArrayList<Product> listWithoutErrors;
        //Split based on ## and Display it
        splittedLines = SplitBasedOnDelimeter(output);
//        for (String s: splittedLines) {
//            System.out.println(s);
//        }

       for(int i=0 ; i < splittedLines.length;i++) {
           //Split each product entry by ;@^%*!
           parseSingleProduct = splittedLines[i].split("[;@^%*!]");
           //Per product , find the group based on field names and populate in the POJO
           name=findAndReturnName();
           price= findAndReturnPrice();
           type= findAndReturnType();
           expiration= findAndReturnExpirationDate();
         //  System.out.println(product.toString());
           products.add(new Product(name,price,type,expiration));
       }
        countOfErrors=countNumberOfError(products);
        //Remove the errors rows from the list
        listWithoutErrors=removeItems(products);
        System.out.println("*********** ITEMS WITHOUT ERRORS ****************");
        for (Product p: products) {
            System.out.println(p.toString());
        }

        //Based on the Name : Apples ,Cookies, Milk Or Bread, popualte the HashMaps
        populateMapsBasedonName(); //returns boolean, not capturing it as added for TDD only.

        System.out.println(productMap);
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
  // Printing output
    public ArrayList<Product> removeItems(ArrayList<Product> list) {
        ArrayList<Product> output = list;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getName().equals("") || list.get(i).getPrice() == 0d || list.get(i).getType().equals("") || list.get(i).getExpiration().equals("")){
                output.remove(list.get(i));
            }
        }
        return output;
    }

    private int countNumberOfError(ArrayList<Product> products) {
        int countOfErrorsLocal=0;
        for(int i = 0; i < products.size(); i++){
            if(products.get(i).getName().equals("") || products.get(i).getPrice() == 0d || products.get(i).getType().equals("") || products.get(i).getExpiration().equals("")){
                countOfErrorsLocal++;
            }
        }
        return countOfErrorsLocal;

    }

}
