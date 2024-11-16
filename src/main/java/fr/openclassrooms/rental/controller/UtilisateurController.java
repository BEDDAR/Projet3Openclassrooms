package fr.openclassrooms.rental.controller;

import fr.openclassrooms.rental.dto.AuthentificationDTO;
import fr.openclassrooms.rental.entite.Utilisateur;
import fr.openclassrooms.rental.securite.JwtService;
import fr.openclassrooms.rental.service.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.management.remote.JMXAuthenticator;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurController {
    private UtilisateurService utilisateurService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(UtilisateurController.class);

    @GetMapping("test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test réussi");
    }

    @PostMapping(path = "register")
    public ResponseEntity<String> inscription(@RequestBody Utilisateur utilisateur) {
        try {
            log.info("Inscription");
            this.utilisateurService.inscription(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur créé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur d'inscription");
        }
    }

    @PostMapping(path = "email")
    public Map<String, String> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.login(), authentificationDTO.password())
        );
    log.info("resultat {}",authenticate.isAuthenticated());
       if(authenticate.isAuthenticated()) {
            return this.jwtService.generate(authentificationDTO.login());
        }
        return null;
    }
}
