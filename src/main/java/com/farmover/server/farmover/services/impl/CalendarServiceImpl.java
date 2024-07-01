package com.farmover.server.farmover.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Production;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.CalendarEventsDto;
import com.farmover.server.farmover.repositories.ProductionRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.services.CalendarService;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private ProductionRepo productionRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<CalendarEventsDto> getCalendarEvents(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));

        List<Production> productions = productionRepo.findByFarmer(user)
                .orElseThrow(() -> new ResourceNotFoundException("production", "farmer", user.getEmail()));

        List<CalendarEventsDto> calendarEvents = productions.stream()
                .flatMap(production -> production.getCropActivities().stream()
                        .map(activity -> {
                            CalendarEventsDto calendarEventsDto = new CalendarEventsDto();
                            calendarEventsDto.title = activity.getActivityTitle();
                            calendarEventsDto.start = activity.getStartDate();
                            calendarEventsDto.end = activity.getEndDate();
                            return calendarEventsDto;
                        }))
                .collect(Collectors.toList());

        return calendarEvents;

    }

}
