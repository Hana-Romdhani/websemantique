package com.greenlink.service;
import com.greenlink.repository.ConseilRepo;
import org.apache.jena.rdf.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConseilService {
    @Autowired
    private ConseilRepo ontologyRepository;

    public void addConseilEnAttente(String titreConseil, String contenuConseil) {
        ontologyRepository.addConseilEnAttente(titreConseil, contenuConseil);
    }

    public void approveConseil(String id) {
        ontologyRepository.approveConseil(id);
    }

    public List<Resource> getAllApprovedConseils() {
        return ontologyRepository.getAllApprovedConseils();
    }

    public void addCommentToConseil(String id, String contenuCommentaire) {
        ontologyRepository.addCommentToConseil(id, contenuCommentaire);
    }

    public List<Resource> getCommentairesByConseilId(String id) {
        return ontologyRepository.getCommentairesByConseilId(id);
    }
}
