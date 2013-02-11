package spygame;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

public class StoreManager
{

    public StoreManager(int i, int j)
    {
        storeData = null;
        position = 0;
        procIndex = 0;
        procOp = 0;
        dataSize = 0;
        loaded = false;
        procIndex = i;
        procOp = j;
        position = 0;
        dataSize = 0;
        loaded = false;
        if(procOp == 1)
            try
            {
                RecordStore recordstore = RecordStore.openRecordStore(RecordIdentifier, true);
                RecordEnumeration recordenumeration = recordstore.enumerateRecords(null, null, false);
                for(int k = 0; k < recordenumeration.numRecords(); k++)
                {
                    int l = recordenumeration.nextRecordId();
                    byte byte0 = recordstore.getRecord(l)[0];
                    if(byte0 == i)
                    {
                        storeData = new byte[recordstore.getRecordSize(l) - 1];
                        System.arraycopy(recordstore.getRecord(l), 1, storeData, 0, recordstore.getRecordSize(l) - 1);
                        dataSize = recordstore.getRecordSize(l) - 1;
                        loaded = true;
                    }
                }

                recordenumeration.destroy();
                recordstore.closeRecordStore();
            }
            catch(Exception exception) { }
        else
            loaded = true;
    }

    private void enLargeData(int i)
    {
        if(dataSize > 0)
        {
            byte abyte0[] = new byte[dataSize + i];
            System.arraycopy(storeData, 0, abyte0, 0, dataSize);
            dataSize += i;
            storeData = new byte[dataSize];
            System.arraycopy(abyte0, 0, storeData, 0, dataSize);
        } else
        {
            storeData = new byte[i];
            dataSize += i;
        }
    }

    private int getFullByte()
    {
        return storeData[position] < 0 ? 256 + storeData[position++] : storeData[position++];
    }

    public byte data(byte byte0)
    {
        byte byte1 = byte0;
        if(procOp == 0)
        {
            enLargeData(1);
            storeData[position++] = byte0;
        } else
        if(loaded)
            byte1 = storeData[position++];
        return byte1;
    }

    public short data(short word0)
    {
        short word1 = word0;
        if(procOp == 0)
        {
            enLargeData(2);
            storeData[position++] = (byte)(word0 & 0xff);
            storeData[position++] = (byte)(word0 >> 8 & 0xff);
        } else
        if(loaded)
            word1 = (short)(getFullByte() + 256 * getFullByte());
        return word1;
    }

    public int data(int i)
    {
        int j = i;
        if(procOp == 0)
        {
            enLargeData(4);
            storeData[position++] = (byte)(i & 0xff);
            storeData[position++] = (byte)(i >> 8 & 0xff);
            storeData[position++] = (byte)(i >> 16 & 0xff);
            storeData[position++] = (byte)(i >> 24 & 0xff);
        } else
        if(loaded)
            j = getFullByte() + 256 * getFullByte() + 0x10000 * getFullByte() + 0x1000000 * getFullByte();
        return j;
    }

    public boolean data(boolean flag)
    {
        return data((byte)(flag ? 1 : 0)) == 1;
    }

    public void save()
    {
        if(procOp == 0)
            try
            {
                RecordStore recordstore = RecordStore.openRecordStore(RecordIdentifier, true);
                RecordEnumeration recordenumeration = recordstore.enumerateRecords(null, null, false);
                for(int i = 0; i < recordenumeration.numRecords(); i++)
                {
                    int j = recordenumeration.nextRecordId();
                    byte byte0 = recordstore.getRecord(j)[0];
                    if(byte0 == procIndex)
                        recordstore.deleteRecord(j);
                }

                byte abyte0[] = new byte[dataSize + 1];
                abyte0[0] = (byte)procIndex;
                System.arraycopy(storeData, 0, abyte0, 1, dataSize);
                recordstore.addRecord(abyte0, 0, abyte0.length);
                recordenumeration.destroy();
                recordstore.closeRecordStore();
            }
            catch(Exception exception) { }
    }

    public static final int SM_SAVE = 0;
    public static final int SM_LOAD = 1;
    private static final String RecordIdentifier = "NuclearBossSaveStore";
    private byte storeData[];
    private int position;
    private int procIndex;
    private int procOp;
    private int dataSize;
    public boolean loaded;
}