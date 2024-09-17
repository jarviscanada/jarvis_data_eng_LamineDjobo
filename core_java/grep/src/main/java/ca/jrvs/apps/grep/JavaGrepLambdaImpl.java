package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImpl extends JavaGrepImpl{
    private static final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaImpl.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();

        if (args.length != 3) {
            logger.error("Usage: JavaGrep regex rootPath outFile");
            return;
        }


            JavaGrepLambdaImpl javaGrepLambdaImpl = new JavaGrepLambdaImpl();
            javaGrepLambdaImpl.setRegex(args[0]);
            javaGrepLambdaImpl.setRootPath(args[1]);
            javaGrepLambdaImpl.setOutFile(args[2]);

            try {
                javaGrepLambdaImpl.process();
            } catch (Exception ex) {
                logger.error("Error: Unable to process", ex);
            }

    }

    @Override
    public List<File> listFiles (String rootDir) {
        try(Stream<Path> pathStream = Files.walk(Paths.get(rootDir))){
            return pathStream.
                    filter(Files::isRegularFile).
                    map(Path::toFile).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error: Unable to list file", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> readLines (String inputFile) {
        try(Stream<String> lineStream = Files.lines(Paths.get(inputFile))){
            return lineStream.collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error: Unable to read lines", e);
            return Collections.emptyList();
        }
    }


}
