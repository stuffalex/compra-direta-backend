package br.com.desafio.compradireta.domain.usecase.compradireta.listar;

import java.util.List;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.pagination.Pagina;
import br.com.desafio.compradireta.domain.pagination.Paginacao;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutputConverter;
import lombok.Builder;

@Builder
public class ListarComprasDiretasUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;

    public List<CompraDiretaResumoOutput> executar() {
        return compraDiretaDataProvider.listarTodas().stream()
                .map(CompraDiretaResumoOutputConverter::converter)
                .collect(Collectors.toList());
    }

    public Pagina<CompraDiretaResumoOutput> executar(Paginacao paginacao) {
        return compraDiretaDataProvider.listarPaginado(paginacao)
                .map(CompraDiretaResumoOutputConverter::converter);
    }
}
