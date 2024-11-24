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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.management.remote.JMXAuthenticator;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/auth")
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

    @PostMapping(path = "login")
    public Map<String, String> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.email(), authentificationDTO.password())
        );
    log.info("resultat {}",authenticate.isAuthenticated());
       if(authenticate.isAuthenticated()) {
            return this.jwtService.generate(authentificationDTO.email());
        }
        return null;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non fourni ou invalide");
        }

        String token = authHeader.substring(7);
        UserDetails userDetails;
        // Validez le token
        if (!jwtService.isTokenGloballyValid(token) ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide");
        }

        // Extrait le nom d'utilisateur du token
        String username = jwtService.extractUsername(token);

        // Récupère les informations utilisateur
        Utilisateur utilisateur = utilisateurService.loadUserByUsername(username);

        return ResponseEntity.ok(utilisateur);
    }
}
