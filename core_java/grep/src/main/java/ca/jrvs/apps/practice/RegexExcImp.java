package ca.jrvs.apps.practice;
import ca.jrvs.apps.practice.RegexExc;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc{

    //Pattern to match the JPEG or jpeg file case insensitive
    private static final Pattern JPEG_PATTERN = Pattern.compile(".*\\.(jpeg|jpg)$", Pattern.CASE_INSENSITIVE);

    //Pattern to match the IP address
    private static final Pattern IP_PATTERM = Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}$");

    //Pattern to match an empty line with only whitespace
    private static final Pattern EMPTYLINE_PATTERN = Pattern.compile("^\\s*$");

    @Override
    public boolean matchJpeg(String filename) {
        if (filename == null) return false;
        return JPEG_PATTERN.matcher(filename).matches();
    }

    @Override
    public boolean matchIp(String ip) {
        if (ip == null) return false;
        return IP_PATTERN.matcher(ip).matches();
    }

    @Override
    public boolean isEmptyLine(String line) {
        if (line == null) return false;
        return EMPTY_LINE_PATTERN.matcher(line).matches();
    }

    public static void main(String[] args) {
        RegexExcImp regex = new RegexExcImp();

        System.out.println(regex.matchJpeg("file.jpeg"));
        System.out.println(regex.matchJpeg("file.JPG"));
        System.out.println(regex.matchJpeg("file.txt"));

        System.out.println(regex.matchIp("256.256.256.256"));
        System.out.println(regex.matchIp("192.168.1"));

        System.out.println(regex.isEmptyLine("\t"));
        System.out.println(regex.isEmptyLine("Not empty"));
    }


}