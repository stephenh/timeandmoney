/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.doctorAppointments;

import com.domainlanguage.time.*;

class Appointment {
    private Long id; //for Hibernate
    private TimeInterval timeInterval;

    public Appointment(TimeInterval interval) {
        timeInterval=interval;
    }
    Appointment() {
        //only for Hibernate
    }
    public TimeInterval getTimeInterval() {
        return timeInterval;
    }
    //for Hibernate
    private Long getIdForPersistentMapping() {
        return id;
    }
    private void setIdForPersistentMapping(Long id) {
        this.id = id;
    }
    private TimeInterval getTimeIntervalForPersistentMapping() {
        return timeInterval;
    }
    private void setTimeIntervalForPersistentMapping(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }
}