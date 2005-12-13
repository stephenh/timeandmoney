/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.doctorAppointments;

import java.util.*;

import com.domainlanguage.time.*;

class AppointmentCalendar {
    TimeZone defaultZone;
    Set events = new HashSet();
    private Long id; //for Hibernate
    
    AppointmentCalendar(TimeZone zone) {
        defaultZone = zone;
    }
    AppointmentCalendar() {
        //for Hibernate
    }

    void add(Appointment anEvent) {
        events.add(anEvent);
    }

    List dailyScheduleFor(CalendarDate calDate) {
        List daysAppointments = new ArrayList();
        TimeInterval day = calDate.asTimeInterval(defaultZone);
        Iterator it = events.iterator();
        while (it.hasNext()) {
            Appointment event = (Appointment) it.next();
            if (event.getTimeInterval().intersects(day)) {
                daysAppointments.add(event);
            }
        }
        return daysAppointments;
    }
    
    //for Hibernate
    private String getDefaultZoneIdForPersistentMapping() {
        return defaultZone.getID();
    }
    private void setDefaultZoneIdForPersistentMapping(String name) {
        defaultZone=TimeZone.getTimeZone(name);
    }
    private Long getIdForPersistentMapping() {
        return id;
    }
    private void setIdForPersistentMapping(Long id) {
        this.id = id;
    }
    private Set getEventsForPersistentMapping() {
        return events;
    }
    private void setEventsForPersistentMapping(Set events) {
        this.events = events;
    }

}