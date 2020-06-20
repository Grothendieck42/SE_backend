package com.example.demo.controllers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.annotations.session.*;
import com.example.demo.entities.ClassEntity;
import com.example.demo.entities.ClassRegistrationEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.exceptions.ClazzNotFoundException;
import com.example.demo.exceptions.PermissionDeniedException;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.utils.ExportEntityUtils;
import com.example.demo.utils.ExportUtils;

import javax.xml.ws.Response;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "excel")
public class ExportExcelController {
    private final ClassRepository classRepository;

    @Autowired
    public ExportExcelController(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    @GetMapping("/download/classes/{classId}")
    //@Authorization
    @ResponseBody
    public ResponseEntity<byte[]> downloadClassExcel(// @CurrentUser UserEntity user,
                                                     @PathVariable Long classId) throws Exception {
        ExportUtils<ExportEntityUtils> ee = new ExportUtils<>();
        String[] headerss = {"学号", "姓名", "性别", "学院", "专业班", "电话", "邮件"};

        // TODO: Add current user
        ClassEntity cl = classRepository.findById(classId).orElseThrow(ClazzNotFoundException::new);

        //List<UserEntity> students = new ArrayList<>();
        List<ExportEntityUtils> exportentities = new ArrayList<>();
        List<ClassRegistrationEntity> crs = cl.getClassRegistrations();
        for (ClassRegistrationEntity cr : crs) {
            ExportEntityUtils exp = new ExportEntityUtils(cr.getStudent().getUid(), cr.getStudent().getName(),
                    cr.getStudent().getGender(), cr.getStudent().readDepartmentName(), cr.getStudent().readClassName(),
                    cr.getStudent().getTelephone(), cr.getStudent().getEmail());
            //students.add(cr.getStudent());
            exportentities.add(exp);
        }
        return ee.exportExcel(headerss, exportentities, cl.getCourse().getId().toString() + ".xls");
    }
}
