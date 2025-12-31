package com.example.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
@Tag(name = "RAG接口", description = "知识库检索增强生成相关接口")
public class RagController {

    @PostMapping("/upload")
    @Operation(summary = "上传文档", description = "上传文档到知识库")
    public ResponseEntity<Map<String, Object>> uploadDocument(@RequestParam("file") MultipartFile file) {
        log.info("Uploading document: {}", file.getOriginalFilename());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("filename", file.getOriginalFilename());
        response.put("size", file.getSize());
        response.put("message", "文档上传成功，正在处理中...");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/query")
    @Operation(summary = "知识库问答", description = "基于知识库的智能问答")
    public ResponseEntity<Map<String, Object>> queryKnowledge(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        log.info("RAG query: {}", question);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("question", question);
        response.put("answer", "这是基于知识库的回答示例。请配置Dify知识库或本地向量存储后使用完整功能。");
        response.put("sources", new String[]{"示例文档.pdf"});
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/documents")
    @Operation(summary = "获取文档列表", description = "获取知识库中的文档列表")
    public ResponseEntity<Map<String, Object>> listDocuments() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documents", new String[]{});
        response.put("total", 0);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/documents/{documentId}")
    @Operation(summary = "删除文档", description = "从知识库中删除文档")
    public ResponseEntity<Map<String, Object>> deleteDocument(@PathVariable String documentId) {
        log.info("Deleting document: {}", documentId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "文档已删除");
        
        return ResponseEntity.ok(response);
    }
}
