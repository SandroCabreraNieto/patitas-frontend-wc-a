package pe.edu.cibertec.patitas_frontend_wc.Controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.patitas_frontend_wc.client.AutenticacionClient;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginResponseDTO;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginControllerAsync {

    @Autowired
    private AutenticacionClient autenticacionClient;

    @PostMapping("/autenticar-async-cloud")
    public Mono<LoginResponseDTO> autenticar(@RequestBody LoginRequestDTO loginRequestDTO) {
        // Validar campos de entrada
        if (loginRequestDTO.tipoDocumento() == null || loginRequestDTO.tipoDocumento().trim().isEmpty() ||
                loginRequestDTO.numeroDocumento() == null || loginRequestDTO.numeroDocumento().trim().isEmpty() ||
                loginRequestDTO.password() == null || loginRequestDTO.password().trim().isEmpty()) {

            return Mono.just(new LoginResponseDTO("01", "Error: Debe completar correctamente sus credenciales", "", ""));
        }

        // Consumir servicio de autenticación
        return Mono.fromCallable(() -> autenticacionClient.login(loginRequestDTO))
                .flatMap(responseEntity -> {
                    if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                        LoginResponseDTO response = responseEntity.getBody();
                        if ("00".equals(response.codigo())) {
                            return Mono.just(new LoginResponseDTO("00", "", response.nombreUsuario(), ""));
                        } else {
                            return Mono.just(new LoginResponseDTO("02", "Error: Autenticación fallida", "", ""));
                        }
                    } else {
                        return Mono.just(new LoginResponseDTO("02", "Error: Autenticación fallida", "", ""));
                    }
                })
                .onErrorReturn(new LoginResponseDTO("99", "Error: Ocurrió un problema en la autenticación", "", ""));
    }

    // Implementando cierre de sesión
    @PostMapping("/cerrarSesion")
    public Mono<ResponseEntity<Void>> cerrarSesion(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        return Mono.fromCallable(() -> {
            ResponseEntity<Void> responseEntity = autenticacionClient.logout(logoutRequestDTO);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok().build(); // Retorna un 200 OK
            } else {
                return ResponseEntity.status(responseEntity.getStatusCode()).build(); // Retorna el código de estado original
            }
        }).then(Mono.just(ResponseEntity.ok().build())); // Indica que el Mono se completa con un 200 OK
    }
}
