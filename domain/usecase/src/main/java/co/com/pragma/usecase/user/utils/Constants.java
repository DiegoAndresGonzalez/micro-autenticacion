package co.com.pragma.usecase.user.utils;

import java.util.regex.Pattern;

public class Constants {

    public static final String INVALID_USER_DATA = "Datos del usuario invalidos o vacios";
    public static final Integer NUMBER_ZERO = 0;
    public static final Long ID_TWO = 2L;
    public static final String EMAIL_EXISTS = "El correo electronico proporcionado ya se encuentra registrado.";
    public static final String DOCUMENT_EXISTS = "El documento proporcionado ya se encuentra registrado.";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    public static final String INVALID_EMAIL_FORMAT = "El formato del correo electrónico no es válido.";
    public static final String SALARY_OUT_OF_RANGE = "El salario base no puede ser mayor a 15,000,000.";
    public static final double MAX_SALARY = 15000000;

    public static final Pattern DOCUMENT_PATTERN = Pattern.compile("^[1-9]\\d{5,9}$");
}
