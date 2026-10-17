package br.com.desafio.compradireta.domain.usecase.compradireta.criar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CategoriaCompra;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDiretaFornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.UnidadeCompradora;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICategoriaCompraDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IFornecedorDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IUnidadeCompradoraDataProvider;
import br.com.desafio.compradireta.domain.usecase.compradireta.criar.converter.ItemCompraDiretaConverter;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutputConverter;
import br.com.desafio.compradireta.domain.usecase.extended.compradireta.validarregras.IValidarRegrasCompraDiretaExtendedUseCase;
import br.com.desafio.compradireta.domain.validation.Validator;
import lombok.Builder;

@Builder
public class CriarCompraDiretaUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;
    private final IUnidadeCompradoraDataProvider unidadeCompradoraDataProvider;
    private final ICategoriaCompraDataProvider categoriaCompraDataProvider;
    private final IFornecedorDataProvider fornecedorDataProvider;
    private final IValidarRegrasCompraDiretaExtendedUseCase validarRegrasCompraDiretaExtendedUseCase;

    public CompraDiretaResumoOutput executar(CriarCompraDiretaInput input) {
        Validator.notBlank(input.getObjeto(), "O objeto da compra direta é obrigatório");
        Validator.notBlank(input.getJustificativa(), "A justificativa da compra direta é obrigatória");
        Validator.notNull(input.getModalidade(), "A modalidade é obrigatória");
        Validator.notNull(input.getCriterioJulgamento(), "O critério de julgamento é obrigatório");
        Validator.isTrue(input.getItens() != null && !input.getItens().isEmpty(),
                "A compra direta deve possuir ao menos um item");
        Validator.notNull(input.getDataAbertura(), "A data de abertura é obrigatória");
        Validator.notNull(input.getDataLimitePropostas(), "A data limite para propostas é obrigatória");
        Validator.isTrue(input.getDataLimitePropostas().isAfter(input.getDataAbertura()),
                "A data limite para propostas deve ser posterior à data de abertura");

        UnidadeCompradora unidadeCompradora = unidadeCompradoraDataProvider.buscarPorId(input.getUnidadeCompradoraId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade compradora não encontrada"));

        CategoriaCompra categoriaCompra = categoriaCompraDataProvider.buscarPorId(input.getCategoriaCompraId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria de compra não encontrada"));

        List<Long> idsConvidados = input.getFornecedoresConvidadosIds() == null
                ? List.of()
                : input.getFornecedoresConvidadosIds();
        List<Fornecedor> fornecedoresConvidados = fornecedorDataProvider.listarPorIds(idsConvidados);
        Validator.isTrue(fornecedoresConvidados.size() == idsConvidados.size(),
                "Um ou mais fornecedores convidados não foram encontrados");

        validarRegrasCompraDiretaExtendedUseCase.executar(input.getModalidade(), input.getCriterioJulgamento(),
                fornecedoresConvidados.size());

        CompraDireta compraDireta = CompraDireta.builder()
                .numero(gerarNumero(input))
                .objeto(input.getObjeto())
                .justificativa(input.getJustificativa())
                .modalidade(input.getModalidade())
                .criterioJulgamento(input.getCriterioJulgamento())
                .unidadeCompradora(unidadeCompradora)
                .categoriaCompra(categoriaCompra)
                .status(StatusCompraDireta.RASCUNHO)
                .dataAbertura(input.getDataAbertura())
                .dataLimitePropostas(input.getDataLimitePropostas())
                .build();

        compraDireta.setItens(ItemCompraDiretaConverter.converter(input.getItens(), compraDireta));
        compraDireta.setFornecedoresConvidados(montarConvites(fornecedoresConvidados, compraDireta));

        CompraDireta compraDiretaSalva = compraDiretaDataProvider.salvar(compraDireta);

        return CompraDiretaResumoOutputConverter.converter(compraDiretaSalva);
    }

    private String gerarNumero(CriarCompraDiretaInput input) {
        int ano = input.getDataAbertura().getYear();
        long proximoSequencial = compraDiretaDataProvider.contarPorAno(ano) + 1;
        return String.format("CD-%04d/%d", proximoSequencial, ano);
    }

    private List<CompraDiretaFornecedor> montarConvites(List<Fornecedor> fornecedores, CompraDireta compraDireta) {
        return fornecedores.stream()
                .map(fornecedor -> CompraDiretaFornecedor.builder()
                        .compraDireta(compraDireta)
                        .fornecedor(fornecedor)
                        .dataConvite(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
    }
}
