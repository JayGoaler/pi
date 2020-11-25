package com.jaygoaler.pitools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
class PitoolsApplicationTests {

    private String basePath = "E:\\TEMP\\pitest";

    @Test
    void contextLoads() {
        String fileName = "测试文件";
        String path = getPath(fileName);
        System.out.println(path);
        System.out.println(getPath("sen_yang-SanMuYuanBook-master"));
        System.out.println(getPath("天河区人民检察院2019年信息化建设项目信息安全测评整改建议--V1.0(整改回复-赛威讯还没完成）"));
        System.out.println(getPath("idea学习资料"));
        System.out.println(getPath("idea资料"));
    }

    /**
     * 根据hash算法，确定存放目录
     * @param fileName
     * @return
     */
    private String getPath(String fileName){
        String separate = File.separator;
        try{
            //得到文件名的hashCode的值，取二进制后16位，文件夹深度为4
            int hashcode = fileName.hashCode();
            String dir_suffix = separate+(hashcode & 0xf)+separate+((hashcode & 0xf0) >> 4)+separate+((hashcode & 0xf00) >> 8)+separate+((hashcode & 0xf000) >> 12);
            //构造新的保存目录
            String dir = basePath + dir_suffix;
            //File既可以代表文件也可以代表目录
            File file = new File(dir);
            //如果目录不存在
            if (!file.exists()) {
                //创建目录
                file.mkdirs();
            }
            return dir;
        }catch (Exception e){
            e.printStackTrace();
            return basePath+"/error";
        }

    }

    /**
     * 保存文件
     * @return 返回访问链接
     */
    public String save(String filename, String ext, String operator, InputStream in) throws IOException {
        FileOutputStream fos = null;
        try{
            String path = this.getPath(filename);
            fos = new FileOutputStream(path+"/"+filename+"."+ext);
            byte [] buffer = new byte[1024];
            int length = 0;
            while ((length = in.read(buffer))>0){
                fos.write(buffer,0,length);
            }
            return path+"/"+filename+"."+ext;
        }catch (Exception e){
            e.printStackTrace();
            return "ERROR";
        }finally {
            if(null != in){
                in.close();
            }
            if(null != fos){
                fos.close();
            }
        }
    }

    /**
     * 根据文件名及其扩展名，返回一个文件链接
     * @param filename
     * @param ext
     * @return
     */
    public String getFileURI(String filename,String ext){
        int hashcode = filename.hashCode();
        String dir_suffix = "/"+(hashcode & 0xf)+"/"+((hashcode & 0xf0) >> 4)+"/"+((hashcode & 0xf00) >> 8)+"/"+((hashcode & 0xf000) >> 12);
        String realPath =  this.getPath(filename)+"/"+filename+"."+ext;
        File file = new File(realPath);
        if(file.exists()){
            return "http://127.0.0.1/file"+dir_suffix+"/"+filename+"."+ext;
        }else return "NONE";
    }

}
