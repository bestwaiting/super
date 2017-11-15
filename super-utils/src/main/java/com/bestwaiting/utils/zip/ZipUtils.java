package com.bestwaiting.utils.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

/**
 * zip4j文件压缩/解压工具类
 * Created by bestwaiting on 17/5/10.
 */
public class ZipUtils {

    /**
     * 文件解压(Zip未加密)
     *
     * @param zipFile
     * @param path
     * @throws ZipException
     */
    public static void unzip(File zipFile, String path) throws ZipException {
        unzip(zipFile, path, null);
    }

    /**
     * 文件解压(Zip加密)
     *
     * @param zipFile
     * @param path
     * @param passwd
     * @throws ZipException
     */
    public static void unzip(File zipFile, String path, String passwd) throws ZipException {

        // 1.创建ZipFile指向磁盘上的.zip文件
        ZipFile zFile = new ZipFile(zipFile);

        // 2.设置文件名编码，
        zFile.setFileNameCharset("utf8");

        // 3.验证zip文件是否合法，包括文件是否存在、是否为zip文件、是否被损坏等
        if (!zFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法,可能被损坏.");
        }

        // 4.设置解压目录
        File destDir = new File(path);
        if (destDir.isDirectory() && !destDir.exists()) {
            destDir.mkdir();
        }

        // 5.设置密码
        if (zFile.isEncrypted()) {
            zFile.setPassword(passwd.toCharArray());
        }

        // 6.将文件抽出到解压目录(解压)
        zFile.extractAll(path);
    }

    /**
     * 压缩文件（未加密）
     * @param src
     * @param zipPathName
     * @param folder
     * @return
     * @throws Exception
     */
    public static File zip(List<File> src, File zipPathName, String folder) throws Exception {
        return zip(src, zipPathName, folder, null);
    }

    /**
     * 压缩文件（加密）
     *
     * @param src         要压缩的文件或文件夹路径
     * @param zipPathName 压缩文件存放名称的全路径/ 举例: /home/baidu/20170109/trans_return_20170109.zip
     * @param folder      zip文件里面第一级路径的名字,比如trans_return_20170109[跟zip文件同名也可不同名]//为空表示不创建路径
     * @param passwd      压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public static File zip(List<File> src, File zipPathName, String folder, String passwd) throws Exception {

        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        // 加密方式
        if (!StringUtils.isEmpty(passwd)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(passwd.toCharArray());
        }
        try {
            // 设置Zip文件名称
            ZipFile zipFile = new ZipFile(zipPathName);
            // 设置压缩目录
            if (!StringUtils.isEmpty(folder)) {

                File folderFile = new File(new String(zipPathName.getParent() + File.separator + folder));
                if (!folderFile.exists()) {
                    folderFile.mkdirs();
                }
                // 把src都扔到folder里面去
                for (File file : src) {
                    FileUtils.copyFileToDirectory(file, folderFile);
                }
                // 压缩文件夹.
                zipFile.addFolder(folderFile.getAbsolutePath(), parameters);
            } else {
                for (File file : src) {
                    zipFile.addFile(file, parameters);
                }
            }
            return zipPathName;
        } catch (Exception e) {
            throw e;
        }
    }
}
