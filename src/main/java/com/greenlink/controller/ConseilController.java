package com.greenlink.controller;

import com.greenlink.service.ConseilService;
import org.apache.jena.rdf.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ontology")
public class ConseilController {

    @Autowired
    private ConseilService ontologyService;

    @PostMapping("/conseils")
    public ResponseEntity<Void> addConseilEnAttente(@RequestParam String titreConseil, @RequestParam String contenuConseil) {
        ontologyService.addConseilEnAttente(titreConseil, contenuConseil);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/conseils/{id}/approve")
    public ResponseEntity<Void> approveConseil(@PathVariable String id) {
        ontologyService.approveConseil(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/conseils/approved")
    public ResponseEntity<List<Resource>> getAllApprovedConseils() {
        List<Resource> approvedConseils = ontologyService.getAllApprovedConseils();
        return ResponseEntity.ok(approvedConseils);
    }

    @PostMapping("/conseils/{id}/comments")
    public ResponseEntity<Void> addCommentToConseil(@PathVariable String id, @RequestParam String contenuCommentaire) {
        ontologyService.addCommentToConseil(id, contenuCommentaire);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/conseils/{id}/comments")
    public ResponseEntity<List<Resource>> getCommentairesByConseilId(@PathVariable String id) {
        List<Resource> commentaires = ontologyService.getCommentairesByConseilId(id);
        return ResponseEntity.ok(commentaires);
    }
}
