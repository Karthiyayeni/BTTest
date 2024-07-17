package org.test;

import org.interview.SessionLogProcessor;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;


public class SessionLogProcessorTest {
	
	 @Test
	    public void testProcessLog_NullFilePath() throws IOException, ParseException {
	            SessionLogProcessor.ProcessLog(null);
	        
	    }
	 
	 @Test
	    public void testProcessLog_NonNullFilePath() throws IOException, ParseException {
	        	String dir = System.getProperty("user.dir");
	            SessionLogProcessor.ProcessLog(dir+"/src/main/resources/sessiondatalog.txt");
	        
	    }
	 
	 
	 @Test
	    public void testProcessLog_isValidTimeFormat() throws IOException, ParseException {
		 assertFalse(SessionLogProcessor.isValidTimeFormat(null));
		 assertFalse(SessionLogProcessor.isValidTimeFormat("1234"));
		 assertFalse(SessionLogProcessor.isValidTimeFormat("123456789"));
		 assertTrue(SessionLogProcessor.isValidTimeFormat("14:02:05"));
		 
	    }
	 
	 
	 
	 @Test
	    public void testProcessLog_isValidUserFormat() throws IOException, ParseException {
		 assertFalse(SessionLogProcessor.isValidUserFormat(null));
		 assertTrue(SessionLogProcessor.isValidUserFormat("ALICE"));
		 assertTrue(SessionLogProcessor.isValidUserFormat("CHARLIE99"));
		 assertFalse(SessionLogProcessor.isValidUserFormat("123"));
		 
	    }
	 
	 @Test
	    public void testProcessLog_isValidAction() throws IOException, ParseException {
		 assertFalse(SessionLogProcessor.isValidAction(null));
		 assertTrue(SessionLogProcessor.isValidAction("START"));
		 assertTrue(SessionLogProcessor.isValidAction("END"));
		 assertFalse(SessionLogProcessor.isValidAction("str"));
		 
	    }


}
