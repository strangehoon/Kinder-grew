package com.sparta.finalproject.global.procedure;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyStoredProcService {
    private final JdbcTemplate jdbcTemplate;

    public void callMyStoredProc() {
        jdbcTemplate.execute("CALL attendance_add_procedure()");
    }
}
