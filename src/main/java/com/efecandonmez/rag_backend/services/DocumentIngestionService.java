package com.efecandonmez.rag_backend.services;


import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;

    @Value("classpath:/docs/spring-boot-reference.pdf")
    private Resource pdfResource;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public String loadPdfToDatabase() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource);
        List<Document> rawDocuments = pdfReader.get();

        TokenTextSplitter textSplitter = new TokenTextSplitter();
        List<Document> chunkedDocuments = textSplitter.apply(rawDocuments);

        vectorStore.add(chunkedDocuments);

        return "PDF başarıyla parçalandı ve vektör veritabanına kaydedildi. Toplam parça sayısı: " + chunkedDocuments.size();

    }


}
