package com.greenlink.utils;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateProcessor;

public class SparqlUtils {

    private Model model; // Your RDF model goes here
    private String endpointUrl; // The endpoint URL for your SPARQL service

    public SparqlUtils(Model model, String endpointUrl) {
        this.model = model;
        this.endpointUrl = endpointUrl;
    }

    // 1. Add a ConseilEnAttente
    public void addConseilEnAttente(String titre, String contenu, String dateSoumission) {
        String query = String.format(
                "INSERT DATA { " +
                        "  _:conseil a <ConseilEnAttente> ; " +
                        "              <titreConseil> \"%s\" ; " +
                        "              <contenuConseil> \"%s\" ; " +
                        "              <dateSoumission> \"%s\" . " +
                        "}",
                titre, contenu, dateSoumission
        );
        executeUpdate(query);
    }

    // 2. Approve a ConseilEnAttente
    public void approveConseilEnAttente(Resource conseil) {
        String query = String.format(
                "DELETE { %s a <ConseilEnAttente> } " +
                        "INSERT { %s a <ConseilApprouve> } " +
                        "WHERE { %s a <ConseilEnAttente> . }",
                conseil.toString(), conseil.toString(), conseil.toString()
        );
        executeUpdate(query);
    }

    // 3. Get All Approved ConseilApprouve
    public ResultSet getAllApprovedConseils() {
        String query = "SELECT ?conseil WHERE { ?conseil a <ConseilApprouve> . }";
        return executeQuery(query);
    }

    // 4. Add a CommentaireVisiteur
    public void addCommentaireVisiteur(String contenu, String dateCommentaire, Resource conseil) {
        String query = String.format(
                "INSERT DATA { " +
                        "  _:commentaire a <CommentaireVisiteur> ; " +
                        "                  <contenuCommentaire> \"%s\" ; " +
                        "                  <dateCommentaire> \"%s\" ; " +
                        "                  <commentaireSur> <%s> . " +
                        "}",
                contenu, dateCommentaire, conseil.toString()
        );
        executeUpdate(query);
    }

    // 5. Get All Comments for a ConseilApprouve
    public ResultSet getAllCommentsForConseil(Resource conseil) {
        String query = String.format(
                "SELECT ?commentaire WHERE { " +
                        "  ?commentaire a <Commentaire> . " +
                        "  ?commentaire <commentaireSur> <%s> . " +
                        "}",
                conseil.toString()
        );
        return executeQuery(query);
    }

    private void executeUpdate(String query) {
        // Create the UpdateRequest using UpdateFactory
        UpdateRequest updateRequest = UpdateFactory.create(query);
        UpdateProcessor processor = UpdateExecutionFactory.create(updateRequest, (Dataset) model);
        processor.execute();
    }


    private ResultSet executeQuery(String query) {
        QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
        return queryExecution.execSelect();
    }
}

