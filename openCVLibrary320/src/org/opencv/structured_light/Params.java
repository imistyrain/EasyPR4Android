
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.structured_light;



// C++: class Params
//javadoc: Params
public class Params {

    protected final long nativeObj;
    protected Params(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    //
    // C++:   Params()
    //

    //javadoc: Params::Params()
    public   Params()
    {
        
        nativeObj = Params_0();
        
        return;
    }


    //
    // C++: int Params::width
    //

    //javadoc: Params::get_width()
    public  int get_width()
    {
        
        int retVal = get_width_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::width
    //

    //javadoc: Params::set_width(width)
    public  void set_width(int width)
    {
        
        set_width_0(nativeObj, width);
        
        return;
    }


    //
    // C++: int Params::height
    //

    //javadoc: Params::get_height()
    public  int get_height()
    {
        
        int retVal = get_height_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::height
    //

    //javadoc: Params::set_height(height)
    public  void set_height(int height)
    {
        
        set_height_0(nativeObj, height);
        
        return;
    }


    //
    // C++: int Params::nbrOfPeriods
    //

    //javadoc: Params::get_nbrOfPeriods()
    public  int get_nbrOfPeriods()
    {
        
        int retVal = get_nbrOfPeriods_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::nbrOfPeriods
    //

    //javadoc: Params::set_nbrOfPeriods(nbrOfPeriods)
    public  void set_nbrOfPeriods(int nbrOfPeriods)
    {
        
        set_nbrOfPeriods_0(nativeObj, nbrOfPeriods);
        
        return;
    }


    //
    // C++: float Params::shiftValue
    //

    //javadoc: Params::get_shiftValue()
    public  float get_shiftValue()
    {
        
        float retVal = get_shiftValue_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::shiftValue
    //

    //javadoc: Params::set_shiftValue(shiftValue)
    public  void set_shiftValue(float shiftValue)
    {
        
        set_shiftValue_0(nativeObj, shiftValue);
        
        return;
    }


    //
    // C++: int Params::methodId
    //

    //javadoc: Params::get_methodId()
    public  int get_methodId()
    {
        
        int retVal = get_methodId_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::methodId
    //

    //javadoc: Params::set_methodId(methodId)
    public  void set_methodId(int methodId)
    {
        
        set_methodId_0(nativeObj, methodId);
        
        return;
    }


    //
    // C++: int Params::nbrOfPixelsBetweenMarkers
    //

    //javadoc: Params::get_nbrOfPixelsBetweenMarkers()
    public  int get_nbrOfPixelsBetweenMarkers()
    {
        
        int retVal = get_nbrOfPixelsBetweenMarkers_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::nbrOfPixelsBetweenMarkers
    //

    //javadoc: Params::set_nbrOfPixelsBetweenMarkers(nbrOfPixelsBetweenMarkers)
    public  void set_nbrOfPixelsBetweenMarkers(int nbrOfPixelsBetweenMarkers)
    {
        
        set_nbrOfPixelsBetweenMarkers_0(nativeObj, nbrOfPixelsBetweenMarkers);
        
        return;
    }


    //
    // C++: bool Params::horizontal
    //

    //javadoc: Params::get_horizontal()
    public  boolean get_horizontal()
    {
        
        boolean retVal = get_horizontal_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::horizontal
    //

    //javadoc: Params::set_horizontal(horizontal)
    public  void set_horizontal(boolean horizontal)
    {
        
        set_horizontal_0(nativeObj, horizontal);
        
        return;
    }


    //
    // C++: bool Params::setMarkers
    //

    //javadoc: Params::get_setMarkers()
    public  boolean get_setMarkers()
    {
        
        boolean retVal = get_setMarkers_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::setMarkers
    //

    //javadoc: Params::set_setMarkers(setMarkers)
    public  void set_setMarkers(boolean setMarkers)
    {
        
        set_setMarkers_0(nativeObj, setMarkers);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   Params()
    private static native long Params_0();

    // C++: int Params::width
    private static native int get_width_0(long nativeObj);

    // C++: void Params::width
    private static native void set_width_0(long nativeObj, int width);

    // C++: int Params::height
    private static native int get_height_0(long nativeObj);

    // C++: void Params::height
    private static native void set_height_0(long nativeObj, int height);

    // C++: int Params::nbrOfPeriods
    private static native int get_nbrOfPeriods_0(long nativeObj);

    // C++: void Params::nbrOfPeriods
    private static native void set_nbrOfPeriods_0(long nativeObj, int nbrOfPeriods);

    // C++: float Params::shiftValue
    private static native float get_shiftValue_0(long nativeObj);

    // C++: void Params::shiftValue
    private static native void set_shiftValue_0(long nativeObj, float shiftValue);

    // C++: int Params::methodId
    private static native int get_methodId_0(long nativeObj);

    // C++: void Params::methodId
    private static native void set_methodId_0(long nativeObj, int methodId);

    // C++: int Params::nbrOfPixelsBetweenMarkers
    private static native int get_nbrOfPixelsBetweenMarkers_0(long nativeObj);

    // C++: void Params::nbrOfPixelsBetweenMarkers
    private static native void set_nbrOfPixelsBetweenMarkers_0(long nativeObj, int nbrOfPixelsBetweenMarkers);

    // C++: bool Params::horizontal
    private static native boolean get_horizontal_0(long nativeObj);

    // C++: void Params::horizontal
    private static native void set_horizontal_0(long nativeObj, boolean horizontal);

    // C++: bool Params::setMarkers
    private static native boolean get_setMarkers_0(long nativeObj);

    // C++: void Params::setMarkers
    private static native void set_setMarkers_0(long nativeObj, boolean setMarkers);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
