/*
 * HelloMidlet.java
 *
 * Created on 30. srpen 2007, 21:56
 */

package hello;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author PC
 */
public class HelloMidlet extends MIDlet implements CommandListener {
    
    /** Creates a new instance of HelloMidlet */
    public HelloMidlet() {
    }
    
    private Form helloForm;//GEN-BEGIN:MVDFields
    private StringItem helloStringItem;
    private Command exitCommand;//GEN-END:MVDFields
    
//GEN-LINE:MVDMethods

    /** This method initializes UI of the application.//GEN-BEGIN:MVDInitBegin
     */
    private void initialize() {//GEN-END:MVDInitBegin
        // Insert pre-init code here
        getDisplay().setCurrent(get_helloForm());//GEN-LINE:MVDInitInit
        // Insert post-init code here
    }//GEN-LINE:MVDInitEnd
    
    /** Called by the system to indicate that a command has been invoked on a particular displayable.//GEN-BEGIN:MVDCABegin
     * @param command the Command that ws invoked
     * @param displayable the Displayable on which the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:MVDCABegin
        // Insert global pre-action code here
        if (displayable == helloForm) {//GEN-BEGIN:MVDCABody
            if (command == exitCommand) {//GEN-END:MVDCABody
                // Insert pre-action code here
                exitMIDlet();//GEN-LINE:MVDCAAction3
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase3
        }//GEN-END:MVDCACase3
        // Insert global post-action code here
}//GEN-LINE:MVDCAEnd
    
    /**
     * This method should return an instance of the display.
     */
    public Display getDisplay() {//GEN-FIRST:MVDGetDisplay
        return Display.getDisplay(this);
    }//GEN-LAST:MVDGetDisplay
    
    /**
     * This method should exit the midlet.
     */
    public void exitMIDlet() {//GEN-FIRST:MVDExitMidlet
        getDisplay().setCurrent(null);
        destroyApp(true);
        notifyDestroyed();
    }//GEN-LAST:MVDExitMidlet
    
    /** This method returns instance for helloForm component and should be called instead of accessing helloForm field directly.//GEN-BEGIN:MVDGetBegin2
     * @return Instance for helloForm component
     */
    public Form get_helloForm() {
        if (helloForm == null) {//GEN-END:MVDGetBegin2
            // Insert pre-init code here
            helloForm = new Form(null, new Item[] {get_helloStringItem()});//GEN-BEGIN:MVDGetInit2
            helloForm.addCommand(get_exitCommand());
            helloForm.setCommandListener(this);//GEN-END:MVDGetInit2
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd2
        return helloForm;
    }//GEN-END:MVDGetEnd2
    
    /** This method returns instance for helloStringItem component and should be called instead of accessing helloStringItem field directly.//GEN-BEGIN:MVDGetBegin4
     * @return Instance for helloStringItem component
     */
    public StringItem get_helloStringItem() {
        if (helloStringItem == null) {//GEN-END:MVDGetBegin4
            // Insert pre-init code here
            helloStringItem = new StringItem("Hello", "Hello, World!");//GEN-LINE:MVDGetInit4
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd4
        return helloStringItem;
    }//GEN-END:MVDGetEnd4
    
    /** This method returns instance for exitCommand component and should be called instead of accessing exitCommand field directly.//GEN-BEGIN:MVDGetBegin5
     * @return Instance for exitCommand component
     */
    public Command get_exitCommand() {
        if (exitCommand == null) {//GEN-END:MVDGetBegin5
            // Insert pre-init code here
            exitCommand = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit5
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd5
        return exitCommand;
    }//GEN-END:MVDGetEnd5
    
    public void startApp() {
        initialize();
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
}
