package com.luoyu.java.zip;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.StopWatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *  说明：
 * （1）可以压缩文件，也可以压缩文件夹
 * （2）同时支持压缩多级文件夹，工具内部做了递归处理
 * （3）碰到空的文件夹，也可以压缩
 * （4）可以选择是否保留原来的目录结构，如果不保留，所有文件跑压缩包根目录去了，且空文件夹直接舍弃。注意：如果不保留文件原来目录结构，在碰到文件名相同的文件时，会压缩失败。
 * （5）代码中提供了2个压缩文件的方法，一个的输入参数为文件夹路径，一个为文件列表，可根据实际需求选择方法。
 *  注意：
 * （1）支持选择是否保留原来的文件目录结构，如果不保留，那么空文件夹直接不用处理。
 * （2）碰到空文件夹时，如果需要保留目录结构，则直接添加个ZipEntry就可以了，不过就是这个entry的名字后面需要带上一斜杠（/）表示这个是目录。
 * （3）递归时，不需要把zip输出流关闭，zip输出流的关闭应该是在调用完递归方法后面关闭
 * （4）递归时，如果是个文件夹且需要保留目录结构，那么在调用方法压缩他的子文件时，需要把文件夹的名字加一斜杠给添加到子文件名字前面，这样压缩后才有多级目录。
 * @author 	jhx
 * @date 	2021年2月121日 下午7:16:08
 * @version v1.0
 */
@Slf4j
public class ZipUtils {

    private static final int  BUFFER_SIZE = 2 * 1024;

    /**
     * 单个
     * @param srcDir 要压缩文件路径
     * @param out    压缩后文件输出流
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     * 							false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
            throws Exception{
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            stopWatch.stop();
            log.info("压缩完成，耗时：{}。", (double) stopWatch.getTime()/1000 + "s");
        } catch (Exception e) {
            throw new Exception("压缩失败：" + e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (Exception e) {
                    log.error("压缩失败：" + e);
                }
            }
        }
    }

    /**
     * 多个
     * @param srcFiles 需要压缩的文件列表
     * @param out 	        压缩后文件输出流
     */
    public static void toZip(List<File> srcFiles , OutputStream out)throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1){
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            stopWatch.stop();
            log.info("压缩完成，耗时：{}。", (double) stopWatch.getTime()/1000 + "s");
        } catch (Exception e) {
            throw new Exception("压缩失败：" + e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (Exception e) {
                    log.error("压缩失败：" + e);
                }
            }
        }
    }

    /**
     * 递归压缩方法
     * @param sourceFile 源文件
     * @param zos		 zip输出流
     * @param name		 压缩后的名称
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     * 							false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        log.info("测试开始!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        // 压缩后的文件
        FileOutputStream fileOutputStream1 = new FileOutputStream("/Users/luoyu/Downloads/test.zip");
        ZipUtils.toZip("/Users/luoyu/Downloads/日报内容.txt", fileOutputStream1,true);

        List<File> fileList = new ArrayList<>();
        fileList.add(new File("/Users/luoyu/Downloads/日报内容1.txt"));
        fileList.add(new File("/Users/luoyu/Downloads/日报内容2.txt"));
        FileOutputStream fileOutputStream2 = new FileOutputStream("/Users/luoyu/Downloads/test.zip");
        ZipUtils.toZip(fileList, fileOutputStream2);
        log.info("测试结束!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

}
