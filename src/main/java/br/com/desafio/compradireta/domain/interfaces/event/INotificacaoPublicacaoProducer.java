package br.com.desafio.compradireta.domain.interfaces.event;

import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event.CompraPublicadaEvent;

public interface INotificacaoPublicacaoProducer {

    void publicar(CompraPublicadaEvent evento);
}
