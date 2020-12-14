import com.sun.deploy.security.SelectableSecurityManager;
import sun.lwawt.macosx.CSystemTray;
import sun.nio.fs.BsdFileSystemProvider;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Used https://regexr.com/ and pasted the text file to find the right regular expression with grouping
public class JerkSonParserList {

    String[] parseSingleProduct;
   // Product product = new Product();
    ArrayList<Product> products= new ArrayList();

    public Product pasreTheLine(String output) {
        String name;
        String type;
        Double price;
        String expiration;
        int countOfErrors=0;
        String[] splittedLines;
        ArrayList<Product> listWithoutErrors;
        //Split based on ## and Display it
        splittedLines = SplitBasedOnDelimeter(output);
        for (String s: splittedLines) {
            System.out.println(s);
        }
      //  System.out.println(splittedLines.length);
       for(int i=0 ; i < splittedLines.length;i++) {
           //Split each product entry by ;@^%*!
           parseSingleProduct = splittedLines[i].split("[;@^%*!]");
           //Per product , find the group based on field names and populate in the POJO
           name=findAndSetName();
           price=findAndSetPrice();
           type=findAndSetType();
           expiration=findAndSetExpirationDate();
         //  System.out.println(product.toString());
           products.add(new Product(name,price,type,expiration));
       }
        countOfErrors=countNumberOfError(products);
        //Remove the errors rows from the list
        listWithoutErrors=removeItems(products);
        System.out.println(listWithoutErrors.size());
             //Browse through the arrayList to display fancy output.

        return null;
    }


    private String findAndSetExpirationDate() {

        Pattern expiration = Pattern.compile("([Ee][Xx][Pp][Ii][Rr][Aa][Tt][Ii][Oo][Nn]):(\\d{1,2}/\\d{1,2}/\\d{4})");
        Matcher matcherExpiration = expiration.matcher(parseSingleProduct[3]);
        if (matcherExpiration.find()) {
            // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            // Does not work so changed to String instead of Date in POJO
            return matcherExpiration.group(2);
        }
        else return "";
    }

    private String findAndSetType() {
        //using + instead of * because if 0 entries found we want to return ""
        Pattern type = Pattern.compile("([Tt][Yy][Pp][Ee]):(\\w+)");
        Matcher matcherType = type.matcher(parseSingleProduct[2]);
        if (matcherType.find()) {
            return matcherType.group(2);
        }
        else
            return "";
    }

    private Double findAndSetPrice() {
        //using + instead of * because if 0 entries found we want to return 0
        Pattern price = Pattern.compile("([Pp][Rr][Ii][Cc][Ee]):(\\d+\\.\\d{2})");
        Matcher matcherPrice = price.matcher(parseSingleProduct[1]);
        if (matcherPrice.find()) {
            return Double.parseDouble(matcherPrice.group(2));
        }
        else
            return 0d;
    }


    private String findAndSetName() {
        //using + instead of * because if 0 entries found we want to return ""
        Pattern name = Pattern.compile("([Nn][Aa][Mm][Ee]):([a-zA-Z]+)");
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
        int countOfErrors=0;
        for(int i = 0; i < products.size(); i++){
            if(products.get(i).getName().equals("") || products.get(i).getPrice() == 0d || products.get(i).getType().equals("") || products.get(i).getExpiration().equals("")){

                countOfErrors++;
            }
        }
        return countOfErrors;

    }

}
