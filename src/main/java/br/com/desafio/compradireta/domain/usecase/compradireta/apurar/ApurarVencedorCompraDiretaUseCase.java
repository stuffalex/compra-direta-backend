package br.com.desafio.compradireta.domain.usecase.compradireta.apurar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.exception.NegocioException;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.usecase.compradireta.apurar.strategy.IApuracaoVencedorStrategy;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutputConverter;
import br.com.desafio.compradireta.domain.validation.Validator;
import lombok.Builder;

public class ApurarVencedorCompraDiretaUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;
    private final IPropostaDataProvider propostaDataProvider;
    private final Map<CriterioJulgamento, IApuracaoVencedorStrategy> strategies;

    @Builder
    public ApurarVencedorCompraDiretaUseCase(
            ICompraDiretaDataProvider compraDiretaDataProvider,
            IPropostaDataProvider propostaDataProvider,
            List<IApuracaoVencedorStrategy> strategies) {
        this.compraDiretaDataProvider = compraDiretaDataProvider;
        this.propostaDataProvider = propostaDataProvider;
        this.strategies = strategies == null ? Map.of() :
                strategies.stream().collect(Collectors.toMap(IApuracaoVencedorStrategy::getCriterio, Function.identity()));
    }

    public CompraDiretaResumoOutput executar(Long id) {
        CompraDireta compraDireta = compraDiretaDataProvider.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Compra direta não encontrada"));

        Validator.isTrue(compraDireta.getStatus() == StatusCompraDireta.PUBLICADA
                        || compraDireta.getStatus() == StatusCompraDireta.EM_JULGAMENTO,
                "Somente compras diretas publicadas podem ter o vencedor apurado");

        List<Proposta> propostas = propostaDataProvider.listarPorCompraDireta(id);
        Validator.isTrue(!propostas.isEmpty(), "Não é possível apurar o vencedor sem propostas registradas");

        IApuracaoVencedorStrategy strategy = strategies.get(compraDireta.getCriterioJulgamento());
        Validator.notNull(strategy, "Estratégia de apuração não encontrada para o critério: "
                + compraDireta.getCriterioJulgamento());

        Proposta propostaVencedora = strategy.apurar(propostas);

        compraDireta.setStatus(StatusCompraDireta.HOMOLOGADA);
        compraDireta.setFornecedorVencedor(propostaVencedora.getFornecedor());
        compraDireta.setDataHomologacao(LocalDateTime.now());

        CompraDireta compraDiretaSalva = compraDiretaDataProvider.salvar(compraDireta);

        return CompraDiretaResumoOutputConverter.converter(compraDiretaSalva);
    }
}
