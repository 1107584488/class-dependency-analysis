package com.lgw.github;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lianguowei <lianguowei@kuaishou.com>
 * Created on 2024-12-12
 */
public class AnalyzeLocalModuleDependencyStarter {


    public static void main(String[] args) {
        // 键入信息
        // 本地excel输出路径
        String outFilePath = "/Users/lianguowei/Documents/草稿";
        // 本地excel文件名，不带.xlsx后缀，sheet仅一列，同名
        String excelFileName = "CreativeComponent";
        // 获取需扫描模块的所有全路径类名，例如：/User/lianguowei/xxx.java
        String pathName = "/Users/lianguowei/Documents/soft/ideaProject/class-dependency-analysis/class-dependency-analysis-spring";
        List<String> list = getScanFolderClassName(pathName);
        // 扫描的依赖模块模块类去重，以免万一重复
        list = list.stream().distinct().collect(Collectors.toList());
        for (String scanClassPath : list) {

        }

    }



    public static List<String> getScanFolderClassName(String pathName) {
        File folder = new File(pathName);
        List<String> list = new ArrayList<>();
        scanFolder(folder, list);
        return list;
    }

    public static void scanFolder(File folder, List<String> list) {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanFolder(file, list);
            } else {
                if (file.getName().endsWith(".java")) {
                    String fileName = file.getPath();
                    list.add(fileName);
                }
            }
        }
    }
}
