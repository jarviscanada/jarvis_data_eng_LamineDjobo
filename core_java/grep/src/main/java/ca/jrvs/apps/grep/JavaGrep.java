package ca.jrvs.apps.grep;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {
    /**
     * Top-level method to start the processing of files.
     * @throws IOException if an I/O error occurs during file reading or writing
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
     * Check if a given line contains the pattern specified by the regex.
     * @param line the line of text to check against the regex pattern
     * @return true if the line matches the pattern, false otherwise
     */
    boolean containsPattern (String line);

    /**
     * Write the list of matched lines to the output file.
     * @param line the list of lines to write to the output file
     * @throws IOException if an I/O error occurs during file writing
     */
    void writeToFile (List<String> line) throws IOException;

    /**
     * Get the root path where the file search starts.
     *
     * @return the root directory path as a string
     */
    String getRoothPath();

    /**
     * Set the root path where the file search should start.
     *
     * @param rootPath rootPath the root directory path as a string
     */
    void setRootPath(String rootPath);

    /**
     * Get the regular expression pattern to use for searching within files.
     *
     * @return regex the regex pattern as a string
     */
    String getRegex();

    /**
     * Set the regular expression pattern to use for searching within files.
     *
     * @param regex regex the regex pattern as a string
     */
    void setRegex(String regex);

    /**
     * Get the output file path where the results will be written.
     *
     * @return the output file path as a string
     */
    String getOutFile() ;

    /**
     * Set the output file path where the results will be written.
     *
     * @param outFile the output file path as a string

     */
    Void setOutFile (String outFile);
}
