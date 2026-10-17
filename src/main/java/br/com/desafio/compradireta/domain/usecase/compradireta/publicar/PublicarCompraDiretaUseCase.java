package br.com.desafio.compradireta.domain.usecase.compradireta.publicar;

import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutputConverter;
import br.com.desafio.compradireta.domain.validation.Validator;
import lombok.Builder;

@Builder
public class PublicarCompraDiretaUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;

    public CompraDiretaResumoOutput executar(Long id) {
        CompraDireta compraDireta = compraDiretaDataProvider.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Compra direta não encontrada"));

        Validator.isTrue(compraDireta.getStatus() == StatusCompraDireta.RASCUNHO,
                "Somente compras diretas em RASCUNHO podem ser publicadas");

        compraDireta.setStatus(StatusCompraDireta.PUBLICADA);
        CompraDireta compraDiretaSalva = compraDiretaDataProvider.salvar(compraDireta);

        // TODO (DESAFIO - RabbitMQ): notificar os fornecedores convidados (compraDiretaSalva.getFornecedoresConvidados())
        // sobre a publicação desta compra direta, usando RabbitMQ. Veja o DESAFIO.md para os requisitos.

        return CompraDiretaResumoOutputConverter.converter(compraDiretaSalva);
    }
}
