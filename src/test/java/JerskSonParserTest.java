

import java.io.BufferedReader;
import java.io.FileReader;
import org.junit.Assert;
import org.junit.Test;
import java.io.*;

public class JerskSonParserTest {

    @Test
        public void testFileWriterROT13(){
            try{
                BufferedReader readerOriginalFile = new BufferedReader(new FileReader("output.txt"));
                BufferedReader readerCreatedFile = new BufferedReader(new FileReader("MyPrettyOutput.txt"));
                String Expected;
                String Actual;
                while(( readerOriginalFile.readLine()) != null && (readerCreatedFile.readLine()) != null){
                    Expected = readerOriginalFile.readLine();
                    Actual = readerCreatedFile.readLine();
                    Assert.assertEquals(Expected, Actual);
                }

                readerOriginalFile.close();
                readerCreatedFile.close();
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }




    }






