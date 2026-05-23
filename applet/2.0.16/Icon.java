import java.awt.*;

public class Icon {
        private int uid = 0;
        private String name = null; 
        private String relation = null; 
        private Rectangle rect = null;
        private boolean active = false;
        private int depth = 0;
        private int type = 0;
        /*
         * type
         * -1 : íœƒ†[ƒU[‚ÌƒAƒCƒRƒ“
         * 0 : “o˜^Ï
         * 1 : ‰¼“o˜^
         */

        public Icon(int uid, String name, String relation, Point p) {
                this(uid, name, relation, p, 0);
        }
        public Icon(int uid, String name, String relation, Point p, int type) {
                this.uid = uid;
                this.name = name;
                this.relation = relation;
                int size = Config.NORMAL_ICON_SIZE;
                this.rect = new Rectangle(p.x-size/2, p.y-size/2, size, size);
                this.depth = 1;
                this.type = type;
        }
        public int getDepth() {
            return this.depth;   
        }
        public void setDepth(int depth) {
            this.depth = depth;
            if (depth == 0) {
                setSize(Config.LARGE_ICON_SIZE);   
            } else {
                if (name != null && name.equals("‚ ‚È‚½")) setSize(Config.LARGE_ICON_SIZE);
                else setSize(Config.NORMAL_ICON_SIZE);
            }
        }
        public int getUid() {
            return uid;
        }
        public String getStringUid() {
            return new Integer(getUid()).toString();
        }
        public String getStringX() {
            return new Integer(getX()).toString();
        }
        public String getStringY() {
            return new Integer(getY()).toString();
        }
        public String getName() {
            return name;
        }
        public String getName2() {
            if (name != null && name.equals("‚ ‚È‚½")) {
                return name;
            } else {
                return name + " ‚³‚ñ";
            }
        }
        public int getType() {
            return type;
        }
        public String getRelation() {
            return relation;
        }        
        public Point getPoint() {
            return new Point(rect.x+rect.width/2, rect.y+rect.height/2);
        }
        public int getX() {
            return getPoint().x;    
        }
        public int getY() {
            return getPoint().y;   
        }
        public Rectangle getRect() {
            return rect;
        }
        public void setPoint(Point p) {
            rect.move(p.x-rect.width/2, p.y-rect.height/2);
            //rect.reshape(p.x-rect.width/2, p.y-rect.height/2, rect.width, rect.height);
        }
        public boolean isActive() {
            return active;   
        }
        
        public void setActive(boolean active) {
            this.active = active;
            if (depth > 0) {
                if (active) setSize(Config.LARGE_ICON_SIZE);
                else setSize(Config.NORMAL_ICON_SIZE);
            }
        }

        public void setSize(int size) {
            int dx = size/2 - rect.width/2;
            rect.reshape(rect.x-dx, rect.y-dx, size, size);
        }
}
