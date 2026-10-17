package br.com.desafio.compradireta.domain.usecase.categoriacompra.listar;

import java.util.List;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICategoriaCompraDataProvider;
import br.com.desafio.compradireta.domain.usecase.categoriacompra.listar.converter.CategoriaCompraOutputConverter;
import lombok.Builder;

@Builder
public class ListarCategoriasCompraUseCase {

    private final ICategoriaCompraDataProvider categoriaCompraDataProvider;

    public List<CategoriaCompraOutput> executar() {
        return categoriaCompraDataProvider.listarTodas().stream()
                .map(CategoriaCompraOutputConverter::converter)
                .collect(Collectors.toList());
    }
}
