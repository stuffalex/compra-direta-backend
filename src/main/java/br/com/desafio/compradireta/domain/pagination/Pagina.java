package br.com.desafio.compradireta.domain.pagination;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagina<T> {

    @Builder.Default
    private List<T> content = Collections.emptyList();
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;

    public static <T> Pagina<T> of(List<T> content, long totalElements, int pageNumber, int pageSize) {
        int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
        return Pagina.<T>builder()
                .content(content == null ? Collections.emptyList() : content)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
    }

    public <R> Pagina<R> map(Function<? super T, ? extends R> converter) {
        List<R> mappedContent = content.stream().map(converter).collect(Collectors.toList());
        return Pagina.<R>builder()
                .content(mappedContent)
                .totalElements(this.totalElements)
                .totalPages(this.totalPages)
                .pageNumber(this.pageNumber)
                .pageSize(this.pageSize)
                .build();
    }
}
