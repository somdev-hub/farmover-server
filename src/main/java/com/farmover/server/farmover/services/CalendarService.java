package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.payloads.CalendarEventsDto;

public interface CalendarService {

    public List<CalendarEventsDto> getCalendarEvents(String email);

}
