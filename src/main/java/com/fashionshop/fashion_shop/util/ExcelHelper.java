package com.fashionshop.fashion_shop.util;

import com.fashionshop.fashion_shop.dto.excel.ProductExcelRowDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExcelHelper {

    public static List<ProductExcelRowDto> readProductExcel(InputStream is) {
        List<ProductExcelRowDto> list = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowNumber = 0;

            for (Row row : sheet) {
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                if (isRowEmpty(row)) {
                    continue;
                }

                ProductExcelRowDto rowDto = ProductExcelRowDto.builder()
                        .rowIndex(row.getRowNum() + 1)
                        .productCode(getVal(row.getCell(0), formatter))
                        .productName(getVal(row.getCell(1), formatter))
                        .categoryId(parseLong(getVal(row.getCell(2), formatter)))
                        .brandId(parseLong(getVal(row.getCell(3), formatter)))
                        .season(getVal(row.getCell(4), formatter))
                        .description(getVal(row.getCell(5), formatter))
                        .size(getVal(row.getCell(6), formatter))
                        .color(getVal(row.getCell(7), formatter))
                        .sku(getVal(row.getCell(8), formatter))
                        .barcode(getVal(row.getCell(9), formatter))
                        .costPrice(parseBigDecimal(getVal(row.getCell(10), formatter)))
                        .retailPrice(parseBigDecimal(getVal(row.getCell(11), formatter)))
                        .imageUrl(getVal(row.getCell(12), formatter))
                        .build();

                list.add(rowDto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc cấu trúc file Excel: " + e.getMessage(), e);
        }
        return list;
    }

    private static String getVal(Cell cell, DataFormatter formatter) {
        if (cell == null) return null;
        String val = formatter.formatCellValue(cell).trim();
        return val.isEmpty() ? null : val;
    }

    private static Long parseLong(String val) {
        if (val == null || val.isBlank()) return null;
        try {
            return Long.parseLong(val.replace(",", "").replace(".", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private static BigDecimal parseBigDecimal(String val) {
        if (val == null || val.isBlank()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(val.replace(",", ""));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
