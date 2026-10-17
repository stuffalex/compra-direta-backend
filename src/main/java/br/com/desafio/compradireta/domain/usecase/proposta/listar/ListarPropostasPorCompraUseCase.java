package br.com.desafio.compradireta.domain.usecase.proposta.listar;

import java.util.List;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.usecase.proposta.dto.PropostaOutput;
import br.com.desafio.compradireta.domain.usecase.proposta.dto.PropostaOutputConverter;
import lombok.Builder;

@Builder
public class ListarPropostasPorCompraUseCase {

    private final IPropostaDataProvider propostaDataProvider;

    public List<PropostaOutput> executar(Long compraDiretaId) {
        return propostaDataProvider.listarPorCompraDireta(compraDiretaId).stream()
                .map(PropostaOutputConverter::converter)
                .collect(Collectors.toList());
    }
}
