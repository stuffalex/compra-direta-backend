package br.com.desafio.compradireta.domain.usecase.notificacao.publicar;

import java.time.LocalDateTime;

import br.com.desafio.compradireta.domain.constant.StatusNotificacao;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDiretaFornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.NotificacaoFornecedor;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.INotificacaoFornecedorDataProvider;
import lombok.Builder;

@Builder
public class NotificarFornecedoresPublicacaoUseCase {

    private final ICompraDiretaDataProvider compraDiretaDataProvider;
    private final INotificacaoFornecedorDataProvider notificacaoFornecedorDataProvider;

    public void executar(Long compraDiretaId) {
        CompraDireta compraDireta = compraDiretaDataProvider.buscarPorId(compraDiretaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Compra direta não encontrada: " + compraDiretaId));

        if (compraDireta.getFornecedoresConvidados() == null || compraDireta.getFornecedoresConvidados().isEmpty()) {
            return;
        }

        for (CompraDiretaFornecedor convite : compraDireta.getFornecedoresConvidados()) {
            Fornecedor fornecedor = convite.getFornecedor();
            if (fornecedor == null || fornecedor.getId() == null) {
                continue;
            }

            if (notificacaoFornecedorDataProvider.existePorCompraDiretaEFornecedor(compraDireta.getId(), fornecedor.getId())) {
                continue;
            }

            NotificacaoFornecedor notificacao = NotificacaoFornecedor.builder()
                    .compraDireta(compraDireta)
                    .fornecedor(fornecedor)
                    .dataEnvio(LocalDateTime.now())
                    .status(StatusNotificacao.ENVIADA)
                    .build();

            notificacaoFornecedorDataProvider.salvar(notificacao);
        }
    }
}
