package ca.jrvs.apps.grep;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {
    /**
     * Top-level method to start the processing of files.
     * @throws IOException
     */
    void process() throws IOException;

    /**
     *Recursively list all files in a given directory.
     * @param rootDir input directory
     * @return file under the root directory
     */
    List<File> listFiles (String rootDir);

    /**
     * Read all lines from a given file.
     * @param inputFile the file to read lines from
     * @return  lines  from the file
     * @throws IllegalArgumentException if the given inputFile is not a file
     */
    List<String> readLines (String inputFile);

    /**
     *
     * @param line
     * @return
     */
    boolean containsPattern (String line);

    /**
     *
     * @param line
     * @throws IOException
     */
    void writeToFile (List<String> line) throws IOException;

    /**
     *
     * @return
     */
    String getRoothPath();

    /**
     *
     * @param rootPath
     */
    void setRootPath(String rootPath);

    /**
     *
     * @return
     */
    String getRegex();

    /**
     *
     * @param regex
     */
    void setRegex(String regex);

    /**
     *
     * @return
     */
    String getOutFile() ;

    /**
     *
     * @param outFile
     * @return
     */
    Void setOutFile (String outFile);
}
