package com.efecandonmez.rag_backend.controllers;


import com.efecandonmez.rag_backend.services.DocumentIngestionService;
import com.efecandonmez.rag_backend.services.RagService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

    private final ChatModel chatModel;
    private final DocumentIngestionService ingestionService;
    private final RagService ragService;

    public AiController(ChatModel chatModel, DocumentIngestionService ingestionService, RagService ragService) {
        this.chatModel = chatModel;
        this.ingestionService = ingestionService;
        this.ragService = ragService;
    }

    @GetMapping("/ai/chat")
    public String chat(@RequestParam String message) {
        return chatModel.call(message);
    }

    @GetMapping("/ai/ingest")
    public String ingestData() {
        return ingestionService.loadPdfToDatabase();
    }

    @GetMapping("/ai/ask")
    public String ask(@RequestParam String question) {
        return ragService.askQuestion(question);
    }

}
