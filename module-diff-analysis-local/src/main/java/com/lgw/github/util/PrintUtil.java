package com.lgw.github.util;

import java.util.Map;

import com.lgw.github.constant.Constants;

/**
 * 打印工具类
 *
 * @author lianguowei <lianguowei@kuaishou.com>
 * Created on 2024-09-03
 */
public class PrintUtil {
    /**
     * 单个分支模块独有类打印
     * @param featureModuleName
     * @param map
     */
    public static void printSingleFeatureClassInfo(String featureModuleName, Map<String, String> map) {
        System.out.println("############################# 单个分支独有扫描打印：开始 ##############################################");
        System.out.println("打印开始：独有分支模块^^^^^^^^^^^^^^^^^^^^^^^ " + featureModuleName + " ^^^^^^^^^^^^^^^^^^^^^^打印开始：独有分支模块");
        int i = 1;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("顺序号：" + (i++) + "，类名：" + entry.getKey() + "，类路径：" + entry.getValue());
        }
        System.out.println("打印结束：独有分支模块^^^^^^^^^^^^^^^^^^^^^^^ " + featureModuleName + " ^^^^^^^^^^^^^^^^^^^^^^打印结束：独有分支模块");
        System.out.println("############################# 单个分支独有扫描打印：结束 ##############################################");
    }

    /**
     * 两个分支类文件存在差异的类信息打印
     */
    public static void printSingleFeatureClassInfo() {
        System.out.println("############################# 两个分支存在差异的类文件信息打印：开始 ##############################################");
        int i = 1;
        for (Map.Entry<String, String> entry : Constants.MASTER_TARGET_CLASS_DIFF_MAP.entrySet()) {
            System.out.println("顺序号：" + (i++) + "，类名：" + entry.getKey() + "，类路径：" + entry.getValue());
        }
        System.out.println("############################# 两个分支存在差异的类文件信息打印：结束 ##############################################");
    }
}
