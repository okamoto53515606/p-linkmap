import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;


public abstract class ConnectionThread extends Thread {
        public PLMApplet applet = null;
        private String sectionName = null;
        
        public ConnectionThread(PLMApplet applet) {
            this.applet = applet;
        }

        public void run() {
        }
        
        public abstract void nextLine(String line);
        

        public final void doConnect(String urlString, Hashtable request) throws MalformedURLException, IOException, Exception {
            if (urlString == null) return;
            StringBuffer requestParameter = null;
            if (request != null) {
                requestParameter = new StringBuffer();
                Enumeration e = request.keys();
                while(e.hasMoreElements()) {
                    String key = (String)e.nextElement();   
                    String value = (String)request.get(key);
                    //key = URLEncoder2.encode(key);
                    //value = URLEncoder2.encode(value);
                    requestParameter.append(key+"="+value+"&");
                }
            }
            if (requestParameter != null) urlString += "?"+(new String(requestParameter));
            URL url = new URL(applet.getCodeBase(), urlString);
            //URL url = new URL("http://www.p-linkmap.com/jp/member/"+urlString);
            applet.debug("Connecting to "+url.toString());
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            
            
            
            //InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF8");
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "SJIS");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            String line;
            while ( (line = bufferedReader.readLine()) != null ) {
                applet.debug(line);
                if (!line.equals("")) {
                    //line = applet.getValidString(line);
                    String sectionName = getSectionName(line);
                    if (sectionName != null) {
                        this.sectionName = sectionName;
                    } else {
                        nextLine(line);
                    }
                }
            }
            bufferedReader.close();
        }

        public final String getSectionName(String line) {
            line = line.trim();
            if (line == null || line.equals("")) return null;
            int length = line.length();
            if (length < 3) return null;
            
            if (!line.substring(0,1).equals("<") || !line.substring(length-1).equals(">")) {
                return null;                
            }

            String startSection = line.substring(1,length-1);
            return startSection;
        }

        public final Vector split(String line, String ifs) {
            Vector list = new Vector();
            String token = null;
            StringTokenizer stringTokenizer = new StringTokenizer(line,ifs);
            while (stringTokenizer.hasMoreTokens()) {
                list.addElement(stringTokenizer.nextToken());
            }
            return list;
        }

        public final Hashtable split2(String line, String ifs) {
            Hashtable pair = new Hashtable();
            String key = null;
            String value = null;
            StringTokenizer stringTokenizer = new StringTokenizer(line,ifs);
            if (stringTokenizer.hasMoreTokens()) key = stringTokenizer.nextToken();
            if (stringTokenizer.hasMoreTokens()) value = stringTokenizer.nextToken();
            if (key != null) {
                if (value == null) value = "";
                pair.put(key,value);
            }

            return pair;
        }
        
        public final String getSectionName() {
            if (sectionName == null) return "";
            return sectionName;
        }
}

