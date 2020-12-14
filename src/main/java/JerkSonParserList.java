import com.sun.deploy.security.SelectableSecurityManager;
import sun.lwawt.macosx.CSystemTray;
import sun.nio.fs.BsdFileSystemProvider;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Used https://regexr.com/ and pasted the text file to find the right regular expression with grouping
public class JerkSonParserList {
    String[] splittedLines;
    String[] parseSingleProduct;
    Product product = new Product();
    ArrayList<Product> products= new ArrayList();
    int countOfErrors=0;
    String name;
    String type;
    private Double price;
    String expiration;
    public Product pasreTheLine(String output) {
        ArrayList<Product> listWithoutErrors;
        //Split based on ## and Display it
      splittedLines = SplitBasedOnDelimeter(output);
        for (String s: splittedLines) {
            System.out.println(s);
        }
        System.out.println(splittedLines.length);
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

//
//        for (int i = 0; i < products.size(); i++) {
//            System.out.println(products.get(i).toString());
//
//        }
        listWithoutErrors=removeItemsWithErrors(products);
       for(int i=0;i< listWithoutErrors.size();i++) {
             System.out.println(listWithoutErrors.get(i).toString());
       }
        System.out.println(countOfErrors);


        //Browse through the arrayList to display fancy output.

        return null;
    }

    private String findAndSetExpirationDate() {
        Pattern expiration = Pattern.compile("[eE][xX][pP][iI][rR][aA][tT][iI][oO][nN]:(\\d{1,2}/\\d{1,2}/\\d{4})");
        Matcher matcherExpiration = expiration.matcher(parseSingleProduct[3]);
        if (matcherExpiration.find()) {
            // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            // Does not work so changed to String instead of Date in POJO
            return matcherExpiration.group(1);
        }
        else return "";
    }

    private String findAndSetType() {
        Pattern type = Pattern.compile("[tT][yY][pP][eE]:(\\w+)");
        Matcher matcherType = type.matcher(parseSingleProduct[2]);
        if (matcherType.find()) {
            return matcherType.group(1);
        }
        else
            return "";
    }

    private Double findAndSetPrice() {
        Pattern price = Pattern.compile("[pP][rR][iI][cC][eE]:(\\d+.\\d{2})");
        Matcher matcherPrice = price.matcher(parseSingleProduct[1]);
        if (matcherPrice.find()) {
            return Double.parseDouble(matcherPrice.group(1));
        }
        else
            return 0d;
    }


    private String findAndSetName() {
        Pattern name = Pattern.compile("[nN][aA][mM][eE]:([a-zA-Z]+)");
        Matcher matcher = name.matcher(parseSingleProduct[0]);
        if (matcher.find()) {
            return matcher.group(1);
        }
        else
           return "";

    }

    private String[] SplitBasedOnDelimeter(String output) {
        return output.split("#{2}");
    }
// Printing output
    public ArrayList<Product> removeItemsWithErrors(ArrayList<Product> list) {
        ArrayList<Product> output = list;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getName().equals("") || list.get(i).getPrice() == 0d || list.get(i).getType().equals("") || list.get(i).getExpiration().equals("")){
                output.remove(list.get(i));
                this.countOfErrors++;
            }
        }
        return output;
    }
}
