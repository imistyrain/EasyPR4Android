
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.tracking;



// C++: class TrackerBoosting
//javadoc: TrackerBoosting
public class TrackerBoosting extends Tracker {

    protected TrackerBoosting(long addr) { super(addr); }


    //
    // C++: static Ptr_TrackerBoosting create()
    //

    //javadoc: TrackerBoosting::create()
    public static TrackerBoosting create()
    {
        
        TrackerBoosting retVal = new TrackerBoosting(create_0());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_TrackerBoosting create()
    private static native long create_0();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
