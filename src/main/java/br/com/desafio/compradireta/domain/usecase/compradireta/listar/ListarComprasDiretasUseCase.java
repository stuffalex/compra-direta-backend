package br.com.desafio.compradireta.domain.usecase.compradireta.listar;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.pagination.Pagina;
import br.com.desafio.compradireta.domain.pagination.Paginacao;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutputConverter;
import lombok.Builder;

@Builder
public class ListarComprasDiretasUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;

    public Pagina<CompraDiretaResumoOutput> executar(Paginacao paginacao) {
        return compraDiretaDataProvider.listarPaginado(paginacao)
                .map(CompraDiretaResumoOutputConverter::converter);
    }
}
