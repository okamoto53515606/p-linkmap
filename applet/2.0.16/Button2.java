import java.awt.*;

    public class Button2 {
            private String actionCommand = null;
            private Rectangle rect = null;
            private boolean active = false;
            private Image image = null;
            private Image activeImage = null;
            private PLMApplet applet;
            public Button2(String actionCommand, Point p, Image image, Image activeImage, PLMApplet applet) {
                    this.applet = applet;
                    this.actionCommand = actionCommand;
                    this.image = image;
                    this.activeImage = activeImage;
                    int width = 0;
                    int height = 0;
                    if (image != null) {
                        width = image.getWidth(applet);
                        height = image.getHeight(applet);
                    }
                    this.rect = new Rectangle(p.x, p.y, width, height);
            }
            public String getActionCommand() {
                if (actionCommand == null) return "";
                return actionCommand;
            }
            public Rectangle getRect() {
                return rect;   
            }
            public int getWidth() {
                return rect.width;
            }
            public int getHeight() {
                return rect.height;
            }
            public Point getPoint() {
                return new Point(rect.x, rect.y);   
            }
            public int getX() {
                return getPoint().x;    
            }
            public int getY() {
                return getPoint().y;   
            }
            public Image getImage() {
                if (isActive() && activeImage != null) return activeImage;
                else return image;
            }
            public boolean isActive() {
                return active;   
            }
            public void setActive(boolean active) {
                this.active = active;
            }
    }
