package br.com.desafio.compradireta.domain.usecase.unidadecompradora.listar;

import java.util.List;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.IUnidadeCompradoraDataProvider;
import br.com.desafio.compradireta.domain.usecase.unidadecompradora.listar.converter.UnidadeCompradoraOutputConverter;
import lombok.Builder;

@Builder
public class ListarUnidadesCompradorasUseCase {

    private final IUnidadeCompradoraDataProvider unidadeCompradoraDataProvider;

    public List<UnidadeCompradoraOutput> executar() {
        return unidadeCompradoraDataProvider.listarTodas().stream()
                .map(UnidadeCompradoraOutputConverter::converter)
                .collect(Collectors.toList());
    }
}
