import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JerkSonParserList {
    String[] splittedLines;
    String[] splitBasedonSemicolon;
    Product product = new Product();
    ArrayList<Product> products= new ArrayList();

    public Product pasreTheLine(String output) {
        //Split based on ##
      splittedLines = SplitBasedOnDelimeter(output);
        for (String s: splittedLines) {
            System.out.println(s);

        }
        //System.out.println(splitTedLines);
        System.out.println(splittedLines.length);
       for(int i=0 ; i < splittedLines.length;i++) {
           splitBasedonSemicolon = splittedLines[i].split("[;@^%*!]");
               Pattern name = Pattern.compile("[nN][aA][mM][eE]:([a-zA-Z]+)");
               Matcher matcher = name.matcher(splitBasedonSemicolon[0]);
               if (matcher.find()) {
                   product.setName(matcher.group(1));
               }

               Pattern price = Pattern.compile("[pP][rR][iI][cC][eE]:(\\d+.\\d{2})");
               Matcher matcherPrice = price.matcher(splitBasedonSemicolon[1]);
               if (matcherPrice.find()) {
                   product.setPrice(Double.parseDouble(matcherPrice.group(1)));
               }
               Pattern type = Pattern.compile("[tT][yY][pP][eE]:(\\w+)");
               Matcher matcherType = type.matcher(splitBasedonSemicolon[2]);
               if (matcherType.find()) {
                   product.setType(matcherType.group(1));
               }
               Pattern expiration = Pattern.compile("[eE][xX][pP][iI][rR][aA][tT][iI][oO][nN]:(\\d{1,2}/\\d{1,2}/\\d{4})");
               Matcher matcherExpiration = expiration.matcher(splitBasedonSemicolon[3]);
               if (matcher.find()) {
                   // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                   product.setExpiration(matcherExpiration.group(1));
                   //For each array of String set the Product POJO - ArrayList ??
               }
               System.out.println(product.toString());
               products.add(product);



       }



        //Browse through the arrayList to display fancy output.

        return null;
    }

    private String[] SplitBasedOnDelimeter(String output) {
        return output.split("#{2}");
    }


}
