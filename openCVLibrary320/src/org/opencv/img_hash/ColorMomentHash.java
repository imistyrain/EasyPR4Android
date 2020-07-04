
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.img_hash;



// C++: class ColorMomentHash
//javadoc: ColorMomentHash
public class ColorMomentHash extends ImgHashBase {

    protected ColorMomentHash(long addr) { super(addr); }


    //
    // C++: static Ptr_ColorMomentHash create()
    //

    //javadoc: ColorMomentHash::create()
    public static ColorMomentHash create()
    {
        
        ColorMomentHash retVal = new ColorMomentHash(create_0());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_ColorMomentHash create()
    private static native long create_0();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
