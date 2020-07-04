
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.img_hash;



// C++: class AverageHash
//javadoc: AverageHash
public class AverageHash extends ImgHashBase {

    protected AverageHash(long addr) { super(addr); }


    //
    // C++: static Ptr_AverageHash create()
    //

    //javadoc: AverageHash::create()
    public static AverageHash create()
    {
        
        AverageHash retVal = new AverageHash(create_0());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_AverageHash create()
    private static native long create_0();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
