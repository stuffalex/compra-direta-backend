package br.com.desafio.compradireta.application.factory.strategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.desafio.compradireta.domain.usecase.compradireta.apurar.strategy.InexigibilidadeStrategy;
import br.com.desafio.compradireta.domain.usecase.compradireta.apurar.strategy.MaiorDescontoStrategy;
import br.com.desafio.compradireta.domain.usecase.compradireta.apurar.strategy.MenorPrecoStrategy;

@Configuration
public class ApuracaoStrategyFactory {

    @Bean
    public MenorPrecoStrategy menorPrecoStrategy() {
        return new MenorPrecoStrategy();
    }

    @Bean
    public MaiorDescontoStrategy maiorDescontoStrategy() {
        return new MaiorDescontoStrategy();
    }

    @Bean
    public InexigibilidadeStrategy inexigibilidadeStrategy() {
        return new InexigibilidadeStrategy();
    }
}
