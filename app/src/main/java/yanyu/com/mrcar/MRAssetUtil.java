package yanyu.com.mrcar;

import android.content.Context;
import android.content.res.Resources;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by yanyu on 2016/8/19.
 */

public class MRAssetUtil {
    //将dir中的path文件拷贝到SD卡上
    public static void CopyOneFile(String path,String dir, Resources Resid){
        File file=new File(String.format(dir+"/"+path));
        if(!file.exists())
        {
            try {
                copyFileFromAssetsToSDCard(path,dir+"/"+ path,Resid);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
    //将单个文件拷贝到SD卡上
    private static void copyFileFromAssetsToSDCard(String resname, String sdpath, Resources Resid) throws Throwable {
        InputStream is =Resid.getAssets().open(resname);
        OutputStream os = new FileOutputStream(sdpath);
        byte data[] = new byte[1024];
        int len;
        while ((len = is.read(data)) > 0) {
            os.write(data, 0, len);
        }
        is.close();
        os.close();
    }
    //将assetDir下的所有文件拷贝到SD卡上
    public static void CopyAssets(Context context,String assetDir, String dir) {
        String[] files;
        try {
            files =context.getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {
            }
        }

        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        CopyAssets(context,fileName, dir + fileName + "/");
                    } else {
                        CopyAssets(context,assetDir + "/" + fileName, dir + "/" + fileName + "/");
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
//                if (outFile.exists())
//                    continue;
                InputStream in = null;
                if (0 != assetDir.length())
                    in = context.getAssets().open(assetDir + "/" + fileName);
                else
                    in = context.getAssets().open(fileName);
                OutputStream out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
