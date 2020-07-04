
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.img_hash;



// C++: class PHash
//javadoc: PHash
public class PHash extends ImgHashBase {

    protected PHash(long addr) { super(addr); }


    //
    // C++: static Ptr_PHash create()
    //

    //javadoc: PHash::create()
    public static PHash create()
    {
        
        PHash retVal = new PHash(create_0());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_PHash create()
    private static native long create_0();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
