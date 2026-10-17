package br.com.desafio.compradireta.adapter.entrypoint.controller.fornecedor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.compradireta.domain.usecase.fornecedor.listar.FornecedorOutput;
import br.com.desafio.compradireta.domain.usecase.fornecedor.listar.ListarFornecedoresUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fornecedores")
@RequiredArgsConstructor
@Tag(name = "Fornecedores", description = "Consulta de fornecedores cadastrados")
public class FornecedorController {

    private final ListarFornecedoresUseCase listarFornecedoresUseCase;

    @GetMapping
    @Operation(summary = "Lista todos os fornecedores")
    public ResponseEntity<List<FornecedorOutput>> listar() {
        return ResponseEntity.ok(listarFornecedoresUseCase.executar());
    }
}
