
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.tracking;



// C++: class TrackerMedianFlow
//javadoc: TrackerMedianFlow
public class TrackerMedianFlow extends Tracker {

    protected TrackerMedianFlow(long addr) { super(addr); }


    //
    // C++: static Ptr_TrackerMedianFlow create()
    //

    //javadoc: TrackerMedianFlow::create()
    public static TrackerMedianFlow create()
    {
        
        TrackerMedianFlow retVal = new TrackerMedianFlow(create_0());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_TrackerMedianFlow create()
    private static native long create_0();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
