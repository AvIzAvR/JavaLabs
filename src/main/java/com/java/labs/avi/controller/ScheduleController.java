package com.java.labs.avi.controller;

import com.java.labs.avi.dto.ScheduleDto;
import com.java.labs.avi.exception.BadRequestException;
import com.java.labs.avi.exception.GlobalExceptionHandler;
import com.java.labs.avi.model.Schedule;
import com.java.labs.avi.service.ScheduleService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getScheduleForDayOfWeek(
            @RequestParam @NotBlank @Pattern(regexp = "\\d+", message = "Group number must be numeric") String groupNumber,
            @RequestParam @NotBlank String dayOfWeek,
            @RequestParam @Min(1) int targetWeekNumber,
            @RequestParam @Min(0) int numSubgroup) {

        List<String> allowedDaysOfWeek = Arrays.asList("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье");

        if (!allowedDaysOfWeek.contains(dayOfWeek)) {
            throw new BadRequestException("Day of week is not valid");
        }

        List<ScheduleDto> scheduleDtos = scheduleService.getScheduleByGroupDayWeekAndSubgroup(groupNumber, dayOfWeek, targetWeekNumber, numSubgroup);
        return ResponseEntity.ok(scheduleDtos);
    }


    @PostMapping
    public ResponseEntity<ScheduleDto> createSchedule(@RequestBody ScheduleDto scheduleDto) {
        Schedule schedule = scheduleService.convertToEntity(scheduleDto);
        ScheduleDto createdScheduleDto = scheduleService.createSchedule(schedule);
        return new ResponseEntity<>(createdScheduleDto, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long id, @RequestBody ScheduleDto scheduleDetails) {
        ScheduleDto updatedScheduleDto = scheduleService.updateSchedule(id, scheduleDetails);
        return ResponseEntity.ok(updatedScheduleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleDto> patchSchedule(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        ScheduleDto updatedScheduleDto = scheduleService.patchSchedule(id, updates);
        return ResponseEntity.ok(updatedScheduleDto);
    }

    @DeleteMapping("/auditorium/{id}")
    public ResponseEntity<HttpStatus> deleteAuditorium(@PathVariable Long id) {
        scheduleService.deleteAuditorium(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<HttpStatus> deleteGroup(@PathVariable Long id) {
        scheduleService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/subject/{id}")
    public ResponseEntity<HttpStatus> deleteSubject(@PathVariable Long id) {
        scheduleService.deleteSubject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
