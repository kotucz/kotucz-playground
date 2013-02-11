/*
 * AnimatedForm.java
 *
 * Created on 28. bøezen 2006, 15:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package spygame;

import javax.microedition.lcdui.*;

import javax.microedition.rms.*;

/**
 *
 * @author PC
 */
public class BestScoresScreen extends AnimatedList implements RecordComparator {
    
     
    /** Creates a new instance of AnimatedForm */
    public BestScoresScreen() {
        setSelected(-1);
        if (!openStore()) {
            System.err.println("failed to open record store");
            setItems(new String[] {"*opening failed"});
            return;
        } 
        
        items = null;
    
    
//    if (Agent.lives==0||Level.currentLevel>1) {
//        addNew(Agent.title, Agent.score);
//    }
//    if (!readScore()) setItems(new String[] {"*loading failed"});
//    
//        Agent.score = 0;
//        Level.currentLevel = 0;
     
    }
        
    public void itemSelected(int i) {
        Game.showMainScreen();
    }
    
    public void paint(Graphics g) {
        if (items==null) readScore();
        super.paint(g);
    } 
    
    private RecordStore store;		// Record store, null if not open

    /**
     * Open the record store and locate
     * the record with the level number in it.
     */
    boolean openStore() {

	try {
	    store = RecordStore.openRecordStore("NuclearBossScores", true);
	} catch (RecordStoreException ex) {
            return false;
	}

	if (store == null)
	    return false;

	return true;
    }

    public int compare(byte[] a, byte[] b) {
        int a1 = getInt(a, 0);
        int b1 = getInt(b, 0);
        if (a1>b1) return PRECEDES;
        if (a1<b1) return FOLLOWS;
        return EQUIVALENT;
    };
    
    /**
     * Read the score for the current level.
     * Read through the records looking for the one for this level.
     */
    boolean readScore() {
	try {
	    
            int scoreId = 0;
            
            String[] names = new String[5];
            
	    // Locate the matching record
	    RecordEnumeration enm = store.enumerateRecords(null, this, true);
	    while (enm.hasNextElement()) {
	        if (scoreId>=names.length) break;
                
                byte[] rec = enm.nextRecord();
                if (new String(rec, 4, rec.length-4)==Agent.name&&Agent.score==getInt(rec, 0)) setSelected(scoreId);
                names[scoreId] = new String(rec, 4, rec.length-4)+"_"+getInt(rec, 0);               
                scoreId++;
		
	    }
            
            setItems(names);
            
            closeStore();
            
            return true;
	} catch (RecordStoreException ex) {
            setItems(new String[] {""+ex});
	    ex.printStackTrace();
	    return false;
	}

    }

    /**
     * Get an integer from an array.
     */
    private int getInt(byte[] buf, int offset) {
	return (buf[offset+0] & 0xff) << 24 |
	    (buf[offset+1] & 0xff) << 16 |
	    (buf[offset+2] & 0xff) << 8 |
	    (buf[offset+3] & 0xff);
    }

    /**
     * Put an integer to an array
     */
    private void putInt(byte[] buf, int offset, int value) {
	buf[offset+0] = (byte)((value >> 24) & 0xff);
	buf[offset+1] = (byte)((value >> 16) & 0xff);
	buf[offset+2] = (byte)((value >>  8) & 0xff);
	buf[offset+3] = (byte)((value >>  0) & 0xff);
    }

/*    String[] getBests () {
        String[] bests = new String[5];
        // TODO loading
        return bests;
    }
  */  
    protected void hideNotify() {
        closeStore();
    }
    
    protected void addNew(String name, int score) {
        byte[] rec = ("    "+name).getBytes();
        putInt(rec, 0, score);
        try {
            if (store!=null) store.addRecord(rec, 0, rec.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.title = name+" "+score;
    }    
    
    /**
     * Close the store.
     */
    void closeStore() {
	try {
	    if (store != null) {
		store.closeRecordStore();
	    }
	} catch (RecordStoreException ex) {
	    ex.printStackTrace();
	}
    }

    
}
