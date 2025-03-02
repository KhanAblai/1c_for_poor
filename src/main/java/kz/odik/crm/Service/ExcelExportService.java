package kz.odik.crm.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    @PersistenceUnit
    private EntityManagerFactory emf;

    public byte[] exportUserActivityToExcel() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Object[]> query = em.createQuery(
                "SELECT ua.userId, u.username, ua.checkedInDate, ua.checkedOutDate, r.name " +
                        "FROM UserActivity ua JOIN Users u ON ua.userId = u.id JOIN u.role r", Object[].class);
        List<Object[]> userActivities = query.getResultList();
        byte[] bytes = exportToExcel(userActivities);
        em.close();
        return bytes;
    }

    private byte[] exportToExcel(List<Object[]> userActivities) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("User Activity");

        String[] columns = {"Сотрудник", "Роль", "Дата прикрепления", "Дата открепления"};
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        int rowNum = 1;
        for (Object[] activity : userActivities) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue((String) activity[1]);
            row.createCell(1).setCellValue((String) activity[4]);

            if (activity[2] instanceof LocalDateTime) {
                row.createCell(2).setCellValue(dateTimeFormatter.format((LocalDateTime) activity[2]));
            } else {
                row.createCell(2).setCellValue("Invalid Date");
            }

            if (activity[3] instanceof LocalDateTime) {
                row.createCell(3).setCellValue(dateTimeFormatter.format((LocalDateTime) activity[3]));
            } else {
                row.createCell(3).setCellValue("Invalid Date");
            }
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
