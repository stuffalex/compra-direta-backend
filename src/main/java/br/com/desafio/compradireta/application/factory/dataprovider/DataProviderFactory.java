package br.com.desafio.compradireta.application.factory.dataprovider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.desafio.compradireta.adapter.gateway.dataprovider.categoriacompra.CategoriaCompraDataProviderImpl;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.categoriacompra.CategoriaCompraJpaRepository;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.compradireta.CompraDiretaDataProviderImpl;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.compradireta.CompraDiretaJpaRepository;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.fornecedor.FornecedorDataProviderImpl;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.fornecedor.FornecedorJpaRepository;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.notificacao.NotificacaoFornecedorDataProviderImpl;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.notificacao.NotificacaoFornecedorJpaRepository;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.proposta.PropostaDataProviderImpl;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.proposta.PropostaJpaRepository;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.unidadecompradora.UnidadeCompradoraDataProviderImpl;
import br.com.desafio.compradireta.adapter.gateway.dataprovider.unidadecompradora.UnidadeCompradoraJpaRepository;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICategoriaCompraDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IFornecedorDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.INotificacaoFornecedorDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IUnidadeCompradoraDataProvider;

@Configuration
public class DataProviderFactory {

    @Bean
    public ICompraDiretaDataProvider compraDiretaDataProvider(CompraDiretaJpaRepository repository) {
        return new CompraDiretaDataProviderImpl(repository);
    }

    @Bean
    public IFornecedorDataProvider fornecedorDataProvider(FornecedorJpaRepository repository) {
        return new FornecedorDataProviderImpl(repository);
    }

    @Bean
    public IPropostaDataProvider propostaDataProvider(PropostaJpaRepository repository) {
        return new PropostaDataProviderImpl(repository);
    }

    @Bean
    public INotificacaoFornecedorDataProvider notificacaoFornecedorDataProvider(
            NotificacaoFornecedorJpaRepository repository) {
        return new NotificacaoFornecedorDataProviderImpl(repository);
    }

    @Bean
    public IUnidadeCompradoraDataProvider unidadeCompradoraDataProvider(UnidadeCompradoraJpaRepository repository) {
        return new UnidadeCompradoraDataProviderImpl(repository);
    }

    @Bean
    public ICategoriaCompraDataProvider categoriaCompraDataProvider(CategoriaCompraJpaRepository repository) {
        return new CategoriaCompraDataProviderImpl(repository);
    }
}
