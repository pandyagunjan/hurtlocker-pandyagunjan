import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Used https://regexr.com/ and pasted the text file to find the right regular expression with grouping
public class JerkSonParserList {
    String[] splittedLines;
    String[] parseSingleProduct;
    Product product = new Product();
    ArrayList<Product> products= new ArrayList();

    public Product pasreTheLine(String output) {
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
           findName();
           findPrice();
           findType();
           findExpirationDate();
           System.out.println(product.toString());
           products.add(product);
       }

        //Browse through the arrayList to display fancy output.

        return null;
    }

    private void findExpirationDate() {
        Pattern expiration = Pattern.compile("[eE][xX][pP][iI][rR][aA][tT][iI][oO][nN]:(\\d{1,2}/\\d{1,2}/\\d{4})");
        Matcher matcherExpiration = expiration.matcher(parseSingleProduct[3]);
        if (matcherExpiration.find()) {
            // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            // Does not work so changed to String instead of Date in POJO
            product.setExpiration(matcherExpiration.group(1));

        }
    }

    private void findType() {
        Pattern type = Pattern.compile("[tT][yY][pP][eE]:(\\w+)");
        Matcher matcherType = type.matcher(parseSingleProduct[2]);
        if (matcherType.find()) {
            product.setType(matcherType.group(1));
        }
        else
            product.setType("");
    }

    private void findPrice() {
        Pattern price = Pattern.compile("[pP][rR][iI][cC][eE]:(\\d+.\\d{2})");
        Matcher matcherPrice = price.matcher(parseSingleProduct[1]);
        if (matcherPrice.find()) {
            product.setPrice(Double.parseDouble(matcherPrice.group(1)));
        }
        else
            product.setPrice(0d);
    }


    private void findName() {
        Pattern name = Pattern.compile("[nN][aA][mM][eE]:([a-zA-Z]+)");
        Matcher matcher = name.matcher(parseSingleProduct[0]);
        if (matcher.find()) {
            product.setName(matcher.group(1));
        }
        else
            product.setName("");

    }

    private String[] SplitBasedOnDelimeter(String output) {
        return output.split("#{2}");
    }


}
