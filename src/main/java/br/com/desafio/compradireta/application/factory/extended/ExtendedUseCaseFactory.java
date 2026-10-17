package br.com.desafio.compradireta.application.factory.extended;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.desafio.compradireta.domain.usecase.extended.compradireta.validarregras.IValidarRegrasCompraDiretaExtendedUseCase;
import br.com.desafio.compradireta.domain.usecase.extended.compradireta.validarregras.ValidarRegrasCompraDiretaExtendedUseCaseImpl;

@Configuration
public class ExtendedUseCaseFactory {

    @Bean
    public IValidarRegrasCompraDiretaExtendedUseCase validarRegrasCompraDiretaExtendedUseCase() {
        return ValidarRegrasCompraDiretaExtendedUseCaseImpl.builder().build();
    }
}
