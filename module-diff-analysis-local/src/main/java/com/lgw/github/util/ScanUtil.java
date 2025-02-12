package com.lgw.github.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.lgw.github.constant.Constants;

/**
 * 扫描工具类
 *
 * @author lianguowei <lianguowei>
 * Created on 2024-09-03
 */
public class ScanUtil {
    /**
     * 嵌套扫描master模块和对比模块的所有类名和类路径
     */
    public static void scanAllModuleClassPath(String masterModuleName, String masterModulePath,
                                                     String targetModuleName, List<String> refactorModulePath) {
        // 标杆类文件信息扫描（泛指master，类名 -> 本地类全路径）
        File masterFolder = new File(masterModulePath);
        scanFolder(masterFolder, Constants.MASTER_CLASS_MAP);
        for (Map.Entry<String, String> entry : Constants.MASTER_CLASS_MAP.entrySet()) {
            scanClassNameAndClassNamePath(entry.getValue(), entry.getKey(), Constants.MASTER_CLASS_PATH_MAP);
        }

        System.out.println("master分支扫描出的类文件数量：" + Constants.MASTER_CLASS_MAP.size());

        // 目标类文件信息扫描（泛指target，类名 -> 本地类全路径）
        for (String path : refactorModulePath) {
            File targetFolder = new File(path);
            scanFolder(targetFolder, Constants.TARGET_CLASS_MAP);
        }
        for (Map.Entry<String, String> entry : Constants.TARGET_CLASS_MAP.entrySet()) {
            scanClassNameAndClassNamePath(entry.getValue(), entry.getKey(), Constants.TARGET_CLASS_PATH_MAP);
        }

        System.out.println("target分支扫描出的类文件数量：" + Constants.TARGET_CLASS_MAP.size());

        // 如果两个分支模块下都为空，输出空文件
        if (Constants.MASTER_CLASS_MAP.isEmpty() && Constants.TARGET_CLASS_MAP.isEmpty()) {
            System.out.println("======================== 两个分支目录下无可用java文件 ========================");
            return;
        }

        // 如果对比分支模块下为空，输出master分支独有类
        if (!Constants.MASTER_CLASS_MAP.isEmpty() && Constants.TARGET_CLASS_MAP.isEmpty()) {
            System.out.println("----------------------- master不为空，目标分支为空的类，打印开始 -----------------------");
            PrintUtil.printSingleFeatureClassInfo(targetModuleName, Constants.MASTER_CLASS_PATH_MAP);
            System.out.println("----------------------- master不为空，目标分支为空的类，打印结束 -----------------------");
            System.out.println();
        }

        // 如果master分支模块下为空，输出对比分支独有的类
        if (!Constants.TARGET_CLASS_MAP.isEmpty() && Constants.MASTER_CLASS_MAP.isEmpty()) {
            System.out.println("----------------------- master为空，目标分支不为空的类，打印开始 -----------------------");
            PrintUtil.printSingleFeatureClassInfo(masterModuleName, Constants.TARGET_CLASS_PATH_MAP);
            System.out.println("----------------------- master为空，目标分支不为空的类，打印结束 -----------------------");
            System.out.println();
        }

        // 求master中有，但对比模块中没有的差集
        Set<String> masterWithoutTarget = Constants.MASTER_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        Set<String> targetClassSet = Constants.TARGET_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        masterWithoutTarget.removeAll(targetClassSet);
        if (!masterWithoutTarget.isEmpty()) {
            Constants.ONLY_MASTER_CLASS_PATH_MAP = Constants.MASTER_CLASS_PATH_MAP.entrySet().stream()
                    .filter(entry -> masterWithoutTarget.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            System.out.println("----------------------- master独有的类，打印开始 -----------------------");
            PrintUtil.printSingleFeatureClassInfo(masterModuleName, Constants.ONLY_MASTER_CLASS_PATH_MAP);
            System.out.println("----------------------- master独有的类，打印结束 -----------------------");
            System.out.println();
        }

        // 求对比模块中有，但master中没有的差集
        Set<String> targetWithoutMaster = Constants.TARGET_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        Set<String> masterClassSet = Constants.MASTER_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        targetWithoutMaster.removeAll(masterClassSet);
        if (!targetWithoutMaster.isEmpty()) {
            Constants.ONLY_TARGET_CLASS_PATH_MAP = Constants.TARGET_CLASS_PATH_MAP.entrySet().stream()
                    .filter(entry -> targetWithoutMaster.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            System.out.println("----------------------- 目标分支独有的类，打印开始 -----------------------");
            PrintUtil.printSingleFeatureClassInfo(targetModuleName, Constants.ONLY_TARGET_CLASS_PATH_MAP);
            System.out.println("----------------------- 目标分支独有的类，打印结束 -----------------------");
            System.out.println();
        }

        // 求master和对比模块中都存在的交集，并做匹配
        Set<String> masterClassSetCopy = Constants.MASTER_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        Set<String> targetClassSetCopy = Constants.TARGET_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        masterClassSetCopy.retainAll(targetClassSetCopy);
        if (!masterClassSetCopy.isEmpty()) {
            masterClassSetCopy.forEach(key -> {
                String masterFilePath = Constants.MASTER_CLASS_MAP.get(key);
                String targetFilePath = Constants.TARGET_CLASS_MAP.get(key);
                if (!areFilesEqualExcludingPackageAndImports(masterFilePath, targetFilePath)) {
                    Constants.MASTER_TARGET_CLASS_DIFF_MAP.put(key, Constants.MASTER_CLASS_PATH_MAP.get(key));
                }
            });
            if (!Constants.MASTER_TARGET_CLASS_DIFF_MAP.isEmpty()) {
                PrintUtil.printSingleFeatureClassInfo();
            }
        }
    }

    /**
     * 嵌套扫描master模块和对比模块的所有类名和类路径
     */
    public static void scanDependencyModuleClassPath(String masterModuleName, String masterModulePath,
                                                     String targetModuleName, String refactorModulePath) {
        // 标杆类文件信息扫描（泛指master，类名 -> 本地类全路径）
        File masterFolder = new File(masterModulePath);
        scanFolder(masterFolder, Constants.MASTER_CLASS_MAP);
        for (Map.Entry<String, String> entry : Constants.MASTER_CLASS_MAP.entrySet()) {
            scanClassNameAndClassNamePath(entry.getValue(), entry.getKey(), Constants.MASTER_CLASS_PATH_MAP);
        }

        System.out.println("master分支扫描出的类文件数量：" + Constants.MASTER_CLASS_MAP.size());

        // 目标类文件信息扫描（泛指target，类名 -> 本地类全路径）
        File targetFolder = new File(refactorModulePath);
        scanFolder(targetFolder, Constants.TARGET_CLASS_MAP);
        for (Map.Entry<String, String> entry : Constants.TARGET_CLASS_MAP.entrySet()) {
            scanClassNameAndClassNamePath(entry.getValue(), entry.getKey(), Constants.TARGET_CLASS_PATH_MAP);
        }

        System.out.println("target分支扫描出的类文件数量：" + Constants.TARGET_CLASS_MAP.size());

        // 如果两个分支模块下都为空，输出空文件
        if (Constants.MASTER_CLASS_MAP.isEmpty() && Constants.TARGET_CLASS_MAP.isEmpty()) {
            System.out.println("======================== 两个分支目录下无可用java文件 ========================");
            return;
        }

        // 如果对比分支模块下为空，输出master分支独有类
        if (!Constants.MASTER_CLASS_MAP.isEmpty() && Constants.TARGET_CLASS_MAP.isEmpty()) {
            System.out.println("----------------------- master不为空，目标分支为空的类，打印开始 -----------------------");
            PrintUtil.printSingleFeatureClassInfo(targetModuleName, Constants.MASTER_CLASS_PATH_MAP);
            System.out.println("----------------------- master不为空，目标分支为空的类，打印结束 -----------------------");
            System.out.println();
        }

        // 如果master分支模块下为空，输出对比分支独有的类
        if (!Constants.TARGET_CLASS_MAP.isEmpty() && Constants.MASTER_CLASS_MAP.isEmpty()) {
            System.out.println("----------------------- master为空，目标分支不为空的类，打印开始 -----------------------");
            PrintUtil.printSingleFeatureClassInfo(masterModuleName, Constants.TARGET_CLASS_PATH_MAP);
            System.out.println("----------------------- master为空，目标分支不为空的类，打印结束 -----------------------");
            System.out.println();
        }

        // 求master中有，但对比模块中没有的差集
        Set<String> masterWithoutTarget = Constants.MASTER_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        Set<String> targetClassSet = Constants.TARGET_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        masterWithoutTarget.removeAll(targetClassSet);
        if (!masterWithoutTarget.isEmpty()) {
            Constants.ONLY_MASTER_CLASS_PATH_MAP = Constants.MASTER_CLASS_PATH_MAP.entrySet().stream()
                    .filter(entry -> masterWithoutTarget.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            System.out.println("----------------------- master独有的类，打印开始 -----------------------");
            PrintUtil.printSingleFeatureClassInfo(masterModuleName, Constants.ONLY_MASTER_CLASS_PATH_MAP);
            System.out.println("----------------------- master独有的类，打印结束 -----------------------");
            System.out.println();
        }

        // 求对比模块中有，但master中没有的差集
        Set<String> targetWithoutMaster = Constants.TARGET_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        Set<String> masterClassSet = Constants.MASTER_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        targetWithoutMaster.removeAll(masterClassSet);
        if (!targetWithoutMaster.isEmpty()) {
            Constants.ONLY_TARGET_CLASS_PATH_MAP = Constants.TARGET_CLASS_PATH_MAP.entrySet().stream()
                    .filter(entry -> targetWithoutMaster.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            System.out.println("----------------------- 目标分支独有的类，打印开始 -----------------------");
            PrintUtil.printSingleFeatureClassInfo(targetModuleName, Constants.ONLY_TARGET_CLASS_PATH_MAP);
            System.out.println("----------------------- 目标分支独有的类，打印结束 -----------------------");
            System.out.println();
        }

        // 求master和对比模块中都存在的交集，并做匹配
        Set<String> masterClassSetCopy = Constants.MASTER_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        Set<String> targetClassSetCopy = Constants.TARGET_CLASS_MAP.keySet().stream().collect(Collectors.toSet());
        masterClassSetCopy.retainAll(targetClassSetCopy);
        if (!masterClassSetCopy.isEmpty()) {
            masterClassSetCopy.forEach(key -> {
                String masterFilePath = Constants.MASTER_CLASS_MAP.get(key);
                String targetFilePath = Constants.TARGET_CLASS_MAP.get(key);
                if (!areFilesEqualExcludingPackageAndImports(masterFilePath, targetFilePath)) {
                    Constants.MASTER_TARGET_CLASS_DIFF_MAP.put(key, Constants.MASTER_CLASS_PATH_MAP.get(key));
                }
            });
            if (!Constants.MASTER_TARGET_CLASS_DIFF_MAP.isEmpty()) {
                PrintUtil.printSingleFeatureClassInfo();
            }
        }
    }

    /**
     * 单个文件或文件夹的扫描处理
     *
     * @param folder
     * @param fileNamePathMap
     */
    public static void scanFolder(File folder, Map<String, String> fileNamePathMap) {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanFolder(file, fileNamePathMap);
            } else {
                if (file.getName().endsWith(".java")) {
                    String fileName = file.getName().replace(".java", "");
                    String filePath = file.getPath();
                    fileNamePathMap.put(fileName, filePath);
                }
            }
        }
    }

    public static void scanMasterFolder(File folder) {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanMasterFolder(file);
            } else {
                if (file.getName().endsWith(".java")) {
                    String fileName = file.getName().replace(".java", "");
                    String filePath = file.getPath();
                    Constants.MASTER_CLASS_MAP.put(fileName, filePath);
                }
            }
        }
    }

    public static void scanTargetFolder(File folder) {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanTargetFolder(file);
            } else {
                if (file.getName().endsWith(".java")) {
                    String fileName = file.getName().replace(".java", "");
                    String filePath = file.getPath();
                    Constants.TARGET_CLASS_MAP.put(fileName, filePath);
                }
            }
        }
    }

    /**
     * 开始扫描类文件内容，按行读取
     * 并在扫描前进行前置处理：清空单个文件的本地临时缓存
     *
     * @param filePath  待扫描的java文件的全路径
     * @param className 类名
     */
    public static void scanClassNameAndClassNamePath(String filePath, String className, Map<String, String> map) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // 读取被依赖模块的全路径，通过首行 package 后的路径 + 类名拼接
            String line = reader.readLine();
            if (line.startsWith("package ")) {
                handlePackageLine(line, className, map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handlePackageLine(String line, String className, Map<String, String> map) {
        String classPath = line.replace("package", "").replaceAll("\\s+", "").replace(";", "");
        String classPathName = classPath + "." + className;
        map.put(className, classPathName);
    }

    //对于大文件，可以使用InputStream的方式逐字节比较
    public static boolean filesEqualByStream(String filePath1, String filePath2) {
        try (InputStream in1 = new FileInputStream(filePath1);
             InputStream in2 = new FileInputStream(filePath2)) {

            int read1, read2;
            while ((read1 = in1.read()) != -1 && (read2 = in2.read()) != -1) {
                if (read1 != read2) {
                    return false;
                }
            }
            // 如果两个流同时结束，则文件相同；否则不同
            return in1.read() == in2.read();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean areFilesEqualExcludingPackageAndImports(String filePath1, String filePath2){
        try {
            List<String> lines1 = filterJavaFileContent(filePath1);
            List<String> lines2 = filterJavaFileContent(filePath2);

            return lines1.equals(lines2);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static List<String> filterJavaFileContent(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines
                    .filter(line -> !line.trim().startsWith("package") && !line.trim().startsWith("import")) // 过滤掉package和import行
                    .map(String::trim) // 去除每行的前后空白字符
                    .filter(line -> !line.isEmpty()) // 移除空行
                    .collect(Collectors.toList()); // 收集结果
        }
    }
}
