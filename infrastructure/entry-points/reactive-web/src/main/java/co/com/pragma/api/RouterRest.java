package co.com.pragma.api;

import co.com.pragma.api.dto.UserResponseDto;
import co.com.pragma.api.utils.Constants;
import co.com.pragma.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = {RequestMethod.POST},
                    beanClass = Handler.class,
                    beanMethod = "createUser",
                    operation = @Operation(
                            operationId = "createUser",
                            summary = "Crear un usuario",
                            description = "Registra un nuevo usuario en el sistema",
                            responses = {
                                    @ApiResponse(responseCode = "201", description = Constants.LOG_SUCCESSFUL_REQUEST,
                                            content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                                    @ApiResponse(responseCode = "400", description = Constants.LOG_ERROR_HANDLER)
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/{documentId}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = {RequestMethod.GET},
                    beanClass = Handler.class,
                    beanMethod = "findUserByDocumentId",
                    operation = @Operation(
                            operationId = "findUserByDocumentId",
                            summary = "Obtener un usuario por su documento",
                            description = "Busca un usuario por su número de documento de identidad",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "documentId", required = true,
                                            description = "El número de documento del usuario")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                                            content = @Content(schema = @Schema(implementation = User.class))),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios")
                .and(accept(MediaType.APPLICATION_JSON)), handler::createUser)
                .andRoute(GET("/api/v1/usuarios/{documentId}")
                        .and(accept(MediaType.APPLICATION_JSON)), handler::findByDocumentId)
                .andRoute(POST("/api/v1/login")
                        .and(accept(MediaType.APPLICATION_JSON)), handler::login);
    }

}
