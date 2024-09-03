package com.lgw.github.util;

import com.lgw.github.constant.Constants;
import com.lgw.github.constant.Variable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫描工具类
 *
 * @author lianguowei <lianguowei>
 * Created on 2024-09-03
 */
public class ScanUtil {

    /**
     * 开始扫描类文件内容，按行读取
     * 并在扫描前进行前置处理：清空单个文件的本地临时缓存
     *
     * @param filePath      待扫描的文帝java文件的全路径
     * @param moduleRule    域命名规范前缀
     */
    public static void scanAndWriteExcel(String filePath, String moduleRule) {
        // 扫描前置处理：可复用的临时存储的扫描内容，每次文件扫描前需清空
        ClearUtil.scanFilePreHandle();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line.trim(), moduleRule);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 行数据处理
     * 1、先扫描import的类（类名、类路径）
     * 2、扫描bean注入（步骤一的类名、beanName）
     * 3、根据步骤二的beanName扫描下文中调用的方法
     *
     * @param line          读取的行数据
     * @param moduleRule    域命名规范前缀
     */
    private static void processLine(String line, String moduleRule) {
        if (line.startsWith("import " + moduleRule)) {
            handleImportLine(line);
        } else if (line.startsWith("@Autowired") || line.startsWith("@Resource") || line.startsWith("@Qualifier")) {
            Variable.flag = true;
            return;
        } else if (Variable.flag) {
            handleBeanLine(line);
        } else {
            handleMethodLine(line);
        }
        Variable.flag = false;
    }

    /**
     * 处理引用
     * 1、筛选处理 com.kuaishou.ad.brand.platform. 开头的import
     * 2、截取 module名称 和 类名
     * 3、保存类名和类路径
     *
     * @param line 读取的行数据
     */
    private static void handleImportLine(String line) {
        String classPath = line.replace("import ", "").replace(";", "");
        String[] split = classPath.replace("com.kuaishou.ad.brand.platform.", "").split("\\.");
        String moduleName = split[0];
        String className = split[split.length - 1];

        Constants.MODULE_CLASS_MAP.computeIfAbsent(moduleName, k -> new HashMap<>())
                .put(className, classPath);
    }

    /**
     * 处理bean，以 @Autowired || @Resource || @Qualifier 等形式调用的bean
     * 1、根据标志位 flag 判定是否上一行是否是注解注入操作
     * 2、将 类名 和 beanName 取出放入map保存映射关系
     *
     * @param line 读取的行数据
     */
    private static void handleBeanLine(String line) {
        String cleanLine = line.replace(";", "");
        String[] split = cleanLine.split("\\s+");
        if (split.length >= 2) {
            Constants.CLASS_MAP.put(split[split.length - 2], split[split.length - 1]);
        }
    }

    /**
     * 全文检索，扫描是否有 bean 的方法调用，不保存参数，不考虑是否重构函数
     * 检索方法：扫描行中是否有beanName.xxx(，有则截取保存beanName.xxx()
     *
     * @param line 读取的行数据
     */
    private static void handleMethodLine(String line) {
        if (line.isEmpty() || line.isBlank()) {
            return;
        }

        for (Map.Entry<String, String> entry : Constants.CLASS_MAP.entrySet()) {
            String beanName = entry.getValue();
            String indexStr = beanName + ".";
            int startIndex = line.indexOf(indexStr);
            if (startIndex != -1) {
                int endIndex = line.substring(startIndex).indexOf("(");
                endIndex = endIndex == -1 ? line.length() : (startIndex + endIndex);
                String methodName = line.substring(startIndex, endIndex) + "()";
                List<String> methodList = Constants.METHOD_MAP.getOrDefault(beanName, new ArrayList<>());
                if (!methodList.contains(methodName)) {
                    methodList.add(methodName);
                    Constants.METHOD_MAP.put(beanName, methodList);
                }
            }
        }
    }
}
