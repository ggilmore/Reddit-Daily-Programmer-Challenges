package markov_chain_error_detection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileUtilities {

    private final static String FILE_LIST_NAME = "ParsedFiles.txt";
    
    private final static String MARKOV_CHAIN_MATRIX_NAME = "MarkovChainMatrixData.mcm";

    public static boolean isFileParsed(String filePathToCheck){
        File parsedFiles = new File(
                "src/markov_chain_error_detection/Resources/DocumentParserData/"
                        + FILE_LIST_NAME);
        if (parsedFiles.exists()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(parsedFiles));
                String fileLine = reader.readLine();
                while (fileLine != null){
                    String trimmedFileLine = fileLine.trim();
                    if (trimmedFileLine.equals(filePathToCheck)){
                        return true;
                    }
                }
                return false;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new RuntimeException("FileNotFoundExcepion: "+e);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new RuntimeException("IOException: "+e);
            }
        }
        else{
            return false;
        }   
     
    }
    
    

}
