import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class Saver extends ConnectionThread {


        private String statusCode = "";
        private String statusMessage = "";

        public Saver(PLMApplet applet) {
            super(applet);
        }

        public void run() {            
            if (applet.isSearchMode()) return;            
            Icon selfIcon = applet.getFirstIcon();
            try {
                applet.setStatusMessage("à íuèÓïÒÇï€ë∂ÇµÇƒÇ¢Ç‹Ç∑..");
                applet.changeStatus(0);
                //sleep(300);
                doSave();
                applet.changeStatus(1);
            } catch (InterruptedException e) {
                applet.error("java.lang.InterruptedException: "+e.getMessage());
            } catch (java.net.MalformedURLException e) {
                applet.error("java.net.MalformedURLException: "+e.getMessage());
            } catch (java.io.IOException e) {
                applet.error("java.io.IOException: "+e.getMessage());
            } catch (Exception e) {
                applet.error("java.lang.Exception: "+e.getMessage());
            }
        }

        private void doSave() throws MalformedURLException, IOException, Exception {
            Hashtable  request = new Hashtable();
            request.put("sessionId", applet.getSessionId());
            Enumeration e = applet.getIconList().elements();
            while (e.hasMoreElements()) {
                Icon icon = (Icon)e.nextElement();
                String uid = icon.getStringUid();
                String x = icon.getStringX();
                String y = icon.getStringY();
                request.put(uid, x+","+y);
             }
             doConnect("saveIconPosition.php", request);

             if (statusCode.equals("FALSE")) { 
                 throw new Exception(statusMessage);
             } else if (!statusCode.equals("TRUE")) {
                 throw new Exception("It was not connectable with the WWW server.");
             }

        }
      
        public void nextLine(String line) {
            if (getSectionName().equals("STATUS")) {
                Hashtable pair = split2(line, "\t");
                if (pair.containsKey("code")) this.statusCode = (String)pair.get("code");
                if (pair.containsKey("message")) this.statusMessage = (String)pair.get("message");
            }
        }

}

