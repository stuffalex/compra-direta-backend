package br.com.desafio.compradireta.domain.usecase.compradireta.publicar;

import java.util.List;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.event.INotificacaoPublicacaoProducer;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutputConverter;
import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event.CompraPublicadaEvent;
import br.com.desafio.compradireta.domain.validation.Validator;
import lombok.Builder;

@Builder
public class PublicarCompraDiretaUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;
    private final INotificacaoPublicacaoProducer notificacaoPublicacaoProducer;

    public CompraDiretaResumoOutput executar(Long id) {
        CompraDireta compraDireta = compraDiretaDataProvider.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Compra direta não encontrada"));

        Validator.isTrue(compraDireta.getStatus() == StatusCompraDireta.RASCUNHO,
                "Somente compras diretas em RASCUNHO podem ser publicadas");

        compraDireta.setStatus(StatusCompraDireta.PUBLICADA);
        CompraDireta compraDiretaSalva = compraDiretaDataProvider.salvar(compraDireta);

        dispararEventoNotificacao(compraDiretaSalva);

        return CompraDiretaResumoOutputConverter.converter(compraDiretaSalva);
    }

    private void dispararEventoNotificacao(CompraDireta compraDireta) {
        if (notificacaoPublicacaoProducer == null) {
            return;
        }

        List<Long> fornecedoresIds = compraDireta.getFornecedoresConvidados() == null
                ? List.of()
                : compraDireta.getFornecedoresConvidados().stream()
                        .map(convite -> convite.getFornecedor().getId())
                        .collect(Collectors.toList());

        CompraPublicadaEvent evento = CompraPublicadaEvent.builder()
                .compraDiretaId(compraDireta.getId())
                .numero(compraDireta.getNumero())
                .fornecedoresConvidadosIds(fornecedoresIds)
                .build();

        notificacaoPublicacaoProducer.publicar(evento);
    }
}
