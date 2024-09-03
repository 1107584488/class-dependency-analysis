package com.lgw.github.util;

import com.lgw.github.constant.Constants;
import com.lgw.github.constant.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 打印工具类
 *
 * @author lianguowei <lianguowei@kuaishou.com>
 * Created on 2024-09-03
 */
public class PrintUtil {

    /**
     * 单个文件内容打印
     */
    public static void printSingleFileContent() {
        System.out.println("############################# 单个文件扫描打印：开始 ##############################################");
        System.out.println("扫描类：打印开始 ^^^^^^^^^^^^^^^^^^^^ " + Variable.indexScanClassName + " ^^^^^^^^^^^^^^^^^^^^ 扫描类：打印开始");
        for (Map.Entry<String, HashMap<String, String>> entry : Constants.MODULE_CLASS_MAP.entrySet()) {
            String moduleName = entry.getKey();
            System.out.println("依赖模块：打印开始 ====================== " + moduleName + " ====================== 依赖模块：打印开始");
            HashMap<String, String> classNameMap = entry.getValue();
            for (Map.Entry<String, String> classEntry : classNameMap.entrySet()) {
                String className = classEntry.getKey();
                System.out.println("依赖类名：打印开始 ********************* " + className + " ********************* 依赖类名：打印开始");
                if (Constants.CLASS_MAP.containsKey(className) && Constants.METHOD_MAP.containsKey(Constants.CLASS_MAP.get(className))) {
                    List<String> methodList = Constants.METHOD_MAP.get(Constants.CLASS_MAP.get(className));
                    methodList.forEach(System.out::println);
                }
                System.out.println("依赖类名：打印结束 ********************* " + className + " ********************* 依赖类名：打印结束");
            }

            System.out.println("依赖模块：打印结束 ====================== " + moduleName + " ====================== 依赖模块：打印结束");
            System.out.println();
        }
        System.out.println("扫描类：打印开始 ^^^^^^^^^^^^^^^^^^^^ " + Variable.indexScanClassName + " ^^^^^^^^^^^^^^^^^^^^ 扫描类：打印结束");
        System.out.println("############################# 单个文件扫描打印：结束 ##############################################");
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
