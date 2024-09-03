package com.lgw.github.util;

import com.lgw.github.constant.Constants;
import com.lgw.github.constant.Variable;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * excel 工具类
 *
 * @author lianguowei <lianguowei>
 * Created on 2024-09-03
 */
public class ExcelUtil {

    /**
     * 文件扫描内容写入本地excel表格
     *
     * @param outFilePath excel文件输出地址
     * @param outFileName excel文件输出名称，本参数不带 .xlsx后缀，sheet列同名
     */
    public static void writeExcelForDependency(String outFilePath, String outFileName) {
        if (Constants.MODULE_CLASS_MAP.isEmpty()) {
            return;
        }

        // 判断文件是否存在，文件存在则读取并续写，不存在则新建
        // 文件完整路径
        String filePath = outFilePath + File.separator + outFileName + ".xlsx";
        // 创建或打开工作簿
        Workbook workbook = FileUtil.getWorkbook(filePath);
        if (workbook == null) {
            System.out.println("excel文件创建或打开失败，excel文件为空");
            return;
        }

        // 判断工作表是否存在，存在则续写，不存在则新建并写标题行
        Sheet sheet = FileUtil.getSheet(workbook, outFileName);
        if (sheet == null) {
            System.out.println("sheet表创建或打开失败，sheet表为空");
            return;
        }

        // 查找第一列（扫描的类名）最长宽度
        Constants.COLUMN_WIDTHS[0] = Math.max(Variable.indexScanClassName.length() + 2, Constants.COLUMN_WIDTHS[0]);

        // 开始画excel并填充数据
        try (FileOutputStream fileOut = new FileOutputStream(outFilePath + File.separator + outFileName + ".xlsx")) {
            // 扫描类合并单元格的起始行号
            int scanClassStartRow = Variable.currentRow;
            for (Map.Entry<String, HashMap<String, String>> entry : Constants.MODULE_CLASS_MAP.entrySet()) {
                String moduleName = entry.getKey();
                HashMap<String, String> classInfo = Optional.ofNullable(entry.getValue()).filter(map -> !map.isEmpty()).orElseGet(HashMap::new);

                // 查找第二列（模块名）最长宽度
                Constants.COLUMN_WIDTHS[1] = Math.max(Constants.COLUMN_WIDTHS[1], (moduleName == null || moduleName.isEmpty()) ? 0 : moduleName.length());

                // 模块单元格合并起止行号
                int moduleStartRow = Variable.currentRow;
                for (Map.Entry<String, String> classEntry : classInfo.entrySet()) {
                    String className = classEntry.getKey();
                    String classPath = classEntry.getValue();
                    // 查找第三（类名）、五列（类路径）查找最长宽度
                    Constants.COLUMN_WIDTHS[2] = Math.max(Constants.COLUMN_WIDTHS[2], (className == null || className.isEmpty()) ? 0 : className.length());
                    Constants.COLUMN_WIDTHS[4] = Math.max(Constants.COLUMN_WIDTHS[4], (classPath == null || classPath.isEmpty()) ? 0 : classPath.length());
                    // 类名 与 类路径，单元格合并起止行号
                    int classStartRow = Variable.currentRow, classEndRow = classStartRow;
                    // 获取class -> beanName -> methodList
                    List<String> methodList = Optional.ofNullable(Constants.CLASS_MAP.get(className))
                            .flatMap(classKey -> Optional.ofNullable(Constants.METHOD_MAP.get(classKey)))
                            .filter(metList -> !metList.isEmpty())
                            .orElse(Collections.emptyList());
                    for (String methodName : methodList) {
                        // 查找第四列（调用方法）查找最长宽度
                        Constants.COLUMN_WIDTHS[3] = Math.max(Constants.COLUMN_WIDTHS[3], (methodName == null || methodName.isEmpty()) ? 0 : methodName.length());
                        // 填充 方法名 单元格，且行号游标下移
                        drawCellAndWriteContent(sheet, methodName, 3, classEndRow, classEndRow++);
                    }
                    // 填充 类名、类路径 单元格
                    drawCellAndWriteContent(sheet, className, 2, classStartRow, classEndRow);
                    drawCellAndWriteContent(sheet, classPath, 4, classStartRow, classEndRow);
                    // 当前行号游标移动，类名行需判断内部是否有方法，方法单元格会将行号游标下移
                    Variable.currentRow = !methodList.isEmpty() ? classEndRow : classEndRow + 1;
                }
                // 填充 模块名 单元格，更新currentRow，每个模块间空一行，更直观
                drawCellAndWriteContent(sheet, moduleName, 1, moduleStartRow, Variable.currentRow++);
            }
            // 填充 扫描类名 单元格，因为上面为了更直观，在每个模块间空了一行，游标下移了一个，所以尾行需-1操作
            drawCellAndWriteContent(sheet, Variable.indexScanClassName, 0, scanClassStartRow, --Variable.currentRow);
            // 设置列宽， 加 2 空白，单位是 1/256 字符宽度
            for (int i = 0; i < Constants.COLUMN_WIDTHS.length; i++) {
                sheet.setColumnWidth(i, (Constants.COLUMN_WIDTHS[i] + 2) * 256);
            }
            workbook.write(fileOut);
            System.out.println("Excel file created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画表格并填充内容
     *
     * @param sheet    sheet
     * @param cellName 列名
     * @param cellNum  列号，从 0 开始
     * @param startRow 起始行
     * @param endRow   结束行
     */
    private static void drawCellAndWriteContent(Sheet sheet, String cellName, int cellNum, int startRow, int endRow) {
        // 合并单元格，只有单元格>=2才能合并，否则报非法异常
        if (endRow > startRow + 1) {
            // 行截止减1是因为endRow已经递增过了
            CellRangeAddress mergedRegion = new CellRangeAddress(startRow, endRow - 1, cellNum, cellNum);
            sheet.addMergedRegion(mergedRegion);
            // 在合并后的单元格中写入模块名
            Row moduleRow = sheet.getRow(startRow);
            moduleRow.createCell(cellNum).setCellValue(cellName);
        } else {
            // 切记！切记！切记！此处有大坑！直接sheet.creativeRow会覆盖当前行，其他列数据被清空！
            Row moduleRow = sheet.getRow(startRow);
            if (moduleRow == null) {
                moduleRow = sheet.createRow(startRow);
            }
            moduleRow.createCell(cellNum).setCellValue(cellName);
        }
    }
}
