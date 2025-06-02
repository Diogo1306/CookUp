package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.ExploreRepository;

import java.util.ArrayList;
import java.util.List;

public class ExploreViewModel extends ViewModel {

    private final ExploreRepository repository = new ExploreRepository();

    // Lista acumulada de todas as receitas já carregadas
    private final MutableLiveData<List<RecipeData>> allRecipes = new MutableLiveData<>(new ArrayList<>());

    // Sinaliza se está em requisição de carregamento de página
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    // Sinaliza se já atingimos a última página
    private final MutableLiveData<Boolean> isLastPage = new MutableLiveData<>(false);

    // Lista de categorias (semelhante ao que já existia)
    private final MutableLiveData<ApiResponse<List<CategoryData>>> categories = new MutableLiveData<>();

    // Página atual (1-based). Começa em 1.
    private int currentPage = 1;

    // Número de itens que cada página deve retornar (de acordo com o servidor)
    public static final int PAGE_SIZE = 6;

    public LiveData<List<RecipeData>> getAllRecipes() {
        return allRecipes;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsLastPage() {
        return isLastPage;
    }

    public LiveData<ApiResponse<List<CategoryData>>> getPopularCategories() {
        return categories;
    }

    // Carrega categorias
    public void loadCategories() {
        repository.getPopularCategories().observeForever(response -> {
            if (response != null && response.getData() != null) {
                categories.setValue(response);
            } else {
                categories.setValue(new ApiResponse<>(false, "Nenhuma categoria", new ArrayList<>()));
            }
        });
    }

    // Carrega a próxima página de receitas (currentPage). Internamente incrementa currentPage se vier PAGE_SIZE itens.
    public void loadNextPage() {
        // Se já está carregando OU já era a última página, retorna sem fazer nada
        if (Boolean.TRUE.equals(isLoading.getValue()) || Boolean.TRUE.equals(isLastPage.getValue())) {
            return;
        }

        // Inicia carregamento
        isLoading.setValue(true);
        int pageToCall = currentPage;

        repository.getPopularRecipes(pageToCall).observeForever(response -> {
            isLoading.setValue(false);

            if (response != null && response.isSuccess() && response.getData() != null) {
                List<RecipeData> novas = response.getData();

                if (!novas.isEmpty()) {
                    // 1) Adiciona os itens recebidos ao final da lista acumulada
                    List<RecipeData> listaAtual = allRecipes.getValue();
                    if (listaAtual == null) {
                        listaAtual = new ArrayList<>();
                    }
                    int oldSize = listaAtual.size();
                    listaAtual.addAll(novas);
                    allRecipes.setValue(listaAtual);

                    // 2) Se veio menos do que PAGE_SIZE, significa que não há mais páginas
                    if (novas.size() < PAGE_SIZE) {
                        isLastPage.setValue(true);
                    } else {
                        // Só incrementa se vier exatamente PAGE_SIZE, pois então existe a próxima página
                        currentPage++;
                    }
                } else {
                    // Servidor devolveu lista vazia → chegamos ao fim
                    isLastPage.setValue(true);
                }
            } else {
                // Em caso de erro ou null, também marcamos como última página (você pode tratar de outra forma se desejar)
                isLastPage.setValue(true);
            }
        });
    }

    // Reinicia a paginação (por exemplo, num pull-to-refresh ou troca de filtro)
    public void resetPagination() {
        currentPage = 1;
        allRecipes.setValue(new ArrayList<>());
        isLastPage.setValue(false);
    }
}
