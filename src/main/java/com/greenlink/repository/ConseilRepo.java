package com.greenlink.repository;
import com.greenlink.config.JenaEngine; // Adjust import based on your package structure
import jakarta.annotation.PostConstruct;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class ConseilRepo {
    @Autowired
    private Model model; // Load your ontology model here

    // Load the model at startup
    @PostConstruct
    public void init() {
        this.model = JenaEngine.readModel("path/to/your/ontology.owl");
    }

    public void addConseilEnAttente(String titreConseil, String contenuConseil) {
        Resource conseilResource = model.createResource(); // Create a new resource
        conseilResource.addProperty(RDF.type, model.createResource("http://example.org#ConseilEnAttente"));
        conseilResource.addProperty(model.createProperty("http://example.org#titreConseil"), titreConseil);
        conseilResource.addProperty(model.createProperty("http://example.org#contenuConseil"), contenuConseil);
        // Optionally, save the model to the file or a database here
    }

    public void approveConseil(String id) {
        // Retrieve the ConseilEnAttente by ID and change its status to ConseilApprouve
        Resource conseilResource = model.getResource("http://example.org#" + id);
        if (conseilResource.hasProperty(RDF.type, model.createResource("http://example.org#ConseilEnAttente"))) {
            conseilResource.removeAll(RDF.type);
            conseilResource.addProperty(RDF.type, model.createResource("http://example.org#ConseilApprouve"));
            // Optionally, save the model to the file or a database here
        }
    }

    public List<Resource> getAllApprovedConseils() {
        List<Resource> approvedConseils = new ArrayList<>();
        String queryStr = "SELECT ?conseil WHERE { ?conseil a <http://example.org#ConseilApprouve> }";
        try (QueryExecution qexec = QueryExecutionFactory.create(queryStr, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                approvedConseils.add(soln.getResource("conseil"));
            }
        }
        return approvedConseils;
    }

    public void addCommentToConseil(String id, String contenuCommentaire) {
        Resource commentaireResource = model.createResource(); // Create a new resource for the comment
        commentaireResource.addProperty(RDF.type, model.createResource("http://example.org#CommentaireVisiteur"));
        commentaireResource.addProperty(model.createProperty("http://example.org#contenuCommentaire"), contenuCommentaire);
        commentaireResource.addProperty(model.createProperty("http://example.org#commentaireSur"), model.createResource("http://example.org#" + id));
        // Optionally, save the model to the file or a database here
    }

    public List<Resource> getCommentairesByConseilId(String id) {
        List<Resource> commentaires = new ArrayList<>();
        String queryStr = "SELECT ?commentaire WHERE { ?commentaire a <http://example.org#CommentaireVisiteur> . ?commentaire <http://example.org#commentaireSur> <http://example.org#" + id + "> }";
        try (QueryExecution qexec = QueryExecutionFactory.create(queryStr, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                commentaires.add(soln.getResource("commentaire"));
            }
        }
        return commentaires;
    }
}
