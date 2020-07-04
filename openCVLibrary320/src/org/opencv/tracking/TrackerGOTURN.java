
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.tracking;



// C++: class TrackerGOTURN
//javadoc: TrackerGOTURN
public class TrackerGOTURN extends Tracker {

    protected TrackerGOTURN(long addr) { super(addr); }


    //
    // C++: static Ptr_TrackerGOTURN create()
    //

    //javadoc: TrackerGOTURN::create()
    public static TrackerGOTURN create()
    {
        
        TrackerGOTURN retVal = new TrackerGOTURN(create_0());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_TrackerGOTURN create()
    private static native long create_0();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
