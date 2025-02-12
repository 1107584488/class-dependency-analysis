package com.lgw.github.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lgw.github.constant.Constants;
import com.lgw.github.constant.Variable;

/**
 * 文件工具类
 *
 * @author lianguowei <lianguowei>
 * Created on 2024-09-03
 */
public class FileUtil {
    /**
     * 删除本地原有的excel文件
     *
     * @param outFilePath   本地excel输出路径
     * @param excelFileName 本地excel文件名
     */
    public static void deleteFile(String outFilePath, String excelFileName) {
        String filePath = outFilePath + File.separator + excelFileName + ".xlsx";
        Path path = Paths.get(filePath);
        try {
            // 检查文件是否存在
            if (Files.exists(path)) {
                // 删除文件
                Files.delete(path);
                System.out.println("文件删除成功: " + filePath);
            } else {
                System.out.println("文件不存在: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("删除文件时发生错误: " + e.getMessage());
        }
    }

    /**
     * 根据文件路径获取excel文件
     *
     * @param filePath
     * @return
     */
    public static Workbook getWorkbook(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        // 判断文件是否存在，文件存在则读取并续写，不存在则新建
        // 文件完整路径
        Path path = Paths.get(filePath);
        // 创建或打开工作簿
        Workbook workbook = null;
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            workbook = WorkbookFactory.create(fis);
        } catch (FileNotFoundException e) {
            // 文件不存在，创建新的工作簿
            workbook = new XSSFWorkbook();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("文件格式错误或无法打开文件: " + path);
        }
        return workbook;
    }

    /**
     * 打开或创建sheet表，新建则添加标题行
     * @param workbook      工作表
     * @param outFileName   输出文件名
     * @return
     */
    public static Sheet getSheet(Workbook workbook, String outFileName) {
        if (workbook == null || outFileName == null || outFileName.isEmpty()) {
            return null;
        }
        Sheet sheet = workbook.getSheet(outFileName);
        if (sheet == null) {
            sheet = workbook.createSheet(outFileName);
            // 画标题行并设置标题行样式：字体加粗、水平居中
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("类名");
            headerRow.getCell(0).setCellStyle(headerStyle);
            headerRow.createCell(1).setCellValue("类路径");
            headerRow.getCell(1).setCellStyle(headerStyle);
        }
        return sheet;
    }
}
