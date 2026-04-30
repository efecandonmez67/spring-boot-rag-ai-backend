package com.efecandonmez.rag_backend.services;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public RagService(ChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    public String askQuestion(String question) {

        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.query(question).withTopK(3)
        );

        String context= similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        String systemMessageText = """
                Sen alanında uzman, profesyonel bir asistansın.
                Sana verilen 'Bağlam' (Context) bilgilerini kullanarak kullanıcının sorusunu cevapla.
                
                KATI KURALLAR:
                1. Cevabını KUSURSUZ, DOĞAL ve AKICI bir TÜRKÇE ile vermelisin.
                2. Asla İngilizce kelime (capabilities, deploy, framework, support vb.) veya yarı Türkçe (diseñlanmıştır) kelimeler KULLANMA. İngilizce terimlerin Türkçe karşılıklarını (özellik, yetenek, altyapı, destek vb.) kullan.
                3. Eğer sorunun cevabı bağlamda yoksa, sadece 'Yüklenen belgede bu bilgiye sahip değilim' de ve asla uydurma.
                
                Bağlam:
                {context}
                """;


        SystemPromptTemplate systemPromptTemplate=new SystemPromptTemplate(systemMessageText);
        Message systemMessage= systemPromptTemplate.createMessage(Map.of("context", context));
        UserMessage userMessage= new UserMessage(question);

        Prompt prompt= new Prompt(List.of(systemMessage, userMessage));
        return chatModel.call(prompt).getResult().getOutput().getContent();
    }

}
