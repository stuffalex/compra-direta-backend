package br.com.desafio.compradireta.adapter.entrypoint.controller.unidadecompradora;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.compradireta.domain.usecase.unidadecompradora.listar.ListarUnidadesCompradorasUseCase;
import br.com.desafio.compradireta.domain.usecase.unidadecompradora.listar.UnidadeCompradoraOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/unidades-compradoras")
@RequiredArgsConstructor
@Tag(name = "Unidades Compradoras", description = "Consulta de unidades compradoras cadastradas")
public class UnidadeCompradoraController {

    private final ListarUnidadesCompradorasUseCase listarUnidadesCompradorasUseCase;

    @GetMapping
    @Operation(summary = "Lista todas as unidades compradoras")
    public ResponseEntity<List<UnidadeCompradoraOutput>> listar() {
        return ResponseEntity.ok(listarUnidadesCompradorasUseCase.executar());
    }
}
