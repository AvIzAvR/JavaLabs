package com.java.labs.avi.repository;

import com.java.labs.avi.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query(value = "SELECT s.* FROM schedules s JOIN groups g ON s.group_id = g.id WHERE g.name = :groupName AND s.day_of_week = :dayOfWeek AND s.week_number = :weekNumber AND s.num_subgroup = :numSubgroup", nativeQuery = true)
    List<Schedule> findByGroupNameAndDayOfWeekAndWeekNumberAndNumSubgroup(@Param("groupName") String groupName, @Param("dayOfWeek") String dayOfWeek, @Param("weekNumber") int weekNumber, @Param("numSubgroup") int numSubgroup);

    List<Schedule> findByGroupNameAndDayOfWeekAndWeekNumberAndNumSubgroupAndStartTimeAndEndTime(
            String groupName, String dayOfWeek, int weekNumber, int numSubgroup, String startTime, String endTime);
    List<Schedule> findByAuditoriumId(Long id);
}
