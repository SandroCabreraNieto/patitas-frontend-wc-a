package pe.edu.cibertec.patitas_frontend_wc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.patitas_frontend_wc.Controler.LogoutRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginResponseDTO;

@FeignClient(name = "autenticacion", url = "http://localhost:8080/autenticacion")
public interface AutenticacionClient {
    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO);

    @PostMapping("/logout")
    ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO logoutRequestDTO);
}
