
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.utils.Converters;

public class Dnn {

    public static final int
            DNN_BACKEND_DEFAULT = 0,
            DNN_BACKEND_HALIDE = 1,
            DNN_TARGET_CPU = 0,
            DNN_TARGET_OPENCL = 1;


    //
    // C++:  Mat blobFromImage(Mat image, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = true, bool crop = true)
    //

    //javadoc: blobFromImage(image, scalefactor, size, mean, swapRB, crop)
    public static Mat blobFromImage(Mat image, double scalefactor, Size size, Scalar mean, boolean swapRB, boolean crop)
    {
        
        Mat retVal = new Mat(blobFromImage_0(image.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, crop));
        
        return retVal;
    }

    //javadoc: blobFromImage(image)
    public static Mat blobFromImage(Mat image)
    {
        
        Mat retVal = new Mat(blobFromImage_1(image.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat blobFromImages(vector_Mat images, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = true, bool crop = true)
    //

    //javadoc: blobFromImages(images, scalefactor, size, mean, swapRB, crop)
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size, Scalar mean, boolean swapRB, boolean crop)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat retVal = new Mat(blobFromImages_0(images_mat.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, crop));
        
        return retVal;
    }

    //javadoc: blobFromImages(images)
    public static Mat blobFromImages(List<Mat> images)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat retVal = new Mat(blobFromImages_1(images_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat readTorchBlob(String filename, bool isBinary = true)
    //

    //javadoc: readTorchBlob(filename, isBinary)
    public static Mat readTorchBlob(String filename, boolean isBinary)
    {
        
        Mat retVal = new Mat(readTorchBlob_0(filename, isBinary));
        
        return retVal;
    }

    //javadoc: readTorchBlob(filename)
    public static Mat readTorchBlob(String filename)
    {
        
        Mat retVal = new Mat(readTorchBlob_1(filename));
        
        return retVal;
    }


    //
    // C++:  Net readNetFromCaffe(String prototxt, String caffeModel = String())
    //

    //javadoc: readNetFromCaffe(prototxt, caffeModel)
    public static Net readNetFromCaffe(String prototxt, String caffeModel)
    {
        
        Net retVal = new Net(readNetFromCaffe_0(prototxt, caffeModel));
        
        return retVal;
    }

    //javadoc: readNetFromCaffe(prototxt)
    public static Net readNetFromCaffe(String prototxt)
    {
        
        Net retVal = new Net(readNetFromCaffe_1(prototxt));
        
        return retVal;
    }


    //
    // C++:  Net readNetFromDarknet(String cfgFile, String darknetModel = String())
    //

    //javadoc: readNetFromDarknet(cfgFile, darknetModel)
    public static Net readNetFromDarknet(String cfgFile, String darknetModel)
    {
        
        Net retVal = new Net(readNetFromDarknet_0(cfgFile, darknetModel));
        
        return retVal;
    }

    //javadoc: readNetFromDarknet(cfgFile)
    public static Net readNetFromDarknet(String cfgFile)
    {
        
        Net retVal = new Net(readNetFromDarknet_1(cfgFile));
        
        return retVal;
    }


    //
    // C++:  Net readNetFromTensorflow(String model, String config = String())
    //

    //javadoc: readNetFromTensorflow(model, config)
    public static Net readNetFromTensorflow(String model, String config)
    {
        
        Net retVal = new Net(readNetFromTensorflow_0(model, config));
        
        return retVal;
    }

    //javadoc: readNetFromTensorflow(model)
    public static Net readNetFromTensorflow(String model)
    {
        
        Net retVal = new Net(readNetFromTensorflow_1(model));
        
        return retVal;
    }


    //
    // C++:  Net readNetFromTorch(String model, bool isBinary = true)
    //

    //javadoc: readNetFromTorch(model, isBinary)
    public static Net readNetFromTorch(String model, boolean isBinary)
    {
        
        Net retVal = new Net(readNetFromTorch_0(model, isBinary));
        
        return retVal;
    }

    //javadoc: readNetFromTorch(model)
    public static Net readNetFromTorch(String model)
    {
        
        Net retVal = new Net(readNetFromTorch_1(model));
        
        return retVal;
    }


    //
    // C++:  Ptr_Importer createCaffeImporter(String prototxt, String caffeModel = String())
    //

    //javadoc: createCaffeImporter(prototxt, caffeModel)
    public static Importer createCaffeImporter(String prototxt, String caffeModel)
    {
        
        Importer retVal = new Importer(createCaffeImporter_0(prototxt, caffeModel));
        
        return retVal;
    }

    //javadoc: createCaffeImporter(prototxt)
    public static Importer createCaffeImporter(String prototxt)
    {
        
        Importer retVal = new Importer(createCaffeImporter_1(prototxt));
        
        return retVal;
    }


    //
    // C++:  Ptr_Importer createTensorflowImporter(String model)
    //

    //javadoc: createTensorflowImporter(model)
    public static Importer createTensorflowImporter(String model)
    {
        
        Importer retVal = new Importer(createTensorflowImporter_0(model));
        
        return retVal;
    }


    //
    // C++:  Ptr_Importer createTorchImporter(String filename, bool isBinary = true)
    //

    //javadoc: createTorchImporter(filename, isBinary)
    public static Importer createTorchImporter(String filename, boolean isBinary)
    {
        
        Importer retVal = new Importer(createTorchImporter_0(filename, isBinary));
        
        return retVal;
    }

    //javadoc: createTorchImporter(filename)
    public static Importer createTorchImporter(String filename)
    {
        
        Importer retVal = new Importer(createTorchImporter_1(filename));
        
        return retVal;
    }


    //
    // C++:  void shrinkCaffeModel(String src, String dst)
    //

    //javadoc: shrinkCaffeModel(src, dst)
    public static void shrinkCaffeModel(String src, String dst)
    {
        
        shrinkCaffeModel_0(src, dst);
        
        return;
    }




    // C++:  Mat blobFromImage(Mat image, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = true, bool crop = true)
    private static native long blobFromImage_0(long image_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, boolean crop);
    private static native long blobFromImage_1(long image_nativeObj);

    // C++:  Mat blobFromImages(vector_Mat images, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = true, bool crop = true)
    private static native long blobFromImages_0(long images_mat_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, boolean crop);
    private static native long blobFromImages_1(long images_mat_nativeObj);

    // C++:  Mat readTorchBlob(String filename, bool isBinary = true)
    private static native long readTorchBlob_0(String filename, boolean isBinary);
    private static native long readTorchBlob_1(String filename);

    // C++:  Net readNetFromCaffe(String prototxt, String caffeModel = String())
    private static native long readNetFromCaffe_0(String prototxt, String caffeModel);
    private static native long readNetFromCaffe_1(String prototxt);

    // C++:  Net readNetFromDarknet(String cfgFile, String darknetModel = String())
    private static native long readNetFromDarknet_0(String cfgFile, String darknetModel);
    private static native long readNetFromDarknet_1(String cfgFile);

    // C++:  Net readNetFromTensorflow(String model, String config = String())
    private static native long readNetFromTensorflow_0(String model, String config);
    private static native long readNetFromTensorflow_1(String model);

    // C++:  Net readNetFromTorch(String model, bool isBinary = true)
    private static native long readNetFromTorch_0(String model, boolean isBinary);
    private static native long readNetFromTorch_1(String model);

    // C++:  Ptr_Importer createCaffeImporter(String prototxt, String caffeModel = String())
    private static native long createCaffeImporter_0(String prototxt, String caffeModel);
    private static native long createCaffeImporter_1(String prototxt);

    // C++:  Ptr_Importer createTensorflowImporter(String model)
    private static native long createTensorflowImporter_0(String model);

    // C++:  Ptr_Importer createTorchImporter(String filename, bool isBinary = true)
    private static native long createTorchImporter_0(String filename, boolean isBinary);
    private static native long createTorchImporter_1(String filename);

    // C++:  void shrinkCaffeModel(String src, String dst)
    private static native void shrinkCaffeModel_0(String src, String dst);

}
