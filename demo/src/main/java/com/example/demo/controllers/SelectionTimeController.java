package com.example.demo.controllers;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.demo.annotations.session.*;
import com.example.demo.entities.SelectionTimeEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.exceptions.PermissionDeniedException;
import com.example.demo.exceptions.SelectionTimeNotFoundException;
import com.example.demo.repositories.*;
import com.example.demo.requests.information.AddSelectionTimeRequest;
import com.example.demo.requests.information.ModifyClassRegistrationRequest;
import com.example.demo.requests.information.ModifySelectionTimeEndRequest;
import com.example.demo.requests.information.ModifySelectionTimeStartRequest;
import com.example.demo.responses.information.BasicResponse;
import com.example.demo.responses.information.DeleteSelectionTimeByIdRequest;
import com.example.demo.responses.information.GetSelectionTimeResponse;

import javax.jws.soap.SOAPBinding;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping()
public class SelectionTimeController {
    private final SelectionTimeRepository selectionTimeRepository;

    @Autowired
    public SelectionTimeController(SelectionTimeRepository selectionTimeRepository) {
        this.selectionTimeRepository = selectionTimeRepository;
    }

    @PostMapping("/selection_time/register")
    @Authorization
    @ResponseStatus(HttpStatus.CREATED)
    public BasicResponse addRegisterTime(@CurrentUser UserEntity user, @RequestBody AddSelectionTimeRequest request) {
        Timestamp startTime = request.getStart();
        Timestamp endTime = request.getEnd();

        /*
        if (!user.readTypeName().equals("System administrator") && !user.readTypeName().equals("Teaching administrator")) {
            throw new PermissionDeniedException();
        }*/

        SelectionTimeEntity selectionTimeEntity;
        Optional<SelectionTimeEntity> selectionTimeEntityOptional = selectionTimeRepository.findByStartAndEnd(startTime, endTime);
        if (!selectionTimeEntityOptional.isPresent()) {
            selectionTimeEntity = new SelectionTimeEntity();
            selectionTimeEntity.setComplement(false);
            selectionTimeEntity.setDrop(false);
            selectionTimeEntity.setRegister(true);
            selectionTimeEntity.setStart(startTime);
            selectionTimeEntity.setEnd(endTime);
        } else {
            selectionTimeEntity = selectionTimeEntityOptional.get();
            selectionTimeEntity.setRegister(true);
        }
        selectionTimeRepository.save(selectionTimeEntity);

        return new BasicResponse("设置选课时间成功。");
    }

    @PostMapping("/selection_time/complement")
    @Authorization
    @ResponseStatus(HttpStatus.CREATED)
    public BasicResponse addComplementTime(@RequestBody AddSelectionTimeRequest request) {
        Timestamp startTime = request.getStart();
        Timestamp endTime = request.getEnd();

        SelectionTimeEntity selectionTimeEntity;
        Optional<SelectionTimeEntity> selectionTimeEntityOptional = selectionTimeRepository.findByStartAndEnd(startTime, endTime);
        if (!selectionTimeEntityOptional.isPresent()) {
            selectionTimeEntity = new SelectionTimeEntity();
            selectionTimeEntity.setComplement(true);
            selectionTimeEntity.setDrop(false);
            selectionTimeEntity.setRegister(false);
            selectionTimeEntity.setStart(startTime);
            selectionTimeEntity.setEnd(endTime);
        } else {
            selectionTimeEntity = selectionTimeEntityOptional.get();
            selectionTimeEntity.setComplement(true);
        }

        selectionTimeRepository.save(selectionTimeEntity);

        return new BasicResponse("设置补选时间成功。");
    }

    @PostMapping("/selection_time/drop")
    @Authorization
    @ResponseStatus(HttpStatus.CREATED)
    public BasicResponse addDropTime(@RequestBody AddSelectionTimeRequest request) {
        Timestamp startTime = request.getStart();
        Timestamp endTime = request.getEnd();

        SelectionTimeEntity selectionTimeEntity;
        Optional<SelectionTimeEntity> selectionTimeEntityOptional = selectionTimeRepository.findByStartAndEnd(startTime, endTime);
        if (!selectionTimeEntityOptional.isPresent()) {
            selectionTimeEntity = new SelectionTimeEntity();
            selectionTimeEntity.setComplement(false);
            selectionTimeEntity.setDrop(true);
            selectionTimeEntity.setRegister(false);
            selectionTimeEntity.setStart(startTime);
            selectionTimeEntity.setEnd(endTime);
        } else {
            selectionTimeEntity = selectionTimeEntityOptional.get();
            selectionTimeEntity.setDrop(true);
        }

        selectionTimeRepository.save(selectionTimeEntity);

        return new BasicResponse("设置退课时间成功。");
    }

    @GetMapping("/selection_time/show")
    @Authorization
    @ResponseStatus(HttpStatus.OK)
    public GetSelectionTimeResponse showSelectionTime() {
        return new GetSelectionTimeResponse(selectionTimeRepository.findAll());
    }

    @DeleteMapping("/selection_time/deleteById")
    @Authorization
    @ResponseStatus(HttpStatus.OK)
    public BasicResponse deleteSelectionTimeById(@RequestBody DeleteSelectionTimeByIdRequest request) {
        Long id = request.getId();
        // Error: Selection time entity not exist
        SelectionTimeEntity selectionTimeEntity = selectionTimeRepository.findById(id).orElseThrow(SelectionTimeNotFoundException::new);
        selectionTimeRepository.delete(selectionTimeEntity);
        return new BasicResponse("Selection time deleted");
    }

    @PutMapping("/selection_time/modify/start")
    @Authorization
    @ResponseStatus(HttpStatus.OK)
    public BasicResponse modifyStartTime(@RequestBody ModifySelectionTimeStartRequest request) {
        Long id = request.getId();
        // Error: Selection time entity not exist
        SelectionTimeEntity selectionTimeEntity = selectionTimeRepository.findById(id).orElseThrow(SelectionTimeNotFoundException::new);

        selectionTimeEntity.setStart(request.getStart());
        selectionTimeRepository.save(selectionTimeEntity);
        return new BasicResponse("Start time modified");
    }

    @PutMapping("/selection_time/modify/end")
    @Authorization
    @ResponseStatus(HttpStatus.OK)
    public BasicResponse modifyEndTime(@RequestBody ModifySelectionTimeEndRequest request) {
        Long id = request.getId();
        // Error: Selection time entity not exist
        SelectionTimeEntity selectionTimeEntity = selectionTimeRepository.findById(id).orElseThrow(SelectionTimeNotFoundException::new);
        selectionTimeEntity.setEnd(request.getEnd());
        selectionTimeRepository.save(selectionTimeEntity);

        return new BasicResponse("End time modified");

    }
/*
    @PutMapping("/selection_time/modify/register")
    @Authorization
    @ResponseStatus(HttpStatus.OK)
    public BasicResponse setRegister(@RequestBody )
    */
}
