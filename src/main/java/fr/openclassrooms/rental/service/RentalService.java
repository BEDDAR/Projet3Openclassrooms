package fr.openclassrooms.rental.service;

import fr.openclassrooms.rental.entite.Rental;
import fr.openclassrooms.rental.entite.Utilisateur;
import fr.openclassrooms.rental.repository.RentalRepository;
import fr.openclassrooms.rental.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RentalService {

    private RentalRepository rentalRepository;
    private UtilisateurRepository utilisateurRepository;

    public Rental createRentalForUser(Integer idUtilisateur, Rental rental) {

        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        rental.setUtilisateur(utilisateur);
        return rentalRepository.save(rental);
    }

    public List<Rental> getAllRentals (){
        return rentalRepository.findAll();
    }
}
