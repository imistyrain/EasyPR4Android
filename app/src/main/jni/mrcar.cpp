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

jobjectArray detect_img(JNIEnv *env, jclass type,cv::Mat &img,bool draw=false){
    vector<CPlate> plateVec;
    cv::TickMeter tm;
    tm.start();
    int re = pp->plateRecognize(img, plateVec);
    tm.stop();
    LOGI("detect %f ms",tm.getTimeMilli());
    jobjectArray faceArgs = nullptr;
    jclass objectClass = env->FindClass("yanyu/com/mrcar/MRPlate");
    jmethodID faceClassInitID = (env)->GetMethodID(objectClass, "<init>", "()V");
    jfieldID lic = env->GetFieldID(objectClass, "lic","Ljava/lang/String;");
    jfieldID rect = env->GetFieldID(objectClass,"rect","Landroid/graphics/Rect;");
    jclass rectClass = env->FindClass("android/graphics/Rect");
    jmethodID rectClassInitID = (env)->GetMethodID(rectClass, "<init>", "()V");
    jfieldID rect_left = env->GetFieldID(rectClass, "left", "I");//获取x的签名
    jfieldID rect_top = env->GetFieldID(rectClass, "top", "I");//获取y的签名
    jfieldID rect_right = env->GetFieldID(rectClass, "right", "I");//获取width的签名
    jfieldID rect_bottom = env->GetFieldID(rectClass, "bottom", "I");//获取height的签名
    faceArgs = (env)->NewObjectArray(plateVec.size(), objectClass, 0);
    //LOGI("find %d plates", plateVec.size());
    for (int i = 0; i < plateVec.size(); i++) {
        CPlate plate = plateVec.at(i);
        string license = plate.getPlateStr();
        //LOGI("%d: %s",i,license.c_str());
        jobject newPlate = (env)->NewObject(objectClass, faceClassInitID);
        jobject newRect = (env)->NewObject(rectClass, rectClassInitID);
        (env)->SetIntField(newRect, rect_left, plate.getPlateMergeCharRect().x);
        (env)->SetIntField(newRect, rect_top, plate.getPlateMergeCharRect().y);
        (env)->SetIntField(newRect, rect_right, plate.getPlateMergeCharRect().x+plate.getPlateMergeCharRect().width);
        (env)->SetIntField(newRect, rect_bottom, plate.getPlateMergeCharRect().y+plate.getPlateMergeCharRect().height);
        (env)->SetObjectField(newPlate,lic,env->NewStringUTF(license.c_str()));
        (env)->SetObjectField(newPlate, rect, newRect);
        (env)->SetObjectArrayElement(faceArgs, i, newPlate);
        if (draw){
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
        }
    }
    return faceArgs;
}

//Java_yanyu_com_mrcar_MRCarUtil_plateRecognition
JNIEXPORT jobjectArray JNICALL plateRecognition(JNIEnv *env, jclass type, jlong matImg,jlong matResult) {
    cv::Mat &img=*(Mat *)matImg;
    cv::cvtColor(img, img, cv::COLOR_RGBA2BGR);
    cv::Mat &result=*(cv::Mat *)matResult;
    jobjectArray faceArgs = detect_img(env,type,img,true);
    result=img.clone();
    cvtColor(result,result, cv::COLOR_RGB2BGRA);
    return faceArgs;
}

JNIEXPORT jobjectArray JNICALL plateLive(JNIEnv *env, jclass type, jlong matImg) {
    cv::Mat &img=*(Mat *)matImg;
    if(img.channels() == 4){
        cv::cvtColor(img, img, cv::COLOR_RGBA2BGR);
    }
    return detect_img(env,type,img);
}

JNIEXPORT jobjectArray JNICALL plateNV21(JNIEnv *env, jclass type, jbyteArray data, jint height, jint width)
{
    jbyte * nv21 = env->GetByteArrayElements(data, 0);
    cv::Mat yuv(height+height/2, width, CV_8UC1, (uchar *)nv21);
    cv::Mat img(height, width, CV_8UC3);
    cv::cvtColor(yuv, img, COLOR_YUV2BGR_NV21);
    //img=img.t();
    cv::transpose(img, img);
    cv::flip(img, img, 1);
    env->ReleaseByteArrayElements(data,nv21, 0);
    return detect_img(env,type,img);
}

JNIEXPORT jint JNICALL
release(JNIEnv *env, jclass type)
{
    delete ptext;
    delete pp;
    return 0;
}