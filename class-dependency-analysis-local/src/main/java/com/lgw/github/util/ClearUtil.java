package com.lgw.github.util;

import com.lgw.github.constant.Constants;

/**
 * 清理工具类
 *
 * @author lianguowei <lianguowei>
 * Created on 2024-09-03
 */
public class ClearUtil {

    /**
     * 扫描前置处理：本地缓存的扫描内容清空
     * 本地缓存只能存储一个扫描类文件内容，因此每个扫描类前置需清空，单个扫描完成后会写入excel
     */
    public static void scanFilePreHandle() {
        Constants.MODULE_CLASS_MAP.clear();
        Constants.CLASS_MAP.clear();
        Constants.METHOD_MAP.clear();
    }
}
