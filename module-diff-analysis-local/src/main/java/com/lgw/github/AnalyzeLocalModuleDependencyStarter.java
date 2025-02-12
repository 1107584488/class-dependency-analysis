package com.lgw.github;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lgw.github.util.ExcelUtil;
import com.lgw.github.util.ScanUtil;

/**
 * @author lianguowei <lianguowei@kuaishou.com>
 * Created on 2024-12-12
 */
public class AnalyzeLocalModuleDependencyStarter {

    public static void main(String[] args) throws IOException {
//        // 创意域api扫描
//        creativeApi();
//        // 创意域runner扫描
//        creativeRunner();
//        // 账户域api扫描
//        accountApi();
//        // 账户域runner扫描
//        accountRunner();
//        // 订单域api扫描
//        orderApi();
//        // 订单域runner扫描
//        orderRunner();
//        // 结算域api扫描
//        settleApi();
//        // 结算域runner扫描
//        settleRunner();
//        // 资源域api扫描
//        resourceApi();
//        // 资源域runner扫描
//        resourceRunner();
//        // 通用域api扫描
//        commonApi();
//        // 通用域runner扫描
//        commonRunner();
//        // 全域api扫描
//        allApi();
//        // 全域runner扫描
//        allRunner();
//        test1();

    }
    /**
     * 全域api扫描
     */
    public static void allApi() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-api";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-api";
        String refactorModuleName = "refactor-creative-center-api";
        List<String> paths = new ArrayList<>();
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-creative-center/ad-brand-creative-center-api");
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-account-center/ad-brand-account-center-api");
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-order-center/ad-brand-order-center-api");
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-settle-center/ad-brand-settle-api");
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-resource-center/ad-brand-resource-center-api");
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-common-center/ad-brand-common-center-api");
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanAllModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, paths);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "全域api扫描");
    }

    /**
     * 全域runner扫描
     */
    public static void allRunner() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-runner";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-runner";
        String refactorModuleName = "refactor-creative-center-runner";
        List<String> paths = new ArrayList<>();
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-creative-center/ad-brand-creative-center-runner");
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-account-center/ad-brand-account-center-runner");
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-order-center/ad-brand-order-runner");
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-settle-center/ad-brand-settle-runner");
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-resource-center/ad-brand-resource-center-runner");
        paths.add("/Users/lianguowei/Documents/soft/ideaProject/ad-brand-common-center/ad-brand-common-center-runner");
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanAllModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, paths);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "全域runner扫描");
    }

    /**
     * 通用域api扫描
     */
    public static void commonApi() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-api";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-api";
        String refactorModuleName = "refactor-creative-center-api";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-common-center/ad-brand-common-center-api";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "通用域api扫描");
    }

    /**
     * 通用域runner扫描
     */
    public static void commonRunner() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-runner";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-runner";
        String refactorModuleName = "refactor-creative-center-runner";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-common-center/ad-brand-common-center-runner";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "通用域runner扫描");
    }

    /**
     * 资源域api扫描
     */
    public static void resourceApi() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-api";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-api";
        String refactorModuleName = "refactor-creative-center-api";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-resource-center/ad-brand-resource-center-api";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "资源域api扫描");
    }

    /**
     * 资源域runner扫描
     */
    public static void resourceRunner() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-runner";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-runner";
        String refactorModuleName = "refactor-creative-center-runner";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-resource-center/ad-brand-resource-center-runner";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "资源域runner扫描");
    }

    /**
     * 结算域api扫描
     */
    public static void settleApi() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-api";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-api";
        String refactorModuleName = "refactor-creative-center-api";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-settle-center/ad-brand-settle-api";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "结算域api扫描");
    }

    /**
     * 结算域runner扫描
     */
    public static void settleRunner() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-runner";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-runner";
        String refactorModuleName = "refactor-creative-center-runner";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-settle-center/ad-brand-settle-runner";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "结算域runner扫描");
    }

    /**
     * 订单域api扫描
     */
    public static void orderApi() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-api";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-api";
        String refactorModuleName = "refactor-creative-center-api";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-order-center/ad-brand-order-center-api";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "订单域api扫描");
    }

    /**
     * 订单域runner扫描
     */
    public static void orderRunner() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-runner";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-runner";
        String refactorModuleName = "refactor-creative-center-runner";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-order-center/ad-brand-order-runner";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "订单域runner扫描");
    }

    /**
     * 账户域api扫描
     */
    public static void accountApi() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-api";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-api";
        String refactorModuleName = "refactor-creative-center-api";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-account-center/ad-brand-account-center-api";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "账户域api扫描");
    }

    /**
     * 账户域runner扫描
     */
    public static void accountRunner() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-runner";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-runner";
        String refactorModuleName = "refactor-creative-center-runner";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-account-center/ad-brand-account-center-runner";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "账户域runner扫描");
    }


    /**
     * 创意域api扫描
     */
    public static void creativeApi() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-api";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-api";
        String refactorModuleName = "refactor-creative-center-api";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-creative-center/ad-brand-creative-center-api";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "创意域api扫描");
    }

    /**
     * 创意域runner扫描
     */
    public static void creativeRunner() {
        // 步骤1:输入待扫描的模块和路径（即需要扫描的被依赖的模块），自动化嵌套扫描类全路径
        String masterModuleName = "master-platform-runner";
        String masterModulePath = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-runner";
        String refactorModuleName = "refactor-creative-center-runner";
        String refactorModulePath = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-creative-center/ad-brand-creative-center-runner";
        // 扫描出当前模块下的所有类路径
        ScanUtil.scanDependencyModuleClassPath(masterModuleName, masterModulePath, refactorModuleName, refactorModulePath);
        ExcelUtil.writeExcelForClass("/Users/lianguowei/Documents/草稿/类扫描", "创意域runner扫描");
    }

    public static void test1() throws IOException{
//        // 预期一致
//        String file1 = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/ad-brand-resource-sdk/src/main/java/com/kuaishou/ad/brand/platform/adpositionnew/vo/AdBrandProductNewNewVO.java";
//        String file2 = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-creative-center/ad-brand-creative-center-component/src/main/java/com/kuaishou/ad/brand/platform/adpositionnew/vo/AdBrandProductNewNewVO.java";
//        System.out.println(ScanUtil.filesEqualByStream(file1, file2));
//
//        //预期不一致
        String file3 = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-creative-center/ad-brand-creative-center-api/src/main/java/com/kuaishou/ad/brand/creative/center/controller/AdBrandCreativeController.java";
        String file4 = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-api/src/main/java/com/kuaishou/ad/brand/platform/controller/AdBrandCreativeController.java";
        System.out.println(ScanUtil.filesEqualByStream(file3, file4));
        System.out.println(ScanUtil.areFilesEqualExcludingPackageAndImports(file3, file4));
//
//        String file5 = "/Users/lianguowei/Documents/soft/ideaProject/kuaishou-ad-brand-platform/kuaishou-ad-brand-platform-api/src/main/java/com/kuaishou/ad/brand/platform/controller/GlobalExceptionHandler.java";
//        String file6 = "/Users/lianguowei/Documents/soft/ideaProject/ad-brand-account-center/ad-brand-account-center-api/src/main/java/com/kuaishou/ad/brand/platform/controller/GlobalExceptionHandler.java";
//        System.out.println(ScanUtil.filesEqualByStream(file5, file6));
//        System.out.println(ScanUtil.areFilesEqualExcludingPackageAndImports(file5, file6));
    }

}
