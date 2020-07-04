
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.tracking;



// C++: class TrackerTLD
//javadoc: TrackerTLD
public class TrackerTLD extends Tracker {

    protected TrackerTLD(long addr) { super(addr); }


    //
    // C++: static Ptr_TrackerTLD create()
    //

    //javadoc: TrackerTLD::create()
    public static TrackerTLD create()
    {
        
        TrackerTLD retVal = new TrackerTLD(create_0());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_TrackerTLD create()
    private static native long create_0();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
