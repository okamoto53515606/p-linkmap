import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;




public class ErrorFrame extends Frame {

    private PLMApplet applet;


        ErrorFrame(String message, PLMApplet applet) {    
            super("エラーが発生しました.");
            this.applet = applet;
            TextArea textArea = new TextArea(3,40);       
            resize(400,300);
            //add(new Label("エラーが発生しました."), "North");
            //add(textArea, "Center");
            add(textArea);
            //Button closeButton = new Button("CLOSE");
            //add(closeButton, "South");
            setVisible(true);
            textArea.appendText(message);
        }
        
        /*
        public boolean action (Event evt, Object obj) {
            if (evt.target instanceof Button) {
                if (obj.equals("CLOSE")) {
                        frameClose();
                }
                return true;
            }
            return false;
        }
        */
        
        public boolean handleEvent(Event evt) {
            if (evt.id == Event.WINDOW_DESTROY) {
                frameClose();   
            }
            return (super.handleEvent(evt));
        }
        
        private void frameClose() {
            applet.changeStatus(1);
            dispose();
        }




}













