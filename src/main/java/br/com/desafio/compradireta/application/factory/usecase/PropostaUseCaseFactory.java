package br.com.desafio.compradireta.application.factory.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IFornecedorDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.usecase.proposta.listar.ListarPropostasPorCompraUseCase;
import br.com.desafio.compradireta.domain.usecase.proposta.registrar.RegistrarPropostaUseCase;

@Configuration
public class PropostaUseCaseFactory {

    @Bean
    public RegistrarPropostaUseCase registrarPropostaUseCase(ICompraDiretaDataProvider compraDiretaDataProvider,
            IFornecedorDataProvider fornecedorDataProvider,
            IPropostaDataProvider propostaDataProvider) {
        return RegistrarPropostaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .fornecedorDataProvider(fornecedorDataProvider)
                .propostaDataProvider(propostaDataProvider)
                .build();
    }

    @Bean
    public ListarPropostasPorCompraUseCase listarPropostasPorCompraUseCase(IPropostaDataProvider propostaDataProvider) {
        return ListarPropostasPorCompraUseCase.builder()
                .propostaDataProvider(propostaDataProvider)
                .build();
    }
}
