package org.interview;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


public class SessionLogProcessor {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private static final Pattern ALPHABETIC_PATTERN = Pattern.compile("^[A-Za-z]+$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[A-Za-z]+\\d+$");
    private static final String START = "Start";
    private static final String END = "End";
    
    public static void processLog(String filePath) throws IOException, ParseException {
    	if(filePath != null) {
    	logProcessor(filePath);
    	}
    }

    private static void logProcessor(String filePath) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        Map<String, List<Session>> userSessions = new HashMap<>();
        Map<String, Stack<Date>> startTimes = new HashMap<>();
        Date initialTime = null;
        Date latestTime = null;

        while ((line = reader.readLine()) != null) {
            String[] segments = line.trim().split(" ");
            if (segments.length != 3 || !isValidTimeFormat(segments[0]) || !isValidUserFormat(segments[1]) || !isValidAction(segments[2])) {
                continue; // Skip invalid lines
            }

            Date currentTimeStamp = TIME_FORMAT.parse(segments[0]);
            String user = segments[1];
            String action = segments[2];

            if (initialTime == null || currentTimeStamp.before(initialTime)) {
                initialTime = currentTimeStamp;
            }
            if (latestTime == null || currentTimeStamp.after(latestTime)) {
                latestTime = currentTimeStamp;
            }

            if (!userSessions.containsKey(user)) {
                userSessions.put(user, new ArrayList<>());//Initialize userSession  if empty
                startTimes.put(user, new Stack<>());//Initialize StartTimes map if empty
            }

            if (action.equalsIgnoreCase(START)) {
                startTimes.get(user).push(currentTimeStamp);
            } else if (action.equalsIgnoreCase(END)) {
                Stack<Date> userStartTimes = startTimes.get(user);
                if (!userStartTimes.isEmpty()) {
                    Date startTime = userStartTimes.pop();
                    userSessions.get(user).add(new Session(startTime, currentTimeStamp));
                } else {
                    // For users with no start time use initialTime
                    userSessions.get(user).add(new Session(initialTime, currentTimeStamp));
                }
            }
        }
        reader.close();

        // Remaining values with unmatched start Time
        for (Map.Entry<String, Stack<Date>> entry : startTimes.entrySet()) {
            String user = entry.getKey();
            Stack<Date> userStartTimes = entry.getValue();
            while (!userStartTimes.isEmpty()) {
                Date startTime = userStartTimes.pop();
                userSessions.get(user).add(new Session(startTime, latestTime));
            }
        }

        // Print results
        for (Map.Entry<String, List<Session>> entry : userSessions.entrySet()) {
            String user = entry.getKey();
            List<Session> sessions = entry.getValue();
            long totalDuration = sessions.stream().mapToLong(Session::getDuration).sum();
            int numSessions = sessions.size();
            System.out.printf("%s %d %d\n", user, numSessions, totalDuration);
        }
    }

    // Method to check if the input matches with the Date format
    public static boolean isValidTimeFormat(String timeString) {
        if (timeString == null || timeString.length() != 8) {
            return false;
        }

        try {
            // Ensuring strict Parsing
            TIME_FORMAT.setLenient(false);
            TIME_FORMAT.parse(timeString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Method to check if the input matches with the name format
    public static boolean isValidUserFormat(String input) {
        return input != null && (ALPHABETIC_PATTERN.matcher(input).matches() || ALPHANUMERIC_PATTERN.matcher(input).matches());
    }

    // Method to check if the input matches either Start or End
    public static boolean isValidAction(String input) {
        return input != null &&("start".equalsIgnoreCase(input) || "end".equalsIgnoreCase(input));
    }


    private static class Session {
        //An inner class that defines session with start time and end time and a method called duration to find the difference between two sessions
        public Date startTime;
        public Date endTime;

        public Session(Date startTime, Date endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public long getDuration() {
            return (endTime.getTime() - startTime.getTime()) / 1000;
            //Dividing by 1000 since we want the results in seconds
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Sorry!Please provide the path of your log file");
            System.exit(1);
        }

        String logFilePath = args[0];
        try {
            processLog(logFilePath);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}