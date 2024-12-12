package com.lgw.github.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量工具类
 *
 * @author lianguowei <lianguowei>
 * Created on 2024-09-03
 */
public class Constants {

    /**
     * 类模块映射：key：moduleName（import第六级），value：Map<类名，类路径>
     */
    public static final Map<String, HashMap<String, String>> MODULE_CLASS_MAP = new HashMap<>();

    /**
     * 类与bean映射关系，key：类名，value：beanName
     */
    public static final Map<String, String> CLASS_MAP = new HashMap<>();

    /**
     * bean与方法映射，key：beanName，value：调用的方法集合（已去重）
     */
    public static final Map<String, List<String>> METHOD_MAP = new HashMap<>();

    /**
     * 标题行名称
     */
    public static final List<String> TITLE_LIST = Arrays.asList("扫描类", "依赖模块", "依赖类名", "调用方法", "依赖类路径");

    /**
     * 表格没有自适应宽度，获取每列最长宽度+2，表格写入末尾设置列宽
     */
    public static final int[] COLUMN_WIDTHS = new int[TITLE_LIST.size()];
}
