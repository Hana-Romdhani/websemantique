package com.greenlink.config;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;

import com.greenlink.utils.FileTool;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;

public class JenaEngine {
    private static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    static public Model readModel(String inputDataFile) {
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(inputDataFile);
        if (in == null) {
            System.out.println("Ontology file: " + inputDataFile + " not found");
            return null;
        }
        model.read(in, "");
        try {
            in.close();
        } catch (IOException e) {
            return null;
        }
        return model;
    }

    static public Model readInferencedModelFromRuleFile(Model model, String inputRuleFile) {
        InputStream in = FileManager.get().open(inputRuleFile);
        if (in == null) {
            System.out.println("Rule File: " + inputRuleFile + " not found");
            return null;
        }
        List<Rule> rules = Rule.rulesFromURL(inputRuleFile);
        GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
        InfModel inf = ModelFactory.createInfModel(reasoner, model);
        return inf;
    }

    static public String executeQuery(Model model, String queryString) {
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qe.execSelect();
            return ResultSetFormatter.asText(results);
        }
    }

    static public String executeQueryFile(Model model, String filepath) {
        File queryFile = new File(filepath);
        String queryString = FileTool.getContents(queryFile);
        return executeQuery(model, queryString);
    }
}
