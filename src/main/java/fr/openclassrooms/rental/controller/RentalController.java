package fr.openclassrooms.rental.controller;

import fr.openclassrooms.rental.entite.Rental;
import fr.openclassrooms.rental.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping(value = "/{id_utilisateur}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Rental> createRental(
            @PathVariable("id_utilisateur") Integer idUtilisateur,
            @RequestParam("name") String name,
            @RequestParam("surface") String surface,
            @RequestParam("price") String price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile imageFile) {

        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        try {
            byte[] imageBytes = imageFile.getBytes();
            String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
            rental.setPicture(base64Image); // Enregistrement dans le champ `picture`
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        Rental createdRental = rentalService.createRentalForUser(idUtilisateur, rental);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRental);
    }

    @GetMapping
    public ResponseEntity<List<Rental>> getRentals(){
        return ResponseEntity.status(HttpStatus.OK).body(rentalService.getAllRentals());
    }
}