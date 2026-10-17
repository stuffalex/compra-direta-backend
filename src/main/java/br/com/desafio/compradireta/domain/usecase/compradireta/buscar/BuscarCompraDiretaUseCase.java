package br.com.desafio.compradireta.domain.usecase.compradireta.buscar;

import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.usecase.compradireta.buscar.converter.BuscarCompraDiretaOutputConverter;
import lombok.Builder;

@Builder
public class BuscarCompraDiretaUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;
    private final IPropostaDataProvider propostaDataProvider;

    public BuscarCompraDiretaOutput executar(Long id) {
        CompraDireta compraDireta = compraDiretaDataProvider.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Compra direta não encontrada"));

        return BuscarCompraDiretaOutputConverter.converter(compraDireta, propostaDataProvider.listarPorCompraDireta(id));
    }
}
