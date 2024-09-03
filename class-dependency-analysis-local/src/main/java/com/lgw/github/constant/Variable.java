package com.lgw.github.constant;

/**
 * 变量工具类
 *
 * @author lianguowei <lianguowei>
 * Created on 2024-09-03
 */
public class Variable {
    /**
     * 当前扫描的类名，首列使用
     */
    public static String indexScanClassName = "";

    /**
     * 扫描bean的标志位，当前行为@Autowired || @Resource || @Qualifier，下一行则为bean
     */
    public static boolean flag = false;

    /**
     * 行游标，行递增，从0开始，0行为标题行
     */
    public static int currentRow = 1;
}
