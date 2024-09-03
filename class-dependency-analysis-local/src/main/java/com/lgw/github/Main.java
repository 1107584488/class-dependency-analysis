package com.lgw.github;

import com.lgw.github.constant.Variable;
import com.lgw.github.util.ExcelUtil;
import com.lgw.github.util.FileUtil;
import com.lgw.github.util.PrintUtil;
import com.lgw.github.util.ScanUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author lianguowei <lianguowei>
 * Created on 2024-09-03
 */
public class Main {
    public static void main(String[] args) {
        // 键入信息
        // 本地excel输出路径
        String outFilePath = "/Users/lianguowei/Documents/草稿";
        // 本地excel文件名，不带.xlsx后缀，sheet仅一列，同名
        String excelFileName = "CreativeDomain";
        // 域命名规范前缀
        String moduleRule = "com.xxx.ad.brand.platform";
        // 待扫描输出excel内容的 扫描类文件路径
        List<String> scanPathList = Arrays.asList(
                "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/creative/CreativeController.java"
                , "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/gray/UnitAndCreativeGrayController.java"
                , "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/tube/TubeController.java"
                , "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/unit/UnitController.java"
                , "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/AdBrandCampaignController.java"
                , "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/AdBrandCommentController.java"
                , "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/AdBrandCreativeController.java"
                , "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/AdBrandPhotoController.java"
                , "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/AdBrandTargetController.java"
                , "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/AdBrandUnitController.java"
                , "/Users/lianguowei/Documents/soft/ideaProject/xxx-ad-brand-platform/xxx-ad-brand-platform-api/src/main/java/com/xxx/ad/brand/platform/controller/AppController.java"
        );

        // 开始执行前删除原有的excel文件
        FileUtil.deleteFile(outFilePath, excelFileName);

        for (String scanClassPath : scanPathList) {
            String[] classNameTemp = scanClassPath.replace(".java", "").split("/");
            // 获取扫描的类名
            Variable.indexScanClassName = classNameTemp[classNameTemp.length - 1];
            // 扫描类文件
            ScanUtil.scanAndWriteExcel(scanClassPath, moduleRule);
            // 打印单个文件内容至控制台，debug使用 或 与excel数据内容对比使用
            PrintUtil.printSingleFileContent();
            // 数据写入excel
            ExcelUtil.writeExcelForDependency(outFilePath, excelFileName);
            // 行号+1，为了直观将扫描类之间空出一行
            Variable.currentRow++;
        }
    }
}