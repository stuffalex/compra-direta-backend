package br.com.desafio.compradireta.domain.usecase.fornecedor.listar;

import java.util.List;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.interfaces.dataprovider.IFornecedorDataProvider;
import br.com.desafio.compradireta.domain.usecase.fornecedor.listar.converter.FornecedorOutputConverter;
import lombok.Builder;

@Builder
public class ListarFornecedoresUseCase {

    private final IFornecedorDataProvider fornecedorDataProvider;

    public List<FornecedorOutput> executar() {
        return fornecedorDataProvider.listarTodos().stream()
                .map(FornecedorOutputConverter::converter)
                .collect(Collectors.toList());
    }
}
