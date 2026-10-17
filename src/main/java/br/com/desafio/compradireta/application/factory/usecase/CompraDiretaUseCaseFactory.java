package br.com.desafio.compradireta.application.factory.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICategoriaCompraDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IFornecedorDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IUnidadeCompradoraDataProvider;
import br.com.desafio.compradireta.domain.usecase.compradireta.apurar.ApurarVencedorCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.atualizar.AtualizarCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.buscar.BuscarCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.cancelar.CancelarCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.criar.CriarCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.listar.ListarComprasDiretasUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.PublicarCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.extended.compradireta.validarregras.IValidarRegrasCompraDiretaExtendedUseCase;

@Configuration
public class CompraDiretaUseCaseFactory {

    @Bean
    public CriarCompraDiretaUseCase criarCompraDiretaUseCase(ICompraDiretaDataProvider compraDiretaDataProvider,
            IUnidadeCompradoraDataProvider unidadeCompradoraDataProvider,
            ICategoriaCompraDataProvider categoriaCompraDataProvider,
            IFornecedorDataProvider fornecedorDataProvider,
            IValidarRegrasCompraDiretaExtendedUseCase validarRegrasCompraDiretaExtendedUseCase) {
        return CriarCompraDiretaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .unidadeCompradoraDataProvider(unidadeCompradoraDataProvider)
                .categoriaCompraDataProvider(categoriaCompraDataProvider)
                .fornecedorDataProvider(fornecedorDataProvider)
                .validarRegrasCompraDiretaExtendedUseCase(validarRegrasCompraDiretaExtendedUseCase)
                .build();
    }

    @Bean
    public ListarComprasDiretasUseCase listarComprasDiretasUseCase(ICompraDiretaDataProvider compraDiretaDataProvider) {
        return ListarComprasDiretasUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .build();
    }

    @Bean
    public BuscarCompraDiretaUseCase buscarCompraDiretaUseCase(ICompraDiretaDataProvider compraDiretaDataProvider,
            IPropostaDataProvider propostaDataProvider) {
        return BuscarCompraDiretaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .propostaDataProvider(propostaDataProvider)
                .build();
    }

    @Bean
    public AtualizarCompraDiretaUseCase atualizarCompraDiretaUseCase(ICompraDiretaDataProvider compraDiretaDataProvider,
            IUnidadeCompradoraDataProvider unidadeCompradoraDataProvider,
            ICategoriaCompraDataProvider categoriaCompraDataProvider,
            IFornecedorDataProvider fornecedorDataProvider,
            IValidarRegrasCompraDiretaExtendedUseCase validarRegrasCompraDiretaExtendedUseCase) {
        return AtualizarCompraDiretaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .unidadeCompradoraDataProvider(unidadeCompradoraDataProvider)
                .categoriaCompraDataProvider(categoriaCompraDataProvider)
                .fornecedorDataProvider(fornecedorDataProvider)
                .validarRegrasCompraDiretaExtendedUseCase(validarRegrasCompraDiretaExtendedUseCase)
                .build();
    }

    @Bean
    public PublicarCompraDiretaUseCase publicarCompraDiretaUseCase(ICompraDiretaDataProvider compraDiretaDataProvider) {
        return PublicarCompraDiretaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .build();
    }

    @Bean
    public CancelarCompraDiretaUseCase cancelarCompraDiretaUseCase(ICompraDiretaDataProvider compraDiretaDataProvider) {
        return CancelarCompraDiretaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .build();
    }

    @Bean
    public ApurarVencedorCompraDiretaUseCase apurarVencedorCompraDiretaUseCase(
            ICompraDiretaDataProvider compraDiretaDataProvider,
            IPropostaDataProvider propostaDataProvider) {
        return ApurarVencedorCompraDiretaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .propostaDataProvider(propostaDataProvider)
                .build();
    }
}
