package br.com.desafio.compradireta.domain.usecase.proposta.registrar;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IFornecedorDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.usecase.proposta.dto.PropostaOutput;
import br.com.desafio.compradireta.domain.usecase.proposta.dto.PropostaOutputConverter;
import br.com.desafio.compradireta.domain.validation.Validator;
import lombok.Builder;

@Builder
public class RegistrarPropostaUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;
    private final IFornecedorDataProvider fornecedorDataProvider;
    private final IPropostaDataProvider propostaDataProvider;

    public PropostaOutput executar(RegistrarPropostaInput input) {
        CompraDireta compraDireta = compraDiretaDataProvider.buscarPorId(input.getCompraDiretaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Compra direta não encontrada"));

        Validator.isTrue(compraDireta.getStatus() == StatusCompraDireta.PUBLICADA
                        || compraDireta.getStatus() == StatusCompraDireta.EM_JULGAMENTO,
                "Só é possível registrar propostas em compras diretas publicadas");
        Validator.isTrue(!LocalDate.now().isAfter(compraDireta.getDataLimitePropostas()),
                "O prazo para envio de propostas desta compra direta já expirou");

        Fornecedor fornecedor = fornecedorDataProvider.buscarPorId(input.getFornecedorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor não encontrado"));

        boolean convidado = compraDireta.getFornecedoresConvidados().stream()
                .anyMatch(convite -> convite.getFornecedor().getId().equals(fornecedor.getId()));
        Validator.isTrue(convidado, "O fornecedor não foi convidado para esta compra direta");

        boolean jaEnviouProposta = propostaDataProvider
                .buscarPorCompraDiretaEFornecedor(compraDireta.getId(), fornecedor.getId())
                .isPresent();
        Validator.isTrue(!jaEnviouProposta, "O fornecedor já enviou uma proposta para esta compra direta");

        Validator.notNull(input.getPrazoEntregaDias(), "O prazo de entrega é obrigatório");
        Validator.isTrue(input.getPrazoEntregaDias() > 0, "O prazo de entrega deve ser maior que zero");
        validarValorConformeCriterio(compraDireta.getCriterioJulgamento(), input);

        Proposta proposta = Proposta.builder()
                .compraDireta(compraDireta)
                .fornecedor(fornecedor)
                .valorProposto(input.getValorProposto())
                .percentualDesconto(input.getPercentualDesconto())
                .prazoEntregaDias(input.getPrazoEntregaDias())
                .dataEnvio(LocalDateTime.now())
                .build();

        Proposta propostaSalva = propostaDataProvider.salvar(proposta);

        return PropostaOutputConverter.converter(propostaSalva);
    }

    private void validarValorConformeCriterio(CriterioJulgamento criterioJulgamento, RegistrarPropostaInput input) {
        if (criterioJulgamento == CriterioJulgamento.MENOR_PRECO) {
            Validator.notNull(input.getValorProposto(), "O valor proposto é obrigatório para o critério MENOR_PRECO");
            Validator.isTrue(input.getValorProposto().signum() > 0, "O valor proposto deve ser maior que zero");
        } else if (criterioJulgamento == CriterioJulgamento.MAIOR_DESCONTO) {
            Validator.notNull(input.getPercentualDesconto(),
                    "O percentual de desconto é obrigatório para o critério MAIOR_DESCONTO");
            Validator.isTrue(input.getPercentualDesconto().signum() > 0,
                    "O percentual de desconto deve ser maior que zero");
        }
    }
}
