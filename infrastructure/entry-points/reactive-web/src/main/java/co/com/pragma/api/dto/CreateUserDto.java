package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record CreateUserDto(String name, String lastName, @JsonFormat(pattern = "dd-MM-yyyy") LocalDate birthday, String address, String email,
                            String documentId, String phone, Integer baseSalary) { }
