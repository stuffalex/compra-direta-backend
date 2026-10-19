package br.com.desafio.compradireta.domain.usecase.proposta.listar;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.pagination.Pagina;
import br.com.desafio.compradireta.domain.pagination.Paginacao;
import br.com.desafio.compradireta.domain.usecase.proposta.dto.PropostaOutput;
import br.com.desafio.compradireta.domain.usecase.proposta.dto.PropostaOutputConverter;
import lombok.Builder;

@Builder
public class ListarPropostasPorCompraUseCase {

    private final IPropostaDataProvider propostaDataProvider;

    public Pagina<PropostaOutput> executar(Long compraDiretaId, Paginacao paginacao) {
        return propostaDataProvider.listarPorCompraDiretaPaginado(compraDiretaId, paginacao)
                .map(PropostaOutputConverter::converter);
    }
}
