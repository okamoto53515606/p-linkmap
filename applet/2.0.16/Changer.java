import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class Changer extends ConnectionThread {

    private Icon targetIcon;
    private boolean backFlag;
    private String statusCode = "";
    private String statusMessage = "";
    private Point newTargetIconPoint = null;
    private Vector newIconList = null;
    private Vector newHistoryIconList = null;
    private Member newMember = null;
    private SearchParameter searchParameter = null;
    
    
    public Changer(Icon targetIcon, boolean backFlag, PLMApplet applet) {
        super(applet);
        this.targetIcon = targetIcon;
        this.backFlag = backFlag;
        this.searchParameter = applet.getSearchParameter();
    }

        public void run() {
            Icon firstIcon = applet.getFirstIcon();
            //SearchParameter searchParameter = applet.getSearchParameter();
            try {
                if (searchParameter != null && searchParameter.getStatusCode() == 0) applet.setStatusMessage("åüçıÇµÇƒÇ¢Ç‹Ç∑..");
                else applet.setStatusMessage(targetIcon.getName2() + "ÇÃmapÇì«Ç›çûÇÒÇ≈Ç¢Ç‹Ç∑..");
                applet.changeStatus(0);
                //sleep(300);
                doUpdate();
                applet.changeStatus(1);
            } catch (InterruptedException e) {
                 e.printStackTrace();
                 applet.error("java.lang.InterruptedException: "+e.getMessage());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                applet.error("java.net.MalformedURLException: "+e.getMessage());
            } catch (java.io.IOException e) {
                e.printStackTrace();
                applet.error("java.io.IOException: "+e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                applet.error("java.lang.Exception: "+e.getMessage());
            }
        }


        private void doUpdate() throws MalformedURLException, IOException, Exception {
            newTargetIconPoint = getNewTargetIconPoint();
            newIconList = new Vector();
            newMember = new Member();

            Hashtable  request = new Hashtable();
            request.put("sessionId", applet.getSessionId());
            request.put("uid", targetIcon.getStringUid());
            
            //for Debug
            //request.put("fontList", applet.getFontList());
            
            //searchParameter = applet.getSearchParameter();
            if (searchParameter != null) {
                if (searchParameter.getStatusCode() == 0) {
                    request.put("DO_SEARCH", "TRUE");
                    searchParameter.setStatusCode(1); 
                } else {
                    searchParameter.setStatusCode(-1);
                }
            }
            doConnect("getMapData.php", request);

            if (statusCode.equals("FALSE")) {
                throw new Exception(statusMessage);
            } else if (statusCode.equals("TRUE")) {                
                if (targetIcon != applet.getFirstIcon() || backFlag) {
                    newHistoryIconList = getNewHistoryIconList();
                } else {
                    newHistoryIconList = applet.getHistoryIconList();
                    targetIcon.setDepth(0);
                }
                
                /*
                 * update
                 */
                applet.setIconList(newIconList);
                targetIcon.setPoint(newTargetIconPoint);
                applet.setHistoryIconList(newHistoryIconList);
                applet.setMember(newMember);
            } else {
                throw new Exception("It was not connectable with the WWW server.");
            }
        }
        
        public void nextLine(String line) {           
            if (getSectionName().equals("STATUS")) {
                
                Hashtable pair = split2(line, "\t");
                if (pair.containsKey("code")) this.statusCode = (String)pair.get("code");
                if (pair.containsKey("message")) this.statusMessage = (String)pair.get("message");
                
            } else if (getSectionName().equals("USERINFO")) {

                Hashtable pair = split2(line, "\t");
                if (pair.containsKey("name")) newMember.setName((String)pair.get("name"));
                if (pair.containsKey("sex")) newMember.setSex((String)pair.get("sex"));
                if (pair.containsKey("blood")) newMember.setBlood((String)pair.get("blood"));
                if (pair.containsKey("birthday")) newMember.setBirthday((String)pair.get("birthday"));
                if (pair.containsKey("prefectures")) newMember.setPrefectures((String)pair.get("prefectures"));
                if (pair.containsKey("job")) newMember.setJob((String)pair.get("job"));
                if (pair.containsKey("interest")) newMember.setInterest((String)pair.get("interest"));
                if (pair.containsKey("keyword")) newMember.setKeyword((String)pair.get("keyword"));
                if (pair.containsKey("email")) newMember.setEmail((String)pair.get("email"));
                if (pair.containsKey("web")) newMember.setUrl((String)pair.get("web"));
                
            } else if (getSectionName().equals("ICONLIST")) {

                newIconList.addElement(createIcon(line));
                
            } else if (getSectionName().equals("SEARCHPARAM")) {
                Hashtable pair = split2(line, "\t");
                if (pair.containsKey("JOKEN")) searchParameter.setJoken((String)pair.get("JOKEN"));
            }
        }

        private Vector getNewHistoryIconList() {
            Icon current = applet.getCurrentIcon();
            Vector newHistoryIconList = new Vector();
            Vector historyIconList = applet.getHistoryIconList();
            for(int i = 0; i < historyIconList.size(); i++) {
              Icon history = (Icon)historyIconList.elementAt(i);
              newHistoryIconList.addElement(history);
              if (history == targetIcon) break;
            }
            if (!backFlag) newHistoryIconList.addElement(targetIcon);
            
            /*
             * Set new depth
             */
            int newDepth = 0;
            for(int i = newHistoryIconList.size()-1; i > -1; i--) {
              Icon history = (Icon)newHistoryIconList.elementAt(i);
              history.setDepth(newDepth);
              newDepth--;
            }
            
            
            return newHistoryIconList;
        }

        private Point getNewTargetIconPoint() {
            Icon current = applet.getCurrentIcon();
            if (current == targetIcon) return targetIcon.getPoint();
            int length, lengthMin, dx ,dy;
            if (applet.getAppletSize().width <= applet.getAppletSize().height) lengthMin = (int)applet.getAppletSize().width/10;
            else lengthMin = (int)applet.getAppletSize().height/10;
            dx =  targetIcon.getX() - current.getX();
            dy =  targetIcon.getY() - current.getY();
            length = (int)Math.sqrt(Math.pow((double)dx, 2)+Math.pow((double)dy, 2));
            return new Point(current.getX() + dx * lengthMin/length, current.getY() + dy * lengthMin/length);
        }

        private Icon createIcon (String line) {
                int id = -1;
                String name = "";
                String relation = "";
                int x = 0;
                int y = 0;
                Point p = null;
                int type = 0;
                boolean havePoint = true;
                StringTokenizer st = new StringTokenizer(line,"\t");
                if (st.hasMoreTokens()) id = Integer.parseInt(st.nextToken());
                if (st.hasMoreTokens()) name = st.nextToken();
                if (st.hasMoreTokens()) relation = st.nextToken();
                
                if (st.hasMoreTokens()) { 
                    type = Integer.parseInt(st.nextToken());
                }
                
                if (st.hasMoreTokens()) x = Integer.parseInt(st.nextToken());
                else  havePoint = false;
                if (st.hasMoreTokens()) y = Integer.parseInt(st.nextToken());
                else  havePoint = false;
                

                
                if (havePoint && id > 0) { p = new Point(x, y); }
                else { p = getRandomPoint(newTargetIconPoint); }
                
                return new Icon(id, name, relation, applet.getValidPoint(p), type);
        }

        private Point getRandomPoint(Point centerPoint) {
                int rMin, rMax, x, y;
                if (applet.getAppletWidth() <= applet.getAppletHeight()) rMax = applet.getAppletWidth()/2;
                else rMax = applet.getAppletHeight()/2;
                rMax = rMax*8/10;
                rMin = (int)rMax/10;
                double r = rMin + (Math.random() * (rMax-rMin) );
                double radians = (double)Math.random()*2*Math.PI;
                double dx = (double)r * Math.cos(radians);
                double dy = (double)r * Math.sin(radians);
                x = centerPoint.x + (int)dx;
                y = centerPoint.y + (int)dy;
                Point p = applet.getValidPoint(new Point(x, y));
                return p;
        }

}
