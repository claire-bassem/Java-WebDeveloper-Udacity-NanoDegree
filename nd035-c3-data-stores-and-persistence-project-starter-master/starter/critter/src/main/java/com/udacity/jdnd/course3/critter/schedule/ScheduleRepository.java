package com.udacity.jdnd.course3.critter.schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // List<Schedule> getScheduleByPets_Id(Long id);
    // List<Schedule> getScheduleByEmployees_Id(Long employeeId);
}
