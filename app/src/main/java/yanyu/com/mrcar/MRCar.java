package yanyu.com.mrcar;

/**
 * Created by yanyu on 2016/8/19.
 */
public class MRCar {
    //初始化，modeldir为SD卡上模型路径
    public static native boolean init(String modeldir);
    //车牌检测
    public static native MRPlate[] plateRecognition(long matImg,long matResult);
    public static native MRPlate[] plateLive(long matImg);
    public static native MRPlate[] plateNV21(byte[]img, int height, int width);
    //释放库
    public static native int release();
}
