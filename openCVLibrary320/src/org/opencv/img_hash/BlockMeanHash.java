
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.img_hash;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.utils.Converters;

// C++: class BlockMeanHash
//javadoc: BlockMeanHash
public class BlockMeanHash extends ImgHashBase {

    protected BlockMeanHash(long addr) { super(addr); }


    //
    // C++: static Ptr_BlockMeanHash create(int mode = BLOCK_MEAN_HASH_MODE_0)
    //

    //javadoc: BlockMeanHash::create(mode)
    public static BlockMeanHash create(int mode)
    {
        
        BlockMeanHash retVal = new BlockMeanHash(create_0(mode));
        
        return retVal;
    }

    //javadoc: BlockMeanHash::create()
    public static BlockMeanHash create()
    {
        
        BlockMeanHash retVal = new BlockMeanHash(create_1());
        
        return retVal;
    }


    //
    // C++:  vector_double getMean()
    //

    //javadoc: BlockMeanHash::getMean()
    public  MatOfDouble getMean()
    {
        
        MatOfDouble retVal = MatOfDouble.fromNativeAddr(getMean_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  void setMode(int mode)
    //

    //javadoc: BlockMeanHash::setMode(mode)
    public  void setMode(int mode)
    {
        
        setMode_0(nativeObj, mode);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_BlockMeanHash create(int mode = BLOCK_MEAN_HASH_MODE_0)
    private static native long create_0(int mode);
    private static native long create_1();

    // C++:  vector_double getMean()
    private static native long getMean_0(long nativeObj);

    // C++:  void setMode(int mode)
    private static native void setMode_0(long nativeObj, int mode);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
