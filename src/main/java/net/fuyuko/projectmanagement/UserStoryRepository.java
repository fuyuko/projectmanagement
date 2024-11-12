package net.fuyuko.projectmanagement;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoryRepository extends JpaRepository<UserStory, Integer> {

    /*

        CRUD Operations:
        
        save(S entity): Saves a given entity.
        saveAll(Iterable<S> entities): Saves all given entities.
        findById(ID id): Retrieves an entity by its ID.
        existsById(ID id): Checks whether an entity with the given ID exists.
        findAll(): Retrieves all entities.
        findAllById(Iterable<ID> ids): Retrieves all entities by their IDs.
        count(): Returns the number of entities.
        deleteById(ID id): Deletes the entity with the given ID.
        delete(T entity): Deletes a given entity.
        deleteAll(Iterable<? extends T> entities): Deletes all given entities.
        deleteAll(): Deletes all entities.

        Pagination and Sorting:

        findAll(Sort sort): Retrieves all entities sorted by the given options.
        findAll(Pageable pageable): Retrieves a Page of entities meeting the paging restriction provided in the Pageable object.

     */



}
