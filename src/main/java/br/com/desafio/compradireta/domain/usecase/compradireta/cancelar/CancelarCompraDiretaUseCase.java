package br.com.desafio.compradireta.domain.usecase.compradireta.cancelar;

import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutputConverter;
import br.com.desafio.compradireta.domain.validation.Validator;
import lombok.Builder;

@Builder
public class CancelarCompraDiretaUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;

    public CompraDiretaResumoOutput executar(CancelarCompraDiretaInput input) {
        CompraDireta compraDireta = compraDiretaDataProvider.buscarPorId(input.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Compra direta não encontrada"));

        Validator.isTrue(compraDireta.getStatus() != StatusCompraDireta.HOMOLOGADA
                        && compraDireta.getStatus() != StatusCompraDireta.CANCELADA,
                "Compras diretas homologadas ou já canceladas não podem ser canceladas");
        Validator.notBlank(input.getMotivo(), "O motivo do cancelamento é obrigatório");

        compraDireta.setStatus(StatusCompraDireta.CANCELADA);
        compraDireta.setMotivoCancelamento(input.getMotivo());

        CompraDireta compraDiretaSalva = compraDiretaDataProvider.salvar(compraDireta);

        return CompraDiretaResumoOutputConverter.converter(compraDiretaSalva);
    }
}
