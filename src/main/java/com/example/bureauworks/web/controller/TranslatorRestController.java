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

import com.example.bureauworks.core.entity.Translator;
import com.example.bureauworks.core.service.TranslatorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/translator")
@RequiredArgsConstructor
public class TranslatorRestController extends BaseRestController { 

    private final TranslatorService service;

    @Operation(summary = "List/search all translators. It is possible to search by name and email.")
	@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucessufully searched for people"),
        @ApiResponse(responseCode = "204", description = "Sucessufully searcher for people, but no results were found."),
    })
    @GetMapping
    public ResponseEntity<Page<Translator>> findAll(
                @RequestParam(required = false, defaultValue = "") final String name,
                @RequestParam(required = false, defaultValue = "") final String email,
                @RequestParam(defaultValue = "0") final Integer page,
                @RequestParam(defaultValue = "10") final Integer size,
                @RequestParam(defaultValue = "ASC") final String orderDirection,
                @RequestParam(defaultValue = "name") final String orderBy) {

        final PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(orderDirection), orderBy);

        return writeResponseBody(service.findAll(pageRequest, name, email));
    }

    @PostMapping
    @Operation(summary = "Create a new translator.") 
    public ResponseEntity<Translator> save(@RequestBody final Translator translator) {
        return writeResponseBody(service.save(translator));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get/find a translator by id.")
    public ResponseEntity<Translator> findByIdQuietly(@PathVariable(required = true) final Integer id) {
        return writeResponseBody(service.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a translator .")
    public ResponseEntity<Translator> update(@PathVariable(required = true) final Integer id, @RequestBody final Translator translator) {
        return writeResponseBody(service.update(id, translator));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a translator .")
    public void deleteById(@PathVariable(required = true) final Integer id) {
        service.delete(id);
    }
    
}
