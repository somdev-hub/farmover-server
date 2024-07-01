package com.farmover.server.farmover.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.CalendarEventsDto;
import com.farmover.server.farmover.services.impl.CalendarServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarServiceImpl calendarService;

    @GetMapping("/get-events")
    public ResponseEntity<List<CalendarEventsDto>> getCalendarEvents(@RequestParam String email) {

        List<CalendarEventsDto> calendarEvents = calendarService.getCalendarEvents(email);

        return new ResponseEntity<List<CalendarEventsDto>>(calendarEvents, HttpStatus.OK);
    }

}
