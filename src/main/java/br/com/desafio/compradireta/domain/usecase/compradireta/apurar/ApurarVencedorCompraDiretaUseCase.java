package br.com.desafio.compradireta.domain.usecase.compradireta.apurar;

import java.util.List;

import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;
import br.com.desafio.compradireta.domain.validation.Validator;
import lombok.Builder;

@Builder
public class ApurarVencedorCompraDiretaUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;
    private final IPropostaDataProvider propostaDataProvider;

    public CompraDiretaResumoOutput executar(Long id) {
        CompraDireta compraDireta = compraDiretaDataProvider.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Compra direta não encontrada"));

        Validator.isTrue(compraDireta.getStatus() == StatusCompraDireta.PUBLICADA
                        || compraDireta.getStatus() == StatusCompraDireta.EM_JULGAMENTO,
                "Somente compras diretas publicadas podem ter o vencedor apurado");

        List<Proposta> propostas = propostaDataProvider.listarPorCompraDireta(id);
        Validator.isTrue(!propostas.isEmpty(), "Não é possível apurar o vencedor sem propostas registradas");

        // TODO (DESAFIO - Strategy): a apuração do vencedor depende do criterioJulgamento da compra
        // (MENOR_PRECO, MAIOR_DESCONTO ou NAO_SE_APLICA) e cada um compara as propostas de um jeito diferente.
        // Aplique o padrão Strategy para resolver qual algoritmo usar e definir o fornecedor vencedor.
        // Depois de apurado: compraDireta.setStatus(StatusCompraDireta.HOMOLOGADA),
        // compraDireta.setFornecedorVencedor(...), compraDireta.setDataHomologacao(...) e salvar.
        // Veja o DESAFIO.md para os requisitos completos.
        throw new UnsupportedOperationException("Apuração de vencedor ainda não implementada");
    }
}
