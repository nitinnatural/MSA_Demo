package nitin.androidyug.com.mindshiftassignment;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IAMONE on 8/22/2015.
 */
public class DataFetcher {

    // xml element name constant
    private static final String XML_STATUS = "Status";
    private static final String XML_SESSION_ID = "SessionId";
    private static final String XML_START_TIME = "StartTime";
    private static final String XML_END_TIME = "EndTime";
    private static final String XML_SESSION_STATE = "SessionState";
    private static final String XML_STUDENT_NAME = "StudentName";
    private static final String XML_STUDENT_ID = "StudentId";
    private static final String XML_CLASS_ID = "ClassId";
    private static final String XML_CLASS_NAME = "ClassName";
    private static final String XML_ROOM_ID = "RoomId";
    private static final String XML_ROOM_NAME = "RoomName";
    private static final String XML_SUBJECT_ID = "SubjectId";
    private static final String XML_SUBJECT_NAME = "SubjectName";

    byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0){
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    // method to convert the url get data into string
    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    ArrayList<SessionItem> parseItems(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        ArrayList<SessionItem> items = new ArrayList<SessionItem>();
        //parser.require(XmlPullParser.START_TAG, null, "Root");
        int eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            //if (parser.getEventType() != XmlPullParser.START_TAG) {
               // continue;
           // }
            String name = parser.getName();
            Log.d("DataFetcher", "parser.getname(): " + name + " ");
            // Starts by looking for the Session tag
            if (eventType == XmlPullParser.START_TAG && name.equals("Session")) {
                items.add(readSession(parser));
            } else {
               //skip(parser);

            }
            eventType = parser.next();
        }

        return items;
    }

    // uses main method that will fetch the data.
    public ArrayList<SessionItem> fetchItem(){
        ArrayList<SessionItem> items = new ArrayList<SessionItem>();
        try{
            String url = "http://54.251.104.13/Jupiter/sun.php?api=%3CSunstone%3E%3CAction%3E%3CService%3EGetThisStudentSessions%3C/Service%3E%3CUserId%3E532%3C/UserId%3E%3C/Action%3E%3C/Sunstone%3E";
            String xmlString = getUrl(url);
            Log.d("DataFetcher", "Fetxhed xml: " + xmlString);
            //XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //XmlPullParser parser = factory.newPullParser();
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(xmlString));
            parser.next();
            items = parseItems(parser);
        } catch (IOException ioe){
            Log.e("something", "fetch item IOexception: ", ioe);
        } catch (XmlPullParserException xppe){
            Log.e("something", "fetch item xmlPullParser exc: ", xppe);
        }
       return items;
    }

    public SessionItem readSession(XmlPullParser parser) throws
            XmlPullParserException, IOException{

        SessionItem sessionItem = new SessionItem();
        //parser.require(XmlPullParser.START_TAG, null, "Session");


        String sessionId = null;
        String startTime = null;
        String endTime = null;
        String sessionState = null;
        String studentName = null;
        String studentId = null;
        String classId = null;
        String className = null;
        String roomId = null;
        String roomName = null;
        String subjectId = null;
        String subjectName = null;

       // Log.d("DataFetcher", "Parser.getName: " + parser.getName() );
        int eventType = parser.next();
        String name1 = parser.getName();
        while ((eventType != XmlPullParser.END_TAG )&& (name1 != "Session")) {
            //Log.d("DataFetcher", "Enter into readSession while loop" );
           // if (parser.getEventType() != XmlPullParser.START_TAG) {
                //continue;
            //}

            String name = parser.getName();

            if (name.equals(XML_SESSION_ID)) {
                sessionId = readSessionId(parser);
            } else if (name.equals(XML_START_TIME)) {
                startTime = readStartTime(parser);
            } else if (name.equals(XML_END_TIME)) {
                endTime = readEndTime(parser);
            } else if (name.equals(XML_SESSION_STATE)){
                sessionState = readSessionState(parser);
            } else if (name.equals(XML_STUDENT_NAME)){
                studentName = readStudentName(parser);
            } else if (name.equals(XML_STUDENT_ID)){
                studentId = readStudentId(parser);
            } else if (name.equals(XML_CLASS_ID)){
                classId = readClassId(parser);
            } else if (name.equals(XML_CLASS_NAME)){
                className = readClassName(parser);
            } else if (name.equals(XML_ROOM_ID)){
                roomId = readRoomId(parser);
            } else if (name.equals(XML_ROOM_NAME)){
                roomName = readRoomName(parser);
            } else if (name.equals(XML_SUBJECT_ID)){
                subjectId = readSubjectId(parser);
            } else if (name.equals(XML_SUBJECT_NAME)){
                subjectName = readSubjectName(parser);
            } else {
                //skip(parser);
            }

            eventType = parser.next();
        }

        sessionItem.setSessionId(sessionId);
        sessionItem.setStartTime(startTime);
        sessionItem.setEndTime(endTime);
        sessionItem.setSessionState(sessionState);
        sessionItem.setStudentName(studentName);
        sessionItem.setStudentId(studentId);
        sessionItem.setClassId(classId);
        sessionItem.setClassName(className);
        sessionItem.setRoomId(roomId);
        sessionItem.setRoomName(roomName);
        sessionItem.setSubjectId(subjectId);
        sessionItem.setSubjectName(subjectName);

        return sessionItem;
    }

    // Processes title tags in the feed.
    private String readSessionId(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process startTime
    private String readStartTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process endTime
    private String readEndTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process sessionState
    private String readSessionState(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process student name
    private String readStudentName(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process student id
    private String readStudentId(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process class id
    private String readClassId(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process class name
    private String readClassName(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process room name
    private String readRoomName(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process room ID
    private String readRoomId(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process subject id
    private String readSubjectId(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }

    // process subject name
    private String readSubjectName(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = readText(parser);
        return result;
    }



    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }




}
