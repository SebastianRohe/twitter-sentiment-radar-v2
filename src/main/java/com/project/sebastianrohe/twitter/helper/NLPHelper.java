package com.project.sebastianrohe.twitter.helper;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.resource.ResourceInitializationException;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger;
import org.hucompute.textimager.uima.textblob.TextBlobSentiment;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/**
 * Class for encapsulating NLP methods and processing.
 */
public class NLPHelper {

    // AnalysisEngine for performing the processing. Is initialized only once.
    private AnalysisEngine analysisEngine = null;

    /**
     * Constructor.
     */
    public NLPHelper() {
        // Creating a pipeline (for Linux)
        AggregateBuilder pipeline = new AggregateBuilder();

        // Add different engines to the Pipeline
        try {
            pipeline.add(createEngineDescription(SpaCyMultiTagger.class));
            pipeline.add(createEngineDescription(TextBlobSentiment.class));

            // Create an AnalysisEngine for running the pipeline.
            this.analysisEngine = pipeline.createAggregate();

        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the analysis engine.
     *
     * @return Analysis engine.
     */
    public AnalysisEngine getAnalysisEngine() {
        return this.analysisEngine;
    }

}
