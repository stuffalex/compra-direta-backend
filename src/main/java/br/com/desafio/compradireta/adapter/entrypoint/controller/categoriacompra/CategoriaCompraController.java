package br.com.desafio.compradireta.adapter.entrypoint.controller.categoriacompra;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.compradireta.domain.usecase.categoriacompra.listar.CategoriaCompraOutput;
import br.com.desafio.compradireta.domain.usecase.categoriacompra.listar.ListarCategoriasCompraUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categorias-compra")
@RequiredArgsConstructor
@Tag(name = "Categorias de Compra", description = "Consulta de categorias de compra cadastradas")
public class CategoriaCompraController {

    private final ListarCategoriasCompraUseCase listarCategoriasCompraUseCase;

    @GetMapping
    @Operation(summary = "Lista todas as categorias de compra")
    public ResponseEntity<List<CategoriaCompraOutput>> listar() {
        return ResponseEntity.ok(listarCategoriasCompraUseCase.executar());
    }
}
