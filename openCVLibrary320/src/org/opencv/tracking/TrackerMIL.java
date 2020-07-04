
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.tracking;



// C++: class TrackerMIL
//javadoc: TrackerMIL
public class TrackerMIL extends Tracker {

    protected TrackerMIL(long addr) { super(addr); }


    //
    // C++: static Ptr_TrackerMIL create()
    //

    //javadoc: TrackerMIL::create()
    public static TrackerMIL create()
    {
        
        TrackerMIL retVal = new TrackerMIL(create_0());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_TrackerMIL create()
    private static native long create_0();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
