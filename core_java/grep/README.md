
# Java Grep Application

## Introduction

The Java Grep application is a command-line tool designed for system administrators and developers who need to search through large directories of text files for specific patterns. It mimics the functionality of the Unix `grep` command, allowing users to recursively search through files and output matching lines to a specified file. The application is implemented using Core Java, with enhancements like Stream API and Lambda expressions for efficient data processing. Logging is handled via SLF4J with Log4j, and the application is containerized using Docker for easy deployment. Comprehensive testing was conducted manually to ensure reliability across different scenarios, including large files and nested directories.

## Quick Start

### Prerequisites

- Java 8 or higher installed.
- Docker installed (if you plan to use the Docker image).

### Clone the Repository

```bash
git clone https://github.com/jarviscanada/jarvis_data_eng_LamineDjobo.git
cd jarvis_data_eng_LamineDjobo/core_java/grep
```

### Run the Application

#### Using an IDE (e.g., IntelliJ IDEA):

1. Open the `core_java/grep` project in your IDE.
2. Set the program arguments in the Run/Debug configuration:
   - Argument 1: `regex` - The regex pattern to search for.
   - Argument 2: `rootPath` - The root directory to start searching.
   - Argument 3: `outFile` - The output file where results will be written.
3. Run the `JavaGrepImpl` or `JavaGrepLambdaImpl` class.

#### Using Command Line:

Using Java -jar

```bash
mvn clean install
java -jar target/grep-1.0-SNAPSHOT.jar "regex" "rootPath" "outputFile"
```
Using Java -cp (Classpath)

```bash
mvn clean install
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepLambdaImpl "regex" "rootPath" "outputFile"
```

#### Using Docker:

1. Pull the Docker image from Docker Hub:

   ```bash
   docker pull lamine005/grep:latest
   ```

   If you have already built the image locally, you can skip this step.

2. Run the Docker container:

   ```bash
   docker run -v $(pwd)/data:/data lamine005/grep:latest "regex" "/data/rootPath" "/data/outputFile"
   ```

   Replace `"regex"`, `"/data/rootPath"`, and `"/data/outputFile"` with your actual parameters.

## Implementation

### Pseudocode for `process` Method

1. Initialize an empty list to store matched lines.
2. Recursively list all files in the root directory.
3. For each file:
   - Read all lines in the file.
   - Filter lines that match the regex pattern.
   - Add matching lines to the list.
4. Write the matched lines to the output file.

## Performance Issue

The original implementation using lists can consume a lot of memory when processing large files or directories, as it loads all data into memory at once. This issue can be mitigated by using Java Streams, which allow for lazy evaluation, processing data on-the-fly without holding everything in memory.

## Test

The application was manually tested by creating sample text files with known content, running the application with various regex patterns, and comparing the output file with expected results. Different scenarios, such as empty files, large files, and deeply nested directories, were tested to ensure the application?s robustness.

## Deployment

The application was dockerized for easier distribution. A Dockerfile was created to package the application into a Docker image. This image can be run as a container, making it easier to deploy across different environments. The image is available on Docker Hub, allowing for easy pulling and running of the container.

## Improvements

1. **Parallel Processing with Streams**: Implement parallel processing by using Java Streams' parallel capabilities, improving performance on large datasets.
2. **Stream-Based Interface**: Introduce a new interface that returns streams throughout the application, allowing for more efficient memory usage and better performance.
3. **Automated Testing**: Develop a comprehensive suite of automated tests, including unit and integration tests, to ensure code reliability and coverage.

