package com.example.bureauworks.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bureauworks.core.entity.Document;
import com.example.bureauworks.core.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/document")
public class DocumentRestController extends BaseRestController {

    private final DocumentService service;

    @Operation(summary = "List/search all documents. It is possible to search by subject and author.")
	@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucessufully searched for people"),
        @ApiResponse(responseCode = "204", description = "Sucessufully searcher for people, but no results were found."),
    })
    @GetMapping
    public ResponseEntity<Page<Document>> findAll(
                @RequestParam(required = false, defaultValue = "") final String author,
                @RequestParam(required = false, defaultValue = "") final String subject,
                @RequestParam(defaultValue = "0") final Integer page,
                @RequestParam(defaultValue = "10") final Integer size,
                @RequestParam(defaultValue = "ASC") final String orderDirection,
                @RequestParam(defaultValue = "author") final String orderBy) {
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(orderDirection), orderBy);
        return writeResponseBody(service.findAll(pageRequest, author, subject));
    }

    @PostMapping(value = "/file", consumes = {"multipart/form-data"})
    @Operation(summary = "Save a batch of documents entity from a file. It can be a CSV or xlsx file")
    public void insertBatch(@RequestParam(required = true) MultipartFile file) {
        service.insertBatch(file);
    }


    @PostMapping
    @Operation(summary = "Save a new document")
    public ResponseEntity<Document> save(@RequestBody Document document) {
        return writeResponseBody(service.save(document));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> update(@PathVariable(required = true) final Integer id, @RequestBody Document document) {
        return writeResponseBody(service.update(id, document));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> findById(@PathVariable(required = true) final Integer id) {
        return writeResponseBody(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(required = true) final Integer id) {
        service.delete(id);
    }
    
}
