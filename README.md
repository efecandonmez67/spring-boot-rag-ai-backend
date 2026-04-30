# 🤖 Spring Boot Local RAG API (AI-Powered Knowledge Base)

This project is a **100% locally running** RAG (Retrieval-Augmented Generation) backend system. It doesn't rely on any external or paid AI APIs (like OpenAI). 

In short: You load a massive PDF into the system, it reads and processes it, and then answers your questions based *strictly* on the information found inside that PDF.

## 🛠️ Tech Stack
* **Backend:** Java 21 & Spring Boot 3.3.0
* **AI Integration:** Spring AI
* **LLM Engine:** Ollama (Fully local)
* **Chat Model:** Llama 3.2
* **Embedding Model:** Nomic-embed-text
* **Database:** PostgreSQL + pgvector (via Docker)

## ⚙️ How It Works

The system consists of two main phases:

1. **Ingestion (Reading & Saving):** The PDF document is split into small, logical paragraphs (chunks). The Nomic model converts these text chunks into vectors (number arrays) and saves them into the PostgreSQL database.
2. **RAG (Asking Questions):** When a user asks a question, the system searches the database for the most relevant paragraphs. It then feeds these paragraphs to Llama 3.2, instructing it: "Answer the user's question using *only* this provided context."

## 🚀 Setup & Run

To get the project running on your local machine, follow these steps:

1. Download [Ollama](https://ollama.com/) and pull the required models via terminal:
   ```bash
   ollama pull llama3.2
   ollama pull nomic-embed-text
2. Open Docker and spin up the database:
    ```bash
    docker-compose up -d
3. Run the Spring Boot application.

🌐 API Endpoints
GET /ai/chat?message=...  ---Normal chat directly with Llama 3.2 (Bypasses the database).
GET /ai/ingest            ---Reads the PDF from the resources/docs folder, chunks it, and saves it to the vector database.  
GET /ai/ask?question=...  ---The Main Event: Answers your question intelligently based on the ingested PDF context.
