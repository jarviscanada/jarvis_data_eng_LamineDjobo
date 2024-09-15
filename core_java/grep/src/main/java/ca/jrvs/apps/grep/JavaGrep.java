package ca.jrvs.apps.grep;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {

    void process() throws IOException;

    List<File> listFiles (String rootDir);

    List<String> readLines (String inputFile);

    boolean containsPattern (String line);

    void writeToFile (List<String> line) throws IOException;

    String getRoothPath();

    void setRootPath(String rootPath);

    String getRegex();

    void setRegex(String regex);

    String getOutFile() ;

    Void setOutFile (String outFile);
}
