package com.lgw.github;

import static com.lgw.github.util.ScanUtil.getScanFolderClassName;
import static com.lgw.github.util.ScanUtil.scanDependencyModuleClassPath;

import java.util.LinkedHashMap;
import java.util.Map;

import com.lgw.github.constant.Variable;
import com.lgw.github.util.ExcelUtil;
import com.lgw.github.util.FileUtil;
import com.lgw.github.util.PrintUtil;
import com.lgw.github.util.ScanUtil;

/**
 * @author lianguowei <lianguowei@kuaishou.com>
 * Created on 2024-12-12
 */
public class AnalyzeLocalModuleDependencyStarter {

    public static void main(String[] args) {
        // 键入信息
        // 本地excel输出路径
        String outFilePath = "/Users/lianguowei/Documents/草稿/依赖扫描2";
        // 本地excel文件名，不带.xlsx后缀，sheet仅一列，同名
        String excelFileName = "CreativeSDK";

        // 开始执行前删除原有的excel文件
        FileUtil.deleteFile(outFilePath, excelFileName);

        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        // key - 模块名
        // value - 扫描的起始包路径
        Map<String, String> dependencyModuleMap = new LinkedHashMap<>() {{
            put("ShareSDK", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-share-sdk");
            put("CommonSDK", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-common-sdk");
            put("SettleSDK", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-settle-sdk");
            put("OrderSDK", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-order-sdk");
            put("AccountSDK", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-account-sdk");
            put("ResourceSDK", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-resource-sdk");

            put("ShareComponent", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-share-component");
            put("CommonComponent", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-common-component");
            put("SettleComponent", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-settle-component");
            put("OrderComponent", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-order-component");
            put("AccountComponent", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-account-component");
            put("ResourceComponent", "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-resource-component");
        }};
        // 扫描出当前模块下的所有类路径
        scanDependencyModuleClassPath(dependencyModuleMap);

        // 步骤2:输入待扫描的模块和路径（即需要分析的模块），自动化嵌套扫描类全路径
        String scanModuleName = "CreativeSDK";
        String modulePath = "/Users/lianguowei/Documents/soft/ideaProject/依赖分析/xxx-ad-brand-platform/ad-brand-creative-sdk";
        Map<String, String> fileNamePathMap = getScanFolderClassName(modulePath);
        if (!fileNamePathMap.isEmpty()) {
            for (Map.Entry<String, String> fileNamePathMapEntry : fileNamePathMap.entrySet()) {
                String fileName = fileNamePathMapEntry.getKey();
                String filePath = fileNamePathMapEntry.getValue();
                // 获取扫描的类名
                Variable.indexScanClassName = fileName;
                // 扫描类文件
                ScanUtil.scanAndWriteExcel(filePath, scanModuleName, fileName, 2);
                // 打印单个文件内容至控制台，debug使用 或 与excel数据内容对比使用
                PrintUtil.printSingleFileContent();
                // 数据写入excel
                ExcelUtil.writeExcelForDependency(outFilePath, excelFileName);
            }
        }
    }
}
