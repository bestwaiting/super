package com.bestwaiting.oss.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Paths;

/**
 * 七牛云存储操作工具类
 * https://developer.qiniu.com/kodo/sdk/1239/java#6
 * Created by bestwaiting on 17/11/1.
 */
public class QiniuOSSUtils {

    public void uploadSimple() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "your access key";
        String secretKey = "your secret key";
        String bucket = "your bucket name";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "/home/qiniu/test.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }

    public void uploadReGet(String accessKey, String secretKey, String bucket) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释

        //...生成上传凭证，然后准备上传

        String localFilePath = "/home/qiniu/test.mp4";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        String localTempDir = Paths.get(System.getenv("java.io.tmpdir"), bucket).toString();
        try {
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            try {
                Response response = uploadManager.put(localFilePath, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void downloadPublic() throws UnsupportedEncodingException {
        String fileName = "七牛/云存储/qiniu.jpg";
        String domainOfBucket = "http://devtools.qiniu.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        System.out.println(finalUrl);

    }

    public void downloadPrivate() throws UnsupportedEncodingException {
        String fileName = "七牛/云存储/qiniu.jpg";
        String domainOfBucket = "http://devtools.qiniu.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);

        String accessKey = "your access key";
        String secretKey = "your secret key";
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);

        System.out.println(finalUrl);

    }

    public void validateCallback() {
        String accessKey = "your access key";
        String secretKey = "your secret key";

        /*
        * 这两个参数就是在定义PutPolicy参数时指定的内容
        */

        //回调地址
        String callbackUrl = "http://api.example.com/qiniu/callback";
        //定义回调内容的组织格式，与上传策略中的callbackBodyType要保持一致
        //String callbackBodyType = "application/x-www-form-urlencoded"; //回调鉴权的签名包括请求内容callbackBody
        String callbackBodyType = "application/json";//回调鉴权的签名不包括请求内容

        /**
         * 这两个参数根据实际所使用的HTTP框架进行获取
         */
        //通过获取请求的HTTP头部Authorization字段获得
        String callbackAuthHeader = "xxx";
        //通过读取回调POST请求体获得，不要设置为null
        byte[] callbackBody = null;


        Auth auth = Auth.create(accessKey, secretKey);
        //检查是否为七牛合法的回调请求
        boolean validCallback = auth.isValidCallback(callbackAuthHeader, callbackUrl, callbackBody, callbackBodyType);
        if (validCallback) {
            //继续处理其他业务逻辑
        } else {
            //这是哪里的请求，被劫持，篡改了吧？
        }

    }

    public static void main(String[] args) {

    }
}
