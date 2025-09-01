package co.com.pragma.api.dto;

import java.time.LocalDate;

public record UserResponseDto(Long id, String name, String lastName, LocalDate birthday, String address, String email,
                              String documentId, String phone, Long roleId, Integer baseSalary) { }