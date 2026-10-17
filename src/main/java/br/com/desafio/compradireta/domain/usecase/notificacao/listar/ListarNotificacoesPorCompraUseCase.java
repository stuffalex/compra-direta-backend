package br.com.desafio.compradireta.domain.usecase.notificacao.listar;

import java.util.List;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.INotificacaoFornecedorDataProvider;
import br.com.desafio.compradireta.domain.usecase.notificacao.listar.converter.NotificacaoFornecedorOutputConverter;
import lombok.Builder;

@Builder
public class ListarNotificacoesPorCompraUseCase {

    private final INotificacaoFornecedorDataProvider notificacaoFornecedorDataProvider;

    public List<NotificacaoFornecedorOutput> executar(Long compraDiretaId) {
        return notificacaoFornecedorDataProvider.listarPorCompraDireta(compraDiretaId).stream()
                .map(NotificacaoFornecedorOutputConverter::converter)
                .collect(Collectors.toList());
    }
}
