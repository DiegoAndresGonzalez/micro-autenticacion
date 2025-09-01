package co.com.pragma.api.dto;

public record LoginResponseDto (String accessToken, String tokenType, Long expiresIn, String email, Long roleId ) {
    public LoginResponseDto(String accessToken, Long expiresIn, String email, Long roleId) {
        this(accessToken, "Bearer", expiresIn, email, roleId);
    }
}
