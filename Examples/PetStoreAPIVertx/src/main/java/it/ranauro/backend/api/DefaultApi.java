package it.ranauro.backend.api;

import it.ranauro.backend.model.NewPet;
import it.ranauro.backend.model.Pet;

import it.ranauro.backend.ApiResponse;

import io.vertx.core.Future;

import java.util.List;

public interface DefaultApi  {
    Future<ApiResponse<Void>> addPet(Pet newPet);
    Future<ApiResponse<Void>> deletePet(Long id);
    Future<ApiResponse<Pet>> findPetById(Long id);
    Future<ApiResponse<List<Pet>>> findPets(List<String> tags, Integer limit);
}
