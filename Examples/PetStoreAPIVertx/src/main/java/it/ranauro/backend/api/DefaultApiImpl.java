package it.ranauro.backend.api;

import it.ranauro.backend.model.NewPet;
import it.ranauro.backend.model.Pet;

import it.ranauro.backend.ApiResponse;

import io.vertx.core.Future;
import io.vertx.ext.web.handler.impl.HttpStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Implement this class

public class DefaultApiImpl implements DefaultApi {
    Map<Integer, Pet> pets = new HashMap<>();


    public Future<ApiResponse<Void>> addPet(Pet newPet) {
        pets.put(newPet.hashCode(), newPet);
        System.out.println("Pet ID: " + newPet.hashCode());

        ApiResponse<Void> response = new ApiResponse<>();
        return Future.succeededFuture(response);
    }

    public Future<ApiResponse<Void>> deletePet(Long id) {
        if (pets.containsKey(id)){
            pets.remove(id);

            ApiResponse<Void> response = new ApiResponse<>();
            return Future.succeededFuture(response);
        }else{
            return Future.failedFuture(new HttpStatusException(404));
        }
    }

    public Future<ApiResponse<Pet>> findPetById(Long id) {
        System.out.println("provided id: " + id);

        System.out.println("keys: " + pets.keySet());
        if (pets.containsKey(Integer.parseInt(String.valueOf(id)))) {
            ApiResponse<Pet> response = new ApiResponse<Pet>(pets.get(Integer.parseInt(String.valueOf(id))));

            return Future.succeededFuture(response);
        }

        return Future.failedFuture(new HttpStatusException(404));

    }

    @Override
    public Future<ApiResponse<List<Pet>>> findPets(List<String> tags, Integer limit) {
        return Future.failedFuture(new HttpStatusException(581));
    }


}
