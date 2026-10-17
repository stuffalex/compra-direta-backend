package br.com.desafio.compradireta.application.factory.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICategoriaCompraDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IFornecedorDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.INotificacaoFornecedorDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IUnidadeCompradoraDataProvider;
import br.com.desafio.compradireta.domain.usecase.categoriacompra.listar.ListarCategoriasCompraUseCase;
import br.com.desafio.compradireta.domain.usecase.fornecedor.listar.ListarFornecedoresUseCase;
import br.com.desafio.compradireta.domain.usecase.notificacao.listar.ListarNotificacoesPorCompraUseCase;
import br.com.desafio.compradireta.domain.usecase.unidadecompradora.listar.ListarUnidadesCompradorasUseCase;

@Configuration
public class DominioUseCaseFactory {

    @Bean
    public ListarFornecedoresUseCase listarFornecedoresUseCase(IFornecedorDataProvider fornecedorDataProvider) {
        return ListarFornecedoresUseCase.builder()
                .fornecedorDataProvider(fornecedorDataProvider)
                .build();
    }

    @Bean
    public ListarUnidadesCompradorasUseCase listarUnidadesCompradorasUseCase(
            IUnidadeCompradoraDataProvider unidadeCompradoraDataProvider) {
        return ListarUnidadesCompradorasUseCase.builder()
                .unidadeCompradoraDataProvider(unidadeCompradoraDataProvider)
                .build();
    }

    @Bean
    public ListarCategoriasCompraUseCase listarCategoriasCompraUseCase(
            ICategoriaCompraDataProvider categoriaCompraDataProvider) {
        return ListarCategoriasCompraUseCase.builder()
                .categoriaCompraDataProvider(categoriaCompraDataProvider)
                .build();
    }

    @Bean
    public ListarNotificacoesPorCompraUseCase listarNotificacoesPorCompraUseCase(
            INotificacaoFornecedorDataProvider notificacaoFornecedorDataProvider) {
        return ListarNotificacoesPorCompraUseCase.builder()
                .notificacaoFornecedorDataProvider(notificacaoFornecedorDataProvider)
                .build();
    }
}
