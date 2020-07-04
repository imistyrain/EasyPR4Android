#include "easypr.h"
#include "CvText.h"
#include "mrcar.h"
#include "util.h"
using namespace easypr;

std::string easypr::modeldir="/sdcard/mrcar";

static void drawRotatedRects(cv::Mat &img,cv::RotatedRect rr,cv::Scalar color=cv::Scalar(255,0,0),int tickness=3){
    cv::Point2f vertices2f[4];
    rr.points(vertices2f);
    cv::Point vertices[4];
    for(int i = 0; i < 4; ++i){
        vertices[i] = vertices2f[i];
    }
    //cv::fillConvexPoly(img,vertices,4,color);
    cv::line(img,vertices[0],vertices[1],color,tickness);
    cv::line(img,vertices[1],vertices[2],color,tickness);
    cv::line(img,vertices[2],vertices[3],color,tickness);
    cv::line(img,vertices[3],vertices[0],color,tickness);
}
CvText *ptext;
CPlateRecognize *pp= nullptr;

JNIEXPORT jboolean JNICALL
init(JNIEnv *env, jclass type,jstring dir)
{
    const char *sd_dir = env->GetStringUTFChars(dir, 0);
    modeldir=sd_dir;
    ptext=new CvText((modeldir+"/simhei.ttf").c_str());
    pp=new CPlateRecognize();
    pp->setResultShow(false);
    pp->setDetectType(PR_DETECT_CMSER);
    env->ReleaseStringUTFChars(dir, sd_dir);
    return true;
}

//Java_yanyu_com_mrcar_MRCarUtil_plateRecognition
JNIEXPORT jstring JNICALL plateRecognition(JNIEnv *env, jclass type, jlong matImg,jlong matResult) {
    cv::Mat &img=*(Mat *)matImg;
    cv::cvtColor(img, img, cv::COLOR_RGBA2BGR);
    cv::Mat &result=*(cv::Mat *)matResult;
    string license;
    vector<CPlate> plateVec;
    cv::TickMeter tm;
    tm.start();
    int re= pp->plateRecognize(img, plateVec);
    tm.stop();
    LOGI("cost %f ms",tm.getTimeMilli());
    for (int i = 0; i < plateVec.size(); i++)
    {
        CPlate plate = plateVec.at(i);
        license = plate.getPlateStr();
        drawRotatedRects(img,plate.getPlatePos());
        auto pp=plate.getPlatePos();
        cv::Point pt=pp.center;
        pt.x-=100;
        if(pt.y>=60)
            pt.y-=60;
        else
            pt.y=60;
        ptext->putText(img, plate.getPlateStr(), pt, cv::Scalar(0,0,255));
        cv::putText(img,to_string(tm.getTimeMilli())+"ms",cv::Point(0,60),1,1,cv::Scalar(0,0,255));
        break;
    }
    //string resultpath="/sdcard/mrcar/result.png";
    //cv::imwrite(resultpath,img);
    result=img.clone();
    cvtColor(result,result, cv::COLOR_RGB2BGRA);
    return env->NewStringUTF(license.c_str());
}
JNIEXPORT jstring JNICALL plateLive(JNIEnv *env, jclass type, jlong matImg) {
    cv::Mat &img=*(Mat *)matImg;
    if(img.channels() == 4){
        cv::cvtColor(img, img, cv::COLOR_RGBA2BGR);
    }
//    int width=img.cols;
//    int height=img.rows;
//    LOGI("w,h: %d,%d",width,height);
    string license;
    vector<CPlate> plateVec;
    cv::TickMeter tm;
    tm.start();
    int re = pp->plateRecognize(img, plateVec);
    tm.stop();
    for (int i = 0; i < plateVec.size(); i++) {
        CPlate plate = plateVec.at(i);
        license = plate.getPlateStr();
        break;
    }
    LOGI("cost %f ms",tm.getTimeMilli());
    return env->NewStringUTF(license.c_str());
}

JNIEXPORT jstring JNICALL plateNV21(JNIEnv *env, jclass type, jbyteArray data, jint height, jint width)
{
    jbyte * nv21 = env->GetByteArrayElements(data, 0);
    cv::Mat yuv(height+height/2, width, CV_8UC1, (uchar *)nv21);
    cv::Mat img(height, width, CV_8UC3);
    cv::cvtColor(yuv, img, COLOR_YUV2BGR_NV21);
    //img=img.t();
    cv::transpose(img, img);
    cv::flip(img, img, 1);
    env->ReleaseByteArrayElements(data,nv21, 0);
    //cv::imwrite("sdcard/mrcar/rgb.png",img);
    string license;
    vector<CPlate> plateVec;
    cv::TickMeter tm;
    tm.start();
    int re = pp->plateRecognize(img, plateVec);
    tm.stop();
    for (int i = 0; i < plateVec.size(); i++) {
        CPlate plate = plateVec.at(i);
        license = plate.getPlateStr();
        break;
    }
    LOGI("cost %f ms",tm.getTimeMilli());
    return env->NewStringUTF(license.c_str());
}


JNIEXPORT jint JNICALL
release(JNIEnv *env, jclass type)
{
    delete ptext;
    delete pp;
    return 0;
}