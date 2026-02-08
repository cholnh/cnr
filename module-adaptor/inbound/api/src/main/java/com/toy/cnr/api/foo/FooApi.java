package com.toy.cnr.api.foo;

import com.toy.cnr.api.foo.request.FooCreateRequest;
import com.toy.cnr.api.foo.request.FooUpdateRequest;
import com.toy.cnr.api.foo.response.FooResponse;
import com.toy.cnr.api.foo.usecase.FooUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/foo")
public class FooApi {

    private final FooUseCase fooUseCase;

    public FooApi(FooUseCase fooUseCase) {
        this.fooUseCase = fooUseCase;
    }

    @GetMapping
    public ResponseEntity<List<FooResponse>> findAll() {
        return ResponseEntity.ok(fooUseCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return fooUseCase.findFooWithBusinessLogic(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FooCreateRequest request) {
        return fooUseCase.create(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
        @PathVariable Long id,
        @RequestBody FooUpdateRequest request
    ) {
        return fooUseCase.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fooUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
