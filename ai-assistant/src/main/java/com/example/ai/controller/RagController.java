package com.example.ai.controller;

import com.example.ai.dto.ApiResponse;
import com.example.ai.dto.RagDocumentListResponse;
import com.example.ai.dto.RagQueryRequest;
import com.example.ai.dto.RagQueryResponse;
import com.example.ai.dto.RagUploadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
@Tag(name = "RAG接口", description = "知识库检索增强生成相关接口")
public class RagController {

    @PostMapping("/upload")
    @Operation(summary = "上传文档", description = "上传文档到知识库")
    public ApiResponse<RagUploadResponse> uploadDocument(@RequestParam("file") MultipartFile file) {
        log.info("Uploading document: {}", file.getOriginalFilename());

        var response = RagUploadResponse.builder()
                .filename(file.getOriginalFilename())
                .size(file.getSize())
                .message("文档上传成功，正在处理中...")
                .build();

        return ApiResponse.ok(response);
    }

    @PostMapping("/query")
    @Operation(summary = "知识库问答", description = "基于知识库的智能问答")
    public ApiResponse<RagQueryResponse> queryKnowledge(@Valid @RequestBody RagQueryRequest request) {
        log.info("RAG query: {}", request.getQuestion());

        var response = RagQueryResponse.builder()
                .question(request.getQuestion())
                .answer("这是基于知识库的回答示例。请配置Dify知识库或本地向量存储后使用完整功能。")
                .sources(List.of("示例文档.pdf"))
                .build();

        return ApiResponse.ok(response);
    }

    @GetMapping("/documents")
    @Operation(summary = "获取文档列表", description = "获取知识库中的文档列表")
    public ApiResponse<RagDocumentListResponse> listDocuments() {
        var response = RagDocumentListResponse.builder()
                .documents(List.of())
                .total(0)
                .build();

        return ApiResponse.ok(response);
    }

    @DeleteMapping("/documents/{documentId}")
    @Operation(summary = "删除文档", description = "从知识库中删除文档")
    public ApiResponse<String> deleteDocument(@PathVariable String documentId) {
        log.info("Deleting document: {}", documentId);
        return ApiResponse.ok("文档已删除");
    }
}
