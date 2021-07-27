package com.zale.controller;

import com.zale.entity.Emp;
import com.zale.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("emp")
public class EmpController {
    @Autowired
    private EmpService empService;
    @Value("${photo.dir}")
    private String realPath;

    //get list of employees
    @GetMapping("findAll")
    public List<Emp> findAll() {
        return empService.findAll();
    }

    //save info
    @PostMapping("save")
    public Map<String, Object> save(Emp emp, MultipartFile photo) throws IOException {
        log.info("Emp info: [{}]", emp.toString());
        log.info("headImg info: [{}]", photo.getOriginalFilename());

        Map<String, Object> map = new HashMap<>();
        try {
            //headImg save
            String newFileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(photo.getOriginalFilename());
            photo.transferTo(new File(realPath, newFileName));
            //set head path
            emp.setPath(newFileName);
            //save to database
            empService.save(emp);
            map.put("state", true);
            map.put("msg", "Employee info save successful!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", "Employee info save failure!");
        }
        return map;
    }

    //find id
    @GetMapping("findOne")
    public Emp findOne(String id){
        log.info("emp id find: [{}]",id);
        return empService.findOne(id);
    }

    @PostMapping("update")
    public Map<String, Object> update(Emp emp, MultipartFile photo) {
        log.info("emp info: [{}]", emp.toString());

        Map<String, Object> map = new HashMap<>();
        try {
            if(photo!=null && photo.getSize()!=0){
                log.info("Head info: [{}]", photo.getOriginalFilename());
                //head save
                String newFileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(photo.getOriginalFilename());
                photo.transferTo(new File(realPath, newFileName));
                //set head path
                emp.setPath(newFileName);
            }
            //save employee
            empService.update(emp);
            map.put("state", true);
            map.put("msg", "save success!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", "save fail!");
        }
        return map;
    }

    //delete emp info
    @GetMapping("delete")
    public Map<String, Object> delete(String id) {
        log.info("delete emp id:[{}]",id);
        Map<String, Object> map = new HashMap<>();
        try {
            //head img
            Emp emp = empService.findOne(id);
            File file = new File(realPath, emp.getPath());
            if(file.exists())file.delete();//删除头像
            //删除员工信息
            empService.delete(id);
            map.put("state",true);
            map.put("msg","delete success!");
        }catch (Exception e){
            e.printStackTrace();
            map.put("state",false);
            map.put("msg","delete fail!");
        }
        return map;
    }

}
