package br.com.desafio.compradireta.domain.usecase.compradireta.buscar.converter;

import java.util.List;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDiretaFornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.ItemCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.usecase.compradireta.buscar.BuscarCompraDiretaOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.buscar.BuscarCompraDiretaOutput.FornecedorResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.buscar.BuscarCompraDiretaOutput.ItemOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.buscar.BuscarCompraDiretaOutput.PropostaResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.buscar.BuscarCompraDiretaOutput.ReferenciaOutput;

public final class BuscarCompraDiretaOutputConverter {

    private BuscarCompraDiretaOutputConverter() {
    }

    public static BuscarCompraDiretaOutput converter(CompraDireta compraDireta, List<Proposta> propostas) {
        return BuscarCompraDiretaOutput.builder()
                .id(compraDireta.getId())
                .numero(compraDireta.getNumero())
                .objeto(compraDireta.getObjeto())
                .justificativa(compraDireta.getJustificativa())
                .modalidade(compraDireta.getModalidade())
                .criterioJulgamento(compraDireta.getCriterioJulgamento())
                .unidadeCompradora(ReferenciaOutput.builder()
                        .id(compraDireta.getUnidadeCompradora().getId())
                        .nome(compraDireta.getUnidadeCompradora().getNome())
                        .build())
                .categoriaCompra(ReferenciaOutput.builder()
                        .id(compraDireta.getCategoriaCompra().getId())
                        .nome(compraDireta.getCategoriaCompra().getNome())
                        .build())
                .status(compraDireta.getStatus())
                .dataAbertura(compraDireta.getDataAbertura())
                .dataLimitePropostas(compraDireta.getDataLimitePropostas())
                .valorEstimadoTotal(compraDireta.getValorEstimadoTotal())
                .itens(converterItens(compraDireta.getItens()))
                .fornecedoresConvidados(converterFornecedoresConvidados(compraDireta.getFornecedoresConvidados()))
                .propostas(converterPropostas(propostas))
                .fornecedorVencedor(converterFornecedor(compraDireta.getFornecedorVencedor()))
                .dataHomologacao(compraDireta.getDataHomologacao())
                .motivoCancelamento(compraDireta.getMotivoCancelamento())
                .build();
    }

    private static List<ItemOutput> converterItens(List<ItemCompraDireta> itens) {
        return itens.stream()
                .map(item -> ItemOutput.builder()
                        .id(item.getId())
                        .descricao(item.getDescricao())
                        .unidadeMedida(item.getUnidadeMedida())
                        .quantidade(item.getQuantidade())
                        .valorUnitarioEstimado(item.getValorUnitarioEstimado())
                        .valorTotalEstimado(item.getValorTotalEstimado())
                        .build())
                .collect(Collectors.toList());
    }

    private static List<FornecedorResumoOutput> converterFornecedoresConvidados(List<CompraDiretaFornecedor> convites) {
        return convites.stream()
                .map(convite -> converterFornecedor(convite.getFornecedor()))
                .collect(Collectors.toList());
    }

    private static List<PropostaResumoOutput> converterPropostas(List<Proposta> propostas) {
        return propostas.stream()
                .map(proposta -> PropostaResumoOutput.builder()
                        .id(proposta.getId())
                        .fornecedorId(proposta.getFornecedor().getId())
                        .fornecedorNome(proposta.getFornecedor().getRazaoSocial())
                        .valorProposto(proposta.getValorProposto())
                        .percentualDesconto(proposta.getPercentualDesconto())
                        .prazoEntregaDias(proposta.getPrazoEntregaDias())
                        .dataEnvio(proposta.getDataEnvio())
                        .build())
                .collect(Collectors.toList());
    }

    private static FornecedorResumoOutput converterFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null) {
            return null;
        }
        return FornecedorResumoOutput.builder()
                .id(fornecedor.getId())
                .razaoSocial(fornecedor.getRazaoSocial())
                .email(fornecedor.getEmail())
                .tipoFornecedor(fornecedor.getTipoFornecedor())
                .build();
    }
}
