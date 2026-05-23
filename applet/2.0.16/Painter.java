import java.util.*;
import java.awt.*;
    

public class Painter {

    private Image image;
    private Graphics imageG;
    private PLMApplet applet;

            public Painter(PLMApplet applet) {
                this.applet = applet;
                image = applet.createImage(applet.getAppletWidth(), applet.getAppletHeight());
                imageG = image.getGraphics();
            }
            public void destroy() {
                imageG.dispose();
            }



            public void drawWaiting(Graphics MAPG) {
              Point maxp = new Point(applet.getAppletWidth(), applet.getAppletHeight());
              Point p;
              FontMetrics fm;
              int lineHeight = 0;
              int x = 11;
              int y = applet.getAppletHeight()/2;
              this.clear();
              imageG.setColor(Config.WAITING_FONT_COLOR);
              imageG.setFont(Config.LARGE_FONT);
              fm = imageG.getFontMetrics(Config.LARGE_FONT);
              lineHeight = fm.getHeight();
              y = y + lineHeight/2;
              imageG.drawString("NOW LOADING..", x, y);
              y = y + lineHeight;
              imageG.setFont(Config.NORMAL_FONT);
              fm = imageG.getFontMetrics(Config.LARGE_FONT);
              lineHeight = fm.getHeight();
               y = y + lineHeight;
               p = myDrawString( new Point(x,y), maxp, applet.getStatusMessage(), fm);
               MAPG.drawImage(this.image, 0, 0, applet);
            }


            public void drawMember(Graphics MAPG) {
                Point maxp = new Point(applet.getMemberBgRect().x+applet.getMemberBgRect().width,applet.getMemberBgRect().y+applet.getMemberBgRect().height);
                Point p;
                FontMetrics fm;
                int lineHeight = 0;
                int x = applet.getMemberBgRect().x+10;
                int y = applet.getMemberBgRect().y;
                
                Image memberBgImage = applet.getMemberBgImage();
                
                if (memberBgImage != null) imageG.drawImage(memberBgImage, applet.getMemberBgRect().x, applet.getMemberBgRect().y, applet);
                Button2 urlButton = applet.getMemberButtons()[0];
                Button2 mailButton = applet.getMemberButtons()[1];
                Button2 closeButton = applet.getMemberButtons()[2];
                if (urlButton.getImage() != null) imageG.drawImage(urlButton.getImage(), urlButton.getRect().x, urlButton.getRect().y, applet);
                if (mailButton.getImage() != null) imageG.drawImage(mailButton.getImage(), mailButton.getRect().x, mailButton.getRect().y, applet);
                if (closeButton.getImage() != null) imageG.drawImage(closeButton.getImage(), closeButton.getRect().x, closeButton.getRect().y, applet);
                
    
                y += 40;
                imageG.setColor(Config.USERINFO_FONT_COLOR);
                
                imageG.setFont(Config.LARGE_FONT);
                fm = imageG.getFontMetrics(Config.LARGE_FONT);                
                lineHeight = fm.getHeight();
                y = y + lineHeight;
                imageG.drawString(applet.getCurrentIcon().getName2() + "の情報", x, y);
                
                y = y + lineHeight;
                
                imageG.setFont(Config.NORMAL_FONT);
                fm = imageG.getFontMetrics(Config.NORMAL_FONT);         
                lineHeight = fm.getHeight();            
                Member member = applet.getMember();                
                int titleWidth = fm.stringWidth("キーワード: ");
    
                /* 名前 */
                y = y + lineHeight;
                p = myDrawString( new Point(x,y), maxp, "名前: ", fm);
                x = p.x; y = p.y;            
                x = x + titleWidth;            
                p = myDrawString( new Point(x,y), maxp, member.getName(), fm);
                x = p.x; y = p.y;
                x = x - titleWidth;
                
                y = y + lineHeight;
                
                /* 性別 */
                y = y + lineHeight;
                p = myDrawString( new Point(x,y), maxp, "性別: ", fm);
                x = p.x; y = p.y;            
                x = x + titleWidth;            
                p = myDrawString( new Point(x,y), maxp, member.getSex(), fm);
                x = p.x; y = p.y;
                x = x - titleWidth;
    
                y = y + lineHeight;
    
                
                /* 血液型 */
                y = y + lineHeight;
                p = myDrawString( new Point(x,y), maxp, "血液型: ", fm);
                x = p.x; y = p.y;            
                x = x + titleWidth;            
                p = myDrawString( new Point(x,y), maxp, member.getBlood(), fm);
                x = p.x; y = p.y;
                x = x - titleWidth;
    
                y = y + lineHeight;
    
                /* 生年月日 */
                y = y + lineHeight;
                p = myDrawString( new Point(x,y), maxp, "生年月日: ", fm);
                x = p.x; y = p.y;            
                x = x + titleWidth;            
                p = myDrawString( new Point(x,y), maxp, member.getBirthday(), fm);
                x = p.x; y = p.y;
                x = x - titleWidth;
    
                y = y + lineHeight;
                
                /* 都道府県 */
                y = y + lineHeight;
                p = myDrawString( new Point(x,y), maxp, "都道府県: ", fm);
                x = p.x; y = p.y;            
                x = x + titleWidth;            
                p = myDrawString( new Point(x,y), maxp, member.getPrefectures(), fm);
                x = p.x; y = p.y;
                x = x - titleWidth;
    
                y = y + lineHeight;
    
                /* 職業 */
                y = y + lineHeight;
                p = myDrawString( new Point(x,y), maxp, "職業: ", fm);
                x = p.x; y = p.y;            
                x = x + titleWidth;            
                p = myDrawString( new Point(x,y), maxp, member.getJob(), fm);
                x = p.x; y = p.y;
                x = x - titleWidth;
    
                y = y + lineHeight;
                
                /* 趣味 */
                y = y + lineHeight;
                p = myDrawString( new Point(x,y), maxp, "趣味: ", fm);
                x = p.x; y = p.y;            
                x = x + titleWidth;            
                p = myDrawString( new Point(x,y), maxp, member.getInterest(), fm);
                x = p.x; y = p.y;
                x = x - titleWidth;
    
                y = y + lineHeight;
                
                /* キーワード */
                y = y + lineHeight;
                p = myDrawString( new Point(x,y), maxp, "キーワード: ", fm);
                x = p.x; y = p.y;            
                x = x + titleWidth;            
                p = myDrawString( new Point(x,y), maxp, member.getKeyword(), fm);
                x = p.x; y = p.y;
                x = x - titleWidth;
    
                y = y + lineHeight;
                




                MAPG.drawImage(this.image, 0, 0, applet);
            }


            public void drawMap(Graphics MAPG) {          
                    Point maxp = new Point(applet.getAppletWidth(), applet.getAppletHeight());
                    Point p;
                    FontMetrics fm;
                    int lineHeight = 0;
                    int x = 0;
                    int y = 0;                    
                    this.clear();
    
                    Button2 saveButton = applet.getMenuButtons()[0];
                    Button2 backButton = applet.getMenuButtons()[1];
                    if (saveButton.getImage() != null && applet.canSave()) {
                        imageG.drawImage(saveButton.getImage(), saveButton.getRect().x, saveButton.getRect().y, applet);
                        if(saveButton.isActive()) drawHelpMessage(applet.getHelpMessage(), saveButton.getRect().x-140, saveButton.getRect().y+saveButton.getRect().height+10);
                    }
                    if (backButton.getImage() != null && applet.canBack()) {
                        imageG.drawImage(backButton.getImage(), backButton.getRect().x, backButton.getRect().y, applet);
                        if(backButton.isActive()) drawHelpMessage(applet.getHelpMessage(), backButton.getRect().x+backButton.getRect().width-30, backButton.getRect().y);

                    }


                    
                    int height1 = 21;
                    int margin = 11;
                    int width1 = 240;
                    int width2 =50;                    
                    Rectangle mapRect = applet.getMapRect();
                    imageG.setColor(Config.MAP_TITLE1_BGCOLOR);
                    imageG.fillRect(mapRect.x+margin,mapRect.y,width1,height1);
                    imageG.setColor(Config.MAP_TITLE2_BGCOLOR);
                    imageG.fillRect(mapRect.x+margin+width1,mapRect.y,width2,height1);
                    
                    
                    imageG.setFont(Config.NORMAL_FONT);
                    //imageG.setFont(new Font("Courier",Font.BOLD,12));                    
                    fm = imageG.getFontMetrics();
                    String title1 = applet.getMapTitle1();
                    y = height1/2 + fm.getHeight()/2;
                    x = margin+width1-fm.stringWidth(title1)-10;
                    imageG.setColor(Config.MAP_TITLE1_FONT_COLOR);
                    imageG.drawString(title1, x, y);
                    
                    imageG.setFont(Config.NORMAL_FONT);
                    fm = imageG.getFontMetrics();
                    String title2 = applet.getMapTitle2();
                    x = margin+width1+width2-fm.stringWidth(title2)-10;
                    imageG.setColor(Config.MAP_TITLE2_FONT_COLOR);
                    imageG.drawString(title2, x, y);

                    String title3 = applet.getMapTitle3();
                    y = height1+height1/2 + fm.getHeight()/2;                    
                    x = margin;
                    imageG.setColor(Config.MAP_FONT_COLOR);
                    p = myDrawString( new Point(x,y), maxp, title3, fm);
                    
                    this.drawIconList();
                    this.drawHistoryIconList();
                    

                    MAPG.drawImage(this.image, 0, 0, applet);
            }

            private void clear() {
                imageG.setColor(Config.MAP_BGCOLOR);
                imageG.fillRect(0,0,applet.getAppletWidth(),applet.getAppletHeight());
            }



            private void drawHistoryIconList() {
                if (applet.getHistoryIconList() == null) return;
                    Font nameFont, relationFont;
                    Color iconColor, nameColor, relationColor, lineColor;
                    Icon current = applet.getCurrentIcon();
    
                    /*
                     * 履歴ラインを描画
                     */
                    for(int i = 1; i < applet.getHistoryIconList().size(); i++) { //loop
                            Icon icon = (Icon)applet.getHistoryIconList().elementAt(i);
                            Icon parent = (Icon)applet.getHistoryIconList().elementAt(i-1);
                            iconColor = Config.HISTORY_ICON_COLOR;
                            lineColor = Config.HISTORY_LINE_COLOR;
                            this.drawRelationLine(icon, parent, lineColor);
                    } //loop
    
    
                    /*
                     * 履歴アイコンを描画
                     */
                    for(int i = 0; i < applet.getHistoryIconList().size(); i++) { //loop
                            Icon icon = (Icon)applet.getHistoryIconList().elementAt(i);
    
                            if (icon == current) {
                                nameFont = Config.NORMAL_FONT;
                                if (icon.isActive()) {
                                    iconColor = Config.ACTIVE_CURRENT_ICON_COLOR;
                                    nameColor = Config.ACTIVE_CURRENT_ICON_COLOR;
                                    this.drawIcon(icon, iconColor);
                                    this.drawName(icon, nameFont, nameColor);                                    
                                    FontMetrics fm = imageG.getFontMetrics(Config.SMALL_FONT);
                                    String helpMessage = applet.getHelpMessage();
                                    int x = 0;
                                    int y = 0;
                                    Point p = applet.getCenterPoint();
                                    if (icon.getX() <= p.x) {
                                        x = icon.getX()+Config.LARGE_ICON_SIZE;
                                        y = icon.getY();
                                    } else {
                                        x = icon.getX()-Config.LARGE_ICON_SIZE-fm.stringWidth(helpMessage);
                                        y = y = icon.getY() - fm.getHeight();
                                    }
                                    drawHelpMessage(helpMessage, x, y);                                    
                                } else {
                                    iconColor = Config.CURRENT_ICON_COLOR;
                                    nameColor = Config.CURRENT_ICON_COLOR;
                                    this.drawIcon(icon, iconColor);
                                    this.drawName(icon, nameFont, nameColor);
                                }

                            } else {
                                iconColor = Config.HISTORY_ICON_COLOR;
                                nameFont = Config.NORMAL_FONT;
                                nameColor = Config.HISTORY_ICON_COLOR;
                                this.drawIcon(icon, iconColor);
                                this.drawName(icon, nameFont, nameColor);
                            }
                            
                            

                            
                    } //loop

            }
    
             /**
             * アイコンを描画
             */
            private void drawIcon(Icon icon, Color iconColor) {
                if (icon.getUid() == -1) return;
                
                //imageG.fillOval(icon.getX() - iconSize/2,icon.getY() - iconSize/2,iconSize,iconSize);
                
                Rectangle rect = icon.getRect();
                if (icon.getType() == 0) {
                    imageG.setColor(iconColor);
                    imageG.fillOval(rect.x, rect.y, rect.width, rect.height);
                } else if(icon.getType() == 1) { //仮登録
                    imageG.setColor(Config.MAP_BGCOLOR);
                    imageG.fillOval(rect.x, rect.y, rect.width, rect.height);
                    imageG.setColor(iconColor);
                    imageG.drawOval(rect.x, rect.y, rect.width, rect.height);
                } else if(icon.getType() == -1) { //削除ユーザー
                    imageG.setColor(iconColor);
                    imageG.drawLine(rect.x, rect.y, rect.x+rect.width, rect.y+rect.height);
                    imageG.drawLine(rect.x+rect.width, rect.y, rect.x, rect.y+rect.height);
                }
            }



    
             /**
             * 通常のアイコンを描画
             */
            private void drawIconList() {
                 /**
                 * 通常のアイコンがない場合は処理を戻す
                 */
                if (applet.getIconList() == null ) return;
    
                Font nameFont, relationFont;
                Color iconColor, nameColor, relationColor, lineColor;
    
                Enumeration e = applet.getIconList().elements();
                while (e.hasMoreElements()) { //loop
                    Icon parent = applet.getCurrentIcon();
                    Icon icon = (Icon)e.nextElement();
                    
                    int hnum = applet.getHistoryIconList().size();
                    if (hnum > 1) {
                      Icon parentParent = (Icon)applet.getHistoryIconList().elementAt(hnum - 2);
                      if (icon.getUid() == parentParent.getUid()) {
                         if (e.hasMoreElements()) icon = (Icon)e.nextElement();
                         else return;
                      }
                    }
    
    
                    if (icon.isActive()) {
                            iconColor = Config.ACTIVE_ICON_COLOR;
                            lineColor = Config.ACTIVE_LINE_COLOR;
                            nameFont = Config.NORMAL_FONT;
                            nameColor = Config.ACTIVE_ICON_COLOR;
                            relationFont = Config.NORMAL_FONT;
                            relationColor = Config.ACTIVE_RELATION_COLOR;

                            this.drawRelationLine(icon, parent, lineColor);
                            this.drawRelation(icon, parent, relationFont, relationColor);
                            this.drawIcon(icon, iconColor);
                            this.drawName(icon, nameFont, nameColor);

                            FontMetrics fm = imageG.getFontMetrics(Config.SMALL_FONT);
                            int x = 0;
                            int y = 0;
                            String helpMessage = applet.getHelpMessage();
                            Point p = applet.getCenterPoint();
                            if (icon.getX() <= p.x) {
                                x = icon.getX()+Config.LARGE_ICON_SIZE;
                                y = icon.getY();
                            } else {
                                x = icon.getX()-Config.LARGE_ICON_SIZE-fm.stringWidth(helpMessage);
                                y = y = icon.getY() - fm.getHeight();
                            }
                            drawHelpMessage(helpMessage, x, y);

                            
                            
                    } else {

                            iconColor = Config.NORMAL_ICON_COLOR;
                            this.drawIcon(icon, iconColor);
                    }
                } //loop
    
            }
    
    
    
    
             /**
             * 名前を描画
             */
            private void drawName(Icon icon, Font nameFont, Color nameColor) {
                if (icon.getUid() == -1) return;
                imageG.setFont(nameFont);
                imageG.setColor(nameColor);
                String name = icon.getName();
                
                Rectangle rect = icon.getRect();
                FontMetrics fm = imageG.getFontMetrics(nameFont);                
                imageG.drawString(name, icon.getX(), icon.getY()-rect.height/2-fm.getDescent()-2);
                
            }
             /**
             * アイコンとアイコンをつなぐラインを描画
             */
            private void drawRelationLine(Icon icon, Icon parent, Color lineColor) {
                if (parent.getUid() == -1) return;
                
                if (icon.getType() == 1) {
                    imageG.setColor(lineColor);
                    drawTensen(icon.getX(), icon.getY(), parent.getX(), parent.getY());
                } else {
                    imageG.setColor(lineColor);
                    imageG.drawLine(icon.getX(), icon.getY(), parent.getX(), parent.getY());
                }
            }
            
            private void drawTensen(int startX, int startY, int entX, int endY) {
                int width = entX - startX;
                int height = endY - startY;
                int num = 20;
                boolean flag = true;
                
                for (int i=0; i<num; i++ ) {//loop
                    if (flag) { //if
                        int j = i + 1;
                        imageG.drawLine(startX+(int)(width*i/num), startY+(int)(height*i/num), startX+(int)(width*j/num), startY+(int)(height*j/num));
                        flag = false;
                    } else {
                        flag = true;   
                    }
                } //loop
            }
            
             /**
             * 関係を描画
             */
            private void drawRelation(Icon icon, Icon parent, Font relationFont, Color relationColor) {
                if (parent.getUid() == -1) return;
                imageG.setFont(relationFont);
                imageG.setColor(relationColor);
                imageG.drawString(icon.getRelation(), (icon.getX()+parent.getX())/2, (icon.getY()+parent.getY())/2);
            }
    
            private void drawHelpMessage(String message, int x, int y) {
                if (message == null) return;
                else if (message.equals("")) return;
                
                FontMetrics fm;
                imageG.setFont(Config.SMALL_FONT);
                fm = imageG.getFontMetrics(Config.SMALL_FONT);
                int height = fm.getHeight();
                int width = fm.stringWidth(message);
                imageG.setColor(Config.HELP_BGCOLOR);
                imageG.fillRect(x,y,width,height);
                imageG.setColor(Config.HELP_FONT_COLOR);
                imageG.drawString(message, x, y+height-fm.getDescent());
            }
    
    
              /**
               * 文字を描画（改行などの処理も行う）
               */
                private Point myDrawString(Point startp, Point maxp, String value, FontMetrics fm) {
                   if (value == null) return startp;
                   int x = startp.x;
                   int y = startp.y;
                   int lineHeight = fm.getHeight();               
                   StringTokenizer st = new StringTokenizer(value,"\n");
                   while (st.hasMoreTokens()) { //loop1
                       x = startp.x;
                       String str = st.nextToken();
                       int width = fm.stringWidth(str);                   
                       if ((x+width) >= maxp.x ) {
                           StringTokenizer st2 = new StringTokenizer(str," ");
                           while (st2.hasMoreTokens()) { //loop2
                               String str2 = st2.nextToken() + " ";
                               int width2 = fm.stringWidth(str2);
                               if (x+width2 >= maxp.x) {
                                 x = startp.x;
                                 y = y + lineHeight;
                                 imageG.drawString(str2, x, y);
                                 x = x + width2;
                               } else {
                                 imageG.drawString(str2, x, y);
                                 x = x + width2;
                               }
                           } //loop2
                       } else {
                               imageG.drawString(str, x, y);
                       }
                       y = y + lineHeight;
                   } //loop1
                   if (!value.equals("")) y = y - lineHeight;
                   return new Point(startp.x, y);
                }

    }
