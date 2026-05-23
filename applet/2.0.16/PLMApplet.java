import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;



public class PLMApplet extends Applet {
    
    private Image memberBgImage = null;
    private Rectangle memberBgRect = null;
    private Icon mouseOveredIcon = null;
    private Button2 mouseOveredButton = null;
    private Painter painter = null;
    private AppletContext appletContext = null;
    private AudioClip clickSound = null;
    private ErrorFrame errorFrame = null;
    private String sessionId = null;
    private String statusMessage = null;
    private int statusCode = 0;
    private Member member = null;
    private Vector iconList = null;
    private Vector historyIconList = null;
    private SearchParameter searchParameter= null;
    private Button2[] menuButtons = new Button2[2];
    private Button2[] memberButtons = new Button2[3];
    private boolean pressedFlag = false;
    
    private static long lastMouseDown = 0;
    private static final long maxdelay = 200;
    private String helpMessage = null;    
    private boolean sampleFlag = false; 
    
    public String getProgramName() {
        return "PLMApplet version 2.0.16";
    }


    public void init() {
        debug(getProgramName());
        
        setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        setBackground(Config.MAP_BGCOLOR);
        
        historyIconList = new Vector();
        Icon firstIcon = null;
        if (getParameter("DO_SEARCH").equals("TRUE")) {
            searchParameter = new SearchParameter();
            firstIcon = new Icon(-1,"","",getCenterPoint());
        } else {
            //firstIcon = new Icon(Integer.parseInt(getParameter("UID")),getParameter("NAME"),"",getCenterPoint());
            firstIcon = new Icon(Integer.parseInt(getParameter("UID")),"あなた","",getCenterPoint());
        }
        historyIconList.addElement(firstIcon);
        
        sessionId = getParameter("SESSIONID");

        String fontName = getJpFont();
        if (fontName == null) { fontName = "Courier"; }

        debug("fontName: "+fontName);

        if (getParameter("CLICK_SOUND") != null) this.clickSound = getAudioClip(getCodeBase(),getParameter("CLICK_SOUND"));
        if (getParameter("NORMAL_FONT_SIZE") != null) Config.NORMAL_FONT = new Font(fontName,Font.PLAIN,Integer.parseInt(getParameter("NORMAL_FONT_SIZE")));
        if (getParameter("LARGE_FONT_SIZE") != null) Config.LARGE_FONT = new Font(fontName,Font.PLAIN,Integer.parseInt(getParameter("LARGE_FONT_SIZE")));
        if (getParameter("SMALL_FONT_SIZE") != null) Config.SMALL_FONT = new Font(fontName,Font.PLAIN,Integer.parseInt(getParameter("SMALL_FONT_SIZE")));
        if (getParameter("NORMAL_ICON_SIZE") != null) Config.NORMAL_ICON_SIZE = Integer.parseInt(getParameter("NORMAL_ICON_SIZE"));
        if (getParameter("LARGE_ICON_SIZE") != null) Config.LARGE_ICON_SIZE = Integer.parseInt(getParameter("LARGE_ICON_SIZE"));
        if (getParameter("MAP_BGCOLOR") != null) Config.MAP_BGCOLOR = createColor(getParameter("MAP_BGCOLOR"));
        if (getParameter("MAP_FONT_COLOR") != null) Config.MAP_FONT_COLOR = createColor(getParameter("MAP_FONT_COLOR"));
        if (getParameter("MAP_TITLE1_BGCOLOR") != null) Config.MAP_TITLE1_BGCOLOR = createColor(getParameter("MAP_TITLE1_BGCOLOR"));
        if (getParameter("MAP_TITLE1_FONT_COLOR") != null) Config.MAP_TITLE1_FONT_COLOR = createColor(getParameter("MAP_TITLE1_FONT_COLOR"));
        if (getParameter("MAP_TITLE2_BGCOLOR") != null) Config.MAP_TITLE2_BGCOLOR = createColor(getParameter("MAP_TITLE2_BGCOLOR"));
        if (getParameter("MAP_TITLE2_FONT_COLOR") != null) Config.MAP_TITLE2_FONT_COLOR = createColor(getParameter("MAP_TITLE2_FONT_COLOR"));        
        if (getParameter("HELP_BGCOLOR") != null) Config.HELP_BGCOLOR = createColor(getParameter("HELP_BGCOLOR"));
        if (getParameter("HELP_FONT_COLOR") != null) Config.HELP_FONT_COLOR = createColor(getParameter("HELP_FONT_COLOR"));
        if (getParameter("WAITING_FONT_COLOR") != null) Config.WAITING_FONT_COLOR = createColor(getParameter("WAITING_FONT_COLOR"));
        if (getParameter("USERINFO_FONT_COLOR") != null) Config.USERINFO_FONT_COLOR = createColor(getParameter("USERINFO_FONT_COLOR"));
        if (getParameter("NORMAL_ICON_COLOR") != null) Config.NORMAL_ICON_COLOR = createColor(getParameter("NORMAL_ICON_COLOR"));
        if (getParameter("ACTIVE_ICON_COLOR") != null) Config.ACTIVE_ICON_COLOR = createColor(getParameter("ACTIVE_ICON_COLOR"));
        if (getParameter("ACTIVE_LINE_COLOR") != null) Config.ACTIVE_LINE_COLOR = createColor(getParameter("ACTIVE_LINE_COLOR"));
        if (getParameter("ACTIVE_RELATION_COLOR") != null) Config.ACTIVE_RELATION_COLOR = createColor(getParameter("ACTIVE_RELATION_COLOR"));
        if (getParameter("HISTORY_ICON_COLOR") != null) Config.HISTORY_ICON_COLOR = createColor(getParameter("HISTORY_ICON_COLOR"));
        if (getParameter("HISTORY_LINE_COLOR") != null) Config.HISTORY_LINE_COLOR = createColor(getParameter("HISTORY_LINE_COLOR"));
        if (getParameter("CURRENT_ICON_COLOR") != null) Config.CURRENT_ICON_COLOR = createColor(getParameter("CURRENT_ICON_COLOR"));
        if (getParameter("ACTIVE_CURRENT_ICON_COLOR") != null) Config.ACTIVE_CURRENT_ICON_COLOR = createColor(getParameter("ACTIVE_CURRENT_ICON_COLOR"));
        
        if (getParameter("SAMPLE_MAP") != null && getParameter("SAMPLE_MAP").equals("TRUE")) this.sampleFlag = true;

        
        loadImage();
        
        this.appletContext = getAppletContext();
        this.statusMessage = null;
        this.painter = new Painter(this);
        
        changeMap(getFirstIcon(), false);
    }
    
    
    private void loadImage() {
        MediaTracker mt = new MediaTracker(this);

        Image saveButtonImage = getImage(getCodeBase(), getParameter("SAVE_BUTTON_IMAGE"));
        Image backButtonImage = getImage(getCodeBase(), getParameter("BACK_BUTTON_IMAGE"));
        Image urlButtonImage = getImage(getCodeBase(), getParameter("URL_BUTTON_IMAGE"));
        Image mailButtonImage = getImage(getCodeBase(), getParameter("MAIL_BUTTON_IMAGE"));
        Image closeButtonImage = getImage(getCodeBase(), getParameter("CLOSE_BUTTON_IMAGE"));        
        Image saveButtonImageF = getImage(getCodeBase(), getParameter("SAVE_BUTTON_IMAGE_F"));
        Image backButtonImageF = getImage(getCodeBase(), getParameter("BACK_BUTTON_IMAGE_F"));
        Image urlButtonImageF = getImage(getCodeBase(), getParameter("URL_BUTTON_IMAGE_F"));
        Image mailButtonImageF = getImage(getCodeBase(), getParameter("MAIL_BUTTON_IMAGE_F"));
        Image closeButtonImageF = getImage(getCodeBase(), getParameter("CLOSE_BUTTON_IMAGE_F"));        
        memberBgImage = getImage(getCodeBase(), getParameter("MEMBER_BG_IMAGE"));
        
        mt.addImage(saveButtonImage, 0);
        mt.addImage(backButtonImage, 1);
        mt.addImage(urlButtonImage, 2);
        mt.addImage(mailButtonImage, 3);
        mt.addImage(closeButtonImage, 4);        
        mt.addImage(saveButtonImageF, 5);
        mt.addImage(backButtonImageF, 6);
        mt.addImage(urlButtonImageF, 7);
        mt.addImage(mailButtonImageF, 8);
        mt.addImage(closeButtonImageF, 9);        
        mt.addImage(memberBgImage, 10);
        
        try {
            mt.waitForAll(1000*60*2);
        } catch (InterruptedException e) {}
        
        this.memberBgRect = new Rectangle(11,25,425,432);
        Button2 saveButton = new Button2("save", new Point(getMapRect().x+getMapRect().width-saveButtonImage.getWidth(this)-10,2), saveButtonImage, saveButtonImageF, this);        
        Button2 backButton = new Button2("back", new Point(getMapRect().x, getMapRect().y+getMapRect().height-backButtonImage.getHeight(this)), backButtonImage, backButtonImageF ,this);
        Button2 urlButton = new Button2("url", new Point(memberBgRect.x+8,memberBgRect.y+8), urlButtonImage, urlButtonImageF ,this);              
        Button2 mailButton = new Button2("mail", new Point(memberBgRect.x+8+10+urlButton.getWidth(),memberBgRect.y+8), mailButtonImage, mailButtonImageF ,this);
        Button2 closeButton = new Button2("close", new Point(memberBgRect.x+memberBgRect.width-closeButtonImage.getWidth(this)-11,memberBgRect.y+8), closeButtonImage,  closeButtonImageF ,this);

        menuButtons[0] = saveButton;
        menuButtons[1] = backButton;
        memberButtons[0] = urlButton;
        memberButtons[1] = mailButton;
        memberButtons[2] = closeButton;
    }

    public Color createColor(String htmlColor) {
          try {
            return new Color(Integer.parseInt(htmlColor.substring(1,7) ,16));
          } catch (Exception e) {
             e.printStackTrace();
             return  new Color(0,0,0);  
          }
    }

    public void start() {
    }
    public void stop() {
    }
    public void destroy() {
        if (errorFrame != null) errorFrame.dispose();
        if (painter != null) painter.destroy();
    }

    
    public void playClickSound() {
        if (clickSound != null) clickSound.play();   
    }


    public void error(String message) {
        changeStatus(-1);
        setStatusMessage(message);
        
        message = "--------------------------------------------------\n" + 
                  "エラー内容\n" + 
                  "--------------------------------------------------\n" + 
                  "[ERROR] " + message+"\n" + 
                  "[VERSION] " + getProgramName() + "\n" + 
                  "[HTTP_USER_AGENT] "+getParameter("HTTP_USER_AGENT") + "\n" + 
                  "--------------------------------------------------\n" + 
                  "恐れ入りますが、上記エラー内容をコピーし、Eメールで\n" + 
                  "webmaster@p-linkmap.com までお知らせ下さい.\n" + 
                  "";
        errorFrame = new ErrorFrame(message, this);
    }

    public void changeStatus(int statusCode) {
            this.helpMessage = null;
            this.statusCode = statusCode;
            playClickSound();
            try {
                switch (statusCode) {
                    case -1:
                        break;
                    case 0:
                        repaint();
                        break;
                    case 1:
                        this.statusMessage = getMapStatusMessage();
                        repaint();
                        break;
                    case 2:
                        this.statusMessage = getCurrentIcon().getName2() + "の情報を表示中.";
                        repaint();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            showStatusMessage();
    }
    public void showStatusMessage() {
        String message = null;
        if (this.helpMessage != null && !this.pressedFlag) message = this.helpMessage;
        else message = this.statusMessage;

        if (message != null) appletContext.showStatus(message);
    }

    public boolean canSave() {
        if (isSample()) return false;
        else if (isSearchMode()) return false;
        else if (getCurrentIcon() != getFirstIcon()) return false;
        else return true;
    }
    
    private synchronized void saveMap() {
        if (!canSave()) return;
        try {
            new Saver(this).start();
        } catch (Exception e) {
            e.printStackTrace();
            error(e.getMessage());
        }
    }
    
    private synchronized void changeMap(Icon targetIcon, boolean backFlag) {
        if (targetIcon.getType() == 1) return; //仮登録アイコンの時は処理しない
        try {
            playClickSound();
            new Changer(targetIcon, backFlag, this).start();
        } catch (Exception e) {
            e.printStackTrace();
            error(e.getMessage());
        }
    }
    private void nextMap(Icon targetIcon) {
        changeMap(targetIcon, false);
    }
    
    private void backMap() {
        if (!canBack()) return;
        if (isSearching() && searchParameter.getStatusCode() == 1) {
            try {
                playClickSound();
                appletContext.showDocument(new URL(getCodeBase(), "search.php"),"_self");
            } catch (java.net.MalformedURLException e) {}
        } else {
            changeMap((Icon)historyIconList.elementAt(historyIconList.size()-2), true);
        }
    }

    public boolean canBack() {
        if (statusCode != 1) {
          return false;
        } else if (isSearching() && searchParameter.getStatusCode() == 1) {
          return true;   
        } else {
          int n = historyIconList.size();
          n -= 2;
          if (n < 0) return false;
          Icon targetIcon = (Icon)historyIconList.elementAt(n);
          if (targetIcon.getUid() == -1) return false;
        }
        return true;
    }

    public void debug(String message) {
        //System.out.println("#" + message);
    }
    
    private void buttonAction(Button2 button2) {
        if (button2 == null) return;
        String actionCommand = button2.getActionCommand();
        if (actionCommand == null) return;

        if (actionCommand.equals("save")) {
            saveMap();
        } else if (actionCommand.equals("back")) {
            backMap();
        } else if (actionCommand.equals("url")) {
            if (member == null) return;
            if (member.getUrl().equals("")) return;
            try {
                playClickSound();
                appletContext.showDocument(new URL(member.getUrl()),"_blank");
            } catch (java.net.MalformedURLException e) {}
        } else if (actionCommand.equals("mail")) {
            if (member == null) return;
            if (member.getEmail().equals("")) return;
            try {
                playClickSound();
                appletContext.showDocument(new URL("mailto:"+member.getEmail()));
            } catch (java.net.MalformedURLException e) {}

        } else if (actionCommand.equals("close")) {
            changeStatus(1);
        }
    }
    private void mouseClicked(Point p) {
        if (statusCode < 1) return;

        Button2 clickedButton = null;
        Icon clickedIcon = null;
        
        if(statusCode == 2) {
            clickedButton = getInsideMemberButtons(p);
            if (clickedButton != null) {
                buttonAction(clickedButton);
            }
            return;
        }

        clickedIcon = getInsideHistoryIcon(p);
        if (clickedIcon != null) {
            if (clickedIcon == getCurrentIcon()) {
                if (clickedIcon.getUid() == -1) return;
                if (member == null) return;
                clickedIcon.setActive(false); //add
                changeStatus(2);
                return;
            }
            return;
        }
        
        clickedIcon = getInsideIcon(p);
        if (clickedIcon != null) {
            if (clickedIcon.getUid() == -1) return;
            clickedIcon.setActive(false); //add
            nextMap(clickedIcon);
            return;
        }
        
        clickedButton = getInsideMenuButtons(p);
        if (clickedButton != null) {
            clickedButton.setActive(false); //add
            buttonAction(clickedButton);
            return;
        }
        return;
    }
    public boolean mouseDown(java.awt.Event e, int x, int y){
        pressedFlag = true;
        this.lastMouseDown = e.when;
        if (this.helpMessage != null) {
            this.helpMessage = null;
            repaint();
            showStatusMessage();
        }
        return true;
    }
    public boolean mouseDrag(java.awt.Event e, int x, int y){
        if (statusCode != 1) return true;
        if (mouseOveredIcon != null) {
            mouseOveredIcon.setPoint(getValidPoint(new Point(x,y)));
            repaint();
        }
        return true;
    }
    public boolean mouseUp(java.awt.Event e, int x, int y){
        long delay = e.when-this.lastMouseDown;
        pressedFlag = false;
        if (delay < this.maxdelay) {
            mouseClicked(new Point(x,y));
        }        
        return true;
     }
    public boolean mouseMove(java.awt.Event e, int x, int y){
        if (pressedFlag) return true;
        if (statusCode < 1) return true;
        
        Point p = new Point(x,y);
        Icon mouseOveredIcon = null;
        Icon mouseOveredCurrentIcon = null;
        Button2 mouseOveredButton = null;
        
        if (statusCode == 2) {
            mouseOveredButton = getInsideMemberButtons(p);
            if (mouseOveredButton != null) {
                 if (this.mouseOveredButton != null && mouseOveredButton != this.mouseOveredButton) {
                     this.mouseOveredButton.setActive(false);
                 }
                 mouseOveredButton.setActive(true);
                 this.mouseOveredButton = mouseOveredButton;
                 repaint();
            } else {
                if (this.mouseOveredButton != null) {
                    this.mouseOveredButton.setActive(false);
                    this.mouseOveredButton = null;
                    repaint();
                }
            }
            return true;
        }

        mouseOveredCurrentIcon = getInsideCurrentIcon(p);
        if (mouseOveredCurrentIcon != null) { //if1

                mouseOveredCurrentIcon.setActive(true);
                if (this.mouseOveredIcon != null && this.mouseOveredIcon != mouseOveredCurrentIcon) {
                    this.mouseOveredIcon.setActive(false);
                }
                this.mouseOveredIcon = mouseOveredCurrentIcon;
                repaint();
                this.helpMessage = "クリックすると、"+mouseOveredCurrentIcon.getName2()+"の情報を表示します.";
                showStatusMessage();
                return true;
                
        } else { //if1
            
            mouseOveredIcon = getInsideIcon(p);
            if (mouseOveredIcon != null) { //if2
            
                mouseOveredIcon.setActive(true);
                if (this.mouseOveredIcon != null && this.mouseOveredIcon != mouseOveredIcon) {
                    this.mouseOveredIcon.setActive(false);
                }
                this.mouseOveredIcon = mouseOveredIcon;
                repaint();
                if (mouseOveredIcon.getType() == 1) this.helpMessage = "仮登録状態です.";
                else this.helpMessage = "クリックすると、"+mouseOveredIcon.getName2()+"のmapに移動します.";
                showStatusMessage();
                return true;
                
            } else { //if2
                
                if (this.mouseOveredIcon != null) {
                    this.mouseOveredIcon.setActive(false);
                    this.mouseOveredIcon = null;
                    repaint();
                }
    
                mouseOveredButton = getInsideMenuButtons(p);
                if (mouseOveredButton != null) {
                     mouseOveredButton.setActive(true);
                     this.mouseOveredButton = mouseOveredButton;
                     repaint();
                     
                     if (mouseOveredButton == menuButtons[0]) {
                         if (canSave()) {
                             this.helpMessage = "クリックすると、現在の位置情報を保存します.";
                             showStatusMessage();
                         }
                     } else if (mouseOveredButton == menuButtons[1]) {
                         if (canBack()) {
                             this.helpMessage = "クリックすると、前の画面に戻ります.";
                             showStatusMessage();
                         }
                     }
                     return true;
                } else {
                    if (this.mouseOveredButton != null) {
                        this.mouseOveredButton.setActive(false);
                        this.mouseOveredButton = null;
                        repaint();
                    }
                }
                
            } //if2
            
            
        } //if1

        
        this.helpMessage = null;
        showStatusMessage();
        
        return true;
    }
    public boolean mouseEnter(java.awt.Event e, int x, int y){
        return true;
    }
    public boolean mouseExit(java.awt.Event e, int x, int y){
        return true;
    }
    public boolean keyDown(java.awt.Event e, int key){
        return true;
    }
    public boolean keyUp(java.awt.Event e, int key){
        return true;
    }
    public void update(Graphics g) {
        paint(g);
    }

    public void paint (Graphics g) {
        if (painter == null) return;
        switch (statusCode) {
            case 0:
                painter.drawWaiting(g);
                break;
            case 1:
                painter.drawMap(g);
                break;
            case 2:
                painter.drawMember(g);
                break;
        }
    }
    
    private String getMapStatusMessage() {
        String message = "";        
        if (!isSearching()) {        
            message = getCurrentIcon().getName2() + "のmapを表示中." + " " + "(" + iconList.size() + "人)";
        } else {
            String JOUKEN = searchParameter.getJoken();
            int searchNum = iconList.size();
            if (searchNum == 0) {
                message ="指定した条件にマッチするデータが見つかりませんでした." + " (" + JOUKEN + ")";
            } else {
                message = "検索結果をmapで表示中." + " (" + searchNum + "件)" + " (" + JOUKEN + ")";
            }
        }
        return message;
    }
    public String getMapTitle1() {
        String message = "";
        if (!isSearching()) {
            message = getCurrentIcon().getName2() + "のmap";
        } else {
            message = "検索map";
        }
        //message = getUnicodeString(message);
        return message;
    }
    public String getMapTitle2() {
        String message = iconList.size()+"人";
        //message = getUnicodeString(message);
        return message;
    }
    public String getMapTitle3() {
        String message = "";
        if (isSearching()) {
            message = "検索条件: " + searchParameter.getJoken();
            if (iconList.size() == 0) { 
                message += "\n"+"指定した条件にマッチするデータが見つかりませんでした." + 
                           "\n"+"もどるボタンで検索フォームにお戻り下さい.";
            }
        }
        return message;
    }
    
    
    
    public Point getCenterPoint() {
        return new Point(getAppletSize().width/2,getAppletSize().height/2);
    }
    private Button2 getInsideMemberButtons(Point p) {
        for(int i=0; i<memberButtons.length; i++) {
            if (memberButtons[i].getRect().inside(p.x, p.y)) return memberButtons[i];
        }
        return null;
    }
    private Button2 getInsideMenuButtons(Point p) {
        for(int i=0; i<menuButtons.length; i++) {
            if (menuButtons[i].getRect().inside(p.x, p.y)) return menuButtons[i];
        }
        return null;
    }
    private Icon getInsideIcon(Point p) {
        if (iconList == null) return null;
        for(int i = 0; i < iconList.size(); i++) {
            Icon icon = (Icon)iconList.elementAt(i);
            Rectangle iconRect = icon.getRect();
            if (iconRect.inside(p.x, p.y)) { return icon; }
        }
        return null;
    }
    private Icon getInsideHistoryIcon(Point p) {
        if (historyIconList == null) return null;
        for(int i = 0; i < historyIconList.size(); i++) {
            Icon icon = (Icon)historyIconList.elementAt(i);
            Rectangle iconRect = icon.getRect();
            if (iconRect.inside(p.x, p.y)) { return icon; }
        }
        return null;
    }
    private Icon getInsideCurrentIcon(Point p) {
        Icon icon = getCurrentIcon();
        Rectangle iconRect = icon.getRect();
        if (iconRect.inside(p.x, p.y)) { return icon; }
        return null;
    }

    public boolean isSearchMode() {
        if (searchParameter != null) return true;
        else return false;
    }
    public boolean isSearching() {
        if (searchParameter != null && searchParameter.getStatusCode() != -1) return true;
        else return false;
    }
    public String getHelpMessage() {
        //if (this.pressedFlag) return "";        
        if (this.helpMessage == null) return "";
        else return this.helpMessage;
    }
    public Dimension getAppletSize() {
        return size();
    }
    public int getAppletWidth() {
        return size().width;
    }
    public int getAppletHeight() {
        return size().height;
    }
    public Rectangle getMapRect() {
        return new Rectangle(0,0,getAppletWidth(),getAppletHeight());
    }
    public Icon getFirstIcon() {
      if (historyIconList == null) return null;
      return (Icon)historyIconList.firstElement();
    }
    public Icon getCurrentIcon() {
      if (historyIconList.size() == 1) return (Icon)historyIconList.firstElement();
      return (Icon)historyIconList.lastElement();
    }
    public Point getValidPoint (Point p) {
      int margin = Config.LARGE_ICON_SIZE;
      int x = p.x;
      int y = p.y;
      Rectangle mapRect = getMapRect();
      if (x < mapRect.x+margin) x = margin;
      if (x > (mapRect.x+mapRect.width-margin)) x = (mapRect.x+mapRect.width) - margin;
      if (y < mapRect.y+margin) y = margin;
      if (y > (mapRect.y+mapRect.height-margin)) y = (mapRect.y+mapRect.height) - margin;
      return new Point(x,y);
    }
    public SearchParameter getSearchParameter() {
        return searchParameter;
    }
    public String getSessionId() {
        return sessionId;
    }
    public String getStatusMessage() {
        return statusMessage;
    }
    public Member getMember() {
        return member;
    }
    public Vector getIconList() {
        return iconList;
    }
    public Vector getHistoryIconList() {
        return historyIconList;
    }
    public Button2[] getMenuButtons() {
        return menuButtons;
    }
    public Button2[] getMemberButtons() {
        return memberButtons;
    }
    public Image getMemberBgImage() {
        return memberBgImage;   
    }
    public Rectangle getMemberBgRect() {
        return memberBgRect;   
    }
    
    public boolean isSample() {
        return this.sampleFlag;   
    }
    
    //synchronized
    public void setMember(Member member) {
        this.member = member;
    }
    public void setIconList(Vector iconList) {
        this.iconList = iconList;
    }
    public void setHistoryIconList(Vector historyIconList) {
        this.historyIconList = historyIconList;
    }
    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter= searchParameter;
    }
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    //public String getValidString(String str) {
    //    if (str == null) return null;
    //    try {
    //        return new String(str.getBytes(encoding), "8859_1");
    //    } catch (UnsupportedEncodingException e) {
    //        return str;
    //    }
    //}
    //public String getUnicodeString(String str) {
    //    if (str == null) return null;
    //    try {
    //        return new String(str.getBytes("8859_1"), "JISAutoDetect");
    //   } catch (UnsupportedEncodingException e) {
    //       return str;
    //    }
    //}
    
    public String getFontList() {
      String fontList[] = getToolkit().getFontList();
      StringBuffer sb = new StringBuffer("");
      for (int i=0; i<fontList.length; i++) {
        sb.append(i+":"+fontList[i]+" ");
      }
      return sb.toString();
    }
    
    public String getJpFont() {
      String fontList[] = getToolkit().getFontList();      
      for (int i=0; i<fontList.length; i++) { //loop      
        StringTokenizer st = new StringTokenizer(fontList[i], " ");
        while (st.hasMoreTokens()) { //loop2
          if (st.nextToken().equals("HeiseiKakuGothic")) {
            return fontList[i];
          } 
        } //loop2
      } //loop
      return null;
    }
    
}
