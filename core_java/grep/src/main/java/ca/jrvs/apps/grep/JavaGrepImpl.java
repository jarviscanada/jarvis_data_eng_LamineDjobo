package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaGrepImpl implements JavaGrep {

    private static final Logger logger = LoggerFactory.getLogger(JavaGrepImpl.class);

    private String regex;
    private String rootPath;
    private String outFile;

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();
        for (File file : listFiles(rootPath)) {
            for (String line : readLines(file.getAbsolutePath())) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<>();
        File dir = new File(rootDir);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        fileList.addAll(listFiles(file.getAbsolutePath()));
                    } else {
                        fileList.add(file);
                    }
                }
            }
        }
        return fileList;
    }

    @Override
    public List<String> readLines(String inputFile) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", inputFile, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("Error reading file: {}", inputFile, e);
            throw new RuntimeException(e);
        }
        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    public void writeToFile(List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Error writing to file: {}", outFile, e);
            throw e;
        }
    }

    @Override
    public String getRoothPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public Void setOutFile(String outFile) {
        this.outFile = outFile;
        return null;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Usage: JavaGrep regex rootPath outfile");
        }

        // Use default logger config
        BasicConfigurator.configure();



        JavaGrepImpl javaGrepImpl = new JavaGrepImpl();
        javaGrepImpl.setRegex(args[0]);
        javaGrepImpl.setRootPath(args[1]);
        javaGrepImpl.setOutFile(args[2]);


        try {
            javaGrepImpl.process();
        } catch (Exception ex) {
            logger.error("Error: Unable to process", ex);
        }
    }
}
