package com.jaygoaler.pitools.service.impl;

import com.jaygoaler.pitools.utils.Encrypt;
import com.jaygoaler.pitools.vo.FileInfo;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@SpringBootTest
class FileSystemServiceImplTest {

    @Value("${pi.fileSavePath}")
    private String fileSavePath;

    @Test
    void test() throws FileNotFoundException {
        List<FileInfo> fileInfos = createFileTree(fileSavePath,null);
        System.out.println(fileInfos.size());
    }



    private List<FileInfo> createFileTree(String filepath,String parentid) throws FileNotFoundException {
        List<FileInfo> list = Lists.newArrayList();
        File file = new File(filepath);
        //1.判断文件
        if(!file.exists()){
            throw new FileNotFoundException("文件不存在");
        }
        String name = file.getName();
        String path = file.getAbsolutePath();
        FileInfo tree = new FileInfo(Encrypt.getMD5Code(path).toLowerCase(),name,path,parentid);
        list.add(tree);
        //3.获取文件夹路径下面的所有文件递归调用；
        if(file.isDirectory()){
            String[] fileList = file.list();
            assert fileList != null;
            for (String s : fileList) {
                //根据当前文件夹，拼接其下文文件形成新的路径
                String newFilePath = path + "\\" + s;
                list.addAll(createFileTree(newFilePath, tree.getId()));
            }
        }
        return list;
    }
}